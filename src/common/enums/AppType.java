package common.enums;

public enum AppType
{
  UNKNOWN("未知"),  CMDLINE("命令行"),  AUTOKEY("自动代填");
  
  private String note;
  
  private AppType(String note)
  {
    this.note = note;
  }
  
  public String note()
  {
    return this.note;
  }
}
