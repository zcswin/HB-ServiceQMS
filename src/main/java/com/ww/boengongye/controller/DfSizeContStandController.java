package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfSizeContStand;
import com.ww.boengongye.entity.DfSizeContStandSaveedit;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.service.DfSizeContStandSaveeditService;
import com.ww.boengongye.service.DfSizeContStandService;
import com.ww.boengongye.timer.InitializeCheckRule;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 尺寸管控标准 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
@Controller
@RequestMapping("/dfSizeContStand")
@Api(tags = "尺寸管控标准")
@CrossOrigin
@ResponseBody
public class DfSizeContStandController {
    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardConfigController.class);

    @Autowired
    private Environment env;
    @Autowired
    private DfSizeContStandService dfSizeContStandService;
    @Autowired
    private DfSizeContStandSaveeditService dfSizeContStandSaveeditService;

    @PostMapping("/upload2")
    public Result upload2(MultipartFile file) throws Exception {
        int count = dfSizeContStandService.importExcel2(file);
        return new Result(200, "成功添加" + count + "条数据");
    }

    @RequestMapping(value = "/getById")
    public Object getById(int id) {

        return new Result(0, "查询成功", dfSizeContStandService.getById(id));
    }


    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",dfSizeContStandService.list());
    }

    @RequestMapping(value = "/updateSystemConfig")
    public Object updateSystemConfig() {
        InitializeCheckRule.sizeContStand=new HashMap<>();
        QueryWrapper<DfSizeContStand> scqw=new QueryWrapper<>();
        scqw.eq("is_use",1);
        List<DfSizeContStand> sizeCont=dfSizeContStandService.list(scqw);
        if(sizeCont.size()>0){
            for(DfSizeContStand d:sizeCont){
                InitializeCheckRule.sizeContStand.put(d.getProject()+d.getColor()+d.getProcess()+d.getTestItem(),d);
					System.out.println(d.getTestItem());
            }
        }
        return new Result(0, "查询成功");
    }


    @Transactional
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfSizeContStand datas) {

        if (null != datas.getId()) {
            DfSizeContStand oldData=dfSizeContStandService.getById(datas.getId());
            if (dfSizeContStandService.updateById(datas)) {
                if(datas.getIsUse()==1){
                    InitializeCheckRule.sizeContStand.put(datas.getProcess()+datas.getTestItem(),datas);
                    if(null!=oldData&&(!oldData.getTestItem().equals(datas.getTestItem())||!oldData.getProcess().equals(datas.getProcess()))){
                        InitializeCheckRule.sizeContStand.remove(oldData.getProcess()+oldData.getTestItem());
                    }
                }else {
                    InitializeCheckRule.sizeContStand.remove(datas.getProcess()+datas.getTestItem());
                }
                //保存修改到df_size_cont_stand_saveedit
                DfSizeContStandSaveedit saveedit = new DfSizeContStandSaveedit();
                BeanUtils.copyProperties(datas, saveedit);
                if (dfSizeContStandSaveeditService.save(saveedit)){
                    return new Result(200, "保存成功");
                }else {
                    return new Result(500, "保存失败");
                }

            } else {
                return new Result(500, "保存失败");
            }
        } else {

            if (dfSizeContStandService.save(datas)) {
                if(datas.getIsUse()==1) {
                    InitializeCheckRule.sizeContStand.put(datas.getProcess() + datas.getTestItem(), datas);
                }
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }

    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        DfSizeContStand data=dfSizeContStandService.getById(id);
        if (dfSizeContStandService.removeById(id)) {
            if(null!=data){
                InitializeCheckRule.sizeContStand.remove(data.getProcess()+data.getTestItem());
            }

            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String testItem,String process,Double standard) {
        Page<DfSizeContStand> pages = new Page<DfSizeContStand>(page, limit);
        QueryWrapper<DfSizeContStand> ew=new QueryWrapper<DfSizeContStand>();
        if(null!=testItem&&!testItem.equals("")) {
            ew.like("test_item", testItem);
        }

        if(null!=process&&!process.equals("")) {
            ew.eq("process", process);
        }

        if(null!=standard&&!standard.equals("")) {
            ew.eq("standard", standard);
        }


        ew.orderByDesc("create_time");
        IPage<DfSizeContStand> list=dfSizeContStandService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }
    @ApiOperation("导入尺寸管控项")
    @PostMapping(value = "/uploadExcel")
    @ResponseBody
    public Object uploadExcel( @RequestParam(value = "file", required = false) MultipartFile file,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
//		String id2 = request.getParameter("id");// 获取data中数据
//		System.out.println(id);
            if (file != null) {
                // 获取文件名
                String fileName = file.getOriginalFilename();
                map.put("code", 0);
                System.out.println(fileName);
            } else {
                map.put("code", 1);
            }

            if (file.isEmpty()) {
                return new Result(0, "上传失败，请选择文件");
            }

//	        System.out.println(id);
//	        ImgManager img = new ImgManager();
//	        img.setArticleId(id);
            System.out.println("开始上传");
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);

            if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
                return new Result(1, "请上传xlsx或xls格式的文件");
            }
            InputStream ins = null;
            try {
                ins = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File fl = new File(env.getProperty("uploadPath") + "excel/");
            if (!fl.exists()) {
                fl.mkdirs();
            }
            File f = new File(env.getProperty("uploadPath") + "excel/" + file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                System.out.println(i);
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = new File(env.getProperty("uploadPath") + "excel/" + fileName);
                i++;
            }

            CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "excel",
                    env.getProperty("uploadPath") + "excel/");

            try {
                ImportExcelResult ter = dfSizeContStandService.importOrder( file);
                return new Result(0, "上传成功", ter);
            } catch (Exception e) {

                e.printStackTrace();
            }

            return new Result(500, "上传失败");

        } catch (Exception e) {
            logger.error("导入excel接口异常", e);
        }
        return new Result(500, "接口异常");
    }

    /**
     * 分页查询所有修改记录
     * @param page
     * @param limit
     * @param testItem 测试项
     * @param process 工序
     * @param standard 标准值
     * @return
     */
    @PostMapping(value = "/listAllEdit")
    @ApiOperation("IQPC尺寸-尺寸管控标准 分页查询所有修改记录")
    public Result listAllEdit(int page , int limit , String testItem , String process , Double standard){
        Page<DfSizeContStandSaveedit> pages = new Page<DfSizeContStandSaveedit>(page,limit);
        QueryWrapper<DfSizeContStandSaveedit> ew = new QueryWrapper<>();
        if(null!=testItem&&!testItem.equals("")) {
            ew.like("test_item", testItem);
        }

        if(null!=process&&!process.equals("")) {
            ew.eq("process", process);
        }

        if(null!=standard&&!standard.equals("")) {
            ew.eq("standard", standard);
        }

        ew.orderByDesc("create_time");
        IPage<DfSizeContStandSaveedit> list=dfSizeContStandSaveeditService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
    }


}
