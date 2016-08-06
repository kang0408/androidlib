package com.langk.base.db.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.langk.base.db.BasePO;
import com.langk.base.db.DBManager;
import com.langk.base.db.helper.DBHelper;
import com.langk.base.log.Log;
import com.langk.base.util.SharedPreferencesUtil;
import com.langk.base.util.StringUtil;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


@SuppressWarnings("unchecked")
public abstract class BaseDAO<T extends BasePO> {
	private static final String TAG = BaseDAO.class.getSimpleName();
	protected Class<T> entityClass;

	public abstract DBManager getDBManager();

	public abstract String getUserAccount();

	public long getTableCurrentVersion() {
		long tableCurrentVersion = 0L;
		try {
			String strTablesVersionFileName = getDbHelper()
					.getTablesVersionFileName(this);
			Context context = getDBManager().getContext();
			SharedPreferences prefs = context.getSharedPreferences(
					strTablesVersionFileName, 0);
			tableCurrentVersion = prefs.getLong(getClass().getName(), 0L);
		} catch (Exception e) {
			Log.e(TAG, "getTableCurrentVersion error", e);
		}

		return tableCurrentVersion;
	}

	@SuppressWarnings("rawtypes")
	public boolean isColumnsExist(String colName) {
		boolean bExist = true;
		try {
			
			Dao entityDao = getEntityDao();

			QueryBuilder queryBuilder = entityDao.queryBuilder();
			queryBuilder.limit(Long.valueOf(1L));
			queryBuilder.where().isNotNull(colName);
			queryBuilder.query();
		} catch (Exception localException) {
			Log.w(TAG, colName + " not exist");
			bExist = false;
		}

		return bExist;
	}

	public void onTableUpdate(String tablesVersionFileName,
			long tableCurrentVersion, long tableNewVersion) {
		try {
			onTableUpdatePre(tableCurrentVersion, tableNewVersion);
			onTableUpdateIng(tableCurrentVersion, tableNewVersion);
			onTableUpdateAfter(tableCurrentVersion, tableNewVersion);
			Context context = getDBManager().getContext();
			SharedPreferencesUtil shareUtil = SharedPreferencesUtil
					.getInstance(context);
			shareUtil.addOrModifyLong(tablesVersionFileName,
					this.entityClass.getName(), tableNewVersion);
		} catch (Exception e) {
			Log.e(TAG, "onTableUpdate error tableNewVersion = "
					+ tableNewVersion, e);
		}
	}

	protected void onTableUpdateAfter(long tableCurrentVersion,
			long tableNewVersion) throws Exception {
	}

	protected void onTableUpdateIng(long tableCurrentVersion,
			long tableNewVersion) throws Exception {
	}

	protected void onTableUpdatePre(long tableCurrentVersion,
			long tableNewVersion) throws Exception {
	}

	public BaseDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@SuppressWarnings("rawtypes")
	public List<T> queryForAll() throws SQLException {
		Dao entityDao = getEntityDao();
		return entityDao.queryForAll();
	}

	@SuppressWarnings("rawtypes")
	public long countOf() throws SQLException {
		Dao entityDao = getEntityDao();
		return entityDao.countOf();
	}

	@SuppressWarnings("rawtypes")
	public int insert(T entityObj) throws SQLException {
		Dao entityDao = getEntityDao();
		return entityDao.create(entityObj);
	}

	@SuppressWarnings("rawtypes")
	public void insertOrUpdate(T entityObj) throws SQLException {
		Dao entityDao = getEntityDao();
		entityObj.setLocalUpdateTime(System.currentTimeMillis());
		entityDao.createOrUpdate(entityObj);
	}

	@SuppressWarnings("rawtypes")
	public Object insertIfNotExists(T entityObj) throws SQLException {
		Dao entityDao = getEntityDao();
		return entityDao.createIfNotExists(entityObj);
	}

	@SuppressWarnings("rawtypes")
	public int update(T entityObj) throws SQLException {
		Dao entityDao = getEntityDao();

		entityObj.setLocalUpdateTime(System.currentTimeMillis());

		return entityDao.update(entityObj);
	}

	@SuppressWarnings("rawtypes")
	public int delete(T entityObj) throws SQLException {
		Dao entityDao = getEntityDao();
		return entityDao.delete(entityObj);
	}

	@SuppressWarnings("rawtypes")
	public void batchInsert(final List<? extends T> entities) throws Exception {
		if (StringUtil.isEmptyObj(entities)) {
			Log.w(TAG, "batchInsert entities is empty.");
			return;
		}
		final Dao entityDao = getEntityDao();
		entityDao.callBatchTasks(new Callable() {
			public Void call() throws SQLException {
				long lCurrentTime = System.currentTimeMillis();
				for (int i = 0; i < entities.size(); i++) {
					BasePO entity = (BasePO) entities.get(i);
					entity.setLocalCreateTime(lCurrentTime + i);
					entity.setLocalUpdateTime(lCurrentTime + i);
					entityDao.create(entity);
				}

				return null;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void batchUpdate(final List<? extends T> entities) throws Exception {
		if (StringUtil.isEmptyObj(entities)) {
			Log.w(TAG, "batchUpdate entities is empty.");
			return;
		}
		final Dao entityDao = getEntityDao();
		entityDao.callBatchTasks(new Callable() {
			public Void call() throws SQLException {
				long lCurrentTime = System.currentTimeMillis()
						+ entities.size();
				for (int i = 0; i < entities.size(); i++) {
					BasePO entity = (BasePO) entities.get(i);

					long lCurrentItemTime = lCurrentTime - i;
					entity.setLocalUpdateTime(lCurrentItemTime);
					entityDao.update(entity);
				}
				return null;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void batchUpdateWithoutLocalUpdateTime(
			final List<? extends T> entities) throws Exception {
		if (StringUtil.isEmptyObj(entities)) {
			Log.w(TAG, "batchUpdate entities is empty.");
			return;
		}
		final Dao entityDao = getEntityDao();
		entityDao.callBatchTasks(new Callable() {
			public Void call() throws SQLException {
				for (int i = 0; i < entities.size(); i++) {
					BasePO entity = (BasePO) entities.get(i);
					entityDao.update(entity);
				}
				return null;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void batchInsertOrUpdate(final List<? extends T> entities)
			throws Exception {
		if (StringUtil.isEmptyObj(entities)) {
			Log.w(TAG, "batchInsertOrUpdate entities is empty.");
			return;
		}

		final Dao entityDao = getEntityDao();
		entityDao.callBatchTasks(new Callable() {
			public Void call() throws SQLException {
				long lCurrentTime = System.currentTimeMillis()
						+ entities.size();
				for (int i = 0; i < entities.size(); i++) {
					BasePO entity = (BasePO) entities.get(i);

					long lCurrentItemTime = lCurrentTime - i;
					entity.setLocalCreateTime(lCurrentItemTime);
					entity.setLocalUpdateTime(lCurrentItemTime);
					entityDao.createOrUpdate(entity);
				}
				return null;
			}
		});
	}

	public void batchDelete(final List<? extends T> entities) throws Exception {
		if (StringUtil.isEmptyObj(entities)) {
			Log.w(TAG, "batchDelete entities is empty.");
			return;
		}
		final Dao entityDao = getEntityDao();
		entityDao.callBatchTasks(new Callable() {
			public Void call() throws SQLException {
				for (BasePO entity : entities) {
					entityDao.delete(entity);
				}
				return null;
			}
		});
	}

	public Dao getEntityDao() throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao;
	}

	public void clearTableData() {
		getDbHelper().clearTableData(this.entityClass);
	}

	
	public T queryForId(Object id) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		Object result = entityDao.queryForId(id);
		if (result == null) {
			return null;
		}
		return (T) result;
	}

	public Object queryForFirst(PreparedQuery preparedQuery)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);

		Object result = entityDao.queryForFirst(preparedQuery);
		if (result == null) {
			return null;
		}
		return (BasePO) result;
	}

	public List<T> queryForEq(String fieldName, Object value)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForEq(fieldName, value);
	}

	public List<T> queryForMatching(Object matchObj) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForMatching(matchObj);
	}

	public List<T> queryForMatchingArgs(Object matchObj) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForMatchingArgs(matchObj);
	}

	public List<T> queryForFieldValues(Map fieldValues) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForFieldValues(fieldValues);
	}

	public List<T> queryForFieldValuesArgs(Map fieldValues) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForFieldValuesArgs(fieldValues);
	}

	public Object queryForSameId(Object data) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryForSameId(data);
	}

	public List<T> query(PreparedQuery preparedQuery) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.query(preparedQuery);
	}

	public int create(Object data) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.create(data);
	}

	public Object createIfNotExists(Object data) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.createIfNotExists(data);
	}

	public Dao.CreateOrUpdateStatus createOrUpdate(Object data)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.createOrUpdate(data);
	}

	public int updateId(Object data, Object newId) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.updateId(data, newId);
	}

	public int update(PreparedUpdate preparedUpdate) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.update(preparedUpdate);
	}

	public int deleteById(Object id) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.deleteById(id);
	}

	public int delete(Collection datas) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.delete(datas);
	}

	public int deleteIds(Collection ids) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.deleteIds(ids);
	}

	public int delete(PreparedDelete preparedDelete) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.delete(preparedDelete);
	}

	public GenericRawResults queryRaw(String query, String[] arguments)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryRaw(query, arguments);
	}

	public GenericRawResults queryRaw(String query, RawRowMapper mapper,
			String[] arguments) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryRaw(query, mapper, arguments);
	}

	public GenericRawResults queryRaw(String query, DataType[] columnTypes,
			String[] arguments) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryRaw(query, columnTypes, arguments);
	}

	public long queryRawValue(String query, String[] arguments)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.queryRawValue(query, arguments);
	}

	public int executeRaw(String statement, String[] arguments)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.executeRaw(statement, arguments);
	}

	public int executeRawNoArgs(String statement) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.executeRawNoArgs(statement);
	}

	public int updateRaw(String statement, String[] arguments)
			throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.updateRaw(statement, arguments);
	}

	public Object callBatchTasks(Callable callable) throws Exception {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.callBatchTasks(callable);
	}

	public Object extractId(Object data) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.extractId(data);
	}

	public long countOf(PreparedQuery preparedQuery) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.countOf(preparedQuery);
	}

	public boolean idExists(Object id) throws SQLException {
		Dao entityDao = getDbHelper().getDao(this.entityClass);
		return entityDao.idExists(id);
	}

	public abstract DBHelper getDbHelper();

	public Class<T> getEntityClass() {
		return this.entityClass;
	}
}
