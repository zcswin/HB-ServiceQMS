package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.mapper.DfAuditDetailMapper;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 稽核NG详细 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-22
 */
@Service
public class DfAuditDetailServiceImpl extends ServiceImpl<DfAuditDetailMapper, DfAuditDetail> implements DfAuditDetailService {

    @Autowired
    DfAuditDetailMapper DfAuditDetailMapper;

    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        /*ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();

        Map<Integer, Integer> dataResNum = new HashMap<>();
        Map<Integer, List<String>> dataResTime = new HashMap<>();

        for (Map<String, String> map : maps) {
            dataResNum.merge(Integer.valueOf(map.get("id")), 1, Integer::sum );
            if (dataResTime.containsKey(Integer.valueOf(map.get("id")))) {
                List<String> list = dataResTime.get(Integer.valueOf(map.get("id")));
                list.add(map.get("创建日期") + " " + map.get("创建时间"));
                dataResTime.put(Integer.valueOf(map.get("id")), list);
            } else {
                List<String> list = new ArrayList<>();
                list.add(map.get("创建日期") + " " + map.get("创建时间"));
                dataResTime.put(Integer.valueOf(map.get("id")), list);
            }
        }

        for (Map.Entry<Integer, Integer> entry : dataResNum.entrySet()) {
            System.out.println("id为：" + entry.getKey() + " 次数为：" + entry.getValue());
        }

        for (Map.Entry<Integer, List<String>> entry : dataResTime.entrySet()) {
            System.out.println("id为：" + entry.getKey() + " 时间为：" + entry.getValue());
        }

        for (Map<String, String> map : maps) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if ("null".equals(entry.getValue()) || "NULL".equals(entry.getValue())) {
                    entry.setValue(null);
                }
            }
        }

        List<DfAuditDetail> list = new ArrayList<>();
        for (Map<String, String> map : maps) {

            if (dataResNum.containsKey(Integer.valueOf(map.get("id"))) && dataResNum.get(Integer.valueOf(map.get("id"))) == 5) {
                DfAuditDetail data = new DfAuditDetail();
                data.setRequestId("null".equals(map.get("requestId责任人工号")) || "NULL".equals(map.get("requestId责任人工号")) ? null : Integer.valueOf(map.get("requestId责任人工号")));
                data.setLcbh("null".equals(map.get("lcbh流程编号")) || "NULL".equals(map.get("lcbh流程编号")) ? null : map.get("lcbh流程编号"));
                data.setProjectName("null".equals(map.get("ProjectName型号")) || "NULL".equals(map.get("ProjectName型号")) ? null : map.get("ProjectName型号"));
                data.setDepartmentLiable("null".equals(map.get("DepartmentLiable工序线体")) || "NULL".equals(map.get("DepartmentLiable工序线体")) ? null : map.get("DepartmentLiable工序线体"));
                data.setDepartment("null".equals(map.get("DepartmentLiable工序线体")) || "NULL".equals(map.get("DepartmentLiable工序线体")) ? null : map.get("DepartmentLiable工序线体"));
                data.setIpqcNumber("null".equals(map.get("IPQCRepNumIPQC发起编号")) || "NULL".equals(map.get("IPQCRepNumIPQC发起编号")) ? null : map.get("IPQCRepNumIPQC发起编号"));
                data.setQuestionName("null".equals(map.get("QuestionName问题点名称")) || "NULL".equals(map.get("QuestionName问题点名称")) ? null : map.get("QuestionName问题点名称"));
                data.setReportMan("null".equals(map.get("RepUser发起人")) || "NULL".equals(map.get("RepUser发起人")) ? null : map.get("RepUser发起人"));
                data.setReportTime("null".equals(map.get("RepTime发起时间")) || "NULL".equals(map.get("RepTime发起时间")) || null == map.get("RepTime发起时间") ? null : Timestamp.valueOf(map.get("RepTime发起时间")));
                data.setControlStandard("null".equals(map.get("ControlStandard管控标准")) || "NULL".equals(map.get("ControlStandard管控标准")) ? null : map.get("ControlStandard管控标准"));
                data.setScenePractical("null".equals(map.get("ActualScene现场实际")) || "NULL".equals(map.get("ActualScene现场实际")) ? null : map.get("ActualScene现场实际"));
                data.setAffectNum("null".equals(map.get("AffectNum")) || "NULL".equals(map.get("AffectNum")) || null == map.get("AffectNum") ? null : Double.valueOf(map.get("AffectNum")));
                data.setAffectMac("null".equals(map.get("AffectMachine")) || "NULL".equals(map.get("AffectMachine")) ? null : map.get("AffectMachine"));
                data.setDecisionLevel("null".equals(map.get("DetermineLevel")) || "NULL".equals(map.get("DetermineLevel")) ? null : map.get("DetermineLevel"));
                data.setOccurrenceTime("null".equals(map.get("OccurrenceTime")) || "NULL".equals(map.get("OccurrenceTime")) || null == map.get("OccurrenceTime") ? null : Timestamp.valueOf(map.get("OccurrenceTime")));
                data.setResponsible("null".equals(map.get("PersonLiable")) || "NULL".equals(map.get("PersonLiable")) ? null : map.get("PersonLiable"));
                data.setHandingOption("null".equals(map.get("HandlingOption")) || "NULL".equals(map.get("HandlingOption")) ? null : map.get("HandlingOption"));
                data.setHandlingSug("null".equals(map.get("HandlingOption")) || "NULL".equals(map.get("HandlingOption")) ? null : map.get("HandlingOption"));
                data.setRemark(map.get("Remarks"));
                data.setResponsibleId(map.get("PersonLiableCode"));
                data.setEventlevel1(map.get("Eventlevel1"));
                data.setEventlevel2(map.get("Eventlevel2"));
                data.setEventlevel3(map.get("Eventlevel3"));
                data.setEventlevel4(map.get("Eventlevel4"));
                data.setKpi(map.get("KPI"));
                data.setOaUuid(map.get("OA_UUID"));
                data.setMesUuid(map.get("MES_UUID"));
                data.setIsEnabled(map.get("IsEnabled"));
                data.setResponsible2(map.get("PersonLiable2"));
                data.setResponsibleId2(map.get("PersonLiableCode2"));
                data.setResponsible3(map.get("PersonLiable3"));
                data.setResponsibleId3(map.get("PersonLiableCode3"));
                data.setResponsible4(map.get("PersonLiable4"));
                data.setResponsibleId4(map.get("PersonLiableCode4"));
                data.setJdclsj2(map.get("jdclsj2"));
                data.setJdclsj3(map.get("jdclsj3"));
                data.setJdclsj4(map.get("jdclsj4"));
                data.setJdclsj5(map.get("jdclsj5"));
                data.setSqr(map.get("sqr"));
                data.setNewRequestname(map.get("new_requestname"));
                data.setBzfa(map.get("bzfa"));
                data.setBzca(map.get("bzca"));
                data.setIsFaca(map.get("IsFACA"));
                data.setFactory(map.get("FactoryID"));
                data.setLine(map.get("LineID"));
                data.setProject(map.get("ProjectID"));
                data.setWorkshop(map.get("WorkShopID"));
                data.setWorkstation(map.get("WorkStationID"));
                data.setProcess(map.get("ProcessID"));
                data.setQuestionType(map.get("ControlTypeName"));
                data.setImpactType(map.get("AffectType"));
                data.setIpqcUrl(map.get("IpqcUrl"));
                data.setRepUserCode(map.get("RepUserCode"));
                data.setBzfaz(map.get("bzfaz"));
                data.setBzcaz(map.get("bzcaz"));
                data.setCreateName(map.get("RepUser发起人"));
                data.setFa(map.get("FA"));
                data.setCa(map.get("CA"));
                data.setDataType("稽查");
                List<String> listTIme = dataResTime.get(Integer.valueOf(map.get("id")));
                data.setEndTime(Timestamp.valueOf(listTIme.get(4)));
                data.setCloseTime(Timestamp.valueOf(listTIme.get(4)));

                save(data);

                DfFlowData flowData = new DfFlowData();
                flowData.setName("IPQC_点检_" + map.get("lcbh流程编号"));
                flowData.setFlowType(map.get("AffectType"));
                flowData.setFlowLevel(1);
                flowData.setDataType("稽查");
                flowData.setDataId(data.getId());
                flowData.setCreateTime(Timestamp.valueOf(listTIme.get(0)));
                flowData.setCreateName(map.get("RepUser发起人"));
                flowData.setUpdateTime(Timestamp.valueOf(listTIme.get(4)));
                flowData.setStatus("已关闭");
                flowData.setFlowLevelName(map.get("DetermineLevel"));
                if (map.get("DetermineLevel").contains("1")) {
                    flowData.setLevel1ReadTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel1AffirmTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel1PushTime(Timestamp.valueOf(listTIme.get(2)));
                } else if (map.get("DetermineLevel").contains("2")) {
                    flowData.setLevel2ReadTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel2AffirmTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel2PushTime(Timestamp.valueOf(listTIme.get(2)));
                } else {
                    flowData.setLevel3ReadTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel3AffirmTime(Timestamp.valueOf(listTIme.get(1)));
                    flowData.setLevel3PushTime(Timestamp.valueOf(listTIme.get(2)));
                }
                System.out.println(flowData);

                dfFlowDataService.save(flowData);

                List<DfFlowOpinion> opinionList = new ArrayList<>();
                DfFlowOpinion opinion1 = new DfFlowOpinion();
                DfFlowOpinion opinion2 = new DfFlowOpinion();
                DfFlowOpinion opinion3 = new DfFlowOpinion();
                DfFlowOpinion opinion4 = new DfFlowOpinion();
                opinion1.setFlowDataId(flowData.getId());
                opinion2.setFlowDataId(flowData.getId());
                opinion3.setFlowDataId(flowData.getId());
                opinion4.setFlowDataId(flowData.getId());
                opinion1.setOpinion("确认收到问题");
                opinion2.setOpinion("已解决问题");
                opinion3.setOpinion("发起人确认");
                opinion4.setOpinion("已关闭");
                opinion1.setSender(map.get("PersonLiable"));
                opinion2.setSender(map.get("PersonLiable"));
                opinion3.setSender(map.get("RepUser发起人"));
                opinion4.setSender(map.get("RepUser发起人"));
                opinion1.setCreateTime(Timestamp.valueOf(listTIme.get(1)));
                opinion2.setCreateTime(Timestamp.valueOf(listTIme.get(2)));
                opinion3.setCreateTime(Timestamp.valueOf(listTIme.get(3)));
                opinion4.setCreateTime(Timestamp.valueOf(listTIme.get(4)));
                opinionList.add(opinion1);
                opinionList.add(opinion2);
                opinionList.add(opinion3);
                opinionList.add(opinion4);

                dfFlowOpinionService.saveBatch(opinionList);
                System.out.println(data);
                System.out.println(flowData);
                for (DfFlowOpinion opinion : opinionList) {
                    System.out.println(opinion);
                }
                dataResNum.remove(Integer.valueOf(map.get("id")));
            }


            //list.add(data);
        }
        //saveBatch(list);
        return list.size();*/
        return 0;
    }

    @Transactional
    public int importExcel2(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfAuditDetail> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            DfAuditDetail data = new DfAuditDetail();
            data.setRequestId(null == map.get("RepUserCode发起人工号") ? null : Integer.valueOf(map.get("RepUserCode发起人工号")));
            data.setLcbh(map.get("lcbh流程编号"));
            data.setProjectName(map.get("ProjectName（型号）"));
            data.setDepartmentLiable(map.get("DepartmentLiable（工序线体）"));
            data.setIpqcNumber(map.get("IPQCRepNum（IPQC发起编号）"));
            data.setQuestionName(map.get("QuestionName问题点名称"));
            data.setReportMan(map.get("RepUser发起人"));
//            data.setReportTime(null == map.get("RepTime发起时间") ? null : LocalDateTime.parse(map.get("RepTime发起时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.setControlStandard(map.get("ControlStandard管控标准"));
            data.setScenePractical(map.get("ActualScene现场实际"));
            data.setAffectNum(null == map.get("AffectNum影响数量") ? null : Double.valueOf(map.get("AffectNum影响数量")));
            data.setAffectMac(map.get("AffectMachine影响机台"));
            data.setDecisionLevel(map.get("DetermineLevel事件等级"));
//            data.setOccurrenceTime(null == map.get("OccurrenceTime发起时间") ? null : LocalDateTime.parse(map.get("OccurrenceTime发起时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.setResponsible(map.get("PersonLiable责任人1"));
            data.setHandingOption(map.get("HandlingOption处理意见"));
            data.setRemark(map.get("Remarks"));
            data.setResponsibleId(map.get("PersonLiableCode责任人1工号"));
            data.setEventlevel1(map.get("Eventlevel1"));
            data.setEventlevel2(map.get("Eventlevel2"));
            data.setEventlevel3(map.get("Eventlevel3"));
            data.setEventlevel4(map.get("Eventlevel4"));
            data.setKpi(map.get("KPI"));
            data.setOaUuid(map.get("OA_UUID"));
            data.setMesUuid(map.get("MES_UUID"));
            data.setIsEnabled(map.get("IsEnabled已启用"));
            data.setResponsible2(map.get("PersonLiable2责任人2"));
            data.setResponsibleId2(map.get("PersonLiableCode2责任人2工号"));
            data.setResponsible3(map.get("PersonLiable3责任人3"));
            data.setResponsibleId3(map.get("PersonLiableCode3责任人3工号"));
            data.setResponsible4(map.get("PersonLiable4责任人4"));
            data.setResponsibleId4(map.get("PersonLiableCode4责任人4工号"));
            data.setJdclsj2(map.get("jdclsj2"));
            data.setJdclsj3(map.get("jdclsj3"));
            data.setJdclsj4(map.get("jdclsj4"));
            data.setJdclsj5(map.get("jdclsj5"));
            data.setSqr(map.get("sqr"));
            data.setNewRequestname(map.get("new_requestname"));
            data.setBzfa(map.get("bzfa"));
            data.setBzca(map.get("bzca"));
            data.setIsFaca(map.get("IsFACA"));
            data.setFactory(map.get("FactoryID工厂"));
            data.setLine(map.get("LineID线体"));
            data.setProject(map.get("ProjectID项目"));
            data.setWorkshop(map.get("WorkShopID工段"));
            data.setWorkstation(map.get("WorkStationID工站"));
            data.setProcess(map.get("ProcessID工序"));
            data.setQuestionType(map.get("ControlTypeName管控类别"));
            data.setImpactType(map.get("AffectType影响类别"));
            data.setIpqcUrl(map.get("IpqcUrl"));
            data.setRepUserCode(map.get("RepUserCode"));
            data.setBzfaz(map.get("bzfaz"));
            data.setBzcaz(map.get("bzcaz"));
            data.setFa(map.get("FA"));
            data.setCa(map.get("CA"));
            data.setOaNum(map.get("OANumOA编号"));
//            data.setEndTime("null".equals(map.get("EndTime结束时间")) || "NULL".equals(map.get("EndTime结束时间")) || null==map.get("EndTime结束时间") ? null : LocalDateTime.parse(map.get("EndTime结束时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) );
            data.setControlStandardId(map.get("ControlStandardID管控标准ID"));
//            data.setCreateTime(null == map.get("CreateTime创建时间") ? null : LocalDateTime.parse(map.get("CreateTime创建时间"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.setCreateUserId(map.get("CreateUser创建人工号"));

            list.add(data);
        }
        //saveBatch(list);
        return list.size();
    }

    @Override
    public List<DfAuditDetail> listByBigScreen(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.listByBigScreen(wrapper);
    }

    @Override
    public DfAuditDetail getEndNum(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getEndNum(wrapper);
    }

    @Override
    public List<DfAuditDetail> getProjectClassNum(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getProjectClassNum(wrapper);
    }

    @Override
    public List<DfAuditDetail> getQuestionNum(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getQuestionNum(wrapper);
    }

    @Override
    public List<DfAuditDetail> getQuestionNumJc(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getQuestionNumJc(wrapper);
    }

    @Override
    public DfAuditDetail getTimeout(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getTimeout(wrapper);
    }

    @Override
    public int getNgCountByMacCode(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getNgCountByMacCode(wrapper);
    }

    @Override
    public List<DfAuditDetail> listByCheckOverTime(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.listByCheckOverTime(wrapper);
    }

    @Override
    public DfQmsIpqcWaigTotal getAppearSnCodeById(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getAppearSnCodeById(wrapper);
    }

    @Override
    public DfSizeDetail getSizeSnCodeById(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getSizeSnCodeById(wrapper);
    }

    @Override
    public List<DfAuditDetail> getEndNumByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getEndNumByDay(wrapper);
    }

    @Override
    public List<DfAuditDetail> getTimeoutByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getTimeoutByDay(wrapper);
    }

    @Override
    public List<Map> exportAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.exportAuditNgDetail(wrapper);
    }

    @Override
    public  List<DfAuditDetail> exportExcleAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.exportExcleAuditNgDetail(wrapper);
    }


    @Override
    public DfAuditDetail getTimeout2(Wrapper<DfAuditDetail> wrapper) {
        return DfAuditDetailMapper.getTimeout2(wrapper);
    }


    @Override
    public List<DfAuditDetail> getTimeoutData(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.getTimeoutData(qw);
    }

    @Override
    public List<DfAuditDetail> getProcessTimeoutData(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.getProcessTimeoutData(qw);
    }


    @Override
    public List<DfAuditDetail> getAuditLevelSummary(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.getAuditLevelSummary(qw);
    }


    @Override
    public List<DfAuditDetail> getAuditSummaryData(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.getAuditSummaryData(qw);
    }



    @Override
    public List<DfAuditDetail> listByProcess(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.listByProcess(qw);
    }

    @Override
    public List<DfAuditDetail> listByProcessHaveQuestionType(Wrapper<DfAuditDetail> qw) {
        return DfAuditDetailMapper.listByProcessHaveQuestionType(qw);
    }
}
