package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfTzSuggest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2024-12-26
 */
public interface DfTzSuggestService extends IService<DfTzSuggest> {
    DfTzSuggest getResult(Double value,String name);
}
