package sys.index.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserLockMap
{
  private static UserLockMap instance = new UserLockMap();
  private Map<String, Integer> map = new HashMap();
  private long latest = 0L;
  
  public static UserLockMap instance()
  {
    return instance;
  }
  
  public boolean allowLogin(String userName, String ip)
  {
    userName = userName + "_" + ip;
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
  
  public void recordLogin(String userName, boolean suc, String ip)
  {
    userName = userName + "_" + ip;
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
    Set<String> keys = this.map.keySet();
    for (String key : keys) {
      if (key.startsWith(userName)) {
        this.map.remove(key);
      }
    }
  }
}
