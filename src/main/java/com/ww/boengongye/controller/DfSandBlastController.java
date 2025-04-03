package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfSandBlast;
import com.ww.boengongye.service.DfSandBlastService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 喷砂记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@RestController
@RequestMapping("/dfSandBlast")
@Api(tags = "喷砂记录表")
@CrossOrigin
public class DfSandBlastController {

	@Autowired
	private DfSandBlastService dfSandBlastService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfSandBlast dfSandBlast) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfSandBlast.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfSandBlast.setClasses("B");
		}
		dfSandBlast.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfSandBlastService.save(dfSandBlast)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page,int limit) { Page<DfSandBlast> pages = new Page<DfSandBlast>(page, limit);
		QueryWrapper<DfSandBlast> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfSandBlast> list = dfSandBlastService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getSandBlastLifetimeByTypeUp")
	@ApiOperation("喷砂-上")
	public Result getSandBlastLifetimeByTypeUp(@ApiParam("M2加砂量,M3加砂量,M2浓度,M3浓度") @RequestParam(required = true) String type
			, String factoryId
			, String model
			, String color) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfSandBlast> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfSandBlast> qwThisweek = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(factoryId)){
			qwLastweek.eq("factory_id", factoryId);
			qwThisweek.eq("factory_id", factoryId);
		}
		if (StringUtils.isNotEmpty(model)){
			qwLastweek.eq("model", model);
			qwThisweek.eq("model", model);
		}
		if (StringUtils.isNotEmpty(color)){
			qwLastweek.eq("color", color);
			qwThisweek.eq("color", color);
		}
		String selectSql = "";
		switch (type){
			case "M2加砂量" :
				selectSql = "COALESCE(sum(sand_addition_m2),0) as ok";
				break;
			case "M3加砂量" :
				selectSql = "COALESCE(sum(sand_addition_m3),0) as ok";
				break;
		}
		DecimalFormat format = new DecimalFormat("#.00");
		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectSql)
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.groupBy("date")
				.orderByAsc("date");
		List<DfSandBlast> listLastweek = dfSandBlastService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekOk = new ArrayList<>();
		for (DfSandBlast dfSandBlast : listLastweek) {
			xLastweek.add(dfSandBlast.getDate());
			yLastweekOk.add(format.format(Double.valueOf(dfSandBlast.getOk())));
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweekOk",yLastweekOk);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectSql)
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.groupBy("date")
				.orderByAsc("date");
		List<DfSandBlast> listThisweek = dfSandBlastService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweekOk = new ArrayList<>();

		for (DfSandBlast dfSandBlast : listThisweek) {
			xThisweek.add(dfSandBlast.getDate());
			yThisweekOk.add(format.format(Double.valueOf(dfSandBlast.getOk())));
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweekOk",yThisweekOk);

		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getSandBlastLifetimeByTypeDwon")
	@ApiOperation("喷砂-下")
	public Result getSandBlastLifetimeByTypeDown(@ApiParam("M2加砂量,M3加砂量,M2浓度,M3浓度") @RequestParam(required = true) String type
			, String factoryId
			, String model
			, String color)  {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfSandBlast> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfSandBlast> qwThisweek = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(factoryId)){
			qwLastweek.eq("factory_id", factoryId);
			qwThisweek.eq("factory_id", factoryId);
		}
		if (StringUtils.isNotEmpty(model)){
			qwLastweek.eq("model", model);
			qwThisweek.eq("model", model);
		}
		if (StringUtils.isNotEmpty(color)){
			qwLastweek.eq("color", color);
			qwThisweek.eq("color", color);
		}
		String sqlColumn = "";
		switch (type){
			case "M2加砂量" :
				sqlColumn = "sand_addition_m2";
				break;
			case "M3加砂量" :
				sqlColumn = "sand_addition_m3";
				break;
		}


		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ","sum(if(" + sqlColumn +" is not null,1,0)) as addition_num")
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.groupBy("date")
				.orderByAsc("addition_num");
		List<DfSandBlast> listLastweek = dfSandBlastService.list(qwLastweek);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweek = new ArrayList<>();
		DecimalFormat format = new DecimalFormat("#.00");
		for (DfSandBlast dfSandBlast : listLastweek) {
			xLastweek.add(dfSandBlast.getDate());
			yLastweek.add(format.format(Double.valueOf(dfSandBlast.getAdditionNum())));
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweek",yLastweek);


		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ","sum(if(" + sqlColumn +" is not null,1,0)) as addition_num")
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.groupBy("date")
				.orderByAsc("addition_num");
		List<DfSandBlast> listThisweek = dfSandBlastService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweek = new ArrayList<>();
		for (DfSandBlast dfSandBlast : listThisweek) {
			xThisweek.add(dfSandBlast.getDate());
			yThisweek.add(format.format(Double.valueOf(dfSandBlast.getAdditionNum())));
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweek",yThisweek);

		return new Result(200,"查询成功",map);
	}


}
