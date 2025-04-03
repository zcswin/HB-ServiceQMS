package com.ww.boengongye.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Pattern;

public class CommunalUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommunalUtils.class);
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //MultipartFile转File
    public static void inputStreamToFile(InputStream ins, File file, String folderPath, String folderPath2, String folderPath3) {
        try {

            File folder = new File(folderPath);
            if (!folder.exists()) {//检查文件夹是否存在
                folder.mkdir();//不存在则创建
            }

            File folder2 = new File(folderPath2);
            if (!folder2.exists()) {//检查文件夹是否存在
                folder2.mkdir();//不存在则创建
            }
            File folder3 = new File(folderPath3);
            if (!folder3.exists()) {//检查文件夹是否存在
                folder3.mkdir();//不存在则创建
            }
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            logger.error("MultipartFile转File", e);
        }
    }


//    public static void main(String[] args) {
//        String ss="20210305002";
//
//        System.out.println(Integer.parseInt(ss));
//    }




    /*
     * 将父类所有的属性COPY到子类中。
     * 类定义中child一定要extends father；
     * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate
     */
    public static final void fatherToChild (Object father,Object child){
//        if(!(child.getClass().getSuperclass()==father.getClass())){
//            System.err.println("child不是father的子类");
//        }
        Class fatherClass= father.getClass();
        Field ff[]= fatherClass.getDeclaredFields();
        for(int i=0;i<ff.length;i++){
            Field f=ff[i];//取出每一个属性，如deleteDate
            Class type=f.getType();
            try {
                Method m = fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate
                Object obj=m.invoke(father);//取出属性值
                f.set(child,obj);
            } catch (SecurityException e) {
                logger.error("将父类所有的属性COPY到子类中", e);
            } catch (IllegalArgumentException e) {
                logger.error("将父类所有的属性COPY到子类中", e);
            } catch (NoSuchMethodException e) {
                logger.error("将父类所有的属性COPY到子类中", e);
            } catch (IllegalAccessException e) {
                logger.error("将父类所有的属性COPY到子类中", e);
            } catch (InvocationTargetException e) {
                logger.error("将父类所有的属性COPY到子类中", e);
            }
        }
    }
    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public static String upperHeadChar(String in){
        String head=in.substring(0,1);
        String out=head.toUpperCase()+in.substring(1,in.length());
        return out;
    }

    public static File generateFileByPathAndFileName(String enviroment, String customPath, String originalFilename) {
        File fl = new File(enviroment + customPath);
        if (!fl.exists()){
            fl.mkdirs();
        }
        return new File(enviroment + customPath + originalFilename);
    }

    public static void inputStreamToFile2(InputStream ins, File f) {
        try {
            OutputStream os = new FileOutputStream(f);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            logger.error("MultipartFile转File", e);
        }
    }

    /**
     * 负数转正数
     * @param in
     * @return
     */
    public static Double changePositive (Double in){
        if(in<0){
            in=in*-1;
        }
        return in;
    }

    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static String formatNumberToTwoDecimalPlaces(double number) {
        // 创建一个 DecimalFormat 对象，指定小数点后保留两位
        DecimalFormat df = new DecimalFormat("#.##");

        // 使用 format 方法格式化数字
        String formattedNumber = df.format(number);

        // 返回格式化后的字符串
        return formattedNumber;
    }
}
