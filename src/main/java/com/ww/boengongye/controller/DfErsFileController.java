package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfErsFile;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ERS文件 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Controller
@RequestMapping("/dfErsFile")
@ResponseBody
@CrossOrigin
public class DfErsFileController {
    @Autowired
    com.ww.boengongye.service.DfErsFileService DfErsFileService;

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(DfErsFileController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfErsFileService.list());
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfErsFile datas) {

        if (null != datas.getId()) {
            if (DfErsFileService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfErsFileService.save(datas)) {
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
        if (DfErsFileService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String ersFileName,String factoryCode,String version,String updateDescription) {
//        try {
        Page<DfErsFile> pages = new Page<DfErsFile>(page, limit);
        QueryWrapper<DfErsFile> ew=new QueryWrapper<DfErsFile>();
        if(null!=ersFileName&&!ersFileName.equals("")) {
            ew.like("ers_file_name", ersFileName);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("factory_code", factoryCode);
        }
        if(null!=version&&!version.equals("")) {
            ew.like("version", version);
        }
        if(null!=updateDescription&&!updateDescription.equals("")) {
            ew.like("update_description", updateDescription);
        }
        ew.orderByDesc("create_time");
        IPage<DfErsFile> list=DfErsFileService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
    @ResponseBody
    public Object uploadExcel(@RequestParam(value = "id", required = false) int routingId, @RequestParam(value = "file", required = false) MultipartFile file,
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
                ImportExcelResult ter = DfErsFileService.importOrder(env.getProperty("uploadPath") + "excel/" + fileName, file);
                return new Result(0, "上传成功", ter);
            } catch (Exception e) {

                e.printStackTrace();
            }

            return new Result(500, "上传失败");

        } catch (Exception e) {
            logger.error("课程导入excel接口异常", e);
        }
        return new Result(500, "接口异常");
    }
}
