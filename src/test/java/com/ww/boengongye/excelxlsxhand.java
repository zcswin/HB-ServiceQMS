package com.ww.boengongye;

import com.ww.boengongye.entity.EquipmentMaintenance;
import com.ww.boengongye.service.EquipmentMaintenanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @autor 96901
 * @date 2025/1/11
 */

@SpringBootTest
@Slf4j
public class excelxlsxhand {



    // @Autowired
    // private EquipmentMaintenanceService equipmentMaintenanceService;
    //
    // @Test
    // public void readExcelAndConvertToEntityList() {
    //     List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();
    //     FileInputStream file = null;
    //     Workbook workbook = null;
    //     try {
    //         // 替换为你实际的Excel文件路径
    //         file = new FileInputStream(new File("D:\\excel\\bbb.xlsx"));
    //         workbook = new XSSFWorkbook(file); // 修改为 XSSFWorkbook
    //         // 假设读取第一个Sheet页，可根据实际情况调整
    //         Sheet sheet = workbook.getSheetAt(0);
    //
    //         // 限制只扫描前55行数据（索引从0开始，所以小于55）
    //         int maxRowIndex = Math.min(50, sheet.getLastRowNum());
    //         for (int rowIndex = 3; rowIndex <= maxRowIndex; rowIndex++) {
    //             Row row = sheet.getRow(rowIndex);
    //             if (row != null) {
    //                 EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();
    //
    //
    //                  String trigger = getStringCellValue(row.getCell(0));
    //
    //                  System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的trigger：" + trigger);
    //
    //
    //
    //
    //
    //
    //                 // 手动获取单元格值并设置到实体对象中
    //                 String process = getStringCellValue(row.getCell(1));
    //                 equipmentMaintenance.setProcess(process);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的process：" + process);
    //
    //
    //
    //
    //
    //
    //
    //
    //                 String machineNumber = getStringCellValue(row.getCell(2));
    //                 equipmentMaintenance.setMachine_number(machineNumber);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的machineNumber：" + machineNumber);
    //
    //                 String problemDetail = getStringCellValue(row.getCell(3));
    //                 equipmentMaintenance.setProblem_detail(problemDetail);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的problemDetail：" + problemDetail);
    //
    //                 String maintenanceSuggestion = getStringCellValue(row.getCell(4));
    //                 equipmentMaintenance.setMaintenance_suggestion(maintenanceSuggestion);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的maintenanceSuggestion：" + maintenanceSuggestion);
    //
    //                 String datasource = getStringCellValue(row.getCell(5));
    //                 equipmentMaintenance.setData_source(datasource);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的datasource：" + datasource);
    //
    //                 equipmentMaintenanceList.add(equipmentMaintenance);
    //             }
    //         }
    //         // 批量保存数据到数据库
    //         equipmentMaintenanceService.saveBatch(equipmentMaintenanceList);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             if (workbook != null) {
    //                 workbook.close();
    //             }
    //             if (file != null) {
    //                 file.close();
    //             }
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
    //
    // private static String getStringCellValue(Cell cell) {
    //     if (cell == null) {
    //         return null;
    //     }
    //     switch (cell.getCellType()) {
    //         case STRING:
    //             return cell.getStringCellValue();
    //         case NUMERIC:
    //             return String.valueOf(cell.getNumericCellValue());
    //         case BOOLEAN:
    //             return String.valueOf(cell.getBooleanCellValue());
    //         case FORMULA:
    //             try {
    //                 return cell.getStringCellValue();
    //             } catch (Exception e) {
    //                 return cell.getNumericCellValue() + "";
    //             }
    //         default:
    //             return "";
    //     }
    // }




    @Autowired
    private EquipmentMaintenanceService equipmentMaintenanceService;

    @Test
    public void readExcelAndConvertToEntityList() {
        List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();
        FileInputStream file = null;
        Workbook workbook = null;
        try {
            // 替换为你实际的Excel文件路径
            file = new FileInputStream(new File("D:\\excel\\bbb.xlsx"));
            workbook = new XSSFWorkbook(file); // 使用 XSSFWorkbook 读取 .xlsx 文件
            // 假设读取第一个Sheet页，可根据实际情况调整
            Sheet sheet = workbook.getSheetAt(0);

            // 限制只扫描前55行数据（索引从0开始，所以小于55）
            int maxRowIndex = Math.min(50, sheet.getLastRowNum());
            for (int rowIndex = 1; rowIndex <= maxRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();

                    // 处理日期格式的单元格
                    Cell triggerCell = row.getCell(0);
                     String triggerString = getDateCellValue(triggerCell); // 使用新的方法解析日期




                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d"); // 定义日期格式
                    LocalDate localDate = LocalDate.parse(triggerString, formatter);
                    String localDateString = localDate.format(formatter);

                     equipmentMaintenance.setTriggerTime(localDateString);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的trigger：" + localDate);

                    // 其他字段的处理
                    String process = getStringCellValue(row.getCell(1));
                    equipmentMaintenance.setProcess(process);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的process：" + process);

                    String machineNumber = getStringCellValue(row.getCell(2));
                    equipmentMaintenance.setMachineNumber(machineNumber);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的machineNumber：" + machineNumber);

                    String problemDetail = getStringCellValue(row.getCell(3));
                    equipmentMaintenance.setProblemDetail(problemDetail);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的problemDetail：" + problemDetail);

                    String maintenanceSuggestion = getStringCellValue(row.getCell(4));
                    equipmentMaintenance.setMaintenanceSuggestion(maintenanceSuggestion);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的maintenanceSuggestion：" + maintenanceSuggestion);

                    String datasource = getStringCellValue(row.getCell(5));
                    equipmentMaintenance.setDataSource(datasource);
                    System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的datasource：" + datasource);

                    equipmentMaintenanceList.add(equipmentMaintenance);
                }
            }
            // 批量保存数据到数据库
            equipmentMaintenanceService.saveBatch(equipmentMaintenanceList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理日期格式的单元格
     */
    private static String getDateCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            // 如果是日期格式，转换为日期对象
            Date date = cell.getDateCellValue();
            // 定义日期格式（例如：yyyy/MM/dd）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(date);
        } else {
            // 如果不是日期格式，按普通字符串处理
            return getStringCellValue(cell);
        }
    }

    /**
     * 处理普通单元格
     */
    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return cell.getNumericCellValue() + "";
                }
            default:
                return "";
        }
    }










}
