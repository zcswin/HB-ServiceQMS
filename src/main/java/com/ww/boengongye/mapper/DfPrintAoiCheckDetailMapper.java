package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfPrintAoiCheck;
import com.ww.boengongye.entity.DfPrintAoiCheckDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 移印AOI检测明细 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
public interface DfPrintAoiCheckDetailMapper extends BaseMapper<DfPrintAoiCheckDetail> {

//    @Select("select \n" +
//            "dpac_new.lineBody\n" +
//            ",dpac_new.onePassPoint\n" +
//            ",dpac_new.dimensionPassPoint\n" +
//            ",dpac_new.cosmeticPassPoint\n" +
//            ",dpac_new.onePassPoint+dpar_new2.overkillPoint-dpar_new2.escapePoint finalPassPoint\n" +
//            "from \n" +
//            "\t(select \n" +
//            "\tdpac.line_body lineBody,\n" +
//            "\tsum(if(dpac.bin like '%NG%',1,0)) ngNumber,\n" +
//            "\tsum(if(dpac.bin in ('NG2','NG6','NG7'),1,0)) dimensiom,\n" +
//            "\tsum(if(dpac.bin in ('NG1','NG2','NG5','NG6'),1,0)) cosmetic,\n" +
//            "\tcount(0) total,\n" +
//            "\tformat(sum(if(dpac.bin like '%NG%',1,0))/count(0)*100,2) onePassPoint, \n" +
//            "\tformat(sum(if(dpac.bin in ('NG2','NG6','NG7'),1,0))/count(0)*100,2) dimensionPassPoint, \n" +
//            "\tformat(sum(if(dpac.bin in ('NG1','NG2','NG5','NG6'),1,0))/count(0)*100,2) cosmeticPassPoint \n" +
//            "\tfrom df_print_aoi_check dpac \n" +
//            "${ew.customSqlSegment} " +
//            "\tgroup by dpac.line_body\n" +
//            "\t)dpac_new \n" +
//            "left join \n" +
//            "\t(select \n" +
//            "\tdpar_new.line_body lineBody\n" +
//            "\t,sum(if(dpar_new.type2 = 'Overkill过杀',dpar_new.overkillOrEscape,'0.00')) overkillPoint\n" +
//            "\t,sum(if(dpar_new.type2 = 'Escape漏检',dpar_new.overkillOrEscape,'0.00')) escapePoint\n" +
//            "\tfrom \n" +
//            "\t\t(select dpar.line_body,dpar.type2\n" +
//            "\t\t,format(avg(dpar.overkill_or_escape)*100,2) overkillOrEscape\n" +
//            "\t\tfrom df_print_aoi_recheck dpar \n" +
//            "${ew_2.customSqlSegment} " +
//            "\t\tgroup by dpar.line_body,dpar.type2 \n" +
//            "\t\t)dpar_new\n" +
//            "\tgroup by dpar_new.line_body\n" +
//            "\t)dpar_new2\n" +
//            "on dpar_new2.lineBody = dpac_new.lineBody")
//    List<DfPrintAoiCheck> getPassPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheck> wrapper2);

    //修改
    @Select("select \n" +
            "dpac_new.lineBody\n" +
            ",dpac_new.onePassPoint\n" +
            ",dpac_new.dimensionPassPoint\n" +
            ",dpac_new.cosmeticPassPoint\n" +
            ",dpac_new.onePassPoint+if(dpar_new2.overkillPoint is null,0,dpar_new2.overkillPoint)-if(dpar_new2.escapePoint is null,0,dpar_new2.overkillPoint) finalPassPoint\n" +
            "from \n" +
            "\t(select \n" +
            "\tdpac.line_body lineBody,\n" +
            "\tsum(if(dpac.bin like '%NG%',1,0)) ngNumber,\n" +
            "\tsum(if(dpac.bin in ('NG2','NG6','NG7'),1,0)) dimensiom,\n" +
            "\tsum(if(dpac.bin in ('NG1','NG2','NG5','NG6'),1,0)) cosmetic,\n" +
            "\tcount(0) total,\n" +
            "\tformat(sum(if(dpac.bin like '%NG%',1,0))/count(0)*100,2) onePassPoint, \n" +
            "\tformat(sum(if(dpac.bin in ('NG2','NG6','NG7'),1,0))/count(0)*100,2) dimensionPassPoint, \n" +
            "\tformat(sum(if(dpac.bin in ('NG1','NG2','NG5','NG6'),1,0))/count(0)*100,2) cosmeticPassPoint \n" +
            "\tfrom df_print_aoi_check dpac \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dpac.line_body\n" +
            "\t)dpac_new \n" +
            "left join \n" +
            "\t(select \n" +
            "\tdpar_new.line_body lineBody\n" +
            "\t,sum(if(dpar_new.type2 = 'Overkill过杀',dpar_new.overkillOrEscape,'0.00')) overkillPoint\n" +
            "\t,sum(if(dpar_new.type2 = 'Escape漏检',dpar_new.overkillOrEscape,'0.00')) escapePoint\n" +
            "\tfrom \n" +
            "\t\t(select dpar.line_body,dpar.type2\n" +
            "\t\t,format(avg(dpar.overkill_or_escape)*100,2) overkillOrEscape\n" +
            "\t\tfrom df_print_aoi_recheck dpar \n" +
            "${ew_2.customSqlSegment} " +
            "\t\tgroup by dpar.line_body,dpar.type2 \n" +
            "\t\t)dpar_new\n" +
            "\tgroup by dpar_new.line_body\n" +
            "\t)dpar_new2\n" +
            "on dpar_new2.lineBody = dpac_new.lineBody")
    List<DfPrintAoiCheck> getPassPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheck> wrapper2);


    @Select("with dpacd_new as(\n" +
            "\tselect \n" +
            "\tdpacd.check_id,substring_index(dpacd.check_name,'_',-1) hole\n" +
            "\t,sum(if(dpacd.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_print_aoi_check_detail dpacd \n" +
            "where dpacd.check_type = 1 " +
            "and substring_index(dpacd.check_name,'_',-1) in ('ML','S','MIC','MW') " +
            "\tgroup by dpacd.check_id,substring_index(dpacd.check_name,'_',-1)\n" +
            ")\n" +
            "select \n" +
            "dpacd_new.hole str1\n" +
            ",sum(if(dpacd_new.ngNumber>0,1,0)) inte1\n" +
            ",count(0)-sum(if(dpacd_new.ngNumber>0,1,0)) inte2\n" +
            ",count(0) inte3\n" +
            ",format((count(0)-sum(if(dpacd_new.ngNumber>0,1,0)))/count(0)*100,2) dou1\n" +
            "from dpacd_new \n" +
            "left join df_print_aoi_check dpac \n" +
            "on dpacd_new.check_id = dpac.id\n" +
            "${ew.customSqlSegment} " +
            "group by dpacd_new.hole")
    List<Rate3> getHolePossPoint(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper);


    @Select("with dpacd_new as(\n" +
            "\tselect \n" +
            "\tdpacd.check_id,dpacd.item_name \n" +
            "\t,sum(if(dpacd.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_print_aoi_check_detail dpacd \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dpacd.check_id,dpacd.item_name \n" +
            ")\n" +
            "select \n" +
            "dpacd_new.item_name str1\n" +
            ",sum(dpacd_new.ngNumber) inte1\n" +
            ",count(0) inte2\n" +
            ",format(sum(dpacd_new.ngNumber)/count(0)*100,2) dou1   \n" +
            "from df_print_aoi_check dpac \n" +
            "left join dpacd_new\n" +
            "on dpacd_new.check_id = dpac.id\n" +
            "${ew.customSqlSegment} " +
            "group by dpacd_new.item_name\n" +
            "order by dou1 desc \n" +
            "limit 3")
    List<Rate3> getHoleDefectPointTop3(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);
    //优化
    @Select("with dpacd_new as(\n" +
            "\tselect \n" +
            "\tsubstring_index(dpacd.check_name,'_',-1) hole,\n" +
            "\tdpacd.check_id,dpacd.item_name \n" +
            "\t,sum(if(dpacd.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_print_aoi_check_detail dpacd \n" +
            "\twhere 1 = 1\n" +
            "\tand dpacd.check_type = 1\n" +
            "\tand substring_index(dpacd.check_name,'_',-1) in ('ML','S','MIC','MW')\n" +
            "\tgroup by dpacd.check_id,dpacd.item_name,substring_index(dpacd.check_name,'_',-1) \n" +
            ")\n" +
            ",hole_defectName as(\n" +
            "\tselect \n" +
            "\tdpacd_new.hole\n" +
            "\t,dpacd_new.item_name defectName\n" +
            "\t,sum(dpacd_new.ngNumber) ngNumber\n" +
            "\t,count(0) total\n" +
            "\t,format(sum(dpacd_new.ngNumber)/count(0)*100,2) defectPoint \n" +
            "\tfrom dpacd_new  \n" +
            "\tleft join df_print_aoi_check dpac\n" +
            "\ton dpacd_new.check_id = dpac.id\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dpacd_new.item_name,dpacd_new.hole\n" +
            ")\n" +
            "select " +
            "defect_new.hole str1\n" +
            ",defect_new.defectName str2\n" +
            ",defect_new.ngNumber inte1\n" +
            ",defect_new.total inte2\n" +
            ",defect_new.defectPoint dou2\n" +
            "from \n" +
            "\t(select hole_defectName.*\n" +
            "\t,row_number()over(partition by hole_defectName.hole order by hole_defectName.defectPoint desc) num \n" +
            "\tfrom hole_defectName\n" +
            "\t)defect_new\n" +
            "where defect_new.num<=3")
    List<Rate3> getAllHoleDefectPointTop3(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper);


    @Select("with dpacd_new as(\n" +
            "\tselect dpacd.check_id\n" +
            "\t,sum(if(dpacd.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_print_aoi_check_detail dpacd \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dpacd.check_id\n" +
            ")\n" +
            "select \n" +
            "sum(if(dpacd_new.ngNumber>0,1,0)) inte1\n" +
            ",count(0)-sum(if(dpacd_new.ngNumber>0,1,0)) inte2\n" +
            ",count(0) inte3\n" +
            ",format(sum(if(dpacd_new.ngNumber>0,1,0))/count(0)*100,2) dou1  \n" +
            ",format((count(0)-sum(if(dpacd_new.ngNumber>0,1,0)))/count(0)*100,2) dou2 " +
            "from df_print_aoi_check dpac \n" +
            "left join dpacd_new\n" +
            "on dpacd_new.check_id = dpac.id\n" +
            "${ew.customSqlSegment} " +
            "having inte3 !=0")
    List<Rate3> getNgPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);

    @Select("with dpacd_new as(\n" +
            "\tselect dpacd.check_id\n" +
            "\t,sum(if(dpacd.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_print_aoi_check_detail dpacd \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dpacd.check_id\n" +
            ")\n" +
            "select \n" +
            "dpac.line_body str1\n" +
            ",sum(if(dpacd_new.ngNumber>0,1,0)) inte1\n" +
            ",count(0)-sum(if(dpacd_new.ngNumber>0,1,0)) inte2\n" +
            ",count(0) inte3\n" +
            ",format(sum(if(dpacd_new.ngNumber>0,1,0))/count(0)*100,2) dou1  \n" +
            ",format((count(0)-sum(if(dpacd_new.ngNumber>0,1,0)))/count(0)*100,2) dou2 " +
            "from df_print_aoi_check dpac \n" +
            "left join dpacd_new\n" +
            "on dpacd_new.check_id = dpac.id\n" +
            "${ew.customSqlSegment} " +
            "group by dpac.line_body ")
    List<Rate3> getOnePassPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);


    @Select("select substring_index(dpacd.item_name,'溢',1) hole  \n" +
            "from df_print_aoi_check_detail dpacd \n" +
            "where dpacd.check_type =2\n" +
            "and dpacd.item_name like '%溢墨宽度%'\n" +
            "group by substring_index(dpacd.item_name,'溢',1)")
    List<String> getAllHoleList();

    @Select("select dpacd.*\n" +
            "from df_print_aoi_check_detail dpacd \n" +
            "left join df_print_aoi_check dpac \n" +
            "on dpac.id = dpacd.check_id " +
            "${ew.customSqlSegment} ")
    List<DfPrintAoiCheckDetail> listItemInfosJoinDetail(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheckDetail> wrapper);

    @Select("select min(dpacd.lsl) dou1,max(dpacd.usl) dou2  \n" +
            "from df_print_aoi_check_detail dpacd \n" +
            "left join df_print_aoi_check dpac \n" +
            "on dpac.id = dpacd.check_id " +
            "${ew.customSqlSegment} ")
    Rate3 getMinLslAndMaxUsl(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheckDetail> wrapper);
}
