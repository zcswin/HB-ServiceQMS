package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfQaBom;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfQaBomMapper;
import com.ww.boengongye.service.DfQaBomService;
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
 * QA-BOM 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Service
public class DfQaBomServiceImpl extends ServiceImpl<DfQaBomMapper, DfQaBom> implements DfQaBomService {

	@Autowired
	private DfQaBomMapper dfQaBomMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfQaBom> qw) {
		return dfQaBomMapper.listByExport(qw);
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


		List<DfQaBom> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfQaBom tc = new DfQaBom();
			tc.setFlowId(map.get("流程编号"));
			tc.setProjectModel(map.get("项目型号"));
			tc.setStage(map.get("阶段"));
			tc.setConfig(map.get("config"));
			tc.setColor(map.get("颜色"));
			tc.setQaBom(map.get("QA-BOM"));
			tc.setConfirmStatus(map.get("确认状态"));
			tc.setLowerDrawings(map.get("图纸下放(PM)"));
			tc.setConvertDrawings(map.get("图纸转换(绘图员)"));
			tc.setOuterDfm(map.get("外部DFM(PIE)"));
			tc.setInnerDfm(map.get("内部DFM(绘图员)"));
			tc.setOuterQcp(map.get("外部QCP(CQE)"));
			tc.setInnerQcpSize(map.get("内部QCP-尺寸(测量组)"));
			tc.setInnerQcpAppearance(map.get("内部QCP-外观(IQPC)"));
			tc.setMaterialBom(map.get("物料BOM(IE)"));
			tc.setErs(map.get("ERS(CQE)"));
			tc.setProcessMil(map.get("过程MIL(PIE)"));
			tc.setFactoryCheck(map.get("工厂审核(工厂)"));
			tc.setManufacturingOrder(map.get("生产单号(生产部)"));
			tc.setFirstReport(map.get("首件报告(工厂测量)"));
			tc.setFirstConfirm(map.get("首件确认(中央IPQC)"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfQaBom c : orderDetails) {
				dfQaBomMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
