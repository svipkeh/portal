package ws.fort.deamon;

import com.fort.servmgr.common.SocketChannel;
import com.google.gson.Gson;
import com.jfinal.kit.Prop;
import com.trustmo.encrypt.En3DESCoder;
import common.enums.FortAccStatus;
import common.enums.FortResStatus;
import common.enums.FortSessionExStatus;
import common.enums.YesNo;
import common.jfinal.WebRootConfig;
import common.web.excetpion.TxDataException;
import fort.model.FortAcc;
import fort.model.FortRes;
import fort.model.FortSessionRecord;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class FortServMgrDeamon
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(FortServMgrDeamon.class);
  private static FortServMgrDeamon instance = new FortServMgrDeamon();
  private boolean running = false;
  private Thread thread;
  
  public static FortServMgrDeamon instance()
  {
    return instance;
  }
  
  public void run()
  {
    logger.info("图堡管理服务启动...");
    while (this.running)
    {
      doManage();
      sleep(10000);
    }
    logger.info("图堡管理服务停止...");
  }
  
  private void doManage()
  {
    List<FortRes> forts = FortRes.dao.listAllEnabled();
    for (FortRes fort : forts)
    {
      String ip = fort.getStr("fort_ip");
      try
      {
        List<FortAcc> accList = FortAcc.dao.listByFortId(fort.get("id").toString());
        if (accList.size() != WebRootConfig.constant.getInt("fort.weihu.acc.num").intValue())
        {
          for (FortAcc acc : accList) {
            acc.delete();
          }
          initAccs(fort);
          accList = FortAcc.dao.listByFortId(fort.get("id").toString());
        }
        refreshAccStatus(fort, accList);
        fort.set("running_status", FortResStatus.ZHENGCHANG.name());
        fort.set("servmgr_syn_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        fort.update();
      }
      catch (Exception e)
      {
        fort.set("running_status", FortResStatus.SHILIAN.name());
        fort.update();
        logger.error("fort " + ip + " deamon error", e);
      }
    }
  }
  
  public void initAccs(FortRes fort)
    throws Exception
  {
    String ip = fort.getStr("fort_ip");
    String port = fort.getStr("servmgr_port");
    int num = WebRootConfig.constant.getInt("fort.weihu.acc.num").intValue();
    String password = FortKit.getRandomPassword();
    for (int i = 1; i <= num; i++)
    {
      String name = "tm" + (i < 10 ? "0" + i : Integer.valueOf(i));
      SocketChannel sc = new SocketChannel(ip, port);
      try
      {
        sc.init();
        sc.write("initAcc|" + name + "|" + password);
        String back = sc.read();
        if (back.startsWith("1:"))
        {
          logger.info("create acc:" + name);
          FortAcc acc = new FortAcc();
          acc.set("fort_id", fort.getInt("id"));
          acc.set("username", name);
          acc.set("password", En3DESCoder.encrypt(password));
          acc.set("pwd_update_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
          acc.set("s_session_name", "");
          acc.set("s_session_id", "");
          acc.set("s_status", FortAccStatus.NOTUSE.name());
          acc.set("s_rest_time", "");
          acc.set("s_login_time", "");
          acc.save();
        }
        else
        {
          throw new Exception("fort " + ip + " can not init acc");
        }
      }
      finally
      {
        sc.close();
      }
      sc.close();
    }
  }
  
  public void refreshAccStatus(FortRes fort, List<FortAcc> accList)
    throws Exception
  {
    String ip = fort.getStr("fort_ip");
    String port = fort.getStr("servmgr_port");
    SocketChannel sc = new SocketChannel(ip, port);
    try
    {
      sc.init();
      sc.write("listAccStatus");
      String back = sc.read();
      if (back.startsWith("1:"))
      {
        List<FortAcc> backAccs = new LinkedList();
        List list = (List)new Gson().fromJson(back.substring(2), List.class);
        for (Object o : list)
        {
          Map m = (Map)o;
          String name = m.get("name").toString();
          String sessionName = m.get("sessionName").toString();
          String sessionId = m.get("sessionId").toString();
          String status = m.get("status").toString();
          String restTime = m.get("restTime").toString();
          String loginTime = m.get("loginTime").toString();
          if (FortKit.isWeihuAcc(name))
          {
            logger.info(name + " status: " + status);
            FortAcc acc = new FortAcc();
            acc.set("username", name);
            acc.set("s_session_name", sessionName);
            acc.set("s_session_id", sessionId);
            acc.set("s_status", "断开".equals(status) ? FortAccStatus.DISCONNECTED.name() : 
              FortAccStatus.CONNECTED.name());
            acc.set("s_rest_time", restTime);
            acc.set("s_login_time", loginTime);
            backAccs.add(acc);
          }
        }
        synAccStatus(accList, backAccs);
      }
      else
      {
        throw new Exception("fort " + ip + " can not refresh acc status");
      }
    }
    finally
    {
      sc.close();
    }
    sc.close();
  }
  
  private synchronized void synAccStatus(List<FortAcc> accList, List<FortAcc> backAccs)
    throws Exception
  {
    for (FortAcc dbAcc : accList)
    {
      boolean match = false;
      for (FortAcc backAcc : backAccs) {
        if (backAcc.getStr("username").equals(dbAcc.getStr("username")))
        {
          if (!backAcc.getStr("s_status").equals(dbAcc.getStr("s_status")))
          {
            dbAcc.set("s_session_name", backAcc.getStr("s_session_name"));
            dbAcc.set("s_session_id", backAcc.getStr("s_session_id"));
            dbAcc.set("s_status", backAcc.getStr("s_status"));
            dbAcc.set("s_rest_time", backAcc.getStr("s_rest_time"));
            dbAcc.set("s_login_time", backAcc.getStr("s_login_time"));
            dbAcc.update();
            changeSessionStatus(dbAcc.getStr("assigned_session_id"), FortSessionExStatus.RUNNING.name());
          }
          match = true;
          break;
        }
      }
      if (!match)
      {
        boolean goNotUse = false;
        if (!FortAccStatus.NOTUSE.name().equals(dbAcc.getStr("s_status"))) {
          if (FortAccStatus.ASSIGNED.name().equals(dbAcc.getStr("s_status")))
          {
            Date date = dbAcc.getDate("assigned_time");
            long sep = System.currentTimeMillis() - date.getTime();
            if (sep > 120000L)
            {
              goNotUse = true;
              changeSessionStatus(dbAcc.getStr("assigned_session_id"), FortSessionExStatus.ERROR.name());
            }
          }
          else if (FortAccStatus.CONNECTED.name().equals(dbAcc.getStr("s_status")))
          {
            goNotUse = true;
            changeSessionStatus(dbAcc.getStr("assigned_session_id"), FortSessionExStatus.OK.name());
          }
          else if (FortAccStatus.DISCONNECTED.name().equals(dbAcc.getStr("s_status")))
          {
            goNotUse = true;
            changeSessionStatus(dbAcc.getStr("assigned_session_id"), FortSessionExStatus.OK.name());
          }
        }
        if (goNotUse)
        {
          dbAcc.set("s_session_name", "");
          dbAcc.set("s_session_id", "");
          dbAcc.set("s_status", FortAccStatus.NOTUSE.name());
          dbAcc.set("s_rest_time", "");
          dbAcc.set("s_login_time", "");
          dbAcc.update();
        }
      }
    }
  }
  
  private void changeSessionStatus(String sessionId, String sessionStatus)
  {
    FortSessionRecord session = FortSessionRecord.dao.getBySessionId(sessionId);
    if (session != null)
    {
      session.set("ex_status", sessionStatus);
      session.update();
    }
  }
  
  public synchronized void changeAccPwd(FortRes fort, String username, String password)
    throws Exception
  {
    String ip = fort.getStr("fort_ip");
    String port = fort.getStr("servmgr_port");
    SocketChannel sc = new SocketChannel(ip, port);
    try
    {
      FortAcc acc = FortAcc.dao.getByFortIdAndAccName(fort.get("id").toString(), username);
      if (acc != null)
      {
        sc.init();
        sc.write("changePwd|" + username + "|" + password);
        String back = sc.read();
        if (back.startsWith("1:"))
        {
          acc.set("password", En3DESCoder.encrypt(password));
          acc.set("pwd_update_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
          acc.update();
        }
        else
        {
          throw new Exception("fort " + ip + " can not change acc password");
        }
      }
      else
      {
        throw new Exception("fort " + ip + " can do not have acc " + username);
      }
    }
    finally
    {
      sc.close();
    }
    sc.close();
  }
  
  public synchronized void closeSession(FortRes fort, String sessionId)
    throws Exception
  {
    String ip = fort.getStr("fort_ip");
    String port = fort.getStr("servmgr_port");
    SocketChannel sc = new SocketChannel(ip, port);
    try
    {
      sc.init();
      sc.write("closeSession|" + sessionId);
      String back = sc.read();
      if (!back.startsWith("1:")) {
        throw new Exception("fort " + ip + " can not change acc password");
      }
    }
    finally
    {
      sc.close();
    }
    sc.close();
  }
  
  public synchronized FortAcc assignAcc(String sessionId, String fortId)
    throws Exception
  {
    List<FortAcc> accs = null;
    if ((fortId != null) && (!"".equals(fortId))) {
      accs = FortAcc.dao.assign(fortId);
    } else {
      accs = FortAcc.dao.assign();
    }
    if (accs.size() > 0)
    {
      Random r = new Random();
      FortAcc acc = (FortAcc)accs.get(r.nextInt(accs.size()));
      acc.set("assigned_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
      acc.set("assigned_session_id", sessionId);
      acc.set("s_status", FortAccStatus.ASSIGNED.name());
      acc.update();
      return acc;
    }
    throw new TxDataException("未找到可用图堡账号");
  }
  
  public synchronized void delRecord(String recordId)
    throws Exception
  {
    FortSessionRecord record = FortSessionRecord.dao.getBySessionId(recordId);
    if (!YesNo.YES.name().equals(record.getStr("is_record"))) {
      throw new TxDataException("该会话未录像(" + recordId + ")");
    }
    if (!FortSessionExStatus.OK.name().equals(record.getStr("ex_status"))) {
      throw new TxDataException("该会话尚未执行结束(" + recordId + ")");
    }
    FortRes fort = (FortRes)FortRes.dao.findById(record.get("fort_id").toString());
    String ip = fort.getStr("fort_ip");
    String port = fort.getStr("servmgr_port");
    SocketChannel sc = new SocketChannel(ip, port);
    try
    {
      sc.init();
      sc.write("deleteRecord|" + recordId);
      String back = sc.read();
      if (back.startsWith("1:"))
      {
        record.set("is_record", YesNo.NO.name());
        record.update();
      }
      else
      {
        throw new TxDataException("删除录像失败(" + recordId + ")");
      }
    }
    finally
    {
      sc.close();
    }
    sc.close();
  }
  
  public void startService()
    throws Exception
  {
    if (this.running)
    {
      logger.info("启动失败，该线程已启动");
      return;
    }
    if (this.thread != null) {
      this.thread.join(0L);
    }
    this.running = true;
    this.thread = new Thread(instance());
    this.thread.start();
  }
  
  public void stopService()
    throws Exception
  {
    this.running = false;
  }
  
  public boolean isRunning()
  {
    return this.running;
  }
  
  private void sleep(int i)
  {
    try
    {
      Thread.sleep(i);
    }
    catch (InterruptedException localInterruptedException) {}
  }
}
