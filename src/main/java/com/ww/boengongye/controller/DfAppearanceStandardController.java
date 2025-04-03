package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfAppearanceStandard;
import com.ww.boengongye.entity.DfDrawingFile;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfAppearanceStandardMapper;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.service.DfAppearanceStandardService;
import com.ww.boengongye.service.DfFlowDataService;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-21
 */
@Controller
@RequestMapping("/dfAppearanceStandard")
@ResponseBody
@CrossOrigin
@Api(tags = "外观标准")
public class DfAppearanceStandardController {

	@Autowired
	private DfAppearanceStandardService dfAppearanceStandardService;
	@Autowired
	private DfAppearanceStandardMapper dfAppearanceStandardMapper;
	@Autowired
	private DfLiableManMapper dfLiableManMapper;
	@Autowired
	private ExportExcelUtil exportExcelUtil;

	@Autowired
	private Environment env;

	@Autowired
	private DfFlowDataService dfFlowDataService;

	private static final Logger logger = LoggerFactory.getLogger(DfDrawingFileController.class);

	@ApiOperation("型号下拉列表")
	@GetMapping("/modelList")
	public Result modelList(){
		QueryWrapper<DfAppearanceStandard> qw = new QueryWrapper<>();
		qw.select("model")
				.groupBy("model");
		qw.isNotNull("model");
		return new Result(200, "查询成功", dfAppearanceStandardService.list(qw));
	}

	@ApiOperation("规格等级下拉列表")
	@GetMapping("/levelList")
	public Result levelList(){
		QueryWrapper<DfAppearanceStandard> qw = new QueryWrapper<>();
		qw.select("level")
				.groupBy("level");
		qw.isNotNull("level");
		return new Result(200, "查询成功", dfAppearanceStandardService.list(qw));
	}

	@ApiOperation("分页查询")
	@GetMapping(value = "/listAll")
	public Object listAll(Integer page,Integer limit,String model,String level) {
		Page<DfAppearanceStandard> pages = new Page<DfAppearanceStandard>(page, limit);
		QueryWrapper<DfAppearanceStandard> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(level)){
			qw.eq("leverl",level);
		}
		if (StringUtils.isNotEmpty(model)){
			qw.eq("model",model);
		}
		qw.orderByDesc("create_time");
		IPage<DfAppearanceStandard> list = dfAppearanceStandardService.page(pages, qw);
		return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
	}


	@ApiOperation("新增")
	@PostMapping(value = "/add")
	public Object add(@RequestBody  DfAppearanceStandard dfAppearanceStandard) {
		if (dfAppearanceStandardService.save(dfAppearanceStandard)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("修改")
	@GetMapping(value = "/edit")
	public Object edit(DfAppearanceStandard dfAppearanceStandard) {
		if (dfAppearanceStandardService.updateById(dfAppearanceStandard)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object delete(DfAppearanceStandard dfAppearanceStandard) {
		if (dfAppearanceStandardService.removeById(dfAppearanceStandard)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}

	@ApiOperation("导出excel表")
	@RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
	public void getExcel(String model, String level, HttpServletResponse response, HttpServletRequest request) {
		try {
			QueryWrapper<DfDrawingFile> qw = new QueryWrapper<>();
			if (StringUtils.isNotEmpty(model)){
				qw.eq("model", model);
			}
			if (StringUtils.isNotEmpty(level)){
				qw.eq("level", level);
			}
			ArrayList titleKeyList = new ArrayList<>();
			titleKeyList.add("model");
			titleKeyList.add("level");
			titleKeyList.add("project");
			titleKeyList.add("category");
			titleKeyList.add("area");
			titleKeyList.add("name");
			titleKeyList.add("definition");
			titleKeyList.add("test_method");
			titleKeyList.add("standard");
			titleKeyList.add("level");
			Map titleMap = new HashMap<>();
			titleMap.put("model","型号");
			titleMap.put("level","规格等级");
			titleMap.put("project","项目");
			titleMap.put("category","类别");
			titleMap.put("area","区域");
			titleMap.put("name","名称");
			titleMap.put("definition","定义");
			titleMap.put("test_method","检验方法");
			titleMap.put("standard","允收标准");
//            titleMap.put("project","客户审批时间");
//            titleMap.put("project","变更内容");
//            titleMap.put("project","确认人");
			List<Map<String,Object>> datas= dfAppearanceStandardService.listByExport(qw);
			exportExcelUtil.expoerDataExcel(response, titleKeyList, titleMap, datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@ApiOperation("Excel导入")
	@RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
	@ResponseBody
	public Object uploadExcel(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			File fl = new File(env.getProperty("uploadPath") + "appearanceStandard/");
			if (!fl.exists()) {
				fl.mkdirs();
			}
			File f = new File(env.getProperty("uploadPath") + "appearanceStandard/" + file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				System.out.println(i);
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = new File(env.getProperty("uploadPath") + "appearanceStandard/" + fileName);
				i++;
			}

			CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "appearanceStandard",
					env.getProperty("uploadPath") + "appearanceStandard/");

			try {
				ImportExcelResult ter = dfAppearanceStandardService.importOrder(env.getProperty("uploadPath") + "dfm/" + fileName, file);
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

	@ApiOperation("下载模板")
	@GetMapping("/downLoadExcelMould")
	public void downLoadExcelMould(HttpServletResponse response) {
		dfAppearanceStandardService.exportModel(response, "外观标准");
	}

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfAppearanceStandard dfAppearanceStandard) {
		try {
			dfAppearanceStandard.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (file.isEmpty()) {
				if (dfAppearanceStandardMapper.insert(dfAppearanceStandard) > 0){
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
					,"/文件变更履历/外观标准/文件/"
					,file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						,"/文件变更履历/外观标准/文件/"
						,fileName);
				i++;
			}
			dfAppearanceStandard.setRealPath("文件变更履历/外观标准/文件/" + f.getName());
			int id = dfAppearanceStandardMapper.insert(dfAppearanceStandard);
			//调PDA
			dfFlowDataService.createFlowDataFileUpdate(f.getName(),"外观标准", id);
			//df_liable_man
			DfLiableMan dfLiableMan = new DfLiableMan();
			//添加责任人记录
//			dfLiableMan.setFactoryName("厂1")
//					.setProcessName("CNC0,CNC1")
//					.setProblemLevel("2")
//					.setDayOrNight("白班")
//					.setLiableManName("李华")
//					.setLiableManCode("admin")
//					.setType("外观标准")
//					.setStartTime(120)
//					.setEndTime(240)
//					.setUpdateTime(LocalDateTime.now())
//					.setBimonthly("双月");
//			dfLiableManMapper.insert(dfLiableMan);
			CommunalUtils.inputStreamToFile2(ins, f);
			return new Result(200, "上传成功");
		} catch (Exception e) {
			logger.error("接口异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return new Result(500, "接口异常");
	}
}
