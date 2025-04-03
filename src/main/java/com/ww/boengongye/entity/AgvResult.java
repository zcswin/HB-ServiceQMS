package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;
@Data
public class AgvResult {

  public Integer code;
  public String desc;
  public List<AgvResultDetail> data;

  public AgvResult(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
