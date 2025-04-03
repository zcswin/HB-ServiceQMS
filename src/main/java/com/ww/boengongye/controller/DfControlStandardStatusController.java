package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfFlowAuditorService;
import com.ww.boengongye.service.impl.ExportDataService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 管控标准状态 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-12
 */
@Controller
@RequestMapping("/dfControlStandardStatus")
@ResponseBody
@CrossOrigin
@Api(tags = "管控标准状态")
public class DfControlStandardStatusController {
    @Autowired
    com.ww.boengongye.service.DfControlStandardStatusService DfControlStandardStatusService;

    @Autowired
    com.ww.boengongye.service.impl.ExportDataService ExportDataService;

    @Autowired
    com.ww.boengongye.service.DfAuditDetailService DfAuditDetailService;

    @Autowired
    com.ww.boengongye.service.DfFlowBlockService DfFlowBlockService;
    @Autowired
    com.ww.boengongye.service.DfFlowNextLevelService DfFlowNextLevelService;
    @Autowired
    com.ww.boengongye.service.DfFlowRelationUserService DfFlowRelationUserService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataUserService DfFlowDataUserService;
    @Autowired
    com.ww.boengongye.service.DfFlowDataService DfFlowDataService;

    @Autowired
    com.ww.boengongye.service.DfMyJobService DfMyJobService;
    @Autowired
    com.ww.boengongye.service.UserService userService;

    @Autowired
    com.ww.boengongye.service.DfApprovalTimeService dfApprovalTimeService;
    @Autowired
    private Environment env;

    @Autowired
    com.ww.boengongye.service.DfFlowAuditorService dfFlowAuditorService;
    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardStatusController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfControlStandardStatusService.list());
    }

    @RequestMapping(value = "/getUUID")
    public Object getUUID() {

        return new Result(0, "查询成功", CommunalUtils.getUUID());
    }


    @RequestMapping(value = "/getDxjhfhd")
    public Object getDxjhfhd(String project,String startTime, String endTime) {
        List<EchartJh> datas = new ArrayList<>();
        QueryWrapper<DfControlStandardStatus> qw1 = new QueryWrapper<>();
        QueryWrapper<DfControlStandardStatus> qw2 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw1.between("s.create_time", startTime, endTime + " 23:59:59");
            qw2.between("s.create_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw1.ge("s.create_time", startTime);
            qw2.ge("s.create_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw1.le("s.create_time", endTime + " 23:59:59");
            qw2.le("s.create_time", endTime + " 23:59:59");
        }
        qw1.eq(StringUtils.isNotEmpty(project),"s.project_id",project);
        qw2.eq(StringUtils.isNotEmpty(project),"s.project_id",project);
        qw1.groupBy("data_type");
        List<DfControlStandardStatus> all = DfControlStandardStatusService.listAllTypeCount(qw1);

        qw2.eq("s.data_status", "NG");
        qw2.groupBy("data_type");
        List<DfControlStandardStatus> ng = DfControlStandardStatusService.listAllTypeCount(qw2);
        Map<String, Integer> allMap = new HashMap<>();
        Map<String, Integer> ngMap = new HashMap<>();
        if (all.size() > 0) {
            for (DfControlStandardStatus a : all) {
                allMap.put(a.getCreateName(), a.getId());
            }
        }

        if (ng.size() > 0) {
            for (DfControlStandardStatus a : ng) {
                ngMap.put(a.getCreateName(), a.getId());
            }
        }

        for (Map.Entry<String, Integer> entry : allMap.entrySet()) {
            EchartJh d = new EchartJh();
            d.setProduct(entry.getKey().toUpperCase());

            if (ngMap.containsKey(entry.getKey().toString())) {
            d.setNG(Double.parseDouble(ngMap.get(entry.getKey().toString()) + "") / Double.parseDouble(entry.getValue() + "") * 100);
                d.setOK(100 - d.getNG());

                DecimalFormat format = new DecimalFormat("#.00");
                String str = format.format(d.getNG());
                d.setNG(Double.parseDouble(str));

                String str2 = format.format(d.getOK());
                d.setOK(Double.parseDouble(str2));
            } else {
                d.setOK(100.0);
                d.setNG(0.0);
            }

            datas.add(d);
        }/*
    if(!allMap.containsKey("sop")){
        EchartJh d=new EchartJh();
        d.setProduct("SOP");
        d.setOk(100.0);
        d.setNg(0.0);
        datas.add(d);
    }
        if(!allMap.containsKey("耗材")){
            EchartJh d=new EchartJh();
            d.setProduct("耗材");
            d.setOk(100.0);
            d.setNg(0.0);
            datas.add(d);
        }
        if(!allMap.containsKey("其它")){
            EchartJh d=new EchartJh();
            d.setProduct("其它");
            d.setOk(100.0);
            d.setNg(0.0);
            datas.add(d);
        }*/
        return new Result(0, "查询成功", datas);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfControlStandardStatus datas) {
//        try {
//        Field[] f= DfControlStandardStatus.class.getDeclaredFields();
//        //给TAnnals对象赋值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            try{
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod= DfControlStandardStatus.class.getMethod("set"+methodName,String.class);
//                if(!methodName.equals("IntroductionLand")){
//                    //执行该set方法
//                    setMethod.invoke(datas, XXSFilter.checkStr(XXSFilter.getFieldValueByFieldName(attributeName,datas)));
//                }
//
//            }catch (NoSuchMethodException e) {
//                logger.error("接口异常", e);
//            } catch (IllegalAccessException e) {
//                logger.error("接口异常", e);
//            } catch (InvocationTargetException e) {
//                logger.error("接口异常", e);
//            }
//        }
        if (null != datas.getId()) {
            datas.setProcess(datas.getDfAuditDetail().getProcess());
            if (DfControlStandardStatusService.updateById(datas)) {
                if (datas.getDfAuditDetail() != null) {
                    datas.getDfAuditDetail().setParentId(datas.getId());
                    DfAuditDetailService.save(datas.getDfAuditDetail());


                }
                if (null != datas.getJobId()) {
                    DfMyJob job = new DfMyJob();
                    job.setId(datas.getJobId());
                    job.setResult("已处理");
                    DfMyJobService.updateById(job);
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {
            datas.setProcess(datas.getDfAuditDetail().getProcess());
//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfControlStandardStatusService.save(datas)) {
                if (datas.getDfAuditDetail() != null) {
                    datas.getDfAuditDetail().setParentId(datas.getId());
                    datas.getDfAuditDetail().setCreateName(datas.getCreateName());
                    datas.getDfAuditDetail().setCreateUserId(datas.getCreateUserId());
                    datas.getDfAuditDetail().setDataType("稽查");
                    DfAuditDetailService.save(datas.getDfAuditDetail());
//                    QueryWrapper<DfFlowBlock> qw = new QueryWrapper<>();
//                    qw.eq("flow_type", "点检");
//                    qw.eq("sort", 1);
//                    qw.last("limit 0,1");
//                    DfFlowBlock fb = DfFlowBlockService.getOne(qw);
                    //关联的用户数组
                    List<DfFlowDataUser> dataUser = new ArrayList<>();
                    DfFlowData fd = new DfFlowData();
                    fd.setFlowLevel(1);
                    fd.setDataType("稽查");
                    fd.setFlowType("稽查");
                    fd.setName("IPQC_点检_" + datas.getDfAuditDetail().getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
                    fd.setDataId(datas.getDfAuditDetail().getId());
//                    fd.setFlowLevelName(fb.getName());
                    fd.setStatus("待确认");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
                    fd.setCreateName(datas.getCreateName());
                    fd.setCreateUserId(datas.getCreateUserId());

                    User u=userService.getById(datas.getCreateUserId());
                    if(null!=u&&null!=u.getName()){
                        //绑定创建人
                        DfFlowDataUser du = new DfFlowDataUser();
//                  du.setFlowDataId(fd.getId());
                        du.setUserAccount(u.getName());
                        dataUser.add(du);
                    }


                    if (datas.getDfAuditDetail().getDecisionLevel().equals("Level1")) {
                        fd.setNextLevelUser(datas.getDfAuditDetail().getResponsibleId2() + "");
                        fd.setNowLevelUser(datas.getDfAuditDetail().getResponsibleId());
                        fd.setNowLevelUserName(datas.getDfAuditDetail().getResponsible());
                        fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                        fd.setCurrentTimeoutLevel(1);
                        //绑定责任人
                        DfFlowDataUser du = new DfFlowDataUser();
                        du.setUserAccount(datas.getDfAuditDetail().getResponsibleId());
                        dataUser.add(du);
                    } else if (datas.getDfAuditDetail().getDecisionLevel().equals("Level2")) {
                        fd.setNextLevelUser(datas.getDfAuditDetail().getResponsibleId3() + "");
                        if (null != datas.getDfAuditDetail().getResponsibleId()) {  // 判断等级1是否有责任人
                            fd.setNowLevelUser(datas.getDfAuditDetail().getResponsibleId() + "," + datas.getDfAuditDetail().getResponsibleId2());
                        } else {
                            fd.setNowLevelUser(datas.getDfAuditDetail().getResponsibleId2());
                        }
                        if (null != datas.getDfAuditDetail().getResponsible()) {  // 判断等级1是否有责任人
                            fd.setNowLevelUserName(datas.getDfAuditDetail().getResponsible() + "," + datas.getDfAuditDetail().getResponsible2());
                        } else {
                            fd.setNowLevelUserName(datas.getDfAuditDetail().getResponsible2());
                        }
                        if (null != datas.getDfAuditDetail().getResponsibleId2()) {
                            //绑定责任人
                            DfFlowDataUser du = new DfFlowDataUser();
                            du.setUserAccount(datas.getDfAuditDetail().getResponsibleId2());
                            dataUser.add(du);
                        }



                        fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                        fd.setCurrentTimeoutLevel(1);
                    } else {

                        QueryWrapper<DfFlowAuditor>flaQw=new QueryWrapper<>();
                        flaQw.eq("approval","Y");
                        List<DfFlowAuditor>approvalList= dfFlowAuditorService.list(flaQw);
                        String nextUser="";
                        String nextUserCode="";
                        int i=0;
                        if(null!=approvalList&&approvalList.size()>0){
                            for(DfFlowAuditor dd:approvalList){
                                if(i>0){
                                    nextUser+="," ;
                                    nextUserCode+="," ;
                                }
                                nextUser+=dd.getManName() ;
                                nextUserCode+=dd.getManCode();
                                i++;
                            }
                        }
                        fd.setNextLevelUser(nextUser);
                        if (null != datas.getDfAuditDetail().getResponsibleId2()) {  // 判断等级2是否有责任人
                            fd.setNowLevelUser(datas.getDfAuditDetail().getResponsibleId2() + "," + datas.getDfAuditDetail().getResponsibleId3());
                        } else {
                            fd.setNowLevelUser(datas.getDfAuditDetail().getResponsibleId3());
                        }
                        if (null != datas.getDfAuditDetail().getResponsible2()) {  // 判断等级2是否有责任人
                            fd.setNowLevelUserName(datas.getDfAuditDetail().getResponsible2() + "," + datas.getDfAuditDetail().getResponsible3());
                        } else {
                            fd.setNowLevelUserName(datas.getDfAuditDetail().getResponsible3());
                        }
                        if (null != datas.getDfAuditDetail().getResponsibleId3()) {
                            //绑定责任人
                            DfFlowDataUser du = new DfFlowDataUser();
                            du.setUserAccount(datas.getDfAuditDetail().getResponsibleId3());
                            dataUser.add(du);
                        }
                        fd.setLevel3PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                        fd.setStatus("待审批");
                        fd.setCurrentTimeoutLevel(2);
                    }
                    fd.setFlowLevelName(datas.getDfAuditDetail().getDecisionLevel());
                    //设置显示人
                    fd.setShowApprover(fd.getNowLevelUserName());
                    QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
                    atQw.eq("type", "稽查");
                    atQw.last("limit 1");
                    DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
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
                    DfFlowDataService.save(fd);
                    for(DfFlowDataUser dfu:dataUser){
                        dfu.setFlowDataId(fd.getId());
                    }
                    DfFlowDataUserService.saveBatch(dataUser);
//                    QueryWrapper<DfFlowRelationUser> qw2 = new QueryWrapper<>();
////                    qw2.eq("flow_id", fb.getFlowId());
//                    List<DfFlowRelationUser> userList = DfFlowRelationUserService.list(qw2);
//                    if (userList.size() > 0) {
//                        List<DfFlowDataUser> dataUser = new ArrayList<>();
//                        for (DfFlowRelationUser u : userList) {
//                            DfFlowDataUser du = new DfFlowDataUser();
//                            du.setFlowDataId(fd.getId());
//                            du.setFlowLevel(fd.getFlowLevel());
//                            du.setUserId(u.getUserId());
//                            du.setCreateName(datas.getDfAuditDetail().getCreateName());
//                            dataUser.add(du);
//                        }
//                        DfFlowDataUserService.saveBatch(dataUser);
//                    }
                }
                if (null != datas.getJobId()) {
                    DfMyJob job = new DfMyJob();
                    job.setId(datas.getJobId());
                    job.setResult("已处理");
                    DfMyJobService.updateById(job);
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public Result batchSave(@RequestBody BatchSaveControlStandardStatus datas) {
        for(DfControlStandardStatus d:datas.getDatas()){
            d.setProjectId(d.getProject());
            d.setFactoryId(d.getFactory());
            d.setLineBodyId(d.getLinebody());
        }

        if (DfControlStandardStatusService.saveBatch(datas.getDatas())) {
            if (null != datas.getJobId()) {
                DfMyJob job = new DfMyJob();
                job.setId(datas.getJobId());
                job.setResult("已处理");
                DfMyJobService.updateById(job);
            }
            return new Result(200, "保存成功");
        } else {
            return new Result(500, "保存失败");
        }
    }

    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfControlStandardStatusService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String dataStatus, String factoryCode, String workshopSectionCode, String stationCode, String lineBodyBode, String projectCode, String routingId) {
//        try {
        Page<DfControlStandardStatus> pages = new Page<DfControlStandardStatus>(page, limit);
        QueryWrapper<DfControlStandardStatus> ew = new QueryWrapper<DfControlStandardStatus>();
        if (null != dataStatus && !dataStatus.equals("")) {
            ew.like("s.data_status", dataStatus);
        }
        if (null != factoryCode && !factoryCode.equals("")) {
            ew.eq("s.factory_id", factoryCode);
        }
        if (null != workshopSectionCode && !workshopSectionCode.equals("")) {
            ew.eq("s.workshop_section_id", workshopSectionCode);
        }
        if (null != stationCode && !stationCode.equals("")) {
            ew.eq("s.workstation_id", stationCode);
        }
        if (null != lineBodyBode && !lineBodyBode.equals("")) {
            ew.eq("s.line_body_id", lineBodyBode);
        }
        if (null != projectCode && !projectCode.equals("")) {
            ew.eq("s.project_id", projectCode);
        }
        if (null != routingId && !routingId.equals("")) {
            ew.eq("s.routing_id", routingId);
        }
        ew.orderByDesc("create_time");
        IPage<DfControlStandardStatus> list = DfControlStandardStatusService.listByJoinPage(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

    /**
     * IPQC品质反馈系统
     * 不同天数对比
     * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
     *      稽核符合度=所有稽核符合项/总稽核项
     * @param factoryId 厂别
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping(value = "/getComparationByDay")
    @ApiOperation("稽查看板KPI - 不同天数对比")
    public Result getComparationByDay(String factoryId , String startTime, String endTime){
		String startWeek = TimeUtil.getWeekOfYear(startTime);
		//获取起始日期到结束日期所有时间
        List<String> daysList = TimeUtil.getTwoDaysDayDes(startTime, endTime);
        //稽核符合度
        QueryWrapper<DfControlStandardStatus> qw1 = new QueryWrapper<>();
        QueryWrapper<DfControlStandardStatus> qw2 = new QueryWrapper<>();
        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
            qw1.between("s.create_time", startTime, endTime + " 23:59:59");
            qw2.between("s.create_time", startTime, endTime + " 23:59:59");
        } else if (null != startTime && !startTime.equals("")) {
            qw1.ge("s.create_time", startTime);
            qw2.ge("s.create_time", startTime);
        } else if (null != endTime && !endTime.equals("")) {
            qw1.le("s.create_time", endTime + " 23:59:59");
            qw2.le("s.create_time", endTime + " 23:59:59");
        }
        List<DfControlStandardStatus> all = DfControlStandardStatusService.listCountByDay(qw1);

        qw2.eq("s.data_status", "NG");
        List<DfControlStandardStatus> ng = DfControlStandardStatusService.listCountByDay(qw2);
        //问题关闭率
        QueryWrapper<DfAuditDetail> qw3 = new QueryWrapper<>();
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            qw3.between("report_time", startTime, endTime + " 23:59:59");
        }
        qw3.ne("data_type","设备状态");
        qw3.ne("data_type","动态IPQC");
        qw3.ne("data_type","风险隔离全检");
        List<DfAuditDetail> endNumList = DfAuditDetailService.getEndNumByDay(qw3);
        //处理时效性
        QueryWrapper<DfAuditDetail> qw4 = new QueryWrapper<>();
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            qw4.between("report_time", startTime, endTime + " 23:59:59");
        }
        qw4.ne("aud.data_type","设备状态");
        qw4.ne("aud.data_type","动态IPQC");
        qw4.ne("aud.data_type","风险隔离全检");
        List<DfAuditDetail> timeoutList = DfAuditDetailService.getTimeoutByDay(qw4);

        HashMap<String, Object> data = new HashMap<>();
        ArrayList<Object> date = new ArrayList<>();
        ArrayList<Object> value = new ArrayList<>();
        for (String day : daysList) {
            BigDecimal result = BigDecimal.ZERO;

            DfAuditDetail close = endNumList.stream()
                .filter(x -> day.equals(new SimpleDateFormat("yyyy-MM-dd").format(x.getReportTime())))
                .findAny().orElse(null);
            if (close != null && close.getParentId() + close.getId() != 0){
                result = result.add(BigDecimal.valueOf(close.getParentId())
                    .divide(BigDecimal.valueOf(close.getParentId() + close.getId()),2,BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(0.4)));
            }

            DfAuditDetail timeout = timeoutList.stream()
                .filter(x -> day.equals(new SimpleDateFormat("yyyy-MM-dd").format(x.getReportTime())))
                .findAny().orElse(null);
            if (timeout != null && !(BigDecimal.ZERO.equals(BigDecimal.valueOf(timeout.getId()).add(new BigDecimal(timeout.getProjectName())).add(new BigDecimal(timeout.getDepartment()))))){
                result =result.add(BigDecimal.valueOf(timeout.getId())
                    .divide(BigDecimal.valueOf(timeout.getId()).add(new BigDecimal(timeout.getProjectName())).add(new BigDecimal(timeout.getDepartment())),2,BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(0.4)));
            }

            DfControlStandardStatus allStatus = all.stream()
                .filter(x -> day.equals(new SimpleDateFormat("yyyy-MM-dd").format(x.getCreateTime())))
                .findAny().orElse(null);
            if (allStatus != null && allStatus.getId() != 0 ){
                DfControlStandardStatus allNgStatus = ng.stream()
                        .filter(x -> day.equals(new SimpleDateFormat("yyyy-MM-dd").format(x.getCreateTime())))
                        .findAny().orElse(null);
                if (allNgStatus == null){
                    allNgStatus = new DfControlStandardStatus();
                    allNgStatus.setId(0);
                }
                result = result.add(BigDecimal.ONE
                        .subtract(BigDecimal.valueOf(allNgStatus.getId()).divide(BigDecimal.valueOf(allStatus.getId()), 2,BigDecimal.ROUND_DOWN))
                        .multiply(BigDecimal.valueOf(0.4)));
            }
            date.add(day);
            value.add(result);
        }
        data.put("date", date);
        data.put("value", value);
        return new Result(200, "查询成功", data);
    }

    /**
     * IPQC品质反馈系统
     * 不同周数对比
     * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
     *      稽核符合度=所有稽核符合项/总稽核项
     * @param factoryId 厂别
     * @param startTime 开始时间 (eg:2023-04-01)
     * @param endTime 结束时间 (eg:2023-06-01)
     * @return
	 * 	factoryId 202315 表示202305年第15周
	 * 	projectId 表示KPI统计结果
     */
    @GetMapping(value = "/getComparationByWeek")
    @ApiOperation("稽查看板KPI - 不同周数对比")
    public Result getComparationByWeek(String factoryId , String startTime, String endTime){
        List<DfControlStandardStatus> all = null;

        if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
             all = DfControlStandardStatusService.getComparationByWeek(startTime, endTime + " 23:59:59");
        }

        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> time = new ArrayList<>();
        ArrayList<Object> value = new ArrayList<>();
        for (DfControlStandardStatus one : all) {
            time.add(new StringBuilder(one.getFactoryId()).insert(4, "-"));
            value.add(one.getProjectId());
        }
        result.put("time",time);
        result.put("result",value);
        return new Result(200, "查询成功", result);
    }

	/**
	 * IPQC品质反馈系统
	 * 不同月份对比
	 * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
	 *      稽核符合度=所有稽核符合项/总稽核项
	 * @param factoryId 厂别
	 * @param startTime 开始时间 (eg:2023-04)
	 * @param endTime 结束时间 (eg:2023-06)
	 * @return
	 * 	factoryId 202305 表示2023年第5月
	 * 	projectId 表示KPI统计结果
	 */
	@GetMapping(value = "/getComparationByMonth")
    @ApiOperation("稽查看板KPI - 不同月份对比")
	public Result getComparationByMonth(String factoryId , String startTime, String endTime) throws ParseException {
		List<DfControlStandardStatus> all = null;

		if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
			all = DfControlStandardStatusService.getComparationByMonth(startTime, endTime);
		}

        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> time = new ArrayList<>();
        ArrayList<Object> value = new ArrayList<>();
        for (DfControlStandardStatus one : all) {
            time.add(new StringBuilder(one.getFactoryId()).insert(4, "-"));
            value.add(one.getProjectId());
        }
        result.put("time",time);
        result.put("result",value);
        return new Result(200, "查询成功", result);
	}
    //优化
    /**
     * IPQC品质反馈系统
     * 不同月份对比
     * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
     *      稽核符合度=所有稽核符合项/总稽核项
     * @param factoryId 厂别
     * @param startDate 开始时间 (eg:2023-04)
     * @param endDate 结束时间 (eg:2023-06)
     * @return
     * 	factoryId 202305 表示2023年第5月
     * 	projectId 表示KPI统计结果
     */
    @GetMapping(value = "/getComparation")
    @ApiOperation("稽查看板KPI - 不同(月周天)对比")
    public Result getComparation(
            String factoryId, @RequestParam String startDate,@RequestParam String endDate
            ,@RequestParam String type
    ) throws ParseException {
        String startTime = startDate +" 00:00:00";
        String endTime = endDate+" 23:59:59";

        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> bielTime = new ArrayList<>();
        ArrayList<Object> bielResult = new ArrayList<>();

        QueryWrapper<String> qw1 = new QueryWrapper<>();
        QueryWrapper<String> qw2 = new QueryWrapper<>();
        QueryWrapper<String> qw3 = new QueryWrapper<>();

        List<Rate3> list = null;

        switch (type){
            case "月":
                qw1.between("c.datelist",startDate,endDate);
                qw2
                        .apply("dad.report_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '动态IPQC' ")
                        .apply("dad.data_type != '风险隔离全检' ");

                qw3
                        .apply("dcsc.create_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionMonth(qw1,qw2,qw3);
                break;
            case "周":
                qw1.between("c.datelist",startDate,endDate);
                qw2
                        .apply("dad.report_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("dcsc.create_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");

                list = DfControlStandardStatusService.getComparaionWeek(qw1,qw2,qw3);
                break;
            case "天":
                qw1.between("c.datelist",startDate,endDate);
                qw2
                        .apply("dad.report_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("dcsc.create_time between '"+startTime+"' and '"+endTime+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionDay(qw1,qw2,qw3);
                break;
        }

        if (list==null||list.size()==0){
            return new Result(200, "该条件下没有相关数据");
        }

        for (Rate3 scorePoint : list) {
            bielTime.add(scorePoint.getStr1());
            bielResult.add(String.format("%.2f",scorePoint.getDou1()));
        }
        result.put("time",bielTime);
        result.put("bielResult",bielResult);
        return new Result(200, "查询成功", result);
    }

    @GetMapping(value = "/getComparationProcess")
    @ApiOperation("稽查看板KPI - 同(月周天)-不同工序对比")
    public Result getComparationProcess(
            String factoryId,String floor, @RequestParam String checkDate
            ,@RequestParam String type
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> processList = new ArrayList<>();
        ArrayList<Object> kpiList = new ArrayList<>();

        QueryWrapper<String> qw1 = new QueryWrapper<>();
        qw1.eq(StringUtils.isNotEmpty(floor),"dp.floor",floor);

        QueryWrapper<String> qw2 = new QueryWrapper<>();
        QueryWrapper<String> qw3 = new QueryWrapper<>();

        List<Rate3> list = null;

        switch (type){
            case "月":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%c')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%c')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionProcess(qw1,qw2,qw3);
                break;
            case "周":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%U')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%U')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");

                list = DfControlStandardStatusService.getComparaionProcess(qw1,qw2,qw3);
                break;
            case "天":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%m-%d')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%m-%d')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionProcess(qw1,qw2,qw3);
                break;
        }

        if (list==null||list.size()==0){
            return new Result(200, "该条件下没有相关数据");
        }

        for (Rate3 scorePoint : list) {
            processList.add(scorePoint.getStr1());
            kpiList.add(String.format("%.2f",scorePoint.getDou1()));
        }
        result.put("processList",processList);
        result.put("kpiList",kpiList);
        return new Result(200, "查询成功", result);
    }

//    /**
//     * IPQC品质反馈系统
//     * 不同月份对比
//     * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
//     *      稽核符合度=所有稽核符合项/总稽核项
//     * @param factoryId 厂别
//     * @param startDate 开始时间 (eg:2023-04)
//     * @param endDate 结束时间 (eg:2023-06)
//     * @return
//     * 	factoryId 202305 表示2023年第5月
//     * 	projectId 表示KPI统计结果
//     */
//    @GetMapping(value = "/getComparationFactory")
//    @ApiOperation("稽查看板KPI - 同(月周天)-不同工厂对比")
//    public Result getComparationFactory(
//            String factoryId, @RequestParam String startDate,@RequestParam String endDate
//    ) throws ParseException {
//        String startTime = startDate +" 00:00:00";
//        String endTime = endDate+" 23:59:59";
//
//        Map<String, Object> result = new HashMap<>();
//        ArrayList<Object> factoryList = new ArrayList<>();
//        ArrayList<Object> kpiList = new ArrayList<>();
//
//        QueryWrapper<String> qw2 = new QueryWrapper<>();
//        QueryWrapper<String> qw3 = new QueryWrapper<>();
//
//        qw2
//                .apply("dad.report_time between '"+startTime+"' and '"+endTime+"'")
//                .apply("dad.data_type != '设备状态' ");
//
//        qw3
//                .apply("dcsc.create_time between '"+startTime+"' and '"+endTime+"'")
//                .apply("dcsc.data_type !='设备状态'");
//        List<Rate3> list = DfControlStandardStatusService.getComparaionFactory(qw2,qw3);
//
//        if (list==null||list.size()==0){
//            return new Result(200, "该条件下没有相关数据");
//        }
//
//        for (Rate3 scorePoint : list) {
//            factoryList.add(scorePoint.getStr1());
//            kpiList.add(String.format("%.2f",scorePoint.getDou1()));
//        }
//        result.put("factoryList",factoryList);
//        result.put("kpiList",kpiList);
//        return new Result(200, "查询成功", result);
//    }

    @GetMapping(value = "/getComparationFactory")
    @ApiOperation("稽查看板KPI - 同(月周天)-不同工厂对比")
    public Result getComparationFactory(
            String factoryId,String floor, @RequestParam String checkDate
            ,@RequestParam String type
    ) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> processList = new ArrayList<>();
        ArrayList<Object> kpiList = new ArrayList<>();

        QueryWrapper<String> qw2 = new QueryWrapper<>();
        QueryWrapper<String> qw3 = new QueryWrapper<>();

        List<Rate3> list = null;

        switch (type){
            case "月":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%c')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%c')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionFactory(qw2,qw3);
                break;
            case "周":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%U')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%U')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");

                list = DfControlStandardStatusService.getComparaionFactory(qw2,qw3);
                break;
            case "天":
                qw2
                        .apply("DATE_FORMAT(dad.report_time,'%Y-%m-%d')= '"+checkDate+"'")
                        .apply("dad.data_type != '设备状态' ")
                        .apply("dad.data_type != '风险隔离全检' ")
                        .apply("dad.data_type != '动态IPQC' ");

                qw3
                        .apply("DATE_FORMAT(dcsc.create_time,'%Y-%m-%d')= '"+checkDate+"'")
                        .apply("dcsc.data_type !='设备状态'")
                        .apply("dcsc.data_type !='风险隔离全检'")
                        .apply("dcsc.data_type !='动态IPQC'");
                list = DfControlStandardStatusService.getComparaionFactory(qw2,qw3);
                break;
        }

        if (list==null||list.size()==0){
            return new Result(200, "该条件下没有相关数据");
        }

        for (Rate3 scorePoint : list) {
            processList.add(scorePoint.getStr1());
            kpiList.add(String.format("%.2f",scorePoint.getDou1()));
        }
        result.put("processList",processList);
        result.put("kpiList",kpiList);
        return new Result(200, "查询成功", result);
    }



//	/**
//	 * IPQC品质反馈系统
//	 * BA客户 VS Biel 稽核得分对比
//	 * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
//	 *      稽核符合度=所有稽核符合项/总稽核项
//	 * @param startTime 开始时间 (eg:2023-04-01)
//	 * @param endTime 结束时间 (eg:2023-06-01)
//	 * @return
//	 * 	factoryId 202305 表示2023年第5月
//	 * 	projectId 表示Biel KPI统计结果
//	 * 	workshopSectionId Apple KPI统计结果
//	 */
//	@GetMapping(value = "/getBaAndBielComparaion")
//    @ApiOperation("稽查看板KPI - BA客户 VS Biel 稽核得分对比")
//	public Result getBaAndBielComparaion(@RequestParam(value = "ids[]" , required = false) String[] ids ,@RequestParam(value = "baIds[]", required = false) String baIds,  String startTime, String endTime) throws ParseException {
//		List<DfControlStandardStatus> all = null;
//
//		if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {
//			all = DfControlStandardStatusService.getBaAndBielComparaion(startTime, endTime + " 23:59:59");
//		}
//
//        Map<String, Object> result = new HashMap<>();
//        ArrayList<Object> bielTime = new ArrayList<>();
//        ArrayList<Object> bielResult = new ArrayList<>();
//        ArrayList<Object> baResult = new ArrayList<>();
//        for (DfControlStandardStatus one : all) {
//            bielTime.add(one.getFactoryId());
//            bielResult.add(one.getProjectId());
//            baResult.add(one.getWorkshopSectionId());
//        }
//        result.put("time",bielTime);
//        result.put("bielResult",bielResult);
//        result.put("baResult",baResult);
//		return new Result(200, "查询成功", result);
//	}


    /**
     * IPQC品质反馈系统
     * BA客户 VS Biel 稽核得分对比
     * 计算方式:KPI=问题关闭率*0.4 + 处理时效性*0.4 + 稽核符合度*0.2
     *      稽核符合度=所有稽核符合项/总稽核项
     * @param startDate
     * @param endDate
     * @return
     * 	factoryId 202305 表示2023年第5月
     * 	projectId 表示Biel KPI统计结果
     * 	workshopSectionId Apple KPI统计结果
     */
    @GetMapping(value = "/getBaAndBielComparaion")
    @ApiOperation("稽查看板KPI - BA客户 VS Biel 稽核得分对比")
    public Result getBaAndBielComparaion(
            @RequestParam(value = "ids[]" , required = false) String[] ids
            ,@RequestParam(value = "baIds[]", required = false) String baIds
//            ,@RequestParam String checkTime
            ,@RequestParam String startDate
            ,@RequestParam String endDate
    ) throws ParseException {
//        String startTime = startDate +" 00:00:00";
//        String endTime = endDate+" 23:59:59";

//        startTime = startTime +" 00:00:00";
//        endTime = endTime+" 23:59:59";

        Map<String, Object> result = new HashMap<>();
        ArrayList<Object> bielTime = new ArrayList<>();
        ArrayList<Object> baResult = new ArrayList<>();

        QueryWrapper<String> qw1 = new QueryWrapper<>();
        qw1.between("c.datelist",startDate,endDate);
        List<Rate3> list = DfControlStandardStatusService.getBaComparaion(qw1);
        if (list==null||list.size()==0){
            return new Result(200, "该条件下没有相关数据");
        }

        for (Rate3 scorePoint : list) {
            bielTime.add(scorePoint.getStr1());
            baResult.add(String.format("%.2f",scorePoint.getDou1()));
        }
        result.put("time",bielTime);
        result.put("baResult",baResult);
        return new Result(200, "查询成功", result);
    }
}
