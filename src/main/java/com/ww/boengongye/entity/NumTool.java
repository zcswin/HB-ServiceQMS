package com.ww.boengongye.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liwei
 * @create 2023-09-20 16:48
 */
@Data
public class NumTool implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nNumTool;

	private Integer toolCutNum;
}
