package com.ww.boengongye.controller;


import com.ww.boengongye.entity.AoiBatchSave;
import com.ww.boengongye.entity.DfAoiBatch;
import com.ww.boengongye.entity.DfAoiBatchDetail;
import com.ww.boengongye.entity.User;
import com.ww.boengongye.service.DfAoiBatchDetailService;
import com.ww.boengongye.service.DfAoiBatchService;
import com.ww.boengongye.utils.Base64Utils;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import smartbi.sdk.RemoteException;

/**
 * <p>
 * aoi批次 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
@Controller
@RequestMapping("/dfAoiBatch")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI批次")
public class DfAoiBatchController {

    @Autowired
    DfAoiBatchService dfAoiBatchService;

    @Autowired
    DfAoiBatchDetailService dfAoiBatchDetailService;


    @PostMapping(value = "/save")
    public Result save(@RequestBody AoiBatchSave datas) {
        if (dfAoiBatchService.save(datas.getBatch())) {
            for(DfAoiBatchDetail d:datas.getDetails()) {
                d.setBatchId(datas.getBatch().getId());
            }
            dfAoiBatchDetailService.saveBatch(datas.getDetails());
            return new Result(200, "保存成功");
        } else {
            return new Result(500, "保存失败");
        }

    }
}
