package com.ww.boengongye.utils;

import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelImg {

    public static Map<String, XSSFPictureData> getPictures(XSSFSheet xssfSheet){

        Map<String,XSSFPictureData> map=new HashMap<>();
        List<XSSFShape> list=xssfSheet.getDrawingPatriarch().getShapes();

        for (XSSFShape shape:list){

            XSSFPicture picture = (XSSFPicture) shape;
            XSSFClientAnchor xssfClientAnchor=(XSSFClientAnchor) picture.getAnchor();
            XSSFPictureData pdata = picture.getPictureData();
            // 行号-列号
            String key = xssfClientAnchor.getRow1() + "-" + xssfClientAnchor.getCol1();
           System.out.println("key数据:{}"+key);
            map.put(key, pdata);

        }

        return map;
    }



//    @Test
    public void test() throws IOException{
        String filename="classpath:file/org.xlsx";

        File file = ResourceUtils.getFile(filename);
        InputStream inputStream = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
        Map<String, XSSFPictureData> map=getPictures(xssfWorkbook.getSheetAt(0));
        String mapKey="3-15";//指定行和列
        XSSFPictureData xssfPictureData= map.get(mapKey);
        byte[] data =xssfPictureData.getData();
        FileOutputStream out = new FileOutputStream("/Users/test12.png");
        out.write(data);
        out.close();
    }


    public static void main(String[] args) throws IOException {

        String filename="D:\\boenFile\\testImg.xlsx";

        File file = ResourceUtils.getFile(filename);
        InputStream inputStream = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
        Map<String, XSSFPictureData> map=getPictures(xssfWorkbook.getSheetAt(0));
        String mapKey="1-5";//指定行和列
        XSSFPictureData xssfPictureData= map.get(mapKey);
        byte[] data =xssfPictureData.getData();
        FileOutputStream out = new FileOutputStream("D:\\boenFile/test12.png");
        out.write(data);
        out.close();
    }

}
