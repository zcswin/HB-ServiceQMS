package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
public interface DfQmsIpqcWaigTotalCheckService extends IService<DfQmsIpqcWaigTotalCheck> {
    List<Rate> listCNC0OkRateOneMonth(QueryWrapper<DfQmsIpqcWaigTotalCheck> qw);


    List<DfQmsIpqcWaigTotalCheck> listOkRate(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("startHour") int startHour,
            @Param("endHour") int endHour,
            @Param("projectName") String projectName,
            @Param("process") String process);


    List<Rate> listNgTop5Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);
    List<Rate> listNgTop10Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour,String fmac);



    int Count(QueryWrapper<DfQmsIpqcWaigTotalCheck> qw);

    List<Rate> listNgItemRate(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgItemRateSortByNgTop(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgItemRateSortByNgTop2(String lineBody, String project, String process, String startTime, String endTime);  // 根据NGTop排序

    List<Rate> listOkRateNear15Day(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<Rate> listNgTop10(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgTop102(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgTop103(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> WaiguanlistNgTop10(String lineBody, String project, String floor, String process, String fsort,String startTime, String endTime, Integer startHour, Integer endHour);


    List<Rate> listAlwaysOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<DfQmsIpqcWaigTotalCheck> listMachineCode(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<DfQmsIpqcWaigTotalCheck> getFaiPassRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<Rate> listAllProcessItemNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<ProcessResRate> listProcessAlwaysNumRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<Rate> listAllProcessOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper, String floor,String project);

    Rate getAppearRealOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<Rate> listProcessNgRateByNgItem(String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listMacNgRateByNgItemAndProcess(String process, String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listAllProcessItemNgRateTop5(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<Rate> listAllProcessOkRate2(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    DfQmsIpqcWaigTotalCheck getProcessYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);


    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String process, String startTime, String endTime);
    //优化（根据楼层获取工序）
    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String floor, String process, String startTime, String endTime);  // 根据NGTop排序


    List<Rate> listDateNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper); // 机台每日良率
    DfQmsIpqcWaigTotalCheck getTotalAndNgCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<DfSizeCheckItemInfos> detailPageApparenceYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate> listAllProcessItemNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate2> listAllProcessItemNgRateTop5V3(String startTime, String endTime);

    List<Rate3> listProcessOkRateOneMonth(String factoryId , String project , String startTime, String endTime, String process);

    Rate getAllYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    Rate getLineYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalCheck> wrapper);

    List<DfQmsIpqcWaigTotalCheck> weekOfPoorTOP3Warning(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);

    List<Rate3> twoWeekOfPoorTOP3Warning();

  //  List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew);
    List<DfQmsIpqcWaigTotalCheck> listWaigExcelDataPage(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew,
                                                 @Param("pageOffset") int pageOffset,
                                                 @Param("pageSize") int pageSize,
                                                 @Param("floor") String floor,
                                                 @Param("project") String project);

    List<Map<String, Object>> exportInspectionTableForProcess(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);

    List<Map<String, Object>> exportInspectionTableForProcessBymachineId(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);

    List<Map<String, Object>> exportNgClassificationScale(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper);

    List<Rate3> getMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    List<Map<String, Object>> getWGDetailInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String, Object>> qw);

}
