package com.ww.boengongye.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.exportExcel.EmpCapacityReport;
import com.ww.boengongye.service.DfAoiObaCompareService;
import com.ww.boengongye.service.DfAoiObaDefectService;
import com.ww.boengongye.utils.Excel;
import com.ww.boengongye.utils.ExcelExportUtil;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * OBA工厂比较 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-09-04
 */
@Controller
@RequestMapping("/dfAoiObaCompare")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI OBA工厂比较")
public class DfAoiObaCompareController {

    @Autowired
    private DfAoiObaCompareService dfAoiObaCompareService;

    @Autowired
    private DfAoiObaDefectService dfAoiObaDefectService;


    /**
     * 关键字查询
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    @ApiOperation("AOI OBA工厂比较列表")
    public Result listBySearch(int page,int limit,String keywords){
        Page<DfAoiObaCompare> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiObaCompare> ew = new QueryWrapper<>();
        if (keywords != null && !"".equals(keywords)) {
            ew.and(wrapper -> wrapper
                    .like("check_week", keywords)
                    .or().like("factory", keywords)
                    .or().like("project", keywords)
                    .or().like("`type`", keywords)
                    .or().like("colour", keywords)
                    .or().like("batch", keywords)
            );
        }
        IPage<DfAoiObaCompare> list = dfAoiObaCompareService.page(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }


    @RequestMapping(value = "getBatchDefectDetail",method = RequestMethod.GET)
    @ApiOperation("通过批次号获取该批次的所有缺陷集合")
    public Result getBatchDefectDetail(String batch){
        QueryWrapper<DfAoiObaDefect> obaDefectWrapper = new QueryWrapper<>();
        obaDefectWrapper.eq("batch",batch);
        List<DfAoiObaDefect> obaDefectList = dfAoiObaDefectService.list(obaDefectWrapper);
        if (obaDefectList==null||obaDefectList.size()==0){
            return new Result(500,"该批次没有缺陷数据");
        }
        return new Result(200,"获取该批次的缺陷集合成功",obaDefectList);
    }


    /**
     * 添加或修改
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或修改AOI OBA工厂比较数据")
    public Result saveOrUpdate(@RequestBody DfAoiObaCompare datas){
        if (datas.getId()!=null){
            if (dfAoiObaCompareService.updateById(datas)){
                return new Result(200,"修改成功");
            }
            return new Result(500,"修改失败");
        }else {
            if (dfAoiObaCompareService.save(datas)){
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
    @ApiOperation("删除AOI OBA工厂比较数据")
    public Result delete(String id){
        if (dfAoiObaCompareService.removeById(id)){
            return new Result(200,"删除成功");
        }
        return new Result(500,"删除失败");
    }


    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        dfAoiObaCompareService.exportModel(response, "MQA_OBA数据模板");
    }

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("导入")
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "file", required = false)MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取OBA工厂比较信息失败,导入文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
            return new Result(500, "请上传xlsx或xls格式的文件");
        }

        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        //导入的OBA工厂比较数据相关集合
        List<DfAoiObaCompare> list = new ArrayList<>();

        for (Map<String, String> map : maps) {
            String batch = map.get("批次号");
            QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
            obaCompareWrapper
                    .eq("batch",batch)
                    .last("limit 1");
            DfAoiObaCompare dfAoiObaCompareOld = dfAoiObaCompareService.getOne(obaCompareWrapper);

            DfAoiObaCompare dfAoiObaCompare = new DfAoiObaCompare();
            dfAoiObaCompare.setCheckTime(map.get("审核日期"));
            dfAoiObaCompare.setCheckWeek(map.get("审核周别"));
            dfAoiObaCompare.setFactory(map.get("工厂"));
            dfAoiObaCompare.setProject(map.get("项目"));
            dfAoiObaCompare.setType(map.get("型号"));
            dfAoiObaCompare.setColour(map.get("颜色"));
            dfAoiObaCompare.setBatch(map.get("批次号"));
            dfAoiObaCompare.setBatchNumber(Integer.valueOf(map.get("批量")));
            dfAoiObaCompare.setCheckNumber(Integer.valueOf(map.get("抽检数")));
            dfAoiObaCompare.setNG(Integer.valueOf(map.get("NG")));
            dfAoiObaCompare.setPassPointGoal(map.get("良率目标"));
            dfAoiObaCompare.setPassPoint(map.get("良率"));
            dfAoiObaCompare.setCheckResult(map.get("抽检结果"));

            QueryWrapper<DfAoiObaDefect> obaDefectWrapper = new QueryWrapper<>();
            obaDefectWrapper.eq("batch",map.get("批次号"));
            List<DfAoiObaDefect> dfAoiObaDefectOldList = dfAoiObaDefectService.list();

            if (dfAoiObaDefectOldList!=null&&dfAoiObaDefectOldList.size()>0){
                dfAoiObaDefectService.remove(obaDefectWrapper);
            }

            boolean startIteration = false;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (startIteration) {
                    DfAoiObaDefect dfAoiObaDefect = new DfAoiObaDefect();
                    dfAoiObaDefect.setBatch(map.get("批次号"));
                    dfAoiObaDefect.setDefectName(entry.getKey());
                    dfAoiObaDefect.setDefectNumber(Integer.valueOf(entry.getValue()));
                    dfAoiObaDefectService.save(dfAoiObaDefect);
                }

                if (entry.getKey().equals("抽检结果")) {
                    startIteration = true;
                }
            }

            if (dfAoiObaCompareOld==null||dfAoiObaCompareOld.getId()==null){
                if (!dfAoiObaCompareService.save(dfAoiObaCompare)){
                    return new Result(500,"批次号"+dfAoiObaCompare.getBatch()+"数据添加失败");
                }
            }else {
                dfAoiObaCompare.setId(dfAoiObaCompareOld.getId());
                if (!dfAoiObaCompareService.updateById(dfAoiObaCompare)){
                    return new Result(500,"批次号"+dfAoiObaCompare.getBatch()+"数据修改失败");
                }
            }
        }
        return new Result(200, "OBA工厂比较数据导入成功", list);
    }

//    @ApiOperation("导出")
//    @RequestMapping(value = "downloadExcel",method = RequestMethod.GET)
//    public void downloadExcel(HttpServletRequest request, HttpServletResponse response
//            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
//            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) throws IOException {
//        QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
//        if (StringUtils.isNotEmpty(startTime)){
//            obaCompareWrapper.ge("check_time",startTime);
//        }
//
//        if (StringUtils.isNotEmpty(endTime)){
//            obaCompareWrapper.le("check_time",endTime);
//        }
//        List<DfAoiObaCompare> datas = dfAoiObaCompareService.list(obaCompareWrapper);
//    }

    @RequestMapping(value = "getObaPassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的OBA工厂对比良率")
    public Result getObaPassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //良率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> factoryNameWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            factoryNameWrapper.eq("daoc.factory",factory);
        }
        if (StringUtils.isNotEmpty(project)){
            factoryNameWrapper.eq("daoc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            factoryNameWrapper.eq("daoc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiObaCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("month(daoc.check_time)>="+startTime)
                        .apply("month(daoc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> obaFactoryNameMonthList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameMonthList==null||obaFactoryNameMonthList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameMonthList){
                    //当前工厂一段时间（月）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("month(daoc.check_time)>="+startTime)
                            .apply("month(daoc.check_time)<="+endTime);


                    //当前工厂一段时间（月）内的良率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaPassPointMonth(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（月）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiObaCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(daoc.check_time)>="+startTime)
                        .apply("week(daoc.check_time)<="+endTime);
                //一段时间（周）内的工厂名
                List<String> obaFactoryNameWeekList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameWeekList==null||obaFactoryNameWeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameWeekList){
                    //当前工厂一段时间（周）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("week(daoc.check_time)>="+startTime)
                            .apply("week(daoc.check_time)<="+endTime);

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper2 = new QueryWrapper<>();
                    obaCompareWrapper2
                            .apply("week(c.datelist)>="+startTime)
                            .apply("week(c.datelist)<="+endTime);

                    //当前工厂一段时间（周）内的良率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaPassPointWeek(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（周）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiObaCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("day(daoc.check_time)>="+startTime)
                        .apply("day(daoc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> obaFactoryNameDayList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameDayList==null||obaFactoryNameDayList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameDayList){
                    //当前工厂一段时间（天）内的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("day(daoc.check_time)>="+startTime)
                            .apply("day(daoc.check_time)<="+endTime);


                    //当前工厂一段时间（天）内的良率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaPassPointDay(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（天）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiObaCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(daoc.check_time)>=week(now())-3 ")
                        .apply("week(daoc.check_time)<=week(now())");
                //近4周工厂名
                List<String> obaFactoryName4WeekList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryName4WeekList==null||obaFactoryName4WeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }
                for (String obaFactoryName:obaFactoryName4WeekList){
                    //单个工厂近4周的良率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }
                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("week(daoc.check_time)>=week(now())-3 ")
                            .apply("week(daoc.check_time)<=week(now())");


                    //当前工厂近4周的良率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaPassPointWeek(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂的近4周的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryName4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI OBA工厂对比良率数据成功",map);
    }


    @RequestMapping(value = "getObaBatchPassPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的OBA工厂对比批通率")
    public Result getObaBatchPassPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();

        //批通率（y轴）
        List<Object> passPointList = new ArrayList<>();

        QueryWrapper<String> factoryNameWrapper = new QueryWrapper<>();
        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        if (StringUtils.isNotEmpty(factory)){
            factoryNameWrapper.eq("daoc.factory",factory);
        }
        if (StringUtils.isNotEmpty(project)){
            factoryNameWrapper.eq("daoc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            factoryNameWrapper.eq("daoc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("month(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("month(c.datelist)<="+endTime);
                }

                //一段时间（月）内的时间
                List<String> timeNewMonthList = dfAoiObaCompareService.getTimeMonth(timeWrapper);

                for (String time:timeNewMonthList){
                    String timeNew = time+"mh";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("month(daoc.check_time)>="+startTime)
                        .apply("month(daoc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> obaFactoryNameMonthList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameMonthList==null||obaFactoryNameMonthList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameMonthList){
                    //当前工厂一段时间（月）内的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("month(daoc.check_time)>="+startTime)
                            .apply("month(daoc.check_time)<="+endTime);


                    //当前工厂一段时间（月）内的批通率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaBatchPassPointMonth(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（月）内的良率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameMonthList);

                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("week(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("week(c.datelist)<="+endTime);
                }

                //一段时间（周）内的时间
                List<String> timeNewWeekList = dfAoiObaCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNewWeekList){
                    String timeNew = time+"wk";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(daoc.check_time)>="+startTime)
                        .apply("week(daoc.check_time)<="+endTime);
                //一段时间（周）内的工厂名
                List<String> obaFactoryNameWeekList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameWeekList==null||obaFactoryNameWeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameWeekList){
                    //当前工厂一段时间（周）内的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("week(daoc.check_time)>="+startTime)
                            .apply("week(daoc.check_time)<="+endTime);

                    //当前工厂一段时间（周）内的批通率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaBatchPassPointWeek(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（周）内的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameWeekList);
                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    timeWrapper.apply("day(c.datelist)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    timeWrapper.apply("day(c.datelist)<="+endTime);
                }

                //一段时间（天）内的时间
                List<String> timeNewDayList = dfAoiObaCompareService.getTimeDay(timeWrapper);

                for (String time:timeNewDayList){
                    String timeNew = time+"dy";
                    //时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("day(daoc.check_time)>="+startTime)
                        .apply("day(daoc.check_time)<="+endTime);
                //一段时间（月）内的工厂名
                List<String> obaFactoryNameDayList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryNameDayList==null||obaFactoryNameDayList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }

                for (String obaFactoryName:obaFactoryNameDayList){
                    //当前工厂一段时间（天）内的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }

                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("day(daoc.check_time)>="+startTime)
                            .apply("day(daoc.check_time)<="+endTime);

                    //当前工厂一段时间（天）内的批通率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaBatchPassPointDay(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂一段时间（天）内的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryNameDayList);
                break;

            case "近4周":
                timeWrapper
                        .apply("week(c.datelist)>=week(now())-3")
                        .apply("week(c.datelist)<=week(now())");
                //近4周时间
                List<String> timeNew4WeekList = dfAoiObaCompareService.getTimeWeek(timeWrapper);

                for (String time:timeNew4WeekList){
                    String timeNew = time+"wk";
                    //近4周时间加入集合
                    timeList.add(timeNew);
                }

                factoryNameWrapper
                        .apply("week(daoc.check_time)>=week(now())-3 ")
                        .apply("week(daoc.check_time)<=week(now())");
                //近4周工厂名
                List<String> obaFactoryName4WeekList = dfAoiObaCompareService.getObaFactoryName(factoryNameWrapper);
                if (obaFactoryName4WeekList==null||obaFactoryName4WeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂对比良率相关数据");
                }
                for (String obaFactoryName:obaFactoryName4WeekList){
                    //单个工厂近4周的批通率(y轴)
                    List<String> factroyPassPointList = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                    }
                    obaCompareWrapper
                            .eq("daoc.factory",obaFactoryName)
                            .apply("week(daoc.check_time)>=week(now())-3 ")
                            .apply("week(daoc.check_time)<=week(now())");

                    //当前工厂近4周的批通率
                    List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaBatchPassPointWeek(obaCompareWrapper,timeWrapper);
                    for (DfAoiObaCompare dfAoiObaCompare:obaCompareList){
                        String passPoint = "0.00";
                        if (dfAoiObaCompare.getPassPoint()!=null){
                            passPoint = dfAoiObaCompare.getPassPoint();
                        }
                        factroyPassPointList.add(passPoint);
                    }
                    //当前工厂的近4周的批通率加入集合
                    passPointList.add(factroyPassPointList);
                }
                map.put("obaFactoryNameList",obaFactoryName4WeekList);
                break;
        }
        map.put("timeListX",timeList);
        map.put("passPointListY",passPointList);

        return new Result(200,"获取AOI OBA工厂对比批通率数据成功",map);
    }


    @RequestMapping(value = "getObaDefectPoint",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的OBA工厂对比不良率和top5不良率")
    public Result getObaDefectPoint(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("时间类型")@RequestParam(required = false) String timeType
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime){
        Map<String,Object> map = new HashMap<>();
        //工厂（x轴）
        List<String> factoryList = new ArrayList<>();

        //不良率（y轴）
        List<Object> defectPointList = new ArrayList<>();

        //不良率top（y轴）
        List<Object> defectPointTop5List = new ArrayList<>();

        QueryWrapper<DfAoiObaCompare> defectPointWrapper = new QueryWrapper<>();
        QueryWrapper<String> defectNameWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            defectPointWrapper.eq("daoc.factory",factory);
            defectNameWrapper.eq("daoc.factory",factory);
        }
        if (StringUtils.isNotEmpty(project)){
            defectPointWrapper.eq("daoc.`type`",project);
            defectNameWrapper.eq("daoc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            defectPointWrapper.eq("daoc.colour",colour);
            defectNameWrapper.eq("daoc.colour",colour);
        }

        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("month(daoc.check_time)>="+startTime);
                    defectNameWrapper.apply("month(daoc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("month(daoc.check_time)<="+endTime);
                    defectNameWrapper.apply("month(daoc.check_time)>="+startTime);
                }

                //所有工厂一段时间（月）内不良率
                List<DfAoiObaCompare> defectPointMonthList = dfAoiObaCompareService.getObaDefectPoint(defectPointWrapper);
                if (defectPointMonthList==null||defectPointMonthList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂相关的不良率");
                }

                for (DfAoiObaCompare dfAoiObaCompare:defectPointMonthList){
                    //不良率加入集合
                    defectPointList.add(dfAoiObaCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiObaCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameMonthList = dfAoiObaCompareService.getObaDefectNameList(defectNameWrapper);
                for (String defectName:defectNameMonthList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        obaCompareWrapper.eq("daoc.factory",factory);
                        obaCompareWrapper2.apply("daoc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                        obaCompareWrapper2.apply("daoc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                        obaCompareWrapper2.apply("daoc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        obaCompareWrapper.apply("month(daoc.check_time)>="+startTime);
                        obaCompareWrapper2.apply("month(daoc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        obaCompareWrapper.apply("month(daoc.check_time)<="+endTime);
                        obaCompareWrapper2.apply("month(daoc.check_time)>="+startTime);
                    }

                    obaCompareWrapper
                            .eq("daod.defect_name",defectName);

                    //该缺陷在一段时间内(月)所有工厂得不良率
                    List<DfAoiObaCompare> oneDefectPointList = dfAoiObaCompareService.getObaOneDefectPointList(obaCompareWrapper,obaCompareWrapper2);
                    for (DfAoiObaCompare dfAoiObaCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiObaCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiObaCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameMonthList);


                break;
            case "周":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("week(daoc.check_time)>="+startTime);
                    defectNameWrapper.apply("week(daoc.check_time)>="+startTime);
                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("week(daoc.check_time)<="+endTime);
                    defectNameWrapper.apply("week(daoc.check_time)<="+endTime);
                }

                //所有工厂一段时间（周）内不良率
                List<DfAoiObaCompare> defectPointWeekList = dfAoiObaCompareService.getObaDefectPoint(defectPointWrapper);
                if (defectPointWeekList==null||defectPointWeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂相关的不良率");
                }

                for (DfAoiObaCompare dfAoiObaCompare:defectPointWeekList){
                    //不良率加入集合
                    defectPointList.add(dfAoiObaCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiObaCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameWeekList = dfAoiObaCompareService.getObaDefectNameList(defectNameWrapper);
                for (String defectName:defectNameWeekList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        obaCompareWrapper.eq("daoc.factory",factory);
                        obaCompareWrapper2.apply("daoc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                        obaCompareWrapper2.apply("daoc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                        obaCompareWrapper2.apply("daoc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        obaCompareWrapper.apply("week(daoc.check_time)>="+startTime);
                        obaCompareWrapper2.apply("week(daoc.check_time)>="+startTime);
                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        obaCompareWrapper.apply("week(daoc.check_time)<="+endTime);
                        obaCompareWrapper2.apply("week(daoc.check_time)<="+endTime);
                    }

                    obaCompareWrapper
                            .eq("daod.defect_name",defectName);

                    //该缺陷在一段时间内(近四周)所有工厂得不良率
                    List<DfAoiObaCompare> oneDefectPointList = dfAoiObaCompareService.getObaOneDefectPointList(obaCompareWrapper,obaCompareWrapper2);
                    for (DfAoiObaCompare dfAoiObaCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiObaCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiObaCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameWeekList);

                break;

            case "天":
                if (StringUtils.isNotEmpty(startTime)){
                    defectPointWrapper.apply("day(daoc.check_time)>="+startTime);
                    defectNameWrapper.apply("day(daoc.check_time)>="+startTime);

                }
                if (StringUtils.isNotEmpty(endTime)){
                    defectPointWrapper.apply("day(daoc.check_time)<="+endTime);
                    defectNameWrapper.apply("day(daoc.check_time)>="+startTime);
                }

                //所有工厂一段时间（天）内不良率
                List<DfAoiObaCompare> defectPointDayList = dfAoiObaCompareService.getObaDefectPoint(defectPointWrapper);
                if (defectPointDayList==null||defectPointDayList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂相关的不良率");
                }

                for (DfAoiObaCompare dfAoiObaCompare:defectPointDayList){
                    //不良率加入集合
                    defectPointList.add(dfAoiObaCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiObaCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNameList = dfAoiObaCompareService.getObaDefectNameList(defectNameWrapper);
                for (String defectName:defectNameList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        obaCompareWrapper.eq("daoc.factory",factory);
                        obaCompareWrapper2.apply("daoc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                        obaCompareWrapper2.apply("daoc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                        obaCompareWrapper2.apply("daoc.colour='"+colour+"'");
                    }
                    if (StringUtils.isNotEmpty(startTime)){
                        obaCompareWrapper.apply("day(daoc.check_time)>="+startTime);
                        obaCompareWrapper2.apply("day(daoc.check_time)>="+startTime);

                    }
                    if (StringUtils.isNotEmpty(endTime)){
                        obaCompareWrapper.apply("day(daoc.check_time)<="+endTime);
                        obaCompareWrapper2.apply("day(daoc.check_time)>="+startTime);
                    }

                    obaCompareWrapper
                            .eq("daod.defect_name",defectName);

                    //该缺陷在一段时间内(天)所有工厂得不良率
                    List<DfAoiObaCompare> oneDefectPointList = dfAoiObaCompareService.getObaOneDefectPointList(obaCompareWrapper,obaCompareWrapper2);

                    for (DfAoiObaCompare dfAoiObaCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiObaCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiObaCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNameList);
                break;

            case "近4周":
                defectPointWrapper
                        .apply("week(daoc.check_time)>=week(now())-3")
                        .apply("week(daoc.check_time)<=week(now())");
                defectNameWrapper
                        .apply("week(daoc.check_time)>=week(now())-3")
                        .apply("week(daoc.check_time)<=week(now())");
                //所有工厂近4周的不良率
                List<DfAoiObaCompare> defectPoint4WeekList = dfAoiObaCompareService.getObaDefectPoint(defectPointWrapper);
                if (defectPoint4WeekList==null||defectPoint4WeekList.size()==0){
                    return new Result(500,"该条件下没有OBA工厂相关得不良率");
                }

                for (DfAoiObaCompare dfAoiObaCompare:defectPoint4WeekList){

                    //不良率加入集合
                    defectPointList.add(dfAoiObaCompare.getDefectPoint());
                    //工厂加入集合
                    factoryList.add(dfAoiObaCompare.getFactory());
                }

                //所有top5缺陷名
                List<String> defectNam4WeekeList = dfAoiObaCompareService.getObaDefectNameList(defectNameWrapper);
                for (String defectName:defectNam4WeekeList){
                    //该缺陷在一段时间内(月)所占所有工厂的不良率
                    List<String> oneDefectPointListNew = new ArrayList<>();

                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper = new QueryWrapper<>();
                    QueryWrapper<DfAoiObaCompare> obaCompareWrapper2 = new QueryWrapper<>();

                    if (StringUtils.isNotEmpty(factory)){
                        obaCompareWrapper.eq("daoc.factory",factory);
                        obaCompareWrapper2.apply("daoc.factory='"+factory+"'");
                    }
                    if (StringUtils.isNotEmpty(project)){
                        obaCompareWrapper.eq("daoc.`type`",project);
                        obaCompareWrapper2.apply("daoc.`type`='"+project+"'");
                    }
                    if (StringUtils.isNotEmpty(colour)){
                        obaCompareWrapper.eq("daoc.colour",colour);
                        obaCompareWrapper2.apply("daoc.colour='"+colour+"'");
                    }

                    obaCompareWrapper
                            .eq("daod.defect_name",defectName)
                            .apply("week(daoc.check_time)>=week(now())-3")
                            .apply("week(daoc.check_time)<=week(now())");

                    obaCompareWrapper2
                            .apply("week(daoc.check_time)>=week(now())-3")
                            .apply("week(daoc.check_time)<=week(now())");

                    //该缺陷在一段时间内(近四周)所有工厂得不良率
                    List<DfAoiObaCompare> oneDefectPointList = dfAoiObaCompareService.getObaOneDefectPointList(obaCompareWrapper,obaCompareWrapper2);

                    for (DfAoiObaCompare dfAoiObaCompare:oneDefectPointList){
                        String defectPoint = "0.00";
                        if (dfAoiObaCompare.getDefectPoint()!=null){
                            defectPoint = dfAoiObaCompare.getDefectPoint();
                        }

                        //该缺陷所占一个工厂的不良率加入集合
                        oneDefectPointListNew.add(defectPoint);
                    }

                    //该缺陷所占所有工厂的不良率加入集合
                    defectPointTop5List.add(oneDefectPointListNew);
                }
                map.put("defectNameList",defectNam4WeekeList);
                break;
        }
        map.put("factoryListX",factoryList);
        map.put("defectPointListY",defectPointList);
        map.put("defectPointTop5ListY",defectPointTop5List);

        return new Result(200,"获取AOI OBA工厂对比不良率数据成功",map);
    }


    @RequestMapping(value = "getObaDefectPointTop5",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的OBA不良TOP5")
    public Result getObaDefectPointTop5(
            @ApiParam("工厂")@RequestParam(required = false) String factory
            ,@ApiParam("项目")@RequestParam(required = false) String project
            ,@ApiParam("颜色")@RequestParam(required = false) String colour
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {

        //所有型号的不良top5的不良名称及其不良占比
        List<Object> obaTypeMapList = new ArrayList<>();

        QueryWrapper<String> obaTypeWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            obaTypeWrapper.eq("daoc.factory",factory);
        }
        if (StringUtils.isNotEmpty(project)){
            obaTypeWrapper.eq("daoc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            obaTypeWrapper.eq("daoc.colour",colour);
        }
        if (StringUtils.isNotEmpty(startTime)){
            obaTypeWrapper.ge("daoc.check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            obaTypeWrapper.le("daoc.check_time",endTime);
        }

        QueryWrapper<DfAoiObaCompare> obaTimeRecentlyWrapper = new QueryWrapper<>();
        obaTimeRecentlyWrapper
                .orderByDesc("check_time")
                .last("limit 1");
        DfAoiObaCompare obaRecently = dfAoiObaCompareService.getOne(obaTimeRecentlyWrapper);
        //有最新数据的时间
        String obaTimeRecently = obaRecently.getCheckTime();

        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            obaTypeWrapper.eq("daoc.check_time",obaTimeRecently);
        }

        //获取一段时间内的型号
        List<String> obaTypeList = dfAoiObaCompareService.getObaTypeList(obaTypeWrapper);
        if (obaTypeList==null||obaTypeList.size()==0){
            return new Result(500,"该条件下没有OBA不良TOP5相关数据");
        }

        for (String obaType:obaTypeList){
            Map<String,Object> oneTypeMap = new HashMap<>();
            oneTypeMap.put("obaType",obaType);

            //该型号的top5缺陷名称
            List<String> defectNameTop5List = new ArrayList<>();
            //该型号的top5占比
            List<String> defectPointTop5List = new ArrayList<>();

            QueryWrapper<DfAoiObaCompare> defectPointTop5Wrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(factory)){
                defectPointTop5Wrapper.eq("daoc.factory",factory);
            }
            if (StringUtils.isNotEmpty(project)){
                defectPointTop5Wrapper.eq("daoc.`type`",project);
            }
            if (StringUtils.isNotEmpty(colour)){
                defectPointTop5Wrapper.eq("daoc.colour",colour);
            }
            if (StringUtils.isNotEmpty(startTime)){
                defectPointTop5Wrapper.ge("daoc.check_time",startTime);
            }
            if (StringUtils.isNotEmpty(endTime)){
                defectPointTop5Wrapper.le("daoc.check_time",endTime);
            }
            if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
                defectPointTop5Wrapper.eq("daoc.check_time",obaTimeRecently);
            }
            defectPointTop5Wrapper
                    .eq("daoc.`type`",obaType);

            List<DfAoiObaCompare> obaDefectPointTop5 = dfAoiObaCompareService.getObaDefectPointTop5List(defectPointTop5Wrapper);

            for (DfAoiObaCompare dfAoiObaCompare:obaDefectPointTop5){
                //当前型号的缺陷名称加入集合
                defectNameTop5List.add(dfAoiObaCompare.getDefectName());
                //当前型号的缺陷不良占比加入集合
                defectPointTop5List.add(dfAoiObaCompare.getDefectPoint());
            }

            oneTypeMap.put("defectNameListX",defectNameTop5List);
            oneTypeMap.put("defectPointTop5ListY",defectPointTop5List);

            obaTypeMapList.add(oneTypeMap);
        }

        return new Result(200,"获取一段时间内的OBA不良TOP5相关数据成功",obaTypeMapList);
    }

    @RequestMapping(value = "getObaPassPointAndBatch",method = RequestMethod.GET)
    @ApiOperation("获取一段时间内的OBA良率和批通率")
    public Result getObaPassPointAndBatch(
        @ApiParam("工厂")@RequestParam(required = false) String factory
        ,@ApiParam("项目")@RequestParam(required = false) String project
        ,@ApiParam("颜色")@RequestParam(required = false) String colour
        ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
        ,@ApiParam("结束时间")@RequestParam(required = false) String endTime) {

        Map<String,Object> map = new HashMap<>();

        //型号集合（x轴）
        List<String> typeList = new ArrayList<>();

        //良率集合（y轴）
        List<String> passPointList = new ArrayList<>();

        //批通率集合（y轴）
        List<String> batchPassPointList = new ArrayList<>();

        QueryWrapper<DfAoiObaCompare> obaPassPointAndBatchWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factory)){
            obaPassPointAndBatchWrapper.eq("daoc.factory",factory);
        }
        if (StringUtils.isNotEmpty(project)){
            obaPassPointAndBatchWrapper.eq("daoc.`type`",project);
        }
        if (StringUtils.isNotEmpty(colour)){
            obaPassPointAndBatchWrapper.eq("daoc.colour",colour);
        }
        if (StringUtils.isNotEmpty(startTime)){
            obaPassPointAndBatchWrapper.ge("daoc.check_time",startTime);
        }
        if (StringUtils.isNotEmpty(endTime)){
            obaPassPointAndBatchWrapper.le("daoc.check_time",endTime);
        }

        QueryWrapper<DfAoiObaCompare> obaTimeRecentlyWrapper = new QueryWrapper<>();
        obaTimeRecentlyWrapper
                .orderByDesc("check_time")
                .last("limit 1");
        DfAoiObaCompare obaRecently = dfAoiObaCompareService.getOne(obaTimeRecentlyWrapper);
        if (obaRecently==null){
            return new Result(500,"最近没有相关数据");
        }

        //有最新数据的时间
        String obaTimeRecently = obaRecently.getCheckTime();

        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            obaPassPointAndBatchWrapper.eq("daoc.check_time",obaTimeRecently);
        }

        List<DfAoiObaCompare> obaCompareList = dfAoiObaCompareService.getObaPassPointAndBatchList(obaPassPointAndBatchWrapper);
        for (DfAoiObaCompare dfAoiObaCompare :obaCompareList){

            //型号加入集合中
            typeList.add(dfAoiObaCompare.getType());
            //良率加入集合中
            passPointList.add(dfAoiObaCompare.getPassPoint());
            //批通率加入集合中
            batchPassPointList.add(dfAoiObaCompare.getBatchPassPoint());

        }

        map.put("typeListX",typeList);
        map.put("passPointListY",passPointList);
        map.put("batchPassPointListY",batchPassPointList);

        return new Result(200,"获取OBA良率和批通率成功",map);
    }
}
