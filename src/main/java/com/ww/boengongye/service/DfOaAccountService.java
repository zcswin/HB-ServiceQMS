package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfOaAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-17
 */
public interface DfOaAccountService extends IService<DfOaAccount> {

    boolean isAccountExist(String account);
}
