package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfRfidRiskDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 风险品信息表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
public interface DfRfidRiskDetailMapper extends BaseMapper<DfRfidRiskDetail> {
    @Select("SELECT r.*,d.f_result as result2  FROM `df_rfid_risk_detail` r left join df_qms_ipqc_waig_detail d on r.bar_code=d.f_product_id and d.f_time >= #{time} ${ew.customSqlSegment}")
    List<DfRfidRiskDetail> listJoinAppearance(String time,@Param(Constants.WRAPPER) Wrapper<DfRfidRiskDetail> wrapper);

    @Select("SELECT r.*,d.result as result2  FROM `df_rfid_risk_detail` r left join df_size_detail d on r.bar_code=d.sn and d.create_time >= #{time}  ${ew.customSqlSegment}")
    List<DfRfidRiskDetail> listJoinSize(String time,@Param(Constants.WRAPPER) Wrapper<DfRfidRiskDetail> wrapper);
}
