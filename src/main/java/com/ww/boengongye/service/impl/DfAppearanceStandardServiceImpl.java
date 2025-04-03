package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfAppearanceStandard;
import com.ww.boengongye.entity.DfDrawingFile;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfAppearanceStandardMapper;
import com.ww.boengongye.service.DfAppearanceStandardService;
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
 * @since 2023-08-21
 */
@Service
public class DfAppearanceStandardServiceImpl extends ServiceImpl<DfAppearanceStandardMapper, DfAppearanceStandard> implements DfAppearanceStandardService {

	@Autowired
	private DfAppearanceStandardMapper dfAppearanceStandardMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfDrawingFile> qw) {
		return dfAppearanceStandardMapper.listByExport(qw);
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


		List<DfAppearanceStandard> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfAppearanceStandard tc = new DfAppearanceStandard();
//            tc.setFactoryName(map.get("工厂"));
//            QueryWrapper<DfFactory>fqw=new QueryWrapper<>();
//            fqw.eq("factory_name",map.get("工厂"));
//            fqw.last("limit 0,1");
//            DfFactory df=DfFactoryMapper.selectOne(fqw);
//            if(null!=df&&null!=df.getFactoryCode()){
//                tc.setFactoryCode(df.getFactoryCode());
//            }
			tc.setModel(map.get("型号"));
			tc.setLevel(map.get("规格等级"));
			tc.setProject(map.get("项目"));
			tc.setCategory(map.get("类别"));
			tc.setArea(map.get("名称"));
			tc.setName(map.get("名称"));
			tc.setDefinition(map.get("定义"));
			tc.setTestMethod(map.get("检验方法"));
			tc.setStandard(map.get("允收标准"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfAppearanceStandard c : orderDetails) {
				dfAppearanceStandardMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}
}
