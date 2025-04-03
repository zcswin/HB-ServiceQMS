package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfEmdCheckItemInfosMapper;
import com.ww.boengongye.mapper.DfEmdDetailMapper;
import com.ww.boengongye.service.DfEmdCheckItemInfosService;
import com.ww.boengongye.service.DfEmdDetailService;
import com.ww.boengongye.service.DfYieldWarnService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * EMD检测 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-13
 */
@RestController
@RequestMapping("/dfEmdCheckItemInfos")
@CrossOrigin
@Api(tags = "EMD检测")
public class DfEmdCheckItemInfosController {
	@Autowired
	private DfEmdCheckItemInfosService dfEmdCheckItemInfosService;
	@Autowired
	private DfEmdDetailService dfEmdDetailService;
	@Autowired
	private DfEmdDetailMapper dfEmdDetailMapper;
	@Autowired
	private DfEmdCheckItemInfosMapper dfEmdCheckItemInfosMapper;

	@Autowired
	private DfYieldWarnService dfYieldWarnService;

	@PostMapping("/importData")
	@ApiOperation("导入数据")
	@Transactional(rollbackFor = Exception.class)
	public Result importData(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			return new Result(500, "获取信息失败");
		}
		String[] split = file.getOriginalFilename().split("\\+");
		//模板格式:J6-6+FMBH19+C26+黑色+2023-09-09+F+27点.xlsx
		if (split.length != 7){
			return new Result(500,"文件名格式不正确,请以:C27+9307+良品+B班+6788+MP+F形式命名");
		}
		int count = 0;

//		String factory = split[0];
//		String linebody = split[1];
		String project = split[0];
//		String color = split[3];
		//F或B
//		String workPosition = split[6];
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 52);
		Map<String, Integer> snResLeadId = new HashMap<>();
		for (int i = 1; i < strings.length; i++) {
			if (StringUtils.isEmpty(strings[i][0])||StringUtils.isEmpty(strings[i][1])){
				continue;
			}

			Date parse = sd.parse(strings[i][0]);
			Timestamp time = new Timestamp(parse.getTime());
			//前或后
			String fOrB = strings[i][1];
			//sn左右
			String snLeft = strings[i][2];
			String snRight = strings[i][6];
			//结果
			String resultLeft = strings[i][3];
			String resultRight = strings[i][7];
			//值
			String valueLeft = strings[i][4];
			String valueRight = strings[i][8];
			//形变
			String changeLeft = strings[i][5];
			String changeRight = strings[i][9];

			DfEmdDetail dataLeft = new DfEmdDetail();
			dataLeft.setSn(snLeft);
			dataLeft.setCheckTime(time);
			dataLeft.setMachineCode(fOrB + "1");
//			dataLeft.setFactory(factory);
//			dataLeft.setLineBody(linebody);
			dataLeft.setProject(project);
//			dataLeft.setColor(color);
			dataLeft.setWorkPosition(fOrB);
			dataLeft.setWorkResult(resultLeft);
			dataLeft.setWorkValue(valueLeft == null ? null : Double.valueOf(valueLeft));
			dataLeft.setWorkChange(changeLeft);
			dataLeft.setMachine("#11");

			if (snResLeadId.containsKey(snLeft)) {
				dataLeft.setCheckType(2);
			} else {
				dataLeft.setCheckType(1);
			}
			dfEmdDetailMapper.insert(dataLeft);
			Integer emdId = dataLeft.getId();
			if (snResLeadId.containsKey(snLeft)) {
				Integer id = snResLeadId.get(snLeft);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snLeft, emdId);

			ArrayList<DfEmdCheckItemInfos> list1 = new ArrayList<>();
			for (int j = 10; j <= 30; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				list1.add(itemInfos);
			}
			try {
				dfEmdCheckItemInfosService.saveBatch(list1);
				count ++;
			}catch (Exception e){
				System.out.println("第:" +i +"行解析异常");
			}

			if(StringUtils.isEmpty(snRight)){
				continue;
			}

			DfEmdDetail dataRight = new DfEmdDetail();
			dataRight.setSn(snRight);
			dataRight.setCheckTime(time);
			dataRight.setMachineCode(fOrB + "2");
//			dataRight.setFactory(factory);
//			dataRight.setLineBody(linebody);
			dataRight.setProject(project);
//			dataRight.setColor(color);
			dataRight.setWorkPosition(fOrB);
			dataRight.setWorkResult(resultRight);
			dataRight.setWorkValue(valueRight == null ? null : Double.valueOf(valueRight));
			dataRight.setWorkChange(changeRight);
			if (snResLeadId.containsKey(snRight)) {
				dataRight.setCheckType(2);
			} else {
				dataRight.setCheckType(1);
			}
			dataRight.setMachine("#11");
			dfEmdDetailMapper.insert(dataRight);
			emdId = dataRight.getId();
			if (snResLeadId.containsKey(snRight)) {
				Integer id = snResLeadId.get(snRight);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snRight, emdId);
			ArrayList<DfEmdCheckItemInfos> list2 = new ArrayList<>();
			for (int j = 31; j <= 51; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				list2.add(itemInfos);
			}
			dfEmdCheckItemInfosService.saveBatch(list2);
			count ++;
		}
		return new Result(200, "添加" + count +"条记录");
	}

	@GetMapping("/listProductionNum")
	@ApiOperation("产能数量趋势图（每两小时）")
	public Result listLeadNum(String factory, String lineBody, String process,
							  @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		Rate4 rate = dfEmdCheckItemInfosService.listLeadNum(qw);
		Integer[] numList = new Integer[]{rate.getInte1(), rate.getInte2(), rate.getInte3(), rate.getInte4(), rate.getInte5(),
				rate.getInte6(), rate.getInte7(), rate.getInte8(), rate.getInte9(), rate.getInte10(), rate.getInte11(), rate.getInte12()};
		String[] timeList = new String[]{"7:00", "9:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00", "1:00", "3:00", "5:00"};
		Map<String, Object> result = new HashMap<>();
		result.put("num", numList);
		result.put("time", timeList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listProductionNum2")
	@ApiOperation("产能数量趋势图（日期分组）")
	public Result listProductionNum2(String factory, String lineBody, String process,
							   @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfEmdCheckItemInfosService.listAllNumGroupByDate(qw);
		List<Object> dateList = new ArrayList<>();
		List<Object> dayList = new ArrayList<>();
		List<Object> nightList = new ArrayList<>();
		for (Rate3 rate : rates) {
			dateList.add(rate.getStr1());
			dayList.add(rate.getInte1());
			nightList.add(rate.getInte2());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("date", dateList);
		result.put("day", dayList);
		result.put("night", nightList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listOKRate")
	@ApiOperation("工厂一次/综合良率对比")
	public Result listOKRate(String factory, String lineBody, String process,
							 @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfEmdCheckItemInfosService.listOKRate(qw);
		List<String> factoryList = new ArrayList<>();
		List<Double> oneRateList = new ArrayList<>();
		List<Double> mutilRateList = new ArrayList<>();
		for (Rate3 rate : rates) {
			oneRateList.add(rate.getDou1());
			mutilRateList.add(rate.getDou2());
		}
		Map<String, Object> result = new HashMap<>();
		factoryList.add("J10-1");
		result.put("factory", factoryList);
		result.put("oneRate", oneRateList);
		result.put("mutilRate", mutilRateList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listOkRateGroupByDate")
	@ApiOperation("白夜班良率趋势对比")
	public Result listOkRateGroupByDate(String factory, String lineBody, String process,
										@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfEmdCheckItemInfosService.listOkRateGroupByDate(qw);
		List<Object> dateList = new ArrayList<>();
		List<Object> dayList = new ArrayList<>();
		List<Object> nightList = new ArrayList<>();
		List<Object> targetList = new ArrayList<>();

		//TZ-目标良率
		QueryWrapper<DfYieldWarn> yieldWarnWrapper = new QueryWrapper<>();
		yieldWarnWrapper
				.eq("`type`","EMD")
				.eq("name","目标良率")
				.last("limit 1");
		DfYieldWarn dfYieldWarn = dfYieldWarnService.getOne(yieldWarnWrapper);

		for (Rate3 rate : rates) {
			dateList.add(rate.getStr1());
			dayList.add(rate.getDou1());
			nightList.add(rate.getDou2());
			targetList.add(dfYieldWarn.getPrewarningValue());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("date", dateList);
		result.put("day", dayList);
		result.put("night", nightList);
		result.put("targetListY",targetList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/leftAndRightCompare")
	@ApiOperation("工位良率对比")
	public Result leftAndRightCompare(String factory, String lineBody, String process,
										@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime)
			.groupBy("machine_code")
			.orderByAsc("machine_code");
		List<Rate3> rates = dfEmdCheckItemInfosService.listWorkPositionOKRate(qw);
		List<Object> machineList = new ArrayList<>();
		List<Object> rateList = new ArrayList<>();
		for (Rate3 rate : rates) {
			machineList.add(rate.getStr1());
			rateList.add(rate.getDou1());
		}
		HashMap<Object, Object> map = new HashMap<>();
		map.put("machineList",machineList);
		map.put("rateList",rateList);
		return new Result(200, "查询成功", map);
	}

	@GetMapping("/listWorkPositionOKRateV2")
	@ApiOperation("工位间良率差异对比V2")
	public Result listWorkPositionOKRateV2(String factory, String lineBody, String process,
										 @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfEmdCheckItemInfosService.listWorkPositionOKRate2(qw);
		Map<String, Integer> macResIndex = new HashMap<>();
		List<String> macList = new ArrayList<>();
		int macIndex = 0;
		for (Rate3 rate : rates) {
			if (!macResIndex.containsKey(rate.getStr1())) {
				macList.add(rate.getStr1());
				macResIndex.put(rate.getStr1(), macIndex++);
			}
		}
		Double[][] rateData = new Double[4][macResIndex.size()];
		Map<String, Integer> positionResIndex = new HashMap<>();
		positionResIndex.put("F1", 0);
		positionResIndex.put("F2", 1);
		positionResIndex.put("B1", 2);
		positionResIndex.put("B2", 3);
		String[] positionList = new String[]{"F1", "F2", "B1", "B2"};
		for (Rate3 rate : rates) {
			String machine = rate.getStr1();
			String position = rate.getStr2();
			Double okRate = rate.getDou1();
			rateData[positionResIndex.get(position)][macResIndex.get(machine)] = okRate;
		}

		Map<String, Object> result = new HashMap<>();
		result.put("position", positionList);
		result.put("machine", macList);
		result.put("A1", rateData[0]);
		result.put("A2", rateData[1]);
		result.put("B3", rateData[2]);
		result.put("B4", rateData[3]);

		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listAll")
	@ApiOperation("获取详情")
	public Result listAll(@RequestParam String startDate,
						  @RequestParam String endDate,
						  String color) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfEmdDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime)
				.eq(!"".equals(color) && null != color, "color", color);
		List<DfEmdDetail> list = dfEmdDetailService.list(qw);

		return new Result(200, "查询成功", list);
	}

	@GetMapping("/listDateOneAndMutilOkRate")
	@ApiOperation("工厂一次/综合良率对比2(时间分组)")
	public Result listDateOneAndMutilOkRate(String factory, String lineBody,
											@RequestParam String startDate, @RequestParam String endDate
			,String project,String color,String device
	) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfEmdCheckItemInfosService.listDateOneAndMutilOkRate(qw);
		List<Object> timeList = new ArrayList<>();
		List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
		List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
		for (Rate3 rate : rates) {
			timeList.add(rate.getStr1());
			oneOkRateList.add(rate.getDou1());
			mutilOkRateList.add(rate.getDou2());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("time", timeList);
		result.put("oneOkRate", oneOkRateList);
		result.put("mutilOkRate", mutilOkRateList);
		return new Result(200, "查询成功", result);
	}


	@GetMapping("/list21positionAvgGraph")
	@ApiOperation("右上方21点位平均直方图")
	public Result list21positionAvgGraph(String factory, String lineBody,
											@RequestParam String startDate, @RequestParam String endDate
			,String project,String color,String device
	) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
		qw.select("point_position","avg(result) result")
			.between("check_time", startTime, endTime)
			.groupBy("point_position")
			.orderByAsc("point_position");
		List<DfEmdCheckItemInfos> list = dfEmdCheckItemInfosService.list(qw);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> x = new ArrayList<>();
		ArrayList<Object> y = new ArrayList<>();
		for (DfEmdCheckItemInfos dfEmdCheckItemInfos : list) {
			x.add(dfEmdCheckItemInfos.getPointPosition());
			y.add(dfEmdCheckItemInfos.getResult());
		}
		map.put("x", x);
		map.put("y",y);
		return new Result(200, "查询成功", map);
	}


}
