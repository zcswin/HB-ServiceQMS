package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.exportExcel.BatchqueryReport;
import com.ww.boengongye.entity.exportExcel.EmpCapacityReport;
import com.ww.boengongye.entity.exportExcel.OqcReport;
import com.ww.boengongye.mapper.*;
import com.ww.boengongye.service.DfAoiDefectService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.InitializeService;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.boengongye.utils.Base64Utils.base64StrToImage;

/**
 * <p>
 * AOI玻璃单片信息表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@Service
@EnableScheduling
public class DfAoiPieceServiceImpl extends ServiceImpl<DfAoiPieceMapper, DfAoiPiece> implements DfAoiPieceService {

    @Autowired
    private DfAoiPieceMapper dfAoiPieceMapper;

    @Autowired
    private DfAuditDetailMapper dfAuditDetailMapper;

    @Autowired
    private DfFlowDataMapper dfFlowDataMapper;

    @Autowired
    private DfLiableManMapper dfLiableManMapper;

    @Autowired
    private DfYieldWarnMapper dfYieldWarnMapper;

    @Autowired
    private DfApprovalTimeMapper dfApprovalTimeMapper;

    @Autowired
    private Environment env;
    @Autowired
    private DfAoiDefectService dfAoiDefectService;

    @Override
    public List<DfAoiPiece> getDfAoiPieceListByBarCode(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getDfAoiPieceListByBarCode(wrapper);
    }

    @Override
    public Integer getDefectPieceNumber(Wrapper<Integer> wrapper) {
        return dfAoiPieceMapper.getDefectPieceNumber(wrapper);
    }

    @Override
    public Rate3 getUserInputAndDefectNumber(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getUserInputAndDefectNumber(wrapper);
    }

    @Override
    public List<DfAoiDefectPoint> getDefectPointList(IPage<DfAoiDefectPoint> page, Wrapper<DfAoiDefectPoint> wrapper) {
        return dfAoiPieceMapper.getDefectPointList(page, wrapper);
    }

    @Override
    public List<DfAoiDefectPoint> getUserDefectTop5List(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getUserDefectTop5List(wrapper);
    }

    @Override
    public List<DfAoiEscapePoint> getEscapePointList(IPage<DfAoiEscapePoint> page, Wrapper<DfAoiEscapePoint> wrapper) {
        return dfAoiPieceMapper.getEscapePointList(page, wrapper);
    }

    @Override
    public List<DfAoiEscapePoint> getEscapePointTop5List(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getEscapePointTop5List(wrapper);
    }

    @Override
    public List<DfAoiEscape> getAllEscapeList(Wrapper<DfAoiEscape> wrapper) {
        return dfAoiPieceMapper.getAllEscapeList(wrapper);
    }

    @Override
    public List<DfAoiEscapePoint> getEscapeTop5PointList(Wrapper<DfAoiEscapePoint> wrapper) {
        return dfAoiPieceMapper.getEscapeTop5PointList(wrapper);
    }

    @Override
    public List<User> getAllUserOQCNumberList(IPage<User> page, Wrapper<User> wrapper) {
        return dfAoiPieceMapper.getAllUserOQCNumberList(page, wrapper);
    }

    @Override
    public Integer getUserEscapeNumber(Wrapper<Integer> wrapper) {
        return dfAoiPieceMapper.getUserEscapeNumber(wrapper);
    }

    @Override
    public List<DfAoiOutputPoint> getUserHourOutputPointList(Wrapper<DfAoiOutputPoint> wrapper) {
        return dfAoiPieceMapper.getUserHourOutputPointList(wrapper);
    }

    @Override
    public List<String> getFqcUserDefect7Day(Wrapper<String> wrapper) {
        return dfAoiPieceMapper.getFqcUserDefect7Day(wrapper);
    }

    @Override
    public List<DfAoiEscape> getFqcUserOqcNumberDay(Wrapper<DfAoiEscape> wrapper) {
        return dfAoiPieceMapper.getFqcUserOqcNumberDay(wrapper);
    }

    @Override
    public List<String> getFqcUserDefect4Week(Wrapper<String> wrapper) {
        return dfAoiPieceMapper.getFqcUserDefect4Week(wrapper);
    }

    @Override
    public List<DfAoiEscape> getFqcUserOqcNumberWeek(Wrapper<DfAoiEscape> wrapper) {
        return dfAoiPieceMapper.getFqcUserOqcNumberWeek(wrapper);
    }

    @Override
    public DfAoiDetermine getDfAoiDetermineByBarCode(Wrapper<DfAoiDetermine> wrapper) {
        return dfAoiPieceMapper.getDfAoiDetermineByBarCode(wrapper);
    }

    @Override
    public List<Rate3> listPieceAndDefectNumGroupByHour(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.listPieceAndDefectNumGroupByHour(wrapper);
    }

    @Override
    public List<BatchqueryReport> getFqcOrOQCDetailByCode(Page<BatchqueryReport> page, QueryWrapper<DfAoiPiece> qw) {
        return dfAoiPieceMapper.getFqcOrOQCDetailByCode(page, qw);
    }

    @Override
    public List<EmpCapacityReport> getEmpCapacityStatement(Page<EmpCapacityReport> page, QueryWrapper<DfAoiPiece> qw, String startTime, String endTime) {
        return dfAoiPieceMapper.getEmpCapacityStatement(page, qw, startTime, endTime);
    }

    @Override
    public List<OqcReport> getOQCReport(Page<OqcReport> pages, QueryWrapper<DfAoiPiece> qw, QueryWrapper<DfAoiPiece> qw2, String startTime, String endTime, String empCode) {
        return dfAoiPieceMapper.getOQCReport(pages, qw, qw2, startTime, endTime, empCode);
    }

    @Override
    public List<Rate3> lineBodyloss(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String clazz) {
        return dfAoiPieceMapper.lineBodyloss(qw, startTime, endTime, clazz);
    }

    @Override
    public List<Map<String, Object>> OQC31Report(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String empCode) {
        return dfAoiPieceMapper.OQC31Report(qw, startTime, endTime, empCode);
    }


    @Override
    public List<Rate3> lineBodylossByMachine(QueryWrapper<DfAoiPiece> qw2, String startTime, String endTime, String clazz) {
        return dfAoiPieceMapper.lineBodylossByMachine(qw2, startTime, endTime, clazz);
    }

    @Override
    public List<Rate3> lossEmpTop10(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String clazz) {
        return dfAoiPieceMapper.lossEmpTop10(qw, startTime, endTime, clazz);
    }

    @Override
    public List<Rate3> lineBodylossV2(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String clazz) {
        return dfAoiPieceMapper.lineBodylossV2(qw, startTime, endTime, clazz);
    }

    @Override
    public List<Map> fqcInputOut(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.fqcInputOut(ew);
    }

    @Override
    public List<Map<String, Object>> lossCheck(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.lossCheck(ew);
    }

    @Override
    public List<Map<String, String>> top5Feature(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.top5Feature(ew);
    }

    @Override
    public List<Map<String, Object>> selectByfeature(QueryWrapper<DfAoiPiece> ew2, String feature) {
        return dfAoiPieceMapper.selectByfeature(ew2, feature);
    }

    @Override
    public List<Rate3> getDetailByMachine(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.getDetailByMachine(ew);
    }

    @Override
    public List<Rate3> empLossChemkRank(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.empLossChemkRank(ew);
    }

    @Override
    public List<String> getProjectList(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getProjectList(wrapper);
    }

    @Override
    public List<Rate3> getProjectDefectPointList(Wrapper<DfAoiPiece> wrapper) {
        return dfAoiPieceMapper.getProjectDefectPointList(wrapper);
    }

    @Override
    public List<Rate3> getItemDefectPointListDay(Wrapper<DfAoiPiece> wrapper, Wrapper<DfAoiPiece> wrapper2, Wrapper<String> wrapper3) {
        return dfAoiPieceMapper.getItemDefectPointListDay(wrapper, wrapper2, wrapper3);
    }

    @Override
    public List<Rate3> getItemDefectPointListWeek(Wrapper<DfAoiPiece> wrapper, Wrapper<DfAoiPiece> wrapper2, Wrapper<String> wrapper3) {
        return dfAoiPieceMapper.getItemDefectPointListWeek(wrapper, wrapper2, wrapper3);
    }

    @Override
    public List<Rate3> getItemDefectPointListMonth(Wrapper<DfAoiPiece> wrapper, Wrapper<DfAoiPiece> wrapper2, Wrapper<String> wrapper3) {
        return dfAoiPieceMapper.getItemDefectPointListMonth(wrapper, wrapper2, wrapper3);
    }

    @Override
    public List<String> getAllDefectClassList() {
        return dfAoiPieceMapper.getAllDefectClassList();
    }

    @Override
    public List<String> getAllDefectNameList() {
        return dfAoiPieceMapper.getAllDefectNameList();
    }

    @Override
    public List<String> getAllDefectAreaList() {
        return dfAoiPieceMapper.getAllDefectAreaList();
    }

    @Override
    public List<String> getAllDefectPosition() {
        return dfAoiPieceMapper.getAllDefectPosition();
    }

    @Override
    public List<Map<String, Object>> getAoiReportByHour(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.getAoiReportByHour(ew);
    }

    @Override
    public List<Map<String, Object>> getAoiReportByDay(QueryWrapper<DfAoiPiece> ew) {
        return dfAoiPieceMapper.getAoiReportByDay(ew);
    }


    @Scheduled(cron = "0 0 * * * *")
    public void checkAoiPassPointHour() {
        System.out.println("AOI机台良率检测start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();

        QueryWrapper<DfYieldWarn> dfYieldWarnWrapper = new QueryWrapper<>();
        dfYieldWarnWrapper
                .eq("`type`", "AOI")
                .eq("name", "良率预警")
                .last("limit 1");
        //AOI良率预警和报警值
        DfYieldWarn dfYieldWarn = dfYieldWarnMapper.selectOne(dfYieldWarnWrapper);

        //所有机台的良率
        List<Rate3> machinePassPointList = dfAoiPieceMapper.getMachinePassPointHour();
        if (machinePassPointList == null || machinePassPointList.size() == 0) {
            return;
        }
        for (Rate3 machine : machinePassPointList) {

            //判断该机台是否进行报警
            if (machine.getDou1() <= dfYieldWarn.getAlarmValue()) {
                QueryWrapper<DfAuditDetail> auditDetailWrapper = new QueryWrapper<>();
                auditDetailWrapper
                        .eq("affect_mac", machine.getStr1())
                        .eq("data_type", "AOI")
                        .eq("question_type", "良率报警")
                        .orderByDesc("occurrence_time")
                        .last("limit 1");
                //获取当前机台的最新稽核记录
                DfAuditDetail dfAuditDetail = dfAuditDetailMapper.selectOne(auditDetailWrapper);
                //当前存在未处理的稽核记录
                if (dfAuditDetail != null && dfAuditDetail.getEndTime() == null) {
                    return;
                }

                QueryWrapper<DfAoiPiece> projectWrapper = new QueryWrapper<>();
                projectWrapper.eq("dap.machine", machine.getStr1());
                List<String> projectList = dfAoiPieceMapper.getProjectByMachine(projectWrapper);

                //获取当前机台的项目
                StringBuilder projectName = new StringBuilder();
                int projectCount = 0;
                for (String project : projectList) {
                    if (projectCount > 0) {
                        projectName.append(",");
                    }
                    projectName.append(project);
                    projectCount++;
                }

                QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                sqw.eq("type", "AOI");
                sqw.eq("problem_level", "1");
                if (TimeUtil.getBimonthly() == 0) {
                    sqw.like("bimonthly", "双月");
                } else {
                    sqw.like("bimonthly", "单月");
                }
                sqw.like("process_name", "AOI良率");
                List<DfLiableMan> lm = dfLiableManMapper.selectList(sqw);
                if (lm == null || lm.size() == 0) {
                    return;
                }
                StringBuilder manCode = new StringBuilder();
                StringBuilder manName = new StringBuilder();
                int manCount = 0;
                for (DfLiableMan l : lm) {
                    if (manCount > 0) {
                        manCode.append(",");
                        manName.append(",");
                    }
                    manCode.append(l.getLiableManCode());
                    manName.append(l.getLiableManName());
                    manCount++;
                }
                DfAuditDetail aud = new DfAuditDetail();
                aud.setLine(machine.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
                aud.setDataType("AOI");
                aud.setDepartment("AOI");
                aud.setAffectMac(machine.getStr1());
                aud.setAffectNum(1.0);
                aud.setControlStandard("AOI良率小于等于" + dfYieldWarn.getAlarmValue() + "%,AOI良率报警");
                aud.setImpactType("AOI");
                aud.setIsFaca("0");

                //问题名称和现场实际调换
                aud.setQuestionName("良率报警");
                aud.setProcess("AOI");
                aud.setProjectName(projectName.toString());

                aud.setReportTime(Timestamp.valueOf(checkTime));
                aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
                aud.setIpqcNumber(checkTimeLong);

                aud.setScenePractical("AOI_机台" + machine.getStr1() + "_良率报警，良率为" + machine.getDou1() + "%");
                aud.setQuestionType("良率报警");
                aud.setDecisionLevel("Level1");
                aud.setHandlingSug("全检风险批");
                aud.setResponsible(manName.toString());
                aud.setResponsibleId(manCode.toString());
                dfAuditDetailMapper.insert(aud);

                DfFlowData fd = new DfFlowData();
                fd.setFlowLevel(1);
                fd.setDataType(aud.getDataType());
                fd.setFlowType(aud.getDataType());
                fd.setName("AOI_机台" + machine.getStr1() + "_" + aud.getQuestionName() + "_NG_" + checkTime);
                fd.setDataId(aud.getId());
                fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

                fd.setNowLevelUser(aud.getResponsibleId());
                fd.setNowLevelUserName(aud.getResponsible());
                fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
                fd.setFlowLevelName(aud.getDecisionLevel());

                QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                atQw.eq("type", "AOI")
                        .last("limit 1");
                DfApprovalTime at = dfApprovalTimeMapper.selectOne(atQw);
                if (null != at) {
                    if (fd.getFlowLevelName().equals("Level1")) {
                        fd.setReadTimeMax(at.getReadTimeLevel1());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                    } else if (fd.getFlowLevelName().equals("Level2")) {
                        fd.setReadTimeMax(at.getReadTimeLevel2());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                    } else if (fd.getFlowLevelName().equals("Level3")) {
                        fd.setReadTimeMax(at.getReadTimeLevel3());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                    }
                }
                //设置显示人
                fd.setShowApprover(fd.getNowLevelUserName());
                dfFlowDataMapper.insert(fd);

                //判断该机台是否进行预警
            } else if (machine.getDou1() <= dfYieldWarn.getPrewarningValue()) {
                QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                sqw.eq("type", "AOI");
                sqw.eq("problem_level", "1");
                if (TimeUtil.getBimonthly() == 0) {
                    sqw.like("bimonthly", "双月");
                } else {
                    sqw.like("bimonthly", "单月");
                }
                sqw.like("process_name", "AOI良率");
                List<DfLiableMan> lm = dfLiableManMapper.selectList(sqw);
                if (lm == null || lm.size() == 0) {
                    return;
                }
                StringBuilder manCode = new StringBuilder();
                StringBuilder manName = new StringBuilder();
                int manCount = 0;
                for (DfLiableMan l : lm) {
                    if (manCount > 0) {
                        manCode.append(",");
                        manName.append(",");
                    }
                    manCode.append(l.getLiableManCode());
                    manName.append(l.getLiableManName());
                    manCount++;
                }

                DfFlowData fd = new DfFlowData();
                fd.setFlowLevel(1);
                fd.setDataType("AOI");
                fd.setFlowType("AOI");
                fd.setName("AOI_机台" + machine.getStr1() + "_良率预警" + "_NG_" + checkTime);
                fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

                fd.setNowLevelUser(manCode.toString());
                fd.setNowLevelUserName(manName.toString());
                fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
                fd.setFlowLevelName("Level1");

                QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                atQw.eq("type", "AOI")
                        .last("limit 1");
                DfApprovalTime at = dfApprovalTimeMapper.selectOne(atQw);
                if (null != at) {
                    if (fd.getFlowLevelName().equals("Level1")) {
                        fd.setReadTimeMax(at.getReadTimeLevel1());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                    } else if (fd.getFlowLevelName().equals("Level2")) {
                        fd.setReadTimeMax(at.getReadTimeLevel2());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                    } else if (fd.getFlowLevelName().equals("Level3")) {
                        fd.setReadTimeMax(at.getReadTimeLevel3());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                    }
                }
                //设置显示人
                fd.setShowApprover(fd.getNowLevelUserName());
                dfFlowDataMapper.insert(fd);
            }

        }
    }


    @Scheduled(cron = "0 0 * * * *")
    public void checkIAEscapePointHour() {
        System.out.println("IA漏检率检测start***************************************");
        //获取当前检测时间
        String checkTime = TimeUtil.getNowTimeByNormal();
        //当前时间戳
        String checkTimeLong = TimeUtil.getNowTimeLong();

        QueryWrapper<DfYieldWarn> dfYieldWarnWrapper = new QueryWrapper<>();
        dfYieldWarnWrapper
                .eq("`type`", "IA")
                .eq("name", "漏检预警")
                .last("limit 1");
        //IA漏检报警值
        DfYieldWarn dfYieldWarn = dfYieldWarnMapper.selectOne(dfYieldWarnWrapper);

        //所有FQC的漏检率
        List<Rate3> fqcEscapePointList = dfAoiPieceMapper.getFqcEscapePointHour();
        if (fqcEscapePointList == null || fqcEscapePointList.size() == 0) {
            return;
        }
        for (Rate3 fqcEscape : fqcEscapePointList) {

            //判断该FQC是否进行报警
            if (fqcEscape.getDou1() >= dfYieldWarn.getAlarmValue()) {
                QueryWrapper<DfAuditDetail> auditDetailWrapper = new QueryWrapper<>();
                auditDetailWrapper
                        .eq("affect_mac", fqcEscape.getStr3())
                        .eq("data_type", "IA")
                        .eq("question_type", "漏检报警")
                        .orderByDesc("occurrence_time")
                        .last("limit 1");
                //获取当前FQC的最新稽核记录
                DfAuditDetail dfAuditDetail = dfAuditDetailMapper.selectOne(auditDetailWrapper);
                //当前存在未处理的稽核记录
                if (dfAuditDetail != null && dfAuditDetail.getEndTime() == null) {
                    return;
                }

                QueryWrapper<DfAoiUndetected> projectWrapper = new QueryWrapper<>();
                projectWrapper.eq("dau.fqc_user", fqcEscape.getStr1());
                List<String> projectList = dfAoiPieceMapper.getProjectByFqc(projectWrapper);

                //获取当前机台的项目
                StringBuilder projectName = new StringBuilder();
                int projectCount = 0;
                for (String project : projectList) {
                    if (projectCount > 0) {
                        projectName.append(",");
                    }
                    projectName.append(project);
                    projectCount++;
                }

                QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
                sqw.eq("type", "IA");
                sqw.eq("problem_level", "1");
                if (TimeUtil.getBimonthly() == 0) {
                    sqw.like("bimonthly", "双月");
                } else {
                    sqw.like("bimonthly", "单月");
                }
                sqw.like("process_name", "IA漏检");
                List<DfLiableMan> lm = dfLiableManMapper.selectList(sqw);
                if (lm == null || lm.size() == 0) {
                    return;
                }
                StringBuilder manCode = new StringBuilder();
                StringBuilder manName = new StringBuilder();
                int manCount = 0;
                for (DfLiableMan l : lm) {
                    if (manCount > 0) {
                        manCode.append(",");
                        manName.append(",");
                    }
                    manCode.append(l.getLiableManCode());
                    manName.append(l.getLiableManName());
                    manCount++;
                }
                DfAuditDetail aud = new DfAuditDetail();
                aud.setLine(fqcEscape.getStr2());
//                aud.setParentId(dfAoiSlThick.getId());
                aud.setDataType("IA");
                aud.setDepartment("IA");
                aud.setAffectMac(fqcEscape.getStr3());
                aud.setAffectNum(1.0);
                aud.setControlStandard("IA漏检率大于等于" + dfYieldWarn.getAlarmValue() + "%,IA漏检率报警");
                aud.setImpactType("IA");
                aud.setIsFaca("0");

                //问题名称和现场实际调换
                aud.setQuestionName("漏检报警");
                aud.setProcess("IA");
                aud.setProjectName(projectName.toString());

                aud.setReportTime(Timestamp.valueOf(checkTime));
                aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
                aud.setIpqcNumber(checkTimeLong);

                aud.setScenePractical("IA_FQC员工" + fqcEscape.getStr1() + "_漏检报警，漏检率为" + fqcEscape.getDou1() + "%");
                aud.setQuestionType("漏检报警");
                aud.setDecisionLevel("Level1");
                aud.setHandlingSug("全检风险批");
                aud.setResponsible(manName.toString());
                aud.setResponsibleId(manCode.toString());
                dfAuditDetailMapper.insert(aud);

                DfFlowData fd = new DfFlowData();
                fd.setFlowLevel(1);
                fd.setDataType(aud.getDataType());
                fd.setFlowType(aud.getDataType());
                fd.setName("IA_FQC员工" + fqcEscape.getStr1() + "_" + aud.getQuestionName() + "_NG_" + checkTime);
                fd.setDataId(aud.getId());
                fd.setStatus("待确定");

//                fd.setCreateName(checkTime);
//                fd.setCreateUserId(aud.getCreateUserId());

                fd.setNowLevelUser(aud.getResponsibleId());
                fd.setNowLevelUserName(aud.getResponsible());
                fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
                fd.setFlowLevelName(aud.getDecisionLevel());

                QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                atQw.eq("type", "IA")
                        .last("limit 1");
                DfApprovalTime at = dfApprovalTimeMapper.selectOne(atQw);
                if (null != at) {
                    if (fd.getFlowLevelName().equals("Level1")) {
                        fd.setReadTimeMax(at.getReadTimeLevel1());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
                    } else if (fd.getFlowLevelName().equals("Level2")) {
                        fd.setReadTimeMax(at.getReadTimeLevel2());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
                    } else if (fd.getFlowLevelName().equals("Level3")) {
                        fd.setReadTimeMax(at.getReadTimeLevel3());
                        fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
                    }
                }
                //设置显示人
                fd.setShowApprover(fd.getNowLevelUserName());
                dfFlowDataMapper.insert(fd);
            }

        }
    }

    @Override
    public List<Map<String, String>> listOQCReport(QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper, String beforesql, String afterSql) {
        return dfAoiPieceMapper.listOQCReport(dfAoiPieceQueryWrapper, beforesql, afterSql);
    }

    @Override
    public List<Rate3> empLossChemkRankV2(QueryWrapper<DfAoiDefect> ew, String feature) {
        return dfAoiPieceMapper.empLossChemkRankV2(ew, feature);
    }

    @Override
    public List<Rate3> empTopFeaRateDesc(QueryWrapper<DfAoiDefect> ew, QueryWrapper<DfAoiDefect> ew2) {
        return dfAoiPieceMapper.empTopFeaRateDesc(ew, ew2);
    }

    @Override
    public List<Rate3> empLossChemkRankTop10DayNight(QueryWrapper<DfAoiDefect> ew, QueryWrapper<DfAoiDefect> ew2) {
        return dfAoiPieceMapper.empLossChemkRankTop10DayNight(ew, ew2);
    }

    @Override
    public void saveMqData(String content) {
        System.out.println("进入AOI");
//        System.out.println(content);

        AOIUpload datas = new Gson().fromJson(content, AOIUpload.class);
        if (datas.getDataType() == 103) {
            datas.getPieces().setReTime(datas.getPieces().getrE_time());
            datas.getPieces().setColor("蓝色");
            datas.getPieces().setProject("C27");
            datas.getPieces().setMachine("1#");
            datas.getPieces().setFactory("J10-1");
            datas.getPieces().setLineBody("Line-23");
            dfAoiPieceMapper.insert(datas.getPieces());

            HashMap<String, AOIClasses> cm = new HashMap<>();
            if (null != datas.getClasses() && datas.getClasses().size() > 0) {
                for (AOIClasses c : datas.getClasses()) {
                    cm.put(c.getClassid(), c);
                }
            }

            HashMap<String, AOIDefectImages> ci = new HashMap<>();
            if (null != datas.getDefectimages() && datas.getDefectimages().size() > 0) {
                for (AOIDefectImages c : datas.getDefectimages()) {
                    ci.put(c.getDefectid(), c);
                }
            }

            if (null != datas.getDefect() && datas.getDefect().size() > 0) {
                String year = TimeUtil.getYear();
                String month = TimeUtil.getMonth();
                String day = TimeUtil.getDay();

                for (DfAoiDefect d : datas.getDefect()) {
                    d.setReResult(d.getrE_result());
                    d.setReTime(d.getrE_time());
                    d.setCheckId(datas.getPieces().getId());
                    if (cm.containsKey(d.getClassid())) {
                        d.setClassName(cm.get(d.getClassid()).getClassname());
                    }

                    if (ci.containsKey(d.getDefectid())) {
//                        d.setBase64str(ci.get(d.getDefectid()).getBase64str());
                        String imgPath = year + "/" + month + "/" + day + "/" + CommunalUtils.getUUID() + ".png";
                        if (null != ci.get(d.getDefectid()).getBase64str()) {
                            if (base64StrToImage(ci.get(d.getDefectid()).getBase64str(), env.getProperty("AOIImgPath") + "/" + imgPath)) {
                                d.setImageUrl(imgPath);
                            }
                        }

                        d.setChannelkey(ci.get(d.getDefectid()).getChannelkey());
                        d.setImageid(ci.get(d.getDefectid()).getImageid());

                        for (DfAoiPosition p : InitializeService.aoiPosition) {
                            Double x1 = p.getX1();
                            Double x2 = p.getX2();
                            Double y1 = p.getY1();
                            Double y3 = p.getY3();
                            Double x = Double.parseDouble(d.getAOIxcenter());
                            Double y = Double.parseDouble(d.getAOIycenter());
                            if (x > x1 && x < x2 && y > y1 && y < y3) {
                                String bigArea = p.getBigArea();
                                String area = p.getArea();
                                d.setBigArea(bigArea);
                                d.setArea(area);
                                //设置前端显示位置
                                d.setShowX((x - p.getX1()) / p.getWidth());
                                d.setShowY((y - p.getY1()) / p.getHeight());
                                //处理有问题的点
                                if (d.getShowX() > 1 || d.getShowX() < 0) {
                                    d.setShowX(0.5);
                                }
                                if (d.getShowY() > 1 || d.getShowY() < 0) {
                                    d.setShowY(0.5);
                                }

                                break;
                            }
                        }
                    }


                }
                dfAoiDefectService.saveBatch(datas.getDefect());
            }

        }

    }
}
