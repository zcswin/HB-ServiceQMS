package com.ww.boengongye.utils;

import org.bouncycastle.util.encoders.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Base64Utils {

    public static String encrypt(String datas){
        byte[] data = Base64.encode(datas.getBytes());
        return  new String(data);
    }

    public static String decodeBase64(String datas){
        byte[] bytes = Base64.decode(datas);
        return  new String(bytes);
    }

    /**
     * base64编码字符串转换为图片,并写入文件
     *
     * @param imgStr base64编码字符串
     * @param path   图片路径
     * @return
     */
    public static boolean base64StrToImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        try {
            // 使用 org.bouncycastle.util.encoders.Base64 进行解码
            byte[] b = Base64.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 文件夹不存在则自动创建
            File tempFile = new File(path);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//import sun.misc.BASE64Decoder;    
//    public static boolean base64StrToImage(String imgStr, String path) {
//        if (imgStr == null)
//            return false;
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            // 解密
//            byte[] b = decoder.decodeBuffer(imgStr);
//            // 处理数据
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {
//                    b[i] += 256;
//                }
//            }
//            //文件夹不存在则自动创建
//            File tempFile = new File(path);
//            if (!tempFile.getParentFile().exists()) {
//                tempFile.getParentFile().mkdirs();
//            }
//            OutputStream out = new FileOutputStream(tempFile);
//            out.write(b);
//            out.flush();
//            out.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    
//import java.util.Base64;    
//  public static boolean base64StrToImage(String imgStr, String path) {
//  if (imgStr == null)
//      return false;
//  try {
//      // 使用 Apache Commons Codec 进行解码
//      byte[] b = Base64.decodeBase64(imgStr);
//      // 处理数据
//      for (int i = 0; i < b.length; ++i) {
//          if (b[i] < 0) {
//              b[i] += 256;
//          }
//      }
//      //文件夹不存在则自动创建
//      File tempFile = new File(path);
//      if (!tempFile.getParentFile().exists()) {
//          tempFile.getParentFile().mkdirs();
//      }
//      OutputStream out = new FileOutputStream(tempFile);
//      out.write(b);
//      out.flush();
//      out.close();
//      return true;
//  } catch (Exception e) {
//      return false;
//  }
//}
}
