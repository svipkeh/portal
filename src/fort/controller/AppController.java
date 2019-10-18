package fort.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.AppType;
import common.enums.DataStatus;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import fort.model.App;
import fort.utils.AppConverseTool;
import fort.utils.FortNetModeTool;
import fort.utils.FortServiceCaller;
import fort.validator.AppValidator;
import java.util.Map;

public class AppController
  extends BaseController
{
  @Info(des="进入运维工具列表页面")
  public void list()
  {
    clearPagerInfo();
    render("appList.jsp");
  }
  
  @Info(des="运维工具列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<App> page = App.dao.getPage(getPageCurrent(), getPageSize(), queryMap);
    for (App data : page.getList())
    {
      data.put("app_type_str", AppType.valueOf(data.getStr("app_type")).note());
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增运维工具页面")
  public void add()
  {
    render("appAdd.jsp");
  }
  
  @Info(des="新增运维工具")
  @Before({Tx.class, AppValidator.class})
  public void addInvoke()
  {
    Res res = new Res();
    App app = (App)getModel(App.class, "app");
    app.set("data_status", DataStatus.ENABLED.name());
    app.save();
    renderJson(res);
  }
  
  @Info(des="进入修改运维工具页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择运维工具");
    }
    App app = (App)App.dao.findById(id);
    if (app == null) {
      throw new TxDataException("未知的运维工具");
    }
    setAttr("app", app);
    render("appEdit.jsp");
  }
  
  @Info(des="修改运维工具")
  @Before({Tx.class, AppValidator.class})
  public void editInvoke() {
    Res res = new Res();
    App app = (App)getModel(App.class, "app");
    Integer id = app.get("id");
    App dbApp = App.dao.findById(id);
    if (dbApp == null) {
      throw new TxDataException("工具未找到");
    }
    app.update();
    renderJson(res);
  }
  
  @Info(des="进入测试运行页面")
  public void run() {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择运维工具");
    }
    App app = (App)App.dao.findById(id);
    if (app == null) {
      throw new TxDataException("未知的运维工具");
    }
    String html = AppConverseTool.getHtml(app.getStr("parameter"));
    app.put("html", html);
    setAttr("app", app);
    render("appRun.jsp");
  }
  
  @Info(des="测试运行")
  @Before({Tx.class, AppValidator.class})
  public void runInvoke()
  {
    Res res = new Res();
    App app = (App)App.dao.findById(getPara("app_id"));
    String cmd = AppConverseTool.getCmdLine(app.getStr("parameter"), getRequest());
    String record = getPara("r");
    String[] backArray = FortServiceCaller.call(FortNetModeTool.getNetModel(getRequest()), cmd, 
      "1".equals(record));
    if (backArray[0].equals("1")) {
      res.setValue(backArray[2]);
    } else {
      throw new TxDataException(backArray[2]);
    }
    renderJson(res);
  }
  
  @Info(des="数据状态改变")
  @Before({Tx.class})
  public void statusInvoke()
  {
    Res res = new Res();
    String status = getPara("status");
    String ids = getPara("ids");
    if (DataStatus.valueOf(status) == null) {
      throw new TxDataException("状态错误");
    }
    if (StringUtil.isBlank(ids)) {
      throw new TxDataException("数据未选择");
    }
    String[] idsArray = ids.split(",");
    for (String id : idsArray)
    {
      App model = (App)App.dao.findById(id);
      if (model != null)
      {
        model.set("data_status", status);
        model.update();
      }
    }
    renderJson(res);
  }
}
