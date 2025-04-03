package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.entity.ProcessConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
public interface ProcessConfigMapper extends BaseMapper<ProcessConfig> {


    @Select("select p.*,f.factory_name as factoryName,l.name as lineBodyName from process_config p left join df_factory f on p.factory_id =f.id left join line_body l on p.line_body_id =l.id  ${ew.customSqlSegment}")
    IPage<ProcessConfig> listByJoinPage(IPage<ProcessConfig> page , @Param(Constants.WRAPPER) Wrapper<ProcessConfig> wrapper);




}
