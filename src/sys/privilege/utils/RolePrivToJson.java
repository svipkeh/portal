package sys.privilege.utils;

import java.util.List;
import sys.privilege.model.RolePriv;

public class RolePrivToJson
{
  private static final String Y = "\"";
  private static final String M = ":";
  private static final String D = ",";
  private List<RolePriv> rolePrivileges = null;
  
  public RolePrivToJson(List<RolePriv> rolePrivileges)
  {
    this.rolePrivileges = rolePrivileges;
  }
  
  public String toJson()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if (this.rolePrivileges != null) {
      for (int i = 0; i < this.rolePrivileges.size(); i++)
      {
        parseOne(sb, (RolePriv)this.rolePrivileges.get(i));
        if (i != this.rolePrivileges.size() - 1) {
          sb.append(",");
        }
      }
    }
    sb.append("]");
    return sb.toString();
  }
  
  private void parseOne(StringBuffer sb, RolePriv rp)
  {
    sb.append("{");
    sb.append(getOneAttr(rp, "res_id") + ",");
    sb.append(getOneAttr(rp, "res_type"));
    sb.append("}");
  }
  
  private String getOneAttr(RolePriv obj, String attr)
  {
    return "\"" + attr + "\"" + ":" + "\"" + obj.get(attr) + "\"";
  }
}
