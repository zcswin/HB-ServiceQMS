package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfGroup;
import com.ww.boengongye.mapper.DfGroupMapper;
import com.ww.boengongye.service.DfGroupService;
import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

//import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
@Service
public class DfGroupServiceImpl extends ServiceImpl<DfGroupMapper, DfGroup> implements DfGroupService {

    @Autowired
    private DfGroupMapper dfGroupMapper;

    @Override
    @Transactional
    public int importExcel(MultipartFile file) throws Exception {
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfGroup> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            DfGroup data = new DfGroup();
            data.setFactory(map.get("工厂"));
            data.setProcess(map.get("工序"));
            data.setLinebody(map.get("线体"));
            data.setSingleMonthWork(map.get("单月"));
            data.setDoubleMonthWork(map.get("双月"));
            data.setMachineCode(map.get("机台号"));
            data.setfGroup(map.get("组别"));
            data.setRespon(map.get("负责人"));
            System.out.println(data);
            list.add(data);

        }
        saveBatch(list);
        return list.size();
    }


    @Override
    public Map<String, Integer> getMachineCode(String month, String dayNight, String process) {
        LambdaQueryWrapper<DfGroup> qw = new LambdaQueryWrapper<>();
        if (month.equals("双月")) {
            qw.eq(DfGroup::getDoubleMonthWork, dayNight);
        } else {
            qw.eq(DfGroup::getSingleMonthWork, dayNight);
        }
        qw.eq(DfGroup::getProcess, process);

        List<DfGroup> list = list(qw);
        Map<String, Integer> result = new HashMap<>();
        for (DfGroup dfGroup : list) {
            for (Integer i = dfGroup.getMacMin1(); i <= dfGroup.getMacMax1(); i++) {
                result.put(i.toString(), dfGroup.getId());
            }
            if (dfGroup.getMacMin2() != null) {
                for (Integer i = dfGroup.getMacMin2(); i <= dfGroup.getMacMax2(); i++) {
                    result.put(i.toString(), dfGroup.getId());
                }
            }
        }
        return result;
    }

    // 获取小组的机台总数
    @Override
    public Map<Integer, Integer> getGroupMacNum(String month, String dayNight) {
        LambdaQueryWrapper<DfGroup> qw = new LambdaQueryWrapper<>();
        if (month.equals("双月")) {
            qw.eq(DfGroup::getDoubleMonthWork, dayNight);
        } else {
            qw.eq(DfGroup::getSingleMonthWork, dayNight);
        }

        List<DfGroup> list = list(qw);
        Map<Integer, Integer> result = new HashMap<>();
        for (DfGroup dfGroup : list) {
            Integer macNum = dfGroup.getMacMax1() - dfGroup.getMacMin1() + 1;
            if (dfGroup.getMacMin2() != null) {
                macNum += dfGroup.getMacMax2() - dfGroup.getMacMin2() + 1;
            }
            result.put(dfGroup.getId(), macNum);
        }
        return result;
    }

    @Override
    public Map<String, Integer> getMacResGroupId(String month, String dayNight) {
        QueryWrapper<DfGroup> qw = new QueryWrapper<>();
        if (month.equals("双月")) {
            qw.eq("grp.double_month_work", dayNight);
        } else {
            qw.eq("grp.single_month_work", dayNight);
        }
        List<DfGroup> dfGroups = dfGroupMapper.listByProcess(qw);
        Map<String, Integer> macResGroupId = new HashMap<>();
        for (DfGroup dfGroup : dfGroups) {
            for (Integer i = dfGroup.getMacMin1(); i <= dfGroup.getMacMax1(); i++) {
                macResGroupId.put(dfGroup.getFirstCode() + String.format("%03d",i), dfGroup.getId());
            }
            if (dfGroup.getMacMin2() != null) {
                for (Integer i = dfGroup.getMacMin2(); i <= dfGroup.getMacMax2(); i++) {
                    macResGroupId.put(dfGroup.getFirstCode() + String.format("%03d",i), dfGroup.getId());
                }
            }
        }
        return macResGroupId;
    }

    @Override
    public Map<String, String> getMacResRespon(String month, String dayNight) {
        QueryWrapper<DfGroup> qw = new QueryWrapper<>();
        if (month.equals("双月")) {
            qw.eq("grp.double_month_work", dayNight);
        } else {
            qw.eq("grp.single_month_work", dayNight);
        }
        List<DfGroup> dfGroups = dfGroupMapper.listByProcess(qw);
        Map<String, String> macResGroupId = new HashMap<>();
        for (DfGroup dfGroup : dfGroups) {
            for (Integer i = dfGroup.getMacMin1(); i <= dfGroup.getMacMax1(); i++) {
                macResGroupId.put(dfGroup.getFirstCode() + String.format("%03d",i), dfGroup.getRespon());
            }
            if (dfGroup.getMacMin2() != null) {
                for (Integer i = dfGroup.getMacMin2(); i <= dfGroup.getMacMax2(); i++) {
                    macResGroupId.put(dfGroup.getFirstCode() + String.format("%03d",i), dfGroup.getRespon());
                }
            }
        }
        return macResGroupId;
    }

}
