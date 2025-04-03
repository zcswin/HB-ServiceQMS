package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfFlowData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 流程数据 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
public interface DfFlowDataMapper extends BaseMapper<DfFlowData> {
    @Select("SELECT d.* FROM `df_flow_data` d left join df_flow_data_user u on d.id=u.flow_data_id and u.flow_level=d.flow_level ${ew.customSqlSegment}")
    IPage<DfFlowData> listBacklog(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT d.* FROM `df_flow_data` d left join df_flow_data_user u on d.id=u.flow_data_id and u.flow_level!=d.flow_level ${ew.customSqlSegment}")
    IPage<DfFlowData> listHaveDone(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT fd.* " +
            "FROM `df_flow_data_overtime` ot " +
            "left join df_audit_detail ad on ot.flow_data_id = ad.id " +
            "left join df_flow_data fd on fd.data_id = ad.id " +
            "${ew.customSqlSegment}")
    IPage<DfFlowData> listOvertime(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT fd.* " +
            "FROM `df_flow_data_user` fu " +
            "left join df_flow_data fd on fd.id = fu.flow_data_id " +
            "${ew.customSqlSegment}")
    IPage<DfFlowData> listOvertimeByMatter(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);


    @Select("SELECT " +
            "\tad.process,lm.start_time " +
            "\t,lm.end_time, " +
            "\tfl.*  " +
            "FROM " +
            "\t`df_flow_data` AS fl " +
            "\tJOIN df_audit_detail AS ad ON fl.data_id = ad.id  " +
            "\tAND fl.data_type = ad.data_type " +
            "\tJOIN df_liable_man AS lm on lm.process_name=ad.process ")
    IPage<DfFlowData> listByMan(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);


    @Select("SELECT count(d.id) as id FROM `df_flow_data` d left join df_flow_data_user u on d.id=u.flow_data_id and u.flow_level=d.flow_level ${ew.customSqlSegment}")
    DfFlowData getBacklogCount( @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT count(d.id) as id FROM `df_flow_data` d left join df_flow_data_user u on d.id=u.flow_data_id and u.flow_level!=d.flow_level ${ew.customSqlSegment}")
    DfFlowData getHaveDoneCount( @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT flow.* " +
            "FROM df_flow_data flow " +
            "left join df_audit_detail aud on flow.data_id = aud.id " +
            "left join user user on flow.create_user_id = user.id " +
            "${ew.customSqlSegment}")
    IPage<DfFlowData> listJoinAudit(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Select("SELECT aud.* " +
            "FROM df_flow_data flow " +
            "left join df_audit_detail aud on flow.data_id = aud.id " +
            "${ew.customSqlSegment}")
    DfAuditDetail getJoinAudit(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    @Update("update df_flow_data set overtime_status = null, start_timeout = null where id = #{id}")
    int updateOverTimeById(int id);

    @Select("select * from  " +
            "df_flow_data flow " +
            "left join user user on flow.create_user_id = user.id " +
            "${ew.customSqlSegment} ")
    List<DfFlowData> listJoinUser(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    // 超时等级升级查询（查询后直接更新）
    @Select("SELECT flow.id,flow.now_level_user_name,ad.process, if(TIMESTAMPDIFF(HOUR,if(flow.level3_push_time is null,flow.create_time, flow.level3_push_time),now()) >= otl.end_time, flow.current_timeout_level + 1, flow.current_timeout_level )  as 'current_timeout_level', TIMESTAMPDIFF(HOUR,flow.create_time,now()) diff_time FROM `df_flow_data` flow " +
            "left join df_overtime_level otl on flow_level_name like concat('%',otl.problem_level,'%') and data_type = otl.type and flow.current_timeout_level = otl.overtime_level " +
            " LEFT JOIN df_audit_detail ad on flow.data_id=ad.id and  flow.flow_type=ad.data_type "+
            "${ew.customSqlSegment} ")
    List<DfFlowData> listOverTimeLevelUp(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    // 获取该账号稽查升级超时的单子，不同事件等级、不同超时等级，由不同职位的人处理
    @Select("SELECT * FROM `df_flow_data` flow " +
            "left join df_audit_detail det on flow.data_id = det.id " +
            "left join df_overtime_level otl on flow_level_name like concat('%',otl.problem_level,'%') and flow.data_type = otl.type and flow.current_timeout_level = otl.overtime_level " +
            "left join (select * from df_liable_man where liable_man_code = #{account} and (type like '%check%' or type = 'all' or type = 'ALL')) u on otl.overtime_level = u.problem_level and (u.process_name like concat('%',det.process,'%') or u.process_name = 'all' or u.process_name = 'ALL' ) " +
            "where flow.data_type = '稽查' and flow.submit_time is null and u.id is not null and ((current_timeout_level > 1 and (flow.flow_level_name like '%1%' or flow.flow_level_name like '%2%')) or current_timeout_level > 2 and flow.flow_level_name like '%3%' ) and (flow.status = '待确认' or flow.status = '待提交')")
    List<DfFlowData> listUpOutTimeLevelByAccount(String account);

    @Select("SELECT * FROM `df_flow_data` flow " +
            "left join df_audit_detail det on flow.data_id = det.id " +
            "left join df_overtime_level otl on flow_level_name like concat('%',otl.problem_level,'%') and flow.data_type = otl.type and flow.current_timeout_level = otl.overtime_level " +
            "left join (select * from df_liable_man where liable_man_name = #{userName} and (type like '%check%' or type = 'all' or type = 'ALL')) u on otl.overtime_level = u.problem_level and (u.process_name like concat('%',det.process,'%') or u.process_name = 'all' or u.process_name = 'ALL' ) " +
            "where flow.data_type = '稽查' and flow.submit_time is null and u.id is not null and ((current_timeout_level > 1 and (flow.flow_level_name like '%1%' or flow.flow_level_name like '%2%')) or current_timeout_level > 2 and flow.flow_level_name like '%3%' ) and (flow.status = '待确认' or flow.status = '待提交')")
    List<DfFlowData> listUpOutTimeLevelByUserName(String userName);

    @Select("select * from df_flow_data  " +
            "where id in ( " +
            "select distinct flow_data_id from df_flow_opinion op " +
            "where sender_id = #{userId}) " +
            "and status = '已关闭' " +
            "order by id desc")
    IPage<DfFlowData> listClosedByUserId(IPage<DfFlowData> page, String userId);
}
