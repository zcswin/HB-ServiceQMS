package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * AOI SI工厂对比 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
public interface DfAoiSiCompareMapper extends BaseMapper<DfAoiSiCompare> {

    @Select("select dasc.factory\n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.factory \n" +
            "order by dasc.factory asc")
    List<String> getSiFactoryName(@Param(Constants.WRAPPER) Wrapper<String> wrapper);

    @Select("select dasc.sell_place \n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.sell_place\n" +
            "order by dasc.sell_place asc")
    List<String> getSiSellPlace(@Param(Constants.WRAPPER) Wrapper<String> wrapper);

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


    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select month(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by month(c.datelist) \n" +
            "\torder by month(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select month(dasc.check_time) checkTime,sum(dasc.check_number) checkNumber,sum(dasc.ok_number) okNumber\n" +
            "\t,FORMAT(sum(dasc.ok_number)/sum(dasc.check_number)*100,2) passPoint\n" +
            "\tfrom df_aoi_si_compare dasc\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by month(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper, @Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select week(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by week(c.datelist) \n" +
            "\torder by week(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select week(dasc.check_time) checkTime,sum(dasc.check_number) checkNumber,sum(dasc.ok_number) okNumber\n" +
            "\t,FORMAT(sum(dasc.ok_number)/sum(dasc.check_number)*100,2) passPoint\n" +
            "\tfrom df_aoi_si_compare dasc\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by week(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select day(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by day(c.datelist) \n" +
            "\torder by day(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select day(dasc.check_time) checkTime,sum(dasc.check_number) checkNumber,sum(dasc.ok_number) okNumber\n" +
            "\t,FORMAT(sum(dasc.ok_number)/sum(dasc.check_number)*100,2) passPoint\n" +
            "\tfrom df_aoi_si_compare dasc\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by day(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);


    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select month(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by month(c.datelist) \n" +
            "\torder by month(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select month(dasc.check_time) checkTime,count(0) checkNumber,sum((if(dasc.determine='Pass',1,0))) okNumber\n" +
            "\t,FORMAT(sum((if(dasc.determine='Pass',1,0)))/count(0)*100,2) passPoint \n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by month(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiBatchPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select week(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by week(c.datelist) \n" +
            "\torder by week(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select week(dasc.check_time) checkTime,count(0) checkNumber,sum((if(dasc.determine='Pass',1,0))) okNumber\n" +
            "\t,FORMAT(sum((if(dasc.determine='Pass',1,0)))/count(0)*100,2) passPoint \n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by week(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiBatchPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select time_table.checkTime,dasc_new.checkNumber,dasc_new.okNumber,dasc_new.passPoint \n" +
            "from \n" +
            "\t(select day(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by day(c.datelist) \n" +
            "\torder by day(c.datelist) asc\n" +
            "\t)time_table\n" +
            "left join \n" +
            "\t(select day(dasc.check_time) checkTime,count(0) checkNumber,sum((if(dasc.determine='Pass',1,0))) okNumber\n" +
            "\t,FORMAT(sum((if(dasc.determine='Pass',1,0)))/count(0)*100,2) passPoint \n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by day(dasc.check_time)\n" +
            "\t)dasc_new\n" +
            "on dasc_new.checkTime = time_table.checkTime")
    List<DfAoiSiCompare> getSiBatchPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    @Select("select dasc.factory,sum(dasc.check_number) checkNumber,sum(dasc.ng_number) ngNumber\n" +
            ",FORMAT(sum(dasc.ng_number)/sum(dasc.check_number)*100,2) defectPoint \n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.factory \n" +
            "order by dasc.factory asc")
    List<DfAoiSiCompare> getSiDefectPointFactory(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

    @Select("select dasc.sell_place,sum(dasc.check_number) checkNumber,sum(dasc.ng_number) ngNumber\n" +
            ",FORMAT(sum(dasc.ng_number)/sum(dasc.check_number)*100,2) defectPoint \n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.sell_place \n" +
            "order by dasc.sell_place asc")
    List<DfAoiSiCompare> getSiDefectPointSellPlace(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

    @Select("select defect_new.defect_name\n" +
            "from \n" +
            "\t(select dasc.factory,dasd.defect_name,sum(dasd.defect_number) \n" +
            "\t,row_number()over(partition by dasc.factory order by sum(dasd.defect_number) desc) num\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "\tleft join df_aoi_si_defect dasd \n" +
            "\ton dasc.batch = dasd.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.factory,dasd.defect_name\n" +
            "\t)defect_new\n" +
            "where defect_new.num<=5\n" +
            "group by defect_new.defect_name")
    List<String> getSiDefectNameListFactory(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select defect_new.defect_name\n" +
            "from \n" +
            "\t(select dasc.sell_place,dasd.defect_name,sum(dasd.defect_number)\n" +
            "\t,row_number()over(partition by dasc.sell_place order by sum(dasd.defect_number) desc) num\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "\tleft join df_aoi_si_defect dasd  \n" +
            "\ton dasc.batch = dasd.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.sell_place,dasd.defect_name\n" +
            "\t)defect_new\n" +
            "where defect_new.num<=5\n" +
            "group by defect_new.defect_name")
    List<String> getSiDefectNameListSellPlace(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select factory_new.factory,defect_new.defectName,defect_new.ngNumber,factory_new.checkNumber\n" +
            ",FORMAT(defect_new.ngNumber/factory_new.checkNumber*100,2) defectPoint\n" +
            "from \n" +
            "\t(select dasc.factory,sum(dasc.check_number) checkNumber\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dasc.factory\n" +
            "\torder by dasc.factory\n" +
            "\t)factory_new\n" +
            "left join \n" +
            "\t(select dasc.factory,dasd.defect_name defectName,sum(dasd.defect_number) ngNumber\n" +
            "\t,row_number()over(partition by dasc.factory order by sum(dasd.defect_number) desc) num\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "\tleft join df_aoi_si_defect dasd \n" +
            "\ton dasc.batch = dasd.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.factory,dasd.defect_name\n" +
            "\t)defect_new\n" +
            "on defect_new.factory = factory_new.factory ")
    List<DfAoiSiCompare> getSiOneDefectPointListFactory(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<DfAoiSiCompare> wrapper2);

    @Select("select sell_place_new.sellPlace,defect_new.defectName,defect_new.ngNumber,sell_place_new.checkNumber\n" +
            ",FORMAT(defect_new.ngNumber/sell_place_new.checkNumber*100,2) defectPoint\n" +
            "from \n" +
            "\t(select dasc.sell_place sellPlace,sum(dasc.check_number) checkNumber\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew_2.customSqlSegment} " +
            "\tgroup by dasc.sell_place\n" +
            "\torder by dasc.sell_place\n" +
            "\t)sell_place_new\n" +
            "left join \n" +
            "\t(select dasc.sell_place,dasd.defect_name defectName,sum(dasd.defect_number) ngNumber\n" +
            "\t,row_number()over(partition by dasc.sell_place order by sum(dasd.defect_number) desc) num\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "\tleft join df_aoi_si_defect dasd \n" +
            "\ton dasc.batch = dasd.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.sell_place,dasd.defect_name\n" +
            "\t)defect_new\n" +
            "on defect_new.sell_place = sell_place_new.sellPlace ")
    List<DfAoiSiCompare> getSiOneDefectPointListSellPlace(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<DfAoiSiCompare> wrapper2);


    @Select("select dasc.`type` \n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.`type` \n" +
            "order by dasc.`type` asc")
    List<String> getSiTypeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


    @Select("select defect_new.`type`,defect_new.defectName,defect_new.ngNumber,type_new.checkNumber\n" +
            ",FORMAT(defect_new.ngNumber/type_new.checkNumber*100,2) defectPoint\n" +
            ",defect_new.num\n" +
            "from \n" +
            "\t(select dasc.`type`,dasd.defect_name defectName,sum(dasd.defect_number) ngNumber\n" +
            "\t,row_number()over(partition by dasc.`type` order by sum(dasd.defect_number) desc) num\n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "\tleft join df_aoi_si_defect dasd \n" +
            "\ton dasd.batch = dasc.batch \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.`type`,dasd.defect_name\n" +
            "\t)defect_new\n" +
            "left join \n" +
            "\t(select dasc.`type`,sum(dasc.check_number) checkNumber \n" +
            "\tfrom df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dasc.`type` \n" +
            "\torder by dasc.`type` asc\n" +
            "\t)type_new\n" +
            "on type_new.`type` = defect_new.`type`\n" +
            "where defect_new.num<=5")
    List<DfAoiSiCompare> getSiDefectPointTop5List(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);


    @Select("select dasc.`type`,sum(if(dasc.determine='Pass',1,0)),sum(dasc.ng_number) ngNumber,sum(dasc.check_number) checkNumber\n" +
            ",FORMAT(sum(if(dasc.determine='Pass',1,0))/sum(dasc.check_number)*100,2) batchPassPoint\n" +
            ",FORMAT((sum(dasc.check_number)-sum(dasc.ng_number))/sum(dasc.check_number)*100,2) passPoint\n" +
            "from df_aoi_si_compare dasc \n" +
            "${ew.customSqlSegment} " +
            "group by dasc.`type` \n" +
            "order by dasc.`type` asc")
    List<DfAoiSiCompare> getSiPassPointAndBatchList(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

}
