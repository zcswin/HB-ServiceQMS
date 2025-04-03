package com.ww.boengongye.entity.exportExcelUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: service-dashboard-data
 * @ClassName ChartEntity
 * @description: 图表实体
 * @author: xiongsg
 * @create: 2024-09-29 14:50
 * @Version 1.0
 **/

@Getter
@Setter
public class ChartEntity {
    /**
     * 图表类型
     *
     * @see NpsConstants.ChartTypeEnum
     */
    private String chartType;
    /**
     * 图表的左上角坐标列
     */
    private int col1 = 10;
    /**
     * 图表的左上角坐标行
     */
    private int row1 = 10;
    /**
     * 图表的右下角坐标列
     */
    private int col2 = 10;
    /**
     * 图表的右下角坐标行t
     */
    private int row2 = 10;

    /**
     * 标题名称
     */
    private String titleName;
    /**
     * x轴名称
     */
    private String xAxisName;
    /**
     * y轴名称
     */
    private String yAxisName;

    public ChartEntity() {
    }

    public ChartEntity(String chartType, int col1, int row1, int col2, int row2, String titleName, String xAxisName, String yAxisName) {
        this.chartType = chartType;
        this.col1 = col1;
        this.row1 = row1;
        this.col2 = col2;
        this.row2 = row2;
        this.titleName = titleName;
        this.xAxisName = xAxisName;
        this.yAxisName = yAxisName;
    }

}
