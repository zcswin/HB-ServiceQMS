package com.ww.boengongye.controller;


import com.ww.boengongye.service.DfAoiDefectSmallClassService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * AOI缺陷小类表 前端控制器
 * </p>
 *
 * @author guangyao
 * @since 2023-10-18
 */
@RestController
@RequestMapping("/dfAoiDefectSmallClass")
@CrossOrigin
@Api(tags = "AOI缺陷小类表")
public class DfAoiDefectSmallClassController {

	@Autowired
	private DfAoiDefectSmallClassService dfAoiDefectSmallClassService;
}
