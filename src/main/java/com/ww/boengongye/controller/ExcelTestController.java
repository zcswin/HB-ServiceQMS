package com.ww.boengongye.controller;

import com.ww.boengongye.utils.ExcelImportUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author liwei
 * @create 2023-09-09 11:03
 */
@RestController
@RequestMapping("/excel")
@CrossOrigin
public class ExcelTestController {

	@RequestMapping(value = "/uploadExcel", method = {RequestMethod.POST})
	@ResponseBody
	public void uploadExcel(@RequestParam(value = "file", required = false) MultipartFile file )throws Exception {

		String fileName = file.getOriginalFilename();
		System.out.println("fileName = " + fileName);
		ExcelImportUtil importUtil = new ExcelImportUtil(file);
		List<Map<String, String>> maps = importUtil.readExcelContent();
		String[] head = importUtil.readExcelTitle();
		System.out.println(head);
	}

}
