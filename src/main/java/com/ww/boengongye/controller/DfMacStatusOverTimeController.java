package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfMacStatusOverTime;
import com.ww.boengongye.service.DfMacStatusOverTimeService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  机台状态超时管理 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/dfMacStatusOverTime")
@CrossOrigin
@Api(tags = "机台状态超时管理")
public class DfMacStatusOverTimeController {

	@Autowired
	private DfMacStatusOverTimeService dfMacStatusOverTimeService;

	@ApiOperation("新增或修改")
	@PostMapping("/saveOrUpdate")
	public Object saveOrUpdate(@RequestBody DfMacStatusOverTime datas){
		if (null != datas.getId()) {
			if (dfMacStatusOverTimeService.updateById(datas)) {
				return new Result(200, "保存成功");
			} else {
				return new Result(500, "保存失败");
			}
		} else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
			if (dfMacStatusOverTimeService.save(datas)) {
				return new Result(200, "保存成功");
			} else {
				return new Result(500, "保存失败");
			}
		}
	}

	@ApiOperation("删除")
	@GetMapping("/delete")
	public Object deleteById(@RequestParam String id){
		if (dfMacStatusOverTimeService.removeById(id)){
			return new Result(200, "删除成功");
		}else {
			return new Result(500, "删除失败");
		}
	}

	@GetMapping("/conditionQuery")
	@ApiOperation("条件查询")
	public Object conditionQuery(int page,int limit,String type){
		Page<DfMacStatusOverTime> pages = new Page<>(page, limit);
		QueryWrapper<DfMacStatusOverTime> ew = new QueryWrapper<>();
		ew.ge(StringUtils.isNotEmpty(type),"type",type);
		IPage<DfMacStatusOverTime> list = dfMacStatusOverTimeService.page(pages, ew);
		return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
	}

}
