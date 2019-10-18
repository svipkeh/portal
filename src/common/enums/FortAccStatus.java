package common.enums;

public enum FortAccStatus {
	NOTUSE("未使用"),  ASSIGNED("已分配"),  CONNECTED("使用中"),  DISCONNECTED("已断开");
	  
	  private String note;
	  
	  private FortAccStatus(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }

}
