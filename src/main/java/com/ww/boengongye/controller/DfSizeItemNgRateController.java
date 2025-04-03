package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfSizeItemNgRate;
import com.ww.boengongye.service.DfSizeItemNgRateService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸NG TOP 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-07-04
 */
@Controller
@RequestMapping("/dfSizeItemNgRate")
@ResponseBody
@CrossOrigin
@Api(tags = "尺寸看板汇总--尺寸NG TOP")
public class DfSizeItemNgRateController {

    @Autowired
    private DfSizeItemNgRateService dfSizeItemNgRateService;

    @GetMapping("/listAllProcessNgItemNgRate")
    public Result listAllProcessNgItemNgRate(){
        List<DfSizeItemNgRate> dfSizeItemNgRates = dfSizeItemNgRateService.listAllProcessNgItemNgRate();
        dfSizeItemNgRateService.saveBatch(dfSizeItemNgRates);
        return new Result(200, "成功添加" + dfSizeItemNgRates.size() + "条数据");
    }
}
