package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfInventory;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 消耗品库存表
 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Controller
@RequestMapping("/dfInventory")
@ResponseBody
@CrossOrigin
public class DfInventoryController {
    @Autowired
    com.ww.boengongye.service.DfInventoryService DfInventoryService;

    private static final Logger logger = LoggerFactory.getLogger(DfInventoryController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfInventoryService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfInventory datas) {

        if (null != datas.getId()) {
            if (DfInventoryService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfInventoryService.save(datas)) {
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
        if (DfInventoryService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name,String factoryCode,String spareType) {
//        try {
        Page<DfInventory> pages = new Page<DfInventory>(page, limit);
        QueryWrapper<DfInventory> ew=new QueryWrapper<DfInventory>();
        if(null!=name&&!name.equals("")) {
            ew.like("i.name", name);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("i.factory_code", factoryCode);
        }
        if(null!=spareType&&!spareType.equals("")) {
            ew.eq("i.spare_type", spareType);
        }
        ew.orderByDesc("i.create_time");
        IPage<DfInventory> list=DfInventoryService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }
}
