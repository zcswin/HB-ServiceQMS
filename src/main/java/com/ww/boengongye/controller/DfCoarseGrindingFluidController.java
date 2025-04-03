package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfCoarseGrindingFluid;
import com.ww.boengongye.service.DfCoarseGrindingFluidService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 粗磨研磨液 添加/更换 记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/dfCoarseGrindingFluid")
@CrossOrigin
@Api(tags = "粗磨研磨液 添加/更换 记录表 前端控制器")
public class DfCoarseGrindingFluidController {

	@Autowired
	private DfCoarseGrindingFluidService dfCoarseGrindingFluidService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfCoarseGrindingFluid dfCoarseGrindingFluid) {
		Integer hour = Integer.parseInt(dfCoarseGrindingFluid.getCreateTime().toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCoarseGrindingFluid.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCoarseGrindingFluid.setClasses("B");
		}
		dfCoarseGrindingFluid.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfCoarseGrindingFluidService.save(dfCoarseGrindingFluid)){
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
		Page<DfCoarseGrindingFluid> pages = new Page<DfCoarseGrindingFluid>(page,limit);
		QueryWrapper<DfCoarseGrindingFluid> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfCoarseGrindingFluid> list = dfCoarseGrindingFluidService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

}
