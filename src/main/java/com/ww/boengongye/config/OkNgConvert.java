package com.ww.boengongye.config;

/**
 * @author liwei
 * @create 2023-09-04 1:32
 */

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * AOI-piece OK/NG 转换器
 */
public class OkNgConvert implements Converter<String> {

	@Override
	public Class supportJavaTypeKey() {
		//实体中对象属性类型
		return String.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		//Excel中对应的cellData属性类型
		return CellDataTypeEnum.STRING;
	}

	@Override
	public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		//Cell中读取数据
		String status = cellData.getStringValue();
		if ("OK".equalsIgnoreCase(status)){
			return "1";
		}else if ("NG".equalsIgnoreCase(status)){
			return "0";
		}
		return null;
	}

	@Override
	public CellData convertToExcelData(String s, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
		if (s == null) {
			return new CellData<>("");
		} else if ("1".equals(s)) {
			return new CellData<>("NG");
		} else if ("0".equals(s)) {
			return new CellData<>("OK");
		}
		return new CellData<>("");
	}
}
