package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DefectSummary;
import com.ww.boengongye.mapper.DefectSummaryMapper;
import com.ww.boengongye.service.DefectSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefectSummaryServiceImpl extends ServiceImpl<DefectSummaryMapper, DefectSummary> implements DefectSummaryService {

    @Resource
    DefectSummaryMapper defectSummaryMapper;
    // Service层实现分批
    @Override
    public List<DefectSummary> batchQuery(List<Integer> allIds) {
        int batchSize = 1000;
        List<DefectSummary> result = new ArrayList<>();

        for (int i = 0; i < allIds.size(); i += batchSize) {
            List<Integer> batchIds = allIds.subList(i, Math.min(i + batchSize, allIds.size()));
            result.addAll(defectSummaryMapper.safeListByProcessSummaryIds(batchIds));
        }

        return result;
    }
}
