package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAppearanceStandard;
import com.ww.boengongye.entity.DfDrawingFile;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.ExportExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-21
 */
public interface DfAppearanceStandardService extends IService<DfAppearanceStandard> {

	List<Map<String, Object>> listByExport(QueryWrapper<DfDrawingFile> qw);

	ImportExcelResult importOrder(String uploadPath, MultipartFile file) throws Exception;

	default void exportModel(HttpServletResponse response, String name){
		ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
		exportExcelUtil.downLoadExcelMould(response, name);
	}
}
