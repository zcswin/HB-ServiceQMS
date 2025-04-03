package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfKnifPosition;
import com.ww.boengongye.service.DfKnifPositionService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 刀具号 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-07
 */
@Api("刀具号")
@RestController
@RequestMapping("/dfKnifPosition")
public class DfKnifPositionController {

	@Autowired
	private DfKnifPositionService dfKnifPositionService;

	@GetMapping("/getKnifPosition")
	public Object getKnifPosition(String process){
		QueryWrapper<DfKnifPosition> ew = new QueryWrapper<>();
		ew.eq(StringUtils.isNotEmpty(process), "process", process);
		List<DfKnifPosition> list = dfKnifPositionService.list(ew);
		return new Result<>(200,"查询成功",list);
	}

}
