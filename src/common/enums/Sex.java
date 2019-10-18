package common.enums;

public enum Sex {
	MALE("男"),  FEMALE("女");
	  
	  private String note;
	  
	  private Sex(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
