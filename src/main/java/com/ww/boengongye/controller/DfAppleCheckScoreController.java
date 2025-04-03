package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAppleCheckScore;
import com.ww.boengongye.entity.DfAppleCheckScore;
import com.ww.boengongye.service.DfAppleCheckScoreService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * APPLE每月稽查得分表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-10-23
 */
@Controller
@RequestMapping("/dfAppleCheckScore")
@ResponseBody
@CrossOrigin
@Api(tags = "APPLE每月稽查得分表")
public class DfAppleCheckScoreController {

    @Autowired
    private DfAppleCheckScoreService dfAppleCheckScoreService;

    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("获取APPLE每月稽查得分列表")
    public Result listBySearch(int page, int limit, String keywords){
        Page<DfAppleCheckScore> pages = new Page<>(page,limit);

        QueryWrapper<DfAppleCheckScore> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("data_type",keywords)
            );
        }
        IPage<DfAppleCheckScore> list = dfAppleCheckScoreService.page(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改APPLE每月稽查得分数据")
    public Result saveOrUpdate(@RequestBody DfAppleCheckScore datas){
        String checkTime = datas.getYear()+"-"+datas.getMonth();
        datas.setCheckTime(checkTime);
        if (datas.getId()!=null){
            if (dfAppleCheckScoreService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfAppleCheckScoreService.save(datas)){
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
    @ApiOperation("删除APPLE每月稽查得分数据")
    public Result delete(String id){
        if (dfAppleCheckScoreService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

}
