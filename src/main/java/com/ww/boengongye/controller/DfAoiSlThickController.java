package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.DfAoiSlThick;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.SlCloseEntity;
import com.ww.boengongye.service.DfAoiSlThickService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 丝印-厚度 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
@RestController
@RequestMapping("/dfAoiSlThick")
@CrossOrigin
@Api(tags = "丝印厚度")
public class DfAoiSlThickController {

	@Autowired
	private DfAoiSlThickService dfAoiSlThickService;

	@PostMapping("/importData")
	@ApiOperation("导入数据")
	@Transactional
	public Result importData(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			return new Result(500, "获取信息失败");
		}
		int count = dfAoiSlThickService.importExcel2(file);
		return new Result(200, "添加" + count +"条记录");
	}

	@GetMapping("/getCloseData")
	@ApiOperation("关闭的表单信息")
	@Transactional
	public Result getCloseData(@RequestParam String startDate,@RequestParam String endDate ,
							   String factory,String process,String linebody ,String status,String model,String color,String clazz) throws Exception {

		QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<DfAoiSlThick>();
		ew.between("create_time",startDate + " 07:00:00",endDate + " 23:59:59")
			.eq(StringUtils.isNotEmpty(factory),"factory",factory)
			.eq(StringUtils.isNotEmpty(process),"process", process)
			.eq(StringUtils.isNotEmpty(linebody),"line_body", linebody)
			.eq(StringUtils.isNotEmpty(status),"status", status)
			.eq(StringUtils.isNotEmpty(color),"color", color)
			.eq(StringUtils.isNotEmpty(clazz),"clazz", clazz)
			.orderByDesc("create_time");
		List<SlCloseEntity> list =  dfAoiSlThickService.getCloseData1(ew);
		return new Result(200, "查询成功",list);
	}

	@GetMapping("/getworstLine")
	@ApiOperation("最差线体")
	public Result getworstLine(@RequestParam String startDate,@RequestParam String endDate ,
							   String factory,String process,String linebody ,String status,String model,String color,String clazz) throws Exception {
		QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<DfAoiSlThick>();
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		List<String> strings = Arrays.asList("丝印厚度", "丝印尺寸", "丝印颜色");
		ew.between("report_time", startTime,endTime)
			.in("DATA_TYPE",strings);
		List<Rate3> list =  dfAoiSlThickService.getworstLine(ew);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> listLine = new ArrayList<>();
		ArrayList<Object> listRate = new ArrayList<>();
		for (Rate3 rate3 : list) {
			listLine.add(rate3.getStr1());
			listLine.add(rate3.getDou1());
		}
		map.put("listLine",listLine);
		map.put("listRate",listRate);
		return new Result(200, "查询成功",list);
	}

	@GetMapping("/getCloseDataZ")
	@ApiOperation("关闭率趋势")
	public Result getCloseDataZ(@RequestParam String startDate,@RequestParam String endDate ,
							   String factory,String process,String linebody ,String status,String model,String color,String clazz) throws Exception {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		List<String> strings = Arrays.asList("丝印厚度", "丝印尺寸", "丝印颜色");
		QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<DfAoiSlThick>();
		ew.between("report_time", startTime,endTime)
				.in("DATA_TYPE",strings);
		List<Rate3> list =  dfAoiSlThickService.getCloseUp(ew);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> listDate = new ArrayList<>();
		ArrayList<Object> listDay = new ArrayList<>();
		ArrayList<Object> listNight = new ArrayList<>();
		ArrayList<Object> listDayClose = new ArrayList<>();
		ArrayList<Object> listNithtClose = new ArrayList<>();
		ArrayList<Object> listAllDayRate = new ArrayList<>();
		for (Rate3 rate3 : list) {
			listDate.add(rate3.getStr1());
			listDay.add(rate3.getInte1());
			listNight.add(rate3.getInte2());
			listDayClose.add(rate3.getDou1());
			listNithtClose.add(rate3.getDou2());
			listAllDayRate.add(rate3.getDou3());
		}
		map.put("date",listDate);
		map.put("listDay",listDay);
		map.put("listNight",listNight);
		map.put("listDayClose",listDayClose);
		map.put("listNithtClose",listNithtClose);
		map.put("listAllDayRate",listAllDayRate);
		return new Result(200, "查询成功",map);
	}
}
