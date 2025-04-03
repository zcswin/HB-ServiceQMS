package com.ww.boengongye.utils;


import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {
    //表头
    private String title;

    private String startTime;
    private String endTime;


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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
        // 检查参数配置信息
        checkConfig();
        // 创建工作簿（XSSFWorkbook 生成 .xlsx 格式）
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 创建工作表
        SXSSFSheet wbSheet = wb.createSheet(this.sheetName);
        // 设置默认列宽
        wbSheet.setDefaultColumnWidth(20);

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

        // 导出数据
        try {
            String name = generateFileName("稽查原始数据",startTime,endTime);

            // RFC 5987 编码处理
            String encodedNameRFC5987 = URLEncoder.encode(name, StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20")
                    .replaceAll("%21", "!")
                    .replaceAll("%7E", "~");

            // ISO-8859-1 编码处理旧版浏览器兼容
            String encodedNameISO88591 = URLEncoder.encode(name, StandardCharsets.ISO_8859_1.name())
                    .replaceAll("\\+", "%20");

            String contentDisposition = String.format(
                    "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                    encodedNameISO88591,
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

    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    public void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}
