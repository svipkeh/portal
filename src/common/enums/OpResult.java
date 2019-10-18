package common.enums;

public enum OpResult {
	UNKNOWN("未知"),  SUCCEED("成功"),  FAILED("失败");
	  
	  private String note;
	  
	  private OpResult(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
