package fort.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.DataStatus;
import common.enums.FortResStatus;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import fort.model.FortAcc;
import fort.model.FortRes;
import fort.validator.FortResValidator;
import java.util.List;
import ws.fort.deamon.FortServMgrDeamon;

public class FortResController extends BaseController {
  @Info(des="进入服务器列表页面")
  public void list() {
    clearPagerInfo();
    render("fortresList.jsp");
  }
  
  @Info(des="服务器列表查询", isNeesLog=false)
  public void listInvoke() {
    Res res = new Res();
    List<FortRes> list = FortRes.dao.listAll();
    for (FortRes data : list) {
      if (data.getStr("data_status").equals(DataStatus.DISABLED.name())) {
        data.put("running_status", FortResStatus.UNKNOWN.name());
      }
      data.put("running_status_str", FortResStatus.valueOf(data.getStr("running_status")).note());
      data.put("data_status_str", DataStatus.valueOf(data.getStr("data_status")).note());
    }
    res.setValue(new Page(list, 0, 0, 0, 0));
    renderJson(res);
  }
  
  @Info(des="进入新增服务器页面")
  public void add()
  {
    render("fortresAdd.jsp");
  }
  
  @Info(des="新增服务器")
  @Before({Tx.class, FortResValidator.class})
  public void addInvoke() {
    Res res = new Res();
    FortRes fort = (FortRes)getModel(FortRes.class, "res");
    String ip1 = (String)fort.get("fort_ip");
    FortRes f1 = FortRes.dao.getByIp(ip1);
    if (f1 != null) {
      throw new TxDataException("validate_fort_ip", "已存在相同用IP");
    }
    String ip2 = (String)fort.get("fort_ip_wan");
    FortRes f2 = FortRes.dao.getByIp(ip2);
    if (f2 != null) {
      throw new TxDataException("validate_fort_ip_wan", "已存在相同用IP");
    }
    fort.set("running_status", FortResStatus.UNKNOWN.name());
    fort.set("data_status", DataStatus.ENABLED.name());
    fort.save();
    renderJson(res);
  }
  
  @Info(des="进入修改服务器页面")
  public void edit() {
    String id = getPara("id");
    if (StringUtil.isBlank(id)) {
      throw new TxDataException("未选择服务器");
    }
    FortRes fortRes = (FortRes)FortRes.dao.findById(id);
    if (fortRes == null) {
      throw new TxDataException("未知的服务器");
    }
    setAttr("res", fortRes);
    render("fortresEdit.jsp");
  }
  
  @Info(des="修改服务器")
  @Before({Tx.class, FortResValidator.class})
  public void editInvoke() {
    Res res = new Res();
    FortRes fort = (FortRes)getModel(FortRes.class, "res");
   /* FortRes dbFort = (FortRes)FortRes.dao.findById(fort.get("id"));
    if (dbFort == null) {
      throw new TxDataException("服务器未找到");
    }*/
    String ip1 = (String)fort.get("fort_ip");
    FortRes f1 = FortRes.dao.getByIp(ip1);
    if ((f1 != null) && (fort.getInt("id").intValue() != f1.getInt("id").intValue())) {
      throw new TxDataException("validate_fort_ip", "已存在相同用IP");
    }
    String ip2 = (String)fort.get("fort_ip_wan");
    FortRes f2 = FortRes.dao.getByIp(ip2);
    if ((f2 != null) && (fort.getInt("id").intValue() != f2.getInt("id").intValue())) {
      throw new TxDataException("validate_fort_ip_wan", "已存在相同用IP");
    }
    fort.update();
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
      FortRes model = (FortRes)FortRes.dao.findById(id);
      if (model != null) {
        if (DataStatus.DELETED.name().equals(status))
        {
          model.delete();
          FortAcc.dao.deletByFortId(id);
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
  
  @Info(des="启动图堡管理服务")
  public void servmgrServiceStartInvoke() {
    if (FortServMgrDeamon.instance().isRunning()) {
      throw new TxDataException("启动失败，该服务已经启动");
    }
    try {
      FortServMgrDeamon.instance().startService();
      renderJson(new Res());
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new TxDataException("启动图堡管理服务出错");
    }
  }
  
  @Info(des="停止图堡管理服务")
  public void servmgrServiceStopInvoke() {
    if (!FortServMgrDeamon.instance().isRunning()) {
      throw new TxDataException("启动失败，该服务已经停止");
    }
    try {
      FortServMgrDeamon.instance().stopService();
      renderJson(new Res());
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new TxDataException("停止图堡管理服务出错");
    }
  }
}
