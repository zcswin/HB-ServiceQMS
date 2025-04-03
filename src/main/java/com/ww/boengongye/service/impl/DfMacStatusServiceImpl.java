package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfMacStatusMapper;
import com.ww.boengongye.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.DynamicIpqcUtil;
import com.ww.boengongye.utils.InitializeService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Service
public class DfMacStatusServiceImpl extends ServiceImpl<DfMacStatusMapper, DfMacStatus> implements DfMacStatusService {

    @Autowired
    DfMacStatusMapper DfMacStatusMapper;

    @Autowired
    DfMacStatusDetailService dfMacStatusDetailService;
    @Autowired
    DfLiableManService dfLiableManService;

    @Autowired
    DfAuditDetailService dfAuditDetailService;

    @Autowired
    DfFlowDataService dfFlowDataService;
    @Autowired
    DfProTimeService dfProTimeService;
    @Autowired
    private Environment env;
    @Autowired
    private DfMacChangeService dfMacChangeService;
    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private DfProcessService dfProcessService;
    @Autowired
    private DfGrindingStandardService dfGrindingStandardService;

    @Override
    public List<DfMacStatus> listStatus(Wrapper<DfMacStatus> wrapper) {
        return DfMacStatusMapper.listStatus(wrapper);
    }

    @Override
    public List<DfMacStatus> countByStatus() {
        return DfMacStatusMapper.countByStatus();
    }

//    private static Lock lock = new ReentrantLock();

    @Override
    public Integer updateStatus(String content) {
//        System.out.println("进入状态方法:"+content);
        MachineStatusQueue datas = new Gson().fromJson(content, MachineStatusQueue.class);


//        QueryWrapper<DfMacStatus> qw = new QueryWrapper<>();
//        qw.eq("MachineCode", datas.getMachineCode());
//        qw.last("limit 1");
//        lock.lock();// 加锁预防并发操作同一条信息
//        try {

//        DfMacStatusMapper.selectOne(qw);
        if (InitializeCheckRule.macStatus.containsKey(datas.getMachineCode())) {
            DfMacStatus ds = InitializeCheckRule.macStatus.get(datas.getMachineCode());
            if (null != ds.getStatusidCur() && ds.getStatusidCur() != datas.getStatusIdCur()) {
                ds.setStatusidPre(datas.getStatusIdPre());
                ds.setStatusStep(datas.getStatusStep());
                ds.setStatusidCur(datas.getStatusIdCur());
                ds.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                InitializeCheckRule.macStatus.put(datas.getMachineCode(), ds);
//                DfMacStatusMapper.updateById(ds);

                if (datas.getStatusIdCur() == 4) {
                    DfMacChange dfMacChange = new DfMacChange();
                    dfMacChange.setMachineCode(datas.getMachineCode());
                    dfMacChange.setStatusIdCur(datas.getStatusIdCur());
                    dfMacChange.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                    dfMacChange.setStatusIdPre(datas.getStatusIdPre());
                    dfMacChange.setStatusStep(datas.getStatusStep());
//                    dfMacChange.setWarningMes(datas.g);
                    dfMacChangeService.save(dfMacChange);
                }


            }
//            DfMacStatusDetail dfm=new DfMacStatusDetail();
//            dfm.setMachineCode(datas.getMachineCode());
//            dfm.setStatusidCur(datas.getStatusIdCur());
//            dfMacStatusDetailService.save(dfm);


            //换算刀具使用时间
//            if (env.getProperty("ISIPQC", "N").equals("Y") &&  (env.getProperty("IPQCMac", "all").contains(datas.getMachineCode())||env.getProperty("IPQCMac", "all").contains("all"))) {
//                String firstCode1 = datas.getMachineCode().substring(0, 1);
//                QueryWrapper<DfProcess> dfProcessQueryWrapper = new QueryWrapper<>();
//                dfProcessQueryWrapper.eq("first_code",firstCode1);
//                DfProcess process = dfProcessService.getOne(dfProcessQueryWrapper);
//
//                QueryWrapper<DfProTime> ptQw = new QueryWrapper<>();
//                ptQw.eq("prcess",process.getProcessName());
//                DfProTime pt =dfProTimeService.getOne(ptQw);
//
//                if(null!=pt){
//                    Set<String> keys= redisTemplate.keys("IpqcGrinding:" + datas.getMachineCode()+"*");
//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    ValueOperations valueOperations = redisTemplate.opsForValue();
//                    int result=0;
//                    for(String key:keys){
//                        Object toolData =  valueOperations.get(key);
//                        if(null!=toolData) {
//                            if(datas.getStatusStep()<=(pt.getStandard()+pt.getUpperLimit())&&datas.getStatusStep()>=(pt.getStandard()-pt.getLowerLimit())){
//
//
//                                DfKnifStatus tool = new Gson().fromJson(toolData.toString(), DfKnifStatus.class);
//                                QueryWrapper<DfGrindingStandard> grQw = new QueryWrapper<>();
//                                grQw.eq("process",process.getProcessName());
//                                grQw.eq("tool_num",tool.getNNumTool());
//                                DfGrindingStandard standard = dfGrindingStandardService.getOne(grQw);
//                                tool.setToolCutNum(tool.getToolCutNum()+1);
//                                if(null!=standard){
//
//                                    if(tool.getToolCutNum()/standard.getStandard()*100<66){
//                                        result=1;
//                                    }
//                                }
//
//                                tool.setCheckTime(TimeUtil.getNowTimeByNormal());
//                                redisTemplate.opsForValue().set(key,gson.toJson(tool));
//                            }
//
//                        }
//                    }
//
//                    if(result==1){
//                        //动态IPQC耗材寿命判定
//                        DynamicIpqcUtil.sendMes(1.0,"外观","耗材寿命衰减","收严",process.getProcessName(),datas.getMachineCode(),0,"C98B","");
//                        DynamicIpqcUtil.sendMes(1.0,"尺寸","耗材寿命衰减","收严",process.getProcessName(),datas.getMachineCode(),0,"C98B","");
//                        Object v =  valueOperations.get("IpqcAppearance:" + datas.getMachineCode());
//                        DynamicIpqcMac dim=new DynamicIpqcMac();
//                        if(null!=v) {
//                            dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
//                        }else {
//                            dim.setMachineCode(datas.getMachineCode());
//                            dim.setRuleName("QCP抽检频率");
//                            dim.setSenMesCount(1);
//                            dim.setNowCount(0);
//                            dim.setSpecifiedCount(1);
//                            dim.setFrequency(2.0);
//                        }
//                            dim.setSenMesCount(1);
//                            dim.setNowCount(0);
////                                            dim.setTotalCount(2);
//                            dim.setSpecifiedCount(99999);
//                            dim.setFrequency(1.0);
//                            dim.setRuleName("耗材寿命衰减");
//
//                            dim.setUpdateTime(new Date().getTime());
//                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//
//                            redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getMachineCode(),gson.toJson(dim));
//                            redisTemplate.opsForValue().set("IpqcSize:"+datas.getMachineCode(),gson.toJson(dim));
//                            DynamicIpqcUtil.clearSizeData(redisTemplate,datas.getMachineCode(),gson);//清空缓存测试项数据
//                    }
//                }
//
//            }
//

//            if (ds.getStatusidCur() == 3) {
//                QueryWrapper<DfAuditDetail> adQw = new QueryWrapper<>();
//                adQw.eq("mac_code", datas.getMachineCode());
//                adQw.isNull("end_time");
//                if (dfAuditDetailService.count(adQw) == 0) {
//
//                    QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
//                    sqw.eq("type", "status");
//                    sqw.eq("problem_level", "1");
////                sqw.last("limit 1");
////                    if(TimeUtil.getBimonthly()==0){
////                        sqw.like("bimonthly","双月");
////                    }else{
////                        sqw.like("bimonthly","单月");
////                    }
////                    sqw.like("process_name",dd.getProcess());
//                    List<DfLiableMan> lm = dfLiableManService.list(sqw);
//                    if (lm.size() > 0) {
//                        StringBuilder manCode = new StringBuilder();
//                        StringBuilder manName = new StringBuilder();
//                        int manCount = 0;
//                        for (DfLiableMan l : lm) {
//                            if (manCount > 0) {
//                                manCode.append(",");
//                                manName.append(",");
//                            }
//                            manCode.append(l.getLiableManCode());
//                            manName.append(l.getLiableManName());
//                            manCount++;
//                        }
//                        DfAuditDetail aud = new DfAuditDetail();
//                        aud.setMacCode(ds.getMachineCode());
//                        aud.setParentId(ds.getId());
//                        aud.setDataType("设备状态");
////                    aud.setDepartment(dd.getProcess());
//                        aud.setAffectMac("1");
//                        aud.setAffectNum(1.0);
////                    aud.setControlStandard(gkbz.toString());
//                        aud.setImpactType("设备状态");
//                        aud.setIsFaca("0");
//                        //问题名称和现场实际调换
//                        aud.setQuestionName(ds.getMachineCode() + ",机台报警!");
//                        aud.setScenePractical(ds.getMachineCode() + ",机台报警!");
////                    aud.setProcess(dd.getProcess());
//                        aud.setProjectName("C27");
//                        QueryWrapper<DfLiableMan> sqw2 = new QueryWrapper<>();
//                        sqw2.eq("type", "statusInitiator");
//                        sqw2.eq("problem_level", "1");
////                    sqw2.like("process_name",dd.getProcess());
//                        sqw2.last("limit 1");
////                        if (TimeUtil.getBimonthly() == 0) {
////                            sqw2.like("bimonthly", "双月");
////                        } else {
////                            sqw2.like("bimonthly", "单月");
////                        }
//                        DfLiableMan rpm = dfLiableManService.getOne(sqw2);
//                        if (null != rpm) {
//                            aud.setReportMan(rpm.getLiableManName());
//                            aud.setCreateName(rpm.getLiableManName());
//                            aud.setCreateUserId(rpm.getLiableManCode());
//                        }
//
//                        aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                        aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                        aud.setIpqcNumber(TimeUtil.getNowTimeLong());
//                        //由于MQ没有返回对应的检测类型,现在默认为过程检CPK
//                        aud.setQuestionType("CPK");
//                        aud.setDecisionLevel("Level1");
//                        aud.setHandlingSug("检查机台异常原因");
//                        aud.setResponsible(manName.toString());
//                        aud.setResponsibleId(manCode.toString());
////                    aud.setCreateName(lm.getLiableManName());
////                    aud.setCreateUserId(lm.getLiableManCode());
//                        dfAuditDetailService.save(aud);
//
//                        DfFlowData fd = new DfFlowData();
//                        fd.setFlowLevel(1);
//                        fd.setDataType(aud.getDataType());
//                        fd.setFlowType(aud.getDataType());
//                        fd.setName("设备状态_" + aud.getQuestionName() + "_" + TimeUtil.getNowTimeByNormal());
//                        fd.setDataId(aud.getId());
////                    fd.setFlowLevelName(fb.getName());
//                        fd.setStatus("待确认");
////                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
//                        fd.setCreateName(aud.getCreateName());
//                        fd.setCreateUserId(aud.getCreateUserId());
//
//                        fd.setNowLevelUser(aud.getResponsibleId());
//                        fd.setNowLevelUserName(aud.getResponsible());
//                        fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//
//                        fd.setFlowLevelName(aud.getDecisionLevel());
////                        QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
////                        atQw.eq("type","尺寸");
////                        atQw.last("limit 1");
////                        DfApprovalTime at=    dfApprovalTimeService.getOne(atQw);
////                        if(null!=at){
////                            if(fd.getFlowLevelName().equals("Level1")){
////                                fd.setReadTimeMax(at.getReadTimeLevel1());
////                                fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
////                            }else  if(fd.getFlowLevelName().equals("Level2")){
////                                fd.setReadTimeMax(at.getReadTimeLevel2());
////                                fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
////                            }else  if(fd.getFlowLevelName().equals("Level3")){
////                                fd.setReadTimeMax(at.getReadTimeLevel3());
////                                fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
////                            }
////                        }
//
//                        dfFlowDataService.save(fd);
//
//                    }
//                }
//            }
        }
//        } finally {
//            lock.unlock();
//        }
        return 0;
    }

    @Override
    public List<DfMacStatus> listJoinCode(Wrapper<DfMacStatus> wrapper) {
        return DfMacStatusMapper.listJoinCode(wrapper);
    }
}
