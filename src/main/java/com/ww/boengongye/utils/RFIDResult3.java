package com.ww.boengongye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ww.boengongye.entity.DfRiskProduct;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFIDResult3 {
	private int code;
	private String message;

	private RFIDResult4 data;
}

