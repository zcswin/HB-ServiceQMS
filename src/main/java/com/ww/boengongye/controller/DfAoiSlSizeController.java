package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiSlSize;
import com.ww.boengongye.service.DfAoiSlSizeService;
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
 * df_aoi尺寸表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/dfAoiSlSize")
@CrossOrigin
@Api(tags = "丝印尺寸")
public class DfAoiSlSizeController {

	@Autowired
	private DfAoiSlSizeService dfAoiSlSizeService;

	@PostMapping("/importData")
	@ApiOperation("导入数据")
	@Transactional
	public Result importData(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			return new Result(500, "获取信息失败");
		}
		int count = dfAoiSlSizeService.importExcel(file);
		return new Result(200, "添加" + count +"条记录");
	}

//	@ApiOperation("LOGO尺寸正态分布")
//	@GetMapping("/getLogeSizenormaldistribution")
//	public Object getLogeSizenormaldistribution(
//			String startDate , String endDate,
//			@RequestParam Integer page, @RequestParam Integer limit) throws ParseException, ParseException {
//		String startTime = startDate + " 07:00:00";
//		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//		Page<DfAoiSlSize> pages = new Page<>();
//		QueryWrapper<DfAoiSlSize> ew = new QueryWrapper<>();
//		ew.between("check_time",startTime,endTime)
//				.eq("item","LOGO尺寸")
//				.isNotNull("item_value");
//		IPage<DfAoiSlSize> records = dfAoiSlSizeService.page(pages, ew);
//		List<DfAoiSlSize> dfAoiSlSizeList = records.getRecords();
//		Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
//		Map<String, Double> itemResStandardValue = new HashMap<>();
//		Map<String, Double> itemResUsl = new HashMap<>();
//		Map<String, Double> itemResLsl = new HashMap<>();
//		Map<String, Integer> itemResOkNum = new HashMap<>();
//		Map<String, Integer> itemResAllNum = new HashMap<>();
//
//		for (DfAoiSlSize checkItemInfo : dfAoiSlSizeList) {
//			String process = "LOGO尺寸";
//			Double checkValue = checkItemInfo.getItemValue();
//			itemResAllNum.merge(process, 1, Integer::sum);
//			if (checkValue <= checkItemInfo.getUp() && checkValue >= checkItemInfo.getDown()) itemResOkNum.merge(process, 1, Integer::sum);
//			if (!itemResCheckValue.containsKey(process)) {
//				List<Double> list = new ArrayList<>();
//				list.add(checkValue);
//				itemResCheckValue.put(process, list);
//				itemResStandardValue.put(process, checkItemInfo.getStandard());
//				itemResUsl.put(process, checkItemInfo.getUp());
//				itemResLsl.put(process, checkItemInfo.getDown());
//			} else {
//				itemResCheckValue.get(process).add(checkValue);
//			}
//		}
//
//		List<Map<String, Object>> result = new ArrayList<>();
//		for (Map.Entry<String, List<Double>> entry : itemResCheckValue.entrySet()) {
//			Map<String, Object> itemData = new HashMap<>();
//			String process = entry.getKey();
//			itemData.put("name", process);
//			itemData.put("standard", itemResStandardValue.get(process));
//			itemData.put("usl", itemResUsl.get(process));
//			itemData.put("lsl", itemResLsl.get(process));
//
//			Integer okNum = itemResOkNum.get(process) == null ? 0 : itemResOkNum.get(process);
//			Integer allNum = itemResAllNum.get(process) == null ? 0 : itemResAllNum.get(process);
//			//itemData.put("良率ALL", itemResAllNum.get(itemName));
//			itemData.put("良率", okNum.doubleValue() / allNum);
//			itemData.put("allNum", allNum);
//			NormalDistributionUtil.normalDistribution2(NormalDistributionUtil.convertToDoubleArray(entry.getValue().toArray()), itemData);
//			result.add(itemData);
//		}
//
//		return new Result(200, "查询成功", result);
//	}

	@GetMapping
	@ApiOperation("LOGO尺寸下拉查询列表")
	public Result logoSizeControlChart(){
		List<String> list = Arrays.asList("油墨面Logo左到玻璃中心距离-X", "油墨面Logo右到玻璃中心距离-X",
				"油墨面Logo顶到玻璃中心距离-Y", "油墨面Logo底到玻璃中心距离-Y",
				"用户面到系统面logo位置度",
				"正面LOGO和背面LOGO位置差异值X", "正面LOGO和背面LOGO位置差异值Y",
				"（带基准）logo轮廓度油墨面", "（无基准）logo轮廓度玻璃面",
				"（带基准）logo轮廓度玻璃面", "（无基准）logo轮廓度玻璃面");
		return new Result(200, "成功",list);
	}

	@GetMapping("/logoSizeControlChart")
	@ApiOperation("LOGO尺寸管制图")
	public Result logoSizeControlChart(@RequestParam String startDate,@RequestParam String endDate,@RequestParam String name,
									   String factory,String lineBody,String project,String color) throws Exception {

		QueryWrapper<DfAoiSlSize> ew = new QueryWrapper<>();
		ew.select("check_time","up","down","standard","item","item_value")
			.between("check_time",startDate + " 00:70:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
				.eq("item",name)
				.isNotNull("item_value")
			.orderByDesc("check_time")
			.last("limit 50");
		List<DfAoiSlSize> list = dfAoiSlSizeService.list(ew);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> listUp = new ArrayList<>();
		ArrayList<Object> listDown = new ArrayList<>();
		ArrayList<Object> listStandard = new ArrayList<>();
		ArrayList<Object> listValue = new ArrayList<>();
		for (DfAoiSlSize dfAoiSlSize : list) {
			listUp.add(dfAoiSlSize.getUp());
			listDown.add(dfAoiSlSize.getDown());
			listStandard.add(dfAoiSlSize.getStandard());
			listValue.add(dfAoiSlSize.getItemValue());
		}
		map.put("listUp",listUp);
		map.put("listDown",listDown);
		map.put("listStandard",listStandard);
		map.put("listValue",listValue);
		map.put("name",name);
		return new Result(200, "查询成功",map);
	}

}
