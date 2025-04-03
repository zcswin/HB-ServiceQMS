package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfSizeFail;
import com.ww.boengongye.mapper.DfSizeFailMapper;
import com.ww.boengongye.service.DfSizeFailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Service
public class DfSizeFailServiceImpl extends ServiceImpl<DfSizeFailMapper, DfSizeFail> implements DfSizeFailService {
    @Autowired
    DfSizeFailMapper dfSizeFailMapper;

    @Override
    public List<DfSizeFail> listKeyPoint(Wrapper<DfSizeFail> wrapper) {
        return dfSizeFailMapper.listKeyPoint(wrapper);
    }
}
