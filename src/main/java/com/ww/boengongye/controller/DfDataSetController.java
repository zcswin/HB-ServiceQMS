package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.DfDataSet;
import com.ww.boengongye.entity.Menu;
import com.ww.boengongye.entity.MenuData;
import com.ww.boengongye.entity.User;
import com.ww.boengongye.service.DfMacModelPositionService;
import com.ww.boengongye.utils.Base64Utils;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import smartbi.catalogtree.ICatalogElement;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.service.catalog.CatalogService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-08-29
 */
@Controller
@RequestMapping("/dfDataSet")
@ResponseBody
@CrossOrigin
public class DfDataSetController {
    @Autowired
    com.ww.boengongye.service.DfMacModelPositionService DfMacModelPositionService;

    private static final Logger logger = LoggerFactory.getLogger(UserDepartmentController.class);

    @Autowired
    com.ww.boengongye.service.UserService UserService;
    @Value("${bi.url}")
    private String url;



    //获取用户的大屏资源
    @RequestMapping(value = "/getScreen")
    public Object getScreen(String name) {

        ClientConnector conn = new ClientConnector(url);

        QueryWrapper<User>qw=new QueryWrapper<User>();
        qw.eq("name",name);
        User user= UserService.getOne(qw);
        conn.open(name, Base64Utils.decodeBase64(user.getPassword())); // 以管理员身份登录

        List<MenuData> saveDatas=new ArrayList<>();

        CatalogService catalogService = new CatalogService(conn);

        List<? extends ICatalogElement> datas= catalogService.getChildElements("PUBLIC_ANALYSIS");
        for( ICatalogElement d:datas){
            MenuData m=new MenuData();
            m.setName(d.getName());
            m.setTitle(d.getAlias());
            m.setJump(d.getId());
            if(d.isHasChild()){
                List<MenuData> saveSonDatas=new ArrayList<>();
                List<? extends ICatalogElement> sonDatas= catalogService.getChildElements(d.getId());
                for( ICatalogElement sd:sonDatas) {
                    MenuData sm=new MenuData();
                    sm.setName(sd.getName());
                   sm.setTitle(sd.getAlias());
                    sm.setJump(sd.getId());
                    saveSonDatas.add(sm);
                }
                m.setList(saveSonDatas);
            }
            saveDatas.add(m);
        }
        conn.close();

        return new Result(0, "查询成功",saveDatas);
    }

    //获取用户的大屏资源
    @RequestMapping(value = "/listScreen")
    public Object listScreen(String name) {
        ClientConnector conn = new ClientConnector(url);
        QueryWrapper<User>qw=new QueryWrapper<User>();
        qw.eq("name",name);
        User user= UserService.getOne(qw);
        conn.open(name, Base64Utils.decodeBase64(user.getPassword())); // 以管理员身份登录
        List<MenuData> saveDatas=new ArrayList<>();
        CatalogService catalogService = new CatalogService(conn);
        List<? extends ICatalogElement> datas= catalogService.getChildElements("PUBLIC_ANALYSIS");
        for( ICatalogElement d:datas){
            if(d.getName().equals("大屏可视化")){
                if(d.isHasChild()){
                    List<MenuData> saveSonDatas=new ArrayList<>();
                    List<? extends ICatalogElement> sonDatas= catalogService.getChildElements(d.getId());
                    for( ICatalogElement sd:sonDatas) {
                        MenuData m=new MenuData();
                        m.setName(sd.getName());
                        m.setTitle(sd.getAlias());
                        m.setJump(sd.getId());
                        saveDatas.add(m);
                    }

                }
                break;
            }
        }
        conn.close();

        return new Result(0, "查询成功",saveDatas);
    }
}
