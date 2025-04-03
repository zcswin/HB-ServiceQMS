package com.ww.boengongye.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExportExcelUtil {

    public void expoerDataExcel(HttpServletResponse response, ArrayList titleKeyList, Map titleMap, List src_list) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = format.format(new Date());
        String xlsFile_name = name+".xlsx"; //输出xls文件名称
        //内存中只创建100个对象
        Workbook wb = new SXSSFWorkbook(100); //关键语句
        Sheet sheet = null; //工作表对象
        Row nRow = null; //行对象
        Cell nCell = null; //列对象
        int rowNo = 0; //总行号
        int pageRowNo = 0; //页行号
        for (int k = 0; k < src_list.size(); k++) {
            Map<String,Object> srcMap = (Map<String, Object>) src_list.get(k);
            //写入300000条后切换到下个工作表
            if (rowNo % 300000 == 0) {
                wb.createSheet("工作簿" + (rowNo / 300000));//创建新的sheet对象
                sheet = wb.getSheetAt(rowNo / 300000); //动态指定当前的工作表
                pageRowNo = 0; //新建了工作表,重置工作表的行号为0
                // -----------定义表头-----------
                nRow = sheet.createRow(pageRowNo++);
                // 列数 titleKeyList.size()
                for (int i = 0; i < titleKeyList.size(); i++) {
                    Cell cell_tem = nRow.createCell(i);
                    cell_tem.setCellValue((String) titleMap.get(titleKeyList.get(i)));
                }
            }
            rowNo++;
            // ---------------------------
            rowNo++;
            nRow = sheet.createRow(pageRowNo++); //新建行对象
            // 行，获取cell值
            for (int j = 0; j < titleKeyList.size(); j++) {
                nCell = nRow.createCell(j);
                if (srcMap.get(titleKeyList.get(j)) != null) {
                    nCell.setCellValue(srcMap.get(titleKeyList.get(j)).toString());
                } else {
                    nCell.setCellValue("");
                }
            }
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + xlsFile_name);
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();
        wb.write(response.getOutputStream());
        wb.close();
        outputStream.flush();
        outputStream.close();
    }

    public void downLoadExcelMould(HttpServletResponse response, String excelName) {
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(excelName + ".xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode(excelName, "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件输出流
        }
        return;
    }

    public void liableManMouldExcel(HttpServletResponse response) {
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("责任人模板.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode("责任人模板", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件输出流
        }
        return;
    }

    public void workmanshipMouldExcel(HttpServletResponse response) {
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("项目工艺模板.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode("项目工艺模板", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            ServletOutputStream out = null;
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件输出流
        }
        return;
    }
}