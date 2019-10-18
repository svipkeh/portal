package fort.utils;

import com.fort.common.Translation;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import common.web.excetpion.TxDataException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class FortServiceCaller
{
  public static String[] call(String netMode, String cmd, boolean record)
  {
    try
    {
      String urlStr = PropKit.use("constant.properties").get("ws.fort.url");
      Service service = new Service();
      Call call = (Call)service.createCall();
      URL url = new URL(urlStr);
      call.setTargetEndpointAddress(url);
      if (record) {
        call.setOperationName(new QName("http://www.trustmo.com", "callWithRecord"));
      } else {
        call.setOperationName(new QName("http://www.trustmo.com", "call"));
      }
      call.addParameter("netmode", XMLType.SOAP_STRING, ParameterMode.IN);
      call.addParameter("cmd", XMLType.SOAP_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.SOAP_STRING);
      String enCmd = Translation.encrypt(cmd);
      String s = (String)call.invoke(new Object[] { netMode, enCmd });
      return s.split(":");
    }
    catch (TxDataException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new TxDataException("调用接口出错");
    }
  }
  
  public static String[] delRecord(String session)
  {
    try
    {
      String urlStr = PropKit.use("constant.properties").get("ws.fort.url");
      Service service = new Service();
      Call call = (Call)service.createCall();
      URL url = new URL(urlStr);
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName("http://www.trustmo.com", "delRecord"));
      call.addParameter("session", XMLType.SOAP_STRING, ParameterMode.IN);
      call.setReturnType(XMLType.SOAP_STRING);
      String enCmd = Translation.encrypt(session);
      String s = (String)call.invoke(new Object[] { enCmd });
      return s.split(":");
    }
    catch (TxDataException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new TxDataException("调用接口出错");
    }
  }
}
