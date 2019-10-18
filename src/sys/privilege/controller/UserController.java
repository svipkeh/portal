package sys.privilege.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.trustmo.encrypt.En3DESCoder;
import common.annotation.Info;
import common.enums.DataStatus;
import common.jfinal.controller.BaseController;
import common.utils.ModelUtil;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.List;
import java.util.Map;
import sys.index.utils.UserLockMap;
import sys.privilege.model.Role;
import sys.privilege.model.User;
import sys.privilege.model.UserRole;
import sys.privilege.validator.UserValidator;

public class UserController
  extends BaseController
{
  @Info(des="进入用户列表页面")
  public void list()
  {
    clearPagerInfo();
    render("userList.jsp");
  }
  
  @Info(des="用户列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<User> page = User.dao.getAllUser(getPageCurrent(), getPageSize(), queryMap);
    for (User data : page.getList())
    {
      data.put("password", "");
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增用户页面")
  public void add()
  {
    List<Role> roles = Role.dao.getAllEnabledRole();
    setAttr("roles", ModelUtil.toJson(roles, new String[] { "id", "name" }));
    render("userAdd.jsp");
  }
  
  @Info(des="新增用户")
  @Before({Tx.class, UserValidator.class})
  public void saveInvoke()
  {
    Res res = new Res();
    User user = (User)getModel(User.class, "user");
    String name = (String)user.get("name");
    User u = User.dao.getByName(name);
    if (u != null) {
      throw new TxDataException("validate_user_name", "已存在相同用户名");
    }
    user.set("data_status", DataStatus.ENABLED.name());
    try
    {
      user.set("password", En3DESCoder.encrypt(user.getStr("name")));
    }
    catch (Exception localException) {}
    user.save();
    
    String[] roleIds = getParaValues("role_ids");
    for (String roleId : roleIds)
    {
      UserRole ur = new UserRole();
      ur.set("user_id", user.getInt("id"));
      ur.set("role_id", roleId);
      ur.save();
    }
    renderJson(res);
  }
  
  @Info(des="进入修改用户页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择用户");
    }
    User user = (User)User.dao.findById(id);
    if (user == null) {
      throw new TxDataException("未知的用户");
    }
    List<UserRole> urs = UserRole.dao.getAllUserRole(id);
    if (urs.size() > 0) {
      setAttr("role_ids", ModelUtil.toArray(urs, "role_id"));
    }
    List<Role> roles = Role.dao.getAllEnabledRole();
    setAttr("roles", ModelUtil.toJson(roles, new String[] { "id", "name" }));
    user.set("password", "");
    setAttr("user", user);
    render("userEdit.jsp");
  }
  
  @Info(des="修改用户")
  @Before({Tx.class, UserValidator.class})
  public void updateInvoke()
  {
	System.out.println("==============================");
    Res res = new Res();
    
    
    User user = (User)getModel(User.class, "user");
    Integer id = user.get("id");
    User dbUser = (User)User.dao.findById(id);
    if (dbUser == null) {
      throw new TxDataException("用户未找到");
    }
    String name = (String)user.get("name");
    User u = User.dao.getByName(name);
    if ((u != null) && (u.getInt("id").intValue() != user.getInt("id").intValue())) {
      throw new TxDataException("validate_user_name", "已存在相同用户名");
    }
    user.update();
    
    List<UserRole> urs = UserRole.dao.getAllUserRole(user.get("id").toString());
    for (UserRole ur : urs) {
      ur.delete();
    }
    String[] roleIds = getParaValues("role_ids");
    for (String roleId : roleIds)
    {
      UserRole ur = new UserRole();
      ur.set("user_id", user.getInt("id"));
      ur.set("role_id", roleId);
      ur.save();
    }
    renderJson(res);
  }
  
  @Info(des="重置用户密码")
  @Before({Tx.class})
  public void refreshPwdInvoke()
  {
    Res res = new Res();
    String ids = getPara("ids");
    if (StringUtil.isBlank(ids)) {
      throw new TxDataException("数据未选择");
    }
    String[] idsArray = ids.split(",");
    for (String id : idsArray)
    {
      User model = (User)User.dao.findById(id);
      if (model != null)
      {
        try
        {
          model.set("password", En3DESCoder.encrypt(model.getStr("name")));
        }
        catch (Exception localException) {}
        model.update();
      }
    }
    renderJson(res);
  }
  
  @Info(des="解锁用户")
  public void unlockInvoke()
  {
    Res res = new Res();
    String name = getPara("name");
    if (StringUtil.isBlank(name)) {
      throw new TxDataException("数据未选择");
    }
    UserLockMap.instance().unlock(name);
    renderJson(res);
  }
  
  @Info(des="用户数据状态改变")
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
      User model = (User)User.dao.findById(id);
      if (model != null) {
        if (status.equals(DataStatus.DELETED.name()))
        {
          if (id.equals(getSessionAttr("SESSEION_USER_ID").toString())) {
            throw new TxDataException("不能删除自己");
          }
          model.delete();
          User.dao.deleteRef(id);
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
