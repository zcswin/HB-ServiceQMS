package com.ww.boengongye.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ww.boengongye.entity.DfTrainingDocumentManagement;
import com.ww.boengongye.entity.Menu;
import com.ww.boengongye.entity.User;
import com.ww.boengongye.service.DfTrainingDocumentManagementService;
import com.ww.boengongye.service.MenuService;
import com.ww.boengongye.service.UserService;
import com.ww.boengongye.utils.CommunalUtils;
import com.ww.boengongye.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 文件培训文件管理 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-09-27
 */
@RestController
@RequestMapping("/dfTrainingDocumentManagement")
@CrossOrigin
@Api(tags = "文件培训文件管理")
public class DfTrainingDocumentManagementController {

	@Autowired
	private DfTrainingDocumentManagementService dfTrainingDocumentManagementService;
	@Autowired
	private Environment env;
	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;

	@Transactional
	@ApiOperation("上传")
	@PostMapping(value = "/uploadTableAndFile")
	public Object upload(@RequestParam String userAccount,@RequestParam(value = "file", required = true) MultipartFile file,String usercode) {
		try {
			QueryWrapper<User>uw=new QueryWrapper<>();
			uw.eq("name",userAccount);
			uw.last("limit 1");
			User user=userService.getOne(uw);
			if(null!=user) {
				QueryWrapper<Menu> mqw = new QueryWrapper<>();
//            mqw.inSql("id","select parent_id from menu where id in(  select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"'))");

				mqw.and(wrapper -> wrapper.inSql("id", "select id  from menu " +
						"where id in(select menu_id FROM station_relation_menu " +
						"WHERE station_id IN ( SELECT station_id FROM user_relation_station " +
						"WHERE user_id = '" + user.getId() + "' )) and parent_id = 0")
				);


				mqw.eq("is_use", "1");
				mqw.eq("menu_type", "管理后台");
				mqw.orderByAsc("sort");
				List<Menu> menus = menuService.list(mqw);
				Menu menu = menus.stream().filter(item -> "培训文件管理".equals(item.getName())).findAny().orElse(null);
				if (menu == null){
					return new Result(500, "没有操作权限");
				}
			}
			if (file.isEmpty()) {
				return new Result(500, "上传失败，请选择文件");
			}
			String fileName = file.getOriginalFilename();
			DfTrainingDocumentManagement dfTrainingDocumentManagement = new DfTrainingDocumentManagement();
			dfTrainingDocumentManagement.setFileName(fileName);

			InputStream ins = null;
			try {
				ins = file.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
					, "/文件培训文件管理/文件/"
					, file.getOriginalFilename());
			String backupFileName = f.getName();
			int i = 1;
			while (f.exists()) {
				String[] list = backupFileName.split("\\.");
				fileName = list[0] + "(" + i + ")." + list[1];
				f = CommunalUtils.generateFileByPathAndFileName(env.getProperty("uploadPath")
						, "/文件培训文件管理/文件/"
						, fileName);
				i++;
			}
			DfTrainingDocumentManagement document = new DfTrainingDocumentManagement();
			document.setFileName(f.getName());
			document.setPath("/文件培训文件管理/文件/" + f.getName());
			CommunalUtils.inputStreamToFile2(ins, f);
			if (dfTrainingDocumentManagementService.save(document)) {
				return new Result(200, "上传成功");
			}
			return new Result(200, "上传成功");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return new Result(500, "接口异常");
	}

	@ApiOperation("查询")
	@GetMapping(value = "/query")
	public Object download(int page,int limit,String startTime,String endTime,String fileName) {
		Page<DfTrainingDocumentManagement> pages = new Page<DfTrainingDocumentManagement>(page, limit);
		QueryWrapper<DfTrainingDocumentManagement> ew = new QueryWrapper<>();
		ew.gt(StringUtils.isNotEmpty(startTime),"create_time",startTime)
			.lt(StringUtils.isNotEmpty(endTime),"create_time",endTime)
			.like(StringUtils.isNotEmpty(fileName),"file_name",fileName )
			.orderByDesc("create_time");
		IPage<DfTrainingDocumentManagement> list = dfTrainingDocumentManagementService.page(pages, ew);
		return new Result(200, "查询成功",list.getRecords(),(int)list.getTotal());
	}

	@ApiOperation("删除")
	@GetMapping(value = "/delete")
	public Object download(@RequestParam String userAccount,@RequestParam(value = "id", required = true) String id) {
		QueryWrapper<User>uw=new QueryWrapper<>();
		uw.eq("name",userAccount);
		uw.last("limit 1");
		User user=userService.getOne(uw);
		if(null!=user) {
			QueryWrapper<Menu> mqw = new QueryWrapper<>();
//            mqw.inSql("id","select parent_id from menu where id in(  select menu_id from station_relation_menu where station_id in(select station_id from user_relation_station where user_id='"+user.getId()+"'))");

			mqw.and(wrapper -> wrapper.inSql("id", "select id  from menu " +
					"where id in(select menu_id FROM station_relation_menu " +
					"WHERE station_id IN ( SELECT station_id FROM user_relation_station " +
					"WHERE user_id = '" + user.getId() + "' )) and parent_id = 0")
			);


			mqw.eq("is_use", "1");
			mqw.eq("menu_type", "管理后台");
			mqw.orderByAsc("sort");
			List<Menu> menus = menuService.list(mqw);
			Menu menu = menus.stream().filter(item -> "培训文件管理".equals(item.getName())).findAny().orElse(null);
			if (menu == null){
				return new Result(500, "没有操作权限");
			}
		}
		if (dfTrainingDocumentManagementService.removeById(id)) {
			DfTrainingDocumentManagement one = dfTrainingDocumentManagementService.getById(id);
			File uploadPath = new File(env.getProperty("uploadPath") + one.getPath());
			if (uploadPath.delete()){
				return new Result(200, "删除成功");
			}else {
				return new Result(500, "找不到此文件");
			}
		}
		return new Result(500, "删除失败");
	}

	@ApiOperation("下载")
	@GetMapping("/downLoad")
	public Object downLoadExcelMould(@RequestParam String userAccount,HttpServletResponse response, @RequestParam(required = true) String id) throws IOException {
		QueryWrapper<User>uw=new QueryWrapper<>();
		uw.eq("name",userAccount);
		uw.last("limit 1");
		User user=userService.getOne(uw);
		//根据id获取相对路径
		DfTrainingDocumentManagement byId = dfTrainingDocumentManagementService.getById(id);
		String path = env.getProperty("uploadPath") + byId.getPath();
		// 读到流中
		InputStream inputStream = new FileInputStream(path);// 文件的存放路径
		response.reset();
		response.setContentType("application/octet-stream");
		String filename = new File(path).getName();
		response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
		ServletOutputStream outputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int len;
		//从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
		while ((len = inputStream.read(b)) > 0) {
			outputStream.write(b, 0, len);
		}
		inputStream.close();
		return null;
	}
}
