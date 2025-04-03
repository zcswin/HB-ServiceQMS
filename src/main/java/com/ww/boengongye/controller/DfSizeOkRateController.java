package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfSizeOkRate;
import com.ww.boengongye.service.DfSizeOkRateService;
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
 * 尺寸看板汇总表--尺寸良率汇总 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-15
 */
@Controller
@RequestMapping("/dfSizeOkRate")
@CrossOrigin
@Api(tags = "尺寸看板汇总表--尺寸良率汇总")
@ResponseBody
public class DfSizeOkRateController {
    @Autowired
    private DfSizeOkRateService dfSizeOkRateService;

    @GetMapping("/updateData")
    public Result updateData() {
        List<DfSizeOkRate> dfSizeOkRates = dfSizeOkRateService.updateDate();
        for (DfSizeOkRate dfSizeOkRate : dfSizeOkRates) {
            dfSizeOkRate.setCreateTime(dfSizeOkRate.getDate());
        }
        dfSizeOkRateService.saveBatch(dfSizeOkRates);
        return new Result(200, "成功添加" + dfSizeOkRates.size() + "条数据");
    }

}
