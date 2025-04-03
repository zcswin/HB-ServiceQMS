package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfDrawingChange;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfDrawingChangeMapper;
import com.ww.boengongye.service.DfDrawingChangeService;
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
 * 图纸变更 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Service
public class DfDrawingChangeServiceImpl extends ServiceImpl<DfDrawingChangeMapper, DfDrawingChange> implements DfDrawingChangeService {

	@Autowired
	private DfDrawingChangeMapper dfDrawingChangeMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfDrawingChange> qw) {
		return dfDrawingChangeMapper.listByExport(qw);
	}

	@Override
	public ImportExcelResult importOrder(String uploadPath, MultipartFile file) throws Exception{
		int successCount = 0;
		int failCount = 0;
		//调用封装好的工具
		ExcelImportUtil importUtil = new ExcelImportUtil(file);
		//调用导入的方法，获取sheet表的内容
		List<Map<String, String>> maps = importUtil.readExcelContent();
		//获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


		List<DfDrawingChange> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfDrawingChange tc = new DfDrawingChange();
			tc.setModel(map.get("型号"));
			tc.setProductionStage(map.get("生产阶段"));
			tc.setChangeArea(map.get("变更范围"));
			tc.setCustomerDrawingName(map.get("客户图纸名称"));
			tc.setInnerDrawingName(map.get("内部图纸名称"));
			tc.setFeiDrawingName(map.get("菲林图纸名称"));
			tc.setChangeDate((map.get("变更时间")));
			tc.setFlowId(map.get("流程单号"));
			tc.setCategory(map.get("类别"));
			tc.setDrawingRoom(map.get("绘图室"));
			tc.setFactoryDocumentControl(map.get("工厂文控"));
			tc.setOuterDfm(map.get("外部DFM"));
			tc.setInnerDfm(map.get("内部DFM"));
			tc.setBom(map.get("BOM"));
			tc.setErs(map.get("ERS"));
			tc.setSizeChangeList(map.get("尺寸变更清单"));
			tc.setOuterQcp(map.get("外部QCP"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfDrawingChange c : orderDetails) {
				dfDrawingChangeMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
