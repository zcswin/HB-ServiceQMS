package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.exportExcel.BatchqueryReport;
import com.ww.boengongye.entity.exportExcel.EmpCapacityReport;
import com.ww.boengongye.entity.exportExcel.OqcReport;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI玻璃单片信息表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@Mapper
public interface DfAoiPieceMapper extends BaseMapper<DfAoiPiece> {
    @Select("select dap.*,damp.machine_code " +
            "from df_aoi_piece dap " +
            "left join df_aoi_machine_protect damp " +
            "on dap.ip =damp.ip " +
            "${ew.customSqlSegment}")
    List<DfAoiPiece> getDfAoiPieceListByBarCode(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    @Select("select count(0) " +
            "from " +
            "   (select dadl.bar_code,dadl.qc_time,dadl.qc_result,dadl.defect_id,dadl.qc_user_code " +
            "   ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl.qc_time desc) num " +
            "   from df_aoi_decide_log dadl) first_qc " +
            "left join " +
            "   (select dap.name,dap.bar_code " +
            "   from df_aoi_piece dap " +
            "   group by dap.name ,dap.bar_code" +
            ") dap_new " +
            "on dap_new.bar_code =first_qc.bar_code " +
            "left join df_e_code_protect decp " +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 " +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "${ew.customSqlSegment}")
    Integer getDefectPieceNumber(@Param(Constants.WRAPPER) Wrapper<Integer> wrapper);
    //优化
    @Select("select \n" +
            "sum(if(dadl_new.qc_result = 'NG',1,0)) inte1\n" +
            ",count(0) inte2  \n" +
            "from \n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select dadl.bar_code,dadl.qc_time,dadl.qc_result,dadl.defect_id,dadl.qc_user_code\n" +
            "\t\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl.qc_time desc) num\n" +
            "\t\tfrom df_aoi_decide_log dadl\n" +
            "\t\t) dadl_new \n" +
            "\twhere dadl_new.num = 1\n" +
            "\t)dadl_new\n" +
            "left join \n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select *\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap\n" +
            "\t\t)dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            "\t)dap_new\n" +
            "on dap_new.bar_code =dadl_new.bar_code " +
            "${ew.customSqlSegment}")
    Rate3 getUserInputAndDefectNumber(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);




    @Select("select dad.featurevalues defectName,count(dad.featurevalues) defectNumber " +
            "from " +
            "(select dadl.bar_code,dadl.qc_time,dadl.qc_result,dadl.defect_id,dadl.qc_user_code," +
            "row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl.qc_time desc) num " +
            "from df_aoi_decide_log dadl) first_qc " +
            "left join df_aoi_defect dad " +
            "on dad.defectid = first_qc.defect_id " +
            "left join " +
            "   (select dap.name,dap.bar_code " +
            "   from df_aoi_piece dap " +
            "   group by dap.name ,dap.bar_code) dap_new " +
            "on dap_new.bar_code =first_qc.bar_code " +
            "left join df_e_code_protect decp " +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 " +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "${ew.customSqlSegment} " +
            "group by dad.featurevalues " +
            "order by defectNumber desc")
    List<DfAoiDefectPoint> getDefectPointList(IPage<DfAoiDefectPoint> page,@Param(Constants.WRAPPER) Wrapper<DfAoiDefectPoint> wrapper);
    //优化
    @Select("select dad.featurevalues defectName,count(0) defectNumber \n" +
            "from \n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select dadl.bar_code,dadl.qc_time,dadl.qc_result,dadl.defect_id,dadl.qc_user_code\n" +
            "\t\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl.qc_time desc) num\n" +
            "\t\tfrom df_aoi_decide_log dadl\n" +
            "\t\twhere dadl.defect_id !=''\n" +
            "\t\t) dadl_new \n" +
            "\twhere dadl_new.num = 1\n" +
            "\t)dadl_new\n" +
            "left join \n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select *\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap\n" +
            "\t\t)dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            "\t)dap_new\n" +
            "on dap_new.bar_code =dadl_new.bar_code\n" +
            "left join df_aoi_defect dad \n" +
            "on dad.defectid = dadl_new.defect_id\n" +
            "${ew.customSqlSegment} " +
            "group by dad.featurevalues \n" +
            "order by count(0) desc \n" +
            "limit 5")
    List<DfAoiDefectPoint> getUserDefectTop5List(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);



    @Select("select dad.featurevalues escapeName,count(dad.featurevalues) escapeNumber " +
            "from df_aoi_undetected dau " +
            "left join df_aoi_defect dad " +
            "on dad.defectid = dau.defectid " +
            "left join " +
            "   (select dap.name,dap.bar_code " +
            "   from df_aoi_piece dap " +
            "   group by dap.name ,dap.bar_code) dap_new " +
            "on dap_new.bar_code =dau.barcode " +
            "left join df_e_code_protect decp " +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 " +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "left join " +
            "   (select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result " +
            "   ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num " +
            "   from df_aoi_decide_log dadl " +
            "   left join `user` u  " +
            "   on u.name = dadl.qc_user_code " +
            "   where u.process = 'FQC' " +
            "   )dadl_new1  " +
            "on dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' " +
            "${ew.customSqlSegment} " +
            "group by dad.featurevalues " +
            "order by escapeNumber desc")
    List<DfAoiEscapePoint> getEscapePointList(IPage<DfAoiEscapePoint> page,@Param(Constants.WRAPPER) Wrapper<DfAoiEscapePoint> wrapper);
    //优化
    @Select("select dad.featurevalues escapeName,count(dad.featurevalues) escapeNumber\n" +
            "from \n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select *\n" +
            "\t\t,row_number()over(partition by dau.barcode,dau.fqc_user  order by dau.create_time desc) num\n" +
            "\t\tfrom df_aoi_undetected dau \n" +
            "\t\t)dau_new\n" +
            "\twhere dau_new.num = 1\n" +
            "\t)dau_new\n" +
            "left join df_aoi_defect dad\n" +
            "on dad.defectid = dau_new.defectid\n" +
            "left join\n" +
            "    (select *\n" +
            "\tfrom \n" +
            "\t\t(select *\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap\n" +
            "\t\t)dap_new\n" +
            "\twhere dap_new.num = 1\n" +
            "\t)dap_new\n" +
            "on dap_new.bar_code =dau_new.barcode\n" +
            "left join\n" +
            "\t(select *\n" +
            "\tfrom \n" +
            "\t\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result\n" +
            "\t     ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\t     from df_aoi_decide_log dadl\n" +
            "\t     left join `user` u\n" +
            "\t     on u.name = dadl.qc_user_code\n" +
            "\t     where u.process = 'FQC'\n" +
            "\t     )dadl_new\n" +
            "\twhere dadl_new.num = 1\n" +
            "\tand dadl_new.qc_result ='OK'\n" +
            "\t)dadl_new\n" +
            "on dadl_new.bar_code =dau_new.barcode\n" +
            "${ew.customSqlSegment} " +
            "group by dad.featurevalues\n" +
            "order by escapeNumber desc\n" +
            "limit 5")
    List<DfAoiEscapePoint> getEscapePointTop5List(@Param(Constants.WRAPPER) Wrapper<DfAoiPiece> wrapper);

    @Select("select userName,sum(coutEscape_new.escapeNumber) escapeTop5Number " +
            "from " +
            "(select userName,escapeName,escapeNumber," +
            "row_number()over(partition by userName order by escapeNumber desc) num " +
            "from " +
            "(select u.alias userName,dad.featurevalues escapeName,count(dad.featurevalues) escapeNumber " +
            "from df_aoi_undetected dau " +
            "left join `user` u " +
            "on u.name = dau.fqc_user " +
            "left join df_aoi_defect dad " +
            "on dad.defectid = dau.defectid " +
            "left join " +
            "   (select dap.name,dap.bar_code " +
            "   from df_aoi_piece dap " +
            "   group by dap.name ,dap.bar_code) dap_new " +
            "on dap_new.bar_code =dau.barcode " +
            "left join df_e_code_protect decp " +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 " +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "left join " +
            "   (select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result " +
            "   ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num " +
            "   from df_aoi_decide_log dadl " +
            "   left join `user` u  " +
            "   on u.name = dadl.qc_user_code " +
            "   where u.process = 'FQC' " +
            "   )dadl_new1  " +
            "on dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' " +
            "${ew.customSqlSegment} " +
            "group by u.alias,dad.featurevalues) " +
            "countEscape) " +
            "coutEscape_new " +
            "where coutEscape_new.num<=5 " +
            "group by coutEscape_new.username " +
            "order by sum(coutEscape_new.escapeNumber) desc")
    List<DfAoiEscape> getAllEscapeList(@Param(Constants.WRAPPER) Wrapper<DfAoiEscape> wrapper);

    @Select("select userName,escapeName,escapeNumber " +
            "from " +
            "(select userName,escapeName,escapeNumber," +
            "row_number()over(partition by userName order by escapeNumber desc) num " +
            "from " +
            "(select u.alias userName,dad.featurevalues escapeName,count(dad.featurevalues) escapeNumber " +
            "from df_aoi_undetected dau " +
            "left join `user` u " +
            "on u.name = dau.fqc_user " +
            "left join df_aoi_defect dad " +
            "on dad.defectid = dau.defectid " +
            "left join " +
            "   (select dap.name,dap.bar_code " +
            "   from df_aoi_piece dap " +
            "   group by dap.name ,dap.bar_code) dap_new " +
            "on dap_new.bar_code =dau.barcode " +
            "left join df_e_code_protect decp " +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 " +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "left join " +
            "   (select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result " +
            "   ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num " +
            "   from df_aoi_decide_log dadl " +
            "   left join `user` u  " +
            "   on u.name = dadl.qc_user_code " +
            "   where u.process = 'FQC' " +
            "   )dadl_new1  " +
            "on dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' " +
            "${ew.customSqlSegment} " +
            "group by u.alias,dad.featurevalues) " +
            "countEscape) " +
            "coutEscape_new " +
            "where coutEscape_new.num<=5")
    List<DfAoiEscapePoint> getEscapeTop5PointList(@Param(Constants.WRAPPER) Wrapper<DfAoiEscapePoint> wrapper);

    @Select("select u.name,u.alias ,u.process ,u.grade,sum(dadl_new3.num) oqcNumber\n" +
            "from `user` u \n" +
            "left join \n" +
            "(select dadl_new1.bar_code ,dadl_new1.qc_time ,dadl_new1.qc_user_code ,dadl_new1.qc_user_name,dadl_new1.num\n" +
            "from \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'FQC'\n" +
            "\t)dadl_new1\n" +
            "left join \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl .qc_user_code ,dadl.qc_user_name ,\n" +
            "\trow_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'OQC'\n" +
            "\t)dadl_new2\n" +
            "on dadl_new2.bar_code = dadl_new1.bar_code\n" +
            "where dadl_new1.num = 1\n" +
            "and dadl_new2.num = 1" +
            ") dadl_new3\n" +
            "on dadl_new3.qc_user_code = u.name \n" +
            "left join df_aoi_seat_protect dasp \n" +
            "on dasp.user_id  = u.id \n" +
            "left join \n" +
            "\t(select dap.name,dap.bar_code \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "on dap_new.bar_code =dadl_new3.bar_code\n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "${ew.customSqlSegment} " +
            "group by u.name,u.alias ,u.process ,u.grade ")
    List<User> getAllUserOQCNumberList(IPage<User> page,@Param(Constants.WRAPPER)Wrapper<User> wrapper);
//    //优化
//    @Select("select u.name,u.alias ,u.process,u.grade,sum(dadl_new3.num) oqcNumber\n" +
//            "from `user` u\n" +
//            "left join\n" +
//            "\t(select dadl_new1.bar_code ,dadl_new1.qc_time ,dadl_new1.qc_user_code ,dadl_new1.qc_user_name,dadl_new1.num\n" +
//            "    from \n" +
//            "    \t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name\n" +
//            "        ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
//            "        from df_aoi_decide_log dadl\n" +
//            "        left join `user` u\n" +
//            "        on u.name = dadl.qc_user_code\n" +
//            "        where u.process = 'FQC'\n" +
//            "        )dadl_new1\n" +
//            "    left join\n" +
//            "        (select dadl.bar_code ,dadl.qc_time ,dadl .qc_user_code ,dadl.qc_user_name \n" +
//            "        ,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
//            "        from df_aoi_decide_log dadl\n" +
//            "        left join `user` u\n" +
//            "        on u.name = dadl.qc_user_code\n" +
//            "        where u.process = 'OQC'\n" +
//            "        )dadl_new2\n" +
//            "    on dadl_new2.bar_code = dadl_new1.bar_code\n" +
//            "    where dadl_new1.num = 1\n" +
//            "    and dadl_new2.num = 1\n" +
//            "   ) dadl_new3\n" +
//            "on dadl_new3.qc_user_code = u.name\n" +
//            "left join\n" +
//            "\t(select *\n" +
//            "\tfrom \n" +
//            "\t\t(select *\n" +
//            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
//            "\t\tfrom df_aoi_piece dap\n" +
//            "\t\t)dap_new\n" +
//            "\t\twhere dap_new.num = 1\n" +
//            "\t)dap_new\n" +
//            "on dap_new.bar_code =dadl_new3.bar_code " +
//            "${ew.customSqlSegment} " +
//            "group by u.name,u.alias ,u.process ,u.grade ")
//    List<User> getAllUserOQCNumberList(IPage<User> page,@Param(Constants.WRAPPER)Wrapper<User> wrapper);

    @Select("select count(0) \n" +
            "from df_aoi_undetected dau \n" +
            "left join `user` u \n" +
            "on u.name = dau.fqc_user \n" +
            "left join df_aoi_seat_protect dasp \n" +
            "on dasp.user_id  = u.id \n" +
            "left join df_aoi_defect dad \n" +
            "on dad.defectid = dau.defectid \n" +
            "left join \n" +
            "\t(select dap.name,dap.bar_code \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "on dap_new.bar_code =dau.barcode\n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "left join \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'FQC'\n" +
            "\t)dadl_new1 \n" +
            "on dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' " +
            "${ew.customSqlSegment}")
    Integer getUserEscapeNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);



    @Select("select count(0) inputNumber,date_format(date_add(first_qc.qc_time,interval 1 hour),'%Y-%m-%d %H') inputTime \n" +
            "from \n" +
            "\t(select dadl.bar_code,dadl.qc_time,dadl.qc_result,dadl.defect_id,dadl.qc_user_code,\n" +
            "\trow_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl.qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl) first_qc \n" +
            "left join \n" +
            "\t(select dap.name,dap.bar_code \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "on dap_new.bar_code =first_qc.bar_code\n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 " +
            "${ew.customSqlSegment} " +
            "group by inputTime " +
            "order by inputTime asc")
    List<DfAoiOutputPoint> getUserHourOutputPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiOutputPoint> wrapper);

    @Select("select escapeName\n" +
            "from \n" +
            "\t(select username,escapeName,escapeTime,escapeNumber\n" +
            "\tfrom \n" +
            "\t\t(select userName,escapeName,escapeTime,escapeNumber\n" +
            "\t\t,row_number()over(partition by userName order by escapeNumber desc) num \n" +
            "\t\tfrom \n" +
            "\t\t\t(select u.alias userName,dad.featurevalues escapeName,date(date_sub(dadl_new1.qc_time,interval 7 hour)) escapeTime,count(dad.featurevalues) escapeNumber \n" +
            "\t\t\tfrom df_aoi_undetected dau \n" +
            "\t\t\tleft join `user` u \n" +
            "\t\t    on u.name = dau.fqc_user \n" +
            "\t\t    left join df_aoi_defect dad \n" +
            "\t\t    on dad.defectid = dau.defectid \n" +
            "\t\t    left join \n" +
            "\t\t\t\t(select dap.name,dap.bar_code \n" +
            "\t\t\t\tfrom df_aoi_piece dap \n" +
            "\t\t\t\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "\t\t\ton dap_new.bar_code =dau.barcode\n" +
            "\t\t    left join df_e_code_protect decp \n" +
            "\t\t    on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\t    and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\t    left join \n" +
            "\t\t\t    (select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result \n" +
            "\t\t\t\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num \n" +
            "\t\t\t\tfrom df_aoi_decide_log dadl \n" +
            "\t\t\t\tleft join `user` u  \n" +
            "\t\t\t\ton u.name = dadl.qc_user_code \n" +
            "\t\t\t\twhere u.process = 'FQC' \n" +
            "\t\t\t\t)dadl_new1  \n" +
            "\t\t\ton dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' \n" +
            "     ${ew.customSqlSegment} " +
            "\t\t\t) countEscape\n" +
            "\t\t) coutEscape_new \n" +
            "\twhere coutEscape_new.num<=5\n" +
            "\torder by escapeTime asc\n" +
            ")coutEscape_new2\n" +
            "group by escapeName")
    List<String> getFqcUserDefect7Day(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select date(date_sub(dadl_new1.qc_time,interval 7 hour)) escapeTime,count(0) oqcNumber\n" +
            "from \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'FQC'\n" +
            "\t)dadl_new1\n" +
            "left join \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl .qc_user_code ,dadl.qc_user_name\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'OQC'\n" +
            "\t)dadl_new2\n" +
            "on dadl_new2.bar_code = dadl_new1.bar_code\n" +
            "left join \n" +
            "\t(select dap.name,dap.bar_code \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "on dap_new.bar_code =dadl_new1.bar_code\n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "${ew.customSqlSegment}")
    List<DfAoiEscape> getFqcUserOqcNumberDay(@Param(Constants.WRAPPER)Wrapper<DfAoiEscape> wrapper);

    @Select("select escapeName\n" +
            "from \n" +
            "\t(select username,escapeName,escapeTime,escapeNumber\n" +
            "\tfrom \n" +
            "\t\t(select userName,escapeName,escapeTime,escapeNumber\n" +
            "\t\t,row_number()over(partition by userName order by escapeNumber desc) num \n" +
            "\t\tfrom \n" +
            "\t\t\t(select u.alias userName,dad.featurevalues escapeName,yearweek(date_sub(dadl_new1.qc_time,interval 7 hour)) escapeTime,count(dad.featurevalues) escapeNumber \n" +
            "\t\t\tfrom df_aoi_undetected dau \n" +
            "\t\t\tleft join `user` u \n" +
            "\t\t    on u.name = dau.fqc_user \n" +
            "\t\t    left join df_aoi_defect dad \n" +
            "\t\t    on dad.defectid = dau.defectid \n" +
            "\t\t    left join \n" +
            "\t\t\t\t(select dap.name,dap.bar_code \n" +
            "\t\t\t\tfrom df_aoi_piece dap \n" +
            "\t\t\t\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "\t\t\ton dap_new.bar_code =dau.barcode\n" +
            "\t\t    left join df_e_code_protect decp \n" +
            "\t\t    on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
            "\t\t    and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
            "\t\t    left join \n" +
            "\t\t\t    (select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name,dadl.qc_result \n" +
            "\t\t\t\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num \n" +
            "\t\t\t\tfrom df_aoi_decide_log dadl \n" +
            "\t\t\t\tleft join `user` u  \n" +
            "\t\t\t\ton u.name = dadl.qc_user_code \n" +
            "\t\t\t\twhere u.process = 'FQC' \n" +
            "\t\t\t\t)dadl_new1  \n" +
            "\t\t\ton dadl_new1.bar_code =dau.barcode and dadl_new1.qc_result ='OK' \n" +
            "     ${ew.customSqlSegment} " +
            "\t\t\t) countEscape\n" +
            "\t\t) coutEscape_new \n" +
            "\twhere coutEscape_new.num<=5\n" +
            "\torder by escapeTime asc\n" +
            ")coutEscape_new2\n" +
            "group by escapeName;")
    List<String> getFqcUserDefect4Week(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


//    @Select("select escapeTime- yearweek(curdate())+4 escapeTime,oqcNumber\n" +
//            "from \n" +
//            "(select yearweek(date_sub(dadl_new1.qc_time,interval 7 hour)) escapeTime,count(0) oqcNumber\n" +
//            "from \n" +
//            "\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name ,\n" +
//            "\trow_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
//            "\tfrom df_aoi_decide_log dadl)dadl_new1\n" +
//            "left join \n" +
//            "\t(select dadl.bar_code ,dadl.qc_time ,dadl .qc_user_code ,dadl.qc_user_name ,\n" +
//            "\trow_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
//            "\tfrom df_aoi_decide_log dadl)dadl_new2\n" +
//            "on dadl_new2.bar_code = dadl_new1.bar_code \n" +
//            "left join \n" +
//            "\t(select dap.name,dap.bar_code \n" +
//            "\tfrom df_aoi_piece dap \n" +
//            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
//            "on dap_new.bar_code =dadl_new1.bar_code\n" +
//            "left join df_e_code_protect decp \n" +
//            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
//            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
//            "${ew.customSqlSegment} ) oqeWeek")
//    List<DfAoiEscape> getFqcUserOqcNumberWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiEscape> wrapper);


    @Select("select escapeTime- yearweek(curdate())+4 escapeTime,oqcNumber\n" +
            "from \n" +
            "(select yearweek(date_sub(dadl_new1.qc_time,interval 7 hour)) escapeTime,count(0) oqcNumber\n" +
            "from \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'FQC'\n" +
            "\t)dadl_new1\n" +
            "left join \n" +
            "\t(select dadl.bar_code ,dadl.qc_time ,dadl .qc_user_code ,dadl.qc_user_name\n" +
            "\t,row_number()over(partition by dadl.bar_code,dadl.qc_user_code order by dadl .qc_time desc) num\n" +
            "\tfrom df_aoi_decide_log dadl\n" +
            "\tleft join `user` u \n" +
            "\ton u.name = dadl.qc_user_code\n" +
            "\twhere u.process = 'OQC'\n" +
            "\t)dadl_new2\n" +
            "on dadl_new2.bar_code = dadl_new1.bar_code\n" +
            "left join \n" +
            "\t(select dap.name,dap.bar_code \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.name ,dap.bar_code) dap_new\n" +
            "on dap_new.bar_code =dadl_new1.bar_code\n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "${ew.customSqlSegment} ) oqeWeek " +
            "order by escapeTime")
    List<DfAoiEscape> getFqcUserOqcNumberWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiEscape> wrapper);


//    @Select("select dap_new.bar_code,dp.name projectName,damp.machine_name machineName,decp.colour,dap_new.qualityid newResult\n" +
//            "from \n" +
//            "\t(select dap.name,dap.bar_code,dap.qualityid,dap.ip,\n" +
//            "\trow_number()over(partition by dap.bar_code order by dap.`time`desc) num\n" +
//            "\tfrom df_aoi_piece dap)dap_new\n" +
//            "left join df_aoi_machine_protect damp \n" +
//            "on damp.ip  = dap_new.ip \n" +
//            "left join df_e_code_protect decp \n" +
//            "on substring(dap_new.name,decp.start_data1,decp.cut_length1) = decp.match_data1 \n" +
//            "and substring(dap_new.name,decp.start_data2,decp.cut_length2) = decp.match_data2 \n" +
//            "left join df_project dp \n" +
//            "on dp.id = decp.project_id  " +
//            "${ew.customSqlSegment} ")
//    DfAoiDetermine getDfAoiDetermineByBarCode(@Param(Constants.WRAPPER)Wrapper<DfAoiDetermine> wrapper);

    @Select("select " +
            "dap_new.bar_code" +
            ",dap_new.project projectName" +
            ",dap_new.machine machineName" +
            ",dap_new.color colour" +
            ",dap_new.qualityid newResult\n" +
            "from \n" +
            "\t(select dap.name,dap.bar_code,dap.project,dap.machine,dap.color,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time`desc) num\n" +
            "\tfrom df_aoi_piece dap" +
            " )dap_new\n" +
            " ${ew.customSqlSegment} ")
    DfAoiDetermine getDfAoiDetermineByBarCode(@Param(Constants.WRAPPER)Wrapper<DfAoiDetermine> wrapper);
























    @Select("select concat(t1.project,'_',t1.color) str1, t1.h inte1, t1.num inte2, t2.num inte3 from (\n" +
            "select color, project, hour(time) h, count(*) num from `df_aoi_piece` pie\n" +
            "${ew.customSqlSegment}\n" +
            "group by color, project, h) t1\n" +
            "left join \n" +
            "(select pie.color, pie.project, hour(pie.time) h, count(distinct pie.frameid) num from `df_aoi_defect` def\n" +
            "left join `df_aoi_piece` pie on pie.frameid = def.frameid\n" +
            "${ew.customSqlSegment}\n" +
            "group by pie.color, pie.project, h) t2 on t1.color = t2.color and t1.project = t2.project and t1.h = t2.h\n" +
            "order by str1")
    List<Rate3> listPieceAndDefectNumGroupByHour(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    @Select("-- 批量查询报表\n" +
            "WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            "        WHERE no = 1\n" +
            "#         AND TIME BETWEEN '2023-01-01' AND '2023-10-10'\n" +
            "#         AND FACTORY LIKE '%%'\n" +
            "#         AND LINE_BODY LIKE '%%'\n" +
            "#         AND PROJECT LIKE '%%'\n" +
            "#         AND COLOR LIKE  '%%'\n" +
            ")\n" +
            "-- 条码分组 过aoi次数\n" +
            ",temp_aoi_pass AS (\n" +
            "    select BAR_CODE,count(*) aoiPass\n" +
            "    from DF_AOI_PIECE\n" +
            "    GROUP BY BAR_CODE\n" +
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
            ",TEMP_OQC_LATEST AS (\n" +
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
            "            USER.PROCESS = 'OQC'\n" +
            "    )t\n" +
            "    WHERE t.no = 1\n" +
            " )\n" +
            "SELECT\n" +
            "temp_piece_latest.BAR_CODE as glassBarCode,\n" +
            "temp_piece_latest.PROJECT as model,\n" +
            "temp_piece_latest.color,\n" +
            "temp_piece_latest.machine as aoiMachine,\n" +
            "temp_aoi_pass.aoipass as count,\n" +
            "temp_piece_latest.time as aoiTime ,\n" +
            "temp_piece_latest.qualityid    as aoiResult,\n" +
            "TEMP_FQC_LATEST.qc_user_name as fqcEmpName,\n" +
            "TEMP_FQC_LATEST.qc_user_code as fqcEmpCode,\n" +
            "TEMP_FQC_LATEST.qc_time      as fqcTime,\n" +
            "TEMP_FQC_LATEST.qc_result    as fqcResult,\n" +
            "TEMP_OQC_LATEST.qc_user_name as oqcEmpName,\n" +
            "TEMP_OQC_LATEST.qc_user_code as oqcEmpCode,\n" +
            "TEMP_OQC_LATEST.qc_time      as oqcTime,\n" +
            "TEMP_OQC_LATEST.qc_result    as oqcResult\n" +
            "from TEMP_PIECE_LATEST\n" +
            "JOIN temp_aoi_pass ON TEMP_PIECE_LATEST.BAR_CODE = temp_aoi_pass.BAR_CODE\n" +
            "JOIN TEMP_FQC_LATEST ON temp_aoi_pass.BAR_CODE = TEMP_FQC_LATEST.BAR_CODE\n" +
            "JOIN temp_oqc_latest ON TEMP_FQC_LATEST.BAR_CODE = temp_oqc_latest.BAR_CODE\n" +
            "${ew.customSqlSegment}\n")
    List<BatchqueryReport> getFqcOrOQCDetailByCode(IPage<BatchqueryReport> page,@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw);

    @Select("with temp_piece as(\n" +
            "\tselect time,name,project,qualityid,bar_code\n" +
            "\t,ROW_NUMBER() over(partition by name ORDER BY time DESC) number1\n" +
            "\tfrom df_aoi_piece piece\n" +
            "\twhere piece.time BETWEEN #{startTime} AND #{endTime}\n" +
            "),\n" +
            "temp_piece_lateset AS(\n" +
            "\tselect DATE_FORMAT(DATE_SUB(piece.time,INTERVAL 7 HOUR),'%m-%d') date,\n" +
            "\tmax(protect.factory_id) as factory_id,\n" +
            "\tmax(protect.line_boby_id) as line_boby_id,\n" +
            "\tmax(project) as project,\n" +
            "\tmax(protect.classCategory) as classCategory,\n" +
            "\tmax(user.process) as process,\n" +
            "\tmax(user.alias) AS alias,\n" +
            "\tmax(user.name) as name,\n" +
            "\tcount(*) as total,\n" +
            "\tsum(IF (qualityid = 0,1,0)) ok,\n" +
            "\tsum(IF (qualityid = 1,1,0)) ng\n" +
            "\tfrom temp_piece piece\n" +
            "\tJOIN df_aoi_decide_log log on piece.bar_code = log.bar_code\n" +
            "\tJOIN df_aoi_seat_protect protect on log.qc_user_code= protect.user_id\n" +
            "\tJOIN user ON protect.user_id = user.name\n" +
            "${ew.customSqlSegment}\n" +
            "GROUP BY date,user.factory_id,protect.line_boby_id,piece.project,user.process,user.name\n" +
            " ORDER BY date,user.factory_id,protect.line_boby_id,piece.project,user.process,user.name" +
            ")\n" +
            "select * \n" +
            "from temp_piece_lateset")
    List<EmpCapacityReport> getEmpCapacityStatement(IPage<EmpCapacityReport> page,
                                                    @Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime);

    @Select("with temp_piece_latest as (\n" +
            "    select project,\n" +
            "    bar_code,\n" +
            "    ROW_NUMBER() over (partition by name ORDER BY time DESC) number1\n" +
            "    from df_aoi_piece piece\n" +
            "),\n" +
            "temp_log_fqc_latest as (\n" +
            "    select log.qc_result,\n" +
            "      log.qc_time,\n" +
            "      user.factory_id,\n" +
            "      protect.line_boby_id,\n" +
            "      pieceLatest.project,\n" +
            "      qc_user_name,\n" +
            "      qc_user_code,\n" +
            "      log.bar_code\n" +
            "    from temp_piece_latest pieceLatest\n" +
            "        JOIN df_aoi_decide_log log on pieceLatest.bar_code = log.bar_code\n" +
            "        JOIN user ON log.qc_user_code = user.name\n" +
            "        JOIN df_aoi_seat_protect protect ON user.name = protect.user_id\n" +
            " ${qw1.customSqlSegment}\n" +
            "    and user.process = 'FQC'\n" +
            "),temp_piece_oqcEmp_latest as (\n" +
            "    select log.qc_result,\n" +
            "       log.qc_time,\n" +
            "       qc_user_name,\n" +
            "       qc_user_code,\n" +
            "       log.bar_code\n" +
            "    FROM temp_piece_latest pieceLatest\n" +
            "    JOIN df_aoi_decide_log log on pieceLatest.bar_code = log.bar_code\n" +
            "    JOIN user ON log.qc_user_code = user.name\n" +
            "    JOIN df_aoi_seat_protect protect ON user.name = protect.user_id\n" +
            "   ${ew.customSqlSegment}\n" +
            "    and user.process = 'OQC'\n" +
            ")\n" +
            "SELECT DATE_FORMAT(DATE_SUB(oqcEmpLatest.qc_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "     max(fqcLatest.factory_id)                                             factoryId,\n" +
            "     max(fqcLatest.line_boby_id)                                           lineBobyId,\n" +
            "     max(fqcLatest.project)                                                project,\n" +
            "     max(fqcLatest.qc_user_name)                                           fqcEmpName,\n" +
            "     fqcLatest.qc_user_code                                                fqcEmpCode,\n" +
            "     max(oqcEmpLatest.qc_user_name)                                        oqcEmpName,\n" +
            "     max(oqcEmpLatest.qc_user_code)                                        oqcEmpCode,\n" +
            "     sum(if(fqcLatest.qc_result = 'OK', 1, 0))                             fqcTotal,\n" +
            "     count(*)                                                              oqcTotal,\n" +
            "     sum(if(oqcEmpLatest.qc_result = 'NG', 1, 0))                          oqcNg\n" +
            "FROM temp_log_fqc_latest fqcLatest\n" +
            "RIGHT JOIN temp_piece_oqcEmp_latest oqcEmpLatest ON fqcLatest.bar_code = oqcEmpLatest.bar_code\n" +
            "where oqcEmpLatest.qc_user_code is not null \n" +
            "GROUP BY date, fqcLatest.qc_user_code\n" +
            "ORDER BY date asc")
//    @Select("select * from df_aoi_piece")
    List<OqcReport> getOQCReport(IPage<OqcReport> pages,
                                 @Param("qw1") QueryWrapper<DfAoiPiece> qw1,
                                 @Param("ew") QueryWrapper<DfAoiPiece> qw2,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime,
                                 @Param("empCode") String empCode);

    @Select("with temp_piece_latest as(\n" +
            "\tselect project,bar_code,color\n" +
            "\t,ROW_NUMBER() over(partition by name ORDER BY time DESC) number1\n" +
            "\tfrom df_aoi_piece piece\n" +
            "\twhere piece.time BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            ",temp_log_latest as(\n" +
            "\tselect machine_code,bar_code\n" +
            "\t,ROW_NUMBER() over(partition by bar_code ORDER BY create_time DESC) number1\n" +
            "\tfrom df_aoi_decide_log log\n" +
            "\twhere log.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            "\n" +
            ",temp_result as (" +
            "select log.machine_code str1,featurevalues str2,count(*) count\n" +
            "from df_aoi_undetected undetected\n" +
            "JOIN df_aoi_defect defect on undetected.defectid = defect.defectid\n" +
            "JOIN temp_piece_latest piece on undetected.barcode = piece.bar_code\n" +
            "JOIN df_aoi_seat_protect protect on undetected.fqc_user = protect.user_id\n" +
            "JOIN temp_log_latest log on undetected.barcode = log.bar_code\n" +
            "${ew.customSqlSegment}\n" +
            "AND protect.classCategory = #{clazz}\n" +
            "GROUP BY log.machine_code,defect.featurevalues\n" +
            "ORDER BY machine_code,featurevalues" +
            ")\n" +
            "select *,count / sum(count) over(PARTITION by str2) dou1\n" +
            "from temp_result " +
            "order by dou1 limit 10")
    List<Rate3> lineBodyloss(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime,
                             @Param("clazz") String clazz);

    @Select("with temp_piece_latest as(\n" +
            "\tselect project,bar_code,color\n" +
            "\t,ROW_NUMBER() over(partition by name ORDER BY time DESC) number1\n" +
            "\tfrom df_aoi_piece piece\n" +
            "\twhere piece.time BETWEEN '2023-08-01 01:00:00' AND '2023-09-01 01:00:00'\n" +
            ")\n" +
            ",temp_log_fqc_latest as(\n" +
            "\tselect log.qc_result,log.qc_time,user.factory_id,protect.line_boby_id,pieceLatest.project,qc_user_name,qc_user_code,log.bar_code,color,classCategory\n" +
            "\t,ROW_NUMBER() over(partition by log.bar_code ORDER BY qc_time DESC) number2\n" +
            "\tfrom temp_piece_latest pieceLatest\n" +
            "\tJOIN df_aoi_decide_log log on pieceLatest.bar_code = log.bar_code\n" +
            "\tJOIN user ON log.qc_user_code = user.name\n" +
            "\tJOIN df_aoi_seat_protect protect ON user.name = protect.user_id\n" +
            "\twhere log.qc_time BETWEEN '2023-08-01 01:00:00' AND '2023-09-01 01:00:00'\n" +
            "\tand number1 = 1 \n" +
            "\t#and user.factory_id = ''\n" +
            "\t#and pieceLatest.project =''\n" +
            "\t#and line_boby_id = ''\n" +
            "\tand user.process = 'FQC'\n" +
            ")\n" +
            ",temp_piece_oqcEmp_latest as(\n" +
            "\tselect log.qc_result,log.qc_time,qc_user_name,qc_user_code,log.bar_code\n" +
            "\t,ROW_NUMBER() over(partition by bar_code ORDER BY qc_time DESC) number3\n" +
            "\tFROM temp_piece_latest pieceLatest \n" +
            "\tJOIN df_aoi_decide_log log on pieceLatest.bar_code = log.bar_code\n" +
            "\tJOIN user ON log.qc_user_code = user.name\n" +
            "\tJOIN df_aoi_seat_protect protect ON user.name = protect.user_id\n" +
            "\twhere log.qc_time BETWEEN '2023-08-01 01:00:00' AND '2023-09-01 01:00:00'\n" +
            "\tand number1 = 1 \n" +
            "\t#and user.factory_id = ''\n" +
            "\t#and pieceLatest.project = ''\n" +
            "\t#and line_boby_id = ''\n" +
            "\tand user.process = 'OQC'\n" +
            "\tand user.name = '1218849'\n" +
            ")\n" +
            ",temp_result1 as(\n" +
            "\tSELECT \n" +
            "\tDATE_FORMAT(DATE_SUB(fqcLatest.qc_time,INTERVAL 7 HOUR),'%Y-%m-%d') date\n" +
            "\t,max(fqcLatest.factory_id) factoryId\n" +
            "\t,max(fqcLatest.line_boby_id) lineBobyId\n" +
            "\t,max(fqcLatest.project) project\n" +
            "\t,max(fqcLatest.qc_user_name) fqcEmpName\n" +
            "\t,fqcLatest.qc_user_code fqcEmpCode\n" +
            "\t,max(oqcEmpLatest.qc_user_name) oqcEmpName\n" +
            "\t,max(oqcEmpLatest.qc_user_code) oqcEmpCode\n" +
            "\t,sum(if(fqcLatest.qc_result = 'OK',1,0)) fqcTotal\n" +
            "\t,count(*) oqcTotal\n" +
            "\t,sum(if(oqcEmpLatest.qc_result = 'NG',1,0)) oqcNg\n" +
            "\t,max(color) color\n" +
            "\t,max(classCategory) classCategory\n" +
            "\tFROM temp_log_fqc_latest fqcLatest\n" +
            "\tLEFT JOIN temp_piece_oqcEmp_latest oqcEmpLatest ON fqcLatest.bar_code = oqcEmpLatest.bar_code\n" +
            "\twhere number2 = 1 and number3 = 1\n" +
            "\tGROUP BY date,fqcLatest.qc_user_code\n" +
            "\tORDER BY date asc\n" +
            ")\n" +
            ",temp_batch as(\n" +
            "\tSELECT \n" +
            "\tDATE_FORMAT(DATE_SUB(batch.create_time,INTERVAL 7 HOUR),'%Y-%m-%d') date\n" +
            "\t,check_user\n" +
            "\t,count(*) approval\n" +
            "\t,sum(if(batch.result = 'OK',1,0)) okBatch\n" +
            "\t,sum(if(batch.result = 'OK',1,0)) / count(*) okPass\n" +
            "\t,sum(total_count) feednumber\n" +
            "\t,sum(if(batch.result = 'OK',1,0)) * 120 + sum(if(batch.result = 'NG',total_count,0)) qPass\n" +
            "\t,sum(total_count) + sum(ng_count) checkCount\n" +
            "\t,sum(total_count) okSum\n" +
            "\t,sum(total_count) / sum(total_count) + sum(ng_count) okRate\n" +
            "\t,sum(ng_count) ngSum\n" +
            "\tfrom df_aoi_batch batch\n" +
            "\tLEFT JOIN df_aoi_batch_detail detail on  batch.id = detail.batch_id\n" +
            "\tGROUP BY date,check_user\n" +
            ")\n" +
            "\n" +
            "select \n" +
            "temp_result1.date\n" +
            ",classCategory\n" +
            ",temp_result1.factoryId\n" +
            ",CONCAT(temp_result1.project,temp_result1.color) type\n" +
            ",temp_result1.fqcEmpCode\n" +
            ",temp_result1.oqcEmpCode\n" +
            ",temp_batch.approval\n" +
            ",temp_batch.okBatch\n" +
            ",temp_batch.okPass\n" +
            ",temp_batch.feednumber\n" +
            ",temp_batch.qPass\n" +
            ",temp_batch.checkCount\n" +
            ",temp_batch.okRate\n" +
            ",temp_batch.ngSum\n" +
            "from temp_result1 \n" +
            "JOIN temp_batch on temp_result1.date = temp_batch.date and temp_result1.oqcEmpcode = temp_batch.check_user")
    List<Map<String, Object>> OQC31Report(QueryWrapper<DfAoiPiece> qw, String startTime, String endTime, String empCode);

    @Select("with temp_piece_latest as(\n" +
            "\tselect project,bar_code,color\n" +
            "\t,ROW_NUMBER() over(partition by name ORDER BY time DESC) number1\n" +
            "\tfrom df_aoi_piece piece\n" +
            "\twhere piece.time BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            ",temp_log_latest as(\n" +
            "\tselect machine_code,bar_code\n" +
            "\t,ROW_NUMBER() over(partition by bar_code ORDER BY create_time DESC) number1\n" +
            "\tfrom df_aoi_decide_log log\n" +
            "\twhere log.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            ",temp_result as(\n" +
            "\tfrom df_aoi_undetected undetected\n" +
            "\tJOIN df_aoi_defect defect on undetected.defectid = defect.defectid\n" +
            "\tJOIN temp_piece_latest piece on undetected.barcode = piece.bar_code\n" +
            "\tJOIN df_aoi_seat_protect protect on undetected.fqc_user = protect.user_id\n" +
            "\tJOIN temp_log_latest log on undetected.barcode = log.bar_code\n" +
            "${ew.customSqlSegment} " +
            "AND protect.classCategory = #{clazz}\n" +
            "\tGROUP BY log.machine_code\n" +
            ")\n" +
            "select machine_code str1,count(*)/sum(count) dou1\n" +
            "from temp_result\n" +
            "GROUP BY machine_code " +
            "order by dou1 limit 10")
    List<Rate3> lineBodylossByMachine(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime,
                                      @Param("clazz") String clazz);

    @Select("with temp_piece as(\n" +
            "\tselect *\n" +
            "\t,ROW_NUMBER() over(partition by name ORDER BY time DESC) number1\n" +
            "\tfrom df_aoi_piece piece\n" +
            "\twhere time BETWEEN #{startTime} AND #{endTime}\n" +
            ")\n" +
            ",temp_fqc as(\n" +
            "\tselect qc_user_code,count(*) total\n" +
            "\tfrom df_aoi_decide_log log\n" +
            "\tJOIN user ON log.qc_user_code = user.name\n" +
            "\twhere log.qc_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\tand user.process = 'FQC'\n" +
            "\tGROUP BY qc_user_code\n" +
            " )\n" +
            ",temp_count as(\n" +
            "\tselect fqc_user,alias,featurevalues,count(*) count\n" +
            "\tfrom df_aoi_undetected undetected\n" +
            "\tjoin df_aoi_defect defect on undetected.defectid = defect.defectid\n" +
            "\tJOIN temp_piece piece on undetected.barcode = piece.bar_code\n" +
            "\tJOIN df_aoi_seat_protect protect on undetected.fqc_user = protect.user_id\n" +
            "\tJOIN user on undetected.fqc_user = user.name\n" +
            "${ew.customSqlSegment}\n" +
            "\tand user.process = 'FQC'\n" +
            "and protect.classCategory = #{clazz}\n"+
            "\tGROUP BY undetected.fqc_user,user.alias,defect.featurevalues\n" +
            "\tORDER BY fqc_user\n" +
            ")\n" +
            ",temp_total as(\n" +
            "\tselect *,sum(count) over(PARTITION by fqc_user) total\n" +
            "\tfrom temp_count\n" +
            ")\n" +
            ",temp_rk as (\n" +
            "\tselect *,DENSE_RANK() over(order by total desc,fqc_user asc)  as rk\n" +
            "\tfrom temp_total\n" +
            ")\n" +
            "select alias str1,featurevalues str2,count inte1,temp_fqc.total inte2,count/temp_fqc.total dou1\n" +
            "FROM temp_rk\n" +
            "JOIN temp_fqc on temp_fqc.qc_user_code = temp_rk.fqc_user\n" +
            "where rk <= 10")
    List<Rate3> lossEmpTop10(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime,
                             @Param("clazz") String clazz);

    @Select("WITH temp_piece_latest AS (\n" +
            "\tSELECT\n" +
            "\t\tproject,\n" +
            "\t\tbar_code,\n" +
            "\t\tcolor,\n" +
            "\t\tROW_NUMBER() over ( PARTITION BY NAME ORDER BY time DESC ) number1 \n" +
            "\tFROM\n" +
            "\t\tdf_aoi_piece piece \n" +
            "\tWHERE\n" +
            "\t\tpiece.time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t),\n" +
            "\ttemp_log_latest AS (\n" +
            "\tSELECT\n" +
            "\t\tmachine_code,\n" +
            "\t\tbar_code,\n" +
            "\t\tROW_NUMBER() over ( PARTITION BY bar_code ORDER BY create_time DESC ) number1 \n" +
            "\tFROM\n" +
            "\t\tdf_aoi_decide_log log \n" +
            "\tWHERE\n" +
            "\t\tlog.create_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t),\n" +
            "\ttemp_result AS (\n" +
            "\tSELECT\n" +
            "\t\tlog.machine_code str1,\n" +
            "\t\tfeaturevalues str2,\n" +
            "\t\tcount(defect.id) count\n" +
            "\tFROM\n" +
            "\t\tdf_aoi_undetected undetected\n" +
            "\t\tJOIN df_aoi_defect defect ON undetected.defectid = defect.defectid\n" +
            "\t\tJOIN df_aoi_seat_protect protect ON undetected.fqc_user = protect.user_id\n" +
            "\t\tJOIN temp_piece_latest piece ON undetected.barcode = piece.bar_code\n" +
            "\t\tRIGHT JOIN temp_log_latest log ON undetected.barcode = log.bar_code \n" +
            "JOIN USER ON undetected.fqc_user = USER.NAME \n"+
            "${ew.customSqlSegment}\n" +
            "\tand user.process = 'FQC'\n" +
            "\t\tAND protect.classCategory = #{clazz} \n" +
            "\tGROUP BY\n" +
            "\t\tlog.machine_code,\n" +
            "\t\tdefect.featurevalues \n" +
            "\tORDER BY\n" +
            "\t\tmachine_code,\n" +
            "\t\tfeaturevalues \n" +
            "\t) ,\n" +
            "temp_machine as(\n" +
            "\tSELECT\n" +
            "\t\tmachine_code,\n" +
            "\t\tcount(*) total \n" +
            "\tFROM\n" +
            "\t\tdf_aoi_decide_log log\n" +
            "\t\tJOIN USER ON log.qc_user_code = USER.NAME \n" +
            "\tWHERE\n" +
            "\t\tlog.qc_time BETWEEN #{startTime} AND #{endTime}\n" +
            "\t\tand USER.process = 'FQC' \n" +
            "\tGROUP BY\n" +
            "\t\tmachine_code \n" +
            ")\t,\n" +
            "temp_ng_total as(\n" +
            "\tselect * ,sum(count) over(PARTITION by str1) ngTotal\n" +
            "\tfrom temp_result\n" +
            ")\n" +
            ",temp_rank as(\n" +
            "\tselect *,ngTotal/total rate,count/total rate1,DENSE_RANK() over(ORDER BY ngTotal/total desc,str1 asc) rk\n" +
            "\tfrom temp_ng_total \n" +
            "\tJOIN temp_machine on temp_ng_total.str1 = temp_machine.machine_code\n" +
            ")\n" +
            "select str1,str2,rate1 dou1\n" +
            "from temp_rank \n" +
            "where rk <= 10")
    List<Rate3> lineBodylossV2(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> qw,
                               @Param("startTime") String startTime,
                               @Param("endTime") String endTime,
                               @Param("clazz") String clazz);

    @Select("WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            " ${ew.customSqlSegment} \n" +
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
            "\n" +
            "SELECT hour(TIME) + 1 as hour , count(*) total ,sum(if(qc_result = 'OK',1,0)) ok, sum(if(qc_result = 'OK',1,0)) / count(*) rate\n" +
            "from temp_piece_latest\n" +
            "JOIN temp_fqc_latest ON temp_piece_latest.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "GROUP BY hour\n" +
            "ORDER BY hour")
    List<Map> fqcInputOut(@Param("ew") QueryWrapper<DfAoiPiece> ew);

    @Select("with temp_piece_latest as (\n" +
            "    select *,row_number() over (partition by bar_code order by create_time desc )  no\n" +
            "    from df_aoi_piece\n" +
            ")\n" +
            ",temp_oqc_latest as (\n" +
            "    select df_aoi_decide_log.*,user.process,row_number() over (partition by bar_code order by qc_time desc) no\n" +
            "    from df_aoi_decide_log\n" +
            "    join user on df_aoi_decide_log.qc_user_code = user.name\n" +
            "    and user.process = 'OQC'\n" +
            ")\n" +
            ",temp_fqc_latest as (\n" +
            "    select df_aoi_decide_log.*,user.process,row_number() over (partition by bar_code order by qc_time desc) no\n" +
            "    from df_aoi_decide_log\n" +
            "    join user on df_aoi_decide_log.qc_user_code = user.name\n" +
            "    and user.process = 'FQC'\n" +
            ")\n" +
            "-- fqc检测总数\n" +
            ",tem_emp_total as(\n" +
            "    select temp_fqc_latest.QC_USER_CODE,count(temp_fqc_latest.QC_USER_CODE)fqcTotal\n" +
            "    FROM temp_fqc_latest\n" +
            "    left JOIN temp_piece_latest on temp_piece_latest.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "${ew.customSqlSegment} " +
            "    group BY temp_fqc_latest.QC_USER_CODE\n" +
            ")\n" +
            ",temp_feature as(\n" +
            "    SELECT temp_fqc_latest.QC_USER_CODE,temp_fqc_latest.QC_USER_NAME,FEATUREVALUES,\n" +
            "       count(temp_fqc_latest.QC_USER_CODE)  single,\n" +
            "       row_number() OVER (PARTITION BY temp_fqc_latest.QC_USER_CODE,\n" +
            "           FEATUREVALUES ORDER BY count(temp_fqc_latest.QC_USER_CODE) desc) rk,\n" +
            "       sum(count(temp_fqc_latest.QC_USER_CODE)) over(PARTITION BY temp_fqc_latest.QC_USER_CODE) ngtotal\n" +
            "    FROM DF_AOI_UNDETECTED\n" +
            "    JOIN temp_piece_latest ON temp_piece_latest.BAR_CODE = DF_AOI_UNDETECTED.BARCODE\n" +
            "    JOIN  DF_AOI_DEFECT ON DF_AOI_UNDETECTED.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "    JOIN temp_fqc_latest on temp_fqc_latest.BAR_CODE = DF_AOI_UNDETECTED.BARCODE\n" +
            "    JOIN DF_AOI_SEAT_PROTECT on temp_fqc_latest.QC_USER_CODE = DF_AOI_SEAT_PROTECT.USER_ID\n" +
            "    where temp_piece_latest.no = 1 AND temp_fqc_latest.no = 1\n" +
            "    GROUP BY temp_fqc_latest.QC_USER_CODE,temp_fqc_latest.QC_USER_NAME,df_aoi_defect.FEATUREVALUES\n" +
            ")\n" +
            "select tem_emp_total.*,QC_USER_NAME,FEATUREVALUES,single,ngtotal,single/fqctotal rate\n" +
            "from tem_emp_total\n" +
            "join temp_feature on tem_emp_total.QC_USER_CODE=temp_feature.QC_USER_CODE\n" +
            "WHERE rk <= 5")
    List<Map<String, Object>> lossCheck(@Param("ew") QueryWrapper<DfAoiPiece> ew);

    @Select("WITH\n" +
            "    TEMP_UNDETECTED_LATEST AS (SELECT *,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
            "    FROM\n" +
            "    DF_AOI_UNDETECTED)\n" +
            "        ,\n" +
            "    TEMP_PIECE_LATEST AS (SELECT *,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY TIME DESC) NO\n" +
            "    FROM\n" +
            "    DF_AOI_PIECE PIECE)\n" +
            "\n" +
            "    SELECT\n" +
            "    FEATUREVALUES,\n" +
            "    COUNT(*)\n" +
            "FROM\n" +
            "    TEMP_UNDETECTED_LATEST\n" +
            "        JOIN DF_AOI_DEFECT ON TEMP_UNDETECTED_LATEST.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "        JOIN TEMP_PIECE_LATEST ON TEMP_PIECE_LATEST.BAR_CODE = TEMP_UNDETECTED_LATEST.BARCODE\n" +
            "${ew.customSqlSegment}" +
            "  AND TEMP_UNDETECTED_LATEST.NO = 1\n" +
            "  AND TEMP_PIECE_LATEST.NO = 1\n" +
            "GROUP BY\n" +
            "    FEATUREVALUES\n" +
            "ORDER BY\n" +
            "    COUNT(*) DESC\n" +
            "LIMIT 5")
	List<Map<String, String>> top5Feature(@Param("ew") QueryWrapper<DfAoiPiece> ew);

    @Select("with temp_undetected_latest as (\n" +
            "    select *,ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) no\n" +
            "    FROM DF_AOI_UNDETECTED\n" +
            ")\n" +
            ",TEMP_PIECE_latest AS (SELECT *,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY TIME DESC) no\n" +
            "    FROM\n" +
            "    DF_AOI_PIECE PIECE\n" +
            ")\n" +
            "\n" +
            ",temp_fqc_latest as (\n" +
            "select df_aoi_decide_log.*,user.alias,user.process,user.factory_id,row_number() over (partition by bar_code order by qc_time desc) no\n" +
            "from df_aoi_decide_log\n" +
            "left join user on df_aoi_decide_log.qc_user_code = user.name\n" +
            "where user.process = 'FQC'\n" +
            ")\n" +
            "\n" +
            ",temp_emp as(\n" +
            "    SELECT QC_USER_CODE,count(*) total\n" +
            "    FROM temp_piece_latest\n" +
            "    JOIN temp_fqc_latest ON temp_piece_latest.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "${ew.customSqlSegment}" +
            "    AND temp_fqc_latest.no = 1 AND  temp_piece_latest.no = 1\n" +
            "    GROUP BY QC_USER_CODE\n" +
            ")\n" +
            ",temp_feature as(\n" +
            "    select FQC_USER,user.ALIAS,FEATUREVALUES,count(*) feature\n" +
            "    FROM temp_piece_latest\n" +
            "    JOIN temp_undetected_latest ON temp_piece_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN DF_AOI_DEFECT ON  temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "     JOIN TEMP_FQC_LATEST ON TEMP_PIECE_LATEST.BAR_CODE = TEMP_FQC_LATEST.BAR_CODE\n" +
            "    JOIN user ON FQC_USER = user.NAME\n" +
            "${ew.customSqlSegment}" +
            "        and FEATUREVALUES = #{feature}\n" +
            "    AND temp_undetected_latest.no = 1 AND TEMP_PIECE_latest.no = 1\n" +
            "    group BY temp_undetected_latest.FQC_USER,user.ALIAS\n" +
            ")\n" +
            "SELECT ALIAS,feature/total rate\n" +
            "    from temp_emp LEFT JOIN temp_feature ON temp_emp.QC_USER_CODE = temp_feature.FQC_USER")
    List<Map<String, Object>> selectByfeature(@Param("ew") QueryWrapper<DfAoiPiece> ew2,@Param("feature") String feature);

    @Select("with temp_piece_latest as(\n" +
            "    select *,row_number() OVER (PARTITION BY BAR_CODE ORDER BY time DESC ) as no\n" +
            "    FROM DF_AOI_PIECE\n" +
            "${ew.customSqlSegment}" +
            ")\n" +
            "-- 一次良率,综合良率,最终良率\n" +
            ",temp_okRate as(\n" +
            "    SELECT machine,\n" +
            "       sum(if(qualityid = '0' and no = 1,1,0)) / sum(if(no = 1,1,0)) dou1,\n" +
            "       sum(if(qualityid = '0' and no != 1,1,0)) / sum(if(no != 1,1,0)) dou2,\n" +
            "       sum(if(qualityid = '0',1,0)) / count(*) dou3\n" +
            "    FROM temp_piece_latest\n" +
            "    GROUP BY MACHINE\n" +
            ")\n" +
            "-- 机台对应的FQC和投入和产出\n" +
            ",temp_fqc_latest as (\n" +
            "    select MACHINE,count(*) total,sum(if(QC_RESULT = 'OK',1,0)) ok\n" +
            "    from df_aoi_decide_log\n" +
            "    join user on df_aoi_decide_log.qc_user_code = user.name\n" +
            "    join temp_piece_latest ON DF_AOI_DECIDE_LOG.BAR_CODE = temp_piece_latest.BAR_CODE\n" +
            "    where user.process = 'FQC'\n" +
            "    AND temp_piece_latest.no = 1\n" +
            "    GROUP BY MACHINE\n" +
            ")\n" +
            "select temp_okRate.MACHINE str1,dou1,dou2,dou3,total inte1 , ok inte2\n" +
            "FROM temp_okRate\n" +
            "LEFT JOIN temp_fqc_latest ON temp_okrate.MACHINE = temp_fqc_latest.MACHINE\n" +
            "ORDER BY temp_okRate.MACHINE")
	List<Rate3> getDetailByMachine(@Param("ew") QueryWrapper<DfAoiPiece> ew);

    @Select("WITH\n" +
            "TEMP_UNDETECTED_LATEST AS (\n" +
            "    SELECT *,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
            "    FROM\n" +
            "    DF_AOI_UNDETECTED\n" +
            ")\n" +
            ",TEMP_PIECE_LATEST AS (\n" +
            "    SELECT *,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY TIME DESC) NO\n" +
            "    FROM\n" +
            "    DF_AOI_PIECE PIECE\n" +
            ")\n" +
            ",TEMP_FQC_LATEST AS (\n" +
            "SELECT\n" +
            "    DF_AOI_DECIDE_LOG.*,\n" +
            "    USER.ALIAS,\n" +
            "    USER.PROCESS,\n" +
            "    ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY QC_TIME DESC) NO\n" +
            "FROM\n" +
            "    DF_AOI_DECIDE_LOG\n" +
            "        LEFT JOIN USER ON DF_AOI_DECIDE_LOG.QC_USER_CODE = USER.NAME\n" +
            "WHERE\n" +
            "    USER.PROCESS = 'FQC'\n" +
            ")\n" +
            ",tmep_fqc_total AS (\n" +
            "    SELECT QC_USER_CODE,QC_USER_NAME,count(*) total\n" +
            "    from temp_fqc_latest\n" +
            "    JOIN TEMP_PIECE_LATEST ON temp_fqc_latest.BAR_CODE = temp_piece_latest.BAR_CODE\n" +
            "${ew.customSqlSegment}" +
            " and temp_fqc_latest.no = 1 AND temp_piece_latest.no = 1\n" +
            "    GROUP BY temp_fqc_latest.QC_USER_CODE,QC_USER_NAME\n" +
            ")\n" +
            ",temp_emp_feature AS (\n" +
            "    SELECT FQC_USER,FEATUREVALUES,count(*) count\n" +
            "    FROM temp_piece_latest\n" +
            "    JOIN TEMP_UNDETECTED_LATEST ON temp_piece_latest.BAR_CODE = TEMP_UNDETECTED_LATEST.BARCODE\n" +
            "    JOIN temp_fqc_latest ON temp_fqc_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN df_aoi_defect ON TEMP_UNDETECTED_LATEST.DEFECTID = df_aoi_defect.DEFECTID\n" +
            "${ew.customSqlSegment}" +
            "and temp_piece_latest.no = 1 AND TEMP_UNDETECTED_LATEST.no = 1 AND temp_fqc_latest.no = 1\n" +
            "    GROUP BY TEMP_UNDETECTED_LATEST.FQC_USER,FEATUREVALUES\n" +
            ")\n" +
            ",temp_result as(\n" +
            "    select *,sum(count) OVER (PARTITION BY QC_USER_NAME) count_sum\n" +
            "    FROM tmep_fqc_total\n" +
            "    LEFT JOIN temp_emp_feature ON tmep_fqc_total.QC_USER_CODE = temp_emp_feature.FQC_USER\n" +
            ")\n" +
            "SELECT QC_USER_CODE str1,QC_USER_NAME str2,FEATUREVALUES str3,count/total dou1 ,count_sum/total rate2\n" +
            "from temp_result\n" +
            "ORDER BY rate2 DESC")
    List<Rate3> empLossChemkRank(@Param("ew") QueryWrapper<DfAoiPiece> ew);


    @Select("select project\n" +
            "from \n" +
            "\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\tfrom df_aoi_piece dap \n" +
            "\t)dap_new\n" +
            "${ew.customSqlSegment} " +
            "group by project")
    List<String> getProjectList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);


    @Select("with dad_new as(\n" +
            "\tselect dad.check_id,dad.featurevalues,count(0) defectNumber\n" +
            "\tfrom df_aoi_defect dad\n" +
            "\tgroup by dad.check_id,dad.featurevalues \n" +
            ")\n" +
            ",dad_new2 as(\n" +
            "\tselect dad_new.featurevalues\n" +
            "\t,sum(if(dad_new.defectNumber>0,1,0)) defectNumber\n" +
            "\t,dap_new.project\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "\tinner join dad_new \n" +
            "\ton dad_new.check_id = dap_new.id\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dap_new.project,dad_new.featurevalues\n" +
            "\torder by defectNumber desc\n" +
            ")\n" +
            "select dad_new2.featurevalues str1" +
            ",project_new.project str2\n" +
            ",dad_new2.defectNumber inte1\n" +
            ",project_new.total inte2\n" +
            ",format(dad_new2.defectNumber/project_new.total*100,2) dou1\n" +
            "from dad_new2\n" +
            "left join \n" +
            "\t(select dap_new.project,count(0) total\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by dap_new.project\n" +
            "\t)project_new\n" +
            "on project_new.project = dad_new2.project")
    List<Rate3> getProjectDefectPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);


    @Select("with time_new as(\n" +
            "\tselect day(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_3.customSqlSegment} " +
            "\tgroup by day(c.datelist)\n" +
            "\torder by day(c.datelist)\n" +
            ")\n" +
            ",dap_new as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            ",defect_new as(\n" +
            "\tselect dad.check_id,dadc.name,dad.featurevalues,ddcp.defect_area,dad.area \n" +
            "\tfrom df_aoi_defect dad \n" +
            "\tleft join df_aoi_defect_class dadc \n" +
            "\ton dadc.code = dad.class_code \n" +
            "\tleft join df_defect_code_protect ddcp \n" +
            "\ton ddcp.defect_name  = dad.featurevalues \n" +
            ")\n" +
            ",piece_defect as(\n" +
            "\tselect *\n" +
            "\tfrom dap_new\n" +
            "\tinner join defect_new\n" +
            "\ton defect_new.check_id = dap_new.id\n" +
            "${ew_2.customSqlSegment} " +
            ")\n" +
            ",piece_defect_number as(\n" +
            "\tselect piece_defect.id,piece_defect.featurevalues,piece_defect.`time`,count(0)   \n" +
            "\tfrom piece_defect\n" +
            "\tgroup by piece_defect.id,piece_defect.featurevalues,piece_defect.`time`\n" +
            ")\n" +
            ",piece_number as(\n" +
            "\tselect day(date_sub(piece_defect_number.`time`,interval 7 hour)) checkTime,count(0) defectNumber\n" +
            "\tfrom piece_defect_number\n" +
            "\tgroup by day(date_sub(piece_defect_number.`time`,interval 7 hour)) \n" +
            "\torder by day(date_sub(piece_defect_number.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            ",piece_total as(\n" +
            "\tselect day(date_sub(dap_new.`time`,interval 7 hour)) checkTime,count(0) total \n" +
            "\tfrom dap_new\n" +
            "\tgroup by day(date_sub(dap_new.`time`,interval 7 hour)) \n" +
            "\torder by day(date_sub(dap_new.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            "select time_new.checkTime str1,piece_number.defectNumber,piece_total.total\n" +
            ",format(piece_number.defectNumber/piece_total.total*100,2) str2\n" +
            "from time_new \n" +
            "left join piece_number\n" +
            "on piece_number.checkTime = time_new.checkTime\n" +
            "left join piece_total\n" +
            "on piece_total.checkTime = time_new.checkTime")
    List<Rate3> getItemDefectPointListDay(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                          @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);


    @Select("with time_new as(\n" +
            "\tselect week(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_3.customSqlSegment} " +
            "\tgroup by week(c.datelist)\n" +
            "\torder by week(c.datelist)\n" +
            ")\n" +
            ",dap_new as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            ",defect_new as(\n" +
            "\tselect dad.check_id,dadc.name,dad.featurevalues,ddcp.defect_area,dad.area \n" +
            "\tfrom df_aoi_defect dad \n" +
            "\tleft join df_aoi_defect_class dadc \n" +
            "\ton dadc.code = dad.class_code \n" +
            "\tleft join df_defect_code_protect ddcp \n" +
            "\ton ddcp.defect_name  = dad.featurevalues \n" +
            ")\n" +
            ",piece_defect as(\n" +
            "\tselect *\n" +
            "\tfrom dap_new\n" +
            "\tinner join defect_new\n" +
            "\ton defect_new.check_id = dap_new.id\n" +
            "${ew_2.customSqlSegment} " +
            ")\n" +
            ",piece_defect_number as(\n" +
            "\tselect piece_defect.id,piece_defect.featurevalues,piece_defect.`time`,count(0)   \n" +
            "\tfrom piece_defect\n" +
            "\tgroup by piece_defect.id,piece_defect.featurevalues,piece_defect.`time`\n" +
            ")\n" +
            ",piece_number as(\n" +
            "\tselect week(date_sub(piece_defect_number.`time`,interval 7 hour)) checkTime,count(0) defectNumber\n" +
            "\tfrom piece_defect_number\n" +
            "\tgroup by week(date_sub(piece_defect_number.`time`,interval 7 hour)) \n" +
            "\torder by week(date_sub(piece_defect_number.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            ",piece_total as(\n" +
            "\tselect week(date_sub(dap_new.`time`,interval 7 hour)) checkTime,count(0) total \n" +
            "\tfrom dap_new\n" +
            "\tgroup by week(date_sub(dap_new.`time`,interval 7 hour)) \n" +
            "\torder by week(date_sub(dap_new.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            "select time_new.checkTime str1,piece_number.defectNumber,piece_total.total\n" +
            ",format(piece_number.defectNumber/piece_total.total*100,2) str2\n" +
            "from time_new \n" +
            "left join piece_number\n" +
            "on piece_number.checkTime = time_new.checkTime\n" +
            "left join piece_total\n" +
            "on piece_total.checkTime = time_new.checkTime")
    List<Rate3> getItemDefectPointListWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                          @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);


    @Select("with time_new as(\n" +
            "\tselect month(c.datelist) checkTime\n" +
            "\tfrom calendar c \n" +
            "${ew_3.customSqlSegment} " +
            "\tgroup by month(c.datelist)\n" +
            "\torder by month(c.datelist)\n" +
            ")\n" +
            ",dap_new as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`,dap.project,dap.color\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            ",defect_new as(\n" +
            "\tselect dad.check_id,dadc.name,dad.featurevalues,ddcp.defect_area,dad.area \n" +
            "\tfrom df_aoi_defect dad \n" +
            "\tleft join df_aoi_defect_class dadc \n" +
            "\ton dadc.code = dad.class_code \n" +
            "\tleft join df_defect_code_protect ddcp \n" +
            "\ton ddcp.defect_name  = dad.featurevalues \n" +
            ")\n" +
            ",piece_defect as(\n" +
            "\tselect *\n" +
            "\tfrom dap_new\n" +
            "\tinner join defect_new\n" +
            "\ton defect_new.check_id = dap_new.id\n" +
            "${ew_2.customSqlSegment} " +
            ")\n" +
            ",piece_defect_number as(\n" +
            "\tselect piece_defect.id,piece_defect.featurevalues,piece_defect.`time`,count(0)   \n" +
            "\tfrom piece_defect\n" +
            "\tgroup by piece_defect.id,piece_defect.featurevalues,piece_defect.`time`\n" +
            ")\n" +
            ",piece_number as(\n" +
            "\tselect month(date_sub(piece_defect_number.`time`,interval 7 hour)) checkTime,count(0) defectNumber\n" +
            "\tfrom piece_defect_number\n" +
            "\tgroup by month(date_sub(piece_defect_number.`time`,interval 7 hour)) \n" +
            "\torder by month(date_sub(piece_defect_number.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            ",piece_total as(\n" +
            "\tselect month(date_sub(dap_new.`time`,interval 7 hour)) checkTime,count(0) total \n" +
            "\tfrom dap_new\n" +
            "\tgroup by month(date_sub(dap_new.`time`,interval 7 hour)) \n" +
            "\torder by month(date_sub(dap_new.`time`,interval 7 hour)) asc\n" +
            ")\n" +
            "select time_new.checkTime str1,piece_number.defectNumber,piece_total.total\n" +
            ",format(piece_number.defectNumber/piece_total.total*100,2) str2\n" +
            "from time_new \n" +
            "left join piece_number\n" +
            "on piece_number.checkTime = time_new.checkTime\n" +
            "left join piece_total\n" +
            "on piece_total.checkTime = time_new.checkTime")
    List<Rate3> getItemDefectPointListMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper,
                                          @Param("ew_2")Wrapper<DfAoiPiece> wrapper2,@Param("ew_3")Wrapper<String> wrapper3);

    @Select("select dadc.name  item2\n" +
            "from df_aoi_defect_class dadc ;")
    List<String> getAllDefectClassList();

    @Select("select dad.featurevalues item2\n" +
            "from df_aoi_defect dad \n" +
            "group by dad.featurevalues;")
    List<String> getAllDefectNameList();

    @Select("select ddcp.defect_area item2\n" +
            "from df_defect_code_protect ddcp \n" +
            "group by ddcp.defect_area ")
    List<String> getAllDefectAreaList();

    @Select("select dad.area item2\n" +
            "from df_aoi_defect dad \n" +
            "group by dad.area")
    List<String> getAllDefectPosition();

    @Select("WITH temp_piece AS (\n" +
            "   SELECT *,row_number() OVER (PARTITION BY BAR_CODE ORDER BY time) no\n" +
            "    from DF_AOI_PIECE\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            "SELECT date_format(TIME,'%Y/%m/%d') date,hour(TIME) hour,count(*) total,\n" +
            "       sum(if(no != 1,1,0)) backTotal,sum(if(no != 1 AND QUALITYID NOT IN ('101','102','103','104','105'),1,0)) backTotalNotinTrace,\n" +
            "       sum(if(QUALITYID = '0' AND no = 1,1,0)) oneOk,sum(if(QUALITYID = '0',1,0)) totalOk,\n" +
            "       sum(if(QUALITYID = '1',1,0)) totalNG,sum(if(no >5,1,0)) more5,0 AS blockNg," +
            "       sum(if(QUALITYID = '103',1,0)) as missing,\n" +
            "       sum(if(QUALITYID = '0',1,0))/sum(if(QUALITYID NOT in ('101','102','103','104','105'),1,0)) okRate,\n" +
            "       sum(if(QUALITYID = '1',1,0))/sum(if(QUALITYID NOT in ('101','102','103','104','105'),1,0)) ngRate," +
            "       sum(if(QUALITYID IN ('101','102','103','104','105'),1,0)) aoiUnknown\n" +
            "from temp_piece\n" +
            "GROUP BY date,hour\n" +
            "ORDER BY date,hour")
    List<Map<String, Object>> getAoiReportByHour(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> ew);

    @Select("WITH temp_piece AS (\n" +
            "   SELECT *,row_number() OVER (PARTITION BY BAR_CODE ORDER BY time) no\n" +
            "    from DF_AOI_PIECE\n" +
            "${ew.customSqlSegment} " +
            ")\n" +
            "SELECT date_format(TIME,'%Y/%m/%d') date,count(*) total,\n" +
            "       sum(if(no != 1,1,0)) backTotal,sum(if(no != 1 AND QUALITYID NOT IN ('101','102','103','104','105'),1,0)) backTotalNotinTrace,\n" +
            "       sum(if(QUALITYID = '0' AND no = 1,1,0)) oneOk,sum(if(QUALITYID = '0',1,0)) totalOk,\n" +
            "       sum(if(QUALITYID = '1',1,0)) totalNG,sum(if(no >5,1,0)) more5,0 AS blockNg,0 as missing,\n" +
            "       sum(if(QUALITYID = '0',1,0))/sum(if(QUALITYID NOT in ('101','102','103','104','105'),1,0)) okRate,\n" +
            "       sum(if(QUALITYID = '1',1,0))/sum(if(QUALITYID NOT in ('101','102','103','104','105'),1,0)) ngRate\n" +
            "from temp_piece\n" +
            "GROUP BY date\n" +
            "ORDER BY date")
    List<Map<String, Object>> getAoiReportByDay(@Param(Constants.WRAPPER) QueryWrapper<DfAoiPiece> ew);

    @Select("with dap_new as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dap.id,dap.bar_code,dap.factory,dap.`time`\n" +
            "\t\t,dap.project,dap.color,dap.machine,dap.line_body,dap.qualityid\n" +
            "\t\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\t\tfrom df_aoi_piece dap \n" +
            "\t\t)dap_new\n" +
            "\twhere num = 1\n" +
            "\tand dap_new.`time`between date_sub(now(),interval 1 hour) and now()  \n" +
            ")\n" +
            "select \n" +
            "dap_new.machine str1\n" +
            ",dap_new.line_body str2" +
            ",sum(if(dap_new.qualityid=0,1,0)) inte1 \n" +
            ",count(0) inte2 \n" +
            ",format(sum(if(dap_new.qualityid=0,1,0))/count(0)*100,2) dou1 \n" +
            "from dap_new\n" +
            "group by dap_new.machine,dap_new.line_body ")
    List<Rate3> getMachinePassPointHour();

    @Select("select dap_new.project\n" +
            "from \n" +
            "\t(select dap.id,dap.bar_code,dap.factory,dap.`time`\n" +
            "\t,dap.project,dap.color,dap.machine,dap.qualityid\n" +
            "\t,row_number()over(partition by dap.bar_code order by dap.`time` desc) num \n" +
            "\tfrom df_aoi_piece dap \n" +
            "${ew.customSqlSegment} " +
            "\t)dap_new\n" +
            "where num = 1\n" +
            "and dap_new.`time`between date_sub(now(),interval 1 hour) and now() \n" +
            "group by dap_new.project")
    List<String> getProjectByMachine(@Param(Constants.WRAPPER)Wrapper<DfAoiPiece> wrapper);

    @Select("with dadl_FQC as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name ,\n" +
            "\t\trow_number()over(partition by dadl.bar_code order by dadl .qc_time desc) num\n" +
            "\t\tfrom df_aoi_decide_log dadl\n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = dadl.qc_user_code\n" +
            "\t\twhere u.process = 'FQC'\n" +
            "\t\t)dadl_new\n" +
            "\twhere dadl_new.num = 1\n" +
            "\tand dadl_new.qc_time between date_sub(now(),interval 1 hour) and now()  \n" +
            ")\n" +
            ",dadl_OQC as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dadl.bar_code ,dadl.qc_time ,dadl.qc_user_code ,dadl.qc_user_name ,\n" +
            "\t\trow_number()over(partition by dadl.bar_code order by dadl .qc_time desc) num\n" +
            "\t\tfrom df_aoi_decide_log dadl\n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name = dadl.qc_user_code\n" +
            "\t\twhere u.process = 'OQC'\n" +
            "\t\t)dadl_new\n" +
            "\twhere dadl_new.num = 1\n" +
            "\tand dadl_new.qc_time between date_sub(now(),interval 1 hour) and now()  \n" +
            ")\n" +
            ",dadl_OQC_check as(\n" +
            "\tselect dadl_FQC.qc_user_code userCode,count(0) oqcCheckNumber \n" +
            "\tfrom dadl_FQC\n" +
            "\tinner join dadl_OQC\n" +
            "\ton dadl_OQC.bar_code = dadl_FQC.bar_code\n" +
            "\tgroup by dadl_FQC.qc_user_code\n" +
            ")\n" +
            ",dau_escape as(\n" +
            "\tselect dau_new.fqc_user userCode,count(0) escapeNumber\n" +
            "\tfrom \n" +
            "\t\t(select dau.*\n" +
            "\t\t,row_number()over(partition by dau.barcode order by dau.create_time desc) num\n" +
            "\t\tfrom df_aoi_undetected dau\n" +
            "\t\t)dau_new\n" +
            "\twhere dau_new.num = 1\n" +
            "\tand dau_new.create_time between date_sub(now(),interval 1 hour) and now() \n" +
            "\tgroup by dau_new.fqc_user\n" +
            ")\n" +
            "select \n" +
            "dau_escape.userCode str1\n" +
            ",lb.name str2\n" +
            ",damp.machine_code str3\n" +
            ",dau_escape.escapeNumber inte1\n" +
            ",dadl_OQC_check.oqcCheckNumber inte2\n" +
            ",format(dau_escape.escapeNumber/dadl_OQC_check.oqcCheckNumber*100,2) dou1 \n" +
            "from dau_escape\n" +
            "left join dadl_OQC_check\n" +
            "on dau_escape.userCode = dadl_OQC_check.userCode\n" +
            "left join `user` u\n" +
            "on u.name = dau_escape.userCode\n" +
            "left join df_aoi_seat_protect dasp\n" +
            "on dasp.user_id = u.id \n" +
            "left join line_body lb \n" +
            "on lb.id = dasp.line_boby_id \n" +
            "left join df_aoi_machine_protect damp\n" +
            "on damp.user_code = dau_escape.userCode ")
    List<Rate3> getFqcEscapePointHour();

    @Select("with dau_escape as(\n" +
            "\tselect *\n" +
            "\tfrom \n" +
            "\t\t(select dau.*\n" +
            "\t\t,row_number()over(partition by dau.barcode order by dau.create_time desc) num\n" +
            "\t\tfrom df_aoi_undetected dau\n" +
            "${ew.customSqlSegment} " +
            "\t\t)dau_new\n" +
            "\twhere dau_new.num = 1\n" +
            "\tand dau_new.create_time between date_sub(now(),interval 1 hour) and now() \n" +
            ")\n" +
            "select dap.project \n" +
            "from dau_escape\n" +
            "left join df_aoi_piece dap \n" +
            "on dap.bar_code = dau_escape.barcode\n" +
            "group by dap.project")
    List<String> getProjectByFqc(@Param(Constants.WRAPPER)Wrapper<DfAoiUndetected> wrapper);

    @Select("\n" +
            "with temp_piece as(\n" +
            "    select *\n" +
            "    FROM (\n" +
            "        select *,row_number() OVER (PARTITION BY BAR_CODE ORDER BY create_time DESC ) no\n" +
            "        from DF_AOI_PIECE\n" +
            "    ) t\n" +
            "    where no = 1\n" +
            ")\n" +
            "\n" +
            ",temp_fqc_latest AS (\n" +
            "   SELECT * FROM (\n" +
            "        select DF_AOI_DECIDE_LOG.*,user.ALIAS,row_number() OVER (PARTITION BY BAR_CODE ORDER BY QC_TIME DESC ) no\n" +
            "        from DF_AOI_DECIDE_LOG\n" +
            "        JOIN user ON DF_AOI_DECIDE_LOG.QC_USER_CODE = user.NAME\n" +
            "        where user.PROCESS = 'FQC'\n" +
            "   ) AS t\n" +
            "   WHERE no = 1\n" +
            ")\n" +
            "\n" +
            ",temp_oqc_latest AS (\n" +
            "    SELECT * FROM (\n" +
            "        select DF_AOI_DECIDE_LOG.*,user.ALIAS,row_number() OVER (PARTITION BY BAR_CODE ORDER BY QC_TIME DESC ) no\n" +
            "        from DF_AOI_DECIDE_LOG\n" +
            "        JOIN user ON DF_AOI_DECIDE_LOG.QC_USER_CODE = user.NAME\n" +
            "        where user.PROCESS = 'OQC'\n" +
            "    ) as t\n" +
            "    WHERE no = 1\n" +
            ")\n" +
            "\n" +
            ",temp_groupby as(\n" +
            "    select\n" +
            "    DATE_FORMAT(DATE_SUB(temp_oqc_latest.QC_TIME,INTERVAL 7 HOUR),'%m-%d') date,\n" +
            "    CASE when hour(DATE_SUB(temp_oqc_latest.QC_TIME, INTERVAL 7 HOUR)) BETWEEN 0 AND  11 THEN '白班' ELSE '晚班' END clazz,\n" +
            "    temp_piece.FACTORY,\n" +
            "    temp_piece.COLOR,\n" +
            "    temp_fqc_latest.QC_USER_CODE fqc,\n" +
            "    temp_oqc_latest.QC_USER_CODE oqc,\n" +
            "    df_aoi_batch.id batchID,\n" +
            "    DF_AOI_BATCH.TOTAL_COUNT,\n" +
            "    DF_AOI_BATCH.NG_COUNT,\n" +
            "    DF_AOI_BATCH.RESULT as ret\n" +
            "    ${beforesql}\n" +
            "    from temp_piece\n" +
            "    JOIN temp_fqc_latest on temp_piece.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "    left JOIN DF_AOI_BATCH_DETAIL on DF_AOI_BATCH_DETAIL.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "    LEFT JOIN df_aoi_batch on DF_AOI_BATCH.id = DF_AOI_BATCH_DETAIL.BATCH_ID\n" +
            "    left JOIN temp_oqc_latest ON  temp_oqc_latest.BAR_CODE = temp_fqc_latest.BAR_CODE\n" +
            "    left join DF_AOI_DEFECT ON temp_oqc_latest.DEFECT_ID = DF_AOI_DEFECT.DEFECTID\n" +
            "${ew.customSqlSegment}\n" +
            "    GROUP BY date,clazz,FACTORY,COLOR,fqc,oqc,batchID,TOTAL_COUNT,NG_COUNT,df_aoi_batch.RESULT,FEATUREVALUES\n" +
            "    order by date,clazz,FACTORY,COLOR,fqc,oqc,batchID\n" +
            ")\n" +
            "\n" +
            "select date,clazz,FACTORY,COLOR,fqc,oqc,\n" +
            "       count(batchid) batchCount,\n" +
            "       sum(if(ret = 'OK',1,0)) okCount, \n" +
            "       sum(if(ret = 'OK',1,0))/count(batchid) pass,\n" +
            "       120 * count(batchid) feedNumber,\n" +
            "       sum(if(ret = 'OK',1,0)) * 120 + sum(if(ret != 'OK',total_count,0)) okNum,\n" +
            "       30 * count(batchid) checkNumber,\n" +
            "       sum(total_count) total_count,\n" +
            "       (sum(if(ret = 'OK',1,0)) * 120 + sum(if(ret != 'OK',total_count,0))) / 120 * count(batchid) okRate,\n" +
            "       sum(NG_COUNT) NG_COUNT\n" +
			"    ${afterSql}\n" +
            "from temp_groupby\n" +
            "GROUP BY date,clazz,FACTORY,COLOR,fqc,oqc;")
	List<Map<String, String>> listOQCReport(@Param("ew") QueryWrapper<DfAoiPiece> dfAoiPieceQueryWrapper, String beforesql, String afterSql);

    @Select("WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            "${ew.customSqlSegment}\n" +
            ")\n" +
            " ,temp_undetected_latest AS (\n" +
            "     select * FROM (\n" +
            "        select *,ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
            "        from DF_AOI_UNDETECTED\n" +
            "     )t\n" +
            "     WHERE no = 1\n" +
            ")\n" +
            "-- fqc员工漏检总数\n" +
            ",temp_undetected_userTotal as(\n" +
            "    select FQC_USER , ALIAS,count(0) total\n" +
            "    from temp_undetected_latest\n" +
            "    JOIN temp_piece_latest ON  temp_undetected_latest.BARCODE = temp_piece_latest.BAR_CODE\n" +
            "    JOIN USER ON user.NAME = temp_undetected_latest.FQC_USER\n" +
            "    GROUP BY FQC_USER,user.ALIAS\n" +
            ")\n" +
            "-- fqc员工单项漏检署\n" +
            ",temp_undetected_featurevalues as(\n" +
            "    select FEATUREVALUES,FQC_USER,count(*) as num\n" +
            "    from temp_piece_latest\n" +
            "    JOIN temp_undetected_latest ON  temp_piece_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN DF_AOI_DEFECT ON  temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "    WHERE FEATUREVALUES = #{feature}\n" +
            "    group by temp_undetected_latest.FQC_USER\n" +
            ")\n" +
            "select FEATUREVALUES str1,ALIAS str2, num/total as dou1\n" +
            "from temp_undetected_userTotal\n" +
            "JOIN temp_undetected_featurevalues ON temp_undetected_userTotal.FQC_USER = temp_undetected_featurevalues.FQC_USER")
    List<Rate3> empLossChemkRankV2(@Param("ew") QueryWrapper<DfAoiDefect> ew,@Param("feature") String feature);

    @Select("WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            "${ew.customSqlSegment}\n" +
            ")\n" +
            " ,temp_undetected_latest AS (\n" +
            "     select * FROM (\n" +
            "        select *,ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
            "        from DF_AOI_UNDETECTED\n" +
            "     )t\n" +
            "     WHERE no = 1\n" +
            ")\n" +
            "-- fqc员工漏检总数\n" +
            ",temp_undetected_userTotal as(\n" +
            "    select FQC_USER , ALIAS,count(0) total\n" +
            "    from temp_undetected_latest\n" +
            "    JOIN temp_piece_latest ON  temp_undetected_latest.BARCODE = temp_piece_latest.BAR_CODE\n" +
            "    JOIN USER ON user.NAME = temp_undetected_latest.FQC_USER\n" +
            "    GROUP BY FQC_USER,user.ALIAS\n" +
            ")\n" +
            "-- fqc员工TOP5漏检数\n" +
            ",temp_undetected_featurevalues as(\n" +
            "    select FQC_USER,count(*) as num\n" +
            "    from temp_piece_latest\n" +
            "    JOIN temp_undetected_latest ON  temp_piece_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN DF_AOI_DEFECT ON  temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "${ew_2.customSqlSegment}\n" +
            "    group by temp_undetected_latest.FQC_USER\n" +
            ")\n" +
            "select ALIAS str1,num/total rate \n" +
            "from temp_undetected_userTotal\n" +
            "JOIN temp_undetected_featurevalues ON  temp_undetected_userTotal.FQC_USER = temp_undetected_featurevalues.FQC_USER\n" +
            "ORDER BY rate desc")
    List<Rate3> empTopFeaRateDesc(@Param("ew") QueryWrapper<DfAoiDefect> ew,@Param("ew_2") QueryWrapper<DfAoiDefect> ew2);

    @Select("WITH TEMP_PIECE_LATEST AS (\n" +
            "        select * from (\n" +
            "         SELECT *, ROW_NUMBER() OVER (PARTITION BY BAR_CODE ORDER BY CREATE_TIME DESC ) NO\n" +
            "         FROM DF_AOI_PIECE\n" +
            "        )t\n" +
            "${ew.customSqlSegment}\n" +
            ")\n" +
            " ,temp_undetected_latest AS (\n" +
            "     select * FROM (\n" +
            "        select *,ROW_NUMBER() OVER (PARTITION BY BARCODE ORDER BY CREATE_TIME DESC) NO\n" +
            "        from DF_AOI_UNDETECTED\n" +
            "     )t\n" +
            "     WHERE no = 1\n" +
            ")\n" +
            "-- fqc员工漏检总数\n" +
            ",temp_undetected_userTotal as(\n" +
            "    select FQC_USER , ALIAS,count(0) total\n" +
            "    from temp_undetected_latest\n" +
            "    JOIN temp_piece_latest ON  temp_undetected_latest.BARCODE = temp_piece_latest.BAR_CODE\n" +
            "    JOIN USER ON user.NAME = temp_undetected_latest.FQC_USER\n" +
            "    GROUP BY FQC_USER,user.ALIAS\n" +
            ")\n" +
            "-- fqc员工TOP5漏检数\n" +
            ",temp_undetected_featurevalues as(\n" +
            "    select FQC_USER,alias,count(*) as total\n" +
            "    from temp_piece_latest\n" +
            "    JOIN temp_undetected_latest ON  temp_piece_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN DF_AOI_DEFECT ON  temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "    JOIN user ON  FQC_USER = user.NAME\n" +
            "${ew_2.customSqlSegment}\n" +
            "    group by temp_undetected_latest.FQC_USER,ALIAS\n" +
            ")\n" +
            "-- 员工top单项漏检数(分白夜班)\n" +
            ",temp_singleUndetected_dayNight as(\n" +
            "    select FQC_USER,FEATUREVALUES,count(0) as num,\n" +
            "    case when (hour(date_sub(temp_piece_latest.TIME,INTERVAL 7 HOUR) ) BETWEEN 0 AND 11) THEN 'day'\n" +
            "        when (hour(date_sub(temp_piece_latest.TIME,INTERVAL 7 HOUR) ) BETWEEN 12 AND 23) THEN 'night' END AS clazz\n" +
            "    from temp_piece_latest\n" +
            "    JOIN temp_undetected_latest ON  temp_piece_latest.BAR_CODE = temp_undetected_latest.BARCODE\n" +
            "    JOIN DF_AOI_DEFECT ON temp_undetected_latest.DEFECTID = DF_AOI_DEFECT.DEFECTID\n" +
            "${ew_2.customSqlSegment}\n" +
            "    GROUP BY BARCODE, FQC_USER,FEATUREVALUES,clazz\n" +
            ")\n" +
            "select ALIAS str1,FEATUREVALUES str2,clazz str3,num/total dou1\n" +
            "from temp_undetected_featurevalues\n" +
            "JOIN temp_singleUndetected_dayNight ON  temp_undetected_featurevalues.FQC_USER = temp_singleUndetected_dayNight.FQC_USER")
    List<Rate3> empLossChemkRankTop10DayNight(@Param("ew") QueryWrapper<DfAoiDefect> ew, @Param("ew_2") QueryWrapper<DfAoiDefect> ew2);
}
