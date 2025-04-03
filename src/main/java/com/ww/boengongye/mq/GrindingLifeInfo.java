package com.ww.boengongye.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.scada.GrindingLife;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.DynamicIpqcUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Component
public class GrindingLifeInfo {
    @Autowired
    private Environment env;
    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private DfProcessService dfProcessService;
    @Autowired
    private DfGrindingStandardService dfGrindingStandardService;
    @Autowired
    private DfGrindingInfoService dfGrindingInfoService;
    @Autowired
    private DfAuditDetailService dfAuditDetailService;
    @Autowired
    private DfFlowDataService dfFlowDataService;
    @Autowired
    DfProTimeService dfProTimeService;
    @JmsListener(destination = "${GrindingLifeInfoTopic}", containerFactory = "jtJmsListenerContainerFactoryTopic")
    @Transactional(rollbackFor = Exception.class)
    public void consume(final String msg) {
//        System.out.println("砂轮寿命统计:"+msg);
        GrindingLife datas = JSON.parseObject(msg, com.ww.boengongye.entity.scada.GrindingLife.class);
        if(null!=datas){
            String firstCode1 = datas.getMachineCode().substring(0, 1);
            QueryWrapper<DfProcess> dfProcessQueryWrapper = new QueryWrapper<>();
            dfProcessQueryWrapper.eq("first_code",firstCode1);
            DfProcess process = dfProcessService.getOne(dfProcessQueryWrapper);

            DfGrindingInfo grindingInfo = new DfGrindingInfo();
            grindingInfo.setTypeData(datas.getTypeData());
            grindingInfo.setMachineCode(datas.getMachineCode());
            grindingInfo.setNIndexTool(datas.getNIndexTool());
            grindingInfo.setNStatusTool(datas.getNStatusTool());
            grindingInfo.setDtChanage(!datas.getDTChanage().equals("") ? Timestamp.valueOf(datas.getDTChanage()) : null);
            grindingInfo.setDtAbandon(!datas.getDTAbandon().equals("") ? Timestamp.valueOf(datas.getDTAbandon()) : null);
            grindingInfo.setToolCode(datas.getToolCode());
            grindingInfo.setToolSpecCode(datas.getToolSpecCode());
            grindingInfo.setNTotalUsagePro(datas.getNTotalUsagePro());
            grindingInfo.setNTotalUsageSec(datas.getNTotalUsageSec());
            grindingInfo.setPubTime(new Timestamp(datas.getPubTime() * 1000));
            if (datas.getNStatusTool() != 1) {  // 是否是首刀
                grindingInfo.setChangeKnifeStatus(0);
            } else {  // 是首刀分状态, 正常换刀/非正常换刀
                QueryWrapper<DfGrindingInfo> gQW = new QueryWrapper<>();
                gQW.eq("machine_code", datas.getMachineCode())
                        .eq("n_index_tool", datas.getNIndexTool())
                        .lt("pub_time", new Timestamp(datas.getPubTime() * 1000))
                        .orderByDesc("id")
                        .last("limit 1");
                // 获取上一次刀
                DfGrindingInfo lastData = dfGrindingInfoService.getOne(gQW);
                if (lastData == null) {
                    grindingInfo.setChangeKnifeStatus(0);
                } else {
                    // 如果上一个刀 刀了600片或以上,则为正常换刀 状态为2, 否则为1
                    if (lastData.getNTotalUsagePro() >= 600) {
                        grindingInfo.setChangeKnifeStatus(2);
                    } else {
                        grindingInfo.setChangeKnifeStatus(1);
                    }
                }
            }
            grindingInfo.setIsUse(0);
//            System.out.println("接收到刀具数据:" + grindingInfo);
            dfGrindingInfoService.save(grindingInfo);


            if (env.getProperty("ISIPQC", "N").equals("Y")
                    && ((env.getProperty("IPQCProcess", "all").contains(process.getProcessName())||env.getProperty("IPQCProcess", "all").equals("all") )
                    && (env.getProperty("IPQCMac", "all").contains(datas.getMachineCode()) || env.getProperty("IPQCMac", "all").equals("all")))) {

                //动态ipqc收严时间(小时)
                double IpqcTimeStandardTighten=Double.valueOf(env.getProperty("IpqcTimeStandardTighten","1.5"));
                //动态ipqc正常时间(小时)
                double IpqcTimeStandardNormal=Double.valueOf(env.getProperty("IpqcTimeStandardNormal","3"));
                //动态ipqc放宽时间阶段一(小时)
                double IpqcTimeStandardRelax1=Double.valueOf(env.getProperty("IpqcTimeStandardRelax1","6"));
                //动态ipqc放宽时间阶段二(小时)
                double IpqcTimeStandardRelax2=Double.valueOf(env.getProperty("IpqcTimeStandardRelax2","12"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ValueOperations valueOperations = redisTemplate.opsForValue();
                if(datas.getNStatusTool()==2){
                    int result=0;
                    QueryWrapper<DfGrindingStandard> grQw = new QueryWrapper<>();
                    grQw.eq("process",process.getProcessName());
                    grQw.eq("tool_num",datas.getNIndexTool());
                    DfGrindingStandard standard = dfGrindingStandardService.getOne(grQw);
                    if(null!=standard){
                        if(((1-(Double.valueOf(datas.getNTotalUsagePro()+"")/Double.valueOf(standard.getStandard()+"")))*100)<Double.valueOf(env.getProperty("IPQCGrindingLife","0"))){
                            result=1;
                        }
                    }
                    if(result==1){
                        //动态IPQC耗材寿命判定
                        DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","耗材寿命衰减","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC", "至"+CommunalUtils.formatNumberToTwoDecimalPlaces(((1-(Double.valueOf(datas.getNTotalUsagePro()+"")/Double.valueOf(standard.getStandard()+"")))*100))+"%("+datas.getNIndexTool()+"号刀)");
                        DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"尺寸","耗材寿命衰减","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC","至"+CommunalUtils.formatNumberToTwoDecimalPlaces(((1-(Double.valueOf(datas.getNTotalUsagePro()+"")/Double.valueOf(standard.getStandard()+"")))*100))+"%("+datas.getNIndexTool()+"号刀)");
                        Object v =  valueOperations.get("IpqcAppearance:" + datas.getMachineCode());
                        DynamicIpqcMac dim=new DynamicIpqcMac();
                        if(null!=v) {
                            dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                        }else {
                            dim.setMachineCode(datas.getMachineCode());
                            dim.setRuleName("QCP抽检频率");
                            dim.setSenMesCount(1);
                            dim.setNowCount(0);
                            dim.setSpecifiedCount(1);
                            dim.setFrequency(IpqcTimeStandardNormal);
                        }
                        dim.setSenMesCount(1);
                        dim.setNowCount(0);
//                                            dim.setTotalCount(2);
                        dim.setSpecifiedCount(99999);
                        dim.setFrequency(IpqcTimeStandardTighten);
                        dim.setRuleName("耗材寿命衰减");

                        dim.setUpdateTime(new Date().getTime());
                        dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());

                        redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getMachineCode(),gson.toJson(dim));
                        redisTemplate.opsForValue().set("IpqcSize:"+datas.getMachineCode(),gson.toJson(dim));
                        DynamicIpqcUtil.clearSizeData(redisTemplate,datas.getMachineCode(),gson);//清空缓存测试项数据
                    }
                } else if (datas.getNStatusTool()==1) {
                    if (env.getProperty("ISIPQC", "N").equals("Y") && (process.getProcessName().equals("CNC3") && (env.getProperty("IPQCMac", "all").contains(datas.getMachineCode())))) {

                        Object v =  valueOperations.get("IpqcAppearance:" + datas.getMachineCode());
                        if(null!=v) {
                            DynamicIpqcMac dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                            //动态IPQC换刀
                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","更换耗材","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC","("+datas.getNIndexTool()+"号刀)");
                            dim.setSenMesCount(1);
                            dim.setNowCount(0);
//                                            dim.setTotalCount(2);
                            dim.setSpecifiedCount(2);
                            dim.setFrequency(IpqcTimeStandardTighten);
                            dim.setRuleName("更换耗材");

                            dim.setUpdateTime(new Date().getTime());
                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                            dim.setAppearanceOkCount(0);
                            redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getMachineCode(),gson.toJson(dim));
                        }else{
                            DynamicIpqcMac dim=new DynamicIpqcMac();
                            dim.setMachineCode( datas.getMachineCode());
                            dim.setNgCount(0);
                            dim.setTotalCount(0);
                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","更换耗材","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC","("+datas.getNIndexTool()+"号刀)");
                            dim.setSenMesCount(1);
                            dim.setNowCount(0);
                            dim.setSpecifiedCount(2);
                            dim.setFrequency(IpqcTimeStandardTighten);
                            dim.setRuleName("更换耗材");

                            dim.setSenMesCount(1);
                            dim.setAppearanceOkCount(0);
                            dim.setCpkCount(0);
                            dim.setFourPointOverOne(0);
                            dim.setTwoPointOverTwo(0);
                            dim.setZugammenCount(0);
                            dim.setSpcOkCount(0);

                            dim.setUpdateTime(new Date().getTime());
                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                            redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getMachineCode(),gson.toJson(dim));
                        }
                        Object v2 =  valueOperations.get("IpqcSize:" + datas.getMachineCode());
                        if(null!=v2) {
                            DynamicIpqcMac dim = new Gson().fromJson(v2.toString(), DynamicIpqcMac.class);
                            //动态IPQC换刀
                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"尺寸","更换耗材","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC","("+datas.getNIndexTool()+"号刀)");
                            dim.setSenMesCount(1);
                            dim.setNowCount(0);
//                                            dim.setTotalCount(2);
                            dim.setSpecifiedCount(2);
                            dim.setFrequency(IpqcTimeStandardTighten);
                            dim.setRuleName("更换耗材");
                            dim.setUpdateTime(new Date().getTime());
                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                            dim.setSizeOkCount(0);
                            redisTemplate.opsForValue().set("IpqcSize:"+datas.getMachineCode(),gson.toJson(dim));
                        }else{
                            DynamicIpqcMac dim=new DynamicIpqcMac();
                            dim.setMachineCode( datas.getMachineCode());
                            dim.setNgCount(0);
                            dim.setTotalCount(0);
                            DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"尺寸","更换耗材","收严",process.getProcessName(),datas.getMachineCode(),0,env.getProperty("Project", "C98B"),"动态IPQC","("+datas.getNIndexTool()+"号刀)");
                            dim.setSenMesCount(1);
                            dim.setNowCount(0);
                            dim.setSpecifiedCount(2);
                            dim.setFrequency(IpqcTimeStandardTighten);
                            dim.setRuleName("更换耗材");

                            dim.setSenMesCount(1);
                            dim.setAppearanceOkCount(0);
                            dim.setSizeOkCount(0);
                            dim.setCpkCount(0);
                            dim.setFourPointOverOne(0);
                            dim.setTwoPointOverTwo(0);
                            dim.setZugammenCount(0);
                            dim.setSpcOkCount(0);

                            dim.setUpdateTime(new Date().getTime());
                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                            redisTemplate.opsForValue().set("IpqcSize:"+datas.getMachineCode(),gson.toJson(dim));
                        }

                    }


                }


            }

        }

    }
}
