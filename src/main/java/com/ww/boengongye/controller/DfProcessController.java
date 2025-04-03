package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 工序 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-19
 */
@Controller
@RequestMapping("/dfProcess")
@ResponseBody
@CrossOrigin
@Api(tags = "工序")
public class DfProcessController {
    private static final Logger logger = LoggerFactory.getLogger(DfProcessController.class);

    @Autowired
    com.ww.boengongye.service.DfProcessService DfProcessService;

    @GetMapping(value = "/listAll")
    public Result listAll() {
        QueryWrapper<DfProcess>qw=new QueryWrapper<>();
        qw.orderByAsc("sort");
        return new Result(
                0, "查询成功", DfProcessService.list());
    }

    @GetMapping(value = "/listByProject")
    public Result listByProject(String project) {
        QueryWrapper<DfProcess>qw=new QueryWrapper<>();
        qw.like("project",project);
        qw.orderByAsc("sort");
        return new Result(
                0, "查询成功", DfProcessService.list(qw));
    }

    @GetMapping(value = "/listByFloor")
    public Result listByFloor(String floor) {
        QueryWrapper<DfProcess>qw=new QueryWrapper<>();
        if(null!=floor&&!floor.equals("")&&!floor.equals("null")){
            qw.eq("floor",floor);
        }else{
//            qw.eq("floor","4F");
        }


        return new Result(
                0, "查询成功", DfProcessService.list(qw));
    }

    @GetMapping(value = "/listPosition")
    public Result listPosition() {
        QueryWrapper<DfProcess>qw=new QueryWrapper<>();
        qw.ne("x",0);

        return new Result(0, "查询成功", DfProcessService.list(qw));
    }

    @ApiOperation("查询工厂等信息")
    @GetMapping(value = "/listJoinOther")
    public Result listJoinOther(int page, int limit, Integer factoryId, Integer sectionId, Integer workstationId) {
        IPage<DfProcess> pages = new Page<>(page, limit);
        QueryWrapper<DfProcess> qw = new QueryWrapper<>();
        if (factoryId != null && !"".equals(factoryId)) qw.eq("fac.id", factoryId);
        if (sectionId != null && !"".equals(sectionId)) qw.eq("sec.id", sectionId);
        if (workstationId != null && !"".equals(workstationId)) qw.eq("sta.id", workstationId);

        qw.orderByDesc("pro.create_time");
        return new Result(0, "查询成功", DfProcessService.listJoinIds(pages, qw));
    }

    @GetMapping("/delete")
    @Transactional
    public Result delete(Integer id){
        boolean b = DfProcessService.removeById(id);
        if (b) {
            return Result.DELETE_SUCCESS;
        } else {
            return Result.DELETE_FAILED;
        }
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody(required = false) DfProcess dfProcess) {
        boolean b = DfProcessService.save(dfProcess);
        if (b) {
            return Result.INSERT_SUCCESS;
        } else {
            return Result.INSERT_FAILED;
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody(required = false) DfProcess dfProcess) {
        boolean b = DfProcessService.updateById(dfProcess);
        if (b) {
            return Result.UPDATE_SUCCESS;
        } else {
            return Result.UPDATE_FAILED;
        }
    }

    @GetMapping(value = "/listByRouting")
    public Object listByRouting(int id) {

        return new Result(0, "查询成功",DfProcessService.listByRouting(id));
    }
}
