package common.enums;

public enum DataStatus {
	UNKNOWN("未知"),  ENABLED("启用"),  DISABLED("禁用"),  DELETED("已删除");
	  
	  private String note;
	  
	  private DataStatus(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
	
}
