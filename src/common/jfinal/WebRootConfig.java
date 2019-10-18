package common.jfinal;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import common.jfinal.interceptor.ExceptionInterceptor;
import common.jfinal.interceptor.LogInterceptor;
import common.jfinal.interceptor.PrivilegeInterceptor;
import common.jfinal.routes.RoutesContext;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Set;
import ws.fort.deamon.AutoUpdatePwd;
import ws.fort.service.FortService;

public class WebRootConfig
  extends JFinalConfig
{
  public static Prop constant = null;
  
  public void configConstant(Constants me)
  {
    constant = PropKit.use("constant.properties");
    me.setDevMode(constant.getBoolean("base.debug").booleanValue());
    me.setViewType(ViewType.JSP);
    me.setBaseViewPath("/WEB-INF/jsp");
    me.setError404View("/WEB-INF/jsp/common/404.jsp");
    
    Set<Object> keys = constant.getProperties().keySet();
    for (Object key : keys) {
      if (key.toString().endsWith(".directory")) {
        checkDirectory(constant.get(key.toString()));
      }
    }
    System.setProperty("LOGDIR", constant.get("base.log4j.directory"));
  }
  
  private void checkDirectory(String directory)
  {
    if ((directory != null) && (!"".equals(directory)))
    {
      File f = new File(directory);
      if (!f.exists())
      {
        f.mkdirs();
        System.out.println("create directory: " + directory);
      }
    }
  }
  
  public void configHandler(Handlers me) {}
  
  public void configInterceptor(Interceptors me)
  {
    me.add(new ExceptionInterceptor());
    me.add(new PrivilegeInterceptor());
    if (constant.getBoolean("base.log.enable").booleanValue()) {
      me.add(new LogInterceptor());
    }
  }
  
  public void configPlugin(Plugins me)
  {
    String url = PropKit.use("db.properties").get("jdbc.url");
    String connectStr = "jdbc:mysql://" + url + "?characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull";
    String user = PropKit.use("db.properties").get("jdbc.user");
    String pwd = PropKit.use("db.properties").get("jdbc.pwd");
    
    C3p0Plugin c3p0Plugin = new C3p0Plugin(connectStr, user, pwd);
    c3p0Plugin.setInitialPoolSize(1);
    c3p0Plugin.setMinPoolSize(1);
    c3p0Plugin.setAcquireIncrement(1);
    c3p0Plugin.setMaxPoolSize(10);
    ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
    arp.setDialect(new MysqlDialect());
    arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
    arp.setShowSql(constant.getBoolean("base.debug").booleanValue());
    me.add(c3p0Plugin);
    me.add(arp);
    
    common.Mapping.config(arp);
    sys.Mapping.config(arp);
    fort.Mapping.config(arp);
    portal.Mapping.config(arp);
  }
  
  @Override
  public void configRoute(Routes me)
  {
    common.Router.config(me);
    sys.Router.config(me);
    fort.Router.config(me);
    portal.Router.config(me);
    RoutesContext.instance().init(me);
  }
  
  public void afterJFinalStart()
  {
    
    if (constant.getBoolean("fort.auto.update.enable").booleanValue()) {
      new Thread(new AutoUpdatePwd()).start();
    }
  }
}
