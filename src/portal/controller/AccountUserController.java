package portal.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.DataStatus;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.Map;
import portal.model.Account;
import portal.model.AccountUser;
import portal.model.Resource;

public class AccountUserController
  extends BaseController
{
  @Info(des="进入所有者管理页面")
  public void list()
  {
    clearPagerInfo();
    String accid = getPara("accid");
    if (StringUtil.isBlank(accid)) {
      throw new TxDataException("未选择账号");
    }
    Account account = (Account)Account.dao.findById(accid);
    if (account == null) {
      throw new TxDataException("未知的账号");
    }
    setAttr("account", account);
    Resource resource = (Resource)Resource.dao.findById(account.get("resource_id"));
    setAttr("resource", resource);
    render("accountUserList.jsp");
  }
  
  @Info(des="所有者查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<AccountUser> page = AccountUser.dao.listPage(getPageCurrent(), getPageSize(), queryMap);
    for (AccountUser data : page.getList())
    {
      data.put("password", "");
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="用户授权")
  @Before({Tx.class})
  public void statusInvoke()
  {
    Res res = new Res();
    int resid = getParaToInt("resid").intValue();
    int accid = getParaToInt("accid").intValue();
    String status = getPara("status");
    String ids = getPara("ids");
    if (DataStatus.valueOf(status) == null) {
      throw new TxDataException("状态错误");
    }
    if (StringUtil.isBlank(ids)) {
      throw new TxDataException("数据未选择");
    }
    String[] idsArray = ids.split(",");
    for (String id : idsArray) {
      if (status.equals(DataStatus.ENABLED.name()))
      {
        AccountUser.dao.delByAccountAndUser(accid, id);
        
        AccountUser tu = new AccountUser();
        tu.set("resource_id", Integer.valueOf(resid));
        tu.set("account_id", Integer.valueOf(accid));
        tu.set("user_id", Integer.valueOf(Integer.parseInt(id)));
        tu.save();
      }
      else
      {
        AccountUser.dao.delByAccountAndUser(accid, id);
      }
    }
    renderJson(res);
  }
}
