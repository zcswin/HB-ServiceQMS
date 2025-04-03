package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfOrtChangeSpecification;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfOrtChangeSpecificationMapper;
import com.ww.boengongye.service.DfOrtChangeSpecificationService;
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
 * ORT规格变更 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Service
public class DfOrtChangeSpecificationServiceImpl extends ServiceImpl<DfOrtChangeSpecificationMapper, DfOrtChangeSpecification> implements DfOrtChangeSpecificationService {

	@Autowired
	private DfOrtChangeSpecificationMapper dfOrtChangeSpecificationMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfOrtChangeSpecification> qw) {
		return dfOrtChangeSpecificationMapper.listByExport(qw);
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


		List<DfOrtChangeSpecification> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfOrtChangeSpecification tc = new DfOrtChangeSpecification();
			tc.setModel(map.get("型号"));
			tc.setTestName(map.get("测试名称"));
			tc.setTestProcessAmount(map.get("工序测试频率/数量"));
			tc.setTestProject(map.get("测试项目"));
			tc.setPredicate(map.get("判定"));
			tc.setStage(map.get("阶段"));
			tc.setStandard(map.get("规格"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfOrtChangeSpecification c : orderDetails) {
				dfOrtChangeSpecificationMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
