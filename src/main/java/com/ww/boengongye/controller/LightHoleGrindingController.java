package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLightHoleGrinding;
import com.ww.boengongye.service.DfLightHoleGrindingService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 光孔磨粉添加/更换记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@RestController
@RequestMapping("/lightHoleGrinding")
@CrossOrigin
@Api(tags = "光孔磨粉添加/更换记录表")
public class LightHoleGrindingController {

	@Autowired
	private DfLightHoleGrindingService dfLightHoleGrindingService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfLightHoleGrinding dfLightHoleGrinding) {
		Integer hour = Integer.parseInt(dfLightHoleGrinding.getAddTime().toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfLightHoleGrinding.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfLightHoleGrinding.setClasses("B");
		}
		dfLightHoleGrinding.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfLightHoleGrindingService.save(dfLightHoleGrinding)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	/**
	 *
	 * @param
	 * @return
	 */
	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page,int limit) {
		Page<DfLightHoleGrinding> pages = new Page<>(page,limit);
		QueryWrapper<DfLightHoleGrinding> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfLightHoleGrinding> list = dfLightHoleGrindingService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

}
