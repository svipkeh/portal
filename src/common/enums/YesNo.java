package common.enums;

public enum YesNo {
	UNKNOWN("未知"),  YES("是"),  NO("否");
	  
	  private String note;
	  
	  private YesNo(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
