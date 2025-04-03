package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfQmsIpqcWaigTotalCheckMapper;
import com.ww.boengongye.mapper.DfWaigContFrequMapper;
import com.ww.boengongye.service.DfQmsIpqcFlawConfigService;
import com.ww.boengongye.service.DfQmsIpqcWaigDetailCheckService;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalCheckService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
@Service
public class DfQmsIpqcWaigTotalCheckServiceImpl extends ServiceImpl<DfQmsIpqcWaigTotalCheckMapper, DfQmsIpqcWaigTotalCheck> implements DfQmsIpqcWaigTotalCheckService {

    @Autowired
    private DfQmsIpqcWaigTotalCheckMapper dfQmsIpqcWaigTotalCheckMapper;

    @Autowired
    private DfWaigContFrequMapper dfWaigContFrequMapper;

    @Autowired
    private DfQmsIpqcWaigDetailCheckService dfQmsIpqcWaigDetailCheckService;
    @Autowired
    private DfQmsIpqcFlawConfigService dfQmsIpqcFlawConfigService;

    @Autowired
    Environment env;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public int Count(QueryWrapper<DfQmsIpqcWaigTotalCheck> qw) {
        return dfQmsIpqcWaigTotalCheckMapper.Count(qw);
    }

    @Override
    public List<Rate> listNgItemRate(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgItemRate(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgItemRateSortByNgTop(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgItemRateSortByNgTop(lineBody, project, process, startTime, endTime);
    }


    @Override
    public List<DfQmsIpqcWaigTotalCheck> listOkRate(String startTime, String endTime, int startHour, int endHour, String projectName, String process) {
        return dfQmsIpqcWaigTotalCheckMapper.listOkRate(startTime,endTime,startHour,endHour,projectName,process);
    }


    @Override
    public List<Rate> listNgItemRateSortByNgTop2(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgItemRateSortByNgTop2(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgItemRateSortByNgTop3(lineBody, project,process, startTime, endTime);
    }
    //优化
    @Override
    public List<Rate> listNgItemRateSortByNgTop3(String lineBody, String project,String floor, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgItemRateSortByNgTop3(lineBody, project,floor, process, startTime, endTime);
    }

    @Override
    public List<Rate> listDateNgRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listDateNgRate(wrapper);
    }

    @Override
    public DfQmsIpqcWaigTotalCheck getTotalAndNgCount(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getTotalAndNgCount(wrapper);
    }

    @Override
    public List<Rate> listOkRateNear15Day(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listOkRateNear15Day(wrapper);
    }

    @Override
    public List<Rate> listNgTop10(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgTop10(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgTop102(String lineBody, String project, String process, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgTop102(lineBody, project, process, startTime, endTime);
    }

    @Override
    public List<Rate> listNgTop103(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgTop103(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> WaiguanlistNgTop10(String lineBody, String project, String floor , String process,String fsort, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalCheckMapper.WaiguanlistNgTop10(lineBody, project, floor, process,fsort, startTime, endTime, startHour, endHour);
    }




    @Override
    public List<Rate> listNgTop5Fmac(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgTop5Fmac(lineBody, project, floor, process, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listNgTop10Fmac(String lineBody, String project, String floor , String process, String startTime, String endTime, Integer startHour, Integer endHour,String fmac) {
        return dfQmsIpqcWaigTotalCheckMapper.listNgTop10Fmac(lineBody, project, floor, process, startTime, endTime, startHour, endHour,fmac);
    }



    @Override
    public List<Rate> listAlwaysOkRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listAlwaysOkRate(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotalCheck> listMachineCode(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listMachineCode(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotalCheck> getFaiPassRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getFaiPassRate(wrapper);
    }

    @Override
    public List<Rate> listAllProcessItemNgRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessItemNgRate(wrapper);
    }

    @Override
    public List<ProcessResRate> listProcessAlwaysNumRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listProcessAlwaysNumRate(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOkRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper, String floor,String project) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessOkRate(wrapper, floor,project);
    }

    @Override
    public Rate getAppearRealOkRate(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getAppearRealOkRate(wrapper);
    }

    @Override
    public List<Rate> listProcessNgRateByNgItem(String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalCheckMapper.listProcessNgRateByNgItem(lineBody, project,floor, ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listMacNgRateByNgItemAndProcess(String process, String lineBody, String project,String floor, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour) {
        return dfQmsIpqcWaigTotalCheckMapper.listMacNgRateByNgItemAndProcess(process, lineBody, project,floor, ngItem, startTime, endTime, startHour, endHour);
    }

    @Override
    public List<Rate> listAllProcessItemNgRateTop5(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessItemNgRateTop5(wrapper);
    }

    @Override
    public List<Rate> listAllProcessOkRate2(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessOkRate2(wrapper);
    }

    @Override
    public DfQmsIpqcWaigTotalCheck getProcessYield(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getProcessYield(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> detailPageApparenceYield(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.detailPageApparenceYield(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate> listAllProcessItemNgRateTop5V2(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessItemNgRateTop5V2(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate2> listAllProcessItemNgRateTop5V3(String startTime, String endTime) {
        return dfQmsIpqcWaigTotalCheckMapper.listAllProcessItemNgRateTop5V3(startTime, endTime);
    }

    @Override
    public List<Rate> listCNC0OkRateOneMonth(QueryWrapper<DfQmsIpqcWaigTotalCheck> qw) {
        return dfQmsIpqcWaigTotalCheckMapper.listCNC0OkRateOneMonth(qw);
    }

    @Override
    public List<Rate3> listProcessOkRateOneMonth(String factoryId, String project,String startTime, String endTime, String process) {
        return dfQmsIpqcWaigTotalCheckMapper.listProcessOkRateOneMonth(factoryId,project,startTime,endTime,process);
    }

    @Override
    public Rate getAllYield(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getAllYield(wrapper);
    }

    @Override
    public Rate getLineYield(Wrapper<DfQmsIpqcWaigTotalCheck> wrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.getLineYield(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotalCheck> weekOfPoorTOP3Warning(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew) {
        return dfQmsIpqcWaigTotalCheckMapper.weekOfPoorTOP3Warning(ew);
    }

    @Override
    public List<Rate3> twoWeekOfPoorTOP3Warning() {
        return dfQmsIpqcWaigTotalCheckMapper.twoWeekOfPoorTOP3Warning();
    }

//    @Override
//    public List<DfQmsIpqcWaigTotalCheck> listWaigExcelData(QueryWrapper<DfQmsIpqcWaigTotalCheck> ew) {
//        return dfQmsIpqcWaigTotalCheckMapper.listWaigExcelData(ew);
//    }

    @Override
    public List<DfQmsIpqcWaigTotalCheck> listWaigExcelDataPage(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalCheck> ew,
                                                          @Param("pageOffset") int pageOffset,
                                                          @Param("pageSize") int pageSize,
                                                          @Param("floor") String floor,
                                                          @Param("project") String project)
    {
        return dfQmsIpqcWaigTotalCheckMapper.listWaigExcelDataPage(ew,pageOffset,pageSize,floor,project);
    }

    @Override
    public List<Map<String, Object>> exportInspectionTableForProcess(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.exportInspectionTableForProcess(waigWrapper);
    }

    @Override
    public List<Map<String, Object>> exportInspectionTableForProcessBymachineId(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.exportInspectionTableForProcessBymachineId(waigWrapper);
    }

    @Override
    public List<Map<String, Object>> exportNgClassificationScale(QueryWrapper<DfQmsIpqcWaigDetailCheck> waigWrapper) {
        return dfQmsIpqcWaigTotalCheckMapper.exportNgClassificationScale(waigWrapper);
    }

    @Override
    public List<Rate3> getMachineOkNumList(QueryWrapper<Rate3> qw) {
        return dfQmsIpqcWaigTotalCheckMapper.getMachineOkNumList(qw);
    }

    @Override
    public List<Map<String, Object>> getWGDetailInfoList(QueryWrapper<Map<String, Object>> qw) {
        return dfQmsIpqcWaigTotalCheckMapper.getWGDetailInfoList(qw);
    }

}
