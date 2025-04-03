package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfFlowBlockService;
import com.ww.boengongye.service.DfFlowNextLevelService;
import com.ww.boengongye.service.DfFlowRelationUserService;
import com.ww.boengongye.service.DfFlowService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.XXSFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-12-09
 */
@Controller
@RequestMapping("/dfFlow")
@ResponseBody
@CrossOrigin
@Api(tags = "流程")
public class DfFlowController {

    @Autowired
    DfFlowService DfFlowService;

    @Autowired
    DfFlowNextLevelService DfFlowNextLevelService;

    @Autowired
    DfFlowRelationUserService DfFlowRelationUserService;
    @Autowired
    DfFlowBlockService DfFlowBlockService;



    @ApiOperation(value = "保存或更新流程数据(数据flow的id不为空时更新)")
    @PostMapping(value = "/saveOrUpdate")
    public Result save(@RequestBody FlowAllData datas) {

        if (null != datas.getFlow()&&null != datas.getFlow().getId()) {
            if (DfFlowService.updateById(datas.getFlow())) {
                List<DfFlowNextLevel> nlList=new ArrayList<>();
                    if(datas.getEdges().size()>0){
                        for(FlowEdges fe:datas.getEdges()){
                            DfFlowNextLevel nl=new DfFlowNextLevel();
                            nl.setParentId(fe.getSourceNodeId());
                            nl.setNextLevel(fe.getTargetNodeId());
                            nl.setFlowId(datas.getFlow().getId());
                            nlList.add(nl);
                        }

                    }
                    List<DfFlowBlock> blList=new ArrayList<>();
                    if(datas.getNodes().size()>0){
                        for(FlowNodes fe:datas.getNodes()){
                            DfFlowBlock bl=new DfFlowBlock();
                            bl.setParentId(datas.getFlow().getId());
                            bl.setFlowId(fe.getId());
                            bl.setName(fe.getText().getValue());
                            blList.add(bl);
                        }
                    }
                QueryWrapper<DfFlowNextLevel>qw=new QueryWrapper<>();
                qw.eq("flow_id",datas.getFlow().getId());
                DfFlowNextLevelService.remove(qw);
                DfFlowNextLevelService.saveBatch(nlList);

                QueryWrapper<DfFlowBlock>qw2=new QueryWrapper<>();
                qw2.eq("parent_id",datas.getFlow().getId());
                DfFlowBlockService.remove(qw2);
                DfFlowBlockService.saveBatch(blList);

                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (DfFlowService.save(datas.getFlow())) {
                List<DfFlowNextLevel> nlList=new ArrayList<>();
                if(datas.getEdges().size()>0){

                    for(FlowEdges fe:datas.getEdges()){
                        DfFlowNextLevel nl=new DfFlowNextLevel();
                        nl.setParentId(fe.getSourceNodeId());
                        nl.setNextLevel(fe.getTargetNodeId());
                        nl.setFlowId(datas.getFlow().getId());
                        nlList.add(nl);
                    }

                }
                List<DfFlowBlock> blList=new ArrayList<>();
                if(datas.getNodes().size()>0){
                    for(FlowNodes fe:datas.getNodes()){
                        DfFlowBlock bl=new DfFlowBlock();
                        bl.setParentId(datas.getFlow().getId());
                        bl.setFlowId(fe.getId());
                        bl.setName(fe.getText().getValue());
                        blList.add(bl);
                    }
                }
                QueryWrapper<DfFlowNextLevel>qw=new QueryWrapper<>();
                qw.eq("flow_id",datas.getFlow().getId());
                DfFlowNextLevelService.remove(qw);
                DfFlowNextLevelService.saveBatch(nlList);

                QueryWrapper<DfFlowBlock>qw2=new QueryWrapper<>();
                qw2.eq("parent_id",datas.getFlow().getId());
                DfFlowBlockService.remove(qw2);
                DfFlowBlockService.saveBatch(blList);
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }
    }


    @ApiOperation(value = "保存流程节点关联的用户")
    @PostMapping(value = "/saveRelationUser")
    public Result saveRelationUser(@RequestBody FlowBlockUser datas) {

            if(datas.getUserIds().size()>0){
                List<DfFlowRelationUser>usList=new ArrayList<>();
                for(User u:datas.getUserIds()){
                    DfFlowRelationUser user=new DfFlowRelationUser();
                    user.setFlowId(datas.getBlockId());
                    user.setUserId(u.getId());
                    user.setUserName(u.getName());
                    usList.add(user);
                }
                QueryWrapper<DfFlowRelationUser> qw=new QueryWrapper<>();
                qw.eq("flow_id",datas.getBlockId());
                DfFlowRelationUserService.remove(qw);
                if(DfFlowRelationUserService.saveBatch(usList)){
                    return new Result(200, "保存成功");
                }

            }
                return new Result(500, "保存失败");

    }

    @ApiOperation(value = "根据节点id获取已关联的用户")
    @GetMapping(value = "/listUserByFlowId")
    public Result listUserByFlowId(String id) {
        QueryWrapper<DfFlowRelationUser>qw=new QueryWrapper();
        qw.eq("flow_id",id);
        return new Result(200, "查询成功",DfFlowRelationUserService.list(qw));
    }

    @ApiOperation(value = "根据id获取流程数据")
    @GetMapping(value = "/getById")
    public Result getById(int id) {
        return new Result(200, "查询成功",DfFlowService.getById(id));
    }

    @ApiOperation(value = "根据id删除流程数据")
    @GetMapping(value = "/delete")
    public Result delete(String id) {
        UpdateWrapper<DfFlow>uw=new UpdateWrapper<>();
        uw.set("delete_flag",1);
        if (DfFlowService.update(uw)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }


    @ApiOperation(value = "分页获取流程数据")
    @GetMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name,String type) {
        Page<DfFlow> pages = new Page<DfFlow>(page, limit);
        QueryWrapper<DfFlow> ew=new QueryWrapper<DfFlow>();
        if(null!=name&&!name.equals("")) {
            ew.like("name", name);
        }
        if(null!=type&&!type.equals("")) {
            ew.like("type", type);
        }
        ew.eq("delete_flag", 0);
        ew.orderByDesc("create_time");
        ew.select("id","name","type","createTime","createName");
        IPage<DfFlow> list=DfFlowService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }

}
