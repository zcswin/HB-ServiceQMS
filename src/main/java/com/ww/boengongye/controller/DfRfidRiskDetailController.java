package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFlowData;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfRfidRiskDetail;
import com.ww.boengongye.entity.DfRiskProduct;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfLiableManService;
import com.ww.boengongye.service.DfRfidRiskDetailService;
import com.ww.boengongye.service.DfRiskProductService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 风险品信息表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Controller
@RequestMapping("/rfidRisk")
@ResponseBody
@CrossOrigin
@Api(tags = "RFID风险品")
public class DfRfidRiskDetailController {


        @Autowired
        DfLiableManService dfLiableManService;

        @Autowired
        DfRfidRiskDetailService dfRfidRiskDetailService;



        @Autowired
        DfFlowDataService dfFlowDataService;

    @Autowired
    DfRiskProductService dfRiskProductService;

        @PostMapping("/push")
        @ApiOperation("提交")
        public Result listProcessYield(@RequestBody List<DfRfidRiskDetail> datas) {

            if(datas.size()>0){
                List<DfRfidRiskDetail>cc=new ArrayList<>();
                List<DfRfidRiskDetail>wg=new ArrayList<>();
                for(DfRfidRiskDetail d:datas){
                    if(d.getType().equals("尺寸")){
                        cc.add(d);
                    }else{
                        wg.add(d);
                    }
                }
                if(cc.size()>0){
                    DfFlowData risk=new DfFlowData();
                    risk.setName("风险品拦截:"+cc.get(0).getType()+"-"+cc.get(0).getProcessName()+" 拦截 "+cc.size()+" 片玻璃,在"+cc.get(0).getCarrier()+"上,请及时处理!");
                    risk.setFlowType(cc.get(0).getType());
                    risk.setDataType("风险隔离全检");
                    risk.setStatus("待确认");
                    risk.setFlowLevelName("Level1");
                    risk.setFlowLevel(1);
                    QueryWrapper<DfLiableMan> qw=new QueryWrapper<>();
                    qw.eq("type","sizeRFID");
                    qw.like("bimonthly", TimeUtil.getDayShift()==0?"双月":"单月");

                    List<DfLiableMan> lm=dfLiableManService.list(qw);
                    if(lm.size()>0){
                        StringBuilder name=new StringBuilder();
                        StringBuilder code=new StringBuilder();
                        int count=0;
                        for(DfLiableMan l:lm){
                            if(count>0){
                                name.append(",");
                                code.append(",");
                            }
                            name.append(l.getLiableManName());
                            code.append(l.getLiableManCode());
                            count++;
                        }
                        risk.setNowLevelUserName(name.toString());
                        risk.setNowLevelUser(code.toString());
                    }
                    dfFlowDataService.save(risk);
                    for(DfRfidRiskDetail d:cc){
                        d.setParentId(risk.getId());
                    }
                    dfRfidRiskDetailService.saveBatch(cc);
                }

                if(wg.size()>0){
                    DfFlowData risk=new DfFlowData();
                    risk.setName("风险品拦截:"+wg.get(0).getType()+"-"+wg.get(0).getProcessName()+" 拦截 "+wg.size()+" 片玻璃,在"+wg.get(0).getCarrier()+"上,请及时处理!");
                    risk.setFlowType(wg.get(0).getType());
                    risk.setDataType("风险隔离全检");
                    risk.setStatus("待确认");
                    risk.setFlowLevelName("Level1");
                    risk.setFlowLevel(1);
                    QueryWrapper<DfLiableMan>qw=new QueryWrapper<>();
                    qw.eq("type","appearanceRFID");
                    qw.like("bimonthly",TimeUtil.getDayShift()==0?"双月":"单月");

                    List<DfLiableMan> lm=dfLiableManService.list(qw);
                    if(lm.size()>0){
                        StringBuilder name=new StringBuilder();
                        StringBuilder code=new StringBuilder();
                        int count=0;
                        for(DfLiableMan l:lm){
                            if(count>0){
                                name.append(",");
                                code.append(",");
                            }
                            name.append(l.getLiableManName());
                            code.append(l.getLiableManCode());
                            count++;
                        }
                        risk.setNowLevelUserName(name.toString());
                        risk.setNowLevelUser(code.toString());
                    }
                    dfFlowDataService.save(risk);
                    for(DfRfidRiskDetail d:wg){
                        d.setParentId(risk.getId());
                    }
                    dfRfidRiskDetailService.saveBatch(wg);
                }



            }else{
                return new Result(500, "至少上传一条数据");
            }


            return new Result(200, "上传成功");
        }




    //获取
    @GetMapping(value = "/listByFlowId")
    @ApiOperation("获取")
    public Object listByFlowId(int id,String type,String time) {
        if(type.equals("外观")){
            QueryWrapper<DfRfidRiskDetail>qw=new QueryWrapper<>();
            qw.eq("r.parent_id",id);
            return new Result(200, "查询成功", dfRfidRiskDetailService.listJoinAppearance(time,qw));
        }else{
            QueryWrapper<DfRfidRiskDetail>qw=new QueryWrapper<>();
            qw.eq("r.parent_id",id);
            return new Result(200, "查询成功", dfRfidRiskDetailService.listJoinSize(time,qw));
        }

    }

    //获取
    @GetMapping(value = "/listByOtherAuditId")
    @ApiOperation("获取拦截记录")
    public Object listByOtherAuditId(int id,String type) {
            QueryWrapper<DfRiskProduct>qw=new QueryWrapper<>();
            qw.eq("parent_id",id);


        List<DfRiskProduct>fxp=dfRiskProductService.list(qw);
                List<String> fxpCode = fxp.stream()
                .map(DfRiskProduct::getBarCode)  // 提取每个 DfRiskProduct 的 code
                .collect(Collectors.toList()); // 收集到 List<String>

        if(fxpCode.size()>0){
            QueryWrapper<DfRfidRiskDetail>qw2=new QueryWrapper<>();
            qw2.in("bar_code",fxpCode);
            qw2.orderByDesc("id");
            qw2.last("limit 1");
            DfRfidRiskDetail rd=dfRfidRiskDetailService.getOne(qw2);
            if(null!=rd){
                if(type.equals("外观")){
                    QueryWrapper<DfRfidRiskDetail>qw3=new QueryWrapper<>();
                    qw.eq("r.parent_id",rd.getParentId());
                    return new Result(200, "查询成功", dfRfidRiskDetailService.listJoinAppearance(TimeUtil.subtractDays(TimeUtil.formatTimestamp(rd.getCreateTime()),5),qw3));
                }else{
                    QueryWrapper<DfRfidRiskDetail>qw3=new QueryWrapper<>();
                    qw.eq("r.parent_id",rd.getParentId());
                    return new Result(200, "查询成功", dfRfidRiskDetailService.listJoinSize(TimeUtil.subtractDays(TimeUtil.formatTimestamp(rd.getCreateTime()),5),qw3));
                }
            }
        }


        return new Result(500, "查询失败");
    }

}
