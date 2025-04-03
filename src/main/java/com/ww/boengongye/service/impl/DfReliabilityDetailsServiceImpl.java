package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfReliabilityDetails;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfReliabilityDetailsMapper;
import com.ww.boengongye.service.DfReliabilityDetailsService;
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
 * ORT可靠性明细 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
@Service
public class DfReliabilityDetailsServiceImpl extends ServiceImpl<DfReliabilityDetailsMapper, DfReliabilityDetails> implements DfReliabilityDetailsService {

	@Autowired
	private DfReliabilityDetailsMapper dfReliabilityDetailsMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfReliabilityDetails> qw) {
		return dfReliabilityDetailsMapper.listByExport(qw);
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


		List<DfReliabilityDetails> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfReliabilityDetails tc = new DfReliabilityDetails();
			tc.setFactory(map.get("工厂"));
			tc.setModel(map.get("型号"));
			tc.setProcess(map.get("工序"));
			tc.setTestProject(map.get("测试项目"));
			tc.setTestStandard(map.get("测试标准"));
			tc.setTestDevice(map.get("测试设备"));
			tc.setTestAmount(map.get("测试频率/数量"));
			tc.setFaiRate(map.get("FAI频率"));
			tc.setFaiAmount(map.get("FAI数量"));
			tc.setFaiUnit(map.get("FAI单位"));
			tc.setIpqcRate(map.get("IPQC频率"));
			tc.setIpqcAmount(map.get("IPQC数量"));
			tc.setIpqcUnit(map.get("IPQC单位"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfReliabilityDetails c : orderDetails) {
				dfReliabilityDetailsMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
