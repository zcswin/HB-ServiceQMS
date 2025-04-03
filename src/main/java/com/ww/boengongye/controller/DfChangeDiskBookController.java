package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfChangeDiskBook;
import com.ww.boengongye.service.DfChangeDiskBookService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 换盘登记本 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Api(tags = "换盘登记本：3D抛光、返磨、扫⾯、粗磨、磨平台、CNC等")
@RestController
@RequestMapping("/dfChangeDiskBook")
@CrossOrigin
public class DfChangeDiskBookController {

	@Autowired
	private DfChangeDiskBookService dfChangeDiskBookService;

	@PostMapping(value = "/upload")
	@ApiOperation("上传")
	public Result upload(@RequestBody DfChangeDiskBook dfChangeDiskBook) {
		dfChangeDiskBook.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
		if (dfChangeDiskBookService.save(dfChangeDiskBook)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}


	@GetMapping(value = "/listAll")
	@ApiOperation("分页查询-时间倒序")
	public Result listAll(int page,int limit,String classes) {
		Page<DfChangeDiskBook> pages = new Page<DfChangeDiskBook>(page, limit);
		QueryWrapper<DfChangeDiskBook> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(classes)){
			qw.eq("classes",classes);
		}
		qw.orderByDesc("create_time");
		IPage<DfChangeDiskBook> list = dfChangeDiskBookService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

}
