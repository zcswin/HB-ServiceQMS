package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DefectSummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DefectSummaryMapper extends BaseMapper<DefectSummary> {

    @Select({
            "<script>",
            "SELECT * FROM defect_summary",
            "<where>",
            "   <if test='list != null and !list.isEmpty()'>",
            "       process_summary_id IN",
            "       <foreach item='id' collection='list' open='(' separator=',' close=')'>",
            "           #{id}",
            "       </foreach>",
            "   </if>",
            "   <if test='list == null or list.isEmpty()'>",
            "       1=0 /* 空列表返回无结果 */",
            "   </if>",
            "</where>",
            "</script>"
    })
    List<DefectSummary> safeListByProcessSummaryIds(@Param("list") List<Integer> ids);
}
