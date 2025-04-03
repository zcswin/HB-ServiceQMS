package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfQcpStandard;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.mapper.DfQcpStandardMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.ExportExcelUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * QCP标准 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Controller
@RequestMapping("/dfQcpStandard")
@ResponseBody
@CrossOrigin
@Api(tags = "QCP文件变更")
public class DfQcpStandardController {
    @Autowired
    com.ww.boengongye.service.DfQcpStandardService DfQcpStandardService;

    @Autowired
    private Environment env;

    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @Autowired
    private DfQcpStandardMapper dfQcpStandardMapper;
    @Autowired
    private DfFlowDataService dfFlowDataService;
    @Autowired
    private DfLiableManMapper dfLiableManMapper;

    private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);

//    @RequestMapping(value = "/listAll")
//    public Object listAll() {
//
//        return new Result(0, "查询成功",DfQcpStandardService.list());
//    }

    @ApiOperation("类别下拉列表")
    @GetMapping("/categoryList")
    public Result categoryList(){
        QueryWrapper<DfQcpStandard> qw = new QueryWrapper<>();
        qw.select("category")
                .groupBy("category");
        qw.isNotNull("category");
        return new Result(200, "查询成功", DfQcpStandardService.list(qw));
    }

    @ApiOperation("型号下拉列表")
    @GetMapping("/modelList")
    public Result modelList(){
        QueryWrapper<DfQcpStandard> qw = new QueryWrapper<>();
        qw.select("model")
                .groupBy("model");
        qw.isNotNull("model");
        return new Result(200, "查询成功", DfQcpStandardService.list(qw));
    }


    @ApiOperation("查询接口")
    @RequestMapping(value = "/listAll")
    public Object listAll(int page,int limit,String category,String model) {
        Page<DfQcpStandard> pages = new Page<>(page, limit);
        QueryWrapper<DfQcpStandard> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(category)){
            qw.eq("category",category);
        }
        if (StringUtils.isNotEmpty(model)){
            qw.eq("model",model);
        }
        qw.orderByDesc("create_time");
        IPage<DfQcpStandard> list = DfQcpStandardService.page(pages, qw);
        return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
    }

    @Transactional
    @ApiOperation("上传")
    @PostMapping(value = "/uploadTableAndFile")
    public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfQcpStandard dfQcpStandard) {
        try {
            dfQcpStandard.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            if (file.isEmpty()) {
                if (dfQcpStandardMapper.insert(dfQcpStandard) > 0){
                    return new Result(200, "上传成功");
                }
                return new Result(0, "上传失败，请选择文件");
            }
            String fileName = file.getOriginalFilename();

            InputStream ins = null;
            try {
                ins = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
                    ,"/文件变更履历/QCP标准/文件/"
                    ,file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
                        ,"/文件变更履历/QCP标准/文件/"
                        ,fileName);
                i++;
            }
            dfQcpStandard.setRealPath("文件变更履历/QCP标准/文件/" + f.getName());
            int id = dfQcpStandardMapper.insert(dfQcpStandard);
            //调PDA
            dfFlowDataService.createFlowDataFileUpdate(f.getName(),"QCP标准", id);
            //df_liable_man
            DfLiableMan dfLiableMan = new DfLiableMan();
            //添加责任人记录
//            dfLiableMan.setFactoryName("厂1")
//                    .setProcessName("CNC0,CNC1")
//                    .setProblemLevel("2")
//                    .setDayOrNight("白班")
//                    .setLiableManName("李华")
//                    .setLiableManCode("admin")
//                    .setType("QCP标准")
//                    .setStartTime(120)
//                    .setEndTime(240)
//                    .setUpdateTime(LocalDateTime.now())
//                    .setBimonthly("双月");
//            dfLiableManMapper.insert(dfLiableMan);
            CommunalUtils.inputStreamToFile2(ins, f);
            return new Result(200, "上传成功");
        } catch (Exception e) {
            logger.error("接口异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new Result(500, "接口异常");
    }

    @ApiOperation("修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody DfQcpStandard dfQcpStandard) {

        if (DfQcpStandardService.updateById(dfQcpStandard)) {
            return new Result(200, "保存成功");
        } else {
            return new Result(500, "保存失败");
        }
    }

    @Transactional
    @ApiOperation("根据id删除")
    // 根据id删除
    @RequestMapping(value = "/deleteById")
    public Result deleteById(String id) {
        try {
            DfQcpStandard byId = DfQcpStandardService.getById(id);
            if (DfQcpStandardService.removeById(id)) {
                File file = new File(byId.getRealPath());
                if (file.delete()){
                    return new Result(200, "删除成功");
                }else
                    return new Result(500,"文件不存在");
            }
            return new Result(500, "删除失败");

        } catch (NullPointerException e) {
            logger.error("根据id删除接口异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new Result(500, "接口异常");
    }

    // 导出excel表
    @ApiOperation("导出excel表")
    @RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
    public void getExcel(String project,String category,HttpServletResponse response, HttpServletRequest request) {
        try {
            QueryWrapper<DfQcpStandard> qw = new QueryWrapper<>();
//            qw.select("application_stage","category","property","dfm_name","change_version",
//                    "change_drawing_version","DATE_FORMAT(upload_date,'%Y-%m-%d %H:%m:%s') upload_date","upload_path");
            if (StringUtils.isNotEmpty(project)){
                qw.eq("project", project);
            }
            if (StringUtils.isNotEmpty(category)){
                qw.eq("category", category);
            }
//            qw.orderByAsc("application_stage")
//                    .orderByAsc("category")
//                    .orderByAsc("property")
//                    .orderByAsc("dfm_name")
//                    .orderByAsc("change_version")
//                    .orderByAsc("change_drawing_version")
//                    .orderByAsc("upload_date")
//                    .orderByAsc("upload_path");
            ArrayList titleKeyList = new ArrayList<>();
            titleKeyList.add("category");
            titleKeyList.add("model");
            titleKeyList.add("build");
            titleKeyList.add("title");
            titleKeyList.add("rev");
            titleKeyList.add("date");
            titleKeyList.add("drawing");
            titleKeyList.add("draw_rev");
            titleKeyList.add("process");
            titleKeyList.add("process_tooling_changes");
            titleKeyList.add("comment");
            titleKeyList.add("mqe_approve");
            titleKeyList.add("approve_date");
            titleKeyList.add("remarks");
            Map titleMap = new HashMap<>();
            titleMap.put("category","类别");
            titleMap.put("model","型号");
            titleMap.put("build","Build");
            titleMap.put("title","Title");
            titleMap.put("rev","Rev");
            titleMap.put("date","Date");
            titleMap.put("drawing","Drawing");
            titleMap.put("draw_rev","DrawRev");
            titleMap.put("process","Process");
            titleMap.put("process_tooling_changes","ProcessToolingChanges");
            titleMap.put("comment","comment");
            titleMap.put("mqe_approve","MqeApprove");
            titleMap.put("approve_date","ApproveDate");
            titleMap.put("remarks","备注");
            titleMap.put("image","图片");
//            titleMap.put("project","客户审批时间");
//            titleMap.put("project","变更内容");
//            titleMap.put("project","确认人");
            List<Map<String,Object>> datas= DfQcpStandardService.listByExport(qw);
            exportExcelUtil.expoerDataExcel(response, titleKeyList, titleMap, datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件
     */
    @ApiOperation("获取文件")
    @RequestMapping(value = "/getFile", produces = MediaType.ALL_VALUE)
    public byte[] getFile(String realPath) throws IOException {
        File file = new File(realPath);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } finally {
            inputStream.close();
        }
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfQcpStandard datas) {

        if (null != datas.getId()) {
            if (DfQcpStandardService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfQcpStandardService.save(datas)) {
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
        if (DfQcpStandardService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String productionPhase,String factoryCode,String fileName,String fileVersion) {
//        try {
        Page<DfQcpStandard> pages = new Page<DfQcpStandard>(page, limit);
        QueryWrapper<DfQcpStandard> ew=new QueryWrapper<DfQcpStandard>();
        if(null!=productionPhase&&!productionPhase.equals("")) {
            ew.like("production_phase", productionPhase);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("factory_code", factoryCode);
        }
        if(null!=fileName&&!fileName.equals("")) {
            ew.like("file_name", fileName);
        }
        if(null!=fileVersion&&!fileVersion.equals("")) {
            ew.like("file_version", fileVersion);
        }
        ew.orderByDesc("create_time");
        IPage<DfQcpStandard> list=DfQcpStandardService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @ApiOperation("Excel导入")
    @RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
    @ResponseBody
    public Object uploadExcel( @RequestParam(value = "file", required = false) MultipartFile file , DfQcpStandard dfQcpStandard) throws Exception {
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
            File fl = new File(env.getProperty("uploadPath") + "qcp/");
            if (!fl.exists()) {
                fl.mkdirs();
            }
            File f = new File(env.getProperty("uploadPath") + "qcp/" + file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                System.out.println(i);
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = new File(env.getProperty("uploadPath") + "qcp/" + fileName);
                i++;
            }

            CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "qcp",
                    env.getProperty("uploadPath") + "qcp/");

            try {
                ImportExcelResult ter = DfQcpStandardService.importOrder(env.getProperty("uploadPath") + "qcp/" + fileName, file);
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


    @ApiOperation("QCP下载接口")
    @RequestMapping(value="/media", method=RequestMethod.GET)
    public void getDownload(Long id, HttpServletRequest request, HttpServletResponse response) {
        FileInputStream fis = null;
        ServletOutputStream sos = null;
        try {
            DfQcpStandard byId = DfQcpStandardService.getById(id);
            String path = env.getProperty("uploadPath") + byId.getRealPath();
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(byId.getFileName(), "UTF-8"));

            File file = new File(path);
            fis = new FileInputStream(file);
            sos = response.getOutputStream();
            IOUtils.copy(fis, sos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("下载失败！");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (sos != null) {
                    sos.flush();
                    sos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation("下载模板")
    @GetMapping("/downLoadExcelMould")
    public void downLoadExcelMould(HttpServletResponse response) {
        DfQcpStandardService.exportModel(response, "QCP标准");
    }

}
