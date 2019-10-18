package fort.controller;

import com.jfinal.plugin.activerecord.Record;
import common.annotation.Info;
import common.enums.DataStatus;
import common.enums.FortAccStatus;
import common.enums.FortResStatus;
import common.jfinal.controller.BaseController;
import fort.model.App;
import fort.model.FortViewHelper;
import java.util.List;
import ws.fort.deamon.FortServMgrDeamon;

public class FortViewController
  extends BaseController
{
  @Info(des="进入总览")
  public void view()
  {
    setAttr("fort_res_num", Long.valueOf(FortViewHelper.getFortResNum()));
    setAttr("fort_acc_num", Long.valueOf(FortViewHelper.getFortAccNum()));
    List<Record> fortResList = FortViewHelper.listFortRes();
    for (Record r : fortResList)
    {
      FortResStatus status = FortResStatus.valueOf(r.getStr("running_status"));
      if (status == FortResStatus.ZHENGCHANG) {
        r.set("bg_class", "success");
      } else if (status == FortResStatus.SHILIAN) {
        r.set("bg_class", "danger");
      } else {
        r.set("bg_class", "default");
      }
      DataStatus dataStatus = DataStatus.valueOf(r.getStr("data_status"));
      if (dataStatus == DataStatus.DISABLED) {
        r.set("bg_class", "default");
      }
      r.set("data_status_str", DataStatus.valueOf(r.getStr("data_status")).note());
      List<Record> accAnlysis = FortViewHelper.getFortAccAnalysis(r.get("id").toString());
      r.set("acc_notuse_num", Long.valueOf(getAccNum(accAnlysis, FortAccStatus.NOTUSE.name())));
      r.set("acc_assigned_num", Long.valueOf(getAccNum(accAnlysis, FortAccStatus.ASSIGNED.name())));
      r.set("acc_connected_num", Long.valueOf(getAccNum(accAnlysis, FortAccStatus.CONNECTED.name())));
      r.set("acc_disconnected_num", Long.valueOf(getAccNum(accAnlysis, FortAccStatus.DISCONNECTED.name())));
    }
    setAttr("fortResList", fortResList);
    setAttr("fort_serv_deamon_service_stauts", FortServMgrDeamon.instance().isRunning() ? "运行中" : "已停止");
    List<App> apps = App.dao.listAll();
    setAttr("apps_num", Integer.valueOf(apps.size()));
    render("fortview.jsp");
  }
  
  private long getAccNum(List<Record> accAnlysis, String name)
  {
    for (Record r : accAnlysis) {
      if (name.equals(r.getStr("s_status"))) {
        return r.getLong("num").longValue();
      }
    }
    return 0L;
  }
}
