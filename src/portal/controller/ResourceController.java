package portal.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.DataStatus;
import common.enums.ResType;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.List;
import java.util.Map;
import portal.model.Resource;
import portal.model.ResourceApp;
import portal.validator.ResourceValidator;

public class ResourceController
  extends BaseController
{
  @Info(des="进入资源管理页面")
  public void list()
  {
    clearPagerInfo();
    render("resourceList.jsp");
  }
  
  @Info(des="资源列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<Resource> page = Resource.dao.getPage(getPageCurrent(), getPageSize(), queryMap);
    for (Resource data : page.getList())
    {
      data.put("res_type_str", ResType.valueOf(data.getStr("res_type")).note());
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
      List<ResourceApp> resapps = ResourceApp.dao.listAllEnabled(data.get("id").toString());
      StringBuilder sb = new StringBuilder();
      for (ResourceApp resapp : resapps) {
        sb.append("(" + resapp.getStr("name_cn") + ") ");
      }
      data.put("resapps", sb.toString());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增资源页面")
  public void add()
  {
    render("resourceAdd.jsp");
  }
  
  @Info(des="新增资源")
  @Before({Tx.class, ResourceValidator.class})
  public void addInvoke()
  {
    Res res = new Res();
    Resource resource = (Resource)getModel(Resource.class, "resource");
    String name = (String)resource.get("name");
    Resource u = Resource.dao.getByName(name);
    if (u != null) {
      throw new TxDataException("validate_name", "已存在相同资源标识");
    }
    resource.set("data_status", DataStatus.ENABLED.name());
    resource.save();
    renderJson(res);
  }
  
  @Info(des="进入修改资源页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择资源");
    }
    Resource resource = (Resource)Resource.dao.findById(id);
    if (resource == null) {
      throw new TxDataException("未知的资源");
    }
    setAttr("resource", resource);
    render("resourceEdit.jsp");
  }
  
  @Info(des="修改资源")
  @Before({Tx.class, ResourceValidator.class})
  public void editInvoke()
  {
    Res res = new Res();
    Resource resource = (Resource)getModel(Resource.class, "resource");
    Integer id = resource.get("id");
    Resource dbResource = Resource.dao.findById(id);
    if (dbResource == null) {
      throw new TxDataException("资源未找到");
    }

    String name = (String)resource.get("name");
    Resource r = Resource.dao.getByName(name);
    if ((r != null) && (r.getInt("id").intValue() != resource.getInt("id").intValue())) {
      throw new TxDataException("validate_name", "已存在相同资源标识");
    }
    resource.update();
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
      Resource model = (Resource)Resource.dao.findById(id);
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
}
