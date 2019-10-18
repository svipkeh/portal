package common.enums;

public enum LegalSourceScopeType {
	UNKNOWN("未知"),  FIXED("特定用户"),  ALL("所有用户");
	  
	  private String note;
	  
	  private LegalSourceScopeType(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
