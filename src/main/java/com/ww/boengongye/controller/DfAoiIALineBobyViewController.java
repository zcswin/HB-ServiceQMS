package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAoiConfigDataService;
import com.ww.boengongye.service.DfAoiPieceService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//import javax.xml.transform.Source;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/dfAoiIALineBobyView")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI IA线体看板")
public class DfAoiIALineBobyViewController {

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private UserService userService;

    @Autowired
    private DfAoiConfigDataService dfAoiConfigDataService;


    /**
     * 通过条件获取所有员工的漏检占比
     * @param factoryId
     * @param lineBobyId
     * @param startTime
     * @param endTime
     * @param projectId
     * @param colour
     * @return
     */
    @ApiOperation("获取所有员工的漏检占比信息")
    @RequestMapping(value = "countAllUserEscape",method = RequestMethod.GET)
    public Result<List<User>> countAllUserEscape(Integer page, Integer limit
            ,@ApiParam("工厂id")@RequestParam(required = false) String factoryId
            ,@ApiParam("线体id")@RequestParam(required = false) String lineBobyId
            ,@ApiParam("开始时间")@RequestParam(required = false) String startTime
            ,@ApiParam("结束时间")@RequestParam(required = false) String endTime
            ,@ApiParam("项目id")@RequestParam(required = false) String projectId
            ,@ApiParam("颜色")@RequestParam(required = false) String colour){
        Page<User> pages = new Page<>(page,limit);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(factoryId)){
            userQueryWrapper.eq("u.factory_id",factoryId);
        }

        if (StringUtils.isNotEmpty(lineBobyId)){
            userQueryWrapper.eq("dasp.line_boby_id",lineBobyId);
        }

        if (StringUtils.isNotEmpty(startTime)){
            userQueryWrapper.ge("dadl_new3.qc_time",startTime);
        }

        if (StringUtils.isNotEmpty(endTime)){
            userQueryWrapper.le("dadl_new3.qc_time",endTime);
        }

        if (StringUtils.isNotEmpty(projectId)){
            userQueryWrapper.eq("decp.project_id",projectId);
        }

        if (StringUtils.isNotEmpty(colour)){
            userQueryWrapper.eq("decp.colour",colour);
        }

        //所有FQC员工被OQC检测过的玻璃总数集合
        List<User> userList = dfAoiPieceService.getAllUserOQCNumberList(pages,userQueryWrapper);
        int totalPage = (int) pages.getPages();

        if (userList==null||userList.size()==0){
            return new Result<List<User>>(500,"该条件下没有员工的漏检占比信息");
        }

        //AOI配置基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();


        for (User user:userList){
            if (user.getProcess()=="OQC"){
                continue;
            }
            if (user.getOqcNumber()==null){
                user.setOqcNumber(0);
                user.setEscapeNumber(0);
                user.setEscapePoint("0.00");
                continue;
            }
            QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(factoryId)){
                integerQueryWrapper.eq("u.factory_id",factoryId);
            }
            if (StringUtils.isNotEmpty(lineBobyId)){
                integerQueryWrapper.eq("dasp.line_boby_id",lineBobyId);
            }
            if (StringUtils.isNotEmpty(startTime)){
                integerQueryWrapper.ge("dadl_new1.qc_time",startTime);
            }
            if (StringUtils.isNotEmpty(endTime)){
                integerQueryWrapper.le("dadl_new1.qc_time",endTime);
            }
            if (StringUtils.isNotEmpty(projectId)){
                integerQueryWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour)){
                integerQueryWrapper.eq("decp.colour",colour);
            }
            integerQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("u.name",user.getName())
                    .last("limit 1");
            //当前员工漏检数量
            Integer escapeNumber = dfAoiPieceService.getUserEscapeNumber(integerQueryWrapper);
            //当前员工漏检占比
            String escapePoint = String.format("%.2f", (float) escapeNumber / (float) user.getOqcNumber() * 100 * escapeData);
            user.setEscapeNumber(escapeNumber);
            user.setEscapePoint(escapePoint);
        }
        return new Result<List<User>>(200,"获取所有员工漏检成功",totalPage,userList);
    }

    /**
     * 获取当前员工当天投入产出等基本信息
     * @param userCode
     * @return
     */
    @ApiOperation("获取当前员工当天投入产出等基本信息")
    @RequestMapping(value = "countUserEscapeBase",method = RequestMethod.GET)
    public Result<User> countUserEscapeBase(
            @ApiParam("员工工号")@RequestParam(required = false)String userCode
            ,@ApiParam("项目id")@RequestParam(required = false)String projectId
            ,@ApiParam("颜色")@RequestParam(required = false)String colour){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .eq("name",userCode)
                .last("limit 1");
        //员工信息
        User user =userService.getOne(userQueryWrapper);

        QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
        integerQueryWrapper
                .eq("first_qc.num",1)
                .eq("first_qc.qc_user_code",userCode)
                .apply("date_sub(first_qc.qc_time,interval 7 hour) BETWEEN CONCAT(date(date_sub(now() ,interval 7 hour)),' 00:00:00') AND CONCAT(date(date_sub(now() ,interval 7 hour)),' 23:59:59')");
//                .apply("date_sub(first_qc.qc_time,interval 7 hour) BETWEEN CONCAT(CURDATE(),' 00:00:00') AND CONCAT(CURDATE(),' 23:59:59')");

        QueryWrapper<User> oqcNumberWrapper = new QueryWrapper<>();
        oqcNumberWrapper
                .eq("u.name",userCode)
                .apply("date_sub(dadl_new3.qc_time,interval 7 hour) BETWEEN CONCAT(date(date_sub(now() ,interval 7 hour)),' 00:00:00') AND CONCAT(date(date_sub(now() ,interval 7 hour)),' 23:59:59')");

        QueryWrapper<Integer> escapeNumberWrapper = new QueryWrapper<>();
        escapeNumberWrapper
                .eq("dadl_new1.num",1)
                .eq("u.name",userCode)
                .apply("date_sub(dadl_new1.qc_time,interval 7 hour) BETWEEN CONCAT(date(date_sub(now() ,interval 7 hour)),' 00:00:00') AND CONCAT(date(date_sub(now() ,interval 7 hour)),' 23:59:59')");

        if (StringUtils.isNotEmpty(projectId)){
            integerQueryWrapper.eq("decp.project_id",projectId);
            oqcNumberWrapper.eq("decp.project_id",projectId);
            escapeNumberWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour)){
            integerQueryWrapper.eq("decp.colour",colour);
            oqcNumberWrapper.eq("decp.colour",colour);
            escapeNumberWrapper.eq("decp.colour",colour);
        }

        //当前员工投入数量
        Integer inputNumber = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);
        if (inputNumber==0){
            return new Result<User>(500,"该条件下没有当前员工当天投入产出等基本信息");
        }
        integerQueryWrapper.eq("first_qc.qc_result","OK");

        //当前员工产出数量
        Integer outputNumber = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);

        //当天的良率
        String passPoint = String.format("%.2f", (float) outputNumber / (float) inputNumber * 100);

        //当前员工当天投入玻璃被OQC检测数量
        Page<User> userPage = new Page<>(1,5);
        List<User> userOqcNumberList = dfAoiPieceService.getAllUserOQCNumberList(userPage,oqcNumberWrapper);
        Integer oqcNumber = 0;

        //当前员工当天漏检数量
        Integer escapeNumber = 0;

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        //当前员工当天漏检率
        String escapePoint = "0.00";
        if (userOqcNumberList!=null&&userOqcNumberList.size()>0){
            User userOqc = userOqcNumberList.get(0);
            oqcNumber = userOqc.getOqcNumber();
            escapeNumber = dfAoiPieceService.getUserEscapeNumber(escapeNumberWrapper);
            escapePoint = String.format("%.2f", (float) escapeNumber / (float) oqcNumber * 100 * escapeData);

        }
        user.setInputNumber(inputNumber);
        user.setOutputNumber(outputNumber);
        user.setPassPoint(passPoint);
        user.setOqcNumber(oqcNumber);
        user.setEscapeNumber(escapeNumber);
        user.setEscapePoint(escapePoint);
        return new Result<User>(200,"获取当前员工投入产出成功",user);
    }

    /**
     * 获取当前员工当天每小时投入产出
     * @param userCode
     * @return
     */
    @ApiOperation("获取当前员工当天每小时投入产出")
    @RequestMapping(value = "countUserEscapeHour",method = RequestMethod.GET)
    public Result<Map<String, Object>> countUserEscapeHour(
            @ApiParam("员工工号")@RequestParam(required = false)String userCode
            ,@ApiParam("项目id")@RequestParam(required = false)String projectId
            ,@ApiParam("颜色")@RequestParam(required = false)String colour){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();
        //投入（y轴）
        List<String> inputNumberList = new ArrayList<>();
        //产出（y轴）
        List<String> outputNumberList = new ArrayList<>();
        //良率（y轴）
        List<String> passPointList = new ArrayList<>();

        QueryWrapper<DfAoiOutputPoint> outputPointWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(projectId)){
            outputPointWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour)){
            outputPointWrapper.eq("decp.colour",colour);
        }

        outputPointWrapper.eq("first_qc.num",1)
                .eq("first_qc.qc_user_code",userCode)
                .apply("date_sub(first_qc.qc_time,interval 7 hour) BETWEEN CONCAT(date(date_sub(now() ,interval 7 hour)),' 00:00:00') AND CONCAT(date(date_sub(now() ,interval 7 hour)),' 23:59:59')");

        //当前员工当天每小时投入数量
        List<DfAoiOutputPoint> userHourOutputPointList = dfAoiPieceService.getUserHourOutputPointList(outputPointWrapper);
        Map<String,Object> dfAoiOutputPointMap = new HashMap<>();
        for (DfAoiOutputPoint dfAoiOutputPoint:userHourOutputPointList){
            QueryWrapper<DfAoiOutputPoint> outputNumberWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(projectId)){
                outputNumberWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour)){
                outputNumberWrapper.eq("decp.colour",colour);
            }

            outputNumberWrapper.eq("first_qc.num",1)
                    .eq("first_qc.qc_user_code",userCode)
                    .eq("first_qc.qc_result","OK")
                    .eq("date_format(date_add(first_qc.qc_time,interval 1 hour),'%Y-%m-%d %H')",dfAoiOutputPoint.getInputTime());

            //当前时间的产出数量
            List<DfAoiOutputPoint> outputList = dfAoiPieceService.getUserHourOutputPointList(outputNumberWrapper);

            String passPoint = "0.00";
            Integer outputNumber = 0;
            if (outputList!=null&&outputList.size()>0){
                DfAoiOutputPoint output = outputList.get(0);
                outputNumber = output.getInputNumber();
                passPoint = String.format("%.2f", (float) outputNumber / (float) dfAoiOutputPoint.getInputNumber() * 100);
            }
            dfAoiOutputPoint.setOutputNumer(outputNumber);
            dfAoiOutputPoint.setPassPoint(passPoint);
            String inputTime = dfAoiOutputPoint.getInputTime().substring(11)+":00";
            dfAoiOutputPoint.setInputTime(inputTime);
            dfAoiOutputPointMap.put(dfAoiOutputPoint.getInputTime(),dfAoiOutputPoint);

            //时间加入集合
            timeList.add(inputTime);
            //投入加入集合
            inputNumberList.add(String.valueOf(dfAoiOutputPoint.getInputNumber()));
            //产出加入集合
            outputNumberList.add(String.valueOf(outputNumber));
            //良率
            passPointList.add(passPoint);
        }

        map.put("timeListX",timeList);
        map.put("inputNumberListY",inputNumberList);
        map.put("outputNumberListY",outputNumberList);
        map.put("passPointListY",passPointList);

        return new Result<Map<String, Object>>(200,"获取当前员工投入产出成功",map);
    }

    /**
     * 获取当前FQC员工近7天的天top5漏检率和天总漏检率
     * @param userCode
     * @return
     */
    @ApiOperation("获取当前员工近7天的天top5漏检率和天总漏检率")
    @RequestMapping(value = "countUserEscapeDay",method = RequestMethod.GET)
    public Result<Map<String, Object>> countUserEscapeDay(
            @ApiParam("员工工号")@RequestParam(required = false)String userCode
            ,@ApiParam("项目id")@RequestParam(required = false)String projectId
            ,@ApiParam("颜色")@RequestParam(required = false)String colour){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();
        //近7天的漏检率(y轴)
        List<String> escapePointList = new ArrayList<>();
        //近7天所有漏检的缺陷占比
        List<Object> defectList = new ArrayList<>();


        QueryWrapper<DfAoiEscape> dfAoiEscapeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<String> defect7DayWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(projectId)){
            dfAoiEscapeQueryWrapper.eq("decp.project_id",projectId);
            defect7DayWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour)){
            dfAoiEscapeQueryWrapper.eq("decp.colour",colour);
            defect7DayWrapper.eq("decp.colour",colour);
        }

        dfAoiEscapeQueryWrapper
                .eq("dadl_new1.num",1)
                .eq("dadl_new2.num",1)
                .eq("dadl_new1.qc_user_code",userCode)
                .apply("DATE_SUB(date(date_sub(now() ,interval 7 hour)), INTERVAL 7 DAY) < date_sub(dadl_new1.qc_time,interval 7 hour)")
                .groupBy("date(date_sub(dadl_new1.qc_time,interval 7 hour))")
                .orderByAsc("date(date_sub(dadl_new1.qc_time,interval 7 hour))");

        defect7DayWrapper
                .eq("dadl_new1.num",1)
                .eq("dau.fqc_user",userCode)
                .apply("DATE_SUB(date(date_sub(now() ,interval 7 hour)), INTERVAL 7 DAY) < date_sub(dadl_new1.qc_time,interval 7 hour)")
                .groupBy("u.alias","dad.featurevalues","date(date_sub(dadl_new1.qc_time,interval 7 hour))");

        //当前FQC员工近7天每天的OQC检测数量
        List<DfAoiEscape> dfAoiEscapeDays = dfAoiPieceService.getFqcUserOqcNumberDay(dfAoiEscapeQueryWrapper);
        if (dfAoiEscapeDays==null||dfAoiEscapeDays.size()==0){
            return new Result<Map<String, Object>>(500,"该条件下没有当前员工近7天的天top5漏检率和天总漏检率等信息");
        }

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();


        for (DfAoiEscape dfAoiEscapeDay:dfAoiEscapeDays){
            QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
            QueryWrapper<DfAoiEscapePoint> escapeTop5PointQueryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(projectId)){
                integerQueryWrapper.eq("decp.project_id",projectId);
                escapeTop5PointQueryWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour)){
                integerQueryWrapper.eq("decp.colour",colour);
                escapeTop5PointQueryWrapper.eq("decp.colour",colour);
            }
            integerQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("u.name",userCode)
                    .eq("date(date_sub(dadl_new1.qc_time,interval 7 hour))",dfAoiEscapeDay.getEscapeTime());
            escapeTop5PointQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("u.name",userCode)
                    .eq("date(date_sub(dadl_new1.qc_time,interval 7 hour))",dfAoiEscapeDay.getEscapeTime());

            //当前员工该天漏检数量
            Integer escapeNumber = dfAoiPieceService.getUserEscapeNumber(integerQueryWrapper);
            //当前员工该天漏检占比
            String escapePoint = String.format("%.2f", (float) escapeNumber / (float) dfAoiEscapeDay.getOqcNumber() * 100 * escapeData);

            //当前员工该天漏检top5总数
            Integer escapeTop5Number = 0;
            //获取当前员工该天的top5漏检及其对应的漏检数量
            List<DfAoiEscapePoint> dfAoiEscapeTop5 = dfAoiPieceService.getEscapeTop5PointList(escapeTop5PointQueryWrapper);

            if (dfAoiEscapeTop5!=null&&dfAoiEscapeTop5.size()>0){
                for (DfAoiEscapePoint dfAoiEscapePoint:dfAoiEscapeTop5){
                    String escapePointTop = String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) dfAoiEscapeDay.getOqcNumber() * 100 * escapeData);
                    dfAoiEscapePoint.setEscapePoint(escapePointTop);
                    escapeTop5Number+=dfAoiEscapePoint.getEscapeNumber();
                }
            }
            //当前员工该天top5漏检占比
            String escapeTop5Point = String.format("%.2f", (float) escapeTop5Number / (float) dfAoiEscapeDay.getOqcNumber() * 100 * escapeData);
            dfAoiEscapeDay.setEscapeTop5Point(escapeTop5Point);
            dfAoiEscapeDay.setEscapePoint(escapePoint);
            dfAoiEscapeDay.setDfAoiEscapePoints(dfAoiEscapeTop5);

            //该天漏检时间加入集合
            timeList.add(dfAoiEscapeDay.getEscapeTime());
            //该天漏检率加入集合
            escapePointList.add(escapePoint);
        }


        //近7天中所有每天的Top5的缺陷名称
        List<String> defect7DayList = dfAoiPieceService.getFqcUserDefect7Day(defect7DayWrapper);
        for (int i= 0;i<defect7DayList.size();i++){
            List<String> list = new ArrayList<>();
            //7天中每天的漏检信息
            for (DfAoiEscape dfAoiEscapeDay:dfAoiEscapeDays){
                String escapePoint = "0.00";
                //该天的漏检的所有缺陷
                for (DfAoiEscapePoint dfAoiEscapePoint : dfAoiEscapeDay.getDfAoiEscapePoints()){
                    if (dfAoiEscapePoint.getEscapeName().equals(defect7DayList.get(i))){
                        escapePoint = dfAoiEscapePoint.getEscapePoint();
                        break;
                    }
                }
                list.add(escapePoint);
            }
            defectList.add(list);
        }
        map.put("defectListY",defectList);
        map.put("defectNameList",defect7DayList);
        map.put("timeListX",timeList);
        map.put("escapePointListY",escapePointList);

        return new Result<Map<String, Object>>(200,"获取当前员工近7天的天top5漏检率和天总漏检率成功",map);
    }



    /**
     * 获取当前员工近4周的周top5漏检率和周总漏检率
     * @param userCode
     * @return
     */
    @ApiOperation("获取当前员工近4周的周top5漏检率和周总漏检率")
    @RequestMapping(value = "countUserEscapeWeek",method = RequestMethod.GET)
    public Result<Map<String, Object>> countUserEscapeWeek(
            @ApiParam("员工工号")@RequestParam(required = false)String userCode
            ,@ApiParam("项目id")@RequestParam(required = false)String projectId
            ,@ApiParam("颜色")@RequestParam(required = false)String colour){
        Map<String,Object> map = new HashMap<>();
        //时间（x轴）
        List<String> timeList = new ArrayList<>();
        //近4周的漏检率(y轴)
        List<String> escapePointList = new ArrayList<>();
        //近4周所有漏检的缺陷占比
        List<Object> defectList = new ArrayList<>();


        QueryWrapper<DfAoiEscape> dfAoiEscapeQueryWrapper = new QueryWrapper<>();
        QueryWrapper<String> defect4WeekWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(projectId)){
            dfAoiEscapeQueryWrapper.eq("decp.project_id",projectId);
            defect4WeekWrapper.eq("decp.project_id",projectId);
        }
        if (StringUtils.isNotEmpty(colour)){
            dfAoiEscapeQueryWrapper.eq("decp.colour",colour);
            defect4WeekWrapper.eq("decp.colour",colour);
        }

        dfAoiEscapeQueryWrapper
                .eq("dadl_new1.num",1)
                .eq("dadl_new2.num",1)
                .eq("dadl_new1.qc_user_code",userCode)
                .apply("yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))-yearweek(curdate())>=-3")
                .groupBy("yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))");

        defect4WeekWrapper
                .eq("dadl_new1.num",1)
                .eq("dau.fqc_user",userCode)
                .apply("yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))-yearweek(curdate())>=-3")
                .groupBy("u.alias","dad.featurevalues","yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))");
        //当前FQC员工近四周的OQC检测数量
        List<DfAoiEscape> dfAoiEscapeWeeks = dfAoiPieceService.getFqcUserOqcNumberWeek(dfAoiEscapeQueryWrapper);
        if (dfAoiEscapeWeeks==null||dfAoiEscapeWeeks.size()==0){
            return new Result<Map<String, Object>>(500,"该条件下没有当前员工近4周的周top5漏检率和周总漏检率等信息");
        }

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        for (DfAoiEscape dfAoiEscapeWeek:dfAoiEscapeWeeks){
            QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
            QueryWrapper<DfAoiEscapePoint> escapeTop5PointQueryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(projectId)){
                integerQueryWrapper.eq("decp.project_id",projectId);
                escapeTop5PointQueryWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour)){
                integerQueryWrapper.eq("decp.colour",colour);
                escapeTop5PointQueryWrapper.eq("decp.colour",colour);
            }
            integerQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("u.name",userCode)
                    .eq("yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))-yearweek(curdate())+4",dfAoiEscapeWeek.getEscapeTime());
            escapeTop5PointQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("u.name",userCode)
                    .eq("yearweek(date_sub(dadl_new1.qc_time,interval 7 hour))-yearweek(curdate())+4",dfAoiEscapeWeek.getEscapeTime());

            //当前员工该周漏检数量
            Integer escapeNumber = dfAoiPieceService.getUserEscapeNumber(integerQueryWrapper);
            //当前员工该周漏检占比
            String escapePoint = String.format("%.2f", (float) escapeNumber / (float) dfAoiEscapeWeek.getOqcNumber() * 100 * escapeData);

            //当前员工该周漏检总数
            Integer escapeTop5Number = 0;
            //获取当前员工该周的top5漏检及其对应的漏检数量
            List<DfAoiEscapePoint> dfAoiEscapeTop5 = dfAoiPieceService.getEscapeTop5PointList(escapeTop5PointQueryWrapper);

            if (dfAoiEscapeTop5!=null&&dfAoiEscapeTop5.size()>0){
                for (DfAoiEscapePoint dfAoiEscapePoint:dfAoiEscapeTop5){
                    String escapePointTop = String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) dfAoiEscapeWeek.getOqcNumber() * 100 * escapeData);
                    dfAoiEscapePoint.setEscapePoint(escapePointTop);
                    escapeTop5Number+=dfAoiEscapePoint.getEscapeNumber();
                }
            }
            //当前员工该周top5漏检占比
            String escapeTop5Point = String.format("%.2f", (float) escapeTop5Number / (float) dfAoiEscapeWeek.getOqcNumber() * 100 * escapeData);
            dfAoiEscapeWeek.setEscapeTime("week"+dfAoiEscapeWeek.getEscapeTime());
            dfAoiEscapeWeek.setEscapeTop5Point(escapeTop5Point);
            dfAoiEscapeWeek.setEscapePoint(escapePoint);
            dfAoiEscapeWeek.setDfAoiEscapePoints(dfAoiEscapeTop5);

            //该周漏检时间加入集合
            timeList.add(dfAoiEscapeWeek.getEscapeTime());
            //该周漏检率加入集合
            escapePointList.add(escapePoint);
        }

        //近4周中所有每周的Top5的缺陷名称
        List<String> defect4WeekList = dfAoiPieceService.getFqcUserDefect4Week(defect4WeekWrapper);
        for (int i= 0;i<defect4WeekList.size();i++){
            List<String> list = new ArrayList<>();
            //4周中每周的漏检信息
            for (DfAoiEscape dfAoiEscapeDay:dfAoiEscapeWeeks){
                String escapePoint = "0.00";
                //该天的漏检的所有缺陷
                for (DfAoiEscapePoint dfAoiEscapePoint : dfAoiEscapeDay.getDfAoiEscapePoints()){
                    if (dfAoiEscapePoint.getEscapeName().equals(defect4WeekList.get(i))){
                        escapePoint = dfAoiEscapePoint.getEscapePoint();
                        break;
                    }
                }
                list.add(escapePoint);
            }
            defectList.add(list);
        }
        map.put("defectListY",defectList);
        map.put("defectNameList",defect4WeekList);
        map.put("timeListX",timeList);
        map.put("escapePointListY",escapePointList);

        return new Result<Map<String, Object>>(200,"获取当前员工近4周的周top5漏检率和周总漏检率",map);
    }



    @RequestMapping(value = "getProjectDefectPointList",method = RequestMethod.GET)
    @ApiOperation("获取IA线体不良看板")
    public Result<List<Object>> getProjectDefectPointList(
            @ApiParam("厂别") @RequestParam(required = false) String factory,
            @ApiParam("线体") @RequestParam(required = false) String lineBody,
            @ApiParam("开始时间") @RequestParam(required = false) String startDate,
            @ApiParam("结束时间") @RequestParam(required = false) String endDate,
            @ApiParam("项目") @RequestParam(required = false) String project,
            @ApiParam("颜色") @RequestParam(required = false) String color
    ) throws ParseException {

        List<Object> list = new ArrayList<>();

        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(factory)){
            ew.eq("dap_new.factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            ew.eq("dap_new.line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(startDate)){
            String startTime = startDate + " 07:00:00";
            ew.ge("dap_new.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endDate)){
            String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
            ew.le("dap_new.`time`",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            ew.eq("dap_new.project",project);
        }
        if (StringUtils.isNotEmpty(color)){
            ew.eq("dap_new.color",color);
        }
        ew.eq("dap_new.num",1);

        //获取所有的项目
        List<String> projectList = dfAoiPieceService.getProjectList(ew);
        if (projectList==null||projectList.size()==0){
            return new Result<List<Object>>(500,"该条件下没有获取IA线体不良看板相关信息");
        }

        for (String oneProject:projectList){
            Map<String,Object> lineBodyMap = new HashMap<>();

            //该项目的缺陷名称集合
            List<Object> defectNameList = new ArrayList<>();

            //该项目的缺陷占比集合
            List<Object> defectPointList = new ArrayList<>();

            QueryWrapper<DfAoiPiece> defectWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(factory)){
                defectWrapper.eq("dap_new.factory",factory);
            }
            if (StringUtils.isNotEmpty(lineBody)){
                defectWrapper.eq("dap_new.line_body",lineBody);
            }
            if (StringUtils.isNotEmpty(startDate)){
                String startTime = startDate + " 07:00:00";
                defectWrapper.ge("dap_new.`time`",startTime);
            }
            if (StringUtils.isNotEmpty(endDate)){
                String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
                defectWrapper.le("dap_new.`time`",endTime);
            }
            if (StringUtils.isNotEmpty(project)){
                defectWrapper.eq("dap_new.project",project);
            }
            if (StringUtils.isNotEmpty(color)){
                defectWrapper.eq("dap_new.color",color);
            }
            defectWrapper.eq("dap_new.num",1)
                    .eq("dap_new.project",oneProject);

            List<Rate3> defectList = dfAoiPieceService.getProjectDefectPointList(defectWrapper);
            for (Rate3 defect:defectList){
                defectNameList.add(defect.getStr1());
                defectPointList.add(defect.getDou1());
            }
            lineBodyMap.put("project",oneProject);
            lineBodyMap.put("defectNameList",defectNameList);
            lineBodyMap.put("defectPointList",defectPointList);
            list.add(lineBodyMap);
        }

        return new Result<List<Object>>(200,"获取IA线体不良看板数据成功",list);
    }


    @RequestMapping(value = "getItemDefectPointList",method = RequestMethod.GET)
    @ApiOperation("IA线体不良趋势")
    public Result<Map<String, Object>> getItemDefectPointList(
            @ApiParam("厂别") @RequestParam(required = false) String factory,
            @ApiParam("线体") @RequestParam(required = false) String lineBody,
            @ApiParam("开始时间") @RequestParam(required = false) String startDate,
            @ApiParam("结束时间") @RequestParam(required = false) String endDate,
            @ApiParam("项目") @RequestParam(required = false) String project,
            @ApiParam("颜色") @RequestParam(required = false) String color,
            @ApiParam("item1") @RequestParam String item1,
            @ApiParam("item2") @RequestParam List<String> item2List,
            @ApiParam("时间类型") @RequestParam String timeType,
            @ApiParam("item开始时间") @RequestParam String itemStartDate,
            @ApiParam("item结束时间") @RequestParam String itemEndDate
    ) throws ParseException {

        Map<String,Object> map = new HashMap<>();

        //时间集合
        LinkedHashSet<Object> timeList = new LinkedHashSet<>();

        //item1的不良率
        List<Object> itemDefectPointList = new ArrayList<>();

        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(factory)){
            ew.eq("dap_new.factory",factory);
        }
        if (StringUtils.isNotEmpty(lineBody)){
            ew.eq("dap_new.line_body",lineBody);
        }
        if (StringUtils.isNotEmpty(startDate)){
            String startTime = startDate + " 07:00:00";
            ew.ge("dap_new.`time`",startTime);
        }
        if (StringUtils.isNotEmpty(endDate)){
            String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
            ew.le("dap_new.`time`",endTime);
        }
        if (StringUtils.isNotEmpty(project)){
            ew.eq("dap_new.project",project);
        }
        if (StringUtils.isNotEmpty(color)){
            ew.eq("dap_new.color",color);
        }
        ew.eq("dap_new.num",1);

        QueryWrapper<String> timeWrapper = new QueryWrapper<>();


        switch (timeType){
            case "月":
                if (StringUtils.isNotEmpty(itemStartDate)){
                    timeWrapper.apply("month(c.datelist) >='"+itemStartDate+"'");
                }
                if (StringUtils.isNotEmpty(itemEndDate)){
                    timeWrapper.apply("month(c.datelist) <='"+itemEndDate+"'");
                }

                for(String item2:item2List){
                    List<Object> oneItemDefectPoint = new ArrayList<>();

                    QueryWrapper<DfAoiPiece> ew2 = new QueryWrapper<>();
                    switch (item1){
                        case "缺陷分组":
                            ew2.apply("defect_new.name ='"+item2+"'");
                            break;
                        case "缺陷名称":
                            ew2.apply("defect_new.featurevalues ='"+item2+"'");
                            break;
                        case "缺陷区域":
                            ew2.apply("defect_new.defect_area ='"+item2+"'");
                            break;
                        case "缺陷位置":
                            ew2.apply("defect_new.area ='"+item2+"'");
                            break;
                    }
                    //该item一段时间（月）的不良率
                    List<Rate3> oneItemDefectPointList = dfAoiPieceService.getItemDefectPointListMonth(ew,ew2,timeWrapper);
                    if (oneItemDefectPointList==null){
                        itemDefectPointList.add(null);
                    }
                    for (Rate3 oneItem:oneItemDefectPointList){
                        timeList.add(oneItem.getStr1()+"mh");
                        String defectPoint = "0.00";
                        if (oneItem.getStr2()!=null){
                            defectPoint = oneItem.getStr2();
                        }
                        oneItemDefectPoint.add(defectPoint);
                    }

                    itemDefectPointList.add(oneItemDefectPoint);
                }
                break;
            case "周":
                if (StringUtils.isNotEmpty(itemStartDate)){
                    timeWrapper.apply("week(c.datelist) >='"+itemStartDate+"'");
                }
                if (StringUtils.isNotEmpty(itemEndDate)){
                    timeWrapper.apply("week(c.datelist) <='"+itemEndDate+"'");
                }

                for(String item2:item2List){
                    List<Object> oneItemDefectPoint = new ArrayList<>();

                    QueryWrapper<DfAoiPiece> ew2 = new QueryWrapper<>();
                    switch (item1){
                        case "缺陷分组":
                            ew2.apply("defect_new.name ='"+item2+"'");
                            break;
                        case "缺陷名称":
                            ew2.apply("defect_new.featurevalues ='"+item2+"'");
                            break;
                        case "缺陷区域":
                            ew2.apply("defect_new.defect_area ='"+item2+"'");
                            break;
                        case "缺陷位置":
                            ew2.apply("defect_new.area ='"+item2+"'");
                            break;
                    }
                    //该item一段时间（周）的不良率
                    List<Rate3> oneItemDefectPointList = dfAoiPieceService.getItemDefectPointListWeek(ew,ew2,timeWrapper);
                    if (oneItemDefectPointList==null){
                        itemDefectPointList.add(null);
                    }
                    for (Rate3 oneItem:oneItemDefectPointList){
                        timeList.add(oneItem.getStr1()+"wk");
                        String defectPoint = "0.00";
                        if (oneItem.getStr2()!=null){
                            defectPoint = oneItem.getStr2();
                        }
                        oneItemDefectPoint.add(defectPoint);
                    }

                    itemDefectPointList.add(oneItemDefectPoint);
                }
                break;
            case "天":
                if (StringUtils.isNotEmpty(itemStartDate)){
                    timeWrapper.apply("day(c.datelist) >='"+itemStartDate+"'");
                }
                if (StringUtils.isNotEmpty(itemEndDate)){
                    timeWrapper.apply("day(c.datelist) <='"+itemEndDate+"'");
                }

                for(String item2:item2List){
                    List<Object> oneItemDefectPoint = new ArrayList<>();

                    QueryWrapper<DfAoiPiece> ew2 = new QueryWrapper<>();
                    switch (item1){
                        case "缺陷分组":
                            ew2.apply("defect_new.name ='"+item2+"'");
                            break;
                        case "缺陷名称":
                            ew2.apply("defect_new.featurevalues ='"+item2+"'");
                            break;
                        case "缺陷区域":
                            ew2.apply("defect_new.defect_area ='"+item2+"'");
                            break;
                        case "缺陷位置":
                            ew2.apply("defect_new.area ='"+item2+"'");
                            break;
                    }
                    //该item一段时间（天）的不良率
                    List<Rate3> oneItemDefectPointList = dfAoiPieceService.getItemDefectPointListDay(ew,ew2,timeWrapper);
                    if (oneItemDefectPointList==null){
                        itemDefectPointList.add(null);
                    }
                    for (Rate3 oneItem:oneItemDefectPointList){
                        timeList.add(oneItem.getStr1()+"dy");
                        String defectPoint = "0.00";
                        if (oneItem.getStr2()!=null){
                            defectPoint = oneItem.getStr2();
                        }
                        oneItemDefectPoint.add(defectPoint);
                    }

                    itemDefectPointList.add(oneItemDefectPoint);
                }
                break;
        }

        map.put("timeListX",timeList);
        map.put("itemDefectPointListY",itemDefectPointList);
        map.put("item2List",item2List);

        return new Result<Map<String, Object>>(200,"获取IA线体不良趋势相关数据成功",map);
    }

    @RequestMapping(value = "getAllItem2List",method = RequestMethod.GET)
    @ApiOperation("获取所有item2")
    public Result<List<String>> getAllItem2List(String item1){
        List<String> item2List = null;

        switch (item1){
            case "缺陷分组":
                item2List = dfAoiPieceService.getAllDefectClassList();
                break;
            case "缺陷名称":
                item2List = dfAoiPieceService.getAllDefectNameList();
                break;
            case "缺陷区域":
                item2List = dfAoiPieceService.getAllDefectAreaList();
                break;
            case "缺陷位置":
                item2List = dfAoiPieceService.getAllDefectPosition();
                break;
        }

        if (item2List==null||item2List.size()==0){
            return new Result<List<String>>(200,"获取所有item2相关数据失败");
        }
        return new Result<List<String>>(200,"获取所有item2相关数据成功",item2List);
    }

}
