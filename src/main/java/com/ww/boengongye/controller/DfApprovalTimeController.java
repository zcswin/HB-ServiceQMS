package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfApprovalTime;
import com.ww.boengongye.service.DfApprovalTimeService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 审批时间配置表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-03
 */
@Controller
@RequestMapping("/dfApprovalTime")
@CrossOrigin
@ResponseBody
public class DfApprovalTimeController {
    
    @Autowired
    DfApprovalTimeService dfApprovalTimeService;
    
    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",dfApprovalTimeService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfApprovalTime datas) {

        if (null != datas.getId()) {
            if (dfApprovalTimeService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (dfApprovalTimeService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (dfApprovalTimeService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name) {
        Page<DfApprovalTime> pages = new Page<DfApprovalTime>(page, limit);
        QueryWrapper<DfApprovalTime> ew=new QueryWrapper<DfApprovalTime>();
        if(null!=name&&!name.equals("")) {
            ew.like("type", name);
        }

        ew.orderByDesc("create_time");
        IPage<DfApprovalTime> list=dfApprovalTimeService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

}
