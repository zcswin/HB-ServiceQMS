package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfAoiSlMapper;
import com.ww.boengongye.mapper.DfAuditDetailMapper;
import com.ww.boengongye.mapper.DfFlowDataMapper;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-10
 */
@Service
public class DfAoiSlServiceImpl extends ServiceImpl<DfAoiSlMapper, DfAoiSl> implements DfAoiSlService {

	@Autowired
	private DfAoiSlMapper dfAoiSlMapper;
	@Autowired
	private DfAuditDetailMapper dfAuditDetailMapper;
	@Autowired
	private DfFlowDataMapper dfFlowDataMapper;
	@Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfLiableManService dfLiableManService;
	@Autowired
	private DfAuditDetailService dfAuditDetailService;
	@Autowired
	private DfApprovalTimeService dfApprovalTimeService;

	//todo Lw 此处模板有出现的问题 1(三层 BG 中间有空格) 2(终烤后sheet页没有提供时间) 3.工厂,工序,线体,提供并使用用分隔符
	@Override
	public List<DfAoiSl> importSL(MultipartFile file) throws Exception {
		ExcelImportUtil excelImportUtil = new ExcelImportUtil(file);
//		String filename = "C:\\Users\\LiWei\\Documents\\WeChat Files\\wxid_jhgt3pav71ii22\\FileStorage\\File\\2023-09\\丝印尺寸数据模版\\过程颜色\\LAB\\C27蓝色MP量产过程颜色数据 8-23 B  舒琴20001041.xlsx";
//		InputStream is = new FileInputStream(filename);
			InputStream is = file.getInputStream();
		String[] split = file.getOriginalFilename().split("_");
		//工厂
			String factory = "J10-1";
			//工序
			String process = "丝印颜色";
			//线体
			String lineBody = "line1";
			//型号(C27)
			String model = split[0];
			//颜色
			String color = split[1];
			//生产DRI
			String produce_DRI = split[6];
			//班别
			String clazz = split[5];
			//日期
			String date = split[4];
		List<String> strings = Arrays.asList(factory, process, lineBody, model, color,produce_DRI,clazz,date);
		try {
			XSSFWorkbook wb = new XSSFWorkbook(is);
			ArrayList<DfAoiSl> resultList = new ArrayList<>();
			XSSFSheet Logo = wb.getSheet("单印LOGO");
			getDataByExcel(excelImportUtil, Logo,resultList,strings);
			XSSFSheet listOneBG = wb.getSheet("一层BG");
			getDataByExcel(excelImportUtil, listOneBG,resultList,strings);
			XSSFSheet listOneLOGO = wb.getSheet("一层LOGO  ");
			getDataByExcel(excelImportUtil, listOneLOGO,resultList,strings);
			XSSFSheet listTwoBG = wb.getSheet("二层BG");
			getDataByExcel(excelImportUtil, listTwoBG,resultList,strings);
			XSSFSheet sheetTwoLogo = wb.getSheet("二层LOGO  ");
			getDataByExcel(excelImportUtil, sheetTwoLogo,resultList,strings);
			XSSFSheet listThreeBG = wb.getSheet("三层 BG");
			getDataByExcel(excelImportUtil, listThreeBG,resultList,strings);
			XSSFSheet listThreeLOGO = wb.getSheet("三层LOGO ");
			getDataByExcel(excelImportUtil, listThreeLOGO,resultList,strings);
			XSSFSheet listFourBG = wb.getSheet("四层BG");
			getDataByExcel(excelImportUtil, listFourBG,resultList,strings);
			XSSFSheet listFourLOGO = wb.getSheet("四层LOGO ");
			getDataByExcel(excelImportUtil, listFourLOGO,resultList,strings);
			XSSFSheet listFiveBG = wb.getSheet("五层BG");
			getDataByExcel(excelImportUtil, listFiveBG,resultList,strings);
			XSSFSheet listFiveLOGO = wb.getSheet("五层LOGO   ");
			getDataByExcel(excelImportUtil, listFiveLOGO,resultList,strings);
			//终烤后BG
			XSSFSheet sheet = wb.getSheet("终烤后");
			getFinallyData(excelImportUtil, sheet,resultList,strings);
			return  resultList;
		}finally {
			is.close();
		}

	}
	//获取某个字符串第一次出现的位置
	public List<Integer> getFirstPosition(XSSFSheet sheet,ExcelImportUtil excelImportUtil,String name){
		ArrayList arrayList = new ArrayList<Integer>();
		for (int i = 0 ;i< sheet.getLastRowNum();i++){
			XSSFRow rowI = sheet.getRow(i);
			for (int j = 0 ; j < rowI.getLastCellNum(); j++){
				if (name.equals(excelImportUtil.parseExcel(rowI.getCell(j)))){
					arrayList.add(i);
					arrayList.add(j);
					break;
				}
			}
		}
		return arrayList;
	}

	private  List<DfAoiSl> getFinallyData(ExcelImportUtil excelImportUtil, XSSFSheet sheet, ArrayList<DfAoiSl> resultList, List<String> Strings) {
		Row row = sheet.getRow(0);
		int column = row.getPhysicalNumberOfCells();

		//获取首行头信息
		ArrayList<String> titleList = new ArrayList<>();
		for (int i = 0; i < column; i++) {
			if (row.getCell(i) != null){
				titleList.add(row.getCell(i).getStringCellValue().replaceAll("\\s+", ""));
			}else {
				column = i -1;
				break;
			}
		}
		//BG
		List<DfAoiSl> bg = getBGData(sheet, excelImportUtil, "BG", titleList,Strings);
		resultList.addAll(bg);
		List<DfAoiSl> logo = getBGData(sheet, excelImportUtil, "LOGO", titleList,Strings);
		resultList.addAll(logo);
		return resultList;
	}

	public List<DfAoiSl> getBGData( XSSFSheet sheet,ExcelImportUtil excelImportUtil,String type,ArrayList<String> titleList,List<String> strings){
		//黄色BG / LOGO 的行列数
		List<Integer> bg = getFirstPosition(sheet,excelImportUtil,type);
		ArrayList<Map<String,String>> listBytype = new ArrayList<>();
		for (int i = bg.get(0) + 1; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, String> map = new LinkedHashMap<>();
			for (int j = bg.get(1) + 1; j < bg.get(1) + 10; j++) {
				//获取每一列的数据值
				String str = excelImportUtil.parseExcel(row.getCell(j));
				//判断对应行的列值是否为空
				if (StringUtils.isNotBlank(str)) {
					//表头的标题为键值，列值为值
					map.put(titleList.get(j), str);
				}
			}
			//判段添加的对象是否为空
			if (!map.isEmpty()) {
				listBytype.add(map);
			}
		}

		//封装到实体
		List<DfAoiSl> collect = listBytype.stream().filter(Objects::nonNull).map(map -> {
			DfAoiSl sLEntity =null;

			sLEntity = new DfAoiSl();
			sLEntity.setType(sheet.getSheetName().replaceAll(" ","") + type);
			sLEntity.setGroupCharacteristics(map.get("群组特性"));
			sLEntity.setTime(Timestamp.valueOf(LocalDateTime.now()));
			sLEntity.setLf2(map.get("L*(F2)") == null?null:new BigDecimal(map.get("L*(F2)")));
			sLEntity.setAf2(map.get("a*(F2)") == null?null:new BigDecimal(map.get("a*(F2)")));
			sLEntity.setBf2(map.get("b*(F2)") == null?null:new BigDecimal(map.get("b*(F2)")));
			sLEntity.setDlf2(map.get("dL*(F2)") == null?null:new BigDecimal(map.get("dL*(F2)")));
			sLEntity.setDaf2(map.get("da*(F2)") == null?null:new BigDecimal(map.get("da*(F2)")));
			sLEntity.setDaf3(map.get("da*(F3)") == null?null:new BigDecimal(map.get("da*(F3)")));
			sLEntity.setDbf2(map.get("db*(F2)") == null?null:new BigDecimal(map.get("db*(F2)")));
			sLEntity.setDe94f2(map.get("dE*94(F2)") == null?null:new BigDecimal(map.get("dE*94(F2)")));
			sLEntity.setJudge(map.get("判断"));
			sLEntity.setFactory(strings.get(0));
			sLEntity.setProcess(strings.get(1));
			sLEntity.setLineBody(strings.get(2));
			sLEntity.setColor(strings.get(4));
			sLEntity.setProduceDri(strings.get(5));
			sLEntity.setClazz(strings.get(6));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
			Date parse = null;
			try {
				parse = format.parse(strings.get(7));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDateTime localDateTime = parse.toInstant().atZone(zoneId).toLocalDateTime();
			sLEntity.setTime(Timestamp.valueOf(localDateTime));
			return sLEntity;
		}).collect(Collectors.toList());
		return collect;
	}

	public void getDataByExcel(ExcelImportUtil excelImportUtil,XSSFSheet sheet,ArrayList<DfAoiSl> resultList,List<String> strings){
		Row row = sheet.getRow(0);
		int column = row.getPhysicalNumberOfCells();

		ArrayList<String> titleList = new ArrayList<>();
		for (int i = 0; i < column; i++) {
			if (row.getCell(i) != null){
				titleList.add(row.getCell(i).getStringCellValue().replaceAll("\\s+", ""));
			}else {
				column = i -1;
				break;
			}
		}
		//获取上下限数据
		//获取某个字符串第一次出现的位置
		int startRow = 0;
		int startColumn = 0;
		for (int i = 0 ;i< sheet.getLastRowNum();i++){
			XSSFRow rowI = sheet.getRow(i);
			for (int j = 0 ; j < rowI.getLastCellNum(); j++){
				if ("上限".equals(excelImportUtil.parseExcel(rowI.getCell(j)))){
					startRow = i;
					startColumn = j;
					break;
				}
			}
		}
		List<String> upDownValue = excelImportUtil.getCellData(sheet, startRow, startColumn + 1, 2, 3);
//

		//第二步骤:获取Data区
		ArrayList<Map<String,String>> list = new ArrayList<>();
		for (int i = 5; i < sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, String> map = new LinkedHashMap<>();
			for (int j = 0; j < column; j++) {
				//获取每一列的数据值
				String str = excelImportUtil.parseExcel(row.getCell(j));
				//判断对应行的列值是否为空
				if (StringUtils.isNotBlank(str)) {
					//表头的标题为键值，列值为值
					map.put(titleList.get(j), str);
				}
			}
			//判段添加的对象是否为空
			if (!map.isEmpty()) {
				list.add(map);
			}
		}
		//第三步:封装到实体
		List<DfAoiSl> collect = list.stream().filter(Objects::nonNull).map(map -> {
			DfAoiSl sLEntity =null;
			if (StringUtils.isNotEmpty(map.get("日期"))){
				sLEntity = new DfAoiSl();
				sLEntity.setFactory(strings.get(0));
				sLEntity.setProcess(strings.get(1));
				sLEntity.setLineBody(strings.get(2));
				sLEntity.setModel(strings.get(3));
				sLEntity.setColor(strings.get(4));
				sLEntity.setProduceDri(strings.get(5));
				sLEntity.setType(sheet.getSheetName().replaceAll(" ",""));
				sLEntity.setCreatetime(Timestamp.valueOf(LocalDateTime.now()));
				sLEntity.setDate(map.get("日期") == null ? null :Timestamp.valueOf(map.get("日期") + " 00:00:00"));
				sLEntity.setClazz(map.get("班别"));
				sLEntity.setTime(map.get("时间") == null || map.get("日期") == null ? null :Timestamp.valueOf(map.get("日期") + " " + map.get("时间")));
				sLEntity.setStage(map.get("阶段"));
				sLEntity.setFirstOrPoi(map.get("首件/巡检"));
				sLEntity.setGroupCharacteristics(map.get("群组特性"));
				sLEntity.setLf2(map.get("L*(F2)") == null?null:new BigDecimal(map.get("L*(F2)")));
				sLEntity.setAf2(map.get("a*(F2)") == null?null:new BigDecimal(map.get("a*(F2)")));
				sLEntity.setBf2(map.get("b*(F2)") == null?null:new BigDecimal(map.get("b*(F2)")));
				sLEntity.setDlf2(map.get("dL*(F2)") == null?null:new BigDecimal(map.get("dL*(F2)")));
				sLEntity.setDaf2(map.get("da*(F2)") == null?null:new BigDecimal(map.get("da*(F2)")));
				sLEntity.setDaf3(map.get("da*(F3)") == null?null:new BigDecimal(map.get("da*(F3)")));
				sLEntity.setDbf2(map.get("db*(F2)") == null?null:new BigDecimal(map.get("db*(F2)")));
				sLEntity.setDe94f2(map.get("dE*94(F2)") == null?null:new BigDecimal(map.get("dE*94(F2)")));
				sLEntity.setJudge(map.get("判断"));
				sLEntity.setLUp(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(0)));
				sLEntity.setLDown(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(3)));
				sLEntity.setAUp(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(1)));
				sLEntity.setADown(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(4)));
				sLEntity.setBUp(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(2)));
				sLEntity.setBDown(map.get("L*(F2)") == null?null:new BigDecimal(upDownValue.get(5)));
			}
			return sLEntity;
		}).collect(Collectors.toList());

		//第四步:去null
		List<DfAoiSl> result = collect.stream().filter(Objects::nonNull).collect(Collectors.toList());
//		resultList.addAll(result);
		//第五步:过滤出所有ng数据,放到df_audit_detail
		//查出数据库中l,a,b类型最大值分别为多少
		QueryWrapper<DfAoiSl> ew = new QueryWrapper<>();
		Rate3 rate3 = dfAoiSlMapper.selectNumberOfRepetitions(ew);
		int dl = rate3 == null? 0:rate3.getInte1();
		int da = rate3 == null? 0:rate3.getInte2();
		int db = rate3 == null? 0:rate3.getInte3();

		for (DfAoiSl ng : result) {
			dfAoiSlMapper.insert(ng);
			if (ng.getLUp() != null && ng.getLDown() != null && ng.getDlf2() != null) {
				if (ng.getLUp().compareTo(ng.getDlf2()) == -1 || ng.getLDown().compareTo(ng.getDlf2()) == 1) {
					//出现问题,dl类型最大重复次数+1,并更新
					ng.setNumberOfRepetitions(++dl);
					ng.setNgType("dl");
					dfAoiSlMapper.updateById(ng);
					//查询相关责任人
					QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
					sqw.eq("type", "丝印颜色");
					sqw.eq("problem_level", "1");
//                sqw.last("limit 1");
					if (TimeUtil.getBimonthly() == 0) {
						sqw.like("bimonthly", "双月");
					} else {
						sqw.like("bimonthly", "单月");
					}
					sqw.like("process_name", ng.getProcess());
					List<DfLiableMan> lm = dfLiableManService.list(sqw);
					if (lm.size() > 0) {
						StringBuilder manCode = new StringBuilder();
						StringBuilder manName = new StringBuilder();
						int manCount = 0;
						for (DfLiableMan l : lm) {
							if (manCount > 0) {
								manCode.append(",");
								manName.append(",");
							}
							manCode.append(l.getLiableManCode());
							manName.append(l.getLiableManName());
							manCount++;
						}
						DfAuditDetail aud = new DfAuditDetail();
						aud.setLine(ng.getLineBody());
						aud.setParentId(ng.getId());
						aud.setDataType("丝印颜色");
						aud.setDepartment(ng.getProcess());
						aud.setAffectMac("1");
						aud.setAffectNum(1.0);
						aud.setControlStandard("上限:" + ng.getLUp() + "下限:" + ng.getLDown());
						aud.setImpactType("丝印颜色");
						aud.setIsFaca("1");
						//设置问题名称
						aud.setQuestionName(ng.getLineBody() + ng.getType() + " DL " + "NG");
						aud.setProcess(ng.getProcess());
						aud.setProjectName(ng.getProject());
						aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
						aud.setOccurrenceTime(ng.getTime());
						aud.setIpqcNumber(TimeUtil.getNowTimeLong());
						//设置类型和级别
						aud.setQuestionType("CPK");
						aud.setDecisionLevel("Level1");
						aud.setHandlingSug("全检风险批");
						aud.setResponsible(manName.toString());
						aud.setResponsibleId(manCode.toString());
						aud.setNumberOfRepetitions(ng.getNumberOfRepetitions());
						aud.setColor(ng.getColor());
//                    aud.setCreateName(l.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
						dfAuditDetailService.save(aud);
						//设置FlowData
						DfFlowData fd = new DfFlowData();
						fd.setFlowLevel(1);
						fd.setDataType(aud.getDataType());
						fd.setFlowType(aud.getDataType());
						fd.setName("IPQC_丝印颜色_" + " DLNG_" + TimeUtil.getNowTimeByNormal());
						fd.setDataId(aud.getId());
						fd.setStatus("待确定");
						fd.setCreateName(aud.getCreateName());
						fd.setCreateUserId(aud.getCreateUserId());

						fd.setNowLevelUser(aud.getResponsibleId());
						fd.setNowLevelUserName(aud.getResponsible());
						fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

						fd.setFlowLevelName(aud.getDecisionLevel());
						QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
						atQw.eq("type", "丝印颜色");
						atQw.last("limit 1");
						DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
						if (null != at) {
							if (fd.getFlowLevelName().equals("Level1")) {
								fd.setReadTimeMax(at.getReadTimeLevel1());
								fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
							} else if (fd.getFlowLevelName().equals("Level2")) {
								fd.setReadTimeMax(at.getReadTimeLevel2());
								fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
							} else if (fd.getFlowLevelName().equals("Level3")) {
								fd.setReadTimeMax(at.getReadTimeLevel3());
								fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
							}
						}
						//设置显示人
						fd.setShowApprover(fd.getNowLevelUserName());
						dfFlowDataService.save(fd);
						continue;
					}
				}
			}

				if (ng.getAUp() != null && ng.getADown() != null && ng.getDaf2() != null) {
					if (ng.getAUp().compareTo(ng.getDaf2()) == -1 || ng.getADown().compareTo(ng.getDaf2()) == 1) {
						//出现问题,dl类型最大重复次数+1,并更新
						ng.setNumberOfRepetitions(++da);
						ng.setNgType("da");
						dfAoiSlMapper.updateById(ng);
						//查询相关责任人
						QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
						sqw.eq("type", "丝印颜色");
						sqw.eq("problem_level", "1");
//                sqw.last("limit 1");
						if (TimeUtil.getBimonthly() == 0) {
							sqw.like("bimonthly", "双月");
						} else {
							sqw.like("bimonthly", "单月");
						}
						sqw.like("process_name", ng.getProcess());
						List<DfLiableMan> lm = dfLiableManService.list(sqw);
						if (lm.size() > 0) {
							StringBuilder manCode = new StringBuilder();
							StringBuilder manName = new StringBuilder();
							int manCount = 0;
							for (DfLiableMan l : lm) {
								if (manCount > 0) {
									manCode.append(",");
									manName.append(",");
								}
								manCode.append(l.getLiableManCode());
								manName.append(l.getLiableManName());
								manCount++;
							}
							DfAuditDetail aud = new DfAuditDetail();
							aud.setLine(ng.getLineBody());
							aud.setParentId(ng.getId());
							aud.setDataType("丝印颜色");
							aud.setDepartment(ng.getProcess());
							aud.setAffectMac("1");
							aud.setAffectNum(1.0);
							aud.setControlStandard("上限:" + ng.getAUp() + "下限:" + ng.getADown());
							aud.setImpactType("丝印颜色");
							aud.setIsFaca("1");
							//设置问题名称
							aud.setQuestionName(ng.getLineBody() + ng.getType() + " DA " + "NG");
							aud.setProcess(ng.getProcess());
							aud.setProjectName(ng.getProject());
							aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
							aud.setOccurrenceTime(ng.getTime());
							aud.setIpqcNumber(TimeUtil.getNowTimeLong());
							//设置类型和级别
							aud.setQuestionType("CPK");
							aud.setDecisionLevel("Level1");
							aud.setHandlingSug("全检风险批");
							aud.setResponsible(manName.toString());
							aud.setResponsibleId(manCode.toString());
							aud.setNumberOfRepetitions(ng.getNumberOfRepetitions());
							aud.setColor(ng.getColor());
//                    aud.setCreateName(l.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
							dfAuditDetailService.save(aud);
							//设置FlowData
							DfFlowData fd = new DfFlowData();
							fd.setFlowLevel(1);
							fd.setDataType(aud.getDataType());
							fd.setFlowType(aud.getDataType());
							fd.setName("IPQC_丝印颜色_" + " DANG_" + TimeUtil.getNowTimeByNormal());
							fd.setDataId(aud.getId());
							fd.setStatus("待确定");
							fd.setCreateName(aud.getCreateName());
							fd.setCreateUserId(aud.getCreateUserId());

							fd.setNowLevelUser(aud.getResponsibleId());
							fd.setNowLevelUserName(aud.getResponsible());
							fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

							fd.setFlowLevelName(aud.getDecisionLevel());
							QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
							atQw.eq("type", "丝印颜色");
							atQw.last("limit 1");
							DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
							if (null != at) {
								if (fd.getFlowLevelName().equals("Level1")) {
									fd.setReadTimeMax(at.getReadTimeLevel1());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
								} else if (fd.getFlowLevelName().equals("Level2")) {
									fd.setReadTimeMax(at.getReadTimeLevel2());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
								} else if (fd.getFlowLevelName().equals("Level3")) {
									fd.setReadTimeMax(at.getReadTimeLevel3());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
								}
							}
							//设置显示人
							fd.setShowApprover(fd.getNowLevelUserName());
							dfFlowDataService.save(fd);
							continue;
						}
					}
				}
				if (ng.getBUp() != null && ng.getBDown() != null && ng.getDbf2() != null) {
					if (ng.getBUp().compareTo(ng.getDbf2()) == -1 || ng.getBDown().compareTo(ng.getDbf2()) == 1) {
						//出现问题,dl类型最大重复次数+1,并更新
						ng.setNumberOfRepetitions(++db);
						ng.setNgType("db");
						dfAoiSlMapper.updateById(ng);
						//查询相关责任人
						QueryWrapper<DfLiableMan> sqw = new QueryWrapper<>();
						sqw.eq("type", "丝印颜色");
						sqw.eq("problem_level", "1");
//                sqw.last("limit 1");
						if (TimeUtil.getBimonthly() == 0) {
							sqw.like("bimonthly", "双月");
						} else {
							sqw.like("bimonthly", "单月");
						}
						sqw.like("process_name", ng.getProcess());
						List<DfLiableMan> lm = dfLiableManService.list(sqw);
						if (lm.size() > 0) {
							StringBuilder manCode = new StringBuilder();
							StringBuilder manName = new StringBuilder();
							int manCount = 0;
							for (DfLiableMan l : lm) {
								if (manCount > 0) {
									manCode.append(",");
									manName.append(",");
								}
								manCode.append(l.getLiableManCode());
								manName.append(l.getLiableManName());
								manCount++;
							}
							DfAuditDetail aud = new DfAuditDetail();
							aud.setLine(ng.getLineBody());
							aud.setParentId(ng.getId());
							aud.setDataType("丝印颜色");
							aud.setDepartment(ng.getProcess());
							aud.setAffectMac("1");
							aud.setAffectNum(1.0);
							aud.setControlStandard("上限:" + ng.getBUp() + "下限:" + ng.getBDown());
							aud.setImpactType("丝印颜色");
							aud.setIsFaca("1");
							//设置问题名称
							aud.setQuestionName(ng.getLineBody() + ng.getType() + " DB " + "NG");
							aud.setProcess(ng.getProcess());
							aud.setProjectName(ng.getProject());
							aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
							aud.setOccurrenceTime(ng.getTime());
							aud.setIpqcNumber(TimeUtil.getNowTimeLong());
							//设置类型和级别
							aud.setQuestionType("CPK");
							aud.setDecisionLevel("Level1");
							aud.setHandlingSug("全检风险批");
							aud.setResponsible(manName.toString());
							aud.setResponsibleId(manCode.toString());
							aud.setNumberOfRepetitions(ng.getNumberOfRepetitions());
							aud.setColor(ng.getColor());
//                    aud.setCreateName(l.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
							dfAuditDetailService.save(aud);
							//设置FlowData
							DfFlowData fd = new DfFlowData();
							fd.setFlowLevel(1);
							fd.setDataType(aud.getDataType());
							fd.setFlowType(aud.getDataType());
							fd.setName("IPQC_丝印颜色_" + " DANG_" + TimeUtil.getNowTimeByNormal());
							fd.setDataId(aud.getId());
							fd.setStatus("待确定");
							fd.setCreateName(aud.getCreateName());
							fd.setCreateUserId(aud.getCreateUserId());

							fd.setNowLevelUser(aud.getResponsibleId());
							fd.setNowLevelUserName(aud.getResponsible());
							fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

							fd.setFlowLevelName(aud.getDecisionLevel());
							QueryWrapper<DfApprovalTime> atQw = new QueryWrapper<>();
							atQw.eq("type", "丝印颜色");
							atQw.last("limit 1");
							DfApprovalTime at = dfApprovalTimeService.getOne(atQw);
							if (null != at) {
								if (fd.getFlowLevelName().equals("Level1")) {
									fd.setReadTimeMax(at.getReadTimeLevel1());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
								} else if (fd.getFlowLevelName().equals("Level2")) {
									fd.setReadTimeMax(at.getReadTimeLevel2());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
								} else if (fd.getFlowLevelName().equals("Level3")) {
									fd.setReadTimeMax(at.getReadTimeLevel3());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
								}
							}
							//设置显示人
							fd.setShowApprover(fd.getNowLevelUserName());
							dfFlowDataService.save(fd);
							continue;
						}
					}
				}
			}
	}


}
