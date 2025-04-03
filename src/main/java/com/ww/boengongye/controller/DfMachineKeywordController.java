package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfMachineKeyword;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 设备关键词 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Controller
@RequestMapping("/dfMachineKeyword")
@ResponseBody
@CrossOrigin
public class DfMachineKeywordController {
    @Autowired
    com.ww.boengongye.service.DfMachineKeywordService DfMachineKeywordService;

    private static final Logger logger = LoggerFactory.getLogger(DfMachineKeywordController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfMachineKeywordService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfMachineKeyword datas) {

        if (null != datas.getId()) {
            if (DfMachineKeywordService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfMachineKeywordService.save(datas)) {
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
        if (DfMachineKeywordService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String machineCode,String keywords,String factoryCode) {

        Page<DfMachineKeyword> pages = new Page<DfMachineKeyword>(page, limit);
        QueryWrapper<DfMachineKeyword> ew=new QueryWrapper<DfMachineKeyword>();
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.like("factory_code", machineCode);
        }
        if(null!=machineCode&&!machineCode.equals("")) {
            ew.like("machine_code", machineCode);
        }
        if(null!=keywords&&!keywords.equals("")) {
            ew.like("keywords", keywords);
        }
        ew.orderByDesc("create_time");
        IPage<DfMachineKeyword> list=DfMachineKeywordService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
}
