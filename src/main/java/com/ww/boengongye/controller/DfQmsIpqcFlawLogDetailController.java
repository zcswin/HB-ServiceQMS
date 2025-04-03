package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfQmsIpqcFlawLogDetail;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.service.DfQmsIpqcFlawLogDetailService;
import com.ww.boengongye.utils.ExcelExportUtil2;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-06-21
 */
@Api(tags = "RFID工序流转明细表记录")
@RestController
@RequestMapping("/dfQmsIpqcFlawLogDetail")
@CrossOrigin
public class DfQmsIpqcFlawLogDetailController {

	@Autowired
	private DfQmsIpqcFlawLogDetailService dfQmsIpqcFlawLogDetailService;
	@Autowired
	private DfProcessService dfProcessService;


	/**
	 * Get请求
	 * 3.明细查询接口,根据id,获取对应parent_id df_qms_ipqc_flaw_log_detail的数据
	 */
	@ApiOperation("按条件查询明细表记录")
	@GetMapping("getDetailList")
	public Result getDetailList(@RequestParam(required = true) int flawLogId) {
		QueryWrapper<DfQmsIpqcFlawLogDetail> ew = new QueryWrapper<DfQmsIpqcFlawLogDetail>();
		ew.eq("parent_id",flawLogId);
		List<DfQmsIpqcFlawLogDetail> list = dfQmsIpqcFlawLogDetailService.list(ew);
		return new Result(200, "查询成功", list);
	}

	/**
	 * Get请求
	 * 导出RFID明细 请求参数(玻璃码,工序,缺陷,开始时间,结束时间)
	 */
	@ApiOperation("导出RFID明细")
	@GetMapping("exportRfidDetail")
	public void exportRfidDetail(String barcode,String process,String defect,String startDate,String endDate,
								   HttpServletRequest request,
								   HttpServletResponse response) throws IOException, ParseException{
		QueryWrapper<DfQmsIpqcFlawLogDetail> ew = new QueryWrapper<DfQmsIpqcFlawLogDetail>();
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			ew.gt("log.create_time",startTime);
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			ew.lt("log.create_time",endTime);
		}
		ew
				.like(StringUtils.isNotEmpty(barcode),"bar_code",barcode)
				.like(StringUtils.isNotEmpty(process),"log.process",process)
				.like(StringUtils.isNotEmpty(defect),"flaw_name",defect);
		List<Map> results = dfQmsIpqcFlawLogDetailService.exportRfidDetail(ew);

		if (!CollectionUtils.isEmpty(results)) {

			//设置表格表头字段
			String[] title = new String[]{"玻璃码", "工序", "缺陷", "扫码时间", "经过工序"};

			// 查询对应的字段
			String[] properties = new String[]{"barCode", "process", "flawName", "scanningTime","processThrough"};
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"RFID明细表导出");
		}
	}

	/**
	 * Get请求
	 * 导出RFID明细 请求参数(玻璃码,工序,缺陷,开始时间,结束时间)
	 */
	@ApiOperation("导出RFID明细(按配置的工序匹配)")
	@GetMapping("exportRfidDetailWithProcess")
	public void exportRfidDetailWithProcess(String barcode,String process,String defect,String startDate,String endDate,
								 HttpServletRequest request,
								 HttpServletResponse response) throws IOException, ParseException{
		QueryWrapper<DfQmsIpqcFlawLogDetail> ew = new QueryWrapper<DfQmsIpqcFlawLogDetail>();
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			ew.gt("log.create_time",startTime);
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			ew.lt("log.create_time",endTime);
		}
		ew
				.like(StringUtils.isNotEmpty(barcode),"bar_code",barcode)
				.like(StringUtils.isNotEmpty(process),"log.process",process)
				.like(StringUtils.isNotEmpty(defect),"flaw_name",defect);
		List<Map> results = dfQmsIpqcFlawLogDetailService.exportRfidDetail(ew);

		//设置表格表头字段
		QueryWrapper<DfProcess> ewProcess = new QueryWrapper<>();
		ewProcess.orderByAsc("sort");
		List<DfProcess> list = dfProcessService.list(ewProcess);
		String[] title = new String[]{"玻璃码", "工序", "缺陷", "扫码时间"};
		String[] properties = new String[]{"barCode", "process", "flawName", "scanningTime"};
		// 获取list的大小
		int size = list.size();
		// 创建一个新的String数组存储process字段
		String[] processes = new String[size];
		// 遍历list，将每个DfProcess对象的process字段添加到processes数组
		for (int i = 0; i < size; i++) {
			processes[i] = list.get(i).getProcessName();
		}
		// 创建一个新的title数组，其大小为原始title数组和processes数组之和
		String[] newTitle = new String[title.length + processes.length];
		String[] newProperties = new String[title.length + processes.length];
		// 首先将title数组元素复制到newTitle数组
		System.arraycopy(title, 0, newTitle, 0, title.length);
		System.arraycopy(properties, 0, newProperties, 0, properties.length);
		// 然后将processes数组元素复制到newTitle数组
		System.arraycopy(processes, 0, newTitle, title.length, processes.length);
		System.arraycopy(processes, 0, newProperties, properties.length, processes.length);
		title = newTitle;
		properties = newProperties;
		for (Map result : results) {
			String processThrough = Optional.ofNullable(result.get("processThrough")).map(Object::toString).orElse("");
			if (StringUtils.isNotEmpty(processThrough)){
				String[] split = processThrough.split("->");
				for (String eachProcess : processes) {
					//字符串中精确匹配(不区分大小写)对应工序
					List<String> collect = Arrays.stream(split).filter(e ->
							e.substring(0,e.indexOf('-')).equalsIgnoreCase(eachProcess)).collect(Collectors.toList());
					//拼接后去掉工序名称
					List<String> removeProcessCollect = collect.stream().map(str -> str.substring(str.indexOf("-") + 1)).collect(Collectors.toList());
					//有多个对应工序用换行符拼接
					result.put(eachProcess,removeProcessCollect.stream().collect(Collectors.joining("\r\n")));
				}
			}
		}

		if (!CollectionUtils.isEmpty(results)) {
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"RFID明细表导出");
		}
	}

}
