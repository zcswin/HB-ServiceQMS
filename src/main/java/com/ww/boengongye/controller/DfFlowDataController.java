package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import com.ww.boengongye.utils.XXSFilter;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程数据 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Controller
@RequestMapping("/dfFlowData")
@ResponseBody
@CrossOrigin
public class DfFlowDataController {
    @Autowired
    com.ww.boengongye.service.DfFlowDataService DfFlowDataService;

    @Autowired
    com.ww.boengongye.service.DfFlowDataUserService DfFlowDataUserService;

    @Autowired
    com.ww.boengongye.service.DfFlowBlockService DfFlowBlockService;

    @Autowired
    com.ww.boengongye.service.DfFlowRelationUserService DfFlowRelationUserService;

    @Autowired
    com.ww.boengongye.service.DfFlowOpinionService DfFlowOpinionService;

    @Autowired
    private DfFlowFinisherService dfFlowFinisherService;

    @Autowired
    private DfLiableManService dfLiableManService;

    @Autowired
    private DfFlowAuditorService dfFlowAuditorService;

    @Autowired
    private DfAuditDetailService dfAuditDetailService;

    private static final Logger logger = LoggerFactory.getLogger(DfFlowDataController.class);

    @GetMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfFlowDataService.list());
    }


    @GetMapping(value = "/getById")
    public Object getById(int id) {
        return new Result(200, "查询成功", DfFlowDataService.getById(id));
    }

    @GetMapping(value = "/getWgMachineCodeLast")
    public Object getMachineCodeLast(String machineCode) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();
        ew.inSql("data_id","SELECT\n" +
                "\tid AS data_id \n" +
                "FROM\n" +
                "\tdf_audit_detail s\n" +
                "  join(SELECT id AS data_id FROM df_qms_ipqc_waig_total WHERE STATUS = 'NG' AND f_mac = '"+machineCode+"' ORDER BY f_time DESC LIMIT 0, 1) d on s.parent_id =d.data_id");
        ew.eq("data_type","外观");
        return new Result(200, "查询成功", DfFlowDataService.getOne(ew));
    }


    @GetMapping(value = "/getByDataId")
    public Object getByDataId(int dataId,String dataType) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();
        ew.eq("data_id",dataId);
        ew.eq("data_type",dataType);
        return new Result(200, "查询成功", DfFlowDataService.getOne(ew));
    }

    //获取我的请求数量
    @GetMapping(value = "/listMyCount")
    public Object listMyCount(String userId) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("create_user_id", userId);
        }
        ew.eq("status", "待办结人确认");
        return new Result(200, "查询成功", DfFlowDataService.count(ew));
    }


    // 获取我的请求且 待办结人确认
    @ApiOperation("我发起的点检 且 待我确认")
    @GetMapping(value = "/listMy")
    public Object listMy(int page, int limit, String userId) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("create_user_id", userId);
        }
        ew.eq("status", "待办结人确认");
        ew.orderByDesc("create_time");
        IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }

    // 获取我的请求
    @ApiOperation("我发起的点检")
    @GetMapping(value = "/listMySend")
    public Object listMySend(int page, int limit, String userId) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("create_user_id", userId);
        }
        ew.orderByDesc("create_time");
        IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(),(int) list.getTotal() ,(int) list.getPages());
    }

    // 获取我的已超时
    @ApiOperation("获取我的已超时")
    @GetMapping(value = "/listOvertime")
    public Object listOvertime(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.like("ot.user_account", userAccount);
        }
        ew.orderByDesc("ad.create_time");
        IPage<DfFlowData> list = DfFlowDataService.listOvertime(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }


    // 获取有关联的已超时
    @ApiOperation("获取有关联的已超时")
    @GetMapping(value = "/listOvertimeByMatter")
    public Object listOvertimeByMatter(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.like("fu.user_account", userAccount);
        }
        ew.eq("fd.overtime_status", "已超时");
        ew.orderByDesc("fd.create_time");
        IPage<DfFlowData> list = DfFlowDataService.listOvertimeByMatter(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal(), (int) list.getPages());
    }



    // 获取有关联的已办
    @ApiOperation("获取有关联的已办")
    @GetMapping(value = "/listHaveDoneByMatter")
    public Object listHaveDoneByMatter(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.like("fu.user_account", userAccount);
        }
//        ew.eq("fd.status", "已关闭");

        ew.and(wrapper -> wrapper.like("fd.status", "已关闭"))
                .or(wrapper -> wrapper.like("fd.status", "待办结人确认"));
        ew.orderByDesc("fd.create_time");
        IPage<DfFlowData> list = DfFlowDataService.listOvertimeByMatter(pages, ew);
        return new Result(0, "查询成功", list.getRecords(),(int) list.getTotal(), (int) list.getPages());
    }

    //获取待办数量
    @GetMapping(value = "/getBacklogCount")
    public Object getBacklogCount(String userId) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("u.user_id", userId);
        }

        DfFlowData d = DfFlowDataService.getBacklogCount(ew);
        return new Result(200, "查询成功", d.getId());
    }

    //获取待办数量2
    @GetMapping(value = "/getBacklogCount2")
    public Object getBacklogCount2(String userAccount) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            QueryWrapper<DfFlowAuditor> qw = new QueryWrapper<DfFlowAuditor>();
            qw.eq("man_code",userAccount);
            qw.eq("approval","Y");
            qw.last("limit 1");
            DfFlowAuditor da=   dfFlowAuditorService.getOne(qw);
            if(null!=da){
                ew.and(wrapper->wrapper.like("now_level_user",userAccount)
                        .and(wrapper2->wrapper2.eq("status", "待确认")
                                .or().eq("status", "待提交").or().eq("status","退回待修改")))
                        .or(wrapper->wrapper.eq("status","待审批"));

            }else{
                ew.like("now_level_user", userAccount);
                ew.and(wrapper -> wrapper.eq("status", "待确认").or().eq("status", "待提交").or().eq("status","退回待修改"));
            }
            return new Result(200, "查询成功", DfFlowDataService.count(ew));
        }
        return new Result(500, "查询失败");
    }

    //获取待办
    @GetMapping(value = "/listBacklog")
    public Object listBacklog(int page, int limit, String userId) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("u.user_id", userId);
        }

        ew.orderByDesc("d.create_time");
        IPage<DfFlowData> list = DfFlowDataService.listBacklog(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }


    //获取待办2
    @GetMapping(value = "/listBacklog2")
    public Object listBacklog2(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {

            QueryWrapper<DfFlowAuditor> qw = new QueryWrapper<DfFlowAuditor>();
            qw.eq("man_code",userAccount);
            qw.eq("approval","Y");
            qw.last("limit 1");
            DfFlowAuditor da=   dfFlowAuditorService.getOne(qw);
            if(null!=da){
                ew.and(wrapper->wrapper.like("now_level_user",userAccount)
                        .and(wrapper2->wrapper2.eq("status", "待确认")
                                .or().eq("status", "待提交")
                                .or().eq("status", "FACA退回待提交")
                                .or().eq("status","退回待修改")))
                        .or(wrapper->wrapper.eq("status","待审批"));

            }else{
                ew.like("now_level_user", userAccount);
                ew.and(wrapper -> wrapper.eq("status", "待确认")
                        .or().eq("status", "待提交")
                        .or().eq("status", "FACA退回待提交")
                        .or().eq("status","退回待修改"));
            }
            ew.orderByDesc("create_time");
            IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);

            // 添加升级超时单子，不同职位获取不一样的单子
            List<DfFlowData> dfFlowData = DfFlowDataService.listUpOutTimeLevelByAccount(userAccount);
            if (dfFlowData.size() > 0) {
                if (list.getTotal() > 0) {
                    list.getRecords().addAll(dfFlowData);
                    return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal() + dfFlowData.size());
                } else {
                    list.setRecords(dfFlowData);
                    return new Result(0, "查询成功", list.getRecords(), dfFlowData.size());
                }
            }

            return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
        }
        return new Result(500, "查询失败");
    }

    //获取待办和待办结
    @GetMapping(value = "/listBacklog3")
    @ApiOperation("获取待办和待办结")
    public Object listBacklog3(int page, int limit, String userName) {
        Page<DfFlowData> pages = new Page<>(page, limit);

        if (null != userName && !userName.equals("")) {
            QueryWrapper<DfFlowData> ew1 = new QueryWrapper<>();
            QueryWrapper<DfFlowData> ew2 = new QueryWrapper<>();
            List<DfFlowData> auditList = null;

            QueryWrapper<DfFlowAuditor> qw = new QueryWrapper<>();
            qw.eq("man_name",userName);
            qw.last("limit 1");
            DfFlowAuditor da = dfFlowAuditorService.getOne(qw);
            if(null!=da){  // 发起权限人

                if ("Y".equals(da.getApproval())) { // 审核人，有审核和办结权限
                    ew1.and(wrapper->wrapper.like("create_name",userName)
                                    .and(wrapper2->wrapper2.eq("status", "待办结人确认")
                                            .or().eq("status","退回待修改")))
                            .or(wrapper->wrapper.eq("status","待审批"));
                } else {  // 非审核人，但是有办结权限
                    ew1.and(wrapper->wrapper.like("create_name",userName)
                            .and(wrapper2->wrapper2.eq("status", "待办结人确认")
                                    .or().eq("status","退回待修改")));
                }
                ew1.orderByDesc("create_time");
                auditList = DfFlowDataService.list(ew1);
            }

            // 办理
            ew2.like("now_level_user_name", userName);
            ew2.and(wrapper -> wrapper.eq("status", "待确认")
                    .or().eq("status", "待提交")
                    .or().eq("status", "FACA退回待提交")
                    .or().eq("status","退回待修改"));
            ew2.orderByDesc("create_time");
            IPage<DfFlowData> list = DfFlowDataService.page(pages, ew2);

            if (null != auditList && auditList.size() > 0) {
                if (list.getTotal() > 0) {
                    list.getRecords().addAll(auditList);
                } else {
                    list.setRecords(auditList);
                }
            }

            // 添加升级超时单子，不同职位获取不一样的单子
            List<DfFlowData> dfFlowData = DfFlowDataService.listUpOutTimeLevelByUserName(userName);
            if (dfFlowData.size() > 0) {
                //list.getRecords().addAll(dfFlowData);
                if (list.getTotal() > 0) {
                    list.getRecords().addAll(dfFlowData);
                    return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal() + dfFlowData.size());
                } else {
                    list.setRecords(dfFlowData);
                    return new Result(0, "查询成功", list.getRecords(), dfFlowData.size());
                }
            }

            return new Result(0, "查询成功", list.getRecords(),(int) list.getTotal(), (int) list.getPages());
        }
        return new Result(500, "查询失败");
    }

    // 获取已办（经过流程就有）
    @GetMapping("/listClosedByUserId")
    @ApiOperation("获取已办（经过流程就有）")
    public Result listClosedByUserId(int page, int limit, @RequestParam String userId) {
        Page<DfFlowData> pages = new Page<>(page, limit);

        IPage<DfFlowData> dfFlowDataIPage = DfFlowDataService.listClosedByUserId(pages, userId);
        return new Result<>(200, "查询成功", dfFlowDataIPage.getRecords(), (int) dfFlowDataIPage.getTotal());
    }

    // 获取登录人的等级
    @GetMapping("/getLoginerLevel")
    @ApiOperation("获取登录人的等级")
    public Result getLoginerLevel(@RequestParam String userAccount) {
        if (!"".equals(userAccount)) {
            QueryWrapper<DfLiableMan> liableQW = new QueryWrapper<>();
            liableQW.eq("liable_man_code", userAccount)
                    .and(wrapper -> wrapper.eq("type", "all").or().eq("type", "ALL").or().like("type", "check"))
                    .last("limit 1");
            DfLiableMan liableMan = dfLiableManService.getOne(liableQW);
            if (null != liableMan) {
                return new Result(200, "获取成功", liableMan.getProblemLevel());
            }

            QueryWrapper<DfFlowAuditor> auditorQW = new QueryWrapper<>();
            auditorQW.eq("man_code", userAccount)
                    .and(wrapper -> wrapper.eq("type", "all").or().eq("type", "ALL").or().like("type", "稽查"))
                    .last("limit 1");
            DfFlowAuditor auditor = dfFlowAuditorService.getOne(auditorQW);
            if (null != auditor) {
                return new Result(200, "获取成功", auditor.getLevel());
            }

            return new Result(300, "查无此人等级");
        } else {
            return new Result<>(300, "账号不能为空");
        }
    }

    //获取待办3
    @GetMapping(value = "/listBacklogByProcess")
    public Object listBacklogByProcess(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.like("now_level_user", userAccount);

            ew.and(wrapper -> wrapper.eq("status", "待确认").or().eq("status", "待提交"));
            ew.orderByDesc("create_time");
            IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
            return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
        }
        return new Result(500, "查询失败");
    }

    //获取已办
    @GetMapping(value = "/listHaveDone")
    public Object listHaveDone(int page, int limit, String userId) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("u.user_id", userId);
        }

        ew.orderByDesc("d.create_time");
        IPage<DfFlowData> list = DfFlowDataService.listHaveDone(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
    }

    //获取已办
    @ApiOperation("获取已办")
    @GetMapping(value = "/listHaveDone2")
    public Object listHaveDone2(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.like("now_level_user", userAccount);
            ew.and(wrapper -> wrapper.eq("status", "已关闭").or().eq("status", "待办结人确认"));

            ew.orderByDesc("create_time");
            IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
            return new Result(0, "查询成功", list.getRecords(),(int) list.getTotal(), (int) list.getPages());
        }
        return new Result(500, "查询失败");
    }

    //获取已办3
    @ApiOperation("获取已办3 -- 发起点检人")
    @GetMapping(value = "/listHaveDone3")
    public Object listHaveDone3(int page, int limit, String userAccount) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.eq("create_name", userAccount);
            ew.eq("status", "已关闭");
            ew.orderByDesc("create_time");
            IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
            return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
        }
        return new Result(500, "查询失败");
    }

    //获取已办数量
    @GetMapping(value = "/getHaveDoneCount")
    public Object getHaveDoneCount(String userId) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userId && !userId.equals("")) {
            ew.eq("u.user_id", userId);
        }
        DfFlowData d = DfFlowDataService.getHaveDoneCount(ew);
        return new Result(200, "查询成功", d.getId());
    }

    //获取已办数量2
    @GetMapping(value = "/getHaveDoneCount2")
    public Object getHaveDoneCount2(String userAccount) {
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();

        if (null != userAccount && !userAccount.equals("")) {
            ew.eq("now_level_user", userAccount);
            ew.eq("status", "已关闭");
            DfFlowData d = DfFlowDataService.getHaveDoneCount(ew);
            return new Result(200, "查询成功", DfFlowDataService.count(ew));
        }
        return new Result(500, "查询失败");
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfFlowData datas) {

        if (null != datas.getId()) {
            QueryWrapper<DfFlowBlock> qw1 = new QueryWrapper<DfFlowBlock>();
            qw1.eq("flow_type", datas.getFlowType());
            qw1.eq("sort", datas.getNextLevel());
            DfFlowBlock fb = DfFlowBlockService.getOne(qw1);
            if (null != fb) {
                datas.setFlowLevelName(fb.getName());
                datas.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
            }
            datas.setFlowLevel(datas.getNextLevel());
            datas.setUpdateName(datas.getOperator());
            if (DfFlowDataService.updateById(datas)) {
                QueryWrapper<DfFlowRelationUser> qw2 = new QueryWrapper<>();
                qw2.eq("flow_id", fb.getFlowId());
                List<DfFlowRelationUser> userList = DfFlowRelationUserService.list(qw2);
                if (userList.size() > 0) {
                    List<DfFlowDataUser> dataUser = new ArrayList<>();
                    List<DfFlowOpinion> opinions = new ArrayList<>();
                    for (DfFlowRelationUser u : userList) {
                        DfFlowDataUser du = new DfFlowDataUser();
                        du.setFlowDataId(datas.getId());
                        du.setFlowLevel(datas.getFlowLevel());
                        du.setUserAccount(u.getUserId());
                        du.setCreateName(datas.operator);

                        QueryWrapper<DfFlowDataUser> fdqw = new QueryWrapper<>();
                        fdqw.eq("flow_data_id", datas.getId());
                        fdqw.eq("user_id", u.getUserId());
                        fdqw.last("limit 0,1");
                        DfFlowDataUser alreadyData = DfFlowDataUserService.getOne(fdqw);
                        if (null != alreadyData) {
                            du.setId(alreadyData.getId());
                            DfFlowDataUserService.updateById(du);
                        } else {
                            dataUser.add(du);
                        }
                        DfFlowOpinion fo = new DfFlowOpinion();
                        fo.setFlowDataId(datas.getId());
                        fo.setOpinion(datas.getFlowOpinion());
                        fo.setSender(datas.getOperator());
                        fo.setSenderId(datas.getOperatorId());
                        fo.setRecipient(u.getUserName());
                        fo.setRecipientId(u.getUserId());
                        opinions.add(fo);
                    }
                    DfFlowDataUserService.saveBatch(dataUser);
                    DfFlowOpinionService.saveBatch(opinions);
                }

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfFlowDataService.save(datas)) {
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


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody DfFlowData datas) {

        if (null != datas.getId()) {
//            if ("待办结人确认".equals(datas.getStatus())) {
//                QueryWrapper<DfFlowData> qw = new QueryWrapper<>();
//                qw.eq("id", datas.getId());
//                DfFlowData flow = DfFlowDataService.getOne(qw);
//                if (null != flow.getStartTimeout()) {
//                    QueryWrapper<DfFlowData> auditQw = new QueryWrapper<>();
//                    auditQw.eq("flow.id", datas.getId());
//                    DfAuditDetail audit = DfFlowDataService.getJoinAudit(auditQw);
//                    long dTime = System.currentTimeMillis() - audit.getReportTime().getTime();
//                    long overTimeLimit = (long) (flow.getReadTimeMax() + flow.getDisposeTimeMax()) * 60 * 1000;
//                    System.out.println("解决用时：" + dTime);
//                    System.out.println("时限用时：" + overTimeLimit);
//                    if (dTime < overTimeLimit) {
//                        DfFlowDataService.updateOverTimeById(datas.getId());
//                    }
//                }
//            }
            if (DfFlowDataService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }
        return new Result(500, "保存失败");
    }
    @RequestMapping(value = "/updateByFaCa", method = RequestMethod.POST)
    public Result updateByFaCa(@RequestBody DfFlowData datas) {


        UpdateWrapper<DfFlowData> uw=new UpdateWrapper<>();
        uw.eq("id",datas.id);
        uw.set("overtime_status",null);
        uw.set("start_timeout",null);
        uw.set("submit_time",datas.getSubmitTime());
        uw.set("status",datas.getStatus());

            if (DfFlowDataService.update(uw)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
    }

    // 根据id删除
    @GetMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfFlowDataService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String name, String factoryId, String lineBodyId) {
//        try {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowData> ew = new QueryWrapper<DfFlowData>();
        if (null != name && !name.equals("")) {
            ew.like("p.name", name);
        }
        if (null != factoryId && !factoryId.equals("")) {
            ew.eq("f.id", factoryId);
        }
        if (null != lineBodyId && !lineBodyId.equals("")) {
            ew.eq("l.id", lineBodyId);
        }
        ew.orderByDesc("p.create_time");
        IPage<DfFlowData> list = DfFlowDataService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @ApiOperation("是否有办结权限")
    @GetMapping("/isFinisher")
    public Result isFinisher(String userAccount) {
        QueryWrapper<DfFlowFinisher> finishQw = new QueryWrapper<>();
        finishQw.eq(null != userAccount, "man_code", userAccount);
        List<DfFlowFinisher> finishList = dfFlowFinisherService.list(finishQw);

        QueryWrapper<DfFlowAuditor> auditorQw = new QueryWrapper<>();  // 发起人也有办结权限
        auditorQw.eq(null != userAccount, "man_code", userAccount);
        List<DfFlowAuditor> auditorList = dfFlowAuditorService.list(auditorQw);

        if (finishList.size() > 0 || auditorList.size() > 0) {
            return new Result(200, "有办结权限", true);
        } else {
            return new Result(200, "无办结权限", false);
        }
        //return new Result(200, "有办结权限", true);
    }

    @ApiOperation("可办结列表")
    @GetMapping("/listFinish")
    public Result listFinish(@RequestParam String userAccount, int page, int limit) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfFlowFinisher> finishQw = new QueryWrapper<>();
        finishQw.eq(null != userAccount, "man_code", userAccount);
        List<DfFlowFinisher> finishList = dfFlowFinisherService.list(finishQw);
        QueryWrapper<DfFlowData> qw = new QueryWrapper<>();
        qw.or().eq("aud.data_type", "稽查")
                .eq("flow.status", "待办结人确认")
                .eq("user.name", userAccount);

        List<String[]> requestList = new ArrayList<>();  // 存储需求
        if (finishList.size() > 0) {
            for (DfFlowFinisher dfFlowFinisher : finishList) {
                String type = dfFlowFinisher.getType();
                String process = dfFlowFinisher.getProcess();
                String level = dfFlowFinisher.getLevel();
                String[] types = type.split(",");
                String[] processes = process.split(",");
                String[] levels = level.split(",");

                for (int i = 0; i < types.length; i++) {
                    if ("all".equals(types[i]) || "ALL".equals(types[i])) types[i] = null;
                    for (int j = 0; j < processes.length; j++) {
                        if ("all".equals(processes[j]) || "ALL".equals(processes[j])) processes[j] = null;
                        for (int k = 0; k < levels.length; k++) {
                            if ("all".equals(levels[k]) || "ALL".equals(levels[k])) levels[k] = null;
                            String[] request = new String[3];
                            request[0] = types[i];
                            request[1] = processes[j];
                            request[2] = levels[k];
                            requestList.add(request);
                        }
                    }
                }
            }
        }


        for (String[] request : requestList) {
            System.out.println(request[0]+" "+request[1]+" "+request[2]);
            if (null == request[2]) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq("flow.status", "待办结人确认")
                        .eq(null != request[1],"aud.process", request[1]);
            } else if (request[2].equals("1")) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level1").or().eq("aud.decision_level", "Level-1"));
            } else if (request[2].equals("2")) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level2").or().eq("aud.decision_level", "Level-2"));
            } else {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level3").or().eq("aud.decision_level", "Level-3"));
            }

        }
        //qw.eq("flow.status", "待办结人确认");
        qw.orderByDesc("flow.create_time");

        IPage<DfFlowData> dfFlowDataIPage = DfFlowDataService.listJoinAudit(pages, qw);



        return new Result(200, "查询成功", dfFlowDataIPage,(int) dfFlowDataIPage.getTotal(),(int)dfFlowDataIPage.getPages());
    }

    @ApiOperation("可办结列表2 -- 页面")
    @GetMapping("/listFinish2")
    public Result listFinish2(String userAccount, int page, int limit) {
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        userAccount = String.valueOf(userAccount);

        QueryWrapper<DfFlowFinisher> finishQw = new QueryWrapper<>();
        finishQw.eq(null != userAccount, "man_code", userAccount);
        List<DfFlowFinisher> finishList = dfFlowFinisherService.list(finishQw);
        List<String[]> requestList = new ArrayList<>();  // 存储需求
        if (finishList.size() > 0) {
            for (DfFlowFinisher dfFlowFinisher : finishList) {
                String type = dfFlowFinisher.getType();
                String process = dfFlowFinisher.getProcess();
                String level = dfFlowFinisher.getLevel();
                String[] types = type.split(",");
                String[] processes = process.split(",");
                String[] levels = level.split(",");

                for (int i = 0; i < types.length; i++) {
                    if ("all".equals(types[i]) || "ALL".equals(types[i])) types[i] = null;
                    for (int j = 0; j < processes.length; j++) {
                        if ("all".equals(processes[j]) || "ALL".equals(processes[j])) processes[j] = null;
                        for (int k = 0; k < levels.length; k++) {
                            if ("all".equals(levels[k]) || "ALL".equals(levels[k])) levels[k] = null;
                            String[] request = new String[3];
                            request[0] = types[i];
                            request[1] = processes[j];
                            request[2] = levels[k];
                            requestList.add(request);
                        }
                    }
                }
            }
        } else {
            return new Result(300, "无办结权限");
        }

        QueryWrapper<DfFlowData> qw = new QueryWrapper<>();
        for (String[] request : requestList) {
            System.out.println(request[0]+" "+request[1]+" "+request[2]);
            if (null == request[2]) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq("flow.status", "待办结人确认")
                        .eq(null != request[1],"aud.process", request[1]);
            } else if (request[2].equals("1")) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level1").or().eq("aud.decision_level", "Level-1"));
            } else if (request[2].equals("2")) {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level2").or().eq("aud.decision_level", "Level-2"));
            } else {
                qw.or().eq(null != request[0], "aud.data_type", request[0])
                        .eq(null != request[1],"aud.process", request[1])
                        .eq("flow.status", "待办结人确认")
                        .and(wrapper -> wrapper.eq("aud.decision_level", "Level3").or().eq("aud.decision_level", "Level-3"));
            }

        }
        qw.orderByDesc("flow.create_time");

        IPage<DfFlowData> dfFlowDataIPage = DfFlowDataService.listJoinAudit(pages, qw);

        return new Result(200, "查询成功", dfFlowDataIPage.getRecords(), (int)dfFlowDataIPage.getTotal());
    }

    @ApiOperation("可办理列表")
    @GetMapping("/listDoIt")
    public Result listDoIt(@RequestParam String userAccount, int page, int limit) {
        if ("".equals(userAccount) || null == userAccount) return new Result(300,"账号异常");
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfLiableMan> qw = new QueryWrapper<>();
        qw.eq("liable_man_code", userAccount);
        List<DfLiableMan> list = dfLiableManService.list(qw);
        List<String[]> requestList = new ArrayList<>();  // 存储需求
        if (null == list) {
            return new Result(200, "无办理权限");
        } else {
            for (DfLiableMan dfLiableMan : list) {
                String type = dfLiableMan.getType();
                String process = dfLiableMan.getProcessName();
                String level = dfLiableMan.getProblemLevel();
                String[] types = type.split(",");
                String[] processes = process.split(",");
                String[] levels = level.split(",");

                for (int i = 0; i < types.length; i++) {
                    if ("all".equals(types[i]) || "ALL".equals(types[i])) types[i] = null;
                    for (int j = 0; j < processes.length; j++) {
                        if ("all".equals(processes[j]) || "ALL".equals(processes[j])) processes[j] = null;
                        for (int k = 0; k < levels.length; k++) {
                            if ("all".equals(levels[k]) || "ALL".equals(levels[k])) levels[k] = null;
                            String[] request = new String[3];
                            request[0] = types[i];
                            request[1] = processes[j];
                            request[2] = levels[k];
                            requestList.add(request);
                        }
                    }
                }
            }

            QueryWrapper<DfFlowData> flowqw = new QueryWrapper<>();
            for (String[] request : requestList) {
                System.out.println(request[0]+" "+request[1]+" "+request[2]);
                if (null == request[2]) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .eq(null != request[1],"aud.process", request[1]);
                } else if (request[2].equals("1")) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level1").or().eq("aud.decision_level", "Level-1"));
                } else if (request[2].equals("2")) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level2").or().eq("aud.decision_level", "Level-2"));
                } else {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level3").or().eq("aud.decision_level", "Level-3"));
                }

            }
            flowqw.orderByDesc("flow.create_time");
            IPage<DfFlowData> dfFlowDataIPage = DfFlowDataService.listJoinAudit(pages, flowqw);
            return new Result(200, "有办理权限", dfFlowDataIPage);
        }

    }

    @ApiOperation("可办理列表2 -- 页面")
    @GetMapping("/listDoIt2")
    public Result listDoIt2(@RequestParam String userAccount, int page, int limit) {
        if ("".equals(userAccount) || null == userAccount) return new Result(300,"账号异常");
        Page<DfFlowData> pages = new Page<DfFlowData>(page, limit);
        QueryWrapper<DfLiableMan> qw = new QueryWrapper<>();
        qw.eq("liable_man_code", userAccount);
        List<DfLiableMan> list = dfLiableManService.list(qw);
        List<String[]> requestList = new ArrayList<>();  // 存储需求
        if (null == list) {
            return new Result(200, "无办理权限");
        } else {
            for (DfLiableMan dfLiableMan : list) {
                String type = dfLiableMan.getType();
                String process = dfLiableMan.getProcessName();
                String level = dfLiableMan.getProblemLevel();
                String[] types = type.split(",");
                String[] processes = process.split(",");
                String[] levels = level.split(",");

                for (int i = 0; i < types.length; i++) {
                    if ("all".equals(types[i]) || "ALL".equals(types[i])) types[i] = null;
                    for (int j = 0; j < processes.length; j++) {
                        if ("all".equals(processes[j]) || "ALL".equals(processes[j])) processes[j] = null;
                        for (int k = 0; k < levels.length; k++) {
                            if ("all".equals(levels[k]) || "ALL".equals(levels[k])) levels[k] = null;
                            String[] request = new String[3];
                            request[0] = types[i];
                            request[1] = processes[j];
                            request[2] = levels[k];
                            requestList.add(request);
                        }
                    }
                }
            }

            QueryWrapper<DfFlowData> flowqw = new QueryWrapper<>();
            for (String[] request : requestList) {
                System.out.println(request[0]+" "+request[1]+" "+request[2]);
                if (null == request[2]) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .eq(null != request[1],"aud.process", request[1]);
                } else if (request[2].equals("1")) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level1").or().eq("aud.decision_level", "Level-1"));
                } else if (request[2].equals("2")) {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level2").or().eq("aud.decision_level", "Level-2"));
                } else {
                    flowqw.or().eq(null != request[0], "aud.data_type", request[0])
                            .eq(null != request[1],"aud.process", request[1])
                            .and(wrapper -> wrapper.eq("flow.status", "待确认").or().eq("flow.status","待提交"))
                            .and(wrapper -> wrapper.eq("aud.decision_level", "Level3").or().eq("aud.decision_level", "Level-3"));
                }

            }
            flowqw.orderByDesc("flow.create_time");
            IPage<DfFlowData> dfFlowDataIPage = DfFlowDataService.listJoinAudit(pages, flowqw);
            return new Result(200, "有办理权限", dfFlowDataIPage.getRecords(), (int)dfFlowDataIPage.getTotal());
        }

    }

    @Transactional
    @GetMapping("/deleteNgByFlowId")
    @ApiOperation("录入异常删除")
    public Result deleteNgByFlowId(@RequestParam Integer auditId) {
        boolean b = dfAuditDetailService.removeById(auditId);

        if (b) {
            QueryWrapper<DfFlowData> removeQW = new QueryWrapper<>();
            removeQW.eq("data_id", auditId);
            DfFlowDataService.remove(removeQW);
            return new Result(200, "删除成功");
        } else {
            return new Result(204, "无此数据");
        }

    }

    @GetMapping("/createFlowDataFileUpdate")
    @ApiOperation("文件变更新增PDA记录")
    public Result createFlowDataFileUpdate(@RequestParam String fileName,
                                           @RequestParam String type,
                                           @RequestParam Integer dataId) {
        String title = fileName + "_" + type + "_更新通知";
        boolean ok = DfFlowDataService.createFlowData(title, type, dataId);
        if (ok) {
            return new Result(200, "更新成功");
        } else {
            return new Result(500, "更新失败");
        }

    }

}
