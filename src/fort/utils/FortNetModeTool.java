package fort.utils;

import com.jfinal.kit.Prop;
import common.enums.FortNetMode;
import common.jfinal.WebRootConfig;
import javax.servlet.http.HttpServletRequest;

public class FortNetModeTool
{
  public static String getNetModel(HttpServletRequest r)
  {
    String p = r.getRequestURL().toString();
    String wanIP = WebRootConfig.constant.get("fort.center.wan.ip");
    if (p.contains(wanIP)) {
      return FortNetMode.WAN.name();
    }
    return FortNetMode.LAN.name();
  }
}
