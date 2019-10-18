package common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil
{
  public static void main(String[] args)
    throws Exception
  {
    zipFile("d:/2016.zip", new File[] { new File("d:/static"), new File("d:/biz.html") });
  }
  
  public static File zipFile(String zipFile, File... inFiles)
    throws IOException
  {
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
    for (File file : inFiles) {
      zipFile(file, zos, "");
    }
    zos.close();
    return new File(zipFile);
  }
  
  private static void zipFile(File inFile, ZipOutputStream zos, String dir)
    throws IOException
  {
    if (inFile.isDirectory())
    {
      File[] files = inFile.listFiles();
      for (File file : files) {
        if ("".equals(dir)) {
          zipFile(file, zos, inFile.getName());
        } else {
          zipFile(file, zos, dir + "/" + inFile.getName());
        }
      }
    }
    else
    {
      String entryName = null;
      if (!"".equals(dir)) {
        entryName = dir + "/" + inFile.getName();
      } else {
        entryName = inFile.getName();
      }
      ZipEntry entry = new ZipEntry(entryName);
      zos.putNextEntry(entry);
      Object is = new FileInputStream(inFile);
      int len = 0;
      while ((len = ((InputStream)is).read()) != -1) {
        zos.write(len);
      }
      ((InputStream)is).close();
    }
  }
}
