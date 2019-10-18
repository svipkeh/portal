package ws.fort.deamon;

import com.jfinal.kit.Prop;
import common.jfinal.WebRootConfig;
import common.web.excetpion.TxDataException;
import fort.model.FortAcc;
import fort.model.FortRes;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class AutoUpdatePwd implements Runnable {
  private static final Logger logger = Logger.getLogger(AutoUpdatePwd.class);
  
  @Override
  public void run() {
    for (;;) {
      try {
        if (FortServMgrDeamon.instance().isRunning()) {
          readAndUpatePwd();
        }
      }
      catch (Exception e) {
        logger.error(e.getMessage());
      }
      try {
        Thread.sleep(600000L);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  private void readAndUpatePwd()
  {
    int day = WebRootConfig.constant.getInt("fort.auto.update.password.interval").intValue();
    long deadline = DateTime.now().minusDays(day).getMillis();
    String pwd = FortKit.getRandomPassword();
    List<FortRes> ress = FortRes.dao.listAllEnabled();
    Iterator localIterator2;
    for (Iterator localIterator1 = ress.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      FortRes res = (FortRes)localIterator1.next();
      
      List<FortAcc> accs = FortAcc.dao.listByFortId(res.get("id").toString());
      localIterator2 = accs.iterator(); 
      /*continue;*/
      FortAcc acc = (FortAcc)localIterator2.next();
      if (acc.getTimestamp("pwd_update_time").getTime() < deadline) {
        try {
          FortServMgrDeamon.instance().changeAccPwd(res, acc.getStr("username"), pwd);
          logger.info("修改密码成功: " + res.getStr("fort_ip") + ": " + acc.getStr("username"));
        }
        catch (Exception e) {
          throw new TxDataException("修改密码失败: " + res.getStr("fort_ip") + ": " + 
            acc.getStr("username"));
        }
      }
    }
  }
}
