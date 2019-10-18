package portal.controller;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import portal.model.Resource;
import portal.model.ResourceApp;
import portal.model.ResourceAppAttr;
import portal.validator.ResourceAppValidator;

public class ResourceAppController
  extends BaseController
{
  @Info(des="进入资源工具列表页面")
  public void list()
  {
    String resid = getPara("resid");
    if (StringUtil.isBlank(resid)) {
      throw new TxDataException("未选择资源");
    }
    Resource resource = (Resource)Resource.dao.findById(resid);
    if (resource == null) {
      throw new TxDataException("未知的资源");
    }
    setAttr("resource", resource);
    render("resourceAppList.jsp");
  }
  
  @Info(des="资源工具列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    List<ResourceApp> list = ResourceApp.dao.listAll(getPara("resid").toString());
    for (ResourceApp data : list)
    {
      data.put("app_type_str", AppType.valueOf(data.getStr("app_type")).note());
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    res.setValue(new Page(list, 0, 0, 0, 0));
    renderJson(res);
  }
  
  @Info(des="进入新增资源工具页面")
  public void add()
  {
    String resid = getPara("resid");
    if (StringUtil.isBlank(resid)) {
      throw new TxDataException("未选择资源");
    }
    Resource resource = (Resource)Resource.dao.findById(resid);
    if (resource == null) {
      throw new TxDataException("未知的资源");
    }
    setAttr("resource", resource);
    
    String appid = getPara("appid");
    App app = (App)App.dao.findById(appid);
    if (app != null)
    {
      setAttr("app", app);
      
      String html = AppConverseTool.getHtmlWithRes(app.getStr("parameter"));
      setAttr("input_html", html);
    }
    render("resourceAppAdd.jsp");
  }
  
  @Info(des="新增资源工具")
  @Before({Tx.class, ResourceAppValidator.class})
  public void addInvoke()
  {
    Res res = new Res();
    ResourceApp resapp = (ResourceApp)getModel(ResourceApp.class, "resapp");
    ResourceApp same = ResourceApp.dao.findByResAndApp(resapp.get("resource_id").toString(), resapp.get("app_id").toString());
    if (same != null) {
      throw new TxDataException("validate_app_id", "已添加该工具，请勿重复添加");
    }
    resapp.set("data_status", DataStatus.ENABLED.name());
    resapp.save();
    
    App tool = (App)App.dao.findById(resapp.get("app_id"));
    List<String> needInputParams = AppConverseTool.getNeedInputParams(tool.getStr("parameter"));
    for (String para : needInputParams)
    {
      String paraVal = getPara(para);
      if (!StringUtil.isBlank(paraVal))
      {
        ResourceAppAttr tpara = new ResourceAppAttr();
        tpara.set("resource_app_id", resapp.get("id"));
        tpara.set("attr_name", para);
        tpara.set("attr_val", paraVal);
        tpara.save();
      }
      else
      {
        throw new TxDataException("validate_" + para, "必填参数");
      }
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
      ResourceApp model = (ResourceApp)ResourceApp.dao.findById(id);
      if (model != null) {
        if (status.equals(DataStatus.DELETED.name()))
        {
          model.delete();
        }
        else
        {
          model.set("data_status", status);
          model.update();
        }
      }
    }
    renderJson(res);
  }
  
  @Info(des="运维工具下拉列表查询")
  public void appListInvoke()
  {
    List<Map<String, String>> listMap = new ArrayList();
    List<App> tools = App.dao.listAll();
    for (App tool : tools)
    {
      Map<String, String> map = new LinkedHashMap();
      map.put("id", tool.get("id").toString());
      map.put("note", tool.getStr("name_cn"));
      listMap.add(map);
    }
    renderJson(listMap);
  }
}
