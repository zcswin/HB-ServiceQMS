package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
public interface DfSizeCheckItemInfosService extends IService<DfSizeCheckItemInfos> {
    List<DfSizeCheckItemInfos> listByFactory(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    DfSizeCheckItemInfos getOKRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoinDetailLimit50(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour);

    List<DfSizeCheckItemInfos> listJoinDetailLimit50OkData(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listSizeNgRate(String process, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listSizeNgRate3(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);


    List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachine(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachineSortByNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50OkData(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listItemCheckValueLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoinDetailAndProcess(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoinDetailLimit50_2();

    List<Rate> listItemOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<Rate> listItemOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoin(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcess(String process,String project,String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit);
    List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcessAndItem(String process,String project,String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit,String itmeName,String machineCode); // 用于获取 单工序 正态分布数据  加上分页
    List<DfSizeCheckItemInfos> listJoinDetailByTime(String process, String project,String color,String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit);  // 用于获取 全工序 正态分布数据

    List<Rate> listAllProcessSizeNgRateTop5(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);  // 各工序不良项比较TOP5

    List<Rate> listAllProcessOKRate(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper); // 各工序的良率

    List<Rate> listSizeNgRate2(String process, String startTime, String endTime);  // 利用中间表查询尺寸NG TOP

    List<Rate> listProcessNgRateByItem(String project,String color,String startTime, String endTime, String checkResult, String itemName); // 获取该不良项不同工序的占比

    List<DfSizeCheckItemInfos> listCheckItemValueLimit50(@Param(Constants.WRAPPER) Wrapper<DfSizeCheckItemInfos> wrapper);

    List<DfSizeCheckItemInfos> listTotalprocessyieldtrendchart(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<DfSizeCheckItemInfos> fullSizeNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<DfSizeCheckItemInfos> listSingleprocessyieldtrendchart(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<DfSizeCheckItemInfos> inputOutputQuantity(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<DfSizeCheckItemInfos> detailPageSizeYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate> listAllProcessSizeNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate2> listAllProcessSizeNgRateTop5V3(String startTime, String endTime);

	List<DfSizeCheckItemInfos> machineSizeAccuracyControlChart(QueryWrapper<DfSizeCheckItemInfos> ew);

    List<DfSizeCheckItemInfos> machineSizeControlChart(QueryWrapper<DfSizeCheckItemInfos> ew);

    ImportExcelResult importOrder(MultipartFile file,String project ,String process,String color)throws Exception;


    List<DfSizeCheckItemInfos>tzInfo(QueryWrapper<DfSizeCheckItemInfos> ew);

    List<DfSizeCheckItemInfos>tzInfoByProcess(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);

    List<DfSizeCheckItemInfos>tzInfoCpkByProcess(@Param("ew") QueryWrapper<DfSizeCheckItemInfos> ew);

    List<Map<String, Object>> getNormalDistributionData(String process, String project, String color,String startTime, String endTime,
                                                        Integer startHour, Integer endHour, Integer startLine, Integer limit);
//
//    List<Map<String,Object>> getDetailData(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);
//
//    List<DfSizeCheckItemInfos> getSizeCheckItemInfosList(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);
}
