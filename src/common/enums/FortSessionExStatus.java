package common.enums;

public enum FortSessionExStatus {
	ASSIGNED("已分配"),  RUNNING("执行中"),  OK("正常结束"),  ERROR("异常结束");
	  
	  private String note;
	  
	  private FortSessionExStatus(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
