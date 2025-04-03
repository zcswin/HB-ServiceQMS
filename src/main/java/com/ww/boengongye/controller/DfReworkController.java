package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfRework;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 返工表 前端控制器 
 * </p>
 *
 * @author zhao
 * @since 2022-12-19
 */
@Controller
@RequestMapping("/dfRework")
@ResponseBody
@CrossOrigin
public class DfReworkController {
    @Autowired
    com.ww.boengongye.service.DfReworkService DfReworkService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfReworkService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfRework datas) {

        if (null != datas.getId()) {
            if (DfReworkService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfReworkService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfReworkService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String factoryCode,String process) {
        Page<DfRework> pages = new Page<DfRework>(page, limit);
        QueryWrapper<DfRework> ew=new QueryWrapper<DfRework>();

        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("r.factory_code", factoryCode);
        }
        if(null!=process&&!process.equals("")) {
            ew.eq("r.process", process);
        }

        ew.orderByDesc("r.create_time");
        IPage<DfRework> list=DfReworkService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
}
