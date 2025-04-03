package com.ww.boengongye;

import com.ww.boengongye.entity.EquipmentMaintenance;
import com.ww.boengongye.service.EquipmentMaintenanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor 96901
 * @date 2024/12/24
 */

@Slf4j
@SpringBootTest
public class exceltest {



    @Autowired
    private EquipmentMaintenanceService equipmentMaintenanceService;

    // @Test
    // public void exceltest() throws IOException {
    //
    //     String process="ss";
    //
    //     EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();
    //     equipmentMaintenance.setProcess(process);
    //
    //     equipmentMaintenanceService.save(equipmentMaintenance);
    // }


    @Test
    public void readExcelAndConvertToEntityList() {
        List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();
        FileInputStream file = null;
        Workbook workbook = null;
        try {
            // 替换为你实际的Excel文件路径
            file = new FileInputStream(new File("D:\\excel\\bbb.xlsx"));
            workbook = new HSSFWorkbook(file);
            // 假设读取第一个Sheet页，可根据实际情况调整
            Sheet sheet = workbook.getSheetAt(0);

            // 限制只扫描前55行数据（索引从0开始，所以小于55）
            int maxRowIndex = Math.min(50, sheet.getLastRowNum());
            for (int rowIndex = 3; rowIndex <= maxRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row!= null) {
                    EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();










                    // 手动获取单元格值并设置到实体对象中
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
            // 批量保存数据到数据库，前提是equipmentMaintenanceService的saveBatch方法已正确实现且配置好相关依赖等
            equipmentMaintenanceService.saveBatch(equipmentMaintenanceList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook!= null) {
                    workbook.close();
                }
                if (file!= null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
    //         file = new FileInputStream(new File("D:\\excel\\aas.xls"));
    //         workbook = new HSSFWorkbook(file);
    //         // 假设读取第一个Sheet页，可根据实际情况调整
    //         Sheet sheet = workbook.getSheetAt(0);
    //
    //         // 限制只扫描前55行数据（索引从0开始，所以小于55）
    //         int maxRowIndex = Math.min(50, sheet.getLastRowNum());
    //         for (int rowIndex = 2; rowIndex <= maxRowIndex; rowIndex++) {
    //             Row row = sheet.getRow(rowIndex);
    //             if (row!= null) {
    //                 EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();
    //
    //
    //
    //                 // 手动获取单元格值并设置到实体对象中
    //                 String process = getStringCellValue(row.getCell(1));
    //                 equipmentMaintenance.setProcess(process);
    //                 System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的process：" + process);
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
    //
    //                  equipmentMaintenance.setRow_number(rowIndex + 1);
    //
    //
    //                 equipmentMaintenanceList.add(equipmentMaintenance);
    //             }
    //         }
    //         // 批量保存数据到数据库，前提是equipmentMaintenanceService的saveBatch方法已正确实现且配置好相关依赖等
    //         equipmentMaintenanceService.saveBatch(equipmentMaintenanceList);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             if (workbook!= null) {
    //                 workbook.close();
    //             }
    //             if (file!= null) {
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










//
//     @Autowired
//     private EquipmentMaintenanceService equipmentMaintenanceService;
//
//     @Test
//     public void readExcelAndConvertToEntityList() {
//         List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();
//         FileInputStream file = null;
//         Workbook workbook = null;
//         try {
//             // 替换为你实际的Excel文件路径
//             file = new FileInputStream(new File("D:\\excel\\aas.xls"));
//             workbook = new HSSFWorkbook(file);
//             // 假设读取第一个Sheet页，可根据实际情况调整
//             Sheet sheet = workbook.getSheetAt(0);
//
//
//             // 限制只扫描前55行数据（索引从0开始，所以小于55）
//             int maxRowIndex = Math.min(55, sheet.getLastRowNum());
//
//
//             // 从第三行（索引为2）开始遍历每一行数据
//             for (int rowIndex = 4; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//                 Row row = sheet.getRow(rowIndex);
//                 if (row!= null) {
//                     EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance();
//
//
//                     // String trigger = getStringCellValue(row.getCell(0));
//                     //
//                     // System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的trigger：" + trigger);
//
//
//
//
// // 将行号属性设置到实体对象中，这里 + 1是因为行索引从0开始，而通常我们说的行号从1开始
//
//
//
//                     // 手动获取单元格值并设置到实体对象中
//                     String process = getStringCellValue(row.getCell(1));
//                     equipmentMaintenance.setProcess(process);
//                     System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的process：" + process);
//
//                     String machineNumber = getStringCellValue(row.getCell(2));
//                     equipmentMaintenance.setMachine_number(machineNumber);
//                     System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的machineNumber：" + machineNumber);
//
//                     String problemDetail = getStringCellValue(row.getCell(3));
//                     equipmentMaintenance.setProblem_detail(problemDetail);
//                     System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的problemDetail：" + problemDetail);
//
//                     String maintenanceSuggestion = getStringCellValue(row.getCell(4));
//                     equipmentMaintenance.setMaintenance_suggestion(maintenanceSuggestion);
//                     System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的maintenanceSuggestion：" + maintenanceSuggestion);
//
//
//
//
//                     String datasource = getStringCellValue(row.getCell(5));
//                     equipmentMaintenance.setData_source(datasource);
//                     System.out.println("已读取Excel第 " + (rowIndex + 1) + " 行的datasource：" + datasource);
//
//
//                     // equipmentMaintenance.setRow_number(rowIndex + 1);
//
//
//
//
//                     // equipmentMaintenanceList.add(equipmentMaintenance);
//
//                      equipmentMaintenanceService.saveBatch(equipmentMaintenanceList);
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         } finally {
//             try {
//                 if (workbook!= null) {
//                     workbook.close();
//                 }
//                 if (file!= null) {
//                     file.close();
//                 }
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
//
//     private static String getStringCellValue(Cell cell) {
//         if (cell == null) {
//             return null;
//         }
//         switch (cell.getCellType()) {
//             case STRING:
//                 return cell.getStringCellValue();
//             case NUMERIC:
//                 return String.valueOf(cell.getNumericCellValue());
//             case BOOLEAN:
//                 return String.valueOf(cell.getBooleanCellValue());
//             case FORMULA:
//                 try {
//                     return cell.getStringCellValue();
//                 } catch (Exception e) {
//                     return cell.getNumericCellValue() + "";
//                 }
//             default:
//                 return "";
//         }
//     }
}
