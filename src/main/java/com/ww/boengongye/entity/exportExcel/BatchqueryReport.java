package com.ww.boengongye.entity.exportExcel;

/**
 * @author liwei
 * @create 2023-09-02 2:45
 */

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ww.boengongye.config.OkNgConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 批量查询报表实体
 */
@Data
@Accessors(chain = true)
@ApiModel("批量查询报表实体")
public class BatchqueryReport {

	@ColumnWidth(70)
	@ExcelProperty(index = 0,value = "玻璃条码")
	@ApiParam("玻璃条码")
	private String glassBarCode;

	@ExcelProperty(index = 1,value = "型号")
	@ApiParam("型号")
	private String model;

	@ExcelProperty(index = 2,value = "颜色")
	@ApiParam("颜色")
	private String color;

	@ExcelProperty(index = 3,value = "AOI机台")
	@ApiParam("AOI机台")
	private String aoiMachine;

	@ExcelProperty(index = 4,value = "过AOI次数")
	@ApiParam("过AOI次数")
	private String count;

	@ExcelProperty(index = 5,value = "AOI判定时间")
	@ApiParam("AOI判定时间")
	@ColumnWidth(20)
	private String aoiTime;

	@ExcelProperty(index = 6,value = "AOI判定结果" , converter = OkNgConvert.class)
	@ApiParam("AOI判定结果")
	private String aoiResult;

	@ExcelProperty(index = 7,value = {"FQC工站","员工姓名"})
	@ApiParam("FQC工站,员工姓名")
	private String fqcEmpName;

	@ColumnWidth(15)
	@ExcelProperty(index = 8,value ={"FQC工站","员工工号"})
	@ApiParam("FQC工站,员工工号")
	private String fqcEmpCode;

	@ColumnWidth(20)
	@ExcelProperty(index = 9,value = {"FQC工站","判定时间"})
	@ApiParam("FQC工站,判定时间")
	private String fqcTime;

	@ExcelProperty(index = 10,value = {"FQC工站","判定结果"})
	@ApiParam("FQC工站,判定结果")
	private String fqcResult;

	@ExcelProperty(index = 11,value = {"OQC工站","员工姓名"})
	@ApiParam("OQC工站,员工姓名")
	private String oqcEmpName;

	@ColumnWidth(15)
	@ExcelProperty(index = 12,value = {"OQC工站","员工工号"})
	@ApiParam("OQC工站,员工工号")
	private String oqcEmpCode;

	@ColumnWidth(20)
	@ExcelProperty(index = 13,value = {"OQC工站","判定时间"})
	@ApiParam("OQC工站,判定时间")
	private String oqcTime;

	@ExcelProperty(index = 14,value = {"OQC工站","判定结果"})
	@ApiParam("OQC工站,判定结果")
	private String oqcResult;

	@ExcelIgnore
	private String empName;
	@ExcelIgnore
	private String empCode;
	@ExcelIgnore
	private String empTime;
	@ExcelIgnore
	private String empResult;

}
