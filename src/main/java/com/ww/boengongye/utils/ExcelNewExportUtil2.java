package com.ww.boengongye.utils;


import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.exportExcelUpdate.ChartEntity;
import com.ww.boengongye.entity.exportExcelUpdate.DfQmsIpqcWaigTotalBo;
import com.ww.boengongye.entity.exportExcelUpdate.DfQmsIpqcWaigTotalVo;
import com.ww.boengongye.entity.exportNewExcel.AuditIssue;
import com.ww.boengongye.entity.exportNewExcel.DataVo;
import com.ww.boengongye.entity.exportNewExcel.DfExcelPortVo;
import com.ww.boengongye.entity.exportNewExcel.jcDataVo;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelNewExportUtil2 {


    //表头
    private String title;
    //各个列的表头
    private String[] heardList;
    //各个列的元素key值
    private String[] heardKey;
    //需要填充的数据信息
    private List<Map> data;
    //字体大小
    private int fontSize = 14;
    //行高
    private int rowHeight = 30;
    //列宽
    private int columWidth = 200;
    //工作表
    private String sheetName = "明细原始数据";

    private static String fac;

    private String period;

    private String startDate;
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // 定义全类公用的 distinctFirstList
    private static List<String> distinctFirstList = new ArrayList<>();

    // 定义全类公用的 distinctFirstList
    private static LinkedHashMap<String, Integer> issuePointList = new LinkedHashMap<>();

    // 如果需要动态更新 distinctFirstList，可以提供 setter 方法
    public void setDistinctFirstList(List<String> newList) {
        this.distinctFirstList = newList;
    }

    // 获取 distinctFirstList 的值
    public static List<String> getDistinctFirstList() {
        return distinctFirstList;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeardList() {
        return heardList;
    }

    public void setHeardList(String[] heardList) {
        this.heardList = heardList;
    }

    public String[] getHeardKey() {
        return heardKey;
    }

    public void setHeardKey(String[] heardKey) {
        this.heardKey = heardKey;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getColumWidth() {
        return columWidth;
    }

    public void setColumWidth(int columWidth) {
        this.columWidth = columWidth;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 开始导出数据信息
     */
    public byte[] exportExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);
        //创建工作表
        SXSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);

        // 标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle = (HSSFCellStyle) wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont fontStyle = (HSSFFont) wb.createFont();
//        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontStyle.setBold(true);   //加粗
        fontStyle.setFontHeightInPoints((short) 16);  //设置标题字体大小
        cellStyle.setFont(fontStyle);

//        //在第0行创建rows  (表标题)
//        HSSFRow title = wbSheet.createRow((int) 0);
//        title.setHeightInPoints(30);//行高
//        HSSFCell cellValue = title.createCell(0);
//        cellValue.setCellValue(this.title);
//        cellValue.setCellStyle(cellStyle);
//        wbSheet.addMergedRegion(new CellRangeAddress(0,0,0,(this.heardList.length-1)));
        //设置表头样式，表头居中
        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
        //设置单元格样式
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置字体
        HSSFFont font = (HSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        //在第1行创建rows
        SXSSFRow row = wbSheet.createRow((int) 0);
        //设置列头元素
        SXSSFCell cellHead = null;
        for (int i = 0; i < heardList.length; i++) {
            cellHead = row.createCell(i);
            cellHead.setCellValue(heardList[i]);
            cellHead.setCellStyle(style);
        }

        //设置每格数据的样式 （字体红色）
        HSSFCellStyle cellParamStyle = (HSSFCellStyle) wb.createCellStyle();
        HSSFFont ParamFontStyle = (HSSFFont) wb.createFont();
//        cellParamStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        ParamFontStyle.setColor(HSSFColor.DARK_RED.index);   //设置字体颜色 (红色)
        ParamFontStyle.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle.setFont(ParamFontStyle);
        //设置每格数据的样式2（字体蓝色）
        HSSFCellStyle cellParamStyle2 = (HSSFCellStyle) wb.createCellStyle();
//        cellParamStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ParamFontStyle2 = (HSSFFont) wb.createFont();
//        ParamFontStyle2.setColor(HSSFColor.BLUE.index);   //设置字体颜色 (蓝色)
        ParamFontStyle2.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle2.setFont(ParamFontStyle2);
        //开始写入实体数据信息
        int a = 1;
        for (int i = 0; i < data.size(); i++) {
            SXSSFRow roww = wbSheet.createRow((int) a);
            Map map = data.get(i);
            SXSSFCell cell = null;
            for (int j = 0; j < heardKey.length; j++) {
                cell = roww.createCell(j);
                cell.setCellStyle(style);
                Object valueObject = map.get(heardKey[j]);
                String value = null;
                if (valueObject == null) {
                    valueObject = "";
                }
                if (valueObject instanceof String) {
                    //取出的数据是字符串直接赋值
                    value = (String) map.get(heardKey[j]);
                } else if (valueObject instanceof Integer) {
                    //取出的数据是Integer
                    value = String.valueOf(((Integer) (valueObject)).floatValue());
                } else if (valueObject instanceof BigDecimal) {
                    //取出的数据是BigDecimal
                    value = String.valueOf(((BigDecimal) (valueObject)).floatValue());
                } else {
                    value = valueObject.toString();
                }
                //设置单个单元格的字体颜色
                if (heardKey[j].equals("ddNum") || heardKey[j].equals("sjNum")) {
                    if ((Long) map.get("ddNum") != null) {
                        if ((Long) map.get("sjNum") == null) {
                            cell.setCellStyle(cellParamStyle);
                        } else if ((Long) map.get("ddNum") != (Long) map.get("sjNum")) {
                            if ((Long) map.get("ddNum") > (Long) map.get("sjNum")) {
                                cell.setCellStyle(cellParamStyle);
                            }
                            if ((Long) map.get("ddNum") < (Long) map.get("sjNum")) {
                                cell.setCellStyle(cellParamStyle2);
                            }
                        } else {
                            cell.setCellStyle(style);
                        }
                    }
                }
                cell.setCellValue(Strings.isEmpty(value) ? "" : value);
            }
            a++;
        }

        //导出数据
        try {
            //设置Http响应头告诉浏览器下载这个附件
            response.setHeader("Content-Disposition", "attachment;Filename=" + System.currentTimeMillis() + ".xls");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.close();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("导出Excel出现严重异常，异常信息：" + ex.getMessage());
        }

    }


    public byte[] exportExportNew(HttpServletRequest request, HttpServletResponse response,
                                  List<DfControlStandardStatus> listData, List<DfControlStandardStatus> SheetList, List<DfAuditDetail> numList, List<DfAuditDetail> timeout, List<DfAuditDetail> list5,List<DfAuditDetail> list6, List<DfAuditDetail> list7, ExcelNewExportUtil2 excelExportSheet8, List<DfAuditDetail> listjc, List<DfProcess> dplist) throws IOException, ParseException {

        // 创建工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook(10000); // 使用 XSSFWorkbook
        /*
         ***************************************************************************************************************
         */
        //第一个工作表
        SXSSFSheet sheet = wb.createSheet("执行率模版");

        List<DfExcelPortVo> testV0List4101 = getDfControlStandardStatusVo1(listData);

        XSSFCellStyle cellParamStyle4 = createCellParamStyle(wb);

        // 表格

        if (testV0List4101.size() > 0) {
            Map<String, Integer> centerTable1 = createSheet1Table(cellParamStyle4, sheet, 0, 0, testV0List4101);
            createBottomChar(testV0List4101, sheet, 0, centerTable1.get("col"));
        }

        //第二个sheet***********************************************************************************************************
        SXSSFSheet sheet2 = wb.createSheet("各工序问题点汇总");

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 30, 30, fac + "厂各部门问题点汇总（前段）", "X轴", "Y轴");
        if (SheetList.size() > 0) {
            Map<String, Integer> centerTable2 = createSheet2Table(wb, cellParamStyle4, sheet2, 0, 0, SheetList);
            //表格添加柱形图表
            createBarChart(centerTable2.get("row"), sheet2, SheetList, barChartchartEntity);
        }
        //第三个sheet***********************************************************************************************************
        SXSSFSheet sheet3 = wb.createSheet("各清单稽核问题点占比");
      //  issuePointList.clear();
        List<DfExcelPortVo> ListSheet3 = getDfAuditDetailVo3(numList);
//        if ("year".equals(period)) {
//            issuePointList = calculateYearlyAndMonthly(issuePointList);
//        }

        if (ListSheet3.size() > 0) {
            Map<String, Integer> centerTable3 = createSheet3Table(cellParamStyle4, sheet3, 0, 0, ListSheet3);
            create3Char(ListSheet3, sheet3, 0, centerTable3.get("col"));
        }


        //第四个sheet***********************************************************************************************************
        SXSSFSheet sheet4 = wb.createSheet("问题点处理时效性");
        List<DfExcelPortVo> ListSheet4 = getDfAuditDetailVo4(timeout);

        if (ListSheet4.size() > 0) {
            Map<String, Integer> centerTable4 = createSheet4Table(cellParamStyle4, sheet4, 0, 0, ListSheet4);
            create4Char(ListSheet4, sheet4, 0, centerTable4.get("col"));
        }

        //第五个sheet***********************************************************************************************************
        SXSSFSheet sheet5 = wb.createSheet("问题点关闭率");
        List<DfExcelPortVo> ListSheet5 = getDfAuditDetailVo5(list5);

        if (ListSheet5.size() > 0) {
            Map<String, Integer> centerTable5 = createSheet5Table(cellParamStyle4, sheet5, 0, 0, ListSheet5);
            create5Char(ListSheet5, sheet5, 0, centerTable5.get("col"));
        }

        /*
         ************************************************************************************
         */
        //第六个工作表
        SXSSFSheet sheet6 = wb.createSheet("各⼯序点超时回复占⽐");
        // 初始化一个二维列表，用于存储良率数据
        if (listjc.size() > 0) {
            Sheet6(dplist, list6, sheet6, wb, 0, cellParamStyle4);
        }


        //第七个sheet***********************************************************************************************************
        SXSSFSheet sheet7 = wb.createSheet("各厂违规Level等级占比");
        List<DfExcelPortVo> ListSheet7 = getDfAuditDetailVo7(list7);

        if (ListSheet7.size() > 0) {
            Map<String, Integer> centerTable7 = createSheet7Table(cellParamStyle4, sheet7, 0, 0, ListSheet7);
            create7Char(ListSheet7, sheet7, 0, centerTable7.get("col"));
        }

        //第八个sheet*************************************************************************************************************'
        /*
         **************************************************************************************************************************
         */           //第八个工作表

        Sheet sheet8 = wb.createSheet("稽查数据列表");
        // 设置列宽
        exportExcleAuditNgDetail(excelExportSheet8, sheet8, wb);


        // 导出数据
        try {
            String name = generateFileName("稽核数据",startDate,endDate);

            // RFC 5987 编码规范处理
            String encodedNameRFC5987 = URLEncoder.encode(name, StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~"); // 部分符号还原以提高可读性

            // 组合Content-Disposition
            String contentDisposition = String.format(
                    "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                    encodedNameRFC5987,
                    encodedNameRFC5987
            );

            response.setHeader("Content-Disposition", contentDisposition);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");

            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            return null;
        } catch (Exception ex) {
            throw new IOException("导出Excel异常: " + ex.getMessage());
        } finally {
            wb.close();
        }
    }

    public static String generateFileName(String contentDescription, String startDate, String endDate) {
        // 目标日期格式：只保留日期部分
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;

        try {
            // 如果传入的字符串长度超过10，说明可能包含时间部分
            if (startDate != null && startDate.length() > 10) {
                // 先尝试用带时间的格式解析
                SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                start = fullFormat.parse(startDate);
            } else {
                start = targetFormat.parse(startDate);
            }
        } catch (Exception e) {
            // 解析失败时，直接赋值为null，后续将直接使用原始字符串
            start = null;
        }

        try {
            if (endDate != null && endDate.length() > 10) {
                SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end = fullFormat.parse(endDate);
            } else {
                end = targetFormat.parse(endDate);
            }
        } catch (Exception e) {
            end = null;
        }

        // 如果解析成功，则格式化为 "yyyy-MM-dd"，否则直接使用原始字符串
        String startStr = (start != null) ? targetFormat.format(start) : startDate;
        String endStr = (end != null) ? targetFormat.format(end) : endDate;

        // 返回文件名，例如：稽核数据2024-12-01至2025-01-01.xlsx
        return contentDescription + startStr + "至" + endStr + ".xlsx";
    }



    // 按日期分组数据
    private Map<LocalDate, List<DfAuditDetail>> groupDataByDate(List<DfAuditDetail> filteredData) {
        return filteredData.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    // 按年和月分组数据
    private Map<Integer, Map<Integer, List<DfAuditDetail>>> groupDataByYearAndMonth(List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData) {
        Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = new TreeMap<>();
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData.getOrDefault(date, Collections.emptyList()));
        }

        return yearlyData;
    }


    // 按年和月分组数据
    private Map<Integer, Map<Integer, List<DfAuditDetail>>> groupDataByMonth(List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData) {
        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData.getOrDefault(date, Collections.emptyList()));
        }

        return monthlyData;
    }


    // 按周和具体日期分组数据
    private Map<Integer, Map<String, List<DfAuditDetail>>> groupDataByWeekAndDate(List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData) {
        // 按周和日期（yyyy-MM-dd）分组的Map（key: 周, value: 每周的Map，key: 日期（yyyy-MM-dd），value: 每日的数据）
        Map<Integer, Map<String, List<DfAuditDetail>>> weeklyAndDateData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取当前日期是该年的第几周
            Integer weekOfYear = getWeekOfYear(date);

            // 获取当前日期的日期字符串（yyyy-MM-dd）
            String dateStr = date.toString();  // `LocalDate` 可以直接转换成 `yyyy-MM-dd` 格式的字符串

            // 按周和日期进行分组
            weeklyAndDateData.computeIfAbsent(weekOfYear, k -> new TreeMap<>())
                    .computeIfAbsent(dateStr, k -> new ArrayList<>())
                    .addAll(dailyData.getOrDefault(date, Collections.emptyList()));
        }


        return weeklyAndDateData;
    }


    // 获取年份中的第几周
    private int getWeekOfYear(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);  // 获取年份中的第几周
    }


    // 处理每年的数据并按月汇总
    private void processYearData(List<DfProcess> dplist, Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry, List<DfAuditDetail> filteredData, List<DfQmsIpqcWaigTotalVo> testV0List6, boolean falg, List<List<Object>> listNum, List<List<Object>> listRate) {
        Integer year = yearEntry.getKey();
        Map<Integer, List<DfAuditDetail>> monthlyRecords = yearEntry.getValue();
        int totalCount = 0;

        // 对每个月的数据进行汇总
        for (Map.Entry<Integer, List<DfAuditDetail>> monthEntry : monthlyRecords.entrySet()) {
            List<Object> NumM = new ArrayList<>();
            List<Object> Num1M = new ArrayList<>();
            List<Object> ShiftYieldRateM = new ArrayList<>();
            List<Object> ShiftYieldRate1M = new ArrayList<>();
            NumM.add("超时次数");
            ShiftYieldRateM.add("超时占比");
            // 统计每个月的数据
            List<DfAuditDetail> monthlyData = monthEntry.getValue();
            Map<String, DfAuditDetail> map = new HashMap<>();
            totalCount = calculateMonthlyOvertime(map, monthlyData);

            // 遍历每个项目，计算超时次数和超时占比
            calculateShiftYieldRate(dplist, map, totalCount, NumM, ShiftYieldRateM, Num1M, ShiftYieldRate1M);

            // 汇总当天数据
            List<List<Object>> datas2 = new ArrayList<>();
            Num1M.add(totalCount);
            ShiftYieldRate1M.add("100%");
            datas2.add(Num1M);
            datas2.add(ShiftYieldRate1M);

            DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
            testB0.setClassPlease("白班");
            testB0.setData(datas2);

            DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
            vo.setData(monthEntry.getKey() + "月");
            vo.setfFac(fac);
            vo.setTestBOs(Collections.singletonList(testB0));
            testV0List6.add(vo);
        }

        // 处理年度数据
        List<DfAuditDetail> periodData = filterDataByYear(filteredData, year);
        Map<String, DfAuditDetail> map = new HashMap<>();
        totalCount = calculateMonthlyOvertime(map, periodData);

        // 遍历每个项目，计算超时次数和超时占比
        List<Object> NumY = new ArrayList<>();
        List<Object> ShiftYieldRateY = new ArrayList<>();
        List<Object> Num1Y = new ArrayList<>();
        List<Object> ShiftYieldRate1Y = new ArrayList<>();
        NumY.add("超时次数");
        ShiftYieldRateY.add("超时占比");
        calculateShiftYieldRate(dplist, map, totalCount, NumY, ShiftYieldRateY, Num1Y, ShiftYieldRate1Y);

        // 汇总年度数据
        List<List<Object>> datas2 = new ArrayList<>();
        Num1Y.add(totalCount);
        ShiftYieldRate1Y.add("100%");
        datas2.add(Num1Y);
        datas2.add(ShiftYieldRate1Y);

        DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
        testB0.setClassPlease("白班");
        testB0.setData(datas2);

        DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
        vo.setData(year + "年");
        vo.setfFac(fac);
        vo.setTestBOs(Collections.singletonList(testB0));
        if (falg == true) {
            listNum.add(NumY);
            listRate.add(ShiftYieldRateY);

        }

        testV0List6.add(vo);
    }


    // 处理每周的数据并按月汇总
    private void processMonthlyData(List<DfProcess> dplist, Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry, List<DfAuditDetail> filteredData, List<DfQmsIpqcWaigTotalVo> testV0List6, boolean falg, List<List<Object>> listNum, List<List<Object>> listRate) {
        Integer month = monthEntry.getKey();
        Map<Integer, List<DfAuditDetail>> weeklyRecords = monthEntry.getValue();
        int totalCount = 0;

        // 按周汇总数据
        for (Map.Entry<Integer, List<DfAuditDetail>> weekEntry : weeklyRecords.entrySet()) {
            List<Object> NumW = new ArrayList<>();
            List<Object> Num1W = new ArrayList<>();
            List<Object> ShiftYieldRateW = new ArrayList<>();
            List<Object> ShiftYieldRate1W = new ArrayList<>();
            NumW.add("超时次数");
            ShiftYieldRateW.add("超时占比");

            // 统计每周的数据
            List<DfAuditDetail> weeklyData = weekEntry.getValue();
            Map<String, DfAuditDetail> map = new HashMap<>();
            totalCount = calculateMonthlyOvertime(map, weeklyData);  // 统计每周的超时次数

            // 遍历每个项目，计算超时次数和超时占比
            calculateShiftYieldRate(dplist, map, totalCount, NumW, ShiftYieldRateW, Num1W, ShiftYieldRate1W);

            // 汇总每周的数据
            List<List<Object>> datas2 = new ArrayList<>();
            Num1W.add(totalCount);
            ShiftYieldRate1W.add("100%");
            datas2.add(Num1W);
            datas2.add(ShiftYieldRate1W);

            DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
            testB0.setClassPlease("白班");
            testB0.setData(datas2);

            DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
            vo.setData("第 " + weekEntry.getKey() + " 周");
            vo.setfFac(fac);
            vo.setTestBOs(Collections.singletonList(testB0));
            testV0List6.add(vo);
        }

        // 处理月度数据
        List<DfAuditDetail> periodData = filterDataByMonth(filteredData, month);
        Map<String, DfAuditDetail> map = new HashMap<>();
        totalCount = calculateMonthlyOvertime(map, periodData);

        // 遍历每个项目，计算超时次数和超时占比
        List<Object> NumY = new ArrayList<>();
        List<Object> ShiftYieldRateY = new ArrayList<>();
        List<Object> Num1Y = new ArrayList<>();
        List<Object> ShiftYieldRate1Y = new ArrayList<>();
        NumY.add("超时次数");
        ShiftYieldRateY.add("超时占比");
        calculateShiftYieldRate(dplist, map, totalCount, NumY, ShiftYieldRateY, Num1Y, ShiftYieldRate1Y);

        // 汇总月度数据
        List<List<Object>> datas2 = new ArrayList<>();
        Num1Y.add(totalCount);
        ShiftYieldRate1Y.add("100%");
        datas2.add(Num1Y);
        datas2.add(ShiftYieldRate1Y);

        DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
        testB0.setClassPlease("白班");
        testB0.setData(datas2);

        DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
        vo.setData(month + "月");
        vo.setfFac(fac);
        vo.setTestBOs(Collections.singletonList(testB0));
        if (falg == true) {
            listNum.add(NumY);
            listRate.add(ShiftYieldRateY);

        }

        testV0List6.add(vo);
    }


    // 处理每周的数据并按月汇总
    private void processWeekData(List<DfProcess> dplist, Map.Entry<Integer, Map<String, List<DfAuditDetail>>> weekEntry, List<DfAuditDetail> filteredData, List<DfQmsIpqcWaigTotalVo> testV0List6, boolean falg, List<List<Object>> listNum, List<List<Object>> listRate) {
        Integer week = weekEntry.getKey();
        Map<String, List<DfAuditDetail>> weeklyRecords = weekEntry.getValue();
        int totalCount = 0;
        // 按周汇总数据
        for (Map.Entry<String, List<DfAuditDetail>> dateEntry : weeklyRecords.entrySet()) {
            List<Object> NumW = new ArrayList<>();
            List<Object> Num1W = new ArrayList<>();
            List<Object> ShiftYieldRateW = new ArrayList<>();
            List<Object> ShiftYieldRate1W = new ArrayList<>();
            NumW.add("超时次数");
            ShiftYieldRateW.add("超时占比");

            // 统计每周的数据
            Map<String, DfAuditDetail> map = new HashMap<>();

            // 统计每周的数据
            List<DfAuditDetail> weeklyData = dateEntry.getValue();
            totalCount = calculateMonthlyOvertime(map, weeklyData);  // 统计每周的超时次数


            // 遍历每个项目，计算超时次数和超时占比
            calculateShiftYieldRate(dplist, map, totalCount, NumW, ShiftYieldRateW, Num1W, ShiftYieldRate1W);

            // 汇总每天的数据
            List<List<Object>> datas2 = new ArrayList<>();
            Num1W.add(totalCount);
            ShiftYieldRate1W.add("100%");
            datas2.add(Num1W);
            datas2.add(ShiftYieldRate1W);

            DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
            testB0.setClassPlease("白班");
            testB0.setData(datas2);

            DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
            vo.setData(dateEntry.getKey()+"");
            vo.setfFac(fac);
            vo.setTestBOs(Collections.singletonList(testB0));
            testV0List6.add(vo);
        }

        // 处理周数据
        List<DfAuditDetail> periodData = filterDataByWeek(filteredData, week);
        Map<String, DfAuditDetail> map = new HashMap<>();
        totalCount = calculateMonthlyOvertime(map, periodData);

        // 遍历每个项目，计算超时次数和超时占比
        List<Object> NumY = new ArrayList<>();
        List<Object> ShiftYieldRateY = new ArrayList<>();
        List<Object> Num1Y = new ArrayList<>();
        List<Object> ShiftYieldRate1Y = new ArrayList<>();
        NumY.add("超时次数");
        ShiftYieldRateY.add("超时占比");
        calculateShiftYieldRate(dplist, map, totalCount, NumY, ShiftYieldRateY, Num1Y, ShiftYieldRate1Y);

        // 汇总月度数据
        List<List<Object>> datas2 = new ArrayList<>();
        Num1Y.add(totalCount);
        ShiftYieldRate1Y.add("100%");
        datas2.add(Num1Y);
        datas2.add(ShiftYieldRate1Y);

        DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
        testB0.setClassPlease("白班");
        testB0.setData(datas2);

        DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
        vo.setData(week + "周");
        vo.setfFac(fac);
        vo.setTestBOs(Collections.singletonList(testB0));
        if (falg == true) {
            listNum.add(NumY);
            listRate.add(ShiftYieldRateY);

        }

        testV0List6.add(vo);
    }


    // 计算每月超时数据
    private int calculateMonthlyOvertime(Map<String, DfAuditDetail> map, List<DfAuditDetail> monthlyData) {
        int totalCount = 0;
        for (DfAuditDetail dfAuditDetail : monthlyData) {
            String keyName = dfAuditDetail.getProcess();
            // 直接判断超时次数是否大于零来计算
            if (dfAuditDetail.getTimeoutPoints() > 0) {  // 如果超时次数字段大于零，表示有超时
                DfAuditDetail existing = map.get(keyName);
                if (existing != null) {
                    // 累加超时次数
                    Integer timeoutPoints = existing.getTimeoutPoints() + 1;
                    existing.setTimeoutPoints(timeoutPoints);
                    map.put(keyName, existing);
                } else {
                    // 新的超时记录
                    dfAuditDetail.setTimeoutPoints(1);
                    map.put(keyName, dfAuditDetail);
                }
                totalCount++;
            }
        }
        return totalCount;
    }

    // 计算每月或每年的超时占比
    private void calculateShiftYieldRate(List<DfProcess> dplist, Map<String, DfAuditDetail> map, int totalCount, List<Object> NumY, List<Object> ShiftYieldRateY, List<Object> Num1Y, List<Object> ShiftYieldRate1Y) {
        for (int i = 0; i < dplist.size(); i++) {
            String pro = dplist.get(i).getProcessName();
            DfAuditDetail dfAuditDetail = map.get(pro);
            if (dfAuditDetail == null) {
                ShiftYieldRateY.add(0);
                ShiftYieldRate1Y.add("0%");
                NumY.add(0);
                Num1Y.add(0);
            } else {
                Integer num = dfAuditDetail.getTimeoutPoints();
                if (totalCount > 0) {
                    double numRate = (double) num / totalCount;
                    ShiftYieldRate1Y.add(String.format("%.2f%%", numRate));
                    ShiftYieldRateY.add(numRate);
                    NumY.add(num);
                    Num1Y.add(num);
                } else {
                    ShiftYieldRateY.add(0);
                    ShiftYieldRate1Y.add("0%");
                    NumY.add(0);
                    Num1Y.add(0);
                }
            }
        }
    }

    // 过滤数据按年
    private List<DfAuditDetail> filterDataByYear(List<DfAuditDetail> filteredData, Integer year) {
        return filteredData.stream()
                .filter(item -> {
                    try {
                        Date itemDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(itemDate);
                        return cal.get(Calendar.YEAR) == year;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // 过滤数据按年
    private List<DfAuditDetail> filterDataByMonth(List<DfAuditDetail> filteredData, Integer month) {
        return filteredData.stream()
                .filter(item -> {
                    try {
                        Date itemDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(itemDate);
                        return cal.get(Calendar.MONTH) == month;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }


    // 过滤数据按周
    private List<DfAuditDetail> filterDataByWeek(List<DfAuditDetail> filteredData, Integer weekOfYear) {
        return filteredData.stream()
                .filter(item -> {
                    try {
                        Date itemDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(itemDate);
                        int week = cal.get(Calendar.WEEK_OF_YEAR);  // 获取该日期属于哪一周
                        return week == weekOfYear;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }



    private void Sheet6(List<DfProcess> dplist, List<DfAuditDetail> listData, SXSSFSheet sheet6, SXSSFWorkbook wb, int col2, XSSFCellStyle cellParamStyle4) throws ParseException {
        List<List<Object>> sheetDataList6 = new ArrayList<>();
        List<List<Object>> barChartsheetDataList6 = new ArrayList<>();
        List<DfQmsIpqcWaigTotalVo> testV0List6 = new ArrayList<>();

//        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
//        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
//
//        // 根据时间范围过滤数据
//        List<DfAuditDetail> filteredData = filterDataByDate(listData, start, end);

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);

        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyData = groupDataByDate(listData);


        // 按年展示数据并统计每年
        if ("year".equals(period)) {
            Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = groupDataByYearAndMonth(allDates, dailyData);
            int totalSize = yearlyData.size();
            int currentIndex = 0;
            for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry : yearlyData.entrySet()) {
                currentIndex++;
                boolean falg = false;
                if (currentIndex == totalSize) {
                    falg = true;
                }
                processYearData(dplist, yearEntry, listData, testV0List6, falg, sheetDataList6, barChartsheetDataList6);
            }
        } else if ("month".equals(period)) {
            // 按月展示数据
            Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = groupDataByMonth(allDates, dailyData);
            int totalSize = monthlyData.size();
            int currentIndex = 0;
            for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry : monthlyData.entrySet()) {
                currentIndex++;
                boolean falg = false;
                if (currentIndex == totalSize) {
                    falg = true;
                }
                processMonthlyData(dplist, monthEntry, listData, testV0List6, falg, sheetDataList6, barChartsheetDataList6);
            }
        } else if ("week".equals(period)) {
            // 按周展示数据
            Map<Integer, Map<String, List<DfAuditDetail>>> weeklyData = groupDataByWeekAndDate(allDates, dailyData);
            int totalSize = weeklyData.size();
            int currentIndex = 0;
            for (Map.Entry<Integer, Map<String, List<DfAuditDetail>>> dateEntry : weeklyData.entrySet()) {
                currentIndex++;
                boolean falg = false;
                if (currentIndex == totalSize) {
                    falg = true;
                }
                processWeekData(dplist, dateEntry, listData, testV0List6, falg, sheetDataList6, barChartsheetDataList6);
            }
        }


        // 按照选择的周期字段排序
//        Collections.sort(testV0List6, new Comparator<DfQmsIpqcWaigTotalVo>() {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//            @Override
//            public int compare(DfQmsIpqcWaigTotalVo vo1, DfQmsIpqcWaigTotalVo vo2) {
//                try {
//                    Date date1 = sdf.parse(vo1.getData());
//                    Date date2 = sdf.parse(vo2.getData());
//                    return date2.compareTo(date1); // 反向排序
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    return 0;
//                }
//            }
//        });


        List<Object> prcNames6 = new ArrayList<>();
        prcNames6.add(""); // 在开始添加空字符串
        prcNames6.addAll(dplist.stream()
                .map(DfProcess::getProcessName)  // 提取字段
                .distinct()  // 去重
                .collect(Collectors.toList()));  // 转换为列表并添加到 prcNames
       // prcNames6.addAll(dplist.stream().map(DfProcessProjectConfig::getProcessName).collect(Collectors.toList()));
        ChartEntity barChartchartEntity6 = new ChartEntity("LINE", 0, 0, 30, 30, testV0List6.get(testV0List6.size() - 1).getData() + testV0List6.get(0).getfFac() + "厂各工序问题点超时回复占比", "X轴", "Y轴");
        //表格添加柱形图表
        //createBarChartSheet6(sheet6, sheetDataList6, prcNames6, barChartchartEntity6, barChartsheetDataList6);
        createBarChart(sheet6, sheetDataList6, prcNames6, barChartchartEntity6, barChartsheetDataList6);

        //生成表格
        int col26 = barChartchartEntity6.getRow2();
        //设置表头
        //创建行
        SXSSFRow row6 = sheet6.createRow(col26);
        XSSFCellStyle cellParamStyleS6 = createCellParamStyle(wb);

        //创建表头前三列
        SXSSFCell cell06 = row6.createCell(0); // 创建单元格
        cell06.setCellValue("日期");
        cell06.setCellStyle(cellParamStyleS6);

        SXSSFCell cell6 = row6.createCell(1); // 创建单元格
        cell6.setCellValue("厂别");
        cell6.setCellStyle(cellParamStyleS6);

        SXSSFCell cell26 = row6.createCell(2); // 创建单元格
        cell26.setCellValue("工序");
        cell26.setCellStyle(cellParamStyleS6);

        DfProcess s = new DfProcess();
        s.setProcessName("合计");
        dplist.add(s);
        for (int i = 0; i < dplist.size(); i++) {
            // 创建单元格
            SXSSFCell cell = row6.createCell(i + 3);
            // 设置单元格值
            cell.setCellValue(dplist.get(i).getProcessName().toString());
            cell.setCellStyle(cellParamStyleS6);
        }

        int Index6 = col26 + 1;
        for (int i = 0; i < testV0List6.size(); i++) {
            DfQmsIpqcWaigTotalVo testVO = testV0List6.get(i);
            //新建15行，并合并
            for (int j = 0; j < 6; j++) {
                sheet6.createRow(Index6 + j);
            }
            SXSSFRow row1 = sheet6.getRow(Index6);
            sheet6.addMergedRegion(new CellRangeAddress(Index6, Index6 + 1, 0, 0));
            SXSSFCell cell00 = row1.createCell(0);
            cell00.setCellStyle(cellParamStyleS6);
            // 设置单元格值
            cell00.setCellValue(testVO.getData());


            sheet6.addMergedRegion(new CellRangeAddress(Index6, Index6 + 1, 1, 1));
            SXSSFCell cell11 = row1.createCell(1);
            cell11.setCellStyle(cellParamStyleS6);
            // 设置单元格值
            cell11.setCellValue(testVO.getfFac());


            List<DfQmsIpqcWaigTotalBo> testBOs = testVO.getTestBOs();

            int index = Index6;
            int boIndex = Index6;
            for (int j = 0; j <= testBOs.size() - 1; j++) {
                DfQmsIpqcWaigTotalBo testBO = testBOs.get(j);

                // 合并单元格的处理
                //sheet6.addMergedRegion(new CellRangeAddress(index, index + 1, 2, 2));
                SXSSFRow rowBO = sheet6.getRow(index);
                SXSSFCell cellBO = rowBO.createCell(2);
                cellBO.setCellStyle(cellParamStyle4);
                // 设置单元格值
                cellBO.setCellValue("超时次数");
                cellBO.setCellStyle(cellParamStyle4);

                SXSSFRow rowBO1 = sheet6.getRow(index + 1);
                SXSSFCell cellBO1 = rowBO1.createCell(2);
                // 设置单元格值
                cellBO1.setCellValue("超时占比");
                cellBO1.setCellStyle(cellParamStyle4);


                // 处理testBO数据的部分
                List<List<Object>> data1 = testBO.getData();
                for (int k = 0; k <= data1.size() - 1; k++) {
                    List<Object> strings = data1.get(k);
                    SXSSFRow rowK = sheet6.getRow(boIndex);
                    if (rowK == null) {
                        rowK = sheet6.createRow(boIndex);
                    }
                    for (int z = 0; z <= strings.size() - 1; z++) {
                        SXSSFCell cell = rowK.createCell(z + 3);
                        // 设置单元格值
                        cell.setCellValue(strings.get(z).toString());
                        cell.setCellStyle(cellParamStyleS6);

                    }

                    boIndex++;
                }

                index += 2;
            }

            Index6 += 2;
        }
    }

    public static void createBarChart(SXSSFSheet sheet, List<List<Object>> sheetDataList, List<Object> heads, ChartEntity chartEntity, List<List<Object>> barChartsheetDataList) {

        int row1 = chartEntity.getRow1();
        int row2 = chartEntity.getRow2();
        int col1 = chartEntity.getCol1();
        int col2 = chartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 12000);
        }

        // X轴显示数据
        String[] headArray = heads.stream().skip(1).collect(Collectors.toList()).toArray(new String[]{});

        // 创建图表
        sheet.createDrawingPatriarch();
        XSSFDrawing drawing = sheet.getDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(chartEntity.getTitleName());

        // 配置图例
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        // ==================== 坐标轴配置 ====================
        // 1. X轴（底部）
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);

        // 2. 左侧数值轴（柱状图）
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        leftAxis.setTitle("数量");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.crossAxis(xAxis);
        xAxis.crossAxis(leftAxis);

        // 3. 右侧百分比轴（折线图）
        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
        rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        rightAxis.setCrosses(AxisCrosses.MAX);
        rightAxis.setTitle("符合率");
        rightAxis.crossAxis(xAxis);
        xAxis.crossAxis(rightAxis);

        // ==================== 强制设置右侧轴为百分比格式 ====================
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        CTValAx rightValAx = null;
        for (CTValAx ax : plotArea.getValAxList()) {
            if (ax.getAxPos().getVal() == STAxPos.R) { // 找到右侧轴
                rightValAx = ax;
                break;
            }
        }
        if (rightValAx != null) {
            CTNumFmt numFmt = rightValAx.isSetNumFmt() ? rightValAx.getNumFmt() : rightValAx.addNewNumFmt();
            numFmt.setFormatCode("0.00%");
            numFmt.setSourceLinked(false);
        }

        // ==================== 柱状图配置（左侧轴） ====================
        XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, leftAxis);
        barChartData.setBarDirection(BarDirection.COL);
        barChartData.setVaryColors(true);

        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        for (List<Object> objects : sheetDataList) {
            String title = String.valueOf(objects.get(0));

            Double[] yArray = objects.stream()
                    .skip(1)
                    .map(obj -> {
                        if (obj instanceof Number) {
                            double value = ((Number) obj).doubleValue();
                            return value;
                        } else if (obj instanceof String) {
                            try {
                                double parsedDouble = Double.parseDouble((String) obj);
                                return parsedDouble;
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toArray(Double[]::new);

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFChartData.Series series = barChartData.addSeries(xData, yData);
            series.setTitle(title, null);
            series.setShowLeaderLines(true);

            XDDFShapeProperties shapeProperties = series.getShapeProperties();
            if (shapeProperties == null) {
                shapeProperties = new XDDFShapeProperties();
                series.setShapeProperties(shapeProperties);
            }
            XDDFLineProperties lineProperties = shapeProperties.getLineProperties();
            if (lineProperties == null) {
                lineProperties = new XDDFLineProperties();
                shapeProperties.setLineProperties(lineProperties);
                series.setLineProperties(lineProperties);
            }
            lineProperties.setWidth(2.5);
            lineProperties.setLineCap(LineCap.FLAT);
        }

        // ==================== 折线图配置（右侧轴） ====================
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, rightAxis);

        for (List<Object> objects : barChartsheetDataList) {
            String title = String.valueOf(objects.get(0));

            Double[] yArray = objects.stream()
                    .skip(1)
                    .map(obj -> {
                        if (obj instanceof Number) {
                            double value = ((Number) obj).doubleValue();
                            return value;
                        } else if (obj instanceof String) {
                            try {
                                double parsedDouble = Double.parseDouble((String) obj);
                                return parsedDouble;
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toArray(Double[]::new);

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
            series.setTitle(title, null);
            series.setShowLeaderLines(false);
            series.setSmooth(false);
            series.setMarkerStyle(MarkerStyle.CIRCLE);

            XDDFShapeProperties shapeProperties = series.getShapeProperties();
            if (shapeProperties == null) {
                shapeProperties = new XDDFShapeProperties();
                series.setShapeProperties(shapeProperties);
            }
            XDDFLineProperties lineProperties = shapeProperties.getLineProperties();
            if (lineProperties == null) {
                lineProperties = new XDDFLineProperties();
                shapeProperties.setLineProperties(lineProperties);
                series.setLineProperties(lineProperties);
            }
            lineProperties.setWidth(2.5);
            lineProperties.setLineCap(LineCap.FLAT);
        }

        // ==================== 数据标签配置 ====================
        // 柱状图标签（显示数值，格式为 General）
        for (CTBarSer ser : plotArea.getBarChartArray(0).getSerList()) {
            CTBoolean ctBoolean = CTBoolean.Factory.newInstance();
            ctBoolean.setVal(false);
            CTDLbls ctdLbls = ser.addNewDLbls();
            ctdLbls.addNewShowVal().setVal(true);
            ctdLbls.addNewNumFmt().setFormatCode("General");  // 使用 General 格式
            ctdLbls.setShowBubbleSize(ctBoolean);
            ctdLbls.setShowCatName(ctBoolean);
            ctdLbls.setShowLeaderLines(ctBoolean);
            ctdLbls.setShowLegendKey(ctBoolean);
            ctdLbls.setShowSerName(ctBoolean);
            ctdLbls.setShowPercent(ctBoolean);
        }

        // 折线图标签（显示百分比）
        for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
            CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

            CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
            numFmt.setFormatCode("0.00%");
            numFmt.setSourceLinked(false);

            labels.addNewShowVal().setVal(true);

            CTBoolean hide = CTBoolean.Factory.newInstance();
            hide.setVal(false);
            labels.setShowCatName(hide);
            labels.setShowSerName(hide);
            labels.setShowLegendKey(hide);
            labels.setShowPercent(hide);

            labels.addNewDLblPos().setVal(STDLblPos.T);
        }

        chart.plot(barChartData);
        chart.plot(lineChartData);
    }



    public static void createBarChartSheet6(XSSFSheet sheet, List<List<Object>> sheetDataList, List<Object> heads, ChartEntity chartEntity, List<List<Object>> barChartsheetDataList) {

        int row1 = chartEntity.getRow1();
        int row2 = chartEntity.getRow2();
        int col1 = chartEntity.getCol1();
        int col2 = chartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 12000);
        }

        // X轴显示数据
        String[] headArray = heads.stream().skip(1).collect(Collectors.toList()).toArray(new String[]{});

        // Create a chart
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(chartEntity.getTitleName());

        // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);

        // 设置y轴的格式为百分比
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        yAxis.setNumberFormat("0.00%"); // 设置百分比格式

        // x轴数据
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        // 创建柱形图数据
        XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
        barChartData.setBarDirection(BarDirection.COL);
        barChartData.setVaryColors(true);

        for (List<Object> objects : sheetDataList) {
            String title = String.valueOf(objects.get(0));

            Double[] yArray = objects.stream()
                    .skip(1)
                    .map(obj -> {
                        if (obj instanceof Number) {
                            // 转换为 double，乘以 100，保留两位小数
                            double value = ((Number) obj).doubleValue() * 100;
                            return Double.parseDouble(String.format("%.2f", value));
                        } else if (obj instanceof String) {
                            try {
                                // 转换为 double，乘以 100，保留两位小数
                                double parsedDouble = Double.parseDouble((String) obj) * 100;
                                return Double.parseDouble(String.format("%.2f", parsedDouble));
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // 过滤掉 null 值
                    .toArray(Double[]::new); // 转换为 Double 数组

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFChartData.Series series = barChartData.addSeries(xData, yData);
            series.setTitle(title, null);
            series.setShowLeaderLines(true);

            XDDFShapeProperties shapeProperties = series.getShapeProperties();
            if (shapeProperties == null) {
                shapeProperties = new XDDFShapeProperties();
                series.setShapeProperties(shapeProperties);
            }
            XDDFLineProperties lineProperties = shapeProperties.getLineProperties();
            if (lineProperties == null) {
                lineProperties = new XDDFLineProperties();
                shapeProperties.setLineProperties(lineProperties);
                series.setLineProperties(lineProperties);
            }
            lineProperties.setWidth(2.5);
            lineProperties.setLineCap(LineCap.FLAT);
        }

        // 创建折线图数据
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        for (List<Object> objects : barChartsheetDataList) {
            String title = String.valueOf(objects.get(0));

            Double[] yArray = objects.stream()
                    .skip(1)
                    .map(obj -> {
                        if (obj instanceof Number) {
                            // 转换为 double，乘以 100，保留两位小数
                            double value = ((Number) obj).doubleValue() * 100;
                            return Double.parseDouble(String.format("%.2f", value));
                        } else if (obj instanceof String) {
                            try {
                                // 转换为 double，乘以 100，保留两位小数
                                double parsedDouble = Double.parseDouble((String) obj) * 100;
                                return Double.parseDouble(String.format("%.2f", parsedDouble));
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // 过滤掉 null 值
                    .toArray(Double[]::new); // 转换为 Double 数组

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
            series.setTitle(title, null);
            series.setShowLeaderLines(false);
            series.setSmooth(false);
            series.setMarkerStyle(MarkerStyle.CIRCLE);

            XDDFShapeProperties shapeProperties = series.getShapeProperties();
            if (shapeProperties == null) {
                shapeProperties = new XDDFShapeProperties();
                series.setShapeProperties(shapeProperties);
            }
            XDDFLineProperties lineProperties = shapeProperties.getLineProperties();
            if (lineProperties == null) {
                lineProperties = new XDDFLineProperties();
                shapeProperties.setLineProperties(lineProperties);
                series.setLineProperties(lineProperties);
            }
            lineProperties.setWidth(2.5);
            lineProperties.setLineCap(LineCap.FLAT);
        }

        CTPlotArea plotArea = chart.getCTChart().getPlotArea();

        for (CTBarSer ser : plotArea.getBarChartArray(0).getSerList()) {
            CTBoolean ctBoolean = CTBoolean.Factory.newInstance();
            ctBoolean.setVal(false);
            CTDLbls ctdLbls = ser.addNewDLbls();
            ctdLbls.addNewShowVal().setVal(true);
            ctdLbls.addNewNumFmt().setFormatCode("0.00%");  // 百分比格式
            ctdLbls.setShowBubbleSize(ctBoolean);
            ctdLbls.setShowCatName(ctBoolean);
            ctdLbls.setShowLeaderLines(ctBoolean);
            ctdLbls.setShowLegendKey(ctBoolean);
            ctdLbls.setShowSerName(ctBoolean);
            ctdLbls.setShowPercent(ctBoolean);
            // 设置数据标签格式为百分比
            ctdLbls.addNewNumFmt().setFormatCode("0.00%");
            ctdLbls.addNewDLblPos().setVal(STDLblPos.Enum.forString("t"));
        }

        for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
            CTBoolean ctBoolean = CTBoolean.Factory.newInstance();
            ctBoolean.setVal(false);
            CTDLbls ctdLbls = ser.addNewDLbls();
            ctdLbls.addNewShowVal().setVal(true);
            ctdLbls.setShowBubbleSize(ctBoolean);
            ctdLbls.setShowCatName(ctBoolean); // 不显示x轴数据标签
            ctdLbls.setShowLeaderLines(ctBoolean);
            ctdLbls.setShowLegendKey(ctBoolean);
            ctdLbls.setShowSerName(ctBoolean);
            ctdLbls.setShowPercent(ctBoolean);
            // 设置数据标签格式为百分比
            ctdLbls.addNewNumFmt().setFormatCode("0.00%");
            ctdLbls.addNewDLblPos().setVal(STDLblPos.Enum.forString("t"));
        }

        chart.plot(barChartData);
        chart.plot(lineChartData);
    }


    public void exportExcleAuditNgDetail(ExcelNewExportUtil2 excelExportUtil2, Sheet wbSheet, SXSSFWorkbook wb) {
        data = excelExportUtil2.getData();
        heardKey = excelExportUtil2.getHeardKey();
        heardList = excelExportUtil2.getHeardList();
        XSSFCellStyle style = createTitleCellStyle8(wb);
        XSSFCellStyle style2 = createCellParamStyle(wb);


        // 创建表头行
        Row headerRow = wbSheet.createRow(0);
        int[] maxColumnWidths = new int[heardList.length];

        // 计算表头列宽
        for (int i = 0; i < heardList.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(heardList[i]);
            cell.setCellStyle(style);
            // 计算表头的实际字符宽度（中文按2，英文按1）
            maxColumnWidths[i] = calculateStringWidth(heardList[i]) + 4; // 增加4字符缓冲
        }

        // 写入数据行并计算最大列宽
        int rowIndex = 1;
        for (int i = 0; i < data.size(); i++) {
            Row dataRow = wbSheet.createRow(rowIndex++);
            Map<String, Object> map = data.get(i);
            for (int j = 0; j < heardKey.length; j++) {
                Cell cell = dataRow.createCell(j);
                cell.setCellStyle(style2);
                Object valueObj = map.get(heardKey[j]);
                String value = (valueObj == null) ? "" : valueObj.toString();
                cell.setCellValue(value);

                // 计算数据内容的实际字符宽度
                int cellWidth = calculateStringWidth(value) + 4; // 增加4字符缓冲
                if (cellWidth > maxColumnWidths[j]) {
                    maxColumnWidths[j] = cellWidth;
                }
            }
        }

        // 设置列宽（根据实际字符宽度 + 缓冲值）
        for (int i = 0; i < heardList.length; i++) {
            int adjustedWidth = (int) ((maxColumnWidths[i] + 20) * 256 * 0.9); // 缓冲4字符，并调整比例
            wbSheet.setColumnWidth(i, Math.min(adjustedWidth, 255 * 256)); // 限制最大列宽255字符
        }
    }

    // 计算字符串的实际显示宽度（中文字符算2，英文算1）
    private int calculateStringWidth(String str) {
        if (str == null || str.isEmpty()) return 0;
        int width = 0;
        for (char c : str.toCharArray()) {
            // 判断是否为中文字符（根据 Unicode 范围）
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) {
                width += 2; // 中文字符宽度为2
            } else {
                width += 1; // 英文字符宽度为1
            }
        }
        return width;
    }



    public XSSFCellStyle createTitleCellStyle8(SXSSFWorkbook wb) {
        XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();

        // 设置字体
        XSSFFont titleFont = (XSSFFont) wb.createFont();
        titleFont.setFontHeightInPoints((short) 14);  // 设置字体大小
        titleFont.setBold(true);  // 设置字体加粗
        titleFont.setColor(IndexedColors.BLACK.getIndex());  // 设置字体为黑色
        titleStyle.setFont(titleFont);

        // 设置单元格背景颜色为自定义颜色 #92CDDC
        XSSFColor customColor = new XSSFColor(new java.awt.Color(146, 205, 220), new DefaultIndexedColorMap());
        titleStyle.setFillForegroundColor(customColor);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  // 设置填充模式为纯色

        // 设置单元格居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中

        return titleStyle;
    }




    public static void createBarChart(int num, SXSSFSheet sheet, List<DfControlStandardStatus> list, ChartEntity chartEntity) {
        int row1 = 7;
        int col1 = chartEntity.getCol1();
        int col2 = chartEntity.getCol2();

        List<String> list1 = new ArrayList<>();
        List<Object> listData = new ArrayList<>();
        List<Object> listData1 = new ArrayList<>();
        List<List<Object>> sheetDataList = new ArrayList<>();
        list1.add("");
        listData.add("稽查项");
        listData1.add("不符");

        List<Object> lineDate = new ArrayList<>();
        lineDate.add("符合率");
        List<List<Object>> Line = new ArrayList<>();

        // 数据准备：确保符合率是0-1的小数
        for (DfControlStandardStatus df : list) {
            if (df.getProcessDrl() != null && !df.getProcessDrl().trim().isEmpty()) {
                list1.add(df.getProcessDrl() + "\n" + df.getProcessCode());
            } else {
                list1.add(df.getProcessCode());
            }
            listData1.add(df.getNgCount());
            listData.add(df.getTotalCount());
            // 关键！将符合率转换为0-1的小数（例如95→0.95）
            lineDate.add(df.getOkRate());
        }

        sheetDataList.add(listData);
        sheetDataList.add(listData1);
        Line.add(lineDate);

        // X轴显示数据
        String[] headArray = list1.stream().skip(1).toArray(String[]::new);

        // 创建图表
        sheet.createDrawingPatriarch();
        XSSFDrawing drawing = sheet.getDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, col2, row1 + 20);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(chartEntity.getTitleName());

        // 配置图例
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        // ==================== 坐标轴配置 ====================
        // 1. X轴（底部）
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);

        // 2. 左侧数值轴（柱状图）
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        leftAxis.setTitle("数量");
        // 左Y轴和X轴交叉点在X轴0点位置
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        // 构建坐标轴
        leftAxis.crossAxis(xAxis);
        xAxis.crossAxis(leftAxis);

        // 3. 右侧百分比轴（折线图）
        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
        rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        // 右Y轴和X轴交叉点在X轴最大值位置
        rightAxis.setCrosses(AxisCrosses.MAX);
        rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        // 构建坐标轴
        rightAxis.crossAxis(xAxis);
        xAxis.crossAxis(rightAxis);

        rightAxis.setTitle("符合率");

        // ==================== 强制设置右侧轴为百分比格式 ====================
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        CTValAx rightValAx = null;
        for (CTValAx ax : plotArea.getValAxList()) {
            if (ax.getAxPos().getVal() == STAxPos.R) { // 通过位置判断右侧轴
                rightValAx = ax;
                break;
            }
        }
        if (rightValAx != null) {
            CTNumFmt numFmt = rightValAx.isSetNumFmt() ? rightValAx.getNumFmt() : rightValAx.addNewNumFmt();
            numFmt.setFormatCode("0.00%");
            numFmt.setSourceLinked(false); // 禁用自动关联格式
        }

        // ==================== 柱状图配置（左侧轴） ====================
        XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, leftAxis);
        barChartData.setBarDirection(BarDirection.COL);
        barChartData.setVaryColors(true);

        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        for (List<Object> objects : sheetDataList) {
            String title = String.valueOf(objects.get(0));
            Double[] yArray = objects.stream()
                    .skip(1)
                    .filter(Objects::nonNull)
                    .map(obj -> (obj instanceof Integer) ? ((Integer) obj).doubleValue() : 0.0)
                    .toArray(Double[]::new);

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFChartData.Series series = barChartData.addSeries(xData, yData);
            series.setTitle(title, null);

            // 柱状图样式
            XDDFShapeProperties shapeProps = new XDDFShapeProperties();
            XDDFLineProperties lineProps = new XDDFLineProperties();
            lineProps.setWidth(2.5);
            shapeProps.setLineProperties(lineProps);
            series.setShapeProperties(shapeProps);
        }

        // ==================== 折线图配置（右侧轴） ====================
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, rightAxis);

        for (List<Object> objects : Line) {
            String title = String.valueOf(objects.get(0));
            Double[] yArray = objects.stream()
                    .skip(1)
                    .map(obj -> ((Number) obj).doubleValue())
                    .toArray(Double[]::new);

            XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
            XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
            lineSeries.setTitle(title, null);
            lineSeries.setSmooth(false);
            lineSeries.setMarkerStyle(MarkerStyle.CIRCLE);

            // 折线图样式
            XDDFShapeProperties shapeProps = new XDDFShapeProperties();
            XDDFLineProperties lineProps = new XDDFLineProperties();
            lineProps.setWidth(2.5);
            shapeProps.setLineProperties(lineProps);
            lineSeries.setShapeProperties(shapeProps);
        }

        // ==================== 数据标签配置 ====================
        // 柱状图标签（显示数值）
        for (CTBarSer ser : plotArea.getBarChartArray(0).getSerList()) {
            CTDLbls labels = ser.isSetDLbls() ? ser.getDLbls() : ser.addNewDLbls();
            CTNumFmt fmt = labels.addNewNumFmt();
            fmt.setFormatCode("General"); // 数值格式
            fmt.setSourceLinked(false);
            labels.addNewShowVal().setVal(true);
            labels.addNewShowCatName().setVal(false);
            labels.addNewShowSerName().setVal(false);
            labels.addNewShowLegendKey().setVal(false);
            labels.addNewShowPercent().setVal(false);
        }

        // 折线图标签（显示百分比）
        for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
            CTDLbls labels = ser.isSetDLbls() ? ser.getDLbls() : ser.addNewDLbls();
            CTNumFmt fmt = labels.addNewNumFmt();
            fmt.setFormatCode("0.00%"); // 百分比格式
            fmt.setSourceLinked(false);
            labels.addNewShowVal().setVal(true);
            labels.addNewShowCatName().setVal(false);
            labels.addNewShowSerName().setVal(false);
            labels.addNewShowLegendKey().setVal(false);
            labels.addNewShowPercent().setVal(false);
        }

        // 绘制图表
        chart.plot(barChartData);
        chart.plot(lineChartData);
    }


    public List<DfExcelPortVo> getDfAuditDetailVo4(
            List<DfAuditDetail> totalShiftList) {
        String type = "sheet4";
        // 结果集合
        List<DfExcelPortVo> result = new ArrayList<>();

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);


        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyData = totalShiftList.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 根据周期处理数据
        switch (period.toLowerCase().trim()) {
            case "week":
                processWeeklyData(null, null, allDates, null, dailyData, result, type, totalShiftList);
                break;
            case "month":
                processMonthlyData(null, null, allDates, null, dailyData, result, type,totalShiftList);
                break;
            case "year":
                processYearlyData(null, null, allDates, null, dailyData, result, type,totalShiftList);
                break;
            default:
                System.out.println("Invalid period: " + period);  // 或者记录到日志
        }

        return result;
    }

    public List<DfExcelPortVo> getDfAuditDetailVo5(
            List<DfAuditDetail> totalShiftList) {
        String type = "sheet5";
        // 结果集合
        List<DfExcelPortVo> result = new ArrayList<>();

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);


        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyData = totalShiftList.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 根据周期处理数据
        switch (period.toLowerCase().trim()) {
            case "week":
                processWeeklyData(null, null, allDates, null, dailyData, result, type,totalShiftList);
                break;
            case "month":
                processMonthlyData(null, null, allDates, null, dailyData, result, type,totalShiftList);
                break;
            case "year":
                processYearlyData(null, null, allDates, null, dailyData, result, type,totalShiftList);
                break;
            default:
                System.out.println("Invalid period: " + period);  // 或者记录到日志
        }

        return result;
    }

    public List<DfExcelPortVo> getDfAuditDetailVo7(
            List<DfAuditDetail> totalShiftList) {
        String type = "sheet7";
        // 结果集合
        List<DfExcelPortVo> result = new ArrayList<>();

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);


        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyData = totalShiftList.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 根据周期处理数据
        switch (period.toLowerCase().trim()) {
            case "week":
                processWeeklyData(null, null, allDates, null, dailyData, result, type,null);
                break;
            case "month":
                processMonthlyData(null, null, allDates, null, dailyData, result, type,null);
                break;
            case "year":
                processYearlyData(null, null, allDates, null, dailyData, result, type,null);
                break;
            default:
                System.out.println("Invalid period: " + period);  // 或者记录到日志
        }

        return result;
    }


    public List<DfExcelPortVo> getDfAuditDetailVo3(
            List<DfAuditDetail> totalShiftList) {
        //issuePointList.clear();
        String type = "sheet3";
        // 分组数据：按项目ID和类别名称
        Map<String, Map<String, List<DfAuditDetail>>> groupedData = totalShiftList.stream()
                .filter(status -> status != null && status.getProjectName() != null && status.getQuestionType() != null)
                .collect(Collectors.groupingBy(
                        status -> Optional.ofNullable(status.getProjectName()).orElse("Unknown"),
                        Collectors.groupingBy(status -> Optional.ofNullable(status.getQuestionType()).orElse("Unknown"))
                ));
        // 结果集合
        List<DfExcelPortVo> result = new ArrayList<>();

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);
        List<String> listType = new ArrayList<>();
        listType.add("MIL");
        listType.add("DFM");
        listType.add("QCP");
        listType.add("SOP");
        listType.add("耗材");

        // 遍历项目和类别
        for (Map.Entry<String, Map<String, List<DfAuditDetail>>> projectEntry : groupedData.entrySet()) {
            String project = projectEntry.getKey();
            for (String category : listType) {
                List<DfAuditDetail> records = projectEntry.getValue().getOrDefault(category, new ArrayList<>());

                // 按日期分组数据
                Map<LocalDate, List<DfAuditDetail>> dailyData = records.stream()
                        .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                // 根据周期处理数据
                switch (period.toLowerCase().trim()) {
                    case "week":
                        processWeeklyData(project, category, allDates, null, dailyData, result, type,totalShiftList);
                        break;
                    case "month":
                        processMonthlyData(project, category, allDates, null, dailyData, result, type,totalShiftList);
                        break;
                    case "year":
                        processYearlyData(project, category, allDates, null, dailyData, result, type,totalShiftList);
                        break;
                    default:
                        System.out.println("周期:" + period);
                }
            }
        }

        return result;
    }


    public List<DfExcelPortVo> getDfControlStandardStatusVo1(
            List<DfControlStandardStatus> totalShiftList) {
        String type = "sheet1";
        // 分组数据：按项目ID和类别名称
        Map<String, Map<String, List<DfControlStandardStatus>>> groupedData = totalShiftList.stream()
                .filter(status -> status != null && status.getProjectId() != null && status.getCreateName() != null)
                .collect(Collectors.groupingBy(
                        status -> Optional.ofNullable(status.getProjectId()).orElse("Unknown"),
                        Collectors.groupingBy(status -> Optional.ofNullable(status.getCreateName()).orElse("Unknown"))
                ));

        // 结果集合
        List<DfExcelPortVo> result = new ArrayList<>();

        // 获取所有日期
        List<LocalDate> allDates = getDatesForPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate), period);
        List<String> listType = new ArrayList<>();
        listType.add("MIL");
        listType.add("DFM");
        listType.add("QCP");
        listType.add("SOP");
        listType.add("耗材");

        // 遍历项目和类别
        for (Map.Entry<String, Map<String, List<DfControlStandardStatus>>> projectEntry : groupedData.entrySet()) {
            String project = projectEntry.getKey();

            // 确保 listType 中的所有类别都存在，即使没有对应的数据
            for (String category : listType) {
                // 获取该项目和类别的记录，如果没有，创建一个空的列表
                List<DfControlStandardStatus> records = projectEntry.getValue().getOrDefault(category, new ArrayList<>());

                // 按日期分组数据
                Map<LocalDate, List<DfControlStandardStatus>> dailyData = records.stream()
                        .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

                // 根据周期处理数据
                switch (period.toLowerCase().trim()) {
                    case "week":
                        processWeeklyData(project, category, allDates, dailyData, null, result, type,null);
                        break;
                    case "month":
                        processMonthlyData(project, category, allDates, dailyData, null, result, type,null);
                        break;
                    case "year":
                        processYearlyData(project, category, allDates, dailyData, null, result, type,null);
                        break;
                    default:
                        System.out.println("Invalid period: " + period);  // 或者记录到日志
                }
            }
        }


        return result;
    }


    /**
     * 根据周期类型获取所有日期
     */
    private List<LocalDate> getDatesForPeriod(LocalDate startDate, LocalDate endDate, String period) {
        List<LocalDate> allDates = new ArrayList<>();
        switch (period.toLowerCase()) {
            case "week":
                allDates = getDaysInRange(startDate, endDate);
                break;
            case "month":
                allDates = getDatesInRange(startDate, endDate);
                break;
            case "year":
                allDates = getDatesInRange(startDate, endDate);
                break;
            default:
                throw new IllegalArgumentException("Unsupported period: " + period);
        }
        return allDates;
    }

    /**
     * 获取开始和结束时间之间的所有天
     */
    private List<LocalDate> getDaysInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> days = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            days.add(currentDate);
            currentDate = currentDate.plusDays(1);  // 下一天
        }
        return days;
    }

    /**
     * 获取开始和结束时间之间的所有周
     */
    private List<LocalDate> getDatesInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        // 遍历从 startDate 到 endDate 之间的所有日期
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);  // 下一天
        }

        return dates;
    }


    /**
     * 获取开始和结束时间之间的所有月
     */
    private List<LocalDate> getMonthsInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> months = new ArrayList<>();
        LocalDate currentDate = startDate.withDayOfMonth(1);  // 设置为当月的第一天

        while (!currentDate.isAfter(endDate)) {
            months.add(currentDate);
            currentDate = currentDate.plusMonths(1);  // 下一月
        }

        return months;
    }


    /**
     * 处理周数据，按天展示数据并统计每周的结果
     */
    private void processWeeklyData(String project, String category, List<LocalDate> allDates,
                                   Map<LocalDate, List<DfControlStandardStatus>> dailyData, Map<LocalDate, List<DfAuditDetail>> dailyData2,
                                   List<DfExcelPortVo> result, String type,List<DfAuditDetail> alllist) {

        distinctFirstList.clear();
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);


        // 按天展示数据并统计每周
        WeekFields weekFields = WeekFields.of(Locale.getDefault()); // 获取当前地区的周定义
        LocalDate startOfWeek = null;
        List<DfControlStandardStatus> weeklyRecords = new ArrayList<>();
        List<jcDataVo> jcDataVoList = new ArrayList<>();


        List<DfAuditDetail> weekdfAuditDetail = new ArrayList<>();
        int totalIssues = 0;
        List<AuditIssue> auditIssueList = new ArrayList<>();


        if (type.equals("sheet1")) {
            int currentWeekOfYear = -1;

            for (LocalDate date : allDates) {
                int week = date.get(weekFields.weekOfYear());
                int abbreviatedYear = date.getYear() % 100; // 24
                String year = String.valueOf(abbreviatedYear); // "24"

                // 处理当前日期
                distinctFirstList.add(date.format(DateTimeFormatter.ofPattern("yy/M/d")));
                List<DfControlStandardStatus> dailyDataForDate = dailyData.getOrDefault(date, Collections.emptyList());
                jcDataVoList.add(createJcDataVo(dailyDataForDate));
                weeklyRecords.addAll(dailyDataForDate);

                // 检测是否为新周
                if (startOfWeek == null || week != currentWeekOfYear) {
                    // 处理前一周的数据（非首次）
                    if (startOfWeek != null) {
                        addWeeklyDataToResult(weeklyRecords, jcDataVoList);
                        distinctFirstList.add(year+"Week " + currentWeekOfYear); // 使用前一周的周数
                        weeklyRecords.clear();
                    }
                    // 更新周信息
                    currentWeekOfYear = week;
                    startOfWeek = date;
                }
            }

//            // 处理最后一周
//            if (!weeklyRecords.isEmpty()) {
//                addWeeklyDataToResult(weeklyRecords, jcDataVoList);
//                distinctFirstList.add("Week " + currentWeekOfYear);
//            }

            vo.setChildren(jcDataVoList);
            result.add(vo);
        } else if ("sheet3".equals(type)) {
            int currentWeekOfYear = -1;

            // 按日期分组数据
            Map<LocalDate, List<DfAuditDetail>> dailyDataAll = alllist.stream()
                    .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            // 遍历每一个日期
            for (LocalDate date : allDates) {
                // 获取当前日期的周数
                int week = date.get(weekFields.weekOfYear());
                int abbreviatedYear = date.getYear() % 100; // 24
                String year = String.valueOf(abbreviatedYear); // "24"

                // 将日期添加到 distinctFirstList 中
                distinctFirstList.add(date.format(DateTimeFormatter.ofPattern("yy/M/d")));
                // 获取当天的数据
                List<DfAuditDetail> dailyDataForDate = dailyData2.getOrDefault(date, Collections.emptyList());

                List<DfAuditDetail> sum = dailyDataAll.getOrDefault(date, Collections.emptyList());

                int totalIdSum = sum.stream().mapToInt(DfAuditDetail::getTotalCount).sum();
                AuditIssue auditIssue = createAuditIssueVo(dailyDataForDate,totalIdSum); // 创建 createAuditIssueVo
                auditIssueList.add(auditIssue);

                totalIssues += totalIdSum;


                // 将当天的数据加入当前周的数据列表
                weekdfAuditDetail.addAll(dailyDataForDate);
                // 检测是否为新周
                if (startOfWeek == null || week != currentWeekOfYear) {
                    // 处理前一周的数据（非首次）
                    if (startOfWeek != null) {
                        addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, year+"Week " + currentWeekOfYear,totalIssues);
                        distinctFirstList.add(year+"Week " + currentWeekOfYear);
                        weekdfAuditDetail.clear(); // 清空当前周数据，准备新周
                        totalIssues = 0;
                    }
                    // 更新周信息
                    currentWeekOfYear = week;
                    startOfWeek = date;
                }

            }

            // 处理最后一周的数据
//            if (!weekdfAuditDetail.isEmpty()) {
//                addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, "Week " + currentWeekOfYear,totalIssues);
//                distinctFirstList.add("Week " + currentWeekOfYear);
//            }
            vo.setAuditIssues(auditIssueList);
            result.add(vo);
        } else if ("sheet4".equals(type)) {
            int currentWeekOfYear = -1;

            // 遍历每一个日期
            for (LocalDate date : allDates) {
                // 获取当前日期的周数
                int week = date.get(weekFields.weekOfYear());
                int abbreviatedYear = date.getYear() % 100; // 24
                String year = String.valueOf(abbreviatedYear); // "24"
                // 将日期添加到 distinctFirstList 中
                distinctFirstList.add(date.format(DateTimeFormatter.ofPattern("yy/M/d")));
                // 获取当天的数据
                List<DfAuditDetail> dailyDataForDate = dailyData2.getOrDefault(date, Collections.emptyList());

                AuditIssue auditIssue = createAuditIssueVo(dailyDataForDate,0); // 创建 createAuditIssueVo
                auditIssueList.add(auditIssue);


                // 如果是新的周，统计前一周
                // 检测是否为新周
                if (startOfWeek == null || week != currentWeekOfYear) {
                    // 处理前一周的数据（非首次）
                    if (startOfWeek != null) {
                        addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, year+"Week " + currentWeekOfYear,0);
                        distinctFirstList.add(year+"Week " + currentWeekOfYear);
                        weekdfAuditDetail.clear(); // 清空当前周数据，准备新周
                    }
                    // 更新周信息
                    currentWeekOfYear = week;
                    startOfWeek = date;
                }

                // 将当天的数据加入当前周的数据列表
                weekdfAuditDetail.addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
            }

            // 处理最后一周的数据
//            if (!weekdfAuditDetail.isEmpty()) {
//                addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, "Week " + startOfWeek.get(weekFields.weekOfYear()),0);
//                // 将最后一周的周数添加到 distinctFirstList 中
//                distinctFirstList.add("Week " + startOfWeek.get(weekFields.weekOfYear()));
//            }
            vo.setAuditIssues(auditIssueList);
            result.add(vo);
        } else if ("sheet5".equals(type)) {
            int currentWeekOfYear = -1;
            // 遍历每一个日期
            for (LocalDate date : allDates) {
                // 获取当前日期的周数
                int week = date.get(weekFields.weekOfYear());
                int abbreviatedYear = date.getYear() % 100; // 24
                String year = String.valueOf(abbreviatedYear); // "24"
                // 将日期添加到 distinctFirstList 中
                distinctFirstList.add(date.format(DateTimeFormatter.ofPattern("yy/M/d")));
                // 获取当天的数据
                List<DfAuditDetail> dailyDataForDate = dailyData2.getOrDefault(date, Collections.emptyList());

                AuditIssue auditIssue = createAuditIssueVo(dailyDataForDate,0); // 创建 createAuditIssueVo
                auditIssueList.add(auditIssue);


                // 检测是否为新周
                if (startOfWeek == null || week != currentWeekOfYear) {
                    // 处理前一周的数据（非首次）
                    if (startOfWeek != null) {
                        addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, year+"Week " + currentWeekOfYear,0);
                        distinctFirstList.add(year+"Week " + currentWeekOfYear);
                        weekdfAuditDetail.clear(); // 清空当前周数据，准备新周
                    }
                    // 更新周信息
                    currentWeekOfYear = week;
                    startOfWeek = date;
                }

                // 将当天的数据加入当前周的数据列表
                weekdfAuditDetail.addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
            }

            // 处理最后一周的数据
//            if (!weekdfAuditDetail.isEmpty()) {
//                addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, "Week " + startOfWeek.get(weekFields.weekOfYear()),0);
//                // 将最后一周的周数添加到 distinctFirstList 中
//                distinctFirstList.add("Week " + startOfWeek.get(weekFields.weekOfYear()));
//            }
            vo.setAuditIssues(auditIssueList);
            result.add(vo);
        } else if ("sheet7".equals(type)) {
            int currentWeekOfYear = -1;

            // 遍历每一个日期
            for (LocalDate date : allDates) {
                // 获取当前日期的周数
                int week = date.get(weekFields.weekOfYear());
                int abbreviatedYear = date.getYear() % 100; // 24
                String year = String.valueOf(abbreviatedYear); // "24"
                // 将日期添加到 distinctFirstList 中
                distinctFirstList.add(date.format(DateTimeFormatter.ofPattern("yy/M/d")));
                // 获取当天的数据
                List<DfAuditDetail> dailyDataForDate = dailyData2.getOrDefault(date, Collections.emptyList());

                AuditIssue auditIssue = createAuditIssueVo(dailyDataForDate,0); // 创建 createAuditIssueVo
                auditIssueList.add(auditIssue);


                if (startOfWeek == null || week != currentWeekOfYear) {
                    // 处理前一周的数据（非首次）
                    if (startOfWeek != null) {
                        addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, year+"Week " + currentWeekOfYear,0);
                        distinctFirstList.add(year+"Week " + currentWeekOfYear);
                        weekdfAuditDetail.clear(); // 清空当前周数据，准备新周
                    }
                    // 更新周信息
                    currentWeekOfYear = week;
                    startOfWeek = date;
                }

                // 将当天的数据加入当前周的数据列表
                weekdfAuditDetail.addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
            }

            // 处理最后一周的数据
//            if (!weekdfAuditDetail.isEmpty()) {
//                addWeeklyDataToSheet3Result(weekdfAuditDetail, auditIssueList, "Week " + startOfWeek.get(weekFields.weekOfYear()),0);
//                // 将最后一周的周数添加到 distinctFirstList 中
//                distinctFirstList.add("Week " + startOfWeek.get(weekFields.weekOfYear()));
//            }
            vo.setAuditIssues(auditIssueList);
            result.add(vo);
        }
    }

    /**
     * 创建 AuditIssue 对象，将当天的数据添加进去
     */
    private AuditIssue createAuditIssueVo(List<DfAuditDetail> dailyDataForDate,int totalIssues) {

        AuditIssue auditIssue = new AuditIssue();
        if (dailyDataForDate != null) {
            //
            // 处理问题点数，加入 null 判断
            Integer issuePoint = dailyDataForDate.stream()
                    .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getId 返回 null，用 0 代替
                    .sum();

            // 处理总问题数，加入 null 判断
//            Integer totalIssues = dailyDataForDate.stream()
//                    .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getTotalCount 返回 null，用 0 代替
//                    .sum();

            double issueRatio = (totalIssues > 0) ? (double) issuePoint / totalIssues : 0.0;

            //sheet4
            // 处理总问题点数，加入 null 判断
            Integer totalPoints = dailyDataForDate.stream()
                    .mapToInt(record -> record.getTotalPoints() != null ? record.getTotalPoints() : 0) // 如果 getTotalPoints 返回 null，用 0 代替
                    .sum();

            // 处理时效内的记录数，加入 null 判断
            Integer onTimePoints = dailyDataForDate.stream()
                    .mapToInt(record -> record.getOnTimePoints() != null ? record.getOnTimePoints() : 0) // 如果 getOnTimePoints 返回 null，用 0 代替
                    .sum();

            // 处理超时的记录数，加入 null 判断
            Integer timeoutPoints = dailyDataForDate.stream()
                    .mapToInt(record -> record.getTimeoutPoints() != null ? record.getTimeoutPoints() : 0) // 如果 getTimeoutPoints 返回 null，用 0 代替
                    .sum();


            double onTimePercentage = (totalPoints > 0) ? (double) onTimePoints / totalPoints : 0.0;
            double timeoutPercentage = (totalPoints > 0) ? (double) timeoutPoints / totalPoints : 0.0;

            //sheet5关闭
            // 处理已关闭记录数，加入 null 判断
            Integer closeRecords = dailyDataForDate.stream()
                    .mapToInt(record -> record.getClosedRecords() != null ? record.getClosedRecords() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();

            // 处理未关闭记录数，加入 null 判断
            Integer openRecords = dailyDataForDate.stream()
                    .mapToInt(record -> record.getOpenRecords() != null ? record.getOpenRecords() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                    .sum();

            double closeRate = (totalPoints > 0) ? (double) closeRecords / totalPoints : 0.0;


            //sheet7
            Integer Level1 = dailyDataForDate.stream()
                    .mapToInt(record -> record.getLevel1() != null ? record.getLevel1() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();

            Integer Level2 = dailyDataForDate.stream()
                    .mapToInt(record -> record.getLevel2() != null ? record.getLevel2() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                    .sum();

            Integer Level3 = dailyDataForDate.stream()
                    .mapToInt(record -> record.getLevel3() != null ? record.getLevel3() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                    .sum();

            double Level1Percentage = (totalPoints > 0) ? (double) Level1 / totalPoints : 0.0;
            double Level2Percentage = (totalPoints > 0) ? (double) Level2 / totalPoints : 0.0;
            double Level3Percentage = (totalPoints > 0) ? (double) Level3 / totalPoints : 0.0;


            //超时
            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setOnTimePoints(onTimePoints);
            auditIssue.setTimeoutPoints(timeoutPoints);
            auditIssue.setOnTimePercentage(onTimePercentage);
            auditIssue.setTimeoutPercentage(timeoutPercentage);

            //执行率
            auditIssue.setIssueRatio(issueRatio);
            auditIssue.setIssuePoint(issuePoint);
            auditIssue.setTotalIssues(totalIssues);

            //关闭
            auditIssue.setTargetCount(100);
            auditIssue.setCloseRecords(closeRecords);
            auditIssue.setOpenRecords(openRecords);
            auditIssue.setCloseRate(closeRate);

            //等级
            auditIssue.setLevel1(Level1);
            auditIssue.setLevel2(Level2);
            auditIssue.setLevel3(Level3);
            auditIssue.setLevel1Percentage(Level1Percentage);
            auditIssue.setLevel2Percentage(Level2Percentage);
            auditIssue.setLevel3Percentage(Level3Percentage);
        } else {
            auditIssue.setIssuePoint(0);
            auditIssue.setTotalIssues(0);
            auditIssue.setTotalPoints(0);
            auditIssue.setOnTimePoints(0);
            auditIssue.setTimeoutPoints(0);
            auditIssue.setOnTimePercentage(0);
            auditIssue.setTimeoutPercentage(0);
            auditIssue.setTargetCount(100);
            auditIssue.setCloseRecords(0);
            auditIssue.setOpenRecords(0);
            auditIssue.setCloseRate(0);
            auditIssue.setLevel1(0);
            auditIssue.setLevel2(0);
            auditIssue.setLevel3(0);
            auditIssue.setLevel1Percentage(0);
            auditIssue.setLevel2Percentage(0);
            auditIssue.setLevel3Percentage(0);
            // auditIssue.setIssueRatio(0.0);
        }
        return auditIssue;
    }

    /**
     * 创建 jcDataVo 对象，将当天的数据添加进去
     */
    private jcDataVo createJcDataVo(List<DfControlStandardStatus> dailyDataForDate) {

        jcDataVo jcData = new jcDataVo();
        if (dailyDataForDate != null) {
            // 示例：根据当天的 DfControlStandardStatus 创建 jcDataVo 对象
            Integer totalAuditCount = dailyDataForDate.stream().mapToInt(DfControlStandardStatus::getTotalCount).sum();
            Integer okCount = dailyDataForDate.stream().mapToInt(DfControlStandardStatus::getOkCount).sum();
            Integer ngCount = dailyDataForDate.stream().mapToInt(DfControlStandardStatus::getNgCount).sum();

            double executionRate = (totalAuditCount > 0) ? (double) okCount / totalAuditCount : 0.0;

            jcData.setTargetCount(99);  // 示例目标值
            jcData.setTotalAuditCount(totalAuditCount);
            jcData.setNotExecutedCount(ngCount);
            jcData.setExecutedCount(okCount);
            jcData.setExecutionRate(executionRate);
        } else {
            jcData.setTargetCount(99);
            jcData.setTotalAuditCount(0);
            jcData.setNotExecutedCount(0);
            jcData.setExecutedCount(0);
            jcData.setExecutionRate(0.0);
        }
        return jcData;
    }

    /**
     * 汇总每周的数据
     */
    private void addWeeklyDataToResult(List<DfControlStandardStatus> weeklyRecords, List<jcDataVo> jcDataVoList) {

        Integer totalAuditCount = weeklyRecords.stream().mapToInt(DfControlStandardStatus::getTotalCount).sum();
        Integer okCount = weeklyRecords.stream().mapToInt(DfControlStandardStatus::getOkCount).sum();
        Integer ngCount = weeklyRecords.stream().mapToInt(DfControlStandardStatus::getNgCount).sum();

        double executionRate = (totalAuditCount > 0) ? (double) okCount / totalAuditCount : 0.0;

        jcDataVo jcDataVo = new jcDataVo();
        jcDataVo.setTargetCount(99);  // 示例目标值
        jcDataVo.setTotalAuditCount(totalAuditCount);
        jcDataVo.setNotExecutedCount(ngCount);
        jcDataVo.setExecutedCount(okCount);
        jcDataVo.setExecutionRate(executionRate);
        jcDataVoList.add(jcDataVo);

    }

    /**
     * 汇总每周的数据
     */
    private void addWeeklyDataToSheet3Result(List<DfAuditDetail> weeklyRecords, List<AuditIssue> auditIssuesVoList, String week,int totalIssues) {

        AuditIssue auditIssue = new AuditIssue();

        //sheet1 执行率
        // 处理问题点数，加入 null 判断
        Integer issuePoint = weeklyRecords.stream()
                .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getId 返回 null，用 0 代替
                .sum();

        // 处理总问题数，加入 null 判断
//        Integer totalIssues = weeklyRecords.stream()
//                .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getTotalCount 返回 null，用 0 代替
//                .sum();

        double issueRatio = (totalIssues > 0) ? (double) issuePoint / totalIssues : 0.0;

        //sheet4
        // 处理总问题点数，加入 null 判断
        Integer totalPoints = weeklyRecords.stream()
                .mapToInt(record -> record.getTotalPoints() != null ? record.getTotalPoints() : 0) // 如果 getTotalPoints 返回 null，用 0 代替
                .sum();

        // 处理时效内的记录数，加入 null 判断
        Integer onTimePoints = weeklyRecords.stream()
                .mapToInt(record -> record.getOnTimePoints() != null ? record.getOnTimePoints() : 0) // 如果 getOnTimePoints 返回 null，用 0 代替
                .sum();

        // 处理超时的记录数，加入 null 判断
        Integer timeoutPoints = weeklyRecords.stream()
                .mapToInt(record -> record.getTimeoutPoints() != null ? record.getTimeoutPoints() : 0) // 如果 getTimeoutPoints 返回 null，用 0 代替
                .sum();


        double onTimePercentage = (totalPoints > 0) ? (double) onTimePoints / totalPoints : 0.0;
        double timeoutPercentage = (totalPoints > 0) ? (double) timeoutPoints / totalPoints : 0.0;

        //sheet5关闭
        // 处理已关闭记录数，加入 null 判断
        Integer closeRecords = weeklyRecords.stream()
                .mapToInt(record -> record.getClosedRecords() != null ? record.getClosedRecords() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                .sum();

        // 处理未关闭记录数，加入 null 判断
        Integer openRecords = weeklyRecords.stream()
                .mapToInt(record -> record.getOpenRecords() != null ? record.getOpenRecords() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                .sum();

        double closeRate = (totalPoints > 0) ? (double) closeRecords / totalPoints : 0.0;


        //sheet7
        Integer Level1 = weeklyRecords.stream()
                .mapToInt(record -> record.getLevel1() != null ? record.getLevel1() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                .sum();

        Integer Level2 = weeklyRecords.stream()
                .mapToInt(record -> record.getLevel2() != null ? record.getLevel2() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                .sum();

        Integer Level3 = weeklyRecords.stream()
                .mapToInt(record -> record.getLevel3() != null ? record.getLevel3() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                .sum();

        double Level1Percentage = (totalPoints > 0) ? (double) Level1 / totalPoints : 0.0;
        double Level2Percentage = (totalPoints > 0) ? (double) Level2 / totalPoints : 0.0;
        double Level3Percentage = (totalPoints > 0) ? (double) Level3 / totalPoints : 0.0;


        //超时
        auditIssue.setTotalPoints(totalPoints);
        auditIssue.setOnTimePoints(onTimePoints);
        auditIssue.setTimeoutPoints(timeoutPoints);
        auditIssue.setOnTimePercentage(onTimePercentage);
        auditIssue.setTimeoutPercentage(timeoutPercentage);

        //执行率
        auditIssue.setIssueRatio(issueRatio);
        auditIssue.setIssuePoint(issuePoint);
        auditIssue.setTotalIssues(totalIssues);

        //关闭
        auditIssue.setTargetCount(100);
        auditIssue.setCloseRecords(closeRecords);
        auditIssue.setOpenRecords(openRecords);
        auditIssue.setCloseRate(closeRate);

        //等级
        auditIssue.setLevel1(Level1);
        auditIssue.setLevel2(Level2);
        auditIssue.setLevel3(Level3);
        auditIssue.setLevel1Percentage(Level1Percentage);
        auditIssue.setLevel2Percentage(Level2Percentage);
        auditIssue.setLevel3Percentage(Level3Percentage);

        auditIssuesVoList.add(auditIssue);


    }


    // 处理月数据
    private void processMonthlyData(String project, String category, List<LocalDate> allDates,
                                    Map<LocalDate, List<DfControlStandardStatus>> dailyData, Map<LocalDate, List<DfAuditDetail>> dailyData2,
                                    List<DfExcelPortVo> result, String type,List<DfAuditDetail> alllist) {

        distinctFirstList.clear();
        switch (type) {
            case "sheet1":
                Sheet1(project, category, allDates, dailyData, result);
                break;
            case "sheet3":
                ExportSheet3(project, category, allDates, dailyData2, result,alllist);
                break;
            case "sheet4":
                ExportSheet4(project, category, allDates, dailyData2, result);
                break;
            case "sheet5":
                ExportSheet5(project, category, allDates, dailyData2, result);
                break;
            case "sheet7":
                ExportSheet7(project, category, allDates, dailyData2, result);
                break;
            default:
                // 可选：处理未知类型的情况
                break;
        }


    }

    private void ExportSheet7(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }

        // 遍历按月份分组的数据
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry : monthlyData.entrySet()) {
            Integer month = monthEntry.getKey();  // 获取月份
            Integer totalPoints = 0;
            Integer Level1 = 0;
            Integer Level2 = 0;
            Integer Level3 = 0;

            // 遍历当前月份内的周数
            for (Map.Entry<Integer, List<DfAuditDetail>> weekEntry : monthEntry.getValue().entrySet()) {
                Integer weekOfMonth = weekEntry.getKey();  // 获取周数
                List<DfAuditDetail> weeklyRecords = weekEntry.getValue();  // 获取该周的数据
                distinctFirstList.add(weekOfMonth + "周");

                // 按周处理数据
                addMonthlyDataToResultSheet3(weeklyRecords, auditIssues, weekOfMonth + "周" + month + "月",0);

                // 累加每月的数据
                totalPoints += weeklyRecords.stream().mapToInt(DfAuditDetail::getTotalPoints).sum();
                Level1 += weeklyRecords.stream().mapToInt(DfAuditDetail::getLevel1).sum();
                Level2 += weeklyRecords.stream().mapToInt(DfAuditDetail::getLevel2).sum();
                Level3 += weeklyRecords.stream().mapToInt(DfAuditDetail::getLevel3).sum();
            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double Level1Percentage = (totalPoints > 0) ? (double) Level1 / totalPoints : 0.0;
            double Level2Percentage = (totalPoints > 0) ? (double) Level2 / totalPoints : 0.0;
            double Level3Percentage = (totalPoints > 0) ? (double) Level3 / totalPoints : 0.0;


            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setLevel1(Level1);
            auditIssue.setLevel2(Level2);
            auditIssue.setLevel3(Level3);
            auditIssue.setLevel1Percentage(Level1Percentage);
            auditIssue.setLevel2Percentage(Level2Percentage);
            auditIssue.setLevel3Percentage(Level3Percentage);
            auditIssues.add(auditIssue);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(month + "月");
        }

        // 将结果添加到返回列表
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void ExportSheet5(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }

        // 遍历按月份分组的数据
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry : monthlyData.entrySet()) {
            Integer month = monthEntry.getKey();  // 获取月份
            Integer totalPoints = 0;
            Integer closedRecords = 0;
            Integer openRecords = 0;
            // 遍历当前月份内的周数
            for (Map.Entry<Integer, List<DfAuditDetail>> weekEntry : monthEntry.getValue().entrySet()) {
                Integer weekOfMonth = weekEntry.getKey();  // 获取周数
                List<DfAuditDetail> weeklyRecords = weekEntry.getValue();  // 获取该周的数据
                distinctFirstList.add(weekOfMonth + "周");

                // 按周处理数据
                addMonthlyDataToResultSheet3(weeklyRecords, auditIssues, weekOfMonth + "周" + month + "月",0);

                // 累加每月的数据
                totalPoints += weeklyRecords.stream().mapToInt(DfAuditDetail::getTotalPoints).sum();
                closedRecords += weeklyRecords.stream().mapToInt(DfAuditDetail::getClosedRecords).sum();
                openRecords += weeklyRecords.stream().mapToInt(DfAuditDetail::getOpenRecords).sum();

            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double closeRate = (totalPoints > 0) ? (double) closedRecords / totalPoints : 0.0;
            auditIssue.setTargetCount(100);

            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setCloseRecords(closedRecords);
            auditIssue.setOpenRecords(openRecords);
            auditIssue.setCloseRate(closeRate);
            auditIssues.add(auditIssue);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(month + "月");
        }

        // 将结果添加到返回列表
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void ExportSheet4(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }

        // 遍历按月份分组的数据
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry : monthlyData.entrySet()) {
            Integer month = monthEntry.getKey();  // 获取月份
            Integer totalIssues = 0;

            Integer totalPoints = 0;
            Integer onTimePoints = 0;
            Integer timeoutPoints = 0;
            // 遍历当前月份内的周数
            for (Map.Entry<Integer, List<DfAuditDetail>> weekEntry : monthEntry.getValue().entrySet()) {
                Integer weekOfMonth = weekEntry.getKey();  // 获取周数
                List<DfAuditDetail> weeklyRecords = weekEntry.getValue();  // 获取该周的数据
                distinctFirstList.add(weekOfMonth + "周");

                // 按周处理数据
                addMonthlyDataToResultSheet3(weeklyRecords, auditIssues, weekOfMonth + "周" + month + "月",0);

                // 累加每月的数据
                totalPoints += weeklyRecords.stream().mapToInt(DfAuditDetail::getTotalPoints).sum();
                onTimePoints += weeklyRecords.stream().mapToInt(DfAuditDetail::getOnTimePoints).sum();
                timeoutPoints += weeklyRecords.stream().mapToInt(DfAuditDetail::getTimeoutPoints).sum();


            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double onTimePercentage = (totalPoints > 0) ? (double) onTimePoints / totalPoints : 0.0;
            double timeoutPercentage = (totalPoints > 0) ? (double) timeoutPoints / totalPoints : 0.0;

            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setOnTimePoints(onTimePoints);
            auditIssue.setTimeoutPoints(timeoutPoints);
            auditIssue.setOnTimePercentage(onTimePercentage);
            auditIssue.setTimeoutPercentage(timeoutPercentage);
            auditIssues.add(auditIssue);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(month + "月");
        }

        // 将结果添加到返回列表
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void ExportSheet3(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result,List<DfAuditDetail> alllist) {
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfAuditDetail>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }


        //总记录数
        Map<Integer, Map<Integer, List<DfAuditDetail>>> DataAll = new TreeMap<>();
        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyDataAll = alllist.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            DataAll.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyDataAll.getOrDefault(date, Collections.emptyList()));
        }




        // 遍历按月份分组的数据
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> monthEntry : monthlyData.entrySet()) {
            Integer month = monthEntry.getKey();  // 获取月份
            // 获取年份，可以通过任意日期获取年份
            LocalDate date = allDates.get(0); // 假设 allDates 中包含至少一个日期
            Integer year = date.getYear();  // 获取年份
            Integer issuePoint = 0;
            Integer totalIssues = 0;
            Integer issueRatio = 0;
            // 遍历当前月份内的周数
            for (Map.Entry<Integer, List<DfAuditDetail>> weekEntry : monthEntry.getValue().entrySet()) {
                Integer weekOfMonth = weekEntry.getKey();  // 获取周数
                List<DfAuditDetail> weeklyRecords = weekEntry.getValue();  // 获取该周的数据
                distinctFirstList.add(weekOfMonth + "周");

                Map<Integer, List<DfAuditDetail>> weekData =  DataAll.get(month);
                List<DfAuditDetail> all = weekData.get(weekOfMonth);


                int totalIdSum = all.stream().mapToInt(DfAuditDetail::getTotalCount).sum();
                // 按周处理数据
                addMonthlyDataToResultSheet3(weeklyRecords, auditIssues, weekOfMonth + "周" + month + "月",totalIdSum);

                // 累加每月的数据
                issuePoint += weeklyRecords.stream().mapToInt(DfAuditDetail::getTotalCount).sum();
                totalIssues += totalIdSum;
            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double monthExecutionRate = (totalIssues > 0) ? (double) issuePoint / totalIssues : 0.0;
            auditIssue.setTotalIssues(totalIssues);
            auditIssue.setIssuePoint(issuePoint);
            auditIssue.setIssueRatio(monthExecutionRate);
            auditIssues.add(auditIssue);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(year+"年"+month + "月");
        }

        // 将结果添加到返回列表
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void Sheet1(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfControlStandardStatus>> dailyData, List<DfExcelPortVo> result) {
        List<jcDataVo> jcDataVoList = new ArrayList<>();

        // 按月分组的Map（key: 月份, value: 每月的数据）
        Map<Integer, Map<Integer, List<DfControlStandardStatus>>> monthlyData = new TreeMap<>();

        // 遍历所有日期
        for (LocalDate date : allDates) {
            // 获取月份（1-12）
            Integer month = date.getMonthValue();

            // 获取当前日期是该月的第几周
            Integer weekOfMonth = getWeekOfMonth(date);

            // 按月份和周数进行分组
            monthlyData.computeIfAbsent(month, k -> new TreeMap<>())
                    .computeIfAbsent(weekOfMonth, k -> new ArrayList<>())
                    .addAll(dailyData.getOrDefault(date, Collections.emptyList()));
        }

        // 遍历按月份分组的数据
        for (Map.Entry<Integer, Map<Integer, List<DfControlStandardStatus>>> monthEntry : monthlyData.entrySet()) {
            Integer month = monthEntry.getKey();  // 获取月份
            Integer totalAuditCount = 0;
            Integer okCount = 0;
            Integer ngCount = 0;
            // 遍历当前月份内的周数
            for (Map.Entry<Integer, List<DfControlStandardStatus>> weekEntry : monthEntry.getValue().entrySet()) {
                Integer weekOfMonth = weekEntry.getKey();  // 获取周数
                List<DfControlStandardStatus> weeklyRecords = weekEntry.getValue();  // 获取该周的数据
                distinctFirstList.add(weekOfMonth + "周");

                // 按周处理数据
                addMonthlyDataToResult(weeklyRecords, jcDataVoList);

                // 累加每月的数据
                totalAuditCount += weeklyRecords.stream().mapToInt(DfControlStandardStatus::getTotalCount).sum();
                okCount += weeklyRecords.stream().mapToInt(DfControlStandardStatus::getOkCount).sum();
                ngCount += weeklyRecords.stream().mapToInt(DfControlStandardStatus::getNgCount).sum();
            }
            // 将每月的数据汇总到 jcDataVoList 中
            jcDataVo monthDataVo = new jcDataVo();
            double monthExecutionRate = (totalAuditCount > 0) ? (double) okCount / totalAuditCount : 0.0;
            monthDataVo.setTargetCount(99);  // 示例目标值
            monthDataVo.setTotalAuditCount(totalAuditCount);
            monthDataVo.setNotExecutedCount(ngCount);
            monthDataVo.setExecutedCount(okCount);
            monthDataVo.setExecutionRate(monthExecutionRate);
            jcDataVoList.add(monthDataVo);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(month + "月");
        }

        // 将结果添加到返回列表
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setChildren(jcDataVoList);
        result.add(vo);
    }


    // 计算每个月的第几周
    private Integer getWeekOfMonth(LocalDate date) {
        // 获取该月的第一天
        LocalDate firstOfMonth = date.withDayOfMonth(1);

        // 计算该日期属于这个月的第几周
        int dayOfMonth = date.getDayOfMonth();
        int weekOfMonth = (int) Math.ceil((double) dayOfMonth / 7);  // 计算周数
        return weekOfMonth;
    }

    // sheet1汇总数据
    private void addMonthlyDataToResult(List<DfControlStandardStatus> monthlyRecords, List<jcDataVo> jcDataVoList) {

        if (jcDataVoList != null) {
            // 处理总审核数，加入 null 判断
            Integer totalAuditCount = monthlyRecords.stream()
                    .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getTotalCount 返回 null，用 0 代替
                    .sum();

            // 处理 ok 数量，加入 null 判断
            Integer okCount = monthlyRecords.stream()
                    .mapToInt(record -> record.getOkCount() != null ? record.getOkCount() : 0) // 如果 getOkCount 返回 null，用 0 代替
                    .sum();

            // 处理 ng 数量，加入 null 判断
            Integer ngCount = monthlyRecords.stream()
                    .mapToInt(record -> record.getNgCount() != null ? record.getNgCount() : 0) // 如果 getNgCount 返回 null，用 0 代替
                    .sum();

            double executionRate = (totalAuditCount > 0) ? (double) okCount / totalAuditCount : 0.0;

            jcDataVo jcDataVo = new jcDataVo();
            jcDataVo.setTargetCount(99);  // 示例目标值
            jcDataVo.setTotalAuditCount(totalAuditCount);
            jcDataVo.setNotExecutedCount(ngCount);
            jcDataVo.setExecutedCount(okCount);
            jcDataVo.setExecutionRate(executionRate);
            jcDataVoList.add(jcDataVo);
        } else {
            jcDataVo jcDataVo = new jcDataVo();
            jcDataVo.setTargetCount(99);  // 示例目标值
            jcDataVo.setTotalAuditCount(0);
            jcDataVo.setNotExecutedCount(0);
            jcDataVo.setExecutedCount(0);
            jcDataVo.setExecutionRate(0.0);
            jcDataVoList.add(jcDataVo);
        }

    }


    // sheet3汇总数据
    private void addMonthlyDataToResultSheet3(List<DfAuditDetail> monthlyRecords, List<AuditIssue> auditIssues, String week,int totalIssues) {

        if (monthlyRecords != null && monthlyRecords.size() > 0) {
            AuditIssue auditIssue = new AuditIssue();
            //sheet1 执行率
            // 处理问题点数，加入 null 判断
            Integer issuePoint = monthlyRecords.stream()
                    .mapToInt(record -> record.getTotalCount() != null ? record.getTotalCount() : 0) // 如果 getId 返回 null，用 0 代替
                    .sum();

            double issueRatio = (totalIssues > 0) ? (double) issuePoint / totalIssues : 0.0;

            //sheet4
            // 处理总问题点数，加入 null 判断
            Integer totalPoints = monthlyRecords.stream()
                    .mapToInt(record -> record.getTotalPoints() != null ? record.getTotalPoints() : 0) // 如果 getTotalPoints 返回 null，用 0 代替
                    .sum();

            // 处理时效内的记录数，加入 null 判断
            Integer onTimePoints = monthlyRecords.stream()
                    .mapToInt(record -> record.getOnTimePoints() != null ? record.getOnTimePoints() : 0) // 如果 getOnTimePoints 返回 null，用 0 代替
                    .sum();

            // 处理超时的记录数，加入 null 判断
            Integer timeoutPoints = monthlyRecords.stream()
                    .mapToInt(record -> record.getTimeoutPoints() != null ? record.getTimeoutPoints() : 0) // 如果 getTimeoutPoints 返回 null，用 0 代替
                    .sum();


            double onTimePercentage = (totalPoints > 0) ? (double) onTimePoints / totalPoints : 0.0;
            double timeoutPercentage = (totalPoints > 0) ? (double) timeoutPoints / totalPoints : 0.0;

            //sheet5关闭
            // 处理已关闭记录数，加入 null 判断
            Integer closeRecords = monthlyRecords.stream()
                    .mapToInt(record -> record.getClosedRecords() != null ? record.getClosedRecords() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();

            // 处理未关闭记录数，加入 null 判断
            Integer openRecords = monthlyRecords.stream()
                    .mapToInt(record -> record.getOpenRecords() != null ? record.getOpenRecords() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                    .sum();

            double closeRate = (totalPoints > 0) ? (double) closeRecords / totalPoints : 0.0;


            //sheet7
            Integer Level1 = monthlyRecords.stream()
                    .mapToInt(record -> record.getLevel1() != null ? record.getLevel1() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();
            Integer Level2 = monthlyRecords.stream()
                    .mapToInt(record -> record.getLevel2() != null ? record.getLevel2() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();
            Integer Level3 = monthlyRecords.stream()
                    .mapToInt(record -> record.getLevel3() != null ? record.getLevel3() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                    .sum();

            double Level1Percentage = (totalPoints > 0) ? (double) Level1 / totalPoints : 0.0;
            double Level2Percentage = (totalPoints > 0) ? (double) Level2 / totalPoints : 0.0;
            double Level3Percentage = (totalPoints > 0) ? (double) Level3 / totalPoints : 0.0;


            //超时
            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setOnTimePoints(onTimePoints);
            auditIssue.setTimeoutPoints(timeoutPoints);
            auditIssue.setOnTimePercentage(onTimePercentage);
            auditIssue.setTimeoutPercentage(timeoutPercentage);

            //执行率
            auditIssue.setIssueRatio(issueRatio);
            auditIssue.setIssuePoint(issuePoint);
            auditIssue.setTotalIssues(totalIssues);

            //关闭
            auditIssue.setTargetCount(100);
            auditIssue.setCloseRecords(closeRecords);
            auditIssue.setOpenRecords(openRecords);
            auditIssue.setCloseRate(closeRate);

            //等级
            auditIssue.setLevel1(Level1);
            auditIssue.setLevel2(Level2);
            auditIssue.setLevel3(Level3);
            auditIssue.setLevel1Percentage(Level1Percentage);
            auditIssue.setLevel2Percentage(Level2Percentage);
            auditIssue.setLevel3Percentage(Level3Percentage);

            auditIssues.add(auditIssue);
//            if (issuePointList.containsKey(String.valueOf(week))) {
//                if (totalIssues != 0) {
//                    issuePointList.put(String.valueOf(week), totalIssues);
//                }
//            } else {
//                issuePointList.put(String.valueOf(week), totalIssues);
//            }
        } else {
            AuditIssue auditIssue = new AuditIssue();
            auditIssue.setIssuePoint(0);
            auditIssue.setIssueRatio(0);

            auditIssue.setTotalPoints(0);
            auditIssue.setOnTimePoints(0);
            auditIssue.setTimeoutPoints(0);
            auditIssue.setOnTimePercentage(0);
            auditIssue.setTimeoutPercentage(0);

            //关闭
            auditIssue.setCloseRecords(0);
            auditIssue.setOpenRecords(0);
            auditIssue.setCloseRate(0);
            auditIssue.setTargetCount(100);

            auditIssue.setLevel1(0);
            auditIssue.setLevel2(0);
            auditIssue.setLevel3(0);
            auditIssue.setLevel1Percentage(0);
            auditIssue.setLevel2Percentage(0);
            auditIssue.setLevel3Percentage(0);

            auditIssues.add(auditIssue);
//            if (!issuePointList.containsKey(String.valueOf(week))) {
//                issuePointList.put(String.valueOf(week), 0);
//            }

        }

    }


    /**
     * 处理年数据，按月展示并统计每年数据
     */
    private void processYearlyData(String project, String category, List<LocalDate> allDates,
                                   Map<LocalDate, List<DfControlStandardStatus>> dailyData, Map<LocalDate, List<DfAuditDetail>> dailyData2,
                                   List<DfExcelPortVo> result, String type,List<DfAuditDetail> alllist) {

        distinctFirstList.clear();
        switch (type) {
            case "sheet1":
                YearSheet1(project, category, allDates, dailyData, result);
                break;
            case "sheet3":
                YearSheet3(project, category, allDates, dailyData2, result,alllist);
                break;
            case "sheet4":
                YearSheet4(project, category, allDates, dailyData2, result);
                break;
            case "sheet5":
                YearSheet5(project, category, allDates, dailyData2, result);
                break;
            case "sheet7":
                YearSheet7(project, category, allDates, dailyData2, result);
                break;
            default:
                // Handle the case when none of the types match
                break;
        }


    }

    private void YearSheet7(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        // 按年展示数据并统计每年
        Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = new TreeMap<>();
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按年和月将数据分组
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }


        // 遍历每年的数据并按月汇总
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry : yearlyData.entrySet()) {
            // 存储年、月的汇总信息
            Integer totalPoints = 0;
            Integer Level1 = 0;
            Integer Level2 = 0;
            Integer Level3 = 0;
            Integer year = yearEntry.getKey();  // 获取年份
            Map<Integer, List<DfAuditDetail>> monthlyRecords = yearEntry.getValue();  // 获取该年的数据

            // 对每个月的数据进行汇总
            for (Map.Entry<Integer, List<DfAuditDetail>> monthEntry : monthlyRecords.entrySet()) {
                Integer month = monthEntry.getKey();  // 获取月份
                distinctFirstList.add(month + "月"); // 添加月份到 distinctFirstList
                List<DfAuditDetail> monthlyData = monthEntry.getValue();  // 获取该月的数据

                // 汇总该月的数据
                addMonthlyDataToResultSheet3(monthlyData, auditIssues, month + "月" + year + "年",0);

                // 累加每月的数据

                totalPoints += monthlyData.stream()
                        .mapToInt(record -> record.getTotalPoints() != null ? record.getTotalPoints() : 0) // 如果 getTotalPoints 返回 null，用 0 代替
                        .sum();
                Level1 += monthlyData.stream()
                        .mapToInt(record -> record.getLevel1() != null ? record.getLevel1() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                        .sum();
                Level2 += monthlyData.stream()
                        .mapToInt(record -> record.getLevel2() != null ? record.getLevel2() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                        .sum();
                Level3 += monthlyData.stream()
                        .mapToInt(record -> record.getLevel3() != null ? record.getLevel3() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                        .sum();
            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double Level1Percentage = (totalPoints > 0) ? (double) Level1 / totalPoints : 0.0;
            double Level2Percentage = (totalPoints > 0) ? (double) Level2 / totalPoints : 0.0;
            double Level3Percentage = (totalPoints > 0) ? (double) Level3 / totalPoints : 0.0;


            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setLevel1(Level1);
            auditIssue.setLevel2(Level2);
            auditIssue.setLevel3(Level3);
            auditIssue.setLevel1Percentage(Level1Percentage);
            auditIssue.setLevel2Percentage(Level2Percentage);
            auditIssue.setLevel3Percentage(Level3Percentage);
            auditIssues.add(auditIssue);
            // 将月份添加到 distinctFirstList 中
            distinctFirstList.add(year + "年");
        }

        // 最终将汇总的结果放到返回的列表中
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void YearSheet5(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        // 按年展示数据并统计每年
        Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = new TreeMap<>();
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按年和月将数据分组
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }


        // 遍历每年的数据并按月汇总
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry : yearlyData.entrySet()) {
            // 存储年、月的汇总信息
            Integer totalPoints = 0;
            Integer targetCount = 100;
            Integer closeRecords = 0;
            Integer openRecords = 0;
            Integer year = yearEntry.getKey();  // 获取年份
            Map<Integer, List<DfAuditDetail>> monthlyRecords = yearEntry.getValue();  // 获取该年的数据

            // 对每个月的数据进行汇总
            for (Map.Entry<Integer, List<DfAuditDetail>> monthEntry : monthlyRecords.entrySet()) {
                Integer month = monthEntry.getKey();  // 获取月份
                distinctFirstList.add(month + "月"); // 添加月份到 distinctFirstList
                List<DfAuditDetail> monthlyData = monthEntry.getValue();  // 获取该月的数据

                // 汇总该月的数据
                addMonthlyDataToResultSheet3(monthlyData, auditIssues, month + "月" + year + "年",0);

                // 累加每月的数据
                totalPoints += monthlyData.stream().mapToInt(DfAuditDetail::getTotalPoints).sum();
                closeRecords += monthlyData.stream()
                        .mapToInt(record -> record.getClosedRecords() != null ? record.getClosedRecords() : 0) // 如果 getClosedRecords 返回 null，用 0 代替
                        .sum();

                // 处理未关闭记录数，加入 null 判断
                openRecords += monthlyData.stream()
                        .mapToInt(record -> record.getOpenRecords() != null ? record.getOpenRecords() : 0) // 如果 getOpenRecords 返回 null，用 0 代替
                        .sum();
            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double closeRate = (totalPoints > 0) ? (double) closeRecords / totalPoints : 0.0;

            auditIssue.setTargetCount(targetCount);
            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setCloseRecords(closeRecords);
            auditIssue.setOpenRecords(openRecords);
            auditIssue.setCloseRate(closeRate);
            auditIssues.add(auditIssue);
            // 将年份添加到 distinctFirstList 中
            distinctFirstList.add(year + "年");
        }

        // 最终将汇总的结果放到返回的列表中
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void YearSheet4(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result) {
        // 按年展示数据并统计每年
        Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = new TreeMap<>();
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按年和月将数据分组
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }


        // 遍历每年的数据并按月汇总
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry : yearlyData.entrySet()) {
            // 存储年、月的汇总信息
            Integer totalPoints = 0;
            Integer onTimePoints = 0;
            Integer timeoutPoints = 0;
            Integer year = yearEntry.getKey();  // 获取年份
            Map<Integer, List<DfAuditDetail>> monthlyRecords = yearEntry.getValue();  // 获取该年的数据

            // 对每个月的数据进行汇总
            for (Map.Entry<Integer, List<DfAuditDetail>> monthEntry : monthlyRecords.entrySet()) {
                Integer month = monthEntry.getKey();  // 获取月份
                distinctFirstList.add(month + "月"); // 添加月份到 distinctFirstList
                List<DfAuditDetail> monthlyData = monthEntry.getValue();  // 获取该月的数据

                // 汇总该月的数据
                addMonthlyDataToResultSheet3(monthlyData, auditIssues, month + "月" + year + "年",0);

                // 累加每月的数据
                totalPoints += monthlyData.stream().mapToInt(DfAuditDetail::getTotalPoints).sum();
                onTimePoints += monthlyData.stream().mapToInt(DfAuditDetail::getOnTimePoints).sum();
                timeoutPoints += monthlyData.stream().mapToInt(DfAuditDetail::getTimeoutPoints).sum();
            }
            // 将每月的数据汇总到 auditIssues 中
            AuditIssue auditIssue = new AuditIssue();
            double onTimePercentage = (totalPoints > 0) ? (double) onTimePoints / totalPoints : 0.0;
            double timeoutPercentage = (totalPoints > 0) ? (double) timeoutPoints / totalPoints : 0.0;

            auditIssue.setTotalPoints(totalPoints);
            auditIssue.setOnTimePoints(onTimePoints);
            auditIssue.setTimeoutPoints(timeoutPoints);
            auditIssue.setOnTimePercentage(onTimePercentage);
            auditIssue.setTimeoutPercentage(timeoutPercentage);
            auditIssues.add(auditIssue);

            // 将年份添加到 distinctFirstList 中
            distinctFirstList.add(year + "年");

            if (!issuePointList.containsKey(year + "年")) {
                issuePointList.put(year + "年", 0);
            }
        }

        // 最终将汇总的结果放到返回的列表中
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void YearSheet3(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfAuditDetail>> dailyData2, List<DfExcelPortVo> result,List<DfAuditDetail> alllist) {
        // 按年展示数据并统计每年
        Map<Integer, Map<Integer, List<DfAuditDetail>>> yearlyData = new TreeMap<>();
        List<AuditIssue> auditIssues = new ArrayList<>();

        // 按年和月将数据分组
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData2.getOrDefault(date, Collections.emptyList()));
        }

        //总记录数
        Map<Integer, Map<Integer, List<DfAuditDetail>>> DataAll = new TreeMap<>();
        // 按日期分组数据
        Map<LocalDate, List<DfAuditDetail>> dailyDataAll = alllist.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        // 遍历所有日期
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 按月份和周数进行分组
            DataAll.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyDataAll.getOrDefault(date, Collections.emptyList()));
        }


        // 遍历每年的数据并按月汇总
        for (Map.Entry<Integer, Map<Integer, List<DfAuditDetail>>> yearEntry : yearlyData.entrySet()) {
            // 存储年、月的汇总信息
            Integer totalIssues = 0;
            Integer issuePoint = 0;
            Integer year = yearEntry.getKey();  // 获取年份
            Map<Integer, List<DfAuditDetail>> monthlyRecords = yearEntry.getValue();  // 获取该年的数据

            // 对每个月的数据进行汇总
            for (Map.Entry<Integer, List<DfAuditDetail>> monthEntry : monthlyRecords.entrySet()) {
                Integer month = monthEntry.getKey();  // 获取月份
                distinctFirstList.add(month + "月"); // 添加月份到 distinctFirstList
                List<DfAuditDetail> monthlyData = monthEntry.getValue();  // 获取该月的数据

                Map<Integer, List<DfAuditDetail>> monthData = DataAll.get(year);

                List<DfAuditDetail> all = monthData.get(month);
                int totalIdSum = all.stream()
                        .mapToInt(detail -> detail.getTotalCount() == null ? 0 : detail.getTotalCount())
                        .sum();
                // 汇总该月的数据
                addMonthlyDataToResultSheet3(monthlyData, auditIssues, month + "月" + year + "年",totalIdSum);

                // 累加全年的数据
                issuePoint += monthlyData.stream().mapToInt(detail -> detail.getTotalCount() == null ? 0 : detail.getTotalCount()).sum();

                totalIssues += totalIdSum;

            }

            // 将整个年份的数据汇总到 jcDataVoList 中
            AuditIssue auditIssue = new AuditIssue();
            double yearExecutionRate = (totalIssues > 0) ? (double) issuePoint / totalIssues : 0.0;
            auditIssue.setTotalIssues(totalIssues);  // 示例目标值
            auditIssue.setIssuePoint(issuePoint);
            auditIssue.setIssueRatio(yearExecutionRate);
            auditIssues.add(auditIssue);

            // 将年份添加到 distinctFirstList 中
            distinctFirstList.add(year + "年");

        }

        // 最终将汇总的结果放到返回的列表中
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setAuditIssues(auditIssues);
        result.add(vo);
    }

    private void YearSheet1(String project, String category, List<LocalDate> allDates, Map<LocalDate, List<DfControlStandardStatus>> dailyData, List<DfExcelPortVo> result) {
        // 按年展示数据并统计每年
        Map<Integer, Map<Integer, List<DfControlStandardStatus>>> yearlyData = new TreeMap<>();
        List<jcDataVo> jcDataVoList = new ArrayList<>();

        // 按年和月将数据分组
        for (LocalDate date : allDates) {
            Integer year = date.getYear();  // 获取年份
            Integer month = date.getMonthValue();  // 获取月份

            // 将每日的数据添加到对应的年和月分组
            yearlyData.computeIfAbsent(year, k -> new TreeMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .addAll(dailyData.getOrDefault(date, Collections.emptyList()));
        }


        // 遍历每年的数据并按月汇总
        for (Map.Entry<Integer, Map<Integer, List<DfControlStandardStatus>>> yearEntry : yearlyData.entrySet()) {
            // 存储年、月的汇总信息
            Integer totalAuditCount = 0;
            Integer okCount = 0;
            Integer ngCount = 0;
            Integer year = yearEntry.getKey();  // 获取年份
            Map<Integer, List<DfControlStandardStatus>> monthlyRecords = yearEntry.getValue();  // 获取该年的数据

            // 对每个月的数据进行汇总
            for (Map.Entry<Integer, List<DfControlStandardStatus>> monthEntry : monthlyRecords.entrySet()) {
                Integer month = monthEntry.getKey();  // 获取月份
                distinctFirstList.add(month + "月");  // 添加月份到 distinctFirstList
                List<DfControlStandardStatus> monthlyData = monthEntry.getValue();  // 获取该月的数据

                // 汇总该月的数据
                addMonthlyDataToResult(monthlyData, jcDataVoList);

                // 累加全年的数据
                totalAuditCount += monthlyData.stream().mapToInt(DfControlStandardStatus::getTotalCount).sum();
                okCount += monthlyData.stream().mapToInt(DfControlStandardStatus::getOkCount).sum();
                ngCount += monthlyData.stream().mapToInt(DfControlStandardStatus::getNgCount).sum();
            }

            // 将整个年份的数据汇总到 jcDataVoList 中
            jcDataVo yearDataVo = new jcDataVo();
            double yearExecutionRate = (totalAuditCount > 0) ? (double) okCount / totalAuditCount : 0.0;
            yearDataVo.setTargetCount(99);  // 示例目标值
            yearDataVo.setTotalAuditCount(totalAuditCount);
            yearDataVo.setNotExecutedCount(ngCount);
            yearDataVo.setExecutedCount(okCount);
            yearDataVo.setExecutionRate(yearExecutionRate);
            jcDataVoList.add(yearDataVo);

            // 将年份添加到 distinctFirstList 中
            distinctFirstList.add(year + "年");
        }

        // 最终将汇总的结果放到返回的列表中
        DfExcelPortVo vo = new DfExcelPortVo();
        vo.setFac(fac);
        vo.setfBigpro(project);
        vo.setCreateName(category);
        vo.setChildren(jcDataVoList);
        result.add(vo);
    }


    private static Map<String, Integer> createSheet1Table(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfExcelPortVo> datas) {
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = firstRow;

        // 创建表头
        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        // 定义表头内容
        String[] headers = {"厂别", "项目", "类别", "日期"};

        // 创建基本表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(firstCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellParamStyle4);
        }

        // 在"日期"之后创建 distinctFirstList 的单元格
        int additionalCol = firstCol + headers.length; // 从表头最后一列之后开始追加
        for (String value : distinctFirstList) {
            Cell cell = headerRow.createCell(additionalCol);
            cell.setCellValue(value); // distinctFirstList 的值作为表头
            cell.setCellStyle(cellParamStyle4);
            additionalCol++;
        }

        totalRow++; // 表头结束后移动到下一行
        int col = additionalCol;

        // 创建表格内容
        for (DfExcelPortVo table : datas) {
            int startRow = totalRow; // 当前表格起始行

            List<jcDataVo> bbList = table.getChildren(); // 获取数据列表

            // 填充第一列到第四列
            for (int i = 0; i < 5; i++) {
                Row row = sheet.getRow(totalRow);
                if (row == null) {
                    row = sheet.createRow(totalRow);
                }

                Cell cell0 = row.createCell(firstCol);
                cell0.setCellValue(table.getFac()); // 厂别
                cell0.setCellStyle(cellParamStyle4);

                Cell cell1 = row.createCell(firstCol + 1);
                cell1.setCellValue(table.getfBigpro()); // 项目
                cell1.setCellStyle(cellParamStyle4);

                Cell cell2 = row.createCell(firstCol + 2);
                cell2.setCellValue(table.getCreateName()); // 类别
                cell2.setCellStyle(cellParamStyle4);

                totalRow++;
            }

            // 填充数据部分
            int dataStartCol = firstCol + 3; // 从第3列开始填充数据
            for (int i = 0; i < 5; i++) { // 填充目标、问题总数、已执行、未执行、执行率
                Row row = sheet.getRow(startRow + i);
                if (row == null) {
                    row = sheet.createRow(startRow + i);
                }
                Cell titleCell = row.createCell(dataStartCol);
                switch (i) {
                    case 0:
                        titleCell.setCellValue("目标");
                        break;
                    case 1:
                        titleCell.setCellValue("稽核总数");
                        break;
                    case 2:
                        titleCell.setCellValue("已执行");
                        break;
                    case 3:
                        titleCell.setCellValue("未执行");
                        break;
                    case 4:
                        titleCell.setCellValue("执行率");
                        break;
                }
                titleCell.setCellStyle(cellParamStyle4);

                for (int j = 0; j < bbList.size(); j++) {
                    jcDataVo bb = bbList.get(j);
                    Cell dataCell = row.createCell(dataStartCol + j + 1);
                    switch (i) {
                        case 0:
                            if ("MIL".equals(table.getCreateName())){
                                dataCell.setCellValue("96%"); // 目标
                            }else{
                                dataCell.setCellValue("98%");
                            }
                          //  dataCell.setCellValue(bb.getTargetCount() + "%"); // 目标
                            break;
                        case 1:
                            dataCell.setCellValue(bb.getTotalAuditCount()); // 问题总数
                            break;
                        case 2:
                            dataCell.setCellValue(bb.getExecutedCount()); // 已执行
                            break;
                        case 3:
                            dataCell.setCellValue(bb.getNotExecutedCount()); // 未执行
                            break;
                        case 4:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getExecutionRate() * 100)); // 执行率
                            break;
                    }
                    dataCell.setCellStyle(cellParamStyle4);
                }
            }

            // 合并单元格
            for (int i = 0; i < 3; i++) { // 合并厂别、项目、类别和日期
                sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, firstCol + i, firstCol + i));
            }
        }

        map.put("row", totalRow);
        map.put("col", col);

        return map;
    }

    private static Map<String, Integer> createSheet3Table(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfExcelPortVo> datas) {
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = firstRow;

        // 创建表头
        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        // 定义表头内容
        String[] headers = {"厂别", "项目", "类别"};

        // 创建基本表头
        for (int i = 0; i < headers.length; i++) {
            // 合并厂别、项目、类别三列单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
            Cell cell = headerRow.createCell(firstCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellParamStyle4);
        }
        // 定义表头内容
        String date = "日期";
        int additionalCol = firstCol + headers.length; // 从表头最后一列之后开始追加
        Cell cell = headerRow.createCell(additionalCol);
        cell.setCellStyle(cellParamStyle4);
        cell.setCellValue(date); //

        totalRow++;
        String wt = "总问题";
        Row headerRow2 = sheet.createRow(totalRow);
        Cell cell22 = headerRow2.createCell(additionalCol);
        cell22.setCellValue(wt); //
        cell22.setCellStyle(cellParamStyle4);

        // 在"日期"之后创建 distinctFirstList 的单元格
        additionalCol = firstCol + headers.length + 1; // 从表头最后一列之后开始追加
        for (String value : distinctFirstList) {
            Cell cell3 = headerRow.createCell(additionalCol);
            cell3.setCellValue(value); // distinctFirstList 的值作为表头
            cell3.setCellStyle(cellParamStyle4);
            additionalCol++;
        }

        totalRow++; // 表头结束后移动到下一行
        int col = additionalCol;

        // 创建表格内容
        List<Integer> issueTotalCount = new ArrayList<>(Collections.nCopies(distinctFirstList.size(), 0)); // 初始化每列总和为0
        for (DfExcelPortVo table : datas) {
            int startRow = totalRow; // 当前表格起始行

            List<AuditIssue> bbList = table.getAuditIssues(); // 获取数据列表

            // 填充第一列到第四列
            for (int i = 0; i < 2; i++) {
                Row row = sheet.getRow(totalRow);
                if (row == null) {
                    row = sheet.createRow(totalRow);
                }

                Cell cell0 = row.createCell(firstCol);
                cell0.setCellValue(table.getFac()); // 厂别
                cell0.setCellStyle(cellParamStyle4);

                Cell cell1 = row.createCell(firstCol + 1);
                cell1.setCellValue(table.getfBigpro()); // 项目
                cell1.setCellStyle(cellParamStyle4);

                Cell cell2 = row.createCell(firstCol + 2);
                cell2.setCellValue(table.getCreateName()); // 类别
                cell2.setCellStyle(cellParamStyle4);

                totalRow++;
            }

            // 填充数据部分
            int dataStartCol = firstCol + 3; // 从第3列开始填充数据
            for (int i = 0; i < 2; i++) { // 填充目标、问题总数、已执行、未执行、执行率
                Row row = sheet.getRow(startRow + i);
                if (row == null) {
                    row = sheet.createRow(startRow + i);
                }
                Cell titleCell = row.createCell(dataStartCol);
                switch (i) {
                    case 0:
                        titleCell.setCellValue("问题点");
                        break;
                    case 1:
                        titleCell.setCellValue("占比");
                        break;
                }
                titleCell.setCellStyle(cellParamStyle4);

                for (int j = 0; j < bbList.size(); j++) {
                    AuditIssue bb = bbList.get(j);
                    Cell dataCell = row.createCell(dataStartCol + j + 1);
                    switch (i) {
                        case 0:
                            dataCell.setCellValue(bb.getIssuePoint()); // 问题
                            // 累加每列问题点数
                            issueTotalCount.set(j, issueTotalCount.get(j) + bb.getIssuePoint());
                            break;
                        case 1:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getIssueRatio() * 100)); // 占比
                            break;
                    }
                    dataCell.setCellStyle(cellParamStyle4);
                }
            }

            // 合并单元格
            for (int i = 0; i < 3; i++) { // 合并厂别、项目、类别和日期
                sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, firstCol + i, firstCol + i));
            }
        }

        // 在"总问题"行填充问题点数的总和
        Row totalRowCell = sheet.getRow(1); // 获取“总问题”行
        if (totalRowCell == null) {
            totalRowCell = sheet.createRow(totalRow); // 如果“总问题”行不存在，则创建
        }
        totalRowCell.createCell(3).setCellValue("总问题");
        totalRowCell.getCell(3).setCellStyle(cellParamStyle4);

        int totalCol = 4;
        for (int i = 0; i < issueTotalCount.size(); i++) {
            Cell sumCell = totalRowCell.createCell(totalCol + i);
            sumCell.setCellValue(issueTotalCount.get(i)); // 填充每列的问题点数总和
            sumCell.setCellStyle(cellParamStyle4);
        }

        map.put("row", totalRow + 1); // 总和行的下一行
        map.put("col", col);

        return map;
    }




    private static Map<String, Integer> createSheet2Table(SXSSFWorkbook wb, XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfControlStandardStatus> datas) {

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = 1;

        Integer totalSum = 0;
        Integer okSum = 0;
        Integer ngSum = 0;
        double rateSum = 0.0;

        for (int i = firstCol; i < firstCol + 6 + datas.size(); i++) {
            sheet.setColumnWidth(i, 500); // 自动适应列宽
        }
        XSSFCellStyle Style = createCellStyle(wb);
        XSSFCellStyle Style1 = createCellStyle1(wb);
        XSSFCellStyle Style2 = createCellStyle2(wb);

        // 创建字体样式
        XSSFFont headerFont = (XSSFFont) wb.createFont();
        headerFont.setFontHeightInPoints((short) 14); // 设置字体大小为14
        headerFont.setBold(true); // 设置字体加粗

        // 创建样式
        XSSFCellStyle headerStyle = (XSSFCellStyle) wb.createCellStyle();
        headerStyle.setFont(headerFont); // 将字体设置到样式上
        headerStyle.setAlignment(HorizontalAlignment.LEFT); // 设置文字居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中

        // 在表格上方插入一行并设置文字
        Row headerRow = sheet.createRow(0); //
        Cell headerCell = headerRow.createCell(0); // 在这一行的第一个单元格中添加文字
        headerCell.setCellValue("中央IPQC排查各部门问题点汇总-" + fac); // 设置表头文字
        headerCell.setCellStyle(headerStyle); // 应用字体样式

        // 合并这一行的所有列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, firstCol + 6 + datas.size()));

        // 创建表格内容
        int startRow = totalRow; // 当前表格起始行
        // 填充数据部分
        int dataStartCol = firstCol; // 从第3列开始填充数据
        for (int i = 0; i < 6; i++) { // 填充目标、问题总数、已执行、未执行、执行率
            Row row = sheet.getRow(startRow + i);
            if (row == null) {
                row = sheet.createRow(startRow + i);
            }
            Cell titleCell = row.createCell(dataStartCol);
            switch (i) {
                case 0:
                    titleCell.setCellValue("DRI");
                    titleCell.setCellStyle(Style);
                    break;
                case 1:
                    titleCell.setCellValue("工序");
                    titleCell.setCellStyle(Style1);
                    break;
                case 2:
                    titleCell.setCellValue("稽核项");
                    titleCell.setCellStyle(cellParamStyle4);
                    break;
                case 3:
                    titleCell.setCellValue("符合");
                    titleCell.setCellStyle(cellParamStyle4);
                    break;
                case 4:
                    titleCell.setCellValue("不符合");
                    titleCell.setCellStyle(Style2);
                    break;
                case 5:
                    titleCell.setCellValue("符合率");
                    titleCell.setCellStyle(cellParamStyle4);
                    break;
            }

            for (int j = 0; j < datas.size(); j++) {
                DfControlStandardStatus bb = datas.get(j);
                Cell dataCell = row.createCell(dataStartCol + j + 1);
                switch (i) {
                    case 0:
                        dataCell.setCellValue(bb.getProcessDrl()); // 负责人
                        dataCell.setCellStyle(Style);
                        break;
                    case 1:
                        dataCell.setCellValue(bb.getProcessCode()); // 工序
                        dataCell.setCellStyle(Style1);
                        break;
                    case 2:
                        dataCell.setCellValue(bb.getTotalCount()); // 问题总数
                        dataCell.setCellStyle(cellParamStyle4);
                        totalSum += bb.getTotalCount();
                        break;
                    case 3:
                        dataCell.setCellValue(bb.getOkCount()); // 符合
                        dataCell.setCellStyle(cellParamStyle4);
                        okSum += bb.getOkCount();
                        break;
                    case 4:
                        dataCell.setCellValue(bb.getNgCount()); // 不符合
                        dataCell.setCellStyle(Style2);
                        ngSum += bb.getNgCount();
                        break;
                    case 5:
                        dataCell.setCellValue(String.format("%.2f%%", bb.getOkRate() * 100)); // 符合率
                        XSSFCellStyle rateCellStyle = (XSSFCellStyle) wb.createCellStyle();
                        rateCellStyle.cloneStyleFrom(cellParamStyle4);

                        // 根据符合率设置背景颜色
                        if (bb.getOkRate() == 1.0) { // 100%
                            rateCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                        } else if (bb.getOkRate() >= 0.95) { // 95% 到 100%
                            rateCellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                        } else { // 95% 以下
                            rateCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                        }
                        rateCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        dataCell.setCellStyle(rateCellStyle);
                        break;
                }
            }
        }

        int lastColumn = firstCol + datas.size(); // 获取合计列的位置

        for (int k = 1; k < 7; k++) { // 填充目标、问题总数、已执行、未执行、执行率
            if (k == 1) {
                Row rows = sheet.getRow(k);
                sheet.addMergedRegion(new CellRangeAddress(1, 2, lastColumn + 1, lastColumn + 1));
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                dataCell2.setCellValue("合计"); //
                dataCell2.setCellStyle(cellParamStyle4);
            } else if (k == 2) {
                Row rows = sheet.getRow(k);
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                dataCell2.setCellValue(""); //
                dataCell2.setCellStyle(cellParamStyle4);
            } else if (k == 3) {
                Row rows = sheet.getRow(k);
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                dataCell2.setCellValue(totalSum); //
                dataCell2.setCellStyle(cellParamStyle4);
            } else if (k == 4) {
                Row rows = sheet.getRow(k);
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                dataCell2.setCellValue(okSum); //
                dataCell2.setCellStyle(cellParamStyle4);
            } else if (k == 5) {
                Row rows = sheet.getRow(k);
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                dataCell2.setCellValue(ngSum); //
                dataCell2.setCellStyle(Style2);
            } else if (k == 6) {
                Row rows = sheet.getRow(k);
                Cell dataCell2 = rows.createCell(lastColumn + 1);
                if (totalSum > 0) {
                    rateSum = Double.parseDouble(String.valueOf(okSum)) / totalSum;
                } else {
                    rateSum = 0.0;
                }
                dataCell2.setCellValue(String.format("%.2f%%", rateSum * 100));

                XSSFCellStyle rateSumStyle = (XSSFCellStyle) wb.createCellStyle();
                rateSumStyle.cloneStyleFrom(cellParamStyle4);

                // 根据总执行率设置背景颜色
                if (rateSum == 1.0) { // 100%
                    rateSumStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                } else if (rateSum >= 0.95) { // 95% 到 100%
                    rateSumStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                } else { // 95% 以下
                    rateSumStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                }
                rateSumStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                dataCell2.setCellStyle(rateSumStyle);
            }
        }
        for (int i = firstCol; i < firstCol + 6 + datas.size(); i++) {
            sheet.setColumnWidth(i, 6000); // 设置列宽为
        }

        map.put("row", totalRow);

        return map;
    }


    private static Map<String, Integer> createSheet4Table(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfExcelPortVo> datas) {
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = firstRow;

        // 创建表头
        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        // 定义表头内容
        String[] headers = {"厂别", "类别"};

        // 创建基本表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(firstCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellParamStyle4);
        }

        // 在"日期"之后创建 distinctFirstList 的单元格
        int additionalCol = firstCol + headers.length; // 从表头最后一列之后开始追加
        for (String value : distinctFirstList) {
            Cell cell = headerRow.createCell(additionalCol);
            cell.setCellValue(value); // distinctFirstList 的值作为表头
            cell.setCellStyle(cellParamStyle4);
            additionalCol++;
        }

        totalRow++; // 表头结束后移动到下一行
        int col = additionalCol;

        // 创建表格内容
        for (DfExcelPortVo table : datas) {
            int startRow = totalRow; // 当前表格起始行

            List<AuditIssue> bbList = table.getAuditIssues(); // 获取数据列表

            // 填充第一列到第四列
            for (int i = 0; i < 1; i++) {
                Row row = sheet.getRow(totalRow);
                if (row == null) {
                    row = sheet.createRow(totalRow);
                }

                Cell cell0 = row.createCell(firstCol);
                cell0.setCellValue(table.getFac()); // 厂别
                cell0.setCellStyle(cellParamStyle4);

                totalRow++;
            }

            // 填充数据部分
            int dataStartCol = firstCol + 1; // 从第1列开始填充数据
            for (int i = 0; i < 5; i++) { //
                Row row = sheet.getRow(startRow + i);
                if (row == null) {
                    row = sheet.createRow(startRow + i);
                }
                Cell titleCell = row.createCell(dataStartCol);
                switch (i) {
                    case 0:
                        titleCell.setCellValue("问题点数");
                        break;
                    case 1:
                        titleCell.setCellValue("时效内≤60min");
                        break;
                    case 2:
                        titleCell.setCellValue("时效内比例");
                        break;
                    case 3:
                        titleCell.setCellValue("超时处理≥60min");
                        break;
                    case 4:
                        titleCell.setCellValue("超时比例");
                        break;
                }
                titleCell.setCellStyle(cellParamStyle4);

                for (int j = 0; j < bbList.size(); j++) {
                    AuditIssue bb = bbList.get(j);
                    Cell dataCell = row.createCell(dataStartCol + j + 1);
                    switch (i) {
                        case 0:
                            dataCell.setCellValue(bb.getTotalPoints()); //
                            break;
                        case 1:
                            dataCell.setCellValue(bb.getOnTimePoints()); //
                            break;
                        case 2:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getOnTimePercentage() * 100)); //
                            break;
                        case 3:
                            dataCell.setCellValue(bb.getTimeoutPoints()); //
                            break;
                        case 4:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getTimeoutPercentage() * 100)); //
                            break;
                    }
                    dataCell.setCellStyle(cellParamStyle4);
                }
            }

            // 合并单元格
            for (int i = 0; i < 1; i++) { // 合并厂别、项目、类别和日期
                sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
            }
        }

        map.put("row", totalRow);
        map.put("col", col);

        return map;
    }


    private static Map<String, Integer> createSheet5Table(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfExcelPortVo> datas) {
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = firstRow;

        // 创建表头
        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        // 定义表头内容
        String[] headers = {"厂别", "类别"};

        // 创建基本表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(firstCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellParamStyle4);
        }

        // 在"日期"之后创建 distinctFirstList 的单元格
        int additionalCol = firstCol + headers.length; // 从表头最后一列之后开始追加
        for (String value : distinctFirstList) {
            Cell cell = headerRow.createCell(additionalCol);
            cell.setCellValue(value); // distinctFirstList 的值作为表头
            cell.setCellStyle(cellParamStyle4);
            additionalCol++;
        }

        totalRow++; // 表头结束后移动到下一行
        int col = additionalCol;

        // 创建表格内容
        for (DfExcelPortVo table : datas) {
            int startRow = totalRow; // 当前表格起始行

            List<AuditIssue> bbList = table.getAuditIssues(); // 获取数据列表

            // 填充第一列到第四列
            for (int i = 0; i < 1; i++) {
                Row row = sheet.getRow(totalRow);
                if (row == null) {
                    row = sheet.createRow(totalRow);
                }

                Cell cell0 = row.createCell(firstCol);
                cell0.setCellValue(table.getFac()); // 厂别
                cell0.setCellStyle(cellParamStyle4);

                totalRow++;
            }

            // 填充数据部分
            int dataStartCol = firstCol + 1; // 从第1列开始填充数据
            for (int i = 0; i < 5; i++) { // 填充目标、问题总数、已执行、未执行、执行率
                Row row = sheet.getRow(startRow + i);
                if (row == null) {
                    row = sheet.createRow(startRow + i);
                }
                Cell titleCell = row.createCell(dataStartCol);
                switch (i) {
                    case 0:
                        titleCell.setCellValue("目标");
                        break;
                    case 1:
                        titleCell.setCellValue("问题点数");
                        break;
                    case 2:
                        titleCell.setCellValue("已关闭");
                        break;
                    case 3:
                        titleCell.setCellValue("未关闭");
                        break;
                    case 4:
                        titleCell.setCellValue("关闭率");
                        break;
                }
                titleCell.setCellStyle(cellParamStyle4);

                for (int j = 0; j < bbList.size(); j++) {
                    AuditIssue bb = bbList.get(j);
                    Cell dataCell = row.createCell(dataStartCol + j + 1);
                    switch (i) {
                        case 0:
                            dataCell.setCellValue(bb.getTargetCount() + "%"); //
                            break;
                        case 1:
                            dataCell.setCellValue(bb.getTotalPoints()); //
                            break;
                        case 2:
                            dataCell.setCellValue(bb.getCloseRecords()); //
                            break;
                        case 3:
                            dataCell.setCellValue(bb.getOpenRecords()); //
                            break;
                        case 4:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getCloseRate() * 100)); //
                            break;
                    }
                    dataCell.setCellStyle(cellParamStyle4);
                }
            }

            // 合并单元格
            for (int i = 0; i < 1; i++) { // 合并厂别、项目、类别和日期
                sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
            }
        }

        map.put("row", totalRow);
        map.put("col", col);

        return map;
    }


    private static Map<String, Integer> createSheet7Table(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<DfExcelPortVo> datas) {
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值

        HashMap<String, Integer> map = new HashMap<>();
        int totalRow = firstRow;

        // 创建表头
        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        // 定义表头内容
        String[] headers = {"厂别", "类别"};

        // 创建基本表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(firstCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellParamStyle4);
        }

        // 在"日期"之后创建 distinctFirstList 的单元格
        int additionalCol = firstCol + headers.length; // 从表头最后一列之后开始追加
        for (String value : distinctFirstList) {
            Cell cell = headerRow.createCell(additionalCol);
            cell.setCellValue(value); // distinctFirstList 的值作为表头
            cell.setCellStyle(cellParamStyle4);
            additionalCol++;
        }

        totalRow++; // 表头结束后移动到下一行
        int col = additionalCol;

        // 创建表格内容
        for (DfExcelPortVo table : datas) {
            int startRow = totalRow; // 当前表格起始行

            List<AuditIssue> bbList = table.getAuditIssues(); // 获取数据列表

            // 填充第一列到第四列
            for (int i = 0; i < 1; i++) {
                Row row = sheet.getRow(totalRow);
                if (row == null) {
                    row = sheet.createRow(totalRow);
                }

                Cell cell0 = row.createCell(firstCol);
                cell0.setCellValue(table.getFac()); // 厂别
                cell0.setCellStyle(cellParamStyle4);

                totalRow++;
            }

            // 填充数据部分
            int dataStartCol = firstCol + 1; // 从第1列开始填充数据
            for (int i = 0; i < 7; i++) { // 填充目标、问题总数、已执行、未执行、执行率
                Row row = sheet.getRow(startRow + i);
                if (row == null) {
                    row = sheet.createRow(startRow + i);
                }
                Cell titleCell = row.createCell(dataStartCol);
                switch (i) {
                    case 0:
                        titleCell.setCellValue("问题点数");
                        break;
                    case 1:
                        titleCell.setCellValue("Level1");
                        break;
                    case 2:
                        titleCell.setCellValue("Level1占比");
                        break;
                    case 3:
                        titleCell.setCellValue("Level2");
                        break;
                    case 4:
                        titleCell.setCellValue("Level2占比");
                        break;
                    case 5:
                        titleCell.setCellValue("Level3");
                        break;
                    case 6:
                        titleCell.setCellValue("Level3占比");
                        break;
                }
                titleCell.setCellStyle(cellParamStyle4);

                for (int j = 0; j < bbList.size(); j++) {
                    AuditIssue bb = bbList.get(j);
                    Cell dataCell = row.createCell(dataStartCol + j + 1);
                    switch (i) {
                        case 0:
                            dataCell.setCellValue(bb.getTotalPoints()); //
                            break;
                        case 1:
                            dataCell.setCellValue(bb.getLevel1() != null ? bb.getLevel1() : 0);

                            break;
                        case 2:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getLevel1Percentage() * 100)); //
                            break;
                        case 3:
                            dataCell.setCellValue(bb.getLevel2() != null ? bb.getLevel2() : 0);

                            break;
                        case 4:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getLevel2Percentage() * 100)); //
                            break;
                        case 5:
                            dataCell.setCellValue(bb.getLevel3() != null ? bb.getLevel3() : 0);
                            break;
                        case 6:
                            dataCell.setCellValue(String.format("%.2f%%", bb.getLevel3Percentage() * 100)); //
                            break;
                    }
                    dataCell.setCellStyle(cellParamStyle4);
                }
            }

            // 合并单元格
            for (int i = 0; i < 1; i++) { // 合并厂别、项目、类别和日期
                sheet.addMergedRegion(new CellRangeAddress(1, 7, 0, 0));
            }
        }

        map.put("row", totalRow);
        map.put("col", col);

        return map;
    }



    private static void createBottomChar(List<DfExcelPortVo> bottomTables, SXSSFSheet sheet, int row, Integer endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值
        for (int i = 0; i < bottomTables.size(); i++) {
            DfExcelPortVo table1 = bottomTables.get(i);
            List<jcDataVo> notProjectList = table1.getChildren();
            sheet.createDrawingPatriarch();
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            chart.setTitleText(String.format("%s稽核清单执行率", table1.getCreateName()));
            StringBuilder chartTitleBuilder = new StringBuilder();
            chartTitleBuilder
                    .append(table1.getfBigpro())
                    .append(" ")
                    .append(table1.getCreateName())
                    .append("稽核清单执行率");
            String chartTitle = chartTitleBuilder.toString();

            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置: 下方
            legend.setPosition(LegendPosition.BOTTOM);

            // 创建 x 轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("");
            // 创建 y 轴，并设置为百分比格式显示
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("");
            leftAxis.setNumberFormat("0.00%");  // Y轴强制保留两位小数百分比
            // 通过底层 XML 强制设置 y 轴数字格式为百分比（0.00%）
            CTValAx ctValAx = chart.getCTChart().getPlotArea().getValAxArray(0);
            if (!ctValAx.isSetNumFmt()) {
                CTNumFmt numFmt = ctValAx.addNewNumFmt();
                numFmt.setFormatCode("0.00%");
                numFmt.setSourceLinked(false);
            } else {
                ctValAx.getNumFmt().setFormatCode("0.00%");
                ctValAx.getNumFmt().setSourceLinked(false);
            }
            // 获取 x 轴数据
            String[] xTitleData = new String[distinctFirstList.size()];
            for (int i1 = 0; i1 < distinctFirstList.size(); i1++) {
                xTitleData[i1] = distinctFirstList.get(i1);
            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            List<DataVo> dataList = new ArrayList<>();
            List<DataVo> dataList1 = new ArrayList<>();
            notProjectList.forEach(e -> {
                DataVo vo = new DataVo();
                DataVo vo1 = new DataVo();
                vo.setType("目标");
                // 目标系列使用整数百分比值，与第一个方法保持一致
                if ("MIL".equals(table1.getCreateName())) {
                    vo.setValue(0.96);
                } else {
                    vo.setValue(0.98);
                }
                vo1.setType("执行率");
                // 先乘以 100，再保留两位小数（例如：0.956 -> 95.60）
                vo1.setValue(new BigDecimal(e.getExecutionRate() * 100)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue()/100);
                dataList.add(vo);

                dataList1.add(vo1);
            });
            List<List<DataVo>> combinedList = new ArrayList<>(); // 创建组合列表
            combinedList.add(dataList);
            combinedList.add(dataList1);

            // 添加每个系列的数据
            for (int i2 = 0; i2 < combinedList.size(); i2++) {
                Double[] xData1 = combinedList.get(i2).stream()
                        .map(DataVo::getValue)
                        .toArray(Double[]::new);
                String type = combinedList.get(i2).get(0).getType();

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                if ("目标".equals(type)) {
                    XDDFSolidFillProperties lineColor = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN));
                    XDDFLineProperties lineProperties = new XDDFLineProperties();
                    lineProperties.setFillProperties(lineColor);
                    series.setLineProperties(lineProperties);
                } else {
                    XDDFSolidFillProperties lineColor = new XDDFSolidFillProperties(
                            XDDFColor.from(new byte[] { (byte) 0, (byte) 150, (byte) 255 })
                    );
                    XDDFLineProperties lineProperties = new XDDFLineProperties();
                    lineProperties.setFillProperties(lineColor);
                    series.setLineProperties(lineProperties);
                }
                // 设置图例标题
                series.setTitle(type, null);
                // 线条样式：false 表示折线
                series.setSmooth(false);
                // 移除点的样式
                series.setMarkerStyle(MarkerStyle.NONE);
            }

            // 强制配置数据标签格式
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
                CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

                CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
                numFmt.setFormatCode("0.00%");        // 数据标签保留两位小数百分比
                numFmt.setSourceLinked(false);        // 禁用自动关联数据格式

                labels.addNewShowVal().setVal(true);  // 显示数值

                // 隐藏其他标签
                CTBoolean hide = CTBoolean.Factory.newInstance();
                hide.setVal(false);
                labels.setShowCatName(hide);
                labels.setShowSerName(hide);
                labels.setShowLegendKey(hide);
                labels.setShowPercent(hide);

                labels.addNewDLblPos().setVal(STDLblPos.T);  // 标签位置顶部
            }

            chart.plot(data);
        }
    }



    private static void create3Char(List<DfExcelPortVo> bottomTables, SXSSFSheet sheet, int row, Integer
            endcol) {


        Map<String, List<DfExcelPortVo>> groupedByProject = bottomTables.stream()
                .collect(Collectors.groupingBy(DfExcelPortVo::getfBigpro));


        int currentRow = row;
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值
        for (Map.Entry<String, List<DfExcelPortVo>> entry : groupedByProject.entrySet()) {
            String project = entry.getKey();
            List<DfExcelPortVo> projectTables = entry.getValue();

            sheet.createDrawingPatriarch();
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            StringBuilder chartTitleBuilder = new StringBuilder();
            chartTitleBuilder
                    .append(fac)
                    .append(" ")
                    .append(project)
                    .append("稽核清单执行率");
            String chartTitle = chartTitleBuilder.toString();


            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            chart.setTitleText(chartTitle);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置:上下左右
            legend.setPosition(LegendPosition.BOTTOM);

            // 创建x轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            // X轴标题
            bottomAxis.setTitle("");
            // 左侧标题
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("");
            // 通过底层 XML 强制设置 y 轴数字格式为百分比（0.00%）
            CTValAx ctValAx = chart.getCTChart().getPlotArea().getValAxArray(0);
            if (!ctValAx.isSetNumFmt()) {
                CTNumFmt numFmt = ctValAx.addNewNumFmt();
                numFmt.setFormatCode("0.00%");
                numFmt.setSourceLinked(false);
            } else {
                ctValAx.getNumFmt().setFormatCode("0.00%");
                ctValAx.getNumFmt().setSourceLinked(false);
            }
            // 获取X轴数据
            String[] xTitleData = new String[distinctFirstList.size()];

            for (int i1 = 0; i1 < distinctFirstList.size(); i1++) {
                xTitleData[i1] = distinctFirstList.get(i1);
            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            List<List<Object>> combinedList = new ArrayList<>(); // 创建组合列表
            for (int i1 = 0; i1 < projectTables.size(); i1++) {
                List<Object> dataList = new ArrayList<>();
                dataList.add(projectTables.get(i1).getCreateName());
                List<AuditIssue> list = projectTables.get(i1).getAuditIssues();
                for (int i = 0; i < list.size(); i++) {
                    dataList.add(list.get(i).getIssueRatio());
                }
                combinedList.add(dataList);

            }
            if (combinedList.size() == 1) {
                // 添加占位符数据或跳过
                List<Object> defaultData = new ArrayList<>();
                defaultData.add("");  // 或者可以根据实际需求添加默认值
                combinedList.add(defaultData); // 仅用于避免 POI 填充 xTitleData 数据
            }


            for (List<Object> objects : combinedList) {
                String title = String.valueOf(objects.get(0));

                Double[] yArray = objects.stream()
                        .skip(1)
                        .map(obj -> {
                            if (obj instanceof Number) {
                                // 转换为 double，乘以 100，保留两位小数
                                double value = ((Number) obj).doubleValue();
                                return value;
                            } else if (obj instanceof String) {
                                try {
                                    // 转换为 double，乘以 100，保留两位小数
                                    double parsedDouble = Double.parseDouble((String) obj);
                                    return parsedDouble;
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull) // 过滤掉 null 值
                        .toArray(Double[]::new); // 转换为 Double 数组

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(yArray);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                // 图例标题
                series.setTitle(title, null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);
            }
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
                CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

                CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
                numFmt.setFormatCode("0.00%");        // 数据标签保留两位小数百分比
                numFmt.setSourceLinked(false);        // 禁用自动关联数据格式

                labels.addNewShowVal().setVal(true);  // 显示数值

                // 隐藏其他标签
                CTBoolean hide = CTBoolean.Factory.newInstance();
                hide.setVal(false);
                labels.setShowCatName(hide);
                labels.setShowSerName(hide);
                labels.setShowLegendKey(hide);
                labels.setShowPercent(hide);

                labels.addNewDLblPos().setVal(STDLblPos.T);  // 标签位置顶部
            }

            chart.plot(data);
        }
    }


    private static void create4Char(List<DfExcelPortVo> bottomTables, SXSSFSheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值
        for (int i = 0; i < bottomTables.size(); i++) {
            DfExcelPortVo table1 = bottomTables.get(i);
            List<AuditIssue> notProjectList = table1.getAuditIssues();
            sheet.createDrawingPatriarch();
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            chart.setTitleText(String.format("%s问题点处理时效性比例", fac));
            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置:上下左右
            legend.setPosition(LegendPosition.BOTTOM);

            // 创建x轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            // X轴标题
            bottomAxis.setTitle("");
            // 左侧标题
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("");

            // 通过底层 XML 强制设置 y 轴数字格式为百分比（0.00%）
            CTValAx ctValAx = chart.getCTChart().getPlotArea().getValAxArray(0);
            if (!ctValAx.isSetNumFmt()) {
                CTNumFmt numFmt = ctValAx.addNewNumFmt();
                numFmt.setFormatCode("0.00%");
                numFmt.setSourceLinked(false);
            } else {
                ctValAx.getNumFmt().setFormatCode("0.00%");
                ctValAx.getNumFmt().setSourceLinked(false);
            }
            // 获取X轴数据
            String[] xTitleData = new String[distinctFirstList.size()];

            for (int i1 = 0; i1 < distinctFirstList.size(); i1++) {
                xTitleData[i1] = distinctFirstList.get(i1);
            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            List<DataVo> dataList = new ArrayList<>();
            List<DataVo> dataList1 = new ArrayList<>();
            notProjectList.forEach(e -> {
                DataVo vo = new DataVo();
                DataVo vo1 = new DataVo();
                vo.setType(fac + "时效内比例");
                vo.setValue(Math.round(e.getOnTimePercentage()));
                vo1.setType(fac + "超时比例");
                vo1.setValue(Math.round(e.getTimeoutPercentage()));
                dataList.add(vo);
                dataList1.add(vo1);
            });
            List<List<DataVo>> combinedList = new ArrayList<>(); // 创建组合列表
            combinedList.add(dataList);
            combinedList.add(dataList1);


            // 添加每个系列的数据
            for (int i2 = 0; i2 < combinedList.size(); i2++) {
                Double[] xData1 = combinedList.get(i2).stream().map(item -> item.getValue()).toArray(Double[]::new);

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                // 图例标题
                series.setTitle(combinedList.get(i2).get(0).getType(), null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);
            }
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
                CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

                CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
                numFmt.setFormatCode("0.00%");        // 数据标签保留两位小数百分比
                numFmt.setSourceLinked(false);        // 禁用自动关联数据格式

                labels.addNewShowVal().setVal(true);  // 显示数值

                // 隐藏其他标签
                CTBoolean hide = CTBoolean.Factory.newInstance();
                hide.setVal(false);
                labels.setShowCatName(hide);
                labels.setShowSerName(hide);
                labels.setShowLegendKey(hide);
                labels.setShowPercent(hide);

                labels.addNewDLblPos().setVal(STDLblPos.T);  // 标签位置顶部
            }

            chart.plot(data);
        }
    }


    private static void create5Char(List<DfExcelPortVo> bottomTables, SXSSFSheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值
        for (int i = 0; i < bottomTables.size(); i++) {
            DfExcelPortVo table1 = bottomTables.get(i);
            List<AuditIssue> notProjectList = table1.getAuditIssues();
            sheet.createDrawingPatriarch();
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            chart.setTitleText(String.format("%s问题关闭率", fac));
            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置:上下左右
            legend.setPosition(LegendPosition.BOTTOM);

            // 创建x轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            // X轴标题
            bottomAxis.setTitle("");
            // 左侧标题
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("");
            // 通过底层 XML 强制设置 y 轴数字格式为百分比（0.00%）
            CTValAx ctValAx = chart.getCTChart().getPlotArea().getValAxArray(0);
            if (!ctValAx.isSetNumFmt()) {
                CTNumFmt numFmt = ctValAx.addNewNumFmt();
                numFmt.setFormatCode("0.00%");
                numFmt.setSourceLinked(false);
            } else {
                ctValAx.getNumFmt().setFormatCode("0.00%");
                ctValAx.getNumFmt().setSourceLinked(false);
            }
            // 获取X轴数据
            String[] xTitleData = new String[distinctFirstList.size()];

            for (int i1 = 0; i1 < distinctFirstList.size(); i1++) {
                xTitleData[i1] = distinctFirstList.get(i1);
            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            List<DataVo> dataList = new ArrayList<>();
            List<DataVo> dataList1 = new ArrayList<>();
            notProjectList.forEach(e -> {
                DataVo vo = new DataVo();
                DataVo vo1 = new DataVo();
                vo.setType(fac + "目标");
                vo.setValue(e.getTargetCount()/100);
                vo1.setType(fac + "关闭率");
                vo1.setValue(Math.round(e.getCloseRate() ));
                dataList.add(vo);
                dataList1.add(vo1);
            });
            List<List<DataVo>> combinedList = new ArrayList<>(); // 创建组合列表
            combinedList.add(dataList);
            combinedList.add(dataList1);


            // 添加每个系列的数据
            for (int i2 = 0; i2 < combinedList.size(); i2++) {
                Double[] xData1 = combinedList.get(i2).stream().map(item -> item.getValue()).toArray(Double[]::new);

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                String type = combinedList.get(i2).get(0).getType();
                if ((fac + "目标").equals(type)){
                    XDDFSolidFillProperties lineColor = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN));
                    XDDFLineProperties lineProperties = new XDDFLineProperties();
                    lineProperties.setFillProperties(lineColor);
                    series.setLineProperties(lineProperties);
                }else {
                    XDDFSolidFillProperties lineColor = new XDDFSolidFillProperties(
                            XDDFColor.from(
                                    new byte[] { (byte) 0, (byte) 150, (byte) 255 }
                            )
                    );
                    XDDFLineProperties lineProperties = new XDDFLineProperties();
                    lineProperties.setFillProperties(lineColor);
                    series.setLineProperties(lineProperties);
                }


                // 图例标题
                series.setTitle(type, null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.NONE);  // 这里移除点的样式，不显示任何标记
            }
            // 强制配置数据标签格式
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
                CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

                CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
                numFmt.setFormatCode("0.00%");        // 数据标签保留两位小数百分比
                numFmt.setSourceLinked(false);        // 禁用自动关联数据格式

                labels.addNewShowVal().setVal(true);  // 显示数值

                // 隐藏其他标签
                CTBoolean hide = CTBoolean.Factory.newInstance();
                hide.setVal(false);
                labels.setShowCatName(hide);
                labels.setShowSerName(hide);
                labels.setShowLegendKey(hide);
                labels.setShowPercent(hide);

                labels.addNewDLblPos().setVal(STDLblPos.T);  // 标签位置顶部
            }

            chart.plot(data);
        }
    }

    private static void create7Char(List<DfExcelPortVo> bottomTables, SXSSFSheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;
        List<String> distinctFirstList = getDistinctFirstList(); // 获取 distinctFirstList 的值
        for (int i = 0; i < bottomTables.size(); i++) {
            DfExcelPortVo table1 = bottomTables.get(i);
            List<AuditIssue> notProjectList = table1.getAuditIssues();
            sheet.createDrawingPatriarch();
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            chart.setTitleText(String.format("%s问题等级违规占比", fac));
            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置:上下左右
            legend.setPosition(LegendPosition.BOTTOM);

            // 创建x轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            // X轴标题
            bottomAxis.setTitle("");
            // 左侧标题
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("");
            leftAxis.setNumberFormat("0.00%");  // Y轴强制保留两位小数百分比
            // 通过底层 XML 强制设置 y 轴数字格式为百分比（0.00%）
            CTValAx ctValAx = chart.getCTChart().getPlotArea().getValAxArray(0);
            if (!ctValAx.isSetNumFmt()) {
                CTNumFmt numFmt = ctValAx.addNewNumFmt();
                numFmt.setFormatCode("0.00%");
                numFmt.setSourceLinked(false);
            } else {
                ctValAx.getNumFmt().setFormatCode("0.00%");
                ctValAx.getNumFmt().setSourceLinked(false);
            }
            // 获取X轴数据
            String[] xTitleData = new String[distinctFirstList.size()];

            for (int i1 = 0; i1 < distinctFirstList.size(); i1++) {
                xTitleData[i1] = distinctFirstList.get(i1);
            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            List<DataVo> dataList1 = new ArrayList<>();
            List<DataVo> dataList2 = new ArrayList<>();
            List<DataVo> dataList3 = new ArrayList<>();

            notProjectList.forEach(e -> {
                DataVo vo1 = new DataVo();
                DataVo vo2 = new DataVo();
                DataVo vo3 = new DataVo();
                vo1.setType("Level1占比");
                vo1.setValue(Math.round(e.getLevel1Percentage()* 100.0 * 100.0) / 100.0 / 100.0);
                vo2.setType("Level2占比");
                vo2.setValue(Math.round(e.getLevel2Percentage()* 100.0 * 100.0) / 100.0 / 100.0);
                vo3.setType("Level3占比");
                vo3.setValue(Math.round(e.getLevel3Percentage()* 100.0 * 100.0) / 100.0 / 100.0);
                dataList1.add(vo1);
                dataList2.add(vo2);
                dataList3.add(vo3);

            });
            List<List<DataVo>> combinedList = new ArrayList<>(); // 创建组合列表
            combinedList.add(dataList1);
            combinedList.add(dataList2);
            combinedList.add(dataList3);

            // 添加每个系列的数据
            for (int i2 = 0; i2 < combinedList.size(); i2++) {
                Double[] xData1 = combinedList.get(i2).stream().map(item -> item.getValue()).toArray(Double[]::new);

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                // 图例标题
                series.setTitle(combinedList.get(i2).get(0).getType(), null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);
            }
            // 强制配置数据标签格式
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer series : plotArea.getLineChartArray(0).getSerList()) {
                CTDLbls labels = series.isSetDLbls() ? series.getDLbls() : series.addNewDLbls();

                CTNumFmt numFmt = labels.isSetNumFmt() ? labels.getNumFmt() : labels.addNewNumFmt();
                numFmt.setFormatCode("0.00%");        // 数据标签保留两位小数百分比
                numFmt.setSourceLinked(false);        // 禁用自动关联数据格式

                labels.addNewShowVal().setVal(true);  // 显示数值

                // 隐藏其他标签
                CTBoolean hide = CTBoolean.Factory.newInstance();
                hide.setVal(false);
                labels.setShowCatName(hide);
                labels.setShowSerName(hide);
                labels.setShowLegendKey(hide);
                labels.setShowPercent(hide);

                labels.addNewDLblPos().setVal(STDLblPos.T);  // 标签位置顶部
            }

            chart.plot(data);
        }
    }


    /**
     * 检查数据配置问题
     *
     * @throws IOException 抛出数据异常类
     */
    protected void checkConfig() throws IOException {
        if (heardKey == null || heardList.length == 0) {
            throw new IOException("列名数组不能为空或者为NULL");
        }

        if (fontSize < 0 || rowHeight < 0 || columWidth < 0) {
            throw new IOException("字体、宽度或者高度不能为负值");
        }

        if (Strings.isEmpty(sheetName)) {
            throw new IOException("工作表表名不能为NULL");
        }
    }

    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    public void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws
            UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


    public XSSFCellStyle createTitleCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle titleStyle = wb.createCellStyle();
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 14);  // 设置字体大小
        titleFont.setBold(true);  // 设置字体加粗
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
/*        titleStyle.setBorderTop(BorderStyle.THIN);  // 上边框
        titleStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
        titleStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
        titleStyle.setBorderRight(BorderStyle.THIN);  // 右边框*/
        return titleStyle;
    }

    public XSSFCellStyle createCellParamStyle(SXSSFWorkbook wb) {
        XSSFCellStyle cellParamStyle = (XSSFCellStyle) wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 12);  // 设置字体大小
        // font.setBold(true);  // 设置字体加粗
        // font.setColor(IndexedColors.BLACK.getIndex());  // 设置字体颜色
        cellParamStyle.setFont(font);
        cellParamStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        cellParamStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
//        cellParamStyle.setBorderTop(BorderStyle.THIN);  // 上边框
//        cellParamStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
//        cellParamStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
//        cellParamStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        return cellParamStyle;
    }

    public static XSSFCellStyle createCellStyle(SXSSFWorkbook wb) {
        XSSFCellStyle cellParamStyle = (XSSFCellStyle) wb.createCellStyle();
        // 创建样式对象
        XSSFColor yellowColor = new XSSFColor(new java.awt.Color(255, 255, 0), null);
        cellParamStyle.setFillForegroundColor(yellowColor);
        cellParamStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 12);  // 设置字体大小
        // font.setBold(true);  // 设置字体加粗
        // font.setColor(IndexedColors.BLACK.getIndex());  // 设置字体颜色
        cellParamStyle.setFont(font);
        cellParamStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        cellParamStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
//        cellParamStyle.setBorderTop(BorderStyle.THIN);  // 上边框
//        cellParamStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
//        cellParamStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
//        cellParamStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        return cellParamStyle;
    }

    public static XSSFCellStyle createCellStyle1(SXSSFWorkbook wb) {
        XSSFCellStyle cellParamStyle = (XSSFCellStyle) wb.createCellStyle();


        // 设置单元格背景颜色为自定义颜色 #92CDDC
        XSSFColor customColor = new XSSFColor(new java.awt.Color(0xFF, 0xE6, 0x99), null);
        cellParamStyle.setFillForegroundColor(customColor);
        cellParamStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  // 设置填充模式为纯色

        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 12);  // 设置字体大小
        // font.setBold(true);  // 设置字体加粗
        // font.setColor(IndexedColors.BLACK.getIndex());  // 设置字体颜色
        cellParamStyle.setFont(font);
        cellParamStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        cellParamStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        return cellParamStyle;
    }

    public static XSSFCellStyle createCellStyle2(SXSSFWorkbook wb) {
        XSSFCellStyle cellParamStyle = (XSSFCellStyle) wb.createCellStyle();

        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontHeightInPoints((short) 12);  // 设置字体大小
        font.setColor(new XSSFColor(new java.awt.Color(255, 0, 0), null)); // 红色字体
        // font.setBold(true);  // 设置字体加粗
        // font.setColor(IndexedColors.BLACK.getIndex());  // 设置字体颜色
        cellParamStyle.setFont(font);
        cellParamStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        cellParamStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        return cellParamStyle;
    }


}
