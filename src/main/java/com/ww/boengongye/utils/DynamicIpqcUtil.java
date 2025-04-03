package com.ww.boengongye.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfLiableManService;
import com.ww.boengongye.service.DfSizeContStandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DynamicIpqcUtil {


//    @Autowired
//    static com.ww.boengongye.service. DfLiableManService dfLiableManService;
//
//    @Autowired
//    static com.ww.boengongye.service. DfAuditDetailService dfAuditDetailService;
//
//    @Autowired
//    static com.ww.boengongye.service. DfFlowDataService dfFlowDataService;
//
//
//    @Autowired
//    static com.ww.boengongye.service.DfApprovalTimeService dfApprovalTimeService;


    /**
     * @param setTime   通知抽检时间
     * @param type      外观/尺寸
     * @param ruleName  当前抽检规则名称
     * @param sendType  放宽还是收严
     * @param process   工序
     * @param macCode   机台号
     * @param parentId  对应的测试数据id
     * @param project   项目
     * @param checkType 抽检类型
     * @param mes       问题明细
     * @return
     */
    public static boolean sendMes(double setTime, String type, String ruleName, String sendType, String process, String macCode, int parentId, String project, String checkType, String mes) {
        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
        if (type.equals("尺寸")) {
            sqw.eq("type", "sizeDyIPQC");
        } else {
            sqw.eq("type", "checkDyIPQC");
        }

        sqw.eq("problem_level", "2");
//                sqw.last("limit 1");
//        if(TimeUtil.getBimonthly()==0){
//            sqw.like("bimonthly","双月");
//        }else{
//            sqw.like("bimonthly","单月");
//        }
        sqw.like("process_name", process);
        List<DfLiableMan> lm = InitializeService.dfLiableManService.list(sqw);
        if (lm.size() > 0) {
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
            aud.setMacCode(macCode);
            aud.setLine(InitializeService.env.getProperty("LineBody", "Line-23"));
            aud.setParentId(parentId);
//            aud.setDataType(type);
            aud.setDataType("动态IPQC");
            aud.setDepartment(process);
            aud.setAffectMac("1");
            aud.setAffectNum(1.0);
            aud.setControlStandard(ruleName);
            aud.setImpactType(type);
            aud.setIsFaca("0");
            //问题名称和现场实际调换
            aud.setQuestionName(ruleName);
            aud.setScenePractical(macCode + "_" + ruleName + mes);
            aud.setProcess(process);
            aud.setProjectName(project);
            aud.setProject(project);
//            aud.setIsFaca(sendType.equals("收严") ? "0" : "1");
            aud.setIsFaca("1");
//            QueryWrapper<DfLiableMan> sqw2=new QueryWrapper<>();
//            if(type.equals("尺寸")){
//                sqw2.eq("type","sizeInitiator");
//            }else{
//                sqw2.eq("type","check");
//            }
//
//            sqw2.eq("problem_level","2");
//            sqw2.like("process_name",process);
//            sqw2.last("limit 1");
//            if(TimeUtil.getDayShift()==1){
//                sqw2.like("bimonthly","双月");
//            }else{
//                sqw2.like("bimonthly","单月");
//            }
//            DfLiableMan rpm =dfLiableManService.getOne(sqw2);
//            if(null!=rpm){
            aud.setReportMan("系统");
            aud.setCreateName("系统");
//                aud.setCreateUserId(rpm.getLiableManCode());
//            }

            aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            aud.setIpqcNumber(TimeUtil.getNowTimeLong());

            aud.setQuestionType(checkType);


            aud.setDecisionLevel("Level2");
            aud.setHandlingSug("抽检频率" + sendType + setTime + "小时");
            aud.setResponsible2(manName.toString());
            aud.setResponsibleId2(manCode.toString());
//                    aud.setCreateName(lm.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
            InitializeService.dfAuditDetailService.save(aud);

            DfFlowData fd = new DfFlowData();
            fd.setFlowLevel(2);
            fd.setDataType(aud.getDataType());
            fd.setFlowType(aud.getDataType());
            if (ruleName.equals("⻛险批次全检")) {
                fd.setName("动态IPQC_" + macCode + "_触发" + ruleName + "_" + "立刻全检该风险批" + TimeUtil.getNowTimeByNormal());

            } else {
                fd.setName("动态IPQC_" + macCode + "_触发" + ruleName + "_" + sendType + "至" + setTime + "小时抽检一次" + TimeUtil.getNowTimeByNormal());

            }
            fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
            fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
            fd.setCreateName(aud.getCreateName());
            fd.setCreateUserId(aud.getCreateUserId());

            fd.setNowLevelUser(aud.getResponsibleId2());
            fd.setNowLevelUserName(aud.getResponsible2());
            fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

            fd.setFlowLevelName(aud.getDecisionLevel());
            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
            atQw.eq("type", type);
            atQw.last("limit 1");
            DfApprovalTime at = InitializeService.dfApprovalTimeService.getOne(atQw);
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
            InitializeService.dfFlowDataService.save(fd);
            //关联的用户数组
            DfFlowDataUser du = new DfFlowDataUser();
            du.setFlowDataId(fd.getId());
            du.setUserAccount(manCode.toString());
            InitializeService.dfFlowDataUserService.save(du);
        }


        return true;
    }

    public static boolean sendMacCheckMes(String type, String ruleName, String process, String macCode, String checkType, String mes) {
        QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
        if (type.equals("尺寸")) {
            sqw.eq("type", "sizeDyIPQC");
        } else {
            sqw.eq("type", "checkDyIPQC");
        }

        sqw.eq("problem_level", "2");
//                sqw.last("limit 1");
//        if(TimeUtil.getBimonthly()==0){
//            sqw.like("bimonthly","双月");
//        }else{
//            sqw.like("bimonthly","单月");
//        }
        sqw.like("process_name", process);
        List<DfLiableMan> lm = InitializeService.dfLiableManService.list(sqw);
        if (lm.size() > 0) {
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
            aud.setMacCode(macCode);
            aud.setLine(InitializeService.env.getProperty("LineBody", "Line-23"));
//            aud.setParentId(parentId);
//            aud.setDataType(type);
            aud.setDataType("动态IPQC");
            aud.setDepartment(process);
            aud.setAffectMac("1");
            aud.setAffectNum(1.0);
            aud.setControlStandard(ruleName);
            aud.setImpactType(type);
            aud.setIsFaca("0");
            //问题名称和现场实际调换
            aud.setQuestionName(ruleName);
            aud.setScenePractical(process + "_" + macCode + "_" + ruleName + mes);
            aud.setProcess(process);
            aud.setProjectName(InitializeService.env.getProperty("Project", "C98B"));
            aud.setProject(InitializeService.env.getProperty("Project", "C98B"));
            aud.setIsFaca("1");//不同天faca
//            QueryWrapper<DfLiableMan> sqw2=new QueryWrapper<>();
//            if(type.equals("尺寸")){
//                sqw2.eq("type","sizeInitiator");
//            }else{
//                sqw2.eq("type","check");
//            }
//
//            sqw2.eq("problem_level","2");
//            sqw2.like("process_name",process);
//            sqw2.last("limit 1");
//            if(TimeUtil.getDayShift()==1){
//                sqw2.like("bimonthly","双月");
//            }else{
//                sqw2.like("bimonthly","单月");
//            }
//            DfLiableMan rpm =dfLiableManService.getOne(sqw2);
//            if(null!=rpm){
            aud.setReportMan("系统");
            aud.setCreateName("系统");
//                aud.setCreateUserId(rpm.getLiableManCode());
//            }

            aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            aud.setIpqcNumber(TimeUtil.getNowTimeLong());

            aud.setQuestionType(checkType);


            aud.setDecisionLevel("Level2");
            aud.setHandlingSug("请及时抽检!");
            aud.setResponsible2(manName.toString());
            aud.setResponsibleId2(manCode.toString());
//                    aud.setCreateName(lm.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
            InitializeService.dfAuditDetailService.save(aud);

            DfFlowData fd = new DfFlowData();
            fd.setFlowLevel(2);
            fd.setDataType(aud.getDataType());
            fd.setFlowType(aud.getDataType());

            fd.setName("动态IPQC_" + macCode + "_" + "请及时抽检!" + TimeUtil.getNowTimeByNormal());


            fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
            fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
            fd.setCreateName(aud.getCreateName());
            fd.setCreateUserId(aud.getCreateUserId());

            fd.setNowLevelUser(aud.getResponsibleId2());
            fd.setNowLevelUserName(aud.getResponsible2());
            fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

            fd.setFlowLevelName(aud.getDecisionLevel());
            QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
            atQw.eq("type", type);
            atQw.last("limit 1");
            DfApprovalTime at = InitializeService.dfApprovalTimeService.getOne(atQw);
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
            InitializeService.dfFlowDataService.save(fd);

            //关联的用户数组
            DfFlowDataUser du = new DfFlowDataUser();
            du.setFlowDataId(fd.getId());
            du.setUserAccount(manCode.toString());
            InitializeService.dfFlowDataUserService.save(du);

        }


        return true;
    }

    public static Double getCpk(SizeQueueData datas, RedisTemplate redisTemplate, String checkType, int id, DynamicIpqcMac dynamicIpqcMac, Gson gson) {
        HashMap<String, DfSizeContStand> itemNamesMap = new HashMap<>();
        HashMap<String, DfSizeCheckItemInfos> itemNamesDataMap = new HashMap<>();
        QueryWrapper<DfSizeContStand> kpQw = new QueryWrapper<>();
        kpQw.eq("process", datas.getProcessNO());
        kpQw.eq("key_point", 1);
        kpQw.eq("project", datas.getItemName().split("-")[0]);
        kpQw.eq("color", datas.getItemName());

        List<DfSizeContStand> keyPoints = InitializeService.dfSizeContStandService.list(kpQw);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        double checkResult = 0.0;//总体结果 0 ok  1 ng
        if (null != datas.getCheckItemInfos()) {
            if (datas.getCheckItemInfos().size() > 0) {
                for (DfSizeCheckItemInfos d : datas.getCheckItemInfos()) {
                    itemNamesDataMap.put(d.getItemName(), d);
                }
            }
        }

        List<DynamicIpqcMacData> cacheDatas = new ArrayList<>();
        if (keyPoints.size() > 0) {
            double cpkResult = 1;
            for (DfSizeContStand d : keyPoints) {
                System.out.println(d.getTestItem());
//                if (redisTemplate.hasKey("IpqcSizeData:" + datas.getMachineCode()+":"+d.getTestItem())) {
                DynamicIpqcMacData details = new DynamicIpqcMacData();
                Object v = valueOperations.get("IpqcSizeData:" + datas.getMachineCode() + ":" + d.getTestItem());
                if (null != v) {
                    details = new Gson().fromJson(v.toString(), DynamicIpqcMacData.class);
                } else {
                    details.setAvgTotal(0.0);
                    details.setAvgCount(0);
                    details.setSigma(0.0);
                    details.setContinuousCount(0);
                    details.setTotalCount(0);
                    details.setContinuousNgCount(0);
                    details.setLastSigma(0.0);
                    details.setLastValue(0.0);
                    details.setLastAvg(0.0);
                    details.setZugammenCount(0);
                    details.setFourPointOverOne(0);
                    details.setTwoPointOverTwo(0);
                    details.setMachineCode(datas.getMachineCode());
                    details.setItemName(d.getTestItem());

                }

//                    itemNamesMap.put(d.getTestItem(), d);
                if (checkResult == 0) {
                    if (itemNamesDataMap.containsKey(d.getTestItem())) {
                        double checkValue = itemNamesDataMap.get(d.getTestItem()).getCheckValue();
                        //计算样本均值
                        double avgTotal = (details.getAvgTotal() + checkValue);
                        int avgCount = (details.getAvgCount() + 1);
                        double avg = avgTotal / avgCount;
                        //计算总体标准差
                        double sigma = details.getSigma() + Math.pow(checkValue - avg, 2);
                        double std = Math.sqrt((sigma / avgCount));
                        details.setTotalCount(details.getTotalCount() + 1);
                        details.setContinuousCount(details.getContinuousCount() + 1);
                        //规则1
                        if (checkValue > (avg + std * 3) || checkValue < (avg - std * 3)) {//判断是否超限
                            if (null != details.getContinuousNgCount()) {
                                details.setContinuousNgCount(details.getContinuousNgCount() + 1);
                            } else {
                                details.setContinuousNgCount(1);
                            }
                            //判定是否NG
                            if (checkIsOver(details.getContinuousNgCount(), details.getContinuousCount()) > 0) {
                                checkResult = 1;
                            }

                        }

                        //规则2
                        int lastScope = getScope(details.getLastSigma(), details.getLastAvg(), details.getLastValue());
                        int nowScope = getScope(sigma, avg, checkValue);
                        if ((lastScope > 0 && nowScope > 0) || (lastScope < 0 && nowScope < 0)) {
                            if (null != details.getZugammenCount()) {
                                details.setZugammenCount(details.getZugammenCount() + 1);
                            } else {
                                details.setZugammenCount(1);
                            }
                            if (details.getZugammenCount() >= 9) { //判定是否NG
                                checkResult = 1;
                            }
                        } else {
                            details.setZugammenCount(0);
                        }

                        //规则5
                        if ((lastScope == 2 && nowScope == 2) || (lastScope == -2 && nowScope == -2)) {
                            if (details.getContinuousCount() >= 3) { //判定是否NG
                                checkResult = 1;
                            }
                        }
                        //规则6
                        if ((lastScope > 1 && nowScope > 1) || (lastScope < -1 && nowScope < -1)) {
                            if (null != details.getFourPointOverOne()) {
                                details.setFourPointOverOne(details.getFourPointOverOne() + 1);
                            } else {
                                details.setFourPointOverOne(1);
                            }

                        } else {
                            details.setFourPointOverOne(0);
                        }

                        if (details.getFourPointOverOne() >= 4 && details.getContinuousCount() >= 5) { //判定是否NG
                            checkResult = 1;
                        }


                        details.setAvgCount(avgCount);
                        details.setAvgTotal(avgTotal);
                        details.setSigma(sigma);
                        //缓存当次数据
                        details.setLastAvg(avg);
                        details.setLastValue(checkValue);
                        details.setLastSigma(std);
                        if (null != details.getValues()) {
                            List<Double> valueList = details.getValues();
                            valueList.add(checkValue);
                            details.setValues(valueList);
                        } else {
                            List<Double> valueList = new ArrayList<>();
                            valueList.add(checkValue);
                            details.setValues(valueList);
                        }
                        //计算cpk
                        if (avgCount >= 32) { //达到32片才计算
                            double cpkStd = Math.sqrt((sigma / (avgCount - 1)));

                            double cpkUsl = (d.getIsolaUpperLimit() - avg) / Math.pow(3, cpkStd);
                            double cpkLsl = (avg - d.getIsolaLowerLimit()) / Math.pow(3, cpkStd);
                            double cpk = Math.min(cpkUsl, cpkLsl);
                            if (cpk > 1) {//ng
                                checkResult = 1;
                            } else {
                                cpkResult = 0;
                            }

                        }


                    }

                }

                cacheDatas.add(details);
//                }
            }

//                if(cpkResult<1 && checkResult<1&&details.getContinuousCount()>=32){
//                    int time=4;
//                    if(dynamicIpqcMac.getFrequency()>=4){//四小时后变8小时
//                        time=8;
//                    }
//                    DynamicIpqcUtil.sendMes(time,"尺寸","制程能⼒达标且稳定","放宽",datas.getProcessNO(),datas.getMachineCode(),id,datas.getItemName().split("-")[0],checkType);
//
//                }
            for (DynamicIpqcMacData d : cacheDatas) {
                if (checkResult > 0 || cpkResult < 1) {//ng 时清空数据
                    d.setAvgCount(0);
                    d.setAvgTotal(0.0);
                    d.setSigma(0.0);
                    //缓存当次数据
                    d.setLastAvg(0.0);
                    d.setLastValue(0.0);
                    d.setLastSigma(0.0);

                    d.getValues().clear();
                }

                redisTemplate.opsForValue().set("IpqcSizeData:" + d.getMachineCode() + ":" + d.getItemName(), gson.toJson(d));
            }

        }


        return checkResult;
    }

    /**
     * 判断触发那条规则
     *
     * @param ngCount    ng数
     * @param totalCount 总数
     * @return
     */
    public static int checkIsOver(int ngCount, int totalCount) {
        if (totalCount <= 20 && ngCount > 0) {
            return 1;
        } else if (totalCount <= 130 && ngCount > 1) {
            return 2;
        } else if (totalCount <= 300 && ngCount > 2) {
            return 3;
        } else if (totalCount >= 300 && ngCount > 3) {
            return 4;
        }

        return 0;
    }

    /**
     * 判定范围
     *
     * @param sigma      平均值方差
     * @param avg        平均值
     * @param checkValue 测试值
     * @return
     */
    public static int getScope(double sigma, double avg, double checkValue) {

        if (checkValue == avg) {
            return 0;
        }
        if (checkValue > avg && checkValue <= (avg + sigma)) {
            return 1;
        } else if (checkValue > (avg + sigma) && checkValue <= (avg + sigma * 2)) {
            return 2;
        } else if (checkValue > (avg + sigma * 2) && checkValue <= (avg + sigma * 3)) {
            return 3;
        }

        if (checkValue < avg && checkValue >= (avg - sigma)) {
            return -1;
        } else if (checkValue < (avg - sigma) && checkValue >= (avg - sigma * 2)) {
            return -2;
        } else if (checkValue < (avg - sigma * 2) && checkValue >= (avg - sigma * 3)) {
            return -3;
        }

        return 10;
    }


    public static boolean clearSizeData(RedisTemplate redisTemplate, String machineCode, Gson gson) {
        Set<String> datas = redisTemplate.keys("IpqcSizeData:" + machineCode + "*");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        for (String key : datas) {
            Object v = valueOperations.get(key);
            if (null != v) {
                DynamicIpqcMacData d = new Gson().fromJson(v.toString(), DynamicIpqcMacData.class);
                d.setAvgCount(0);
                d.setAvgTotal(0.0);
                d.setSigma(0.0);
                //缓存当次数据
                d.setLastAvg(0.0);
                d.setLastValue(0.0);
                d.setLastSigma(0.0);

                d.setValues(new ArrayList<>());

                redisTemplate.opsForValue().set(key, gson.toJson(d));
            }
        }


        return true;
    }

}
