package com.ww.boengongye.config;


import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行合并处理器
 *
 */
public class ExcelRowMergeHandler implements CellWriteHandler {
    private static final String KEY ="%s-%s";
    //所有的合并信息都存在了这个map里面
    Map<String, Integer> mergeInfo = new HashMap<>();

    public ExcelRowMergeHandler() {
    }
    public ExcelRowMergeHandler(Map<String, Integer> mergeInfo) {
        this.mergeInfo = mergeInfo;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        Integer num = mergeInfo.get(String.format(KEY, curRowIndex, curColIndex));
        if(null != num){
            // 合并最后一行 ,列
            mergeWithPrevCol(writeSheetHolder, cell, curRowIndex, curColIndex,num);
        }
    }

    public void mergeWithPrevCol(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex, int num) {
        Sheet sheet = writeSheetHolder.getSheet();
        CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex, curRowIndex, curColIndex, curColIndex + num);
        sheet.addMergedRegion(cellRangeAddress);
    }


    /**
     * 添加合并行
     *      比如参数：(3,0,2)
     *      从第三行的地0列开始向右合并2格
     *      这样，第三行的地0列到第2列就会合并
     *
     * 注意：
     *      合并不能冲突，如果合并到已经被合并的地方会报一个POI错误
     *      (java.lang.IllegalStateException: Cannot add merged region B3:B6 to sheet because it overlaps with an existing merged region (A6:C6).)
     *
     * @param curRowIndex 当前行： 需要合并的哪一行(一般用数据List的“index”+“表头的行数”,表头站的行数是1就+1，是2就+2)
     * @param curColIndex 合并开始列：从第几行开始合并
     * @param num 合并列的数量：从第curColIndex行开始，向右合并多少格
     */
    public void add (int curRowIndex,  int curColIndex , int num){
        mergeInfo.put(String.format(KEY, curRowIndex, curColIndex),num);
    }

}
