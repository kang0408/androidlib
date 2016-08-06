package com.langk.base.http.request;

import java.io.File;
import java.io.Serializable;

/**
 * 附件
 * @author K
 *
 */
public class BaseAttachment
  implements Serializable
{
  private static final long serialVersionUID = -8297692606018678482L;
  private String id;
  private File file;

  public BaseAttachment()
  {
  }

  public BaseAttachment(String id, File file)
  {
    this.id = id;
    this.file = file;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public File getFile() {
    return this.file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
