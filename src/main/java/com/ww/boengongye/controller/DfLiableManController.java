package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAuditDetailService;
import com.ww.boengongye.service.DfLiableManService;
import com.ww.boengongye.utils.ExcelImportUtil;
import com.ww.boengongye.utils.ExportExcelUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 责任人 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
@Controller
@RequestMapping("/dfLiableMan")
@ResponseBody
@CrossOrigin
public class DfLiableManController {
    @Autowired
    private DfLiableManService dfLiableManService;
    @Autowired
    private DfAuditDetailService dfAuditDetailService;
    @GetMapping("/listAll")
    public Result listAll(int page, int limit,
                          String factoryName,
                          String sectionName,
                          String stationName,
                          String processName) {
        Page<DfLiableMan> pages = new Page<>(page, limit);
        LambdaQueryWrapper<DfLiableMan> qw = new LambdaQueryWrapper<>();
        if (factoryName != null && !"".equals(factoryName)) qw.eq(DfLiableMan::getFactoryName, factoryName);
        if (sectionName != null && !"".equals(sectionName)) qw.eq(DfLiableMan::getSectionName, sectionName);
        if (stationName != null && !"".equals(stationName)) qw.eq(DfLiableMan::getStationName, stationName);
        if (processName != null && !"".equals(processName)) qw.eq(DfLiableMan::getProcessName, processName);
        IPage<DfLiableMan> page1 = dfLiableManService.page(pages, qw);
        return new Result(200, "查询成功", page1);
    }


    @ApiOperation("根据员工账和类型获取办理等级,类型传(稽查/尺寸/外观)")
    @GetMapping("/getUserLevel")
    public Result getUserLevel(
                          String userCode,String type) {
        QueryWrapper<DfLiableMan>qw=new QueryWrapper<>();
        qw.eq("liable_man_code",userCode);
        String searchType="check";
        if(type.equals("稽查")||type.equals("外观") ){
            searchType="check";
        }else if(type.equals("尺寸")){
            searchType="size";
        }

        qw.eq("type",searchType);
        List<DfLiableMan>data= dfLiableManService.list(qw);
        String level="";
        int i=0;
        for(DfLiableMan d:data){
            if(i>0){
                level=level+",";
            }
            level=level+d.getProblemLevel();
        }
        return new Result(200, "查询成功", level);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody DfLiableMan dfLiableMan) {
        boolean b = dfLiableManService.save(dfLiableMan);
        if (b) {
            return Result.INSERT_SUCCESS;
        } else {
            return Result.INSERT_FAILED;
        }
    }

    @GetMapping("/delete")
    public Result delete(Integer[] ids) {
        List<Integer> list = Arrays.asList(ids);
        boolean b = dfLiableManService.removeByIds(list);
        if (b) {
            return Result.DELETE_SUCCESS;
        } else {
            return Result.DELETE_FAILED;
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody DfLiableMan dfLiableMan) {
        boolean b = dfLiableManService.updateById(dfLiableMan);
        if (b) {
            return Result.UPDATE_SUCCESS;
        } else {
            return Result.UPDATE_FAILED;
        }
    }

    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.downLoadExcelMould(response, "责任人模板");
    }

    @PostMapping("/importExcel")
    public Result importExcel(MultipartFile file, String factoryName, String processName) throws Exception {
        int count = dfLiableManService.importExcel(file, factoryName, processName);
        return new Result(200, "成功添加" + count + "条数据");
    }


    //获取树状菜单
    @RequestMapping(value = "/getLevelTree")
    public Object getTree2() {
        try {
            Map<String,String> map=new HashMap<>();


            List<LayuiTree3> list = new ArrayList<>();


            QueryWrapper<DfLiableMan> ew = new QueryWrapper<DfLiableMan>();
            ew.eq("problem_level", "1");
            LayuiTree3 ra = new LayuiTree3();
            ra.setId("1");
            ra.setTitle("等级一");
            ra.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs = dfLiableManService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());

                    list2.add(raf);

                }
                ra.setChildren(list2);
            }
            list.add(ra);


            QueryWrapper<DfLiableMan> ew2 = new QueryWrapper<DfLiableMan>();
            ew2.eq("problem_level", "2");
            LayuiTree3 ra2 = new LayuiTree3();
            ra2.setId("2");
            ra2.setTitle("等级二");
            ra2.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs2 = dfLiableManService.list(ew2);
            if (trs2.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs2) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());

                    list2.add(raf);

                }
                ra2.setChildren(list2);
            }
            list.add(ra2);


            QueryWrapper<DfLiableMan> ew3 = new QueryWrapper<DfLiableMan>();
            ew3.eq("problem_level", "3");
            LayuiTree3 ra3 = new LayuiTree3();
            ra3.setId("3");
            ra3.setTitle("等级三");
            ra3.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs3 = dfLiableManService.list(ew3);
            if (trs3.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs3) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());

                    list2.add(raf);

                }
                ra3.setChildren(list2);
            }
            list.add(ra3);

            return list;
//		return new Result(200, "查询成功", list);
        }catch(Exception e) {
//            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }


    //获取树状菜单
    @RequestMapping(value = "/getLevelTreeWg")
    public Object getLevelTreeWg() {
        try {
            Map<String,String> map=new HashMap<>();


            List<LayuiTree3> list = new ArrayList<>();


            QueryWrapper<DfLiableMan> ew = new QueryWrapper<DfLiableMan>();
            ew.eq("problem_level", "1");
            LayuiTree3 ra = new LayuiTree3();
            ra.setId("1");
            ra.setTitle("等级一");
            ra.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs = dfLiableManService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());
                    if(t.getLiableManName().equals("李海涛")){
                        raf.setChecked(true);
                    }
                    list2.add(raf);

                }
                ra.setChildren(list2);
            }
            list.add(ra);


            QueryWrapper<DfLiableMan> ew2 = new QueryWrapper<DfLiableMan>();
            ew2.eq("problem_level", "2");
            LayuiTree3 ra2 = new LayuiTree3();
            ra2.setId("2");
            ra2.setTitle("等级二");
            ra2.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs2 = dfLiableManService.list(ew2);
            if (trs2.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs2) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());
                    if(t.getLiableManName().equals("彭清文")){
                        raf.setChecked(true);
                    }
                    list2.add(raf);

                }
                ra2.setChildren(list2);
            }
            list.add(ra2);


            QueryWrapper<DfLiableMan> ew3 = new QueryWrapper<DfLiableMan>();
            ew3.eq("problem_level", "3");
            LayuiTree3 ra3 = new LayuiTree3();
            ra3.setId("3");
            ra3.setTitle("等级三");
            ra3.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs3 = dfLiableManService.list(ew3);
            if (trs3.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                for (DfLiableMan t : trs3) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());

                    list2.add(raf);

                }
                ra3.setChildren(list2);
            }
            list.add(ra3);

            return list;
//		return new Result(200, "查询成功", list);
        }catch(Exception e) {
//            logger.error("获取树状接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    @GetMapping("/listAllLiableMan")
    public Result listAllLiableMan() {
        return new Result(200, "查询成功", dfLiableManService.list());
    }

    @ApiOperation("查询该工序办理人员")
    @GetMapping("/listByProcess")
    public Result listByProcess(String process, String type) {
        QueryWrapper<DfLiableMan> qw = new QueryWrapper<>();
        qw.like(null != process, "process_name", process);
        if (!type.equals("all") && !type.equals("ALL")) {
            qw.like("type", type);
        }
        List<DfLiableMan> list = dfLiableManService.list(qw);
        List<DfLiableMan> level1 = new ArrayList<>();
        List<DfLiableMan> level2 = new ArrayList<>();
        List<DfLiableMan> level3 = new ArrayList<>();
        for (DfLiableMan dfLiableMan : list) {
            switch (dfLiableMan.getProblemLevel()) {
                case "1": level1.add(dfLiableMan); break;
                case "2": level2.add(dfLiableMan); break;
                case "3": level3.add(dfLiableMan); break;
                default: return new Result(202, "等级数据有异常");
            }
        }
        Map<String, List<DfLiableMan>> result = new HashMap<>();
        result.put("等级一办理人员", level1);
        result.put("等级二办理人员", level2);
        result.put("等级三办理人员", level3);
        return new Result(200, "查询到办理人", result);
    }



    //获取树状菜单新流程
    @RequestMapping(value = "/getLevelTreeProcess")
    public Object getLevelTreeProcess(String process, String type) {
//        try {
            Map<String,String> map=new HashMap<>();


            List<LayuiTree3> list = new ArrayList<>();


            QueryWrapper<DfLiableMan> ew = new QueryWrapper<DfLiableMan>();
            ew.eq("problem_level", "1");
            ew.like("process_name",process);
            if (!type.equals("all") && !type.equals("ALL")) {
                ew.like("type", type);
            }
            LayuiTree3 ra = new LayuiTree3();
            ra.setId("1");
            ra.setTitle("等级一");
            ra.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs = dfLiableManService.list(ew);
            if (trs.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
                int i=0;
                for (DfLiableMan t : trs) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());
//                    if(i==0){
//                        raf.setChecked(true);
//                    }
//                    i++;
                    list2.add(raf);

                }
                ra.setChildren(list2);
            }
            list.add(ra);


            QueryWrapper<DfLiableMan> ew2 = new QueryWrapper<DfLiableMan>();
            ew2.eq("problem_level", "2");
            ew2.like("process_name",process);
            if (!type.equals("all") && !type.equals("ALL")) {
                ew2.like("type", type);
            }
            LayuiTree3 ra2 = new LayuiTree3();
            ra2.setId("2");
            ra2.setTitle("等级二");
            ra2.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs2 = dfLiableManService.list(ew2);
            if (trs2.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
//                int i=0;
                for (DfLiableMan t : trs2) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());
//                    if(i==0){
//                        raf.setChecked(true);
//                    }
//                    i++;
                    list2.add(raf);

                }
                ra2.setChildren(list2);
            }
            list.add(ra2);


            QueryWrapper<DfLiableMan> ew3 = new QueryWrapper<DfLiableMan>();
            ew3.eq("problem_level", "3");
            ew3.like("process_name",process);
            if (!type.equals("all") && !type.equals("ALL")) {
                ew3.like("type", type);
            }
            LayuiTree3 ra3 = new LayuiTree3();
            ra3.setId("3");
            ra3.setTitle("等级三");
            ra3.setSpread(true);
//            ra.setDepartmentCode("ROOT");
            List<DfLiableMan> trs3 = dfLiableManService.list(ew3);
            if (trs3.size() > 0) {
                List<LayuiTree3> list2 = new ArrayList<>();
//                int i=0;
                for (DfLiableMan t : trs3) {

                    LayuiTree3 raf = new LayuiTree3();
                    raf.setId(t.getId()+"");
                    raf.setTitle(t.getLiableManName()+"-"+t.getLiableManCode()+"-"+t.getDayOrNight());
                    raf.setSpread(true);
                    raf.setParentId(t.getProblemLevel());
                    raf.setUserCode(t.getLiableManCode());
                    raf.setUserName(t.getLiableManName());
//                    if(i==0){
//                        raf.setChecked(true);
//                    }
//                    i++;
                    list2.add(raf);

                }
                ra3.setChildren(list2);
            }
            list.add(ra3);

            return list;
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfLiableMan datas) {

        if (null != datas.getId()) {
            if (dfLiableManService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (dfLiableManService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/deleteById")
    public Result delete(String id) {
        if (dfLiableManService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String liableManCode,String liableManName,String process ,String type ,String problemLevel ,String bimonthly,String dayOrNight) {
        Page<DfLiableMan> pages = new Page<DfLiableMan>(page, limit);
        QueryWrapper<DfLiableMan> ew=new QueryWrapper<DfLiableMan>();
        if(null!=liableManCode&&!liableManCode.equals("")) {
            ew.like("liable_man_code", liableManCode);
        }
        if(null!=liableManName&&!liableManName.equals("")) {
            ew.like("liable_man_name", liableManName);
        }

        if(null!=process&&!process.equals("")) {
            ew.like("process_name", process);
        }

        if(null!=type&&!type.equals("")) {
            ew.like("type", type);
        }

        if(null!=problemLevel&&!problemLevel.equals("")) {
            ew.like("problem_level", problemLevel);
        }
        if(null!=bimonthly&&!bimonthly.equals("")) {
            ew.like("bimonthly", bimonthly);
        }

        if(null!=dayOrNight&&!dayOrNight.equals("")) {
            ew.like("day_or_night", dayOrNight);
        }

        ew.orderByDesc("create_time");
        IPage<DfLiableMan> list=dfLiableManService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }


}
