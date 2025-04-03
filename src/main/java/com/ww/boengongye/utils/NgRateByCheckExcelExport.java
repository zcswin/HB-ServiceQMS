package com.ww.boengongye.utils;

import com.ww.boengongye.entity.DfQmsIpqcWaigDetailCheck;
import com.ww.boengongye.entity.Rate;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xddf.usermodel.LineCap;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NgRateByCheckExcelExport {

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


    public byte[] NgRateByExport(HttpServletRequest request, HttpServletResponse response, List<Rate> rates, Map<String, List<Object>> resultSheet2, Map<String, List<Object>> resultSheet3, List<Rate> ratesSheet4, List<DfQmsIpqcWaigDetailCheck> datas,String fsort) throws IOException {

        // 创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook(); // 使用 XSSFWorkbook

        //sheet1
        XSSFSheet sheet1 = wb.createSheet("工序"+fsort+"明细表");
        Map<String, Integer> centerTable1 = createSheet1Table(sheet1, 0, 0, rates,fsort);


        //sheet2
        XSSFSheet sheet2 = wb.createSheet("工序"+fsort+"NG率");
        createSheet2Char(resultSheet2, sheet2, 0,fsort);


        //sheet3
        XSSFSheet sheet3 = wb.createSheet(fsort+"缺陷分布机台");
        createSheet3Char(resultSheet3, sheet3, 0,fsort);


        //sheet4
        XSSFSheet sheet4 = wb.createSheet(fsort+"分布机台明细表");
        createSheet4Table(sheet4, 0, 0, ratesSheet4,fsort);


        //sheet5
        if (datas!= null){
            XSSFSheet sheet5 = wb.createSheet("PPM");
            createSheet5Table(sheet5, 0, 0, datas,fsort);
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


    private static Map<String, Integer> createSheet5Table(Sheet sheet, int firstRow, int firstCol, List<DfQmsIpqcWaigDetailCheck> datas,String fsort) {
        HashMap<String, Integer> map = new HashMap<>(); // 创建一个 HashMap 用于存储数据
        // 创建表头
        int totalRow = firstRow; // 初始化总行数为 firstRow
        int startRow = totalRow + 1; // 设置开始行数

        Row headerRow = sheet.getRow(totalRow); // 获取表头行
        if (headerRow == null) { // 如果表头行不存在
            headerRow = sheet.createRow(totalRow); // 创建表头行
        }
        String[] headers = {fsort+"分布位置", "抽检总数", "NG数", "PPM"}; // 表头内容
        for (int i = 0; i < headers.length; i++) { // 遍历表头内容
            Cell cell = headerRow.createCell(firstCol + i); // 创建单元格
            cell.setCellValue(headers[i]); // 设置单元格值为表头内容
        }
        totalRow++; // 移动到下一行
        Row row; // 声明数据行
        // 遍历数据列表，处理每个 Rate 对象
        if (datas != null && !datas.isEmpty()) { // 检查 datas 列表是否为 null 或空
            for (DfQmsIpqcWaigDetailCheck bb : datas) { // 遍历每个 Rate 对象
                row = sheet.createRow(totalRow); // 创建这一行
                // 根据索引设置单元格的值
                Integer ngNum = bb.getId();
                Integer count = bb.getFParentId();
                if (ngNum == null){
                    ngNum = 0;
                }
                double ppm = 0; // 初始化ppm为0
                if (count != null && count > 0) { // 检查count是否非空且大于0
                    ppm = ((ngNum != null ? ngNum : 0) / (double) count) * 1000000; // 计算ppm值
                }

                row.createCell(firstCol).setCellValue(bb.getfSmArea()); // 设置设备名称
                row.createCell(firstCol + 1).setCellValue(bb.getFParentId()); // 设置抽检总数
                row.createCell(firstCol + 2).setCellValue(ngNum); // 设置NG数
                row.createCell(firstCol + 3).setCellValue(ppm); // 设置ppm率
                totalRow++; // 移动到下一行
            }
        }
        return map; // 返回创建的 Map
    }


    private static Map<String, Integer> createSheet1Table(Sheet sheet, int firstRow, int firstCol, List<
            Rate> datas,String fsort) {
        HashMap<String, Integer> map = new HashMap<>();
        // 创建表头
        // 创建表头
        int totalRow = 0;

        Row headerRow = sheet.getRow(totalRow);
        if (headerRow == null) {
            headerRow = sheet.createRow(totalRow);
        }
        String[] headers = {fsort+"分布工序", "抽检总数", "NG数", "NG率"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }


        for (Rate bb : datas) {
            totalRow++;
            Row row = sheet.createRow(totalRow);
            if (row == null) {
                row = sheet.createRow(totalRow);
            }
            for (int i = 0; i < headers.length; i++) {
                if (i ==0) {
                    Cell cell = row.createCell(firstCol + i);
                    cell.setCellValue(bb.getName());
                }
                if (i == 1) {
                    Cell cell = row.createCell(firstCol + i);
                    cell.setCellValue(bb.getAllNum());
                }
                if (i == 2) {
                    Cell cell = row.createCell(firstCol + i);
                    cell.setCellValue(bb.getNgNum());
                }
                if (i == 3) {
                    Cell cell = row.createCell(firstCol + i);
                    double rate = bb.getRate(); // 获取 rate 值
                    DecimalFormat df = new DecimalFormat("0.00%"); // 创建格式化对象，格式为百分比，保留两位小数
                    String formattedRate = df.format(rate);
                    cell.setCellValue(formattedRate);
                }

            }
        }

        map.put("row", totalRow);
        return map;

    }

    private static Map<String, Integer> createSheet4Table(Sheet sheet, int firstRow, int firstCol, List<Rate> datas,String fsort) {
        HashMap<String, Integer> map = new HashMap<>(); // 创建一个 HashMap 用于存储数据
        // 创建表头
        int totalRow = 0; // 初始化总行数为 firstRow

        Row headerRow = sheet.getRow(totalRow); // 获取表头行
        if (headerRow == null) { // 如果表头行不存在
            headerRow = sheet.createRow(totalRow); // 创建表头行
        }
        String[] headers = {fsort+"分布机台", "抽检总数", "NG数", "NG率"}; // 表头内容
        for (int i = 0; i < headers.length; i++) { // 遍历表头内容
            Cell cell = headerRow.createCell(firstCol + i); // 创建单元格
            cell.setCellValue(headers[i]); // 设置单元格值为表头内容
        }
        totalRow++; // 移动到下一行
        Row row; // 声明数据行
        // 遍历数据列表，处理每个 Rate 对象
        if (datas != null && !datas.isEmpty()) { // 检查 datas 列表是否为 null 或空
            for (Rate bb : datas) { // 遍历每个 Rate 对象
                row = sheet.createRow(totalRow); // 创建这一行
                // 根据索引设置单元格的值
                row.createCell(firstCol).setCellValue(bb.getName()); // 设置设备名称
                row.createCell(firstCol + 1).setCellValue(bb.getAllNum()); // 设置抽检总数
                row.createCell(firstCol + 2).setCellValue(bb.getNgNum()); // 设置NG数

                double rate = bb.getRate(); // 获取 rate 值
                DecimalFormat df = new DecimalFormat("0.00%"); // 创建格式化对象，格式为百分比，保留两位小数
                String formattedRate = df.format(rate);
                row.createCell(firstCol + 3).setCellValue(formattedRate); // 设置NG率
                totalRow++; // 移动到下一行
            }
        }
        return map; // 返回创建的 Map
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

    private static int createSheet2Char(Map<String, List<Object>> resultSheet2, Sheet sheet, Integer endcol,String fsort) {

        List<Object> nameList = resultSheet2.get("name");
        List<Object> rateList = resultSheet2.get("rate");
        List<Object> ngNumList = resultSheet2.get("ngNum");
        List<Object> allNumList = resultSheet2.get("allNum");


        // 为了避免覆盖，控制图表的行偏移
        int baseRow = 0; // 获取当前工作表已使用的行数

        // 用于存储每个图表的纵向偏移量，避免图表位置重叠
        int rowOffset = baseRow;

        //   for (int i = 0; i < rates.size(); i++) {

        // 最终得到拼接后的标题
        String chartTitle = "工序"+fsort+"NG率";

        // 创建图表
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 图表锚点：纵向偏移 + (i * 15) 或 (i1 * 15)，确保每个图表都有独立的区域
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + 12);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(chartTitle);
        chart.setTitleOverlay(false);

  /*      XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        // X轴(分类轴)相关设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴
        xAxis.setTitle(""); // x轴标题


        String[] xAxisData = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            xAxisData[i] = String.valueOf(nameList.get(i));
        }


        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

        // Y轴(值轴)相关设置
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴
        yAxis.setTitle(""); // Y轴标题
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN); // 设置柱的位置:BETWEEN居中

        Double[] yAxisData = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            yAxisData[i] = (Double) rateList.get(i);
        }

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
        // }
        return rowOffset;
    }


    private static int createSheet3Char(Map<String, List<Object>> resultSheet2, Sheet sheet, Integer endcol,String fsort) {

        List<Object> nameList = resultSheet2.get("name");
        List<Object> rateList = resultSheet2.get("rate");
        List<Object> ngNumList = resultSheet2.get("ngNum");
        List<Object> allNumList = resultSheet2.get("allNum");


        // 为了避免覆盖，控制图表的行偏移
        int baseRow = 0; // 获取当前工作表已使用的行数

        // 用于存储每个图表的纵向偏移量，避免图表位置重叠
        int rowOffset = baseRow;

        //   for (int i = 0; i < rates.size(); i++) {

        // 最终得到拼接后的标题
        String chartTitle = fsort+"缺陷分布机台";

        // 创建图表
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 图表锚点：纵向偏移 + (i * 15) 或 (i1 * 15)，确保每个图表都有独立的区域
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + 12);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(chartTitle);
        chart.setTitleOverlay(false);

/*        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);*/

        // X轴(分类轴)相关设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); // 创建X轴
        xAxis.setTitle(""); // x轴标题


        String[] xAxisData = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            xAxisData[i] = String.valueOf(nameList.get(i));
        }


        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

        // Y轴(值轴)相关设置
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT); // 创建Y轴
        yAxis.setTitle(""); // Y轴标题
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN); // 设置柱的位置:BETWEEN居中

        Double[] yAxisData = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            yAxisData[i] = (Double) rateList.get(i);
        }

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

        rowOffset += 15;  // 每个图表的纵向间隔可以调节为 15 或其他适当值
        // }
        return rowOffset;
    }




/*

    private static void createSheet2Char(Map<String, Map<String, List<Object>>> itemResData, Sheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;


        for (Map.Entry<String, Map<String, List<Object>>> outerEntry : itemResData.entrySet()) { // 遍历外层 Map
            String outerKey = outerEntry.getKey(); // 获取外层 Map 的键
            Map<String, List<Object>> innerMap = outerEntry.getValue(); // 获取内层 Map

            List<Object> dateList = new ArrayList<>();
            List<List<TbRate>> combinedList = new ArrayList<>(); // 创建组合列表


            dateList = innerMap.get("date");

            Optional<Object> dayRateListOptional = Optional.ofNullable(innerMap.get("dayRateList"));
            List<Rate> BrateList = dayRateListOptional
                    .filter(value -> value instanceof List)    // 过滤出 List 类型
                    .map(value -> (List<Rate>) value)          // 转换为 List<Rate>
                    .orElse(new ArrayList<Rate>());             // 如果没有值，则返回空 List

            Optional<Object> ydayRateListOptional = Optional.ofNullable(innerMap.get("nightRateList"));
            List<Rate> YrateList = ydayRateListOptional
                    .filter(value -> value instanceof List)    // 过滤出 List 类型
                    .map(value -> (List<Rate>) value)          // 转换为 List<Rate>
                    .orElse(new ArrayList<Rate>());

            List<TbRate> tbRateslist = new ArrayList<>();
            List<TbRate> ybRateslist = new ArrayList<>();

            TbRate BtbRate = new TbRate();
            BtbRate.setBanbie("白班");
            TbRate YbRate2 = new TbRate();
            YbRate2.setBanbie("夜班");
            tbRateslist.add(BtbRate);
            ybRateslist.add(YbRate2);

            try {
                for (int i = 0; i < YrateList.size(); i++) {
                    TbRate tbRate = new TbRate();
                    BeanUtils.copyProperties(YrateList.get(i), tbRate);
                    ybRateslist.add(tbRate);
                }

                for (int i = 0; i < BrateList.size(); i++) {
                    TbRate tbRate = new TbRate();
                    BeanUtils.copyProperties(YrateList.get(i), tbRate);
                    tbRateslist.add(tbRate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


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
            chart.setTitleText(String.format("%s", outerKey));
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
            leftAxis.setTitle("不良");

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
            for (int i2 = 0; i2 < combinedList.size(); i2++) {
                Double[] xData1 = combinedList.get(i2).stream().map(item -> item.getRate()).toArray(Double[]::new);
                XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
                XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);

                String baibie = combinedList.get(i2).get(0).getBanbie();
                // 图例标题
                series.setTitle(baibie, null);
                // 线条样式:true平滑曲线,false折线
                series.setSmooth(false);
                // 点的样式
                series.setMarkerStyle(MarkerStyle.CIRCLE);
            }
            chart.plot(data);

        }
    }
*/


    private static void createSheet4Char(Map<String, List<Object>> map, Sheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;


        // 从 resultSheet3 中获取两个数组
        List<Object> nameList = (List<Object>) map.get("name");
        List<Object> rateList = (List<Object>) map.get("rate");


        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 调整图表的位置，避免重叠
        int rowOffset = currentRow; // 图表的开始行
        int rowHeight = 15;  // 图表的行高，可以根据需要调整
        currentRow += rowHeight; // 下一次图表的位置

        // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
        XSSFChart chart = drawing.createChart(anchor);

        // 图表标题
        chart.setTitleText(String.format("%s", "各工序良率"));
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
        leftAxis.setTitle("不良");

        // 获取X轴数据
        String[] xTitleData = new String[nameList.size()]; // 根据 dateList 的大小初始化数组
        for (int i = 0; i < nameList.size(); i++) {
            xTitleData[i] = nameList.get(i).toString();
        }


        XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        Double[] xData1 = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            xData1[i] = (Double) rateList.get(i);
        }


        XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
        XDDFLineChartData.Series series = series1;

        // 图例标题
        series.setTitle("良率", null);
        // 线条样式:true平滑曲线,false折线
        series.setSmooth(false);
        // 点的样式
        series.setMarkerStyle(MarkerStyle.CIRCLE);
        chart.plot(data);

    }

    private static void createSheet5Char(Map<String, List<Object>> map, Sheet sheet, int row, Integer
            endcol) {
        // 用来动态更新行号，以避免多个图表重叠
        int currentRow = row;


        // 从 resultSheet3 中获取两个数组
        List<Object> dateList = (List<Object>) map.get("date");
        List<Object> rateList = (List<Object>) map.get("rate");


        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 调整图表的位置，避免重叠
        int rowOffset = currentRow; // 图表的开始行
        int rowHeight = 15;  // 图表的行高，可以根据需要调整
        currentRow += rowHeight; // 下一次图表的位置

        // 2 左侧距离单元格个数, 4 顶部距离单元格个数, 7 左侧距离单元格个数, 26 顶部距离单元格个数
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, endcol + 2, rowOffset, endcol + 10, rowOffset + rowHeight);
        XSSFChart chart = drawing.createChart(anchor);

        // 图表标题
        chart.setTitleText(String.format("%s", "各工序良率"));
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
        leftAxis.setTitle("不良");

        // 获取X轴数据
        String[] xTitleData = new String[dateList.size()]; // 根据 dateList 的大小初始化数组
        for (int i = 0; i < dateList.size(); i++) {
            xTitleData[i] = dateList.get(i).toString();
        }


        XDDFDataSource<String> date = XDDFDataSourcesFactory.fromArray(xTitleData);
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        Double[] xData1 = new Double[rateList.size()];
        for (int i = 0; i < rateList.size(); i++) {
            // 强制转换 Object 为 Double
            xData1[i] = (Double) rateList.get(i);
        }


        XDDFNumericalDataSource<Double> doubleXDDFNumericalDataSource = XDDFDataSourcesFactory.fromArray(xData1);
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(date, doubleXDDFNumericalDataSource);
        XDDFLineChartData.Series series = series1;

        // 图例标题
        series.setTitle("良率", null);
        // 线条样式:true平滑曲线,false折线
        series.setSmooth(false);
        // 点的样式
        series.setMarkerStyle(MarkerStyle.CIRCLE);
        chart.plot(data);

    }

}
