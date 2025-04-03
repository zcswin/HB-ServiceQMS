package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfControlStandardConfigBatch;
import com.ww.boengongye.entity.DfControlStandardConfigBatchLog;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.service.impl.ExportDataService;
import com.ww.boengongye.utils.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.ww.boengongye.utils.ExcelExportUtil2.generateFileName;

/**
 * <p>
 * 管控标准配置 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Controller
@RequestMapping("/dfControlStandardConfig")
@ResponseBody
@CrossOrigin
public class DfControlStandardConfigController {
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigService DfControlStandardConfigService;
    @Autowired
    com.ww.boengongye.service.DfControlStandardConfigBatchService dfControlStandardConfigBatchService;
    @Autowired
    ExportDataService ExportDataService;
    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(DfControlStandardConfigController.class);

    @RequestMapping(value = "/listAll")
    public Object listAll() {

        return new Result(0, "查询成功", DfControlStandardConfigService.list());
    }
    @RequestMapping(value = "/listDataType")
    public Object listDataType() {

        return new Result(0, "查询成功", DfControlStandardConfigService.listDataType());
    }
    @RequestMapping(value = "/listByCraftId")
    public Object listByCraftId(int id) {
        QueryWrapper<DfControlStandardConfig> qw = new QueryWrapper<>();
        qw.eq("craft_id", id);
        return new Result(0, "查询成功", DfControlStandardConfigService.list(qw));
    }


    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public Result save(@RequestBody DfControlStandardConfig datas) {
//        try {
//        Field[] f= DfControlStandardConfig.class.getDeclaredFields();
//        //给TAnnals对象赋值
//        for(int i=0;i<f.length;i++){
//            //获取属相名
//            String attributeName=f[i].getName();
//            //将属性名的首字母变为大写，为执行set/get方法做准备
//            String methodName=attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
//            try{
//                //获取TAnnals类当前属性的setXXX方法（只能获取公有方法）
//                Method setMethod= DfControlStandardConfig.class.getMethod("set"+methodName,String.class);
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
        if (null != datas.getId()) {
            if (DfControlStandardConfigService.updateById(datas)) {
                return new Result(200, "保存成功");
            } else {
                return new Result(500, "保存失败");
            }
        } else {

//            datas.setCreateTime(TimeUtil.getNowTimeByNormal());
            if (DfControlStandardConfigService.save(datas)) {
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
        if (DfControlStandardConfigService.removeById(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

//        } catch (NullPointerException e) {
//            logger.error("根据id删除接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

    @ApiOperation("批量删除")
    @PostMapping(value = "/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> id) {
        if (DfControlStandardConfigService.removeByIds(id)) {
            return new Result(200, "删除成功");
        }
        return new Result(500, "删除失败");

    }

    @RequestMapping(value = "/listAllByAdmin")
    public Result listAllByAdmin(int page, int limit,String project,String process,String line, String content) {
//        try {
        Page<DfControlStandardConfig> pages = new Page<DfControlStandardConfig>(page, limit);
        QueryWrapper<DfControlStandardConfig> ew = new QueryWrapper<DfControlStandardConfig>();
            if(StringUtils.isNotEmpty(content)){
                ew.and(wrapper -> wrapper.like("project", content))
                        .or(wrapper -> wrapper.like("linebody", content))
                        .or(wrapper -> wrapper.like("process", content))
                        .or(wrapper -> wrapper.like("data_type", content))
                        .or(wrapper -> wrapper.like("data_classify", content))
                        .or(wrapper -> wrapper.like("detail", content))
                        .or(wrapper -> wrapper.like("standard_value", content))
                        .or(wrapper -> wrapper.like("frequency", content))
                        .or(wrapper -> wrapper.like("machine", content))

                ;
            }
            ew.like(StringUtils.isNotEmpty(project),"project",project)
                    .like(StringUtils.isNotEmpty(process),"process",process)
                    .like(StringUtils.isNotEmpty(line),"linebody",line);

        ew.orderByDesc("create_time");
        IPage<DfControlStandardConfig> list = DfControlStandardConfigService.page(pages, ew);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());

    }

    @RequestMapping(value = "/listAllByAdminJoinStatus")
    public Result listAllByAdminJoinStatus(int page, int limit, String type, String routingId,String batchId) {
//        try {
        Page<DfControlStandardConfig> pages = new Page<DfControlStandardConfig>(page, limit);
        QueryWrapper<DfControlStandardConfig> ew = new QueryWrapper<DfControlStandardConfig>();
        if (null != type && !type.equals("")) {
            ew.like("d.data_type", type);
        }
        if (null != routingId && !routingId.equals("")) {
            ew.eq("d.routing_id", routingId);
        }
//        if (null != batchId && !batchId.equals("")) {
//            ew.eq("s.batch_id", batchId);
//        }
        ew.orderByDesc("d.create_time");
        IPage<DfControlStandardConfig> list = DfControlStandardConfigService.listByJoinPage(pages, ew,batchId);
        return new Result(0, "查询成功", list.getRecords(), (int) list.getTotal());
//        }catch(NullPointerException e) {
//            logger.error("管理后台分页获取接口异常", e);
//        }
//        return new Result(500, "接口异常");
    }

    @RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
    @ResponseBody
    public Object uploadExcel(@RequestParam(value = "factory", required = false) String  factory, @RequestParam(value = "project", required = false) String  project,@RequestParam(value = "process", required = false) String  process,@RequestParam(value = "line", required = false) String  line,@RequestParam(value = "file", required = false) MultipartFile file,
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
                ImportExcelResult ter = DfControlStandardConfigService.importOrder(factory,project,process,line, file);
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


    // 导出excel表
    @RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
    public void getExcel(HttpServletResponse response, HttpServletRequest request,
                                     @RequestParam(value = "routingId", required = false) int routingId ) {
        try {
            ArrayList titleKeyList = new ArrayList<>();
//            titleKeyList.add("factoryName");
//            titleKeyList.add("workStationName");
//            titleKeyList.add("workshopSectionName");
//            titleKeyList.add("routingName");
            titleKeyList.add("dataClassify");
            titleKeyList.add("detail");
            titleKeyList.add("standardValue");
            titleKeyList.add("createTime");
            Map titleMap = new HashMap<>();
//            titleMap.put("factoryName","工厂");
//            titleMap.put("workStationName","工站");
//            titleMap.put("workshopSectionName","工段");
//            titleMap.put("routingName","工艺");
            titleMap.put("dataClassify","分类");
            titleMap.put("detail","细项");
            titleMap.put("standardValue","标准值/方法");
            titleMap.put("createTime","创建时间");
            System.out.println("D付多付多付多付付付付付付付付付付付付付付付付付付付付");
//            List<Map<String,Object>> datas=DfControlStandardConfigService.listByExport(routingId);
////           List<DfControlStandardConfig>datas=DfControlStandardConfigService.list();
//            ExportDataService.exportDataToEx(response, titleKeyList, titleMap, datas);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }


    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.GET })
    @ResponseBody
    public void downloadExcel(int routingId, HttpServletResponse response, HttpServletRequest request
    ) throws IOException {


        List<DfControlStandardConfig> datas=DfControlStandardConfigService.listByExport(routingId);

        List<Map> maps = new ArrayList<>();
        for(DfControlStandardConfig r:datas){
            try {
                maps.add(Excel.objectToMap(r));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        if(maps!=null && maps.size()>0){
            String companyName = "管控标准列表";
            String sheetTitle = companyName;
            String [] title = new String[]{"工厂","工站","工段","工艺","分类","细项","标准值/方法","创建时间"};        //设置表格表头字段
            String [] properties = new String[]{"factoryName","workStationName","workshopSectionName","routingName",
                    "dataClassify","detail","standardValue","createTime" };  // 查询对应的字段
            ExcelExportUtil excelExport2 = new ExcelExportUtil();
            excelExport2.setData(maps);
            excelExport2.setHeardKey(properties);
            excelExport2.setFontSize(14);
            excelExport2.setSheetName(sheetTitle);
            excelExport2.setTitle(sheetTitle);
            excelExport2.setHeardList(title);
            excelExport2.exportExport(request, response);
        }
    }






    @PostMapping("/upload")
    public Result upload(MultipartFile file, String process, String project) throws Exception {
        String factory = "J10-1";
        String lineBody = "Line1";
        int num = DfControlStandardConfigService.upload(file, factory, lineBody, process, project);

        return new Result(200, "成功插入" + num + "条数据");
    }

    @GetMapping("/listByProcess")
    public Result listByProcess(int page, int limit, String factory, String lineBody, String process,
                                String project, String dataType) {
        Page<DfControlStandardConfig> pages = new Page<DfControlStandardConfig>(page, limit);
        QueryWrapper<DfControlStandardConfig> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "factory", factory)
            .eq(null != lineBody && !"".equals(lineBody), "linebody", lineBody)
            .eq(null != process && !"".equals(process), "process", process)
            .eq(null != project && !"".equals(project), "project", project)
            .eq(null != dataType && !"".equals(dataType), "data_type", dataType);
        IPage<DfControlStandardConfig> page1 = DfControlStandardConfigService.page(pages, qw);
        return new Result(0, "查询成功", page1.getRecords(), (int)page1.getTotal(),(int)page1.getPages());
    }

    @PostMapping(value = "/importDataAll")
    @ApiOperation("导入数据(多线体)")
    @Transactional(rollbackFor = Exception.class)
    public Result importDataAll(MultipartFile file) throws Exception {
        Map<String, DfControlStandardConfigBatch>batchData=new HashMap<>();

        ExcelImportUtil excel = new ExcelImportUtil(file);
        List<Map<String, String>> maps = excel.readExcelContent();
        List<DfControlStandardConfig> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            if(null!=map.get("分类")&&!map.get("分类").equals("")){
                String[] lineList=map.get("线体").split("/");
                for(String line:lineList){
//                    String[] colorList=map.get("颜色").split("/");
//                    for(String color:colorList){

                        DfControlStandardConfig tc = new DfControlStandardConfig();
                        tc.setDataType(map.get("管控类型名称"));
                        tc.setDataClassify(map.get("分类"));
                        tc.setDetail(map.get("细项"));
                        tc.setStandardValue(map.get("标准值/方法"));
                        tc.setFrequency(map.get("抽检频率"));
                        tc.setMachine(map.get("设备"));
                        tc.setFactory(map.get("工厂"));
                        tc.setProject(map.get("项目"));
                        tc.setProcess(map.get("工序"));
                        tc.setLinebody(line);
//                        tc.setColor(color);
                    if(!batchData.containsKey(map.get("项目")+line)){
                        DfControlStandardConfigBatch batch=new DfControlStandardConfigBatch();
                        tc.setBatchCode(map.get("项目")+"_"+line+"-"+TimeUtil.getNowTimeLong());
                        batch.setBatchCode(tc.getBatchCode());
                        batch.setProject(tc.getProject());
                        batch.setLineBody(line);
                        batchData.put(map.get("项目")+line,batch);
                    }else{
                        tc.setBatchCode(batchData.get(map.get("项目")+line).getBatchCode());
                    }
                        list.add(tc);
//                    }
                }

            }

        }
        if (DfControlStandardConfigService.saveBatch(list)){
            //遍历保存批次数据
            batchData.forEach((key,value)->{
                dfControlStandardConfigBatchService.save(value);
            });
            return new Result(200,"成功导入"+list.size()+"条数据");

        }
        return new Result(200,"导入数据失败");
    }
    @ApiOperation("下载模板文件")
    @RequestMapping(value ="/download/{fileName}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void download(@PathVariable String fileName,HttpServletResponse response) {
        FileInputStream fis = null;
        ServletOutputStream sos = null;
        try {
            String path = env.getProperty("templateFilePath") + fileName;
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            File file=new File(path);
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




}
