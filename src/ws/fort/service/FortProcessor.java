package ws.fort.service;

import com.fort.common.Request;
import com.fort.common.Translation;
import com.trustmo.decrypt.De3DESCoder;
import common.enums.FortNetMode;
import common.enums.FortSessionExStatus;
import common.enums.YesNo;
import common.web.excetpion.TxDataException;
import fort.model.FortAcc;
import fort.model.FortSessionRecord;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import ws.fort.deamon.FortServMgrDeamon;

public class FortProcessor
{
  private static final Logger logger = Logger.getLogger(FortProcessor.class);
  
  public static String process(String netmode, boolean record, String sessionId, String cmdline)
  {
    Request r = new Request();
    FortAcc acc = null;
    try
    {
      FortNetMode fortNetMode = FortNetMode.valueOf(netmode);
      if (!FortServMgrDeamon.instance().isRunning()) {
        throw new TxDataException("堡垒机服务未运行");
      }
      if (cmdline.startsWith("player "))
      {
        String recordid = cmdline.substring("player ".length());
        
        FortSessionRecord fsr = FortSessionRecord.dao.getBySessionId(recordid);
        if (fsr == null) {
          throw new TxDataException("该会话不存在");
        }
        if (!YesNo.YES.name().equals(fsr.getStr("is_record"))) {
          throw new TxDataException("该会话没有录像");
        }
        if (!FortSessionExStatus.OK.name().equals(fsr.getStr("ex_status"))) {
          throw new TxDataException("会话异常，当前状态：" + 
            FortSessionExStatus.valueOf(fsr.getStr("ex_status")).note());
        }
        acc = FortServMgrDeamon.instance().assignAcc(sessionId, fsr.get("fort_id").toString());
        r.setIp(fortNetMode == FortNetMode.WAN ? acc.getStr("fort_ip_wan") : acc.getStr("fort_ip"));
        r.setPort(acc.getStr("mstsc_port"));
        r.setUsername(acc.getStr("username"));
        r.setPassword(De3DESCoder.decrypt(acc.getStr("password")));
        r.setParams(cmdline);
      }
      else
      {
        acc = FortServMgrDeamon.instance().assignAcc(sessionId, null);
        r.setIp(fortNetMode == FortNetMode.WAN ? acc.getStr("fort_ip_wan") : acc.getStr("fort_ip"));
        r.setPort(acc.getStr("mstsc_port"));
        r.setUsername(acc.getStr("username"));
        r.setPassword(De3DESCoder.decrypt(acc.getStr("password")));
        r.setParams(cmdline);
        if (record) {
          r.setParams("/r" + sessionId + " " + cmdline);
        }
      }
      String appName = cmdline;
      if (cmdline.indexOf(" ") > 0) {
        appName = cmdline.substring(0, cmdline.indexOf(" "));
      }
      FortSessionRecord fsr = new FortSessionRecord();
      fsr.set("session_id", sessionId);
      fsr.set("fort_id", acc.getInt("fort_id"));
      fsr.set("acc_name", acc.getStr("username"));
      fsr.set("app_name", appName);
      fsr.set("is_record", record ? YesNo.YES.name() : YesNo.NO.name());
      fsr.set("ex_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
      fsr.set("ex_status", FortSessionExStatus.ASSIGNED.name());
      fsr.save();
      return Translation.encrypt(Translation.toJson(r));
    }
    catch (TxDataException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new TxDataException("分配图堡出错");
    }
  }
}
