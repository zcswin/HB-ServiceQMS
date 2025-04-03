package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.entity.DfFactory;
import com.ww.boengongye.entity.LineBody;
import com.ww.boengongye.service.DfAoiCarProtectService;
import com.ww.boengongye.service.DfFactoryService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
@Controller
@RequestMapping("/lineBody")
@ResponseBody
@CrossOrigin
@Api(tags = "线体")
public class LineBodyController {
    @Autowired
    com.ww.boengongye.service.LineBodyService LineBodyService;

    @Autowired
    private DfAoiCarProtectService dfAoiCarProtectService;

    @Autowired
    private DfFactoryService dfFactoryService;

    private static final Logger logger = LoggerFactory.getLogger(LineBodyController.class);

    @ApiOperation("获取全部线体-大屏搜索类界面使用")
    @GetMapping(value = "/listAll")
    public Object listAll() {
        QueryWrapper<LineBody>qw=new QueryWrapper<>();
//        qw.eq("is_use",)
        qw.orderByAsc("sort");
        return new Result(0, "查询成功",LineBodyService.list(qw));
    }

    @ApiOperation("获取线体-大屏搜索类界面使用")
    @GetMapping(value = "/listByCondition")
    public Object listByCondition(String factoryCode,String project,String process,String floor) {
        QueryWrapper<LineBody>qw=new QueryWrapper<>();
//        qw.eq("is_use",)
        if(null!=factoryCode&&!factoryCode.equals("")&&!factoryCode.equals("null")){
            qw.eq("factory_code",factoryCode);
        }
        if(null!=project&&!project.equals("")&&!project.equals("null")){
            qw.eq("project",project);
        }
        if(null!=process&&!process.equals("")&&!process.equals("null")){
            qw.like("process",process);
        }
        if(null!=floor&&!floor.equals("")&&!floor.equals("null")){
            qw.like("floor",floor);
        }
        qw.orderByAsc("sort");
        return new Result(0, "查询成功",LineBodyService.list(qw));
    }

    @ApiOperation("获取全部线体-录入类界面使用")
    @GetMapping(value = "/listByEnter")
    public Object listByEnter() {
        QueryWrapper<LineBody>qw=new QueryWrapper<>();
        qw.eq("is_use",1);
        qw.orderByAsc("sort");
        return new Result(0, "查询成功",LineBodyService.list(qw));
    }

    @GetMapping(value = "/listBySeach")
    public Object listBySeach(String factory,String project,String process) {
        QueryWrapper<LineBody>qw=new QueryWrapper<>();
        if(null!=factory&&!factory.equals("")){
            qw.eq("factory_code",factory);
        }


        return new Result(0, "查询成功",LineBodyService.list(qw));
    }


    @PostMapping(value = "/saveOrUpdate")
    @ApiOperation("添加或修改")
    public Result save(@RequestBody LineBody datas) {
        if (datas.getWorkshop()!=null){
            QueryWrapper<DfAoiCarProtect> carProtectWrapper = new QueryWrapper<>();
            carProtectWrapper
                    .eq("workshop",datas.getWorkshop())
                    .last("limit 1");
            DfAoiCarProtect dfAoiCarProtect = dfAoiCarProtectService.getOne(carProtectWrapper);
            DfFactory dfFactory = dfFactoryService.getById(dfAoiCarProtect.getFactoryId());
            datas.setFactoryCode(dfFactory.getFactoryCode());
        }

        if (null != datas.getId()) {
            if (LineBodyService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (LineBodyService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @GetMapping(value = "/delete")
    @ApiOperation("删除")
    public Result delete(String id) {
        if (LineBodyService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String factoryCode,String name) {
        Page<LineBody> pages = new Page<LineBody>(page, limit);
        QueryWrapper<LineBody> ew=new QueryWrapper<LineBody>();

        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("r.factory_code", factoryCode);
        }

        if(null!=name&&!name.equals("")) {
            ew.like("r.name", name);
        }
        ew.orderByDesc("r.create_time");
        IPage<LineBody> list=LineBodyService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("关键字查询线体维护列表")
    public Result listBySearch(int page, int limit, String keywords){
        Page<LineBody> pages = new Page<>(page,limit);

        QueryWrapper<LineBody> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.like("r.workshop",keywords)
                    .or().like("r.name",keywords)
                    .or().like("r.code",keywords);
        }

        IPage<LineBody> list = LineBodyService.listJoinIds(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }

}
