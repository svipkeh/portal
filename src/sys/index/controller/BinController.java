package sys.index.controller;

import com.jfinal.aop.Before;
import com.jfinal.kit.Prop;
import com.jfinal.plugin.activerecord.Record;
import com.trustmo.decrypt.De3DESCoder;
import com.trustmo.encrypt.En3DESCoder;
import common.annotation.Info;
import common.enums.DataStatus;
import common.jfinal.WebRootConfig;
import common.jfinal.controller.BaseController;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import fort.model.App;
import fort.utils.AppConverseTool;
import fort.utils.FortNetModeTool;
import fort.utils.FortServiceCaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import portal.model.Account;
import portal.model.AccountUser;
import portal.model.Resource;
import portal.model.ResourceApp;
import portal.model.ResourceAppAttr;
import sys.index.model.BinDbHelper;
import sys.index.utils.MenuToHtml;
import sys.index.utils.MyCaptchaRender;
import sys.index.utils.UserLockMap;
import sys.index.validator.BinValidator;
import sys.privilege.model.LegalSource;
import sys.privilege.model.Menu;
import sys.privilege.model.Method;
import sys.privilege.model.SysLog;
import sys.privilege.model.User;

public class BinController
  extends BaseController
{
  @Info(des="进入登录页面", isControl=false)
  public void index()
  {
    render("/WEB-INF/jsp/sys/index/bin/login.jsp");
  }
  
  @Info(des="生成验证码", isControl=false, isNeesLog=false)
  public void checkCodeImg()
  {
    render(new MyCaptchaRender(60, 22, 4, true));
  }
  
  @Info(des="登录", isControl=false)
  @Before({BinValidator.class})
  public void loginInvoke()
  {
    Res res = new Res();
    String username = getPara("username");
    String password = getPara("password");
    String inputRandomCode = getPara("checkcode");
    if (!MyCaptchaRender.validate(this, inputRandomCode)) {
      throw new TxDataException("validate_login", "验证码错误");
    }
    if (!UserLockMap.instance().allowLogin(username, getRequest().getRemoteAddr())) {
      throw new TxDataException("validate_login", "该用户已被锁定，请联系管理员");
    }
    try
    {
      password = En3DESCoder.encrypt(password);
    }
    catch (Exception localException) {}
    User u = User.dao.getByName(username);
    if (u == null) {
      throw new TxDataException("validate_login", "该用户不存在");
    }
    if (!DataStatus.ENABLED.name().equals(u.getStr("data_status"))) {
      throw new TxDataException("validate_login", "该用户已被禁用，请联系管理员");
    }
    if (!password.equals(u.getStr("password")))
    {
      UserLockMap.instance().recordLogin(username, false, getRequest().getRemoteAddr());
      throw new TxDataException("validate_login", "密码错误");
    }
    UserLockMap.instance().recordLogin(username, true, getRequest().getRemoteAddr());
    if (WebRootConfig.constant.getBoolean("base.legalsource.enable").booleanValue()) {
      if (LegalSource.dao.getEnabledByIpAndUserName(getRequest().getRemoteAddr(), username) == null)
      {
        SysLog log = new SysLog();
        log.set("user_id", u.get("id"));
        log.set("source_ip", getRequest().getRemoteAddr());
        log.set("action", "登录失败，未授权访问源登录");
        log.set("ex_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        log.save();
        
        throw new TxDataException("validate_login", "未授权访问源，请联系管理员");
      }
    }
    setSessionAttr("SESSEION_USER_ID", u.getInt("id"));
    setSessionAttr("SESSEION_USER_NAME", u.getStr("name"));
    setSessionAttr("SESSEION_USER_NAME_CN", u.getStr("name_cn"));
    
    List<Menu> menus = null;
    if ("admin".equals(u.getStr("name"))) {
      menus = Menu.dao.getAllEnabledMenu();
    } else {
      menus = Menu.dao.getMenusByUser(u.getInt("id").intValue());
    }
    String menuHtml = new MenuToHtml(getRequest().getContextPath(), menus).getMenuHtml();
    setSessionAttr("SESSEION_USER_MENU", menuHtml);
    
    List<Method> methods = Method.dao.getMethodsByUser(u.get("id").toString());
    setSessionAttr("SESSEION_USER_METHODS", methods);
    renderJson(res);
  }
  
  @Info(des="退出登录", isControl=false)
  public void logout()
  {
    clearSession();
    render("/WEB-INF/jsp/sys/index/bin/login.jsp");
  }
  
  @Info(des="自维护", isControl=false)
  public void weihu()
  {
    Object userId = getSession().getAttribute("SESSEION_USER_ID");
    if (userId != null)
    {
      User user = (User)User.dao.findById(userId);
      setAttr("user", user);
      render("/WEB-INF/jsp/sys/index/bin/weihu.jsp");
    }
    else
    {
      render("/WEB-INF/jsp/sys/index/bin/login.jsp");
    }
  }
  
  @Info(des="自维护保存", isControl=false)
  @Before({BinValidator.class})
  public void weihuInvoke()
  {
    Res res = new Res();
    Object userId = getSession().getAttribute("SESSEION_USER_ID");
    if (userId == null) {
      throw new TxDataException("您还未登录系统");
    }
    User user = (User)getModel(User.class, "user");
    if (!user.get("id").equals(userId)) {
      throw new TxDataException("只能修改自己的信息");
    }
    User dbUser = (User)User.dao.findById(userId);
    String newEnpwd = null;
    String oldEnPwd = null;
    try
    {
      newEnpwd = En3DESCoder.encrypt(user.getStr("password"));
      oldEnPwd = En3DESCoder.encrypt(getPara("password_old"));
    }
    catch (Exception localException) {}
    if (!oldEnPwd.equals(dbUser.getStr("password"))) {
      throw new TxDataException("validate_password_old", "原密码输入错误");
    }
    dbUser.set("password", newEnpwd);
    dbUser.update();
    renderJson(res);
  }
  
  @Info(des="进入首页")
  public void dashboard()
  {
    List<Record> ress = BinDbHelper.listOwnRes(((Integer)getSessionAttr("SESSEION_USER_ID")).intValue());
    List<Record> accs = BinDbHelper.listOwnAcc(((Integer)getSessionAttr("SESSEION_USER_ID")).intValue());
    for (Record res : ress)
    {
      List<Record> apps = BinDbHelper.listResApp(res.getInt("id"));
      res.set("apps", apps);
      List<Record> thisAccs = new ArrayList();
      for (Record acc : accs) {
        if (acc.getInt("resource_id").equals(res.getInt("id"))) {
          thisAccs.add(acc);
        }
      }
      res.set("accs", thisAccs);
    }
    setAttr("ress", ress);
    render("/WEB-INF/jsp/sys/index/bin/dashboard.jsp");
  }
  
  @Info(des="运行工具", isControl=false)
  public void runInvoke()
  {
    String appid = getPara("appid");
    String accid = getPara("accid");
    String resid = getPara("resid");
    App app = (App)App.dao.findById(appid);
    Resource res = (Resource)Resource.dao.findById(resid);
    Account acc = (Account)Account.dao.findById(accid);
    if ((app == null) || (res == null) || (acc == null)) {
      throw new TxDataException("运维工具参数错误");
    }
    ResourceApp ra = ResourceApp.dao.findByResAndApp(res.get("id").toString(), app.get("id").toString());
    if (ra == null) {
      throw new TxDataException("资源未配置该工具");
    }
    AccountUser au = AccountUser.dao.getByAccountAndUser(acc.getInt("id").intValue(), getSessionAttr("SESSEION_USER_ID")
      .toString());
    if (au == null) {
      throw new TxDataException("未获得账号授权");
    }
    run(app, res, acc, ra);
  }
  
  private void run(App app, Resource resource, Account acc, ResourceApp ra)
  {
    Res res = new Res();
    List<ResourceAppAttr> attrs = ResourceAppAttr.dao.listByResourceAppId(ra.get("id").toString());
    
    String cmdLine = app.getStr("parameter");
    List<String> paras = AppConverseTool.getParametes(cmdLine);
    for (String para : paras)
    {
      String paraVal = null;
      if ("ip".equals(para)) {
        paraVal = resource.getStr("ip");
      } else if ("accname".equals(para)) {
        paraVal = acc.getStr("name");
      } else if ("username".equals(para)) {
        paraVal = (String)getSessionAttr("SESSEION_USER_NAME");
      } else if ("password".equals(para)) {
        try
        {
          paraVal = 
            De3DESCoder.decrypt(acc.getStr("password"), acc.getStr("en_key"));
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      } else {
        paraVal = geInputParatVal(attrs, para);
      }
      cmdLine = cmdLine.replace("{{" + para + "}}", paraVal);
    }
    String[] backArray = FortServiceCaller.call(FortNetModeTool.getNetModel(getRequest()), cmdLine, true);
    if (backArray[0].equals("1")) {
      res.setValue(backArray[2]);
    } else {
      throw new TxDataException(backArray[2]);
    }
    renderJson(res);
  }
  
  private String geInputParatVal(List<ResourceAppAttr> attrs, String paraName)
  {
    if (attrs != null) {
      for (ResourceAppAttr a : attrs) {
        if (a.getStr("attr_name").equals(paraName)) {
          return a.getStr("attr_val");
        }
      }
    }
    return "";
  }
  
  @Info(des="下载客户端", isControl=false)
  public void clientDownload()
    throws Exception
  {
    File log = new File(getRequest().getServletContext().getRealPath("/") + "/WEB-INF/file/setup.exe");
    if (log.exists()) {
      renderFile(log);
    } else {
      renderText("文件未找到");
    }
  }
}
