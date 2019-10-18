package ws.fort.service;

import com.fort.common.Translation;
import com.jfinal.kit.Prop;
import com.sun.net.httpserver.HttpExchange;
import common.jfinal.WebRootConfig;
import common.web.excetpion.TxDataException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import ws.fort.deamon.FortServMgrDeamon;

@WebService(targetNamespace="http://www.trustmo.com", serviceName="fort", name="fort")
public class FortService
{
  private static final Logger logger = Logger.getLogger(FortService.class);
  @Resource
  private WebServiceContext wsContext;
  
  @WebMethod(operationName="call")
  public String call(@WebParam(name="netmode") String netmode, @WebParam(name="cmd") String cmd)
  {
    return exec(netmode, cmd, false);
  }
  
  @WebMethod(operationName="callWithRecord")
  public String callWithRecord(@WebParam(name="netmode") String netmode, @WebParam(name="cmd") String cmd)
  {
    return exec(netmode, cmd, true);
  }
  
  private String exec(String netmode, String cmd, boolean record)
  {
    String back = "";
    

    
    /**
     * 类型转换  long to string
     * String sessionid = getSessionId();
     */

    String sessionid = String.valueOf(getSessionId());
    try
    {
      String ip = getClientIp();
      logger.info("ws fort request from:" + ip + ":" + netmode + ":data:" + cmd);
      validateIp(ip);
      cmd = Translation.decrypt(cmd);
      back = "1:" + sessionid + ":" + FortProcessor.process(netmode, record, sessionid, cmd);
    }
    catch (TxDataException e)
    {
      back = "0:" + sessionid + ":" + e.getMessage();
    }
    catch (Exception e)
    {
      logger.error("", e);
      back = "0:" + sessionid + ":" + "程序处理错误";
    }
    logger.info("ws fort response length:" + back.length() + ":data:" + back);
    return back;
  }
  
  @WebMethod(operationName="delRecord")
  public String delRecord(@WebParam(name="session") String session)
  {
    String back = "";
    try
    {
      String ip = getClientIp();
      logger.info("ws fort request from:" + ip + ":delete record:" + session);
      validateIp(ip);
      session = Translation.decrypt(session);
      if (!FortServMgrDeamon.instance().isRunning()) {
        throw new TxDataException("堡垒机服务未运行");
      }
      FortServMgrDeamon.instance().delRecord(session);
      back = "1:删除成功";
    }
    catch (TxDataException e)
    {
      back = "0:" + e.getMessage();
    }
    catch (Exception e)
    {
      logger.error("", e);
      back = "0:程序处理错误";
    }
    logger.info("ws fort response length:" + back.length() + ":data:" + back);
    return back;
  }
  
  private static synchronized Long getSessionId()
  {
    try
    {
      Thread.sleep(100L);
    }
    catch (Exception localException) {}
    return System.currentTimeMillis();
  }
  
  private String getClientIp()
  {
    MessageContext mc = this.wsContext.getMessageContext();
    HttpExchange exchange = (HttpExchange)mc.get("com.sun.xml.internal.ws.http.exchange");
    return exchange.getRemoteAddress().getAddress().getHostAddress();
  }
  
  private void validateIp(String clientIP)
  {
    boolean ok = false;
    String ips = WebRootConfig.constant.get("ws.fort.legal.ips");
    if ((ips != null) && (!"".equals(ips)))
    {
      String[] ipList = ips.split(";");
      for (String ip : ipList) {
        if ((ip != null) && (ip.equals(clientIP)))
        {
          ok = true;
          break;
        }
      }
    }
    else
    {
      ok = true;
    }
    if (!ok) {
      throw new TxDataException("非法访问源");
    }
  }
  
  public static void start()
  {
    try
    {
      String url = WebRootConfig.constant.get("ws.fort.url");
      Endpoint.publish(url, new FortService());
      logger.info("Fort Service 发布成功: " + url);
    }
    catch (Exception e)
    {
      logger.warn("", e);
    }
  }
}
