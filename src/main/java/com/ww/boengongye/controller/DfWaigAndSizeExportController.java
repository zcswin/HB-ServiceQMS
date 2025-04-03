package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetail;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.mapper.DfQmsIpqcWaigTotalMapper;
import com.ww.boengongye.mapper.DfSizeDetailMapper;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.utils.Excel;
import com.ww.boengongye.utils.ExcelExportUtil2;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liwei
 * @create 2024-06-18 14:59
 */
@Controller
@RequestMapping("/dfWaigAndSizeExportController")
@ResponseBody
@CrossOrigin
@Api(tags = "尺寸外观数据导出")
public class DfWaigAndSizeExportController {

	@Autowired
	private DfSizeDetailService dfSizeDetailService;

	@Autowired
	private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

	@Autowired
	private DfAuditDetailService dfAuditDetailService;

	@RequestMapping(value = "exportInspectionTableForProcess",method = RequestMethod.GET)
	@ApiOperation("导出工序抽检数量表")
	public void exportInspectionTableForProcess( String startDate,
												 String endDate,
												 String type,//尺寸,外观
												 String project,
												 String process,
												HttpServletRequest request,
												HttpServletResponse response) throws IOException, ParseException {
		QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper = null;
		QueryWrapper<DfSizeDetail> sizeWrapper = null;

		//类型
		if(StringUtils.isEmpty(type)){
			// 如果type为null，那么创建两个QueryWrapper
			waigWrapper = new QueryWrapper<>();
			sizeWrapper = new QueryWrapper<>();
			sizeWrapper.groupBy("date");
			waigWrapper.groupBy("date");
		} else {
			// 根据type值创建对应的QueryWrapper
			if("尺寸".equals(type)){
				sizeWrapper = new QueryWrapper<>();
				sizeWrapper.groupBy("date");
			} else if("外观".equals(type)){
				waigWrapper = new QueryWrapper<>();
				waigWrapper.groupBy("date");
			}
		}
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.gt("test_time",startTime);
			}
			if (waigWrapper != null){
				waigWrapper.gt("f_time",startTime);
			}
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.lt("test_time",endTime);
			}
			if (waigWrapper != null){
				waigWrapper.lt("f_time",endTime);
			}
		}
		//项目
		if (sizeWrapper != null){
			sizeWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
				sizeWrapper.eq("project",project);
			}
		}
		if (waigWrapper != null){
				waigWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
			waigWrapper.eq("f_bigpro",project);
			}
		}
		//工序
		if (sizeWrapper != null) {
			sizeWrapper.groupBy("process");
			if (StringUtils.isNotEmpty(process)) {
				sizeWrapper.eq("process", process);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("process");
			if(StringUtils.isNotEmpty(process)){
				waigWrapper.eq("f_seq",process);
			}
		}
//		//机台
//		if(StringUtils.isNotEmpty(machineCode)){
//			if (sizeWrapper != null){
//				sizeWrapper.eq("machine_code",machineCode);
////				sizeWrapper.groupBy("machineCode");
//			}
//			if (waigWrapper != null){
//				waigWrapper.eq("f_mac",machineCode);
////				waigWrapper.groupBy("machineCode");
//			}
//		}

		List<Map<String,Object>> sizeResults = null;
		List<Map<String,Object>> waigResults = null;
		if (sizeWrapper != null){
			sizeResults = dfSizeDetailService.exportInspectionTableForProcess(sizeWrapper);
		}

		if (waigWrapper != null){
			waigResults = dfQmsIpqcWaigTotalService.exportInspectionTableForProcess(waigWrapper);
		}

		List<Map> results = new ArrayList<>();
		if (!CollectionUtils.isEmpty(sizeResults)){
			results.addAll(sizeResults);
		}
		if (!CollectionUtils.isEmpty(waigResults)){
			results.addAll(waigResults);
		}

		results.stream().forEach(map -> map.computeIfPresent("qa", (k, v) -> v + "%"));

		if (!CollectionUtils.isEmpty(results)) {

			//设置表格表头字段
			String[] title = new String[]{"项目", "类型", "工序", "时间", "抽检数量",
					"NG数量", "良率"};

			// 查询对应的字段
			String[] properties = new String[]{"project", "type", "process", "date",
					"total", "ng", "qa"};
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"工序抽检数量表");
		}
	}

	@RequestMapping(value = "exportInspectionTableForProcessBymachineId",method = RequestMethod.GET)
	@ApiOperation("导出机台抽检数量表")
	public void exportInspectionTableForProcessBymachineId( String startDate,
												 String endDate,
												 String type,//尺寸,外观
												 String project,
												  String process,
												  String machineCode,
												HttpServletRequest request,
												HttpServletResponse response) throws IOException, ParseException {
		QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper = null;
		QueryWrapper<DfSizeDetail> sizeWrapper = null;

		//类型
		if(StringUtils.isEmpty(type)){
			// 如果type为null，那么创建两个QueryWrapper
			waigWrapper = new QueryWrapper<>();
			sizeWrapper = new QueryWrapper<>();
			sizeWrapper.groupBy("date");
			waigWrapper.groupBy("date");
		} else {
			// 根据type值创建对应的QueryWrapper
			if("尺寸".equals(type)){
				sizeWrapper = new QueryWrapper<>();
				sizeWrapper.groupBy("date");
			} else if("外观".equals(type)){
				waigWrapper = new QueryWrapper<>();
				waigWrapper.groupBy("date");
			}
		}
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.gt("test_time",startTime);
			}
			if (waigWrapper != null){
				waigWrapper.gt("f_time",startTime);
			}
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.lt("test_time",endTime);
			}
			if (waigWrapper != null){
				waigWrapper.lt("f_time",endTime);
			}
		}
		//项目
		if (sizeWrapper != null){
			sizeWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
				sizeWrapper.eq("project",project);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
				waigWrapper.eq("f_bigpro",project);
			}
		}
		//工序
		if (sizeWrapper != null) {
			sizeWrapper.groupBy("process");
			if (StringUtils.isNotEmpty(process)) {
				sizeWrapper.eq("process", process);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("process");
			if(StringUtils.isNotEmpty(process)){
				waigWrapper.eq("f_seq",process);
			}
		}
		//机台
		if (sizeWrapper != null) {
			sizeWrapper.groupBy("machineCode");
			if (StringUtils.isNotEmpty(machineCode)) {
				sizeWrapper.eq("machine_code", machineCode);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("machineCode");
			if (StringUtils.isNotEmpty(machineCode)) {
				waigWrapper.eq("f_mac",machineCode);
			}
		}

		List<Map<String,Object>> sizeResults = null;
		List<Map<String,Object>> waigResults = null;
		if (sizeWrapper != null){
			sizeResults = dfSizeDetailService.exportInspectionTableForProcessBymachineId(sizeWrapper);
		}

		if (waigWrapper != null){
			waigResults = dfQmsIpqcWaigTotalService.exportInspectionTableForProcessBymachineId(waigWrapper);
		}

		List<Map> results = new ArrayList<>();
		if (!CollectionUtils.isEmpty(sizeResults)){
			results.addAll(sizeResults);
		}
		if (!CollectionUtils.isEmpty(waigResults)){
			results.addAll(waigResults);
		}

		results.stream().forEach(map -> map.computeIfPresent("qa", (k, v) -> v + "%"));

		if (!CollectionUtils.isEmpty(results)) {

			//设置表格表头字段
			String[] title = new String[]{"项目", "类型", "工序","机台", "时间", "抽检数量",
					"NG数量", "良率"};

			// 查询对应的字段
			String[] properties = new String[]{"project", "type", "process","machineCode", "date",
					"total", "ng", "qa"};
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"机台抽检数量表");
		}
	}

	@RequestMapping(value = "exportNgClassificationScale",method = RequestMethod.GET)
	@ApiOperation("导出NG分类数量表")
	public void exportNgClassificationScale( String startDate,
												  String endDate,
												  String type,//尺寸,外观
												  String project,
												  String process,
												HttpServletRequest request,
												HttpServletResponse response) throws IOException, ParseException {
		QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper = null;
		QueryWrapper<DfSizeDetail> sizeWrapper = null;

		//类型
		if(StringUtils.isEmpty(type)){
			// 如果type为null，那么创建两个QueryWrapper
			waigWrapper = new QueryWrapper<>();
			sizeWrapper = new QueryWrapper<>();
			sizeWrapper.groupBy("date","item");
			waigWrapper.groupBy("date","item");
		} else {
			// 根据type值创建对应的QueryWrapper
			if("尺寸".equals(type)){
				sizeWrapper = new QueryWrapper<>();
				sizeWrapper.groupBy("date","item");
			} else if("外观".equals(type)){
				waigWrapper = new QueryWrapper<>();
				waigWrapper.groupBy("date","item");
			}
		}
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.gt("test_time",startTime);
			}
			if (waigWrapper != null){
				waigWrapper.gt("df_qms_ipqc_waig_detail.f_time",startTime);
			}
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			if (sizeWrapper != null){
				sizeWrapper.lt("test_time",endTime);
			}
			if (waigWrapper != null){
				waigWrapper.lt("df_qms_ipqc_waig_detail.f_time",endTime);
			}
		}
		//项目
		if (sizeWrapper != null){
			sizeWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
				sizeWrapper.eq("project",project);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("project");
			if(StringUtils.isNotEmpty(project)){
				waigWrapper.eq("f_bigpro",project);
			}
		}
		//工序
		if (sizeWrapper != null) {
			sizeWrapper.groupBy("process");
			if (StringUtils.isNotEmpty(process)) {
				sizeWrapper.eq("process", process);
			}
		}
		if (waigWrapper != null){
			waigWrapper.groupBy("process");
			if(StringUtils.isNotEmpty(process)){
				waigWrapper.eq("f_seq",process);
			}
		}
//		//机台
//		if(StringUtils.isNotEmpty(machineCode)){
//			if (sizeWrapper != null){
//				sizeWrapper.eq("machine_code",machineCode);
////				sizeWrapper.groupBy("machineCode");
//			}
//			if (waigWrapper != null){
//				waigWrapper.eq("f_mac",machineCode);
////				waigWrapper.groupBy("machineCode");
//			}
//		}

		List<Map<String,Object>> sizeResults = null;
		List<Map<String,Object>> waigResults = null;
		if (sizeWrapper != null){
			sizeResults = dfSizeDetailService.exportNgClassificationScale(sizeWrapper);
		}

		if (waigWrapper != null){
			waigResults = dfQmsIpqcWaigTotalService.exportNgClassificationScale(waigWrapper);
		}

		List<Map> results = new ArrayList<>();
		if (!CollectionUtils.isEmpty(sizeResults)){
			results.addAll(sizeResults);
		}
		if (!CollectionUtils.isEmpty(waigResults)){
			results.addAll(waigResults);
		}

		results.stream().forEach(map -> map.computeIfPresent("qa", (k, v) -> v + "%"));

		if (!CollectionUtils.isEmpty(results)) {

			//设置表格表头字段
			String[] title = new String[]{"项目", "类型", "工序", "NG类型", "时间",
					"NG数量", "NG占比"};

			// 查询对应的字段
			String[] properties = new String[]{"project", "type", "process", "item",
					"date", "ng", "qa"};
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"NG分类数量表");
		}
	}


	@RequestMapping(value = "exportAuditNgDetail",method = RequestMethod.GET)
	@ApiOperation("导出稽查NG明细表")
	public void exportAuditNgDetail(  String startDate,
												  String endDate,
												  String type,//尺寸,外观
												  String project,
												  String process,
												HttpServletRequest request,
												HttpServletResponse response) throws IOException, ParseException {
		QueryWrapper<DfAuditDetail> wrapper = new QueryWrapper<>();
		//todo 添加 筛选NG
		//类型
		if(StringUtils.isEmpty(type)){
			wrapper.and(i -> i.eq("data_type","尺寸").or().eq("data_type","外观"));
		} else {
			// 根据type值创建对应的QueryWrapper
			if("尺寸".equals(type)){
				wrapper.eq("data_type","尺寸");
			} else if("外观".equals(type)){
				wrapper.eq("data_type","外观");
			}
		}
		//开始日期
		if(StringUtils.isNotEmpty(startDate)){
			String startTime = startDate + " 07:00:00";
			wrapper.gt("report_time",startTime);
		}
		//结束日期
		if(StringUtils.isNotEmpty(endDate)){
			String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
			wrapper.lt("report_time",endTime);
		}
		//项目
		if(StringUtils.isNotEmpty(project)){
			wrapper.eq("project_name",project);
		}
		//工序
		if (StringUtils.isNotEmpty(process)) {
			wrapper.eq("process", process);
		}

		List<Map> results = dfAuditDetailService.exportAuditNgDetail(wrapper);

		if (!CollectionUtils.isEmpty(results)) {

			//设置表格表头字段
			String[] title = new String[]{"项目", "工序", "机台", "NG类型", "NG时间",
					"FA", "CA"};

			// 查询对应的字段
			String[] properties = new String[]{"processName", "process", "macCode", "data_type",
					"reportTime", "fa", "ca"};
			ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
			excelExport2.setData(results);
			excelExport2.setHeardKey(properties);
			excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
			excelExport2.setHeardList(title);
			excelExport2.exportExportWithFileName(request, response,"NG明细表");
		}
	}

}
