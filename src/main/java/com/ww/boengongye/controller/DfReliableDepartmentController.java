package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfReliableDepartment;
import com.ww.boengongye.service.DfReliableDepartmentService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-10-24
 */
@RestController
@RequestMapping("/dfReliableDepartment")
@CrossOrigin
@Api(tags = "责任部门")
public class DfReliableDepartmentController {

	@Autowired
	private DfReliableDepartmentService dfReliableDepartmentService;

	@ApiOperation("新增或修改")
	@PostMapping(value = "/saveOrUpdate")
	public Object add(@RequestBody DfReliableDepartment dfReliableDepartment) {
		QueryWrapper<DfReliableDepartment> ew = new QueryWrapper<>();
		ew.eq("name", dfReliableDepartment.getName());
		List<DfReliableDepartment> list = dfReliableDepartmentService.list(ew);
		if (! CollectionUtils.isEmpty(list)){
			return new Result(500, "存在:" + dfReliableDepartment.getName() + "部门,不能重复新增或修改");
		}
		if (dfReliableDepartmentService.saveOrUpdate(dfReliableDepartment)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object add(String id) {
		if (dfReliableDepartmentService.removeById(id)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}
	@ApiOperation("根据部门名称查询")
	@GetMapping(value = "/queryDepartment")
	public Object queryDepartment(int page,int limit , String name) {
		Page<DfReliableDepartment> pages = new Page<>(page, limit);
		QueryWrapper<DfReliableDepartment> ew = new QueryWrapper<>();
		ew.like(StringUtils.isNotEmpty(name),"name", name);
		IPage<DfReliableDepartment> list = dfReliableDepartmentService.page(pages,ew);
		return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
	}

}
