package com.ww.boengongye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result2<T> {
	private int code;
	private String msg;
	private T data;


	public Result2() {
	}

	public Result2(int code, String msg) {
		this(code, msg, null);
	}

	public Result2(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}









	public Result2(Errors errors) {
		StringBuilder msg = new StringBuilder();
		errors.getFieldErrors().forEach((ObjectError error) -> {
			msg.append(error.getDefaultMessage() + "\n");
		});

		this.code = 0;
		this.msg = msg.toString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}



}
