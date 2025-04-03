package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ww.boengongye.entity.DfPrintAoiRecheck;
import com.ww.boengongye.entity.DfPrintAoiRecheckDetail;
import com.ww.boengongye.service.DfPrintAoiRecheckService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 移印AOI人工复查 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Controller
@RequestMapping("/dfPrintAoiRecheck")
@ResponseBody
@CrossOrigin
@Api(tags = "移印AOI人工复查")
public class DfPrintAoiRecheckController {

    @Autowired
    private DfPrintAoiRecheckService dfPrintAoiRecheckService;

    @PostMapping("/importData")
    @ApiOperation("导入数据")
    public Result importData(MultipartFile file) throws Exception {
        int i = dfPrintAoiRecheckService.importExcel(file);
        return new Result(200, "成功添加" + i + "条数据");
    }


    @RequestMapping(value = "getOverkillOrEscapeList",method = RequestMethod.GET)
    @ApiOperation("过杀漏检直方图")
    public Result getOverkillOrEscapeList(
            String factory,String process,String lineBody,String project,
            String startDate, String endDate
    ){
        Map<String,Object> map = new HashMap<>();

        //线体集合
        List<String> lineBodyList = new ArrayList<>();

        //过杀率集合
        List<String> overkillList = new ArrayList<>();

        //漏检率集合
        List<String> escapeList = new ArrayList<>();

        QueryWrapper<DfPrintAoiRecheck> overkillWrapper = new QueryWrapper<>();
        QueryWrapper<DfPrintAoiRecheck> escapeWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            overkillWrapper.eq("dpar.factory",factory);
            escapeWrapper.eq("dpar.factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            overkillWrapper.eq("dpar.line_body",lineBody);
            escapeWrapper.eq("dpar.line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(project)){
            overkillWrapper.eq("dpar.project",project);
            escapeWrapper.eq("dpar.project",project);
        }
        if (StringUtils.isNotEmpty(startDate)){
            overkillWrapper.ge("dpar.check_time",startDate);
            escapeWrapper.ge("dpar.check_time",startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            overkillWrapper.le("dpar.check_time",endDate);
            escapeWrapper.le("dpar.check_time",endDate);
        }
        overkillWrapper.eq("dpar.type2","Overkill过杀");
        escapeWrapper.eq("dpar.type2","Escape漏检");

        //获取所有线体的过杀率
        List<DfPrintAoiRecheck> overkillRecheckList = dfPrintAoiRecheckService.getOverkillOrEscapeList(overkillWrapper);
        if (overkillRecheckList==null||overkillRecheckList.size()==0){
            return new Result(500,"该条件下没有过杀漏检相关数据");
        }
        for (DfPrintAoiRecheck dfPrintAoiRecheck:overkillRecheckList){
            lineBodyList.add(dfPrintAoiRecheck.getLineBody());
            overkillList.add(dfPrintAoiRecheck.getOverkillOrEscape());
        }
        //获取所有线体的过杀率
        List<DfPrintAoiRecheck> escapeRecheckList = dfPrintAoiRecheckService.getOverkillOrEscapeList(escapeWrapper);
        for (DfPrintAoiRecheck dfPrintAoiRecheck :escapeRecheckList){
            escapeList.add(dfPrintAoiRecheck.getOverkillOrEscape());
        }

        map.put("lineBodyListX",lineBodyList);
        map.put("overkillListY",overkillList);
        map.put("escapeListY",escapeList);
        return new Result(200,"获取过杀漏检直方图成功",map);
    }


    @RequestMapping(value = "getEscapeDetailList",method = RequestMethod.GET)
    @ApiOperation("漏检明细")
    public Result getEscapeDetailList(
            String factory,String process,String lineBody,String project,
            String startDate, String endDate
    ){
        List<Object> list = new ArrayList<>();


        QueryWrapper<DfPrintAoiRecheck> lineBodyWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(factory)){
            lineBodyWrapper.eq("factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            lineBodyWrapper.eq("line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(project)){
            lineBodyWrapper.eq("project",project);
        }
        if (StringUtils.isNotEmpty(startDate)){
            lineBodyWrapper.ge("check_time",startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            lineBodyWrapper.le("check_time",endDate);
        }
        lineBodyWrapper
                .select("line_body")
                .eq("type2","Escape漏检")
                .groupBy("line_body");

        //获取该条件下所有的线体
        List<DfPrintAoiRecheck> printAoiRecheckList = dfPrintAoiRecheckService.list(lineBodyWrapper);
        if (printAoiRecheckList==null||printAoiRecheckList.size()==0){
            return new Result(500,"该条件下没有漏检明细相关信息");
        }

        for (DfPrintAoiRecheck dfPrintAoiRecheck :printAoiRecheckList){
            Map<String,Object> map = new HashMap<>();
            map.put("lineBody",dfPrintAoiRecheck.getLineBody());
            List<Object> defectNameList = new ArrayList<>();
            List<Object> defectNumberList = new ArrayList<>();

            QueryWrapper<DfPrintAoiRecheckDetail> escapeDetailWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(factory)){
                escapeDetailWrapper.eq("dpar.factory",factory);
            }
            if (StringUtils.isNotEmpty(lineBody)){
                escapeDetailWrapper.eq("dpar.line_body",lineBody);
            }
            if (StringUtils.isNotEmpty(project)){
                escapeDetailWrapper.eq("dpar.project",project);
            }
            if (StringUtils.isNotEmpty(startDate)){
                escapeDetailWrapper.ge("dpar.check_time",startDate);
            }
            if (StringUtils.isNotEmpty(endDate)){
                escapeDetailWrapper.le("dpar.check_time",endDate);
            }
            escapeDetailWrapper
                    .eq("dpar.type2","Escape漏检")
                    .eq("dpar.line_body",dfPrintAoiRecheck.getLineBody());
            //获取每个线体所有的漏检缺陷及其数量
            List<DfPrintAoiRecheckDetail> escapeDetailList = dfPrintAoiRecheckService.getEscapeDetailList(escapeDetailWrapper);
            for (DfPrintAoiRecheckDetail dfPrintAoiRecheckDetail :escapeDetailList){
                defectNameList.add(dfPrintAoiRecheckDetail.getDefectName());
                defectNumberList.add(dfPrintAoiRecheckDetail.getDefectNumber());
            }

            map.put("lineBody",dfPrintAoiRecheck.getLineBody());
            map.put("defectNameListX",defectNameList);
            map.put("defectNumberListY",defectNumberList);

            list.add(map);
        }

        return new Result(200,"获取漏检明细成功",list);
    }
}
