package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.MenuService;
import com.ww.boengongye.service.StationService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.Result2;
import com.ww.boengongye.utils.TimeUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-01-25
 */
@Controller
@RequestMapping("/menu")
@ResponseBody
@CrossOrigin
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    com.ww.boengongye.service.MenuService MenuService;

    @Autowired
    com.ww.boengongye.service.UserService UserService;

    @Autowired
    com.ww.boengongye.service.StationService StationService;




    @RequestMapping(value = "/getMenu")
    public Result2 getMenu(String account,String access_token) {
        System.out.println(account);
        List<MenuData> list=new ArrayList<>();

        QueryWrapper<User>uqw=new QueryWrapper<>();
        uqw.eq("name",account);
        uqw.last("limit 0,1");
        User user=UserService.getOne(uqw);
        if(null!=user){
            QueryWrapper<Menu>mqw=new QueryWrapper<>();
//            mqw.inSql("id","select parent_id from menu where id in(  select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"'))");

            mqw.and(wrapper -> wrapper.inSql("id","select parent_id from menu where id in(  select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"'))")
                    .or().inSql("id", "select id  from menu where id in(select menu_id FROM station_relation_menu WHERE station_id IN ( SELECT station_id FROM user_relation_station WHERE user_id = '"+user.getId()+"' )) and parent_id=0")
            );


            mqw.eq("is_use","1");
            mqw.eq("menu_type","管理后台");
            mqw.orderByAsc("sort");
            List<Menu> menus=MenuService.list(mqw);


            if(menus.size()>0){
                for(Menu m:menus){
                    MenuData data=new MenuData();
                    data.setIcon(m.getIcon());
                    data.setName(m.getName());
                    data.setTitle(m.getTitle());
                    List<MenuData> sons=new ArrayList<>();
                    QueryWrapper<Menu>mqw2=new QueryWrapper<>();
                    mqw2.eq("parent_id",m.getId());
                    mqw2.eq("is_use","1");
                    mqw2.eq("menu_type","管理后台");
//                    mqw2.inSql("id","select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"')");
                    mqw2.inSql("id","select m.menu_id from station_relation_menu m join (select station_id from user_relation_station where user_id='"+user.getId()+"') as ddd on m.station_id=ddd.station_id");
                    mqw2.orderByAsc("sort");
                    List<Menu> ss=MenuService.list(mqw2);
                    if(ss.size()>0){
                        for(Menu s:ss){
                            MenuData data2=new MenuData();
                            data2.setName(s.getName());
                            data2.setTitle(s.getTitle());
                            data2.setIsUrl(s.getIsUrl());
                            data2.setJump(s.getJump());
    //第三层
//                            List<MenuData> sons3=new ArrayList<>();
//                            QueryWrapper<Menu>mqw3=new QueryWrapper<>();
//                            mqw3.eq("parent_id",s.getId());
//                            mqw3.eq("is_use","1");
//                            mqw3.inSql("id","select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"')");
//                            mqw3.orderByAsc("sort");
//                            List<Menu> ss3=MenuService.list(mqw3);
//                            if(ss3.size()>0){
//                                for(Menu s3:ss3){
//                                    MenuData data3=new MenuData();
//                                    data3.setName(s3.getName());
//                                    data3.setTitle(s3.getTitle());
//                                    data3.setIsUrl(s3.getIsUrl());
//                                    data3.setJump(s3.getJump());
//                                    sons3.add(data3);
//                                }
//                                data2.setList(sons3);
//                            }
                            sons.add(data2);
                        }
                        data.setList(sons);
                    }
                    list.add(data);
                }
            }
        }

        return new Result2(0, "查询成功",list);
    }


    @RequestMapping(value = "/listAll")
    public Result listAll() {
        return new Result(0, "查询成功",MenuService.list());
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody Menu datas) {
//        try {
//        Field[] f= Menu.class.getDeclaredFields();
//        //给TAnnals对象赋值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            try{
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod= Menu.class.getMethod("set"+methodName,String.class);
//                if(!methodName.equals("IntroductionLand")){
//                    //执行该set方法
//                    setMethod.invoke(datas, XXSFilter.checkStr(XXSFilter.getFieldValueByFieldName(attributeName,datas)));
//                }
//
//            }catch (NoSuchMethodException e) {
//                logger.error("接口异常", e);
//            } catch (IllegalAccessException e) {
//                logger.error("接口异常", e);
//            } catch (InvocationTargetException e) {
//                logger.error("接口异常", e);
//            }
//        }
        if(null!=datas.getJump()&&!datas.getJump().equals("")){
            datas.setIsUrl("2");
        }
        if (null != datas.getId()) {
            if (MenuService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {
            if(datas.getParentId()==0){
                datas.setIcon("layui-icon-template");
            }
            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (MenuService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        if (MenuService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String name) {
//        try {
        Page<Menu> pages = new Page<Menu>(page, limit);
        QueryWrapper<Menu> ew=new QueryWrapper<Menu>();
        if(null!=name&&!name.equals("")) {
            ew.like("name", name);
        }

        ew.orderByDesc("create_time");
        IPage<Menu> list=MenuService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

    //获取树状菜单
    @RequestMapping(value = "/getTree")
    public Object getTree(int id) {
        try {
            Map<Integer,Integer> map=new HashMap<>();
            if(id!=0){
                QueryWrapper<Menu>tw=new QueryWrapper<>();
                tw.eq("parent_id","0");
                List<Menu> relations=MenuService.list(tw);
                if(relations.size()>0){
                    for(Menu t:relations){
                        map.put(t.getId(),t.getId());
                    }
                }
            }

            List<LayuiTree> list = new ArrayList<>();
            QueryWrapper<Menu> ew = new QueryWrapper<Menu>();
            ew.eq("parent_id", 0);
            LayuiTree ra = new LayuiTree();
            ra.setId(0);
            ra.setTitle("菜单栏");
            ra.setSpread(true);

            List<Menu> trs = MenuService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree> list2 = new ArrayList<>();
                for (Menu t : trs) {
                    System.out.println(t.title);
                    QueryWrapper<Menu> ew2 = new QueryWrapper<Menu>();
                    ew2.eq("parent_id", t.getId());
                    List<Menu> tcs = MenuService.list(ew2);
                    LayuiTree raf = new LayuiTree();
                    raf.setId(t.getId());
                    raf.setTitle(t.getTitle());
                    raf.setSpread(true);
                    raf.setParentId(t.getParentId());
                    if(map.containsKey(t.getId())){
                        raf.setOnSelected(true);
                    }else{
                        raf.setOnSelected(false);
                    }
                    List<LayuiTree> list3 = new ArrayList<>();
                    if (tcs.size() > 0) {
                        for (Menu tc : tcs) {
                            LayuiTree rac = new LayuiTree();
                            rac.setId(tc.getId());
                            rac.setTitle(tc.getTitle());
                            rac.setSpread(true);
                            rac.setParentId(t.getId());
                            if(map.containsKey(tc.getId())){
                                rac.setOnSelected(true);
                            }else{
                                rac.setOnSelected(false);
                            }


                            QueryWrapper<Menu> ew3 = new QueryWrapper<Menu>();
                            ew3.eq("parent_id", tc.getId());
                            List<Menu> tcs3 = MenuService.list(ew3);

                            List<LayuiTree> list4 = new ArrayList<>();
                            if (tcs3.size() > 0) {
                                for (Menu tc3 : tcs3) {
                                    LayuiTree rac3 = new LayuiTree();
                                    rac3.setId(tc3.getId());
                                    rac3.setTitle(tc3.getTitle());
                                    rac3.setSpread(true);
                                    rac3.setParentId(tc.getId());
                                    if (map.containsKey(tc3.getId())) {
                                        rac3.setOnSelected(true);
                                    } else {
                                        rac3.setOnSelected(false);
                                    }
                                    list4.add(rac3);
                                }
                            }
                            rac.setChildren(list4);
                            list3.add(rac);

                        }
                    }
                    raf.setChildren(list3);
                    list2.add(raf);

                }
                ra.setChildren(list2);
            }
            list.add(ra);
            return list;
//		return new Result(200, "查询成功", list);
        }catch(Exception e) {
            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    //获取树状菜单
    @RequestMapping(value = "/getTree2")
    public Object getTree2(int id,String type) {
        try {
            Map<Integer,Integer> map=new HashMap<>();
            if(id!=0){
                QueryWrapper<Menu>tw=new QueryWrapper<>();
                tw.eq("parent_id","0");
                List<Menu> relations=MenuService.list(tw);
                if(relations.size()>0){
                    for(Menu t:relations){
                        map.put(t.getId(),t.getId());
                    }
                }
            }

            List<LayuiTree> list = new ArrayList<>();
            QueryWrapper<Menu> ew = new QueryWrapper<Menu>();
            ew.eq("parent_id", 0);
//            ew.eq("type", type);
            LayuiTree ra = new LayuiTree();
            ra.setId(0);
            ra.setTitle("菜单栏");
            ra.setSpread(true);

            List<Menu> trs = MenuService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree> list2 = new ArrayList<>();
                for (Menu t : trs) {
                    System.out.println(t.title);
                    QueryWrapper<Menu> ew2 = new QueryWrapper<Menu>();
                    ew2.eq("parent_id", t.getId());
                    if(!type.equals("管理员")){
                        ew2.eq("type", type);
                    }

                    List<Menu> tcs = MenuService.list(ew2);
                    LayuiTree raf = new LayuiTree();
                    raf.setId(t.getId());
                    raf.setTitle(t.getTitle());
                    raf.setSpread(true);
                    raf.setParentId(t.getParentId());
                    if(map.containsKey(t.getId())){
                        raf.setOnSelected(true);
                    }else{
                        raf.setOnSelected(false);
                    }
                    List<LayuiTree> list3 = new ArrayList<>();
                    if (tcs.size() > 0) {
                        for (Menu tc : tcs) {
                            LayuiTree rac = new LayuiTree();
                            rac.setId(tc.getId());
                            rac.setTitle(tc.getTitle());
                            rac.setSpread(true);
                            rac.setParentId(t.getId());
                            if(map.containsKey(tc.getId())){
                                rac.setOnSelected(true);
                            }else{
                                rac.setOnSelected(false);
                            }


                            QueryWrapper<Menu> ew3 = new QueryWrapper<Menu>();
                            ew3.eq("parent_id", tc.getId());
                            if(!type.equals("管理员")) {
                                ew3.eq("type", type);
                            }
                            List<Menu> tcs3 = MenuService.list(ew3);

                            List<LayuiTree> list4 = new ArrayList<>();
                            if (tcs3.size() > 0) {
                                for (Menu tc3 : tcs3) {
                                    LayuiTree rac3 = new LayuiTree();
                                    rac3.setId(tc3.getId());
                                    rac3.setTitle(tc3.getTitle());
                                    rac3.setSpread(true);
                                    rac3.setParentId(tc.getId());
                                    if (map.containsKey(tc3.getId())) {
                                        rac3.setOnSelected(true);
                                    } else {
                                        rac3.setOnSelected(false);
                                    }
                                    list4.add(rac3);
                                }
                            }
                            rac.setChildren(list4);
                            list3.add(rac);

                        }
                    }
                    raf.setChildren(list3);
                    list2.add(raf);

                }
                ra.setChildren(list2);
            }
            list.add(ra);
            return list;
//		return new Result(200, "查询成功", list);
        }catch(Exception e) {
            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listByStationId")
    public Result listByStationId(String id ) {
//        try {
        QueryWrapper<Menu> ew=new QueryWrapper<Menu>();
        ew.inSql("id", "select menu_id from station_relation_menu where station_id='"+id+"'");
        return new Result(0, "查询成功",MenuService.list(ew));
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }
}
