package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfFlowBlock;
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
 * 审核流程块 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-20
 */
@Controller
@RequestMapping("/dfFlowBlock")
@ResponseBody
@CrossOrigin
public class DfFlowBlockController {
    @Autowired
    com.ww.boengongye.service.DfFlowBlockService DfFlowBlockService;

    private static final Logger logger = LoggerFactory.getLogger(DfFlowBlockController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfFlowBlockService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfFlowBlock datas) {
//        try {
        Field[] f= DfFlowBlock.class.getDeclaredFields();
        //给TAnnals对象赋值
        for(int i=0;i<f.length;i++){
            //获取属相名
            String attributeName=f[i].getName();
            //将属性名的首字母变为大写，为执行set/get方法做准备
            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
            try{
                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
                Method setMethod= DfFlowBlock.class.getMethod("set"+methodName,String.class);
                if(!methodName.equals("IntroductionLand")){
                    //执行该set方法
                    setMethod.invoke(datas, XXSFilter.checkStr(XXSFilter.getFieldValueByFieldName(attributeName,datas)));
                }

            }catch (NoSuchMethodException e) {
                logger.error("接口异常", e);
            } catch (IllegalAccessException e) {
                logger.error("接口异常", e);
            } catch (InvocationTargetException e) {
                logger.error("接口异常", e);
            }
        }
        if (null != datas.getId()) {
            if (DfFlowBlockService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfFlowBlockService.save(datas)) {
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
        if (DfFlowBlockService.removeById(id)) {
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
        Page<DfFlowBlock> pages = new Page<DfFlowBlock>(page, limit);
        QueryWrapper<DfFlowBlock> ew=new QueryWrapper<DfFlowBlock>();
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
        IPage<DfFlowBlock> list=DfFlowBlockService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


}
