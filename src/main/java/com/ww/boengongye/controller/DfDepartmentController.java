package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfDepartment;
import com.ww.boengongye.service.DfDepartmentService;
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
 * @since 2023-10-17
 */
@RestController
@RequestMapping("/dfDepartment")
@CrossOrigin
@Api(tags = "部门")
public class DfDepartmentController {
	@Autowired
	private DfDepartmentService dfDepartmentService;

//	@ApiOperation("查询全部")
//	@GetMapping("/listAll")
//	public Object listAll(){
//		List<DfDepartment> list = dfDepartmentService.list();
//		return new Result(200, "查询成功",list);
//	}

	@ApiOperation("新增或修改")
	@PostMapping(value = "/saveOrUpdate")
	public Object add(@RequestBody DfDepartment dfDepartment) {
		QueryWrapper<DfDepartment> ew = new QueryWrapper<>();
		ew.eq("name", dfDepartment.getName());
		List<DfDepartment> list = dfDepartmentService.list(ew);
		if (! CollectionUtils.isEmpty(list)){
			return new Result(500, "存在:" + dfDepartment.getName() + "部门,不能重复新增或修改");
		}
		if (dfDepartmentService.saveOrUpdate(dfDepartment)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object add(String id) {
		if (dfDepartmentService.removeById(id)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}
	@ApiOperation("根据部门名称或码查询")
	@GetMapping(value = "/queryDepartment")
	public Object queryDepartment(int page,int limit , String name,String code) {
		Page<DfDepartment> pages = new Page<>(page, limit);
		QueryWrapper<DfDepartment> ew = new QueryWrapper<>();
		ew.like(StringUtils.isNotEmpty(name),"name", name)
			.like(StringUtils.isNotEmpty(code),"code",code);
		IPage<DfDepartment> list = dfDepartmentService.page(pages,ew);
		return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
	}



}
