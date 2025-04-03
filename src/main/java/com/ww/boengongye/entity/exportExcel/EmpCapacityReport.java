package com.ww.boengongye.entity.exportExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 员工产能报表
 * @author liwei
 * @create 2023-09-04 10:25
 */
@Data
@Accessors(chain = true)
@ApiModel
public class EmpCapacityReport {

	@ColumnWidth(15)
	@ExcelProperty(index = 0,value = "日期")
	@ApiModelProperty("日期")
	private String date;

	@ExcelProperty(index = 1,value = "工厂")
	@ApiModelProperty("工厂")
	private String factoryId;

	@ExcelProperty(index = 2,value = "线体")
	@ApiModelProperty("线体")
	private String lineBobyId;

	@ExcelProperty(index = 3,value = "项目")
	@ApiModelProperty("项目")
	private String project;

	@ExcelProperty(index = 4,value = "白夜班")
	@ApiModelProperty("白夜班")
	private String classCategory;

	@ExcelProperty(index = 5,value = "工站")
	@ApiModelProperty("工站")
	private String process;

	@ExcelProperty(index = 6,value = "员工姓名" )
	@ApiModelProperty("员工姓名")
	private String alias;

	@ExcelProperty(index = 7,value = "员工工号")
	@ApiModelProperty("员工工号")
	private String name;

	@ExcelProperty(index = 8,value ="投入")
	@ApiModelProperty("投入")
	private String total;

	@ExcelProperty(index = 9,value = "产出")
	@ApiModelProperty("产出")
	private String ok;

	@ExcelProperty(index = 10,value = "不良")
	@ApiModelProperty("不良")
	private String ng;

}
