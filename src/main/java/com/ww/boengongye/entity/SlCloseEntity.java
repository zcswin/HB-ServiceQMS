package com.ww.boengongye.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liwei
 * @create 2023-09-19 11:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlCloseEntity implements Serializable {

	public static final long serialVersionUID = 1L;

	private String status;

	private String dataType;

	private String questionName;

	private String clazz;

	private String factory;

	private String process;

	private String model;

	private String lineBody;

	private String userName;

	private String checkTime;

	private String endTime;

	private String fa;

	private String ca;

	private String responsible;

	private String remark;

	private String color;

	private String numberOfRepetitions;
}
