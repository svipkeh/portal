package sys.privilege.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SysLog
  extends Model<SysLog>
{
  private static final long serialVersionUID = 1L;
  public static final SysLog dao = new SysLog();
  
  public Page<SysLog> getAllSyslog(int pageCurrent, int pageSize, Map<String, String> qm)
  {
    String username = (String)qm.get("username");
    String action = (String)qm.get("action");
    String sourceIp = (String)qm.get("source_ip");
    String time1 = (String)qm.get("date1") + " " + (String)qm.get("time1");
    String time2 = (String)qm.get("date2") + " " + (String)qm.get("time2");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*, b.name_cn");
    sqlExceptSelect.append("from sys_logs a left join sys_user b on a.user_id=b.id where 1=1");
    if (!StringUtil.isBlank(username))
    {
      sqlExceptSelect.append(" and b.name_cn like ?");
      paramList.add("%" + username + "%");
    }
    if (!StringUtil.isBlank(action))
    {
      sqlExceptSelect.append(" and a.action like ?");
      paramList.add("%" + action + "%");
    }
    if (!StringUtil.isBlank(sourceIp))
    {
      sqlExceptSelect.append(" and a.source_ip = ?");
      paramList.add(sourceIp);
    }
    if (!StringUtil.isBlank(time1))
    {
      sqlExceptSelect.append(" and a.ex_time >= ?");
      paramList.add(time1);
    }
    if (!StringUtil.isBlank(time2))
    {
      sqlExceptSelect.append(" and a.ex_time <= ?");
      paramList.add(time2);
    }
    sqlExceptSelect.append("order by id desc");
    Page<SysLog> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getAllSyslog(pageCurrent - 1, pageSize, qm);
    }
    return page;
  }
}
