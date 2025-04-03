package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiMachineInspection;
import com.ww.boengongye.entity.DfAoiMachineProtect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfAoiPassPoint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * AOI机台维护 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
public interface DfAoiMachineProtectMapper extends BaseMapper<DfAoiMachineProtect> {
    @Select("select damp.*,df.factory_name factoryName " +
            "from df_aoi_machine_protect damp " +
            "left join df_factory df " +
            "on damp.factory_id =df.id " +
            "${ew.customSqlSegment}")
    IPage<DfAoiMachineProtect> listJoinPage(IPage<DfAoiMachineProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiMachineProtect> wrapper);

    @Select("select damp.machine_code machineCode,u.alias userName,dp.name projectName,decp.colour colour,count(0) synthesizeInput \n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "group by damp.machine_code ,u.alias ,dp.name ,decp.colour ")
    List<DfAoiMachineInspection> getAllMachineInspectionList(@Param(Constants.WRAPPER) Wrapper<DfAoiMachineInspection> wrapper);

    @Select("select damp.machine_code machineCode,u.alias userName \n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "group by damp.machine_code ,u.alias " +
            "order by damp.machine_code asc ")
    List<DfAoiMachineInspection> getMachineAndUserNameList(IPage<DfAoiMachineInspection> page, @Param(Constants.WRAPPER) Wrapper<DfAoiMachineInspection> wrapper);

    @Select("select inspection_input.machineCode,inspection_input.userName,inspection_input.projectName,\n" +
            "inspection_input.colour,inspection_input.synthesizeInput,inspection_output.synthesizeOutput\n" +
            "from \n" +
            "\t(select damp.machine_code machineCode,u.alias userName,dp.name projectName,decp.colour colour,count(0) synthesizeInput \n" +
            "\tfrom \n" +
            "\t\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\tfrom df_aoi_piece dap) new_piece\n" +
            "\tleft join df_aoi_machine_protect damp \n" +
            "\ton damp.ip = new_piece.ip \n" +
            "\tleft join df_factory df \n" +
            "\ton df.id  = damp.factory_id \n" +
            "\tleft join `user` u \n" +
            "\ton u.name  = damp.user_code \n" +
            "\tleft join df_e_code_protect decp \n" +
            "\ton substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "\tand substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "\tleft join df_project dp\n" +
            "\ton dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "\tgroup by damp.machine_code ,u.alias ,dp.name ,decp.colour ) inspection_input\n" +
            "left join \n" +
            "\t(select damp.machine_code machineCode,u.alias userName,dp.name projectName,decp.colour colour,count(0) synthesizeOutput\n" +
            "\t\tfrom \n" +
            "\t\t\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\t\t\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\t\t\tfrom df_aoi_piece dap) new_piece\n" +
            "\t\tleft join df_aoi_machine_protect damp \n" +
            "\t\ton damp.ip = new_piece.ip \n" +
            "\t\tleft join df_factory df \n" +
            "\t\ton df.id  = damp.factory_id \n" +
            "\t\tleft join `user` u \n" +
            "\t\ton u.name  = damp.user_code \n" +
            "\t\tleft join df_e_code_protect decp \n" +
            "\t\ton substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "\t\tand substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "\t\tleft join df_project dp\n" +
            "\t\ton dp.id = decp.project_id \n" +
            "${ew_2.customSqlSegment} " +
            "\t\tgroup by damp.machine_code ,u.alias ,dp.name ,decp.colour ) inspection_output\n" +
            "on inspection_output.machineCode = inspection_input.machineCode\n" +
            "and inspection_output.userName = inspection_input.userName\n" +
            "and inspection_output.projectName = inspection_input.projectName\n" +
            "and inspection_output.colour = inspection_input.colour")
    List<DfAoiMachineInspection> getMachineInspectionList(@Param(Constants.WRAPPER) Wrapper<DfAoiMachineInspection> wrapper, @Param("ew_2") Wrapper<DfAoiMachineInspection> wrapper2);

    @Select("select count(0) totalInput\n" +
            "from  df_aoi_piece dap\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = dap.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(dap.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(dap.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} ")
    Integer getTotalInputNumber(@Param(Constants.WRAPPER) Wrapper<Integer> wrapper);

    @Select("select sum(new_piece2.num) backNumber\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "inner join \n" +
            "\t(select dap.bar_code,(count(0)-1) num\n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.bar_code\n" +
            "\thaving num>0) new_piece2\n" +
            "on new_piece2.bar_code = new_piece.bar_code\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "group by dp.name ,decp.colour,new_piece2.num")
    Integer getBackNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    @Select("select count(0) backOKNumber\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "inner join \n" +
            "\t(select dap.bar_code,(count(0)-1) num\n" +
            "\tfrom df_aoi_piece dap \n" +
            "\tgroup by dap.bar_code\n" +
            "\thaving num>0) new_piece2\n" +
            "on new_piece2.bar_code = new_piece.bar_code\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n" +
            "${ew.customSqlSegment} " +
            "group by dp.name ,decp.colour,new_piece2.num")
    Integer getBackOKNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    @Select("select dp.name projectName,decp.colour colour,count(0) synthesizeInput\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n " +
            "${ew.customSqlSegment}")
    List<DfAoiPassPoint> getAllAoiPassPointList(@Param(Constants.WRAPPER) Wrapper<DfAoiPassPoint> wrapper);

    @Select("select dp.name projectName,decp.colour colour,count(0) synthesizeInput\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` asc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n " +
            "${ew.customSqlSegment}")
    List<DfAoiPassPoint> getAoiPassPointOneList(@Param(Constants.WRAPPER) Wrapper<DfAoiPassPoint> wrapper);


    @Select("select dp.name projectName,decp.colour colour,count(0) synthesizeInput\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n " +
            "${ew.customSqlSegment}")
    List<DfAoiPassPoint> getAoiPassPointList(IPage<DfAoiPassPoint> page, @Param(Constants.WRAPPER) Wrapper<DfAoiPassPoint> wrapper);


    @Select("select dp.name projectName,decp.colour colour,count(0) synthesizeInput,DATE_FORMAT(new_piece.`time`,'%Y-%m-%d') currentTime\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` asc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n " +
            "${ew.customSqlSegment}")
    List<DfAoiPassPoint> getAoiPassPointWeekOneList(@Param(Constants.WRAPPER) Wrapper<DfAoiPassPoint> wrapper);


    @Select("select dp.name projectName,decp.colour colour,count(0) synthesizeInput,DATE_FORMAT(new_piece.`time`,'%Y-%m-%d') currentTime\n" +
            "from \n" +
            "\t(select dap.name ,dap.bar_code,dap.`time` ,dap.qualityid,dap.ip,\n" +
            "\trow_number()over(partition by dap.bar_code order by dap.`time` desc) num\n" +
            "\tfrom df_aoi_piece dap) new_piece\n" +
            "left join df_aoi_machine_protect damp \n" +
            "on damp.ip = new_piece.ip \n" +
            "left join df_factory df \n" +
            "on df.id  = damp.factory_id \n" +
            "left join `user` u \n" +
            "on u.name  = damp.user_code \n" +
            "left join df_e_code_protect decp \n" +
            "on substring(new_piece.name,decp.start_data1,decp.cut_length1) = decp.match_data1\n" +
            "and substring(new_piece.name,decp.start_data2,decp.cut_length2) = decp.match_data2\n" +
            "left join df_project dp\n" +
            "on dp.id = decp.project_id \n " +
            "${ew.customSqlSegment}")
    List<DfAoiPassPoint> getAoiPassPointWeekList(@Param(Constants.WRAPPER) Wrapper<DfAoiPassPoint> wrapper);


}
