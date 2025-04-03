package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfColorStandard;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfColorStandardMapper;
import com.ww.boengongye.service.DfColorStandardService;
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
public class DfColorStandardServiceImpl extends ServiceImpl<DfColorStandardMapper, DfColorStandard> implements DfColorStandardService {

	@Autowired
	private DfColorStandardMapper dfColorStandardMapper;

	@Override
	public List<Map<String, Object>> listByExport(QueryWrapper<DfColorStandard> qw) {
		return dfColorStandardMapper.listByExport(qw);
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


		List<DfColorStandard> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
			DfColorStandard tc = new DfColorStandard();
//            tc.setFactoryName(map.get("工厂"));
//            QueryWrapper<DfFactory>fqw=new QueryWrapper<>();
//            fqw.eq("factory_name",map.get("工厂"));
//            fqw.last("limit 0,1");
//            DfFactory df=DfFactoryMapper.selectOne(fqw);
//            if(null!=df&&null!=df.getFactoryCode()){
//                tc.setFactoryCode(df.getFactoryCode());
//            }
			tc.setModel(map.get("型号"));
			tc.setColor(map.get("颜色"));
			tc.setTestMode(map.get("测量模式"));
			tc.setP2SandArea(map.get("p2-蒙砂区规格"));
			tc.setP2Gloss(map.get("p2-光泽度规格"));
			tc.setP2Remarks(map.get("p2备注"));
			tc.setEvtSandArea(map.get("evt-蒙砂区规格"));
			tc.setEvtGloss(map.get("evt-光泽度规格"));
			tc.setEvtRemarks(map.get("evt备注"));
			return tc;
		}).collect(Collectors.toList());

		if (orderDetails.size() > 0) {
			for (DfColorStandard c : orderDetails) {
				dfColorStandardMapper.insert(c);
			}
		}
		ImportExcelResult ter = new ImportExcelResult();
		ter.setFail(failCount);
		ter.setSuccess(successCount);

		return ter;
	}

}
