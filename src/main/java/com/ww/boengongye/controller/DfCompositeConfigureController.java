package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfCompositeConfigure;
import com.ww.boengongye.entity.DfCompositeConfigure;
import com.ww.boengongye.service.DfCompositeConfigureService;
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

/**
 * <p>
 * 综合配置 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
@Controller
@RequestMapping("/dfCompositeConfigure")
@ResponseBody
@CrossOrigin
@Api(tags = "综合配置")
public class DfCompositeConfigureController {
    private static final Logger logger = LoggerFactory.getLogger(DfCompositeConfigureController.class);

    @Autowired
    DfCompositeConfigureService dfCompositeConfigureService;


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch")
    public Result listBySearch(int page, int limit, String keywords){
        Page<DfCompositeConfigure> pages = new Page<>(page,limit);

        QueryWrapper<DfCompositeConfigure> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("dcc.name",keywords)
                    .or().like("dcc.content",keywords)
            );
        }

        IPage<DfCompositeConfigure> list = dfCompositeConfigureService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DfCompositeConfigure datas){
        if (datas.getId()!=null){
            if (dfCompositeConfigureService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfCompositeConfigureService.save(datas)){
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
        if (dfCompositeConfigureService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }
}
