package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfBcLinker;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.ww.boengongye.service.DfBcLinkerService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * BC-linker表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-09
 */
@Controller
@RequestMapping("/dfBcLinker")
@ResponseBody
@CrossOrigin
@Api(tags = "BC-linker表")
public class DfBcLinkerController {

    @Autowired
    private DfBcLinkerService dfAoiBcLinkerService;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("导入")
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取BC-linker表信息失败,导入文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
            return new Result(500, "请上传xlsx或xls格式的文件");
        }

        ExcelImportUtil excel = new ExcelImportUtil(file);
        String checkTime = StringUtils.substringBefore(file.getOriginalFilename(),".") ;
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        //导入的OBA工厂比较数据相关集合
        List<DfBcLinker> list = new ArrayList<>();

        for (Map<String, String> map : maps) {

            DfBcLinker dfBcLinker = new DfBcLinker();
            dfBcLinker.setCheckTime(checkTime+" "+map.get("时间"));
            dfBcLinker.setRowId(map.get("RowID"));
            dfBcLinker.setPosition(map.get("位置"));
            dfBcLinker.setResult(map.get("结果"));
            dfBcLinker.setBarcode(map.get("条码"));
            dfBcLinker.setCipher(map.get("暗码"));
            dfBcLinker.setSilo(map.get("分料料仓"));
            dfBcLinker.setCipherResult(map.get("暗码结果"));
            dfBcLinker.setNetworkResult(map.get("网络校验结果"));
            dfBcLinker.setTraceCard(map.get("Trace卡站"));
            dfBcLinker.setGet2DResult(map.get("获取2D结果"));
            dfBcLinker.setIsmResult(map.get("回捞ISM结果"));
            dfBcLinker.setRadiumResult(map.get("镭码结果"));

            dfAoiBcLinkerService.save(dfBcLinker);
            list.add(dfBcLinker);
        }
        return new Result(200, "BC-linker数据导入成功,导入"+list.size()+"条");
    }

    @RequestMapping(value = "getCapacityRowDataList",method = RequestMethod.GET)
    @ApiOperation("获取产能原始数据")
    public Result getCapacityRowDataList(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("线体")@RequestParam(required = false) String lineBody
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("设备")@RequestParam(required = false) String device
            ,Integer page,Integer limit
    )throws ParseException {
        QueryWrapper<DfBcLinker> bcLinkerWrapper = new QueryWrapper<>();
        bcLinkerWrapper.eq("1","1");

        QueryWrapper<String> timeWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            bcLinkerWrapper.eq("factory",factory);
            timeWrapper.eq("factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            bcLinkerWrapper.eq("lineBody",lineBody);
            timeWrapper.eq("lineBody",lineBody);
        }
        if (StringUtils.isNotEmpty(startTime)){
            startTime = startTime + " 07:00:00";
            bcLinkerWrapper.ge("check_time",startTime);
            timeWrapper.ge("check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";
            bcLinkerWrapper.le("check_time",endTime);
            timeWrapper.le("check_time",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            bcLinkerWrapper.eq("project",project);
            timeWrapper.eq("project",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            bcLinkerWrapper.eq("colour",colour);
            timeWrapper.eq("colour",colour);
        }
        if (StringUtils.isNotEmpty(device)){
            bcLinkerWrapper.eq("device",device);
            timeWrapper.eq("device",device);
        }
        Page<DfBcLinker> pages = new Page<>(page,limit);
        //原始数据
        IPage<DfBcLinker> list = dfAoiBcLinkerService.page(pages,bcLinkerWrapper);

        return new Result(200,"获取产能原始数据成功",list.getRecords(),(int) list.getTotal());
    }


    @RequestMapping(value = "getCapacityDataList",method = RequestMethod.GET)
    @ApiOperation("获取产能数据趋势图")
    public Result getCapacityDataList(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("线体")@RequestParam(required = false) String lineBody
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("设备")@RequestParam(required = false) String device
    )throws ParseException {
        Map<String,Object> map = new HashMap<>();

        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //白班产能（y轴）
        List<Integer> dayDataList = new ArrayList<>();

        //夜班产能（y轴）
        List<Integer> nightDataList = new ArrayList<>();

        QueryWrapper<DfBcLinker> bcLinkerWrapper = new QueryWrapper<>();
        bcLinkerWrapper.eq("1","1");

        QueryWrapper<String> timeWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            bcLinkerWrapper.eq("factory",factory);
            timeWrapper.eq("factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            bcLinkerWrapper.eq("lineBody",lineBody);
            timeWrapper.eq("lineBody",lineBody);
        }
        if (StringUtils.isNotEmpty(startTime)){
            startTime = startTime + " 07:00:00";
            bcLinkerWrapper.ge("check_time",startTime);
            timeWrapper.ge("check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";
            bcLinkerWrapper.le("check_time",endTime);
            timeWrapper.le("check_time",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            bcLinkerWrapper.eq("project",project);
            timeWrapper.eq("project",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            bcLinkerWrapper.eq("colour",colour);
            timeWrapper.eq("colour",colour);
        }
        if (StringUtils.isNotEmpty(device)){
            bcLinkerWrapper.eq("device",device);
            timeWrapper.eq("device",device);
        }

        //原始数据
//        List<DfBcLinker> bcLinkerList = dfAoiBcLinkerService.list(bcLinkerWrapper);

        //获取条件下有数据的日期
        timeList = dfAoiBcLinkerService.getTimeList(timeWrapper);

        //白班产能
        List<DfBcLinker> bcLinkerDayList = dfAoiBcLinkerService.getDayDataList(bcLinkerWrapper);
        if (bcLinkerDayList!=null&&bcLinkerDayList.size()>=0){
            for (DfBcLinker bcLinker:bcLinkerDayList){
                Integer checkNumber = 0;
                if (bcLinker.getCheckNumber()!=null){
                    checkNumber = bcLinker.getCheckNumber();
                }
                dayDataList.add(checkNumber);
            }
        }

        //夜班产能
        List<DfBcLinker> bcLinkerNightList = dfAoiBcLinkerService.getNightDataList(bcLinkerWrapper);
        if (bcLinkerDayList!=null&&bcLinkerDayList.size()>=0){
            for (DfBcLinker bcLinker:bcLinkerNightList){
                Integer checkNumber = 0;
                if (bcLinker.getCheckNumber()!=null){
                    checkNumber = bcLinker.getCheckNumber();
                }
                nightDataList.add(checkNumber);
            }
        }

        map.put("timeListX",timeList);
        map.put("dayDataListY",dayDataList);
        map.put("nightDataListY",nightDataList);
//        map.put("bcLinkerRowDataList",bcLinkerList);

        return new Result(200,"获取产能数据趋势图数据成功",map);
    }


    @RequestMapping(value = "getCipherAndTraceCardPointList",method = RequestMethod.GET)
    @ApiOperation("获取不良分析统计")
    public Result getCipherAndTraceCardPointList(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("线体")@RequestParam(required = false) String lineBody
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("设备")@RequestParam(required = false) String device
    )throws ParseException {
        Map<String,Object> map = new HashMap<>();

        //位置（x轴）
        List<String> positionList = new ArrayList<>();

        //读不出码不良率（y轴）
        List<String> cipherDefectPointList = new ArrayList<>();

        //卡站不良率（y轴）
        List<String> cardDefectPointList = new ArrayList<>();

        QueryWrapper<DfBcLinker> bcLinkerWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            bcLinkerWrapper.eq("factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            bcLinkerWrapper.eq("lineBody",lineBody);
        }
        if (StringUtils.isNotEmpty(startTime)){
            startTime = startTime + " 07:00:00";
            bcLinkerWrapper.ge("check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";
            bcLinkerWrapper.le("check_time",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            bcLinkerWrapper.eq("project",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            bcLinkerWrapper.eq("colour",colour);
        }
        if (StringUtils.isNotEmpty(device)){
            bcLinkerWrapper.eq("device",device);
        }

        //所有位置的读不出码和卡站的不良率
        List<DfBcLinker> bcLinkerList = dfAoiBcLinkerService.getCipherAndTraceCardPointList(bcLinkerWrapper);
        for (DfBcLinker dfBcLinker:bcLinkerList){

            //位置加入集合
            positionList.add(dfBcLinker.getPosition());
            //读不出码不良率加入集合
            cipherDefectPointList.add(dfBcLinker.getCipherPoint());
            //卡站不良率加入集合
            cardDefectPointList.add(dfBcLinker.getTraceCardPoint());
        }

//        bcLinkerWrapper.and(wrapper -> wrapper
//                .or().eq("cipher_result","NG")
//                .or().eq("trace_card","NG"));
        //不良分析原始数据
//        List<DfBcLinker> bcLinkerRowDataList = dfAoiBcLinkerService.list(bcLinkerWrapper);

        map.put("positionListX",positionList);
        map.put("cipherDefectPointListY",cipherDefectPointList);
        map.put("cardDefectPointListY",cardDefectPointList);
//        map.put("bcLinkerRowDataList",bcLinkerRowDataList);

        return new Result(200,"获取不良分析相关信息成功",map);
    }

    @RequestMapping(value = "getDefectPointTop3List",method = RequestMethod.GET)
    @ApiOperation("获取工厂不良TOP3分布对比")
    public Result getDefectPointTop3List(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("线体")@RequestParam(required = false) String lineBody
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("设备")@RequestParam(required = false) String device
    ) throws ParseException {
        Map<String,Object> map = new HashMap<>();

        //缺陷名称（x轴）
        List<String> defectNameList = new ArrayList<>();

        //缺陷数量（y轴）
        List<Integer> defectNumberList = new ArrayList<>();

        //缺陷不良率（y轴）
        List<String> defectPointList = new ArrayList<>();

        QueryWrapper<DfBcLinker> bcLinkerWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            bcLinkerWrapper.eq("factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            bcLinkerWrapper.eq("lineBody",lineBody);
        }
        if (StringUtils.isNotEmpty(startTime)){
            startTime = startTime + " 07:00:00";
            bcLinkerWrapper.ge("check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";
            bcLinkerWrapper.le("check_time",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            bcLinkerWrapper.eq("project",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            bcLinkerWrapper.eq("colour",colour);
        }
        if (StringUtils.isNotEmpty(device)){
            bcLinkerWrapper.eq("device",device);
        }

        //获取TOP3不良缺陷的相关信息
        List<DfBcLinker> bcLinkerList = dfAoiBcLinkerService.getDefectPointTop3List(bcLinkerWrapper);
        for (DfBcLinker dfBcLinker:bcLinkerList){

            //缺陷名称加入集合
            defectNameList.add(dfBcLinker.getDefectZHName());
            //缺陷数量加入集合
            defectNumberList.add(dfBcLinker.getDefectNumber());
            //缺陷率加入集合
            defectPointList.add(dfBcLinker.getDefectPoint());
        }

        map.put("defectNameListX",defectNameList);
        map.put("defectNumberListY",defectNumberList);
        map.put("defectPointListY",defectPointList);

        return new Result(200,"获取工厂不良TOP3分布对比成功",map);
    }



}
