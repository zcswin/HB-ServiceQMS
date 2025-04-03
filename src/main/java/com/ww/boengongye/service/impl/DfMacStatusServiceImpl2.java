//package com.ww.boengongye.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.google.gson.Gson;
//import com.ww.boengongye.entity.DfMacChange;
//import com.ww.boengongye.entity.DfMacStatus;
//import com.ww.boengongye.entity.MachineStatusQueue;
//import com.ww.boengongye.mapper.DfMacStatusMapper;
//import com.ww.boengongye.service.*;
//import com.ww.boengongye.utils.TimeUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.sql.Timestamp;
//import java.util.List;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author zhao
// * @since 2022-08-04
// */
//@Service
//public class DfMacStatusServiceImpl2 extends ServiceImpl<DfMacStatusMapper, DfMacStatus> implements DfMacStatusService {
//
//    @Autowired
//    DfMacStatusMapper DfMacStatusMapper;
//
//    @Autowired
//    DfMacStatusDetailService dfMacStatusDetailService;
//    @Autowired
//    DfLiableManService dfLiableManService;
//
//    @Autowired
//    DfAuditDetailService dfAuditDetailService;
//
//    @Autowired
//    DfFlowDataService dfFlowDataService;
//    @Autowired
//    DfProTimeService dfProTimeService;
//    @Autowired
//    private Environment env;
//    @Autowired
//    private DfMacChangeService dfMacChangeService;
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private DfProcessService dfProcessService;
//    @Autowired
//    private DfGrindingStandardService dfGrindingStandardService;
//
//    @Override
//    public List<DfMacStatus> listStatus(Wrapper<DfMacStatus> wrapper) {
//        return DfMacStatusMapper.listStatus(wrapper);
//    }
//
//    @Override
//    public List<DfMacStatus> countByStatus() {
//        return DfMacStatusMapper.countByStatus();
//    }
//
////    private static Lock lock = new ReentrantLock();
//
//    @Override
//    public Integer updateStatus(String content) {
////        System.out.println("进入状态方法:"+content);
//        MachineStatusQueue datas = new Gson().fromJson(content, MachineStatusQueue.class);
//
//        synchronized (datas.getMachineCode()) {
//            QueryWrapper<DfMacStatus> qw = new QueryWrapper<>();
//            qw.eq("MachineCode", datas.getMachineCode());
//            qw.last("limit 1");
////        lock.lock();// 加锁预防并发操作同一条信息
////        try {
//            DfMacStatus ds = DfMacStatusMapper.selectOne(qw);
//            if (null != ds) {
//                if (null != ds.getStatusidCur() && ds.getStatusidCur() != datas.getStatusIdCur()) {
//                    System.out.println("更新");
//                    ds.setStatusidPre(datas.getStatusIdPre());
//                    ds.setStatusStep(datas.getStatusStep());
//                    ds.setStatusidCur(datas.getStatusIdCur());
//                    ds.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                    DfMacStatusMapper.updateById(ds);
//
//                    if (datas.getStatusIdCur() == 4) {
//                        DfMacChange dfMacChange = new DfMacChange();
//                        dfMacChange.setMachineCode(datas.getMachineCode());
//                        dfMacChange.setStatusIdCur(datas.getStatusIdCur());
//                        dfMacChange.setPubTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
//                        dfMacChange.setStatusIdPre(datas.getStatusIdPre());
//                        dfMacChange.setStatusStep(datas.getStatusStep());
//                        dfMacChangeService.save(dfMacChange);
//                    }
//
//
//                }
//
//
////                if (ds.getStatusidCur() == 3) {
////                    QueryWrapper<DfAuditDetail> adQw = new QueryWrapper<>();
////                    adQw.eq("mac_code", datas.getMachineCode());
////                    adQw.isNull("end_time");
////                    if (dfAuditDetailService.count(adQw) == 0) {
////
////                        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
////                        sqw.eq("type", "status");
////                        sqw.eq("problem_level", "1");
////
////                        List<DfLiableMan> lm = dfLiableManService.list(sqw);
////                        if (lm.size() > 0) {
////                            StringBuilder manCode = new StringBuilder();
////                            StringBuilder manName = new StringBuilder();
////                            int manCount = 0;
////                            for (DfLiableMan l : lm) {
////                                if (manCount > 0) {
////                                    manCode.append(",");
////                                    manName.append(",");
////                                }
////                                manCode.append(l.getLiableManCode());
////                                manName.append(l.getLiableManName());
////                                manCount++;
////                            }
////                            DfAuditDetail aud = new DfAuditDetail();
////                            aud.setMacCode(ds.getMachineCode());
////                            aud.setParentId(ds.getId());
////                            aud.setDataType("设备状态");
////                            aud.setAffectMac("1");
////                            aud.setAffectNum(1.0);
////                            aud.setImpactType("设备状态");
////                            aud.setIsFaca("0");
////                            //问题名称和现场实际调换
////                            aud.setQuestionName(ds.getMachineCode() + ",机台报警!");
////                            aud.setScenePractical(ds.getMachineCode() + ",机台报警!");
////                            aud.setProjectName("C27");
////                            QueryWrapper<DfLiableMan> sqw2 = new QueryWrapper<>();
////                            sqw2.eq("type", "statusInitiator");
////                            sqw2.eq("problem_level", "1");
////                            sqw2.last("limit 1");
////
////                            DfLiableMan rpm = dfLiableManService.getOne(sqw2);
////                            if (null != rpm) {
////                                aud.setReportMan(rpm.getLiableManName());
////                                aud.setCreateName(rpm.getLiableManName());
////                                aud.setCreateUserId(rpm.getLiableManCode());
////                            }
////                            aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
////                            aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
////                            aud.setIpqcNumber(TimeUtil.getNowTimeLong());
////                            //由于MQ没有返回对应的检测类型,现在默认为过程检CPK
////                            aud.setQuestionType("CPK");
////                            aud.setDecisionLevel("Level1");
////                            aud.setHandlingSug("检查机台异常原因");
////                            aud.setResponsible(manName.toString());
////                            aud.setResponsibleId(manCode.toString());
////                            dfAuditDetailService.save(aud);
////
////                            DfFlowData fd = new DfFlowData();
////                            fd.setFlowLevel(1);
////                            fd.setDataType(aud.getDataType());
////                            fd.setFlowType(aud.getDataType());
////                            fd.setName("设备状态_" + aud.getQuestionName() + "_" + TimeUtil.getNowTimeByNormal());
////                            fd.setDataId(aud.getId());
////                            fd.setStatus("待确认");
////                            fd.setCreateName(aud.getCreateName());
////                            fd.setCreateUserId(aud.getCreateUserId());
////                            fd.setNowLevelUser(aud.getResponsibleId());
////                            fd.setNowLevelUserName(aud.getResponsible());
////                            fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
////                            fd.setFlowLevelName(aud.getDecisionLevel());
////
////
////                            dfFlowDataService.save(fd);
////
////                        }
////                    }
////                }
//            }
////        } finally {
////            lock.unlock();
////        }
//        }
//
//        return 0;
//    }
//
//    @Override
//    public List<DfMacStatus> listJoinCode(Wrapper<DfMacStatus> wrapper) {
//        return DfMacStatusMapper.listJoinCode(wrapper);
//    }
//}
