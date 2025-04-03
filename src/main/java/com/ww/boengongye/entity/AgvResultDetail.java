package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;
@Data
public class AgvResultDetail {

  public String   deviceCode;
  public String   payLoad;
  public List<Double> devicePostionRec;
  public String  devicePosition;
  public String   battery;
  public String   deviceName;
  public Integer   deviceStatus;
  public String   state;
  public Integer   oritation;
  public Integer   speed;


}
