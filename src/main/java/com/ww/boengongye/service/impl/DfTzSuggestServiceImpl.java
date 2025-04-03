package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfTzSuggest;
import com.ww.boengongye.mapper.DfTzSuggestMapper;
import com.ww.boengongye.service.DfTzSuggestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2024-12-26
 */
@Service
public class DfTzSuggestServiceImpl extends ServiceImpl<DfTzSuggestMapper, DfTzSuggest> implements DfTzSuggestService {

    @Autowired
    DfTzSuggestMapper dfTzSuggestMapper;

    @Override
    public DfTzSuggest getResult(Double value,String name) {
        return dfTzSuggestMapper.getResult(value,name);
    }
}
