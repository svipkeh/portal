package fort.controller;

import com.jfinal.plugin.activerecord.Page;
import common.annotation.Info;
import common.enums.FortAccStatus;
import common.enums.FortResStatus;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import fort.model.FortAcc;
import fort.model.FortRes;
import java.util.Map;
import ws.fort.deamon.FortKit;
import ws.fort.deamon.FortServMgrDeamon;

public class FortAccController
  extends BaseController
{
  @Info(des="进入图堡账号列表页面")
  public void list()
  {
    clearPagerInfo();
    render("fortaccList.jsp");
  }
  
  @Info(des="图堡账号列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<FortAcc> page = FortAcc.dao.getPage(getPageCurrent(), getPageSize(), queryMap);
    for (FortAcc data : page.getList())
    {
      data.put("s_status_str", FortAccStatus.valueOf(data.getStr("s_status")).note());
      data.put("fort_running_status_str", FortResStatus.valueOf(data.getStr("running_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="断开会话")
  public void closeSessionInvoke()
  {
    Res res = new Res();
    String accId = getPara("acc_id");
    if (StringUtil.isBlank(accId)) {
      throw new TxDataException("未选择账号");
    }
    FortAcc acc = (FortAcc)FortAcc.dao.findById(accId);
    if (acc == null) {
      throw new TxDataException("未知的账号");
    }
    if (!FortAccStatus.CONNECTED.name().equals(acc.getStr("s_status"))) {
      throw new TxDataException("该账号未处于登录状态");
    }
    FortRes fort = (FortRes)FortRes.dao.findById(acc.get("fort_id").toString());
    if (fort == null) {
      throw new TxDataException("错误的账号，未找到对应堡垒机");
    }
    try
    {
      String sessionId = acc.getStr("s_session_id");
      Integer.parseInt(sessionId);
      FortServMgrDeamon.instance().closeSession(fort, sessionId);
    }
    catch (Exception e)
    {
      throw new TxDataException("注销账号失败");
    }
    renderJson(res);
  }
  
  @Info(des="修改图堡账号密码")
  public void changePwdInvoke()
  {
    Res res = new Res();
    String accId = getPara("acc_id");
    String pwd = getPara("new_pwd");
    String autoPwd = getPara("auto_pwd");
    if (StringUtil.isBlank(accId)) {
      throw new TxDataException("未选择账号");
    }
    FortAcc acc = (FortAcc)FortAcc.dao.findById(accId);
    if (acc == null) {
      throw new TxDataException("未知的账号");
    }
    FortRes fort = (FortRes)FortRes.dao.findById(acc.get("fort_id").toString());
    if (fort == null) {
      throw new TxDataException("错误的账号，未找到对应堡垒机");
    }
    pwd = ("on".equals(autoPwd)) || (pwd == null) || ("".equals(pwd)) ? FortKit.getRandomPassword() : pwd;
    try
    {
      FortServMgrDeamon.instance().changeAccPwd(fort, acc.getStr("username"), pwd);
    }
    catch (Exception e)
    {
      throw new TxDataException("修改密码失败");
    }
    renderJson(res);
  }
  
  @Info(des="修改本页所有图堡账号密码")
  public void changePwdAllInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<FortAcc> page = FortAcc.dao
      .getPage(getPageCurrent("listInvoke"), getPageSize("listInvoke"), queryMap);
    String pwd = FortKit.getRandomPassword();
    for (FortAcc acc : page.getList()) {
      try
      {
        FortRes fort = (FortRes)FortRes.dao.findById(acc.get("fort_id").toString());
        FortServMgrDeamon.instance().changeAccPwd(fort, acc.getStr("username"), pwd);
      }
      catch (Exception e)
      {
        throw new TxDataException("修改密码失败: " + acc.getStr("username"));
      }
    }
    renderJson(res);
  }
}
