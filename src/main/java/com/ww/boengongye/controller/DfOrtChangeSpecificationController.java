package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfOrtChangeSpecification;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.mapper.DfOrtChangeSpecificationMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfOrtChangeSpecificationService;
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
 * ORT规格变更 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Controller
@RequestMapping("/dfOrtChangeSpecification")
@ResponseBody
@CrossOrigin
@Api(tags = "ORT规格变更")
public class DfOrtChangeSpecificationController {

	@Autowired
	private DfOrtChangeSpecificationService dfOrtChangeSpecificationService;

	@Autowired
	private Environment env;
	@Autowired
	private DfOrtChangeSpecificationMapper dfOrtChangeSpecificationMapper;
	@Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfLiableManMapper dfLiableManMapper;

	@Autowired
	private ExportExcelUtil exportExcelUtil;

	private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);
	@ApiOperation("型号下拉列表")
	@GetMapping("/modelList")
	public Result modelList() {
		QueryWrapper<DfOrtChangeSpecification> qw = new QueryWrapper<>();
		qw.select("model")
				.groupBy("model");
		qw.isNotNull("model");
		return new Result(200, "查询成功", dfOrtChangeSpecificationService.list(qw));
	}

	@ApiOperation("分页查询")
	@GetMapping(value = "/listAll")
	public Object listAll(Integer page,Integer limit,String model) {
		Page<DfOrtChangeSpecification> pages = new Page<DfOrtChangeSpecification>(page, limit);
		QueryWrapper<DfOrtChangeSpecification> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(model)){
			qw.eq("model",model);
		}
		qw.orderByAsc("test_name")
				.orderByAsc("test_process_amount")
				.orderByAsc("test_project")
				.orderByDesc("create_time");
		IPage<DfOrtChangeSpecification> list = dfOrtChangeSpecificationService.page(pages, qw);
		return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
	}


	@ApiOperation("新增")
	@PostMapping(value = "/add")
	public Object add(DfOrtChangeSpecification dfOrtChangeSpecification) {
		if (dfOrtChangeSpecificationService.save(dfOrtChangeSpecification)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("修改")
	@GetMapping(value = "/edit")
	public Object edit(DfOrtChangeSpecification dfOrtChangeSpecification) {
		if (dfOrtChangeSpecificationService.updateById(dfOrtChangeSpecification)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object delete(DfOrtChangeSpecification dfOrtChangeSpecification) {
		if (dfOrtChangeSpecificationService.removeById(dfOrtChangeSpecification)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}

	@ApiOperation("导出excel表")
	@RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
	public void getExcel(String model, HttpServletResponse response, HttpServletRequest request) {
		try {
			QueryWrapper<DfOrtChangeSpecification> qw = new QueryWrapper<>();
			if (StringUtils.isNotEmpty(model)){
				qw.eq("model",model);
			}
			qw.orderByAsc("test_name")
					.orderByAsc("test_process_amount")
					.orderByAsc("test_project");
			ArrayList titleKeyList = new ArrayList<>();
			titleKeyList.add("model");
			titleKeyList.add("test_name");
			titleKeyList.add("test_process_amount");
			titleKeyList.add("test_project");
			titleKeyList.add("predicate");
			titleKeyList.add("stage");
			titleKeyList.add("standard");
			Map titleMap = new HashMap<>();
			titleMap.put("model","型号");
			titleMap.put("test_name","测试名称");
			titleMap.put("test_process_amount","工序测试频率/数量");
			titleMap.put("test_project","测试项目");
			titleMap.put("predicate","判定");
			titleMap.put("stage","阶段");
			titleMap.put("standard","规格");
			List<Map<String,Object>> datas= dfOrtChangeSpecificationService.listByExport(qw);
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
			File fl = new File(env.getProperty("uploadPath") + "dfOrtChangeSpecification/");
			if (!fl.exists()) {
				fl.mkdirs();
			}
			File f = new File(env.getProperty("uploadPath") + "dfOrtChangeSpecification/" + file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				System.out.println(i);
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = new File(env.getProperty("uploadPath") + "dfOrtChangeSpecification/" + fileName);
				i++;
			}

			CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "dfOrtChangeSpecification",
					env.getProperty("uploadPath") + "dfOrtChangeSpecification/");

			try {
				ImportExcelResult ter = dfOrtChangeSpecificationService.importOrder(env.getProperty("uploadPath") + "dfOrtChangeSpecification/" + fileName, file);
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
		dfOrtChangeSpecificationService.exportModel(response, "ORT规格");
	}

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfOrtChangeSpecification dfOrtChangeSpecification) {
		try {
			dfOrtChangeSpecification.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (file.isEmpty()) {
				if (dfOrtChangeSpecificationMapper.insert(dfOrtChangeSpecification) > 0){
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
					,"/文件变更履历/ORT规格变更/文件/"
					,file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						,"/文件变更履历/ORT规格变更/文件/"
						,fileName);
				i++;
			}
			dfOrtChangeSpecification.setRealPath("文件变更履历/ORT规格变更/文件/" + f.getName());
			int id = dfOrtChangeSpecificationMapper.insert(dfOrtChangeSpecification);
			//调PDA
			dfFlowDataService.createFlowDataFileUpdate(f.getName(),"ORT规格变更", id);
			//df_liable_man
			DfLiableMan dfLiableMan = new DfLiableMan();
			//添加责任人记录
//			dfLiableMan.setFactoryName("厂1")
//					.setProcessName("CNC0,CNC1")
//					.setProblemLevel("2")
//					.setDayOrNight("白班")
//					.setLiableManName("李华")
//					.setLiableManCode("admin")
//					.setType("ORT规格变更")
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
