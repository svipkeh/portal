package fort.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FortSessionRecord
  extends Model<FortSessionRecord>
{
  private static final long serialVersionUID = 1L;
  public static final FortSessionRecord dao = new FortSessionRecord();
  
  public FortSessionRecord getBySessionId(String sessionid)
  {
    return (FortSessionRecord)findFirst("select * from fort_session_record a where a.session_id=?", new Object[] { sessionid });
  }
  
  public Page<FortSessionRecord> getPage(int pageCurrent, int pageSize, Map<String, String> qm)
  {
    String session_id = (String)qm.get("session_id");
    String is_record = (String)qm.get("is_record");
    String ex_status = (String)qm.get("ex_status");
    String time1 = (String)qm.get("date1") + " " + (String)qm.get("time1");
    String time2 = (String)qm.get("date2") + " " + (String)qm.get("time2");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*, b.fort_ip");
    sqlExceptSelect.append("from fort_session_record a, fort_res b where a.fort_id=b.id");
    if (!StringUtil.isBlank(session_id))
    {
      sqlExceptSelect.append(" and a.session_id like ?");
      paramList.add("%" + session_id + "%");
    }
    if (!StringUtil.isBlank(is_record))
    {
      sqlExceptSelect.append(" and a.is_record=?");
      paramList.add(is_record);
    }
    if (!StringUtil.isBlank(ex_status))
    {
      sqlExceptSelect.append(" and a.ex_status=?");
      paramList.add(ex_status);
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
    sqlExceptSelect.append(" order by a.ex_time desc");
    Page<FortSessionRecord> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getPage(pageCurrent - 1, pageSize, qm);
    }
    return page;
  }
}
