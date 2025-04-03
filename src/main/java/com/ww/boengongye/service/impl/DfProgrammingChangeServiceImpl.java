package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfProgrammingChange;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfProgrammingChangeMapper;
import com.ww.boengongye.service.DfProgrammingChangeService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
@Service
public class DfProgrammingChangeServiceImpl extends ServiceImpl<DfProgrammingChangeMapper, DfProgrammingChange> implements DfProgrammingChangeService {

	@Autowired
	private DfProgrammingChangeMapper dfProgrammingChangeMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfProgrammingChange> qw) {
		return dfProgrammingChangeMapper.listByExport(qw);
	}

	@Override
	public ImportExcelResult importOrder(String filePath, MultipartFile file) throws Exception {
		int successCount = 0;
		int failCount = 0;
		//调用封装好的工具
		ExcelImportUtil importUtil = new ExcelImportUtil(file);
		//调用导入的方法，获取sheet表的内容
		List<Map<String, String>> maps = importUtil.readExcelContent();
		//获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


		List<DfProgrammingChange> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfProgrammingChange tc = new DfProgrammingChange();
			tc.setModel(map.get("型号"));
			tc.setRegistTime(map.get("登记时间"));
			tc.setTestProgrammingVersion(map.get("测试程序版本"));
			tc.setProgrammingChangeTime(map.get("程序变更时间"));
			tc.setSizeDrawingVersion(map.get("对应尺寸图纸版本"));
			tc.setColorDrawingVersion(map.get("对应颜色图纸版本"));
			tc.setSizeDrawingArea(map.get("尺寸图纸适用范围"));
			tc.setSizeChangeTime(map.get("尺寸图纸变更时间"));
			tc.setProgrammingChangeReason(map.get("程序变更原因"));
			tc.setProgrammingChangeContent(map.get("程序变更内容"));
			tc.setProgrammingImportTime(map.get("程序导入时间"));
			tc.setConfirmer(map.get("确认人"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfProgrammingChange c : orderDetails) {
				dfProgrammingChangeMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
