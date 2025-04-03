package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfRegrindingProcessCarpet;
import com.ww.boengongye.service.DfRegrindingProcessCarpetService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 返磨工序地毯更换记录 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@RestController
@RequestMapping("/dfRegrindingProcessCarpet")
@Api(tags = "返磨工序地毯更换记录)")
@CrossOrigin
public class DfRegrindingProcessCarpetController {

	@Autowired
	private DfRegrindingProcessCarpetService dfRegrindingProcessCarpetService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfRegrindingProcessCarpet dfRegrindingProcessCarpet ) {
		if (dfRegrindingProcessCarpetService.saveOrUpdate(dfRegrindingProcessCarpet)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-id倒序")
	public Result listAll(int page,int limit,@ApiParam("A || B") String classes) {
		Page<DfRegrindingProcessCarpet> pages = new Page<DfRegrindingProcessCarpet>(page, limit);
		QueryWrapper<DfRegrindingProcessCarpet> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(classes)){
			qw.eq(StringUtils.isNotEmpty(classes), "classes", classes);
		}
		qw.orderByDesc("id");
		IPage<DfRegrindingProcessCarpet> list = dfRegrindingProcessCarpetService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getById")
	@ApiOperation("根据id查询")
	public Result listAll(int id) {
		return new Result(200,"查询成功",dfRegrindingProcessCarpetService.getById(id));
	}

	@GetMapping(value = "/deleteById")
	@ApiOperation("根据id删除")
	public Result deleteById(int id) {
		if(dfRegrindingProcessCarpetService.removeById(id)){
			return Result.DELETE_SUCCESS;
		}
		else{
			return Result.DELETE_FAILED;
		}
	}

	@GetMapping(value = "/getDataUp")
	@ApiOperation("返磨-上")
	public Result getDataUp(@ApiParam("磨皮...") @RequestParam(required = false) String process
			,@ApiParam("加工天数") @RequestParam(required = false) Integer num
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String project
			, @ApiParam("颜色") @RequestParam(required = false) String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfRegrindingProcessCarpet> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfRegrindingProcessCarpet> qwThisweek = new QueryWrapper<>();
		String selectColumn1 = "sum(if(process_day <= "+ num +",1,0)) ok" ;
		String selectColumn2 = "sum(if(process_day <= "+ num +",0,1)) ng" ;

		qwLastweek.select("DATE_FORMAT(DATE_SUB(date,INTERVAL 7 HOUR ),'%m-%d') as x",selectColumn1,selectColumn2)
				.between("date", startTimeLastweek, endTimeLastweek)
				.eq(StringUtils.isNotEmpty("磨皮"), "process",process)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("x")
				.orderByAsc("x");
		List<DfRegrindingProcessCarpet> listLastweek = dfRegrindingProcessCarpetService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweekDate = new ArrayList<>();
		ArrayList<Object> yLastweekOk = new ArrayList<>();
		ArrayList<Object> yLastweekNg = new ArrayList<>();
		for (DfRegrindingProcessCarpet dfRegrindingProcessCarpet : listLastweek) {
			xLastweekDate.add(dfRegrindingProcessCarpet.getX());
			yLastweekOk.add(dfRegrindingProcessCarpet.getOk());
			yLastweekNg.add(dfRegrindingProcessCarpet.getNg());
		}
		map.put("xLastweekDate",xLastweekDate);
		map.put("yLastweekOk",yLastweekOk);
		map.put("yLastweekNg",yLastweekNg);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(date,INTERVAL 7 HOUR ),'%m-%d') as x",selectColumn1,selectColumn2)
				.between("date", startTimeThisweek, endTimeThisweek)
				.eq(StringUtils.isNotEmpty("磨皮"), "process",process)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("x")
				.orderByAsc("x");
		List<DfRegrindingProcessCarpet> listThisweek = dfRegrindingProcessCarpetService.list(qwThisweek);
		ArrayList<Object> xThisweekDate = new ArrayList<>();
		ArrayList<Object> yThisweekOk = new ArrayList<>();
		ArrayList<Object> yThisweekNg = new ArrayList<>();
		for (DfRegrindingProcessCarpet dfRegrindingProcessCarpet : listThisweek) {
			xThisweekDate.add(dfRegrindingProcessCarpet.getX());
			yThisweekOk.add(dfRegrindingProcessCarpet.getOk());
			yThisweekNg.add(dfRegrindingProcessCarpet.getNg());
		}
		map.put("xThisweekDate",xThisweekDate);
		map.put("yThisweekOk",yThisweekOk);
		map.put("yThisweekNg",yThisweekNg);

		return new Result(200,"查询成功",map);
	}


	@GetMapping(value = "/getDataDown")
	@ApiOperation("返磨-下")
	public Result getDataUp(@ApiParam("磨皮...") @RequestParam(required = true) String process
//			,@ApiParam("加工天数") Integer num
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String project
			, @ApiParam("颜色") @RequestParam(required = false) String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfRegrindingProcessCarpet> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfRegrindingProcessCarpet> qwThisweek = new QueryWrapper<>();
//		String selectColumn1 = "sum(if(process_day <= "+ num +",1,0)) ok" ;
//		String selectColumn2 = "sum(if(process_day <- "+ num +",0,1)) ng" ;

		qwLastweek.select("machine_id","count(*) as num")
				.between("date", startTimeLastweek, endTimeThisweek)
				.eq(StringUtils.isNotEmpty("磨皮"), "process",process)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("machine_id")
				.orderByAsc("machine_id");
		List<DfRegrindingProcessCarpet> listLastweek = dfRegrindingProcessCarpetService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> x = new ArrayList<>();
		ArrayList<Object> y = new ArrayList<>();
		for (DfRegrindingProcessCarpet dfRegrindingProcessCarpet : listLastweek) {
			x.add(dfRegrindingProcessCarpet.getMachineId());
			y.add(dfRegrindingProcessCarpet.getNum());
		}
		map.put("x",x);
		map.put("y",y);
		return new Result(200,"查询成功",map);
	}

}
