package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacStatusAppearanceService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.service.DfSizeMacDurationInfoService;
import com.ww.boengongye.service.DfSizeMacDurationService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.HttpUtil;
import com.ww.boengongye.utils.RFIDResult;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.ResultExtractor;
import org.bouncycastle.util.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Controller
@RequestMapping("/dfMacStatusSize")
@ResponseBody
@CrossOrigin
@Api(tags = "尺寸机台状态")
public class DfMacStatusSizeController {

    @Autowired
    com.ww.boengongye.service.DfMacStatusSizeService DfMacStatusSizeService;

    @Autowired
    com.ww.boengongye.service.DfMacStatusOverTimeService dfMacStatusOverTimeService;
    @Autowired
    com.ww.boengongye.service.DfProcessService dfProcessService;

    @Autowired
    private DfSizeMacDurationService dfSizeMacDurationService;

    @Autowired
    private DfSizeMacDurationInfoService dfSizeMacDurationInfoService;


    @Autowired
    Environment env;

    @GetMapping(value ="/listStatus")
    public Result listStatus(String process,String floor,String project) {
        Map<String, String> headers = new HashMap<>();

//        //请求RFID
//        HttpParamTime param = new HttpParamTime(60);
//        RFIDResult dl = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDWaitMaterialURL"), null, headers, JSONObject.toJSONString(param),false), RFIDResult.class);
//
//        //请求RFID
//        HttpParamTime param2 = new HttpParamTime(120);
//        RFIDResult tj = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDWaitMaterialURL"), null, headers, JSONObject.toJSONString(param2),false), RFIDResult.class);
//
//        Map<String,Integer>machineMap=new HashMap<>();
//        //待料
//        if(null!=dl&&null!=dl.getData()&&null!=dl.getData().getMachineCodeList()&&dl.getData().getMachineCodeList().size()>0){
//            for(String mac:dl.getData().getMachineCodeList()){
//                machineMap.put(mac,91);
//
//            }
//        }
//
//        //停机
//        if(null!=tj&&null!=tj.getData()&&null!=tj.getData().getMachineCodeList()&&tj.getData().getMachineCodeList().size()>0){
//            for(String mac:tj.getData().getMachineCodeList()){
//                machineMap.put(mac,-1);
//
//            }
//        }
        SizeStatusCount counts=new SizeStatusCount();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int overTime=0;
        int ng=0;
        int noData=0;
        int normal=0;
        int improper=0;
        int waitMaterial=0;
        QueryWrapper<DfMacStatusSize>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }

//        qw.eq(StringUtils.isNotEmpty(project),"mac.project",project);
        List<DfMacStatusSize>datas= DfMacStatusSizeService.listStatus2(qw);
        String firstCode="";
        if(null!=process&&!process.equals("")){
            QueryWrapper<DfProcess>pqw=new QueryWrapper<>();
            pqw.eq("process_name",process);
            pqw.last("limit 1");
            DfProcess pro=dfProcessService.getOne(pqw);
            if(null!=pro&&null!=pro.getFirstCode()){
                firstCode=pro.getFirstCode();
            }
        }
        QueryWrapper<DfMacStatusOverTime> oqw=new QueryWrapper<>();
        oqw.eq("type","尺寸");
        oqw.last("limit 1");
        DfMacStatusOverTime overTimeData= dfMacStatusOverTimeService.getOne(oqw);
//          counts.setAll(datas.size());
        for(DfMacStatusSize s:datas){
//            if (!machineMap.containsKey(s.getMachineCode())){
                if(InitializeCheckRule.sizeStatus.containsKey(s.getMachineCode())){
                    s.setStatusidCur(InitializeCheckRule.sizeStatus.get(s.getMachineCode()).getStatusidCur());
                    s.setPubTime(InitializeCheckRule.sizeStatus.get(s.getMachineCode()).getPubTime());
                    s.setStatusName(InitializeCheckRule.statusName.get(s.getStatusidCur()));


                    long  diff = 0;
                    try {
                        diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() -  s.getPubTime().getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(diff/60/1000>overTimeData.getStopTime()){

                        s.setStatusidCur(-1);

                    }else if(diff/60/1000>overTimeData.getOverTime()){

                        s.setStatusidCur(10);
                    }
                    if(firstCode.equals("")||s.getMachineCode().indexOf(firstCode)!=-1&&(StringUtils.isEmpty(project)||s.getProject().equals(project))){
                        if(s.getStatusidCur()==2){
//                    counts.setNormal(counts.getNormal()+1);
                            normal+=1;
                        }else if(s.getStatusidCur()==3){
//                    counts.setNg(counts.getNg()+1);
                            ng+=1;
                        }else if(s.getStatusidCur()==4){
//                    counts.setImproper(counts.getImproper()+1);
                            improper+=1;
                        }else if(s.getStatusidCur()==10){
//                    counts.setOverTime(counts.getOverTime()+1);
                            overTime+=1;
                        }else if(s.getStatusidCur()==-1){
//                    counts.setNoData(counts.getNoData()+1);
                            noData+=1;
                        }
                    }

                }

//            }else{
//                s.setStatusidCur(machineMap.get(s.getMachineCode()));
//                if(s.getStatusidCur()==91){
//                    waitMaterial+=1;
//                }else if(s.getStatusidCur()==-1){
//                    noData+=1;
//                }
//                s.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                s.setStatusName(InitializeCheckRule.statusName.get(s.getStatusidCur()));
//            }

        }
        counts.setAll(noData+overTime+ng+improper+normal+waitMaterial);
        counts.setNoData(noData);
        counts.setOverTime(overTime);
        counts.setNg(ng);
        counts.setImproper(improper);
        counts.setNormal(normal);
        counts.setWaitMaterial(waitMaterial);
        return new Result(0,"查询成功",counts, datas);
//        return new Result(0,"查询成功", DfMacStatusSizeService.listStatus());
    }


    @GetMapping(value ="/listOverTime")
    public Result listOverTime(String floor) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<DfMacStatusSize>reponseData= new ArrayList<>();
        QueryWrapper<DfMacStatusSize>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }

        List<DfMacStatusSize>datas= DfMacStatusSizeService.listStatus2(qw);
        QueryWrapper<DfMacStatusOverTime> oqw=new QueryWrapper<>();
        oqw.eq("type","尺寸");
        oqw.last("limit 1");
        DfMacStatusOverTime overTimeData= dfMacStatusOverTimeService.getOne(oqw);
//          counts.setAll(datas.size());
        for(DfMacStatusSize s:datas){
            if(InitializeCheckRule.sizeStatus.containsKey(s.getMachineCode())){
                s.setStatusidCur(InitializeCheckRule.sizeStatus.get(s.getMachineCode()).getStatusidCur());
                s.setPubTime(InitializeCheckRule.sizeStatus.get(s.getMachineCode()).getPubTime());
                s.setStatusName(InitializeCheckRule.statusName.get(s.getStatusidCur()));
                long  diff = 0;
                try {
                    diff = sd.parse(TimeUtil.getNowTimeByNormal()).getTime() -  s.getPubTime().getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if((diff/60/1000)>overTimeData.getOverTime()&&(diff/60/1000)<overTimeData.getStopTime()){

                    s.setId(Integer.parseInt((diff/60/1000)+""));
                    reponseData.add(s);
                }


            }

        }

        return new Result(0,"查询成功",reponseData );
//        return new Result(0,"查询成功", DfMacStatusSizeService.listStatus());
    }

    @ApiOperation("统计设备状态个数")
    @GetMapping(value ="/countByStatus")
    public Result countByStatus() {


        return new Result(0,"查询成功", DfMacStatusSizeService.countByStatus());
    }

    @GetMapping(value ="/listNgStatus")
    public Result listNgStatus() {
        QueryWrapper<DfMacStatusSize>qw=new QueryWrapper<>();
        qw.eq("s.StatusID_Cur",3);
//        qw.isNotNull("")
        qw.orderByDesc("s.pub_time");

        return new Result(0,"查询成功", DfMacStatusSizeService.listJoinCode(qw));
    }

    /**
     * 根据工序获取机台尺寸信息
     * @param process 工序名称
     * @return
     */
    @GetMapping(value ="/getMachineByProcess")
    public Result timeoutMac(String process) {
        List<DfMacStatusSize> list = new ArrayList<>();

        if (StringUtils.isEmpty(process)){
            return new Result(200,"查询成功",DfMacStatusSizeService.list());
        }

        QueryWrapper<DfProcess> qw = new QueryWrapper<>();
        qw.eq("process_name", process);
        DfProcess one = dfProcessService.getOne(qw);
        if (one != null){
            QueryWrapper<DfMacStatusSize> qw1 = new QueryWrapper<>();
            qw1.likeRight("MachineCode", one.getFirstCode());
            list = DfMacStatusSizeService.list(qw1);

        }
        return new Result(200,"查询成功",list);
    }


    @ApiOperation("添加机台工序")
    @GetMapping("updateMacProcess")
    @Transactional(rollbackFor = Exception.class)
    public Result updateMacProcess(){
        List<DfMacStatusSize> list = DfMacStatusSizeService.list();

        for (DfMacStatusSize dfMacStatusSize:list){
            String MachineCode = dfMacStatusSize.getMachineCode();
            String MachineCodeFirst = MachineCode.substring(0,1);
            QueryWrapper<DfProcess> ew = new QueryWrapper<>();
            ew
                    .select("process_name")
                    .eq("first_code",MachineCodeFirst)
                    .last("limit 1");
            DfProcess dfProcess = dfProcessService.getOne(ew);
            dfMacStatusSize.setProcess(dfProcess.getProcessName());
            dfMacStatusSize.setChangeTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
        }
        DfMacStatusSizeService.updateBatchById(list);

        return new Result(200,"成功添加"+list.size()+"条数据的工序");
    }

    @ApiOperation("当天工序所有机台的状态和状态改变时间差")
    @GetMapping("getProcessMacStatusList")
    public Result getProcessMacStatusList(@RequestParam String process){
        Map<String,Object> map = new HashMap<>();

        QueryWrapper<DfMacStatusSize> ew = new QueryWrapper<>();
        ew
                .eq("dmss.process",process)
                .apply("date(dmss.pub_time) = current_date()");
        //当天工序所有机台的状态和状态改变时间差
        List<Rate3> macStatusList = DfMacStatusSizeService.getProcessMacStatusList(ew);
        if (macStatusList==null|macStatusList.size()==0){
            return new Result(200,"当天该工序没有机台的状态和状态改变时间差的相关信息",map);
        }

        for(Rate3 entity:macStatusList){
            //正常
            if (entity.getInte1()==2){
                entity.setStr2("#78c47c");
                entity.setStr3("transparent");
                if (entity.getInte2()>5400){
                    entity.setStr3("#cc14d2");
                }
                entity.setStr4("正常");
                continue;
            }

            //隔离
            if (entity.getInte1()==3){
                entity.setStr2("#ca3f39");
                entity.setStr3("transparent");
                if (entity.getInte2()>5400){
                    entity.setStr3("#ffffff");
                }
                entity.setStr4("隔离");
                continue;
            }

            //调机
            if (entity.getInte1()==10){
                entity.setStr2("#e5c355");
                entity.setStr3("transparent");
                if (entity.getInte2()>5400){
                    entity.setStr3("#ffffff");
                }
                entity.setStr4("调机");
                continue;
            }

            //闲置
            if (entity.getInte1()==-1){
                entity.setStr2("#999999");
                entity.setStr3("transparent");
                entity.setStr4("闲置");
            }
        }

        long normal = macStatusList.stream()
                .filter(entity ->entity.getInte1()==2).count();
        long normalGt = macStatusList.stream()
                .filter(entity ->entity.getInte1()==2&&entity.getInte2()>5400).count();
        long quarantine = macStatusList.stream()
                .filter(entity ->entity.getInte1()==3).count();
        long quarantineGt = macStatusList.stream()
                .filter(entity ->entity.getInte1()==3&&entity.getInte2()>5400).count();
        long adjust = macStatusList.stream()
                .filter(entity ->entity.getInte1()==10).count();
        long adjustGt = macStatusList.stream()
                .filter(entity ->entity.getInte1()==10&&entity.getInte2()>5400).count();
        long leaveUnused = macStatusList.stream()
                .filter(entity ->entity.getInte1()==-1).count();

        map.put("macStatusList",macStatusList);
        map.put("normal",normal);
        map.put("normalGt",normalGt);
        map.put("quarantine",quarantine);
        map.put("quarantineGt",quarantineGt);
        map.put("adjust",adjust);
        map.put("adjustGt",adjustGt);
        map.put("leaveUnused",leaveUnused);

        return new Result(200,"获取当前工序机台状态成功",map);
    }


    /**
     * 定时任务-每小时整点添加机台状态更改记录和持续时间
     */
//    @Scheduled(cron ="0 * * * * *")
    @GetMapping("checkSizeMacDurationInfo")
    @ApiOperation("每小时整点添加机台状态更改记录和持续时间")
    @Transactional(rollbackFor = Exception.class)
    public void checkSizeMacDurationInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前整点时间
        String checkTime = TimeUtil.getNowTimeHour();
        //一小时前的时间
        String beforeHourTime = TimeUtil.getTimeAfterSubHour(checkTime,1);

        System.out.println("------------统计每小时整点添加机台状态更改记录和持续时间start-------------------");
        List<DfMacStatusSize> macStatusSizeList = DfMacStatusSizeService.list();
        List<DfSizeMacDuration> list = new ArrayList<>();
        for (DfMacStatusSize dfMacStatusSize:macStatusSizeList){
            DfSizeMacDuration dfSizeMacDuration = new DfSizeMacDuration();
            dfSizeMacDuration.setProcess(dfMacStatusSize.getProcess());
            dfSizeMacDuration.setMachineCode(dfMacStatusSize.getMachineCode());
            dfSizeMacDuration.setStatus(dfMacStatusSize.getStatusidCur());
            dfSizeMacDuration.setAfterStatus(dfMacStatusSize.getStatusidCur());

            String changeTime = sdf.format(dfMacStatusSize.getChangeTime());
            Integer durationTime = TimeUtil.getSecondsDifferenceInt(changeTime,checkTime);
            dfSizeMacDuration.setDurationTime(durationTime);
            dfSizeMacDuration.setCheckTime(Timestamp.valueOf(checkTime));
            list.add(dfSizeMacDuration);
        }
//        if (!dfSizeMacDurationService.saveBatch(list)){
//           return;
//        }

        QueryWrapper<DfSizeMacDuration> sizeMacDurationWrapper = new QueryWrapper<>();
        sizeMacDurationWrapper
                .gt("dsmd.check_time",beforeHourTime)
                .le("dsmd.check_time",checkTime);

        QueryWrapper<DfSizeDetail> sizeDetailWrapper = new QueryWrapper<>();
        sizeDetailWrapper
                .apply("dsd.test_time >= '"+beforeHourTime+"'")
                .apply("dsd.test_time < '"+checkTime+"'");

        List<Rate3> sizeMacDurationInfoList = DfMacStatusSizeService.countSizeMacDurationInfoList(sizeMacDurationWrapper,sizeDetailWrapper);

        List<DfSizeMacDurationInfo> entityList = new ArrayList<>();
        for (Rate3 entity:sizeMacDurationInfoList){
            DfSizeMacDurationInfo dfSizeMacDurationInfo = new DfSizeMacDurationInfo();
            dfSizeMacDurationInfo.setProcess(entity.getStr1());
            dfSizeMacDurationInfo.setInfoTime(Timestamp.valueOf(checkTime));
            dfSizeMacDurationInfo.setNormal(entity.getDou1()*100);
            dfSizeMacDurationInfo.setDebug(entity.getDou2()*100);
            dfSizeMacDurationInfo.setQuarantine(entity.getDou3()*100);
            dfSizeMacDurationInfo.setMachineCount(entity.getInte1());

            entityList.add(dfSizeMacDurationInfo);
        }

//        if (!dfSizeMacDurationInfoService.saveBatch(entityList)){
//            return;
//        }

    }



    @ApiOperation("获取该工序当天每小时的机台状态统计")
    @GetMapping("getSizeMacDurationInfoList")
    public Result getSizeMacDurationInfoList(@RequestParam String process){
        //获取当天整点时间
        String today = TimeUtil.getTodayHour();
        QueryWrapper<DfSizeMacDurationInfo> ew = new QueryWrapper<>();
        ew
                .eq("process",process)
                .gt("info_time", today);
        List<DfSizeMacDurationInfo> list = dfSizeMacDurationInfoService.list(ew);
        if (list==null||list.size()==0){
            return new Result(200,"该工序当天没有每小时的机台状态统计相关数据",list);
        }

        return new Result(200,"获取该工序当天每小时的机台状态统计成功",list);
    }


    @ApiOperation("获取该机台当天状态信息")
    @GetMapping("getMacStatusInfo")
    public Result getMacStatusInfo(@RequestParam String process,@RequestParam String machineCode){
        Map<String,Object> map = new HashMap<>();
        //获取当前时间
        String timeNow = TimeUtil.getNowTimeByNormal();
        //获取当天整点时间
        String today = TimeUtil.getTodayHour();

        QueryWrapper<DfSizeMacDuration> macDurationWrapper = new QueryWrapper<>();
        macDurationWrapper
                .eq("machine_code",machineCode)
                .gt("check_time", today)
                .orderByAsc("check_time");
        //获取该机台当天所有状态持续时间
        List<DfSizeMacDuration> macDurationList = dfSizeMacDurationService.list(macDurationWrapper);
        if (macDurationList==null||macDurationList.size()==0){
            return new Result(200,"该机台当天没有相关状态持续时间信息",map);
        }

        DfSizeMacDuration dfSizeMacDuration = new DfSizeMacDuration();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //最后一次状态改变时间
        String lastChangeTime = sdf.format(macDurationList.get(macDurationList.size()-1).getCheckTime());
        //当前时间到上次状态变化之间相差的秒数
        Integer diffTime = TimeUtil.getSecondsDifferenceInt(lastChangeTime,timeNow);
        dfSizeMacDuration.setCheckTime(Timestamp.valueOf(timeNow));
        dfSizeMacDuration.setDurationTime(diffTime);
        dfSizeMacDuration.setStatus(macDurationList.get(macDurationList.size()-1).getAfterStatus());
        macDurationList.add(dfSizeMacDuration);

        int normal = macDurationList.stream()
                .filter(entity->entity.getStatus()==2)
                .collect(Collectors.summingInt(entity->entity.getDurationTime()));
        int quarantine = macDurationList.stream()
                .filter(entity->entity.getStatus()==3)
                .collect(Collectors.summingInt(entity->entity.getDurationTime()));
        int adjust = macDurationList.stream()
                .filter(entity->entity.getStatus()==10)
                .collect(Collectors.summingInt(entity->entity.getDurationTime()));
        int leaveUnused = macDurationList.stream()
                .filter(entity->entity.getStatus()==-1)
                .collect(Collectors.summingInt(entity->entity.getDurationTime()));

        Double total = (double) normal+quarantine+adjust+leaveUnused;

        String normalPoint = String.format("%.2f", (double)normal/total*100);
        String quarantinePoint = String.format("%.2f", (double)quarantine/total*100);
        String adjustPoint = String.format("%.2f", (double)adjust/total*100);
        String leaveUnusedPoint = String.format("%.2f", (double)leaveUnused/total*100);

        //该机台当天正常时长的排名
        QueryWrapper<DfSizeMacDuration> normalTimeWrapper = new QueryWrapper<>();
        normalTimeWrapper
                .eq("dsmd.process",process)
                .gt("dsmd.check_time",today);
        List<Rate3> normalTimeList = DfMacStatusSizeService.getProcessMacNormalTime(normalTimeWrapper);
        Integer index = IntStream.range(0, normalTimeList.size())
                .filter(i -> normalTimeList.get(i).getStr1().equals(machineCode))
                .boxed()
                .findFirst().get();

        map.put("normal",normal);
        map.put("normalPoint",normalPoint);
        map.put("quarantine",quarantine);
        map.put("quarantinePoint",quarantinePoint);
        map.put("adjust",adjust);
        map.put("adjustPoint",adjustPoint);
        map.put("leaveUnused",leaveUnused);
        map.put("macRank",index+1);
        map.put("leaveUnusedPoint",leaveUnusedPoint);

        return new Result(200,"获取当前机台状态信息成功",map);
    }

    @ApiOperation("获取该机台当天的状态和测试类别")
    @GetMapping("getMacStatusAndCheckStatusList")
    public Result getMacStatusAndCheckStatusList(@RequestParam String machineCode){
        QueryWrapper<DfSizeDetail> ew = new QueryWrapper<>();
        ew
                .eq("dsd.machine_code",machineCode)
                .apply("date(dsd.test_time) = current_date()");
        List<Rate3> macStatusInfoList = DfMacStatusSizeService.getMacStatusInfoList(ew);
        if (macStatusInfoList==null||macStatusInfoList.size()==0){
            return new Result(200,"该机台当天没有状态和测试类别相关数据",macStatusInfoList);
        }

        for (Rate3 entity:macStatusInfoList){
            if ("正常".equals(entity.getStr2())){
                entity.setStr4("#78c47c");
                continue;
            }
            if ("隔离".equals(entity.getStr2())){
                entity.setStr4("#ca3f39");
                continue;
            }
            if ("调机".equals(entity.getStr2())){
                entity.setStr4("#e5c355");
                continue;
            }
            if ("闲置".equals(entity.getStr2())){
                entity.setStr4("#999999");
                continue;
            }
        }

        return new Result(200,"获取该机台当天的状态和测试类别成功",macStatusInfoList);
    }
}
