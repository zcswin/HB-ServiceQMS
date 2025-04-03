package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfIsmCheckItemInfosService;
import com.ww.boengongye.service.DfYieldWarnService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.NormalDistributionUtil;
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
import java.util.stream.Collectors;

/**
 * <p>
 * ISM测量 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/dfIsmCheckItemInfos")
@CrossOrigin
@Api(tags = "ISM测量")
public class DfIsmCheckItemInfosController {
	@Autowired
	private DfIsmCheckItemInfosService dfIsmCheckItemInfosService;

	@Autowired
	private DfYieldWarnService dfYieldWarnService;

	@PostMapping(value = "upload")
	@Transactional
	public Result upload(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			return new Result(500, "获取信息失败");
		}
		ExcelImportUtil excel = new ExcelImportUtil(file);
		SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
		//获取excel表中所有的数据
//		List<Map<String, String>> maps = excel.readExcelContent(2, 1);
		ExcelImportUtil importUtil = new ExcelImportUtil(file);
		//调用导入的方法，获取sheet表的内容
		List<Map<String, String>> maps = importUtil.readExcelContent();
		int count = 0;
		Map<String, Integer> snISMId = new HashMap<>();

		for (Map<String, String> map : maps) {
			DfIsmCheckItemInfos dfIsmCheckItemInfos = new DfIsmCheckItemInfos();
			String sn = null;
			if (StringUtils.isNotEmpty(map.get("暗码"))) {
				sn = map.get("暗码");
			}else {
				continue;
			}
			if (snISMId.containsKey(sn)) {
				dfIsmCheckItemInfos.setCheckType(2);
			} else {
				dfIsmCheckItemInfos.setCheckType(1);
			}
			dfIsmCheckItemInfos.setCheckTime(Timestamp.valueOf(map.get("时间")));
			dfIsmCheckItemInfos.setRowId(Integer.valueOf(map.get("RowID")));
			dfIsmCheckItemInfos.setPosition(map.get("位置"));
			dfIsmCheckItemInfos.setResult(map.get("结果"));
			dfIsmCheckItemInfos.setPrivateMark(map.get("暗码"));
			dfIsmCheckItemInfos.setDivideMaterialsDifferentParts(map.get("分料料仓"));
			dfIsmCheckItemInfos.setPrivateMarkResult(map.get("暗码结果"));
			dfIsmCheckItemInfos.setNetworkValidationResults(map.get("网络校验结果"));
			dfIsmCheckItemInfos.setTrackUrca(map.get("Trace卡站"));
			dfIsmCheckItemInfos.setAbnormalThickness(map.get("厚度异常"));
			dfIsmCheckItemInfos.setStressResults(map.get("应力结果"));
			dfIsmCheckItemInfos.setCs(null == map.get("cs") ? null : Double.valueOf(map.get("cs")));
			dfIsmCheckItemInfos.setCsk(null == map.get("csk") ? null : Double.valueOf(map.get("csk")));
			dfIsmCheckItemInfos.setDol(null == map.get("dol") ? null : Double.valueOf(map.get("dol")));
			dfIsmCheckItemInfos.setDoc(null == map.get("doc") ? null : Double.valueOf(map.get("doc")));
			dfIsmCheckItemInfos.setCt(null == map.get("ct") ? null : Double.valueOf(map.get("ct")));
			dfIsmCheckItemInfos.setCtOrTa(null == map.get("ct/ta") ? null : Double.valueOf(map.get("ct/ta")));
			dfIsmCheckItemInfos.setTact(null == map.get("ta*ct") ? null : Double.valueOf(map.get("ta*ct")));
			dfIsmCheckItemInfos.setGGt(null == map.get("gt") ? null : Double.valueOf(map.get("gt")));
			dfIsmCheckItemInfos.setIsmRollbackTime(null == map.get("ISM回传时间") ? null : new Timestamp(sd.parse(map.get("ISM回传时间")).getTime()));
			dfIsmCheckItemInfos.setIsmRollbackModel(map.get("ISM回传型号"));
			dfIsmCheckItemInfos.setAbnormalThickness(map.get("ISM回传报错"));
			dfIsmCheckItemInfos.setNgType(map.get("NgType"));
			dfIsmCheckItemInfos.setMachineId("11");
			dfIsmCheckItemInfosService.save(dfIsmCheckItemInfos);
			Integer IsmId = dfIsmCheckItemInfos.getId();
			//如果Map中包含,说明这一片是重复
			if (snISMId.containsKey(sn)) {
				Integer id = snISMId.get(sn);
				//删掉原来的,录最新的
				dfIsmCheckItemInfosService.removeById(id);
			}
			snISMId.put(sn, IsmId);
			count ++;
		}
			return new Result(200, "导入" + count + "条信息");
	}

	@GetMapping("/listLeadNum")
	@ApiOperation("产能数量趋势图（每两小时）")
	public Result listLeadNum(String factory, String lineBody, String process,
							  @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		Rate4 rate = dfIsmCheckItemInfosService.listIsmNum(qw);
		Integer[] numList = new Integer[]{};
		if (null != rate) {
			numList = new Integer[]{rate.getInte1(), rate.getInte2(), rate.getInte3(), rate.getInte4(), rate.getInte5(),
					rate.getInte6(), rate.getInte7(), rate.getInte8(), rate.getInte9(), rate.getInte10(), rate.getInte11(), rate.getInte12()};
		}
		String[] timeList = new String[]{"7:00", "9:00", "11:00", "13:00", "15:00", "17:00", "19:00", "21:00", "23:00", "1:00", "3:00", "5:00"};
		Map<String, Object> result = new HashMap<>();
		result.put("num", numList);
		result.put("time", timeList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listLeadNum2")
	@ApiOperation("产能数量趋势图（日期分组）")
	public Result listLeadNum2(String factory, String lineBody, String process,
							   @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfLeadDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfIsmCheckItemInfosService.listAllNumGroupByDate(qw);
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

	@ApiOperation("工厂一次综合良率对比1")
	@GetMapping("OneAndCompositeComparable1")
	public Object OneAndCompositeComparable(String factory, String lineBody, String process,
											@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> ew = new QueryWrapper<>();
		ew.select("machine_id str1",
				"sum(if(check_type = 1 and stress_results = 'OK', 1, 0)) / count(*) as dou1 ",
				"sum(if(stress_results = 'OK', 1, 0)) / count(*) as dou2 ")
			.between("check_time",startTime, endTime)
			.groupBy("machine_id")
			.orderByAsc("machine_id");
		List<Map<String, Object>> list = dfIsmCheckItemInfosService.listMaps(ew);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> machine = new ArrayList<>();
		ArrayList<Object> listYc = new ArrayList<>();
		ArrayList<Object> listZh = new ArrayList<>();
		for (Map<String, Object> one : list) {
			machine.add(one.get("str1"));
			listYc.add(one.get("dou1"));
			listZh.add(one.get("dou2"));
		}
		map.put("machineId",machine);
		map.put("yc",listYc);
		map.put("zh",listZh);

		return new Result(200, "查询成功",map);
	}

	@ApiOperation("工厂一次综合良率对比2")
	@GetMapping("OneAndCompositeComparable2")
	public Object OneAndCompositeComparable2(String factory, String lineBody, String process,
											 @RequestParam String machineId,
//											 @RequestParam String type,
											 @RequestParam String startDate,
											 @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> ew = new QueryWrapper<>();
		ew.select("position","sum(if(stress_results = 'OK',1,0))/count(*) as result")
				.between("check_time",startTime, endTime)
				.eq(StringUtils.isNotEmpty(machineId),"machine_id",machineId)
//				.eq(StringUtils.isNotEmpty(type),"check_type",type)
				.groupBy("position")
				.orderByAsc("position");
		List<DfIsmCheckItemInfos> list = dfIsmCheckItemInfosService.list(ew);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> position = new ArrayList<>();
		ArrayList<Object> rate = new ArrayList<>();
		for (DfIsmCheckItemInfos one : list) {
			position.add(one.getPosition());
			rate.add(one.getResult());
		}
		map.put("position",position);
		map.put("rate",rate);

		return new Result(200, "查询成功",map);
	}

	@GetMapping("/listMachineOneAndMutilOkRate")
	@ApiOperation("工厂一次/综合良率对比2")
	public Result listMachineOneAndMutilOkRate(String factory, String lineBody, String process,
											   @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfIsmCheckItemInfosService.listMachineOneAndMutilOkRate(qw);
		List<Object> machineList = new ArrayList<>();
		List<Object> oneOkRateList = new ArrayList<>();  // 一次良率
		List<Object> mutilOkRateList = new ArrayList<>();  // 综合良率
		for (Rate3 rate : rates) {
			machineList.add(rate.getStr1());
			oneOkRateList.add(rate.getDou1());
			mutilOkRateList.add(rate.getDou2());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("machine", machineList);
		result.put("oneOkRate", oneOkRateList);
		result.put("mutilOkRate", mutilOkRateList);
		return new Result(200, "查询成功", result);
	}

	@GetMapping("/listAllOKRateTop")
	@ApiOperation("工厂不良TOP10分布对比")
	/**
	 * 区间范围:
	 * cs:530-680
	 * dol:4.5-5.8
	 * csk:100-190
	 * doc:100-140
	 * ct:85-124
	 */
	public Result listAllOKRateTop(String factory, String lineBody, String process,
								   @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.select(
				"sum(if(cs_result = 'NG' ,1, 0))/ count(*) cs",
				"sum(if(csk_result = 'NG' ,1, 0))/ count(*) csk",
				"sum(if(dol_result = 'NG' ,1, 0))/ count(*) dol",
				"sum(if(doc_result = 'NG' ,1, 0))/ count(*) doc",
				"sum(if(ct_result = 'NG' ,1, 0))/ count(*) ct",
				"sum(if(ct_or_ta_result = 'NG' ,1, 0))/ count(*) ct_or_ta",
				"sum(if(tact_result = 'NG' ,1, 0))/ count(*) tact",
				"sum(if(g_gt_result = 'NG' ,1, 0))/ count(*) g_gt")
				.between("check_time", startTime, endTime);
		Map<String, Object> map = dfIsmCheckItemInfosService.getMap(qw);
		Map<String, Double> doubleMap = new HashMap<String, Double>();
		HashMap<Object, Object> result = new HashMap<>();
		ArrayList<Object> listName = new ArrayList<>();
		ArrayList<Object> listValue = new ArrayList<>();
		result.put("factory",Arrays.asList("J10-1"));
		result.put("listName",listName);
		result.put("listValue",listValue);
		if (null != map){
			for (String i : map.keySet()) {
				if (map.get(i)!=null){
					doubleMap.put(i, Double.valueOf(map.get(i).toString()));
				}
			}
		} else {
			return new Result(203, "查询成功", result);
		}
		Map<String, Double> sortedMap2 = doubleMap.entrySet().stream()
				.sorted((o1, o2) ->  (o2.getValue().compareTo(o1.getValue())))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(oldVal, newVal) -> oldVal,
						LinkedHashMap::new));

		for (Map.Entry<String, Double> entry : sortedMap2.entrySet()) {
			listName.add(entry.getKey());
			listValue.add(entry.getValue());
		}
		return new Result(200, "查询成功",result);
	}

	@GetMapping("/FourPositionCompare")
	@ApiOperation("工位间良率差异对比")
	public Result FourPositionCompare(String factory, String lineBody, String process, String machineName,
										 @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		qw.eq(StringUtils.isNotEmpty(machineName),"machine_id", machineName);
		List<Rate3> rates = dfIsmCheckItemInfosService.listWorkPositionOKRate(qw);
		Map<String, Integer> macResIndex = new HashMap<>();
		List<String> macList = new ArrayList<>();
		int macIndex = 0;
		for (Rate3 rate : rates) {
			if (!macResIndex.containsKey(rate.getStr1())) {
				macList.add(rate.getStr1());
				macResIndex.put(rate.getStr1(), macIndex++);
			}
		}
		Double[][] rateOkData = new Double[4][macResIndex.size()];
		Double[][] rateNaData = new Double[4][macResIndex.size()];
		Map<String, Integer> positionResIndex = new HashMap<>();
		positionResIndex.put("A1", 0);
		positionResIndex.put("A2", 1);
		positionResIndex.put("B1", 2);
		positionResIndex.put("B2", 3);
		String[] positionList = new String[]{"A1", "A2", "B1", "B2"};
		for (Rate3 rate : rates) {
			String machine = rate.getStr1();
			String position = rate.getStr2();
			Double okRate = rate.getDou1();
			Double naRate = rate.getDou2();
			rateOkData[positionResIndex.get(position)][macResIndex.get(machine)] = okRate;
			rateNaData[positionResIndex.get(position)][macResIndex.get(machine)] = naRate;
		}

		Map<String, Object> result = new HashMap<>();
		result.put("position", positionList);
		result.put("machine", macList);
		result.put("A1", rateOkData[0]);
		result.put("A2", rateOkData[1]);
		result.put("B1", rateOkData[2]);
		result.put("B2", rateOkData[3]);

		result.put("A1Na", rateNaData[0]);
		result.put("A2Na", rateNaData[1]);
		result.put("B1Na", rateNaData[2]);
		result.put("B2Na", rateNaData[3]);

		return new Result(200, "查询成功", result);
	}


	@ApiOperation("白夜班良率趋势对比")
	@GetMapping("dayNightOkRate")
	public Object dayNightOkRate(String factory, String lineBody, String process,
								 @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfIsmCheckItemInfosService.listOkRateGroupByDate(qw);
		List<Object> dateList = new ArrayList<>();
		List<Object> dayList = new ArrayList<>();
		List<Object> nightList = new ArrayList<>();
		List<Object> targetList = new ArrayList<>();

		//TZ-目标良率
		QueryWrapper<DfYieldWarn> yieldWarnWrapper = new QueryWrapper<>();
		yieldWarnWrapper
				.eq("`type`","ISM")
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


	@GetMapping("/listAll")
	@ApiOperation("获取详情")
	public Result listAll(@RequestParam String startDate,
						  @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<DfIsmCheckItemInfos> list = dfIsmCheckItemInfosService.list(qw);

		return new Result(200, "查询成功", list);
	}

	@GetMapping("/getItemNormalDistributionByItem")
	@ApiOperation("获取测试项的正态分布图(cs,dol,csk,doc,ct)")
	public Result getItemNormalDistributionByItem(@RequestParam String item,
												  @RequestParam String startDate,
												  @RequestParam String endDate) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		//上限
		double up = 0;
		//下限
		double down = 0;
		switch (item){
			case "cs":
				down = 530;
				up = 680;
				break;
			case "dol":
				down = 4.5;
				up = 5.8;
				break;
			case "csk":
				down = 100;
				up = 190;
				break;
			case "doc":
				down = 100;
				up = 140;
				break;
			case "ct":
				down = 85;
				up = 124;
				break;
		}
		QueryWrapper<DfIsmCheckItemInfos> qw = new QueryWrapper<>();
		qw.select(item + " cs")
			.between("check_time", startTime, endTime);
		List<DfIsmCheckItemInfos> dfIsmCheckItemInfos = dfIsmCheckItemInfosService.list(qw);
		Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
		Map<String, Double> itemResStandardValue = new HashMap<>();
		Map<String, Double> itemResUsl = new HashMap<>();
		Map<String, Double> itemResLsl = new HashMap<>();
		Map<String, Integer> itemResOkNum = new HashMap<>();
		Map<String, Integer> itemResAllNum = new HashMap<>();

		for (DfIsmCheckItemInfos checkItemInfo : dfIsmCheckItemInfos) {
			String process = item;
			itemResAllNum.merge(process, 1, Integer::sum);
			if (checkItemInfo.getCs() <= up && checkItemInfo.getCs() >= down) itemResOkNum.merge(process, 1, Integer::sum);
			if (checkItemInfo == null || checkItemInfo.getCs() == null){
				continue;
			}
			Double checkValue = checkItemInfo.getCs();
			if (!itemResCheckValue.containsKey(item)) {
				List<Double> list = new ArrayList<>();
				list.add(checkValue);
				itemResCheckValue.put(item, list);
				itemResUsl.put(item, down);
				itemResLsl.put(item, up);
			} else {
				itemResCheckValue.get(item).add(checkValue);
			}
		}

		List<Map<String, Object>> result = new ArrayList<>();
		for (Map.Entry<String, List<Double>> entry : itemResCheckValue.entrySet()) {
			Map<String, Object> itemData = new HashMap<>();
			String process = entry.getKey();
			itemData.put("name", process);
//			itemData.put("standard", itemResStandardValue.get(process));
			itemData.put("usl", itemResUsl.get(process));
			itemData.put("lsl", itemResLsl.get(process));

			Integer okNum = itemResOkNum.get(item) == null ? 0 : itemResOkNum.get(item);
			Integer allNum = itemResAllNum.get(item) == null ? 0 : itemResAllNum.get(item);
			//itemData.put("良率OK", itemResOkNum.get(itemName));
			itemData.put("良率", okNum.doubleValue() / allNum);
			itemData.put("allNum", allNum);
			NormalDistributionUtil.normalDistribution2(NormalDistributionUtil.convertToDoubleArray(entry.getValue().toArray()), itemData);
			result.add(itemData);
		}
		return new Result(200, "查询成功", result);
	}



	@GetMapping("/listDateOneAndMutilOkRate")
	@ApiOperation("工厂一次/综合良率对比2(时间分组)")
	public Result listDateOneAndMutilOkRate(String factory, String lineBody,
											@RequestParam String startDate, @RequestParam String endDate
			,String project,String color,String device
	) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		QueryWrapper<DfTzDetail> qw = new QueryWrapper<>();
		qw.between("check_time", startTime, endTime);
		List<Rate3> rates = dfIsmCheckItemInfosService.listDateOneAndMutilOkRate(qw);
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


}
