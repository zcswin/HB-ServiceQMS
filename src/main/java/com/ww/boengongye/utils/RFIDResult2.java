package com.ww.boengongye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ww.boengongye.entity.RFIDMachineCodeList;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFIDResult2 {
	private int code;
	private String message;

	private List<RFIDRecord> data;
}
