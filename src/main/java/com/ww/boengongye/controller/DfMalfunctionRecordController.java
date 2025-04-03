package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfMalfunctionRecord;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 设备故障记录 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Controller
@RequestMapping("/dfMalfunctionRecord")
@ResponseBody
@CrossOrigin
public class DfMalfunctionRecordController {
    @Autowired
    com.ww.boengongye.service.DfMalfunctionRecordService DfMalfunctionRecordService;

    private static final Logger logger = LoggerFactory.getLogger(DfMalfunctionRecordController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfMalfunctionRecordService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfMalfunctionRecord datas) {

        if (null != datas.getId()) {
            if (DfMalfunctionRecordService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfMalfunctionRecordService.save(datas)) {
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
        if (DfMalfunctionRecordService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String malfunctionType,String factoryCode,String machineCode) {
//        try {
        Page<DfMalfunctionRecord> pages = new Page<DfMalfunctionRecord>(page, limit);
        QueryWrapper<DfMalfunctionRecord> ew=new QueryWrapper<DfMalfunctionRecord>();
        if(null!=machineCode&&!machineCode.equals("")) {
            ew.like("m.machine_code", machineCode);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("m.factory_code", factoryCode);
        }

        if(null!=malfunctionType&&!malfunctionType.equals("")) {
            ew.like("m.malfunction_type", malfunctionType);
        }
        ew.orderByDesc("m.create_time");
        IPage<DfMalfunctionRecord> list=DfMalfunctionRecordService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }
}
