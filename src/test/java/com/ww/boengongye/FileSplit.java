package com.ww.boengongye;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liwei
 * @create 2023-10-20 15:26
 */
@SpringBootTest
public class FileSplit {

	@Test
	void fileSplit() {
//		try {
//			// 打开Excel文件
//			FileInputStream fis = new FileInputStream("C:\\Users\\LiWei\\Desktop\\伯恩-照\\10.20\\J10-1 量产报告 20231016.xlsx");
//			Workbook workbook = new XSSFWorkbook(fis);
//
//			// 循环处理每个工作表
//			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//				Sheet sheet = workbook.getSheetAt(i);
//
//				// 创建一个新工作簿
//				Workbook newWorkbook = new XSSFWorkbook();
//				newWorkbook.createSheet(sheet.getSheetName());
//
//				// 复制工作表数据
//				for (Row row : sheet) {
//					Row newRow = newWorkbook.getSheetAt(0).createRow(row.getRowNum());
//
//					for (Cell cell : row) {
//						Cell newCell = newRow.createCell(cell.getColumnIndex(), cell.getCellType());
//
//						switch (cell.getCellTypeEnum()) {
//							case STRING:
//								newCell.setCellValue(cell.getStringCellValue());
//								break;
//							case NUMERIC:
//								if (DateUtil.isCellDateFormatted(cell)) {
//									newCell.setCellValue(cell.getDateCellValue());
//								} else {
//									newCell.setCellValue(cell.getNumericCellValue());
//								}
//								break;
//							case BOOLEAN:
//								newCell.setCellValue(cell.getBooleanCellValue());
//								break;
//							case FORMULA:
//								newCell.setCellFormula(cell.getCellFormula());
//								break;
//							default:
//								newCell.setCellValue(cell.toString());
//						}
//					}
//				}
//
//				// 保存新工作簿到文件
//				FileOutputStream fos = new FileOutputStream("C:\\Users\\LiWei\\Desktop\\伯恩-照\\10.20\\" + sheet.getSheetName() + ".xlsx");
//				newWorkbook.write(fos);
//				fos.close();
//			}
//
//			// 关闭原始工作簿
//			fis.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
