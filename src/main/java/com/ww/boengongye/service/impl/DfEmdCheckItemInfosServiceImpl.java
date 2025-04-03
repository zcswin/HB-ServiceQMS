package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfEmdCheckItemInfosMapper;
import com.ww.boengongye.mapper.DfEmdDetailMapper;
import com.ww.boengongye.service.DfEmdCheckItemInfosService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * EMD检测 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-13
 */
@Service
public class DfEmdCheckItemInfosServiceImpl extends ServiceImpl<DfEmdCheckItemInfosMapper, DfEmdCheckItemInfos> implements DfEmdCheckItemInfosService {
	@Autowired
	private DfEmdCheckItemInfosMapper dfEmdCheckItemInfosMapper;
	@Autowired
	private DfEmdDetailMapper dfEmdDetailMapper;



//	@Override
//	public ArrayList<DfEmdCheckItemInfos> importExcel(MultipartFile file,String[] split) throws Exception {
//		ExcelImportUtil importUtil = new ExcelImportUtil(file);
//
//		String factory = split[0];
//		String linebody = split[1];
//		String project = split[2];
//		String color = split[3];
//		String work_position = split[5];
//
//		//调用导入的方法，获取sheet表的内容
//		Sheet sheet = importUtil.getWb().getSheetAt(0);
//		int lastRowNum = 0;
//		if (sheet != null){
//			lastRowNum = sheet.getLastRowNum();
//		}
//		//获取左右工位结果
//		List<Map<String, String>> listLeft = importUtil.readExcelBlock3(2, lastRowNum + 1, 1, 6,11,37);
//		List<Map<String, String>> listRight = importUtil.readExcelBlock3(2, lastRowNum + 1, 1, 10,38,64);
//		int count = 0;
//		Map<String, DfEmdCheckItemInfos> snISMId = new HashMap<>();
//		if (!CollectionUtils.isEmpty(listLeft) || !CollectionUtils.isEmpty(listRight)){
//			for (int i = 0 ; i < Math.max(listLeft.size(),listRight.size()) ; i++) {
//				Map<String, String> mapLeft = listLeft.get(i);
//				Map<String, String> mapRight = listRight.get(i);
//				if (!CollectionUtils.isEmpty(mapLeft)){
//					String sn = mapLeft.get("左工位SN");
//					DfEmdCheckItemInfos left = new DfEmdCheckItemInfos();
//					if (snISMId.containsKey(sn)) {
//						left.setCheckType(2);
//					} else {
//						left.setCheckType(1);
//					}
//					left.setCheckTime(mapLeft.get("时间"));
//					left.setWorkPosition(mapLeft.get("前/后工位"));
//					left.setSn(mapLeft.get("左工位SN"));
//					left.setResult(mapLeft.get("左工位结果"));
//					left.setValue(StringUtils.isEmpty(mapLeft.get("左工位"))?null:Double.valueOf(mapLeft.get("左工位")));
//					left.setWorkChange(mapLeft.get("左工位形变"));
//					left.setType("左工位");
//					left.setFactory(factory);
//					left.setLineBody(linebody);
//					left.setColor(color);
//					left.setProject(project);
//					generateData(left,mapLeft);
//					snISMId.put(sn, left);
//				}
//				if (!CollectionUtils.isEmpty(mapRight)){
//					String sn = mapRight.get("右工位SN");
//					DfEmdCheckItemInfos right = new DfEmdCheckItemInfos();
//					if (snISMId.containsKey(sn)) {
//						right.setCheckType(2);
//					} else {
//						right.setCheckType(1);
//					}
//					right.setCheckTime(mapRight.get("时间"));
//					right.setWorkPosition(mapRight.get("前/后工位"));
//					right.setSn(mapRight.get("右工位SN"));
//					right.setResult(mapRight.get("右工位结果"));
//					right.setValue(StringUtils.isEmpty(mapRight.get("右工位"))?null:Double.valueOf(mapLeft.get("左工位")));
//					right.setWorkChange(mapRight.get("右工位形变"));
//					right.setType("右工位");
//					right.setFactory(factory);
//					right.setLineBody(linebody);
//					right.setColor(color);
//					right.setProject(project);
//					generateData(right,mapRight);
//					generateData(right,mapRight);
//					snISMId.put(sn, right);
//				}
//				count ++;
//			}
//		}
//		ArrayList<DfEmdCheckItemInfos> items = new ArrayList<>();
//		snISMId.entrySet().forEach(m->items.add(m.getValue()));
//		return items;
//	}
//
//
//	private void generateData(DfEmdCheckItemInfos left,Map<String,String> map) {
//		left.setB15(StringUtils.isEmpty(map.get("B15"))?null:Double.valueOf(map.get("B15")));
//		left.setB13(StringUtils.isEmpty(map.get("B13"))?null:Double.valueOf(map.get("B13")));
//		left.setB12(StringUtils.isEmpty(map.get("B12"))?null:Double.valueOf(map.get("B12")));
//		left.setB11(StringUtils.isEmpty(map.get("B11"))?null:Double.valueOf(map.get("B11")));
//		left.setB10(StringUtils.isEmpty(map.get("B10"))?null:Double.valueOf(map.get("B10")));
//		left.setB09(StringUtils.isEmpty(map.get("B09"))?null:Double.valueOf(map.get("B09")));
//		left.setB08(StringUtils.isEmpty(map.get("B08"))?null:Double.valueOf(map.get("B08")));
//		left.setB07(StringUtils.isEmpty(map.get("B07"))?null:Double.valueOf(map.get("B07")));
//		left.setB05(StringUtils.isEmpty(map.get("B05"))?null:Double.valueOf(map.get("B05")));
//		left.setB01(StringUtils.isEmpty(map.get("B01"))?null:Double.valueOf(map.get("B01")));
//		left.setIbb(StringUtils.isEmpty(map.get("IBB"))?null:Double.valueOf(map.get("IBB")));
//		left.setIbe(StringUtils.isEmpty(map.get("IBE"))?null:Double.valueOf(map.get("IBE")));
//		left.setIbh(StringUtils.isEmpty(map.get("IBH"))?null:Double.valueOf(map.get("IBH")));
//		left.setIbk(StringUtils.isEmpty(map.get("IBK"))?null:Double.valueOf(map.get("IBK")));
//		left.setIbn(StringUtils.isEmpty(map.get("IBN"))?null:Double.valueOf(map.get("IBN")));
//		left.setIbq(StringUtils.isEmpty(map.get("IBQ"))?null:Double.valueOf(map.get("IBQ")));
//		left.setIbt(StringUtils.isEmpty(map.get("IBT"))?null:Double.valueOf(map.get("IBT")));
//		left.setB19(StringUtils.isEmpty(map.get("B19"))?null:Double.valueOf(map.get("B19")));
//		left.setB23(StringUtils.isEmpty(map.get("B23"))?null:Double.valueOf(map.get("B23")));
//		left.setB25(StringUtils.isEmpty(map.get("B25"))?null:Double.valueOf(map.get("B25")));
//		left.setB26(StringUtils.isEmpty(map.get("B26"))?null:Double.valueOf(map.get("B26")));
//		left.setB27(StringUtils.isEmpty(map.get("B27"))?null:Double.valueOf(map.get("B27")));
//		left.setB28(StringUtils.isEmpty(map.get("B28"))?null:Double.valueOf(map.get("B28")));
//		left.setB29(StringUtils.isEmpty(map.get("B29"))?null:Double.valueOf(map.get("B29")));
//		left.setB30(StringUtils.isEmpty(map.get("B30"))?null:Double.valueOf(map.get("B30")));
//		left.setB31(StringUtils.isEmpty(map.get("B31"))?null:Double.valueOf(map.get("B31")));
//		left.setB33(StringUtils.isEmpty(map.get("B33"))?null:Double.valueOf(map.get("B33")));
//	}

	@Override
	//27点位
	public int importExcel2(MultipartFile file,String[] split) throws Exception {
		ExcelImportUtil importUtil = new ExcelImportUtil(file);

		int count = 0;

		String factory = split[0];
		String linebody = split[1];
		String project = split[2];
		String color = split[3];
		//F或B
		String workPosition = split[5].split("-")[0];
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 64);
		Map<String, Integer> snResLeadId = new HashMap<>();
		for (int i = 1; i < strings.length; i++) {
			Date parse = sd.parse(strings[i][0]);
			Timestamp time = new Timestamp(parse.getTime());
			//前或后
			String fOrB = strings[i][1];
			//sn左右
			String snLeft = strings[i][2];
			String snRight = strings[i][6];
			//结果
			String resultLeft = strings[i][3];
			String resultRight = strings[i][7];
			//值
			String valueLeft = strings[i][4];
			String valueRight = strings[i][8];
			//形变
			String changeLeft = strings[i][5];
			String changeRight = strings[i][9];

			DfEmdDetail dataLeft = new DfEmdDetail();
			dataLeft.setSn(snLeft);
			dataLeft.setCheckTime(time);
			dataLeft.setMachineCode(fOrB + "1");
			dataLeft.setFactory(factory);
			dataLeft.setLineBody(linebody);
			dataLeft.setProject(project);
			dataLeft.setColor(color);
			dataLeft.setWorkPosition(workPosition);
			dataLeft.setWorkResult(resultLeft);
			dataLeft.setWorkValue(valueLeft == null ? null : Double.valueOf(valueLeft));
			dataLeft.setWorkChange(changeLeft);
			if (snResLeadId.containsKey(snLeft)) {
				dataLeft.setCheckType(2);
			} else {
				dataLeft.setCheckType(1);
			}
			dfEmdDetailMapper.insert(dataLeft);
			Integer emdId = dataLeft.getId();
			if (snResLeadId.containsKey(snLeft)) {
				Integer id = snResLeadId.get(snLeft);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snLeft, emdId);


			for (int j = 10; j <= 36; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				dfEmdCheckItemInfosMapper.insert(itemInfos);
			}

			DfEmdDetail dataRight = new DfEmdDetail();
			dataRight.setSn(snRight);
			dataRight.setCheckTime(time);
			dataRight.setMachineCode(fOrB + "2");
			dataRight.setFactory(factory);
			dataRight.setLineBody(linebody);
			dataRight.setProject(project);
			dataRight.setColor(color);
			dataRight.setWorkPosition(workPosition);
			dataRight.setWorkResult(resultRight);
			dataRight.setWorkValue(valueRight == null ? null : Double.valueOf(valueRight));
			dataRight.setWorkChange(changeRight);
			if (snResLeadId.containsKey(snRight)) {
				dataRight.setCheckType(2);
			} else {
				dataRight.setCheckType(1);
			}
			dfEmdDetailMapper.insert(dataRight);
			emdId = dataRight.getId();
			if (snResLeadId.containsKey(snRight)) {
				Integer id = snResLeadId.get(snRight);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snRight, emdId);

			for (int j = 37; j <= 63; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				dfEmdCheckItemInfosMapper.insert(itemInfos);
			}
			count ++;
		}
		return count;

	}

	@Override
	//21点位
	public int importExcel3(MultipartFile file,String[] split) throws Exception {
		int count = 0;

//		String factory = split[0];
//		String linebody = split[1];
		String project = split[0];
//		String color = split[3];
		//F或B
		String workPosition = split[6];
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 52);
		Map<String, Integer> snResLeadId = new HashMap<>();
		for (int i = 1; i < strings.length; i++) {
			Date parse = sd.parse(strings[i][0]);
			Timestamp time = new Timestamp(parse.getTime());
			//前或后
			String fOrB = strings[i][1];
			//sn左右
			String snLeft = strings[i][2];
			String snRight = strings[i][6];
			//结果
			String resultLeft = strings[i][3];
			String resultRight = strings[i][7];
			//值
			String valueLeft = strings[i][4];
			String valueRight = strings[i][8];
			//形变
			String changeLeft = strings[i][5];
			String changeRight = strings[i][9];

			DfEmdDetail dataLeft = new DfEmdDetail();
			dataLeft.setSn(snLeft);
			dataLeft.setCheckTime(time);
			dataLeft.setMachineCode(fOrB + "1");
//			dataLeft.setFactory(factory);
//			dataLeft.setLineBody(linebody);
			dataLeft.setProject(project);
//			dataLeft.setColor(color);
			dataLeft.setWorkPosition(workPosition);
			dataLeft.setWorkResult(resultLeft);
			dataLeft.setWorkValue(valueLeft == null ? null : Double.valueOf(valueLeft));
			dataLeft.setWorkChange(changeLeft);
			if (snResLeadId.containsKey(snLeft)) {
				dataLeft.setCheckType(2);
			} else {
				dataLeft.setCheckType(1);
			}
			dfEmdDetailMapper.insert(dataLeft);
			Integer emdId = dataLeft.getId();
			if (snResLeadId.containsKey(snLeft)) {
				Integer id = snResLeadId.get(snLeft);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snLeft, emdId);


			for (int j = 10; j <= 30; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				dfEmdCheckItemInfosMapper.insert(itemInfos);
			}

			DfEmdDetail dataRight = new DfEmdDetail();
			dataRight.setSn(snRight);
			dataRight.setCheckTime(time);
			dataRight.setMachineCode(fOrB + "2");
//			dataRight.setFactory(factory);
//			dataRight.setLineBody(linebody);
			dataRight.setProject(project);
//			dataRight.setColor(color);
			dataRight.setWorkPosition(workPosition);
			dataRight.setWorkResult(resultRight);
			dataRight.setWorkValue(valueRight == null ? null : Double.valueOf(valueRight));
			dataRight.setWorkChange(changeRight);
			if (snResLeadId.containsKey(snRight)) {
				dataRight.setCheckType(2);
			} else {
				dataRight.setCheckType(1);
			}
			dfEmdDetailMapper.insert(dataRight);
			emdId = dataRight.getId();
			if (snResLeadId.containsKey(snRight)) {
				Integer id = snResLeadId.get(snRight);
				dfEmdDetailMapper.deleteById(id);

				QueryWrapper<DfEmdCheckItemInfos> qw = new QueryWrapper<>();
				qw.eq("check_id", id);
				dfEmdCheckItemInfosMapper.delete(qw);
			}
			snResLeadId.put(snRight, emdId);

			for (int j = 31; j <= 51; j++) {
				DfEmdCheckItemInfos itemInfos = new DfEmdCheckItemInfos();
				itemInfos.setPointPosition(strings[0][j]);
				itemInfos.setCheckTime(time);
				itemInfos.setResult(StringUtils.isEmpty(strings[i][j])?null:Double.valueOf(strings[i][j]));
				itemInfos.setCheckId(emdId);
				dfEmdCheckItemInfosMapper.insert(itemInfos);
			}
			count ++;
		}
		return count;

	}

	@Override
	public Rate4 listLeadNum(QueryWrapper<DfEmdCheckItemInfos> qw) {
		return dfEmdCheckItemInfosMapper.listLeadNum(qw);
	}

	@Override
	public List<Rate3> listAllNumGroupByDate(QueryWrapper<DfLeadDetail> qw) {
		return dfEmdCheckItemInfosMapper.listAllNumGroupByDate(qw);
	}

	@Override
	public List<Rate3> listOKRate(QueryWrapper<DfLeadDetail> qw) {
		return dfEmdCheckItemInfosMapper.listOKRate(qw);
	}

	@Override
	public List<Rate3> listOkRateGroupByDate(QueryWrapper<DfLeadDetail> qw) {
		return dfEmdCheckItemInfosMapper.listOkRateGroupByDate(qw);
	}

	@Override
	public List<Rate3> listWorkPositionOKRate(QueryWrapper<DfEmdCheckItemInfos> qw) {
		return dfEmdCheckItemInfosMapper.listWorkPositionOKRate(qw);
	}

	@Override
	public List<Rate3> listWorkPositionOKRate2(QueryWrapper<DfLeadDetail> qw) {
		return dfEmdCheckItemInfosMapper.listWorkPositionOKRate2(qw);
	}

	@Override
	public List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfEmdCheckItemInfos> qw) {
		return dfEmdCheckItemInfosMapper.listDateOneAndMutilOkRate(qw);
	}
}
