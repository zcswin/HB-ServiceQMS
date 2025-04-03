package com.ww.boengongye.utils;


import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.exportExcelUpdate.*;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.service.impl.DfQmsIpqcWaigTotalServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.LineCap;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ExcelCheckExportUtil2 {
    //表头
    private String title;

    private String period;
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

    private String startDate;
    private String endDate;

    private String factoryId;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

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

    private static final Logger log = LoggerFactory.getLogger(ExcelCheckExportUtil2.class);


    /**
     * 开始导出数据信息
     */
    public byte[] exportExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);

        // 标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont fontStyle = wb.createFont();
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
        HSSFCellStyle style = wb.createCellStyle();
        //设置单元格样式
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        //在第1行创建rows
        HSSFRow row = wbSheet.createRow((int) 0);
        //设置列头元素
        HSSFCell cellHead = null;
        for (int i = 0; i < heardList.length; i++) {
            cellHead = row.createCell(i);
            cellHead.setCellValue(heardList[i]);
            cellHead.setCellStyle(style);
        }

        //设置每格数据的样式 （字体红色）
        HSSFCellStyle cellParamStyle = wb.createCellStyle();
        HSSFFont ParamFontStyle = wb.createFont();
//        cellParamStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        ParamFontStyle.setColor(HSSFColor.DARK_RED.index);   //设置字体颜色 (红色)
        ParamFontStyle.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle.setFont(ParamFontStyle);
        //设置每格数据的样式2（字体蓝色）
        HSSFCellStyle cellParamStyle2 = wb.createCellStyle();
//        cellParamStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ParamFontStyle2 = wb.createFont();
//        ParamFontStyle2.setColor(HSSFColor.BLUE.index);   //设置字体颜色 (蓝色)
        ParamFontStyle2.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle2.setFont(ParamFontStyle2);
        //开始写入实体数据信息
        int a = 1;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow roww = wbSheet.createRow((int) a);
            Map map = data.get(i);
            HSSFCell cell = null;
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
            return wb.getBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("导出Excel出现严重异常，异常信息：" + ex.getMessage());
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

        sheet.createDrawingPatriarch();
        XSSFDrawing drawing = sheet.getDrawingPatriarch();

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(chartEntity.getTitleName());

        // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
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
        }

        chart.plot(barChartData);
        chart.plot(lineChartData);
    }

    public static void createBarChart6(SXSSFSheet sheet, List<List<Object>> sheetDataList, List<Object> heads, ChartEntity chartEntity, List<List<Object>> barChartsheetDataList) {

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

        sheet.createDrawingPatriarch();
        XSSFDrawing drawing = sheet.getDrawingPatriarch();

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(chartEntity.getTitleName());

        // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
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
                    .skip(1) // 跳过第一个元素
                    .map(obj -> {
                        try {
                            // 尝试将 obj 转换为 double
                            if (obj instanceof Number) {
                                return ((Number) obj).doubleValue();
                            } else if (obj instanceof String) {
                                return Double.parseDouble((String) obj);
                            }
                        } catch (NumberFormatException e) {
                            // 如果转换失败，返回 null
                            return null;
                        }
                        return null; // 非 Number 或 String 类型，返回 null
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
        }

        chart.plot(barChartData);
        chart.plot(lineChartData);
    }


    public byte[] exportExportNew(SXSSFWorkbook wb,HttpServletRequest request, HttpServletResponse response,
                                  Map<String, List<DfQmsIpqcFlawConfig>> listMap,
                                  List<DfProcessProjectConfig> dplist, List<DfProject> activeProjects, List<NewImageRequest> projects, ExcelCheckExportUtil2 excelExportSheet5, List<DfAuditDetail> listData) throws IOException {
        // 检查参数配置信息
      //  checkConfig();

        // 创建工作簿
//        SXSSFWorkbook wb = new SXSSFWorkbook(30000); // 使用 XSSFWorkbook
//
//        //第一个sheet
//        // 创建工作表
//        SXSSFSheet wbSheet = wb.createSheet(this.sheetName); // 使用 XSSFSheet
//        wbSheet.setDefaultColumnWidth(20); // 设置默认列宽
//
//        // 创建标题样式
//        XSSFCellStyle titleStyle = createTitleCellStyle(wb);
//
//        // 创建前四行用于布局
//        createEmptyRows(wbSheet, 4);
//
//        // 创建标题行
//        int rowNum = 0;
//        SXSSFRow row0 = wbSheet.createRow(rowNum++);
//        row0.setHeight((short) 800);
//        createTitleRow(wbSheet, row0, titleStyle);
//
//        // 设置表头
//        createTableHeader(wbSheet, titleStyle);
//
//        // 遍历输入的 Map 填充数据
//        fillDataWithMap(wbSheet, listMap, titleStyle);
//
//        // 设置每格数据的样式
//        XSSFCellStyle cellParamStyle = createCellParamStyle(wb);
//
//        // 从第4行开始写入实体数据
//        int rowIndex = 4;
//        // 预处理数据，将 Map 转换为二维字符串数组
//        String[][] preData = new String[data.size()][heardKey.length];
//        for (int i = 0; i < data.size(); i++) {
//            Map<String, Object> map = data.get(i);
//            for (int j = 0; j < heardKey.length; j++) {
//                Object valueObj = map.get(heardKey[j]);
//                preData[i][j] = (valueObj == null) ? "" : valueObj.toString();
//            }
//        }
//
//        // 写入预处理后的数据
//        int flushSize = 1000; // 每写入1000行刷新一次
//        for (int i = 0; i < preData.length; i++) {
//            SXSSFRow row = wbSheet.createRow(rowIndex++);
//            for (int j = 0; j < preData[i].length; j++) {
//                SXSSFCell cell = row.createCell(j);
//                cell.setCellStyle(cellParamStyle);
//                cell.setCellValue(preData[i][j]);
//            }
//            if (rowIndex % flushSize == 0) {
//                wbSheet.flushRows();
//            }
//        }

        /*
         ************************************************************************************
         */
        //第二个工作表
        SXSSFSheet sheet = wb.createSheet("各工序直通率-抽检良率");
        XSSFCellStyle cellParamStyle4 = createCellParamStyle(wb);

        cellParamStyle4 = getExportSheet2(dplist, activeProjects, sheet, wb, period);





        /*
         ***************************************************************************************************************
         */
        //第三个工作表
//        SXSSFSheet sheet3 = wb.createSheet("外观各工序TOP 10不良");
//
//        List<DfQmsIpqcWaigTotalVo> testV0List4101 = getDfQmsIpqcWaigTotalVos3(activeProjects,dplist);
//
//        // 表格
//        if (testV0List4101.size() > 0) {
//            Map<String, Integer> centerTable1 = createSheet3Table(cellParamStyle4, sheet3, 0, 0, testV0List4101);
//            createCenterChar(testV0List4101, sheet3, centerTable1.get("col") + 2);
//        }


//        List<ImageRequest> imageRequests = null;
//        // 插入所有项目的图片
//        int currentRow = 0;
//        if (projects != null) {
//
//            imageRequests = groupImageRequests(projects);
//
//            if (imageRequests != null) {
//                for (ImageRequest project : imageRequests) {
//                    // 插入项目名称标题
//                    currentRow = insertProjectTitle(sheet3, project.getProject(), currentRow, 35);
//
//                    // 插入白班图片（带标签）
//                    currentRow = insertShiftImage(sheet3, project.getWhiteShift(), currentRow, "白班", 35);
//
//                    // 插入晚班图片（带标签）
//                    currentRow = insertShiftImage(sheet3, project.getNightShift(), currentRow, "晚班", 35);
//
//                    // 插入合计图片（带标签）
//                    currentRow = insertShiftImage(sheet3, project.getTotalShift(), currentRow, "全部", 35);
//
//                    // 添加项目间间隔
//                    currentRow += 3;
//                }
//            }
//
//        }



        /*
         ***************************************************************************************************************
         */
        //第四个工作表
        //     List<DfQmsIpqcWaigTotalVo> testV0List4 = getQmsIpqcWaigTotalVos(totalShiftList);

//        List<DfQmsIpqcWaigTotalVo> testV0List410 = getDfQmsIpqcWaigTotalVos(activeProjects, dplist);
//
// //       List<DfQmsIpqcWaigTotalVo> testV0List411 = getDfQmsIpqcWaigTotalVoss(activeProjects, dplist);
//
//
//        SXSSFSheet sheet4 = wb.createSheet("外观各工序TOP 10不良 趋势");
//        Map<String, Integer> bottomTable = new HashMap<>();
//        int row = 0;
//        if (testV0List411.size() > 0) {
//            bottomTable = createBottomTable(cellParamStyle4, sheet4, 0, 0, testV0List411);
//            row = createBottomChar(testV0List411, sheet4, 0, 14);
//        }


//        // 中间表格
//        Map<String, Integer> centerTable = new HashMap<>();
//        if (testV0List410.size() > 0) {
//            centerTable = createCenterTable(cellParamStyle4, sheet4, bottomTable.get("row") + 1, 0, testV0List410);
//            createBottomChar4(testV0List410, sheet4, row, 14);
//        }
//
//        int currentRow4 = 0;
//        if (imageRequests != null) {
//            for (ImageRequest project : imageRequests) {
//                // 插入项目名称标题
//                currentRow4 = insertProjectTitle(sheet4, project.getProject(), currentRow4, 35);
//
//                // 插入白班图片（带标签）
//                currentRow4 = insertShiftImage(sheet4, project.getWhiteShift(), currentRow4, "白班", 35);
//
//                // 插入晚班图片（带标签）
//                currentRow4 = insertShiftImage(sheet4, project.getNightShift(), currentRow4, "晚班", 35);
//
//                // 插入合计图片（带标签）
//                currentRow4 = insertShiftImage(sheet4, project.getTotalShift(), currentRow4, "全部", 35);
//
//                // 添加项目间间隔
//                currentRow4 += 3;
//            }
//
//        }





        /*
         **************************************************************************************************************************
         */           //第五个工作表

//        Sheet sheet5 = wb.createSheet("外观问题点导出明细数据");
//        // 设置列宽
//
//
//        if (excelExportSheet5.getData().size() > 0) {
//            exportExcleAuditNgDetail(excelExportSheet5, sheet5, wb);
//        }


//        /*
//         ************************************************************************************
//         */
//        //第六个工作表
//        SXSSFSheet sheet6 = wb.createSheet("外观问题点各工序超时情况");
//        // 初始化一个二维列表，用于存储良率数据
//        if (listData.size() > 0) {
//            Sheet6(dplist, listData, sheet6, wb, 0, cellParamStyle);
//        }


        // 导出数据
        try {
            String name = generateFileName("中央外观明细数据");

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

    /**
     * 根据前端传来的新数据格式，分组构建ImageRequest集合
     */
    public List<ImageRequest> groupImageRequests(List<NewImageRequest> base64List) {
        Map<String, ImageRequest> projectMap = new HashMap<>();

        for (NewImageRequest req : base64List) {
            String projectName = req.getProject();
            // 根据项目名称查找已有的ImageRequest，没有则创建新的
            ImageRequest imgReq = projectMap.get(projectName);
            if (imgReq == null) {
                imgReq = new ImageRequest();
                imgReq.setProject(projectName);
                projectMap.put(projectName, imgReq);
            }
            // 根据classs来判断设置哪个班次的图片
            if ("白班".equals(req.getClasss())) {
                imgReq.setWhiteShift(req.getBase64Path());
            } else if ("晚班".equals(req.getClasss())) {
                imgReq.setNightShift(req.getBase64Path());
            } else if ("全部".equals(req.getClasss())) {
                imgReq.setTotalShift(req.getBase64Path());
            }
        }

        // 返回所有组装后的ImageRequest
        return new ArrayList<>(projectMap.values());
    }


    /**
     * 插入项目标题（仅操作目标列）
     */
    private int insertProjectTitle(Sheet sheet, String title, int startRow, int targetCol) {
        // 获取或创建行（不影响其他列）
        Row row = sheet.getRow(startRow) != null ? sheet.getRow(startRow) : sheet.createRow(startRow);
        // 仅操作目标列单元格
        Cell cell = row.createCell(targetCol);
        cell.setCellValue("项目：" + title);
        // 返回下一行号
        return startRow + 1;
    }

    /**
     * 插入班次图片（带标签）
     * 文字独占一行，留一空行后插入图片，图片覆盖多个单元格
     */
    private int insertShiftImage(Sheet sheet, String base64Image, int startRow,
                                 String label, int targetCol) {
        if (StringUtils.isBlank(base64Image)) {
            return startRow;
        }

        try {
            // 插入标签（仅操作目标列）
            Row labelRow = sheet.getRow(startRow) != null ? sheet.getRow(startRow) : sheet.createRow(startRow);
            Cell labelCell = labelRow.createCell(targetCol);
            labelCell.setCellValue(label);

            // 留出一行空白（例如用于视觉分隔）
            int imageStartRow = startRow + 2;

            // 处理base64字符串，检查是否包含","，否则使用整个字符串
            String[] parts = base64Image.split(",", 2);
            String base64Data = parts.length > 1 ? parts[1] : parts[0];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 插入图片，图片放置在空白行后
            insertImageToSheet(sheet, imageBytes, imageStartRow, targetCol);

            // 图片按4行高度插入，这里返回下一可用行（文字所在行 + 1行空白 + 4行图片 + 1行间隔）
            return startRow + 2 + 4 + 1;  // = startRow + 7
        } catch (Exception e) {
            log.error("插入{}图片失败: {}", label, e.getMessage());
            return startRow;
        }
    }

    /**
     * 图片插入核心方法（不影响其他列）
     * 直接设置图片覆盖多个单元格（例如覆盖4个单元格的宽度和高度）
     */
    private void insertImageToSheet(Sheet sheet, byte[] imageData,
                                    int rowNum, int colNum) throws Exception {
        Workbook workbook = sheet.getWorkbook();
        int pictureType = Workbook.PICTURE_TYPE_PNG;

        // 添加图片到工作簿
        int pictureIdx = workbook.addPicture(imageData, pictureType);

        // 创建绘图对象
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();

        // 设置图片锚点，覆盖目标单元格范围
        anchor.setCol1(colNum);
        anchor.setRow1(rowNum);
        anchor.setCol2(colNum + 4); // 图片覆盖4个单元格宽度
        anchor.setRow2(rowNum + 4); // 图片覆盖4个单元格高度

        // 插入图片（不调用resize，避免改变列宽或行高）
        drawing.createPicture(anchor, pictureIdx);
    }

    /**
     * 将图片文件编码为Base64字符串
     */
    private static String encodeImageToBase64(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream imageStream = new FileInputStream(imageFile);
            byte[] imageBytes = new byte[(int) imageFile.length()];
            imageStream.read(imageBytes);
            imageStream.close();
            // Encode the byte array to Base64
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("Failed to read or encode the image: " + e.getMessage());
            return null;
        }
    }


    public static String generateFileName(String contentDescription) {
        // 获取当前日期和时间
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 生成4位随机数
        int randomNumber = (int) (Math.random() * 10000); // 生成一个0-9999之间的数字

        // 组合文件名
        return contentDescription + timestamp + String.format("%04d", randomNumber) + ".xlsx";
    }

    private XSSFCellStyle getExportSheet2(List<DfProcessProjectConfig> dplist,
                                          List<DfProject> totalShiftList,
                                          SXSSFSheet sheet,
                                          SXSSFWorkbook wb,
                                          String period) { // 新增period参数
        // 初始化时间范围
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 初始化一个二维列表，用于存储良率数据
        List<List<Object>> sheetDataList = new ArrayList<>();
        // 初始化一个二维列表，用于存储直通率数据
        List<List<Object>> barChartsheetDataList = new ArrayList<>();

        List<DfQmsIpqcWaigTotalVo> testV0List = new ArrayList<>();


        int count = 0;
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 00:00:00", formatter);

        // 根据周期分割时间段
        List<TimePeriod> timePeriods = DateSplitter.splitPeriod(start, end, period);

        // 遍历每个时间段
        for (TimePeriod tp : timePeriods) {
            // 调整结束时间为次日7点（原逻辑）
            LocalDateTime newEndDateTime = tp.getEnd().plusDays(1).withHour(7).withMinute(0).withSecond(0);
            String newEndDate = newEndDateTime.format(formatter);

            String newstartDateTime = tp.getStart().format(formatter);


            for (DfProject dfproject : totalShiftList) {

                String project = dfproject.getName();
                // 初始化各类列表
                List<Object> whiteShiftYieldRate = new ArrayList<>();
                List<Object> nightShiftYieldRate = new ArrayList<>();
                List<Object> whiteShiftYieldRate1 = new ArrayList<>();
                List<Object> nightShiftYieldRate1 = new ArrayList<>();
                List<Object> whiteSpotCheckCount = new ArrayList<>();
                List<Object> nightSpotCheckCount = new ArrayList<>();
                List<Object> whiteOkNum = new ArrayList<>();
                List<Object> nightOkNum = new ArrayList<>();
                List<Object> whiteNg = new ArrayList<>();
                List<Object> nightNg = new ArrayList<>();
                List<Object> totalShiftYieldRate = new ArrayList<>();
                List<Object> totalShiftYieldRate1 = new ArrayList<>();

                whiteShiftYieldRate.add("白班 良率");
                nightShiftYieldRate.add("晚班 良率");
                totalShiftYieldRate.add("合计 良率");

                // 直通率相关数据列表（如有需要）
                List<Object> whiteDirectRate = new ArrayList<>();
                List<Object> nightDirectRate = new ArrayList<>();
                List<Object> totalDirectRate = new ArrayList<>();
                List<Object> whiteDirectRate1 = new ArrayList<>();
                List<Object> nightDirectRate1 = new ArrayList<>();
                List<Object> totalDirectRate1 = new ArrayList<>();
                whiteDirectRate.add("白班 直通率");
                nightDirectRate.add("夜班 直通率");
                totalDirectRate.add("合计 直通率");

                DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
                vo.setData(tp.getLabel());
                vo.setfFac(factoryId);
                vo.setfBigpro(project);


                // 初始化数据行（根据是否为汇总行）
                // 处理每日/周/月详细数据
                for (DfProcessProjectConfig dp : dplist) {
                    String pro = dp.getProcessName();
                    DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService = BeanUtils.getBean(DfQmsIpqcWaigTotalServiceImpl.class);
                    List<DfQmsIpqcWaigTotal> rates = dfQmsIpqcWaigTotalService.listOkRate(
                            newstartDateTime,
                            newEndDate,
                            0,
                            23,
                            project,
                            pro,
                            period
                    );
                    // 定义班次统计变量
                    int whiteSpotCheck = 0, BOkNum = 0, BNgNum = 0;
                    int nightSpotCheck = 0, WOkNum = 0, WNgNum = 0;

                    if (rates == null || rates.isEmpty()) {
                        // 如果该工序没有数据，则添加默认值
                        whiteShiftYieldRate.add(1);
                        whiteShiftYieldRate1.add("100%");
                        whiteSpotCheckCount.add(0);
                        whiteOkNum.add(0);
                        whiteNg.add(0);

                        nightShiftYieldRate.add(1);
                        nightShiftYieldRate1.add("100%");
                        nightSpotCheckCount.add(0);
                        nightOkNum.add(0);
                        nightNg.add(0);
                    } else {
                        // 遍历数据，根据检查时间划分班次
                        for (DfQmsIpqcWaigTotal rate : rates) {
                            int hour = extractHourFromDate(rate.getfTime());
                            if (hour >= 0 && hour <= 11) { // 白班（0-11时）
                                whiteSpotCheck += rate.getSpotCheckCount();
                                BOkNum += Integer.parseInt(rate.getOkNum());
                                BNgNum += Integer.parseInt(rate.getNgNum());
                            } else { // 晚班（12-23时）
                                nightSpotCheck += rate.getSpotCheckCount();
                                WOkNum += Integer.parseInt(rate.getOkNum());
                                WNgNum += Integer.parseInt(rate.getNgNum());
                            }
                        }
                        // 白班数据处理
                        if (whiteSpotCheck > 0) {
                            double whiteYield = calculateYield(whiteSpotCheck, BOkNum);
                            whiteShiftYieldRate.add(whiteYield);
                            whiteShiftYieldRate1.add(formatPercent(whiteYield));
                            whiteSpotCheckCount.add(whiteSpotCheck);
                        } else {
                            // 没有抽检样本时默认良率100%
                            whiteShiftYieldRate.add(1);
                            whiteShiftYieldRate1.add("100%");
                            whiteSpotCheckCount.add(0);
                        }
                        whiteOkNum.add(BOkNum);
                        whiteNg.add(BNgNum);

                        // 晚班数据处理
                        if (nightSpotCheck > 0) {
                            double nightYield = calculateYield(nightSpotCheck, WOkNum);
                            nightShiftYieldRate.add(nightYield);
                            nightShiftYieldRate1.add(formatPercent(nightYield));
                            nightSpotCheckCount.add(nightSpotCheck);
                        } else {
                            nightShiftYieldRate.add(1);
                            nightShiftYieldRate1.add("100%");
                            nightSpotCheckCount.add(0);
                        }
                        nightOkNum.add(WOkNum);
                        nightNg.add(WNgNum);
                    }
                    calculateShiftYieldRates(whiteSpotCheckCount,
                            nightSpotCheckCount,
                            whiteOkNum,
                            nightOkNum,
                            whiteShiftYieldRate,
                            nightShiftYieldRate,
                            whiteShiftYieldRate1,
                            nightShiftYieldRate1,
                            totalShiftYieldRate,
                            totalShiftYieldRate1,
                            totalDirectRate,
                            totalDirectRate1,
                            whiteDirectRate,
                            whiteDirectRate1,
                            nightDirectRate,
                            nightDirectRate1);

                } // end for 每个工序
                // 构造报表数据（以白班、晚班、合计为例）
                List<List<Object>> datas2 = new ArrayList<>();
                datas2.add(whiteSpotCheckCount);
                datas2.add(whiteOkNum);
                datas2.add(whiteNg);
                datas2.add(whiteShiftYieldRate1);
                datas2.add(whiteDirectRate1);

                List<List<Object>> wdatas2 = new ArrayList<>();
                wdatas2.add(nightSpotCheckCount);
                wdatas2.add(nightOkNum);
                wdatas2.add(nightNg);
                wdatas2.add(nightShiftYieldRate1);
                wdatas2.add(nightDirectRate1);

                List<DfQmsIpqcWaigTotalBo> testB0List = new ArrayList<>();
                DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
                testB0.setClassPlease("白班");
                testB0.setData(datas2);
                testB0List.add(testB0);

                DfQmsIpqcWaigTotalBo wtestB0 = new DfQmsIpqcWaigTotalBo();
                wtestB0.setClassPlease("晚班");
                wtestB0.setData(wdatas2);
                testB0List.add(wtestB0);

                DfQmsIpqcWaigTotalBo testTotalB0 = new DfQmsIpqcWaigTotalBo();
                testTotalB0.setClassPlease("合计");
                List<List<Object>> Hdatas2 = new ArrayList<>();
                Hdatas2.add(totalShiftYieldRate1);
                Hdatas2.add(totalDirectRate1);
                testTotalB0.setData(Hdatas2);
                testB0List.add(testTotalB0);

                vo.setTestBOs(testB0List);

                // 如果第一次处理，将良率和直通率数据添加到主数据列表中
                if (count == 0) {
                    count++;
                    sheetDataList.add(whiteShiftYieldRate);
                    sheetDataList.add(nightShiftYieldRate);
                    sheetDataList.add(totalShiftYieldRate);
                    barChartsheetDataList.add(whiteDirectRate);
                    barChartsheetDataList.add(nightDirectRate);
                    barChartsheetDataList.add(totalDirectRate);
                }
                testV0List.add(vo);
            }
        }

        List<Object> prcNames = new ArrayList<>();
        prcNames.add(""); // 在开始添加空字符串
        prcNames.addAll(dplist.stream()
                .map(DfProcessProjectConfig::getProcessName)  // 提取字段
                .distinct()  // 去重
                .collect(Collectors.toList()));  // 转换为列表并添加到 prcNames

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 30, 30, testV0List.get(0).getfFac() + testV0List.get(0).getfBigpro() + "各工序直通率抽检良率", "X轴", "Y轴");
        //表格添加柱形图表
        createBarChart(sheet, sheetDataList, prcNames, barChartchartEntity, barChartsheetDataList);

        //生成表格
        int col2 = barChartchartEntity.getCol2();
        //设置表头
        //创建行
        SXSSFRow row = sheet.createRow(col2);
        XSSFCellStyle cellParamStyle4 = createCellParamStyle(wb);

        //创建表头前三列
        SXSSFCell cell0 = row.createCell(0); // 创建单元格
        cell0.setCellValue("日期");
        cell0.setCellStyle(cellParamStyle4);

        SXSSFCell cell1 = row.createCell(1); // 创建单元格
        cell1.setCellValue("厂别");
        cell1.setCellStyle(cellParamStyle4);
        SXSSFCell cell2 = row.createCell(2); // 创建单元格
        cell2.setCellValue("项目");
        cell2.setCellStyle(cellParamStyle4);

        SXSSFCell cell3 = row.createCell(3); // 创建单元格
        cell3.setCellValue("班别");
        cell3.setCellStyle(cellParamStyle4);

        SXSSFCell cell4 = row.createCell(4); // 创建单元格
        cell4.setCellValue("工序");
        cell4.setCellStyle(cellParamStyle4);

        for (int i = 0; i < dplist.size(); i++) {
            // 创建单元格
            SXSSFCell cell = row.createCell(i + 5);
            // 设置单元格值
            cell.setCellValue(dplist.get(i).getProcessName().toString());
            cell.setCellStyle(cellParamStyle4);
        }

        int Index2 = col2 + 1;
        for (int i = 0; i < testV0List.size(); i++) {
            DfQmsIpqcWaigTotalVo testVO = testV0List.get(i);
            //新建15行，并合并
            for (int j = 0; j < 12; j++) {
                sheet.createRow(Index2 + j);
            }
            SXSSFRow row1 = sheet.getRow(Index2);
            sheet.addMergedRegion(new CellRangeAddress(Index2, Index2 + 11, 0, 0));
            SXSSFCell cell00 = row1.createCell(0);
            cell00.setCellStyle(cellParamStyle4);
            // 设置单元格值
            cell00.setCellValue(testVO.getData());
            sheet.addMergedRegion(new CellRangeAddress(Index2, Index2 + 11, 1, 1));
            SXSSFCell cell11 = row1.createCell(1);
            cell11.setCellStyle(cellParamStyle4);
            // 设置单元格值
            cell11.setCellValue(testVO.getfFac());
            sheet.addMergedRegion(new CellRangeAddress(Index2, Index2 + 11, 2, 2));

            SXSSFCell cell22 = row1.createCell(2);
            cell22.setCellStyle(cellParamStyle4);

            // 设置单元格值
            cell22.setCellValue(testVO.getfBigpro());

            List<DfQmsIpqcWaigTotalBo> testBOs = testVO.getTestBOs();

            int index = Index2;
            int boIndex = Index2;
            for (int j = 0; j <= testBOs.size() - 1; j++) {
                DfQmsIpqcWaigTotalBo testBO = testBOs.get(j);

                // 判断是否是“合计”
                boolean isTotal = "合计".equals(testBO.getClassPlease());

                // 合并单元格的处理
                sheet.addMergedRegion(new CellRangeAddress(index, index + (isTotal ? 1 : 4), 3, 3));

                SXSSFRow rowBO = sheet.getRow(index);
                SXSSFCell cellBO = rowBO.createCell(3);
                cellBO.setCellStyle(cellParamStyle4);

                // 设置单元格值
                cellBO.setCellValue(String.valueOf(testBO.getClassPlease()));
                // 如果不是“合计”，设置其余单元格
                if (!isTotal) {
                    SXSSFCell cellBO4 = rowBO.createCell(4);
                    // 设置单元格值
                    cellBO4.setCellValue("抽检数");
                    cellBO4.setCellStyle(cellParamStyle4);

                    SXSSFRow rowBO1 = sheet.getRow(index + 1);
                    SXSSFCell cellBO1 = rowBO1.createCell(4);
                    // 设置单元格值
                    cellBO1.setCellValue("OK数");
                    cellBO1.setCellStyle(cellParamStyle4);


                    SXSSFRow rowBO2 = sheet.getRow(index + 2);
                    SXSSFCell cellBO2 = rowBO2.createCell(4);
                    // 设置单元格值
                    cellBO2.setCellValue("NG数");
                    cellBO2.setCellStyle(cellParamStyle4);
                    SXSSFRow rowBO3 = sheet.getRow(index + 3);
                    SXSSFCell cellBO3 = rowBO3.createCell(4);
                    // 设置单元格值
                    cellBO3.setCellValue("良率");
                    cellBO3.setCellStyle(cellParamStyle4);

                    SXSSFRow rowBO44 = sheet.getRow(index + 4);
                    SXSSFCell cellBO44 = rowBO44.createCell(4);
                    // 设置单元格值
                    cellBO44.setCellValue("直通率");
                    cellBO44.setCellStyle(cellParamStyle4);

                } else {
                    // 如果是“合计”，只需要“良率”和“直通率”
                    SXSSFRow rowBO3 = sheet.getRow(index);
                    SXSSFCell cellBO3 = rowBO3.createCell(4);
                    // 设置单元格值
                    cellBO3.setCellValue("良率");
                    cellBO3.setCellStyle(cellParamStyle4);
                    SXSSFRow rowBO44 = sheet.getRow(index + 1);
                    SXSSFCell cellBO44 = rowBO44.createCell(4);
                    // 设置单元格值
                    cellBO44.setCellValue("直通率");
                    cellBO44.setCellStyle(cellParamStyle4);
                    // 只需要占两行，所以index + 1行不处理
                }

                // 处理testBO数据的部分
                List<List<Object>> data1 = testBO.getData();
                for (int k = 0; k <= data1.size() - 1; k++) {
                    List<Object> strings = data1.get(k);
                    SXSSFRow rowK = sheet.getRow(boIndex);
                    if (rowK == null) {
                        rowK = sheet.createRow(boIndex);
                    }
                    for (int z = 0; z <= strings.size() - 1; z++) {
                        SXSSFCell cell = rowK.createCell(z + 5);
                        // 设置单元格值
                        cell.setCellValue(strings.get(z).toString());
                        cell.setCellStyle(cellParamStyle4);

                    }

                    boIndex++;
                }

                index += (isTotal ? 2 : 5); // 如果是“合计”，只增加2行，否则增加5行
            }

            Index2 += 12;
        }
        return cellParamStyle4;

    }


    private void Sheet6(List<DfProcessProjectConfig> dplist, List<DfAuditDetail> listData, SXSSFSheet sheet6, SXSSFWorkbook wb, int col2, XSSFCellStyle cellParamStyle4) {
        List<List<Object>> sheetDataList6 = new ArrayList<>();
        // 初始化一个二维列表，用于存储直通率数据
        List<List<Object>> barChartsheetDataList6 = new ArrayList<>();


        List<DfQmsIpqcWaigTotalVo> testV0List6 = new ArrayList<>();


        // 这个集合应该已经有值
//        Map<String, List<DfAuditDetail>> groupedData6 = listData.stream()
//                .sorted(Comparator.comparing(DfAuditDetail::getDate).reversed()) // 按日期降序排序
//                .collect(Collectors.groupingBy(
//                        item -> String.join("_", item.getFFac()),
//                        LinkedHashMap::new, // 保持顺序
//                        Collectors.toList()
//                ));

        int count6 = 0;

        // 遍历每个分组，计算 SpotCheckCount 和 okNum 累加
        // for (Map.Entry<String, List<DfAuditDetail>> entry : groupedData6.entrySet()) {
        List<Object> whiteShiftYieldRate = new ArrayList<>();
        List<Object> nightShiftYieldRate = new ArrayList<>();
        List<Object> whiteShiftYieldRate1 = new ArrayList<>();
        List<Object> nightShiftYieldRate1 = new ArrayList<>();
        List<Object> whiteNum = new ArrayList<>();
        List<Object> nightNum = new ArrayList<>();
        List<Object> totalNum = new ArrayList<>();
        List<Object> whiteNum1 = new ArrayList<>();
        List<Object> nightNum1 = new ArrayList<>();
        List<Object> totalNum1 = new ArrayList<>();
        List<Object> totalShiftYieldRate = new ArrayList<>();
        List<Object> totalShiftYieldRate1 = new ArrayList<>();


        whiteNum.add("白班 超时次数");
        nightNum.add("晚班 超时次数");
        totalNum.add("合计 超时次数");
        whiteShiftYieldRate.add("白班 超时占比");
        nightShiftYieldRate.add("夜班 超时占比");
        totalShiftYieldRate.add("合计 超时占比");

        //  String key = entry.getKey();
        // String[] keys = key.split("_");
        DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
        String formattedData = formatDateRange(startDate, endDate);

        vo.setData(formattedData);
        vo.setfFac(factoryId);
        // vo.setfBigpro(keys[2]);

        Map<String, DfAuditDetail> bmap6 = new HashMap<>();
        Map<String, DfAuditDetail> wmap6 = new HashMap<>();

        // List<DfAuditDetail> group = groupedData6.get(key);


        int totalCount = 0;
        int bCount = 0;
        int wCount = 0;
        for (DfAuditDetail dfAuditDetail : listData) {
            String keyName = dfAuditDetail.getProcess();
            String claess = dfAuditDetail.getShift();
            if ("白班".equals(claess) && !"未超时".equals(dfAuditDetail.getOverTime())) {
                if (bmap6.containsKey(keyName)) {
                    // 获取已经存在的对象
                    DfAuditDetail existing = bmap6.get(keyName);
                    if (existing != null) {
                        Integer overTimeTimes = existing.getOvertimeTimes();
                        overTimeTimes += 1;
                        existing.setOvertimeTimes(overTimeTimes);
                    }
                    bmap6.put(keyName, existing);
                } else {
                    Integer overTimeTimes = 0;
                    if (dfAuditDetail != null) {
                        overTimeTimes = 1;
                        dfAuditDetail.setOvertimeTimes(overTimeTimes);
                        // 如果不存在相同的键，直接添加
                        bmap6.put(keyName, dfAuditDetail);
                    }

                }
                bCount++;
            } else if ("晚班".equals(claess) && !"未超时".equals(dfAuditDetail.getOverTime())) {
                if (wmap6.containsKey(keyName)) {
                    DfAuditDetail existing = wmap6.get(keyName);
                    if (existing != null) {
                        Integer overTimeTimes = existing.getOvertimeTimes();
                        overTimeTimes += 1;
                        existing.setOvertimeTimes(overTimeTimes);
                    }
                    wmap6.put(keyName, existing);
                } else {
                    Integer overTimeTimes = 0;
                    if (dfAuditDetail != null) {
                        overTimeTimes = 1;
                        dfAuditDetail.setOvertimeTimes(overTimeTimes);
                        // 如果不存在相同的键，直接添加
                        wmap6.put(keyName, dfAuditDetail);
                    }

                }
                wCount++;
            }

        }

        totalCount = bCount + wCount;

        for (int i = 0; i < dplist.size(); i++) {
            String pro = dplist.get(i).getProcessName();
            DfAuditDetail dfAuditDetail = bmap6.get(pro);
            DfAuditDetail wdfAuditDetail = wmap6.get(pro);
            if (dfAuditDetail == null) {
                whiteShiftYieldRate.add(0); // 如果没有样本，添加 0.0 到白班良率列表中
                whiteShiftYieldRate1.add("0%");
                whiteNum.add(0);
                whiteNum1.add(0);

            } else {
                Integer num = dfAuditDetail.getOvertimeTimes();
                if (bCount > 0) { // 确保总样本检查计数不为零，避免除零异常
                    double numRate = Double.parseDouble(String.valueOf(num)) / bCount;
                    whiteShiftYieldRate1.add(String.format("%.2f%%", numRate * 100));
                    whiteShiftYieldRate.add(numRate); // 计算合格率并添加到白班良率列表中
                    whiteNum.add(num);
                    whiteNum1.add(num);

                } else {
                    whiteShiftYieldRate.add(0); // 如果没有样本，添加 0.0 到白班良率列表中
                    whiteShiftYieldRate1.add("0%");
                    whiteNum.add(0);
                    whiteNum1.add(0);
                }
            }

            if (wdfAuditDetail == null) {
                nightShiftYieldRate1.add("0%"); // 如果没有样本，添加 0.0 到白班良率列表中
                nightShiftYieldRate.add(0);
                nightNum.add(0);
                nightNum1.add(0);

            } else {
                Integer num = wdfAuditDetail.getOvertimeTimes();
                if (wCount > 0) { // 确保总样本检查计数不为零，避免除零异常
                    double numRate = Double.parseDouble(String.valueOf(num)) / wCount;
                    nightShiftYieldRate1.add(String.format("%.2f%%", numRate * 100));
                    nightShiftYieldRate.add(numRate); // 计算合格率并添加到白班良率列表中
                    nightNum.add(num);
                    nightNum1.add(num);

                } else {
                    nightShiftYieldRate.add(0); // 如果没有样本，添加 0.0 到白班良率列表中
                    nightShiftYieldRate1.add("0%");
                    nightNum.add(0);
                    nightNum1.add(0);
                }

            }
            int bnum = 0;
            int wnum = 0;
            if (dfAuditDetail != null) {
                bnum = dfAuditDetail.getOvertimeTimes();
            }
            if (wdfAuditDetail != null) {
                wnum = wdfAuditDetail.getOvertimeTimes();
            }
            Integer hj = bnum + wnum;
            if (totalCount > 0) {
                double hjRate = Double.parseDouble(String.valueOf(hj)) / totalCount;
                hj = hj / 100;
                totalNum.add(hj);
                totalShiftYieldRate1.add(String.format("%.2f%%", hjRate * 100));
                totalShiftYieldRate.add(hjRate);
                totalNum1.add(hj);
            } else {
                totalNum.add(0);
                totalShiftYieldRate1.add("0%");
                totalShiftYieldRate.add(0);
                totalNum1.add(0);
            }

        }

        List<List<Object>> datas2 = new ArrayList<>();
        List<List<Object>> wdatas2 = new ArrayList<>();
        whiteNum1.add(bCount);
        whiteShiftYieldRate1.add("100%");
        datas2.add(whiteNum1);
        datas2.add(whiteShiftYieldRate1);
        List<DfQmsIpqcWaigTotalBo> testB0List = new ArrayList<>();
        DfQmsIpqcWaigTotalBo testB0 = new DfQmsIpqcWaigTotalBo();
        testB0.setClassPlease("白班");
        testB0.setData(datas2);
        testB0List.add(testB0);
        vo.setTestBOs(testB0List);

        nightNum1.add(wCount);
        nightShiftYieldRate1.add("100%");
        wdatas2.add(nightNum1);
        wdatas2.add(nightShiftYieldRate1);
        DfQmsIpqcWaigTotalBo wtestB0 = new DfQmsIpqcWaigTotalBo();
        wtestB0.setClassPlease("晚班");
        wtestB0.setData(wdatas2);
        testB0List.add(wtestB0);

        DfQmsIpqcWaigTotalBo testTotalB0 = new DfQmsIpqcWaigTotalBo();
        testTotalB0.setClassPlease("合计");
        List<List<Object>> Hdatas2 = new ArrayList<>();
        totalNum1.add(totalCount);
        totalShiftYieldRate1.add("100%");
        Hdatas2.add(totalNum1);
        Hdatas2.add(totalShiftYieldRate1);
        testTotalB0.setData(Hdatas2);
        testB0List.add(testTotalB0);
        vo.setTestBOs(testB0List);

        if (count6 == 0) {
            count6++;
            // 将白班良率数据添加到主数据列表中
            sheetDataList6.add(whiteShiftYieldRate);
            // 将晚班良率数据添加到主数据列表中
            sheetDataList6.add(nightShiftYieldRate);
            // 将总良率数据添加到主数据列表中
            sheetDataList6.add(totalShiftYieldRate);
            // 将白班直通率数据添加到直通率数据列表中
            barChartsheetDataList6.add(whiteNum);
            // 将晚班直通率数据添加到直通率数据列表中
            barChartsheetDataList6.add(nightNum);
            // 将总直通率数据添加到直通率数据列表中
            barChartsheetDataList6.add(totalNum);

        }
        testV0List6.add(vo);
        // }

        // 按照日期降序进行排序
        Collections.sort(testV0List6, new Comparator<DfQmsIpqcWaigTotalVo>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public int compare(DfQmsIpqcWaigTotalVo vo1, DfQmsIpqcWaigTotalVo vo2) {
                try {
                    Date date1 = sdf.parse(vo1.getData());
                    Date date2 = sdf.parse(vo2.getData());
                    // 使用反向的比较方式，确保按降序排列
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;  // 如果发生解析异常，则返回 0 (不排序)
                }
            }
        });


        List<Object> prcNames6 = new ArrayList<>();
        prcNames6.add(""); // 在开始添加空字符串
        prcNames6.addAll(dplist.stream().map(DfProcessProjectConfig::getProcessName).collect(Collectors.toList()));
        ChartEntity barChartchartEntity6 = new ChartEntity("LINE", 0, 0, 30, 30, testV0List6.get(0).getData() + testV0List6.get(0).getfFac() + "各工序直通率抽检良率", "X轴", "Y轴");
        //表格添加柱形图表
        createBarChart6(sheet6, sheetDataList6, prcNames6, barChartchartEntity6, barChartsheetDataList6);

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

        SXSSFCell cell16 = row6.createCell(2); // 创建单元格
        cell16.setCellValue("班别");
        cell16.setCellStyle(cellParamStyleS6);

        SXSSFCell cell26 = row6.createCell(3); // 创建单元格
        cell26.setCellValue("工序");
        cell26.setCellStyle(cellParamStyleS6);

        DfProcessProjectConfig s = new DfProcessProjectConfig();
        s.setProcessName("合计");
        dplist.add(s);
        for (int i = 0; i < dplist.size(); i++) {
            // 创建单元格
            SXSSFCell cell = row6.createCell(i + 4);
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
            sheet6.addMergedRegion(new CellRangeAddress(Index6, Index6 + 5, 0, 0));
            SXSSFCell cell00 = row1.createCell(0);
            cell00.setCellStyle(cellParamStyleS6);
            // 设置单元格值
            cell00.setCellValue(testVO.getData());


            sheet6.addMergedRegion(new CellRangeAddress(Index6, Index6 + 5, 1, 1));
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
                sheet6.addMergedRegion(new CellRangeAddress(index, index + 1, 2, 2));
                SXSSFRow rowBO = sheet6.getRow(index);
                SXSSFCell cellBO = rowBO.createCell(2);
                cellBO.setCellStyle(cellParamStyle4);
                // 设置单元格值
                cellBO.setCellValue(String.valueOf(testBO.getClassPlease()));


                SXSSFCell cellBO4 = rowBO.createCell(3);
                // 设置单元格值
                cellBO4.setCellValue("超时次数");
                cellBO4.setCellStyle(cellParamStyle4);

                SXSSFRow rowBO1 = sheet6.getRow(index + 1);
                SXSSFCell cellBO1 = rowBO1.createCell(3);
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
                        SXSSFCell cell = rowK.createCell(z + 4);
                        // 设置单元格值
                        cell.setCellValue(strings.get(z).toString());
                        cell.setCellStyle(cellParamStyleS6);

                    }

                    boIndex++;
                }

                index += 2; // 如果是“合计”，只增加2行，否则增加5行
            }

            Index6 += 6;
        }
    }


    public void exportExcleAuditNgDetail(ExcelCheckExportUtil2 excelExportUtil2, Sheet wbSheet, SXSSFWorkbook wb) {
        data = excelExportUtil2.getData();
        heardKey = excelExportUtil2.getHeardKey();
        heardList = excelExportUtil2.getHeardList();
        XSSFCellStyle style = createTitleCellStyle5(wb);
        XSSFCellStyle style2 = createCellParamStyle(wb);

        // 设置默认列宽（可选，根据需求调整）
        wbSheet.setDefaultColumnWidth(20);

        // 创建表头行
        Row headerRow = wbSheet.createRow(0);
        int[] maxColumnWidths = new int[heardList.length];

        // 计算表头列宽
        for (int i = 0; i < heardList.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(heardList[i]);
            cell.setCellStyle(style);
            // 计算表头的实际字符宽度（中文按2，英文按1）
            maxColumnWidths[i] = calculateStringWidth(heardList[i]);
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
                int cellWidth = calculateStringWidth(value);
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
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }


    public static String formatDateRange(String startDateStr, String endDateStr) {
        // 原始的日期格式
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 目标日期格式：只需要年月日 (yyyy/MM/dd)
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd");

        // 解析开始日期和结束日期
        Date startDate = null;
        Date endDate = null;

        try {
            // 尝试解析日期
            startDate = originalFormat.parse(startDateStr);
            endDate = originalFormat.parse(endDateStr);
        } catch (Exception e) {
            // 捕获解析异常并打印日志
            System.err.println("日期解析失败: " + e.getMessage());
            return "日期格式错误";  // 返回一个默认的错误信息或空字符串
        }

        // 格式化为目标格式并返回
        String formattedStartDate = targetFormat.format(startDate);
        String formattedEndDate = targetFormat.format(endDate);

        return formattedStartDate + "-" + formattedEndDate;
    }


    private List<DfQmsIpqcWaigTotalVo> getDfQmsIpqcWaigTotalVos(List<DfProject> totalShiftList,List<DfProcessProjectConfig> dplist) {


        // 初始化时间范围
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DfQmsIpqcWaigTotalVo> testV0List410 = new ArrayList<>();
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 00:00:00", formatter);

        // 根据周期分割时间段
        List<TimePeriod> timePeriods = DateSplitter.splitPeriod(start, end, period);

        // 遍历每个时间段
        for (TimePeriod tp : timePeriods) {
            // 调整结束时间为次日7点（原逻辑）
            LocalDateTime newEndDateTime = tp.getEnd().plusDays(1).withHour(7).withMinute(0).withSecond(0);
            String newEndDate = newEndDateTime.format(formatter);

            String newstartDateTime = tp.getStart().format(formatter);


            // 遍历每一组数据
            for (DfProject dfProject : totalShiftList) {
                for (DfProcessProjectConfig dfProcess : dplist) {
                    String type = "机台+不良项";
                    DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
                    String fBigpro = dfProcess.getProcessName();
                    String fSeq = dfProcess.getProcessName();
                    vo.setData(tp.getLabel());
                    vo.setfFac(factoryId);
                    vo.setfBigpro(fBigpro);
                    vo.setFseq(fSeq);
                    vo.setFmac("");
                    // 定义 banbie 和数据列表
                    List<Banbie> banbies10 = createBanbies(newstartDateTime, newEndDate, fBigpro, fSeq, type, null, tp.getLabel());
                    vo.setBbList(banbies10);
                    testV0List410.add(vo);
                }
            }
        }
        return testV0List410;
    }


/*

    private static List<DfQmsIpqcWaigTotalVo> getDfQmsIpqcWaigTotalVos
            (ArrayList<Map<String, DfQmsIpqcWaigTotal>> dpmap10) {
        Map<String, DfQmsIpqcWaigTotal> ro10 = dpmap10.get(0);
        Map<String, DfQmsIpqcWaigTotal> ng10 = dpmap10.get(1);
        Map<String, DfQmsIpqcWaigTotal> zh10 = dpmap10.get(2);


        ArrayList<HzData> BbjtData10 = new ArrayList<>();
        Banbie banbie310 = new Banbie();
        banbie310.setName("白班");
        ArrayList<HzData> wBjtData10 = new ArrayList<>();
        ArrayList<HzData> hZjtData10 = new ArrayList<>();

        Banbie banbie210 = new Banbie();
        banbie210.setName("汇总");
        Banbie banbie410 = new Banbie();
        banbie410.setName("夜班");
        DfQmsIpqcWaigTotalVo testv0110 = new DfQmsIpqcWaigTotalVo();
        List<DfQmsIpqcWaigTotalVo> testV0List410 = new ArrayList<>();
        zh10.forEach((key, value) -> {
            if (zh10.size() > 0 && ro10.containsKey(key)) {
                // 如果匹配，则获取 Map 中的 DfQmsIpqcWaigTotal 对象
                DfQmsIpqcWaigTotal v = ro10.get(key);
                HzData data = new HzData();
                data.setfSort(v.getfSort());
                data.setfFmac(v.getfMac());
                data.setSpotCheckCount(v.getSpotCheckCount());
                data.setNgNum(Integer.valueOf(v.ngNum));
                data.setNgRate(Double.valueOf(v.ngRate * 100));
                BbjtData10.add(data);
            } else {
                HzData data = new HzData();
                data.setfSort(zh10.get(key).getfSort());
                data.setSpotCheckCount(0);
                data.setNgNum(0);
                data.setNgRate(0);
                BbjtData10.add(data);
            }
            if (ng10.containsKey(key)) {
                // 如果匹配，则获取 Map 中的 DfQmsIpqcWaigTotal 对象
                DfQmsIpqcWaigTotal e = zh10.get(key);
                DfQmsIpqcWaigTotal v = ng10.get(key);
                testv0110.setData(e.getDate());
                testv0110.setfFac(e.getfFac());
                testv0110.setfBigpro(e.getfBigpro());
                testv0110.setFseq(e.getfSeq());
                testv0110.setFmac(e.getfMac());
                HzData data2 = new HzData();
                data2.setfSort(v.getfSort());
                data2.setSpotCheckCount(v.getSpotCheckCount());
                data2.setNgNum(Integer.valueOf(v.ngNum));
                data2.setNgRate(Double.valueOf(v.ngRate * 100));
                wBjtData10.add(data2);
            } else {
                HzData data2 = new HzData();
                data2.setfSort(zh10.get(key).getfSort());
                data2.setSpotCheckCount(0);
                data2.setNgNum(Integer.valueOf(0));
                data2.setNgRate(Double.valueOf(0));
                wBjtData10.add(data2);
            }
            HzData data3 = new HzData();
            data3.setfSeq(zh10.get(key).fSeq);
            data3.setfSort(zh10.get(key).getfSort());
            data3.setfFmac(zh10.get(key).fMac);
            data3.setSpotCheckCount(zh10.get(key).getSpotCheckCount());
            data3.setNgNum(Integer.valueOf(zh10.get(key).ngNum));
            data3.setNgRate(Double.valueOf(zh10.get(key).ngRate * 100));
            hZjtData10.add(data3);

        });
        banbie310.setJTDatas(BbjtData10);
        banbie210.setJTDatas(hZjtData10);
        banbie410.setJTDatas(wBjtData10);
        ArrayList<Banbie> banbies10 = new ArrayList<>();
        banbies10.add(banbie210);
        banbies10.add(banbie310);
        banbies10.add(banbie410);
        testv0110.setBbList(banbies10);
        testV0List410.add(testv0110);
        return testV0List410;
    }
*/










/*    private static List<DfQmsIpqcWaigTotalVo> getDfQmsIpqcWaigTotalVos3
            (ArrayList<Map<String, DfQmsIpqcWaigTotal>> dpmap10) {
        Map<String, DfQmsIpqcWaigTotal> ro10 = dpmap10.get(0);
        Map<String, DfQmsIpqcWaigTotal> ng10 = dpmap10.get(1);
        Map<String, DfQmsIpqcWaigTotal> zh10 = dpmap10.get(2);


        ArrayList<HzData> BbjtData10 = new ArrayList<>();
        Banbie banbie310 = new Banbie();
        banbie310.setName("白班");
        ArrayList<HzData> wBjtData10 = new ArrayList<>();
        ArrayList<HzData> hZjtData10 = new ArrayList<>();

        Banbie banbie210 = new Banbie();
        banbie210.setName("汇总");
        Banbie banbie410 = new Banbie();
        banbie410.setName("夜班");
        DfQmsIpqcWaigTotalVo testv0110 = new DfQmsIpqcWaigTotalVo();
        List<DfQmsIpqcWaigTotalVo> testV0List410 = new ArrayList<>();
        zh10.forEach((key, value) -> {
            if (zh10.size() > 0 && ro10.containsKey(key)) {
                // 如果匹配，则获取 Map 中的 DfQmsIpqcWaigTotal 对象
                DfQmsIpqcWaigTotal v = ro10.get(key);
                HzData data = new HzData();
                data.setfSort(v.getfSort());
                data.setfFmac(v.getfMac());
                data.setSpotCheckCount(v.getSpotCheckCount());
                data.setNgNum(Integer.valueOf(v.ngNum));
                data.setNgRate(Double.valueOf(v.ngRate * 100));
                BbjtData10.add(data);
            } else {
                HzData data = new HzData();
                data.setfSort(zh10.get(key).getfSort());
                data.setSpotCheckCount(0);
                data.setNgNum(0);
                data.setNgRate(0);
                BbjtData10.add(data);
            }
            if (ng10.containsKey(key)) {
                // 如果匹配，则获取 Map 中的 DfQmsIpqcWaigTotal 对象
                DfQmsIpqcWaigTotal e = zh10.get(key);
                DfQmsIpqcWaigTotal v = ng10.get(key);
                testv0110.setData(e.getDate());
                testv0110.setfFac(e.getfFac());
                testv0110.setfBigpro(e.getfBigpro());
                testv0110.setFseq(e.getfSeq());
                testv0110.setFmac(e.getfMac());
                testV0List410.add(testv0110);

                HzData data2 = new HzData();
                data2.setfSort(v.getfSort());
                data2.setSpotCheckCount(v.getSpotCheckCount());
                data2.setNgNum(Integer.valueOf(v.ngNum));
                data2.setNgRate(Double.valueOf(v.ngRate * 100));
                wBjtData10.add(data2);
            } else {
                HzData data2 = new HzData();
                data2.setfSort(zh10.get(key).getfSort());
                data2.setSpotCheckCount(0);
                data2.setNgNum(Integer.valueOf(0));
                data2.setNgRate(Double.valueOf(0));
                wBjtData10.add(data2);
            }
            HzData data3 = new HzData();
            data3.setfSeq(zh10.get(key).fSeq);
            data3.setfSort(zh10.get(key).getfSort());
            data3.setfFmac(zh10.get(key).fMac);
            data3.setSpotCheckCount(zh10.get(key).getSpotCheckCount());
            data3.setNgNum(Integer.valueOf(zh10.get(key).ngNum));
            data3.setNgRate(Double.valueOf(zh10.get(key).ngRate * 100));
            hZjtData10.add(data3);

        });
        banbie310.setJTDatas(BbjtData10);
        banbie210.setJTDatas(hZjtData10);
        banbie410.setJTDatas(wBjtData10);
        ArrayList<Banbie> banbies10 = new ArrayList<>();
        banbies10.add(banbie210);
        banbies10.add(banbie310);
        banbies10.add(banbie410);
        testv0110.setBbList(banbies10);
        testV0List410.add(testv0110);
        return testV0List410;
    }*/

    // 创建一个公共方法来处理数据的合并和计算
    private void updateShiftData(Map<String, DfQmsIpqcWaigTotalCheck> shiftMap, DfQmsIpqcWaigTotalCheck dfQmsIpqcWaigTotal) {
        String keyName = dfQmsIpqcWaigTotal.getfSeq();
        String claess = dfQmsIpqcWaigTotal.getShift();

        shiftMap.compute(keyName, (k, existing) -> {
            if (existing == null) {
                return dfQmsIpqcWaigTotal;
            } else {
                int oldOkNum = Integer.parseInt(existing.getOkNum());
                int oldSpotCheckCount = existing.getSpotCheckCount();
                int oldNgNum = Integer.parseInt(existing.getNgNum());

                int newOkNum = Integer.parseInt(dfQmsIpqcWaigTotal.getOkNum());
                int newSpotCheckCount = dfQmsIpqcWaigTotal.getSpotCheckCount();
                int newNgNum = Integer.parseInt(dfQmsIpqcWaigTotal.getNgNum());

                existing.setSpotCheckCount(oldSpotCheckCount + newSpotCheckCount);
                existing.setNgNum(String.valueOf(oldNgNum + newNgNum));
                existing.setOkNum(String.valueOf(oldOkNum + newOkNum));
                return existing;
            }
        });
    }


    private List<DfQmsIpqcWaigTotalVo> getDfQmsIpqcWaigTotalVos3(List<DfProject> totalShiftList, List<DfProcessProjectConfig> dplist) {


        // 初始化时间范围
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DfQmsIpqcWaigTotalVo> testV0List410 = new ArrayList<>();
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 00:00:00", formatter);

        // 根据周期分割时间段
        List<TimePeriod> timePeriods = DateSplitter.splitPeriod(start, end, period);

        // 遍历每个时间段
        for (TimePeriod tp : timePeriods) {
            // 调整结束时间为次日7点（原逻辑）
            LocalDateTime newEndDateTime = tp.getEnd().plusDays(1).withHour(7).withMinute(0).withSecond(0);
            String newEndDate = newEndDateTime.format(formatter);

            String newstartDateTime = tp.getStart().format(formatter);


            // 遍历每一组数据
            for (DfProject dfProject : totalShiftList) {
                for (DfProcessProjectConfig dfProcess : dplist) {
                    String type = "不良项";
                    String fBigpro = dfProject.getName();
                    String fSeq = dfProcess.getProcessName();
                    String formattedData = tp.getLabel();
                    DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
                    // 设置基本信息
                    vo.setData(formattedData);
                    vo.setfFac(factoryId);
                    vo.setfBigpro(fBigpro);
                    vo.setFseq(fSeq);

                    // 定义 banbie 和数据列表
                    List<Banbie> banbies10 = createBanbies(newstartDateTime, newEndDate, fBigpro, fSeq, type, null,tp.getLabel());

                    // 设置结果
                    vo.setBbList(banbies10);
                    testV0List410.add(vo);
                }
            }
        }
        return testV0List410;
    }


    private List<Banbie> createBanbies(String startDate, String endDate, String fBigpro, String fSeq, String type, String fmac,String lable) {
        List<Banbie> banbies = new ArrayList<>();

        // 使用异步处理每个班别的查询
        CompletableFuture<List<Rate>> whiteShiftFuture = CompletableFuture.supplyAsync(() -> {
            return createBanbieRates("白班", startDate, endDate, fBigpro, fSeq, 0, 11, type, fmac,lable);
        });

        CompletableFuture<List<Rate>> summaryFuture = CompletableFuture.supplyAsync(() -> {
            return createBanbieRates("汇总", startDate, endDate, fBigpro, fSeq, 0, 23, type, fmac,lable);
        });

        CompletableFuture<List<Rate>> nightShiftFuture = CompletableFuture.supplyAsync(() -> {
            return createBanbieRates("夜班", startDate, endDate, fBigpro, fSeq, 12, 23, type, fmac,lable);
        });

        // 使用allOf等待所有异步任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(whiteShiftFuture, summaryFuture, nightShiftFuture);

        try {
            // 等待所有异步任务完成
            allOf.get();

            // 将查询结果添加到班别列表
            Banbie banbie310 = createBanbieFromRates("白班", whiteShiftFuture.get(), startDate, endDate, fBigpro, fSeq, 0, 11, type, fmac);
            Banbie banbie210 = createBanbieFromRates("汇总", summaryFuture.get(), startDate, endDate, fBigpro, fSeq, 0, 23, type, fmac);
            Banbie banbie410 = createBanbieFromRates("夜班", nightShiftFuture.get(), startDate, endDate, fBigpro, fSeq, 12, 23, type, fmac);

            // 将所有班别加入列表
            banbies.add(banbie210);
            banbies.add(banbie310);
            banbies.add(banbie410);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return banbies;
    }

    /**
     * 创建班别的相关数据
     */
    private List<Rate> createBanbieRates(String banbieName, String startDate, String endDate, String fBigpro, String fSeq,
                                         int startHour, int endHour, String type, String fmac,String lable) {
        DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService = BeanUtils.getBean(DfQmsIpqcWaigTotalServiceImpl.class);
        if ("全工序".equals(fSeq)) {
            fSeq = "%%";
        }
        List<Rate> rates = new ArrayList<>();
        if ("机台".equals(type)) {
            rates = dfQmsIpqcWaigTotalService.listNgTop5Fmac("%%", fBigpro, "%%", fSeq, startDate, endDate, startHour, endHour);
        } else if ("不良项".equals(type)) {
            rates = dfQmsIpqcWaigTotalService.listNgTop103("%%", fBigpro, "%%", fSeq, startDate, endDate, startHour, endHour);
        } else if ("机台+不良项".equals(type)) {
            rates = dfQmsIpqcWaigTotalService.listNgTop10Fmac("%%", fBigpro, "%%", fSeq, startDate, endDate, startHour, endHour, fmac);
        }
        return rates;
    }

    /**
     * 根据查询结果生成班别数据
     */
    private Banbie createBanbieFromRates(String banbieName, List<Rate> rates, String startDate, String endDate,
                                         String fBigpro, String fSeq, int startHour, int endHour, String type, String fmac) {
        Banbie banbie = new Banbie();
        banbie.setName(banbieName);

        // 创建 HzData 列表并填充数据
        List<HzData> hzDataList = new ArrayList<>();
        for (Rate rate : rates) {
            HzData data = new HzData();
            data.setfFmac(rate.getName());
            data.setfSort(rate.getName());
            data.setSpotCheckCount(rate.getAllNum());
            data.setNgNum(rate.getNgNum());
            data.setNgRate(Double.valueOf(String.format("%.2f", rate.getRate() * 100)));
            hzDataList.add(data);
        }

        // 设置班别的数据
        banbie.setJTDatas(hzDataList);
        return banbie;
    }


//    private List<DfQmsIpqcWaigTotalVo> getDfQmsIpqcWaigTotalVoss(List<DfQmsIpqcWaigTotalCheck> totalShiftList) {
//        List<String> lastNineDates = new ArrayList<>();
//        // 获取最近9天的日期（格式 yyyy-MM-dd）
//        List<LocalDate> formattedDates = getLastNineDays(startDate, endDate);
//        formattedDates.sort(Comparator.naturalOrder());
//        lastNineDates.addAll(formattedDates.stream()
//                .map(LocalDate::toString)
//                .collect(Collectors.toList()));
//
//        // 按“项目-工序”分组（保留插入顺序）
//        List<DfQmsIpqcWaigTotalCheck> sortedTotalShiftList = totalShiftList.stream()
//                .sorted(Comparator.comparing(DfQmsIpqcWaigTotalCheck::getSort))
//                .collect(Collectors.toList());
//        Map<String, List<DfQmsIpqcWaigTotalCheck>> groupedData = sortedTotalShiftList.stream()
//                .collect(Collectors.groupingBy(
//                        item -> String.join("_", item.getfBigpro(), item.getfSeq()),
//                        LinkedHashMap::new,
//                        Collectors.toList()));
//
//        List<DfQmsIpqcWaigTotalVo> resultList = new ArrayList<>();
//        DfQmsIpqcWaigTotalCheckService service = BeanUtils.getBean(DfQmsIpqcWaigTotalCheckServiceImpl.class);
//
//        // 遍历每个分组（按项目_工序）
//        for (Map.Entry<String, List<DfQmsIpqcWaigTotalCheck>> entry : groupedData.entrySet()) {
//            String key = entry.getKey();
//            String[] keys = key.split("_");
//            String fBigpro = keys[0];
//            String fSeq = keys[1];
//
//            // 构造日期区间描述
//            String firstDate = lastNineDates.get(0);
//            String lastDate = lastNineDates.get(lastNineDates.size() - 1);
//            String formattedFirstDate = firstDate.replace("-", "/");
//            String formattedLastDate = lastDate.replace("-", "/");
//            String dataValue = formattedFirstDate + " - " + formattedLastDate;
//
//            // 查询整个时间段内所有可能的 fsort 值
//            List<Rate> list = service.WaiguanlistNgTop10("%%", fBigpro, "%%", fSeq, "%%", startDate, endDate, 0, 23);
//            Set<String> fsortSet = list.stream()
//                    .map(Rate::getName)
//                    .filter(name -> name != null && !name.isEmpty())
//                    .collect(Collectors.toSet());
//
//            // 遍历每个 fsort，生成一个VO对象
//            for (String s : fsortSet) {
//                DfQmsIpqcWaigTotalVo vo = new DfQmsIpqcWaigTotalVo();
//                vo.setData(dataValue);
//                vo.setfFac(factoryId);
//                vo.setfBigpro(fBigpro);
//                vo.setFseq(fSeq);
//                vo.setFsort(s);
//
//                // 存放每天各班的数据
//                List<DayData> morningList = new ArrayList<>();
//                List<DayData> nightList = new ArrayList<>();
//                List<DayData> totalList = new ArrayList<>();
//
//                // 对每一天进行并行查询
//                for (String lastNineDate : lastNineDates) {
//                    String dayStart = lastNineDate + " 07:00:00";
//                    String dayEnd   = lastNineDate + " 23:00:00";
//
//                    // 使用 CompletableFuture 并行查询
//                    CompletableFuture<List<Rate>> morningFuture = CompletableFuture.supplyAsync(() ->
//                            service.WaiguanlistNgTop10("%%", fBigpro, "%%", fSeq, s, dayStart, dayEnd, 0, 11)
//                    );
//                    CompletableFuture<List<Rate>> nightFuture = CompletableFuture.supplyAsync(() ->
//                            service.WaiguanlistNgTop10("%%", fBigpro, "%%", fSeq, s, dayStart, dayEnd, 12, 23)
//                    );
//                    CompletableFuture<List<Rate>> totalFuture = CompletableFuture.supplyAsync(() ->
//                            service.WaiguanlistNgTop10("%%", fBigpro, "%%", fSeq, s, dayStart, dayEnd, 0, 23)
//                    );
//
//                    // 等待所有查询完成
//                    CompletableFuture.allOf(morningFuture, nightFuture, totalFuture).join();
//                    List<Rate> morningRates = morningFuture.join();
//                    List<Rate> nightRates = nightFuture.join();
//                    List<Rate> totalRates = totalFuture.join();
//
//                    // 构建白班数据
//                    DayData morningData = new DayData();
//                    morningData.setDay(lastNineDate);
//                    if (morningRates != null && !morningRates.isEmpty()) {
//                        Rate r = morningRates.get(0);
//                        morningData.setSpotCheckCount(r.getAllNum());
//                        morningData.setNgNum(r.getNgNum());
//                        morningData.setNgl(Double.valueOf(String.format("%.2f", r.getRate() * 100)));
//                    } else {
//                        morningData.setSpotCheckCount(0);
//                        morningData.setNgNum(0);
//                        morningData.setNgl(0.0);
//                    }
//                    morningList.add(morningData);
//
//                    // 构建夜班数据
//                    DayData nightData = new DayData();
//                    nightData.setDay(lastNineDate);
//                    if (nightRates != null && !nightRates.isEmpty()) {
//                        Rate r = nightRates.get(0);
//                        nightData.setSpotCheckCount(r.getAllNum());
//                        nightData.setNgNum(r.getNgNum());
//                        nightData.setNgl(Double.valueOf(String.format("%.2f", r.getRate() * 100)));
//                    } else {
//                        nightData.setSpotCheckCount(0);
//                        nightData.setNgNum(0);
//                        nightData.setNgl(0.0);
//                    }
//                    nightList.add(nightData);
//
//                    // 构建合计数据
//                    DayData totalData = new DayData();
//                    totalData.setDay(lastNineDate);
//                    if (totalRates != null && !totalRates.isEmpty()) {
//                        Rate r = totalRates.get(0);
//                        totalData.setSpotCheckCount(r.getAllNum());
//                        totalData.setNgNum(r.getNgNum());
//                        totalData.setNgl(Double.valueOf(String.format("%.2f", r.getRate() * 100)));
//                    } else {
//                        totalData.setSpotCheckCount(0);
//                        totalData.setNgNum(0);
//                        totalData.setNgl(0.0);
//                    }
//                    totalList.add(totalData);
//                } // end for each day
//
//                // 封装班别数据到 NotProject 中
//                Banbie banbieMorning = new Banbie();
//                banbieMorning.setName("白班");
//                banbieMorning.setLable("NG率");
//                banbieMorning.setDayDatas(morningList);
//
//                Banbie banbieNight = new Banbie();
//                banbieNight.setName("夜班");
//                banbieNight.setLable("NG率");
//                banbieNight.setDayDatas(nightList);
//
//                Banbie banbieTotal = new Banbie();
//                banbieTotal.setName("合计");
//                banbieTotal.setLable("NG率");
//                banbieTotal.setDayDatas(totalList);
//
//                List<Banbie> banbieList = Arrays.asList(banbieMorning, banbieNight, banbieTotal);
//                NotProject notProject = new NotProject();
//                notProject.setName(s);
//                notProject.setBanbieList(banbieList);
//                vo.setNotProjectList(Collections.singletonList(notProject));
//
//                resultList.add(vo);
//            } // end for each fsort in fsortSet
//        } // end for each group
//
//        return resultList;
//    }


    public static List<LocalDate> getLastNineDays(String startDate, String endDate) {
        // 使用DateTimeFormatter解析输入的日期字符串，包括时间部分
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 将字符串日期转换为LocalDateTime
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        // 存储日期范围内最后九天的日期（只保留日期部分）
        List<LocalDate> lastNineDays = new ArrayList<>();

        // 获取结束日期的前9天
        for (int i = 0; i < 9; i++) {
            // 计算最后九天的日期
            LocalDateTime date = end.minusDays(i);
            // 如果该日期在startDate之后，则添加到结果列表
            if (!date.toLocalDate().isBefore(start.toLocalDate())) {
                lastNineDays.add(date.toLocalDate());  // 只取日期部分
            }
        }

        return lastNineDays;
    }

    private static int createBottomChar(List<DfQmsIpqcWaigTotalVo> bottomTables, SXSSFSheet sheet, int row, Integer endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;

        for (int i = 0; i < bottomTables.size(); i++) {
            DfQmsIpqcWaigTotalVo table1 = bottomTables.get(i);
            List<NotProject> notProjectList = table1.getNotProjectList();

            for (int i1 = 0; i1 < notProjectList.size(); i1++) {
                NotProject notProject = notProjectList.get(i1);

                // 检查是否所有数据都为0
                boolean allZero = true;
                for (int i2 = 0; i2 < notProject.getBanbieList().size(); i2++) {
                    for (int i3 = 0; i3 < notProject.getBanbieList().get(i2).getDayDatas().size(); i3++) {
                        if (notProject.getBanbieList().get(i2).getDayDatas().get(i3).getNgl() != 0) {
                            allZero = false;
                            break;
                        }
                    }
                    if (!allZero) break;
                }

                // 如果数据全是0，跳过该图表
                if (allZero) {
                    continue;
                }

                sheet.createDrawingPatriarch();
                XSSFDrawing drawing = sheet.getDrawingPatriarch();

                // 调整图表的位置，避免重叠
                int rowOffset = currentRow; // 图表的开始行
                int rowHeight = 15;  // 图表的行高，可以根据需要调整
                currentRow += rowHeight; // 下一次图表的位置

                // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
                XSSFChart chart = drawing.createChart(anchor);


                // 使用 StringBuilder 更高效地构建标题
                StringBuilder chartTitleBuilder = new StringBuilder();


                chartTitleBuilder
                        .append(table1.getfFac())
                        .append(" ")
                        .append(table1.getfBigpro())
                        .append(" ")
                        .append(table1.getFseq())
                        .append(" ")
                        .append(table1.getFsort())
                        .append("不良趋势");


                String chartTitle = chartTitleBuilder.toString();
                // 图例是否覆盖标题
                chart.setTitleOverlay(false);
                chart.setTitleText(chartTitle);
                XDDFChartLegend legend = chart.getOrAddLegend();
                // 图例位置:上下左右
                legend.setPosition(LegendPosition.TOP_RIGHT);

                // 创建x轴
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                // X轴标题
                bottomAxis.setTitle("");
                // 左侧标题
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle("不良");

                // 获取X轴数据
                String[] xTitleData = new String[]{"10.01", "10.02", "10.03", "10.04", "10.05", "10.06", "10.07"};
                for (int i2 = 0; i2 < notProject.getBanbieList().size(); i2++) {
                    xTitleData = notProject.getBanbieList().get(i2).getDayDatas().stream().map(item -> item.getDay()).toArray(String[]::new);
                    break;
                }
                XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                // 添加每个系列的数据
                for (int i2 = 0; i2 < notProject.getBanbieList().size(); i2++) {
                    Double[] xData1 = notProject.getBanbieList().get(i2).getDayDatas().stream().map(item -> item.getNgl()).toArray(Double[]::new);
                    XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                    XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                    // 图例标题
                    series.setTitle(notProject.getBanbieList().get(i2).getName(), null);
                    // 线条样式:true平滑曲线,false折线
                    series.setSmooth(false);
                    // 点的样式
                    series.setMarkerStyle(MarkerStyle.CIRCLE);
                }

                CTPlotArea plotArea = chart.getCTChart().getPlotArea();

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
                }

                chart.plot(data);
            }
        }

        return currentRow; // 返回当前行号（即图表生成后的总行数）
    }


    private static int createBottomChar4(List<DfQmsIpqcWaigTotalVo> bottomTables, SXSSFSheet sheet, int row, Integer endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;

        for (int i = 0; i < bottomTables.size(); i++) {
            DfQmsIpqcWaigTotalVo table1 = bottomTables.get(i);
            List<Banbie> bbList = table1.getBbList();

            for (int i1 = 0; i1 < bbList.size(); i1++) {
                Banbie banbie = bbList.get(i1);
                List<HzData> JTDatas = banbie.getJTDatas();


                // 检查是否所有数据都为0
                boolean allZero = banbie.getJTDatas().stream()
                        .allMatch(item -> item.getNgRate() == 0);

                // 如果数据全是0，跳过该图表
                if (allZero) {
                    continue;
                }

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
                if (!StringUtils.isEmpty(table1.getFseq())) {
                    chartTitleBuilder
                            .append(table1.getfFac())
                            .append(" ")
                            .append(table1.getfBigpro())
                            .append(" ")
                            .append(table1.getFseq())
                            .append("")
                            .append(banbie.getName())
                            .append("不良Top10");
                } else {
                    chartTitleBuilder
                            .append(table1.getfFac())
                            .append(" ")
                            .append(table1.getfBigpro())
                            .append(" ")
                            .append(table1.getFseq())
                            .append(" ")
                            .append(banbie.getName())
                            .append("不良Top10");
                }
                String chartTitle = chartTitleBuilder.toString();
                chart.setTitleText(chartTitle);
                // 图例是否覆盖标题
                chart.setTitleOverlay(false);
//                XDDFChartLegend legend = chart.getOrAddLegend();
//                // 图例位置:上下左右
//                legend.setPosition(LegendPosition.TOP_RIGHT);

                // 创建x轴
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                // X轴标题
                bottomAxis.setTitle("");
                // 左侧标题
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle("不良");
                // 获取X轴数据
                String[] xAxisData = banbie.getJTDatas().stream().map(item -> item.getfSort()).toArray(String[]::new);

                XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xAxisData);

                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                // 添加所有数据到一个系列中
                Double[] yAxisData = banbie.getJTDatas().stream().map(item -> item.getNgRate()).toArray(Double[]::new);
                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(yAxisData);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
                // 图例标题
                series.setTitle("不良率", null); // 设置图例标题
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);

                CTPlotArea plotArea = chart.getCTChart().getPlotArea();

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
                }

                chart.plot(data);
            }
        }

        return currentRow; // 返回当前行号（即图表生成后的总行数）
    }

    private static int createCenterChar(List<DfQmsIpqcWaigTotalVo> centerTables, SXSSFSheet sheet, Integer endcol) {

        // 为了避免覆盖，控制图表的行偏移
        int baseRow = 0; // 获取当前工作表已使用的行数

        // 用于存储每个图表的纵向偏移量，避免图表位置重叠
        int rowOffset = baseRow;

        for (int i = 0; i < centerTables.size(); i++) {
            DfQmsIpqcWaigTotalVo table1 = centerTables.get(i);
            List<Banbie> bbList = table1.getBbList();

            for (int i1 = 0; i1 < bbList.size(); i1++) {
                Banbie banbie = bbList.get(i1);

                // 检查是否所有数据都为0
                boolean allZero = banbie.getJTDatas().stream()
                        .allMatch(item -> item.getNgRate() == 0);

                // 如果数据全是0，跳过该图表
                if (allZero) {
                    continue;
                }

                // 使用 StringBuilder 更高效地构建标题
                StringBuilder chartTitleBuilder = new StringBuilder();

                // 拼接图表标题
                String type = "";
                if (StringUtils.isNotEmpty(table1.getFmac())) {
                    type = table1.getFmac();
                } else {
                    type = table1.getFseq();
                }

                if (!StringUtils.isEmpty(table1.getFseq())) {
                    chartTitleBuilder
                            .append(table1.getfFac())
                            .append(" ")
                            .append(table1.getfBigpro())
                            .append(" ")
                            .append(type)
                            .append(" ")
                            .append(banbie.getName())
                            .append("不良Top10");
                } else {
                    chartTitleBuilder
                            .append(table1.getfFac())
                            .append(" ")
                            .append(table1.getfBigpro())
                            .append(" ")
                            .append(type)
                            .append(" ")
                            .append(table1.getFseq())
                            .append(" ")
                            .append(banbie.getName())
                            .append("不良Top10");
                }

                // 最终得到拼接后的标题
                String chartTitle = chartTitleBuilder.toString();

                // 创建图表
                sheet.createDrawingPatriarch();
                XSSFDrawing drawing = sheet.getDrawingPatriarch();

                // 图表锚点：纵向偏移 + (i * 15) 或 (i1 * 15)，确保每个图表都有独立的区域
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + 12);

                XSSFChart chart = drawing.createChart(anchor);
                chart.setTitleText(chartTitle);
                chart.setTitleOverlay(false);
/*

                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP_RIGHT);
*/

                // X轴(分类轴)相关设置
                XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴
                xAxis.setTitle(""); // x轴标题

                String[] xAxisData = banbie.getJTDatas().stream().map(item -> item.getfSort()).toArray(String[]::new);
                XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

                // Y轴(值轴)相关设置
                XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴
                yAxis.setTitle(""); // Y轴标题
                yAxis.setCrossBetween(AxisCrossBetween.BETWEEN); // 设置柱的位置:BETWEEN居中
                Double[] yAxisData = banbie.getJTDatas().stream().map(item -> item.getNgRate()).toArray(Double[]::new);
                XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // Y轴数据

                // 创建柱状图对象
                XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
                barChart.setBarDirection(BarDirection.COL); // 设置柱状图的方向:COL竖向

                // 加载柱状图数据集
                XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) barChart.addSeries(xAxisSource, yAxisSource);
                barSeries.setTitle("", null); // 图例标题
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
                }

                // 绘制柱状图
                chart.plot(barChart);

                // 更新 rowOffset 以防下一个图表与当前图表重叠
                rowOffset += 15;  // 每个图表的纵向间隔可以调节为 15 或其他适当值
            }
        }
        return rowOffset;
    }


    private static Map<String, Integer> createBottomTable(XSSFCellStyle cellParamStyle4, SXSSFSheet sheet, int firstRow, int firstCol, List<DfQmsIpqcWaigTotalVo> datas) {
        Map<String, Integer> map = new HashMap<>();
        // 创建表头（移除"机台"后共6列）
        int totalRow = firstRow;
        for (DfQmsIpqcWaigTotalVo table : datas) {
            int startRow = totalRow + 1;

            Row headerRow = sheet.getRow(totalRow);
            if (headerRow == null) {
                headerRow = sheet.createRow(totalRow);
            }
            // 修改1: 移除headers中的"机台"
            String[] headers = {"日期", "厂别", "项目", "工序", "不良项目", "班别"};
            for (int i = 0; i < headers.length; i++) { // 现在循环6次
                Cell cell = headerRow.createCell(firstCol + i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellParamStyle4);
            }
            totalRow++;

            List<NotProject> notProjectList = table.getNotProjectList();
            for (int i1 = 0; i1 < notProjectList.size(); i1++) {
                Row row = sheet.getRow(totalRow + i1 * 1);
                if (row == null) {
                    row = sheet.createRow(totalRow + i1 * 1);
                }
                // 修改2: 列数减少到6列，调整后续列索引判断
                for (int i = 0; i < headers.length; i++) {
                    if (i + firstCol == firstCol) { // 日期列
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getData());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    if (i + firstCol == firstCol + 1) { // 厂别列
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfFac());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    if (i + firstCol == firstCol + 2) { // 项目列
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfBigpro());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    if (i + firstCol == firstCol + 3) { // 工序列
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getFseq());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    // 修改3: 跳过原"机台"列（原i=4），后续列索引-1
                    if (i + firstCol == firstCol + 4) { // 不良项目列（原i=5，现i=4）
                        NotProject notProject = notProjectList.get(i1);
                        List<Banbie> banbieList = notProject.getBanbieList();
                        if (i1 == 0) {
                            Cell cell = row.createCell(firstCol + i);
                            String fsort = notProjectList.get(i1).getName();
                            if (StringUtils.isEmpty(fsort) || "null".equals(fsort)) {
                                fsort = " ";
                            }
                            cell.setCellValue(fsort);
                            cell.setCellStyle(cellParamStyle4);

                            for (int i2 = 0; i2 < banbieList.size(); i2++) {
                                Row row1 = sheet.getRow(row.getRowNum() + i2);
                                if (row1 == null) {
                                    row1 = sheet.createRow(row.getRowNum() + i2);
                                }
                                Cell cell1 = row1.createCell(cell.getColumnIndex() + 1); // 班别列
                                cell1.setCellValue(banbieList.get(i2).getName());
                                cell1.setCellStyle(cellParamStyle4);

                                Cell cell3 = headerRow.createCell(cell1.getColumnIndex() + 1);
                                cell3.setCellValue("日期");
                                cell3.setCellStyle(cellParamStyle4);

                                if (!banbieList.get(i2).getName().equals("合计")) {
                                    Cell cell2 = row1.createCell(cell1.getColumnIndex() + 1);
                                    cell2.setCellValue(banbieList.get(i2).getLable());
                                    cell2.setCellStyle(cellParamStyle4);
                                }

                                totalRow = row1.getRowNum() + 1;
                                // 设置日期数据
                                Banbie banbie = banbieList.get(i2);
                                List<DayData> dayDatas = banbie.getDayDatas();
                                for (int i3 = 0; i3 < dayDatas.size(); i3++) {
                                    Cell dayCell = headerRow.createCell(cell3.getColumnIndex() + i3 + 1);
                                    dayCell.setCellValue(dayDatas.get(i3).getDay());
                                    dayCell.setCellStyle(cellParamStyle4);

                                    Cell cell4 = row1.createCell(cell3.getColumnIndex() + i3 + 1);
                                    cell4.setCellValue(String.format("%.2f%%", dayDatas.get(i3).getNgl()));
                                    cell4.setCellStyle(cellParamStyle4);
                                }
                            }
                        } else {
                            // ...类似调整后续列索引...
                        }
                    }
                }
            }
            // 合并单元格调整
            for (int i2 = 0; i2 < notProjectList.size(); i2++) {
                for (int i = 0; i < headers.length + 1; i++) { // headers.length现在为6
                    if (i == 4) { // 修改4: 原i==5调整为i==4（不良项目列）
                        sheet.addMergedRegion(new CellRangeAddress(
                                startRow + (i2 * 3),
                                startRow + 2 + (i2 * 3),
                                firstCol + i,
                                firstCol + i
                        ));
                        sheet.addMergedRegion(new CellRangeAddress(
                                startRow + 2 + (i2 * 3),
                                startRow + 2 + (i2 * 3),
                                firstCol + i + 1,
                                firstCol + i + 2
                        ));
                    }
                    // 修改5: 合并范围调整（原i==0-4改为0-3）
                    if (i == 0 || i == 1 || i == 2 || i == 3) {
                        int headerStartRow = startRow + (i2 * 3);
                        int endIndex = headerStartRow + 2;
                        sheet.addMergedRegion(new CellRangeAddress(
                                headerStartRow,
                                endIndex,
                                firstCol + i,
                                firstCol + i
                        ));
                    }
                }
            }
        }
        map.put("row", totalRow);
        return map;
    }



/*private static void createCenterChar(List<DfQmsIpqcWaigTotalVo> centerTables, Sheet sheet, Integer endcol) {
    // 为了避免覆盖，控制图表的行偏移
    int baseRow = sheet.getPhysicalNumberOfRows(); // 获取当前工作表已使用的行数

    // 用于存储每个图表的纵向偏移量，避免图表位置重叠
    int rowOffset = baseRow;

    for (int i = 0; i < centerTables.size(); i++) {
        DfQmsIpqcWaigTotalVo table1 = centerTables.get(i);
        List<Banbie> bbList = table1.getBbList();

        for (int i1 = 0; i1 < bbList.size(); i1++) {
            Banbie banbie = bbList.get(i1);

            // 组合图表标题
            String chartTitle = String.format("%s%s%s%s不良机台Top10_%s(比例%)%s",
                    table1.getData(),
                    table1.getfFac(),
                    table1.getFseq(),
                    banbie.getName(),
                    i);

            // 创建图表
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            // 图表锚点：纵向偏移 + (i * 15) 或 (i1 * 15)，确保每个图表都有独立的区域
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + 12);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText(chartTitle);
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            // X轴(分类轴)相关设置
            XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴
            xAxis.setTitle(""); // x轴标题

            String[] xAxisData = banbie.getJTDatas().stream().map(item -> item.getfSort()).toArray(String[]::new);
            XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

            // Y轴(值轴)相关设置
            XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴
            yAxis.setTitle(""); // Y轴标题
            yAxis.setCrossBetween(AxisCrossBetween.BETWEEN); // 设置柱的位置:BETWEEN居中

            // 将 Y 轴数据格式化为百分比并保留两位小数
            Double[] yAxisData = banbie.getJTDatas().stream()
                    .map(item -> item.getNgRate())  // 乘以100以转化为百分比
                    .map(rate -> Math.round(rate))  // 保留两位小数
                    .toArray(Double[]::new);

            XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // Y轴数据

            // 设置 Y 轴的数字格式为百分比
            yAxis.setNumberFormat("0.00%"); // 格式化为百分比，保留两位小数

            // 创建柱状图对象
            XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
            barChart.setBarDirection(BarDirection.COL); // 设置柱状图的方向:COL竖向

            // 加载柱状图数据集
            XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) barChart.addSeries(xAxisSource, yAxisSource);
            barSeries.setTitle("", null); // 图例标题

            // 绘制柱状图
            chart.plot(barChart);

            // 更新 rowOffset 以防下一个图表与当前图表重叠
            rowOffset += 15;  // 每个图表的纵向间隔可以调节为 15 或其他适当值
        }
    }
}*/


    private static Map<String, Integer> createSheet3Table(XSSFCellStyle cellParamStyle4, SXSSFSheet sheet, int firstRow, int firstCol, List<
            DfQmsIpqcWaigTotalVo> datas) {

        HashMap<String, Integer> map = new HashMap<>();
        // 创建表头
        // 创建表头
        int totalRow = firstRow;
        for (DfQmsIpqcWaigTotalVo table : datas) {
            int startRow = totalRow + 1;

            Row headerRow = sheet.getRow(totalRow);
            if (headerRow == null) {
                headerRow = sheet.createRow(totalRow);
            }
            String[] headers = {"日期", "厂别", "项目", "工序", "班别"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(firstCol + i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellParamStyle4);
            }
            totalRow++;
            List<Banbie> bbList = table.getBbList();
            Row row = sheet.createRow(totalRow);
            if (row == null) {
                row = sheet.createRow(totalRow);
            }
            totalRow++;

            for (Banbie bb : bbList) {
                for (int i = 0; i < headers.length; i++) {
                    if (i + firstCol == firstCol) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getData());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    if (i + firstCol == firstCol + 1) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfFac());
                        cell.setCellStyle(cellParamStyle4);


                    }
                    if (i + firstCol == firstCol + 2) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfBigpro());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 3) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getFseq());
                        cell.setCellStyle(cellParamStyle4);

                    }

//                    if (i + firstCol == firstCol + 4) {
//                        Cell cell = row.createCell(firstCol + i);
//                        cell.setCellValue(table.getFmac());
//                    }
                    if (i + firstCol == firstCol + 4) {
                        int colIndex = i + firstCol + 1;
                        Row bbRow = null;
                        if (bb.getName().equals("汇总")) {
                            bbRow = row;
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 2;
                            for (int j = 0; j < 4; j++) {
                                if (j == 0) {
                                    Cell cell2 = headerRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = headerRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Cell cell1 = bbRow.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);


                                    }
                                }
                            }
                            map.put("col", colIndex + bb.getJTDatas().size());

                        }
                        if (bb.getName().equals("白班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);

                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                        if (bb.getName().equals("夜班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 合并表格

            for (int i = 0; i < headers.length; i++) {
                if (i != 4) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, firstCol + i, firstCol + i));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 9, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 8, totalRow - 5, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 4, totalRow - 1, firstCol + i, firstCol + i));
                }
            }
        }
        map.put("row", totalRow);
        return map;

    }


    private static Map<String, Integer> createCenterTable(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<
            DfQmsIpqcWaigTotalVo> datas) {
        HashMap<String, Integer> map = new HashMap<>();

        // 创建表头
        // 创建表头
        int totalRow = firstRow;
        for (DfQmsIpqcWaigTotalVo table : datas) {
            int startRow = totalRow + 1;

            Row headerRow = sheet.getRow(totalRow);
            if (headerRow == null) {
                headerRow = sheet.createRow(totalRow);
            }
            String[] headers = {"日期", "厂别", "项目", "工序", "班别"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(firstCol + i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellParamStyle4);
            }
            totalRow++;
            List<Banbie> bbList = table.getBbList();
            Row row = sheet.getRow(totalRow);
            if (row == null) {
                row = sheet.createRow(totalRow);
            }
            totalRow++;

            for (Banbie bb : bbList) {
                for (int i = 0; i < headers.length; i++) {
                    if (i + firstCol == firstCol) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getData());
                        cell.setCellStyle(cellParamStyle4);
                    }
                    if (i + firstCol == firstCol + 1) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfFac());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 2) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfBigpro());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 3) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getFseq());
                        cell.setCellStyle(cellParamStyle4);

                    }
/*                    if (i + firstCol  == firstCol + 4) {
                        Cell cell = row.createCell(firstCol  + i);
                        cell.setCellValue(table.getFmac());
                        cell.setCellStyle(cellParamStyle4);

                    }*/
                    if (i + firstCol == firstCol + 4) {
                        int colIndex = i + firstCol + 1;
                        Row bbRow = null;
                        if (bb.getName().equals("汇总")) {
                            bbRow = row;
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 2;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = headerRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = headerRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Cell cell1 = bbRow.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                            map.put("col", colIndex + bb.getJTDatas().size());

                        }
                        if (bb.getName().equals("白班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);

                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);

                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }

                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }


                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                        if (bb.getName().equals("夜班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfSort());
                                        cell_i.setCellStyle(cellParamStyle4);
                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);

                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }

                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }

                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);

                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }

                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 合并表格

            for (int i = 0; i < headers.length; i++) {
                if (i != 4) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, firstCol + i, firstCol + i));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 9, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 8, totalRow - 5, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 4, totalRow - 1, firstCol + i, firstCol + i));
                }
            }
        }
        map.put("row", totalRow);
        return map;

    }

    /**
     * 左侧表格数据柱状图
     *
     * @param leftTables
     * @param sheet
     * @param col
     */
    private static void createLeftChar(List<DfQmsIpqcWaigTotalVo> leftTables, Sheet sheet, int col) {
        // 用来跟踪当前可用的起始行
        int currentRow = sheet.getLastRowNum() + 2; // 从当前工作表的最后一行的下方开始放置图表

        for (int i = 0; i < leftTables.size(); i++) {
            DfQmsIpqcWaigTotalVo table1 = leftTables.get(i);
            List<Banbie> bbList = table1.getBbList();
            for (int i1 = 0; i1 < bbList.size(); i1++) {
                Banbie banbie = bbList.get(i1);

                // 组合图表标题
                String chartTitle = String.format("%s%s%s%s不良机台Top5_%s",
                        table1.getfFac(),
                        table1.getfBigpro(),
                        table1.getFseq(),
                        banbie.getName(),
                        i);

                // 创建图表
                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

                // 更新锚点的位置，保证每个图表放置在不同的行
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                        0, currentRow, col, currentRow + 10); // 各个图表的高度可以调整

                XSSFChart chart = drawing.createChart(anchor);
                chart.setTitleText(chartTitle);
                chart.setTitleOverlay(false);

                // 图例
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP_RIGHT);

                // X轴 (分类轴) 相关设置
                XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                xAxis.setTitle(""); // x轴标题

                // 获取班别数据的机台号
                String[] xAxisData = (banbie.getJTDatas() != null ?
                        banbie.getJTDatas().stream()
                                .map(jtData -> jtData.getfFmac())
                                .toArray(String[]::new)
                        : new String[0]);

                XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

                // Y轴 (值轴) 相关设置
                XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
                yAxis.setTitle("百分比（%）");
                yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
                yAxis.setNumberFormat("0.00%");

                // 获取班别数据的NG不良率
                Double[] yAxisData = (banbie.getJTDatas() != null ?
                        banbie.getJTDatas().stream()
                                .map(jtData -> jtData.getNgRate())
                                .toArray(Double[]::new)
                        : new Double[0]);

                XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData);

                // 创建柱状图对象
                XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
                barChart.setBarDirection(BarDirection.COL);

                // 加载柱状图数据集
                XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) barChart.addSeries(xAxisSource, yAxisSource);
                barSeries.setTitle("不良率", null);

                // 绘制柱状图
                chart.plot(barChart);

                // 更新当前行，确保下一个图表放在一个新的位置
                currentRow += 12; // 每个图表占12行高度（可以根据实际需要调整）
            }
        }
    }


    // 创建空行
    private void createEmptyRows(SXSSFSheet sheet, int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            sheet.createRow(i);
        }
    }

    // 创建标题行
    private void createTitleRow(SXSSFSheet sheet, SXSSFRow row, XSSFCellStyle style) {
        String title = "明细数据";
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, heardKey.length - 1)); // 合并标题单元格
    }

    // 设置表头
    private void createTableHeader(SXSSFSheet sheet, XSSFCellStyle style) {
        SXSSFRow headerRow = sheet.createRow(1);
        for (int j = 0; j < heardList.length; j++) {
            SXSSFCell cell = headerRow.createCell(j);
            cell.setCellValue(heardList[j]);
            cell.setCellStyle(style);
            sheet.addMergedRegion(new CellRangeAddress(1, 3, j, j)); // 合并表头
        }
    }

    // 填充Map数据
    private void fillDataWithMap(SXSSFSheet
                                         sheet, Map<String, List<DfQmsIpqcFlawConfig>> listMap, XSSFCellStyle style) {
        int startIndex = heardList.length;
        for (Map.Entry<String, List<DfQmsIpqcFlawConfig>> entry : listMap.entrySet()) {
            List<DfQmsIpqcFlawConfig> value = entry.getValue();
            SXSSFRow rowOne = sheet.getRow(1);
            SXSSFCell cellHead = rowOne.createCell(startIndex);
            cellHead.setCellValue(entry.getKey());
            cellHead.setCellStyle(style);

            int endIndex = startIndex + value.size() * 2;
            sheet.addMergedRegion(new CellRangeAddress(1, 1, startIndex, endIndex - 1));

            int startSecondIndex = startIndex;
            for (int j = 0; j < value.size(); j++) {
                SXSSFRow rowTwo = sheet.getRow(2);
                String secondName = value.get(j).getFlawName();
                SXSSFCell cellName = rowTwo.createCell(startSecondIndex);
                cellName.setCellValue(secondName);
                cellName.setCellStyle(style);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, startSecondIndex, startSecondIndex + 1));

                SXSSFRow rowThird = sheet.getRow(3);
                SXSSFCell cellCount = rowThird.createCell(startSecondIndex);
                cellCount.setCellValue("数量");
                cellCount.setCellStyle(style);
                SXSSFCell cellRatio = rowThird.createCell(startSecondIndex + 1);
                cellRatio.setCellValue("占比");
                cellRatio.setCellStyle(style);

                startSecondIndex += 2;
            }
            startIndex = endIndex;
        }
    }


    // 写入数据行
    private void writeRowData(SXSSFSheet sheet, int rowIndex, Map<String, Object> map, XSSFCellStyle style) {
        SXSSFRow row = sheet.createRow(rowIndex);
        for (int j = 0; j < heardKey.length; j++) {
            SXSSFCell cell = row.createCell(j);
            cell.setCellStyle(style);

            Object valueObject = map.get(heardKey[j]);
            String value = (valueObject == null) ? "" : valueObject.toString();

            if (valueObject instanceof String) {
                value = (String) valueObject;
            } else if (valueObject instanceof Integer) {
                value = String.valueOf(((Integer) valueObject).floatValue());
            } else if (valueObject instanceof BigDecimal) {
                value = String.valueOf(((BigDecimal) valueObject).floatValue());
            }

            if (heardKey[j].equals("ddNum") || heardKey[j].equals("sjNum")) {
                if ((Long) map.get("ddNum") != null) {
                    if ((Long) map.get("sjNum") == null) {
                        cell.setCellStyle(style);
                    } else if ((Long) map.get("ddNum") != (Long) map.get("sjNum")) {
                        if ((Long) map.get("ddNum") > (Long) map.get("sjNum")) {
                            cell.setCellStyle(style);
                        } else {
                            cell.setCellStyle(style);
                        }
                    }
                }
            }
            cell.setCellValue(Strings.isEmpty(value) ? "" : value);
        }
    }


    public byte[] exportExportWithFileName(HttpServletRequest request, HttpServletResponse response, String
            fileName) throws IOException {
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);
        //设置自动换行
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);

        // 标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont fontStyle = wb.createFont();
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
        //设置单元格样式
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        //在第1行创建rows
        HSSFRow row = wbSheet.createRow((int) 0);
        //设置列头元素
        HSSFCell cellHead = null;
        for (int i = 0; i < heardList.length; i++) {
            cellHead = row.createCell(i);
            cellHead.setCellValue(heardList[i]);
            cellHead.setCellStyle(style);
        }

        //设置每格数据的样式 （字体红色）
        HSSFCellStyle cellParamStyle = wb.createCellStyle();
        HSSFFont ParamFontStyle = wb.createFont();
//        cellParamStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        ParamFontStyle.setColor(HSSFColor.DARK_RED.index);   //设置字体颜色 (红色)
        ParamFontStyle.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle.setFont(ParamFontStyle);
        //设置每格数据的样式2（字体蓝色）
        HSSFCellStyle cellParamStyle2 = wb.createCellStyle();
//        cellParamStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        cellParamStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont ParamFontStyle2 = wb.createFont();
//        ParamFontStyle2.setColor(HSSFColor.BLUE.index);   //设置字体颜色 (蓝色)
        ParamFontStyle2.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle2.setFont(ParamFontStyle2);
        //开始写入实体数据信息
        int a = 1;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow roww = wbSheet.createRow((int) a);
            Map map = data.get(i);
            HSSFCell cell = null;
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
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;Filename=" + encodedFileName + System.currentTimeMillis() + ".xls");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.close();
            return wb.getBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("导出Excel出现严重异常，异常信息：" + ex.getMessage());
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


    /**
     * 左侧表格
     *
     * @param sheet
     * @param firstRow
     * @param firstCol
     * @param datas
     */
    /*    */

    /**
     * 左侧表格
     *
     * @param sheet
     * @param firstRow
     * @param firstCol
     * @param datas
     *//*
    private static Map<String, Integer> createLeftTable(Sheet sheet, int firstRow, int firstCol, List<
            DfQmsIpqcWaigTotalVo> datas) {
        HashMap<String, Integer> map = new HashMap<>();
        // 创建表头
        // 创建表头
        int totalRow = firstRow;
        for (DfQmsIpqcWaigTotalVo table : datas) {
            int startRow = totalRow + 1;
            Row headerRow = sheet.createRow(totalRow);

            String[] headers = {"日期", "厂别", "项目", "工序", "班别"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(firstCol + i);
                cell.setCellValue(headers[i]);
            }
            totalRow++;
            List<Banbie> bbList = table.getBbList();
            Row row = sheet.createRow(totalRow);
            totalRow++;

            for (Banbie bb : bbList) {
                for (int i = 0; i < headers.length; i++) {
                    if (i == 0) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getData());
                    }
                    if (i == 1) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfFac());
                    }
                    if (i == 2) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfBigpro());
                    }
                    if (i == 3) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getFseq());
                    }
                    if (i == 4) {
                        int colIndex = 4 + 1;
                        Row bbRow = null;
                        if (bb.getName().equals("汇总")) {
                            bbRow = row;
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            totalRow = totalRow + 2;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = headerRow.createCell(colIndex);
                                    cell2.setCellValue("机台号");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = headerRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                    }
                                }
                                if (j == 1) {
                                    Cell cell1 = bbRow.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%",jtDatas.get(ji).getNgRate()));

                                    }
                                }

                                map.put("col", colIndex + bb.getJTDatas().size());
                            }


                        }
                        if (bb.getName().equals("白班")) {
                            bbRow = sheet.createRow(totalRow);
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("机台号");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%",jtDatas.get(ji).getNgRate()));
                                    }
                                }
                            }
                        }
                        if ("夜班".equals(bb.getName())) { //
                            bbRow = sheet.createRow(totalRow);
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("机台号");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%",jtDatas.get(ji).getNgRate()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 合并表格

            for (int i = 0; i < headers.length; i++) {
                if (i != 4) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, i, i));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 9, i, i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 8, totalRow - 5, i, i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 4, totalRow - 1, i, i));
                }
            }
        }
        map.put("row", totalRow);
        return map;
    }*/
    private static Map<String, Integer> createLeftTable(XSSFCellStyle cellParamStyle4, Sheet sheet, int firstRow, int firstCol, List<
            DfQmsIpqcWaigTotalVo> datas) {
        HashMap<String, Integer> map = new HashMap<>();
        // 创建表头
        // 创建表头
        int totalRow = firstRow;
        for (DfQmsIpqcWaigTotalVo table : datas) {
            int startRow = totalRow + 1;

            Row headerRow = sheet.getRow(totalRow);
            if (headerRow == null) {
                headerRow = sheet.createRow(totalRow);
            }
            String[] headers = {"日期", "厂别", "项目", "工序", "班别"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(firstCol + i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellParamStyle4);
            }
            totalRow++;
            List<Banbie> bbList = table.getBbList();
            Row row = sheet.createRow(totalRow);
            if (row == null) {
                row = sheet.createRow(totalRow);
            }
            totalRow++;

            for (Banbie bb : bbList) {
                for (int i = 0; i < headers.length; i++) {
                    if (i + firstCol == firstCol) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getData());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 1) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfFac());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 2) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getfBigpro());
                        cell.setCellStyle(cellParamStyle4);

                    }
                    if (i + firstCol == firstCol + 3) {
                        Cell cell = row.createCell(firstCol + i);
                        cell.setCellValue(table.getFseq());
                        cell.setCellStyle(cellParamStyle4);

                    }
//                    if (i + firstCol == firstCol + 4) {
//                        Cell cell = row.createCell(firstCol + i);
//                        cell.setCellValue(table.getFmac());
//                    }
                    if (i + firstCol == firstCol + 4) {
                        int colIndex = i + firstCol + 1;
                        Row bbRow = null;
                        if (bb.getName().equals("汇总")) {
                            bbRow = row;
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);

                            totalRow = totalRow + 2;
                            for (int j = 0; j < 4; j++) {
                                if (j == 0) {
                                    Cell cell2 = headerRow.createCell(colIndex);
                                    cell2.setCellValue("机台号");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = headerRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Cell cell1 = bbRow.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);


                                    }
                                }
                            }
                            map.put("col", colIndex + bb.getJTDatas().size());

                        }
                        if (bb.getName().equals("白班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("机台号");
                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                        if (bb.getName().equals("夜班")) {
                            bbRow = sheet.getRow(totalRow);
                            if (bbRow == null) {
                                bbRow = sheet.createRow(totalRow);
                            }
                            Cell cell = bbRow.createCell(firstCol + i);
                            cell.setCellValue(bb.getName());
                            cell.setCellStyle(cellParamStyle4);
                            totalRow = totalRow + 4;
                            for (int j = 0; j < 4; j++) {

                                if (j == 0) {
                                    Cell cell2 = bbRow.createCell(colIndex);
                                    cell2.setCellValue("不良项目");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = bbRow.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getfFmac());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 1) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 1);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 1);
                                    }
                                    Cell cell1 = row1.createCell(colIndex);
                                    cell1.setCellValue("抽检数");
                                    cell1.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getSpotCheckCount());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 2) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 2);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 2);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良数");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(jtDatas.get(ji).getNgNum());
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                                if (j == 3) {
                                    Row row1 = sheet.getRow(bbRow.getRowNum() + 3);
                                    if (row1 == null) {
                                        row1 = sheet.createRow(bbRow.getRowNum() + 3);
                                    }
                                    Cell cell2 = row1.createCell(colIndex);
                                    cell2.setCellValue("NG不良率");
                                    cell2.setCellStyle(cellParamStyle4);

                                    List<HzData> jtDatas = bb.getJTDatas();
                                    for (int ji = 0; ji < jtDatas.size(); ji++) {
                                        Cell cell_i = row1.createCell(colIndex + ji + 1);
                                        cell_i.setCellValue(String.format("%.2f%%", jtDatas.get(ji).getNgRate()));
                                        cell_i.setCellStyle(cellParamStyle4);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 合并表格

            for (int i = 0; i < headers.length; i++) {
                if (i != 4) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 1, firstCol + i, firstCol + i));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, totalRow - 9, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 8, totalRow - 5, firstCol + i, firstCol + i));
                    sheet.addMergedRegion(new CellRangeAddress(totalRow - 4, totalRow - 1, firstCol + i, firstCol + i));
                }
            }
        }
        map.put("row", totalRow);
        return map;

    }


    private static String extracted(String stime) {
        // 根据日期判断班次
        String classes = ""; // 存储班次信息
        LocalTime morningShiftStart = LocalTime.of(7, 0); // 白班开始时间
        LocalTime morningShiftEnd = LocalTime.of(19, 0); // 白班结束时间
        try {
            Timestamp time = Timestamp.valueOf(stime); // 转换为时间戳
            LocalTime localTime = time.toLocalDateTime().toLocalTime(); // 转换为本地时间


            // 判断班次
            if (localTime.isAfter(morningShiftStart) && localTime.isBefore(morningShiftEnd)) {
                classes = "白班"; // 白班时间
            } else {
                classes = "夜班"; // 晚班时间
            }
        } catch (IllegalArgumentException e) {
            // 处理时间解析异常
            classes = "未知班次";
        }
        return classes;
    }

    static List<Double> ZTlist = new ArrayList<>();
    static List<Double> BBlist = new ArrayList<>();
    static List<Double> WBlist = new ArrayList<>();

    public static void calculateShiftYieldRates(List<Object> whiteSpotCheckCount,
                                                List<Object> nightSpotCheckCount,
                                                List<Object> whiteOkNum,
                                                List<Object> nightOkNum,
                                                List<Object> whiteShiftYieldRate,
                                                List<Object> nightShiftYieldRate,
                                                List<Object> whiteShiftYieldRate1,
                                                List<Object> nightShiftYieldRate1,
                                                List<Object> totalShiftYieldRate,
                                                List<Object> totalShiftYieldRate1,
                                                List<Object> totalDirectRate,
                                                List<Object> totalDirectRate1,
                                                List<Object> whiteDirectRate,
                                                List<Object> whiteDirectRate1,
                                                List<Object> nightDirectRate,
                                                List<Object> nightDirectRate1) {

        Double ZT = 0.0;
        Double BB = 0.0;
        Double WB = 0.0;


        for (int i = nightSpotCheckCount.size() - 1; i < nightSpotCheckCount.size(); i++) {
            // 计算当前的总量和良率
            Double whiteSpot = Double.parseDouble(String.valueOf(whiteSpotCheckCount.get(i)));
            Double nightSpot = Double.parseDouble(String.valueOf(nightSpotCheckCount.get(i)));
            Double whiteOk = Double.parseDouble(String.valueOf(whiteOkNum.get(i)));
            Double nightOk = Double.parseDouble(String.valueOf(nightOkNum.get(i)));

            // 计算总量和良率
            Double total = whiteSpot + nightSpot;
            Double totallv = whiteOk + nightOk;

            // 如果总量为0，直接跳过当前循环，将直通率设置为100
//            if (total == 0) {
//                totalShiftYieldRate.add(1);
//                totalShiftYieldRate1.add("100%");
//                totalDirectRate.add(1);
//                totalDirectRate1.add("100%");
//                whiteDirectRate.add(1);
//                whiteDirectRate1.add("100%");
//                nightDirectRate.add(1);
//                nightDirectRate1.add("100%");
//                ZTlist.add(1.0);
//                BBlist.add(1.0);
//                WBlist.add(1.0);
//                continue;
//            }
            Double ShiftYieldRate = 0.0;
            if (total == 0 || totallv == 0) {
                ShiftYieldRate = 1.0;
            } else {
                ShiftYieldRate = totallv / total;
            }

            // 处理首次（i == 0）和后续（i > 0）的情况
            if (i == 0) {
                ZT = ShiftYieldRate;
                String brate1 = String.valueOf(whiteShiftYieldRate1.get(i));
                String wrate1 = String.valueOf(nightShiftYieldRate1.get(i));
                double brate = Double.parseDouble(String.valueOf(whiteShiftYieldRate.get(i + 1)));
                double wrate = Double.parseDouble(String.valueOf(nightShiftYieldRate.get(i + 1)));
                BB = brate;
                WB = wrate;
                whiteDirectRate1.add(brate1);
                whiteDirectRate.add(brate);
                nightDirectRate1.add(wrate1);
                nightDirectRate.add(wrate);
                totalDirectRate.add(ShiftYieldRate);
                totalDirectRate1.add(java.lang.String.format("%.2f%%", ShiftYieldRate * 100));


            } else {
                // 后续元素，直通率等于当前 ShiftYieldRate * 上次的 ZT
                if (ZTlist.size() > 0) {
                    Double newDirectRate = ShiftYieldRate * (ZTlist.get(ZTlist.size() - 1));
                    totalDirectRate1.add(java.lang.String.format("%.2f%%", newDirectRate * 100)); // 更新直通率
                    totalDirectRate.add(newDirectRate); // 更新直通率
                    ZT = newDirectRate; // 更新 ZT 为当前直通率
                }
                if (BBlist.size() > 0) {
                    double num = Double.parseDouble(String.valueOf(whiteShiftYieldRate.get(i + 1)));
                    if (num == 0) {
                        num = 1;
                    }
                    Double BbDirectRate = num * (BBlist.get(BBlist.size() - 1));
                    whiteDirectRate1.add(java.lang.String.format("%.2f%%", BbDirectRate * 100));
                    whiteDirectRate.add(BbDirectRate);
                    BB = BbDirectRate;
                }
                if (WBlist.size() > 0) {
                    double num = Double.parseDouble(String.valueOf(nightShiftYieldRate.get(i + 1)));
                    if (num == 0) {
                        num = 1;
                    }
                    Double WbDirectRate = num * (WBlist.get(WBlist.size() - 1));
                    nightDirectRate1.add(java.lang.String.format("%.2f%%", WbDirectRate * 100));
                    nightDirectRate.add(WbDirectRate);
                    WB = WbDirectRate;
                }
            }

            // 更新各个列表
            ZTlist.add(ZT);
            BBlist.add(BB);
            WBlist.add(WB);
            totalShiftYieldRate.add(ShiftYieldRate);
            totalShiftYieldRate1.add(java.lang.String.format("%.2f%%", ShiftYieldRate * 100));
        }
    }

    public XSSFCellStyle createTitleCellStyle(SXSSFWorkbook wb) {
        XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
        XSSFFont titleFont = (XSSFFont) wb.createFont();
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

    public XSSFCellStyle createTitleCellStyle5(SXSSFWorkbook wb) {
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

    public XSSFCellStyle createTableHeaderStyle(XSSFWorkbook wb) {
        XSSFCellStyle headerStyle = wb.createCellStyle();
        XSSFFont headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);  // 字体加粗
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());  // 背景色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  // 填充背景色
        headerStyle.setBorderTop(BorderStyle.THIN);  // 上边框
        headerStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
        headerStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
        headerStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        return headerStyle;
    }

    public XSSFCellStyle createNumberCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle numberStyle = wb.createCellStyle();
        XSSFFont numberFont = wb.createFont();
        numberFont.setFontHeightInPoints((short) 12);  // 设置字体大小
        numberStyle.setFont(numberFont);
        numberStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        numberStyle.setBorderTop(BorderStyle.THIN);  // 上边框
        numberStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
        numberStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
        numberStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        // 设置数字格式为百分比
        numberStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
        return numberStyle;
    }

    public XSSFCellStyle createDateCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle dateStyle = wb.createCellStyle();
        XSSFFont dateFont = wb.createFont();
        dateFont.setFontHeightInPoints((short) 12);  // 设置字体大小
        dateStyle.setFont(dateFont);
        dateStyle.setAlignment(HorizontalAlignment.CENTER);  // 水平居中
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        dateStyle.setBorderTop(BorderStyle.THIN);  // 上边框
        dateStyle.setBorderBottom(BorderStyle.THIN);  // 下边框
        dateStyle.setBorderLeft(BorderStyle.THIN);  // 左边框
        dateStyle.setBorderRight(BorderStyle.THIN);  // 右边框
        // 设置日期格式
        dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd"));
        return dateStyle;
    }

    // 从时间字段提取小时（示例实现）
    private int extractHourFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    // 计算良率（处理除零异常）
    private double calculateYield(int spotCheck, int okNum) {
        return spotCheck > 0 ? (double) okNum / spotCheck : 1.0;
    }

    // 格式化百分比
    private String formatPercent(double rate) {
        return rate == 1.0 ? "100%" : String.format("%.2f%%", rate * 100);
    }


}
