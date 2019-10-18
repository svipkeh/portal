package sys.privilege.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.DataStatus;
import common.enums.LegalSourceScopeType;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.util.Map;
import sys.privilege.model.LegalSource;
import sys.privilege.model.User;
import sys.privilege.validator.LegalSourceValidator;

public class LegalSourceController
  extends BaseController
{
  @Info(des="进入访问源列表页面")
  public void list()
  {
    clearPagerInfo();
    render("legalSourceList.jsp");
  }
  
  @Info(des="访问源列表查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<LegalSource> page = LegalSource.dao.getAll(getPageCurrent(), getPageSize(), queryMap);
    for (LegalSource data : page.getList())
    {
      data.put("scope_type_str", LegalSourceScopeType.valueOf(data.getStr("scope_type")).note());
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="进入新增访问源页面")
  public void add()
  {
    render("legalSourceAdd.jsp");
  }
  
  @Info(des="新增访问源")
  @Before({Tx.class, LegalSourceValidator.class})
  public void saveInvoke()
  {
    Res res = new Res();
    LegalSource source = (LegalSource)getModel(LegalSource.class, "source");
    if (LegalSourceScopeType.ALL.name().equals(source.getStr("scope_type")))
    {
      LegalSource s = LegalSource.dao.getByIp(source.getStr("ip"), LegalSourceScopeType.ALL.name());
      if (s != null) {
        throw new TxDataException("validate_source_ip", "该IP已配置");
      }
      source.set("scope_target", null);
    }
    else if (LegalSourceScopeType.FIXED.name().equals(source.getStr("scope_type")))
    {
      if (StringUtil.isBlank(source.getStr("scope_target"))) {
        throw new TxDataException("validate_scope_target", "请输入用户名");
      }
      User u = User.dao.getByName(source.getStr("scope_target"));
      if (u == null) {
        throw new TxDataException("validate_scope_target", "该用户不存在");
      }
      LegalSource s = LegalSource.dao.getByIpAndUserName(source.getStr("ip"), 
        LegalSourceScopeType.FIXED.name(), source.getStr("scope_target"));
      if (s != null) {
        throw new TxDataException("validate_scope_target", "该用户已配置");
      }
    }
    else
    {
      throw new TxDataException("validate_scope_type", "参数适用范围错误");
    }
    source.set("data_status", DataStatus.ENABLED.name());
    source.save();
    renderJson(res);
  }
  
  @Info(des="访问源数据状态改变")
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
      LegalSource model = (LegalSource)LegalSource.dao.findById(id);
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
