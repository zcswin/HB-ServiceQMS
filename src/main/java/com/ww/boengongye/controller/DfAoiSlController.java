package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiSl;
import com.ww.boengongye.entity.DfAoiSlThick;
import com.ww.boengongye.service.DfAoiSlService;
import com.ww.boengongye.service.DfAoiSlThickService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-10
 */
@RestController
@RequestMapping("/dfAoiSl")
@CrossOrigin
@Api(tags = "丝印")
public class DfAoiSlController {

	@Autowired
	private DfAoiSlService dfAoiSlService;
	@Autowired
	private DfAoiSlThickService dfAoiSlThickService;

	private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);

	@ApiOperation("丝印录入接口")
	@PostMapping("/import")
	@Transactional
	public Object slImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {

		try {
			if (file.isEmpty()){
				return new Result(0, "请选择文件");
			}
			String fileName = file.getOriginalFilename();
			if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
				return new Result(0, "请上传xlsx或xls格式的文件");
			}
			try {
				List<DfAoiSl> list= dfAoiSlService.importSL(file);
				if (dfAoiSlService.saveBatch(list)){
					return  new Result(0, "上传成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new Result(500, "上传失败");
		} catch (Exception e) {
			logger.error("导入excel接口异常", e);
		}
		return new Result(500, "接口异常");
	}


	@ApiOperation("油墨区类型下拉列表(SCI)")
	@GetMapping("/getTypeList")
	public Object getTypeList() throws Exception {
		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
		ew.select("distinct group_characteristics");
		List<DfAoiSl> list = dfAoiSlService.list(ew);
		return new Result(200, "成功",list);
	}

//
//	@ApiOperation("丝印油墨区过程L趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapL")
//	public Object screenPrintingInkAreaProcessTrendMap(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dlf2) dlf2","avg(l_up) l_up,avg(l_down) l_down")
//		.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//			startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//		.and(wrapper -> wrapper.eq("type","一层BG")
//			.or().eq("type", "二层BG")
//			.or().eq("type","三层BG")
//			.or().eq("type","四层BG")
//			.or().eq("type","五层BG")
//			.or().eq("type","终烤后BG"))
//		.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> dlf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			dlf2.add(one.getDlf2());
//			up.add(one.getLUp());
//			down.add(one.getLDown());
//		}
//		map.put("type",type);
//		map.put("dlf2",dlf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//
//	@ApiOperation("丝印油墨区过程A趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapA")
//	public Object screenPrintingInkAreaProcessTrendMapA(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(daf2)","avg(a_up),avg(a_down)")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层BG")
//						.or().eq("type", "二层BG")
//						.or().eq("type","三层BG")
//						.or().eq("type","四层BG")
//						.or().eq("type","五层BG")
//						.or().eq("type","终烤后BG"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> daf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			daf2.add(one.getDaf2());
//			up.add(one.getAUp());
//			down.add(one.getADown());
//		}
//		map.put("type",type);
//		map.put("daf2",daf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//	@ApiOperation("丝印油墨区过程B趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapB")
//	public Object screenPrintingInkAreaProcessTrendMapB(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dbf2) dbf2","avg(b_up) b_up,avg(b_down) b_down")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层BG")
//						.or().eq("type", "二层BG")
//						.or().eq("type","三层BG")
//						.or().eq("type","四层BG")
//						.or().eq("type","五层BG")
//						.or().eq("type","终烤后BG"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> dbf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			dbf2.add(one.getDbf2());
//			up.add(one.getBUp());
//			down.add(one.getBDown());
//		}
//		map.put("type",type);
//		map.put("dbf2",dbf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//	@ApiOperation("丝印油墨区过程E94趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapE94")
//	public Object screenPrintingInkAreaProcessTrendMapE94(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dbf2) dbf2")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层BG")
//						.or().eq("type", "二层BG")
//						.or().eq("type","三层BG")
//						.or().eq("type","四层BG")
//						.or().eq("type","五层BG")
//						.or().eq("type","终烤后BG"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> e94 = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			e94.add(one.getDe94f2());
//		}
//		map.put("type",type);
//		map.put("e94",e94);
//		return new Result(500, "查询成功",map);
//	}
//
//
//	@ApiOperation("LOGO油墨区过程L趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapLLOGO")
//	public Object screenPrintingInkAreaProcessTrendMapLLOGO(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dlf2) dlf2","avg(l_up) l_up,avg(l_down) l_down")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层LOGO")
//						.or().eq("type", "二层LOGO")
//						.or().eq("type","三层LOGO")
//						.or().eq("type","四层LOGO")
//						.or().eq("type","五层LOGO")
//						.or().eq("type","终烤后LOGO"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> dlf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			dlf2.add(one.getDlf2());
//			up.add(one.getLUp());
//			down.add(one.getLDown());
//		}
//		map.put("type",type);
//		map.put("dlf2",dlf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//
//	@ApiOperation("LOGO油墨区过程A趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapALOGO")
//	public Object screenPrintingInkAreaProcessTrendMapALOGO(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(daf2)","avg(a_up),avg(a_down)")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层LOGO")
//						.or().eq("type", "二层LOGO")
//						.or().eq("type","三层LOGO")
//						.or().eq("type","四层LOGO")
//						.or().eq("type","五层LOGO")
//						.or().eq("type","终烤后LOGO"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> daf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			daf2.add(one.getDaf2());
//			up.add(one.getAUp());
//			down.add(one.getADown());
//		}
//		map.put("type",type);
//		map.put("daf2",daf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//	@ApiOperation("LOGO油墨区过程B趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapBLOGO")
//	public Object screenPrintingInkAreaProcessTrendMapBLOGO(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dbf2) dbf2","avg(b_up) b_up,avg(b_down) b_down")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层LOGO")
//						.or().eq("type", "二层LOGO")
//						.or().eq("type","三层LOGO")
//						.or().eq("type","四层LOGO")
//						.or().eq("type","五层LOGO")
//						.or().eq("type","终烤后LOGO"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> dbf2 = new ArrayList<>();
//		ArrayList<Object> up = new ArrayList<>();
//		ArrayList<Object> down = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			dbf2.add(one.getDbf2());
//			up.add(one.getBUp());
//			down.add(one.getBDown());
//		}
//		map.put("type",type);
//		map.put("dbf2",dbf2);
//		map.put("up",up);
//		map.put("down",down);
//		return new Result(500, "查询成功",map);
//	}
//
//	@ApiOperation("LOGO油墨区过程E94趋势图")
//	@PostMapping("/screenPrintingInkAreaProcessTrendMapE94LOGO")
//	public Object screenPrintingInkAreaProcessTrendMapE94LOGO(String startDate , String endDate) throws Exception {
//		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
//		ew.select("type","avg(dbf2) dbf2")
//				.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
//						startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
//				.and(wrapper -> wrapper.eq("type","一层LOGO")
//						.or().eq("type", "二层LOGO")
//						.or().eq("type","三层LOGO")
//						.or().eq("type","四层LOGO")
//						.or().eq("type","五层LOGO")
//						.or().eq("type","终烤后LOGO"))
//				.groupBy("type");
//		List<DfAoiSl> list = dfAoiSlService.list(ew);
//		HashMap<Object, Object> map = new HashMap<>();
//		ArrayList<Object> type = new ArrayList<>();
//		ArrayList<Object> e94 = new ArrayList<>();
//		for (DfAoiSl one : list) {
//			type.add(one.getType());
//			e94.add(one.getDe94f2());
//		}
//		map.put("type",type);
//		map.put("e94",e94);
//		return new Result(500, "查询成功",map);
//	}

	@ApiOperation("过程颜色分布model传1,clazz传A或B,type传类型下拉列表(BG/LOGO)")
	@GetMapping("/usualThick")
		public Object usualThick(String factory,String linebody ,String project ,
				@RequestParam String startDate ,
				@RequestParam String endDate ,
//							 @RequestParam int model,
				 String clazz,
				@ApiParam("传BG/LOGO") @RequestParam String type) throws Exception {
			ArrayList<Object> arrayList = new ArrayList<>();
		switch (type){
			case "BG":
				//dlf2
				QueryWrapper<DfAoiSl> ew1 = new QueryWrapper<>();
				ew1 = generateWrapper(ew1,1,startDate,endDate,clazz,type);
				List<DfAoiSl> list1 = dfAoiSlService.list(ew1);
				Map map1 = generateMap(1,list1);
				//daf2
				QueryWrapper<DfAoiSl> ew2 = new QueryWrapper<>();
				ew2 = generateWrapper(ew2,2,startDate,endDate,clazz,type);
				List<DfAoiSl> list2 = dfAoiSlService.list(ew2);
				Map map2 = generateMap(2,list2);
				//dbf2
				QueryWrapper<DfAoiSl> ew3 = new QueryWrapper<>();
				ew3 = generateWrapper(ew3,3,startDate,endDate,clazz,type);
				List<DfAoiSl> list3 = dfAoiSlService.list(ew3);
				Map map3 = generateMap(3,list3);
				//de94
				QueryWrapper<DfAoiSl> ew4 = new QueryWrapper<>();
				ew4 = generateWrapper(ew4,4,startDate,endDate,clazz,type);
				List<DfAoiSl> list = dfAoiSlService.list(ew4);
				Map map4 = generateMap(4,list);
				arrayList.add(map1);
				arrayList.add(map2);
				arrayList.add(map3);
				arrayList.add(map4);
				break;
			case "LOGO":
				//dlf2
				QueryWrapper<DfAoiSl> ew5 = new QueryWrapper<>();
				ew5 = generateWrapper(ew5,5,startDate,endDate,clazz,type);
				List<DfAoiSl> list5 = dfAoiSlService.list(ew5);
				Map map5 = generateMap(5,list5);
				//daf2
				QueryWrapper<DfAoiSl> ew6 = new QueryWrapper<>();
				ew6 = generateWrapper(ew6,6,startDate,endDate,clazz,type);
				List<DfAoiSl> list6 = dfAoiSlService.list(ew6);
				Map map6 = generateMap(6,list6);
				//dbf2
				QueryWrapper<DfAoiSl> ew7 = new QueryWrapper<>();
				ew7 = generateWrapper(ew7,7,startDate,endDate,clazz,type);
				List<DfAoiSl> list7 = dfAoiSlService.list(ew7);
				Map map7 = generateMap(7,list7);
				//de94
				QueryWrapper<DfAoiSl> ew8 = new QueryWrapper<>();
				ew8 = generateWrapper(ew8,8,startDate,endDate,clazz,type);
				List<DfAoiSl> list8 = dfAoiSlService.list(ew8);
				Map map8 = generateMap(8,list8);
				arrayList.add(map5);
				arrayList.add(map6);
				arrayList.add(map7);
				arrayList.add(map8);
				break;
		}

		return new Result(200, "查询成功",arrayList);
	}

	private Map generateMap(int type,List<DfAoiSl> list) {
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> up = new ArrayList<>();
		ArrayList<Object> down = new ArrayList<>();
		ArrayList<Object> data = new ArrayList<>();
		switch(type){
			case 1:
				for (DfAoiSl one : list) {
					data.add(one.getDlf2());
					up.add("0.9");
					down.add("-0.8");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","SCI-dlf2");
				break;
			case 2:
				for (DfAoiSl one : list) {
					data.add(one.getDaf2());
					up.add("0.3");
					down.add("-0.2");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","SCI-daf2");
				break;
			case 3:
				for (DfAoiSl one : list) {
					data.add(one.getDbf2());
					up.add("0.3");
					down.add("-0.2");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","SCI-dbf2");
				break;
			case 4:
				for (DfAoiSl one : list) {
					data.add(one.getDe94f2());
					up.add(null);
					down.add(null);
				}
				map.put("data",data);
				map.put("name","SCI-de94f2");
				break;
			case 5:
				for (DfAoiSl one : list) {
					data.add(one.getDlf2());
					up.add("0.9");
					down.add("-0.7");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","LOGO-dlf2");
				break;
			case 6:
				for (DfAoiSl one : list) {
					data.add(one.getDaf2());
					up.add("0.4");
					down.add("-0.3");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","LOGO-daf2");
				break;
			case 7:
				for (DfAoiSl one : list) {
					data.add(one.getDbf2());
					up.add("0.6");
					down.add("-0.7");
				}
				map.put("data",data);
				map.put("up",up);
				map.put("down",down);
				map.put("name","LOGO-dbf2");
				break;
			case 8:
				for (DfAoiSl one : list) {
					data.add(one.getDe94f2());
				}
				map.put("data",data);
				map.put("name","LOGO-de94f2");
				break;
		}
		map.put("x", Arrays.asList("一层","二层","三层","四层","五层","终烤"));
		return map;
	}

	private QueryWrapper<DfAoiSl> generateWrapper(QueryWrapper<DfAoiSl> ew , int type,String startDate , String endDate ,String clazz,String groupCharacteristics) throws ParseException {
		switch(type){
			case 1:
				ew.select("type","avg(dlf2) dlf2","avg(l_up) l_up,avg(l_down) l_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层BG")
								.or().eq("type", "二层BG")
								.or().eq("type","三层BG")
								.or().eq("type","四层BG")
								.or().eq("type","五层BG")
								.or().eq("type","终烤后BG"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 2:
				ew.select("type","avg(daf2) daf2","avg(a_up) a_up,avg(a_down) a_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层BG")
								.or().eq("type", "二层BG")
								.or().eq("type","三层BG")
								.or().eq("type","四层BG")
								.or().eq("type","五层BG")
								.or().eq("type","终烤后BG"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 3:
				ew.select("type","avg(dbf2) dbf2","avg(b_up) b_up,avg(b_down) b_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层BG")
								.or().eq("type", "二层BG")
								.or().eq("type","三层BG")
								.or().eq("type","四层BG")
								.or().eq("type","五层BG")
								.or().eq("type","终烤后BG"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 4:
				ew.select("type","avg(de94f2) de94f2","avg(b_up) b_up,avg(b_down) b_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层BG")
								.or().eq("type", "二层BG")
								.or().eq("type","三层BG")
								.or().eq("type","四层BG")
								.or().eq("type","五层BG")
								.or().eq("type","终烤后BG"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 5:
				ew.select("type","avg(dlf2) dlf2","avg(l_up) l_up,avg(l_down) l_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层LOGO")
								.or().eq("type", "二层LOGO")
								.or().eq("type","三层LOGO")
								.or().eq("type","四层LOGO")
								.or().eq("type","五层LOGO")
								.or().eq("type","终烤后LOGO"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 6:
				ew.select("type","avg(daf2) daf2","avg(a_up) a_up,avg(a_down) a_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层LOGO")
								.or().eq("type", "二层LOGO")
								.or().eq("type","三层LOGO")
								.or().eq("type","四层LOGO")
								.or().eq("type","五层LOGO")
								.or().eq("type","终烤后LOGO"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 7:
				ew.select("type","avg(dbf2) dbf2","avg(b_up) b_up,avg(b_down) b_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层LOGO")
								.or().eq("type", "二层LOGO")
								.or().eq("type","三层LOGO")
								.or().eq("type","四层LOGO")
								.or().eq("type","五层LOGO")
								.or().eq("type","终烤后LOGO"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
			case 8:
				ew.select("type","avg(de94f2) de94f2","avg(b_up) b_up,avg(b_down) b_down")
						.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "time",
								startDate+" 07:00:00", TimeUtil.getNextDay(endDate) + " 07:00:00")
						.eq(StringUtils.isNotEmpty(clazz),"clazz",clazz)
						.and(wrapper -> wrapper.eq("type","一层LOGO")
								.or().eq("type", "二层LOGO")
								.or().eq("type","三层LOGO")
								.or().eq("type","四层LOGO")
								.or().eq("type","五层LOGO")
								.or().eq("type","终烤后LOGO"))
//						.eq("group_characteristics",groupCharacteristics)
						.groupBy("type");
				break;
		}
		return ew;
	}

	@ApiOperation("油墨总厚良率")
	@GetMapping("/totalInkThicknessYield")
	public Object totalInkThicknessYield(String startDate , String endDate) throws Exception {
		QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
		ew.select("date_format(date_sub(CHECK_TIME,INTERVAL 7 HOUR ),'%m-%d') date", "sum(if(CHECK_VALUE >= ISOLATION_DOWN AND CHECK_VALUE <= ISOLATION_UP,1,0)) /count(*) rate")
			.between(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate), "CHECK_TIME",
				startDate+" 00:00:00", TimeUtil.getNextDay(endDate) + " 00:00:00")
				.ne("WEEKDAY(DATE_SUB(CHECK_TIME, INTERVAL 7 HOUR))", 6)   // 去掉周日的数据
			.groupBy("date")
			.orderByAsc("date");
		List<Map<String, Object>> list = dfAoiSlThickService.listMaps(ew);
		HashMap<Object, Object> map = new HashMap<>();
		ArrayList<Object> date = new ArrayList<>();
		ArrayList<Object> result = new ArrayList<>();
		for (Map<String, Object> stringObjectMap : list) {
			date.add(stringObjectMap.get("date"));
			result.add(stringObjectMap.get("rate"));
		}
		map.put("date",date);
		map.put("result",result);
		return new Result(200, "查询成功",map);
	}


	//todo 目前给的模板没有提供*L,A,B标准
	@ApiOperation("下方管制图-油墨厚度变化(近50片l(f2)趋势)")
	@GetMapping("/getTrendRecent50")
	public Object getTrendRecent50(String factory,String project,String linebody,String color,
			@RequestParam String startDate , @RequestParam String endDate) throws ParseException, ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		List<String> asList = Arrays.asList("单印LOGO","一层LOGO", "二层LOGO", "三层LOGO", "四层LOGO", "五层LOGO");
		ArrayList<Object> arrayList = new ArrayList<>();
		for (String one : asList) {
			QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
			ew.select("time","lf2","l_up","l_down").between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"time",startTime,endTime)
					.eq("type",one)
					.eq(StringUtils.isNotEmpty(color),"color",color)
					.orderByDesc("time")
					.last("limit 50");
			List<DfAoiSl> list = dfAoiSlService.list(ew);
			HashMap<Object, Object> map = new HashMap<>();
			ArrayList<Object> listUp = new ArrayList<>();
			ArrayList<Object> listDown = new ArrayList<>();
			ArrayList<Object> listStandard = new ArrayList<>();
			ArrayList<Object> value = new ArrayList<>();
			for (DfAoiSl dfAoiSl : list) {
				listUp.add(dfAoiSl.getLUp());
				listDown.add(dfAoiSl.getLDown());
				if (dfAoiSl.getLUp() != null && dfAoiSl.getLDown() != null){
					listStandard.add(dfAoiSl.getLUp().add(dfAoiSl.getLDown()) );
				}else {
					listStandard.add(null);
				}
				value.add(dfAoiSl.getDlf2());
			}
			map.put("listUp",listUp);
			map.put("listDown",listDown);
			map.put("listStandard",listStandard);
			map.put("value",value);
			map.put("name",one);
			arrayList.add(map);
		}

		return new Result(200, "查询成功", arrayList);
	}

	@ApiOperation("下方管制图-油墨厚度变化近50片趋势,点击丝印LOGO,丝印一层,获取层数最近50片数据)")
	@GetMapping("/getTrendRecent50ByOneType")
	public Object getTrendRecent50ByOneType(@ApiParam(value = "传丝印LOGO,丝印一层...") @RequestParam(required = false) String name ,
									 String factory,String project,String linebody,String color) throws ParseException {
		List<String> asList = new ArrayList<>();
		switch (name){
			case "全工序":
				asList = Arrays.asList("Logo 油墨厚度","一层 油墨厚度", "二层 油墨厚度", "三层 油墨厚度",
						"四层 油墨厚度", "五层 油墨厚度","光油 油墨厚度");
				break;
			case "丝印LOGO":
				asList = Arrays.asList("Logo 油墨厚度");
				break;
			case "丝印一层":
				asList = Arrays.asList("一层 油墨厚度");
				break;
			case "丝印二层":
				asList = Arrays.asList( "二层 油墨厚度");
				break;
			case "丝印三层":
				asList = Arrays.asList( "三层 油墨厚度");
				break;
			case "丝印四层":
				asList = Arrays.asList("四层 油墨厚度");
				break;
			case "丝印五层":
				asList = Arrays.asList("五层 油墨厚度");
				break;
			case "丝印六层":
				asList = Arrays.asList("光油 油墨厚度");
				break;
			default:
				asList = Arrays.asList("Logo 油墨厚度","一层 油墨厚度", "二层 油墨厚度", "三层 油墨厚度",
						"四层 油墨厚度", "五层 油墨厚度","光油 油墨厚度");
				break;
		}

		ArrayList<Object> arrayList = new ArrayList<>();
		for (String one : asList) {
			QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
			ew.select("check_time","check_value","isolation_up","isolation_down","standard")
//					.between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"check_time",startTime,endTime)
					.eq("name",one)
					.eq(StringUtils.isNotEmpty(color),"color",color)
					.orderByDesc("check_time")
					.last("limit 50");
			List<DfAoiSlThick> list = dfAoiSlThickService.list(ew);
			HashMap<Object, Object> map = new HashMap<>();
			ArrayList<Object> listUp = new ArrayList<>();
			ArrayList<Object> listDown = new ArrayList<>();
			ArrayList<Object> listStandard = new ArrayList<>();
			ArrayList<Object> value = new ArrayList<>();
			for (DfAoiSlThick dfAoiSlThick : list) {
				listUp.add(dfAoiSlThick.getIsolationUp());
				listDown.add(dfAoiSlThick.getIsolationDown());
				listStandard.add(dfAoiSlThick.getStandard());
				value.add(dfAoiSlThick.getCheckValue());
			}
			map.put("listUp",listUp);
			map.put("listDown",listDown);
			map.put("listStandard",listStandard);
			map.put("value",value);
			map.put("name",one);
			arrayList.add(map);
		}

		return new Result(200, "查询成功", arrayList);
	}


//	@ApiOperation("下方管制图-油墨厚度变化近50片趋势)")
//	@GetMapping("/getTrendRecent50V2")
//	public Object getTrendRecent50V2(@ApiParam("传丝印LOGO") String name ,
//									 String factory,String project,String linebody,String color
//								  /* @RequestParam String startDate , @RequestParam String endDate*/) throws ParseException, ParseException {
////		String startTime = startDate + " 07:00:00";
////		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//		List<String> asList = Arrays.asList("Logo 油墨厚度","一层 油墨厚度", "二层 油墨厚度", "三层 油墨厚度",
//				"四层 油墨厚度", "五层 油墨厚度","光油 油墨厚度");
//		ArrayList<Object> arrayList = new ArrayList<>();
//		for (String one : asList) {
//			QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
//			ew.select("check_time","check_value","isolation_up","isolation_down","standard")
////					.between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"check_time",startTime,endTime)
//					.eq("name",one)
//					.eq(StringUtils.isNotEmpty(color),"color",color)
//					.orderByDesc("check_time")
//					.last("limit 50");
//			List<DfAoiSlThick> list = dfAoiSlThickService.list(ew);
//			HashMap<Object, Object> map = new HashMap<>();
//			ArrayList<Object> listUp = new ArrayList<>();
//			ArrayList<Object> listDown = new ArrayList<>();
//			ArrayList<Object> listStandard = new ArrayList<>();
//			ArrayList<Object> value = new ArrayList<>();
//			for (DfAoiSlThick dfAoiSlThick : list) {
//				listUp.add(dfAoiSlThick.getIsolationUp());
//				listDown.add(dfAoiSlThick.getIsolationDown());
//				listStandard.add(dfAoiSlThick.getStandard());
//				value.add(dfAoiSlThick.getCheckValue());
//			}
//			map.put("listUp",listUp);
//			map.put("listDown",listDown);
//			map.put("listStandard",listStandard);
//			map.put("value",value);
//			map.put("name",one);
//			arrayList.add(map);
//		}
//
//		return new Result(200, "查询成功", arrayList);
//	}

	@ApiOperation("点击名称,传名字分别获取三个点位最近50条数据")
	@GetMapping("/getThreePosition")
	public Object getThreePosition(String factory,String project,String linebody,String color,
									 @ApiParam("传名称") @RequestParam String name
									 /*@RequestParam String startDate , @RequestParam String endDate*/) throws ParseException, ParseException {
//		String startTime = startDate + " 07:00:00";
//		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
		//查询所有点位
		QueryWrapper<DfAoiSlThick> ew1 = new QueryWrapper<>();
		ew1.select("distinct position");
		List<String> positions = dfAoiSlThickService.list(ew1).stream().map(DfAoiSlThick::getPosition).collect(Collectors.toList());
		ArrayList<Object> arrayList = new ArrayList<>();
		for (String one : positions) {
			QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
			ew.select("check_time","check_value","isolation_up","isolation_down","standard")
//					.between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"check_time",startTime,endTime)
					.eq("name",name)
					.eq("position",one)
					.eq(StringUtils.isNotEmpty(color),"color",color)
					.orderByDesc("check_time")
					.last("limit 50");
			List<DfAoiSlThick> list = dfAoiSlThickService.list(ew);
			HashMap<Object, Object> map = new HashMap<>();
			ArrayList<Object> listUp = new ArrayList<>();
			ArrayList<Object> listDown = new ArrayList<>();
			ArrayList<Object> listStandard = new ArrayList<>();
			ArrayList<Object> value = new ArrayList<>();
			for (DfAoiSlThick dfAoiSlThick : list) {
				listUp.add(dfAoiSlThick.getIsolationUp());
				listDown.add(dfAoiSlThick.getIsolationDown());
				listStandard.add(dfAoiSlThick.getStandard());
				value.add(dfAoiSlThick.getCheckValue());
			}
			map.put("listUp",listUp);
			map.put("listDown",listDown);
			map.put("listStandard",listStandard);
			map.put("value",value);
			map.put("name",one);
			map.put("position",one);
			arrayList.add(map);
		}

		return new Result(200, "查询成功", arrayList);
	}

}
