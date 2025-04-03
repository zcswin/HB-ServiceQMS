package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfFlatWasherCotton;
import com.ww.boengongye.service.DfFlatWasherCottonService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 平板清洗机棉芯更换记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/dfFlatWasherCotton")
@CrossOrigin
@Api(tags="平板清洗机棉芯更换记录表")
public class DfFlatWasherCottonController {
	@Autowired
	private DfFlatWasherCottonService dfFlatWasherCottonService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfFlatWasherCotton dfFlatWasherCotton) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfFlatWasherCotton.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfFlatWasherCotton.setClasses("B");
		}
		dfFlatWasherCotton.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfFlatWasherCottonService.save(dfFlatWasherCotton)){
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
		Page<DfFlatWasherCotton> pages = new Page<DfFlatWasherCotton>(page,limit);
		QueryWrapper<DfFlatWasherCotton> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfFlatWasherCotton> list = dfFlatWasherCottonService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}
}
