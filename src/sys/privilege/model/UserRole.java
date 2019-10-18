package sys.privilege.model;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public class UserRole
  extends Model<UserRole>
{
  private static final long serialVersionUID = 1L;
  public static final UserRole dao = new UserRole();
  
  public List<UserRole> getAllUserRole(String userId)
  {
    return find("select a.* from sys_user_role a where a.user_id=?", new Object[] { userId });
  }
}
