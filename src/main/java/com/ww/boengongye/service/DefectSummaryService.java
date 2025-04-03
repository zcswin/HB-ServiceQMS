package com.ww.boengongye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DefectSummary;

import java.util.List;

public interface DefectSummaryService  extends IService<DefectSummary> {

    List<DefectSummary> batchQuery(List<Integer> allIds);


    }
