package ws.fort.deamon;

import java.util.Random;

public class FortKit
{
  private static final String num = "0123456789";
  private static final String charLower = "abcdefghijklmnopqrstuvwxyz";
  private static final String charUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  public static boolean isWeihuAcc(String name)
  {
    if (name.startsWith("tm")) {
      if (name.length() == 4) {
        try
        {
          Integer.parseInt(name.substring(2, 3));
          Integer.parseInt(name.substring(3));
          return true;
        }
        catch (Exception localException) {}
      }
    }
    return false;
  }
  
  public static String getRandomPassword()
  {
    StringBuffer sb = new StringBuffer();
    Random r = new Random();
    int sep = 2 + r.nextInt(6);
    sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(r.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
    for (int i = 0; i < sep; i++) {
      sb.append("abcdefghijklmnopqrstuvwxyz".charAt(r.nextInt("abcdefghijklmnopqrstuvwxyz".length())));
    }
    sb.append("@");
    for (int i = sep; i < 10; i++) {
      sb.append("0123456789".charAt(r.nextInt("0123456789".length())));
    }
    return sb.toString();
  }
}
