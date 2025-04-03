package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfWorkstation;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 工站 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Controller
@RequestMapping("/dfWorkstation")
@ResponseBody
@CrossOrigin
@Api(tags = "工站")
public class DfWorkstationController {
    @Autowired
    com.ww.boengongye.service.DfWorkstationService DfWorkstationService;

    private static final Logger logger = LoggerFactory.getLogger(DfWorkstationController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfWorkstationService.list());
    }

    @RequestMapping(value = "/listByCraftId")
    public Object listByCraftId(int id) {
        QueryWrapper<DfWorkstation> qw=new QueryWrapper<>();
        qw.eq("craft_id",id);
        return new Result(0, "查询成功",DfWorkstationService.list(qw));
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfWorkstation datas) {

        if (null != datas.getId()) {
            if (DfWorkstationService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfWorkstationService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (DfWorkstationService.removeById(id)) {
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
        Page<DfWorkstation> pages = new Page<DfWorkstation>(page, limit);
        QueryWrapper<DfWorkstation> ew=new QueryWrapper<DfWorkstation>();
        if(null!=name&&!name.equals("")) {
            ew.like("p.name", name);
        }
        if(null!=factoryId&&!factoryId.equals("")) {
            ew.eq("f.id", factoryId);
        }
        if(null!=lineBodyId&&!lineBodyId.equals("")) {
            ew.eq("l.id", lineBodyId);
        }
        ew.orderByDesc("p.create_time");
        IPage<DfWorkstation> list=DfWorkstationService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }
}
