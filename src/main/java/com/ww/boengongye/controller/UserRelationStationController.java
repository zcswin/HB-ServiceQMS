package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.ww.boengongye.entity.SaveURS;
import com.ww.boengongye.entity.UserRelationStation;
import com.ww.boengongye.service.UserRelationStationService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户关联岗位 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-01-27
 */
@Controller
@RequestMapping("/userRelationStation")
@ResponseBody
@CrossOrigin
public class UserRelationStationController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    com.ww.boengongye.service.UserService UserService;

    @Autowired
    com.ww.boengongye.service.UserRelationStationService UserRelationStationService;


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody SaveURS datas) {

        UpdateWrapper<UserRelationStation>uw=new UpdateWrapper<>();
        uw.eq("user_id",datas.getId());
        UserRelationStationService.remove(uw);
            if (UserRelationStationService.saveBatch(datas.getUserRelationStation())) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }



    }

}
