package common.enums;

public enum FortNetMode {
	WAN("公网"),  LAN("内网");
	  
	  private String note;
	  
	  private FortNetMode(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }

}
