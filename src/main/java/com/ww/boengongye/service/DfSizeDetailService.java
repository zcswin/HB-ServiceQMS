package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.Rate;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 尺寸数据 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
public interface DfSizeDetailService extends IService<DfSizeDetail>, Excelable {
    List<DfSizeDetail> listAllByFactory(String factory, String process, String project, String linebody, String dayOrNight,
                                        String startDate, String endDate);

    DfSizeDetail getOkRate(String factory, String process, String project, String linebody, String dayOrNight,
                           String startDate, String endDate);

    IPage<DfSizeDetail> listJoinIds(IPage<DfSizeDetail> page, @Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    String saveMqData(String content,String type) throws JsonProcessingException, ParseException;

    int importExcel2(MultipartFile file) throws Exception;

    List<DfSizeDetail> listMachineCode(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<DfSizeDetail> getFaiPassRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listDateOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listDateOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    Rate getSizeRealRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listAlwaysOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listAlwaysOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper, String project);

    List<Rate> listProcessNgRateByNgItem(String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listProcessNgRateByNgItem2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listMacNgRateByNgItemAndProcess(String process,String project,String color, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    List<Rate> listAllProcessOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listAllProcessOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper,String project);

    List<Rate> listDateNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate> listItemNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    List<Rate3> listOpenRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper, String process);
    Rate getAllProcessYield(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);
    Rate getLineYield(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

	List<DfSizeDetail> weekOfPoorTOP3Warning(QueryWrapper<DfSizeDetail> ew);

    List<Rate3> twoWeekOfPoorTOP3Warning();

    List<Map<String,Object>> getSizeDetailInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String,Object>> qw);

    List<Rate3> getMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    List<Map<String,Object>> getQmsWaigInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String,Object>> qw);

    List<Rate3> getQmsWaigMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    List<Map<String, Object>> exportInspectionTableForProcess(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);

    List<Map<String, Object>> exportInspectionTableForProcessBymachineId(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);

    List<Map<String, Object>> exportNgClassificationScale(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);
}
