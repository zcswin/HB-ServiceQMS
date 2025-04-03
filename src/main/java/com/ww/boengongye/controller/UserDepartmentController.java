package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-02
 */
@Controller
@RequestMapping("/userDepartment")
@ResponseBody
@CrossOrigin
public class UserDepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(UserDepartmentController.class);

    @Autowired
    com.ww.boengongye.service.UserDepartmentService UserDepartmentService;

    //获取树状菜单
    @RequestMapping(value = "/getTree")
    public Object getTree(int id) {
        try {
            Map<String, String> map = new HashMap<>();
            if (id != 0) {
                QueryWrapper<UserDepartment> tw = new QueryWrapper<>();
                tw.eq("parent_id", "DEPARTMENT");
                List<UserDepartment> relations = UserDepartmentService.list(tw);
                if (relations.size() > 0) {
                    for (UserDepartment t : relations) {
                        map.put(t.getId(), t.getId());
                    }
                }
            }

            List<LayuiTree2> list = new ArrayList<>();
            QueryWrapper<UserDepartment> ew = new QueryWrapper<UserDepartment>();
            ew.eq("parent_id", "DEPARTMENT");
            LayuiTree2 ra = new LayuiTree2();
            ra.setId("DEPARTMENT");
            ra.setTitle("惠州");
            ra.setSpread(true);
            ra.setDepartmentCode("ROOT");
            List<UserDepartment> trs = UserDepartmentService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree2> list2 = new ArrayList<>();
                for (UserDepartment t : trs) {
                    QueryWrapper<UserDepartment> ew2 = new QueryWrapper<UserDepartment>();
                    ew2.eq("parent_id", t.getId());
                    List<UserDepartment> tcs = UserDepartmentService.list(ew2);
                    LayuiTree2 raf = new LayuiTree2();
                    raf.setId(t.getId());
                    raf.setTitle(t.getName());
                    raf.setSpread(true);
                    raf.setParentId(t.getParentId());
                    raf.setDepartmentCode(t.getDepartmentCode());
                    if(null!=t.getManagerId()){
                        raf.setManagerId(t.getManagerId());
                    }
                    if (map.containsKey(t.getId())) {
                        raf.setOnSelected(true);
                    } else {
                        raf.setOnSelected(false);
                    }
                    List<LayuiTree2> list3 = new ArrayList<>();
                    if (trs.size() > 0) {
                        for (UserDepartment tc : tcs) {
                            LayuiTree2 rac = new LayuiTree2();
                            rac.setId(tc.getId());
                            rac.setTitle(tc.getName());
                            rac.setSpread(true);
                            rac.setParentId(t.getId());
                            rac.setDepartmentCode(tc.getDepartmentCode());
                            if(null!=tc.getManagerId()){
                                rac.setManagerId(tc.getManagerId());
                            }
                            if (map.containsKey(tc.getId())) {
                                rac.setOnSelected(true);
                            } else {
                                rac.setOnSelected(false);
                            }
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
        } catch (Exception e) {
            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    //获取树状菜单
    @RequestMapping(value = "/getTree2")
    public Object getTree2(int id) {
        try {
            Map<String, String> map = new HashMap<>();
            Map<String, String> lv1 = new HashMap<>();
            Map<String, String> lv2 = new HashMap<>();
            if (id != 0) {
                QueryWrapper<UserDepartment> tw = new QueryWrapper<>();
                tw.eq("parent_id", "DEPARTMENT");
                List<UserDepartment> relations = UserDepartmentService.list(tw);
                if (relations.size() > 0) {
                    for (UserDepartment t : relations) {
                        map.put(t.getId(), t.getId());
                    }
                }
            }

            List<LayuiTree2> list = new ArrayList<>();
            QueryWrapper<UserDepartment> ew = new QueryWrapper<UserDepartment>();
            ew.eq("parent_id", "DEPARTMENT");
            LayuiTree2 ra = new LayuiTree2();
            ra.setId("DEPARTMENT");
            ra.setTitle("惠州");
            ra.setSpread(true);
            ra.setDepartmentCode("ROOT");
            List<UserDepartment> trs = UserDepartmentService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree2> list2 = new ArrayList<>();
                for (UserDepartment t : trs) {
                    QueryWrapper<UserDepartment> ew2 = new QueryWrapper<UserDepartment>();
                    ew2.eq("parent_id", t.getId());
                    List<UserDepartment> tcs = UserDepartmentService.list(ew2);
                    LayuiTree2 raf = new LayuiTree2();
                    raf.setId(t.getId());
                    raf.setTitle(t.getName());
                    raf.setSpread(true);
                    raf.setParentId(t.getParentId());
                    raf.setDepartmentCode(t.getDepartmentCode());
                    if (map.containsKey(t.getId())) {
                        raf.setOnSelected(true);
                    } else {
                        raf.setOnSelected(false);
                    }
                    List<LayuiTree2> list3 = new ArrayList<>();
                    if (trs.size() > 0) {
                        for (UserDepartment tc : tcs) {
                            LayuiTree2 rac = new LayuiTree2();
                            rac.setId(tc.getId());
                            rac.setTitle(tc.getName());
                            rac.setSpread(true);
                            rac.setParentId(t.getId());
                            rac.setDepartmentCode(t.getDepartmentCode());
                            if (map.containsKey(tc.getId())) {
                                rac.setOnSelected(true);
                            } else {
                                rac.setOnSelected(false);
                            }
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
        } catch (Exception e) {
            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }


    //获取树状菜单
    @RequestMapping(value = "/getByManagerId")
    public Object getByManagerId(String id) {
        try {
            Map<String, String> map = new HashMap<>();

            if (!id.equals("")) {
                QueryWrapper<UserDepartment> tw = new QueryWrapper<>();
                tw.eq("manager_id", id);
                List<UserDepartment> relations = UserDepartmentService.list(tw);
                if (relations.size() > 0) {
                    for (UserDepartment t : relations) {
                        map.put(t.getId(), t.getId());
                    }
                }
            }

            List<LayuiTree2> list = new ArrayList<>();
            QueryWrapper<UserDepartment> ew = new QueryWrapper<UserDepartment>();
            ew.eq("parent_id", "DEPARTMENT");
            LayuiTree2 ra = new LayuiTree2();
            ra.setId("DEPARTMENT");
            ra.setTitle("惠州");
            ra.setSpread(true);
            ra.setDepartmentCode("ROOT");
            if (map.containsKey(ra.getId())) {
                ra.setDisabled(false);
            } else {
                ra.setDisabled(true);
            }
            List<UserDepartment> trs = UserDepartmentService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree2> list2 = new ArrayList<>();
                for (UserDepartment t : trs) {
                    QueryWrapper<UserDepartment> ew2 = new QueryWrapper<UserDepartment>();
                    ew2.eq("parent_id", t.getId());
                    List<UserDepartment> tcs = UserDepartmentService.list(ew2);
                    LayuiTree2 raf = new LayuiTree2();
                    raf.setId(t.getId());
                    raf.setTitle(t.getName());
                    raf.setSpread(true);
                    raf.setParentId(t.getParentId());
                    raf.setDepartmentCode(t.getDepartmentCode());
                    if (map.containsKey(t.getId())) {
                        raf.setDisabled(false);
                    } else {
                        raf.setDisabled(true);
                    }
                    List<LayuiTree2> list3 = new ArrayList<>();
                    if (trs.size() > 0) {
                        for (UserDepartment tc : tcs) {
                            LayuiTree2 rac = new LayuiTree2();
                            rac.setId(tc.getId());
                            rac.setTitle(tc.getName());
                            rac.setSpread(true);
                            rac.setParentId(t.getId());
                            rac.setDepartmentCode(t.getDepartmentCode());
                            if (map.containsKey(tc.getId())) {
                                rac.setDisabled(false);
                            } else {
                                rac.setDisabled(true);
                            }
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
        } catch (Exception e) {
            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody UserDepartment datas) {
        if (null != datas.getId()) {
            if (UserDepartmentService.updateById(datas)) {
                return new Result(200, "保存成功");
            }
        } else {
            datas.setId(CommunalUtils.getUUID());
            if (UserDepartmentService.save(datas)) {
                return new Result(200, "保存成功");
            }
        }

        return new Result(500, "保存失败");


    }

    @RequestMapping(value = "/deleteByID")
    public Result deleteByID(String  id) {

            if (UserDepartmentService.removeById(id)) {
                return new Result(200, "保存成功");
            }

        return new Result(500, "保存失败");


    }
}
