package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.service.DfAoiCarProtectService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 车间维护 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/dfAoiCarProtect")
@ResponseBody
@CrossOrigin
@Api(tags = "车间维护")
public class DfAoiCarProtectController {
    @Autowired
    DfAoiCarProtectService dfAoiCarProtectService;

    private static final Logger logger = LoggerFactory.getLogger(DfAoiCarProtectController.class);


    @RequestMapping(value = "/getAllWorkshopList",method = RequestMethod.GET)
    @ApiOperation("获取所有车间")
    public Result getAllWorkshopList(){
        List<DfAoiCarProtect> list = dfAoiCarProtectService.list();
        if (list==null||list.size()==0){
            return new Result(500,"没有车间数据");
        }
        return new Result(0,"查询成功",list);
    }


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("获取车间列表")
    public Result listBySearch(int page,int limit,String keywords){
        Page<DfAoiCarProtect> pages = new Page<>(page,limit);

        QueryWrapper<DfAoiCarProtect> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("dacp.workshop",keywords)
                    .or().like("dacp.park",keywords)
                    .or().like("dacp.crowd",keywords)
                    .or().like("dacp.category",keywords)
                    .or().like("dacp.workshop_code",keywords)
            );
        }
        IPage<DfAoiCarProtect> list = dfAoiCarProtectService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改车间数据")
    public Result saveOrUpdate(@RequestBody DfAoiCarProtect datas){
        if (datas.getId()!=null){
            if (dfAoiCarProtectService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfAoiCarProtectService.save(datas)){
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
    @ApiOperation("删除车间信息")
    public Result delete(String id){
        if (dfAoiCarProtectService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

}
