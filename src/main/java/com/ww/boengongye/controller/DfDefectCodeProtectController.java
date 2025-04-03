package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.ww.boengongye.entity.DfProject;
import com.ww.boengongye.service.DfDefectCodeProtectService;
import com.ww.boengongye.service.DfProjectService;
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
 * 不良代码维护 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Controller
@RequestMapping("/dfDefectCodeProtect")
@ResponseBody
@CrossOrigin
@Api(tags = "不良代码维护")
public class DfDefectCodeProtectController {
    private static final Logger logger = LoggerFactory.getLogger(DfDefectCodeProtectController.class);

    @Autowired
    DfDefectCodeProtectService dfDefectCodeProtectService;

    @Autowired
    DfProjectService dfProjectService;

    /**
     * 关键字查询E-Code
     * @param page
     * @param limit
     * @param keywords
     * @return
     */
    @ApiOperation("获取不良代码配置列表")
    @RequestMapping(value = "/listBySearch",method = RequestMethod.GET)
    public Result listBySearch(Integer page, Integer limit, String keywords,
                               Integer projectId,String defectArea){
        Page<DfDefectCodeProtect> pages = new Page<>(page,limit);

        QueryWrapper<DfDefectCodeProtect> ew = new QueryWrapper<>();

        if (projectId!=null&&!"".equals(projectId)){
            ew.eq("ddcp.project_id",projectId);
        }

        if (defectArea!=null&&!"".equals(defectArea)){
            ew.eq("ddcp.defect_area",defectArea);
        }

        if (keywords!=null&&!"".equals(keywords)){
            ew.and(wrapper -> wrapper
                    .like("dp.name",keywords)
                    .or().like("ddcp.defect_area",keywords)
                    .or().like("ddcp.defect_area_data",keywords)
                    .or().like("ddcp.defect_name",keywords)
                    .or().like("ddcp.defect_name_english",keywords));
        }

        IPage<DfDefectCodeProtect> list = dfDefectCodeProtectService.listJoinPage(pages,ew);
        return new Result(0,"查询成功",list.getRecords(),(int)list.getTotal());
    }

    /**
     * 添加或者修改
     *
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation("添加或者修改不良代码配置数据")
    public Result saveOrUpdate(@RequestBody DfDefectCodeProtect datas) {
        if (datas.getId() != null) {
            if (dfDefectCodeProtectService.updateById(datas)) {
                return new Result(200, "修改成功");
            }
            return new Result(500, "修改失败");
        } else {
            if (dfDefectCodeProtectService.save(datas)) {
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
    @ApiOperation("删除不良代码配置数据")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(String id) {
        if (dfDefectCodeProtectService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");
    }



    /**
     * 导出
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "downloadExcel",method = RequestMethod.GET)
    @ApiOperation("导出")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<DfDefectCodeProtect> datas = dfDefectCodeProtectService.getAllList();

        List<Map> maps = new ArrayList<>();

        for (DfDefectCodeProtect dfDefectCodeProtect : datas) {
            try {
                maps.add(Excel.objectToMap(dfDefectCodeProtect));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (maps != null && maps.size() > 0) {
//            String companyName = "不良代码维护表";
//            String sheetTitle = companyName;

            //设置表格表头字段
            String[] title = new String[]{"项目", "缺陷区域", "缺陷区域代码", "不良名称", "不良名称英文",
                    "创建时间", "创建人", "最新修改时间", "修改人"};

            // 查询对应的字段
            String[] properties = new String[]{"projectName", "defectArea", "defectAreaData", "defectName",
                    "defectNameEnglish", "createTime", "createName", "updateTime", "updateName"};
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
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ApiOperation("导入")
    public Result upload(MultipartFile file,String userName) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Result(500, "获取工位维护信息失败");
        }
        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent(1, 1);

        List<DfDefectCodeProtect> list = new ArrayList<>();

        for (Map<String, String> map : maps) {
            DfDefectCodeProtect dfDefectCodeProtect = new DfDefectCodeProtect();
            dfDefectCodeProtect.setProjectName(map.get("项目"));

            //获取项目id
            QueryWrapper<DfProject> ew = new QueryWrapper<>();
            ew.eq("name",dfDefectCodeProtect.getProjectName());
            DfProject dfProject = dfProjectService.getOne(ew);
            if (dfProject.getId()==null||dfProject.getId()==0){
                return new Result(500,"导入的项目不存在");
            }
            dfDefectCodeProtect.setProjectId(dfProject.getId());

            dfDefectCodeProtect.setDefectArea(map.get("缺陷区域"));
            dfDefectCodeProtect.setDefectAreaData(map.get("缺陷区域代码"));
            dfDefectCodeProtect.setDefectName(map.get("不良名称"));
            dfDefectCodeProtect.setDefectNameEnglish(map.get("不良名称英文"));
            dfDefectCodeProtect.setUpdateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
            dfDefectCodeProtect.setUpdateName(userName);

            //判断该不良代码配置是否存在
            QueryWrapper<DfDefectCodeProtect> dfDefectCodeProtectQueryWrapper = new QueryWrapper<>();
            dfDefectCodeProtectQueryWrapper
                    .eq("project_id",dfDefectCodeProtect.getProjectId())
                    .eq("defect_area",dfDefectCodeProtect.getDefectArea())
                    .eq("defect_area_data",dfDefectCodeProtect.getDefectAreaData())
                    .eq("defect_name",dfDefectCodeProtect.getDefectName())
                    .last("limit 1");
            DfDefectCodeProtect dfDefectCodeProtectOld= dfDefectCodeProtectService.getOne(dfDefectCodeProtectQueryWrapper);
            if (dfDefectCodeProtectOld==null||dfDefectCodeProtectOld.getId()==null){
                dfDefectCodeProtect.setCreateTime(Timestamp.valueOf(TimeUtil.getNowTimeByNormal()));
                dfDefectCodeProtect.setCreateName(userName);
                if (!dfDefectCodeProtectService.save(dfDefectCodeProtect)){
                    return new Result(500,"添加失败");
                }
            }else {
                dfDefectCodeProtect.setId(dfDefectCodeProtectOld.getId());
                if (!dfDefectCodeProtectService.updateById(dfDefectCodeProtect)){
                    return new Result(500,"修改失败");
                }

            }
        }
        return new Result(200, "导入工位维护信息成功");
    }
}
