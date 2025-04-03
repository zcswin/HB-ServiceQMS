package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 设备状态校准记录 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-17
 */
@Controller
@RequestMapping("/dfDeviceStatusCalibration")
@ResponseBody
@CrossOrigin
public class DfDeviceStatusCalibrationController {
    @Autowired
    com.ww.boengongye.service.DfDeviceStatusCalibrationService DfDeviceStatusCalibrationService;

    private static final Logger logger = LoggerFactory.getLogger(DfDeviceStatusCalibrationController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfDeviceStatusCalibrationService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfDeviceStatusCalibration datas) {

        if (null != datas.getId()) {
            if (DfDeviceStatusCalibrationService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfDeviceStatusCalibrationService.save(datas)) {
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


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfDeviceStatusCalibrationService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String endStatus,String factoryCode,String machineCode) {
//        try {
        Page<DfDeviceStatusCalibration> pages = new Page<DfDeviceStatusCalibration>(page, limit);
        QueryWrapper<DfDeviceStatusCalibration> ew=new QueryWrapper<DfDeviceStatusCalibration>();
        if(null!=machineCode&&!machineCode.equals("")) {
            ew.like("d.machine_code", machineCode);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("d.factory_code", factoryCode);
        }

        if(null!=endStatus&&!endStatus.equals("")) {
            ew.like("d.end_status", endStatus);
        }
        ew.orderByDesc("d.create_time");
        IPage<DfDeviceStatusCalibration> list=DfDeviceStatusCalibrationService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }
}
