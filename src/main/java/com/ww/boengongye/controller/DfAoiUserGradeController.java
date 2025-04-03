package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfAoiUserGrade;
import com.ww.boengongye.service.DfAoiUserGradeService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * AOI员工考试等级 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-10-23
 */
@Controller
@RequestMapping("/dfAoiUserGrade")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI员工考试等级")
public class DfAoiUserGradeController {

    @Autowired
    private DfAoiUserGradeService dfAoiUserGradeService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public Result save(@RequestBody DfAoiUserGrade dfAoiUserGrade){
        dfAoiUserGrade.setTestTime(Timestamp.valueOf(LocalDateTime.now()));
        if (!dfAoiUserGradeService.save(dfAoiUserGrade)){
            return new Result(200,"账号为"+dfAoiUserGrade.getUserCode()+"的考试等级数据保存失败");
        }
        return new Result(200,"账号为"+dfAoiUserGrade.getUserCode()+"的考试等级数据保存成功");
    }
}
