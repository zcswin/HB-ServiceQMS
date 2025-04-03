package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfIqcMaterialConfig;

import java.util.List;

// 接口
public interface DfIqcMaterialConfigService extends IService<DfIqcMaterialConfig> {

    // 自定义关联查询方法（返回列表）
    List<DfIqcMaterialConfig> listConfigWithJoin(Wrapper<DfIqcMaterialConfig> wrapper);
}