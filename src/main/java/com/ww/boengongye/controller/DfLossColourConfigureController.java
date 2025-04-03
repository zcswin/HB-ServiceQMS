package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLossColourConfigure;
import com.ww.boengongye.service.DfLossColourConfigureService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Wrapper;

/**
 * <p>
 * 漏检率颜色配置 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
@Controller
@RequestMapping("/dfLossColourConfigure")
@ResponseBody
@CrossOrigin
@Api(tags = "漏检率颜色配置")
public class DfLossColourConfigureController {

    private static final Logger logger = LoggerFactory.getLogger(DfCompositeConfigureController.class);

    @Autowired
    DfLossColourConfigureService dfLossColourConfigureService;


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch")
    public Result listBySearch(int page, int limit, String keywords){
        Page<DfLossColourConfigure> pages = new Page<>(page,limit);

        QueryWrapper<DfLossColourConfigure> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("dlcc.name",keywords)
                    .or().like("dlcc.colour",keywords)
            );
        }

        IPage<DfLossColourConfigure> list = dfLossColourConfigureService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DfLossColourConfigure datas){
        if (datas.getId()!=null){
            if (dfLossColourConfigureService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfLossColourConfigureService.save(datas)){
                return new Result(200,"添加成功");
            }
            return new Result(500,"添加失败");
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    public Result delete(String id){
        if (dfLossColourConfigureService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

}
