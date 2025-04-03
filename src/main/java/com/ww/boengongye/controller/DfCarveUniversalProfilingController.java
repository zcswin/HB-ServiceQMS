package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfCarveUniversalProfiling;
import com.ww.boengongye.service.DfCarveUniversalProfilingService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 精雕通⽤ 夹具靠模点检表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/dfCarveUniversalProfiling")
@Api(tags = "精雕通⽤夹具靠模点检表")
@CrossOrigin
public class DfCarveUniversalProfilingController {

	@Autowired
	private DfCarveUniversalProfilingService dfCarveUniversalProfilingService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfCarveUniversalProfiling dfCarveUniversalProfiling) {
		dfCarveUniversalProfiling.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfCarveUniversalProfilingService.save(dfCarveUniversalProfiling)){
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
		Page<DfCarveUniversalProfiling> pages = new Page<DfCarveUniversalProfiling>(page,limit);
		QueryWrapper<DfCarveUniversalProfiling> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfCarveUniversalProfiling> list = dfCarveUniversalProfilingService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

}
