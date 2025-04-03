package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.service.DfAoiSeatProtectService;
import com.ww.boengongye.service.DfFactoryService;
import com.ww.boengongye.service.LineBodyService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工位维护 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-04
 */
@Controller
@RequestMapping("/dfAoiSeatProtect")
@ResponseBody
@CrossOrigin
@Api(tags = "工位维护")
public class DfAoiSeatProtectController {

    private static final Logger logger = LoggerFactory.getLogger(DfAoiMachineProtectController.class);

    @Autowired
    DfAoiSeatProtectService dfAoiSeatProtectService;

    @Autowired
    UserService userService;

    @Autowired
    DfFactoryService dfFactoryService;

    @Autowired
    LineBodyService lineBodyService;


    /**
     * 关键字查询
     *
     * @param page
     * @param limit
     * @param keywords
     * @param factoryId
     * @param lineBobyId
     * @param userName
     * @param classCategory
     * @return
     */
    @RequestMapping(value = "/listBySearch")
    @ApiOperation("获取工位维护列表")
    public Result listBySearch(Integer page, Integer limit, String keywords,
                               String factoryId, String lineBobyId, String userName, String classCategory) {
        Page<DfAoiSeatProtect> pages = new Page<>(page, limit);

        QueryWrapper<DfAoiSeatProtect> ew = new QueryWrapper<>();
        if (factoryId != null && !"".equals(factoryId)) {
            ew.eq("dasp.factory_id", factoryId);
        }

        if (lineBobyId != null && !"".equals(lineBobyId)) {
            ew.eq("dasp.line_boby_id", lineBobyId);
        }

        if (userName != null && !"".equals(userName)) {
            ew.eq("u.alias", userName);
        }

        if (classCategory != null && !"".equals(classCategory)) {
            ew.eq("dasp.classCategory", classCategory);
        }

        if (keywords != null && !"".equals(keywords)) {
            ew.and(wrapper -> wrapper
                    .like("u.name", keywords)
                    .or().like("u.alias", keywords)
                    .or().like("df.factory_name", keywords)
                    .or().like("lb.name", keywords)
                    .or().like("dasp.classCategory", keywords));
        }
        ew.orderByAsc("dasp.seat");
        IPage<DfAoiSeatProtect> list = dfAoiSeatProtectService.listJoinPage(pages, ew);
        return new Result(200, "查询成功", list.getRecords(), (int) list.getTotal());
    }


    /**
     * 添加或者修改
     *
     * @param datas
     * @return
     */
    @ApiOperation("添加或者修改工位维护数据")
    @RequestMapping(value = "/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DfAoiSeatProtect datas) {
        if (datas.getId() != null) {
            if (dfAoiSeatProtectService.updateById(datas)) {
                return new Result(200, "修改成功");
            }
            return new Result(500, "修改失败");
        } else {
            if (dfAoiSeatProtectService.save(datas)) {
                return new Result(200, "添加成功");
            }
            return new Result(500, "添加失败");
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation("删除工位维护数据")
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (dfAoiSeatProtectService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }


    /**
     * 通过员工名称查询员工信息
     *
     * @param userName
     * @return
     */
    @ApiOperation("通过员工名称查询员工信息")
    @RequestMapping(value = "/getUserByUserName")
    public Result getUserByUserName(String userName) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("alias", userName);
        User user = userService.getOne(ew);
        if (user.getId() != null && !"".equals(user.getId())) {
            return new Result(200, "获取员工信息成功", user);
        }
        return new Result(500, "获取员工信息失败");
    }

    /**
     * 通过员工工号查询员工信息
     *
     * @param userCode
     * @return
     */
    @ApiOperation("通过员工工号查询员工信息")
    @RequestMapping(value = "/getUserByUserCode")
    public Result getUserByUserCode(String userCode) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("name", userCode);
        User user = userService.getOne(ew);
        if (user.getId() != null && !"".equals(user.getId())) {
            return new Result(200, "获取员工信息成功", user);
        }
        return new Result(500, "获取员工信息失败");
    }

    /**
     * 通过线体id和线体工位获取线体信息
     *
     * @param lineBobyId
     * @param seat
     * @return
     */
    @ApiOperation("通过线体id和线体工位获取线体信息")
    @RequestMapping(value = "/getLineBobyLBIdAndSeat")
    public Result getLineBobyByNameAndSeat(String lineBobyId, String seat) {
        QueryWrapper<DfAoiSeatProtect> ew = new QueryWrapper<>();
        ew.eq("line_boby_id", lineBobyId);
        ew.eq("seat", seat);
        DfAoiSeatProtect dfAoiSeatProtect = dfAoiSeatProtectService.getOne(ew);
        if (dfAoiSeatProtect != null && dfAoiSeatProtect.getId() != null) {
            return new Result(200, "获取线体信息成功", dfAoiSeatProtect);
        }
        return new Result(500, "获取线体信息失败");
    }


    /**
     * 通过工号获取工位维护信息
     * @param userCode
     * @return
     */
    @ApiOperation("通过工号获取工位维护信息")
    @RequestMapping(value = "/getSeatProtectByUserCode")
    public Result getSeatProtectByUserCode(String userCode){
        QueryWrapper<DfAoiSeatProtect> ew = new QueryWrapper<>();
        ew.eq("u.name",userCode);
        DfAoiSeatProtect dfAoiSeatProtect = dfAoiSeatProtectService.getSeatProtectByUserCode(ew);
        if (dfAoiSeatProtect!=null&&dfAoiSeatProtect.getId()!=null){
            return new Result(200,"通过工号获取工位维护信息成功",dfAoiSeatProtect);
        }

        return new Result(500,"工位维护信息获取失败");
    }


    /**
     * 导出
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("导出")
    @RequestMapping(value = "downloadExcel",method = RequestMethod.GET)
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<DfAoiSeatProtect> datas = dfAoiSeatProtectService.getAllList();

        List<Map> maps = new ArrayList<>();

        for (DfAoiSeatProtect dfAoiSeatProtect : datas) {
            try {
                maps.add(Excel.objectToMap(dfAoiSeatProtect));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (maps != null && maps.size() > 0) {
//            String companyName = "工位维护界面表";
//            String sheetTitle = companyName;
            String[] title = new String[]{"员工工号", "员工姓名", "工厂", "线体", "工位", "班别", "工序",
                    "考试等级", "入职日期", "劳动关系", "最新修改日期", "修改人"};        //设置表格表头字段

            String[] properties = new String[]{"userCode", "userName", "factoryName", "lineBobyName",
                    "seat", "classCategory", "userProcess", "userGrade", "userCreateTime", "userLaborRelation", "updateTime", "updateName"};  // 查询对应的字段
            ExcelExportUtil2 excelExport2 = new ExcelExportUtil2();
            excelExport2.setData(maps);
            excelExport2.setHeardKey(properties);
            excelExport2.setFontSize(14);
//            excelExport2.setSheetName(sheetTitle);
//            excelExport2.setTitle(sheetTitle);
            excelExport2.setHeardList(title);
            excelExport2.exportExport(request, response);
        }
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation("导入")
    @RequestMapping(value = "upload")
    public Result upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取工位维护信息失败");
        }
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        List<DfAoiSeatProtect> list = new ArrayList<>();

        for (Map<String, String> map : maps) {
            DfAoiSeatProtect dfAoiSeatProtect = new DfAoiSeatProtect();
            dfAoiSeatProtect.setUserCode(map.get("员工工号"));
            //获取员工id
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name",dfAoiSeatProtect.getUserCode());
            User user = userService.getOne(userQueryWrapper);
            dfAoiSeatProtect.setUserId(user.getId());

            dfAoiSeatProtect.setUserName(map.get("员工姓名"));

            dfAoiSeatProtect.setFactoryName(map.get("工厂"));
            //获取工厂id
            QueryWrapper<DfFactory> dfFactoryQueryWrapper = new QueryWrapper<>();
            dfFactoryQueryWrapper.eq("factory_name",dfAoiSeatProtect.getFactoryName());
            DfFactory dfFactory = dfFactoryService.getOne(dfFactoryQueryWrapper);
            dfAoiSeatProtect.setFactoryId(dfFactory.getId());

            dfAoiSeatProtect.setLineBobyName(map.get("线体"));
            //获取线体id
            QueryWrapper<LineBody> lineBodyQueryWrapper = new QueryWrapper<>();
            lineBodyQueryWrapper.eq("name",dfAoiSeatProtect.getLineBobyName());
            LineBody lineBody = lineBodyService.getOne(lineBodyQueryWrapper);
            dfAoiSeatProtect.setLineBobyId(lineBody.getId());


            dfAoiSeatProtect.setSeat(Integer.parseInt(map.get("工位")));
            dfAoiSeatProtect.setClassCategory(map.get("班别"));
            dfAoiSeatProtect.setUserProcess(map.get("工序"));
            dfAoiSeatProtect.setUserGrade(map.get("考试等级"));
            dfAoiSeatProtect.setUserCreateTime(map.get("入职日期"));
            dfAoiSeatProtect.setUserLaborRelation(map.get("劳动关系"));
            dfAoiSeatProtect.setUpdateTime(Timestamp.valueOf(map.get("最新修改日期")));
            dfAoiSeatProtect.setUpdateName(map.get("修改人"));
            list.add(dfAoiSeatProtect);
        }
        return new Result(200, "导入工位维护信息成功", list);
    }


    @RequestMapping(value = "/saveList")
    @ApiOperation("新增保存")
    public Result saveList(@RequestBody List<DfAoiSeatProtect> list){
        if (list==null||list.size()==0){
            return new  Result(500,"保存的工位维护信息不能为空");
        }
        for (DfAoiSeatProtect dfAoiSeatProtect:list){
            //判断该工位维护数据是否存在
            QueryWrapper<DfAoiSeatProtect> dfAoiSeatProtectQueryWrapper = new QueryWrapper<>();
            dfAoiSeatProtectQueryWrapper.eq("user_id",dfAoiSeatProtect.getUserId());
            DfAoiSeatProtect dfAoiSeatProtectOld = dfAoiSeatProtectService.getOne(dfAoiSeatProtectQueryWrapper);
            if (dfAoiSeatProtectOld==null||dfAoiSeatProtectOld.getId()==null){

                if (!dfAoiSeatProtectService.save(dfAoiSeatProtect)){
                    return new Result(500,"用户"+dfAoiSeatProtect.getUserName()+"的工位数据添加失败");
                }
            }else {
                dfAoiSeatProtect.setId(dfAoiSeatProtectOld.getId());
                if (!dfAoiSeatProtectService.updateById(dfAoiSeatProtect)){
                    return new Result(500,"用户"+dfAoiSeatProtect.getUserName()+"的工位数据修改失败");
                }
            }
        }
        return new Result(200, "保存工位维护信息成功");
    }
}
