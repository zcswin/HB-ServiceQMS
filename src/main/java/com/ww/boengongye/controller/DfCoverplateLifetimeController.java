package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfCoverplateLifetime;
import com.ww.boengongye.service.DfControlStandardConfigService;
import com.ww.boengongye.service.DfCoverplateLifetimeService;
import com.ww.boengongye.service.impl.ExportDataService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 磨底盖板使用寿命管控 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-07
 */
@RequestMapping("/dfCoverplateLifetime")
@RestController
@Api(tags = "磨底盖板使用寿命管控")
@CrossOrigin
public class DfCoverplateLifetimeController {

	@Autowired
	private DfCoverplateLifetimeService dfCoverplateLifetimeService;
	@Autowired DfControlStandardConfigService dfControlStandardConfigService;
	@Autowired
	ExportDataService ExportDataService;
	@Autowired
	private Environment env;

	@PostMapping(value = "/recordTimes")
	@ApiOperation("记录次数")
	public Result recordTimes(@RequestBody  DfCoverplateLifetime dfCoverplateLifetime) {
		QueryWrapper<DfCoverplateLifetime> qw = new QueryWrapper<>();
		qw.eq("磨底".equals(dfCoverplateLifetime.getType()), "type","磨底");
		qw.eq("磨平台".equals(dfCoverplateLifetime.getType()), "type","磨平台");
		qw.orderByDesc("create_time");
		qw.last("limit 1");
		DfCoverplateLifetime latestOne = dfCoverplateLifetimeService.getOne(qw);
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCoverplateLifetime.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCoverplateLifetime.setClasses("B");
		}
		latestOne.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setUseTime(Integer.parseInt(latestOne.getUseTime()) + Integer.parseInt(dfCoverplateLifetime.getUseTime()) + "");
		if (dfCoverplateLifetimeService.updateById(latestOne)){
			return Result.UPDATE_SUCCESS;
		}else{
			return Result.UPDATE_FAILED;
		}

	}


	@GetMapping(value = "/changePlate")
	@ApiOperation("更换盖板")
	public Result changePlate(int count,@ApiParam("类型(磨底,磨平台)")String type) {
		DfCoverplateLifetime dfCoverplateLifetime = new DfCoverplateLifetime();
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCoverplateLifetime.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCoverplateLifetime.setClasses("B");
		}
		dfCoverplateLifetime.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		dfCoverplateLifetime.setType(type);
		QueryWrapper<DfCoverplateLifetime> qw = new QueryWrapper<DfCoverplateLifetime>();
		qw.eq("磨底".equals(type), "type","磨底");
		qw.eq("磨平台".equals(type), "type","磨平台");
		qw.orderByDesc("create_time");
		qw.last("limit 1");
		DfCoverplateLifetime one = dfCoverplateLifetimeService.getOne(qw);
		if (one == null){
			if (dfCoverplateLifetimeService.save(dfCoverplateLifetime)){
				return Result.INSERT_SUCCESS;
			}else{
				return Result.INSERT_FAILED;
			}

		}
		one.setUseTime(Integer.parseInt(one.getUseTime()) + count +"");
		dfCoverplateLifetimeService.updateById(one);
		dfCoverplateLifetime.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfCoverplateLifetimeService.save(dfCoverplateLifetime)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page, int limit, @ApiParam("类型(磨底,磨平台)") String type) {
		Page<DfCoverplateLifetime> pages = new Page<>(page, limit);
		QueryWrapper<DfCoverplateLifetime> qw = new QueryWrapper<>();
		qw.eq("磨底".equals(type), "type","磨底");
		qw.eq("磨平台".equals(type), "type","磨平台");
		qw.orderByDesc("create_time");
		IPage<DfCoverplateLifetime> list = dfCoverplateLifetimeService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getCovertPlateLifetimeUp")
	@ApiOperation("磨底/磨平台-上")
	public Result getCovertPlateLifetimeUp(@ApiParam("磨底,磨平台...") @RequestParam(required = true) String type
			,String factoryId
			,@ApiParam("17,...") @RequestParam(required = false) String project
			,@ApiParam("颜色") @RequestParam(required = false) String color
			,@ApiParam("批次 (17,...)") @RequestParam(required = false) Integer batch) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCoverplateLifetime> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCoverplateLifetime> qwThisweek = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(factoryId)){
			qwLastweek.eq("factory_id", factoryId);
			qwThisweek.eq("factory_id", factoryId);
		}
		if (StringUtils.isNotEmpty(project)){
			qwLastweek.eq("project", project);
			qwThisweek.eq("project", project);
		}
		if (StringUtils.isNotEmpty(color)){
			qwLastweek.eq("color", color);
			qwThisweek.eq("color", color);
		}
		String sqlColumn1 = "";
		String sqlColumn2 = "";
		switch (type){
			case "磨底" :
				qwLastweek.eq("type", "磨底");
				qwThisweek.eq("type", "磨底");
				sqlColumn1 = "SUM(IF(use_time < " + batch +" ,1,0)) as less";
				sqlColumn2 = "SUM(IF(use_time < " + batch +" ,0,1)) as equal";
				break;
			case "磨平台" :
				qwLastweek.eq("type", "磨平台");
				qwThisweek.eq("type", "磨平台");
				sqlColumn1 = "SUM(IF(use_time < " + batch +" ,1,0)) as less";
				sqlColumn2 = "SUM(IF(use_time < " + batch +" ,0,1)) as equal";
				break;
		}

		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",sqlColumn1,sqlColumn2)
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.groupBy("date")
				.orderByAsc("date");
		List<DfCoverplateLifetime> listLastweek = dfCoverplateLifetimeService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekLess = new ArrayList<>();
		ArrayList<Object> yLastweekEqual = new ArrayList<>();
		for (DfCoverplateLifetime dfCoverplateLifetime : listLastweek) {
			xLastweek.add(dfCoverplateLifetime.getDate());
			yLastweekLess.add(dfCoverplateLifetime.getLess());
			yLastweekEqual.add(dfCoverplateLifetime.getEqual());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweekLess",yLastweekLess);
		map.put("yLastweekEqual",yLastweekEqual);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",sqlColumn1,sqlColumn2)
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.groupBy("date")
				.orderByAsc("date");
		List<DfCoverplateLifetime> listThisweek = dfCoverplateLifetimeService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweekLess = new ArrayList<>();
		ArrayList<Object> yThisweekEqual = new ArrayList<>();
		for (DfCoverplateLifetime dfCoverplateLifetime : listThisweek) {
			xThisweek.add(dfCoverplateLifetime.getDate());
			yThisweekLess.add(dfCoverplateLifetime.getLess());
			yThisweekEqual.add(dfCoverplateLifetime.getEqual());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweekLess",yThisweekLess);
		map.put("yThisweekEqual",yThisweekEqual);

		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getCovertPlateLifetimeDown")
	@ApiOperation("磨底/磨平台-下")
	public Result getCovertPlateLifetimeDown(@ApiParam("磨底,磨平台...") @RequestParam(required = true) String type
			,String factoryId
			,@ApiParam("17,...") @RequestParam(required = false) String project
			,@ApiParam("颜色") @RequestParam(required = false) String color) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCoverplateLifetime> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCoverplateLifetime> qwThisweek = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(factoryId)){
			qwLastweek.eq("factory_id", factoryId);
			qwThisweek.eq("factory_id", factoryId);
		}
		if (StringUtils.isNotEmpty(project)){
			qwLastweek.eq("project", project);
			qwThisweek.eq("project", project);
		}
		if (StringUtils.isNotEmpty(color)){
			qwLastweek.eq("color", color);
			qwThisweek.eq("color", color);
		}
		String sqlColumn1 = "";
		switch (type){
			case "磨底" :
				qwLastweek.eq("type", "磨底");
				qwThisweek.eq("type", "磨底");
				sqlColumn1 = "count(*) as count";
				break;
			case "磨平台" :
				qwLastweek.eq("type", "磨平台");
				qwThisweek.eq("type", "磨平台");
				sqlColumn1 = "count(*) as count";
				break;
		}

		qwLastweek.select("machine_id",sqlColumn1)
				.between("create_time", startTimeLastweek, endTimeThisweek)
				.groupBy("machine_id")
				.orderByAsc("machine_id");
		List<DfCoverplateLifetime> listLastweek = dfCoverplateLifetimeService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekCount = new ArrayList<>();
		for (DfCoverplateLifetime dfCoverplateLifetime : listLastweek) {
			xLastweek.add(dfCoverplateLifetime.getMachineId());
			yLastweekCount.add(dfCoverplateLifetime.getCount());
		}
		map.put("x",xLastweek);
		map.put("y",yLastweekCount);

		return new Result(200,"查询成功",map);
	}


}
