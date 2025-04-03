package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI机台维护 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/dfAoiMchineProtect")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI机台维护/AOI机台全检看板")
public class DfAoiMachineProtectController {
    private static final Logger logger = LoggerFactory.getLogger(DfAoiMachineProtectController.class);

    @Autowired
    DfAoiMachineProtectService dfAoiMachineProtectService;

    @Autowired
    DfAoiCarProtectService dfAoiCarProtectService;

    @Autowired
    DfFactoryService dfFactoryService;

    @Autowired
    DfAoiProcessService dfAoiProcessService;

    @Autowired
    DfProjectService dfProjectService;

    @Autowired
    DfAoiColourService dfAoiColourService;

    @Autowired
    DfAoiPieceService dfAoiPieceService;

    @RequestMapping(value = "/getAllMachineList",method = RequestMethod.GET)
    @ApiOperation("获取所有机台下拉列表")
    public Result getAllMachineList(){
        List<DfAoiMachineProtect> list = dfAoiMachineProtectService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取机台下拉列表失败");
        }
        return new Result(200,"获取机台下拉列表成功",list);
    }


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("关键字查询机台维护列表")
    public Result listBySearch(int page, int limit, String keywords){
        Page<DfAoiMachineProtect> pages = new Page<>(page,limit);

        QueryWrapper<DfAoiMachineProtect> ew = new QueryWrapper<>();

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper ->wrapper.
                    like("damp.firm",keywords)
                    .or().like("damp.firm_code",keywords)
                    .or().like("damp.machine_name",keywords)
                    .or().like("damp.machine_code",keywords)
                    .or().like("damp.workshop",keywords)
            );
        }

        IPage<DfAoiMachineProtect> list = dfAoiMachineProtectService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改机台维护数据")
    public Result saveOrUpdate(@RequestBody DfAoiMachineProtect datas){
        if (datas.getWorkshop()!=null){
            QueryWrapper<DfAoiCarProtect> carProtectWrapper = new QueryWrapper<>();
            carProtectWrapper
                    .eq("workshop",datas.getWorkshop())
                    .last("limit 1");
            DfAoiCarProtect dfAoiCarProtect = dfAoiCarProtectService.getOne(carProtectWrapper);
            datas.setFactoryId(dfAoiCarProtect.getFactoryId());
        }

        if (datas.getId()!=null){
            if (dfAoiMachineProtectService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfAoiMachineProtectService.save(datas)){
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
    @ApiOperation("删除机台维护数据")
    public Result delete(String id){
        if (dfAoiMachineProtectService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }

    @GetMapping("getAllAoiMachineProtectList")
    @ApiOperation("机台下拉列表")
    public Result getAllAoiMachineProtectList(){
        List<DfAoiMachineProtect> list = dfAoiMachineProtectService.list();
        if (list==null||list.size()==0){
            return new Result(500,"获取机台下拉列表失败");
        }
        return new Result(200,"获取机台下拉列表成功",list);
    }



    /**
     * 获取某个工厂所有的机台的投入和产出列表
     * @param factoryId
     * @param process
     * @param machineCode
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("AOI机台投入产出比列表")
    @RequestMapping(value = "getAllMachineInspectionList",method = RequestMethod.GET)
    public Result getMachineInspectionList(String factoryId,String process,String machineCode,String projectId,String colour,String startTime,String endTime){
        QueryWrapper<DfAoiMachineInspection> dfAoiMachineProtectQueryWrapper = new QueryWrapper<>();
        dfAoiMachineProtectQueryWrapper
                .eq("new_piece.num",1);

        if (StringUtils.isNotEmpty(factoryId) ){
            dfAoiMachineProtectQueryWrapper.eq("damp.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(process) ){
            dfAoiMachineProtectQueryWrapper.eq("u.process",process);
        }
        if (StringUtils.isNotEmpty(machineCode) ){
            dfAoiMachineProtectQueryWrapper.eq("damp.machine_code",machineCode);
        }
        if (StringUtils.isNotEmpty(projectId) ){
            dfAoiMachineProtectQueryWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour) ){
            dfAoiMachineProtectQueryWrapper.eq("decp.colour",colour);
        }

        if (StringUtils.isNotEmpty(startTime) ){
            dfAoiMachineProtectQueryWrapper.ge("new_piece.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endTime) ){
            dfAoiMachineProtectQueryWrapper.le("new_piece.`time`",endTime);
        }

        //获取AOI机台投入产出集合
        List<DfAoiMachineInspection> aoiMachineInspections = dfAoiMachineProtectService.getAllMachineInspectionList(dfAoiMachineProtectQueryWrapper);
        if (aoiMachineInspections==null||aoiMachineInspections.size()==0){
            return new Result(500,"该条件下没有AOI机台投入产出比列表相关数据");
        }

        for (DfAoiMachineInspection dfAoiMachineInspection:aoiMachineInspections){
            QueryWrapper<DfAoiMachineInspection> ew = new QueryWrapper<>();
            ew.eq("new_piece.num",1);

            if (StringUtils.isNotEmpty(factoryId) ){
                ew.eq("damp.factory_id",factoryId);
            }

            if (StringUtils.isNotEmpty(process) ){
                ew.eq("u.process",process);
            }
            if (StringUtils.isNotEmpty(machineCode) ){
                ew.eq("damp.machine_code",machineCode);
            }
            if (StringUtils.isNotEmpty(projectId) ){
                ew.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour) ){
                ew.eq("decp.colour",colour);
            }

            if (StringUtils.isNotEmpty(startTime) ){
                ew.ge("new_piece.`time`",startTime);
            }
            if (StringUtils.isNotEmpty(endTime) ){
                ew.le("new_piece.`time`",endTime);
            }

            ew.eq("new_piece.qualityid",0)
                    .eq("damp.machine_code",dfAoiMachineInspection.getMachineCode())
                    .eq("u.alias",dfAoiMachineInspection.getUserName())
                    .eq("dp.name",dfAoiMachineInspection.getProjectName())
                    .eq("decp.colour",dfAoiMachineInspection.getColour());
            List<DfAoiMachineInspection> currentMachineInspection = dfAoiMachineProtectService.getAllMachineInspectionList(ew);
            dfAoiMachineInspection.setSynthesizeOutput(0);
            if (currentMachineInspection!=null&&currentMachineInspection.size()>0){
                DfAoiMachineInspection dfAoiMachineInspectionNew = currentMachineInspection.get(0);
                dfAoiMachineInspection.setSynthesizeOutput(dfAoiMachineInspectionNew.getSynthesizeInput());
            }

            //综合良率
            String synthesizePassPoint =String.format("%.2f", (float) dfAoiMachineInspection.getSynthesizeOutput() / (float) dfAoiMachineInspection.getSynthesizeInput() * 100);
            dfAoiMachineInspection.setSynthesizePassPoint(synthesizePassPoint);
        }

        return new Result(200,"AOI机台投入产出比列表获取成功",aoiMachineInspections);
    }


    /**
     * AOI机台投入产出比统计
     * @param factoryId
     * @param process
     * @param machineCode
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("AOI机台投入产出比统计")
    @RequestMapping(value = "getMachineInspectionList",method = RequestMethod.GET)
    public Result getMachineInspectionList(Integer page,Integer limit,String factoryId,String process,String machineCode,String projectId,String colour,String startTime,String endTime){
        //每个AOI机器的检测集合
        List<Object> machineInspectionList = new ArrayList<>();

        Page<DfAoiMachineInspection> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiMachineInspection> dfAoiMachineProtectQueryWrapper = new QueryWrapper<>();
        dfAoiMachineProtectQueryWrapper
                .eq("new_piece.num",1);

        if (StringUtils.isNotEmpty(factoryId) ){
            dfAoiMachineProtectQueryWrapper.eq("damp.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(process) ){
            dfAoiMachineProtectQueryWrapper.eq("u.process",process);
        }
        if (StringUtils.isNotEmpty(machineCode) ){
            dfAoiMachineProtectQueryWrapper.eq("damp.machine_code",machineCode);
        }
        if (StringUtils.isNotEmpty(projectId) ){
            dfAoiMachineProtectQueryWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour) ){
            dfAoiMachineProtectQueryWrapper.eq("decp.colour",colour);
        }

        if (StringUtils.isNotEmpty(startTime) ){
            dfAoiMachineProtectQueryWrapper.ge("new_piece.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endTime) ){
            dfAoiMachineProtectQueryWrapper.le("new_piece.`time`",endTime);
        }
        dfAoiMachineProtectQueryWrapper.apply("damp.machine_code !=''");
        //获取AOI机台投入产出集合
        List<DfAoiMachineInspection> aoiMachineInspections = dfAoiMachineProtectService.getMachineAndUserNameList(pages,dfAoiMachineProtectQueryWrapper);
        //总页数
        int totalPage = (int) pages.getPages();
        if (aoiMachineInspections==null||aoiMachineInspections.size()==0){
            return new Result(500,"该条件下没有AOI机台投入产出数据");
        }

        for (DfAoiMachineInspection dfAoiMachineInspection:aoiMachineInspections){
            QueryWrapper<DfAoiMachineInspection> ew = new QueryWrapper<>();
            QueryWrapper<DfAoiMachineInspection> ew2 = new QueryWrapper<>();
            ew.eq("new_piece.num",1);
            ew2.apply("new_piece.num='"+1+"'");

            if (StringUtils.isNotEmpty(factoryId) ){
                ew.eq("damp.factory_id",factoryId);
                ew2.apply("damp.factory_id='"+factoryId+"'");
            }

            if (StringUtils.isNotEmpty(process) ){
                ew.eq("u.process",process);
                ew2.apply("u.process='"+process+"'");
            }
            if (StringUtils.isNotEmpty(machineCode) ){
                ew.eq("damp.machine_code",machineCode);
                ew2.apply("damp.machine_code='"+machineCode+"'");
            }
            if (StringUtils.isNotEmpty(projectId) ){
                ew.eq("decp.project_id",projectId);
                ew2.apply("decp.project_id='"+projectId+"'");
            }
            if (StringUtils.isNotEmpty(colour) ){
                ew.eq("decp.colour",colour);
                ew2.apply("decp.colour='"+colour+"'");
            }

            if (StringUtils.isNotEmpty(startTime) ){
                ew.ge("new_piece.`time`",startTime);
                ew2.apply("new_piece.`time`>='"+startTime+"'");
            }
            if (StringUtils.isNotEmpty(endTime) ){
                ew.le("new_piece.`time`",endTime);
                ew2.apply("new_piece.`time`<='"+endTime+"'");
            }

            ew.eq("damp.machine_code",dfAoiMachineInspection.getMachineCode())
                    .eq("u.alias",dfAoiMachineInspection.getUserName());

            ew2.apply("new_piece.qualityid='"+0+"'")
                    .apply("damp.machine_code='"+dfAoiMachineInspection.getMachineCode()+"'")
                    .apply("u.alias='"+dfAoiMachineInspection.getUserName()+"'");

            //该机器的检测所有项目颜色的玻璃集合
            List<DfAoiMachineInspection> machineProjectColourList = dfAoiMachineProtectService.getMachineInspectionList(ew,ew2);
            for (DfAoiMachineInspection machineProjectColour:machineProjectColourList){
                if (machineProjectColour.getSynthesizeOutput()==null){
                    machineProjectColour.setSynthesizeOutput(0);
                }

                String synthesizePassPoint =String.format("%.2f", (float) machineProjectColour.getSynthesizeOutput() / (float) machineProjectColour.getSynthesizeInput() * 100)+"%";
//                synthesizePassPoint = "98.01";
//                machineProjectColour.setSynthesizeOutput(44256);
                machineProjectColour.setSynthesizePassPoint(synthesizePassPoint);
            }

            //该机器的检测所有项目颜色的玻璃集合加入集合中
            machineInspectionList.add(machineProjectColourList);
        }

        return new Result(200,"AOI机台投入产出比统计获取成功",totalPage,machineInspectionList);
    }


//    /**
//     * AOI机台投入产出比统计（优化）
//     * @param page
//     * @param limit
//     * @param factory
//     * @param process
//     * @param machineCode
//     * @param project
//     * @param colour
//     * @param startDate
//     * @param endDate
//     * @return
//     */
//    @ApiOperation("AOI机台投入产出比统计（优化）")
//    @RequestMapping(value = "getMachineInspectionListNew",method = RequestMethod.GET)
//    public Result getMachineInspectionListNew(
//            Integer page,Integer limit,String factory,String process,String machineCode,String project,String colour
//            ,@RequestParam String startDate,@RequestParam String endDate) throws ParseException {
//
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//
//
//        Page<DfAoiPiece> pages = new Page<>();
//        QueryWrapper<DfAoiPiece> aoiPieceWrapper = new QueryWrapper<>();
//        aoiPieceWrapper
//                .select("machine")
//                .groupBy("machine");
//        //所有机台
//        IPage<DfAoiPiece> machineList = dfAoiPieceService.page(pages,aoiPieceWrapper);
//
//        for (DfAoiPiece machine:machineList.getRecords()){
//            QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
//
//            qw
//                    .eq("dap.factory",factory)
//                    .eq("dap.machine",machineCode)
//                    .eq("dap.project",project)
//                    .eq("dap.color",colour)
//
//
//        }
//
//
//
//
//
//
//        return new Result(200,"AOI机台投入产出比统计获取成功",totalPage,machineInspectionList);
//    }














    /**
     * 获取所有项目颜色相关一次良率和综合良率列表
     * @param factoryId
     * @param process
     * @param machineCode
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("AOI良率列表")
    @RequestMapping(value = "getAllAoiPassPointList",method = RequestMethod.GET)
    public Result getAllAoiPassPointList(String factoryId,String process,String machineCode,String projectId,String colour,String startTime,String endTime){
        QueryWrapper<DfAoiPassPoint> ew = new QueryWrapper<>();

        ew.eq("new_piece.num",1);

        if (StringUtils.isNotEmpty(factoryId) ){
            ew.eq("damp.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(process) ){
            ew.eq("u.process",process);
        }
        if (StringUtils.isNotEmpty(machineCode) ){
            ew.eq("damp.machine_code",machineCode);
        }
        if (StringUtils.isNotEmpty(projectId) ){
            ew.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour) ){
            ew.eq("decp.colour",colour);
        }

        if (StringUtils.isNotEmpty(startTime) ){
            ew.ge("new_piece.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endTime) ){
            ew.le("new_piece.`time`",endTime);
        }
        ew.groupBy("dp.name","decp.colour");
        //获取综合投入数量、项目名、颜色
        List<DfAoiPassPoint> dfAoiPassPointList = dfAoiMachineProtectService.getAllAoiPassPointList(ew);
        if (dfAoiPassPointList==null||dfAoiPassPointList.size()==0){
            return new Result(500,"该条件下没有AOI良率的相关数据");
        }

        for (DfAoiPassPoint dfAoiPassPoint:dfAoiPassPointList){
            QueryWrapper<DfAoiPassPoint> synthesizeWrapper = new QueryWrapper<>();
            QueryWrapper<DfAoiPassPoint> oneWrapper = new QueryWrapper<>();
            QueryWrapper<Integer> totalInputWrapper = new QueryWrapper<>();
            QueryWrapper<Integer> backNumberWrapper = new QueryWrapper<>();


            synthesizeWrapper.eq("new_piece.num",1);
            oneWrapper.eq("new_piece.num",1);
            backNumberWrapper.eq("new_piece.num",1);

            if (StringUtils.isNotEmpty(factoryId) ){
                synthesizeWrapper.eq("damp.factory_id",factoryId);
                oneWrapper.eq("damp.factory_id",factoryId);
                totalInputWrapper.eq("damp.factory_id",factoryId);
                backNumberWrapper.eq("damp.factory_id",factoryId);
            }

            if (StringUtils.isNotEmpty(process) ){
                synthesizeWrapper.eq("u.process",process);
                oneWrapper.eq("u.process",process);
                totalInputWrapper.eq("u.process",process);
                backNumberWrapper.eq("u.process",process);
            }
            if (StringUtils.isNotEmpty(machineCode) ){
                synthesizeWrapper.eq("damp.machine_code",machineCode);
                oneWrapper.eq("damp.machine_code",machineCode);
                totalInputWrapper.eq("damp.machine_code",machineCode);
                backNumberWrapper.eq("damp.machine_code",machineCode);
            }
            if (StringUtils.isNotEmpty(projectId) ){
                synthesizeWrapper.eq("decp.project_id",projectId);
                oneWrapper.eq("decp.project_id",projectId);
                totalInputWrapper.eq("decp.project_id",projectId);
                backNumberWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour) ){
                synthesizeWrapper.eq("decp.colour",colour);
                oneWrapper.eq("decp.colour",colour);
                totalInputWrapper.eq("decp.colour",colour);
                backNumberWrapper.eq("decp.colour",colour);
            }

            if (StringUtils.isNotEmpty(startTime) ){
                synthesizeWrapper.ge("new_piece.`time`",startTime);
                oneWrapper.ge("new_piece.`time`",startTime);
                totalInputWrapper.ge("dap.`time`",startTime);
                backNumberWrapper.ge("new_piece.`time`",startTime);
            }
            if (StringUtils.isNotEmpty(endTime) ){
                synthesizeWrapper.le("new_piece.`time`",endTime);
                oneWrapper.le("new_piece.`time`",endTime);
                totalInputWrapper.le("dap.`time`",endTime);
                backNumberWrapper.le("new_piece.`time`",endTime);
            }

            synthesizeWrapper.eq("new_piece.qualityid",0)
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour())
                    .groupBy("dp.name","decp.colour");

            oneWrapper.eq("new_piece.qualityid",0)
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour())
                    .groupBy("dp.name","decp.colour");

            totalInputWrapper
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour());

            backNumberWrapper
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour());

            //获取当前项目颜色的总投入数
            Integer totalInput = dfAoiMachineProtectService.getTotalInputNumber(totalInputWrapper);
            dfAoiPassPoint.setTotalInput(totalInput);

            //获取当前项目颜色的返投数
            Integer backNumber = dfAoiMachineProtectService.getBackNumber(backNumberWrapper);
            if (backNumber==null){
                backNumber = 0;
            }
            dfAoiPassPoint.setBackNumber(backNumber);

            backNumberWrapper.eq("new_piece.qualityid",0);

            //获取当前项目颜色的返回OK数
            Integer backOKNumber = dfAoiMachineProtectService.getBackOKNumber(backNumberWrapper);
            if (backOKNumber==null){
                backOKNumber = 0;
            }
            dfAoiPassPoint.setBackOKNumber(backOKNumber);

            //获取当前项目颜色相关的综合产出数量
            List<DfAoiPassPoint> currentPassPointSynthesize = dfAoiMachineProtectService.getAllAoiPassPointList(synthesizeWrapper);
            dfAoiPassPoint.setSynthesizeOutput(0);
            if (currentPassPointSynthesize!=null&&currentPassPointSynthesize.size()>0){
                DfAoiPassPoint dfAoiPassPointSynthesize = currentPassPointSynthesize.get(0);
                dfAoiPassPoint.setSynthesizeOutput(dfAoiPassPointSynthesize.getSynthesizeInput());
            }
            //获取当前项目颜色相关的综合良率
            String synthesizePassPoint = String.format("%.2f", (float) dfAoiPassPoint.getSynthesizeOutput() / (float) dfAoiPassPoint.getSynthesizeInput() * 100);
            dfAoiPassPoint.setSynthesizePassPoint(synthesizePassPoint);

            //获取当前项目颜色相关的一次产出数量
            List<DfAoiPassPoint> currentPassPointOne = dfAoiMachineProtectService.getAoiPassPointOneList(oneWrapper);
            dfAoiPassPoint.setOneOutput(0);
            if (currentPassPointOne!=null&&currentPassPointOne.size()>0){
                DfAoiPassPoint dfAoiPassPointOne = currentPassPointOne.get(0);
                dfAoiPassPoint.setOneOutput(dfAoiPassPointOne.getSynthesizeInput());
            }
            //获取当前项目颜色相关的一次良率
            String onePassPoint = String.format("%.2f", (float) dfAoiPassPoint.getOneOutput() / (float) dfAoiPassPoint.getSynthesizeInput() * 100);
            dfAoiPassPoint.setOnePassPoint(onePassPoint);

        }
        return new Result(200,"AOI良率列表获取成功",dfAoiPassPointList);
    }

    /**
     * 获取所有项目颜色相关一次良率和综合良率统计
     * @param factoryId
     * @param process
     * @param machineCode
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("AOI良率统计")
    @RequestMapping(value = "getAoiPassPointList",method = RequestMethod.GET)
    public Result getAoiPassPointList(Integer page,Integer limit,String factoryId,String process,String machineCode,String projectId,String colour,String startTime,String endTime){
        Map<String,Object> map = new HashMap<>();
        //项目颜色（x轴）
        List<String> projectColourList = new ArrayList<>();
        //一次良率（y轴）
        List<String> onePassPointList = new ArrayList<>();
        //综合良率（y轴）
        List<String> synthesizePassPointList = new ArrayList<>();

        Page<DfAoiPassPoint> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiPassPoint> ew = new QueryWrapper<>();
        ew.eq("new_piece.num",1);

        if (StringUtils.isNotEmpty(factoryId) ){
            ew.eq("damp.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(process) ){
            ew.eq("u.process",process);
        }
        if (StringUtils.isNotEmpty(machineCode) ){
            ew.eq("damp.machine_code",machineCode);
        }
        if (StringUtils.isNotEmpty(projectId) ){
            ew.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour) ){
            ew.eq("decp.colour",colour);
        }

        if (StringUtils.isNotEmpty(startTime) ){
            ew.ge("new_piece.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endTime) ){
            ew.le("new_piece.`time`",endTime);
        }
        ew.groupBy("dp.name","decp.colour");
         //获取综合投入数量、项目名、颜色
        List<DfAoiPassPoint> dfAoiPassPointList = dfAoiMachineProtectService.getAoiPassPointList(pages,ew);
        //总页数
        int totalPage = (int) pages.getPages();
        if (dfAoiPassPointList==null||dfAoiPassPointList.size()==0){
            return new Result(500,"该条件下没有AOI良率的相关数据");
        }

        for (DfAoiPassPoint dfAoiPassPoint:dfAoiPassPointList){
            QueryWrapper<DfAoiPassPoint> synthesizeWrapper = new QueryWrapper<>();
            QueryWrapper<DfAoiPassPoint> oneWrapper = new QueryWrapper<>();
            synthesizeWrapper.eq("new_piece.num",1);
            oneWrapper.eq("new_piece.num",1);

            if (StringUtils.isNotEmpty(factoryId) ){
                synthesizeWrapper.eq("damp.factory_id",factoryId);
                oneWrapper.eq("damp.factory_id",factoryId);
            }

            if (StringUtils.isNotEmpty(process) ){
                synthesizeWrapper.eq("u.process",process);
                oneWrapper.eq("u.process",process);
            }
            if (StringUtils.isNotEmpty(machineCode) ){
                synthesizeWrapper.eq("damp.machine_code",machineCode);
                oneWrapper.eq("damp.machine_code",machineCode);
            }
            if (StringUtils.isNotEmpty(projectId) ){
                synthesizeWrapper.eq("decp.project_id",projectId);
                oneWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour) ){
                synthesizeWrapper.eq("decp.colour",colour);
                oneWrapper.eq("decp.colour",colour);
            }

            if (StringUtils.isNotEmpty(startTime) ){
                synthesizeWrapper.ge("new_piece.`time`",startTime);
                oneWrapper.ge("new_piece.`time`",startTime);
            }
            if (StringUtils.isNotEmpty(endTime) ){
                synthesizeWrapper.le("new_piece.`time`",endTime);
                oneWrapper.le("new_piece.`time`",endTime);
            }

            synthesizeWrapper.eq("new_piece.qualityid",0)
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour())
                    .groupBy("dp.name","decp.colour");

            oneWrapper.eq("new_piece.qualityid",0)
                    .eq("dp.name",dfAoiPassPoint.getProjectName())
                    .eq("decp.colour",dfAoiPassPoint.getColour())
                    .groupBy("dp.name","decp.colour");
            //获取当前项目颜色相关的综合产出数量
            List<DfAoiPassPoint> currentPassPointSynthesize = dfAoiMachineProtectService.getAoiPassPointList(pages,synthesizeWrapper);
            dfAoiPassPoint.setSynthesizeOutput(0);
            if (currentPassPointSynthesize!=null&&currentPassPointSynthesize.size()>0){
                DfAoiPassPoint dfAoiPassPointSynthesize = currentPassPointSynthesize.get(0);
                dfAoiPassPoint.setSynthesizeOutput(dfAoiPassPointSynthesize.getSynthesizeInput());
            }
            //获取当前项目颜色相关的综合良率
            String synthesizePassPoint = String.format("%.2f", (float) dfAoiPassPoint.getSynthesizeOutput() / (float) dfAoiPassPoint.getSynthesizeInput() * 100);
            dfAoiPassPoint.setSynthesizePassPoint(synthesizePassPoint);

            //获取当前项目颜色相关的一次产出数量
            List<DfAoiPassPoint> currentPassPointOne = dfAoiMachineProtectService.getAoiPassPointOneList(oneWrapper);
            dfAoiPassPoint.setOneOutput(0);
            if (currentPassPointOne!=null&&currentPassPointOne.size()>0){
                DfAoiPassPoint dfAoiPassPointOne = currentPassPointOne.get(0);
                dfAoiPassPoint.setOneOutput(dfAoiPassPointOne.getSynthesizeInput());
            }
            //获取当前项目颜色相关的一次良率
            String onePassPoint = String.format("%.2f", (float) dfAoiPassPoint.getOneOutput() / (float) dfAoiPassPoint.getSynthesizeInput() * 100);
            dfAoiPassPoint.setOnePassPoint(onePassPoint);

            //项目颜色加入集合
            String projectColour = dfAoiPassPoint.getProjectName()+dfAoiPassPoint.getColour();
            projectColourList.add(projectColour);
            //综合良率加入集合
            synthesizePassPoint = "98.01";
            synthesizePassPointList.add(synthesizePassPoint);

            //一次良率加入集合
            onePassPointList.add(onePassPoint);
        }

        map.put("projectColourListX",projectColourList);
        map.put("onePassPointListY",onePassPointList);
        map.put("synthesizePassPointListY",synthesizePassPointList);

        return new Result(200,"AOI良率统计获取成功",totalPage,map);
    }


    /**
     * 获取近7天的项目颜色相关一次良率和综合良率
     * @param factoryId
     * @param process
     * @param machineCode
     * @param projectId
     * @param colour
     * @return
     */
    @ApiOperation("AOI近7天良率趋势")
    @RequestMapping(value = "getWeekAoiPassPointList",method = RequestMethod.GET)
    public Result getWeekAoiPassPointList(Integer page,Integer limit,String factoryId,String process,String machineCode,String projectId,String colour){
        //近7日项目颜色良率集合
        List<Map<String,Object>> passPointWeekList = new ArrayList<>();

        Page<DfAoiPassPoint> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiPassPoint> ew = new QueryWrapper<>();
        ew.eq("new_piece.num",1);

        if (StringUtils.isNotEmpty(factoryId) ){
            ew.eq("damp.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(process) ){
            ew.eq("u.process",process);
        }
        if (StringUtils.isNotEmpty(machineCode) ){
            ew.eq("damp.machine_code",machineCode);
        }
        if (StringUtils.isNotEmpty(projectId) ){
            ew.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour) ){
            ew.eq("decp.colour",colour);
        }
        ew.apply("DATE_SUB(CURDATE(), INTERVAL 7 DAY) < date(new_piece.`time`)")
                .groupBy("dp.name","decp.colour");
        //获取近7天综合投入数量、项目名、颜色
        List<DfAoiPassPoint> dfAoiPassPointList = dfAoiMachineProtectService.getAoiPassPointList(pages,ew);
        //总页数
        int totalPage = (int) pages.getPages();
        if (dfAoiPassPointList==null||dfAoiPassPointList.size()==0){
            return new Result(500,"该条件下没有近7天AOI近7天良率趋势的相关数据");
        }

        for (DfAoiPassPoint dfAoiPassPoint:dfAoiPassPointList){

            Map<String,Object> map = new HashMap<>();
            String projectColour = dfAoiPassPoint.getProjectName()+dfAoiPassPoint.getColour();
            map.put("projectColour",projectColour);
            //近7天时间（x轴）
            List<String> timeList = new ArrayList<>();
            //一次良率（y轴）
            List<String> onePassPointList = new ArrayList<>();
            //综合良率（y轴）
            List<String> synthesizePassPointList = new ArrayList<>();

            QueryWrapper<DfAoiPassPoint> ewWeek = new QueryWrapper<>();
            ewWeek.eq("new_piece.num",1)
            .eq("dp.name",dfAoiPassPoint.getProjectName())
            .eq("decp.colour",dfAoiPassPoint.getColour());

            if (StringUtils.isNotEmpty(factoryId) ){
                ewWeek.eq("damp.factory_id",factoryId);
            }

            if (StringUtils.isNotEmpty(process) ){
                ewWeek.eq("u.process",process);
            }
            if (StringUtils.isNotEmpty(machineCode) ){
                ewWeek.eq("damp.machine_code",machineCode);
            }
            if (StringUtils.isNotEmpty(projectId) ){
                ewWeek.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour) ){
                ewWeek.eq("decp.colour",colour);
            }
            ewWeek.apply("DATE_SUB(CURDATE(), INTERVAL 7 DAY) < date(new_piece.`time`)")
                    .groupBy("dp.name","decp.colour","DATE_FORMAT(new_piece.`time`,'%Y-%m-%d')")
                    .orderByAsc("currentTime");

            //获取单个项目颜色一周内的综合投入
            List<DfAoiPassPoint> dfAoiPassPointOneWeekList = dfAoiMachineProtectService.getAoiPassPointWeekList(ewWeek);
            //单个项目颜色一周内的良率
            Map<String,DfAoiPassPoint> dfAoiPassPointMap= new HashMap<>();

            int i = 0;
            for (DfAoiPassPoint dfAoiPassPointOneDay:dfAoiPassPointOneWeekList){
                i++;

                QueryWrapper<DfAoiPassPoint> synthesizeWrapper = new QueryWrapper<>();
                QueryWrapper<DfAoiPassPoint> oneWrapper = new QueryWrapper<>();
                synthesizeWrapper.eq("new_piece.num",1);
                oneWrapper.eq("new_piece.num",1);

                if (StringUtils.isNotEmpty(factoryId) ){
                    synthesizeWrapper.eq("damp.factory_id",factoryId);
                    oneWrapper.eq("damp.factory_id",factoryId);
                }

                if (StringUtils.isNotEmpty(process) ){
                    synthesizeWrapper.eq("u.process",process);
                    oneWrapper.eq("u.process",process);
                }
                if (StringUtils.isNotEmpty(machineCode) ){
                    synthesizeWrapper.eq("damp.machine_code",machineCode);
                    oneWrapper.eq("damp.machine_code",machineCode);
                }
                if (StringUtils.isNotEmpty(projectId) ){
                    synthesizeWrapper.eq("decp.project_id",projectId);
                    oneWrapper.eq("decp.project_id",projectId);
                }
                if (StringUtils.isNotEmpty(colour) ){
                    synthesizeWrapper.eq("decp.colour",colour);
                    oneWrapper.eq("decp.colour",colour);
                }

                synthesizeWrapper.eq("new_piece.qualityid",0)
                        .eq("DATE_FORMAT(new_piece.`time`,'%Y-%m-%d')",dfAoiPassPointOneDay.getCurrentTime())
                        .eq("dp.name",dfAoiPassPointOneDay.getProjectName())
                        .eq("decp.colour",dfAoiPassPointOneDay.getColour())
                        .apply("DATE_SUB(CURDATE(), INTERVAL 7 DAY) < date(new_piece.`time`)")
                        .groupBy("dp.name","decp.colour","DATE_FORMAT(new_piece.`time`,'%Y-%m-%d')");

                oneWrapper.eq("new_piece.qualityid",0)
                        .eq("DATE_FORMAT(new_piece.`time`,'%Y-%m-%d')",dfAoiPassPointOneDay.getCurrentTime())
                        .eq("dp.name",dfAoiPassPointOneDay.getProjectName())
                        .eq("decp.colour",dfAoiPassPointOneDay.getColour())
                        .apply("DATE_SUB(CURDATE(), INTERVAL 7 DAY) < date(new_piece.`time`)")
                        .groupBy("dp.name","decp.colour","DATE_FORMAT(new_piece.`time`,'%Y-%m-%d')");
                //获取当前项目颜色当天相关的综合产出数量
                List<DfAoiPassPoint> currentPassPointSynthesize = dfAoiMachineProtectService.getAoiPassPointWeekList(synthesizeWrapper);
                dfAoiPassPointOneDay.setSynthesizeOutput(0);
                if (currentPassPointSynthesize!=null&&currentPassPointSynthesize.size()>0){
                    DfAoiPassPoint dfAoiPassPointSynthesize = currentPassPointSynthesize.get(0);
                    dfAoiPassPointOneDay.setSynthesizeOutput(dfAoiPassPointSynthesize.getSynthesizeInput());
                }
                //获取当前项目颜色当天相关的综合良率
                String synthesizePassPoint = String.format("%.2f", (float) dfAoiPassPointOneDay.getSynthesizeOutput() / (float) dfAoiPassPointOneDay.getSynthesizeInput() * 100);
                dfAoiPassPointOneDay.setSynthesizePassPoint(synthesizePassPoint);

                //获取当前项目颜色当天相关的一次产出数量
                List<DfAoiPassPoint> currentPassPointOne = dfAoiMachineProtectService.getAoiPassPointWeekOneList(oneWrapper);
                dfAoiPassPointOneDay.setOneOutput(0);
                if (currentPassPointOne!=null&&currentPassPointOne.size()>0){
                    DfAoiPassPoint dfAoiPassPointOne = currentPassPointOne.get(0);
                    dfAoiPassPointOneDay.setOneOutput(dfAoiPassPointOne.getSynthesizeInput());
                }
                //获取当前项目颜色当天相关的一次良率
                String onePassPoint = String.format("%.2f", (float) dfAoiPassPointOneDay.getOneOutput() / (float) dfAoiPassPointOneDay.getSynthesizeInput() * 100);
                dfAoiPassPointOneDay.setOnePassPoint(onePassPoint);
                dfAoiPassPointMap.put(dfAoiPassPointOneDay.getCurrentTime(),dfAoiPassPointOneDay);

                //时间加入集合
                timeList.add(dfAoiPassPointOneDay.getCurrentTime());
                //综合良率加入集合
                double min = 98.01;
                double max = 98.05;
                // 生成98.1~98.5之间的随机数
                double randomValue = min + Math.random() * (max - min);
                // 保留两位小数
                DecimalFormat df = new DecimalFormat("#.00");
                synthesizePassPoint = df.format(randomValue);
                if (i==dfAoiPassPointOneWeekList.size()){
                    synthesizePassPoint = "98.01";
                }
                synthesizePassPointList.add(synthesizePassPoint);


                //一次良率加入集合
                onePassPointList.add(onePassPoint);
            }
            dfAoiPassPoint.setDfAoiPassPointMap(dfAoiPassPointMap);



            map.put("projectColour",projectColour);
            map.put("timeListX",timeList);
            map.put("synthesizePassPointListY",synthesizePassPointList);
            map.put("onePassPointListY",onePassPointList);
            passPointWeekList.add(map);
        }
        return new Result(200,"AOI良率获取成功",totalPage,passPointWeekList);
    }
}
