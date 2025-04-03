package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
public interface DfQmsIpqcWaigTotalService extends IService<DfQmsIpqcWaigTotal>, Excelable {
    List<Rate> listCNC0OkRateOneMonth(QueryWrapper<DfQmsIpqcWaigTotal> qw);

    int Count(QueryWrapper<DfQmsIpqcWaigTotal> qw);

    List<DfQmsIpqcWaigTotal> listWaigExcelDataPage(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotal> ew,
                                                          @Param("pageOffset") int pageOffset,
                                                          @Param("pageSize") int pageSize,
                                                          @Param("floor") String floor,
                                                   @Param("project") String project);



    List<DfQmsIpqcWaigTotal> getOptimizedDataWithCustomExecutor(String startDate, String endDate, String startHour, String endHour, String processName, String projectName);

    List<Rate> listNgItemRate(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgItemRateSortByNgTop(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgItemRateSortByNgTop2(String lineBody, String project, String process, String startTime, String endTime);  // 根据NGTop排序

    List<Rate> listOkRateNear15Day(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);


    List<DfQmsIpqcWaigTotal> listOkRate(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("startHour") int startHour,
            @Param("endHour") int endHour,
            @Param("projectName") String projectName,
            @Param("process") String process,
            @Param("period")String period);


    List<Rate> listNgTop10(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgTop102(String lineBody, String project, String process, String startTime, String endTime);

    List<Rate> listNgTop103(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);


    List<Rate> listNgTop104(String lineBody, String project, String floor, List<String> process, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listNgTop5Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour);
    List<Rate> listNgTop10Fmac(String lineBody, String project, String floor, String process, String startTime, String endTime, Integer startHour, Integer endHour,String fmac);


    List<Rate> listAlwaysOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper,String project);

    List<DfQmsIpqcWaigTotal> listMachineCode(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<DfQmsIpqcWaigTotal> getFaiPassRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<Rate> listAllProcessItemNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<ProcessResRate> listProcessAlwaysNumRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<Rate> listAllProcessOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper, String floor,String project);

    Rate getAppearRealOkRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<Rate> listProcessNgRateByNgItem(String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listMacNgRateByNgItemAndProcess(String process, String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listAllProcessItemNgRateTop5(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<Rate> listAllProcessOkRate2(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    DfQmsIpqcWaigTotal getProcessYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);


    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String process, String startTime, String endTime);
    //优化（根据楼层获取工序）
    List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String floor, String process, String startTime, String endTime);  // 根据NGTop排序


    List<Rate> listDateNgRate(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper); // 机台每日良率
    DfQmsIpqcWaigTotal getTotalAndNgCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    List<DfSizeCheckItemInfos> detailPageApparenceYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

	List<Rate> listAllProcessItemNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime);

    List<Rate2> listAllProcessItemNgRateTop5V3(String startTime, String endTime);

    List<Rate3> listProcessOkRateOneMonth(String factoryId , String project , String startTime, String endTime, String process);

    Rate getAllYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    Rate getLineYield(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

	List<DfQmsIpqcWaigTotal> weekOfPoorTOP3Warning(QueryWrapper<DfQmsIpqcWaigTotal> ew);

    List<Rate3> twoWeekOfPoorTOP3Warning();

	List<DfQmsIpqcWaigTotal> listWaigExcelData(QueryWrapper<DfQmsIpqcWaigTotal> ew);

	List<Map<String, Object>> exportInspectionTableForProcess(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper);

    List<Map<String, Object>> exportInspectionTableForProcessBymachineId(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper);

    List<Map<String, Object>> exportNgClassificationScale(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper);

    List<Rate3> getMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    List<Map<String, Object>> getWGDetailInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String, Object>> qw);

     String DynamicIpqcBoSongFenBu(DfQmsIpqcWaigTotal datas);
}
