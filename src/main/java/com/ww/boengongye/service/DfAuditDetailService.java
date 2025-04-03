package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 稽核NG详细 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-22
 */
public interface DfAuditDetailService extends IService<DfAuditDetail> {

    int importExcel(MultipartFile file) throws Exception;

    int importExcel2(MultipartFile file) throws Exception;

    List<DfAuditDetail> listByBigScreen(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    DfAuditDetail getEndNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getProjectClassNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getQuestionNum(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getQuestionNumJc(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);
    List<DfAuditDetail>  getProcessTimeoutData (@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getTimeoutData(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getAuditSummaryData(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getAuditLevelSummary(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);



    DfAuditDetail getTimeout(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    int getNgCountByMacCode(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> listByCheckOverTime( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    DfQmsIpqcWaigTotal getAppearSnCodeById(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    DfSizeDetail getSizeSnCodeById(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getEndNumByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> getTimeoutByDay(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);

	List<Map> exportAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper);

    List<DfAuditDetail> exportExcleAuditNgDetail(@Param(Constants.WRAPPER) QueryWrapper<DfAuditDetail> wrapper);

    DfAuditDetail getTimeout2(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> wrapper);
    List<DfAuditDetail> listByProcess(@Param(Constants.WRAPPER) Wrapper<DfAuditDetail> qw);
    List<DfAuditDetail> listByProcessHaveQuestionType( @Param(Constants.WRAPPER) Wrapper<DfAuditDetail> qw);
}
