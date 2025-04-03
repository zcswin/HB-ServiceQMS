package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfSizeCheckItemInfosMapper;
import com.ww.boengongye.mapper.DfSizeDetailMapper;
import com.ww.boengongye.service.DfSizeCheckItemInfosService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.service.DfSizeContStandService;
import com.ww.boengongye.service.DfSizeDetailService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.boengongye.utils.NormalDistributionUtil.convertToDoubleArray;
import static com.ww.boengongye.utils.NormalDistributionUtil.normalDistribution2;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Service
public class DfSizeCheckItemInfosServiceImpl extends ServiceImpl<DfSizeCheckItemInfosMapper, DfSizeCheckItemInfos> implements DfSizeCheckItemInfosService {

    @Autowired
    private DfSizeCheckItemInfosMapper dfSizeCheckItemInfosMapper;

    @Autowired
    private DfSizeContStandService dfSizeContStandService;

    @Autowired
    private DfSizeDetailMapper dfSizeDetailMapper;


    @Override
    public List<DfSizeCheckItemInfos> listByFactory(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listByFactory(wrapper);
    }

    @Override
    public DfSizeCheckItemInfos getOKRate(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.getOKRate(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailLimit50(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfSizeCheckItemInfosMapper.listJoinDetailLimit50(project, color, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailLimit50OkData(String project, String color, String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfSizeCheckItemInfosMapper.listJoinDetailLimit50OkData(project, color, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listSizeNgRate(String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfSizeCheckItemInfosMapper.listSizeNgRate(process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listSizeNgRate3(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listSizeNgRate3(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachine(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listJoinDetailLimit50ByMachine(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailLimit50ByMachineSortByNgRate(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listJoinDetailLimit50ByMachineSortByNgRate(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listMachineJoinDetailLimit50(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listMachineJoinDetailLimit50OkData(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listMachineJoinDetailLimit50OkData(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listItemCheckValueLimit50(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listItemCheckValueLimit50(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailAndProcess(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listJoinDetailAndProcess(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailLimit50_2() {
        return dfSizeCheckItemInfosMapper.listJoinDetailLimit50_2();
    }

    @Override
    public List<Rate> listItemOkRate(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listItemOkRate(wrapper);
    }

    @Override
    public List<Rate> listItemOkRate2(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listItemOkRate2(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoin(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listJoin(wrapper);
    }


    //根据时间获取测量值
    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcess(String process,String project,String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit) {
        return dfSizeCheckItemInfosMapper.listJoinDetailByTimeAndProcess(process,project,color, startTime, endTime, startHour, endHour, startLine, limit);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailByTimeAndProcessAndItem(String process, String project, String color, String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit, String itmeName,String machineCode) {
        return dfSizeCheckItemInfosMapper.listJoinDetailByTimeAndProcessAndItem(process, project, color, startTime, endTime, startHour, endHour, startLine, limit, itmeName,machineCode);
    }

    @Override
    public List<DfSizeCheckItemInfos> listJoinDetailByTime(String process, String project,String color,String startTime, String endTime, Integer startHour, Integer endHour, Integer startLine, Integer limit) {
        return dfSizeCheckItemInfosMapper.listJoinDetailByTime(process, project,color,startTime, endTime, startHour, endHour, startLine, limit);
    }

    @Override
    public List<Rate> listAllProcessSizeNgRateTop5(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listAllProcessSizeNgRateTop5(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOKRate(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listAllProcessOKRate(wrapper);
    }

    @Override
    public List<Rate> listSizeNgRate2(String process, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.listSizeNgRate2(process, startTime, endTime);
    }

    @Override
    public List<Rate> listProcessNgRateByItem(String project,String color,String startTime, String endTime, String checkResult, String itemName) {
        return dfSizeCheckItemInfosMapper.listProcessNgRateByItem(project,color,startTime, endTime, checkResult, itemName);
    }

    @Override
    public List<DfSizeCheckItemInfos> listCheckItemValueLimit50(Wrapper<DfSizeCheckItemInfos> wrapper) {
        return dfSizeCheckItemInfosMapper.listCheckItemValueLimit50(wrapper);
    }


    @Override
    public List<DfSizeCheckItemInfos> listTotalprocessyieldtrendchart(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.listTotalprocessyieldtrendchart(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<DfSizeCheckItemInfos> fullSizeNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.fullSizeNGTop5(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<DfSizeCheckItemInfos> listSingleprocessyieldtrendchart(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.listSingleprocessyieldtrendchart(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<DfSizeCheckItemInfos> inputOutputQuantity(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.inputOutputQuantity(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<DfSizeCheckItemInfos> detailPageSizeYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.detailPageSizeYield(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate> listAllProcessSizeNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.listAllProcessSizeNgRateTop5V2(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate2> listAllProcessSizeNgRateTop5V3(String startTime, String endTime) {
        return dfSizeCheckItemInfosMapper.listAllProcessSizeNgRateTop5V3(startTime, endTime);
    }

    @Override
    public List<DfSizeCheckItemInfos> machineSizeAccuracyControlChart(QueryWrapper<DfSizeCheckItemInfos> ew) {
        return dfSizeCheckItemInfosMapper.machineSizeAccuracyControlChart(ew);
    }

    @Override
    public List<DfSizeCheckItemInfos> machineSizeControlChart(QueryWrapper<DfSizeCheckItemInfos> ew) {
        return dfSizeCheckItemInfosMapper.machineSizeControlChart(ew);
    }

    public ImportExcelResult importOrder(MultipartFile file,String project ,String process,String color) throws Exception {

       QueryWrapper<DfSizeContStand>sqw=new QueryWrapper<>();
        sqw.eq("project",project);
        sqw.eq("process",process);
        sqw.eq("color",color);

        List<DfSizeContStand>stand=dfSizeContStandService.list(sqw);
        Map<String,DfSizeContStand> standMap=new HashMap<>();
        Map<String,DfSizeDetail> sizeMap=new HashMap<>();
        if(stand.size()>0){
            for(DfSizeContStand s:stand){
                standMap.put(s.getTestItem(),s);
            }
        }

        int successCount = 0;
        int failCount = 0;
        //调用封装好的工具
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        //调用导入的方法，获取sheet表的内容
        List<Map<String, String>> maps = importUtil.readExcelContent();
        //获取自定义表头标题数据
//        Map<String, Object> someTitle = importUtil.readExcelSomeTitle();


        List<DfSizeCheckItemInfos> orderDetails = maps.stream().filter(Objects::nonNull).map(map -> {
            DfSizeDetail sizeDetail=new DfSizeDetail();
            if(sizeMap.containsKey(map.get("TwoDimensionCode"))){
                sizeDetail=sizeMap.get(map.get("TwoDimensionCode"));
            }else{
                QueryWrapper<DfSizeDetail>qw=new QueryWrapper<>();
                qw.eq("sn",map.get("TwoDimensionCode"));
                qw.last("limit 1");
                 sizeDetail=dfSizeDetailMapper.selectOne(qw);
                 if(null!=sizeDetail){
                     sizeMap.put(sizeDetail.getSn(),sizeDetail);
                 }

            }


            DfSizeCheckItemInfos tc = new DfSizeCheckItemInfos();
            if(null!=sizeDetail&&standMap.containsKey(map.get("Positon"))){


                tc.setCheckValue(Double.parseDouble(map.get("DetectionValue")));
                tc.setCheckId(sizeDetail.getId()+"");
                tc.setCreateTime(sizeDetail.getTestTime());
                tc.setCheckTime(TimeUtil.formatTimestamp(sizeDetail.getTestTime()));
                tc.setItemName(map.get("Positon"));
                tc.setLsl(standMap.get(map.get("Positon")).getLowerLimit());
                tc.setUsl(standMap.get(map.get("Positon")).getUpperLimit());
                tc.setControlLowerLimit(standMap.get(map.get("Positon")).getIsolaLowerLimit());
                tc.setControlUpperLimit(standMap.get(map.get("Positon")).getIsolaUpperLimit());
                tc.setStandardValue(standMap.get(map.get("Positon")).getStandard());
                tc.setKeyPoint(standMap.get(map.get("Positon")).getKeyPoint());
                tc.setCheckResult("OK");
                if (tc.getCheckValue() > standMap.get(map.get("Positon")).getIsolaUpperLimit()) {
                    tc.setBadCondition(tc.getItemName() + "偏大");
                    tc.setCheckResult("NG");
                } else if (tc.getCheckValue() < standMap.get(map.get("Positon")).getIsolaLowerLimit()) {
                    tc.setBadCondition(tc.getItemName() + "偏小");
                    tc.setCheckResult("NG");
                }
                tc.setRemark("import");
                if(tc.getCheckResult().equals("NG")&&sizeDetail.getInfoResult().equals("OK")){
                    UpdateWrapper<DfSizeDetail>uw=new UpdateWrapper<>();
                    uw.set("info_result","NG");
                    uw.eq("id",sizeDetail.getId());
                    sizeDetail.setInfoResult("NG");
                    dfSizeDetailMapper.updateById(sizeDetail);
                    sizeMap.put(sizeDetail.getSn(),sizeDetail);
                }


            }
            return tc;
        }).collect(Collectors.toList());

        if (orderDetails.size() > 0) {
            for (DfSizeCheckItemInfos c : orderDetails) {
                    if(null!=c&&null!=c.getCheckId()){
                        dfSizeCheckItemInfosMapper.insert(c);
                    }


            }


        }


        ImportExcelResult ter = new ImportExcelResult();
        ter.setFail(failCount);
        ter.setSuccess(successCount);

        return ter;
    }

    @Override
    public List<DfSizeCheckItemInfos> tzInfo(QueryWrapper<DfSizeCheckItemInfos> ew) {
        return dfSizeCheckItemInfosMapper.tzInfo(ew);
    }

    @Override
    public List<DfSizeCheckItemInfos> tzInfoByProcess(QueryWrapper<DfSizeCheckItemInfos> ew) {
        return dfSizeCheckItemInfosMapper.tzInfoByProcess(ew);
    }

    @Override
    public List<DfSizeCheckItemInfos> tzInfoCpkByProcess(QueryWrapper<DfSizeCheckItemInfos> ew) {
        return dfSizeCheckItemInfosMapper.tzInfoCpkByProcess(ew);
    }

    /***
     * 获取正太分布数据
     */
    @Override
    public List<Map<String, Object>> getNormalDistributionData(String process, String project, String color, String startTime, String endTime,
                                                               Integer startHour, Integer endHour, Integer startLine, Integer limit) {
        List<DfSizeCheckItemInfos> dfSizeCheckItemInfos;
        dfSizeCheckItemInfos = listJoinDetailByTime(process, project, color, startTime, endTime, startHour, endHour, startLine, limit);
        Map<String, List<Double>> itemResCheckValue = new LinkedHashMap<>();
        Map<String, Double> itemResStandardValue = new HashMap<>();
        Map<String, Double> itemResUsl = new HashMap<>();
        Map<String, Double> itemResLsl = new HashMap<>();
        Map<String, Integer> itemResOkNum = new HashMap<>();
        Map<String, Integer> itemResAllNum = new HashMap<>();

        for (DfSizeCheckItemInfos dfSizeCheckItemInfo : dfSizeCheckItemInfos) {
            String itemName = dfSizeCheckItemInfo.getItemName();
            Double checkValue = dfSizeCheckItemInfo.getCheckValue();
            itemResAllNum.merge(itemName, 1, Integer::sum);
            if (dfSizeCheckItemInfo.getCheckResult().equals("OK")) {
                itemResOkNum.merge(itemName, 1, Integer::sum);
                if (!itemResCheckValue.containsKey(itemName)) {
                    List<Double> list = new ArrayList<>();
                    list.add(checkValue);
                    itemResCheckValue.put(itemName, list);
                    itemResStandardValue.put(itemName, dfSizeCheckItemInfo.getStandardValue());
                    itemResUsl.put(itemName, dfSizeCheckItemInfo.getUsl());
                    itemResLsl.put(itemName, dfSizeCheckItemInfo.getLsl());
                } else {
                    itemResCheckValue.get(itemName).add(checkValue);
                }
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : itemResCheckValue.entrySet()) {
            Map<String, Object> itemData = new HashMap<>();
            String itemName = entry.getKey();
            itemData.put("name", itemName);
            itemData.put("standard", itemResStandardValue.get(itemName));
            itemData.put("usl", itemResUsl.get(itemName));
            itemData.put("lsl", itemResLsl.get(itemName));

            Integer okNum = itemResOkNum.get(itemName) == null ? 0 : itemResOkNum.get(itemName);
            Integer allNum = itemResAllNum.get(itemName) == null ? 0 : itemResAllNum.get(itemName);
            //itemData.put("良率OK", itemResOkNum.get(itemName));
            itemData.put("良率", okNum.doubleValue() / allNum);
            itemData.put("抽检数", allNum);
            //itemData.put("良率ALL", itemResAllNum.get(itemName));
            normalDistribution2(convertToDoubleArray(entry.getValue().toArray()), itemData);
            result.add(itemData);
        }
        return result;
    }

//    @Override
//    public List<Map<String,Object>> getDetailData(Wrapper<DfSizeDetail> wrapper) {
//        return dfSizeCheckItemInfosMapper.getDetailData(wrapper);
//    }
//
//    @Override
//    public List<DfSizeCheckItemInfos> getSizeCheckItemInfosList(Wrapper<DfSizeDetail> wrapper) {
//        return dfSizeCheckItemInfosMapper.getSizeCheckItemInfosList(wrapper);
//    }
}
