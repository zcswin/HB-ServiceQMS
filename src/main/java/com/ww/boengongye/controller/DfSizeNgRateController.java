package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.service.DfSizeNgRateService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 尺寸NG率 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Controller
@RequestMapping("/dfSizeNgRate")
@CrossOrigin
@ResponseBody
@Api(tags = "尺寸NG率")
public class DfSizeNgRateController {
    @Autowired
    private DfSizeNgRateService dfSizeNgRateService;

    @GetMapping("/getNgRate")
    public Result getNgRate(String factory, String process, String project, String linebody, String dayOrNight,
                            String startDate, String endDate) {
        DfSizeNgRate data = dfSizeNgRateService.getAvg(factory, process, project, linebody, dayOrNight, startDate, endDate + " 23:59:59");
        if (null == data) return new Result(360, "找不到数据");
        Double[] value = new Double[22];
        value[0] = data.getAppearLength1();
        value[1] = data.getAppearLength2();
        value[2] = data.getAppearLength3();
        value[3] = data.getAppearWidth1();
        value[4] = data.getAppearWidth2();
        value[5] = data.getAppearWidth3();
        value[6] = data.getMiHoleDiameter();
        value[7] = data.getMiHoleRoundness();
        value[8] = data.getMiHoleCenterDistanceX();
        value[9] = data.getMiHoleCenterDistanceY();
        value[10] = data.getDbHoleDiameter();
        value[11] = data.getDbHoleRoundness();
        value[12] = data.getDbHoleCenterDistanceX();
        value[13] = data.getDbHoleCenterDistanceY();
        value[14] = data.getSHoleDiameter();
        value[15] = data.getSHoleRoundness();
        value[16] = data.getSHoleCenterDistanceX();
        value[17] = data.getSHoleCenterDistanceY();
        value[18] = data.getMicHoleDiameter();
        value[19] = data.getMicHoleRoundness();
        value[20] = data.getMicHoleCenterDistanceX();
        value[21] = data.getMicHoleCenterDistanceY();

        String[] name = new String[22];
        name[0] = "外形长1";
        name[1] = "外形长2";
        name[2] = "外形长3";
        name[3] = "外形宽1";
        name[4] = "外形宽2";
        name[5] = "外形宽3";
        name[6] = "MI孔直径";
        name[7] = "MI孔真圆度";
        name[8] = "MI孔中心距X";
        name[9] = "MI孔中心距Y";
        name[10] = "DB孔直径";
        name[11] = "DB孔真圆度";
        name[12] = "DB孔中心距X";
        name[13] = "DB孔中心距Y";
        name[14] = "S孔直径";
        name[15] = "S孔真圆度";
        name[16] = "S孔中心距X";
        name[17] = "S孔中心距Y";
        name[18] = "MIC孔直径";
        name[19] = "MIC孔真圆度";
        name[20] = "MIC孔中心距X";
        name[21] = "MIC孔中心距Y";

        String[] nameResult = new String[10];
        Double[] valueResult = new Double[10];

        for (int i = 0; i < 10; i++) {
            Double max = -1d;
            int maxIndex = -1;
            for (int j = 0; j < 22; j++) {
                if (value[j] > max) {
                    max = value[j];
                    maxIndex = j;
                }
            }
            nameResult[i] = name[maxIndex];
            valueResult[i] = value[maxIndex];
            value[maxIndex] = -1d;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("name", nameResult);
        result.put("value", valueResult);
        return new Result(200, "查询成功", result);
    }

}
