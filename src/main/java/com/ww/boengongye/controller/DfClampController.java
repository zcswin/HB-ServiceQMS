package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.entity.DfAoiPiece;
import com.ww.boengongye.entity.DfClamp;
import com.ww.boengongye.service.DfClampService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 夹具表 前端控制器
 * </p>
 *
 * @author liwei
 * @since 2024-05-27
 */
@RestController
@RequestMapping("/dfClamp")
@Api(tags = "夹具")
@CrossOrigin
public class DfClampController {

    @Autowired
    private DfClampService dfClampService;

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ApiOperation("查询夹具表")
    public Result getList(
            String project, String process, String machineCode,String lineBody
            ,String startDate,String endDate
            ,int page,int limit
    ) throws ParseException {
        Page<DfClamp> pages = new Page<>(page,limit);
        QueryWrapper<DfClamp> ew = new QueryWrapper<>();
        ew
                .eq(StringUtils.isNotEmpty(project), "project", project)
                .eq(StringUtils.isNotEmpty(process), "process", process)
                .eq(StringUtils.isNotEmpty(machineCode), "machine", machineCode)
                .eq(StringUtils.isNotEmpty(lineBody), "line_body", lineBody)
                .ge(StringUtils.isNotEmpty(startDate),"time",startDate + " 00:00:00")
                .le(StringUtils.isNotEmpty(endDate),"time",endDate + " 23:59:59");

        IPage<DfClamp> list = dfClampService.page(pages,ew);
        return new Result(200, "查询成功", list.getRecords(),(int)list.getTotal());
    }

    /**
     * 添加或修改
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation("新增修改夹具表")
    public Result saveOrUpdate(@RequestBody DfClamp data) {
        if (data.getId() != null) {
            if (dfClampService.updateById(data)) {
                return new Result(200, "修改成功");
            }
            return new Result(500, "修改失败");
        } else {
            if (dfClampService.save(data)) {
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
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiOperation("夹具表")
    public Result delete(String id) {
        if (dfClampService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }

}
