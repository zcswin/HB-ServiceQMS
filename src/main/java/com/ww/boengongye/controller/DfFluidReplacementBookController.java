package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfFluidReplacementBook;
import com.ww.boengongye.service.DfFluidReplacementBookService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 磨液更换记录本-磨液池-非单机台 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Api(tags = "磨液更换记录本-磨液池-非单机台")
@RequestMapping("/dfFluidReplacementBook")
@RestController
@CrossOrigin
public class DfFluidReplacementBookController {

	@Autowired
	private DfFluidReplacementBookService dfFluidReplacementBookService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfFluidReplacementBook dfFluidReplacementBook) {
		Integer hour = Integer.parseInt(dfFluidReplacementBook.getReplaceTime().toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfFluidReplacementBook.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfFluidReplacementBook.setClasses("B");
		}
		dfFluidReplacementBook.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfFluidReplacementBookService.save(dfFluidReplacementBook)){
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
	public Result getRecordByMachineId(int page,int limit) {
		Page<DfFluidReplacementBook> pages = new Page<>(page,limit);
		QueryWrapper<DfFluidReplacementBook> qw = new QueryWrapper<>();
		qw.orderByDesc("create_time");
		IPage<DfFluidReplacementBook> list = dfFluidReplacementBookService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

}
