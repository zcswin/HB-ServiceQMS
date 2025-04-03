package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfBrushReplacementRecords;
import com.ww.boengongye.service.DfBrushReplacementRecordsService;
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
 * 毛刷更换记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@RestController
@RequestMapping("/dfBrushReplacementRecords")
@Api(tags = "毛刷更换记录表)")
@CrossOrigin
public class DfBrushReplacementRecordsController {

	@Autowired
	private DfBrushReplacementRecordsService dfBrushReplacementRecordsService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfBrushReplacementRecords dfBrushReplacementRecords ) {
		if (dfBrushReplacementRecordsService.saveOrUpdate(dfBrushReplacementRecords)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-id倒序")
	public Result listAll(int page,int limit,String classes) {
		Page<DfBrushReplacementRecords> pages = new Page<DfBrushReplacementRecords>(page, limit);
		QueryWrapper<DfBrushReplacementRecords> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(classes)){
			qw.eq( "classes",classes);
		}
		qw.orderByDesc("id");
		IPage<DfBrushReplacementRecords> list = dfBrushReplacementRecordsService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	@GetMapping(value = "/getById")
	@ApiOperation("根据id查询")
	public Result listAll(int id) {
		return new Result(200,"查询成功",dfBrushReplacementRecordsService.getById(id));
	}

	@GetMapping(value = "/deleteById")
	@ApiOperation("根据id删除")
	public Result deleteById(int id) {
		if(dfBrushReplacementRecordsService.removeById(id)){
			return Result.DELETE_SUCCESS;
		}
		else{
			return Result.DELETE_FAILED;
		}
	}

	@GetMapping(value = "/getBrushUp")
	@ApiOperation("毛刷-上")
	public Result getBrushCUp(@ApiParam("相机孔猪毛刷,闪光孔猪毛刷...") String type
			, Double lengthBegin
			, Double lengthEnd
			, Double thickBegin
			, Double thickEnd
			, String factoryId
			, @ApiParam("项目") String project
			, @ApiParam("颜色")String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfBrushReplacementRecords> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfBrushReplacementRecords> qwThisweek = new QueryWrapper<>();
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
		String selectColumn1 = "SUM(if (thick between " + thickBegin + " and " + thickEnd +" and length between " + lengthBegin + " and " + lengthEnd + ",1,0)) as ok";
		String selectColumn2 = "SUM(if (thick between " + thickBegin + " and " + thickEnd +" and length between " + lengthBegin + " and " + lengthEnd + ",0,1)) as ng";
		switch (type){
			case "相机孔猪毛刷" :
				qwLastweek.eq("process", "相机孔猪毛刷");
				qwThisweek.eq("process", "相机孔猪毛刷");
				break;
			case "闪光孔猪毛刷" :
				qwLastweek.eq("process", "闪光孔猪毛刷");
				qwThisweek.eq("process", "闪光孔猪毛刷");
				break;
		}
		qwLastweek.select("replace_time as date "
				,selectColumn1,selectColumn2)
				.between("replace_time", startTimeLastweek, endTimeLastweek)
				.groupBy("replace_time")
				.orderByAsc("replace_time");
		List<DfBrushReplacementRecords> listLastweek = dfBrushReplacementRecordsService.list(qwLastweek);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> xLastweekDate = new ArrayList<>();
		ArrayList<Object> yLastweekOk = new ArrayList<>();
		ArrayList<Object> yLastweekNg = new ArrayList<>();
		for (DfBrushReplacementRecords dfBrushReplacementRecords : listLastweek) {
			xLastweekDate.add(dfBrushReplacementRecords.getDate());
			yLastweekOk.add(dfBrushReplacementRecords.getOk());
			yLastweekNg.add(dfBrushReplacementRecords.getNg());
		}
		map.put("xLastweekDate",xLastweekDate);
		map.put("xLastweekOk",yLastweekOk);
		map.put("yLastweekNg",yLastweekNg);

		qwThisweek.select("replace_time as date "
				,selectColumn1,selectColumn2)
				.between("replace_time", startTimeThisweek, endTimeThisweek)
				.groupBy("replace_time")
				.orderByAsc("replace_time");
		List<DfBrushReplacementRecords> listThisweek = dfBrushReplacementRecordsService.list(qwThisweek);
		ArrayList<Object> xThisweekDate = new ArrayList<>();
		ArrayList<Object> yThisweekOk = new ArrayList<>();
		ArrayList<Object> yThisweekNg = new ArrayList<>();
		for (DfBrushReplacementRecords dfBrushReplacementRecords : listThisweek) {
			xThisweekDate.add(dfBrushReplacementRecords.getDate());
			yThisweekOk.add(dfBrushReplacementRecords.getOk());
			yThisweekNg.add(dfBrushReplacementRecords.getNg());
		}
		map.put("xLastweekDate",xLastweekDate);
		map.put("xLastweekOk",yLastweekOk);
		map.put("yLastweekNg",yLastweekNg);

		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getBrushDown")
	@ApiOperation("毛刷-下")
	public Result getCarveLifetimeBycCarvePositionDown(
			@ApiParam("相机孔猪毛刷,闪光孔猪毛刷...") String type
			, Double lengthBegin
			, Double lengthEnd
			, Double thickBegin
			, Double thickEnd
			, String factoryId
			, @ApiParam("项目") String project
			, @ApiParam("颜色") String color
	) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfBrushReplacementRecords> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfBrushReplacementRecords> qwThisweek = new QueryWrapper<>();
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
		String selectColumn1 = "SUM(if (thick between " + thickBegin + " and " + thickEnd + "),0,1) as thick_ng";
		String selectColumn2 = "SUM(if (length between " + lengthBegin + " and " + lengthEnd + "),0,1) as length_ng";
		switch (type){
			case "相机孔猪毛刷" :
				qwLastweek.eq("process", "相机孔猪毛刷");
				qwThisweek.eq("process", "相机孔猪毛刷");
				break;
			case "闪光孔猪毛刷" :
				qwLastweek.eq("process", "闪光孔猪毛刷");
				qwThisweek.eq("process", "闪光孔猪毛刷");
				break;
		}

		qwLastweek.select("machine_id",selectColumn1,selectColumn2)
				.between("use_time", startTimeLastweek, endTimeLastweek)
				.groupBy("machine_id")
				.orderByAsc("machine_id");
		List<DfBrushReplacementRecords> listLastweek = dfBrushReplacementRecordsService.list(qwLastweek);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> xMachineId = new ArrayList<>();
		ArrayList<Object> yThickNg = new ArrayList<>();
		ArrayList<Object> yLengthNg = new ArrayList<>();
		for (DfBrushReplacementRecords dfBrushReplacementRecords : listLastweek) {
			xMachineId.add(dfBrushReplacementRecords.getMachineId());
			yThickNg.add(dfBrushReplacementRecords.getThick());
			yLengthNg.add(dfBrushReplacementRecords.getLengthNg());
		}
		map.put("xMachineId",xMachineId);
		map.put("yThickNg",yThickNg);
		map.put("yLengthNg",yLengthNg);

		return new Result(200,"查询成功",map);
	}

}
