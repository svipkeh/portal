package sys.privilege.controller;

import com.google.gson.Gson;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.DataStatus;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.List;
import java.util.Map;
import sys.privilege.model.Function;
import sys.privilege.model.Menu;
import sys.privilege.model.Method;
import sys.privilege.utils.FunctionToJson;
import sys.privilege.validator.FunctionValidator;

public class FunctionController
  extends BaseController
{
  @Info(des="进入功能列表页面")
  public void list()
  {
    clearPagerInfo();
    render("functionList.jsp");
  }
  
  @Info(des="功能列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    List<Function> list = Function.dao.getFunctions((String)queryMap.get("menu_id"), (String)queryMap.get("function_id"));
    for (Function data : list) {
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(null, queryMap);
    res.setValue(new Page(list, 0, 0, 0, 0));
    renderJson(res);
  }
  
  @Info(des="进入新增功能页面")
  public void add()
  {
    render("functionAdd.jsp");
  }
  
  @Info(des="新增功能")
  @Before({Tx.class, FunctionValidator.class})
  public void saveInvoke()
  {
    Res res = new Res();
    Function function = (Function)getModel(Function.class, "function");
    Menu menu = (Menu)Menu.dao.findById(function.getInt("menu_id"));
    if ((menu == null) || (StringUtil.isBlank(menu.getStr("url")))) {
      throw new TxDataException("validate_function_menu_id", "菜单选择无效");
    }
    function.set("data_status", DataStatus.ENABLED.name());
    function.save();
    
    String f = getPara("f");
    Gson gson = new Gson();
    Map fMap = (Map)gson.fromJson(f, Map.class);
    List<List<String>> methods = (List)fMap.get("methods");
    for (List<String> m : methods)
    {
      Method mt = new Method();
      String[] marray = ((String)m.get(1)).split("--");
      mt.set("func_id", function.getInt("id"));
      mt.set("controller", marray[0]);
      mt.set("method", marray[1]);
      mt.set("name_cn", marray[2]);
      mt.save();
    }
    renderJson(res);
  }
  
  @Info(des="进入修改功能页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择功能");
    }
    Function function = (Function)Function.dao.findById(id);
    if (function == null) {
      throw new TxDataException("未知的功能");
    }
    setAttr("function", function);
    List<Method> methods = Method.dao.getMethodsByFunc(function.get("id").toString());
    setAttr("methods", methods);
    Menu menu = (Menu)Menu.dao.findById(function.getInt("menu_id"));
    setAttr("menu", menu);
    render("functionEdit.jsp");
  }
  
  @Info(des="修改功能")
  @Before({Tx.class, FunctionValidator.class})
  public void updateInvoke()
  {
    Res res = new Res();
    
    Function function = getModel(Function.class, "function");
    Integer id = function.get("id");
    Function dbFunction = Function.dao.findById(id);
    if (dbFunction == null) {
      throw new TxDataException("该功能未找到");
    }
    function.update();
    
    List<Method> oldMs = Method.dao.getMethodsByFunc(function.get("id").toString());
    for (Method m : oldMs) {
      m.delete();
    }
    String f = getPara("f");
    Gson gson = new Gson();
    Map fMap = (Map)gson.fromJson(f, Map.class);
    List<List<String>> methods = (List)fMap.get("methods");
    for (List<String> m : methods)
    {
      Method mt = new Method();
      String[] marray = ((String)m.get(1)).split("--");
      mt.set("func_id", function.getInt("id"));
      mt.set("controller", marray[0]);
      mt.set("method", marray[1]);
      mt.set("name_cn", marray[2]);
      mt.save();
    }
    renderJson(res);
  }
  
  @Info(des="功能数据状态改变")
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
      Function model = (Function)Function.dao.findById(id);
      if (model != null) {
        if (DataStatus.DELETED.name().equals(status))
        {
          model.delete();
          Function.dao.deleteRef(id);
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
  
  @Info(des="功能树形结构查询")
  public void functionTreeInvoke()
  {
    List<Menu> menus = Menu.dao.getAllMenu();
    List<Function> functions = Function.dao.getAllFunction();
    renderJson(new FunctionToJson(menus, functions).toJson());
  }
}
