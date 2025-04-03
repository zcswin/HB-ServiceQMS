package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiDecideLog;
import com.ww.boengongye.service.DfAoiDecideLogService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 判定记录表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Controller
@RequestMapping("/dfAoiDecideLog")
@ResponseBody
@CrossOrigin
@Api(tags = "判定记录")
public class DfAoiDecideLogController {
    private static final Logger logger = LoggerFactory.getLogger(DfAoiDecideLogController.class);

    @Autowired
    private DfAoiDecideLogService dfAoiDecideLogService;


    /**
     * 通过明码获取AOI玻璃单片检测记录
     * @param barCode
     * @return
     */
    @RequestMapping(value = "getAllDecideLogList",method = RequestMethod.GET)
    @ApiOperation("通过明码获取AOI玻璃单片检测记录")
    public Result getAllDecideLogList(String barCode){
        QueryWrapper<DfAoiDecideLog> ew = new QueryWrapper<>();
        ew.eq("bar_code",barCode);
        List<DfAoiDecideLog> list = dfAoiDecideLogService.list(ew);
        return new Result(200,"获取AOI玻璃单片检测记录成功",list);
    }


}
