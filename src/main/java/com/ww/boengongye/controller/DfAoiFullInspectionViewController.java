package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import com.ww.boengongye.utils.TxtToObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * AOI机台全检看板
 */

@Controller
@RequestMapping("/dfAoiFullInspectionView")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI机台全检看板")
public class DfAoiFullInspectionViewController {

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private DfAoiDefectService dfAoiDefectService;

    @Autowired
    private DfAoiUnitDefectService dfAoiUnitDefectService;

    @Autowired
    private DfAoiDefectSmallClassService dfAoiDefectSmallClassService;

    @Autowired
    private DfAoiDefectClassService dfAoiDefectClassService;

    @Autowired
    private DfAoiPositionService dfAoiPositionService;

    @GetMapping("getAllDefectNameList")
    @ApiOperation("获取所有缺陷名称")
    public Result getAllDefectNameList(){
        QueryWrapper<DfAoiDefectSmallClass> qw = new QueryWrapper<>();
        qw.select("name");
        List<DfAoiDefectSmallClass> list = dfAoiDefectSmallClassService.list(qw);
        return new Result(200,"获取所有缺陷名称成功",list);
    }

    @GetMapping("getAllDefectClassNameList")
    @ApiOperation("获取所有缺陷大类名称")
    public Result getAllDefectClassNameList(){
        QueryWrapper<DfAoiDefectClass> qw = new QueryWrapper<>();
        qw.select("name");
        List<DfAoiDefectClass> list =dfAoiDefectClassService.list(qw);
        return new Result(200,"获取所有缺陷大类名称成功",list);
    }

    @GetMapping("getAllDefectAreaNameList")
    @ApiOperation("获取所有缺陷区域名称")
    public Result getAllDefectAreaNameList(){
        QueryWrapper<DfAoiPosition> qw = new QueryWrapper<>();
        qw
                .select("big_area")
                .groupBy("big_area");
        List<DfAoiPosition> list =dfAoiPositionService.list(qw);
        return new Result(200,"获取所有缺陷区域名称成功",list);
    }


    @RequestMapping(value = "getAllDefectList",method = RequestMethod.GET)
    @ApiOperation("获取所有缺陷列表及其权重等信息(AOI Mapping图)")
    public Result getAllDefectList(
            @ApiParam("工厂") @RequestParam(required = false) String factory
            ,@ApiParam("机台") @RequestParam(required = false) String machine
            ,@ApiParam("线体") @RequestParam(required = false) String lineBoby
            ,@ApiParam("开始时间") @RequestParam String startTime
            ,@ApiParam("结束时间") @RequestParam String endTime
            ,@ApiParam("项目") @RequestParam(required = false) String project
            ,@ApiParam("颜色") @RequestParam(required = false) String color
            ,@ApiParam("缺陷名称") @RequestParam(value = "featurevaluesList",required = false) List<String> featurevaluesList
            ,@ApiParam("缺陷大类") @RequestParam(value ="defectClassNameList",required = false) List<String> defectClassNameList
            ,@ApiParam("缺陷区域") @RequestParam(value ="defectAreaList",required = false) List<String> defectAreaList
            ,@ApiParam("时间") @RequestParam(required = false) String defectTime
    ) throws ParseException {
//        startTime = startTime + " 07:00:00";
//        endTime = TimeUtil.getNextDay(endTime) + " 07:00:00";

        startTime = startTime + " 00:00:00";
        endTime = endTime + " 23:59:59";

        //减1小时后的时间
        if (StringUtils.isNotEmpty(defectTime)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(defectTime,formatter);
            LocalDateTime dateTimeNew = dateTime.minusHours(1);
            defectTime = dateTimeNew.format(formatter);
        }

        Map<String,Object> map = new HashMap<>();

        QueryWrapper<DfAoiPiece> pieceDefectPointWrapper = new QueryWrapper<>();
        pieceDefectPointWrapper
                .eq(StringUtils.isNotEmpty(factory),"dap.factory",factory)
                .eq(StringUtils.isNotEmpty(machine),"dap.machine",machine)
                .eq(StringUtils.isNotEmpty(lineBoby),"dap.line_body",lineBoby)
                .between("dap.`time`",startTime,endTime)
                .eq(StringUtils.isNotEmpty(project),"dap.project",project)
                .eq(StringUtils.isNotEmpty(color),"dap.color",color)
                .apply(StringUtils.isNotEmpty(defectTime),"hour(dap.`time`)=hour('"+defectTime+"')");

        Rate3 pieceDefectPoint = dfAoiDefectService.getPieceDefectPoint(pieceDefectPointWrapper);
        map.put("pieceNumber",pieceDefectPoint.getInte2());
        map.put("ngNumber",pieceDefectPoint.getInte1());
        map.put("defectPoint",String.format("%.2f",pieceDefectPoint.getDou1())+"%");

        QueryWrapper<DfAoiDefect> dfAoiDefectWrapper = new QueryWrapper<>();

        if (featurevaluesList!=null&&featurevaluesList.size()>0){
            String featurevaluesListString = TxtToObject.listToString(featurevaluesList);
            dfAoiDefectWrapper.apply("dad.featurevalues in ("+featurevaluesListString+")");
        }
        if (defectClassNameList!=null&&defectClassNameList.size()>0){
            String defectClassNameListString = TxtToObject.listToString(defectClassNameList);
            dfAoiDefectWrapper.apply("dad.class_name in ("+defectClassNameListString+")");
        }
        if (defectAreaList!=null&&defectAreaList.size()>0){
            String defectAreaListListString = TxtToObject.listToString(defectAreaList);
            dfAoiDefectWrapper.apply("dad.big_area in ("+defectAreaListListString+")");
        }
        dfAoiDefectWrapper.apply("dad.big_area is not null");

        //缺陷TOP10数据以及对应的缺陷权重
        List<DfAoiDefect> dfAoiDefectList = dfAoiDefectService.getAllDefectAndWeightList(pieceDefectPointWrapper,dfAoiDefectWrapper);
        if (dfAoiDefectList==null||defectAreaList.size()==0){
            return new Result(200,"该条件下没有缺陷数");
        }

        for (DfAoiDefect dfAoiDefect:dfAoiDefectList){
            dfAoiDefect.setPieceNumber(pieceDefectPoint.getInte2());
            QueryWrapper<DfAoiUnitDefect> unitWrapper = new QueryWrapper<>();
            unitWrapper
                    .select("count")
                    .eq("area",dfAoiDefect.getBigArea());
            if(dfAoiDefect.getBigArea()=="孔"){
                unitWrapper.eq("project_name",project);
            }
            unitWrapper.last("limit 1");
            //单位缺陷机会数
            DfAoiUnitDefect dfAoiUnitDefect =dfAoiUnitDefectService.getOne(unitWrapper);
            dfAoiDefect.setUnitDefectNumber(dfAoiUnitDefect.getCount());

            //NG率
            String NGPoint = String.format("%.2f", (float)dfAoiDefect.getDefectWeight() / (float)pieceDefectPoint.getInte2() * 100)+"%";
            dfAoiDefect.setNGPoint(NGPoint);

            //DPMO
            float DPMOFloat = (float)dfAoiDefect.getDefectNumber() / ((float)pieceDefectPoint.getInte2() *(float)dfAoiDefect.getUnitDefectNumber()) * 1000000;
            Integer DPMO = Math.round(DPMOFloat);
            dfAoiDefect.setDPMO(String.valueOf(DPMO));

            //时间
            if (StringUtils.isNotEmpty(defectTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(defectTime);

                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                String hour = hourFormat.format(date);
                int hourValue = Integer.parseInt(hour);
                int hourValue2 = hourValue+1;
                if (hourValue2==24){
                    hourValue2 = 0;
                }
                String defectTimeNew = hourValue+":00-"+hourValue2+":00";
                dfAoiDefect.setDefectTime(defectTimeNew);
            }

        }

        map.put("dfAoiDefectList",dfAoiDefectList);
        return new Result(200,"获取所有缺陷列表成功",map);
    }


    @RequestMapping(value = "getAllDefectMappingList",method = RequestMethod.GET)
    @ApiOperation("获取所有缺陷Mapping列表及其权重等信息")
    public Result getAllDefectMappingList(String factoryId,String machineCode, String lineBobyId,String startTime,String endTime,
                                          String projectId,String colour,String defectTime, String featurevalues,
                                          String defectArea,String projectName){

        QueryWrapper<DfAoiDefect> dfAoiDefectQueryWrapper = new QueryWrapper<>();
        QueryWrapper<DfAoiDefect> dfAoiDefectQueryWrapper2 = new QueryWrapper<>();

        dfAoiDefectQueryWrapper.eq("dap_new.num",1);
        dfAoiDefectQueryWrapper2.apply("dap_new.num='"+1+"'");


        if (StringUtils.isNotEmpty(factoryId)){
            dfAoiDefectQueryWrapper.eq("damp.factory_id",factoryId);
            dfAoiDefectQueryWrapper2.apply("damp.factory_id='"+factoryId+"'");
        }
        if (StringUtils.isNotEmpty(machineCode)){
            dfAoiDefectQueryWrapper.eq("damp.machine_code",machineCode);
            dfAoiDefectQueryWrapper2.apply("damp.machine_code='"+machineCode+"'");
        }
        if (StringUtils.isNotEmpty(lineBobyId)){
            dfAoiDefectQueryWrapper.eq("dasp.line_boby_id",lineBobyId);
            dfAoiDefectQueryWrapper2.apply("dasp.line_boby_id='"+lineBobyId+"'");
        }
        if (StringUtils.isNotEmpty(startTime)){
            dfAoiDefectQueryWrapper.ge("dap_new.`time`",startTime);
            dfAoiDefectQueryWrapper2.apply("dap_new.`time`>='"+startTime+"'");
        }
        if (StringUtils.isNotEmpty(endTime)){
            dfAoiDefectQueryWrapper.le("dap_new.`time`",endTime);
            dfAoiDefectQueryWrapper2.apply("dap_new.`time`<='"+endTime+"'");
        }
        if (StringUtils.isNotEmpty(projectId)){
            dfAoiDefectQueryWrapper.eq("decp.project_id",projectId);
            dfAoiDefectQueryWrapper2.apply("decp.project_id='"+projectId+"'");
        }
        if (StringUtils.isNotEmpty(colour)){
            dfAoiDefectQueryWrapper.eq("decp.colour",colour);
            dfAoiDefectQueryWrapper2.apply("decp.colour='"+colour+"'");
        }

        dfAoiDefectQueryWrapper
                .apply("hour(dap_new.`time`) = hour('"+defectTime+"')-1")
                .eq("dad.featurevalues",featurevalues)
                .eq("ddcp.defect_area",defectArea)
                .eq("dp.name",projectName);


        //获取缺陷列表
        List<DfAoiDefect> dfAoiDefectList = dfAoiDefectService.getAllDefectMappingList(dfAoiDefectQueryWrapper,dfAoiDefectQueryWrapper2);
        for (DfAoiDefect dfAoiDefect:dfAoiDefectList){
            QueryWrapper<Integer> unitQueryWrapper = new QueryWrapper<>();
            unitQueryWrapper.eq("daud.area",dfAoiDefect.getDefectArea());
            if(dfAoiDefect.getDefectArea()=="孔"){
                unitQueryWrapper.eq("daud.project_id",projectId);
            }
            //单位缺陷机会数据
            Integer unitDefectNumber =dfAoiUnitDefectService.getUnitNumberByDefectAndProjectId(unitQueryWrapper);
            dfAoiDefect.setUnitDefectNumber(unitDefectNumber);

            QueryWrapper<Integer> defectNumberWrapper = new QueryWrapper<>();
            defectNumberWrapper.eq("dap_new.num",1);

            if (StringUtils.isNotEmpty(factoryId)){
                defectNumberWrapper.eq("damp.factory_id",factoryId);
            }
            if (StringUtils.isNotEmpty(machineCode)){
                defectNumberWrapper.eq("damp.machine_code",machineCode);
            }
            if (StringUtils.isNotEmpty(lineBobyId)){
                defectNumberWrapper.eq("dasp.line_boby_id",lineBobyId);
            }
            if (StringUtils.isNotEmpty(startTime)){
                defectNumberWrapper.ge("dap_new.`time`",startTime);
            }
            if (StringUtils.isNotEmpty(endTime)){
                defectNumberWrapper.le("dap_new.`time`",endTime);
            }
            if (StringUtils.isNotEmpty(projectId)){
                defectNumberWrapper.eq("decp.project_id",projectId);
            }
            if (StringUtils.isNotEmpty(colour)){
                defectNumberWrapper.eq("decp.colour",colour);
            }
            if (StringUtils.isNotEmpty(defectTime)){
                defectNumberWrapper.apply("hour(dap_new.`time`) = hour('"+defectTime+"')-1");
            }

            defectNumberWrapper
                    .eq("dad.featurevalues",dfAoiDefect.getFeaturevalues())
                    .eq("ddcp.defect_area",dfAoiDefect.getDefectArea())
                    .eq("dp.name",dfAoiDefect.getProjectName())
                    .eq("hour(dap_new.`time`)",dfAoiDefect.getDefectTime())
                    .eq("dad.area",dfAoiDefect.getArea());
            //缺陷总数
            Integer defectNumber = dfAoiDefectService.getDefectNumber(defectNumberWrapper);
            dfAoiDefect.setDefectNumber(defectNumber);

            //NG率
            String NGPoint = String.format("%.2f", (float) dfAoiDefect.getDefectWeight() / (float) dfAoiDefect.getPieceNumber() * 100)+"%";
            dfAoiDefect.setNGPoint(NGPoint);

            //DPMO
            float DPMOFloat = (float)dfAoiDefect.getDefectNumber() / ((float)dfAoiDefect.getPieceNumber() *(float)dfAoiDefect.getUnitDefectNumber()) * 1000000;
            Integer DPMO = Math.round(DPMOFloat);
            dfAoiDefect.setDPMO(String.valueOf(DPMO));

        }

        return new Result(200,"获取所有缺陷Mapping列表成功",dfAoiDefectList);
    }


    @RequestMapping(value = "listItemInfo",method = RequestMethod.GET)
    @ApiOperation("获取缺陷列表及其权重等信息")
    public Result listItemInfo(String factoryId,String machineCode, String lineBobyId,String startTime,String endTime,
                                         String color,String defectTime, String featurevalues,
                                          String bigArea,String projectName){

        QueryWrapper<DfAoiDefect> qw1 = new QueryWrapper<>();
        QueryWrapper<DfAoiDefect> qw2 = new QueryWrapper<>();
        QueryWrapper<DfAoiPiece> qw3 = new QueryWrapper<>();

        int qwCount=0;
        String sql="select frameid from df_aoi_piece ";
        if(null!=defectTime&&!defectTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            qwCount++;
        }

        if(null!=startTime&&!startTime.equals("")&&null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time between '"+startTime+"' and '"+endTime+"' ";
            qw3.between("create_time",startTime,endTime);
            qwCount++;
        }else if(null!=startTime&&!startTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time > '"+startTime+"'  " ;
            qw3.ge("create_time",startTime);
            qwCount++;
        }else if(null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time < '"+endTime+"'  " ;
            qw3.le("create_time",endTime);
            qwCount++;
        }

        if(null!=color&&!color.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" color ='"+color+"'  " ;
            qw3.eq("color",color);
            qwCount++;
        }

        if(null!=projectName&&!projectName.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" project ='"+projectName+"'  " ;
            qw3.eq("project",projectName);
            qwCount++;
        }

//待完成总片数的区域等筛选

        if(null!=bigArea&&!bigArea.equals("")){
            qw1.eq("big_area",bigArea);
            qw2.eq("big_area",bigArea);
        }

        if(null!=featurevalues&&!featurevalues.equals("")){
            qw1.eq("featurevalues",featurevalues);
            qw2.eq("featurevalues",featurevalues);
        }

        qw1.inSql("frameid",sql);
        qw2.inSql("frameid",sql);
        qw3.select("distinct(frameid) ");
        //获取缺陷列表
        List<DfAoiDefect> dfAoiDefectList =dfAoiDefectService.listItemInfo(qw1,qw2);

        int totalCount=dfAoiPieceService.count(qw3);
        qw3.eq("qualityid",1);
        int ngCount=dfAoiPieceService.count(qw3);
        System.out.println("NG:"+ngCount);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        if(dfAoiDefectList.size()>0){
            for(DfAoiDefect d:dfAoiDefectList){
                QueryWrapper<Integer> unitQueryWrapper = new QueryWrapper<>();
                unitQueryWrapper.eq("daud.area",d.getBigArea());
                if(d.getDefectArea()=="孔"){
                    unitQueryWrapper.eq("daud.project_name",projectName);
                }
                unitQueryWrapper.last("limit 1");
                //单位缺陷机会数据
                Integer unitDefectNumber =dfAoiUnitDefectService.getUnitNumberByDefectAndProjectId(unitQueryWrapper);
                d.setUnitDefectNumber(unitDefectNumber);
                d.setPieceNumber(totalCount);
//                System.out.println(d.getBigArea());
//                System.out.println(d.getUnitDefectNumber());
//                System.out.println(d.getPieceNumber());
//                System.out.println(d.getDefectNumber());
                if(null!=d.getDefectWeight()&&null!=d.getPieceNumber()&&d.getDefectWeight()!=0&&d.getPieceNumber()!=0){
                    //NG率
                    Double NGPoint =  (Double.valueOf(d.getDefectWeight()) /Double.valueOf( d.getPieceNumber())) * 100.0;
//                    System.out.println("NGPoint");
//                    System.out.println(NGPoint);
                    String result = decimalFormat.format(NGPoint);
                    d.setNGPoint(result+"%");

                }else{
                    d.setNGPoint("0%");
                }

                //DPMO
                float DPMOFloat = (float)d.getDefectNumber() / ((float)d.getPieceNumber() *(float)d.getUnitDefectNumber()) * 1000000;
                Integer DPMO = Math.round(DPMOFloat);
                d.setDPMO(String.valueOf(DPMO));
            }
        }
        AOITotalCountAndList result=new AOITotalCountAndList();
        result.setList(dfAoiDefectList);
        result.setNgCount(ngCount);
        result.setTotalCount(totalCount);

        return new Result(200,"查询成功",result);
    }


    @RequestMapping(value = "listFeaturevaluesInfo",method = RequestMethod.GET)
    @ApiOperation("获取缺陷区域列表及其权重等信息")
    public Result listFeaturevaluesInfo(String factoryId,String machineCode, String lineBobyId,String startTime,String endTime,
                               String color,String defectTime, String featurevalues,
                               String bigArea,String projectName){

        QueryWrapper<DfAoiDefect> qw1 = new QueryWrapper<>();
        QueryWrapper<DfAoiDefect> qw2 = new QueryWrapper<>();
        QueryWrapper<DfAoiPiece> qw3 = new QueryWrapper<>();

        int qwCount=0;
        String sql="select frameid from df_aoi_piece ";
        if(null!=defectTime&&!defectTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            qwCount++;
        }

        if(null!=startTime&&!startTime.equals("")&&null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time between '"+startTime+"' and '"+endTime+"' ";
            qw3.between("create_time",startTime,endTime);
            qwCount++;
        }else if(null!=startTime&&!startTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time > '"+startTime+"'  " ;
            qw3.ge("create_time",startTime);
            qwCount++;
        }else if(null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time < '"+endTime+"'  " ;
            qw3.le("create_time",endTime);
            qwCount++;
        }

        if(null!=color&&!color.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" color ='"+color+"'  " ;
            qw3.eq("color",color);
            qwCount++;
        }

        if(null!=projectName&&!projectName.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" project ='"+projectName+"'  " ;
            qw3.eq("project",projectName);
            qwCount++;
        }

        qw3.inSql("frameid","select frameid from df_aoi_defect where featurevalues='"+featurevalues+"'");
        //待完成总片数的区域等筛选
        if(null!=bigArea&&!bigArea.equals("")){
            qw1.eq("big_area",bigArea);
            qw2.eq("big_area",bigArea);
        }

        if(null!=featurevalues&&!featurevalues.equals("")){
            qw1.eq("featurevalues",featurevalues);
            qw2.eq("featurevalues",featurevalues);
        }

        qw1.inSql("frameid",sql);
        qw2.inSql("frameid",sql);
        qw3.select("distinct(frameid) ");
        //获取缺陷列表
        List<DfAoiDefect> dfAoiDefectList =dfAoiDefectService.listFeaturevaluesInfo(qw1,qw2);

        int totalCount=dfAoiPieceService.count(qw3);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        if(dfAoiDefectList.size()>0){
            for(DfAoiDefect d:dfAoiDefectList){
                QueryWrapper<Integer> unitQueryWrapper = new QueryWrapper<>();
                unitQueryWrapper.eq("daud.area",d.getBigArea());
                if(d.getDefectArea()=="孔"){
                    unitQueryWrapper.eq("daud.project_name",projectName);
                }
                unitQueryWrapper.last("limit 1");
                //单位缺陷机会数据
                Integer unitDefectNumber =dfAoiUnitDefectService.getUnitNumberByDefectAndProjectId(unitQueryWrapper);
                d.setUnitDefectNumber(unitDefectNumber);
                d.setPieceNumber(totalCount);
//                System.out.println(d.getBigArea());
//                System.out.println(d.getUnitDefectNumber());
//                System.out.println(d.getPieceNumber());
//                System.out.println(d.getDefectNumber());
                if(null!=d.getDefectWeight()&&d.getDefectWeight()!=0&&d.getPieceNumber()!=0){
                    //NG率
                    Double NGPoint =  (Double.valueOf(d.getDefectWeight()) /Double.valueOf( d.getPieceNumber())) * 100.0;
//                    System.out.println("NGPoint");
//                    System.out.println(NGPoint);
                    String result = decimalFormat.format(NGPoint);
                    d.setNGPoint(result+"%");

                }else{
                    d.setNGPoint("0%");
                }

                //DPMO
                float DPMOFloat = (float)d.getDefectNumber() / ((float)d.getPieceNumber() *(float)d.getUnitDefectNumber()) * 1000000;
                Integer DPMO = Math.round(DPMOFloat);
                d.setDPMO(String.valueOf(DPMO));
            }
        }


        return new Result(200,"查询成功",dfAoiDefectList);
    }

    @RequestMapping(value = "listMapping",method = RequestMethod.GET)
    @ApiOperation("获取Mapping")
    public Result listMapping(String factoryId,String machineCode, String lineBobyId,String startTime,String endTime,
                               String color,String defectTime, String featurevalues,
                               String bigArea,String projectName){

        QueryWrapper<DfAoiDefect> qw1 = new QueryWrapper<>();

        int qwCount=0;
        String sql="select frameid from df_aoi_piece ";
        if(null!=defectTime&&!defectTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            qwCount++;
        }

        if(null!=startTime&&!startTime.equals("")&&null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time between '"+startTime+"' and '"+endTime+"' ";
            qwCount++;
        }else if(null!=startTime&&!startTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time > '"+startTime+"'  " ;
            qwCount++;
        }else if(null!=endTime&&!endTime.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" create_time < '"+endTime+"'  " ;
            qwCount++;
        }

        if(null!=color&&!color.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" color ='"+color+"'  " ;
            qwCount++;
        }

        if(null!=projectName&&!projectName.equals("")){
            if(qwCount==0){
                sql+=" where ";
            }else {
                sql+=" and ";
            }
            sql+=" project ='"+projectName+"'  " ;
            qwCount++;
        }

//待完成总片数的区域等筛选

        if(null!=bigArea&&!bigArea.equals("")){
            qw1.eq("big_area",bigArea);
        }

        if(null!=featurevalues&&!featurevalues.equals("")){
            qw1.eq("featurevalues",featurevalues);
        }

        qw1.inSql("frameid",sql);
        qw1.select("AOIxcenter","AOIycenter","big_area","area","featurevalues" );
        //获取缺陷列表
        List<DfAoiDefect> dfAoiDefectList =dfAoiDefectService.list(qw1);



        return new Result(200,"查询成功",dfAoiDefectList);
    }

}
