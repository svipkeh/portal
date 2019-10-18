package fort.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.enums.FortAccStatus;
import common.enums.FortResStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FortAcc
  extends Model<FortAcc>
{
  private static final long serialVersionUID = 1L;
  public static final FortAcc dao = new FortAcc();
  
  public void deletByFortId(String fortId)
  {
    Db.update("delete from fort_acc where fort_id=?", new Object[] { fortId });
  }
  
  public List<FortAcc> listByFortId(String fortId)
  {
    return find("select * from fort_acc where fort_id=?", new Object[] { fortId });
  }
  
  public FortAcc getByFortIdAndAccName(String fortId, String accName)
  {
    return (FortAcc)findFirst("select * from fort_acc where fort_id=? and username=?", new Object[] { fortId, accName });
  }
  
  public List<FortAcc> assign(String fortId)
  {
    return find(
      "select a.*, b.fort_ip, b.fort_ip_wan, b.mstsc_port from fort_acc a, fort_res b where a.fort_id=b.id and b.data_status=? and b.running_status=? and a.fort_id=? and a.s_status=?", new Object[] {
      DataStatus.ENABLED.name(), FortResStatus.ZHENGCHANG.name(), fortId, FortAccStatus.NOTUSE.name() });
  }
  
  public List<FortAcc> assign()
  {
    return find(
      "select a.*, b.fort_ip, b.fort_ip_wan, b.mstsc_port from fort_acc a, fort_res b where a.fort_id=b.id and b.data_status=? and b.running_status=? and a.s_status=?", new Object[] {
      DataStatus.ENABLED.name(), FortResStatus.ZHENGCHANG.name(), FortAccStatus.NOTUSE.name() });
  }
  
  public Page<FortAcc> getPage(int pageCurrent, int pageSize, Map<String, String> qm)
  {
    String fort_ip = (String)qm.get("fort_ip");
    String status = (String)qm.get("status");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*, b.fort_ip, b.fort_ip_wan, b.running_status, b.servmgr_syn_time");
    sqlExceptSelect.append("from fort_acc a, fort_res b where a.fort_id=b.id");
    if (!StringUtil.isBlank(fort_ip))
    {
      sqlExceptSelect.append(" and (b.fort_ip like ? or b.fort_ip_wan like ?)");
      paramList.add("%" + fort_ip + "%");
      paramList.add("%" + fort_ip + "%");
    }
    if (!StringUtil.isBlank(status))
    {
      sqlExceptSelect.append(" and a.s_status=?");
      paramList.add(status);
    }
    Page<FortAcc> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getPage(pageCurrent - 1, pageSize, qm);
    }
    return page;
  }
}
