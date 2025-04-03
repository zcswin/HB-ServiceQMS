package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfHardRecords;
import com.ww.boengongye.service.DfHardRecordsService;
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
 * 加硬生产记录本 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@RestController
@RequestMapping("/dfHardRecords")
@Api(tags = "加硬生产记录本)")
@CrossOrigin
public class DfHardRecordsController {
	@Autowired
	private DfHardRecordsService dfHardRecordsService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfHardRecords dfHardRecords ) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		dfHardRecords.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (hour >= 7 && hour < 19){
			dfHardRecords.setClassess("A");
		}else if (hour >= 19 || hour < 7){
			dfHardRecords.setClassess("B");
		}
		if (dfHardRecordsService.saveOrUpdate(dfHardRecords)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-id倒序")
	public Result listAll(int page,int limit) {
		Page<DfHardRecords> pages = new Page<DfHardRecords>(page, limit);
		QueryWrapper<DfHardRecords> qw = new QueryWrapper<>();
		qw.orderByDesc("id");
		IPage<DfHardRecords> list = dfHardRecordsService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getById")
	@ApiOperation("根据id查询")
	public Result listAll(int id) {
		return new Result(200,"查询成功",dfHardRecordsService.getById(id));
	}

	@GetMapping(value = "/deleteById")
	@ApiOperation("根据id删除")
	public Result deleteById(int id) {
		if(dfHardRecordsService.removeById(id)){
			return Result.DELETE_SUCCESS;
		}
		else{
			return Result.DELETE_FAILED;
		}
	}

	@GetMapping(value = "/getDataUp")
	@ApiOperation("加硬-上")
	public Result getDataUp(@ApiParam("加工片数") String num
			, String factoryId
			, @ApiParam("项目") String project
			, @ApiParam("颜色")String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfHardRecords> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfHardRecords> qwThisweek = new QueryWrapper<>();
		String selectColumn1 = " sum(if(hard_total < " + num +", 1, 0)) as ok";
		String selectColumn2 = " sum(if(hard_total < " + num +", 0, 1)) as ng";

		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectColumn1,selectColumn2)
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfHardRecords> listLastweek = dfHardRecordsService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekLess = new ArrayList<>();
		ArrayList<Object> yLastweekEqual = new ArrayList<>();
		for (DfHardRecords dfHardRecords : listLastweek) {
			xLastweek.add(dfHardRecords.getDate());
			yLastweekLess.add(dfHardRecords.getOk());
			yLastweekEqual.add(dfHardRecords.getNg());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweekLess",yLastweekLess);
		map.put("yLastweekEqual",yLastweekEqual);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectColumn1,selectColumn2)
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfHardRecords> listThisweek = dfHardRecordsService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweekLess = new ArrayList<>();
		ArrayList<Object> yThisweekEqual = new ArrayList<>();
		for (DfHardRecords dfHardRecords : listThisweek) {
			xThisweek.add(dfHardRecords.getDate());
			yThisweekLess.add(dfHardRecords.getOk());
			yThisweekEqual.add(dfHardRecords.getNg());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweekLess",yThisweekLess);
		map.put("yThisweekEqual",yThisweekEqual);
		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getDataDown")
	@ApiOperation("加硬-下")
	public Result getDataDown(@ApiParam("加工片数") String num
			, String factoryId
			, @ApiParam("项目") String project
			, @ApiParam("颜色")String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfHardRecords> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfHardRecords> qwThisweek = new QueryWrapper<>();
		String selectColumn = "count(*) as ok";

		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectColumn)
				.between("create_time", startTimeLastweek, endTimeLastweek)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfHardRecords> listLastweek = dfHardRecordsService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweek = new ArrayList<>();
		for (DfHardRecords dfHardRecords : listLastweek) {
			xLastweek.add(dfHardRecords.getDate());
			yLastweek.add(dfHardRecords.getOk());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweek",yLastweek);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectColumn)
				.between("create_time", startTimeThisweek, endTimeThisweek)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfHardRecords> listThisweek = dfHardRecordsService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweek = new ArrayList<>();
		for (DfHardRecords dfHardRecords : listThisweek) {
			xThisweek.add(dfHardRecords.getDate());
			yThisweek.add(dfHardRecords.getOk());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweek",yThisweek);
		return new Result(200,"查询成功",map);
	}


}
