package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 湿度表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-21
 */
@Controller
@RequestMapping("/dfHumidity")
@CrossOrigin
@ResponseBody
@Api(tags = "湿度表")
public class DfHumidityController {

    @Autowired
    private DfHumidityService dfHumidityService;

	@Autowired
	private DfDustMonitorService dfDustMonitorService;

	@Autowired
	private DfLiableManService dfLiableManService;

	@Autowired
	private DfApprovalTimeService dfApprovalTimeService;

	@Autowired
	private DfAuditDetailService dfAuditDetailService;

	@Autowired
	private DfFlowDataService dfFlowDataService;


	@GetMapping("/importData")
	@ApiOperation("导入数据")
	public Result importData(MultipartFile file) throws Exception {
		//获取当前检测时间
		String checkTimeNow = TimeUtil.getNowTimeByNormal();

		//当前时间戳
		String checkTimeLong = TimeUtil.getNowTimeLong();

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 31);
		//落尘标准
		String standardTotal = strings[0][0];

		List<DfDustMonitor> list = new ArrayList<>();
		for (int i = 2;i<strings.length;i++){
			if (StringUtils.isEmpty(strings[i][2])){
				continue;
			}
			//标准
			Double standard = Double.valueOf(strings[i][0]);
			//位置
			String location = strings[i][2];

			for (int j = 10;j<=11;j++){
				if (strings[i][j]==null){
					continue;
				}
				Date checkTimeDate = sd.parse(strings[1][j]);
				Timestamp checkTime = new Timestamp(checkTimeDate.getTime());
				Double checkValue = Double.valueOf(strings[i][j]);
				String checkResult = "NG";
				if (checkValue<=standard){
					checkResult = "OK";
				}

				DfDustMonitor dfDustMonitor = new DfDustMonitor();
				dfDustMonitor.setFactory("10栋5F无尘车间");
				dfDustMonitor.setStandard(standard);
				dfDustMonitor.setLocation(location);
				dfDustMonitor.setCheckTime(checkTime);
				dfDustMonitor.setCheckValue(checkValue);
				dfDustMonitor.setCheckResult(checkResult);
				list.add(dfDustMonitor);
			}
			dfDustMonitorService.saveBatch(list);
		}


		for (DfDustMonitor entity:list){
			if ("NG".equals(entity.getCheckResult())){
				QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
				sqw.like("process_name","湿度");
				sqw.like("type","湿度");
				sqw.eq("problem_level","1");
				if(TimeUtil.getBimonthly()==0){
					sqw.like("bimonthly","双月");
				}else{
					sqw.like("bimonthly","单月");
				}
				List<DfLiableMan> lm =dfLiableManService.list(sqw);
				if (lm==null||lm.size()==0){
					return new Result(200,"没有相关湿度责任人数据，无法生成审批单");
				}
				//责任人编号
				StringBuilder manCode=new StringBuilder();
				//责任人
				StringBuilder manName=new StringBuilder();
				int manCount=0;
				for(DfLiableMan l:lm){
					if(manCount>0){
						manCode.append(",");
						manName.append(",");
					}
					manCode.append(l.getLiableManCode());
					manName.append(l.getLiableManName());
					manCount++;
				}

				DfAuditDetail aud = new DfAuditDetail();
				aud.setDataType("湿度");
				aud.setDepartment("湿度");
				aud.setAffectNum(1.0);
				aud.setControlStandard("落尘标准:" + standardTotal);
				aud.setImpactType("湿度");
				aud.setIsFaca("0");
				aud.setQuestionName("数据未达标");
				aud.setProcess("湿度");

				aud.setScenePractical("落尘_位置" +entity.getLocation() +"_数据不符，超出参考限样");
				aud.setReportMan("系统");
				aud.setCreateName("系统");

				aud.setReportTime(Timestamp.valueOf(checkTimeNow));
				aud.setOccurrenceTime(Timestamp.valueOf(checkTimeNow));
				aud.setIpqcNumber(checkTimeLong);

				aud.setProcess("湿度");
				aud.setQuestionType("数据不符");
				aud.setDecisionLevel("Level1");
				aud.setHandlingSug("全检风险批");
				aud.setResponsible(manName.toString());
				aud.setResponsibleId(manCode.toString());
				dfAuditDetailService.save(aud);

				DfFlowData fd = new DfFlowData();
				fd.setFlowLevel(1);
				fd.setDataType(aud.getDataType());
				fd.setFlowType(aud.getDataType());
				fd.setName("湿度"+ aud.getQuestionName() + "_NG_" + checkTimeNow);
				fd.setDataId(aud.getId());
				fd.setStatus("待确认");
				fd.setCreateName("系统");
				fd.setCreateUserId("系统");
				fd.setNowLevelUser(aud.getResponsibleId());
				fd.setNowLevelUserName(aud.getResponsible());
				fd.setLevel1PushTime(Timestamp.valueOf(checkTimeNow));
				fd.setFlowLevelName(aud.getDecisionLevel());

				QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
				atQw.eq("type","湿度")
						.last("limit 1");
				DfApprovalTime at= dfApprovalTimeService.getOne(atQw);
				if(null!=at){
					if(fd.getFlowLevelName().equals("Level1")){
						fd.setReadTimeMax(at.getReadTimeLevel1());
						fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
					}else  if(fd.getFlowLevelName().equals("Level2")){
						fd.setReadTimeMax(at.getReadTimeLevel2());
						fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
					}else  if(fd.getFlowLevelName().equals("Level3")){
						fd.setReadTimeMax(at.getReadTimeLevel3());
						fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
					}
				}
				//设置显示人
				fd.setShowApprover(fd.getNowLevelUserName());
				dfFlowDataService.save(fd);
			}
		}
		return new Result(200,"成功导入"+list.size()+"条数据");
	}

    @GetMapping("/getNowData")
    @ApiOperation("获取湿度最新数据")
    public Result getNowData(){
        QueryWrapper<DfHumidity> ew = new QueryWrapper<>();
        ew.orderByDesc("check_time")
                .last("limit 1");
        DfHumidity dfHumidity = dfHumidityService.getOne(ew);
        return new Result(200,"获取湿度最新数据成功",dfHumidity.getCheckValue());
    }

    @GetMapping("/getHumidityNew")
    @ApiOperation("获取湿度全部数据")
    public Result getHumidityNew(@RequestParam String startDate , @RequestParam String endDate, String factory, String linebody, String project, String color){
        String startTime = startDate + " 07:00:00";
        String endTime = endDate + " 23:59:59";
        QueryWrapper<DfHumidity> ew = new QueryWrapper<>();
        ew.select("date_format(check_time, '%m-%d') time","avg(lsl) lsl","avg(usl) usl","avg(check_value) check_value")
                .between("check_time", startTime,endTime)
                .groupBy("time")
                .orderByDesc("time");
        List<Map<String,Object>> list = dfHumidityService.listMaps(ew);
        HashMap<Object, Object> map = new HashMap<>();
        ArrayList<Object> checkValue = new ArrayList<>();
        ArrayList<Object> up = new ArrayList<>();
        ArrayList<Object> down = new ArrayList<>();
        ArrayList<Object> date = new ArrayList<>();
        for (Map<String,Object> one : list) {
            checkValue.add(one.get("check_value"));
            up.add(one.get("usl"));
            down.add(one.get("lsl"));
            date.add(one.get("time"));
        }
        QueryWrapper<DfHumidity> ew2 = new QueryWrapper<>();
        ew2.orderByDesc("check_time")
                .last("limit 1");
        DfHumidity dfHumidity = dfHumidityService.getOne(ew2);
        map.put("checkValue",checkValue);
        map.put("up",up);
        map.put("down",down);
        map.put("date",date);
        map.put("newOne",dfHumidity.getCheckValue());
        return new Result(200,"获取湿度最新数据成功",map);
    }

	@ApiOperation("新增或修改")
	@PostMapping("/saveOrUpdate")
	public Object saveOrUpdate(@RequestBody DfHumidity datas){
		if (null != datas.getId()) {
			if (dfHumidityService.updateById(datas)) {
				return new Result(200, "保存成功");
			} else {
				return new Result(500, "保存失败");
			}
		} else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
			if (dfHumidityService.save(datas)) {
				return new Result(200, "保存成功");
			} else {
				return new Result(500, "保存失败");
			}
		}
	}

	@ApiOperation("删除")
	@GetMapping("/delete")
	public Object deleteById(@RequestParam  String id){
		if (dfHumidityService.removeById(id)){
			return new Result(200, "删除成功");
		}else {
			return new Result(500, "删除失败");
		}
	}

	@GetMapping("/conditionQuery")
	@ApiOperation("条件查询")
	public Object conditionQuery(int page,int limit,String factoryArea,String factory,String checkPosition,String startDate,String endDate){
		Page<DfHumidity> pages = new Page<>(page, limit);
		QueryWrapper<DfHumidity> ew = new QueryWrapper<>();
		ew.ge(StringUtils.isNotEmpty(startDate),"check_time",startDate + " 00:00:00")
				.le(StringUtils.isNotEmpty(endDate),"check_time",endDate + " 23:59:59")
				.eq(StringUtils.isNotEmpty(factoryArea), "factory_area", factoryArea)
				.eq(StringUtils.isNotEmpty(factory), "factory", factory)
				.eq(StringUtils.isNotEmpty(checkPosition), "check_position", checkPosition)
				.orderByDesc("check_time");
		IPage<DfHumidity> list = dfHumidityService.page(pages, ew);
		return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
	}


}
