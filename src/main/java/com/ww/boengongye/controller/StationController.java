package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ww.boengongye.entity.SaveList;
import com.ww.boengongye.entity.Station;
import com.ww.boengongye.entity.StationRelationMenu;
import com.ww.boengongye.entity.UserDepartment;
import com.ww.boengongye.service.StationRelationMenuService;
import com.ww.boengongye.service.StationService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
import com.ww.boengongye.utils.XXSFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smartbi.sdk.ClientConnector;
import smartbi.sdk.RemoteException;
import smartbi.sdk.service.user.UserManagerService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 岗位表 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-01-25
 */
@Controller
@RequestMapping("/station")
@ResponseBody
@CrossOrigin
public class StationController {
    private static final Logger logger = LoggerFactory.getLogger(StationController.class);

    @Autowired
    com.ww.boengongye.service.StationService StationService;
    @Autowired
    com.ww.boengongye.service.UserDepartmentService UserDepartmentService;
    @Autowired
    com.ww.boengongye.service.StationRelationMenuService StationRelationMenuService;

    @Value("${bi.url}")
    private String url;

    @Value("${bi.account}")
    private String account;

    @Value("${bi.password}")
    private String password;


    @RequestMapping(value = "/listByUser")
    public Result listByUser(String id) {
        QueryWrapper<Station> qw = new QueryWrapper<>();
        qw.inSql("id", "select station_id from  user_relation_station where user_id='" + id + "'");
        List<Station> datas = StationService.list(qw);
        Map<String, String> map = new HashMap<>();
        if (datas.size() > 0) {
            for (Station s : datas) {
                map.put(s.id, "");
            }
        }

        List<Station> all = StationService.list();
        if (all.size() > 0) {
            for (Station s : all) {
                if (map.containsKey(s.id)) {
                    s.setUpdateName("select");
                }
            }
        }
        return new Result(0, "查询成功", all);
    }


    @RequestMapping(value = "/listByUser2")
    public Result listByUser2(String id, String departmentId) {
        QueryWrapper<Station> qw = new QueryWrapper<>();
        qw.inSql("id", "select station_id from  user_relation_station where user_id='" + id + "'");
        List<Station> datas = StationService.list(qw);
        Map<String, String> map = new HashMap<>();
        if (datas.size() > 0) {
            for (Station s : datas) {
                map.put(s.id, "");
            }
        }
        QueryWrapper<Station> qw2 = new QueryWrapper<>();
        qw2.eq("group_id", departmentId);

//        QueryWrapper<UserDepartment> qw3 = new QueryWrapper<>();
//        UserDepartment ud = UserDepartmentService.getById(departmentId);
        //判断是否是管理员,管理员就查出全部角色
//        if (null == ud||null==ud.getManagerId()||!ud.getManagerId().equals(id)) {
//                qw2.eq("type", "部门人员");
//        }
        List<Station> all = StationService.list(qw2);
        if (all.size() > 0) {
            for (Station s : all) {
                if (map.containsKey(s.id)) {
                    s.setUpdateName("select");
                }
            }
        }
        return new Result(0, "查询成功", all);
    }

    @RequestMapping(value = "/listAll")
    public Result listAll() {
        return new Result(0, "查询成功", StationService.list());
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody Station datas) {
//        try {
//        Field[] f= Station.class.getDeclaredFields();
//        //给TAnnals对象赋值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            try{
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod= Station.class.getMethod("set"+methodName,String.class);
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
//    }
//        ClientConnector conn = new ClientConnector(url);
//        conn.open(account, password); // 以管理员身份登录

        try {
            // 创建用户管理服务
//            UserManagerService userManagerService = new UserManagerService(conn);
            if (null != datas.getId()) {
//                boolean updateRet = userManagerService.updateRole(datas.getId(), datas.getAlias(), datas.getDataDesc());
//                if (updateRet) {
                if (StationService.updateById(datas)) {
                    return new Result(200, "保存成功");
                } else {
                    return new Result(500, "保存失败");
                }
//                }
            } else {
                datas.setCreateTime(TimeUtil.getNowTimeByNormal());
//                String userId1 = userManagerService.createRole(datas.getName(), datas.getAlias(), datas.getDataDesc(), datas.getGroupId());
//                datas.setId(userId1);
                datas.setId(CommunalUtils.getUUID());
                if (StationService.save(datas)) {

                    return new Result(200, "保存成功");
                } else {
                    return new Result(500, "保存失败");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭应用连接器
//            conn.close();
        }
        return new Result(500, "保存失败");
//        } catch (NullPointerException e) {
//            logger.error("保存操作记录接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
//        try {
        ClientConnector conn = new ClientConnector(url);
        conn.open(account, password); // 以管理员身份登录
        try {
            // 创建用户管理服务
            UserManagerService userManagerService = new UserManagerService(conn);
            if (userManagerService.deleteRole(id)) {
                if (StationService.removeById(id)) {
                    UpdateWrapper<StationRelationMenu> menuUw = new UpdateWrapper<>();
                    menuUw.eq("station_id", id);
                    StationRelationMenuService.remove(menuUw);
                    return new Result(200, "删除成功");
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            // 关闭应用连接器
            conn.close();
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit, String name, String groupId) {
//        try {
        Page<Station> pages = new Page<Station>(page, limit);
        QueryWrapper<Station> ew = new QueryWrapper<Station>();
        if (null != name && !name.equals("")) {
            ew.like("name", name);
        }
        if (null != groupId && !groupId.equals("")) {
            ew.eq("group_id", groupId);
        }

        ew.orderByDesc("create_time");
        IPage<Station> list = StationService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/saveMenus", method = RequestMethod.POST)
    public Result saveMenus(@RequestBody Station datas) {
//        try {

        if (null != datas.getId()) {

            if (datas.getSaveList().size() > 0) {
                List<StationRelationMenu> srmList = new ArrayList<>();
                for (SaveList s : datas.getSaveList()) {
                    StationRelationMenu srm = new StationRelationMenu();
                    srm.setCreateTime(TimeUtil.getNowTimeByNormal());
                    srm.setMenuId(s.getId());
                    srm.setStationId(datas.getId());
                    srmList.add(srm);
                }

                UpdateWrapper<StationRelationMenu> menuUw = new UpdateWrapper<>();
                menuUw.eq("station_id", datas.getId());
                StationRelationMenuService.remove(menuUw);
                StationRelationMenuService.saveBatch(srmList);
            }


            return new Result(200, "保存成功");

        }
        return new Result(500, "保存失败");

    }


    @RequestMapping(value = "/getPermission")
    public Result getPermission(String userId) {

        String permission = "";
        int i = 0;
        HashMap<String, String> map = new HashMap<>();
        QueryWrapper<Station> qw = new QueryWrapper<>();
        qw.inSql("id", "select station_id from user_relation_station where user_id='" + userId + "'");
        List<Station> data = StationService.list(qw);
        if (data.size() > 0) {
            for (Station s : data) {
                if (null != s.getPermission() && !s.getPermission().equals("") && !map.containsKey(s.getPermission())) {
                    if (i > 0) {
                        permission += ",";
                    }
                    permission += s.getPermission();
                    map.put(s.getPermission(), s.getPermission());
                    i++;
                }
            }
        }
        return new Result(0, "查询成功", permission);

    }


}
