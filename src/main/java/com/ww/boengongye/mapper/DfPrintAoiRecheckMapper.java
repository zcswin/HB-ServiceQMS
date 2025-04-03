package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfPrintAoiRecheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfPrintAoiRecheckDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 移印AOI人工复查 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
public interface DfPrintAoiRecheckMapper extends BaseMapper<DfPrintAoiRecheck> {

    @Select("select dpar.line_body\n" +
            ",format(avg(dpar.overkill_or_escape)*100,2) overkillOrEscape\n" +
            "from df_print_aoi_recheck dpar \n" +
            "${ew.customSqlSegment} "+
            "group by dpar.line_body,dpar.type2 ")
    List<DfPrintAoiRecheck> getOverkillOrEscapeList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiRecheck> wrapper);


    @Select("select dpar.line_body,dpard.defect_name defectName,sum(dpard.defect_number) defectNumber\n" +
            "from df_print_aoi_recheck_detail dpard \n" +
            "left join df_print_aoi_recheck dpar \n" +
            "on dpar.id = dpard.check_id \n" +
            "${ew.customSqlSegment} "+
            "group by dpar.line_body,dpard.defect_name")
    List<DfPrintAoiRecheckDetail> getEscapeDetailList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiRecheckDetail> wrapper);

}
