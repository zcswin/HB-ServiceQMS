package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfProgrammingChange;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.mapper.DfProgrammingChangeMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfProgrammingChangeService;
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
 * @since 2023-08-22
 */
@Controller
@RequestMapping("/dfProgrammingChange")
@ResponseBody
@CrossOrigin
@Api(tags = "程序变更")
public class DfProgrammingChangeController {

	@Autowired
	private DfProgrammingChangeService dfProgrammingChangeService;

	@Autowired
	private Environment env;

	@Autowired
	private ExportExcelUtil exportExcelUtil;
	@Autowired
	private DfProgrammingChangeMapper dfProgrammingChangeMapper;
	@Autowired
	private DfFlowDataService dfFlowDataService;
	@Autowired
	private DfLiableManMapper dfLiableManMapper;

	private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);

	@ApiOperation("型号下拉列表")
	@GetMapping("/modelList")
	public Result modelList(){
		QueryWrapper<DfProgrammingChange> qw = new QueryWrapper<>();
		qw.select("model")
				.groupBy("model");
		qw.isNotNull("model");
		return new Result(200, "查询成功", dfProgrammingChangeService.list(qw));
	}


	@ApiOperation("分页查询")
	@GetMapping(value = "/listAll")
	public Object listAll(Integer page,Integer limit,String model) {
		Page<DfProgrammingChange> pages = new Page<DfProgrammingChange>(page, limit);
		QueryWrapper<DfProgrammingChange> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(model)){
			qw.eq("model",model);
		}
		qw.orderByDesc("create_time");
		IPage<DfProgrammingChange> list = dfProgrammingChangeService.page(pages, qw);
		return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
	}


	@ApiOperation("新增")
	@PostMapping(value = "/add")
	public Object add(@RequestBody DfProgrammingChange dfProgrammingChange) {
		if (dfProgrammingChangeService.save(dfProgrammingChange)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("修改")
	@GetMapping(value = "/edit")
	public Object edit(DfProgrammingChange dfProgrammingChange) {
		if (dfProgrammingChangeService.updateById(dfProgrammingChange)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object delete(DfProgrammingChange dfProgrammingChange) {
		if (dfProgrammingChangeService.removeById(dfProgrammingChange)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}

	@ApiOperation("导出excel表")
	@RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
	public void getExcel(String model, HttpServletResponse response, HttpServletRequest request) {
		try {
			QueryWrapper<DfProgrammingChange> qw = new QueryWrapper<>();
			if (StringUtils.isNotEmpty(model)){
				qw.eq("model", model);
			}
			qw.orderByAsc("model");
			ArrayList titleKeyList = new ArrayList<>();
			titleKeyList.add("model");
			titleKeyList.add("regist_time");
			titleKeyList.add("test_programming_version");
			titleKeyList.add("programming_change_time");
			titleKeyList.add("size_drawing_version");
			titleKeyList.add("color_drawing_version");
			titleKeyList.add("size_drawing_area");
			titleKeyList.add("size_change_time");
			titleKeyList.add("programming_change_reason");
			titleKeyList.add("programming_change_content");
			titleKeyList.add("programming_import_time");
			titleKeyList.add("confirmer");
			Map titleMap = new HashMap<>();
			titleMap.put("model","型号");
			titleMap.put("regist_time","登记时间");
			titleMap.put("test_programming_version","测试程序版本");
			titleMap.put("programming_change_time","程序变更时间");
			titleMap.put("size_drawing_version","对应尺寸图纸版本");
			titleMap.put("color_drawing_version","对应颜色图纸版本");
			titleMap.put("size_drawing_area","尺寸图纸适用范围");
			titleMap.put("size_change_time","尺寸图纸变更时间");
			titleMap.put("programming_change_reason","程序变更原因");
			titleMap.put("programming_change_content","程序变更内容");
			titleMap.put("programming_import_time","程序导入时间");
			titleMap.put("confirmer","确认人");
			List<Map<String,Object>> datas= dfProgrammingChangeService.listByExport(qw);
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
			File fl = new File(env.getProperty("uploadPath") + "programmingChange/");
			if (!fl.exists()) {
				fl.mkdirs();
			}
			File f = new File(env.getProperty("uploadPath") + "programmingChange/" + file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				System.out.println(i);
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = new File(env.getProperty("uploadPath") + "programmingChange/" + fileName);
				i++;
			}

			CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "programmingChange",
					env.getProperty("uploadPath") + "programmingChange/");

			try {
				ImportExcelResult ter = dfProgrammingChangeService.importOrder(env.getProperty("uploadPath") + "dfReliabilityDetails/" + fileName, file);
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
		dfProgrammingChangeService.exportModel(response, "程序标准");
	}

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfProgrammingChange dfProgrammingChange) {
		try {
			dfProgrammingChange.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (file.isEmpty()) {
				if (dfProgrammingChangeMapper.insert(dfProgrammingChange) > 0){
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
					,"/文件变更履历/程序变更/文件/"
					,file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f =CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						,"/文件变更履历/程序变更/文件/"
						,fileName);
				i++;
			}
			dfProgrammingChange.setRealPath("文件变更履历/程序变更/文件/" + f.getName());
			int id = dfProgrammingChangeMapper.insert(dfProgrammingChange);
			//调PDA
			dfFlowDataService.createFlowDataFileUpdate(f.getName(),"程序变更", id);
			//df_liable_man
			DfLiableMan dfLiableMan = new DfLiableMan();
			//添加责任人记录
//			dfLiableMan.setFactoryName("厂1")
//					.setProcessName("CNC0,CNC1")
//					.setProblemLevel("2")
//					.setDayOrNight("白班")
//					.setLiableManName("李华")
//					.setLiableManCode("admin")
//					.setType("程序变更")
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
