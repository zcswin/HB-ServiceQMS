package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 精雕通⽤
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/dfCarveUniversal")
@Api(tags = "精雕通⽤ 记录表 type值来区分")
@CrossOrigin
public class DfCarveUniversalController {

	@Autowired
	private DfCarveUniversalService dfCarveUniversalService;
	@Autowired
	private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;
	@Autowired
	private DfKnifStatusService dfKnifStatusService;
	@Autowired
	private DfKnifStatusType6Service dfKnifStatusType6Service;

	@Autowired
	private DfKnifeUseInfoService dfKnifeUseInfoService;

	@PostMapping(value = "/suctionUpload")
	@ApiOperation("洗盘吸嘴-上传")
	public Result suctionUpload(@RequestBody DfCarveUniversal dfCarveUniversal) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCarveUniversal.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCarveUniversal.setClasses("B");
		}
		dfCarveUniversal.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType(1);
		if (dfCarveUniversalService.save(dfCarveUniversal)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@PostMapping(value = "/buzzerUpload")
	@ApiOperation("轮砂刀具-上传")
	public Result buzzerUpload(@RequestBody DfCarveUniversal dfCarveUniversal) {
		Integer hour = Integer.parseInt(dfCarveUniversal.getUseTime().toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCarveUniversal.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCarveUniversal.setClasses("B");
		}
		dfCarveUniversal.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType(2);
		if (dfCarveUniversalService.save(dfCarveUniversal)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@PostMapping(value = "/coatUpload")
	@ApiOperation("涂层砂轮-上传")
	public Result coatUpload(@RequestBody DfCarveUniversal dfCarveUniversal) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCarveUniversal.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCarveUniversal.setClasses("B");
		}
		dfCarveUniversal.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType(3);
		if (dfCarveUniversalService.save(dfCarveUniversal)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	@PostMapping(value = "/grindingFluidUpload")
	@ApiOperation("磨削液-上传")
	public Result grindingFluidUpload(@RequestBody DfCarveUniversal dfCarveUniversal) {
		Integer hour = Integer.parseInt(Timestamp.valueOf(LocalDateTime.now()).toString().substring(11, 13));
		if (hour >= 7 && hour < 19){
			dfCarveUniversal.setClasses("A");
		}else if (hour >= 19 || hour < 7){
			dfCarveUniversal.setClasses("B");
		}
		dfCarveUniversal.setCreateTime(Timestamp.valueOf(LocalDateTime.now()))
				.setType(4);
		if (dfCarveUniversalService.save(dfCarveUniversal)){
			return Result.INSERT_SUCCESS;
		}else{
			return Result.INSERT_FAILED;
		}
	}

	/**
	 *
	 * @param
	 * @return
	 */
	@GetMapping(value = "/listSuction")
	@ApiOperation("分页查询-时间倒序-洗盘吸嘴")
	public Result listSuction(int page,int limit,int type) {
		Page<DfCarveUniversal> pages = new Page<>(page,limit);
		QueryWrapper<DfCarveUniversal> qw = new QueryWrapper<>();
		qw.eq("type",1);
		qw.orderByDesc("create_time");
		IPage<DfCarveUniversal> list = dfCarveUniversalService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	/**
	 *
	 * @param
	 * @return
	 */
	@GetMapping(value = "/listBuzzer")
	@ApiOperation("分页查询-时间倒序-轮砂刀具")
	public Result listBuzzer(int page,int limit,int type) {
		Page<DfCarveUniversal> pages = new Page<>(page,limit);
		QueryWrapper<DfCarveUniversal> qw = new QueryWrapper<>();
		qw.eq("type",2);
		qw.orderByDesc("create_time");
		IPage<DfCarveUniversal> list = dfCarveUniversalService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	/**
	 *
	 * @param
	 * @return
	 */
	@GetMapping(value = "/listCoat")
	@ApiOperation("分页查询-时间倒序-涂层砂轮")
	public Result listCoat(int page,int limit,int type) {
		Page<DfCarveUniversal> pages = new Page<>(page,limit);
		QueryWrapper<DfCarveUniversal> qw = new QueryWrapper<>();
		qw.eq("type",3);
		qw.orderByDesc("create_time");
		IPage<DfCarveUniversal> list = dfCarveUniversalService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}

	/**
	 *
	 * @param
	 * @return
	 */
	@GetMapping(value = "/getKnifNo")
	@ApiOperation("根据工序返回刀具号")
	public Result getKnifNo(String process) {
		List<String> list = new ArrayList<>();
		switch(process){
			case "CNC0":
				list = Arrays.asList("砂轮1-精修轮廓","砂轮1-粗开轮廓","砂轮1-精修孔位","砂轮1-粗开孔位");
				break;
			case "CNC1":
				list = Arrays.asList("砂轮2-粗开大面");
				break;
			case "CNC2":
				list = Arrays.asList("砂轮3-粗开A/R/MC孔+轮廓","砂轮3-精修A/R/MC孔+轮廓","砂轮3-精修S孔","砂轮3-粗开S孔","砂轮3-精修MIC孔"
						,"砂轮3-粗开MIC孔");
				break;
			case "CNC3":
				list = Arrays.asList("砂轮4-粗开大面和平台线","砂轮5-精修大面和平台线","砂轮6-精磨平台线","砂轮7-精修孔位");
				break;
		}
		return new Result(200,"查询成功",list);
	}

	@GetMapping(value = "/listGrindingFluid")
	@ApiOperation("")
	public Result listGrindingFluid(int page,int limit,int type) {
		Page<DfCarveUniversal> pages = new Page<>(page,limit);
		QueryWrapper<DfCarveUniversal> qw = new QueryWrapper<>();
		qw.eq("type",4);
		qw.orderByDesc("create_time");
		IPage<DfCarveUniversal> list = dfCarveUniversalService.page(pages, qw);
		return new Result(200,"查询成功",list.getRecords(),(int)list.getTotal());
	}


	@GetMapping(value = "/getCarveLifetimeBycCarvePosition")
	@ApiOperation("砂轮寿命分布-上")
	public Result getCarveLifetimeBycCarvePosition(
			@ApiParam("粗开轮廓,精修轮廓,粗开孔位,精修孔位...") @RequestParam(required = true) String carvePosition,
			@ApiParam("砂轮寿命标准(150,600,150,250,300...)") @RequestParam(required = true) int lifeTime,
			String factoryId,
			String productModel) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCarveUniversal> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCarveUniversal> qwThisweek = new QueryWrapper<>();

		qwLastweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","SUM(IF(quantity_processed <= "  + lifeTime + " ,1,0)) as ok","SUM(IF(quantity_processed <= "  + lifeTime + " ,0,1)) as ng")
			.between("use_time", startTimeLastweek, endTimeLastweek)
			.eq("carve_position",carvePosition)
			.eq("type",2)
			.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
			.eq(StringUtils.isNotEmpty(productModel), "product_model",productModel)
			.groupBy("date")
			.orderByAsc("date");
		List<DfCarveUniversal> listLastweek = dfCarveUniversalService.list(qwLastweek);
		HashMap<String, Object> map = new HashMap<>();
		int okLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getOk()));
		int ngLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getNg()));
		if (okLastweek + ngLastweek !=0){
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			String rate = decimalFormat.format((float) okLastweek / (okLastweek + ngLastweek));
			map.put("rateLastweek",rate);
		}else {
			map.put("rateLastweek",0);
		}

		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweekOk = new ArrayList<>();
		ArrayList<Object> yLastweekNg = new ArrayList<>();
		for (DfCarveUniversal dfCarveUniversal : listLastweek) {
			xLastweek.add(dfCarveUniversal.getDate());
			yLastweekOk.add(dfCarveUniversal.getOk());
			yLastweekNg.add(dfCarveUniversal.getNg());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweekOk",yLastweekOk);
		map.put("yLastweekNg",yLastweekNg);


		qwThisweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","SUM(IF(quantity_processed <= "  + lifeTime + " ,1,0)) as ok","SUM(IF(quantity_processed <= "  + lifeTime + " ,0,1)) as ng")
				.between("use_time", startTimeThisweek, endTimeThisweek)
				.eq("type",2)
				.eq("carve_position",carvePosition)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
				.eq(StringUtils.isNotEmpty(productModel), "product_model",productModel)
				.groupBy("date")
				.orderByAsc("date");
		List<DfCarveUniversal> listThisweek = dfCarveUniversalService.list(qwThisweek);
		int okThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getOk()));
		int ngThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getNg()));
		if (okThisweek + ngThisweek !=0){
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			String rate = decimalFormat.format((float) okThisweek / (okThisweek + ngThisweek));
			map.put("rateThisweek",rate);
		}else {
			map.put("rateThisweek",0);
		}
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweekOk = new ArrayList<>();
		ArrayList<Object> yThisweekNg = new ArrayList<>();
		for (DfCarveUniversal dfCarveUniversal : listThisweek) {
			xThisweek.add(dfCarveUniversal.getDate());
			yThisweekOk.add(dfCarveUniversal.getOk());
			yThisweekNg.add(dfCarveUniversal.getNg());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweekOk",yThisweekOk);
		map.put("yThisweekNg",yThisweekNg);

		return new Result(200,"查询成功",map);
	}

	@GetMapping(value = "/getCarveLifetimeBycCarvePositionDown")
	@ApiOperation("砂轮寿命分布-下")
	public Result getCarveLifetimeBycCarvePositionDown(
			@ApiParam("粗开轮廓,精修轮廓,粗开孔位,精修孔位...") @RequestParam(required = true) String carvePosition
			,String factoryId
			,String productModel) {
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
		QueryWrapper<DfCarveUniversal> qwLastweek = new QueryWrapper<>();
		QueryWrapper<DfCarveUniversal> qwThisweek = new QueryWrapper<>();

		qwLastweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","count(*) as replace_num")
				.between("use_time", startTimeLastweek, endTimeLastweek)
				.eq("type",2)
				.eq("carve_position",carvePosition)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
				.eq(StringUtils.isNotEmpty(productModel), "product_model",productModel)
				.groupBy("date")
				.orderByAsc("replace_num");
		List<DfCarveUniversal> listLastweek = dfCarveUniversalService.list(qwLastweek);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Object> xLastweek = new ArrayList<>();
		ArrayList<Object> yLastweek = new ArrayList<>();
		for (DfCarveUniversal dfCarveUniversal : listLastweek) {
			xLastweek.add(dfCarveUniversal.getDate());
			yLastweek.add(dfCarveUniversal.getReplaceNum());
		}
		map.put("xLastweek",xLastweek);
		map.put("yLastweek",yLastweek);


		qwThisweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","count(*) as replace_num")
				.between("use_time", startTimeThisweek, endTimeThisweek)
				.eq("type",2)
				.eq("carve_position",carvePosition)
				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
				.eq(StringUtils.isNotEmpty(productModel), "product_model",productModel)
				.groupBy("date")
				.orderByAsc("replace_num");
		List<DfCarveUniversal> listThisweek = dfCarveUniversalService.list(qwThisweek);
		ArrayList<Object> xThisweek = new ArrayList<>();
		ArrayList<Object> yThisweek = new ArrayList<>();
		for (DfCarveUniversal dfCarveUniversal : listThisweek) {
			xThisweek.add(dfCarveUniversal.getDate());
			yThisweek.add(dfCarveUniversal.getReplaceNum());
		}
		map.put("xThisweek",xThisweek);
		map.put("yThisweek",yThisweek);

		return new Result(200,"查询成功",map);
	}

//	@GetMapping(value = "/getCarveLifetimeBycCarvePositionUpByCNC2And3")
//	@ApiOperation("砂轮寿命分布-上(CNC2,CNC3)")
//	public Result getCarveLifetimeBycCarvePositionUpByCNC2And3(
//			@ApiParam("粗开A/R/MC孔+轮廓,精修A/R/MC孔+轮廓,粗开S孔,精修S孔...") @RequestParam(required = true) String carvePosition,
//			@ApiParam("砂轮寿命标准(150,600,150,250,300...)") @RequestParam(required = true) int lifeTime,
//			String factoryId,
//			String project,
//			@RequestParam String process) {
////		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
////		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
////		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
////		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
//		String startTimeLastweek = "2023-01-01 00:00:00";
//		String endTimeLastweek = "2023-11-09 00:00:00";
//		String startTimeThisweek = "2023-01-01 00:00:00";
//		String endTimeThisweek = "2023-11-09 00:00:00";
//		QueryWrapper<DfCarveUniversal> qwLastweek = new QueryWrapper<>();
//		QueryWrapper<DfCarveUniversal> qwThisweek = new QueryWrapper<>();
//
//		qwLastweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","SUM(IF(quantity_processed <= "  + lifeTime + " ,1,0)) as ok","SUM(IF(quantity_processed <= "  + lifeTime + " ,0,1)) as ng")
//				.between("use_time", startTimeLastweek, endTimeLastweek)
//				.eq("carve_position",carvePosition)
////				.eq("type",2)
////				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
////				.eq(StringUtils.isNotEmpty(productModel), "product_model",project)
//				.groupBy("date")
//				.orderByAsc("date");
//		List<DfCarveUniversal> listLastweek = dfCarveUniversalService.list(qwLastweek);
//		HashMap<String, Object> map = new HashMap<>();
//		int okLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getOk()));
//		int ngLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getNg()));
//		if (okLastweek + ngLastweek !=0){
//			DecimalFormat decimalFormat = new DecimalFormat("#.###");
//			String rate = decimalFormat.format((float) okLastweek / (okLastweek + ngLastweek));
//			map.put("rateLastweek",rate);
//		}else {
//			map.put("rateLastweek",0);
//		}
//
//		ArrayList<Object> xLastweek = new ArrayList<>();
//		ArrayList<Object> yLastweekOk = new ArrayList<>();
//		ArrayList<Object> yLastweekNg = new ArrayList<>();
//		for (DfCarveUniversal dfCarveUniversal : listLastweek) {
//			xLastweek.add(dfCarveUniversal.getDate());
//			yLastweekOk.add(dfCarveUniversal.getOk());
//			yLastweekNg.add(dfCarveUniversal.getNg());
//		}
//		map.put("xLastweek",xLastweek);
//		map.put("yLastweekOk",yLastweekOk);
//		map.put("yLastweekNg",yLastweekNg);
//
//
//		qwThisweek.select("DATE_FORMAT(DATE_SUB(use_time,INTERVAL 7 HOUR ),'%m-%d') as date ","SUM(IF(quantity_processed <= "  + lifeTime + " ,1,0)) as ok","SUM(IF(quantity_processed <= "  + lifeTime + " ,0,1)) as ng")
//				.between("use_time", startTimeThisweek, endTimeThisweek)
////				.eq("type",2)
//				.eq("carve_position",carvePosition)
////				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
////				.eq(StringUtils.isNotEmpty(productModel), "product_model",project)
//				.groupBy("date")
//				.orderByAsc("date");
//		List<DfCarveUniversal> listThisweek = dfCarveUniversalService.list(qwThisweek);
//		int okThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getOk()));
//		int ngThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getNg()));
//		if (okThisweek + ngThisweek !=0){
//			DecimalFormat decimalFormat = new DecimalFormat("#.###");
//			String rate = decimalFormat.format((float) okThisweek / (okThisweek + ngThisweek));
//			map.put("rateThisweek",rate);
//		}else {
//			map.put("rateThisweek",0);
//		}
//		ArrayList<Object> xThisweek = new ArrayList<>();
//		ArrayList<Object> yThisweekOk = new ArrayList<>();
//		ArrayList<Object> yThisweekNg = new ArrayList<>();
//		for (DfCarveUniversal dfCarveUniversal : listThisweek) {
//			xThisweek.add(dfCarveUniversal.getDate());
//			yThisweekOk.add(dfCarveUniversal.getOk());
//			yThisweekNg.add(dfCarveUniversal.getNg());
//		}
//		map.put("xThisweek",xThisweek);
//		map.put("yThisweekOk",yThisweekOk);
//		map.put("yThisweekNg",yThisweekNg);
//
//		return new Result(200,"查询成功",map);
//	}

	@GetMapping(value = "/getCarveLifetimeBycCarvePositionUpByCNC2And3")
	@ApiOperation("砂轮寿命分布-上(CNC2,CNC3)")
	public Result getCarveLifetimeBycCarvePositionUpByCNC2And3(
			String factoryId,
			String project,
			@RequestParam String process) {

		List<Object> list = new ArrayList<>();
		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
		String endTimeThisweek = TimeUtil.getNowTimeByNormal();

		QueryWrapper<DfKnifeUseInfo> qw = new QueryWrapper<>();
		qw
				.select("mac_tool_index")
				.between("create_time", startTimeLastweek, endTimeThisweek)
				.eq("process",process)
				.groupBy("mac_tool_index")
				.orderByAsc("mac_tool_index");
		//该条件下所有的刀具号
		List<DfKnifeUseInfo> macToolIndexList = dfKnifeUseInfoService.list(qw);
		if (macToolIndexList==null||macToolIndexList.size()==0){
			return new Result(200,"该条件下没有砂轮寿命分布相关数据",list);
		}

		for (DfKnifeUseInfo dfKnifeUseInfo:macToolIndexList){
			Map<String,Object> map = new HashMap<>();

			//刀具号
			Integer nNumTool = dfKnifeUseInfo.getMacToolIndex();

			QueryWrapper<DfKnifeUseInfo> qwLastweek = new QueryWrapper<>();
			QueryWrapper<DfKnifeUseInfo> qwThisweek = new QueryWrapper<>();

			qwLastweek
					.between("create_time", startTimeLastweek, endTimeLastweek)
					.eq("dt_type","2")
					.eq("mac_tool_index",nNumTool)
					.eq("process",process);
			//上周换刀记录
			List<Rate3> listLastweek = dfKnifeUseInfoService.getKnifeLifeDistribution(qwLastweek);

			int ngLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getInte1()));
			int okLastweek = listLastweek.stream().collect(Collectors.summingInt(x -> x.getInte2()));

			if (okLastweek + ngLastweek !=0){
				Double passPoint = (double)okLastweek/(ngLastweek+okLastweek);
				String rate = String.format("%.2f",passPoint*100);
				map.put("rateLastweek",rate);

			}else {
				map.put("rateLastweek",null);
			}

			ArrayList<Object> xLastweek = new ArrayList<>();
			ArrayList<Object> yLastweekOk = new ArrayList<>();
			ArrayList<Object> yLastweekNg = new ArrayList<>();
			ArrayList<Object> yLastweekTotal = new ArrayList<>();
			for (Rate3 rate3LastWeek : listLastweek) {
				xLastweek.add(rate3LastWeek.getStr1());
				yLastweekNg.add(rate3LastWeek.getInte1());
				yLastweekOk.add(rate3LastWeek.getInte2());
				yLastweekTotal.add(rate3LastWeek.getInte1()+rate3LastWeek.getInte2());
			}
			map.put("nNumTool",nNumTool);
			map.put("xLastweek",xLastweek);
			map.put("yLastweekOk",yLastweekOk);
			map.put("yLastweekNg",yLastweekNg);
			map.put("yLastweekTotal",yLastweekTotal);


			qwThisweek
					.between("create_time", startTimeThisweek, endTimeThisweek)
					.eq("dt_type","2")
					.eq("mac_tool_index",nNumTool)
					.eq("process",process);
			//本周换刀记录
			List<Rate3> listThisweek = dfKnifeUseInfoService.getKnifeLifeDistribution(qwThisweek);

			int ngThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getInte1()));
			int okThisweek = listThisweek.stream().collect(Collectors.summingInt(x -> x.getInte2()));

			if (okThisweek + ngThisweek !=0){
				Double passPoint = (double)okThisweek/(ngThisweek+okThisweek);
				String rate = String.format("%.2f",passPoint*100);
				map.put("rateThisweek",rate);
			}else {
				map.put("rateThisweek",null);
			}
			ArrayList<Object> xThisweek = new ArrayList<>();
			ArrayList<Object> yThisweekOk = new ArrayList<>();
			ArrayList<Object> yThisweekNg = new ArrayList<>();
			ArrayList<Object> yThisweekTotal = new ArrayList<>();
			for (Rate3 rate3ThisWeek : listThisweek) {
				xThisweek.add(rate3ThisWeek.getStr1());
				yThisweekNg.add(rate3ThisWeek.getInte1());
				yThisweekOk.add(rate3ThisWeek.getInte2());
				yThisweekTotal.add(rate3ThisWeek.getInte1()+rate3ThisWeek.getInte2());
			}
			map.put("xThisweek",xThisweek);
			map.put("yThisweekNg",yThisweekNg);
			map.put("yThisweekOk",yThisweekOk);
			map.put("yThisweekTotal",yThisweekTotal);

			list.add(map);
		}

		return new Result(200,"查询成功",list);
	}

//	@GetMapping(value = "/getCarveLifetimeBycCarvePositionDownByCNC2And3")
//	@ApiOperation("砂轮寿命分布-下(CNC2,CNC3)")
//	public Result getCarveLifetimeBycCarvePositionDownByCNC2And3(
//			 String factoryId,
//			 String project,
//			 @RequestParam String process) {
//		String startTimeLastweek = TimeUtil.getLastweekFirstDay();
//		String endTimeLastweek = TimeUtil.getLastWeekEndTime();
//		String startTimeThisweek = TimeUtil.getThisweekFirstDay();
//		String endTimeThisweek = TimeUtil.getNowTimeByNormal();
//		QueryWrapper<DfKnifStatus> qwLastweek = new QueryWrapper<>();
//		QueryWrapper<DfKnifStatus> qwThisweek = new QueryWrapper<>();
//
//		qwLastweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as typeData ","n_num_tool","count(*) as shiftName")
//				.between("create_time", startTimeLastweek, endTimeLastweek)
////				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
////				.eq(StringUtils.isNotEmpty(project), "product_model",project)
//				.eq(StringUtils.isNotEmpty(process),"process", process)
//				.groupBy("n_num_tool","typeData")
//				.orderByAsc("n_num_tool","typeData");
//		List<DfKnifStatus> listLastweek = dfKnifStatusService.list(qwLastweek);
//
//		ArrayList<Object> last = new ArrayList<>();
//		Map<String, List<DfKnifStatus>> collect = listLastweek.stream().collect(Collectors.groupingBy(DfKnifStatus::getNNumTool));
//		for (Map.Entry<String, List<DfKnifStatus>> stringListEntry : collect.entrySet()) {
//			HashMap<String, Object> map = new HashMap<>();
//			List<DfKnifStatus> value = stringListEntry.getValue();
//			ArrayList<Object> xLastweek = new ArrayList<>();
//			ArrayList<Object> yLastweek = new ArrayList<>();
//			for (DfKnifStatus knifStatus : value) {
//				xLastweek.add(knifStatus.getTypeData());
//				yLastweek.add(knifStatus.getShiftName());
//				map.put("xLastweek",xLastweek);
//				map.put("yLastweek",yLastweek);
//			}
//			map.put("name",stringListEntry.getKey());
//			last.add(map);
//		}
//
//
//		qwThisweek.select("DATE_FORMAT(DATE_SUB(create_time,INTERVAL 7 HOUR ),'%m-%d') as typeData ","n_num_tool","count(*) as shiftName")
//				.between("create_time", startTimeThisweek, endTimeThisweek)
////				.eq(StringUtils.isNotEmpty(factoryId), "factory_id",factoryId)
////				.eq(StringUtils.isNotEmpty(project), "product_model",project)
//				.eq(StringUtils.isNotEmpty(process),"process", process)
//				.groupBy("n_num_tool","typeData")
//				.orderByAsc("n_num_tool","typeData");
//		List<DfKnifStatus> listThisweek = dfKnifStatusService.list(qwThisweek);
//
//		ArrayList<Object> thisWeek = new ArrayList<>();
//		Map<String, List<DfKnifStatus>> collectThis = listThisweek.stream().collect(Collectors.groupingBy(DfKnifStatus::getNNumTool));
//		for (Map.Entry<String, List<DfKnifStatus>> stringListEntry : collectThis.entrySet()) {
//			HashMap<String, Object> map = new HashMap<>();
//			ArrayList<Object> xThisweek = new ArrayList<>();
//			ArrayList<Object> yThisweek = new ArrayList<>();
//			List<DfKnifStatus> value = stringListEntry.getValue();
//			for (DfKnifStatus knifStatus : value) {
//				xThisweek.add(knifStatus.getTypeData());
//				yThisweek.add(knifStatus.getShiftName());
//				map.put("xThisweek",xThisweek);
//				map.put("yThisweek",yThisweek);
//			}
//			map.put("name",stringListEntry.getKey());
//			thisWeek.add(map);
//		}
//		HashMap<Object, Object> map = new HashMap<>();
//		map.put("last",last);
//		map.put("this",thisWeek);
//
//		return new Result(200,"查询成功",map);
//	}


	@GetMapping(value = "/getCarveLifetimeKPI")
	@ApiOperation("砂轮寿命分布-KpI")
	public Result getCarveLifetimeKPI(
			String factoryId
			, @ApiParam("CNC0,CNC1,CNC2,CNC3属于哪个界面传哪个") String process
			, String project) {
		String startTime = TimeUtil.getBeforMonthFirstDay();
		String endTime = TimeUtil.getNowTimeByNormal();

		//cnc0,1,2,3良率
		QueryWrapper<DfQmsIpqcWaigTotal> qwCnc0 = new QueryWrapper<>();
		qwCnc0.eq("tol.f_seq", process);
		qwCnc0.eq(StringUtils.isNotEmpty(factoryId), "tol.f_fac", factoryId)
				.eq(StringUtils.isNotEmpty(project), "tol.f_bigpro", project)
				.between("tol.f_time", startTime, endTime)
				.isNotNull("spot_check_count");
		List<Rate> rateCnc0 = dfQmsIpqcWaigTotalService.listCNC0OkRateOneMonth(qwCnc0);

		//精Q1良率
		QueryWrapper<DfQmsIpqcWaigTotal> qwJq1 = new QueryWrapper<>();
		qwJq1.eq("tol.f_seq", "精Q1");
		qwJq1.eq(StringUtils.isNotEmpty(factoryId), "tol.f_fac", factoryId)
				.eq(StringUtils.isNotEmpty(project), "tol.f_bigpro", project)
				.between("tol.f_time", startTime, endTime)
				.isNotNull("spot_check_count");
		List<Rate> rateJq1 = dfQmsIpqcWaigTotalService.listCNC0OkRateOneMonth(qwJq1);


		List<Object> dateListweekYear = new ArrayList<>();
		List<Object> rateListCnc0 = new ArrayList<>();
		List<Object> rateListJq1 = new ArrayList<>();
		for (Rate rate : rateCnc0) {
			dateListweekYear.add(rate.getDate());
			rateListCnc0.add(rate.getRate());
			Rate rateTemp = rateJq1.stream().filter(m -> rate.getDate().equals(m.getDate())).findAny().orElse(new Rate());
			rateListJq1.add(rateTemp.getRate());
		}

		Map<String, List<Object>> result = new HashMap<>();
		result.put("dateListweekYear", dateListweekYear);
		result.put("rateListCnc0", rateListCnc0);
		result.put("rateListJq1", rateListJq1);
		return new Result(200, "查询成功", result);
	}

	@GetMapping(value = "/getCarveLifetimeKPIV2")
	@ApiOperation("砂轮寿命分布-KpIV2")
	public Result getCarveLifetimeKPIV2(
			String factoryId
			, @ApiParam("CNC0,CNC1,CNC2,CNC3属于哪个界面传哪个") @RequestParam(required = true) String process
			, String project) {
		String startTime = TimeUtil.getBeforMonthFirstDay();
		String endTime = TimeUtil.getNowTimeByNormal();

		List<Rate3> rate = dfQmsIpqcWaigTotalService.listProcessOkRateOneMonth(factoryId,project,startTime,endTime,process);

		List<Object> dateListweekYear = new ArrayList<>();
		List<Object> rateListLead = new ArrayList<>();
		List<Object> rateListJq1 = new ArrayList<>();
		List<Object> rateListProcess = new ArrayList<>();
		Map<String, List<Object>> result = new HashMap<>();

		for (Rate3 rate3 : rate) {
			dateListweekYear.add(rate3.getStr1());
			rateListLead.add(rate3.getDou1());
			rateListJq1.add(rate3.getDou2());
			rateListProcess.add(rate3.getDou3());
		}

		result.put("dateListweekYear", dateListweekYear);
		result.put("rateListLead", rateListLead);
		result.put("rateListJq1", rateListJq1);
		result.put("rateListProcess", rateListProcess);
		return new Result(200, "查询成功", result);
	}


	@GetMapping("/insertData")
	@ApiOperation("添加数据")
	public Result insertData(int num) {
		String[] machineCode = {"M01", "M02","M03","M04","M05","M06","M07","M08","M09","M10"};
		String[] position = {"粗开轮廓", "精修轮廓", "粗开孔位", "精修孔位", "粗开⼤⾯", "粗开A/R/MC孔+轮廓", "精修A/R/MC孔+轮廓", "粗开S孔", "精修S孔", "粗开MIC孔", "精修MIC孔", "粗开⼤⾯和平台线", "精修⼤⾯和平台线", "精修平台线", "精修边缘"};
		String[] dao = {"D01", "D02","D03","D04","D05","D06","D07","D08","D09","D10"};
		List<DfCarveUniversal> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			DfCarveUniversal data = new DfCarveUniversal();
			boolean isOver = Math.random() < 0.8; // true:完的砂轮
			if (isOver) {
				data.setQuantityProcessed(150);
			} else {
				data.setQuantityProcessed((int)(150 * Math.random()));
				data.setCarvePosition(position[(int)(position.length * Math.random())]);
			}
			Timestamp t = new Timestamp(Timestamp.valueOf("2023-08-01 00:00:00").getTime() - (long)((25 * 24 * 60 * 60 * 1000) * Math.random()));
			data.setCreateTime(t);
			data.setUseTime(t);
			data.setMachineId(machineCode[(int)(machineCode.length * Math.random())]);
			data.setKnifeNumber(dao[(int)(dao.length * Math.random())]);
			list.add(data);
		}
//		for (DfCarveUniversal dfCarveUniversal : list) {
//			System.out.println(dfCarveUniversal);
//		}
		dfCarveUniversalService.saveBatch(list);
		return new Result(200, "成功添加" + list.size() + "条");
	}
}
