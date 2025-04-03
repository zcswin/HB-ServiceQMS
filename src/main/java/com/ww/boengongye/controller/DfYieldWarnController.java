package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.entity.DfYieldWarn;
import com.ww.boengongye.service.DfYieldWarnService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 良率配置表  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-06
 */
@Controller
@RequestMapping("/dfYieldWarn")
@ResponseBody
@CrossOrigin
@Api(tags = "良率配置表")
public class DfYieldWarnController {
    @Autowired
    private DfYieldWarnService dfYieldWarnService;


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("获取良率配置列表")
    public Result listBySearch(int page,int limit,String keywords){
        Page<DfYieldWarn> pages = new Page<>(page,limit);

        QueryWrapper<DfYieldWarn> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("process",keywords)
                    .or().like("`type`",keywords)
                    .or().like("name",keywords)
            );
        }
        IPage<DfYieldWarn> list = dfYieldWarnService.page(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改良率配置数据")
    public Result saveOrUpdate(@RequestBody DfYieldWarn datas){
        if (datas.getId()!=null){
            if (dfYieldWarnService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfYieldWarnService.save(datas)){
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
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ApiOperation("删除良率配置信息")
    public Result delete(String id){
        if (dfYieldWarnService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }
}
