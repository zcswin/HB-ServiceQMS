package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfSizeFail;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.service.DfSizeFailService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Controller
@RequestMapping("/dfSizeFail")
@Api(tags = "尺寸NG数据")
@CrossOrigin
@ResponseBody
public class DfSizeFailController {
    @Autowired
    private DfSizeFailService DfSizeFailService;

    @RequestMapping(value = "/getByParentId")
    public Object getByParentId(int id,String type) {
        QueryWrapper<DfSizeFail>qw=new QueryWrapper<>();
        qw.eq("parent_id",id);
        qw.eq("type",type);
//        qw.eq("s.key_point",1);
//        return new Result(0, "查询成功", DfSizeFailService.listKeyPoint(qw));
        return new Result(0, "查询成功", DfSizeFailService.list(qw));
    }

}
