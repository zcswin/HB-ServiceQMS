package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfReliabilityDetails;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.mapper.DfReliabilityDetailsMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfReliabilityDetailsService;
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
 * ORT可靠性明细 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
@Controller
@RequestMapping("/dfReliabilityDetails")
@ResponseBody
@CrossOrigin
@Api(tags = "ORT可靠性明细")
public class DfReliabilityDetailsController {

	@Autowired
	private DfReliabilityDetailsService dfReliabilityDetailsService;

	@Autowired
	private DfReliabilityDetailsMapper dfReliabilityDetailsMapper;
	@Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfLiableManMapper dfLiableManMapper;

	@Autowired
	private Environment env;

	@Autowired
	private ExportExcelUtil exportExcelUtil;

	private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);

	@ApiOperation("工厂下拉列表")
	@GetMapping("/factoryList")
	public Result factoryList(){
		QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
		qw.select("factory")
				.groupBy("factory");
		qw.isNotNull("factory");
		return new Result(200, "查询成功", dfReliabilityDetailsService.list(qw));
	}

	@ApiOperation("型号下拉列表")
	@GetMapping("/modelList")
	public Result modelList(){
		QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
		qw.select("model")
				.groupBy("model");
		qw.isNotNull("model");
		return new Result(200, "查询成功", dfReliabilityDetailsService.list(qw));
	}
	@ApiOperation("工序下拉列表")
	@GetMapping("/processList")
	public Result processList(){
		QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
		qw.select("process")
				.groupBy("process");
		qw.isNotNull("process");
		return new Result(200, "查询成功", dfReliabilityDetailsService.list(qw));
	}

	@ApiOperation("测试项目下拉列表")
	@GetMapping("/testProjectList")
	public Result testProjectList(){
		QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
		qw.select("test_project")
				.groupBy("test_project");
		qw.isNotNull("test_project");
		return new Result(200, "查询成功", dfReliabilityDetailsService.list(qw));
	}



	@ApiOperation("分页查询")
	@GetMapping(value = "/listAll")
	public Object listAll(Integer page,Integer limit,String factory,String model,String process,String testProject) {
		Page<DfReliabilityDetails> pages = new Page<DfReliabilityDetails>(page, limit);
		QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(factory)){
			qw.eq("factory",factory);
		}
		if (StringUtils.isNotEmpty(model)){
			qw.eq("model",model);
		}
		if (StringUtils.isNotEmpty(process)){
			qw.eq("process",process);
		}
		if (StringUtils.isNotEmpty(testProject)){
			qw.eq("test_project",testProject);
		}
		qw.orderByAsc("factory")
				.orderByAsc("model")
				.orderByAsc("process")
				.orderByDesc("create_time");
		IPage<DfReliabilityDetails> list = dfReliabilityDetailsService.page(pages, qw);
		return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
	}


	@ApiOperation("新增")
	@PostMapping(value = "/add")
	public Object add(DfReliabilityDetails dfReliabilityDetails) {
		if (dfReliabilityDetailsService.save(dfReliabilityDetails)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("修改")
	@GetMapping(value = "/edit")
	public Object edit(DfReliabilityDetails dfReliabilityDetails) {
		if (dfReliabilityDetailsService.updateById(dfReliabilityDetails)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object delete(DfReliabilityDetails dfReliabilityDetails) {
		if (dfReliabilityDetailsService.removeById(dfReliabilityDetails)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}

	@ApiOperation("导出excel表")
	@RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
	public void getExcel(String factory,String model,String process,String testProject, HttpServletResponse response, HttpServletRequest request) {
		try {
			QueryWrapper<DfReliabilityDetails> qw = new QueryWrapper<>();
			if (StringUtils.isNotEmpty(factory)){
				qw.eq("factory",factory);
			}
			if (StringUtils.isNotEmpty(model)){
				qw.eq("model",model);
			}
			if (StringUtils.isNotEmpty(process)){
				qw.eq("process",process);
			}
			if (StringUtils.isNotEmpty(testProject)){
				qw.eq("testProject",testProject);
			}
			qw.orderByAsc("factory")
					.orderByAsc("model")
					.orderByAsc("process")
					.orderByAsc("test_project");
			ArrayList titleKeyList = new ArrayList<>();
			titleKeyList.add("factory");
			titleKeyList.add("model");
			titleKeyList.add("process");
			titleKeyList.add("test_project");
			titleKeyList.add("test_standard");
			titleKeyList.add("test_device");
			titleKeyList.add("test_amount");
			titleKeyList.add("fai_rate");
			titleKeyList.add("fai_amount");
			titleKeyList.add("fai_unit");
			titleKeyList.add("ipqc_rate");
			titleKeyList.add("ipqc_amount");
			titleKeyList.add("ipqc_unit");
			Map titleMap = new HashMap<>();
			titleMap.put("factory","工厂");
			titleMap.put("model","型号");
			titleMap.put("process","工序");
			titleMap.put("test_project","测试项目");
			titleMap.put("test_standard","测试标准");
			titleMap.put("test_device","测试设备");
			titleMap.put("test_amount","测试频率/数量");
			titleMap.put("fai_rate","FAI频率");
			titleMap.put("fai_amount","FAI数量");
			titleMap.put("fai_unit","FAI单位");
			titleMap.put("ipqc_rate","IPQC频率");
			titleMap.put("ipqc_amount","IPQC数量");
			titleMap.put("ipqc_unit","IPQC单位");
			List<Map<String,Object>> datas= dfReliabilityDetailsService.listByExport(qw);
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
			File fl = new File(env.getProperty("uploadPath") + "dfReliabilityDetails/");
			if (!fl.exists()) {
				fl.mkdirs();
			}
			File f = new File(env.getProperty("uploadPath") + "dfReliabilityDetails/" + file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				System.out.println(i);
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = new File(env.getProperty("uploadPath") + "dfReliabilityDetails/" + fileName);
				i++;
			}

			CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "dfReliabilityDetails",
					env.getProperty("uploadPath") + "dfReliabilityDetails/");

			try {
				ImportExcelResult ter = dfReliabilityDetailsService.importOrder(env.getProperty("uploadPath") + "dfReliabilityDetails/" + fileName, file);
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
		dfReliabilityDetailsService.exportModel(response, "ORT可靠性");
	}

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfReliabilityDetails dfReliabilityDetails) {
		try {
			dfReliabilityDetails.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (file.isEmpty()) {
				if (dfReliabilityDetailsMapper.insert(dfReliabilityDetails) > 0){
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
					,"/文件变更履历/ORT可靠性明细/文件/"
					,file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						,"/文件变更履历/ORT可靠性明细/文件/"
						,fileName);
				i++;
			}
			dfReliabilityDetails.setRealPath("文件变更履历/ORT可靠性明细/文件/" + f.getName());
			int id = dfReliabilityDetailsMapper.insert(dfReliabilityDetails);
			//调PDA
			dfFlowDataService.createFlowDataFileUpdate(f.getName(),"ORT可靠性明细", id);
			//df_liable_man
			DfLiableMan dfLiableMan = new DfLiableMan();
			//添加责任人记录
//			dfLiableMan.setFactoryName("厂1")
//					.setProcessName("CNC3")
//					.setProblemLevel("2")
//					.setDayOrNight("白班")
//					.setLiableManName("雷德喜")
//					.setLiableManCode("admin")
//					.setType("ORT可靠性明细")
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
