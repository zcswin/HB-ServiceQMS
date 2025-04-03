package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfECodeProtect;
import com.ww.boengongye.service.DfECodeProtectService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * E-Code维护 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Controller
@RequestMapping("/dfEcodeProtect")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI E-Code维护")
public class DfECodeProtectController {

    private static final Logger logger = LoggerFactory.getLogger(DfECodeProtectController.class);

    @Autowired
    DfECodeProtectService dfECodeProtectService;


    /**
     * 关键字查询E-Code
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @ApiOperation("获取E-code维护列表")
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    public Result listBySearch(int page, int limit,String keywords){
        Page<DfECodeProtect> pages = new Page<>(page,limit);

        QueryWrapper<DfECodeProtect> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper
                    .like("decp.series",keywords)
                    .or().like("dp.name",keywords)
                    .or().like("decp.colour",keywords)
            );
            ew.orderByDesc("create_time");
        }

        IPage<DfECodeProtect> list = dfECodeProtectService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或者修改
     *
     * @param datas
     * @return
     */
    @ApiOperation("添加或者修改E-code维护数据")
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    public Result saveOrUpdate(@RequestBody DfECodeProtect datas) {
        if (datas.getId() != null) {
            if (dfECodeProtectService.updateById(datas)) {
                return new Result(200, "修改成功");
            }
            return new Result(500, "修改失败");
        } else {
            if (dfECodeProtectService.save(datas)) {
                return new Result(200, "添加成功");
            }
            return new Result(500, "添加失败");
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation("删除E-code维护数据")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(String id) {
        if (dfECodeProtectService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }
}
