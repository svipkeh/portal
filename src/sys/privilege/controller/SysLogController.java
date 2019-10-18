package sys.privilege.controller;

import com.jfinal.plugin.activerecord.Page;
import common.annotation.Info;
import common.jfinal.controller.BaseController;
import common.web.ws.Res;
import java.util.Map;
import sys.privilege.model.SysLog;


public class SysLogController
  extends BaseController
{
  @Info(des="进入操作日志页面")
  public void list()
  {
    clearPagerInfo();
    render("syslogList.jsp");
  }
  
  @Info(des="操作日志列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<SysLog> page = SysLog.dao.getAllSyslog(getPageCurrent(), getPageSize(), queryMap);
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
}
