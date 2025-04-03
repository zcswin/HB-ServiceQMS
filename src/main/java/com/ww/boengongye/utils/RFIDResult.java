package com.ww.boengongye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ww.boengongye.entity.AppearanceStatusCount;
import com.ww.boengongye.entity.RFIDMachineCodeList;
import com.ww.boengongye.entity.SizeStatusCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFIDResult {
	private int code;
	private String message;
	private RFIDMachineCodeList data;

	public RFIDResult() {
	}

	public RFIDResult(int code, String message, RFIDMachineCodeList data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public RFIDMachineCodeList getData() {
		return data;
	}

	public void setData(RFIDMachineCodeList data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RFIDResult{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
