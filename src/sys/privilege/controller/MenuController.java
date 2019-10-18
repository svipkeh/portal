package sys.privilege.controller;

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
import sys.privilege.model.Menu;
import sys.privilege.utils.MenuToJson;
import sys.privilege.validator.MenuValidator;

public class MenuController
  extends BaseController
{
  @Info(des="进入菜单列表页面")
  public void list()
  {
    clearPagerInfo();
    render("menuList.jsp");
  }
  
  @Info(des="菜单列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> qm = getQueryMap();
    Page<Menu> page = Menu.dao.getAllMenu(getPageCurrent(), getPageSize(), qm);
    for (Menu data : page.getList()) {
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, qm);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增菜单页面")
  public void add()
  {
    render("menuAdd.jsp");
  }
  
  @Info(des="新增菜单")
  @Before({Tx.class, MenuValidator.class})
  public void saveInvoke()
  {
    Res res = new Res();
    Menu menu = (Menu)getModel(Menu.class, "menu");
    if (menu.get("pid") == null) {
      menu.set("pid", Integer.valueOf(0));
    }
    menu.set("data_status", DataStatus.ENABLED.name());
    menu.save();
    renderJson(res);
  }
  
  @Info(des="进入修改菜单页面")
  public void edit()
  {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择菜单");
    }
    Menu menu = (Menu)Menu.dao.findById(id);
    if (menu == null) {
      throw new TxDataException("未知的菜单");
    }
    List<Menu> allMenu = Menu.dao.getAllMenu();
    for (Menu m : allMenu) {
      if (id.equals(m.get("id").toString()))
      {
        allMenu.remove(m);
        break;
      }
    }
    setAttr("allMenu", new MenuToJson(allMenu).toJson());
    setAttr("menu", menu);
    render("menuEdit.jsp");
  }
  
  @Info(des="修改菜单")
  @Before({Tx.class, MenuValidator.class})
  public void updateInvoke()
  {
    Res res = new Res();
    Menu menu = getModel(Menu.class, "menu");
    Integer id = menu.get("id");
    Menu dbMenu = Menu.dao.findById(id);
    if (dbMenu == null) {
      throw new TxDataException("菜单未找到");
    }
    if ((menu.get("pid") == null) || ("".equals(menu.get("pid")))) {
      menu.set("pid", Integer.valueOf(0));
    }
    menu.update();
    renderJson(res);
  }
  
  @Info(des="菜单数据状态改变")
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
      Menu model = (Menu)Menu.dao.findById(id);
      if (model != null) {
        if (status.equals(DataStatus.DELETED.name()))
        {
          model.delete();
          Menu.dao.deleteRef(id);
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
  
  @Info(des="菜单树形结构查询")
  public void menuTreeInvoke()
  {
    List<Menu> allMenu = Menu.dao.getAllMenu();
    renderJson(new MenuToJson(allMenu).toJson());
  }
}
