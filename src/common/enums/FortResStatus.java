package common.enums;

public enum FortResStatus {
	UNKNOWN("未知"),  ZHENGCHANG("正常"),  SHILIAN("失联");
	  
	  private String note;
	  
	  private FortResStatus(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }

}
