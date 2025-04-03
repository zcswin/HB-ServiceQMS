package com.ww.boengongye.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.*;
import com.ww.boengongye.utils.DynamicIpqcUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Component
public class GrindingLife {
    @Autowired
    private DfYieldWarnService dfYieldWarnService;
    @Autowired
    private DfKnifStatusService dfKnifStatusService;
    @Autowired
	private DfAuditDetailService dfAuditDetailService;
    @Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfProcessService dfProcessService;
	@Autowired
	private DfKnifStatusType6Service dfKnifStatusType6Service;

	@Autowired
	private DfLiableManService dfLiableManService;

	@Autowired
	private DfApprovalTimeService dfApprovalTimeService;
	@Autowired
	private Environment env;
	@Resource
	private RedisTemplate redisTemplate;


	@JmsListener(destination = "${GrindingLifeTopic}", containerFactory = "jtJmsListenerContainerFactoryTopic")
	@Transactional(rollbackFor = Exception.class)
    public void consume(final String msg) {
		System.out.println("砂轮寿命预警 start***************************************");
		//获取当前检测时间
		String checkTime = TimeUtil.getNowTimeByNormal();
		//当前时间戳
		String checkTimeLong = TimeUtil.getNowTimeLong();
		//json字符串转砂轮对象
        DfKnifStatus dfKnifStatus = JSON.parseObject(msg, com.ww.boengongye.entity.DfKnifStatus.class);

        //从yield_warn获取预警和报警范围
		UpdateWrapper<DfYieldWarn> ew = new UpdateWrapper<>();
        ew.eq("type","砂轮" );
        DfYieldWarn warn = dfYieldWarnService.getOne(ew);
        String typeData = dfKnifStatus.getTypeData();

        switch (typeData){
            case "101":
            	//通过机台编号的首字母查找对应的工序
				String firstCode1 = dfKnifStatus.getMachineCode().substring(0, 1);
				QueryWrapper<DfProcess> dfProcessQueryWrapper = new QueryWrapper<>();
				dfProcessQueryWrapper.eq("first_code",firstCode1);
				DfProcess dfProcess1 = dfProcessService.getOne(dfProcessQueryWrapper);
				String process1 = dfProcess1.getProcessName();

                //根据机台编号首字母查对应工序,EventType是1的时候为换刀
				if ("2".equals(dfKnifStatus.getEventType())){//表示换刀
//					if (env.getProperty("ISIPQC", "N").equals("Y") && (process1.equals("CNC3") && (env.getProperty("IPQCMac", "all").contains(dfKnifStatus.getMachineCode())))) {
//
//						Gson gson = new GsonBuilder().setPrettyPrinting().create();
//						ValueOperations valueOperations = redisTemplate.opsForValue();
//						//缓存刀具使用信息
//						DfKnifStatus tool = new DfKnifStatus();
//						tool.setMachineCode(dfKnifStatus.getMachineCode());
//						tool.setNNumTool(dfKnifStatus.getNNumTool());
//						tool.setToolCutNum(0);
//						tool.setCheckTime(TimeUtil.getNowTimeByNormal());
//						redisTemplate.opsForValue().set("IpqcGrinding:" + dfKnifStatus.getMachineCode()+":"+dfKnifStatus.getNNumTool(),gson.toJson(tool));
//
//
//						Object v =  valueOperations.get("IpqcAppearance:" + dfKnifStatus.getMachineCode());
//						if(null!=v) {
//							DynamicIpqcMac dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
//							//动态IPQC换刀
//							DynamicIpqcUtil.sendMes(1.0,"外观","更换耗材","收严",process1,dfKnifStatus.getMachineCode(),0,"C98B","");
//							dim.setSenMesCount(1);
//							dim.setNowCount(0);
////                                            dim.setTotalCount(2);
//							dim.setSpecifiedCount(2);
//							dim.setFrequency(1.0);
//							dim.setRuleName("更换耗材");
//
//							dim.setUpdateTime(new Date().getTime());
//							dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//
//							redisTemplate.opsForValue().set("IpqcAppearance:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//						}else{
//							DynamicIpqcMac dim=new DynamicIpqcMac();
//							dim.setMachineCode( dfKnifStatus.getMachineCode());
//							dim.setNgCount(0);
//							dim.setTotalCount(0);
//							DynamicIpqcUtil.sendMes(1.0,"外观","更换耗材","收严",process1,dfKnifStatus.getMachineCode(),0,"C98B","");
//							dim.setSenMesCount(1);
//							dim.setNowCount(0);
//							dim.setSpecifiedCount(2);
//							dim.setFrequency(1.0);
//							dim.setRuleName("更换耗材");
//
//							dim.setSenMesCount(1);
//							dim.setAppearanceOkCount(0);
//							dim.setCpkCount(0);
//							dim.setFourPointOverOne(0);
//							dim.setTwoPointOverTwo(0);
//							dim.setZugammenCount(0);
//							dim.setSpcOkCount(0);
//
//							dim.setUpdateTime(new Date().getTime());
//							dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//							redisTemplate.opsForValue().set("IpqcAppearance:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//						}
//						Object v2 =  valueOperations.get("IpqcSize:" + dfKnifStatus.getMachineCode());
//						if(null!=v2) {
//							DynamicIpqcMac dim = new Gson().fromJson(v2.toString(), DynamicIpqcMac.class);
//							//动态IPQC换刀
//							DynamicIpqcUtil.sendMes(1.0,"尺寸","更换耗材","收严",process1,dfKnifStatus.getMachineCode(),0,"C98B","");
//							dim.setSenMesCount(1);
//							dim.setNowCount(0);
////                                            dim.setTotalCount(2);
//							dim.setSpecifiedCount(2);
//							dim.setFrequency(1.0);
//							dim.setRuleName("更换耗材");
//							dim.setUpdateTime(new Date().getTime());
//							dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//
//							redisTemplate.opsForValue().set("IpqcSize:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//						}else{
//							DynamicIpqcMac dim=new DynamicIpqcMac();
//							dim.setMachineCode( dfKnifStatus.getMachineCode());
//							dim.setNgCount(0);
//							dim.setTotalCount(0);
//							DynamicIpqcUtil.sendMes(1.0,"尺寸","更换耗材","收严",process1,dfKnifStatus.getMachineCode(),0,"C98B","");
//							dim.setSenMesCount(1);
//							dim.setNowCount(0);
//							dim.setSpecifiedCount(2);
//							dim.setFrequency(1.0);
//							dim.setRuleName("更换耗材");
//
//							dim.setSenMesCount(1);
//							dim.setAppearanceOkCount(0);
//							dim.setCpkCount(0);
//							dim.setFourPointOverOne(0);
//							dim.setTwoPointOverTwo(0);
//							dim.setZugammenCount(0);
//							dim.setSpcOkCount(0);
//
//							dim.setUpdateTime(new Date().getTime());
//							dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//							redisTemplate.opsForValue().set("IpqcSize:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//						}
//
//					}
//


					//找到最新的切削次数
					QueryWrapper<DfKnifStatusType6> ew2 = new QueryWrapper<>();
					ew2.eq(StringUtils.isNotEmpty(dfKnifStatus.getMachineCode()), "mac_code",dfKnifStatus.getMachineCode())
						.eq(StringUtils.isNotEmpty(dfKnifStatus.getNNumTool()), "n_num_tool", dfKnifStatus.getNNumTool())
						.orderByDesc("create_time")
						.last("limit 1");
					DfKnifStatusType6 one1 = dfKnifStatusType6Service.getOne(ew2);
					if (one1 != null){
						dfKnifStatus.setToolCutNum(one1.getToolCutNum());
					}
				}
				dfKnifStatus.setProcess(process1);
				//保存机台绑定哪个刀具号和对应工序
				dfKnifStatusService.save(dfKnifStatus);
                break;
            case "102":
                break;
            case "103":
                break;
            case "104":
                break;
            case "105":
                break;
            case "106":
				String firstCode = dfKnifStatus.getMacCode().substring(0, 1);
				QueryWrapper<DfProcess> dfProcessQueryWrapper6 = new QueryWrapper<>();
				dfProcessQueryWrapper6.eq("first_code",firstCode);
				DfProcess dfProcess6 = dfProcessService.getOne(dfProcessQueryWrapper6);
				//工序
				String process6 = dfProcess6.getProcessName();

//				if(process6.equals("CNC3")){
//					Gson gson = new GsonBuilder().setPrettyPrinting().create();
//					ValueOperations valueOperations = redisTemplate.opsForValue();
//					Object v =  valueOperations.get("IpqcAppearance:" + dfKnifStatus.getMachineCode());
//					DynamicIpqcMac dim=new DynamicIpqcMac();
//					if(null!=v) {
//						 dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
//					}else {
//						dim.setMachineCode(dfKnifStatus.getMachineCode());
//						dim.setRuleName("QCP抽检频率");
//						dim.setSenMesCount(1);
//						dim.setNowCount(0);
//						dim.setSpecifiedCount(1);
//						dim.setFrequency(2.0);
//					}
//						if(!dim.getRuleName().equals("耗材寿命衰减")){
//							UpdateWrapper<DfYieldWarn> ew2 = new UpdateWrapper<>();
//							ew2.eq("type","砂轮标准" );
//							ew2.eq("name","砂轮寿命标准" );
//							ew2.eq("process",process6 );
//
//							DfYieldWarn warn2 = dfYieldWarnService.getOne(ew);
//
//							UpdateWrapper<DfYieldWarn> ew3 = new UpdateWrapper<>();
//							ew3.eq("type","砂轮标准" );
//							ew3.eq("name","砂轮寿命收严" );
//							ew3.eq("process",process6 );
//
//							DfYieldWarn warn3 = dfYieldWarnService.getOne(ew);
//							List<NumTool> detailList = dfKnifStatus.getDetail();
//
//							int checkResult=0;
//							//判断触发预警还是报警
//							for (NumTool numTool : detailList) {
//								if(1-(numTool.getToolCutNum()/warn2.getAlarmValue()*100)<warn3.getAlarmValue()){
//									checkResult=1;
//									break;
//								}
//
//							}
//							if(checkResult>0){
//								//动态IPQC耗材寿命判定
//								DynamicIpqcUtil.sendMes(1.0,"外观","耗材寿命衰减","收严",process6,dfKnifStatus.getMachineCode(),0,"C98B","");
//								DynamicIpqcUtil.sendMes(1.0,"尺寸","耗材寿命衰减","收严",process6,dfKnifStatus.getMachineCode(),0,"C98B","");
//
//								dim.setSenMesCount(1);
//								dim.setNowCount(0);
////                                            dim.setTotalCount(2);
//								dim.setSpecifiedCount(99999);
//								dim.setFrequency(1.0);
//								dim.setRuleName("耗材寿命衰减");
//
//								dim.setUpdateTime(new Date().getTime());
//								dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
//
//								redisTemplate.opsForValue().set("IpqcAppearance:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//								redisTemplate.opsForValue().set("IpqcSize:"+dfKnifStatus.getMachineCode(),gson.toJson(dim));
//								DynamicIpqcUtil.clearSizeData(redisTemplate,dfKnifStatus.getMachineCode(),gson);//清空缓存测试项数据
//
//							}
//
//						}
//
//
//				}




				QueryWrapper<DfLiableMan> sqw=new QueryWrapper<>();
				sqw.like("process_name",process6);
				sqw.eq("type","砂轮");
				sqw.eq("problem_level","1");
				if(TimeUtil.getBimonthly()==0){
					sqw.like("bimonthly","双月");
				}else{
					sqw.like("bimonthly","单月");
				}
				List<DfLiableMan> lm =dfLiableManService.list(sqw);
				if (lm==null||lm.size()==0){
					return;
				}
				//责任人编号
				StringBuilder manCode=new StringBuilder();
				//责任人
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
				List<NumTool> detailList = dfKnifStatus.getDetail();

                //判断触发预警还是报警
                for (NumTool numTool : detailList) {
					//每条信息存入df_knif_status_type6表
					DfKnifStatusType6 dfKnifStatusType6 = new DfKnifStatusType6();
					dfKnifStatusType6.setMacCode(dfKnifStatus.getMacCode());
					dfKnifStatusType6.setNNumTool(numTool.getNNumTool());
					dfKnifStatusType6.setToolCutNum(numTool.getToolCutNum());
					dfKnifStatusType6.setProcess(process6);
					dfKnifStatusType6Service.save(dfKnifStatusType6);

					//砂轮结果是否推送RFID(Y/N)
					if (!"Y".equals(env.getProperty("isPushKnifeResult"))){
						continue;
					}

					//大于触发报警
					if (numTool.getToolCutNum() > warn.getAlarmValue()) {
						//获取对应机台号对应的最新的(正在使用的)刀具编号
						QueryWrapper<DfKnifStatus> ew2 = new QueryWrapper<>();
						ew2.eq("machine_code",dfKnifStatus.getMacCode())
								.eq("n_num_tool", numTool.getNNumTool())
								.orderByDesc("create_time")
								.last("limit 1");
						List<DfKnifStatus> knifStatusList = dfKnifStatusService.list(ew2);
						if (!CollectionUtils.isEmpty(knifStatusList)){
							DfKnifStatus knifStatus = knifStatusList.get(0);
							DfAuditDetail aud = new DfAuditDetail();
							aud.setDataType("砂轮寿命");
							aud.setDepartment("砂轮寿命");
							aud.setAffectMac("1");
							aud.setAffectNum(1.0);
							aud.setControlStandard("砂轮寿命报警:大于" + warn.getAlarmValue());
							aud.setImpactType("砂轮寿命");
							aud.setIsFaca("0");
							aud.setQuestionName("砂轮寿命报警");
							aud.setScenePractical("工序"+process6+"_机台号" +knifStatus.getMachineCode() +"_刀号"+ knifStatus.getNNumTool()+"_刀具切削次数"+numTool.getToolCutNum()+"_砂轮寿命报警");
							aud.setReportMan("系统");
							aud.setCreateName("系统");

							aud.setReportTime(Timestamp.valueOf(checkTime));
							aud.setOccurrenceTime(Timestamp.valueOf(checkTime));
							aud.setIpqcNumber(checkTimeLong);

							aud.setProcess(process6);
							aud.setQuestionType("砂轮寿命");
							aud.setDecisionLevel("Level1");
							aud.setHandlingSug("全检风险批");
							aud.setResponsible(manName.toString());
							aud.setResponsibleId(manCode.toString());
							aud.setMacCode(knifStatus.getMachineCode());
							aud.setnNumTool(knifStatus.getNNumTool());
							aud.setToolCode(knifStatus.getToolCode());
							dfAuditDetailService.save(aud);

							DfFlowData fd = new DfFlowData();
							fd.setFlowLevel(1);
							fd.setDataType(aud.getDataType());
							fd.setFlowType(aud.getDataType());
							fd.setName("IPQC_砂轮寿命_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
							fd.setDataId(aud.getId());
							fd.setStatus("待确认");
							fd.setCreateName("系统");
							fd.setCreateUserId("系统");
							fd.setNowLevelUser(aud.getResponsibleId());
							fd.setNowLevelUserName(aud.getResponsible());
							fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
							fd.setFlowLevelName(aud.getDecisionLevel());
							QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
							atQw.eq("type","砂轮")
									.last("limit 1");
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

					} else if (numTool.getToolCutNum() > warn.getPrewarningValue() && numTool.getToolCutNum() <= warn.getAlarmValue()) {
						//两者之间触发预警
						//触发预警
						//获取对应机台号对应的最新的刀具号
						QueryWrapper<DfKnifStatus> ew2 = new QueryWrapper<>();
						ew2.eq("machine_code",dfKnifStatus.getMacCode())
								.eq("n_num_tool", numTool.getNNumTool())
								.orderByDesc("create_time")
								.last("limit 1");
						List<DfKnifStatus> knifStatusList = dfKnifStatusService.list(ew2);
						if (!CollectionUtils.isEmpty(knifStatusList)){
							DfKnifStatus knifStatus = knifStatusList.get(0);
							DfAuditDetail aud = new DfAuditDetail();
							aud.setDataType("砂轮寿命");
							aud.setDepartment("砂轮寿命");
							aud.setAffectMac("1");
							aud.setAffectNum(1.0);
							aud.setControlStandard("砂轮寿命预警:大于" + warn.getPrewarningValue() + "小于等于" + warn.getAlarmValue());
							aud.setImpactType("砂轮寿命");
							aud.setIsFaca("0");
							aud.setQuestionName("砂轮寿命预警");
							aud.setScenePractical("工序"+process6+"_机台号" +knifStatus.getMachineCode() +"_刀号"+ knifStatus.getNNumTool()+"_刀具切削次数"+numTool.getToolCutNum()+"_砂轮寿命预警");
							aud.setReportMan("系统");
							aud.setCreateName("系统");

							aud.setReportTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
							aud.setOccurrenceTime(knifStatus.getDtEvent());
							aud.setIpqcNumber(TimeUtil.getNowTimeLong());

							aud.setProcess(process6);
							aud.setQuestionType("砂轮寿命");
							aud.setDecisionLevel("Level1");
							aud.setHandlingSug("全检风险批");
							aud.setResponsible(manName.toString());
							aud.setResponsibleId(manCode.toString());
							aud.setMacCode(knifStatus.getMachineCode());
							aud.setnNumTool(knifStatus.getNNumTool());
							aud.setToolCode(knifStatus.getToolCode());
							dfAuditDetailService.save(aud);

							DfFlowData fd = new DfFlowData();
							fd.setFlowLevel(1);
							fd.setDataType(aud.getDataType());
							fd.setFlowType(aud.getDataType());
							fd.setName("IPQC_砂轮寿命_" + aud.getQuestionName() + "_NG_" + TimeUtil.getNowTimeByNormal());
							fd.setDataId(aud.getId());
							fd.setStatus("待确认");

							fd.setCreateName("系统");
							fd.setCreateUserId("系统");
							fd.setNowLevelUser(aud.getResponsibleId());
							fd.setNowLevelUserName(aud.getResponsible());
							fd.setLevel1PushTime(Timestamp.valueOf(checkTime));
							fd.setFlowLevelName(aud.getDecisionLevel());
							QueryWrapper<DfApprovalTime>atQw=new QueryWrapper<>();
							atQw.eq("type","砂轮")
									.last("limit 1");
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
                break;
            case "107":
                //关闭任务单
				QueryWrapper<DfAuditDetail> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("tool_code",dfKnifStatus.getOldToolCode());
				List<DfAuditDetail> list = dfAuditDetailService.list(queryWrapper);
				//根据编号可能找到多个任务,都设置endTime
				for (DfAuditDetail dfAuditDetail : list) {
					dfAuditDetail.setEndTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
					dfAuditDetail.setFa("砂轮寿命到期预警");
					dfAuditDetail.setCa("更换砂轮");
					dfAuditDetailService.updateById(dfAuditDetail);
					//更新flow_data
					UpdateWrapper<DfFlowData> flowDataUpdateWrapper = new UpdateWrapper<>();
					DfFlowData dfFlowData = new DfFlowData();
					dfFlowData.setStatus("已关闭");
					flowDataUpdateWrapper.eq("data_id", dfAuditDetail.getId());
					dfFlowDataService.update(dfFlowData,flowDataUpdateWrapper);
				}
                break;
            case "108":
                break;
        }
    }
}
