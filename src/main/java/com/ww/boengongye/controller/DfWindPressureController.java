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
 * 风压点检表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-18
 */
@Controller
@RequestMapping("/dfWindPressure")
@CrossOrigin
@ResponseBody
@Api(tags = "风压点检表")
public class DfWindPressureController {
    @Autowired
    private DfWindPressureService dfWindPressureService;

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
                sqw.like("process_name","风压");
                sqw.like("type","风压");
                sqw.eq("problem_level","1");
                if(TimeUtil.getBimonthly()==0){
                    sqw.like("bimonthly","双月");
                }else{
                    sqw.like("bimonthly","单月");
                }
                List<DfLiableMan> lm =dfLiableManService.list(sqw);
                if (lm==null||lm.size()==0){
                    return new Result(200,"没有相关风压责任人数据，无法生成审批单");
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
                aud.setDataType("风压");
                aud.setDepartment("风压");
                aud.setAffectNum(1.0);
                aud.setControlStandard("落尘标准:" + standardTotal);
                aud.setImpactType("风压");
                aud.setIsFaca("0");
                aud.setQuestionName("数据未达标");
                aud.setProcess("风压");

                aud.setScenePractical("落尘_位置" +entity.getLocation() +"_数据不符，超出参考限样");
                aud.setReportMan("系统");
                aud.setCreateName("系统");

                aud.setReportTime(Timestamp.valueOf(checkTimeNow));
                aud.setOccurrenceTime(Timestamp.valueOf(checkTimeNow));
                aud.setIpqcNumber(checkTimeLong);

                aud.setProcess("风压");
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
                fd.setName("风压"+ aud.getQuestionName() + "_NG_" + checkTimeNow);
                fd.setDataId(aud.getId());
                fd.setStatus("待确认");
                fd.setCreateName("系统");
                fd.setCreateUserId("系统");
                fd.setNowLevelUser(aud.getResponsibleId());
                fd.setNowLevelUserName(aud.getResponsible());
                fd.setLevel1PushTime(Timestamp.valueOf(checkTimeNow));
                fd.setFlowLevelName(aud.getDecisionLevel());

                QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
                atQw.eq("type","风压")
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
    @ApiOperation("获取风压最新数据")
    public Result getNowData(){
        QueryWrapper<DfWindPressure> ew = new QueryWrapper<>();
        ew.orderByDesc("check_time")
                .last("limit 1");
        DfWindPressure dfWindPressure = dfWindPressureService.getOne(ew);
        return new Result(200,"获取风压最新数据成功",dfWindPressure.getCheckValue());
    }

    @GetMapping("/getPressureNow")
    @ApiOperation("获取风压")
    public Result getPressureNow(){
        QueryWrapper<DfWindPressure> ew = new QueryWrapper<>();
        ew.select("date_format(check_time, '%m-%d') time","avg(standard) standard","avg(check_value) check_value,")
                .groupBy("time")
                .orderByDesc("time");
        List<Map<String,Object>> list = dfWindPressureService.listMaps(ew);
        HashMap<Object, Object> map = new HashMap<>();
        ArrayList<Object> checkValue = new ArrayList<>();
        ArrayList<Object> standard = new ArrayList<>();
        ArrayList<Object> date = new ArrayList<>();
        for (Map<String,Object> one : list) {
            checkValue.add(one.get("check_value"));
            standard.add(one.get("standard"));
            date.add(one.get("time"));
        }
        QueryWrapper<DfWindPressure> ew2 = new QueryWrapper<>();
        ew2.orderByDesc("check_time")
                .last("limit 1");
        DfWindPressure dfWindPressure = dfWindPressureService.getOne(ew2);
        map.put("checkValue",checkValue);
        map.put("standard",standard);
        map.put("date",date);
        map.put("newOne",dfWindPressure.getCheckValue());
        return new Result(200,"获取风压最新数据成功",map);
    }


    @GetMapping("/getWindPressureList")
    @ApiOperation("获取风压折线图")
    public Result getWindPressureList(
            String factory, String lineBody,
            @RequestParam String startDate, @RequestParam String endDate
            ,String project,String color
    ){
        Map<String,Object> map = new HashMap<>();

        //时间集合（x轴）
        LinkedHashSet<Object> timeList = new LinkedHashSet<>();

        //风压点集合
        List<Object> spotWindPressureList = new ArrayList<>();

        //风压点名称
        List<Object> spotNameList = new ArrayList<>();

        QueryWrapper<DfWindPressure> ew = new QueryWrapper<>();
        ew.orderByDesc("check_time")
                .last("limit 1");
        DfWindPressure dfWindPressure = dfWindPressureService.getOne(ew);
        //实时压差
        Double windPressureNow = dfWindPressure.getCheckValue();

        QueryWrapper<DfWindPressure> windPressureWrapper = new QueryWrapper<>();
        windPressureWrapper
                .select("spot")
                .eq(StringUtils.isNotEmpty(factory),"factory",factory)
                .apply(StringUtils.isNotEmpty(startDate),"date(check_time)>='"+startDate+"'")
                .apply(StringUtils.isNotEmpty(endDate),"date(check_time)<='"+endDate+"'")
                .groupBy("spot");
        //所有风压点
        List<DfWindPressure> spotList = dfWindPressureService.list(windPressureWrapper);
        if (spotList==null||spotList.size()==0){
            return new Result(500,"该条件下没有相关的压差数据");
        }
        for (DfWindPressure windPressure:spotList){
            List<Object> oneSpotCheckValueList = new ArrayList<>();

            List<Object> lslList = new ArrayList<>();

            List<Object> uslList = new ArrayList<>();

            String spot = windPressure.getSpot();
            QueryWrapper<String> timeWrapper = new QueryWrapper<>();
            timeWrapper
                    .apply("c.datelist >='"+startDate+"'")
                    .apply("c.datelist <='"+endDate+"'");

            QueryWrapper<DfWindPressure> spotWrapper = new QueryWrapper<>();
            spotWrapper
                    .eq(StringUtils.isNotEmpty(factory),"dwp.factory",factory)
                    .apply(StringUtils.isNotEmpty(startDate),"date(dwp.check_time)>='"+startDate+"'")
                    .apply(StringUtils.isNotEmpty(endDate),"date(dwp.check_time)<='"+endDate+"'")
                    .eq("dwp.spot",spot);
            //当前风压点的压差检测值
            List<Rate3> OneSpotWindPressureList = dfWindPressureService.getOneSpotWindPressureList(spotWrapper,timeWrapper);
            for (Rate3 oneSpot:OneSpotWindPressureList){
                lslList.add(10);
                uslList.add(15);
                timeList.add(oneSpot.getStr1());
                oneSpotCheckValueList.add(oneSpot.getDou1());
            }
            map.put("lslListY",lslList);
            map.put("uslList",uslList);
            spotNameList.add(spot);
            spotWindPressureList.add(oneSpotCheckValueList);
        }


        map.put("windPressureNow",windPressureNow);
        map.put("timeListX",timeList);
        map.put("spotNameList",spotNameList);
        map.put("spotWindPressureListY",spotWindPressureList);

        return new Result(200,"风压折现图数据获取成功",map);
    }

    @ApiOperation("新增或修改")
    @PostMapping("/saveOrUpdate")
    public Object saveOrUpdate(@RequestBody DfWindPressure datas){
        if (null != datas.getId()) {
            if (dfWindPressureService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (dfWindPressureService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public Object deleteById(@RequestParam  String id){
        if (dfWindPressureService.removeById(id)){
            return new Result(200, "删除成功");
        }else {
            return new Result(500, "删除失败");
        }
    }

    @GetMapping("/conditionQuery")
    @ApiOperation("条件查询")
    public Object conditionQuery(int page,int limit,String factoryArea,String factory,String spot,String startDate,String endDate){
        Page<DfWindPressure> pages = new Page<>(page, limit);
        QueryWrapper<DfWindPressure> ew = new QueryWrapper<>();
        ew.ge(StringUtils.isNotEmpty(startDate),"check_time",startDate + " 00:00:00")
                .le(StringUtils.isNotEmpty(endDate),"check_time",endDate + " 23:59:59")
                .eq(StringUtils.isNotEmpty(factoryArea), "factory_area", factoryArea)
                .eq(StringUtils.isNotEmpty(factory), "factory", factory)
                .eq(StringUtils.isNotEmpty(spot), "spot", spot)
                .orderByDesc("check_time");
        IPage<DfWindPressure> list = dfWindPressureService.page(pages, ew);
        return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
    }

}
