package fort;

import com.jfinal.config.Routes;
import fort.controller.AppController;
import fort.controller.FortAccController;
import fort.controller.FortResController;
import fort.controller.FortSessionRecordController;
import fort.controller.FortViewController;

public class Router
{
  public static void config(Routes me)
  {
    me.add("/fort/app", AppController.class);
    me.add("/fort/fortres", FortResController.class);
    me.add("/fort/fortacc", FortAccController.class);
    me.add("/fort/fortsessionrecord", FortSessionRecordController.class);
    me.add("/fort/fortview", FortViewController.class);
  }
}
