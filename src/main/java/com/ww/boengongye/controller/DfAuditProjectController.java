package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAuditProject;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.XXSFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * 稽核项目 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-22
 */
@Controller
@RequestMapping("/dfAuditProject")
@ResponseBody
@CrossOrigin
public class DfAuditProjectController {
    @Autowired
    com.ww.boengongye.service.DfAuditProjectService DfAuditProjectService;

    private static final Logger logger = LoggerFactory.getLogger(DfAuditProjectController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfAuditProjectService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfAuditProject datas) {

        if (null != datas.getId()) {
            if (DfAuditProjectService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfAuditProjectService.save(datas)) {
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
        if (DfAuditProjectService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name,String factoryId,String lineBodyId) {
//        try {
        Page<DfAuditProject> pages = new Page<DfAuditProject>(page, limit);
        QueryWrapper<DfAuditProject> ew=new QueryWrapper<DfAuditProject>();
        if(null!=name&&!name.equals("")) {
            ew.like("p.name", name);
        }
        if(null!=factoryId&&!factoryId.equals("")) {
            ew.eq("f.id", factoryId);
        }
        if(null!=lineBodyId&&!lineBodyId.equals("")) {
            ew.eq("l.id", lineBodyId);
        }
        ew.orderByDesc("create_time");
        IPage<DfAuditProject> list=DfAuditProjectService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

}
