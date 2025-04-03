package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfProcessService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-13
 */
@Controller
@RequestMapping("/dfMacStatusAppearance")
@ResponseBody
@CrossOrigin
@Api(tags = "外观机台状态")
public class DfMacStatusAppearanceController {
    @Autowired
    com.ww.boengongye.service.DfMacStatusAppearanceService DfMacStatusAppearanceService;

    @Autowired
    com.ww.boengongye.service.DfMacStatusOverTimeService dfMacStatusOverTimeService;
    @Autowired
    com.ww.boengongye.service.DfProcessService dfProcessService;
    @GetMapping(value ="/listStatus")
    public Result listStatus(String process,String floor) {
        QueryWrapper<DfMacStatusAppearance>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }
        return new Result(0,"查询成功", DfMacStatusAppearanceService.listStatus(qw));
    }


    @GetMapping(value ="/listStatus2")
    public Result listStatus2(String project,String process,String floor) {
        AppearanceStatusCount counts=new AppearanceStatusCount();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int overTime=0;
        int ng=0;
        int noData=0;
        int normal=0;
        int improper=0;
        int ngOverTime=0;
        int waitMaterial=0;
        QueryWrapper<DfMacStatusAppearance>qw=new QueryWrapper();
        if(null!=floor&&!floor.equals("")){
            qw.eq("mac.floor",floor);
        }else{
            qw.eq("mac.floor","4F");
        }
//        qw.eq(StringUtils.isNotEmpty(project),"mac.project",project);

        List<DfMacStatusAppearance>datas= DfMacStatusAppearanceService.listStatus(qw);
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

        for(DfMacStatusAppearance s:datas){

            if((firstCode.equals("")||s.getMachineCode().indexOf(firstCode)!=-1)&&(StringUtils.isEmpty(project)||s.getProject().equals(project))){
                if(s.getStatusidCur()==2){
                    normal+=1;
                }else if(s.getStatusidCur()==3){
                    ng+=1;
                }else if(s.getStatusidCur()==4){
                    improper+=1;
                }else if(s.getStatusidCur()==10){
                    overTime+=1;
                }else if(s.getStatusidCur()==-1){
                    noData+=1;
                }else if(s.getStatusidCur()==90){
                    ngOverTime+=1;
                }else if(s.getStatusidCur()==91){
                    waitMaterial+=1;
                }
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
//        return new Result(0,"查询成功", DfMacStatusAppearanceService.listStatus());
    }

    @ApiOperation("统计设备状态个数")
    @GetMapping(value ="/countByStatus")
    public Result countByStatus() {
        return new Result(0,"查询成功", DfMacStatusAppearanceService.countByStatus());
    }


//    @GetMapping(value ="/preparationTimeout")
//    public Result preparationTimeout() {
//        QueryWrapper<DfQmsIpqcWaigTotal>qw=new QueryWrapper<>();
//        QueryWrapper<DfMacStatusOverTime>ow=new QueryWrapper<>();
//        ow.eq("type","外观");
//        ow.last("limit 1");
//        DfMacStatusOverTime owt=dfMacStatusOverTimeService.getOne(ow);
//        if(null!=owt){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date d=new Date();
//
//                    String startTime=sdf.format(  d.getTime()-((owt.getOverTime()-1)*60*1000));
//                    String endTime=sdf.format(  d.getTime()-((owt.getOverTime()-15)*60*1000));
//                    System.out.println(startTime+" - " +endTime);
//                    qw.between("t.f_time",startTime,endTime);
//            List<DfQmsIpqcWaigTotal>datas=DfMacStatusAppearanceService.preparationTimeout(qw);
//            if(datas.size()>0){
//                for(DfQmsIpqcWaigTotal qt:datas){
//                    qt.setId(Integer.parseInt(((qt.getfTime().getTime()-(d.getTime()-owt.getOverTime()*60*1000))/60/1000)+"") );
//                }
//            }
//            return new Result(200,"查询成功", datas);
//        }
//        return new Result(500,"查询失败");
//    }

    @GetMapping(value ="/preparationTimeout")
    public Result preparationTimeout() {
//        QueryWrapper<DfQmsIpqcWaigTotal>qw=new QueryWrapper<>();
//        QueryWrapper<DfMacStatusOverTime>ow=new QueryWrapper<>();
//        ow.eq("type","外观");
//        ow.last("limit 1");
//        DfMacStatusOverTime owt=dfMacStatusOverTimeService.getOne(ow);
//        if(null!=owt){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date d=new Date();
//
//            String startTime=sdf.format(  d.getTime()-((owt.getOverTime()-1)*60*1000));
//            String endTime=sdf.format(  d.getTime()-((owt.getOverTime()-15)*60*1000));
//            System.out.println(startTime+" - " +endTime);
////                    qw.between("t.f_time",startTime,endTime);
//            List<DfQmsIpqcWaigTotal>datas=DfMacStatusAppearanceService.preparationTimeout(qw);
//            List<DfQmsIpqcWaigTotal>datas2=new ArrayList<>();
//            if(datas.size()>0){
//                for(DfQmsIpqcWaigTotal qt:datas){
//                    if( qt.getfTime().after(new Date(d.getTime()-((owt.getOverTime()-1)*60*1000)))&&qt.getfTime().before(new Date(d.getTime()-((owt.getOverTime()-15)*60*1000)))) {
//
//                        qt.setId(Integer.parseInt(((qt.getfTime().getTime()-(d.getTime()-owt.getOverTime()*60*1000))/60/1000)+"") );
//                        datas2.add(qt);
//                    }
//                }
//            }
//            return new Result(200,"查询成功", datas2);
//        }
        return new Result(500,"查询失败");
    }

    @GetMapping(value ="/timeoutMac")
    public Result timeoutMac() {
        QueryWrapper<DfMacStatusAppearance>qw=new QueryWrapper<>();
        QueryWrapper<DfMacStatusOverTime>ow=new QueryWrapper<>();
        ow.eq("type","外观");
        ow.last("limit 1");
        DfMacStatusOverTime owt=dfMacStatusOverTimeService.getOne(ow);
        if(null!=owt){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d=new Date();

            String startTime=sdf.format(  d.getTime()-((owt.getOverTime()*2)*60*1000));
            String endTime=sdf.format(  d.getTime()-((owt.getOverTime())*60*1000));
            System.out.println(startTime+" - " +endTime);
//            qw.between("pub_time",startTime,endTime);
            List<DfMacStatusAppearance>datas=DfMacStatusAppearanceService.list(qw);
            List<DfMacStatusAppearance>datas2=new ArrayList<>();
            if(datas.size()>0){
                for(DfMacStatusAppearance qt:datas){
                    if( qt.getPubTime().after(new Date(d.getTime()-((owt.getOverTime()*2)*60*1000)))&&qt.getPubTime().before(new Date(d.getTime()-((owt.getOverTime())*60*1000)))) {
                        qt.setId(Integer.parseInt((((d.getTime()-owt.getOverTime()*60*1000)-qt.getPubTime().getTime())/60/1000)+"") );
                        datas2.add(qt);
                    }

                }
            }
            return new Result(200,"查询成功", datas2);
        }
        return new Result(500,"查询失败");
    }
    /**
     * 根据工序获取机台外观信息
     * @param process 工序名称
     * @return
     */
    @GetMapping(value ="/getMachineByProcess")
    public Result timeoutMac(String process) {
        List<DfMacStatusAppearance> list = new ArrayList<>();

        if (StringUtils.isEmpty(process)){
            return new Result(200,"查询成功",DfMacStatusAppearanceService.list());
        }

        QueryWrapper<DfProcess> qw = new QueryWrapper<>();
        qw.eq("process_name", process);
        DfProcess one = dfProcessService.getOne(qw);
        if (one != null){
            QueryWrapper<DfMacStatusAppearance> qw1 = new QueryWrapper<>();
            qw1.likeRight("MachineCode", one.getFirstCode());
             list = DfMacStatusAppearanceService.list(qw1);

        }
        return new Result(200,"查询成功",list);

    }

}
