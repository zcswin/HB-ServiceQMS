package com.ww.boengongye.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelImportUtil {
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    /**
     * 读取Excel
     *
     * @author ZHY
     */
    public ExcelImportUtil(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        InputStream is = file.getInputStream();
        if (".xls".equals(ext)) {
            wb = new HSSFWorkbook(is);
        } else if (".xlsx".equals(ext)) {
            ZipSecureFile.setMinInflateRatio(-1.0d);  // 压缩率，小于这个数值就报错。
            wb = new XSSFWorkbook(is);
        } else {
            wb = null;
        }
    }

    /**
     * 读取Excel表格表头的内容输出
     */
    public List<Map<String, Object>> readExcelTitleOut() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(0);
            // 标题总列数
            int colNum = row.getPhysicalNumberOfCells();

            System.out.println("colNum:" + colNum);

            Map<String, Object> map = new LinkedHashMap<String, Object>();

            for (int i = 0; i < colNum; i++) {
                String stringCellValue = row.getCell(i).getStringCellValue();
                map.put(stringCellValue, null);
            }
            list.add(map);
            return list;
        }
        return list;
    }

    /**
     * 读取Excel表格表头
     */
    public String[] readExcelTitle() {
        String[] title = {};
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(0);
            // 标题总列数
            int colNum = row.getPhysicalNumberOfCells();

            System.out.println("colNum:" + colNum);

            title = new String[colNum];

            for (int i = 0; i < colNum; i++) {
                title[i] = row.getCell(i).getStringCellValue().replaceAll("\\s+", "");
            }
        }
        return title;
    }

    /**
     * 读取Excel表格表头
     * 参数： titleRowNum 表头开始的行数
     *       titleColNum 表头开始的列数
     */
    public String[] readExcelTitle(int titleRowNum, int titleColNum) {
        String[] title = {};
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(titleRowNum - 1);
            // 标题总列数
            int colNum = row.getPhysicalNumberOfCells() + titleColNum - 1;

            System.out.println("colNum:" + colNum);

            title = new String[colNum];

            for (int i = titleColNum - 1 ; i < colNum; i++) {
                title[i] = row.getCell(i).getStringCellValue().replaceAll("\\s+", "");
            }
        }
        return title;
    }

    /**
     * 读取Excel表格的某一个数值
     *
     * @return
     */
    public Map<String, Object> readExcelSomeTitle() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            String title = parseExcel(sheet.getRow(2).getCell(1));
            String remark = parseExcel(sheet.getRow(3).getCell(1));
            map.put("date", title);
            map.put("remark", remark);
        }
        return map;
    }

    /**
     * 读取Excel数据内容
     */
    public List<Map<String, String>> readExcelContent() {
        List<Map<String, String>> list = new ArrayList<>();
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            //获取表头的标题
            String[] readExcelTitle = readExcelTitle();
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < readExcelTitle.length; j++) {
                    //获取每一列的数据值
                    String str = parseExcel(row.getCell(j));
                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j], str);
                    }
                }
                //判段添加的对象是否为空
                if (!map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 读取Excel数据内容
     * 参数： titleRowNum 表头行数
     *       titleColNum 表头列数
     */
    public List<Map<String, String>> readExcelContent(int titleRowNum, int titleColNum) {
        List<Map<String, String>> list = new ArrayList<>();
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            //获取表头的标题
            String[] readExcelTitle = readExcelTitle(titleRowNum, titleColNum);
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = titleRowNum; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < readExcelTitle.length; j++) {
                    //获取每一列的数据值
                    String str = parseExcel(row.getCell(j));
                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j], str);
                    }
                }
                //判段添加的对象是否为空
                if (!map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param row    行下标
     * @param column 列下标
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 读取Excel数据内容
     * 自定义读取
     * 参数：开始读取行数，表头信息数组
     */
    public List<Map<String, String>> readExcelContentDIY(int startRowNum, String[] titleArray) {
        List<Map<String, String>> list = new ArrayList<>();
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            //获取表头的标题
            String[] readExcelTitle = titleArray;
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRowNum; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < readExcelTitle.length; j++) {
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        System.out.println("是合并单元格，位置是：" + i + ":" + j);
                    }

                    //获取每一列的数据值
                    String str = parseExcel(row.getCell(j));
                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j], str);
                    }
                }
                //判段添加的对象是否为空
                if (!map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 读取Excel数据内容
     * 自定义读取
     * 参数：开始读取行数，结束读取行数，表头信息数组
     */
    public List<Map<String, String>> readExcelContentDIYFromTo(int startRowNum, int endRowNum, String[] titleArray) {
        List<Map<String, String>> list = new ArrayList<>();
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = endRowNum;
            //获取表头的标题
            String[] readExcelTitle = titleArray;
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRowNum; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < readExcelTitle.length; j++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }


                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j], str);
                    }
                }
                //判段添加的对象是否为空
                if (!map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 读取Excel数据内容(可选择工作表)
     * 自定义读取块
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数  序号从0开始
     */
    public String[][] readExcelBlock(int startRow, int endRow, int startCol, int endCol,int sheetIndex) {

        startRow--;endRow--;startCol--;endCol--;
        if (endRow <= -1) {  // 表示读取到最后一行
            //获取sheet表
            sheet = wb.getSheetAt(sheetIndex);
            endRow = sheet.getLastRowNum();
        }
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        String[][] result = new String[rowNum][colNum];
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(sheetIndex);

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        result[k][l] = str;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 读取Excel数据内容（可选择工作表）
     * 自定义读取块
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数，工作表下标  A为1，B为2，
     */
    public String[][] readExcelBlockSheet(int startRow, int endRow, int startCol, int endCol,int sheetIndex) {

        startRow--;endRow--;startCol--;endCol--;
        //获取sheet表
        sheet = wb.getSheetAt(sheetIndex);
        if (endRow <= -1) {  // 表示读取到最后一行
            endRow = sheet.getLastRowNum();
        }
        if (endCol <= -1) {  // 表示读取到最后一列
            endCol = sheet.getRow(startRow).getLastCellNum();
        }
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        String[][] result = new String[rowNum][colNum];
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(sheetIndex);

            if (sheet == null){
                return null;
            }

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        result[k][l] = str;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 读取Excel数据内容（可通过工作表名称指定相应的工作表）
     * 自定义读取块
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数，工作表名称  A为1，B为2
     */
    public String[][] readExcelBlockBySheetName(int startRow, int endRow, int startCol, int endCol,String sheetName) {

        startRow--;endRow--;startCol--;endCol--;
        //获取sheet表
        sheet = wb.getSheet(sheetName);
        if (endRow <= -1) {  // 表示读取到最后一行
            endRow = sheet.getLastRowNum();
        }
        if (endCol <= -1) {  // 表示读取到最后一列
            endCol = sheet.getRow(startRow).getLastCellNum();
        }
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        String[][] result = new String[rowNum][colNum];
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheet(sheetName);

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        result[k][l] = str;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取excel中所有工作表名称
     * @return
     */
    public List<String> getAllExcelSheetNameList(){
        List<String> sheetNameList = new ArrayList<>();
        //工作表数量
        int sheetNum = wb.getNumberOfSheets();

        for (int sheetIndex = 0; sheetIndex< sheetNum; sheetIndex++){
            sheet = wb.getSheetAt(sheetIndex);
            sheetNameList.add(sheet.getSheetName());
        }

        return sheetNameList;
    }


    /**
     * 读取Excel数据内容
     * 自定义读取块
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数  序号从0开始
     */
    public String[][] readExcelBlock(int startRow, int endRow, int startCol, int endCol) {

        startRow--;endRow--;startCol--;endCol--;
        if (endRow <= -1) {  // 表示读取到最后一行
            //获取sheet表
            sheet = wb.getSheetAt(0);
            endRow = sheet.getLastRowNum();
        }
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        String[][] result = new String[rowNum][colNum];
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        result[k][l] = str;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 读取Excel数据内容
     * 自定义读取块
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数  A为1，B为2
     */
    public String[][] readExcelBlock2(int startRow, int endRow, int startCol, int endCol) {
        startRow--;endRow--;startCol--;endCol--;
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        String[][] result = new String[rowNum][colNum];
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        result[k][l] = str;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return parseExcel(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * 根据Cell类型设置数据
     */
    public String parseExcel(Cell cell) {
        String result = "";
        if (cell != null) {
            SimpleDateFormat sdf = null;
            switch (cell.getCellType()) {
                case NUMERIC:// 数字类型
                    if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                        System.out.println(cell.getCellStyle().getDataFormat());
                        if (cell.getCellStyle().getDataFormat() == 20 ||
                                cell.getCellStyle().getDataFormat() == 21) {
                            sdf = new SimpleDateFormat("HH:mm:ss");
                        } else if (cell.getCellStyle().getDataFormat() == 14 ||
                                cell.getCellStyle().getDataFormat() == 22 ||
                                cell.getCellStyle().getDataFormat() == 47 ||
                                cell.getCellStyle().getDataFormat() == 176 ||
                                cell.getCellStyle().getDataFormat() == 177 ||
                                cell.getCellStyle().getDataFormat() == 178 ||
                                cell.getCellStyle().getDataFormat() == 179 ||
                                cell.getCellStyle().getDataFormat() == 180||
                                cell.getCellStyle().getDataFormat() == 181||
                                cell.getCellStyle().getDataFormat() == 182||
                                cell.getCellStyle().getDataFormat() == 186
                        ){
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        } else {// 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        }
                        String dateFormat = sdf.format(cell.getDateCellValue());
                        result = dateFormat;
                    } else if (cell.getCellStyle().getDataFormat() == 58) {
                        // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        result = sdf.format(date);
                    } else {
                        double value = cell.getNumericCellValue();
                        DecimalFormat format = new DecimalFormat("#.###########");
                        String strVal = format.format(value);
                        result = strVal;
                    }
                    break;
                case STRING:// String类型
                    result = cell.getRichStringCellValue().toString();
                    break;
                case FORMULA:// 公式类型
                    switch (cell.getCachedFormulaResultType()) {
                        case NUMERIC:
                            result = cell.getNumericCellValue() + "";
                            break;
                        case STRING:
                            result = cell.getStringCellValue();
                            break;
                        default:
                            result = "";
                            break;
                    }

                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     *解析固定行列数据
     */
    public List<String> getCellData(Sheet sheet, int startRow, int startColumn, int rowNum, int colunmNum) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = startRow;i < startRow + rowNum; i++) {
            Row row = sheet.getRow(i);
            for (int j = startColumn; j < startColumn + colunmNum ; j++) {
                //获取每一列的数据值
                String str = parseExcel(row.getCell(j));
                list.add(str);
            }
        }
        return list;
    }
    /**
     * 通过字母获取列数（只能写大写且最多两位）
     * A  : 1
     * AA : 27
     * AB : 28
     * BA : 53
     */
    public int getColNumByLetter(String letter) {

        int len = letter.length();
        if (len == 1) {
            char l1 = letter.charAt(0);
            if (l1 > 90 || l1 < 65) {
                return 0;
            } else {
                return l1 - 64;
            }
        } else if (len == 2) {
            char l1 = letter.charAt(0);
            char l2 = letter.charAt(1);
            if (l1 > 90 || l1 < 65 || l2 > 90 || l2 < 65) {
                return 0;
            } else {
                return (l1 - 64) * 26 + l2 - 64;
            }
        }
        return 0;
    }

    public Workbook getWb(){
        return this.wb;
    }

    /**
     * 读取Excel数据内容
     * 自定义读取块 - 返回List<Map> 头作为键盘\
     * 参数：开始读取行数，结束读取行数，开始读取列数，结束读取列数  序号从0开始
     */
    public List<Map<String,String>> readExcelBlock3(int startRow, int endRow, int startCol, int endCol,int startCol2,int endCol2) {

        startRow--;endRow--;startCol--;endCol--;endCol2--;startCol2--;
        if (endRow <= -1) {  // 表示读取到最后一行
            //获取sheet表
            sheet = wb.getSheetAt(0);
            endRow = sheet.getLastRowNum();
        }
        if (startCol > endCol || startRow > endRow || startRow < 0 || startCol < 0) {
            System.out.println("行数或者列数错误");
            return null;
        }
        // 得到总行数
        int rowNum = endRow - startRow + 1;
        // 得到总列数
        int colNum = endCol - startCol + 1;
        ArrayList<Map<String,String>> list = new ArrayList<>();
        if (wb != null) {
            //获取sheet表
            sheet = wb.getSheetAt(0);
            String[] readExcelTitle = readExcelTitle();

            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = startRow, k = 0; i <= endRow; i++, k++) {
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = startCol, l = 0; j <= endCol; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j],str);
                    }
                }

                for (int j = startCol2, l = 0; j <= endCol2; j++, l++) {
                    String str;
                    // 判断是否是合并单元格
                    if (isMergedRegion(sheet, i, j)){
                        str = getMergedRegionValue(sheet, i, j);
                    } else{
                        //获取每一列的数据值
                        str = parseExcel(row.getCell(j));
                    }

                    //判断对应行的列值是否为空
                    if (StringUtils.isNotBlank(str)) {
                        //表头的标题为键值，列值为值
                        map.put(readExcelTitle[j],str);
                    }
                }
                //判段添加的对象是否为空
                if (!map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }


}
