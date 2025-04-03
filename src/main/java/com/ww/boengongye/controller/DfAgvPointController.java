package com.ww.boengongye.controller;


import com.google.gson.Gson;
import com.ww.boengongye.entity.ARVPositionData;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.entity.SizeQueueData;
import com.ww.boengongye.service.DfAgvPointService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.RedisUtils;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-14
 */
@Controller
@RequestMapping("/dfAgvPoint")
@ResponseBody
@CrossOrigin
@Api(tags = "agv")
public class DfAgvPointController {
    @Autowired
    private Environment env;

    @Autowired
    DfAgvPointService dfAgvPointService;

    @Resource
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DfAgvPointController.class);

    @GetMapping("/getById")
    public Result getById(int id) {

        return new Result(200, "查询成功",dfAgvPointService.getById(id));
    }
    @GetMapping("/listXSARV")
    public Result listXSARV() {
        RedisUtils redis=new RedisUtils();
        Set<String> datas= redisTemplate.keys("XSARV*");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<ARVPositionData>result=new ArrayList<>();
        for (String key : datas) {
            System.out.println("keys:"+key);
            Object v = valueOperations.get(key);
            if(null!=v){
                ARVPositionData position = new Gson().fromJson(v.toString(), ARVPositionData.class);
                result.add(position);
            }
        }
        return new Result(200, "查询成功",result);
    }
    @GetMapping("/listARV")
    public Result listARV() {
        RedisUtils redis=new RedisUtils();
        Set<String> datas= redisTemplate.keys("ARV*");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<ARVPositionData>result=new ArrayList<>();
        for (String key : datas) {
            System.out.println("keys:"+key);
            Object v = valueOperations.get(key);
            if(null!=v){
                ARVPositionData position = new Gson().fromJson(v.toString(), ARVPositionData.class);
                result.add(position);
            }
        }
        return new Result(200, "查询成功",result);
    }

    @GetMapping("/listAll")
    public Result listAll() {

        return new Result(200, "查询成功",dfAgvPointService.list());
    }
    @RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
    @ResponseBody
    public Object uploadExcel(@RequestParam(value = "factory", required = false) String  factory, @RequestParam(value = "project", required = false) String  project, @RequestParam(value = "process", required = false) String  process, @RequestParam(value = "line", required = false) String  line, @RequestParam(value = "file", required = false) MultipartFile file,
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
                ImportExcelResult ter = dfAgvPointService.importOrder( file);
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
