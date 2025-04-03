package com.ww.boengongye.utils;

/**
 * @author liwei
 * @create 2023-10-23 13:09
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExport {
	public static void main(String[] args) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Sheet1");

			Row row0 = sheet.createRow(0);
			Cell cellA0 = row0.createCell(0);
			cellA0.setCellValue("Merged Cells");
			sheet.addMergedRegion(new CellRangeAddress(0, 10, 0, 3));

			Row row1 = sheet.createRow(1);
			Cell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Data 1");
			Cell cellB1 = row1.createCell(1);
			cellB1.setCellValue("Data 2");
			Cell cellC1 = row1.createCell(2);
			cellC1.setCellValue("Data 3");
			Cell cellD1 = row1.createCell(3);
			cellD1.setCellValue("Data 4");

//			for (int i = 1; i <= 10; i++) {
//				Row row = sheet.createRow(i);
//				Cell cellA1 = row.createCell(0);
//				cellA1.setCellValue("Data 1");
//				Cell cellB1 = row.createCell(1);
//				cellB1.setCellValue("Data 2");
//				Cell cellC1 = row.createCell(2);
//				cellC1.setCellValue("Data 3");
//				Cell cellD1 = row.createCell(3);
//				cellD1.setCellValue("Data 4");
//			}

			try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\LiWei\\Documents\\测试文件夹\\workbook5.xlsx")) {
				workbook.write(fileOut);
			}

			System.out.println("Excel file has been generated!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

