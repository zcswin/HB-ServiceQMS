package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfInventory;
import com.ww.boengongye.entity.DfInventoryUse;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

/**
 * <p>
 * 库存使用记录 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Controller
@RequestMapping("/dfInventoryUse")
@ResponseBody
@CrossOrigin
public class DfInventoryUseController {
    @Autowired
    com.ww.boengongye.service.DfInventoryUseService DfInventoryUseService;
    @Autowired
    com.ww.boengongye.service.DfInventoryService DfInventoryService;

    private static final Logger logger = LoggerFactory.getLogger(DfInventoryUseController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfInventoryUseService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfInventoryUse datas) {

        if (null != datas.getId()) {
            if (DfInventoryUseService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (DfInventoryUseService.save(datas)) {
                DfInventory di=DfInventoryService.getById(datas.getParentId());

                BigDecimal num1 = new BigDecimal(datas.getQuantity());
                BigDecimal num2 = new BigDecimal(di.getQuantity());

                // num2 - num1
                BigDecimal subtract = num2.subtract(num1);
                di.setQuantity(subtract.doubleValue());
                DfInventoryService.updateById(di);

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfInventoryUseService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String parentId,String factoryId,String dateUsed) {
        Page<DfInventoryUse> pages = new Page<DfInventoryUse>(page, limit);
        QueryWrapper<DfInventoryUse> ew=new QueryWrapper<DfInventoryUse>();
        if(null!=parentId&&!parentId.equals("")) {
            ew.eq("parent_id", parentId);
        }
        if(null!=dateUsed&&!dateUsed.equals("")) {
            ew.eq("date_used", dateUsed);
        }

        ew.orderByDesc("create_time");
        IPage<DfInventoryUse> list=DfInventoryUseService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
}
