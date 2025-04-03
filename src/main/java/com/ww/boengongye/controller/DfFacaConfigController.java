package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfFacaConfig;
import com.ww.boengongye.entity.DfRework;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import com.ww.boengongye.utils.TimeUtil;
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
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Controller
@RequestMapping("/dfFacaConfig")
@ResponseBody
@CrossOrigin
public class DfFacaConfigController {
    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardConfigController.class);

    @Autowired
    private Environment env;

    @Autowired
    com.ww.boengongye.service.DfFacaConfigService DfFacaConfigService;

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功",DfFacaConfigService.list());
    }

    /**
     *
     * @param type FA/CA
     * @param process  工序
     * @param receiptType 审批单类型
     * @param questionType 稽查的问题NG标准类型
     * @return
     */
    @RequestMapping(value = "/listByPDA")
    public Object listByPDA(String type,String process,String receiptType,String questionType) {
        QueryWrapper<DfFacaConfig>qw=new QueryWrapper<>();
        if(null!=type&&!type.equals("")){
            qw.eq("type",type);
        }
        if(null!=process&&!process.equals("")){
            qw.eq("process",process);
        }

        if(null!=receiptType&&!receiptType.equals("")){
            qw.eq("receipt_type",receiptType);
        }
        if(null!=questionType&&!questionType.equals("")){
            qw.eq("question_type",questionType);
        }

        return new Result(0, "查询成功",DfFacaConfigService.list(qw));
    }


    @RequestMapping(value = "/listByType")
    public Object listByType(String type) {
        QueryWrapper<DfFacaConfig>qw=new QueryWrapper<>();
        qw.eq("type",type);
        return new Result(0, "查询成功",DfFacaConfigService.list(qw));
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfFacaConfig datas) {

        if (null != datas.getId()) {
            if (DfFacaConfigService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfFacaConfigService.save(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        }


    }


    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        if (DfFacaConfigService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");


    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String type,String content) {
        Page<DfFacaConfig> pages = new Page<DfFacaConfig>(page, limit);
        QueryWrapper<DfFacaConfig> ew=new QueryWrapper<DfFacaConfig>();

        if(null!=type&&!type.equals("")) {
            ew.eq("type", type);
        }
        if(null!=content&&!content.equals("")) {
            ew.like("content", content);
        }

        ew.orderByDesc("create_time");
        IPage<DfFacaConfig> list=DfFacaConfigService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());

    }


    @PostMapping(value = "/uploadExcel")
    @ResponseBody
    public Object uploadExcel(@RequestParam(value = "file", required = false) MultipartFile file,
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
                ImportExcelResult ter = DfFacaConfigService.importOrder( file);
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


}
