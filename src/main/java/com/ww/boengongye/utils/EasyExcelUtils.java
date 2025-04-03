package com.ww.boengongye.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.ww.boengongye.config.ExcelColumnMergeHandler;
import com.ww.boengongye.config.ExcelRowMergeHandler;
import com.ww.boengongye.config.SortRowWriteHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel工具类
 */
public class EasyExcelUtils {


    /**
     * 注入的具有排序功能的handle
     */
    private static final SortRowWriteHandler SORT_ROW_WRITE_HANDLER = new SortRowWriteHandler();

    /**
     * 导出excel-按指定顺序
     *
     * @param <T>      类(必须是小写，并遵守阿里开发规范)
     * @param list     列表数据
     * @param fileName 文件名称
     * @param useDefaultStyle   是否使用默认样式
     * @param response 响应
     */
    public static <T> Map<String, Object> exportExcelInclude(List<T> list, String fileName,Boolean useDefaultStyle, HttpServletResponse response) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        if (list.size() == 0) {
            resultMap.put("msg", "数据长度为空");
            resultMap.put("result", -1);
            return resultMap;
        }

        Class<?> clazz = list.get(0).getClass();

        /*定义编码，格式*/
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String exportFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + exportFileName + ".xlsx");
        response.setHeader("filename", exportFileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "filename");
        /**
         * .head(head(headNameList))
         * 改行代码不可以用ExcelWriterSheetBuilder类型的变量单独写，否则会包错：表头长度与字段列表长度不符
         */
        /*导出表格*/
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(response.getOutputStream(), clazz)
                .useDefaultStyle(useDefaultStyle)
                .sheet(fileName);
        excelWriterSheetBuilder.doWrite(list);

        resultMap.put("msg", "excel export success");
        resultMap.put("result", 0);
        return resultMap;
    }


    /**
     * 导出excel-默认顺序
     *
     * @param <T>      类(必须是小写，并遵守阿里开发规范)
     * @param list     列表数据
     * @param fileName 文件名称
     * @param useDefaultStyle   是否使用默认样式
     * @param response 响应
     * @param headNameList
     * @param columnList 设置列字段(必须是小写)
     */
    public static <T> Map<String, Object> exportExcelInclude(List<T> list, String fileName,Boolean useDefaultStyle, HttpServletResponse response, List<String> columnList, List<String> headNameList) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        if (list.size() == 0) {
            resultMap.put("msg", "数据长度为空");
            resultMap.put("result", -1);
            return resultMap;
        }

        if (CollectionUtils.isEmpty(headNameList)) {
            /*设置表头*/
            resultMap.put("msg", "表头长度为空");
            resultMap.put("result", -2);
            return resultMap;
        }

        if (CollectionUtils.isEmpty(columnList)) {
            /*设置列字段*/
            resultMap.put("msg", "列字段长度为空");
            resultMap.put("result", -3);
            return resultMap;
        }

        Class<?> clazz = list.get(0).getClass();

        /*定义编码，格式*/
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String exportFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + exportFileName + ".xlsx");
        response.setHeader("filename", exportFileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "filename");
        /**
         * .head(head(headNameList))
         * 改行代码不可以用ExcelWriterSheetBuilder类型的变量单独写，否则会包错：表头长度与字段列表长度不符
         */
        /*导出表格*/
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(response.getOutputStream(), clazz)
                .head(head(headNameList))
                /*设置列字段（会按顺序）*/
                .includeColumnFiledNames(columnList)
                /*注入的具有排序功能的handle*/
                .registerWriteHandler(SORT_ROW_WRITE_HANDLER)
                .useDefaultStyle(useDefaultStyle)
                .sheet(fileName);
        excelWriterSheetBuilder.doWrite(list);

        resultMap.put("msg", "excel export success");
        resultMap.put("result", 0);
        return resultMap;
    }


    /**
     * 支持动态头,行列合并导出
     *
     * @param response web响应
     * @param headList 动态头
     * @param dataList 数据列表
     * @param columnMergeHandler 注入列合并
     * @param rowMergeHandler 注入行合并
     * @throws IOException
     */
    public static Map<String, Object> writeDynamicExcel(HttpServletResponse response, @NotEmpty List<List<String>> headList, @NotEmpty List<List<Object>> dataList, @NotNull ExcelColumnMergeHandler columnMergeHandler, @NotNull ExcelRowMergeHandler rowMergeHandler) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        if (dataList.size() == 0) {
            resultMap.put("msg", "数据长度为空");
            resultMap.put("result", -1);
            return resultMap;
        }

        if (CollectionUtils.isEmpty(headList)) {
            /*设置表头*/
            resultMap.put("msg", "表头长度为空");
            resultMap.put("result", -2);
            return resultMap;
        }

        if (columnMergeHandler == null || rowMergeHandler == null) {
            /*设置列字段*/
            resultMap.put("msg", "行列处理器为空");
            resultMap.put("result", -3);
            return resultMap;
        }

        EasyExcel.write(response.getOutputStream())
                // 设置动态头
                .head(headList)
                .sheet("模板")
                /*注入列合并*/
                .registerWriteHandler(columnMergeHandler)
                /*注入行合并*/
                .registerWriteHandler(rowMergeHandler)
                /*传需要写入的数据，类型List<List<Object>>*/
                .doWrite(dataList);

        resultMap.put("msg", "excel export success");
        resultMap.put("result", 0);
        return resultMap;
    }


    /**
     * 支持动态头导出
     *
     * @param response web响应
     * @param headList 动态头
     * @param dataList 数据列表
     * @throws IOException
     */
    public static void writeDynamicExcel(HttpServletResponse response,List<List<String>> headList, List<List<Object>> dataList) throws IOException {
        EasyExcel.write(response.getOutputStream())
                // 设置动态头
                .head(headList)
                .sheet("模板")
                /*传需要写入的数据，类型List<List<Object>>*/
                .doWrite(dataList);
    }


    /**
     * 设置Excel头
     *
     * @param headList  Excel头信息
     * @return
     */
    public static List<List<String>> head(List<String> headList) {
        List<List<String>> list = new ArrayList<>();
        for (String value : headList) {
            List<String> head = new ArrayList<>();
            head.add(value);
            list.add(head);
        }
        return list;
    }

    /**
     * Excel头对应的字段转换小写
     *
     * @param ColumnListTemp Excel字段
     * @return
     */
    public static List<String> castLowerCase(List<String> ColumnListTemp) {
        List<String> ColumnList = new ArrayList<>();
        for (String name : ColumnListTemp) {
            ColumnList.add(StrUtil.lowerFirst(name));
        }
        return ColumnList;
    }

}

