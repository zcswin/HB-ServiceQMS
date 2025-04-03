package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiDefect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfAoiPiece;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI缺陷表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
public interface DfAoiDefectMapper extends BaseMapper<DfAoiDefect> {

    @Select("with dap_new as(\n" +
            "\tselect dap_new.*\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.`time`\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap\n" +
            " ${ew.customSqlSegment} " +
            "\t\t)dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            ")\n" +
            "select \n" +
            "dad.*\n" +
            "from dap_new \n" +
            "left join df_aoi_defect dad \n" +
            "on dad.check_id = dap_new.id " +
            " order by dad.id desc")
    List<DfAoiDefect> getDefectByBarCode(@Param(Constants.WRAPPER) Wrapper<DfAoiDefect> wrapper);

    @Select("with dap_new as(\n" +
            "\tselect dap_new.*\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.name,dap.`time`\n" +
            "\t\t,row_number()over(partition by dap.name order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap\n" +
            " ${ew.customSqlSegment} " +
            "\t\t)dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            ")\n" +
            "select \n" +
            "dad.featurevalues \n" +
            ",dad.severityid\n" +
            ",dad.qualityid \n" +
            ",dad.area \n" +
            ",dad.AOIxcenter \n" +
            ",dad.AOIycenter \n" +
            "from dap_new \n" +
            "left join df_aoi_defect dad \n" +
            "on dad.check_id = dap_new.id")
    List<DfAoiDefect> getDefectByPieceName(@Param(Constants.WRAPPER) Wrapper<DfAoiDefect> wrapper);

    @Select("select defect_one.*,defect_two.pieceNumber,(defect_one.defectWeight/defect_two.pieceNumber) NGPoint\n" +
            "from \n" +
            "\t(select featurevalues,defectClassName,defectArea,projectName,defectTime,\n" +
            "\tcount(0) defectWeight\n" +
            "\tfrom \n" +
            "\t\t(select dap_new.bar_code,dad.featurevalues featurevalues,dadc.name defectClassName,\n" +
            "\t\tddcp.defect_area defectArea,dp.name projectName,hour(dap_new.`time`) defectTime\n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\t\tleft join df_aoi_defect dad \n" +
            "\t\ton dad.frameid = dap_new.frameid\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = dap_new.ip \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = damp.user_code \n" +
            "\t\tleft join df_aoi_seat_protect dasp \n" +
            "\t\ton dasp.user_id = u.id \n" +
            "\t\tleft join df_aoi_defect_class dadc \n" +
            "\t\ton dadc.code = dad.class_code \n" +
            "\t\tleft join df_defect_code_protect ddcp \n" +
            "\t\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\tleft join df_project dp \n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "\t\tgroup by dap_new.bar_code,dad.featurevalues,dadc.name,ddcp.defect_area,dp.name,hour(dap_new.`time`)) defect_weight\n" +
            "\tgroup by featurevalues,defectClassName,defectArea,projectName,defectTime) defect_one\n" +
            "left join \n" +
            "\t(select defectTime,count(0) pieceNumber\n" +
            "\tfrom \n" +
            "\t(select dap_new.bar_code,hour(dap_new.`time`) defectTime,count(0) defectNumber\n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\t\tleft join df_aoi_defect dad \n" +
            "\t\ton dad.frameid = dap_new.frameid\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = dap_new.ip \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = damp.user_code \n" +
            "\t\tleft join df_aoi_seat_protect dasp \n" +
            "\t\ton dasp.user_id = u.id \n" +
            "\t\tleft join df_aoi_defect_class dadc \n" +
            "\t\ton dadc.code = dad.class_code \n" +
            "\t\tleft join df_defect_code_protect ddcp \n" +
            "\t\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\tleft join df_project dp \n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "\t\tgroup by dap_new.bar_code,hour(dap_new.`time`))piece_hour\n" +
            "\tgroup by defectTime) defect_two\n" +
            "on defect_one.defectTime = defect_two.defectTime\n" +
            "${ew_2.customSqlSegment} " +
            "order by NGPoint desc")
    List<DfAoiDefect> getAllDefectList(IPage<DfAoiDefect> page, @Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);


    @Select("select count(0) \n" +
            "from \n" +
            "(select dap_new.bar_code,hour(dap_new.`time`) defectTime,count(0) defectNumber\n" +
            "\tfrom \n" +
            "\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\tleft join df_aoi_defect dad \n" +
            "\ton dad.frameid = dap_new.frameid\n" +
            "\tleft join df_aoi_machine_protect damp \n" +
            "\ton damp.ip = dap_new.ip \n" +
            "\tleft join `user` u \n" +
            "\ton u.name = damp.user_code \n" +
            "\tleft join df_aoi_seat_protect dasp \n" +
            "\ton dasp.user_id = u.id \n" +
            "\tleft join df_aoi_defect_class dadc \n" +
            "\ton dadc.code = dad.class_code \n" +
            "\tleft join df_defect_code_protect ddcp \n" +
            "\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\tleft join df_e_code_protect decp \n" +
            "\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\tleft join df_project dp \n" +
            "\ton dp.id = decp.project_id " +
            "${ew.customSqlSegment} " +
            "group by dap_new.bar_code,hour(dap_new.`time`))piece_hour")
    Integer getTotalPieceNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    @Select("select count(0) defectNumber \n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\t\tleft join df_aoi_defect dad \n" +
            "\t\ton dad.frameid = dap_new.frameid\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = dap_new.ip \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = damp.user_code \n" +
            "\t\tleft join df_aoi_seat_protect dasp \n" +
            "\t\ton dasp.user_id = u.id \n" +
            "\t\tleft join df_aoi_defect_class dadc \n" +
            "\t\ton dadc.code = dad.class_code \n" +
            "\t\tleft join df_defect_code_protect ddcp \n" +
            "\t\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\tleft join df_project dp \n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "\t\tgroup by dad.featurevalues,dadc.name,ddcp.defect_area,dp.name,hour(dap_new.`time`)")
    Integer getDefectNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);


    @Select("select defect_one.*,defect_two.pieceNumber,(defect_one.defectWeight/defect_two.pieceNumber) NGPoint\n" +
            "from \n" +
            "\t(select area,featurevalues,defectClassName,defectArea,projectName,defectTime,\n" +
            "\tcount(0) defectWeight\n" +
            "\tfrom \n" +
            "\t\t(select dap_new.bar_code,dad.area,dad.featurevalues featurevalues,dadc.name defectClassName,\n" +
            "\t\tddcp.defect_area defectArea,dp.name projectName,hour(dap_new.`time`) defectTime\n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\t\tleft join df_aoi_defect dad \n" +
            "\t\ton dad.frameid = dap_new.frameid\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = dap_new.ip \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = damp.user_code \n" +
            "\t\tleft join df_aoi_seat_protect dasp \n" +
            "\t\ton dasp.user_id = u.id \n" +
            "\t\tleft join df_aoi_defect_class dadc \n" +
            "\t\ton dadc.code = dad.class_code \n" +
            "\t\tleft join df_defect_code_protect ddcp \n" +
            "\t\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\tleft join df_project dp \n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "\t\tgroup by dap_new.bar_code,dad.featurevalues,dadc.name,ddcp.defect_area,dp.name,hour(dap_new.`time`),dad.area) defect_weight\n" +
            "\tgroup by area,featurevalues,defectClassName,defectArea,projectName,defectTime) defect_one\n" +
            "left join \n" +
            "\t(select defectTime,count(0) pieceNumber\n" +
            "\tfrom \n" +
            "\t(select dap_new.bar_code,hour(dap_new.`time`) defectTime,count(0) defectNumber\n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) dap_new\n" +
            "\t\tleft join df_aoi_defect dad \n" +
            "\t\ton dad.frameid = dap_new.frameid\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = dap_new.ip \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = damp.user_code \n" +
            "\t\tleft join df_aoi_seat_protect dasp \n" +
            "\t\ton dasp.user_id = u.id \n" +
            "\t\tleft join df_aoi_defect_class dadc \n" +
            "\t\ton dadc.code = dad.class_code \n" +
            "\t\tleft join df_defect_code_protect ddcp \n" +
            "\t\ton ddcp.defect_name  = dad.featurevalues \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\tand substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\tleft join df_project dp \n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew_2.customSqlSegment} " +
            "\t\tgroup by dap_new.bar_code,hour(dap_new.`time`))piece_hour\n" +
            "\tgroup by defectTime) defect_two\n" +
            "on defect_one.defectTime = defect_two.defectTime\n" +
            "order by NGPoint desc;")
    List<DfAoiDefect> getAllDefectMappingList(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);


    @Select("SELECT\n" +
            "\tbig.*,\n" +
            "\tsm.defectWeight \n" +
            "FROM\n" +
            "\t( SELECT featurevalues, big_area AS bigArea, count( id ) defectNumber FROM df_aoi_defect ${ew.customSqlSegment} GROUP BY featurevalues, bigArea ) big\n" +
            "\tLEFT JOIN (\n" +
            "SELECT\n" +
            "\ts.featurevalues,\n" +
            "\tcount( s.frameid ) AS defectWeight,\n" +
            "\ts.bigArea \n" +
            "FROM\n" +
            "\t( SELECT featurevalues, frameid, count( id ) defectWeight, big_area AS bigArea FROM df_aoi_defect ${ew_2.customSqlSegment} GROUP BY featurevalues, bigArea, frameid ) s \n" +
            "  GROUP BY\n" +
            "\ts.featurevalues,\n" +
            "\ts.bigArea \n" +
            "\t) sm ON big.featurevalues = sm.featurevalues \n" +
            "\tAND big.bigArea = sm.bigArea ORDER BY sm.defectWeight desc")
    List<DfAoiDefect> listItemInfo(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);


    @Select("SELECT\n" +
            "\tbig.*,\n" +
            "\tsm.defectWeight \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\n" +
            "\tarea ,\n" +
            "\tcount( id ) defectNumber ,\n" +
            "\tbig_area\n" +
            "FROM\n" +
            "\tdf_aoi_defect ${ew.customSqlSegment} \n" +

            "GROUP BY\n" +
            "\n" +
            "\tarea ,big_area\n" +
            "\t) big\n" +
            "\tLEFT JOIN (\n" +
            "SELECT\n" +
            "\n" +
            "\tcount( s.frameid ) AS defectWeight,\n" +
            "\ts.area,\n" +
            "\ts.big_area\n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\n" +
            "\tframeid,\n" +
            "\tcount( id ) defectWeight,\n" +
            "\tarea,\n" +
            "\tbig_area\n" +
            "FROM\n" +
            "\tdf_aoi_defect ${ew_2.customSqlSegment} \n" +

            "GROUP BY\n" +
            "\n" +
            "\tarea,\n" +
            "\tbig_area,\n" +
            "\tframeid \n" +
            "\t) s \n" +
            "GROUP BY\n" +
            "\n" +
            "\ts.area,\n" +
            "\ts.big_area\n" +
            "\t) sm ON big.area = sm.area \n" +
            "\tAND big.big_area = sm.big_area \n" +
            "ORDER BY\n" +
            "\tsm.defectWeight DESC\n" +
            "\t")
    List<DfAoiDefect> listFeaturevaluesInfo(@Param(Constants.WRAPPER)Wrapper<DfAoiDefect> wrapper,@Param("ew_2")Wrapper<DfAoiDefect> wrapper2);

    @Select("WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            "        ${ew.customSqlSegment} " +
            ")\n" +
            ",TEMP_FQC_LATEST AS (\n" +
            "    select * from (\n" +
            "        SELECT\n" +
            "            DF_AOI_DECIDE_LOG.*,\n" +
            "            USER.NAME,\n" +
            "            USER.ALIAS,\n" +
            "            USER.PROCESS,\n" +
            "            USER.FACTORY_ID,\n" +
            "            ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY QC_TIME DESC) NO\n" +
            "        FROM\n" +
            "            DF_AOI_DECIDE_LOG\n" +
            "        LEFT JOIN USER ON DF_AOI_DECIDE_LOG.QC_USER_CODE = USER.NAME\n" +
            "        WHERE\n" +
            "            USER.PROCESS = 'FQC'\n" +
            "    )t\n" +
            "    WHERE t.no = 1\n" +
            " )\n" +
            "-- 算出来total\n" +
            ",temp_total as(\n" +
            "    select temp_fqc_latest.* ,(select count(0) from temp_fqc_latest) as total\n" +
            "    from temp_fqc_latest\n" +
            "    join temp_piece_latest on temp_fqc_latest.BAR_CODE = temp_piece_latest.BAR_CODE\n" +
            ")\n" +
            "select FEATUREVALUES , count(*)/total rate\n" +
            "FROM temp_total\n" +
            "JOIN DF_AOI_DEFECT ON temp_total.DEFECT_ID = DF_AOI_DEFECT.DEFECTID\n" +
            "GROUP BY DF_AOI_DEFECT.FEATUREVALUES\n" +
            "order by rate desc " +
            "limit #{top} ")
    List<Map<String, Object>> fqcNgTopRate(@Param(Constants.WRAPPER) QueryWrapper<DfAoiDefect> ew,
                                           @Param("top") Integer top);

    @Select("select \n" +
            "sum(if(dap_new.qualityid = '1',1,0)) inte1\n" +
            ",count(0) inte2\n" +
            ",sum(if(dap_new.qualityid = '1',1,0))/count(0)*100 dou1\n" +
            "from \n" +
            "\t(select dap.name,dap.bar_code ,dap.frameid ,dap.ip,dap.`time` ,dap.qualityid\n" +
            "\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap\n" +
            " ${ew.customSqlSegment} " +
            "\t) dap_new\n" +
            "where dap_new.num = 1")
    Rate3 getPieceDefectPoint(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    @Select("with dap_new as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code ,dap.frameid ,dap.ip,dap.`time`,dap.qualityid,dap.project\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap\n" +
            " ${ew.customSqlSegment} " +
            "\t\t) dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            ")\n" +
            ",dad_new as(\n" +
            "\tselect *\n" +
            "\tfrom df_aoi_defect dad \n" +
            " ${ew_2.customSqlSegment} " +
            ")\n" +
            ",dad_dap as(\n" +
            "\tselect\n" +
            "\tdad_new.check_id,dad_new.featurevalues,dad_new.class_name,dad_new.big_area\n" +
            "\tfrom dad_new\n" +
            "\tinner join dap_new\n" +
            "\ton dad_new.check_id = dap_new.id\n" +
            ")\n" +
            ",defect_number as(\n" +
            "\tselect \n" +
            "\tdad_dap.featurevalues,dad_dap.class_name,dad_dap.big_area\n" +
            "\t,count(0) defectNumber\n" +
            "\tfrom dad_dap\n" +
            "\tgroup by dad_dap.featurevalues,dad_dap.class_name,dad_dap.big_area\n" +
            ")\n" +
            ",defect_weigth as(\n" +
            "\tselect \n" +
            "\tdefect_piece.featurevalues,defect_piece.class_name,defect_piece.big_area\n" +
            "\t,count(0) defect_weight\n" +
            "\tfrom \n" +
            "\t\t(select \n" +
            "\t\tdad_dap.check_id,dad_dap.featurevalues,dad_dap.class_name,dad_dap.big_area\n" +
            "\t\t,count(0) defectNumber\n" +
            "\t\tfrom dad_dap\n" +
            "\t\tgroup by dad_dap.featurevalues,dad_dap.class_name,dad_dap.big_area,dad_dap.check_id\n" +
            "\t\t)defect_piece\n" +
            "\tgroup by defect_piece.featurevalues,defect_piece.class_name,defect_piece.big_area\n" +
            ")\n" +
            "select\n" +
            "defect_weigth.featurevalues featurevalues\n" +
            ",defect_weigth.class_name className\n" +
            ",defect_weigth.big_area bigArea\n" +
            ",defect_weigth.defect_weight defectWeight\n" +
            ",defect_number.defectNumber defectNumber\n" +
            "from defect_weigth\n" +
            "left join defect_number\n" +
            "on defect_number.featurevalues = defect_weigth.featurevalues\n" +
            "and defect_number.class_name = defect_weigth.class_name \n" +
            "and defect_number.big_area = defect_weigth.big_area\n" +
            "order by defect_weigth.defect_weight desc " +
            "limit 10")
    List<DfAoiDefect> getAllDefectAndWeightList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper, @Param("ew_2")Wrapper<DfAoiDefect> wrapper2);
}
