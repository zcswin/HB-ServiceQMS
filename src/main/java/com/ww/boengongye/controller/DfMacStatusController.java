package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfMacModelPositionService;
import com.ww.boengongye.service.DfMacStatusService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Controller
@RequestMapping("/dfMacStatus")
@ResponseBody
@CrossOrigin
@Api(tags = "设备状态")
public class DfMacStatusController {

    @Autowired
    DfMacStatusService DfMacStatusService;
    @Autowired
    DfMacModelPositionService DfMacModelPositionService;
    @GetMapping (value ="/listStatus")
    public Result sendQueue(String floor) {
        QueryWrapper<DfMacStatus>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }
        List<DfMacStatus>datas=DfMacStatusService.listStatus(qw);
        AppearanceStatusCount counts=new AppearanceStatusCount();
        int overTime=0;
        int ng=0;
        int noData=0;
        int normal=0;
        int improper=0;
        int ngOverTime=0;
        int waitMaterial=0;
        for(DfMacStatus s:datas){
            s.setStatusidCur(InitializeCheckRule.macStatus.get(s.getMachineCode()).getStatusidCur());

            if(s.getStatusidCur()==2){
                normal+=1;
            }else if(s.getStatusidCur()==3){
                ng+=1;
            }else if(s.getStatusidCur()==4){
                improper+=1;
            }else if(s.getStatusidCur()==5){
                overTime+=1;
            }else if(s.getStatusidCur()==1){
                waitMaterial+=1;
            }else {
                noData+=1;
            }



        }
        counts.setAll(noData+overTime+ng+improper+normal+ngOverTime+waitMaterial);
        counts.setNoData(noData);
        counts.setOverTime(overTime);
        counts.setNg(ng);
        counts.setImproper(improper);
        counts.setNormal(normal);
        counts.setNgOverTime(ngOverTime);
        counts.setWaitMaterial(waitMaterial);
        return new Result(0,"查询成功",counts, datas);
    }


//    @GetMapping (value ="/listStatus")
//    public Result sendQueue(String floor) {
//         QueryWrapper<DfMacStatus>qw=new QueryWrapper();
//         if(null!=floor&&!floor.equals("")){
//             qw.eq("mac.floor",floor);
//         }else{
//             qw.eq("mac.floor","4F");
//         }
//        List<DfMacStatus>datas=DfMacStatusService.listStatus(qw);
//        AppearanceStatusCount counts=new AppearanceStatusCount();
//        int overTime=0;
//        int ng=0;
//        int noData=0;
//        int normal=0;
//        int improper=0;
//        int ngOverTime=0;
//        int waitMaterial=0;
//        for(DfMacStatus s:datas){
//
//
//                if(s.getStatusidCur()==2){
//                    normal+=1;
//                }else if(s.getStatusidCur()==3){
//                    ng+=1;
//                }else if(s.getStatusidCur()==4){
//                    improper+=1;
//                }else if(s.getStatusidCur()==5){
//                    overTime+=1;
//                }else if(s.getStatusidCur()==1){
//                    waitMaterial+=1;
//                }else {
//                    noData+=1;
//                }
//
//
//
//        }
//        counts.setAll(noData+overTime+ng+improper+normal+ngOverTime+waitMaterial);
//        counts.setNoData(noData);
//        counts.setOverTime(overTime);
//        counts.setNg(ng);
//        counts.setImproper(improper);
//        counts.setNormal(normal);
//        counts.setNgOverTime(ngOverTime);
//        counts.setWaitMaterial(waitMaterial);
//        return new Result(0,"查询成功",counts, datas);
//    }
//

    @GetMapping (value ="/listStatus2")
    public Result listStatus2(String floor,String firstCode) {
        QueryWrapper<DfMacStatus>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }

        if(null!=firstCode&&!firstCode.equals("")){
            qw.like("mac.code",firstCode);
        }
        List<DfMacStatus>datas=DfMacStatusService.listStatus(qw);
        AppearanceStatusCount counts=new AppearanceStatusCount();
        int overTime=0;
        int ng=0;
        int noData=0;
        int normal=0;
        int improper=0;
        int ngOverTime=0;
        int waitMaterial=0;
        for(DfMacStatus s:datas){
            s.setStatusidCur(InitializeCheckRule.macStatus.get(s.getMachineCode()).getStatusidCur());

            if(s.getStatusidCur()==2){
                normal+=1;
            }else if(s.getStatusidCur()==3){
                ng+=1;
            }else if(s.getStatusidCur()==4){
                improper+=1;
            }else if(s.getStatusidCur()==5){
                overTime+=1;
            }else if(s.getStatusidCur()==1){
                waitMaterial+=1;
            }else {
                noData+=1;
            }



        }
        counts.setAll(noData+overTime+ng+improper+normal+ngOverTime+waitMaterial);
        counts.setNoData(noData);
        counts.setOverTime(overTime);
        counts.setNg(ng);
        counts.setImproper(improper);
        counts.setNormal(normal);
        counts.setNgOverTime(ngOverTime);
        counts.setWaitMaterial(waitMaterial);
        return new Result(0,"查询成功",counts, datas);
    }


    @GetMapping (value ="/listSeachStatus")
    public Result listSeachStatus(String process,String keyWord) {
        QueryWrapper<DfMacModelPosition>qw=new QueryWrapper<>();
        if(null!=process&&!process.equals("")&&!process.equals("null")){
            qw.eq("area",process);
        }
        if(null!=keyWord&&!keyWord.equals("")&&!keyWord.equals("null")){
            qw.like("MachineCode",keyWord);
        }
        return new Result(0,"查询成功", DfMacModelPositionService.list(qw));
    }

    @GetMapping (value ="/listAll")
    public Result listAll() {
        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
        qw.orderByAsc("MachineCode");
        return new Result(0,"查询成功", DfMacStatusService.list());
    }

//    @GetMapping (value ="/listNg")
//    public Result listNg(int count) {
//        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
//        qw.eq("s.StatusID_Cur",3);
//        qw.orderByDesc("s.pub_time");
//        qw.last("limit 0,"+count);
//        return new Result(0,"查询成功", DfMacStatusService.listJoinCode(qw));
//    }

    @GetMapping (value ="/listNg")
    public Result listNg(int count) {
//        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
//        qw.eq("s.StatusID_Cur",3);
//        qw.orderByDesc("s.pub_time");
//        qw.last("limit 0,"+count);
        List<DfMacStatus>data=new ArrayList<>();
        for (Map.Entry<String, DfMacStatus> entry : InitializeCheckRule.macStatus.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if(entry.getValue().getStatusidCur()==3){

                data.add(entry.getValue());

            }
        }
        Collections.sort(data, (o1, o2) -> o1.getPubTime().compareTo(o2.getPubTime()));
        for(DfMacStatus d:data){
            d.setStatusName(InitializeCheckRule.statusName.get(d.getStatusidCur()));
        }

        Collections.reverse(data);
        return new Result(0,"查询成功", data.subList(0,data.size()>count?count:data.size()));
    }


    @GetMapping (value ="/listOther")
    public Result listOther(int count) {
        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
//        qw.ne("s.StatusID_Cur",3);
//        qw.isNotNull("")
        qw.orderByDesc("s.pub_time");
        qw.last("limit 0,"+count);
        return new Result(0,"查询成功", DfMacStatusService.listJoinCode(qw));
    }

//    @GetMapping (value ="/listNews")
//    public Result listNews(String time) {
//        System.out.println(time);
//        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
////        qw.ne("s.StatusID_Cur",3);
////        qw.isNotNull("")
//        qw.ge("s.pub_time",time);
//        qw.orderByDesc("s.pub_time");
////        qw.last("limit 0,"+count);
//        return new Result(0,"查询成功", DfMacStatusService.listJoinCode(qw));
//    }

    @GetMapping (value ="/listNews")
    public Result listNews(String time) {
        System.out.println(time);
        QueryWrapper<DfMacStatus>qw=new QueryWrapper<>();
//        qw.ne("s.StatusID_Cur",3);
//        qw.isNotNull("")
        qw.ge("s.pub_time",time);
        qw.orderByDesc("s.pub_time");
//        qw.last("limit 0,"+count);

        List<DfMacStatus>data=new ArrayList<>();
        for (Map.Entry<String, DfMacStatus> entry : InitializeCheckRule.macStatus.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if(null!=entry.getValue().getPubTime()){
                if(entry.getValue().getPubTime().after( Timestamp.valueOf(time))){

                    data.add(entry.getValue());

                }
            }

        }
        Collections.sort(data, (o1, o2) -> o1.getPubTime().compareTo(o2.getPubTime()));
        for(DfMacStatus d:data){
            d.setStatusName(InitializeCheckRule.statusName.get(d.getStatusidCur()));
        }
        Collections.reverse(data);
        return new Result(0,"查询成功", DfMacStatusService.listJoinCode(qw));
    }

    @ApiOperation("统计设备状态个数")
    @GetMapping(value ="/countByStatus")
    public Result countByStatus() {
        int wlj=0;
        List<DfMacStatus>datas=DfMacStatusService.countByStatus();
        for(DfMacStatus d:datas){
            if(d.statusName.equals("全部")){
                wlj=d.getCount();
                break;
            }
        }
        for(DfMacStatus d:datas){
            if(!d.statusName.equals("未链接")&&!d.statusName.equals("全部")){
                wlj=wlj-d.getCount();
            }
        }
        for(DfMacStatus d:datas){
            if(d.statusName.equals("未链接")){
               d.setCount(wlj);
               break;
            }
        }
        return new Result(0,"查询成功", datas);
    }



    @ApiOperation("统计设备状态个数")
    @GetMapping(value ="/countByStatus2")
    public Result countByStatus2(String floor) {
        int wlj=0;
        List<DfMacStatus>datas=DfMacStatusService.countByStatus();
        for(DfMacStatus d:datas){
            if(d.statusName.equals("全部")){
                wlj=d.getCount();
                break;
            }
        }
        for(DfMacStatus d:datas){
            if(!d.statusName.equals("未链接")&&!d.statusName.equals("全部")){
                wlj=wlj-d.getCount();
            }
        }
        for(DfMacStatus d:datas){
            if(d.statusName.equals("未链接")){
                d.setCount(wlj);
                break;
            }
        }
        return new Result(0,"查询成功", datas);
    }
    @RequestMapping (value ="/addData")
    public Result addData() {
        for(int i=111;i<=190;i++){
            DfMacStatus s=new DfMacStatus();
            s.setMachineCode("K"+i);
            s.setStatusidCur(2);
            DfMacStatusService.save(s);
        }
//        for(int i=111;i<=190;i++){
//            DfMacModelPosition s=new DfMacModelPosition();
//            if(i<10){
//                s.setMachineCode("K0"+i);
//            }else{
//                s.setMachineCode("K"+i);
//            }
//
//            s.setPositionX(0.0);
//            s.setPositionY(0.0);
//            s.setPositionZ(0.0);
//            DfMacModelPositionService.save(s);
//        }

        return new Result(0,"查询成功");
    }




    @RequestMapping (value ="/addData2")
    public Result addData2() {
        for(int i=1;i<=75;i++){

        }


        return new Result(0,"查询成功");
    }
}
