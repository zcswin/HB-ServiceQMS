package com.ww.boengongye.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DTO.FilterCondition;
import com.ww.boengongye.entity.DTO.orderCondition;
import com.ww.boengongye.entity.DTO.xlsqueryDTO;
import com.ww.boengongye.entity.EquipmentPictureVO;
import com.ww.boengongye.service.EquipmentMaintenanceService;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * @autor 96901
 * @date 2024/12/24
 */


@Controller
@RequestMapping("/jiaTai/open")
@ApiOperation("excel查看")
@ResponseBody
@CrossOrigin
public class EquipmentMaintenanceController {


    @Resource
    private EquipmentMaintenanceService equipmentMaintenanceService;


    //
    @PostMapping("/queryVb")
    public Result handleRequest(@RequestBody xlsqueryDTO requestDto) {

        FilterCondition filterCondition = requestDto.getFilterCondition();
        String[] timeRange = filterCondition == null? new String[0] : filterCondition.getTriggerTime();
        String begin = null;
        String end = null;
        if (timeRange.length == 2) {
            begin = timeRange[0];
            end = timeRange[1];
        }
        if (begin == null) {
            begin = "2024-12-16 00:00:00";
        }
        if (end == null) {
            end = "2024-12-29 00:00:00";
        }
        System.out.println("Begin Time: " + begin);
        System.out.println("End Time: " + end);





        // 从 xlsqueryDTO 中获取 FilterCondition 并取出其中的数据
        // FilterCondition filterCondition = requestDto.getFilterCondition();
        // TimeRangeDto timeRange = filterCondition == null ? null : filterCondition.getTimeRange();
        //
        //
        // String begin = (timeRange == null || timeRange.getBegin() == null)? "2024-12-16 00:00:00" : timeRange.getBegin();
        // String end = (timeRange == null || timeRange.getEnd() == null)? "2024-12-29 00:00:00" : timeRange.getEnd();


        // String begin = timeRange == null ? null : timeRange.getBegin();
        //
        // String end = timeRange == null ? null : timeRange.getEnd();

        System.out.println("Begin Time: " + begin);
        System.out.println("End Time: " + end);


        String processKeyword = filterCondition == null ? null : filterCondition.getProcess();
        String machineNumber = filterCondition == null ? null : filterCondition.getMachineNumber();
        String problemDetail = filterCondition == null ? null : filterCondition.getProblem_detail();
        String maintenanceSuggestion = filterCondition == null ? null : filterCondition.getMaintenanceSuggestion();
        String dataSource = filterCondition == null ? null : filterCondition.getDataSource();
        System.out.println("Process Keyword: " + processKeyword);
        System.out.println("Machine Number: " + machineNumber);
        System.out.println("Problem Detail: " + problemDetail);
        System.out.println("Maintenance Suggestion: " + maintenanceSuggestion);
        System.out.println("Data Source: " + dataSource);


        // 从 xlsqueryDTO 中获取 orderCondition 并取出其中的数据
        orderCondition order = requestDto.getOrderCondition();
        String triggerTimeOrder = order == null ? null : order.getTriggerTimeOrder();
        String processKeywordOrder = order == null ? null : order.getProcesskeywordorder();
        String machineNumberOrder = order == null ? null : order.getMachineNumberorder();
        String problemDetailOrder = order == null ? null : order.getProblemdetailorder();
        String maintenanceSuggestionOrder = order == null ? null : order.getMaintenanceSuggestionorder();
        String dataSourceOrder = order == null ? null : order.getDataSourceorder();
        // System.out.println("Trigger Time Order: " + triggerTimeOrder);
        // System.out.println("Process Keyword Order: " + processKeywordOrder);
        // System.out.println("Machine Number Order: " + machineNumberOrder);
        // System.out.println("Problem Detail Order: " + problemDetailOrder);
        // System.out.println("Maintenance Suggestion Order: " + maintenanceSuggestionOrder);
        // System.out.println("Data Source Order: " + dataSourceOrder);


        // 从 xlsqueryDTO 中获取 pageNum 和 pageSize
        // int pageNum = requestDto.getCurrentPageNum();
        // int pageSize = requestDto.getPageSize();
        int pageNum = requestDto.getCurrentPageNum() == 0? 1 : requestDto.getCurrentPageNum();
        int pageSize = requestDto.getPageSize() == 0? 10 : requestDto.getPageSize();

        // System.out.println("Page Number: " + pageNum);
        // System.out.println("Page Size: " + pageSize);


        // 进行时间格式转换
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = begin == null ? null : LocalDateTime.parse(begin, formatter);
        LocalDateTime endTime = end == null ? null : LocalDateTime.parse(end, formatter);


        //调用 equipmentMaintenanceService 的 buildDynamicQuery 方法进行查询
        IPage<EquipmentPictureVO> t1 = equipmentMaintenanceService.buildDynamicQuery(
                startTime, endTime,
                triggerTimeOrder,
                processKeyword, processKeywordOrder,
                machineNumber, machineNumberOrder,
                problemDetail, problemDetailOrder,
                maintenanceSuggestion, maintenanceSuggestionOrder,
                dataSource, dataSourceOrder,
                pageNum, pageSize);


        // 最后返回一个响应给客户端
        return new Result(200, "查询成功", t1);
    }






    @GetMapping("/queryprocess")
    public Result handleRequest(){

        List<String> t1 = equipmentMaintenanceService.findDistinctProcessesByDateRange();


        return new Result(200, "查询成功", t1);

    }




































    //
    //     @PostMapping("/handleRequest")
    //     public Result handleRequest(@RequestBody xlsqueryDTO requestDto) {
    //
    //
    //
    //
    //        // 从 xlsqueryDTO 中获取 FilterCondition 并取出其中的数据
    //         FilterCondition filterCondition = requestDto.getFilterCondition();
    //         TimeRangeDto timeRange = requestDto.getTimeRange();
    //         String begin = timeRange.getBegin();
    //         String end = timeRange.getEnd();
    //         System.out.println("Begin Time: " + begin);
    //         System.out.println("End Time: " + end);
    //
    //         String processKeyword = filterCondition.getProcesskeyword();
    //         String machineNumber = filterCondition.getMachineNumber();
    //         String problemDetail = filterCondition.getProblem_detail();
    //         String maintenanceSuggestion = filterCondition.getMaintenanceSuggestion();
    //         String dataSource = filterCondition.getDataSource();
    //         System.out.println("Process Keyword: " + processKeyword);
    //         System.out.println("Machine Number: " + machineNumber);
    //         System.out.println("Problem Detail: " + problemDetail);
    //         System.out.println("Maintenance Suggestion: " + maintenanceSuggestion);
    //         System.out.println("Data Source: " + dataSource);
    //
    //         // 从 xlsqueryDTO 中获取 orderCondition 并取出其中的数据
    //         orderCondition order = requestDto.getOrder();
    //         String triggerTimeOrder = order.getTriggerTimeOrder();
    //         String processKeywordOrder = order.getProcesskeywordorder();
    //         String machineNumberOrder = order.getMachineNumberorder();
    //         String problemDetailOrder = order.getProblemdetailorder();
    //         String maintenanceSuggestionOrder = order.getMaintenanceSuggestionorder();
    //         String dataSourceOrder = order.getDataSourceorder();
    //         System.out.println("Trigger Time Order: " + triggerTimeOrder);
    //         System.out.println("Process Keyword Order: " + processKeywordOrder);
    //         System.out.println("Machine Number Order: " + machineNumberOrder);
    //         System.out.println("Problem Detail Order: " + problemDetailOrder);
    //         System.out.println("Maintenance Suggestion Order: " + maintenanceSuggestionOrder);
    //         System.out.println("Data Source Order: " + dataSourceOrder);
    //
    //         // 从 xlsqueryDTO 中获取 pageNum 和 pageSize
    //         int pageNum = requestDto.getPageSize();
    //         int pageSize = requestDto.getPageSize();
    //         System.out.println("Page Number: " + pageNum);
    //         System.out.println("Page Size: " + pageSize);
    //
    //
    //         // 进行时间格式转换
    //         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //         LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
    //         LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    //
    //
    //         // 调用 equipmentMaintenanceService 的 buildDynamicQuery 方法进行查询
    //         IPage<EquipmentPictureVO> t1 = equipmentMaintenanceService.buildDynamicQuery(
    //                 startTime, endTime,
    //                 triggerTimeOrder,
    //                 processKeyword, processKeywordOrder,
    //                 machineNumber, machineNumberOrder,
    //                 problemDetail, problemDetailOrder,
    //                 maintenanceSuggestion, maintenanceSuggestionOrder,
    //                 dataSource, dataSourceOrder,
    //                 pageNum, pageSize);

    //
    //         // 最后返回一个响应给客户端
    //         return new Result(200, "查询成功", t1);
    //     }



    //
    // @PostMapping("/handleRequest")
    // public Result handleRequest(@RequestBody RequestDto requestDto) {
    //     // 从 RequestDto 中获取 TimeRangeDto 并取出其中的数据
    //     TimeRangeDto timeRange = requestDto.getTimeRange();
    //     String begin = timeRange.getBegin();
    //     String end = timeRange.getEnd();
    //     System.out.println("Begin Time: " + begin);
    //     System.out.println("End Time: " + end);
    //
    //     // 从 RequestDto 中获取 SearchCriteriaDto 并取出其中的数据
    //     SearchCriteriaDto searchCriteria = requestDto.getSearchCriteria();
    //     String processKeyword = searchCriteria.getProcesskeyword();
    //     String machineNumber = searchCriteria.getMachineNumber();
    //     String problemDetail = searchCriteria.getProblem_detail();
    //     String maintenanceSuggestion = searchCriteria.getMaintenanceSuggestion();
    //     String dataSource = searchCriteria.getDataSource();
    //     System.out.println("Process Keyword: " + processKeyword);
    //     System.out.println("Machine Number: " + machineNumber);
    //     System.out.println("Problem Detail: " + problemDetail);
    //     System.out.println("Maintenance Suggestion: " + maintenanceSuggestion);
    //     System.out.println("Data Source: " + dataSource);
    //
    //     // 从 RequestDto 中获取 OrderDto 并取出其中的数据
    //     OrderDto order = requestDto.getOrder();
    //     String triggerTimeOrder = order.getTriggerTimeOrder();
    //     String processKeywordOrder = order.getProcesskeywordorder();
    //     String machineNumberOrder = order.getMachineNumberorder();
    //     String problemDetailOrder = order.getProblemdetailorder();
    //     String maintenanceSuggestionOrder = order.getMaintenanceSuggestionorder();
    //     String dataSourceOrder = order.getDataSourceorder();
    //     System.out.println("Trigger Time Order: " + triggerTimeOrder);
    //     System.out.println("Process Keyword Order: " + processKeywordOrder);
    //     System.out.println("Machine Number Order: " + machineNumberOrder);
    //     System.out.println("Problem Detail Order: " + problemDetailOrder);
    //     System.out.println("Maintenance Suggestion Order: " + maintenanceSuggestionOrder);
    //     System.out.println("Data Source Order: " + dataSourceOrder);
    //
    //     // 从 RequestDto 中获取 pageNum 和 pageSize
    //     int pageNum = requestDto.getPageNum();
    //     int pageSize = requestDto.getPageSize();
    //     System.out.println("Page Number: " + pageNum);
    //     System.out.println("Page Size: " + pageSize);
    //
    //
    //     // 进行时间格式转换
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //     LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
    //     LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    //
    //
    //     // 调用 equipmentMaintenanceService 的 buildDynamicQuery 方法进行查询
    //     IPage<EquipmentPictureVO> t1 = equipmentMaintenanceService.buildDynamicQuery(
    //             startTime, endTime,
    //             triggerTimeOrder,
    //             processKeyword, processKeywordOrder,
    //             machineNumber, machineNumberOrder,
    //             problemDetail, problemDetailOrder,
    //             maintenanceSuggestion, maintenanceSuggestionOrder,
    //             dataSource, dataSourceOrder,
    //             pageNum, pageSize);
    //
    //
    //     // 最后返回一个响应给客户端
    //     return new Result(200, "查询成功", t1);
    // }













    // @PostMapping("/combinedMapping")
    // @ApiOperation("根据需求以及排序进行查询")
    // public Result combinedMapping(
    //         @RequestParam(required = false, defaultValue = "2024-12-24 00:00:00") String begin,
    //         @RequestParam(required = false, defaultValue = "2024-12-29 00:00:00") String end,
    //         @RequestParam(required = false) String triggerTimeOrder,
    //         @RequestParam(required = false) String processkeyword,
    //         @RequestParam(required = false) String machineNumber,
    //         @RequestParam(required = false) String problem_detail,
    //         @RequestParam(required = false) String maintenanceSuggestion,
    //         @RequestParam(required = false) String dataSource,
    //         @RequestParam(required = false) String processkeywordorder,
    //         @RequestParam(required = false) String machineNumberorder,
    //         @RequestParam(required = false) String problemdetailorder,
    //         @RequestParam(required = false) String maintenanceSuggestionorder,
    //         @RequestParam(required = false) String dataSourceorder,
    //         @RequestParam(defaultValue = "1") int pageNum,
    //         @RequestParam(defaultValue = "10") int pageSize) {
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //     LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
    //     LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    //
    //
    //
    //
    //       IPage<EquipmentPictureVO> t1 =equipmentMaintenanceService.buildDynamicQuery(
    //               startTime,  endTime,
    //               triggerTimeOrder,
    //               processkeyword,  processkeywordorder,
    //               machineNumber, machineNumberorder,
    //               problem_detail,problemdetailorder,
    //               maintenanceSuggestion, maintenanceSuggestionorder,
    //               dataSource ,  dataSourceorder,
    //      pageNum,  pageSize);
    //
    //
    //
    //     return new Result(200, "查询成功", t1);







        // if (processkeyword != null) {
        //     // 当 processkeyword 不为 null 时，调用原第一个方法的逻辑
        //
        //     IPage<EquipmentPictureVO> resultss = equipmentMaintenance.findAllprocess(startTime, endTime, processkeyword, pageNum, pageSize);
        //     return new Result(200, "查询成功", resultss);
        //
        //
        // } else {
        //     // 当 processkeyword 为 null 时，调用原第二个方法的逻辑
        //     IPage<EquipmentPictureVO> results = equipmentMaintenance.findAllsByCollecttimeBetweens(startTime, endTime, pageNum, pageSize);
        //     return new Result(200, "查询成功", results);
        //
        // }


    // }


    //
    // @GetMapping("/findEquipmentAndPictures")
    // @ApiOperation("默认打开页面的查询接口")
    // public Result findEquipmentAndPictures(
    //         @RequestParam(required = false, defaultValue = "2024-12-24 00:00:00") String begin,
    //         @RequestParam(required = false, defaultValue = "2024-12-29 00:00:00") String end,
    //         @RequestParam(defaultValue = "1") int pageNum,
    //         @RequestParam(defaultValue = "10") int pageSize) {
    //
    //
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //         LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
    //         LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    //
    //
    //
    //     // 调用服务层的分页查询方法
    //     IPage<EquipmentPictureVO> result = equipmentMaintenance.findAllsByCollecttimeBetweens(startTime, endTime, pageNum, pageSize);
    //
    //
    //     return new Result(200, "查询成功", result);
    // }

//
//     @GetMapping("/interprocess")
// public Result findEquipmentAndPicturess(
//         @RequestParam  String begin,
//         @RequestParam  String end,
//         @RequestParam String processkeyword,
//         @RequestParam(defaultValue = "1") int pageNum,
//         @RequestParam(defaultValue = "10") int pageSize){
//
//
//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//     LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
//     LocalDateTime endTime = LocalDateTime.parse(end, formatter);
//
//
//
//     // 调用服务层的分页查询方法
//         IPage<EquipmentPictureVO> t1=equipmentMaintenance.findAllprocess(startTime,endTime,processkeyword,pageNum,pageSize);
//
//         System.out.println(t1);
//
//
//         return new Result(200, "查询成功", t1);









    // @RequestMapping("/chart")
    // @ApiOperation("查看图数据")
    // public Result chars(@RequestParam(required = false, defaultValue = "2024-10-24 00:00:00") String begin,
    //                     @RequestParam(required = false, defaultValue = "2024-12-25 00:00:00") String end,
    //                      @RequestParam(required = false) String processkeyword
    // ) {
    //
    //
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //     LocalDateTime startTime = LocalDateTime.parse(begin, formatter);
    //     LocalDateTime endTime = LocalDateTime.parse(end, formatter);
    //
    //
    //     if (processkeyword == null) {
    //   //  如果count为null，即没有传入该参数，则调用不限制数量的查询方法（假设tmlogService中有对应的方法实现）
    //     //如果count为null，即没有传入该参数，则调用不限制数量的查询方法（假设demotestService中有对应的方法实现）
    //     List<EquipmentPictureVO> result = equipmentMaintenanceService.findAllByCollecttimeBetweens(startTime, endTime);
    //     return new Result(200, "查询成功", result);
    //
    //     }
    //       List<EquipmentPictureVO> result =equipmentMaintenanceService.findAllByCollecttimeBetween(startTime,endTime,processkeyword);
    //                 return new Result(200, "查询成功", result);
    //
    //
    //     }




}
