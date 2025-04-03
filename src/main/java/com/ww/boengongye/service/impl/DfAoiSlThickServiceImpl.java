package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfAoiSlThickMapper;
import com.ww.boengongye.mapper.DfAuditDetailMapper;
import com.ww.boengongye.mapper.DfFlowDataMapper;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 丝印-厚度 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
@Service
public class DfAoiSlThickServiceImpl extends ServiceImpl<DfAoiSlThickMapper, DfAoiSlThick> implements DfAoiSlThickService {
	@Autowired
	private DfAoiSlThickMapper dfAoiSlThickMapper;
	@Autowired
	private DfAoiSlThickMapper dfAoiSlThickService;

	@Autowired
	private DfAuditDetailMapper dfAuditDetailMapper;
	@Autowired
	private DfAuditDetailService dfAuditDetailService;
	@Autowired
	private DfFlowDataMapper dfFlowDataMapper;
	@Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfLiableManService dfLiableManService;
	@Autowired
	private DfApprovalTimeService dfApprovalTimeService;

	@Override
	public int importExcel(MultipartFile file) throws Exception {
		String filename = file.getOriginalFilename();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		String[] split = filename.split("_");
		//工厂
		String factory = split[0];
		//工序
		String process = "丝印厚度";
		//线体
		String lineBody = "LINE_1";
		//项目
		String project = "C27";
		//颜色
		String color = "蓝色";
		//生产DRI
		String produceDRI = "黎明";
		String date = "2023-" + filename.split(" ")[5] + " ";

		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 5);


		String faiName = strings[0][1];
		String name = strings[1][1];
		Double standard = strings[2][1] == null ? null : Double.valueOf(strings[2][1]);
		Double up = strings[3][1] == null ? null : Double.valueOf(strings[3][1]);
		Double down = strings[4][1] == null ? null : Double.valueOf(strings[4][1]);
		Double isolationUp = strings[4][1] == null ? null : Double.valueOf(strings[5][1]);
		Double isolationdown = strings[4][1] == null ? null : Double.valueOf(strings[6][1]);
		int count = 0;
		ArrayList<DfAoiSlThick> thicks = new ArrayList<>();
		for (int i = 7; i < strings.length; i++) {
			if (StringUtils.isEmpty(strings[i][0])) {
				continue;
			}
			Date parse = sd.parse(date + strings[i][0]);
			Timestamp time = new Timestamp(parse.getTime());
			Double checkValue = strings[i][1] == null ? null : Double.valueOf(strings[i][1]);
			String status = strings[i][2];
			String position = strings[i][3];
			String machineCode = strings[i][4];

			DfAoiSlThick thick = new DfAoiSlThick();
			thick.setCheckTime(time);
			thick.setFactory(factory);
			thick.setLineBody(lineBody);
			thick.setProject(project);
			thick.setColor(color);
//			thick.setModel();
			thick.setUserName(produceDRI);
			thick.setFaiName(faiName);
			thick.setName(name);
			thick.setStandard(standard);
			thick.setUp(up);
			thick.setDown(down);
			thick.setIsolationUp(isolationUp);
			thick.setIsolationDown(isolationdown);
			thick.setStatus(status);
			thick.setPosition(position);
			thick.setMachineCode(machineCode);
			thick.setCheckValue(checkValue);
			thick.setProcess("丝印厚度");
			thicks.add(thick);
			count++;
		}
		//查询本类型存在的最大重复发生次数
		QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
		ew.select("max(if(name = '" + name +"',number_of_repetitions,0)) as number_of_repetitions");
		DfAoiSlThick thick = dfAoiSlThickMapper.selectOne(ew);
		int max= 0;
		if (thick != null){
			max = thick.getNumberOfRepetitions();
		}
		//检测NG,flow_data 和 audit_detail添加数据
		for (DfAoiSlThick dfAoiSlThick : thicks) {
			if (dfAoiSlThick.getCheckValue() > dfAoiSlThick.getUp() || dfAoiSlThick.getCheckValue() < dfAoiSlThick.getDown()){
//				dfAoiSlThickMapper.insert(dfAoiSlThick);
//
//				DfAuditDetail dfAuditDetail = new DfAuditDetail();
//				dfAuditDetail.setProjectName(dfAoiSlThick.getProject());
//				dfAuditDetail.setDepartment(dfAoiSlThick.getFactory() + dfAoiSlThick.getName());
//				dfAuditDetail.setIpqcNumber("IPQC_" + LocalDateTime.now());
//				dfAuditDetail.setQuestionName(dfAoiSlThick.getLineBody() + dfAoiSlThick.getName() + "NG" );
//				dfAuditDetail.setReportMan(dfAoiSlThick.getUserName());
//				dfAuditDetail.setReportTime(Timestamp.valueOf(LocalDateTime.now()));
//				dfAuditDetail.setControlStandard("上限:" + up + "下限:" + down);
//				dfAuditDetail.setAffectMac(dfAoiSlThick.getMachineCode());
//				dfAuditDetail.setDecisionLevel("level1");
//				dfAuditDetail.setOccurrenceTime(dfAoiSlThick.getCheckTime());
//				dfAuditDetail.setFactory(dfAoiSlThick.getFactory());
//				dfAuditDetail.setProject(dfAoiSlThick.getProject());
//				dfAuditDetail.setLine(dfAoiSlThick.getLineBody());
//				dfAuditDetail.setProcess("丝印");
//				dfAuditDetail.setDataType("丝印厚度");
//				dfAuditDetailMapper.insert(dfAuditDetail);
//
//				//查询责任人
//				QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
//				sqw.eq("type","size");
//				sqw.eq("problem_level","2");
////                sqw.last("limit 1");
//				if(TimeUtil.getBimonthly()==0){
//					sqw.like("bimonthly","双月");
//				}else{
//					sqw.like("bimonthly","单月");
//				}
//				sqw.like("process_name",dfAuditDetail.getProcess());
//				List<DfLiableMan> lm =dfLiableManService.list(sqw);
				dfAoiSlThick.setNumberOfRepetitions(++max);
				dfAoiSlThickMapper.insert(dfAoiSlThick);

				QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
				sqw.eq("type","丝印厚度");
				sqw.eq("problem_level","1");
//                sqw.last("limit 1");
				if(TimeUtil.getBimonthly()==0){
					sqw.like("bimonthly","双月");
				}else{
					sqw.like("bimonthly","单月");
				}
				sqw.like("process_name",dfAoiSlThick.getProcess());
				List<DfLiableMan> lm =dfLiableManService.list(sqw);
				if(lm.size()>0){
					StringBuilder manCode=new StringBuilder();
					StringBuilder manName=new StringBuilder();
					int manCount=0;
					for(DfLiableMan l:lm){
						if(manCount>0){
							manCode.append(",");
							manName.append(",");
						}
						manCode.append(l.getLiableManCode());
						manName.append(l.getLiableManName());
						manCount++;
					}
					DfAuditDetail aud=new DfAuditDetail();
					aud.setLine(dfAoiSlThick.getLineBody());
					aud.setParentId(dfAoiSlThick.getId());
					aud.setDataType("丝印厚度");
					aud.setDepartment(dfAoiSlThick.getProcess());
					aud.setAffectMac("1");
					aud.setAffectNum(1.0);
					aud.setControlStandard("上限:" + up + "下限:" + down);
					aud.setImpactType("丝印厚度");
					aud.setIsFaca("0");
					//问题名称和现场实际调换
					aud.setQuestionName(dfAoiSlThick.getLineBody() + dfAoiSlThick.getName() + "NG" );
//					aud.setScenePractical(questionName.toString());
					aud.setProcess(dfAoiSlThick.getProcess());
					aud.setProjectName(dfAoiSlThick.getProject());
//					QueryWrapper<DfLiableMan> sqw2=new QueryWrapper<>();
//					sqw2.eq("type","sizeInitiator");
//					sqw2.eq("problem_level","2");
//					sqw2.like("process_name",dd.getProcess());
//					sqw2.last("limit 1");
//					if(TimeUtil.getDayShift()==1){
//						sqw2.like("bimonthly","双月");
//					}else{
//						sqw2.like("bimonthly","单月");
//					}
//					DfLiableMan rpm =dfLiableManService.getOne(sqw2);
//					if(null!=rpm){
//						aud.setReportMan(rpm.getLiableManName());
//						aud.setCreateName(rpm.getLiableManName());
//						aud.setCreateUserId(rpm.getLiableManCode());
//					}

					aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
					aud.setOccurrenceTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
					aud.setIpqcNumber(TimeUtil.getNowTimeLong());
					//由于MQ没有返回对应的检测类型,现在默认为过程检CPK
//					if(null!=dd.getCheckType()&&!dd.getCheckType().equals("")){
//						if(dd.getCheckType().equals("1")){
//
//						}else if(dd.getCheckType().equals("3")){
//							aud.setQuestionType("FAI");
//						}else {
//							aud.setQuestionType("CPK");
//						}
//					}else{
//						aud.setQuestionType("CPK");
//					}

					aud.setQuestionType("丝印厚度");
					aud.setDecisionLevel("Level1");
					aud.setHandlingSug("全检风险批");
					aud.setResponsible(manName.toString());
					aud.setResponsibleId(manCode.toString());
//                    aud.setCreateName(l.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
					dfAuditDetailService.save(aud);

					DfFlowData fd = new DfFlowData();
					fd.setFlowLevel(1);
					fd.setDataType(aud.getDataType());
					fd.setFlowType(aud.getDataType());
					fd.setName("IPQC_丝印厚度_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
					fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
					fd.setStatus("待确定");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
					fd.setCreateName(aud.getCreateName());
					fd.setCreateUserId(aud.getCreateUserId());

					fd.setNowLevelUser(aud.getResponsibleId());
					fd.setNowLevelUserName(aud.getResponsible());
					fd.setLevel2PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

					fd.setFlowLevelName(aud.getDecisionLevel());
					QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
					atQw.eq("type","丝印厚度");
					atQw.last("limit 1");
					DfApprovalTime at= dfApprovalTimeService.getOne(atQw);
					if(null!=at){
						if(fd.getFlowLevelName().equals("Level1")){
							fd.setReadTimeMax(at.getReadTimeLevel1());
							fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
						}else  if(fd.getFlowLevelName().equals("Level2")){
							fd.setReadTimeMax(at.getReadTimeLevel2());
							fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
						}else  if(fd.getFlowLevelName().equals("Level3")){
							fd.setReadTimeMax(at.getReadTimeLevel3());
							fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
						}
					}
					//设置显示人
					fd.setShowApprover(fd.getNowLevelUserName());
					dfFlowDataService.save(fd);
				}
			}
			else {
				dfAoiSlThickMapper.insert(dfAoiSlThick);
			}
		}
		return count;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int importExcel2(MultipartFile file) throws Exception {
		String filename = file.getOriginalFilename();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-M-dd HH:mm");
		String[] split = filename.split("_");
		//工厂
		String factory = "J10-1";
		//工序
		String process = "丝印厚度";
		//线体
		String lineBody = "LINE_1";
		//项目
		String project = "C27";
		//颜色
		String color = "蓝色";
		//生产DRI
		String produceDRI = "黎明";
//		String date = "2023-" + filename.split(" ")[6] + " ";
		ExcelImportUtil excel = new ExcelImportUtil(file);
		String[][] strings = excel.readExcelBlock(1, -1, 1, 29);

		int count = 0;
		//每一项的列的位置
		List<Integer> integers = Arrays.asList(1, 5, 9, 13, 17, 21, 25);
		for (int i = 7 ; i < strings.length ; i++){
			for (Integer integer : integers) {
				String faiName = strings[i][0];
				String name = strings[0][integer];
				Double standard = strings[1][integer] == null ? null : Double.valueOf(strings[1][integer]);
				Double up = strings[2][integer] == null ? null : Double.valueOf(strings[2][integer]);
				Double down = strings[3][integer] == null ? null : Double.valueOf(strings[3][integer]);
				Double isolationUp = strings[4][integer] == null ? null : Double.valueOf(strings[4][integer]);
				Double isolationdown = strings[5][integer] == null ? null : Double.valueOf(strings[5][integer]);

				if (StringUtils.isEmpty(strings[i][integer]) || StringUtils.isEmpty(strings[i][integer + 1]) || StringUtils.isEmpty(strings[i][integer + 2])) {
					continue;
				}

				String checkTime = strings[i][integer];
				Date parse = sd.parse(checkTime);
//				Date parse = sd.parse(date + strings[i][integer]);
				Timestamp time = new Timestamp(parse.getTime());
				Double checkValue = strings[i][integer + 1] == null ? null : Double.valueOf(strings[i][integer + 1]);
				String status = strings[i][integer + 2];
				String position = faiName;
				String machineCode = strings[i][integer + 3];

				DfAoiSlThick thick = new DfAoiSlThick();
				thick.setCheckTime(time);
				thick.setFactory(factory);
				thick.setLineBody(lineBody);
				thick.setProject(project);
				thick.setColor(color);
//			thick.setModel();
				thick.setUserName(produceDRI);
				thick.setFaiName(faiName);
				thick.setName(name);
				thick.setStandard(standard);
				thick.setUp(up);
				thick.setDown(down);
				thick.setIsolationUp(isolationUp);
				thick.setIsolationDown(isolationdown);
				thick.setStatus(status);
				thick.setPosition(position);
				thick.setMachineCode(machineCode);
				thick.setCheckValue(checkValue);
				thick.setProcess(process);
				dfAoiSlThickMapper.insert(thick);
				//判断是否发生NG

				if (thick.getCheckValue() > thick.getUp() || thick.getCheckValue() < thick.getDown()){
					//查询本类型存在的最大重复发生次数
					QueryWrapper<DfAoiSlThick> ew = new QueryWrapper<>();
					ew.select("max(if(name = '" + name +"',number_of_repetitions,0)) as number_of_repetitions");
					DfAoiSlThick maxOne = dfAoiSlThickMapper.selectOne(ew);
						Integer max = maxOne.getNumberOfRepetitions();
						thick.setNumberOfRepetitions(max == null? 0:++max);
						//更新为最大重复次数
						dfAoiSlThickService.updateById(thick);

						QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
						sqw.eq("type","丝印厚度");
						sqw.eq("problem_level","1");
//                sqw.last("limit 1");
						if(TimeUtil.getBimonthly()==0){
							sqw.like("bimonthly","双月");
						}else{
							sqw.like("bimonthly","单月");
						}
						sqw.like("process_name",thick.getProcess());
						List<DfLiableMan> lm =dfLiableManService.list(sqw);
						if(lm.size()>0){
							StringBuilder manCode=new StringBuilder();
							StringBuilder manName=new StringBuilder();
							int manCount=0;
							for(DfLiableMan l:lm){
								if(manCount>0){
									manCode.append(",");
									manName.append(",");
								}
								manCode.append(l.getLiableManCode());
								manName.append(l.getLiableManName());
								manCount++;
							}
							DfAuditDetail aud=new DfAuditDetail();
							aud.setLine(thick.getLineBody());
							aud.setParentId(thick.getId());
							aud.setDataType("丝印厚度");
							aud.setDepartment(thick.getProcess());
							aud.setAffectMac("1");
							aud.setAffectNum(Double.valueOf("1"));
							aud.setControlStandard("上限:" + up + "下限:" + down);
							aud.setImpactType("丝印厚度");
							aud.setIsFaca("1");
							//问题名称和现场实际调换
							aud.setQuestionName(thick.getLineBody() + thick.getName() + "NG");
							aud.setScenePractical(thick.getLineBody() + thick.getName() + "NG");
							aud.setProcess(thick.getProcess());
							aud.setProjectName(thick.getProject());
							QueryWrapper<DfLiableMan> sqw2=new QueryWrapper<>();
//					sqw2.eq("type","sizeInitiator");
//					sqw2.eq("problem_level","2");
//					sqw2.like("process_name",dd.getProcess());
//					sqw2.last("limit 1");
//					if(TimeUtil.getDayShift()==1){
//						sqw2.like("bimonthly","双月");
//					}else{
//						sqw2.like("bimonthly","单月");
//					}
//					DfLiableMan rpm =dfLiableManService.getOne(sqw2);
//					if(null!=rpm){
//						aud.setReportMan(rpm.getLiableManName());
//						aud.setCreateName(rpm.getLiableManName());
//						aud.setCreateUserId(rpm.getLiableManCode());
//					}

							aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
							aud.setOccurrenceTime(thick.getCheckTime());
							aud.setIpqcNumber(TimeUtil.getNowTimeLong());
							//由于MQ没有返回对应的检测类型,现在默认为过程检CPK
//					if(null!=dd.getCheckType()&&!dd.getCheckType().equals("")){
//						if(dd.getCheckType().equals("1")){
//
//						}else if(dd.getCheckType().equals("3")){
//							aud.setQuestionType("FAI");
//						}else {
//							aud.setQuestionType("CPK");
//						}
//					}else{
//						aud.setQuestionType("CPK");
//					}

							aud.setQuestionType("CPK");
							aud.setDecisionLevel("Level1");
							aud.setHandlingSug("隔离,全检");
							aud.setResponsible(manName.toString());
							aud.setResponsibleId(manCode.toString());
							aud.setNumberOfRepetitions(thick.getNumberOfRepetitions());
							aud.setColor(thick.getColor());
//                    aud.setCreateName(l.getLiableManName());
//                    aud.setCreateUserId(lm.getLiableManCode());
							dfAuditDetailService.save(aud);

							DfFlowData fd = new DfFlowData();
							fd.setFlowLevel(1);
							fd.setDataType(aud.getDataType());
							fd.setFlowType(aud.getDataType());
							fd.setName("IPQC_丝印厚度_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
							fd.setDataId(aud.getId());
//                    fd.setFlowLevelName(fb.getName());
							fd.setStatus("待确定");
//                    fd.setValidTime(new Timestamp(TimeUtil.getValidTime(fb.getValidTime()).getTime()));
							fd.setCreateName(aud.getCreateName());
							fd.setCreateUserId(aud.getCreateUserId());

							fd.setNowLevelUser(aud.getResponsibleId());
							fd.setNowLevelUserName(aud.getResponsible());
							fd.setLevel1PushTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));

							fd.setFlowLevelName(aud.getDecisionLevel());
							QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
							atQw.eq("type","丝印厚度");
							atQw.last("limit 1");
							DfApprovalTime at= dfApprovalTimeService.getOne(atQw);
							if(null!=at){
								if(fd.getFlowLevelName().equals("Level1")){
									fd.setReadTimeMax(at.getReadTimeLevel1());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel1());
								}else  if(fd.getFlowLevelName().equals("Level2")){
									fd.setReadTimeMax(at.getReadTimeLevel2());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel2());
								}else  if(fd.getFlowLevelName().equals("Level3")){
									fd.setReadTimeMax(at.getReadTimeLevel3());
									fd.setDisposeTimeMax(at.getDisposeTimeLevel3());
								}
							}
							//设置显示人
							fd.setShowApprover(fd.getNowLevelUserName());
							dfFlowDataService.save(fd);
						}
					}
			}

			count ++;
		}
		return count;
	}

	@Override
	public List<SlCloseEntity> getCloseData1(QueryWrapper<DfAoiSlThick> ew) {
		return dfAoiSlThickMapper.getCloseData1(ew);
	}

	@Override
	public List<SlCloseEntity> getCloseDataZ(QueryWrapper<DfAoiSlThick> ew) {
		return dfAoiSlThickMapper.getCloseDataZ(ew);
	}

	@Override
	public List<Rate3> getworstLine(QueryWrapper<DfAoiSlThick> ew) {
		return dfAoiSlThickMapper.getworstLine(ew);
	}

	@Override
	public List<Rate3> getCloseUp(QueryWrapper<DfAoiSlThick> ew) {
		return dfAoiSlThickMapper.getCloseUp(ew);
	}
}
