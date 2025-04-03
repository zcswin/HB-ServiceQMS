package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfQcpStandard;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.ExportExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * QCP标准 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
public interface DfQcpStandardService extends IService<DfQcpStandard> {
    ImportExcelResult importOrder(String filePath, MultipartFile file ) throws Exception;

	List<Map<String, Object>> listByExport(QueryWrapper<DfQcpStandard> qw);

	default void exportModel(HttpServletResponse response, String name){
		ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
		exportExcelUtil.downLoadExcelMould(response, name);
	}
}
