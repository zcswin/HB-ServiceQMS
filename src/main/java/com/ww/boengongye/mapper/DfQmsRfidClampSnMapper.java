package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotalNew;
import com.ww.boengongye.entity.DfQmsRfidClampSn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2024-06-24
 */
public interface DfQmsRfidClampSnMapper extends BaseMapper<DfQmsRfidClampSn> {

    @Select("SELECT\n" +
            "\tc.*,\n" +
            "\ts.create_time AS sizeTime,\n" +
            "\tw.f_time AS wgTime,\n" +
            "CASE\n" +
            "\t\n" +
            "\tWHEN s.id >= 0 THEN\n" +
            "\t'√' ELSE '' \n" +
            "\tEND AS isSize,\n" +
            "CASE\n" +
            "\t\t\n" +
            "\t\tWHEN w.id >= 0 THEN\n" +
            "\t\t'√' ELSE '' \n" +
            "\tEND AS isWg \n" +
            "FROM\n" +
            "\t`df_qms_rfid_clamp_sn` c\n" +
            "\tLEFT JOIN df_size_detail s ON s.sn = c.bar_code\n" +
            "LEFT JOIN df_qms_ipqc_waig_detail w ON w.f_product_id = c.bar_code " +

                " ${ew.customSqlSegment} ")
    List<DfQmsRfidClampSn> listByExport(@Param(Constants.WRAPPER) Wrapper<DfQmsRfidClampSn> wrapper);

}
