package com.ww.boengongye.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.ww.boengongye.entity.DfUserMachineInfo;
import com.ww.boengongye.entity.HttpParamTime;
import com.ww.boengongye.entity.UserMachineInfos;
import com.ww.boengongye.service.DfUserMachineInfoService;
import com.ww.boengongye.utils.HttpUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 员工机台绑定 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2023-10-19
 */
@RestController
@RequestMapping("/dfUserMachineInfo")
@CrossOrigin
@Api(tags = "员工机台绑定")
public class DfUserMachineInfoController {
	@Autowired
	private DfUserMachineInfoService dfUserMachineInfoService;
	@Autowired
	private Environment env;

//	@Scheduled(cron = "0 0/30 * * * ?")
//	@GetMapping("/getUserMachineInfos")
	public void getUserMachineInfos() throws ParseException {

//		System.out.println("检测外观机台超时送检");
//		System.out.println(env.getProperty("RFIDWaitMaterialURL"));
		Map<String, String> headers = new HashMap<>();
		//请求RFID
		HttpParamTime param2 = new HttpParamTime(120);
		UserMachineInfos infos = new Gson().fromJson(HttpUtil.postJson(env.getProperty("RFIDUserMachineInfosURL"), null,
				headers, JSONObject.toJSONString(param2), false), UserMachineInfos.class);
		ArrayList<DfUserMachineInfo> list = new ArrayList<>();
		for (DfUserMachineInfo data : infos.getData()) {
			QueryWrapper<DfUserMachineInfo> ew = new QueryWrapper<>();
			ew.eq(StringUtils.isNotEmpty(data.getUserName()),"user_name", data.getUserName())
				.orderByDesc("login_time")
				.last("limit 1");
			DfUserMachineInfo one = dfUserMachineInfoService.getOne(ew);
			//没找到或人员machineCode改变或 两次间隔时间超过12小时那么保存一条记录
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dataTime = df.parse(data.getLoginTime());
			if (one == null || !data.getMachineCode().equals(one.getMachineCode())
					|| Math.abs(dataTime.getTime() - df.parse(one.getLoginTime()).getTime()) / (1000*3600) > 12){
				list.add(data);
			}
		}
		dfUserMachineInfoService.saveBatch(list);
	}
}
