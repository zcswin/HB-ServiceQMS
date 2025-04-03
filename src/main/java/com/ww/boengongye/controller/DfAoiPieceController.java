package com.ww.boengongye.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.exportExcel.BatchqueryReport;
import com.ww.boengongye.entity.exportExcel.EmpCapacityReport;
import com.ww.boengongye.entity.exportExcel.OqcReport;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.ExcelExportUtil;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * AOI玻璃单片信息表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@Controller
@RequestMapping("/dfAoiPiece")
@ResponseBody
@CrossOrigin
@Api(tags = "AOI玻璃单片信息")
public class DfAoiPieceController {

    private static final Logger logger = LoggerFactory.getLogger(DfAoiMachineProtectController.class);

    @Autowired
    private DfAoiPieceService dfAoiPieceService;

    @Autowired
    private UserService userService;

    @Autowired
    private DfAoiConfigDataService dfAoiConfigDataService;

    @Autowired
    private DfAoiFlawConfigService dfAoiFlawConfigService;

    @Autowired
    private DfAoiUndetectedService dfAoiUndetectedService;

    /**
     * 获取FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("获取FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比")
    @RequestMapping(value = "countDefectAndEscape",method = RequestMethod.GET)
    public Result countDefectAndEscape(Integer page, Integer limit,String userCode,String projectId,String colour,String startTime,String endTime){
        QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();

        IPage<DfAoiDefectPoint> dfAoiDefectPointPage = new Page<>(page,limit);
        QueryWrapper<DfAoiDefectPoint> dfAoiDefectPointQueryWrapper = new QueryWrapper<>();

        IPage<DfAoiEscapePoint> dfAoiEscapePointPage = new Page<>(page,limit);
        QueryWrapper<DfAoiEscapePoint> dfAoiEscapePointQueryWrapper = new QueryWrapper<>();

        QueryWrapper<User> oqcNumberWrapper = new QueryWrapper<>();

        integerQueryWrapper
                .eq("first_qc.num",1)
                .eq("first_qc.qc_user_code",userCode);
        dfAoiDefectPointQueryWrapper
                .eq("first_qc.qc_user_code",userCode)
                .eq("first_qc.num",1)
                .eq("first_qc.qc_result","NG");

        dfAoiEscapePointQueryWrapper
                .eq("dadl_new1.num",1)
                .eq("dau.fqc_user",userCode);

        oqcNumberWrapper
                .eq("u.name",userCode);

        if (projectId!=null&&!"".equals(projectId)){
            integerQueryWrapper.eq("decp.project_id",projectId);
            dfAoiDefectPointQueryWrapper.eq("decp.project_id",projectId);
            dfAoiEscapePointQueryWrapper.eq("decp.project_id",projectId);
            oqcNumberWrapper.eq("decp.project_id",projectId);
        }
        if (colour!=null&&!"".equals(colour)){
            integerQueryWrapper.eq("decp.colour",colour);
            dfAoiDefectPointQueryWrapper.eq("decp.colour",colour);
            dfAoiEscapePointQueryWrapper.eq("decp.colour",colour);
            oqcNumberWrapper.eq("decp.colour",colour);
        }
        if (startTime!=null&&!"".equals(startTime)){
            integerQueryWrapper.ge("first_qc.qc_time",startTime);
            dfAoiDefectPointQueryWrapper.ge("first_qc.qc_time",startTime);
            dfAoiEscapePointQueryWrapper.ge("dadl_new1.qc_time",startTime);
            oqcNumberWrapper.ge("dadl_new3.qc_time",startTime);
        }
        if (endTime!=null&&!"".equals(endTime)){
            integerQueryWrapper.le("first_qc.qc_time",endTime);
            dfAoiDefectPointQueryWrapper.le("first_qc.qc_time",endTime);
            dfAoiEscapePointQueryWrapper.le("dadl_new1.qc_time",endTime);
            oqcNumberWrapper.le("dadl_new3.qc_time",endTime);
        }

        //投入数量
        Integer total = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);
        if (total==0){
            return new Result(500,"该条件下没有FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比");
        }
        integerQueryWrapper.eq("first_qc.qc_result","NG");

        //抛出不良数量
        Integer defectTotal = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);

        //不良top5占比集合
        List<DfAoiDefectPoint> dfAoiDefectPoints = null;

        //不良top5占比
        String defectTop5Point = "0%";

        if (defectTotal>0){
            //不良top5总数
            Integer defectTop5Number = 0;

            //不良top5占比集合
            dfAoiDefectPoints = dfAoiPieceService.getDefectPointList(dfAoiDefectPointPage,dfAoiDefectPointQueryWrapper);
            if (dfAoiDefectPoints!=null&&dfAoiDefectPoints.size()>0){
                for(DfAoiDefectPoint dfAoiDefectPoint:dfAoiDefectPoints){
                    String defectPoint =String.format("%.2f", (float) dfAoiDefectPoint.getDefectNumber() / (float) total * 100)+"%";
                    dfAoiDefectPoint.setDefectPoint(defectPoint);
                    defectTop5Number+=dfAoiDefectPoint.getDefectNumber();
                }
            }
            //不良top5占比
            defectTop5Point =String.format("%.2f", (float) defectTop5Number / (float) total * 100)+"%";

        }

        //当前员工投入玻璃被OQC检测数量
        Page<User> userPage = new Page<>(1,5);
        List<User> userOqcNumberList = dfAoiPieceService.getAllUserOQCNumberList(userPage,oqcNumberWrapper);
        Integer oqcNumber = 0;

        //漏检top5占比集合
        List<DfAoiEscapePoint> dfAoiEscapePoints = null;

        //当前员工漏检top5占比
        String escapeTop5Point = "0%";

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        if (userOqcNumberList!=null&&userOqcNumberList.size()>0&&userOqcNumberList.get(0).getOqcNumber()!=null){
            User userOqc = userOqcNumberList.get(0);
            oqcNumber = userOqc.getOqcNumber();

            //漏检缺陷top5总数
            Integer escapeTop5Number = 0;

            //漏检top5占比集合
            dfAoiEscapePoints = dfAoiPieceService.getEscapePointList(dfAoiEscapePointPage,dfAoiEscapePointQueryWrapper);
            if (dfAoiEscapePoints!=null&&dfAoiEscapePoints.size()>0){
                for(DfAoiEscapePoint dfAoiEscapePoint:dfAoiEscapePoints){
                    //单个缺陷的漏检率
                    String escapePoint =String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) oqcNumber * 100 * escapeData)+"%";
                    dfAoiEscapePoint.setEscapePoint(escapePoint);
                    escapeTop5Number+=dfAoiEscapePoint.getEscapeNumber();
                }
            }
            //漏检top5占比
            escapeTop5Point =String.format("%.2f", (float) escapeTop5Number / (float) oqcNumber * 100 * escapeData)+"%";

        }

        DfAoiDefectAndEscape dfAoiDefectAndEscape = new DfAoiDefectAndEscape();
        dfAoiDefectAndEscape.setTotal(total);
        dfAoiDefectAndEscape.setDefectTotal(defectTotal);
        dfAoiDefectAndEscape.setDefectTop5Point(defectTop5Point);
        dfAoiDefectAndEscape.setDfAoiDefectPoints(dfAoiDefectPoints);
        dfAoiDefectAndEscape.setEscapeTop5Point(escapeTop5Point);
        dfAoiDefectAndEscape.setDfAoiEscapePoints(dfAoiEscapePoints);

        return new Result(200,"获取缺陷占比和漏检占比结果成功",dfAoiDefectAndEscape);
    }


    /**
     * 获取OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比
     * @param page
     * @param limit
     * @param userCode
     * @param projectId
     * @param colour
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("获取OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比")
    @RequestMapping(value = "countAllEscape",method = RequestMethod.GET)
    public Result countAllEscape(Integer page, Integer limit,String userCode,String projectId,String colour,String startTime,String endTime){
        QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
        QueryWrapper<DfAoiEscape> dfAoiEscapeQueryWrapper = new QueryWrapper<>();

        integerQueryWrapper
                .eq("first_qc.num",1)
                .eq("first_qc.qc_user_code",userCode);
        dfAoiEscapeQueryWrapper
                .eq("dadl_new1.num",1)
                .eq("dau.oqc_use",userCode);

        if (projectId!=null&&!"".equals(projectId)){
            integerQueryWrapper.eq("decp.project_id",projectId);
            dfAoiEscapeQueryWrapper.eq("decp.project_id",projectId);
        }
        if (colour!=null&&!"".equals(colour)){
            integerQueryWrapper.eq("decp.colour",colour);
            dfAoiEscapeQueryWrapper.eq("decp.colour",colour);
        }
        if (startTime!=null&&!"".equals(startTime)){
            integerQueryWrapper.ge("first_qc.qc_time",startTime);
            dfAoiEscapeQueryWrapper.ge("dau.create_time",startTime);
        }
        if (endTime!=null&&!"".equals(endTime)){
            integerQueryWrapper.le("first_qc.qc_time",endTime);
            dfAoiEscapeQueryWrapper.le("dau.create_time",endTime);
        }

        //投入数量
        Integer total = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);

        integerQueryWrapper.eq("first_qc.qc_result","NG");

        //抛出不良数量
        Integer defectTotal = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);

        //获取所有漏检信息集合
        List<DfAoiEscape> dfAoiEscapes = dfAoiPieceService.getAllEscapeList(dfAoiEscapeQueryWrapper);
        if (dfAoiEscapes==null||dfAoiEscapes.size()==0){
            return new Result(500,"该条件下没有OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比的相关信息");
        }

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        for (DfAoiEscape dfAoiEscape:dfAoiEscapes){
            //计算每个人漏检缺陷的占比
            String escapeTop5Point = String.format("%.2f", (float) dfAoiEscape.getEscapeTop5Number() / (float) total * 100 * escapeData)+"%";
            dfAoiEscape.setEscapeTop5Point(escapeTop5Point);

            //获取该员工漏检的top5缺陷并计算每个缺陷的占比
            QueryWrapper<DfAoiEscapePoint> dfAoiEscapePointQueryWrapper = new QueryWrapper<>();
            dfAoiEscapePointQueryWrapper
                    .eq("dadl_new1.num",1)
                    .eq("dau.oqc_use",userCode)
                    .eq("u.alias",dfAoiEscape.getUserName());

            if (projectId!=null&&!"".equals(projectId)){
                dfAoiEscapePointQueryWrapper.eq("decp.project_id",projectId);
            }
            if (colour!=null&&!"".equals(colour)){
                dfAoiEscapePointQueryWrapper.eq("decp.colour",colour);
            }
            if (startTime!=null&&!"".equals(startTime)){
                dfAoiEscapePointQueryWrapper.ge("dau.create_time",startTime);
            }
            if (endTime!=null&&!"".equals(endTime)){
                dfAoiEscapePointQueryWrapper.le("dau.create_time",endTime);
            }
            //该员工漏检的top5缺陷集合
            List<DfAoiEscapePoint> dfAoiEscapePoints = dfAoiPieceService.getEscapeTop5PointList(dfAoiEscapePointQueryWrapper);
            for (DfAoiEscapePoint dfAoiEscapePoint : dfAoiEscapePoints){
                String escapePoint = String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) total * 100 * escapeData)+"%";
                dfAoiEscapePoint.setEscapePoint(escapePoint);
            }
            //更新该员工漏检top5集合
            dfAoiEscape.setDfAoiEscapePoints(dfAoiEscapePoints);
        }

        DfAoiDefectAndEscape dfAoiDefectAndEscape = new DfAoiDefectAndEscape();
        dfAoiDefectAndEscape.setTotal(total);
        dfAoiDefectAndEscape.setDefectTotal(defectTotal);
        dfAoiDefectAndEscape.setDfAoiEscapes(dfAoiEscapes);

        return new Result(200,"获取所有漏检的员工及其漏检的缺陷占比结果成功",dfAoiDefectAndEscape);
    }
//优化
//    /**
//     * 获取FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比
//     * @param userCode
//     * @param project
//     * @param colour
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    @ApiOperation("获取FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比")
//    @RequestMapping(value = "countDefectAndEscape",method = RequestMethod.GET)
//    public Result countDefectAndEscape(String userCode,String project,String colour,String startTime,String endTime){
//        QueryWrapper<DfAoiPiece> inputAndDefectWrapper = new QueryWrapper<>();
//
//        QueryWrapper<User> oqcNumberWrapper = new QueryWrapper<>();
//
//        inputAndDefectWrapper
//                .eq("dadl_new.qc_user_code",userCode);
//
//        oqcNumberWrapper
//                .eq("u.name",userCode);
//
//        if (StringUtils.isNotEmpty(project)){
//            inputAndDefectWrapper.eq("dap_new.project",project);
//            oqcNumberWrapper.eq("dap_new.project",project);
//        }
//        if (StringUtils.isNotEmpty(colour)){
//            inputAndDefectWrapper.eq("dap_new.color",colour);
//            oqcNumberWrapper.eq("dap_new.color",colour);
//        }
//        if (StringUtils.isNotEmpty(startTime)){
//            inputAndDefectWrapper.ge("dadl_new.qc_time",startTime);
//            oqcNumberWrapper.ge("dadl_new3.qc_time",startTime);
//        }
//        if (StringUtils.isNotEmpty(endTime)){
//            inputAndDefectWrapper.le("dadl_new.qc_time",endTime);
//            oqcNumberWrapper.le("dadl_new3.qc_time",endTime);
//        }
//
//        Rate3 inputAndDefect = dfAoiPieceService.getUserInputAndDefectNumber(inputAndDefectWrapper);
//        if (inputAndDefect==null||inputAndDefect.getInte2()==0){
//            return new Result(500,"该条件下没有FQC用户检测玻璃的不良top5缺陷和漏检top5缺陷及其占比");
//        }
//
//        //投入数量
//        Integer total = inputAndDefect.getInte2();
//
//        //抛出不良数量
//        Integer defectTotal = inputAndDefect.getInte1();
//
//        //不良top5占比集合
//        List<DfAoiDefectPoint> defectPointTop5List = null;
//
//        //不良top5占比
//        String defectTop5Point = "0%";
//
//        if (defectTotal>0){
//            //不良top5总数
//            Integer defectTop5Number = 0;
//
//            //不良top5占比集合
//            defectPointTop5List = dfAoiPieceService.getUserDefectTop5List(inputAndDefectWrapper);
//            if (defectPointTop5List!=null&&defectPointTop5List.size()>0){
//                for(DfAoiDefectPoint dfAoiDefectPoint:defectPointTop5List){
//                    String defectPoint =String.format("%.2f", (float) dfAoiDefectPoint.getDefectNumber() / (float) total * 100)+"%";
//                    dfAoiDefectPoint.setDefectPoint(defectPoint);
//                    defectTop5Number+=dfAoiDefectPoint.getDefectNumber();
//                }
//            }
//            //不良top5占比
//            defectTop5Point =String.format("%.2f", (float) defectTop5Number / (float) total * 100)+"%";
//
//        }
//
//        //当前员工投入玻璃被OQC检测数量
//        Page<User> userPage = new Page<>(1,1);
//        List<User> userOqcNumberList = dfAoiPieceService.getAllUserOQCNumberList(userPage,oqcNumberWrapper);
//        Integer oqcNumber = 0;
//
//        //漏检top5占比集合
//        List<DfAoiEscapePoint> dfAoiEscapePoints = null;
//
//        //当前员工漏检top5占比
//        String escapeTop5Point = "0%";
//
//        if (userOqcNumberList!=null&&userOqcNumberList.size()>0){
//            User userOqc = userOqcNumberList.get(0);
//            oqcNumber = userOqc.getOqcNumber();
//
//            //漏检缺陷top5总数
//            Integer escapeTop5Number = 0;
//
//            //漏检top5占比集合
//            dfAoiEscapePoints = dfAoiPieceService.getEscapePointTop5List(inputAndDefectWrapper);
//            if (dfAoiEscapePoints!=null&&dfAoiEscapePoints.size()>0){
//                for(DfAoiEscapePoint dfAoiEscapePoint:dfAoiEscapePoints){
//                    //单个缺陷的漏检率
//                    String escapePoint =String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) oqcNumber * 100)+"%";
//                    dfAoiEscapePoint.setEscapePoint(escapePoint);
//                    escapeTop5Number+=dfAoiEscapePoint.getEscapeNumber();
//                }
//            }
//            //漏检top5占比
//            escapeTop5Point =String.format("%.2f", (float) escapeTop5Number / (float) oqcNumber * 100)+"%";
//
//        }
//
//        DfAoiDefectAndEscape dfAoiDefectAndEscape = new DfAoiDefectAndEscape();
//        dfAoiDefectAndEscape.setTotal(total);
//        dfAoiDefectAndEscape.setDefectTotal(defectTotal);
//        dfAoiDefectAndEscape.setDefectTop5Point(defectTop5Point);
//        dfAoiDefectAndEscape.setDfAoiDefectPoints(defectPointTop5List);
//        dfAoiDefectAndEscape.setEscapeTop5Point(escapeTop5Point);
//        dfAoiDefectAndEscape.setDfAoiEscapePoints(dfAoiEscapePoints);
//
//        return new Result(200,"获取缺陷占比和漏检占比结果成功",dfAoiDefectAndEscape);
//    }
//
//
//    /**
//     * 获取OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比
//     * @param page
//     * @param limit
//     * @param userCode
//     * @param projectId
//     * @param colour
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    @ApiOperation("获取OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比")
//    @RequestMapping(value = "countAllEscape",method = RequestMethod.GET)
//    public Result countAllEscape(Integer page, Integer limit,String userCode,String projectId,String colour,String startTime,String endTime){
//        QueryWrapper<Integer> integerQueryWrapper = new QueryWrapper<>();
//        QueryWrapper<DfAoiEscape> dfAoiEscapeQueryWrapper = new QueryWrapper<>();
//
//        integerQueryWrapper
//                .eq("first_qc.num",1)
//                .eq("first_qc.qc_user_code",userCode);
//        dfAoiEscapeQueryWrapper
//                .eq("dadl_new1.num",1)
//                .eq("dau.oqc_use",userCode);
//
//        if (projectId!=null&&!"".equals(projectId)){
//            integerQueryWrapper.eq("decp.project_id",projectId);
//            dfAoiEscapeQueryWrapper.eq("decp.project_id",projectId);
//        }
//        if (colour!=null&&!"".equals(colour)){
//            integerQueryWrapper.eq("decp.colour",colour);
//            dfAoiEscapeQueryWrapper.eq("decp.colour",colour);
//        }
//        if (startTime!=null&&!"".equals(startTime)){
//            integerQueryWrapper.ge("first_qc.qc_time",startTime);
//            dfAoiEscapeQueryWrapper.ge("dau.create_time",startTime);
//        }
//        if (endTime!=null&&!"".equals(endTime)){
//            integerQueryWrapper.le("first_qc.qc_time",endTime);
//            dfAoiEscapeQueryWrapper.le("dau.create_time",endTime);
//        }
//
//        //投入数量
//        Integer total = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);
//
//        integerQueryWrapper.eq("first_qc.qc_result","NG");
//
//        //抛出不良数量
//        Integer defectTotal = dfAoiPieceService.getDefectPieceNumber(integerQueryWrapper);
//
//        //获取所有漏检信息集合
//        List<DfAoiEscape> dfAoiEscapes = dfAoiPieceService.getAllEscapeList(dfAoiEscapeQueryWrapper);
//        if (dfAoiEscapes==null||dfAoiEscapes.size()==0){
//            return new Result(500,"该条件下没有OQC用户检测出FQC用户漏检的漏检top5缺陷及其占比的相关信息");
//        }
//        for (DfAoiEscape dfAoiEscape:dfAoiEscapes){
//            //计算每个人漏检缺陷的占比
//            String escapeTop5Point = String.format("%.2f", (float) dfAoiEscape.getEscapeTop5Number() / (float) total * 100)+"%";
//            dfAoiEscape.setEscapeTop5Point(escapeTop5Point);
//
//            //获取该员工漏检的top5缺陷并计算每个缺陷的占比
//            QueryWrapper<DfAoiEscapePoint> dfAoiEscapePointQueryWrapper = new QueryWrapper<>();
//            dfAoiEscapePointQueryWrapper
//                    .eq("dadl_new1.num",1)
//                    .eq("dau.oqc_use",userCode)
//                    .eq("u.alias",dfAoiEscape.getUserName());
//
//            if (projectId!=null&&!"".equals(projectId)){
//                dfAoiEscapePointQueryWrapper.eq("decp.project_id",projectId);
//            }
//            if (colour!=null&&!"".equals(colour)){
//                dfAoiEscapePointQueryWrapper.eq("decp.colour",colour);
//            }
//            if (startTime!=null&&!"".equals(startTime)){
//                dfAoiEscapePointQueryWrapper.ge("dau.create_time",startTime);
//            }
//            if (endTime!=null&&!"".equals(endTime)){
//                dfAoiEscapePointQueryWrapper.le("dau.create_time",endTime);
//            }
//            //该员工漏检的top5缺陷集合
//            List<DfAoiEscapePoint> dfAoiEscapePoints = dfAoiPieceService.getEscapeTop5PointList(dfAoiEscapePointQueryWrapper);
//            for (DfAoiEscapePoint dfAoiEscapePoint : dfAoiEscapePoints){
//                String escapePoint = String.format("%.2f", (float) dfAoiEscapePoint.getEscapeNumber() / (float) total * 100)+"%";
//                dfAoiEscapePoint.setEscapePoint(escapePoint);
//            }
//            //更新该员工漏检top5集合
//            dfAoiEscape.setDfAoiEscapePoints(dfAoiEscapePoints);
//        }
//
//        DfAoiDefectAndEscape dfAoiDefectAndEscape = new DfAoiDefectAndEscape();
//        dfAoiDefectAndEscape.setTotal(total);
//        dfAoiDefectAndEscape.setDefectTotal(defectTotal);
//        dfAoiDefectAndEscape.setDfAoiEscapes(dfAoiEscapes);
//
//        return new Result(200,"获取所有漏检的员工及其漏检的缺陷占比结果成功",dfAoiDefectAndEscape);
//    }

//    @GetMapping("/updateTime")
//    public Result updateTime() {
//        List<DfAoiPiece> list = dfAoiPieceService.list();
//        for (DfAoiPiece dfAoiPiece : list) {
//            long oldTime = Timestamp.valueOf("2023-08-08 07:00:00").getTime();
//            double v = Math.random() * 24 * 60 * 60 * 1000;
//            Timestamp newTime = new Timestamp(oldTime + (long)v);
//            dfAoiPiece.setTime(newTime.toString().split("\\.")[0]);
//        }
//        dfAoiPieceService.updateBatchById(list);
//        return new Result(200, "更新成功");
//    }

    @GetMapping("/getUControl")
    @ApiOperation("获取U图")
    public Result getUControl(@RequestParam String date) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.between("pie.time", startTime, endTime);
        List<Rate3> rates = dfAoiPieceService.listPieceAndDefectNumGroupByHour(qw);
        Map<String, Integer[][]> projectResNum = new HashMap<>();
        for (Rate3 rate : rates) {
            String project = rate.getStr1();
            Integer hour = rate.getInte1();
            Integer inNum = rate.getInte2();
            Integer DefectNum = rate.getInte3();
            if (!projectResNum.containsKey(project)) {
                Integer[][] num = new Integer[24][2];
                num[(hour + 17) % 24][0] = inNum;  // 为了把7点放到第一位
                num[(hour + 17) % 24][1] = DefectNum;
                projectResNum.put(project, num);
            } else {
                Integer[][] num = projectResNum.get(project);
                num[(hour + 17) % 24][0] = inNum;
                num[(hour + 17) % 24][1] = DefectNum;
            }
        }
        List<Object> result = new ArrayList<>();
        int[] x = new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 1, 2, 3, 4, 5, 6};
        for (Map.Entry<String, Integer[][]> entry : projectResNum.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            Integer[][] num = entry.getValue();
            Integer allInNum = 0;
            Integer allDefectNum = 0;
            double[] point = new double[24];
            double[] avgs = new double[24];
            for (int i = 0; i < num.length; i++) {
                if (null != num[i][0]) allInNum += num[i][0];
                if (null != num[i][1]) allDefectNum += num[i][1];
                if (null == num[i][0] || null == num[i][1]) {
                    point[i] = 0d;
                } else {
                    point[i] = num[i][1].doubleValue() / num[i][0];
                }
            }
            double avg = allDefectNum.doubleValue() / allInNum;
            Arrays.fill(avgs, avg);

            double[] lsl = new double[24];
            double[] usl = new double[24];
            for (int i = 0; i < num.length; i++) {
                if (null != num[i][0]) {
                    lsl[i] = Math.max(0, avg - 3 * Math.sqrt(avg / num[i][0]));
                    usl[i] = avg + 3 * Math.sqrt(avg / num[i][0]);
                } else {
                    lsl[i] = 0;
                    usl[i] = 0;
                }
            }

            map.put("projectColor", entry.getKey());
            map.put("point", point);
            map.put("avg", avgs);
            map.put("lsl", lsl);
            map.put("usl", usl);
            map.put("hour", x);

            result.add(map);
        }
        return new Result(200, "获取成功", result);
    }


    @GetMapping("/getUControlDetail")
    @ApiOperation("获取U图详情")
    public Result getUControlDetail(String date,
                                    String project,
                                    String color) throws ParseException {
        String startTime = date + " 07:00:00";
        String endTime = TimeUtil.getNextDay(date) + " 07:00:00";
        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.between("pie.time", startTime, endTime)
                .eq("pie.project", project)
                .eq("pie.color", color);
        List<Rate3> rates = dfAoiPieceService.listPieceAndDefectNumGroupByHour(qw);
        Map<String, Integer[][]> projectResNum = new HashMap<>();
        Integer[][] num = new Integer[24][2];
        for (Rate3 rate : rates) {
            Integer hour = rate.getInte1();
            Integer inNum = rate.getInte2();
            Integer DefectNum = rate.getInte3();
            num[(hour + 17) % 24][0] = inNum;  // 为了把7点放到第一位
            num[(hour + 17) % 24][1] = DefectNum;
        }
        List<Object> result = new ArrayList<>();
        int hour = 7;
        for (int i = 0; i < num.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date);
            map.put("time", hour + ":00:00-" + hour + ":59:59");
            hour = (hour + 1) % 24;
            map.put("inNum", num[i][0]);
            map.put("defectNum", num[i][1]);
            if (num[i][0] != null && num[i][1] != null) {
                map.put("rate", num[i][1].doubleValue() / num[i][0]);
            } else {
                map.put("rate", 0);
            }

            result.add(map);
        }
        return new Result(200, "获取成功", result);
    }

	@GetMapping("/exportbject/getSingleQueryPiece")
	@ApiOperation("报表管理-查询报表(注意:+这样的符号要%2B)")
	public Object getSingleQueryPiece(Integer page,Integer limit ,String code, HttpServletResponse response) {
        Page<BatchqueryReport> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<DfAoiPiece>();
        ew.eq(StringUtils.isNotEmpty(code),"temp_piece_latest.BAR_CODE",code);
		List<BatchqueryReport> reportList = dfAoiPieceService.getFqcOrOQCDetailByCode(pages,ew);
		return new Result(200, "查询成功", reportList,(int)pages.getTotal());
	}



//    @GetMapping("/export/singleQueryPiece")
//    @ApiOperation("报表管理-批量查询报表 单个查询并导出(注意:+这样的符号要%2B)")
//    public void singleQueryPiece(@ApiParam("二维码") String code, HttpServletResponse response) {
//        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<DfAoiPiece>();
//        qw.eq(StringUtils.isNotEmpty(code), "fqc.name", code);
//        List<BatchqueryReport> reportList = dfAoiPieceService.getFqcOrOQCDetailByCode(null,qw);
//        try {
//            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
//            String filename = "批量查询报表" + System.currentTimeMillis();
//            excelExportUtil.setExcelResponseProp(response,filename);
//            EasyExcel.write(response.getOutputStream())
//                    .head(BatchqueryReport.class)
//                    .excelType(ExcelTypeEnum.XLSX)
//                    .sheet(filename)
//                    .doWrite(reportList);
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }


    @GetMapping("/export/downloadTemplate")
    @ApiOperation("报表管理-批量查询报表 模板下载")
    public void downloadTemplate(HttpServletResponse response) {
        ArrayList<List<String>> list = new ArrayList<List<String>>();
        List<String> title = Arrays.asList("玻璃条码");
        list.add(title);
        try {
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            String filename = "模板" + System.currentTimeMillis();
            excelExportUtil.setExcelResponseProp(response,filename);
            EasyExcel.write(response.getOutputStream())
                    .head(list)
                    .sheet("模板")
                    .doWrite(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/export/batchQueryPiece")
    @ApiOperation("报表管理-导入模板后批量查询并导出")
    public Object batchQueryPiece(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashMap<>();

            if (file == null || file.isEmpty()) {
                return new Result(0, "上传失败，请选择文件");
            }
            String fileName = file.getOriginalFilename();
            if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
                return new Result(1, "请上传xlsx或xls格式的文件");
            }

            ExcelImportUtil importUtil = new ExcelImportUtil(file);
            List<Map<String, String>> maps = importUtil.readExcelContent();
            List<String> nameList = new ArrayList<>();
            maps.stream().forEach(item->nameList.add(item.get("玻璃条码")));

            QueryWrapper<DfAoiPiece> qw = new QueryWrapper<DfAoiPiece>();
            qw.in(!CollectionUtils.isEmpty(nameList),"temp_piece_latest.BAR_CODE", nameList);
            List<BatchqueryReport> list = dfAoiPieceService.getFqcOrOQCDetailByCode(null,qw);

            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            String filename = "批量查询报表" + System.currentTimeMillis();
            excelExportUtil.setExcelResponseProp(response,filename);
            EasyExcel.write(response.getOutputStream())
                    .head(BatchqueryReport.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("批量查询报表")
                    .doWrite(list);

        } catch (Exception e) {
            logger.error("导入excel接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    @GetMapping("/empCapacityStatement")
    @ApiOperation("员工产能报表导出")
    public void empCapacityStatement(   @ApiParam("开始日期") String startDate, @ApiParam("结束日期")String endDate,
                                        @ApiParam("工厂")String factory,
                                        @ApiParam("线体")String lineBody,
                                        @ApiParam("项目")String project,
                                        @ApiParam("员工编号")String empCode,
                                        HttpServletResponse response) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.eq("number1",1)
                .eq(StringUtils.isNotEmpty(empCode), "user.name", empCode)
                .eq(StringUtils.isNotEmpty(factory),"protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"project",project);
        List<EmpCapacityReport> reportList = dfAoiPieceService.getEmpCapacityStatement(null,qw,startTime,endTime);
        try {
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            String filename = "员工产能报表" + System.currentTimeMillis();
            excelExportUtil.setExcelResponseProp(response,filename);
            EasyExcel.write(response.getOutputStream())
                    .head(EmpCapacityReport.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("员工产能报表")
                    .doWrite(reportList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getEmpCapacityStatement")
    @ApiOperation("员工产能报表查询")
    public Object getEmpCapacityStatement(Integer page,Integer limit,@ApiParam("开始日期") String startDate, @ApiParam("结束日期")String endDate,
                                     @ApiParam("工厂")String factory,
                                     @ApiParam("线体")String lineBody,
                                     @ApiParam("项目")String project,
                                      @ApiParam("员工编号")String empCode) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        Page<EmpCapacityReport> pages = new Page<EmpCapacityReport>(page, limit);
        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.eq("number1",1)
                .eq(StringUtils.isNotEmpty(empCode), "user.name", empCode)
                .eq(StringUtils.isNotEmpty(factory),"protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"project",project);
        List<EmpCapacityReport> reportList = dfAoiPieceService.getEmpCapacityStatement(pages,qw,startTime,endTime);
        return new Result(200,"成功",reportList,(int)pages.getTotal());
    }

    @GetMapping("/OQCReport")
    @ApiOperation("OQC报表导出")
    public void OQCReport( String startDate,  String endDate, String factory,String lineBody,String project,String empCode,HttpServletResponse response) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        QueryWrapper<DfAoiPiece> qw1 = new QueryWrapper<>();
        qw1.between("log.qc_time",startTime,endTime)
            .eq("number1",1)
            .eq(StringUtils.isNotEmpty(factory),"user.factory_id",factory)
            .eq(StringUtils.isNotEmpty(project),"pieceLatest.project",project)
            .eq(StringUtils.isNotEmpty(lineBody),"line_boby_id",lineBody);
        QueryWrapper<DfAoiPiece> qw2 = new QueryWrapper<>();
        qw2.between("log.qc_time",startTime,endTime)
                .eq("number1",1)
                .eq(StringUtils.isNotEmpty(factory),"user.factory_id",factory)
                .eq(StringUtils.isNotEmpty(project),"pieceLatest.project",project)
                .eq(StringUtils.isNotEmpty(lineBody),"line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(empCode),"user.name",empCode);
        List<OqcReport> reportList = dfAoiPieceService.getOQCReport(null,qw1,qw2,startTime,endTime,empCode);
        try {
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            String filename = "OQC报表" + System.currentTimeMillis();
            excelExportUtil.setExcelResponseProp(response,filename);
            EasyExcel.write(response.getOutputStream())
                    .head(EmpCapacityReport.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("OQC报表")
                    .doWrite(reportList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getOQCReport")
    @ApiOperation("OQC报表查询")
    public Object getOQCReport(Integer page,Integer limit,String startDate, String endDate,
                          String factory,
                          String lineBody,
                          String project,
                          String empCode) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        Page<OqcReport> pages = new Page<>(page,limit);
        QueryWrapper<DfAoiPiece> qw1 = new QueryWrapper<>();
        qw1.between("log.qc_time",startTime,endTime)
            .eq("number1",1)
            .eq(StringUtils.isNotEmpty(factory),"user.factory_id",factory)
            .eq(StringUtils.isNotEmpty(project),"pieceLatest.project",project)
            .eq(StringUtils.isNotEmpty(lineBody),"line_boby_id",lineBody);
        QueryWrapper<DfAoiPiece> qw2 = new QueryWrapper<>();
        qw2.between("log.qc_time",startTime,endTime)
                .eq("number1",1)
                .eq(StringUtils.isNotEmpty(factory),"user.factory_id",factory)
                .eq(StringUtils.isNotEmpty(project),"pieceLatest.project",project)
                .eq(StringUtils.isNotEmpty(lineBody),"line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(empCode),"user.name",empCode);
        List<OqcReport> reportList = dfAoiPieceService.getOQCReport(pages,qw1,qw2,startTime,endTime,empCode);
        return new Result(200,"成功",reportList,(int)pages.getTotal());
    }


    @GetMapping("/OQC31Report")
    @ApiOperation("OQC3+1报表")
    public void OQC31Report( String startDate,  String endDate,
                          String factory,
                          String lineBody,
                          String project, String empCode,
                          HttpServletResponse response) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.between("log.qc_time",startTime,endTime)
                .eq("number1",1)
                .eq(StringUtils.isNotEmpty(factory),"user.factory_id",factory)
                .eq(StringUtils.isNotEmpty(project),"pieceLatest.project",project)
                .eq(StringUtils.isNotEmpty(lineBody),"line_boby_id",lineBody);
        List<Map<String,Object>> reportList = dfAoiPieceService.OQC31Report(qw,startTime,endTime,empCode);
        try {
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            String filename = "OQC报表" + System.currentTimeMillis();
            excelExportUtil.setExcelResponseProp(response,filename);
            EasyExcel.write(response.getOutputStream())
                    .head(EmpCapacityReport.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("OQC报表")
                    .doWrite(reportList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @GetMapping("/lineBodyloss")
    @ApiOperation("FQC线体漏检对比")
    public Object lineBodyloss(String startDate, String endDate,
                          String factory,
                          String lineBody,
                          String project,String color , String classes) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.between("undetected.create_time",startTime,endTime)
                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",0,11)
                .eq("log.number1",1)
                .eq("piece.number1",1)
                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
        List<Rate3> listA = dfAoiPieceService.lineBodyloss(qw,startTime,endTime,"白班");


        QueryWrapper<DfAoiPiece> qw2 = new QueryWrapper<>();
        qw.between("undetected.create_time",startTime,endTime)
                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",12,23)
                .eq("log.number1",1)
                .eq("piece.number1",1)
                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
        List<Rate3> listB = dfAoiPieceService.lineBodyloss(qw2,startTime,endTime,"夜班");

        TreeSet<String> machineSet = new TreeSet<>();
        TreeSet<String> featureSet = new TreeSet<>();
        listA.stream().forEach(item->{
            machineSet.add(item.getStr1());
            featureSet.add(item.getStr2());
        });
        listB.stream().forEach(item->{
            machineSet.add(item.getStr1());
            featureSet.add(item.getStr2());
        });

        ArrayList<String> listMachine = new ArrayList<>(machineSet);
        ArrayList<String> listFeature = new ArrayList<>(featureSet);

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        ArrayList<Object> listDay = new ArrayList<>();
        for (String feature : listFeature) {
            ArrayList<Object> array = new ArrayList<>();
            for (String machine : listMachine) {
                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
                if (rate3 != null){
                    array.add(rate3.getDou1()*escapeData);
                }else{
                    array.add(null);
                }
            }
            listDay.add(array);
        }

        ArrayList<Object> listNight = new ArrayList<>();
        for (String feature : listFeature) {
            ArrayList<Object> array = new ArrayList<>();
            for (String machine : listMachine) {
                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
                if (rate3 != null){
                    array.add(rate3.getDou1()*escapeData);
                }else{
                    array.add(null);
                }
            }
            listNight.add(array);
        }

        List<Rate3> day = dfAoiPieceService.lineBodylossByMachine(qw,startTime,endTime,"白班");
        List<Rate3> night = dfAoiPieceService.lineBodylossByMachine(qw2,startTime,endTime,"夜班");
        ArrayList<String> dayZ = new ArrayList<>();
        ArrayList<String> nightZ = new ArrayList<>();
        for (Rate3 one : day) {
            dayZ.add(one.getDou1().toString());
        }
        for (Rate3 one : night) {
            nightZ.add(one.getDou1().toString());
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("machineId", listMachine);
        map.put("process",listFeature);
        map.put("day",listDay);
        map.put("night",listNight);
        map.put("dayZ", dayZ);
        map.put("nightZ", nightZ);

        return new Result(200,"获取漏检占比结果成功",map);
    }

//    @GetMapping("/lineBodylossV2")
//    @ApiOperation("FQC线体漏检对比V2")
//    public Object lineBodylossV2(String startDate, String endDate,
//                                 String factory,
//                                 String lineBody,
//                               String project, String color , String classes) throws ParseException {
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//
//        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
//        qw.between("undetected.create_time",startTime,endTime)
//                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",0,11)
//                .eq("log.number1",1)
//                .eq("piece.number1",1)
//                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
//                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
//                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
//                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
//        List<Rate3> listA = dfAoiPieceService.lineBodylossV2(qw,startTime,endTime,"白班");
//
//
//        QueryWrapper<DfAoiPiece> qw2 = new QueryWrapper<>();
//        qw.between("undetected.create_time",startTime,endTime)
//                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",12,23)
//                .eq("number1",1)
//                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
//                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
//                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
//                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
//        List<Rate3> listB = dfAoiPieceService.lineBodylossV2(qw2,startTime,endTime,"夜班");
//
//        LinkedHashSet<String> machineSet = new LinkedHashSet<>();
//        LinkedHashSet<String> featureSet = new LinkedHashSet<>();
//        listA.stream().forEach(item->{
//            machineSet.add(item.getStr1());
//            featureSet.add(item.getStr2());
//        });
//        listB.stream().forEach(item->{
//            machineSet.add(item.getStr1());
//            featureSet.add(item.getStr2());
//        });
//
//        ArrayList<String> listMachine = new ArrayList<>(machineSet);
//        ArrayList<String> listFeature = new ArrayList<>(featureSet);
//
//        //AOI配置漏检基数
//        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
//        aoiConfigDataWrapper
//                .eq("config_name","AOI漏检基数")
//                .last("limit 1");
//        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
//        Double escapeData = dfAoiConfigData.getConfigValue();
//
//        ArrayList<List<Double>> listDay = new ArrayList<>();
//        for (String machine : listMachine) {
//            ArrayList<Double> array = new ArrayList<>();
//            for (String feature : listFeature) {
//                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
//                if (rate3 != null) {
//                    array.add(rate3.getDou1()*escapeData);
//                } else {
//                    array.add(Double.valueOf(0));
//                }
//            }
//            listDay.add(array);
//        }
//
//        ArrayList<List<Double>> listNight = new ArrayList<>();
//        for (String machine : listMachine) {
//            ArrayList<Double> array = new ArrayList<>();
//            for (String feature : listFeature) {
//                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
//                if (rate3 != null){
//                    array.add(rate3.getDou1()*escapeData);
//                }else{
//                    array.add(Double.valueOf(0));
//                }
//            }
//            listNight.add(array);
//        }
//
//        ArrayList<Double> dayZ = new ArrayList<>();
//        ArrayList<Double> nightZ = new ArrayList<>();
//
//
//        for (List<Double> machineRate : listDay) {
//            double sum = 0;
//            for (Double rate : machineRate) {
//                if (null != rate) {
//                    sum += rate;
//                }
//            }
//            dayZ.add(sum);
//        }
//        for (List<Double> machineRate : listNight) {
//            double sum = 0;
//            for (Double rate : machineRate) {
//                if (null != rate) {
//                    sum += rate;
//                }
//            }
//            nightZ.add(sum);
//        }
//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("machineId", listMachine);
//        map.put("process",listFeature);
//        map.put("day",listDay);
//        map.put("night",listNight);
//        map.put("dayZ", dayZ);
//        map.put("nightZ", nightZ);
//
//        return new Result(200,"查询成功",map);
//    }

    @GetMapping("/lineBodylossV2")
    @ApiOperation("FQC线体漏检对比V2")
    public Object lineBodylossV2(String startDate, String endDate,
                                 String factory,
                                 String lineBody,
                                 String project, String color , String classes) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        //查询前10的缺陷名
        QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
        ew.eq("no",1);
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
            ew.between("TIME", startTime, endTime);
        }
        ew.eq(StringUtils.isNotEmpty(factory), "FACTORY",factory)
                .eq(StringUtils.isNotEmpty(lineBody), "LINE_BODY",lineBody)
                .eq(StringUtils.isNotEmpty(project), "PROJECT",project)
                .eq(StringUtils.isNotEmpty(color), "COLOR",color);

        List<Map<String,Object>> list = dfAoiUndetectedService.fqcNgTopRate(ew,5);
        LinkedHashSet<String> empList = new LinkedHashSet<>();
        LinkedHashSet<String> featureList = new LinkedHashSet<>();

        //获取Top5缺陷封装进集合
        for (Map<String, Object> map : list) {
            featureList.add(map.get("FEATUREVALUES").toString());
        }

        //将缺陷集合拼接成in查询的形式
        StringBuilder result = new StringBuilder("(");
        int i = 0;
        for ( String s : featureList) {
            if (i > 0) {
                result.append(",");
            }
            result.append("'").append(s).append("'");
            i++;
        }
        result.append(")");

        //找到Top5缺陷员工占比的倒叙
        QueryWrapper<DfAoiDefect> ew2 = new QueryWrapper<>();
        ew2.apply(!CollectionUtils.isEmpty(featureList),"FEATUREVALUES in " + result.toString());

        List<Rate3> empTopFeaRateDescList  = dfAoiPieceService.empTopFeaRateDesc(ew,ew2);
        //放入员工集合,取前十
        empTopFeaRateDescList.stream().limit(10).forEach(item->empList.add(item.getStr1()));
        //用来存放所有类型和对应员工不良率数据
        ArrayList<Rate3> listRate3 = new ArrayList<>();
        //查找员工对应所有Top对应缺陷不良率
        List<Rate3> rate3List = dfAoiPieceService.empLossChemkRankTop10DayNight(ew,ew2);

        String[][] stringsDay = new String[featureList.size()][empList.size()];
        String[][] stringsNight = new String[featureList.size()][empList.size()];

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        int row = 0;
        for (String feature : featureList) {
            int column = 0;
            for (String emp : empList) {
                Rate3 rate3Day = rate3List.stream().filter(item -> "day".equals(item.getStr3()) && feature.equals(item.getStr2()) && emp.equals(item.getStr1())).findAny().orElse(null);
                if(rate3Day != null){
                    stringsDay[row][column] = String.valueOf(rate3Day.getDou1() * escapeData);
                }else{
                    stringsDay[row][column] = null;
                }

                Rate3 rate3Night = rate3List.stream().filter(item -> "night".equals(item.getStr3()) && feature.equals(item.getStr2()) && emp.equals(item.getStr1())).findAny().orElse(null);
                if(rate3Night != null){
                    stringsNight[row][column] = String.valueOf(rate3Night.getDou1() * escapeData);
                }else{
                    stringsNight[row][column] = null;
                }
                column ++;
            }
            row ++;
        }

        double[] columnSumsDay = new double[empList.size()];
        double[] columnSumsNight = new double[empList.size()];

        // 计算每一列的和
        for (int col = 0; col < empList.size(); col++) {
            for (int row2 = 0; row2 < featureList.size(); row2++) {
                if (stringsDay[row2][col] != null){
                    columnSumsDay[col] += Double.valueOf(stringsDay[row2][col]);
                }
                if (stringsNight[row2][col] != null){
                    columnSumsNight[col] += Double.valueOf(stringsNight[row2][col]);
                }
            }
        }

        HashMap<Object, Object> map = new HashMap<>();
        map.put("machineId", empList);
        map.put("process",featureList);
        map.put("day",stringsDay);
        map.put("night",stringsNight);
        map.put("dayZ", columnSumsDay);
        map.put("nightZ", columnSumsNight);

        return new Result(200,"查询成功",map);
    }

	@GetMapping("/lossEmpTop10")
	@ApiOperation("漏检员工Top10V2")
	public Object lossEmpTop10V2(String startDate, String endDate,
								 String factory,
								 String lineBody,
								 String project, String color) throws ParseException {
		String startTime = startDate + " 07:00:00";
		String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

		QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
		ew.between("temp_undetected_latest.CREATE_TIME", startTime,endTime)
                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
		List<Map<String,String>> list = dfAoiPieceService.top5Feature(ew);
        ArrayList<ArrayList<Object>> dataList = new ArrayList<>();
        ArrayList<Object> featureList = new ArrayList<>();
        ArrayList<Object> userList = new ArrayList<>();
        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        for (Map<String, String> map : list) {
            String featurevalues = map.get("FEATUREVALUES");
            featureList.add(featurevalues);
            QueryWrapper<DfAoiPiece> ew2 = new QueryWrapper<>();
            ew2.between("temp_fqc_latest.QC_TIME", startTime, endTime)
                .eq(StringUtils.isNotEmpty(factory),"temp_piece_latest.FACTORY" ,factory)
                .eq(StringUtils.isNotEmpty(lineBody),"temp_piece_latest.LINE_BODY" ,lineBody )
                .eq(StringUtils.isNotEmpty(project),"temp_piece_latest.PROJECT" ,project )
                .eq(StringUtils.isNotEmpty(color),"temp_piece_latest.COLOR" ,color);
            List<Map<String,Object>> mapList = dfAoiPieceService.selectByfeature(ew2,featurevalues);
            ArrayList<Object> user = new ArrayList<>();
            for (Map<String, Object> objectMap : mapList) {
                if (objectMap == null || objectMap.get("rate") == null) {
                    user.add(null);
                } else {
                    user.add(Double.parseDouble(objectMap.get("rate").toString())*escapeData);
                    linkedHashMap.put(objectMap.get("ALIAS"),objectMap.get("ALIAS"));
                }
            }
            dataList.add(user);
        }
        for (Map.Entry<Object, Object> entry : linkedHashMap.entrySet()) {
            userList.add(entry.getKey());
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("emp",userList);
        map.put("feature",featureList);
        map.put("data",dataList);

        return new Result(200,"查询成功",map);
	}

//    @GetMapping("/lossEmpTop10")
    @ApiOperation("漏检员工Top10")
    public Object lossEmpTop10(String startDate, String endDate,
                               String factory,
                               String lineBody,
                               String project,String color ) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";

        QueryWrapper<DfAoiPiece> qw = new QueryWrapper<>();
        qw.between("undetected.create_time",startTime,endTime)
                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",0,11)
                .eq("number1",1)
                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
        List<Rate3> listA = dfAoiPieceService.lossEmpTop10(qw,startTime,endTime,"白班");


        QueryWrapper<DfAoiPiece> qw2 = new QueryWrapper<>();
        qw.between("undetected.create_time",startTime,endTime)
                .between("HOUR(DATE_SUB(undetected.create_time, INTERVAL 7 HOUR))",12,23)
                .eq("number1",1)
                .eq(StringUtils.isNotEmpty(factory), "protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"piece.project",project)
                .eq(StringUtils.isNotEmpty(color),"piece.color",color);
        List<Rate3> listB = dfAoiPieceService.lossEmpTop10(qw2,startTime,endTime,"夜班");

        LinkedHashSet<String> machineSet = new LinkedHashSet<>();
        LinkedHashSet<String> featureSet = new LinkedHashSet<>();
        listA.stream().forEach(item->{
            machineSet.add(item.getStr1());
            featureSet.add(item.getStr2());
        });
        listB.stream().forEach(item->{
            machineSet.add(item.getStr1());
            featureSet.add(item.getStr2());
        });

        ArrayList<String> listMachine = new ArrayList<>(machineSet);
        ArrayList<String> listFeature = new ArrayList<>(featureSet);

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        ArrayList<List<Double>> listDay = new ArrayList<>();
        for (String feature : listFeature) {
            ArrayList<Double> array = new ArrayList<>();
            for (String machine : listMachine) {
                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
                if (rate3 != null) {
                    array.add(rate3.getDou1()*escapeData);
                } else {
                    array.add(Double.valueOf(0));
                }
            }
            listDay.add(array);
        }

        ArrayList<List<Double>> listNight = new ArrayList<>();
        for (String feature : listFeature) {
            ArrayList<Double> array = new ArrayList<>();
            for (String machine : listMachine) {
                Rate3 rate3 = listA.stream().filter(itme -> itme.getStr1().equals(machine) && itme.getStr2().equals(feature)).findAny().orElse(null);
                if (rate3 != null){
                    array.add(rate3.getDou1()*escapeData);
                }else{
                    array.add(Double.valueOf(0));
                }
            }
            listNight.add(array);
        }

        ArrayList<Double> dayZ = new ArrayList<>();
        ArrayList<Double> nightZ = new ArrayList<>();
        for (List<Double> machineRate : listDay) {
            double sum = 0;
            for (Double rate : machineRate) {
                if (null != rate) {
                    sum += rate;
                }
            }
            dayZ.add(sum);
        }

        for (List<Double> machineRate : listNight) {
            double sum = 0;
            for (Double rate : machineRate) {
                if (null != rate) {
                    sum += rate;
                }
            }
            nightZ.add(sum);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("machineId", listMachine);
        map.put("process",listFeature);
        map.put("day",listDay);
        map.put("night",listNight);
        map.put("dayZ", dayZ);
        map.put("nightZ", nightZ);

        return new Result(200,"查询成功",map);
    }

    @GetMapping("fqcInputOutPerHour")
    @ApiOperation("线体看板明细 - FQC每小时投入产出")
    public Object fqcInputOutPerHour(@ApiParam("厂别") @RequestParam(required = false) String factory,
                               @ApiParam("线体") @RequestParam(required = false) String lineBody,
                               @ApiParam("时间-开始") @RequestParam(required = false) String startDate,
                               @ApiParam("时间-结束") @RequestParam(required = false) String endDate,
                               @ApiParam("项目") @RequestParam(required = false) String project,
                               @ApiParam("颜色") @RequestParam(required = false) String color,
                               @ApiParam("班别(传白班,夜班)") @RequestParam(required = false) String clazz
    ) throws ParseException {
        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        int startHour, endHour;
        if ("白班".equals(clazz)) {
            startHour = 0;
            endHour = 11;
        } else if ("夜班".equals(clazz)) {
            startHour = 12;
            endHour = 23;
        } else {
            startHour = 0;
            endHour = 23;
        }
        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
        ew.eq("no",1);
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
            ew.between("TIME", startTime, endTime);
        }
        ew.between("hour(date_sub(TIME, interval 7 HOUR))",startHour , endHour)
                .eq(StringUtils.isNotEmpty(factory), "FACTORY",factory)
                .eq(StringUtils.isNotEmpty(lineBody), "LINE_BODY",lineBody)
                .eq(StringUtils.isNotEmpty(project), "PROJECT",project)
                .eq(StringUtils.isNotEmpty(color), "COLOR",color);
        HashMap<Object, Object> map = new HashMap<>();
        ArrayList<Object> listOk = new ArrayList<>();
        ArrayList<Object> lisHour = new ArrayList<>();
        ArrayList<Object> listTotal = new ArrayList<>();
        ArrayList<Object> listRate = new ArrayList<>();
        List<Map> mapList = dfAoiPieceService.fqcInputOut(ew);
        for (Map<String, Object> one : mapList) {
            listOk.add(one.get("ok"));
            lisHour.add(one.get("hour"));
            listTotal.add(one.get("total"));
            listRate.add(one.get("rate"));
        }
        Integer total = mapList.stream().collect(Collectors.summingInt(m -> Integer.parseInt(m.get("total").toString())));
        Integer ok = mapList.stream().collect(Collectors.summingInt(m -> Integer.parseInt(m.get("ok").toString())));
        map.put("listOk",listOk);
        map.put("lisHour",lisHour);
        map.put("listTotal",listTotal);
        map.put("listRate",listRate);
        map.put("total",total);
        map.put("ok",ok);
        return new Result(200,"查询成功",map);
    }

    @GetMapping("lossCheck")
    @ApiOperation("线体看板明细 - 漏检")
    public Object lossCheck(@ApiParam("厂别") @RequestParam(required = false) String factory,
                              @ApiParam("线体") @RequestParam(required = false) String lineBody,
                              @ApiParam("时间-开始") @RequestParam(required = false) String startDate,
                              @ApiParam("时间-结束") @RequestParam(required = false) String endDate,
                              @ApiParam("项目") @RequestParam(required = false) String project,
                              @ApiParam("颜色") @RequestParam(required = false) String color ) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
            ew.between("temp_fqc_latest.qc_time",startTime , endTime)
                .eq(StringUtils.isNotEmpty(factory),"df_aoi_seat_protect.factory_id",factory)
                .eq(StringUtils.isNotEmpty(lineBody),"df_aoi_seat_protect.line_boby_id",lineBody)
                .eq(StringUtils.isNotEmpty(project),"temp_piece_latest.project",project)
                .eq(StringUtils.isNotEmpty(color),"temp_piece_latest.color",color)
                .eq("temp_piece_latest.no",1)
                .eq("temp_fqc_latest.no", 1);
        List<Map<String,Object>> list = dfAoiPieceService.lossCheck(ew);
        Map<Object, List<Map<String, Object>>> QcUserName = list.stream().collect(Collectors.groupingBy(m -> m.get("qc_user_name")));
        return new Result(200,"查询成功",QcUserName);
    }

    @GetMapping("machineDetail")
    @ApiOperation("线体看板明细 - 信息汇总")
    public Object machineDetail(@ApiParam("厂别") @RequestParam(required = false) String factory,
                            @ApiParam("线体") @RequestParam(required = false) String lineBody,
                            @ApiParam("时间-开始") @RequestParam String startDate,
                            @ApiParam("时间-结束") @RequestParam String endDate,
                            @ApiParam("项目") @RequestParam(required = false) String project,
                            @ApiParam("颜色") @RequestParam(required = false) String color ) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        //求出机台对应一次良率,综合良率,最终良率
        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
       ew.between("time",startTime,endTime)
			.eq(StringUtils.isNotEmpty(factory),"FACTORY",factory)
			.eq(StringUtils.isNotEmpty(project),"project",project)
			.eq(StringUtils.isNotEmpty(lineBody),"LINE_BODY",lineBody)
			.eq(StringUtils.isNotEmpty(color),"COLOR",color);
		List<Rate3> rate3 = dfAoiPieceService.getDetailByMachine(ew);
		//几天后面加#号
        rate3.stream().forEach(m->m.setStr1(m.getStr1() + "#"));
		return new Result(200,"查询成功",rate3);
    }

//    @GetMapping("empLossChemkRank")
//    @ApiOperation("员工漏检排名")
//    public Object empLossChemkRank(@ApiParam("厂别") @RequestParam(required = false) String factory,
//                                @ApiParam("线体") @RequestParam(required = false) String lineBody,
//                                @ApiParam("时间-开始") @RequestParam String startDate,
//                                @ApiParam("时间-结束") @RequestParam String endDate,
//                                @ApiParam("项目") @RequestParam(required = false) String project,
//                                @ApiParam("颜色") @RequestParam(required = false) String color ) throws ParseException {
//
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        //求出机台对应一次良率,综合良率,最终良率
//        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
//        ew.between("temp_fqc_latest.QC_TIME",startTime,endTime)
//                .eq(StringUtils.isNotEmpty(factory),"temp_piece_latest.FACTORY",factory)
//                .eq(StringUtils.isNotEmpty(project),"temp_piece_latest.PROJECT",project)
//                .eq(StringUtils.isNotEmpty(lineBody),"temp_piece_latest.LINE_BODY",lineBody)
//                .eq(StringUtils.isNotEmpty(color),"temp_piece_latest.COLOR",color);
//        List<Rate3> rate3 = dfAoiPieceService.empLossChemkRank(ew);
//        LinkedHashSet<String> empList = new LinkedHashSet<>();
//        LinkedHashSet<String> featureList = new LinkedHashSet<>();
//
//        //AOI配置漏检基数
//        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
//        aoiConfigDataWrapper
//                .eq("config_name","AOI漏检基数")
//                .last("limit 1");
//        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
//        Double escapeData = dfAoiConfigData.getConfigValue();
//
//        for (Rate3 rate : rate3) {
//            empList.add(rate.getStr2());
//            featureList.add(rate.getStr3());
//        }
//
//        String[][] strings = new String[featureList.size()][empList.size()];
//        int x = 0;
//        for (String f : featureList) {
//            int y = 0;
//            for (String e : empList) {
//                Rate3 orElse = rate3.stream().filter(item -> f.equals(item.getStr3()) && e.equals(item.getStr2())).findAny().orElse(null);
//                if (orElse != null){
//                    strings[x][y] = String.valueOf(orElse.getDou1()*escapeData);
//                }else {
//                    strings[x][y] = null;
//                }
//                y++;
//            }
//            x++;
//        }
//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("empList",empList);
//        map.put("featureList",featureList);
//        map.put("data",strings);
//        return new Result(200,"查询成功",map);
//    }

    @GetMapping("empLossChemkRank")
    @ApiOperation("员工漏检排名")
    public Object empLossChemkRank(@ApiParam("厂别") @RequestParam(required = false) String factory,
                                   @ApiParam("线体") @RequestParam(required = false) String lineBody,
                                   @ApiParam("时间-开始") @RequestParam String startDate,
                                   @ApiParam("时间-结束") @RequestParam String endDate,
                                   @ApiParam("项目") @RequestParam(required = false) String project,
                                   @ApiParam("颜色") @RequestParam(required = false) String color ) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        //查询前5的缺陷名
        QueryWrapper<DfAoiDefect> ew = new QueryWrapper<>();
        ew.eq("no",1);
        if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)){
            ew.between("TIME", startTime, endTime);
        }
        ew.eq(StringUtils.isNotEmpty(factory), "FACTORY",factory)
                .eq(StringUtils.isNotEmpty(lineBody), "LINE_BODY",lineBody)
                .eq(StringUtils.isNotEmpty(project), "PROJECT",project)
                .eq(StringUtils.isNotEmpty(color), "COLOR",color);

        List<Map<String,Object>> list = dfAoiUndetectedService.fqcNgTopRate(ew,5);
        LinkedHashSet<String> empList = new LinkedHashSet<>();
        LinkedHashSet<String> featureList = new LinkedHashSet<>();

        //获取Top5缺陷封装进集合
        for (Map<String, Object> map : list) {
            featureList.add(map.get("FEATUREVALUES").toString());
        }

        //将缺陷集合拼接成in查询的形式
        StringBuilder result = new StringBuilder("(");
        int i = 0;
        for ( String s : featureList) {
            if (i > 0) {
                result.append(",");
            }
            result.append("'").append(s).append("'");
            i++;
        }
        result.append(")");

        //找到Top5缺陷员工占比的倒叙
        QueryWrapper<DfAoiDefect> ew2 = new QueryWrapper<>();
        ew2.apply(!CollectionUtils.isEmpty(featureList),"FEATUREVALUES in " + result.toString());

        List<Rate3> empTopFeaRateDescList  = dfAoiPieceService.empTopFeaRateDesc(ew,ew2);
        //放入员工集合
        empTopFeaRateDescList.stream().forEach(item->empList.add(item.getStr1()));
        //用来存放所有类型和对应员工不良率数据
        ArrayList<Rate3> listRate3 = new ArrayList<>();
        //遍历每一个缺陷找每个用户对应缺陷不良率
        for (String feature : featureList) {
            List<Rate3> rate3 = dfAoiPieceService.empLossChemkRankV2(ew,feature);
            listRate3.addAll(rate3);
        }
        String[][] strings = new String[featureList.size()][empList.size()];

        //AOI配置漏检基数
        QueryWrapper<DfAoiConfigData> aoiConfigDataWrapper = new QueryWrapper<>();
        aoiConfigDataWrapper
                .eq("config_name","AOI漏检基数")
                .last("limit 1");
        DfAoiConfigData dfAoiConfigData = dfAoiConfigDataService.getOne(aoiConfigDataWrapper);
        Double escapeData = dfAoiConfigData.getConfigValue();

        int row = 0;
        for (String feature : featureList) {
            int column = 0;
            for (String emp : empList) {
                Rate3 rate3 = listRate3.stream().filter(item -> feature.equals(item.getStr1()) && emp.equals(item.getStr2())).findAny().orElse(null);
                if(rate3 != null){
                    strings[row][column] = String.valueOf(rate3.getDou1() * escapeData);
                }else{
                    strings[row][column] = null;
                }
                column ++;
            }
            row ++;
        }

        HashMap<Object, Object> map = new HashMap<>();
        map.put("empList",empList);
        map.put("featureList",featureList);
        map.put("data",strings);
        return new Result(200,"查询成功",map);
    }

//    @GetMapping("OQCReport")
//    @ApiOperation("OQC报表导出")
//    public Object machineDetail(@ApiParam("厂别") @RequestParam(required = false) String factory,
//                                @ApiParam("线体") @RequestParam(required = false) String lineBody,
//                                @ApiParam("时间-开始") @RequestParam String startDate,
//                                @ApiParam("时间-结束") @RequestParam String endDate,
//                                @ApiParam("项目") @RequestParam(required = false) String project,
//                                @ApiParam("员工编号") @RequestParam(required = false) String empCode ) throws ParseException {
//
//        String startTime = startDate + " 07:00:00";
//        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
//        //求出机台对应一次良率,综合良率,最终良率
//        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
//        ew.between("time",startTime,endTime)
//                .eq(StringUtils.isNotEmpty(factory),"FACTORY",factory)
//                .eq(StringUtils.isNotEmpty(project),"project",project)
//                .eq(StringUtils.isNotEmpty(lineBody),"LINE_BODY",lineBody)
//                .eq(StringUtils.isNotEmpty(color),"COLOR",color);
//        List<Rate3> rate3 = dfAoiPieceService.getDetailByMachine(ew);
//    }



    @GetMapping("/aoiReport")
    @ApiOperation("AOI报表")
    public Object machineDetail(@ApiParam("厂别") @RequestParam(required = false) String factory,
                                @ApiParam("线体") @RequestParam(required = false) String lineBody,
                                @ApiParam("时间-开始") @RequestParam String startDate,
                                @ApiParam("时间-结束") @RequestParam String endDate,
                                @ApiParam("机台号-可多选") @RequestParam(required = false) List<String> machineList,
                                @ApiParam("统计类型 传:小时/天") @RequestParam String type) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        //求出机台对应一次良率,综合良率,最终良率
        QueryWrapper<DfAoiPiece> ew = new QueryWrapper<>();
        ew.between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"time", startTime, endTime)
            .eq(StringUtils.isNotEmpty(lineBody), "line_body",lineBody);
        List<Map<String,Object>> list = new ArrayList<>();
        switch (type){
            case "小时":
                list = dfAoiPieceService.getAoiReportByHour(ew);
                break;
            case "天":
                list = dfAoiPieceService.getAoiReportByDay(ew);
                break;
        }
        return new Result(200,"查询成功",list);
    }


    @GetMapping("/listOQCReport")
    @ApiOperation("OQC3+1报表")
    public Object OQCReport(@ApiParam("厂别") @RequestParam(required = false) String factory,
                                @ApiParam("线体") @RequestParam(required = false) String lineBody,
                                @ApiParam("时间-开始") @RequestParam String startDate,
                                @ApiParam("时间-结束") @RequestParam String endDate,
                                @ApiParam("项目") @RequestParam(required = false) String project,
                                @ApiParam("OQC员工工号") @RequestParam(required = false) String empCode) throws ParseException {

        String startTime = startDate + " 07:00:00";
        String endTime = TimeUtil.getNextDay(endDate) + " 07:00:00";
        //查出所有缺陷类型
        QueryWrapper<DfAoiFlawConfig> dfAoiFlawConfigQueryWrapper = new QueryWrapper<>();
        dfAoiFlawConfigQueryWrapper.select("DISTINCT FLAW_NAME")
                .apply("BIG_AREA in ('孔')\n" +
                        "UNION\n" +
                        "select DISTINCT FLAW_NAME\n" +
                        "from DF_AOI_FLAW_CONFIG\n" +
                        "where BIG_AREA in ('用户面')");
        List<DfAoiFlawConfig> list = dfAoiFlawConfigService.list(dfAoiFlawConfigQueryWrapper);
        List<String> flawNameList = list.stream().map(DfAoiFlawConfig::getFlawName).collect(Collectors.toList());
        String beforeSql = "";
        String afterSql = "";
        for (String flawName : flawNameList) {
            beforeSql += ",SUM(IF (FEATUREVALUES='"+ flawName +"',1,0)) as '"+flawName +"' ";
            afterSql += ",SUM('"+ flawName +"') as '"+flawName +"'";
        }
        QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper = new QueryWrapper<>();
        /**
         * temp_oqc_latest.QC_TIME BETWEEN 1 AND 2
         * and temp_piece.FACTORY IS NOT NULL
         * AND temp_piece.LINE_BODY IS NOT NULL
         * AND temp_piece.PROJECT IS NOT NULL
         * AND temp_oqc_latest.ID IS NOT NULL
         * AND temp_oqc_latest.QC_USER_CODE IS NOT NULL
         */
        dfAoiPieceQueryWrapper.between("temp_oqc_latest.QC_TIME",startTime,endTime)
                .eq(StringUtils.isNotEmpty(factory), "temp_piece.FACTORY",factory)
                .eq(StringUtils.isNotEmpty(lineBody), "temp_piece.LINE_BODY", lineBody)
                .eq(StringUtils.isNotEmpty(project),"temp_piece.PROJECT",project)
                .eq(StringUtils.isNotEmpty(empCode),"temp_oqc_latest.QC_USER_CODE",empCode)
                .isNotNull("temp_oqc_latest.QC_USER_CODE");
        List<Map<String,String>> reportList = dfAoiPieceService.listOQCReport(dfAoiPieceQueryWrapper,beforeSql,afterSql);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("flawNameList",flawNameList);
        map.put("reportList",reportList);
        return new Result(200,"查询成功",map);
    }

}
