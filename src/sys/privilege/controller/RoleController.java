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
import sys.privilege.model.Role;
import sys.privilege.model.RolePriv;
import sys.privilege.utils.FunctionToJson;
import sys.privilege.utils.RolePrivToJson;
import sys.privilege.validator.RoleValidator;

public class RoleController
  extends BaseController
{
  @Info(des="进入角色列表页面")
  public void list()
  {
    clearPagerInfo();
    render("roleList.jsp");
  }
  
  @Info(des="角色列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<Role> page = Role.dao.getAllRole(getPageCurrent(), getPageSize(), queryMap);
    for (Role data : page.getList()) {
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增角色页面")
  public void add()
  {
    render("roleAdd.jsp");
  }
  
  @Info(des="新增角色")
  @Before({Tx.class, RoleValidator.class})
  public void saveInvoke()
  {
    Res res = new Res();
    Role role = (Role)getModel(Role.class, "role");
    String name = (String)role.get("name");
    Role r = Role.dao.getByName(name);
    if (r != null) {
      throw new TxDataException("validate_role_name", "已存在相同角色名");
    }
    role.set("data_status", DataStatus.ENABLED.name());
    role.save();
    
    String selectedNodes = getPara("selected_nodes");
    Gson gson = new Gson();
    List<List<String>> nodes = (List)gson.fromJson(selectedNodes, List.class);
    if (nodes.size() == 0) {
      throw new TxDataException("validate_role_priv", "未选择权限");
    }
    for (List<String> n : nodes)
    {
      RolePriv rp = new RolePriv();
      rp.set("role_id", role.getInt("id"));
      rp.set("res_id", n.get(0));
      rp.set("res_type", n.get(1));
      rp.save();
    }
    renderJson(res);
  }
  
  @Info(des="进入修改角色页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择角色");
    }
    Role role = (Role)Role.dao.findById(id);
    if (role == null) {
      throw new TxDataException("未知的角色");
    }
    setAttr("role", role);
    List<RolePriv> privileges = RolePriv.dao.getAllPrivilege(role.get("id").toString());
    setAttr("privileges", new RolePrivToJson(privileges).toJson());
    render("roleEdit.jsp");
  }
  
  @Info(des="修改角色")
  @Before({Tx.class, RoleValidator.class})
  public void updateInvoke()
  {
    Res res = new Res();
    Role role = getModel(Role.class, "role");
    Integer id = role.get("id");
    Role dbRole = Role.dao.findById(id);
    if (dbRole == null) {
      throw new TxDataException("角色未找到");
    }
    String name = (String)role.get("name");
    Role r = Role.dao.getByName(name);
    if ((r != null) && (r.getInt("id").intValue() != role.getInt("id").intValue())) {
      throw new TxDataException("validate_role_name", "已存在相同角色名");
    }
    role.update();
    
    List<RolePriv> oldRolePrivelege = RolePriv.dao.getAllPrivilege(role.get("id").toString());
    for (RolePriv rpv : oldRolePrivelege) {
      rpv.delete();
    }
    String selectedNodes = getPara("selected_nodes");
    Gson gson = new Gson();
    List<List<String>> nodes = (List)gson.fromJson(selectedNodes, List.class);
    if (nodes.size() == 0) {
      throw new TxDataException("validate_role_priv", "未选择权限");
    }
    for (List<String> n : nodes)
    {
      RolePriv rp = new RolePriv();
      rp.set("role_id", role.getInt("id"));
      rp.set("res_id", n.get(0));
      rp.set("res_type", n.get(1));
      rp.save();
    }
    renderJson(res);
  }
  
  @Info(des="角色数据状态改变")
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
      Role model = (Role)Role.dao.findById(id);
      if (model != null) {
        if (status.equals(DataStatus.DELETED.name()))
        {
          model.delete();
          Role.dao.deleteRef(id);
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
    if ("admin".equals(getSessionAttr("SESSEION_USER_NAME")))
    {
      List<Menu> menus = Menu.dao.getAllEnabledMenu();
      List<Function> functions = Function.dao.getAllEnabledFunction();
      renderJson(new FunctionToJson(menus, functions).toJson());
    }
    else
    {
      List<Menu> menus = Menu.dao.getMenusByUser(((Integer)getSessionAttr("SESSEION_USER_ID")).intValue());
      List<Function> functions = Function.dao.getFunctionsByUser(((Integer)getSessionAttr("SESSEION_USER_ID")).intValue());
      renderJson(new FunctionToJson(menus, functions).toJson());
    }
  }
}
