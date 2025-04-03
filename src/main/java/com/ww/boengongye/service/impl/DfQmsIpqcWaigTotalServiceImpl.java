package com.ww.boengongye.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfQmsIpqcWaigTotalMapper;
import com.ww.boengongye.mapper.DfWaigContFrequMapper;
import com.ww.boengongye.service.DfQmsIpqcFlawConfigService;
import com.ww.boengongye.service.DfQmsIpqcWaigDetailService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import com.ww.boengongye.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Service("dfQmsIpqcWaigTotalServiceImpl")
public class DfQmsIpqcWaigTotalServiceImpl extends ServiceImpl<DfQmsIpqcWaigTotalMapper, DfQmsIpqcWaigTotal> implements DfQmsIpqcWaigTotalService {

    @Autowired
    private DfQmsIpqcWaigTotalMapper dfQmsIpqcWaigTotalMapper;

    @Autowired
    private DfWaigContFrequMapper dfWaigContFrequMapper;

    @Autowired
    private DfQmsIpqcWaigDetailService dfQmsIpqcWaigDetailService;
    @Autowired
    private DfQmsIpqcFlawConfigService dfQmsIpqcFlawConfigService;

    @Autowired
    Environment env;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public int importExcel(MultipartFile file) throws Exception {
        List<DfWaigContFrequ> dfWaigContFrequs = dfWaigContFrequMapper.selectList(null);

        Map<String, Integer> CPKContFrequ = new HashMap<>();  // 过程检频率
        Map<String, Integer> FAIContFrequ = new HashMap<>();  // 首件频率
        for (DfWaigContFrequ dfWaigContFrequ : dfWaigContFrequs) {
            //CPKContFrequ.put(dfWaigContFrequ.getProcess(), dfWaigContFrequ.getCpkNum());
            //FAIContFrequ.put(dfWaigContFrequ.getProcess(), dfWaigContFrequ.getFaiNum());
        }

        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent(2, 2);
        List<DfQmsIpqcWaigDetail> detailList = new ArrayList<>();
        List<DfQmsIpqcWaigTotal> totalList = new ArrayList<>();
        int totalId = 10;
        DfQmsIpqcWaigTotal lastTotal = new DfQmsIpqcWaigTotal();
        for (Map<String, String> map : maps) {
            DfQmsIpqcWaigDetail detail = new DfQmsIpqcWaigDetail();
            DfQmsIpqcWaigTotal total = new DfQmsIpqcWaigTotal();
            total.setId(totalId);
            total.setfType(map.get("外观类型"));
            total.setfFac(map.get("工厂"));
            total.setfSeq(map.get("工序"));
            total.setfBigpro(map.get("项目"));
            total.setfLine(map.get("线体"));
            total.setfBarcode(map.get("产品条码"));
            total.setfStage(map.get("生产阶段"));
            total.setfColor(map.get("颜色"));
            total.setfMac(map.get("机台号"));
            total.setfTestType(map.get("检验类型"));
            total.setfTestMan(map.get("检验员"));
            total.setfTestCategory(map.get("测试类别"));
            total.setfTime(null == map.get("时间") ? null : Timestamp.valueOf(map.get("时间")));
            //total.setFContFrequ(total.getfTestCategory() == "CPK" ? CPKContFrequ.get(total.getfSeq()) : FAIContFrequ.get(total.getfSeq()));
            if (lastTotal.equals(total)) {
                //System.out.println("相同");
            } else {  // total不同的话就加id并且存储起来
                //System.out.println("不同");
                total.setId(++totalId);
                totalList.add(total);
            }
            lastTotal = total;

            detail.setfBigArea(map.get("大区域"));
            detail.setfSmArea(map.get("小区域"));
            detail.setfDefect(map.get("缺陷明细"));
            detail.setfSort(map.get("类型"));
            detail.setfLevel(map.get("缺陷等级"));
            detail.setfStandard(map.get("标准"));
            detail.setfResult(map.get("判定"));
            detail.setfTime(null == map.get("时间") ? null : Timestamp.valueOf(map.get("时间")));
            detail.setFParentId(totalId);
            detailList.add(detail);
        }

        dfQmsIpqcWaigDetailService.saveBatch(detailList);
        saveBatch(totalList);

        return detailList.size() + totalList.size();
    }


    @Override
    public List<DfQmsIpqcWaigTotal> getOptimizedDataWithCustomExecutor(String startDate, String endDate, String startHour, String endHour, String processName, String projectName){
        return dfQmsIpqcWaigTotalMapper.getOptimizedDataWithCustomExecutor( startDate,  endDate,  startHour,  endHour,  processName,  projectName);
    }






    @Override
    public List<Rate> listNgItemRate(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgItemRate(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgItemRateSortByNgTop(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgItemRateSortByNgTop(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgItemRateSortByNgTop2(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgItemRateSortByNgTop2(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgItemRateSortByNgTop3(lineBody, project,process, startTime, endTime);
    }
    //优化
    @Override
    public List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String floor, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgItemRateSortByNgTop3(lineBody, project,floor, process, startTime, endTime);
    }

    @Override
    public List<Rate> listDateNgRate(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listDateNgRate(wrapper);
    }

    @Override
    public DfQmsIpqcWaigTotal getTotalAndNgCount(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getTotalAndNgCount(wrapper);
    }

    @Override
    public List<Rate> listOkRateNear15Day(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listOkRateNear15Day(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotal> listOkRate(String startTime, String endTime, int startHour, int endHour, String projectName, String process,String period) {
        return dfQmsIpqcWaigTotalMapper.listOkRate(startTime,endTime,startHour,endHour,projectName,process,period);
    }


    @Override
    public List<Rate> listNgTop10(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgTop10(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgTop102(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listNgTop102(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgTop103(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalMapper.listNgTop103(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
    }


    @Override
    public List<Rate> listNgTop5Fmac(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalMapper.listNgTop5Fmac(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listNgTop10Fmac(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour,String fmac) {
        return dfQmsIpqcWaigTotalMapper.listNgTop10Fmac(lineBody, project, floor, process, startTime, endTime, startHour, endHour,fmac);
    }


    @Override
    public List<Rate> listNgTop104(String lineBody, String project, String floor, List<String> process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalMapper.listNgTop104(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listAlwaysOkRate(Wrapper<DfQmsIpqcWaigTotal> wrapper,String project) {
        return dfQmsIpqcWaigTotalMapper.listAlwaysOkRate(wrapper,project);
    }

    @Override
    public List<DfQmsIpqcWaigTotal> listMachineCode(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listMachineCode(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotal> getFaiPassRate(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getFaiPassRate(wrapper);
    }

    @Override
    public List<Rate> listAllProcessItemNgRate(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessItemNgRate(wrapper);
    }

    @Override
    public List<ProcessResRate> listProcessAlwaysNumRate(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listProcessAlwaysNumRate(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOkRate(Wrapper<DfQmsIpqcWaigTotal> wrapper, String floor, String project) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessOkRate(wrapper, floor,project);
    }

    @Override
    public Rate getAppearRealOkRate(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getAppearRealOkRate(wrapper);
    }

    @Override
    public List<Rate> listProcessNgRateByNgItem(String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalMapper.listProcessNgRateByNgItem(lineBody, project,floor, ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listMacNgRateByNgItemAndProcess(String process, String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalMapper.listMacNgRateByNgItemAndProcess(process, lineBody, project,floor, ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listAllProcessItemNgRateTop5(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessItemNgRateTop5(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOkRate2(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessOkRate2(wrapper);
    }

    @Override
    public DfQmsIpqcWaigTotal getProcessYield(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getProcessYield(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> detailPageApparenceYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.detailPageApparenceYield(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate> listAllProcessItemNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessItemNgRateTop5V2(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate2> listAllProcessItemNgRateTop5V3(String startTime, String endTime) {
        return dfQmsIpqcWaigTotalMapper.listAllProcessItemNgRateTop5V3(startTime, endTime);
    }

    @Override
    public List<Rate> listCNC0OkRateOneMonth(QueryWrapper<DfQmsIpqcWaigTotal> qw) {
        return dfQmsIpqcWaigTotalMapper.listCNC0OkRateOneMonth(qw);
    }

    @Override
    public int Count(QueryWrapper<DfQmsIpqcWaigTotal> qw) {
        return dfQmsIpqcWaigTotalMapper.Count(qw);
    }

    @Override
    public List<Rate3> listProcessOkRateOneMonth(String factoryId, String project,String startTime, String endTime, String process) {
        return dfQmsIpqcWaigTotalMapper.listProcessOkRateOneMonth(factoryId,project,startTime,endTime,process);
    }

    @Override
    public Rate getAllYield(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getAllYield(wrapper);
    }

    @Override
    public Rate getLineYield(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return dfQmsIpqcWaigTotalMapper.getLineYield(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotal> weekOfPoorTOP3Warning(QueryWrapper<DfQmsIpqcWaigTotal> ew) {
        return dfQmsIpqcWaigTotalMapper.weekOfPoorTOP3Warning(ew);
    }

    @Override
    public List<Rate3> twoWeekOfPoorTOP3Warning() {
        return dfQmsIpqcWaigTotalMapper.twoWeekOfPoorTOP3Warning();
    }

    @Override
    public List<DfQmsIpqcWaigTotal> listWaigExcelData(QueryWrapper<DfQmsIpqcWaigTotal> ew) {
        return dfQmsIpqcWaigTotalMapper.listWaigExcelData(ew);
    }

    @Override
    public List<DfQmsIpqcWaigTotal> listWaigExcelDataPage(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotal> ew,
                                                          @Param("pageOffset") int pageOffset,
                                                          @Param("pageSize") int pageSize,
                                                          @Param("floor") String floor,
                                                          @Param("project") String project)
    {
        return dfQmsIpqcWaigTotalMapper.listWaigExcelDataPage(ew,pageOffset,pageSize,floor,project);
    }



    @Override
    public List<Map<String, Object>> exportInspectionTableForProcess(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper) {
        return dfQmsIpqcWaigTotalMapper.exportInspectionTableForProcess(waigWrapper);
    }

    @Override
    public List<Map<String, Object>> exportInspectionTableForProcessBymachineId(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper) {
        return dfQmsIpqcWaigTotalMapper.exportInspectionTableForProcessBymachineId(waigWrapper);
    }

    @Override
    public List<Map<String, Object>> exportNgClassificationScale(QueryWrapper<DfQmsIpqcWaigDetail> waigWrapper) {
        return dfQmsIpqcWaigTotalMapper.exportNgClassificationScale(waigWrapper);
    }

    @Override
    public List<Rate3> getMachineOkNumList(QueryWrapper<Rate3> qw) {
        return dfQmsIpqcWaigTotalMapper.getMachineOkNumList(qw);
    }

    @Override
    public List<Map<String, Object>> getWGDetailInfoList(QueryWrapper<Map<String, Object>> qw) {
        return dfQmsIpqcWaigTotalMapper.getWGDetailInfoList(qw);
    }
    @Override
    public String DynamicIpqcBoSongFenBu(DfQmsIpqcWaigTotal datas){
        int ipqcType=0;
        //动态ipqc收严时间(小时)
        double IpqcTimeStandardTighten=Double.valueOf(env.getProperty("IpqcTimeStandardTighten","1.5"));
        //动态ipqc正常时间(小时)
        double IpqcTimeStandardNormal=Double.valueOf(env.getProperty("IpqcTimeStandardNormal","3"));
        //动态ipqc放宽时间阶段一(小时)
        double IpqcTimeStandardRelax1=Double.valueOf(env.getProperty("IpqcTimeStandardRelax1","6"));
        //动态ipqc放宽时间阶段二(小时)
        double IpqcTimeStandardRelax2=Double.valueOf(env.getProperty("IpqcTimeStandardRelax2","12"));
        //CNC3 泊松分布计算 ---  start  ----
        if(env.getProperty("IPQCCNC3BoSongFenBu","N").equals("Y")&&datas.getfSeq().equals("CNC3")&&datas.getStatus().equals("NG")){

            for(DfQmsIpqcWaigDetail d: datas.getDetailList()){
                if(d.getfResult().equals("fail")&&null!=d.getfProductId()&&!d.getfProductId().equals("")){
                    List<String>processList=new ArrayList<>();
                    processList.add("CNC1");
                    processList.add("CNC2");
                    processList.add("CNC3");
                    QueryWrapper<DfQmsIpqcFlawConfig>fcqw=new QueryWrapper<>();
                    fcqw.eq("project",datas.getfBigpro());
                    fcqw.eq("flaw_name",d.getfSort());
                    fcqw.in("process",processList);
                    System.out.println(d.getfSort());
                    List<DfQmsIpqcFlawConfig>flawConfigList=dfQmsIpqcFlawConfigService.listDistinctAreaAndProcess(fcqw);
                    if(flawConfigList.size()>1){
                        String maxProcess="";
                        double maxProb=0.0;
                        for(DfQmsIpqcFlawConfig c:flawConfigList){


                            Object v= redisTemplate.opsForValue().get(datas.getfBigpro()+":"+d.getfSort()+":"+c.getBigArea()+":"+d.getfSmArea()+":"+c.getProcess());
//                                        Object v = valueOperations.get(datas.getfBigpro()+":"+d.getfSort()+":"+c.getBigArea()+":"+d.getfSmArea()+":"+c.getProcess());
                            if(null!=v){
                                System.out.println("进入1");
                                System.out.println(v.toString());
                                Double porb = Double.parseDouble(v.toString());
                                if(porb>maxProb) {
                                    maxProb=porb;
                                    maxProcess=c.getProcess();
                                }
                                System.out.println(porb);



                            }

                        }

                        if(maxProcess.equals("CNC3")){
                            ipqcType=1;
                        }else{
                            String url = env.getProperty("FindVbCodeAPI");
                            Map<String, String> headers = new HashMap<>();
                            HashMap<Object, Object> map = new HashMap<>();
                            map.put("vbCode",d.getfProductId());
                            RFIDResult2 rfidResult2 = new Gson().fromJson(HttpUtil.postJson(url, null, headers,
                                    JSONObject.toJSONString(map), false), RFIDResult2.class);
                            List<RFIDRecord> data = rfidResult2.getData();
                            ArrayList<DfQmsIpqcFlawLogDetail> collect = new ArrayList<DfQmsIpqcFlawLogDetail>();
                            ValueOperations valueOperations = redisTemplate.opsForValue();
                            for (RFIDRecord record : data) {
                                String procedureName = record.getProcedureName();
                                String operateTime = record.getOperateTime();
                                String procedureStepName = record.getProcedureStepName();
                                String machineCode = record.getMachineCode();
                                if(record.getProcedureName().equals(maxProcess)){
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","CNC3全检NG关联不良"+d.getfSort(),"收严",procedureName,machineCode,datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    if(redisTemplate.hasKey("IpqcAppearance:"+datas.getfMac())){
                                        Object v =  valueOperations.get("IpqcAppearance:" + datas.getfMac());
                                        if(null!=v) {
                                            DynamicIpqcMac dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);
                                            dim.setSenMesCount(1);
                                            dim.setNowCount(0);
                                            dim.setFrequency(IpqcTimeStandardTighten);
//                                            dim.setTotalCount(2);
                                            dim.setSpecifiedCount(2);
                                            dim.setRuleName("CNC3全检NG关联不良"+d.getfSort());
                                            dim.setAppearanceOkCount(0);
                                            dim.setUpdateTime(new Date().getTime());
                                            dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());

                                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                            redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getfMac(),gson.toJson(dim));
                                        }
                                    }

                                }
                            }
                        }


                    }
                }



            }


        }else{
            ipqcType=1;
        }
        //CNC3 泊松分布计算 ---  end  ----


        if(ipqcType==1&&env.getProperty("ISWGIPQC","N").equals("Y")
                && ((env.getProperty("IPQCProcess", "all").contains(datas.getfSeq())||env.getProperty("IPQCProcess", "all").equals("all") )
                && (env.getProperty("IPQCMac", "all").contains(datas.getfMac()) || env.getProperty("IPQCMac", "all").equals("all")))) {


            ValueOperations valueOperations = redisTemplate.opsForValue();
            if(redisTemplate.hasKey("IpqcAppearance:"+datas.getfMac())){
                Object v =  valueOperations.get("IpqcAppearance:" + datas.getfMac());
                if(null!=v){
                    DynamicIpqcMac dim = new Gson().fromJson(v.toString(), DynamicIpqcMac.class);

                    if (!dim.getRuleName().equals("耗材寿命衰减")){
                        dim.setTotalCount(dim.getTotalCount()+datas.getSpotCheckCount());
                        dim.setNgCount(dim.getNgCount()+datas.getAffectCount());
                        //判断是否连续ok
                        if(datas.getAffectCount()>0){
                            dim.setAppearanceOkCount(0);
                            if(datas.getfTestCategory().equals("风险批全检")&&dim.getRuleName().equals("⻛险批次全检")){
                                //因为重复抽检,数量减一
                                dim.setTotalCount(dim.getTotalCount()-1);
                                dim.setNgCount(dim.getNgCount()-1);
                                if (datas.getAffectCount() > 2) {
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","突发性⼤量不良","收严",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setSenMesCount(1);
                                    dim.setNowCount(0);
                                    dim.setFrequency(IpqcTimeStandardTighten);
//                                            dim.setTotalCount(2);
                                    dim.setSpecifiedCount(2);
                                    dim.setRuleName("突发性⼤量不良");
                                }else{
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardNormal,"外观","恢复为QCP抽检频率","放宽",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setFrequency(IpqcTimeStandardNormal);
                                    dim.setNowCount(1);
//                                            dim.setTotalCount(1);
                                    dim.setSpecifiedCount(1);
                                    dim.setSenMesCount(1);
                                    dim.setRuleName("QCP抽检频率");
                                }
                            }else if(dim.getRuleName().equals("更换耗材")){
                                dim.setSenMesCount(1);
                                dim.setNowCount(0);
                                dim.setSpecifiedCount(2);
                                dim.setFrequency(IpqcTimeStandardTighten);
                            }else{
                                if(dim.getNgCount()/dim.getTotalCount()*100>=Integer.parseInt(env.getProperty("IpqcMqcYield","10"))){
                                    //发通知
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","连续性机台异常","收严",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setSenMesCount(1);
                                    dim.setNowCount(0);
//                                            dim.setTotalCount(2);
                                    dim.setSpecifiedCount(2);
                                    dim.setFrequency(IpqcTimeStandardTighten);
                                    dim.setRuleName("连续性机台异常");

                                }else{
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","⻛险批次全检","收严",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setSenMesCount(1);
                                    dim.setNowCount(0);
//                                            dim.setTotalCount(1);
                                    dim.setSpecifiedCount(1);
                                    dim.setFrequency(IpqcTimeStandardTighten);
                                    dim.setRuleName("⻛险批次全检");

                                }
                            }

                        }else{
                            dim.setAppearanceOkCount(dim.getAppearanceOkCount()+datas.getSpotCheckCount());
                            if(dim.getAppearanceOkCount()>=32){

                                dim.setNowCount(1);
//                                        dim.setTotalCount(1);
                                dim.setSpecifiedCount(1);
                                dim.setSenMesCount(1);
                                dim.setRuleName("制程能⼒达标且稳定");
                                if(dim.getFrequency()==IpqcTimeStandardRelax1){
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardRelax2,"外观","制程能⼒达标且稳定","放宽",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setFrequency(IpqcTimeStandardRelax2);
                                }else{
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardRelax1,"外观","制程能⼒达标且稳定","放宽",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setFrequency(IpqcTimeStandardRelax1);
                                }
                                dim.setAppearanceOkCount(0);

                            }else{
                                //判断是否放宽
                                dim.setNowCount(dim.getNowCount()+1);
                                if(dim.getNowCount()>=dim.getSpecifiedCount()&&dim.getFrequency()<2){
                                    DynamicIpqcUtil.sendMes(IpqcTimeStandardNormal,"外观","恢复为QCP抽检频率","放宽",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                                    dim.setFrequency(IpqcTimeStandardNormal);
                                    dim.setNowCount(1);
//                                        dim.setTotalCount(1);
                                    dim.setSpecifiedCount(1);
                                    dim.setSenMesCount(1);
                                    dim.setRuleName("QCP抽检频率");
                                }




                            }


                        }

                        dim.setUpdateTime(new Date().getTime());
                        dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getfMac(),gson.toJson(dim));
                    }




                }
            }else{
                DynamicIpqcMac dim=new DynamicIpqcMac();
                dim.setMachineCode(datas.getfMac());
                dim.setNgCount(datas.getAffectCount());
                dim.setTotalCount(datas.getSpotCheckCount());
                if(datas.getAffectCount()>0){
                    //判断ng率是否超过
                    if(datas.getAffectCount()/datas.getSpotCheckCount()*100>=Integer.parseInt(env.getProperty("IpqcMqcYield","10"))){
                        //发通知
                        DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","连续性机台异常","收严",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                        dim.setFrequency(IpqcTimeStandardTighten);
                        dim.setNowCount(0);
//                                dim.setTotalCount(2);
                        dim.setSpecifiedCount(2);
                        dim.setRuleName("连续性机台异常");

                    }else{
                        DynamicIpqcUtil.sendMes(IpqcTimeStandardTighten,"外观","⻛险批次全检","收严",datas.getfSeq(),datas.getfMac(),datas.getId(),datas.getfBigpro(),datas.getfTestCategory(),"");
                        dim.setSenMesCount(1);
                        dim.setNowCount(0);
//                                dim.setTotalCount(1);
                        dim.setSpecifiedCount(1);
                        dim.setFrequency(IpqcTimeStandardTighten);
                        dim.setRuleName("⻛险批次全检");

                    }
                    dim.setSenMesCount(1);
                    dim.setAppearanceOkCount(0);
                }else{
                    dim.setAppearanceOkCount(datas.getSpotCheckCount());
                    dim.setNowCount(1);
//                            dim.setTotalCount(1);
                    dim.setSpecifiedCount(1);
                    dim.setSenMesCount(1);
                    dim.setFrequency(IpqcTimeStandardNormal);
                    dim.setRuleName("QCP抽检频率");
                }
                dim.setCpkCount(0);
                dim.setFourPointOverOne(0);
                dim.setTwoPointOverTwo(0);
                dim.setZugammenCount(0);
                dim.setSpcOkCount(0);

                dim.setUpdateTime(new Date().getTime());
                dim.setUpdateTimeStr(TimeUtil.getNowTimeByNormal());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                redisTemplate.opsForValue().set("IpqcAppearance:"+datas.getfMac(),gson.toJson(dim));

            }
//            Thread t1 = new DynamicIpqcBoSongFenBu(datas);
//            appreancePool.execute(t1);

        }



        return "";
    }

}
