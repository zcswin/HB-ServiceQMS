package com.ww.boengongye.utils;

import com.ww.boengongye.entity.Rate;
import com.ww.boengongye.entity.exportExcelUpdate.ChartEntity;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xddf.usermodel.LineCap;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BigScreenCheckExcelExporter {
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
    private String sheetName = "sheet1";

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
    public byte[] exportExport(HttpServletRequest request, HttpServletResponse response, List<Rate> rates, List<Object> result,
                               List<Rate> Rate1, List<Rate> Rate3, Map<String, Object> resultSheet3, Map<String, List<Object>> resultSheet4, Map<String, List<Object>> resultSheet5) throws IOException {

        // 创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook(); // 使用 XSSFWorkbook

        //sheet1
        XSSFSheet sheet1 = wb.createSheet("各工序TOP10缺陷占比");
        createSheet1Char(Rate1, sheet1, 0);

        //sheet2
        XSSFSheet sheet2 = wb.createSheet("折线图");
        createSheet2Char(result, sheet2, 0, 0);

        //sheet3
        XSSFSheet sheet3 = wb.createSheet("各工序良率");
        createSheet3Char(resultSheet3, sheet3, 0, 0);

        //sheet4
        XSSFSheet sheet4 = wb.createSheet("各工序抽检直通趋势对比");
        createSheet4Char(resultSheet4, sheet4, 0, 0);

        //sheet5

        List<Object> namelist = (List<Object>) resultSheet5.get("fseq");
        if (namelist != null) {
            String fseq = String.valueOf(namelist.get(0));
            if (fseq != null && !"null".equals(fseq)) {
                XSSFSheet sheet5 = wb.createSheet(fseq+"良率");
                createSheet5Char(resultSheet5, sheet5, 0, 0,fseq);
            }

        }


        //导出数据
        try {
            // 设置Http响应头告诉浏览器下载这个附件
            response.setHeader("Content-Disposition", "attachment;Filename=" + System.currentTimeMillis() + ".xlsx"); // 设置文件名
            OutputStream outputStream = response.getOutputStream(); // 获取输出流
            wb.write(outputStream); // 写入工作簿
            return null;
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

    private static int createSheet1Char(List<Rate> rates, Sheet sheet, Integer endcol) {
        // 为了避免覆盖，控制图表的行偏移
        int baseRow = 0; // 获取当前工作表已使用的行数

        // 用于存储每个图表的纵向偏移量，避免图表位置重叠
        int rowOffset = baseRow;

        // 最终得到拼接后的标题
        String chartTitle = "各工序top10缺陷占比";

        // 创建图表
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 图表锚点：纵向偏移 + (i * 15) 或 (i1 * 15)，确保每个图表都有独立的区域
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + 12);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(chartTitle);
        chart.setTitleOverlay(false);
/*
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        // X轴(分类轴)相关设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴
        xAxis.setTitle(""); // x轴标题

        String[] xAxisData = rates != null ? rates.stream()  // 检查 rates 列表是否为 null
                .map(rate -> rate.getName())  // 获取每个 Rate 对象的 name
                .filter(Objects::nonNull)  // 过滤掉 null 值
                .toArray(String[]::new)      // 转换成 String 数组
                : new String[0];

        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

        // Y轴(值轴)相关设置
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴
        yAxis.setTitle("占比比例(%)"); // Y轴标题
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN); // 设置柱的位置:BETWEEN居中


        // 处理Y轴数据：保留两位小数并乘以100
        Double[] yAxisData = rates.stream()
                .map(rate -> Math.round(rate.getRate() * 10000.0) / 100.0) // 保留两位小数并乘以100
                .toArray(Double[]::new);

        XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // Y轴数据

        // 创建柱状图对象
        XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
        barChart.setBarDirection(BarDirection.COL); // 设置柱状图的方向:COL竖向


        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(xAxisData);
        XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yAxisData);
        XDDFChartData.Series series = barChart.addSeries(xData, yData);
        series.setTitle("", null);
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

        // 设置数字标签为百分比
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
        chart.plot(barChart);
        // 更新 rowOffset 以防下一个图表与当前图表重叠
        rowOffset += 15;  // 每个图表的纵向间隔可以调节为 15 或其他适当值
        return rowOffset;
    }


/*    private static void createSheet2Char(List<Object> result, Sheet sheet, int row, Integer
            endcol) {

        int currentRow = row;


        for (int e = 0; e<result.size(); e++) { // 遍历外层 Map
            Map<String, Object> innerMap = (Map<String, Object>) result.get(e);

            List<Object> dateList = new ArrayList<>();
            List<List<Object>> combinedList = new ArrayList<>(); // 创建组合列表

            String name  = innerMap.get("name").toString();

            dateList = (List<Object>) innerMap.get("date");

            List<Object> dayRateListOptional = (List<Object>)(innerMap.get("dayRateList"));

            List<Object> ydayRateListOptional =(List<Object>)(innerMap.get("nightRateList"));

            List<Object> tbRateslist = new ArrayList<>();
            List<Object> ybRateslist = new ArrayList<>();
            tbRateslist.add("白班");
            ybRateslist.add("晚班");
            dayRateListOptional.forEach(t->{
                tbRateslist.add(t);
            });

            ydayRateListOptional.forEach(g->{
                ybRateslist.add(g);
            });

//
//
//            try {
//                for (int i = 0; i < YrateList.size(); i++) {
//                    TbRate tbRate = new TbRate();
//                    BeanUtils.copyProperties(YrateList.get(i), tbRate);
//                    ybRateslist.add(tbRate);
//                }
//
//                for (int i = 0; i < BrateList.size(); i++) {
//                    TbRate tbRate = new TbRate();
//                    BeanUtils.copyProperties(BrateList.get(i), tbRate);
//                    tbRateslist.add(tbRate);
//                }
//            } catch (Exception v) {
//                v.printStackTrace();
//            }


            combinedList.add(tbRateslist); // 将 BrateList 添加到组合列表
            combinedList.add(ybRateslist);


            // 创建SimpleDateFormat来转换日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");

            // 创建新的List来存储转换后的字符串数据
            List<Object> formattedDates = new ArrayList<>();

            // 将dateList中的日期格式化并添加到formattedDates中
//            for (Object date : dateList) {
//                String formattedDate = sdf.format((Date) date); // 格式化为MM.dd格式
//                formattedDates.add(formattedDate);
//            }


            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            // 调整图表的位置，避免重叠
            int rowOffset = currentRow; // 图表的开始行
            int rowHeight = 15;  // 图表的行高，可以根据需要调整
            currentRow += rowHeight; // 下一次图表的位置

            // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
            XSSFChart chart = drawing.createChart(anchor);

            // 图表标题
            chart.setTitleText(String.format("%s", name));
            // 图例是否覆盖标题
            chart.setTitleOverlay(false);
            XDDFChartLegend legend = chart.getOrAddLegend();
            // 图例位置:上下左右
            legend.setPosition(LegendPosition.TOP_RIGHT);

            // 创建x轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            // X轴标题
            bottomAxis.setTitle("");
            // 左侧标题
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("占比比例(%)");

            // 获取X轴数据
            String[] xTitleData = new String[dateList.size()]; // 根据 dateList 的大小初始化数组
            for (int i = 0; i < dateList.size(); i++) { // 遍历 dateList
                xTitleData[i] = dateList.get(i).toString();  // 转换为字符串并赋值
            }
//            for (int i2 = 0; i2 < notProject.getBanbieList().size(); i2++) {
//                xTitleData = notProject.getBanbieList().get(i2).getDayDatas().stream().map(item -> item.getDay()).toArray(String[]::new);
//                break;
//            }
            XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            // 添加每个系列的数据
            for (List<Object> objects : combinedList) {
               // Double[] xData1 = combinedList.get(i2).stream().map(item -> item.getRate()).toArray(Double[]::new);
                String title = String.valueOf(objects.get(0));
                Double[] xData1 = objects.stream()
                        .skip(1)
                        .map(obj -> {
                            if (obj instanceof Number) {
                                return Double.parseDouble(String.format("%.2f", (double) Math.round(((Number) obj).doubleValue()))); // 保留两位小数
                            } else if (obj instanceof String) {
                                try {
                                    Double parsedDouble = Double.parseDouble((String) obj);
                                    return Double.parseDouble(String.format("%.2f", parsedDouble)); // 保留两位小数
                                } catch (NumberFormatException v) {
                                    return null;
                                }
                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toArray(Double[]::new);

                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);

//
//                String baibie = "";
//                if (i2 == 0) {  // 白班
//                    baibie = "白班";
//                } else {  // 晚班
//                    baibie = "晚班";
//                }


                // 图例标题
                series.setTitle(title, null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);
            }
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            chart.plot(data);
//            plotArea.getBarChartArray(0).getSerArray(0).addNewDLbls();
//            plotArea.getBarChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(true);
//            plotArea.getBarChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);
//            plotArea.getBarChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(false);
//            plotArea.getBarChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
        }


    }*/

 /*   private static void createSheet2Char(List<Object> result, Sheet sheet, int row, Integer
            endcol) {

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 20, 10, "各缺陷良率", "X轴", "Y轴");

        // 用来动态更新行号，以避免多个图表重叠
        int row1 = barChartchartEntity.getRow1();
        int row2 = barChartchartEntity.getRow2();
        int col1 = barChartchartEntity.getCol1();
        int col2 = barChartchartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        for (int i = 0; i < result.size(); i++) {
            List<Object> dateList = new ArrayList<>();
            List<List<Object>> combinedList = new ArrayList<>(); // 创建组合列表

            Map<String, Object> innerMap = (Map<String, Object>) result.get(i);
            dateList = (List<Object>) innerMap.get("date");

            List<Object> dayRateListOptional = (List<Object>) (innerMap.get("dayRateList"));
//            List<Rate> BrateList = dayRateListOptional
//                    .filter(value -> value instanceof List)    // 过滤出 List 类型
//                    .map(value -> (List<Rate>) value)          // 转换为 List<Rate>
//                    .orElse(new ArrayList<Rate>());             // 如果没有值，则返回空 List

            List<Object> ydayRateListOptional = (List<Object>) (innerMap.get("nightRateList"));
//            List<Rate> YrateList = ydayRateListOptional
//                    .filter(value -> value instanceof List)    // 过滤出 List 类型
//                    .map(value -> (List<Rate>) value)          // 转换为 List<Rate>
//                    .orElse(new ArrayList<Rate>());


            List<Object> tbRateslist = new ArrayList<>();
            List<Object> ybRateslist = new ArrayList<>();
            tbRateslist.add("白班");
            ybRateslist.add("晚班");
            dayRateListOptional.forEach(e -> {
                tbRateslist.add(e);
            });

            ydayRateListOptional.forEach(e -> {
                ybRateslist.add(e);
            });

//            BrateList.forEach(e->{
//                tbRateslist.add(e.getRate());
//            });
//
//            YrateList.forEach(e->{
//                ybRateslist.add(e.getRate());
//            });


//            try {
//                for (int j = 0; j < YrateList.size(); j++) {
//                    TbRate tbRate = new TbRate();
//                    BeanUtils.copyProperties(YrateList.get(i), tbRate);
//                    ybRateslist.add(tbRate.getRate());
//                }
//
//                for (int j = 0; j < BrateList.size(); j++) {
//                    TbRate tbRate = new TbRate();
//                    BeanUtils.copyProperties(BrateList.get(j), tbRate);
//                    tbRateslist.add(tbRate.getRate());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            combinedList.add(tbRateslist); // 将 BrateList 添加到组合列表
            combinedList.add(ybRateslist);


            // X轴显示数据
            String[] headArray = dateList.stream().collect(Collectors.toList()).toArray(new String[]{});

            // Create a chart
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleOverlay(true);
            chart.setTitleText(barChartchartEntity.getTitleName());

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
            List<List<Object>> sheetDataList = new ArrayList<>();

   *//*         for (List<Object> objects : sheetDataList) {
                String title = String.valueOf(objects.get(0));

                Double[] yArray = objects.stream()
                        .skip(1)
                        .map(obj -> {
                            if (obj instanceof Number) {
                                return Double.parseDouble(String.format("%.2f", (double) Math.round(((Number) obj).doubleValue()))); // 保留两位小数
                            } else if (obj instanceof String) {
                                try {
                                    Double parsedDouble = Double.parseDouble((String) obj);
                                    return Double.parseDouble(String.format("%.2f", parsedDouble)); // 保留两位小数
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
            }*//*

            // 创建折线图数据
            XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

            for (List<Object> objects : combinedList) {
                String title = String.valueOf(objects.get(0));

                Double[] yArray = objects.stream()
                        .skip(1)
                        .map(obj -> {
                            if (obj instanceof Number) {
                                return Double.parseDouble(String.format("%.2f", (double) Math.round(((Number) obj).doubleValue()))); // 保留两位小数
                            } else if (obj instanceof String) {
                                try {
                                    Double parsedDouble = Double.parseDouble((String) obj);
                                    return Double.parseDouble(String.format("%.2f", parsedDouble)); // 保留两位小数
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

            // 设置数字标签为百分比
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

           // chart.plot(barChartData);
            chart.plot(lineChartData);
        }
    }*/

    private static void createSheet2Char(List<Object> result, Sheet sheet, int row, Integer endcol) {

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 20, 10, "各缺陷良率", "X轴", "Y轴");

        // 用来动态更新行号，以避免多个图表重叠
        int row1 = barChartchartEntity.getRow1();
        int row2 = barChartchartEntity.getRow2();
        int col1 = barChartchartEntity.getCol1();
        int col2 = barChartchartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 500);
        }

        int currentRow = row;  // 当前行号，确保每个图表都有一个独立的行号

        for (int i = 0; i < result.size(); i++) {
            List<Object> dateList = new ArrayList<>();
            List<List<Object>> combinedList = new ArrayList<>(); // 创建组合列表

            Map<String, Object> innerMap = (Map<String, Object>) result.get(i);
            dateList = (List<Object>) innerMap.get("date");
            String name = innerMap.get("name").toString();
            List<Object> dayRateListOptional = (List<Object>) (innerMap.get("dayRateList"));
            List<Object> ydayRateListOptional = (List<Object>) (innerMap.get("nightRateList"));

            List<Object> tbRateslist = new ArrayList<>();
            List<Object> ybRateslist = new ArrayList<>();
            tbRateslist.add("白班");
            ybRateslist.add("晚班");
            dayRateListOptional.forEach(e -> tbRateslist.add(e));
            ydayRateListOptional.forEach(e -> ybRateslist.add(e));

            combinedList.add(tbRateslist); // 将白班数据添加到组合列表
            combinedList.add(ybRateslist); // 将晚班数据添加到组合列表

            // X轴显示数据
            String[] headArray = dateList.stream().collect(Collectors.toList()).toArray(new String[]{});

            // Create a chart
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, currentRow, col2, currentRow + row2);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleOverlay(true);
            chart.setTitleText(barChartchartEntity.getTitleName());
            chart.setTitleText(String.format("%s", name));

            // 创建图表系列
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
            yAxis.setTitle("占比比例(%)");


            // 设置y轴的格式为百分比
            yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            yAxis.setNumberFormat("0.00%"); // 设置百分比格式

            // x轴数据
            XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

            // 创建折线图数据
            XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

            for (List<Object> objects : combinedList) {
                String title = String.valueOf(objects.get(0));

                Double[] yArray = objects.stream()
                        .skip(1)                           // 跳过第一个元素
                        .map(obj -> {                      // 转换每个元素为 Double
                            // 假设 obj 可以转换为 Double 类型，你可以自定义转换逻辑
                            if (obj instanceof Number) {
                                return ((Number) obj).doubleValue();
                            } else {
                                return null;  // 如果不能转换为 Double，返回 null
                            }
                        })
                        .filter(Objects::nonNull)            // 过滤掉 null
                        .toArray(Double[]::new);            // 收集为 Double[] 数组


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

            // 设置数字标签为百分比
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
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

            // 绘制图表
            chart.plot(lineChartData);

            // 每次生成完一个图表，更新当前行号
            currentRow += row2 + 1;  // 将图表位置下移一行，防止覆盖
        }
    }


    private static void createSheet3Char(Map<String, Object> map, Sheet sheet, int row, Integer
            endcol) {

        // 从 resultSheet3 中获取两个数组
        String[] processArr = (String[]) map.get("name");
        Double[] rateArr = (Double[]) map.get("rate");

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 20, 100, "各缺陷良率", "X轴", "Y轴");

        // 用来动态更新行号，以避免多个图表重叠
        int row1 = barChartchartEntity.getRow1();
        int row2 = barChartchartEntity.getRow2();
        int col1 = barChartchartEntity.getCol1();
        int col2 = barChartchartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 500);
        }
        String[] headArray = processArr;

        // Create a chart
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText("各工序良率");

  /*      // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle("占比比例(%)");



        // 设置y轴的格式为百分比
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        yAxis.setNumberFormat("0.00%"); // 设置百分比格式

        // x轴数据
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        // 创建折线图数据
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        //  String title = String.valueOf(objects.get(0));

        Double[] yArray = rateArr;

        XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
        series.setTitle("", null);
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

        // 设置数字标签为百分比
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
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

        // 绘制图表
        chart.plot(lineChartData);
    }

    private static void createSheet4Char(Map<String, List<Object>> map, Sheet sheet, int row, Integer
            endcol) {

        List<Object> nameList = (List<Object>) map.get("name");
        List<Object> rateList = (List<Object>) map.get("rate");

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 20, 100, "各缺陷良率", "X轴", "Y轴");

        // 用来动态更新行号，以避免多个图表重叠
        int row1 = barChartchartEntity.getRow1();
        int row2 = barChartchartEntity.getRow2();
        int col1 = barChartchartEntity.getCol1();
        int col2 = barChartchartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 500);
        }
        // 获取X轴数据
        String[] headArray = new String[nameList.size()]; // 根据 dateList 的大小初始化数组
        for (int i = 0; i < nameList.size(); i++) {
            headArray[i] = nameList.get(i).toString();
        }

        // Create a chart
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText("各工序良率");

  /*      // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle("占比比例(%)");


        // 设置y轴的格式为百分比
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        yAxis.setNumberFormat("0.00%"); // 设置百分比格式

        // x轴数据
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        // 创建折线图数据
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        //  String title = String.valueOf(objects.get(0));


        Double[] yArray = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            yArray[i] = (Double) rateList.get(i);
        }

        XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
        series.setTitle("", null);
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

        // 设置数字标签为百分比
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
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

        // 绘制图表
        chart.plot(lineChartData);

    }

    private static void createSheet5Char(Map<String, List<Object>> map, Sheet sheet, int row, Integer
            endcol,String fseq) {

        List<Object> dateList = (List<Object>) map.get("date");
        List<Object> rateList = (List<Object>) map.get("rate");
        List<Object> namelist = (List<Object>) map.get("fseq");

        ChartEntity barChartchartEntity = new ChartEntity("LINE", 0, 0, 20, 100, "各缺陷良率", "X轴", "Y轴");

        // 用来动态更新行号，以避免多个图表重叠
        int row1 = barChartchartEntity.getRow1();
        int row2 = barChartchartEntity.getRow2();
        int col1 = barChartchartEntity.getCol1();
        int col2 = barChartchartEntity.getCol2();

        // 设置图表列宽
        for (int i = 0; i < row2; i++) {
            sheet.setColumnWidth(i, 500);
        }

        // 获取X轴数据
        String[] headArray = new String[dateList.size()]; // 根据 dateList 的大小初始化数组
        for (int i = 0; i < dateList.size(); i++) {
            headArray[i] = dateList.get(i).toString();
        }

        // Create a chart
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col1, row1, row2, col2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleOverlay(true);
        chart.setTitleText(fseq+"良率");

  /*      // 创建图表系列
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle("占比比例(%)");

        // 设置y轴的格式为百分比
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        yAxis.setNumberFormat("0.00%"); // 设置百分比格式

        // x轴数据
        XDDFDataSource<String> xData = XDDFDataSourcesFactory.fromArray(headArray);

        // 创建折线图数据
        XDDFLineChartData lineChartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        //  String title = String.valueOf(objects.get(0));


        Double[] yArray = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            yArray[i] = (Double) rateList.get(i);
        }

        XDDFNumericalDataSource<Double> yData = XDDFDataSourcesFactory.fromArray(yArray);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) lineChartData.addSeries(xData, yData);
        series.setTitle("", null);
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

        // 设置数字标签为百分比
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineSer ser : plotArea.getLineChartArray(0).getSerList()) {
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

        // 绘制图表
        chart.plot(lineChartData);

    }


}
