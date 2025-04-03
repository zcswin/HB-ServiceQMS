package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiObaCompare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * OBA工厂比较 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-04
 */
public interface DfAoiObaCompareMapper extends BaseMapper<DfAoiObaCompare> {

    @Select("select daoc.factory\n" +
            "from df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "group by daoc.factory \n" +
            "order by daoc.factory asc")
    List<String> getObaFactoryName(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select month(c.datelist) `time`\n" +
            "from calendar c \n" +
            "${ew.customSqlSegment} " +
            "group by month(c.datelist) \n" +
            "order by month(c.datelist) asc ;")
    List<String> getTimeMonth(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select week(c.datelist) `time`\n" +
            "from calendar c \n" +
            "${ew.customSqlSegment} " +
            "group by week(c.datelist) \n" +
            "order by week(c.datelist) asc")
    List<String> getTimeWeek(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select day(c.datelist) `time`\n" +
            "from calendar c \n" +
            "${ew.customSqlSegment} " +
            "group by day(c.datelist) \n" +
            "order by day(c.datelist) asc")
    List<String> getTimeDay(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.NG,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select month(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by month(c.datelist) \n" +
            "\torder by month(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select month(daoc.check_time) checkTime,sum(daoc.check_number) checkNumber,sum(daoc.NG) NG\n" +
            ",FORMAT((sum(daoc.check_number)-sum(daoc.NG))/sum(daoc.check_number)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by month(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.NG,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select week(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by week(c.datelist) \n" +
            "\torder by week(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select week(daoc.check_time) checkTime,sum(daoc.check_number) checkNumber,sum(daoc.NG) NG\n" +
            ",FORMAT((sum(daoc.check_number)-sum(daoc.NG))/sum(daoc.check_number)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by week(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.NG,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select day(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by day(c.datelist) \n" +
            "\torder by day(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select day(daoc.check_time) checkTime,sum(daoc.check_number) checkNumber,sum(daoc.NG) NG\n" +
            ",FORMAT((sum(daoc.check_number)-sum(daoc.NG))/sum(daoc.check_number)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by day(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);


    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.passNumber,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select month(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by month(c.datelist) \n" +
            "\torder by month(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select month(daoc.check_time) checkTime,count(0) checkNumber,sum((if(daoc.check_result='Pass',1,0))) passNumber\n" +
            ",FORMAT(sum((if(daoc.check_result='Pass',1,0)))/count(0)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by month(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaBatchPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.passNumber,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select week(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by week(c.datelist) \n" +
            "\torder by week(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select week(daoc.check_time) checkTime,count(0) checkNumber,sum((if(daoc.check_result='Pass',1,0))) passNumber\n" +
            ",FORMAT(sum((if(daoc.check_result='Pass',1,0)))/count(0)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by week(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaBatchPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,daoc_new.checkNumber,daoc_new.passNumber,daoc_new.passPoint\n" +
            "from \n" +
            "\t(select day(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by day(c.datelist) \n" +
            "\torder by day(c.datelist) \n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select day(daoc.check_time) checkTime,count(0) checkNumber,sum((if(daoc.check_result='Pass',1,0))) passNumber\n" +
            ",FORMAT(sum((if(daoc.check_result='Pass',1,0)))/count(0)*100,2) passPoint " +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by day(daoc.check_time)\n" +
            "\t)daoc_new\n" +
            "on daoc_new.checkTime = time_table.checkTime")
    List<DfAoiObaCompare> getObaBatchPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select daoc.factory,sum(daoc.check_number) checkNumber,sum(daoc.NG) NG,FORMAT(sum(daoc.NG)/sum(daoc.check_number)*100,2) defectPoint \n" +
            "from df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "group by daoc.factory \n" +
            "order by daoc.factory asc")
    List<DfAoiObaCompare> getObaDefectPoint(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);

    @Select("select defect_new.defect_name\n" +
            "from \n" +
            "\t(select daoc.factory,daod.defect_name,sum(daod.defect_number)\n" +
            "\t,row_number()over(partition by daoc.factory order by sum(daod.defect_number) desc) num\n" +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "\tleft join df_aoi_oba_defect daod \n" +
            "\ton daoc.batch = daod.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by daoc.factory,daod.defect_name\n" +
            "\t)defect_new\n" +
            "where defect_new.num<=5\n" +
            "group by defect_new.defect_name")
    List<String> getObaDefectNameList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


    @Select("select defect_new.factory,defect_new.defectName,defect_new.NG,factory_new.checkNumber\n" +
            ",FORMAT(defect_new.NG/factory_new.checkNumber*100,2) defectPoint\n" +
            "from \n" +
            "\t(select daoc.factory,sum(daoc.check_number) checkNumber\n" +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by daoc.factory\n" +
            "\torder by daoc.factory\n" +
            "\t)factory_new\n" +
            "left join \n" +
            "\t(select daoc.factory,daod.defect_name defectName,sum(daod.defect_number) NG\n" +
            "\t,row_number()over(partition by daoc.factory order by sum(daod.defect_number) desc) num\n" +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "\tleft join df_aoi_oba_defect daod \n" +
            "\ton daoc.batch = daod.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by daoc.factory,daod.defect_name\n" +
            "\t)defect_new\n" +
            "on defect_new.factory = factory_new.factory ")
    List<DfAoiObaCompare> getObaOneDefectPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<DfAoiObaCompare> wrapper2);


    @Select("select daoc.`type`\n" +
            "from df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "group by daoc.`type` \n" +
            "order by daoc.`type` asc")
    List<String> getObaTypeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


    @Select("select defect_new.`type`,defect_new.defectName,defect_new.NG,type_new.checkNumber\n" +
            ",FORMAT(defect_new.NG/type_new.checkNumber*100,2) defectPoint\n" +
            "from \n" +
            "\t(select daoc.`type`,daod.defect_name defectName,sum(daod.defect_number) NG\n" +
            "\t,row_number()over(partition by daoc.`type` order by sum(daod.defect_number) desc) num\n" +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "\tleft join df_aoi_oba_defect daod \n" +
            "\ton daod.batch = daoc.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by daoc.`type`,daod.defect_name\n" +
            "\t)defect_new\n" +
            "left join \n" +
            "\t(select daoc.`type`,sum(daoc.check_number) checkNumber \n" +
            "\tfrom df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by daoc.`type` \n" +
            "\torder by daoc.`type` asc\n" +
            "\t)type_new\n" +
            "on type_new.`type` = defect_new.`type`\n" +
            "where defect_new.num<=5")
    List<DfAoiObaCompare> getObaDefectPointTop5List(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);


    @Select("select daoc.`type`,sum(if(daoc.check_result='Pass',1,0)),sum(daoc.NG) NG,sum(daoc.check_number) checkNumber\n" +
            ",FORMAT(sum(if(daoc.check_result='Pass',1,0))/sum(daoc.check_number)*100,2) batchPassPoint\n" +
            ",FORMAT((sum(daoc.check_number)-sum(daoc.NG))/sum(daoc.check_number)*100,2) passPoint\n" +
            "from df_aoi_oba_compare daoc \n" +
            "${ew.customSqlSegment} " +
            "group by daoc.`type` \n" +
            "order by daoc.`type` asc")
    List<DfAoiObaCompare> getObaPassPointAndBatchList(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);
}
