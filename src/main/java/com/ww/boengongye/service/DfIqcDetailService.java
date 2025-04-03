package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfIqcDetail;

import java.util.List;

// 接口
public interface DfIqcDetailService extends IService<DfIqcDetail> {

    // 自定义关联查询方法（返回列表）
    List<DfIqcDetail> listDetailWithJoin(Wrapper<DfIqcDetail> wrapper);
}
