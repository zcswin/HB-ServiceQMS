package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfLiableMan;
import com.ww.boengongye.entity.DfQaBom;
import com.ww.boengongye.entity.ImportExcelResult;
import com.ww.boengongye.mapper.DfLiableManMapper;
import com.ww.boengongye.mapper.DfQaBomMapper;
import com.ww.boengongye.service.DfFlowDataService;
import com.ww.boengongye.service.DfQaBomService;
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
 * QA-BOM 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Controller
@RequestMapping("/dfQaBom")
@ResponseBody
@CrossOrigin
@Api(tags = "QA-BOM")
public class DfQaBomController {

	@Autowired
	private DfQaBomService dfQaBomService;

	@Autowired
	private DfQaBomMapper dfQaBomMapper;

	@Autowired
	private DfFlowDataService dfFlowDataService;

	@Autowired
	private DfLiableManMapper dfLiableManMapper;

	@Autowired
	private Environment env;

	@Autowired
	private ExportExcelUtil exportExcelUtil;

	private static final Logger logger = LoggerFactory.getLogger(DfQcpStandardController.class);


	@ApiOperation("项目型号下拉列表")
	@GetMapping("/projectModellList")
	public Result projectModellList(){
		QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
		qw.select("project_model")
				.groupBy("project_model");
		qw.isNotNull("project_model");
		return new Result(200, "查询成功", dfQaBomService.list(qw));
	}

	@ApiOperation("阶段下拉列表")
	@GetMapping("/stageList")
	public Result stageList(){
		QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
		qw.select("stage")
				.groupBy("stage");
		qw.isNotNull("stage");
		return new Result(200, "查询成功", dfQaBomService.list(qw));
	}

	@ApiOperation("config下拉列表")
	@GetMapping("/modelList")
	public Result configList(){
		QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
		qw.select("config")
				.groupBy("config");
		qw.isNotNull("config");
		return new Result(200, "查询成功", dfQaBomService.list(qw));
	}

	@ApiOperation("确认状态下拉列表")
	@GetMapping("/confirmStatuslList")
	public Result modelList(){
		QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
		qw.select("confirm_status")
				.groupBy("confirm_status");
		qw.isNotNull("confirm_status");
		return new Result(200, "查询成功", dfQaBomService.list(qw));
	}


	@ApiOperation("分页查询")
	@GetMapping(value = "/listAll")
	public Object listAll(Integer page,Integer limit,String projectModel,String stage,String config,String confirmStatus) {
		Page<DfQaBom> pages = new Page<DfQaBom>(page, limit);
		QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(projectModel)){
			qw.eq("project_model",projectModel);
		}
		if (StringUtils.isNotEmpty(stage)){
			qw.eq("stage",stage);
		}
		if (StringUtils.isNotEmpty(config)){
			qw.eq("config",config);
		}
		if (StringUtils.isNotEmpty(confirmStatus)){
			qw.eq("confirm_status",confirmStatus);
		}
		qw.orderByDesc("create_time");
		IPage<DfQaBom> list = dfQaBomService.page(pages, qw);
		return new Result(0, "查询成功", list.getRecords(),(int)list.getTotal());
	}


	@ApiOperation("新增")
	@PostMapping(value = "/add")
	public Object add(DfQaBom dfQaBom) {
		if (dfQaBomService.save(dfQaBom)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("修改")
	@GetMapping(value = "/edit")
	public Object edit(DfQaBom dfQaBom) {
		if (dfQaBomService.updateById(dfQaBom)) {
			return new Result(200, "保存成功");
		} else {
			return new Result(500, "保存失败");
		}
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object delete(DfQaBom dfQaBom) {
		if (dfQaBomService.removeById(dfQaBom)) {
			return new Result(200, "删除成功");
		} else {
			return new Result(500, "删除失败");
		}
	}

	@ApiOperation("导出excel表")
	@RequestMapping(value = "/getExcel", method = { RequestMethod.GET })
	public void getExcel(String projectModel,String stage,String config,String confirmStatus, HttpServletResponse response, HttpServletRequest request) {
		try {
			QueryWrapper<DfQaBom> qw = new QueryWrapper<>();
			if (StringUtils.isNotEmpty(projectModel)){
				qw.eq("project_model",projectModel);
			}
			if (StringUtils.isNotEmpty(stage)){
				qw.eq("stage",stage);
			}
			if (StringUtils.isNotEmpty(config)){
				qw.eq("config",config);
			}
			if (StringUtils.isNotEmpty(confirmStatus)){
				qw.eq("confirm_status",confirmStatus);
			}
			qw.orderByAsc("flow_id");
			ArrayList titleKeyList = new ArrayList<>();
			titleKeyList.add("flow_id");
			titleKeyList.add("project_model");
			titleKeyList.add("stage");
			titleKeyList.add("config");
			titleKeyList.add("color");
			titleKeyList.add("qa_bom");
			titleKeyList.add("confirm_status");
			titleKeyList.add("lower_drawings");
			titleKeyList.add("convert_drawings");
			titleKeyList.add("outer_dfm");
			titleKeyList.add("inner_dfm");
			titleKeyList.add("outer_qcp");
			titleKeyList.add("inner_qcp_size");
			titleKeyList.add("inner_qcp_appearance");
			titleKeyList.add("material_bom");
			titleKeyList.add("ers");
			titleKeyList.add("process_mil");
			titleKeyList.add("factory_check");
			titleKeyList.add("manufacturing_order");
			titleKeyList.add("first_report");
			titleKeyList.add("first_confirm");
			Map titleMap = new HashMap<>();
			titleMap.put("flow_id","流程编号");
			titleMap.put("project_model","项目型号");
			titleMap.put("stage","阶段");
			titleMap.put("config","config");
			titleMap.put("color","颜色");
			titleMap.put("qa_bom","QA-BOM");
			titleMap.put("confirm_status","确认状态");
			titleMap.put("lower_drawings","图纸下放(PM)");
			titleMap.put("convert_drawings","图纸转换(绘图员)");
			titleMap.put("outer_dfm","外部DFM(PIE)");
			titleMap.put("inner_dfm","内部DFM(绘图员)");
			titleMap.put("outer_qcp","外部QCP(CQE)");
			titleMap.put("inner_qcp_size","内部QCP-尺寸(测量组)");
			titleMap.put("inner_qcp_appearance","内部QCP-外观(IQPC)");
			titleMap.put("material_bom","物料BOM(IE)");
			titleMap.put("ers","ERS(CQE)");
			titleMap.put("process_mil","过程MIL(PIE)");
			titleMap.put("factory_check","工厂审核(工厂)");
			titleMap.put("manufacturing_order","生产单号(生产部)");
			titleMap.put("first_report","首件报告(工厂测量)");
			titleMap.put("first_confirm","首件确认(中央IPQC)");
			List<Map<String,Object>> datas= dfQaBomService.listByExport(qw);
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
			File fl = new File(env.getProperty("uploadPath") + "qaBom/");
			if (!fl.exists()) {
				fl.mkdirs();
			}
			File f = new File(env.getProperty("uploadPath") + "qaBom/" + file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				System.out.println(i);
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = new File(env.getProperty("uploadPath") + "qaBom/" + fileName);
				i++;
			}

			CommunalUtils.inputStreamToFile(ins, f, env.getProperty("uploadPath"), env.getProperty("uploadPath") + "qaBom",
					env.getProperty("uploadPath") + "qaBom/");

			try {
				ImportExcelResult ter = dfQaBomService.importOrder(env.getProperty("uploadPath") + "qaBom/" + fileName, file);
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
		dfQaBomService.exportModel(response, "QA_BOM");
	}

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile file , DfQaBom dfQaBom) {
		try {
			dfQaBom.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (file.isEmpty()) {
				if (dfQaBomMapper.insert(dfQaBom) > 0){
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
					,"/文件变更履历/QA-BOM/文件/"
					,file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						,"/文件变更履历/QA-BOM/文件/"
						,fileName);
				i++;
			}
			dfQaBom.setRealPath("文件变更履历/QA-BOM/文件/" + f.getName());
			int id = dfQaBomMapper.insert(dfQaBom);
			//调PDA
			dfFlowDataService.createFlowDataFileUpdate(f.getName(),"QA-BOM", id);
			//df_liable_man
			DfLiableMan dfLiableMan = new DfLiableMan();
			//添加责任人记录
//			dfLiableMan.setFactoryName("厂1")
//					.setProcessName("CNC0,CNC1")
//					.setProblemLevel("2")
//					.setDayOrNight("白班")
//					.setLiableManName("李四")
//					.setLiableManCode("admin")
//					.setType("QA-BOM")
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
