package common.enums;

public enum ResType {
	UNKNOWN("未知"),  HOST("主机"),  DB("数据库"),  NETDEVICE("网络设备"),  WEBAPP("应用程序");
	  
	  private String note;
	  
	  private ResType(String note)
	  {
	    this.note = note;
	  }
	  
	  public String note()
	  {
	    return this.note;
	  }
}
