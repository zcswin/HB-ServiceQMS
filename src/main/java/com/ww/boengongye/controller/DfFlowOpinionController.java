package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfFlowDataUserService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 流转意见表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-31
 */
@Controller
@RequestMapping("/dfFlowOpinion")
@ResponseBody
@CrossOrigin
public class DfFlowOpinionController {
    @Autowired
    com.ww.boengongye.service.DfFlowOpinionService DfFlowOpinionService;

    @Autowired
    private DfFlowDataUserService dfFlowDataUserService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(DfFlowOpinionController.class);

    @RequestMapping(value = "/listByFlowDataId")
    public Object listByFlowDataId(int id) {
        QueryWrapper<DfFlowOpinion> qw = new QueryWrapper<>();
        qw.eq("flow_data_id", id);
        return new Result(200, "查询成功", DfFlowOpinionService.list(qw));
    }

    @RequestMapping(value = "/listByFlowDataIdAndUserId")
    public Object listByFlowDataIdAndUserId(int id,String userId) {
        QueryWrapper<DfFlowOpinion> qw = new QueryWrapper<>();
        qw.eq("flow_data_id", id);
        qw.and(wrapper -> wrapper.eq("sender_id", userId).or().eq("recipient_id", userId));
        return new Result(200, "查询成功", DfFlowOpinionService.list(qw));
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result update(@RequestBody DfFlowOpinion datas) {
        if (datas.getOpinion().contains("审批")) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", datas.getSenderId());
            User user = userService.getOne(userQueryWrapper);

            QueryWrapper<DfFlowDataUser> qw = new QueryWrapper<>();
            qw.eq("flow_data_id", datas.getFlowDataId())
                    .eq("user_account", user.getName());
            DfFlowDataUser one = dfFlowDataUserService.getOne(qw);
            if (null == one) {
                DfFlowDataUser dfFlowDataUser = new DfFlowDataUser();
                dfFlowDataUser.setFlowDataId(datas.getFlowDataId());
                dfFlowDataUser.setUserAccount(user.getName());
                dfFlowDataUserService.save(dfFlowDataUser);
            }
        }

            if (DfFlowOpinionService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
    }
}
