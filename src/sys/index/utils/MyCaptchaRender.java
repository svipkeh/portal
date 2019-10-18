package sys.index.utils;

import com.jfinal.core.Controller;
import com.jfinal.render.Render;
import common.utils.StringUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyCaptchaRender
  extends Render
{
  private static final String[] strArr = { "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", 
    "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y" };
  private static String randomCodeKey = "JFINAL_RENDER_RANDOM_CODE";
  private static boolean caseInsensitive = true;
  private int img_width = 60;
  private int img_height = 22;
  private int img_randNumber = 4;
  
  public MyCaptchaRender(int width, int height, int count, boolean isCaseInsensitive)
  {
    if ((width <= 0) || (height <= 0) || (count <= 0)) {
      throw new IllegalArgumentException("Image width or height or count must be > 0");
    }
    this.img_width = width;
    this.img_height = height;
    this.img_randNumber = count;
    caseInsensitive = isCaseInsensitive;
  }
  
  public void render()
  {
    BufferedImage image = new BufferedImage(this.img_width, this.img_height, 1);
    String vCode = drawGraphic(image);
    this.request.getSession().setAttribute(randomCodeKey, vCode.toUpperCase());
    this.response.setHeader("Pragma", "no-cache");
    this.response.setHeader("Cache-Control", "no-cache");
    this.response.setDateHeader("Expires", 0L);
    this.response.setContentType("image/jpeg");
    ServletOutputStream sos = null;
    try
    {
      sos = this.response.getOutputStream();
      ImageIO.write(image, "jpeg", sos);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if (sos != null) {
        try
        {
          sos.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  private String drawGraphic(BufferedImage image)
  {
    Graphics g = image.createGraphics();
    
    Random random = new Random();
    
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, this.img_width, this.img_height);
    
    g.setFont(new Font("Times New Roman", 0, 18));
    
    g.setColor(getRandColor(160, 200));
    for (int i = 0; i < 155; i++)
    {
      int x = random.nextInt(this.img_width);
      int y = random.nextInt(this.img_height);
      int xl = random.nextInt(12);
      int yl = random.nextInt(12);
      g.drawLine(x, y, x + xl, y + yl);
    }
    String sRand = "";
    for (int i = 0; i < this.img_randNumber; i++)
    {
      String rand = String.valueOf(strArr[random.nextInt(strArr.length)]);
      sRand = sRand + rand;
      
      g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
      
      g.drawString(rand, 13 * i + 6, 16);
    }
    g.dispose();
    return sRand;
  }
  
  private Color getRandColor(int fc, int bc)
  {
    Random random = new Random();
    if (fc > 255) {
      fc = 255;
    }
    if (bc > 255) {
      bc = 255;
    }
    int r = fc + random.nextInt(bc - fc);
    int g = fc + random.nextInt(bc - fc);
    int b = fc + random.nextInt(bc - fc);
    return new Color(r, g, b);
  }
  
  public static boolean validate(Controller controller, String inputRandomCode)
  {
    if (StringUtil.isBlank(inputRandomCode)) {
      return false;
    }
    try
    {
      if (caseInsensitive) {
        inputRandomCode = inputRandomCode.toUpperCase();
      }
      return inputRandomCode.equals(controller.getSessionAttr(randomCodeKey));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
}
