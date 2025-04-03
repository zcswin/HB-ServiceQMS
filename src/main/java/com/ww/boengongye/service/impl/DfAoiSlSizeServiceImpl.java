package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfAoiSlSize;
import com.ww.boengongye.entity.DfAoiSlSizeDetail;
import com.ww.boengongye.mapper.DfAoiSlSizeDetailMapper;
import com.ww.boengongye.mapper.DfAoiSlSizeMapper;
import com.ww.boengongye.service.DfAoiSlSizeService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * df_aoi尺寸表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@Service
public class DfAoiSlSizeServiceImpl extends ServiceImpl<DfAoiSlSizeMapper, DfAoiSlSize> implements DfAoiSlSizeService {
	@Autowired
	private DfAoiSlSizeMapper dfAoiSlSizeMapper;
	@Autowired
	private DfAoiSlSizeDetailMapper dfAoiSlSizeDetailMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int importExcel(MultipartFile file) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 33);
		int count = 0;
		for (int i = 11; i < strings.length; i++) {
			if (StringUtils.isEmpty(strings[i][0])){
				continue;
			}
			Date parse = sd.parse(strings[i][0]);
			Timestamp time = new Timestamp(parse.getTime());
			String status = strings[i][29];
			String machineCode = strings[i][30];
			String color = strings[i][31];
			String model  = strings[i][32];

			DfAoiSlSizeDetail data = new DfAoiSlSizeDetail();
			data.setCheckTime(time);
			data.setStatus(status);
			data.setMachineCode(machineCode);
			data.setColor(color);
			data.setModel(model);
			data.setColor(color);
			dfAoiSlSizeDetailMapper.insert(data);
			Integer sizeId = data.getId();

			for (int j = 1; j <= 28; j++) {
//				System.out.println(strings[3][j]);
				String name = strings[2][j];
				Double standard = StringUtils.isEmpty(strings[3][j])? null:Double.valueOf(strings[3][j]);
				Double faiUp = StringUtils.isEmpty(strings[4][j])? null:Double.valueOf(strings[4][j]);
				Double faiDown = StringUtils.isEmpty(strings[5][j])? null:Double.valueOf(strings[5][j]);
				Double cpkUp = StringUtils.isEmpty(strings[6][j])? null:Double.valueOf(strings[6][j]);
				Double cpkDown = StringUtils.isEmpty(strings[7][j])? null:Double.valueOf(strings[7][j]);
				Double usl = StringUtils.isEmpty(strings[8][j])? null:Double.valueOf(strings[8][j]);
				Double lsl = StringUtils.isEmpty(strings[9][j])? null:Double.valueOf(strings[9][j]);

				DfAoiSlSize itemInfos = new DfAoiSlSize();
				itemInfos.setStandard(standard);
				itemInfos.setFaiUp(faiUp);
				itemInfos.setFaiDown(faiDown);
				itemInfos.setCpkUp(cpkUp);
				itemInfos.setCpkDown(cpkDown);
				itemInfos.setUp(usl);
				itemInfos.setDown(lsl);
				itemInfos.setCheckTime(time);
				itemInfos.setCheckId(sizeId);
				itemInfos.setItem(name);
				itemInfos.setItemValue(StringUtils.isEmpty(strings[i][j])? null:Double.valueOf(strings[i][j]));
				dfAoiSlSizeMapper.insert(itemInfos);
			}
			count ++;
		}

		return count;
	}
}
