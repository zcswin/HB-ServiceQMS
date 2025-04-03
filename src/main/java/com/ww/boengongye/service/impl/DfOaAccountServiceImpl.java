package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ww.boengongye.entity.DfOaAccount;
import com.ww.boengongye.mapper.DfOaAccountMapper;
import com.ww.boengongye.service.DfOaAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-17
 */
@Service
public class DfOaAccountServiceImpl extends ServiceImpl<DfOaAccountMapper, DfOaAccount> implements DfOaAccountService {

    @Autowired
    private DfOaAccountMapper dfOaAccountMapper;

    @Override
    public boolean isAccountExist(String account) {
        Integer count = dfOaAccountMapper.selectCount(new LambdaQueryWrapper<DfOaAccount>()
                .eq(DfOaAccount::getAccount, account));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
