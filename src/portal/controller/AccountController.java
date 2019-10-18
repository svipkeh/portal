package portal.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.trustmo.encrypt.En3DESCoder;
import common.annotation.Info;
import common.enums.DataStatus;
import common.enums.ResType;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import portal.model.Account;
import portal.model.AccountUser;
import portal.model.Resource;
import portal.validator.AccountValidator;

public class AccountController extends BaseController {
  @Info(des="进入账号管理页面")
  public void list() {
    clearPagerInfo();
    render("accountList.jsp");
  }
  
  @Info(des="账号列表查询", isNeesLog=false)
  public void listInvoke() {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<Account> page = Account.dao.getPage(getPageCurrent(), getPageSize(), queryMap);
    for (Account data : page.getList()) {
      data.put("res_type_str", ResType.valueOf(data.getStr("res_type")).note());
      long num = AccountUser.getAccountUserNum(data.getInt("id").intValue());
      data.put("user_num", Long.valueOf(num));
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增账号页面")
  public void add()
  {
    render("accountAdd.jsp");
  }
  
  @Info(des="新增账号")
  @Before({Tx.class, AccountValidator.class})
  public void addInvoke() {
    Res res = new Res();
    Account account = (Account)getModel(Account.class, "account");
    Account a = Account.dao.getByName(account.get("resource_id").toString(), account.get("name").toString());
    if (a != null) {
      throw new TxDataException("validate_name", "已存在相同账号");
    }
    try
    {
      String key = En3DESCoder.initKey();
      account.set("password", En3DESCoder.encrypt(account.getStr("password"), key));
      account.set("en_key", key);
      account.set("time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
      account.save();
    }
    catch (Exception e)
    {
      throw new TxDataException("保存失败");
    }
    renderJson(res);
  }
  
  @Info(des="手动设置账号密码")
  @Before({Tx.class, AccountValidator.class})
  public void setPwdInvoke()
  {
    Res res = new Res();
    String accId = getPara("accid");
    String password = getPara("password");
    Account account = (Account)Account.dao.findById(accId);
    if (account == null) {
      throw new TxDataException("账号不存在");
    }
    try
    {
      String key = En3DESCoder.initKey();
      account.set("password", En3DESCoder.encrypt(password, key));
      account.set("en_key", key);
      account.set("time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }
    catch (Exception e)
    {
      throw new TxDataException("密码加密失败");
    }
    account.update();
    renderJson(res);
  }
  
  @Info(des="数据状态改变")
  @Before({Tx.class})
  public void statusInvoke() {
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
      Account model = (Account)Account.dao.findById(id);
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
  
  @Info(des="资源下拉列表查询")
  public void resourceListInvoke()
  {
    List<Map<String, String>> listMap = new ArrayList();
    List<Resource> ress = Resource.dao.listAllEnabled();
    for (Resource res : ress)
    {
      Map<String, String> map = new LinkedHashMap();
      map.put("id", res.get("id").toString());
      map.put("note", res.getStr("name_cn"));
      listMap.add(map);
    }
    renderJson(listMap);
  }
}
