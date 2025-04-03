package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfTzSuggest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2024-12-26
 */
public interface DfTzSuggestMapper extends BaseMapper<DfTzSuggest> {

    @Select("SELECT * " +
            "FROM df_tz_suggest " +
            "WHERE  " +
            "    (( " +
            "         " +
            "        (type = 'single' AND  " +
            "            ( " +
            "                (calculate1    = '<' AND  #{value}  < COALESCE(max, -9999999999)) OR " +
            "                (calculate1    = '<=' AND  #{value}  <= COALESCE(max, -9999999999)) " +
            "            ) " +
            "        ) " +
            "    ) " +
            "    OR " +
            "    ( " +
            "       " +
            "        (type = 'between' AND  " +
            "            ( " +
            "                ((calculate1    = '>' AND  #{value}  > COALESCE(min, -9999999999)) OR " +
            "                 (calculate1    = '>=' AND  #{value} >= COALESCE(min, -9999999999))) " +
            "                AND " +
            "                ((calculate2 = '<' AND  #{value}  < COALESCE(max, 9999999999)) OR " +
            "                 (calculate2 = '<=' AND  #{value}  <= COALESCE(max, 9999999999))) " +
            "            ) " +
            "        ) " +
            "    )) and name= #{name} limit 1  ")

    DfTzSuggest getResult(Double value,String name);

}
