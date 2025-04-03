package com.ww.boengongye.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void main(String[] args) throws IOException {
        String folderPath = "D:\\WeChatFile\\WeChat Files\\wxid_3t9euh9yfmf022\\FileStorage\\File\\2023-11\\数据上传\\数据上传\\每日更新数据11_6、7\\LEAD\\11-06白班";
        String suffix = ".csv";
        List<File> files = searchFiles(folderPath, suffix);
        int i = 1;
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            String csvFile = file.getAbsolutePath();
            String excelFile = "D:\\desktop\\excel数据lead\\" + i++ + ".xlsx";
            convert(csvFile, excelFile);
        }
        //int totalLines = countTotalLines(files);
        //System.out.println("Total lines of code: " + totalLines);
        //String csvFile = "D:\\WeChatFile\\WeChat Files\\wxid_3t9euh9yfmf022\\FileStorage\\File\\2023-10\\数据更新\\LEAD\\9-25白班\\10-4夜班\\C27-9307-黄金线-MP量产-5313pcs\\2023-10-04\\AWhiteProduction_Blue.csv";
        //String excelFile = "D:\\WeChatFile\\WeChat Files\\wxid_3t9euh9yfmf022\\FileStorage\\File\\2023-10\\数据更新\\LEAD\\9-25白班\\10-4夜班\\C27-9307-黄金线-MP量产-5313pcs\\2023-10-04\\AWhiteProduction_Blue.xlsx";

        //convert(csvFile, excelFile);
    }

    public static List<File> searchFiles(String folderPath, String suffix) {
        List<File> result = new ArrayList<>();
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(searchFiles(file.getAbsolutePath(), suffix));
                } else {
                    if (file.getName().endsWith(suffix)) {
                        result.add(file);
                    }
                }
            }
        }
        return result;
    }

    public static int countLines(File file) throws IOException {
        int count = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            count++;
        }
        br.close();
        return count;
    }

    public static int countTotalLines(List<File> files) throws IOException {
        int totalLines = 0;
        for (File file : files) {
            totalLines += countLines(file);
        }
        return totalLines;
    }

    // 读取CSV文件并解析数据
    public static List<String[]> csvRead(String csvFile) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // 创建Excel工作簿并写入数据
    public static void excelWrite(List<String[]> data, String excelFile) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            int rowNum = 0;
            for (String[] rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将CSV转换为Excel
    public static void convert(String csvFile, String excelFile) {
        //String csvFile = "D:\\WeChatFile\\WeChat Files\\wxid_3t9euh9yfmf022\\FileStorage\\File\\2023-10\\数据更新\\LEAD\\9-25白班\\10-4夜班\\C27-9307-黄金线-MP量产-5313pcs\\2023-10-04\\AWhiteProduction_Blue.csv";
        //String excelFile = "D:\\WeChatFile\\WeChat Files\\wxid_3t9euh9yfmf022\\FileStorage\\File\\2023-10\\数据更新\\LEAD\\9-25白班\\10-4夜班\\C27-9307-黄金线-MP量产-5313pcs\\2023-10-04\\AWhiteProduction_Blue.xlsx";

        List<String[]> data = csvRead(csvFile);
        excelWrite(data, excelFile);

        System.out.println("CSV to Excel conversion completed!");
    }
}