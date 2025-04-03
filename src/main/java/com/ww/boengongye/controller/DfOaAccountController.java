package com.ww.boengongye.controller;


import com.ww.boengongye.entity.DfOaAccount;
import com.ww.boengongye.service.DfOaAccountService;
import com.ww.boengongye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-17
 */
@Controller
@RequestMapping("/dfOaAccount")
@CrossOrigin
@ResponseBody
public class DfOaAccountController {

    @Autowired
    private DfOaAccountService dfOaAccountService;

    @GetMapping("/listAll")
    public Result listAll() {
        List<DfOaAccount> list = dfOaAccountService.list();
        for (DfOaAccount dfOaAccount : list) {
            dfOaAccount.setPassword(null);
        }
        return new Result(200, "查询成功", list);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody Map<String, Object> map) {
        if (dfOaAccountService.isAccountExist((String)map.get("account"))) {
            return new Result(600, "OA账号已存在,请使用其他账号!");
        }
        DfOaAccount account = new DfOaAccount();
        account.setAccount((String)map.get("account"));
        account.setPassword((String)map.get("password"));
        account.setCreateMan((String)map.get("createMan"));
        account.setUpdateMan((String)map.get("createMan"));
        boolean b = dfOaAccountService.save(account);
        if (b) {
            return Result.INSERT_SUCCESS;
        } else {
            return Result.INSERT_FAILED;
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Map<String, Object> map) {
        DfOaAccount dfOaAccount = new DfOaAccount();
        dfOaAccount.setId((Integer)map.get("id"));
        dfOaAccount.setPassword((String)map.get("password"));
        dfOaAccount.setUpdateMan((String)map.get("updateMan"));
        boolean b = dfOaAccountService.updateById(dfOaAccount);
        if (b) {
            return Result.UPDATE_SUCCESS;
        } else {
            return Result.UPDATE_FAILED;
        }
    }
}
