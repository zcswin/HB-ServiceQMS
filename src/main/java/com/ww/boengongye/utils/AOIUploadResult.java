package com.ww.boengongye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ww.boengongye.entity.AppearanceStatusCount;
import com.ww.boengongye.entity.SizeStatusCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AOIUploadResult<T> {
	private String status_code;
	private String message;


}
