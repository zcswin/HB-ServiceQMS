package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;

/**
 * @author liwei
 * @create 2023-10-19 9:34
 */
@Data
public class UserMachineInfos {

	private String code;

	private String message;

	private List<DfUserMachineInfo> data;
}
