package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfQmsIpqcFlawConfig;
import com.ww.boengongye.service.DfQmsIpqcFlawConfigService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-29
 */
@Controller
@RequestMapping("/dfQmsIpqcFlawConfig")
@ResponseBody
@CrossOrigin
public class DfQmsIpqcFlawConfigController {

    @Autowired
    DfQmsIpqcFlawConfigService dfQmsIpqcFlawConfigService;


    public String testType;

    @RequestMapping(value = "/listDistinct")
    public Object listDistinct() {

        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();

        return new Result(0, "查询成功", dfQmsIpqcFlawConfigService.listDistinct(qw));
    }

    @RequestMapping(value = "/listByJoin")
    public Object listByJoin(String testType, String process, String project, String bigArea) {

        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();
        if (null != testType && !testType.equals("")) {
            qw.eq("f.test_type", testType);
        }
        if (null != process && !process.equals("")) {
            qw.eq("f.process", process);
        }
        if (null != project && !project.equals("")) {
            qw.eq("f.project", project);
        }
        if (null != bigArea && !bigArea.equals("")) {
            qw.eq("f.big_area", bigArea);
        }
        return new Result(0, "查询成功", dfQmsIpqcFlawConfigService.listByJoin(qw));
    }


    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", dfQmsIpqcFlawConfigService.list());
    }


    @ApiOperation("批量保存绑定关系")
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public Result batchSave(@RequestBody DfQmsIpqcFlawConfig datas) {

        List<DfQmsIpqcFlawConfig> all = new ArrayList<>();
        if (null != datas.getRmoveList()) {
            for (DfQmsIpqcFlawConfig d : datas.getRmoveList()) {

                d.setProcess(Arrays.stream(d.getProcess().split(","))
                        .filter(name -> !name.equals(datas.getProcess())) // 过滤掉要移除的名字
                        .collect(Collectors.joining(",")));
                all.add(d);
            }
        }

        if (null != datas.getSaveList()) {
            for (DfQmsIpqcFlawConfig d : datas.getSaveList()) {
                if (StringUtils.isNotEmpty(d.getProcess())) {
                    d.setProcess(d.getProcess() + "," + datas.getProcess());
                } else {
                    d.setProcess(datas.getProcess());
                }
                all.add(d);
            }
        }
        dfQmsIpqcFlawConfigService.saveOrUpdateBatch(all);
        return new Result(200, "保存成功");


    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfQmsIpqcFlawConfig datas) {
        QueryWrapper<DfQmsIpqcFlawConfig> ew = new QueryWrapper<>();
        ew.eq(StringUtils.isNotEmpty(datas.getTestType()), "test_type", datas.getTestType())
                .eq(StringUtils.isNotEmpty(datas.getProcess()), "process", datas.getProcess())
                .eq(StringUtils.isNotEmpty(datas.getProject()), "project", datas.getProject())
                .eq(StringUtils.isNotEmpty(datas.getBigArea()), "big_area", datas.getBigArea())
                .eq(StringUtils.isNotEmpty(datas.getFlawName()), "flaw_name", datas.getFlawName());
        List<DfQmsIpqcFlawConfig> list = dfQmsIpqcFlawConfigService.list(ew);
        if (!CollectionUtils.isEmpty(list)) {
            return new Result(500, "存在相同工序,项目,大区域,小区域的数据,不能重复添加");
        } else {
            if (dfQmsIpqcFlawConfigService.saveOrUpdate(datas)) {
                return new Result(200, "新增或修改成功");
            } else {
                return new Result(500, "新增或修改失败");
            }
        }
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (dfQmsIpqcFlawConfigService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String testType, String process, String project, String bigArea, String flawName) {
        Page<DfQmsIpqcFlawConfig> pages = new Page<DfQmsIpqcFlawConfig>(page, limit);
        QueryWrapper<DfQmsIpqcFlawConfig> ew = new QueryWrapper<DfQmsIpqcFlawConfig>();
        if (null != testType && !testType.equals("")) {
            ew.eq("test_type", testType);
        }

        if (null != process && !process.equals("")) {
            ew.eq("process", process);
        }

        if (null != project && !project.equals("")) {
            ew.eq("project", project);
        }

        if (null != bigArea && !bigArea.equals("")) {
            ew.eq("big_area", bigArea);
        }
        if (null != flawName && !flawName.equals("")) {
            ew.like("flaw_name", flawName);
        }
        ew.orderByDesc("create_time");
        IPage<DfQmsIpqcFlawConfig> list = dfQmsIpqcFlawConfigService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());

    }

    @PostMapping("/importExcel")
    public Result importExcel(MultipartFile file) throws Exception {
        int count = dfQmsIpqcFlawConfigService.importExcel(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    @ApiOperation("导入外观缺陷")
    @PostMapping("/importExcel2")
    public Result importExcel2(MultipartFile file) throws Exception {
        int count = dfQmsIpqcFlawConfigService.importOrder(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    @RequestMapping(value = "/listDistinctByArea")
    public Object listDistinctByArea() {

        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();
        return new Result(0, "查询成功", dfQmsIpqcFlawConfigService.listDistinctArea(qw));
    }

    @RequestMapping(value = "/listDistinctByProcess")
    public Object listDistinctByProcess(String process) {

        QueryWrapper<DfQmsIpqcFlawConfig> qw = new QueryWrapper<>();
        qw.eq("process", process);
        return new Result(0, "查询成功", dfQmsIpqcFlawConfigService.listDistinct(qw));
    }


}
