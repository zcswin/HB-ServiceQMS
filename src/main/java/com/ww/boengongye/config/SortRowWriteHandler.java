package com.ww.boengongye.config;


import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 列排序处理器
 *
 * @author wf
 */
public class SortRowWriteHandler implements RowWriteHandler {

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {
        // 获取传入的包含的列(字段的名字list),将headMap的索引按照传入的列的顺序重新放入,即可实现排序
        ExcelWriteHeadProperty excelWriteHeadProperty = writeSheetHolder.getExcelWriteHeadProperty();
        //行头部排序map
        Map<Integer, Head> headMap = excelWriteHeadProperty.getHeadMap();
        //行内容排序map
        Map<Integer, ExcelContentProperty> contentPropertyMap = excelWriteHeadProperty.getContentPropertyMap();
        Collection<String> includeColumnFieldNames = writeSheetHolder.getIncludeColumnFiledNames();
        // 将headMap中的字段名字对应Head
        Map<String, Head> fieldNameHead = headMap.values().stream().collect(Collectors.toMap(Head::getFieldName, head -> head));
        Map<String, ExcelContentProperty> fieldNameColumnFiled = contentPropertyMap.values().stream().collect(Collectors.toMap(e -> e.getHead().getFieldName(), e -> e));
        int index = 0;
        for (String includeColumnFieldName : includeColumnFieldNames) {
            if (isHead) {
                // 按照includeColumnFieldNames中的顺序取出head重新覆盖
                Head head = fieldNameHead.get(includeColumnFieldName);
                if (head == null) {
                    continue;
                }
                headMap.put(index, head);
            } else {
                // 按照includeColumnFieldNames中的顺序取出fieldNameColumnFiled重新覆盖
                ExcelContentProperty contentProperty = fieldNameColumnFiled.get(includeColumnFieldName);
                if (contentProperty == null) {
                    continue;
                }
                contentPropertyMap.put(index,contentProperty);
            }
            index++;
        }
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {

    }

}


