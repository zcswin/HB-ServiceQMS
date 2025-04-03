package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProject;
import com.ww.boengongye.entity.DfRework;
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
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Controller
@RequestMapping("/dfProject")
@ResponseBody
@CrossOrigin
@Api(tags = "项目")
public class DfProjectController {
    private static final Logger logger = LoggerFactory.getLogger(DfProjectController.class);

    @Autowired
    com.ww.boengongye.service.DfProjectService DfProjectService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfProjectService.list());
    }

    @RequestMapping(value = "/listByFactory")
    public Object listByFactory(String factoryCode,String floor) {
        QueryWrapper<DfProject>qw=new QueryWrapper<>();

        if(null!=factoryCode&&!factoryCode.equals("")&&!factoryCode.equals("null")){
            qw.eq("factory_code",factoryCode);
        }
        if(null!=floor&&!floor.equals("")&&!floor.equals("null")){
            qw.like("floor",floor);
        }
        return new Result(0, "查询成功",DfProjectService.list(qw));
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfProject datas) {

        if (null != datas.getId()) {
            if (DfProjectService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfProjectService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfProjectService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String factoryCode,String line) {
        Page<DfProject> pages = new Page<DfProject>(page, limit);
        QueryWrapper<DfProject> ew=new QueryWrapper<DfProject>();

        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("r.factory_code", factoryCode);
        }
        if(null!=line&&!line.equals("")) {
            ew.eq("r.line_code", line);
        }

        ew.orderByDesc("r.create_time");
        IPage<DfProject> list=DfProjectService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

    @GetMapping("getAllProjectList")
    @ApiOperation("项目下拉列表")
    public Result getAllProjectList(){
        List<DfProject> list = DfProjectService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取项目下拉列表失败");
        }
        return new Result(200,"获取项目下拉列表成功",list);
    }
}
