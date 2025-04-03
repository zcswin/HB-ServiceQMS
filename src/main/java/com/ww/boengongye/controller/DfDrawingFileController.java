package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfDrawingFile;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfDrawingFileMapper;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.impl.ExportDataService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.ExportExcelUtil;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收集各阶段生产需参考的DFM文件 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-11-25
 */
@Controller
@RequestMapping("/dfDrawingFile")
@ResponseBody
@CrossOrigin
@Api(tags = "DFM文件变更")
public class DfDrawingFileController {
    @Autowired
    com.ww.boengongye.service.DfDrawingFileService DfDrawingFileService;

    @Autowired
    private Environment env;
    @Autowired
    ExportDataService exportDataService;
    @Autowired
    private ExportExcelUtil exportExcelUtil;

    @Autowired
    private DfDrawingFileMapper dfDrawingFileMapper;
    @Autowired
    private DfFlowDataService dfFlowDataService;
    @Autowired
    private DfLiableManMapper dfLiableManMapper;

    private static final Logger logger = LoggerFactory.getLogger(DfDrawingFileController.class);


    @ApiOperation("类别下拉列表")
    @GetMapping("/categoryList")
    public Result categoryList(){
        QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
        qw.select("category")
                .groupBy("category");
        qw.isNotNull("category");
        return new Result(200, "查询成功",DfDrawingFileService.list(qw));
    }

    @ApiOperation("项目下拉列表")
    @GetMapping("/projectList")
    public Result projectList(){
        QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
        qw.select("project")
                .groupBy("project");
        qw.isNotNull("project");
        return new Result(200, "查询成功",DfDrawingFileService.list(qw));
    }
    @ApiOperation("适用范围_阶段下拉列表")
    @GetMapping("/applicationStageList")
    public Result applicationStageList(){
        QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
        qw.select("application_stage")
                .groupBy("application_stage");
        qw.isNotNull("application_stage");
        return new Result(200, "查询成功",DfDrawingFileService.list(qw));
    }

    @ApiOperation("型号下拉列表")
    @GetMapping("/modelList")
    public Result modelList(){
        QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
        qw.select("model")
                .groupBy("model");
        qw.isNotNull("model");
        return new Result(200, "查询成功",DfDrawingFileService.list(qw));
    }

    @ApiOperation("分页查询接口")
    @RequestMapping(value = "/listAll")
    public Object listAll(int page,int limit,String category,String model) {
        Page<DfDrawingFile> pages = new Page<DfDrawingFile>(page,limit);
        QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(category)){
            qw.eq("category",category);
        }
        if (StringUtils.isNotEmpty(model)){
            qw.eq("model",model);
        }
//        qw.select("project","application_stage","category","property","dfm_name","change_version","change_drawing_version"
//                ,"upload_date","upload_path","realPath");
        qw.orderByAsc("application_stage")
                .orderByAsc("dfm_name")
                .orderByDesc("create_time");
        IPage<DfDrawingFile> list = DfDrawingFileService.page(pages, qw);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
    }

    @Transactional
    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfDrawingFile dfDrawingFile) {
        try {
            if (file.isEmpty()) {
                return new Result(0, "上传失败，请选择文件");
            }
            System.out.println("开始上传");
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);

//            if (fileName.indexOf(".xlsx") == -1 && fileName.indexOf(".xls") == -1) {
//                return new Result(1, "请上传xlsx或xls格式的文件");
//            }
            InputStream ins = null;
            try {
                ins = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            File fl = new File(env.getProperty("uploadPath") + "dfm/");
//            if (!fl.exists()){
//                fl.mkdirs();
//            }
            File f = new File(env.getProperty("uploadPath") + "dfm/" + file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                System.out.println(i);
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = new File(dfDrawingFile.getUploadPath() + fileName);
                dfDrawingFile.setRealPath(dfDrawingFile.getUploadPath() + fileName);
                i++;
            }
            dfDrawingFile.setRealPath(f.getPath());
            DfDrawingFileService.save(dfDrawingFile);
            CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "dfm",
                    env.getProperty("uploadPath") + "dfm/");

            return new Result(200, "上传成功");
        } catch (Exception e) {
            logger.error("接口异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new Result(500, "接口异常");
    }

    @ApiOperation("修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody DfDrawingFile datas) {

        if (DfDrawingFileService.updateById(datas)) {
            return new Result(200, "保存成功");
        } else {
            return new Result(500, "保存失败");
        }
    }



    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfDrawingFile datas) {

        if (null != datas.getId()) {
            if (DfDrawingFileService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfDrawingFileService.save(datas)) {
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

    @Transactional
    @ApiOperation("根据id删除")
    // 根据id删除
    @RequestMapping(value = "/delete")
    public Result delete(String id) {
        try {
            DfDrawingFile byId = DfDrawingFileService.getById(id);
            if (DfDrawingFileService.removeById(id)) {
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


    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page ,int limit,String category,String factoryCode,String model,String applicationStage) {
//        try {
        Page<DfDrawingFile> pages = new Page<DfDrawingFile>(page, limit);
        QueryWrapper<DfDrawingFile> ew=new QueryWrapper<DfDrawingFile>();
        if(null!=category&&!category.equals("")) {
            ew.like("category", category);
        }
        if(null!=factoryCode&&!factoryCode.equals("")) {
            ew.eq("factory_code", factoryCode);
        }
        if(null!=model&&!model.equals("")) {
            ew.like("model", model);
        }
        if(null!=applicationStage&&!applicationStage.equals("")) {
            ew.like("application_stage", applicationStage);
        }
        ew.orderByDesc("create_time");
        IPage<DfDrawingFile> list=DfDrawingFileService.page(pages,ew);
        return new Result(0, "查询成功",list.getRecords(),(int)list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }


    @ApiOperation("Excel导入")
    @RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
    @ResponseBody
    public Object uploadExcel( @RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            System.out.println("******");
            String id2 = request.getParameter("id");// 获取data中数据
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
            File fl = new File(env.getProperty("uploadPath") + "dfm/");
            if (!fl.exists()) {
                fl.mkdirs();
            }
            File f = new File(env.getProperty("uploadPath") + "dfm/" + file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                System.out.println(i);
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = new File(env.getProperty("uploadPath") + "dfm/" + fileName);
                i++;
            }

            CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "dfm",
                    env.getProperty("uploadPath") + "dfm/");

            try {
                ImportExcelResult ter = DfDrawingFileService.importOrder(env.getProperty("uploadPath") + "dfm/" + fileName, file);
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
     * 获取图片
     *
     * @param floder1
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getImg/{floder1}/{floder2}/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String floder1,@PathVariable String floder2, @PathVariable String name) throws IOException {
        File file = new File(env.getProperty("uploadPath") + floder1 +"/"+ floder2 +"/" + name);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } finally {
            inputStream.close();
        }
    }

    /**
     * 获取图片
     *
     * @param floder1
     * @param name
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getImg2/{floder1}/{floder2}/{floder3}/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage2(@PathVariable String floder1,@PathVariable String floder2,@PathVariable String floder3, @PathVariable String name) throws IOException {
        File file = new File(env.getProperty("uploadPath") + floder1 +"/"+ floder2 +"/" + floder3 +"/"+ name);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } finally {
            inputStream.close();
        }
    }

    /**
     * 获取图片
     */
    @ApiOperation("获取图片")
    @RequestMapping(value = "/getImage3", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage3(String realPath) throws IOException {
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

    // 导出excel表
    @ApiOperation("导出excel表")
    @RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
    public void getExcel(String project,String category,HttpServletResponse response, HttpServletRequest request) {
        try {
            QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
            qw.select("application_stage","category","property","dfm_name","change_version",
                    "change_drawing_version","DATE_FORMAT(upload_date,'%Y-%m-%d %H:%m:%s') upload_date","upload_path");
            if (StringUtils.isNotEmpty(project)){
                qw.eq("project", project);
            }
            if (StringUtils.isNotEmpty(category)){
                qw.eq("category", category);
            }
            qw.orderByAsc("application_stage")
                    .orderByAsc("category")
                    .orderByAsc("property")
                    .orderByAsc("dfm_name")
                    .orderByAsc("change_version")
                    .orderByAsc("change_drawing_version")
                    .orderByAsc("upload_date")
                    .orderByAsc("upload_path");
            ArrayList titleKeyList = new ArrayList<>();
            titleKeyList.add("category");
            titleKeyList.add("project");
            titleKeyList.add("application_stage");
            titleKeyList.add("property");
            titleKeyList.add("dfm_name");
            titleKeyList.add("change_version");
            titleKeyList.add("change_drawing_version");
            titleKeyList.add("change_time");
            titleKeyList.add("upload_date");
            Map titleMap = new HashMap<>();
			titleMap.put("project","项目");
            titleMap.put("category","类别");
//            titleMap.put("project","型号");
            titleMap.put("application_stage","适用范围/阶段");
            titleMap.put("property","属性");
            titleMap.put("dfm_name","DFM名称");
            titleMap.put("change_version","变更DFM版本");
            titleMap.put("change_drawing_version","变更DFM图纸版本");
            titleMap.put("change_time","变更日期");
            titleMap.put("upload_date","上传Teams时间");
            List<Map<String,Object>> datas= DfDrawingFileService.listByExport(qw);
			SimpleDateFormat format = new SimpleDateFormat();
			datas.forEach(m->{
				m.put("change_time",m.get("change_time").toString().replaceAll("T", " "));
				m.put("upload_date",m.get("change_time").toString().replaceAll("T", " "));
			});
            exportExcelUtil.expoerDataExcel(response, titleKeyList, titleMap, datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@ApiOperation("下载模板")
	@GetMapping("/downLoadExcelMould")
	public void downLoadExcelMould(HttpServletResponse response) {
		DfDrawingFileService.exportModel(response, "DFM文件变更");
	}

    @Transactional
    @ApiOperation("上传")
    @PostMapping(value = "/uploadTableAndFile")
    public Object uploadTableAndFile(@RequestParam(value = "file", required = false) MultipartFile file , DfDrawingFile dfDrawingFile) {
        try {
            dfDrawingFile.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            if (file.isEmpty()) {
                if (dfDrawingFileMapper.insert(dfDrawingFile) > 0){
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
                    ,"/文件变更履历/DFM变更/文件/"
                    ,file.getOriginalFilename());
            String backupFileName = f.getName();
            int i = 1;
            while (f.exists()) {
                String[] list = backupFileName.split("\\.");
                fileName = list[0] + "(" + i + ")." + list[1];
                f = f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
                        ,"/文件变更履历/DFM变更/文件/"
                        ,fileName);
                i++;
            }
            dfDrawingFile.setRealPath("文件变更履历/DFM变更/文件/" + f.getName());
            int id = dfDrawingFileMapper.insert(dfDrawingFile);
            //调PDA
            dfFlowDataService.createFlowDataFileUpdate(f.getName(),"DFM变更", id);
            //df_liable_man
            DfLiableMan dfLiableMan = new DfLiableMan();
            //添加责任人记录
//            dfLiableMan.setFactoryName("厂1")
//                    .setProcessName("CNC0,CNC1")
//                    .setProblemLevel("2")
//                    .setDayOrNight("白班")
//                    .setLiableManName("李华")
//                    .setLiableManCode("admin")
//                    .setType("DFM变更")
//                    .setStartTime(120)
//                    .setEndTime(240)
//                    .setUpdateTime(LocalDateTime.now())
//                    .setBimonthly("双月");
//            dfLiableManMapper.insert(dfLiableMan);
//            CommunalUtils.inputStreamToFile2(ins, f);
//            return new Result(200, "上传成功");
        } catch (Exception e) {
            logger.error("接口异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new Result(500, "接口异常");
    }
}
