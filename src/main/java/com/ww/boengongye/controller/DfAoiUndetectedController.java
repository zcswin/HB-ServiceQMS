package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiConfigData;
import com.ww.boengongye.entity.DfAoiDefect;
import com.ww.boengongye.service.DfAoiConfigDataService;
import com.ww.boengongye.service.DfAoiUndetectedService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI漏检记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@Controller
@RequestMapping("/dfAoiUndetected")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI漏检")
public class DfAoiUndetectedController {

	@Autowired
	private DfAoiUndetectedService dfAoiUndetectedService;

	@Autowired
	private DfAoiConfigDataService dfAoiConfigDataService;

	@GetMapping("fqcNgTopRate")
	@ApiOperation("线体看板明细 - FQC漏检TOP(N)及占比")
	public Object fqcNgTopRate(@ApiParam("Top几") @RequestParam(required = false) Integer top,
							   @ApiParam("厂别") @RequestParam(required = false) String factory,
							   @ApiParam("线体") @RequestParam(required = false) String lineBody,
							   @ApiParam("时间-开始") @RequestParam(required = false) String startDate,
							   @ApiParam("时间-结束") @RequestParam(required = false) String endDate,
							   @ApiParam("项目") @RequestParam(required = false) String project,
							   @ApiParam("颜色") @RequestParam(required = false) String color) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
		ew.eq("no",1);
		if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
			ew.between("TIME", startTime, endTime);
		}
		ew.eq(StringUtils.isNotEmpty(factory), "FACTORY",factory)
				.eq(StringUtils.isNotEmpty(lineBody), "LINE_BODY",lineBody)
				.eq(StringUtils.isNotEmpty(project), "PROJECT",project)
				.eq(StringUtils.isNotEmpty(color), "COLOR",color);

		List<Map<String,Object>> list = dfAoiUndetectedService.fqcNgTopRate(ew,top);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> listQx = new ArrayList<>();
		ArrayList<Object> listRate = new ArrayList<>();

		//AOI配置漏检基数
		QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
		aoiConfigDataWrapper
				.eq("config_name","AOI漏检基数")
				.last("limit 1");
		DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
		Double escapeData = dfAoiConfigData.getConfigValue();


		for (Map<String, Object> one : list) {
			listQx.add(one.get("FEATUREVALUES"));
			listRate.add(Double.parseDouble(one.get("rate").toString())*escapeData);
		}
		map.put("listQx",listQx);
		map.put("listRate",listRate);
		return new Result<>(200,"查询成功",map);
	}
}
