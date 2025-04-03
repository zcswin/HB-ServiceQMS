package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.entity.exportExcel.BatchqueryReport;
import com.ww.boengongye.entity.exportExcel.EmpCapacityReport;
import com.ww.boengongye.entity.exportExcel.OqcReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI玻璃单片信息表 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
public interface DfAoiPieceService extends IService<DfAoiPiece> {
    List<DfAoiPiece> getDfAoiPieceListByBarCode(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    Integer getDefectPieceNumber(@Param(Constants.WRAPPER) Wrapper<Integer> wrapper);
    //优化
    Rate3 getUserInputAndDefectNumber(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    List<DfAoiDefectPoint> getDefectPointList(IPage<DfAoiDefectPoint> page, @Param(Constants.WRAPPER) Wrapper<DfAoiDefectPoint> wrapper);
    //优化
    List<DfAoiDefectPoint> getUserDefectTop5List(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    List<DfAoiEscapePoint> getEscapePointList(IPage<DfAoiEscapePoint> page, @Param(Constants.WRAPPER) Wrapper<DfAoiEscapePoint> wrapper);
    //优化
    List<DfAoiEscapePoint> getEscapePointTop5List(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    List<DfAoiEscape> getAllEscapeList(@Param(Constants.WRAPPER) Wrapper<DfAoiEscape> wrapper);

    List<DfAoiEscapePoint> getEscapeTop5PointList(@Param(Constants.WRAPPER) Wrapper<DfAoiEscapePoint> wrapper);

    List<User> getAllUserOQCNumberList(IPage<User> page,@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    Integer getUserEscapeNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    List<DfAoiOutputPoint> getUserHourOutputPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiOutputPoint> wrapper);

    List<String> getFqcUserDefect7Day(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiEscape> getFqcUserOqcNumberDay(@Param(Constants.WRAPPER)Wrapper<DfAoiEscape> wrapper);

    List<String> getFqcUserDefect4Week(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiEscape> getFqcUserOqcNumberWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiEscape> wrapper);

    DfAoiDetermine getDfAoiDetermineByBarCode(@Param(Constants.WRAPPER)Wrapper<DfAoiDetermine> wrapper);

    List<Rate3> listPieceAndDefectNumGroupByHour(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    List<BatchqueryReport>  getFqcOrOQCDetailByCode(Page<BatchqueryReport> pages,QueryWrapper<DfAoiPiece> qw);

    List<EmpCapacityReport> getEmpCapacityStatement(Page<EmpCapacityReport> page, QueryWrapper<DfAoiPiece> qw, String startTime, String endTime);

    List<OqcReport> getOQCReport(Page<OqcReport> pages,QueryWrapper<DfAoiPiece> qw, QueryWrapper<DfAoiPiece> qw2,String startTime, String endTime, String empCode);

    List<Rate3> lineBodyloss(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime,String clazz);

    List<Map<String, Object>> OQC31Report(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String empCode);

    List<Rate3> lineBodylossByMachine(QueryWrapper<DfAoiPiece> qw2, String startTime, String endTime, String clazz);

    List<Rate3> lossEmpTop10(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String clazz);

    List<Rate3> lineBodylossV2(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String clazz);

	List<Map> fqcInputOut(QueryWrapper<DfAoiPiece> ew);

    List<Map<String, Object>> lossCheck(QueryWrapper<DfAoiPiece> ew);

	List<Map<String, String>> top5Feature(QueryWrapper<DfAoiPiece> ew);

    List<Map<String, Object>> selectByfeature(QueryWrapper<DfAoiPiece> ew2,String feature);

	List<Rate3> getDetailByMachine(QueryWrapper<DfAoiPiece> ew);

    List<Rate3> empLossChemkRank(QueryWrapper<DfAoiPiece> ew);

    List<String> getProjectList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    List<Rate3> getProjectDefectPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    List<Rate3> getItemDefectPointListDay(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                          @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);

    List<Rate3> getItemDefectPointListWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                           @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);

    List<Rate3> getItemDefectPointListMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                           @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);

    List<String> getAllDefectClassList();

    List<String> getAllDefectNameList();

    List<String> getAllDefectAreaList();

    List<String> getAllDefectPosition();

    List<Map<String, Object>> getAoiReportByHour(QueryWrapper<DfAoiPiece> ew);

    List<Map<String, Object>> getAoiReportByDay(QueryWrapper<DfAoiPiece> ew);

	List<Map<String, String>> listOQCReport(QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper,String beforesql,String afterSql);

    List<Rate3> empLossChemkRankV2(QueryWrapper<DfAoiDefect> ew,String feature);

	List<Rate3> empTopFeaRateDesc(QueryWrapper<DfAoiDefect> ew, QueryWrapper<DfAoiDefect> ew2);

    List<Rate3> empLossChemkRankTop10DayNight(QueryWrapper<DfAoiDefect> ew, QueryWrapper<DfAoiDefect> ew2);


    void saveMqData(String datas);

}
