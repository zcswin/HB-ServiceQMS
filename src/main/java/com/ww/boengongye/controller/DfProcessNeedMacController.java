package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfProcessNeedMac;
import com.ww.boengongye.entity.DfProcessNeedRelationMachine;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 工序需求机台数量表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Controller
@RequestMapping("/dfProcessNeedMac")
@ResponseBody
@CrossOrigin
public class DfProcessNeedMacController {
    @Autowired
    com.ww.boengongye.service.DfProcessNeedMacService DfProcessNeedMacService;


    @Autowired
    com.ww.boengongye.service.DfProcessNeedRelationMachineService DfProcessNeedRelationMachineService;


    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfProcessNeedMacService.list());
    }

    @RequestMapping(value = "/listRelationMachine")
    public Object listRelationMachine(int id) {
        QueryWrapper<DfProcessNeedRelationMachine>qw=new QueryWrapper<>();
        qw.eq("parent_id",id);
        return new Result(200, "查询成功",DfProcessNeedRelationMachineService.list(qw));
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfProcessNeedMac datas) {

        if (null != datas.getId()) {
            if (DfProcessNeedMacService.updateById(datas)) {
                QueryWrapper<DfProcessNeedRelationMachine>qw=new QueryWrapper<>();
                qw.eq("parent_id",datas.getId());
                DfProcessNeedRelationMachineService.remove(qw);
                if(datas.getMachines().size()>0){
                    for(DfProcessNeedRelationMachine d:datas.getMachines()){
                        d.setParentId(datas.getId());
                    }
                    DfProcessNeedRelationMachineService.saveBatch(datas.getMachines());
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfProcessNeedMacService.save(datas)) {
                if(datas.getMachines().size()>0){
                    for(DfProcessNeedRelationMachine d:datas.getMachines()){
                        d.setParentId(datas.getId());
                    }
                    DfProcessNeedRelationMachineService.saveBatch(datas.getMachines());
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfProcessNeedMacService.removeById(id)) {
            QueryWrapper<DfProcessNeedRelationMachine>qw=new QueryWrapper<>();
            qw.eq("parent_id",id);
            DfProcessNeedRelationMachineService.remove(qw);
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String factoryCode,String process) {
        Page<DfProcessNeedMac> pages = new Page<DfProcessNeedMac>(page, limit);
        QueryWrapper<DfProcessNeedMac> ew=new QueryWrapper<DfProcessNeedMac>();

        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("pn.factory_code", factoryCode);
        }
        if(null!=process&&!process.equals("")) {
            ew.eq("pn.process_code", process);
        }

        ew.orderByDesc("pn.create_time");
        IPage<DfProcessNeedMac> list=DfProcessNeedMacService.listJoinIds(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
}
