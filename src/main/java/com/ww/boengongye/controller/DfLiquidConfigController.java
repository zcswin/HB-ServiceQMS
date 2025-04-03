package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiquidConfig;
import com.ww.boengongye.service.DfLiquidConfigService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 液抛参数记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@RestController
@RequestMapping("/dfLiquidConfig")
@CrossOrigin
@Api(tags = "液抛参数记录表")
public class DfLiquidConfigController {
	@Autowired
	private DfLiquidConfigService dfLiquidConfigService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfLiquidConfig dfLiquidConfig) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfLiquidConfig.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfLiquidConfig.setClasses("B");
		}
		dfLiquidConfig.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfLiquidConfigService.save(dfLiquidConfig)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page,int limit) {
		Page<DfLiquidConfig> pages = new Page<DfLiquidConfig>(page, limit);
		QueryWrapper<DfLiquidConfig> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfLiquidConfig> list = dfLiquidConfigService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getLiquidConfigLifetimeUp")
	@ApiOperation("液抛-上")
	public Result getLiquidConfigLifetimeUp(@ApiParam("N葡,...") @RequestParam(required = true) String type
			, @ApiParam("17,...") @RequestParam(required = true) int batch
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String model
			, String color) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfLiquidConfig> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfLiquidConfig> qwThisweek = new QueryWrapper<>();
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
			case "N葡" :
				qwLastweek.and(qw -> qw.eq("material", "NaOH")
						.or()
						.eq("material", "葡萄糖酸钠"));
				qwThisweek.and(qw -> qw.eq("material", "NaOH")
						.or()
						.eq("material", "葡萄糖酸钠"));
				break;
		}

		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date "
				,"SUM(IF(batch < " + batch +" ,1,0)) as less","SUM(IF(batch < " + batch +" ,0,1)) as equal")
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.groupBy("date");
		List<DfLiquidConfig> listLastweek = dfLiquidConfigService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekLess = new ArrayList<>();
		ArrayList<Object> yLastweekEqual = new ArrayList<>();
		for (DfLiquidConfig dfLiquidConfig : listLastweek) {
			xLastweek.add(dfLiquidConfig.getDate());
			yLastweekLess.add(dfLiquidConfig.getLess());
			yLastweekEqual.add(dfLiquidConfig.getEqual());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweekLess",yLastweekLess);
		map.put("yLastweekEqual",yLastweekEqual);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date "
				,"SUM(IF(batch < " + batch +" ,1,0)) as less","SUM(IF(batch < " + batch +" ,0,1)) as equal")
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.groupBy("date");
		List<DfLiquidConfig> listThisweek = dfLiquidConfigService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweekLess = new ArrayList<>();
		ArrayList<Object> yThisweekEqual = new ArrayList<>();
		for (DfLiquidConfig dfLiquidConfig : listThisweek) {
			xThisweek.add(dfLiquidConfig.getDate());
			yThisweekLess.add(dfLiquidConfig.getLess());
			yThisweekEqual.add(dfLiquidConfig.getEqual());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweekLess",yThisweekLess);
		map.put("yThisweekEqual",yThisweekEqual);

		return new Result(200,"查询成功",map);
	}


	@GetMapping(value = "/getLiquidConfigLifetimeDown")
	@ApiOperation("液抛-下")
	public Result getLiquidConfigLifetimeDown(@ApiParam("N葡,...") @RequestParam(required = true) String type
			, @ApiParam("17,...") @RequestParam(required = true) int batch
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String model
			, String color) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfLiquidConfig> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfLiquidConfig> qwThisweek = new QueryWrapper<>();
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
			case "N葡" :
				qwLastweek.and(qw -> qw.eq("material", "NaOH")
						.or()
						.eq("material", "葡萄糖酸钠"));
				qwThisweek.and(qw -> qw.eq("material", "NaOH")
						.or()
						.eq("material", "葡萄糖酸钠"));
				break;
		}

		qwLastweek.select("count(*) as count ","potion_tank_code")
				.between("create_time", startTimeLastweek, endTimeThisweek)
				.groupBy("potion_tank_code")
				.orderByAsc("potion_tank_code");
		List<DfLiquidConfig> listLastweek = dfLiquidConfigService.list(qwLastweek);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> x = new ArrayList<>();
		ArrayList<Object> y= new ArrayList<>();
		for (DfLiquidConfig dfLiquidConfig : listLastweek) {
			x.add(dfLiquidConfig.getPotionTankCode());
			y.add(dfLiquidConfig.getCount());
		}
		map.put("x",x);
		map.put("y",y);
		return new Result(200,"查询成功",map);
	}


}
