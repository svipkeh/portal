package portal.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountUser
  extends Model<AccountUser>
{
  private static final long serialVersionUID = 1L;
  public static final AccountUser dao = new AccountUser();
  
  public static long getAccountUserNum(int accountId)
  {
    return Db.queryLong("select count(*) from base_account_user where account_id=?", new Object[] { Integer.valueOf(accountId) }).longValue();
  }
  
  public void delByAccountAndUser(int accountId, String userId)
  {
    Db.update("delete from base_account_user where account_id=? and user_id=?", new Object[] { Integer.valueOf(accountId), userId });
  }
  
  public AccountUser getByAccountAndUser(int accountId, String userId)
  {
    return (AccountUser)findFirst("select * from base_account_user where account_id=? and user_id=?", new Object[] { Integer.valueOf(accountId), userId });
  }
  
  public Page<AccountUser> listPage(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String accid = (String)queryMap.get("accid");
    String name = (String)queryMap.get("name");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*, b.user_id");
    sqlExceptSelect.append(" from sys_user a left join");
    sqlExceptSelect.append(" (select user_id from base_account_user where account_id=?) b");
    sqlExceptSelect.append(" on a.id=b.user_id where a.name<>?");
    paramList.add(accid);
    paramList.add("admin");
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and (a.name like ? or a.name_cn like ?)");
      paramList.add("%" + name + "%");
      paramList.add("%" + name + "%");
    }
    sqlExceptSelect.append(" order by b.user_id desc, a.id");
    Page<AccountUser> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = listPage(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
