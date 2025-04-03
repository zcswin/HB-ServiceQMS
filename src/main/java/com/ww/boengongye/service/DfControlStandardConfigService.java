package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.ImportExcelResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管控标准配置 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
public interface DfControlStandardConfigService extends IService<DfControlStandardConfig> {
    ImportExcelResult importOrder(String factory,String project,String process,String line, MultipartFile file ) throws Exception;
    List<DfControlStandardConfig>  listByExport(@Param("id")int id);
    IPage<DfControlStandardConfig> listByJoinPage(IPage<DfControlStandardConfig> page, @Param(Constants.WRAPPER) Wrapper<DfControlStandardConfig> wrapper,@Param("batchId") String batchId);

    int upload(MultipartFile file, String factory, String lineBody, String process, String project) throws Exception;

    List<DfControlStandardConfig> listDataType();
}
