package sys.index.utils;

import java.util.HashMap;
import java.util.Map;

public class UserLockMap20190313
{
  private static UserLockMap20190313 instance = new UserLockMap20190313();
  private Map<String, Integer> map = new HashMap();
  private long latest = 0L;
  
  public static UserLockMap20190313 instance()
  {
    return instance;
  }
  
  public boolean allowLogin(String userName)
  {
    refresh();
    Integer num = (Integer)this.map.get(userName);
    if ((num != null) && (num.intValue() > 5)) {
      return false;
    }
    return true;
  }
  
  private void refresh()
  {
    if (this.latest == 0L) {
      this.latest = System.currentTimeMillis();
    }
    long now = System.currentTimeMillis();
    if (now - this.latest > 1800000L)
    {
      this.map.clear();
      this.latest = now;
    }
  }
  
  public void recordLogin(String userName, boolean suc)
  {
    if (suc)
    {
      this.map.remove(userName);
    }
    else
    {
      Integer num = (Integer)this.map.get(userName);
      if (num != null) {
        this.map.put(userName, Integer.valueOf(num.intValue() + 1));
      } else {
        this.map.put(userName, Integer.valueOf(1));
      }
    }
  }
  
  public void unlock(String userName)
  {
    this.map.remove(userName);
  }
}
