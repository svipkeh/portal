package common.jfinal.routes;

import common.annotation.Info;

public class ControllerMethodInfo
{
  private String controller;
  private String method;
  private String des;
  private boolean isControl;
  
  public String toString()
  {
    return this.controller + "--" + this.method + "--" + this.des;
  }
  
  public ControllerMethodInfo(String controller, String method, Info info)
  {
    this.controller = controller;
    this.method = method;
    if (info != null)
    {
      this.des = info.des();
      this.isControl = info.isControl();
    }
  }
  
  public String getController()
  {
    return this.controller;
  }
  
  public void setController(String controller)
  {
    this.controller = controller;
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public void setMethod(String method)
  {
    this.method = method;
  }
  
  public String getDes()
  {
    return this.des;
  }
  
  public void setDes(String des)
  {
    this.des = des;
  }
  
  public boolean isControl()
  {
    return this.isControl;
  }
  
  public void setControl(boolean isControl)
  {
    this.isControl = isControl;
  }
}
