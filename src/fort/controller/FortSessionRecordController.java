package fort.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import common.annotation.Info;
import common.enums.FortSessionExStatus;
import common.enums.YesNo;
import common.jfinal.controller.BaseController;
import common.utils.StringUtil;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import fort.model.FortSessionRecord;
import fort.utils.FortNetModeTool;
import fort.utils.FortServiceCaller;
import fort.validator.SessionLogValidator;
import java.util.Map;

public class FortSessionRecordController
  extends BaseController
{
  @Info(des="进入历史运维会话页面")
  public void list()
  {
    clearPagerInfo();
    render("fortSessionRecordList.jsp");
  }
  
  @Info(des="历史运维会话查询", isNeesLog=false)
  public void listInvoke()
  {
    Res res = new Res();
    Map<String, String> queryMap = getQueryMap();
    Page<FortSessionRecord> page = FortSessionRecord.dao.getPage(getPageCurrent(), getPageSize(), queryMap);
    for (FortSessionRecord data : page.getList())
    {
      data.put("is_record_str", YesNo.valueOf(data.getStr("is_record")).note());
      data.put("ex_status_str", FortSessionExStatus.valueOf(data.getStr("ex_status")).note());
    }
    savePagerInfo(page, queryMap);
    res.setValue(page);
    renderJson(res);
  }
  
  @Info(des="播放录像")
  @Before({Tx.class, SessionLogValidator.class})
  public void playInvoke()
  {
    Res res = new Res();
    String[] backArray = FortServiceCaller.call(FortNetModeTool.getNetModel(getRequest()), "player " + 
      getPara("sessionid"), false);
    if (backArray[0].equals("1")) {
      res.setValue(backArray[2]);
    } else {
      throw new TxDataException(backArray[2]);
    }
    renderJson(res);
  }
  
  @Info(des="删除录像")
  @Before({Tx.class, SessionLogValidator.class})
  public void delRecordInvoke()
  {
    Res res = new Res();
    String ids = getPara("ids");
    if (StringUtil.isBlank(ids)) {
      throw new TxDataException("数据未选择");
    }
    String[] idsArray = ids.split(",");
    for (String id : idsArray)
    {
      String[] backArray = FortServiceCaller.delRecord(id);
      if (backArray[0].equals("1")) {
        res.setValue(backArray[1]);
      } else {
        throw new TxDataException(backArray[1]);
      }
    }
    renderJson(res);
  }
}
