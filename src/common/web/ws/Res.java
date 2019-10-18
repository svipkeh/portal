package common.web.ws;

import common.enums.OpResult;

public class Res
{
  private String code;
  private String note;
  private Object value;
  
  public Res()
  {
    this.code = OpResult.SUCCEED.name();
    this.note = OpResult.SUCCEED.note();
  }
  
  public Object getValue()
  {
    return this.value;
  }
  
  public void setValue(Object value)
  {
    this.value = value;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getNote()
  {
    return this.note;
  }
  
  public void setNote(String note)
  {
    this.note = note;
  }
}
