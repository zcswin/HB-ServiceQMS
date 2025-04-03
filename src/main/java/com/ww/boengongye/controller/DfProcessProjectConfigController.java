package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.entity.DfProcessProjectConfig;
import com.ww.boengongye.service.DfProcessProjectConfigService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-04-26
 */
@Controller
@RequestMapping("/dfProcessProjectConfig")
@ResponseBody
@CrossOrigin
@Api(tags = "项目工序")
public class DfProcessProjectConfigController {
    @Autowired
    DfProcessProjectConfigService DfProcessProjectConfigService;

    @RequestMapping(value = "/listAll")
    public Object listAll(String floor) {
        QueryWrapper<DfProcessProjectConfig>qw=new QueryWrapper<>();
        if(null!=floor&&!floor.equals("")&&!floor.equals("null")){
            qw.eq("floor",floor);
        }else{
            qw.eq("floor","4F");
        }

        return new Result(0, "查询成功",DfProcessProjectConfigService.list());
    }



    @RequestMapping(value = "/listByCondition")
    public Object listByCondition(String project,String type,String floor,String factoryCode) {
        List<DfProcessProjectConfig>datas=new ArrayList<>();
        QueryWrapper<DfProcessProjectConfig>qw=new QueryWrapper<>();
        QueryWrapper<DfProcessProjectConfig>qw2=new QueryWrapper<>();
//        if(null==project||project.equals("")&&null==type||type.equals("")){
//            datas=DfProcessProjectConfigService.listDistinct(qw);
//        }else if(null!=project&&!project.equals("")&&null==type||type.equals("")){
//            qw.eq("project",project);
//            datas=DfProcessProjectConfigService.listDistinct(qw);
//        }else if(null==project||project.equals("")&&null!=type&&!type.equals("")){
//            qw.like("type",type);
//            datas=DfProcessProjectConfigService.listDistinct(qw);
//        }else{
//            qw.eq("project",project);

        if(null!=factoryCode&&!factoryCode.equals("")&&!factoryCode.equals("null")){
            qw.eq("factory_code",factoryCode);
            qw2.eq("factory_code",factoryCode);
        }
        if(null!=floor&&!floor.equals("")&&!floor.equals("null")){
            qw.like("floor",floor);
        }
//        else{
//            qw.eq("floor","4F");
//        }

        qw.like("type",type);
        qw.orderByAsc("sort");
        if(null!=project&&!project.equals("")&&!project.equals("null")){
            qw.like("project",project);
            datas=DfProcessProjectConfigService.list(qw);
        }else{

            qw2.select("distinct(process_name) as process_name");
            datas=DfProcessProjectConfigService.list(qw2);
        }


//        }

        return new Result(0, "查询成功",datas);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfProcessProjectConfig datas) {

        if (null != datas.getId()) {
            if (DfProcessProjectConfigService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (DfProcessProjectConfigService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfProcessProjectConfigService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name) {
        Page<DfProcessProjectConfig> pages = new Page<DfProcessProjectConfig>(page, limit);
        QueryWrapper<DfProcessProjectConfig> ew=new QueryWrapper<DfProcessProjectConfig>();
        if(null!=name&&!name.equals("")) {
            ew.like("type", name);
        }

        ew.orderByDesc("create_time");
        IPage<DfProcessProjectConfig> list=DfProcessProjectConfigService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

}
