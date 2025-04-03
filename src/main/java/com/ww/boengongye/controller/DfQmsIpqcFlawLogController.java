package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfQmsIpqcFlawLog;
import com.ww.boengongye.entity.DfQmsIpqcFlawLogDetail;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.service.DfQmsIpqcFlawLogDetailService;
import com.ww.boengongye.service.DfQmsIpqcFlawLogService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2024-06-21
 */
@Api(tags = "RFID工序流转记录")
@RestController
@RequestMapping("/dfQmsIpqcFlawLog")
@CrossOrigin
public class DfQmsIpqcFlawLogController {

	@Autowired
	private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;
	@Autowired
	private DfAuditDetailService dfAuditDetailService;
	@Autowired
	private DfQmsIpqcFlawLogService dfQmsIpqcFlawLogService;
	@Autowired
	private DfQmsIpqcFlawLogDetailService dfQmsIpqcFlawLogDetailService;

	@Autowired
	private Environment env;

	/**
	 * POST请求
	 * 接收后
	 * 1:前端的信息存入df_qms_ipqc_flaw_log表,
	 * 2:根据玻璃码调用RFID 接口,把rfid接口的数据存入df_qms_ipqc_flaw_log_detail
	 * fa,ca 字段根据玻璃码去df_qms_ipqc_waig_total 表 模糊查询f_barcode 字段,然后根据id 找 df_audit_detail
	 * 对应data_type=外观 parent_id=df_qms_ipqc_waig_total.id 的FA ,CA
	 * @param dfQmsIpqcFlawLog
	 */
	@ApiOperation("保存RFID工序流转记录")
	@PostMapping("saveLogs")
	@Transactional
	public Result saveLogs(@RequestBody DfQmsIpqcFlawLog dfQmsIpqcFlawLog){
		String barCode = dfQmsIpqcFlawLog.getBarCode();
		QueryWrapper<DfQmsIpqcWaigTotal> ewWaig = new QueryWrapper<>();
		//这里可能要改成右匹配 或者 全文索引
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneWeekBefore = now.minusWeeks(1);
		ewWaig
				.between("f_time",oneWeekBefore,now)
				.like("f_barcode" , barCode)
				.orderByDesc("f_time");
		List<DfQmsIpqcWaigTotal> listWaig = dfQmsIpqcWaigTotalService.list(ewWaig);
		if (CollectionUtils.isNotEmpty(listWaig)){
			Integer id = listWaig.get(0).getId();
			QueryWrapper<DfAuditDetail> ewAudit = new QueryWrapper<>();
			ewAudit.eq("data_type","外观")
					.eq("parent_id",id)
					.orderByDesc("report_time");
			List<DfAuditDetail> listAudit = dfAuditDetailService.list(ewAudit);
			if (CollectionUtils.isNotEmpty(listAudit)){
				dfQmsIpqcFlawLog.setFa(listAudit.get(0).getFa());
				dfQmsIpqcFlawLog.setCa(listAudit.get(0).getCa());
			}
		}
		dfQmsIpqcFlawLogService.save(dfQmsIpqcFlawLog);

		//RFID
		Integer flawLogId = dfQmsIpqcFlawLog.getId();

//		String url = "http://10.77.31.200/product-resume-biel/jiaTai/open/queryVb";
//		测试用接口,测试方法在MyTestController

//		String url = "http://localhost:15005/test/getData";
		String url = env.getProperty("FindVbCodeAPI");
		Map<String, String> headers = new HashMap<>();
		HashMap<Object, Object> map = new HashMap<>();
		map.put("vbCode",dfQmsIpqcFlawLog.getBarCode());
		RFIDResult2 rfidResult2 = new Gson().fromJson(HttpUtil.postJson(url, null, headers,
				JSONObject.toJSONString(map), false), RFIDResult2.class);
		List<RFIDRecord> data = rfidResult2.getData();
		ArrayList<DfQmsIpqcFlawLogDetail> collect = new ArrayList<DfQmsIpqcFlawLogDetail>();
		for (RFIDRecord record : data) {
			String procedureName = record.getProcedureName();
			String operateTime = record.getOperateTime();
			String procedureStepName = record.getProcedureStepName();
			String machineCode = record.getMachineCode();
			DfQmsIpqcFlawLogDetail dfQmsIpqcFlawLogDetail = new DfQmsIpqcFlawLogDetail();

			dfQmsIpqcFlawLogDetail.setParentId(flawLogId);
			dfQmsIpqcFlawLogDetail.setProcess(procedureName);
			if (StringUtils.isNotEmpty(operateTime)){
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime localDateTime = LocalDateTime.parse(operateTime, formatter);
					dfQmsIpqcFlawLogDetail.setProductTime(localDateTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			dfQmsIpqcFlawLogDetail.setClampCode(procedureStepName);
			dfQmsIpqcFlawLogDetail.setMachineCode(machineCode);
			collect.add(dfQmsIpqcFlawLogDetail);
		}

		dfQmsIpqcFlawLogDetailService.saveBatch(collect);
		return new Result(200, "操作成功");
	}

	/**
	 * Get请求
	 * 查询接口,分页查询,可选参数(玻璃码,工序,缺陷,开始时间,结束时间)返回
	 */
	@ApiOperation("按条件查询主表记录")
	@GetMapping("getMainList")
	public Result getMainList( @RequestParam(required = true) int page,
							   @RequestParam(required = true) int limit,
							   @RequestParam(required = false) @ApiParam("玻璃码") String barcode,
							   @RequestParam(required = false) @ApiParam("工序") String process,
							   @RequestParam(required = false) @ApiParam("缺陷") String defect,
							   @RequestParam(required = false) @ApiParam("开始日期") String startDate,
							   @RequestParam(required = false) @ApiParam("结束日期") String endDate) throws ParseException {
		Page<DfQmsIpqcFlawLog> pages = new Page<DfQmsIpqcFlawLog>(page, limit);
		QueryWrapper<DfQmsIpqcFlawLog> ew = new QueryWrapper<DfQmsIpqcFlawLog>();
		if (StringUtils.isNotEmpty(startDate)){
			//
			ew.gt("create_time",startDate + " 07:00:00");
		}
		if (StringUtils.isNotEmpty(endDate)){
			ew.le("create_time",TimeUtil.getNextDay(endDate) + " 07:00:00");
		}
		ew
				.like(StringUtils.isNotEmpty(barcode),"bar_code",barcode)
				.like(StringUtils.isNotEmpty(process),"process",process)
				.like(StringUtils.isNotEmpty(defect),"flaw_name",defect);
		ew.orderByDesc("create_time");
		IPage<DfQmsIpqcFlawLog> list = dfQmsIpqcFlawLogService.page(pages, ew);
		return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
	}




}
