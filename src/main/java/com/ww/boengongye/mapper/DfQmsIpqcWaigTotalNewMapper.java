package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetail;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetailNew;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotalNew;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
public interface DfQmsIpqcWaigTotalNewMapper extends BaseMapper<DfQmsIpqcWaigTotalNew> {
    @Select("select sum(spot_check_count) spot_check_count,sum(affect_count) affect_count " +
            "from df_qms_ipqc_waig_total_new " +
            "${ew.customSqlSegment} ")
    DfQmsIpqcWaigTotalNew getTotalAndNgCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalNew> wrapper);

    //优化
    @Select("WITH temp_main as(\n" +
            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,sum(spot_check_count) spot_check_count,confirmor,f_type,\n" +
            "       sum(spot_check_count - affect_count) as okNum,sum(spot_check_count - affect_count)/sum(spot_check_count) as okRate,\n" +
            "       sum(affect_count) as ngNum,F_TEST_MAN \n" +
            "    from df_qms_ipqc_waig_total_new total\n" +
            "   inner join df_process dp on dp.process_name = total.f_seq " +
            "   ${ew.customSqlSegment} " +
            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,F_TEST_MAN,confirmor,f_type\n" +
            "    having spot_check_count is not null " +
            ")\n" +
            ", temp_slave AS (\n" +
            "    select date_format(date_sub(total.F_TIME,INTERVAL 7 HOUR ),'%Y-%m-%d') date,F_BIGPRO,f_seq,F_FAC,F_COLOR,F_TEST_MAN,F_SORT,count(0) num,confirmor,f_type\n" +
            "    from df_qms_ipqc_waig_total_new total\n" +
            "   inner join df_process dp on dp.process_name = total.f_seq " +
            "    left join df_qms_ipqc_waig_detail_new detail ON total.ID = detail.F_PARENT_ID\n" +
            "   ${ew.customSqlSegment} " +
            "    GROUP BY date,total.F_BIGPRO,total.f_seq,total.F_FAC,total.F_COLOR,F_TEST_MAN,F_SORT,confirmor,f_type\n" +
            "    having F_SORT is not null" +
            ")\n" +
            "select main.confirmor,main.f_type,main.date,main.F_BIGPRO,main.f_seq,main.F_FAC,main.F_COLOR,spot_check_count,okNum,okRate,ngNum,main.F_TEST_MAN,F_SORT,num from(\n" +
            "    temp_main main\n" +
            "    JOIN temp_slave slave\n" +
            "    ON main.date = slave.date\n" +
            "    AND main.F_BIGPRO = slave.F_BIGPRO\n" +
            "    AND main.f_seq = slave.f_seq\n" +
            "    AND main.F_FAC = slave.F_FAC\n" +
            "    AND main.F_COLOR = slave.F_COLOR\n" +
            "    AND main.F_TEST_MAN = slave.F_TEST_MAN\n" +
//            "    AND main.confirmor = slave.confirmor\n" +
            "    AND main.f_type = slave.f_type\n" +
            ")\n" +
            "ORDER BY date DESC;")
    List<DfQmsIpqcWaigTotalNew> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalNew> ew);
}
