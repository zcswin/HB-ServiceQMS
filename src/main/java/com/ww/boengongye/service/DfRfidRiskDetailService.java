package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRfidRiskDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 风险品信息表 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
public interface DfRfidRiskDetailService extends IService<DfRfidRiskDetail> {
    List<DfRfidRiskDetail> listJoinAppearance(String time,@Param(Constants.WRAPPER) Wrapper<DfRfidRiskDetail> wrapper);

    List<DfRfidRiskDetail> listJoinSize(String time,@Param(Constants.WRAPPER) Wrapper<DfRfidRiskDetail> wrapper);
}
