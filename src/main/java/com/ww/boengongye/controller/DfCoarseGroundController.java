package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfCoarseGround;
import com.ww.boengongye.service.DfCoarseGroundService;
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
 * 粗磨,磨皮 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@RestController
@RequestMapping("/dfCoarseGround")
@Api(tags = "粗磨,磨皮")
@CrossOrigin
public class DfCoarseGroundController {
	@Autowired
	private DfCoarseGroundService dfCoarseGroundService;

	@GetMapping(value = "/recordTimes")
	@ApiOperation("记录次数(创建新的一条)")
	public Result recordTimes(String classess,Integer num) {
		DfCoarseGround dfCoarseGround = new DfCoarseGround();
		dfCoarseGround.setClasses(classess)
				.setUseTime(num)
				.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setReplaceTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType("磨皮");
		if(dfCoarseGroundService.saveOrUpdate(dfCoarseGround)){
			return Result.INSERT_SUCCESS;
		}else {
			return Result.INSERT_FAILED;
		}
	}


	@GetMapping(value = "/changePlate")
	@ApiOperation("更换盖板")
	public Result changePlate(int count,String type,String classess) {
		DfCoarseGround dfCoarseGround = new DfCoarseGround();
//		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
//		if (hour >= 7 && hour < 19){
//			dfCoarseGround.setClasses("A");
//		}else if (hour >= 19 || hour < 7){
//			dfCoarseGround.setClasses("B");
//		}
		dfCoarseGround.setReplaceTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType(type);
		QueryWrapper<DfCoarseGround> qw = new QueryWrapper<DfCoarseGround>();
		qw.eq("磨皮".equals(type), "type","磨皮");
		qw.eq("classes",classess);
		qw.orderByDesc("id");
		qw.last("limit 1");
		DfCoarseGround one = dfCoarseGroundService.getOne(qw);
		if (one == null){
			if (dfCoarseGroundService.save(dfCoarseGround)){
				return Result.INSERT_SUCCESS;
			}else{
				return Result.INSERT_FAILED;
			}

		}
		one.setUseTime(one.getUseTime() + count);
		dfCoarseGroundService.updateById(one);
		dfCoarseGround.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfCoarseGroundService.save(dfCoarseGround)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page, int limit,String classess) {
		Page<DfCoarseGround> pages = new Page<>(page, limit);
		QueryWrapper<DfCoarseGround> qw = new QueryWrapper<>();
		qw.orderByDesc("id");
		qw.eq(StringUtils.isNotEmpty(classess),"classes",classess);
		IPage<DfCoarseGround> list = dfCoarseGroundService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getDataUp")
	@ApiOperation("粗磨-上")
	public Result getDataUp(@ApiParam("4um菱形盘磨皮(上盘),磨皮(下盘),胶模,研磨液...") @RequestParam(required = true) String type
//			, Double thick
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String project
			, @ApiParam("颜色") @RequestParam(required = false) String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCoarseGround> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCoarseGround> qwThisweek = new QueryWrapper<>();
		String selectSql = "count(*) as num" ;
//		if ("4um菱形盘磨皮(上盘)".equals(type) || "磨皮(下盘)".equals(type) || "胶模".equals(type)){
//			selectSql = "count(*) as project";
//		}else if ("研磨液".equals(type)){
//			selectSql ="";
//		}
		qwLastweek.select("DATE_FORMAT(DATE_SUB(replace_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectSql)
				.between("replace_time", startTimeLastweek, endTimeLastweek)
				.eq(StringUtils.isNotEmpty(type), "process",type)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfCoarseGround> listLastweek = dfCoarseGroundService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweekDate = new ArrayList<>();
		ArrayList<Object> yLastweek = new ArrayList<>();
		for (DfCoarseGround DfCoarseGround : listLastweek) {
			xLastweekDate.add(DfCoarseGround.getDate());
			yLastweek.add(DfCoarseGround.getNum());
		}
		map.put("xLastweekDate",xLastweekDate);
		map.put("xLastweek",yLastweek);

		qwThisweek.select("DATE_FORMAT(DATE_SUB(replace_time,INTERVAL 7 HOUR ),'%m-%d') as date ",selectSql)
				.between("replace_time", startTimeThisweek, endTimeThisweek)
				.eq(StringUtils.isNotEmpty(type), "process",type)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
				.eq(StringUtils.isNotEmpty(project), "project", project)
				.eq(StringUtils.isNotEmpty(color), "color", color)
				.groupBy("date")
				.orderByAsc("date");
		List<DfCoarseGround> listThisweek = dfCoarseGroundService.list(qwThisweek);
		ArrayList<Object> xThisweekDate = new ArrayList<>();
		ArrayList<Object> yThisweek = new ArrayList<>();
		for (DfCoarseGround DfCoarseGround : listThisweek) {
			xThisweekDate.add(DfCoarseGround.getDate());
			yThisweek.add(DfCoarseGround.getNum());
		}
		map.put("xThisweekDate",xThisweekDate);
		map.put("yThisweek",yThisweek);

		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getDataDown")
	@ApiOperation("粗磨-下")
	public Result getDataDown(@ApiParam("4um菱形盘磨皮(上盘),磨皮(下盘),胶模,研磨液...") @RequestParam(required = true) String type
			, Double thick
			, String factoryId
			, @ApiParam("项目") @RequestParam(required = false) String project
			, @ApiParam("颜色") @RequestParam(required = false) String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCoarseGround> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCoarseGround> qwThisweek = new QueryWrapper<>();
		String selectSql = "count(*) as num" ;
		HashMap<Object, Object> map = new HashMap<>();
//		if ("4um菱形盘磨皮(上盘)".equals(type) || "磨皮(下盘)".equals(type) || "胶模".equals(type)){
//			selectSql = "count(*) as project";
//		}else if ("研磨液".equals(type)){
//			selectSql ="";
//		}
		if ("研磨液".equals(type)){
			qwThisweek.select("line_body",selectSql)
					.between("replace_time", startTimeLastweek, endTimeThisweek)
					.eq(StringUtils.isNotEmpty(type), "process",type)
					.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
					.eq(StringUtils.isNotEmpty(project), "project", project)
					.eq(StringUtils.isNotEmpty(color), "color", color)
					.groupBy("line_body")
					.orderByAsc("line_body");
			List<DfCoarseGround> listThisweek = dfCoarseGroundService.list(qwThisweek);
			ArrayList<Object> xThisweekLineBody = new ArrayList<>();
			ArrayList<Object> yThisweek = new ArrayList<>();
			for (DfCoarseGround DfCoarseGround : listThisweek) {
				xThisweekLineBody.add(DfCoarseGround.getLineBody());
				yThisweek.add(DfCoarseGround.getNum());
			}
			map.put("x",xThisweekLineBody);
			map.put("y",yThisweek);

			return new Result(200,"查询成功",map);
		}
		else{
			qwLastweek.select("machine_id ",selectSql)
					.between("replace_time", startTimeLastweek, endTimeThisweek)
					.eq(StringUtils.isNotEmpty(type), "process",type)
					.eq(StringUtils.isNotEmpty(factoryId), "factory_id", factoryId)
					.eq(StringUtils.isNotEmpty(project), "project", project)
					.eq(StringUtils.isNotEmpty(color), "color", color)
					.groupBy("machine_id")
					.orderByAsc("machine_id");
			List<DfCoarseGround> listLastweek = dfCoarseGroundService.list(qwLastweek);

			ArrayList<Object> x = new ArrayList<>();
			ArrayList<Object> y = new ArrayList<>();
			for (DfCoarseGround DfCoarseGround : listLastweek) {
				x.add(DfCoarseGround.getMachineId());
				y.add(DfCoarseGround.getNum());
			}
			map.put("x",x);
			map.put("y",y);

			return new Result(200,"查询成功",map);
		}


	}


}
