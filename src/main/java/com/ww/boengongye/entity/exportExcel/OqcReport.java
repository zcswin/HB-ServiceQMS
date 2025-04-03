package com.ww.boengongye.entity.exportExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liwei
 * @create 2023-09-04 15:31
 */
@Data
@Accessors(chain = true)
@ApiModel
public class OqcReport {
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

	@ExcelProperty(index = 4,value = "FQC员工姓名")
	@ApiModelProperty("FQC员工姓名")
	private String fqcEmpName;

	@ExcelProperty(index = 5,value = "FQC员工工号")
	@ApiModelProperty("FQC员工工号")
	private String fqcEmpCode;

	@ExcelProperty(index = 6,value = "OQC员工姓名" )
	@ApiModelProperty("OQC员工姓名")
	private String oqcEmpName;

	@ExcelProperty(index = 7,value = "OQC员工工号")
	@ApiModelProperty("OQC员工工号")
	private String oqcEmpCode;

	@ExcelProperty(index = 8,value ="FQC来料数")
	@ApiModelProperty("FQC来料数")
	private String fqcTotal;

	@ExcelProperty(index = 9,value = "OQC检查数")
	@ApiModelProperty("OQC检查数")
	private String oqcTotal;

	@ExcelProperty(index = 10,value = "OQC不良数")
	@ApiModelProperty("OQC不良数")
	private String oqcNg;
}
