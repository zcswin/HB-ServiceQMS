package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-08-28
 */
@Data
public class AgvInfo extends Model<AgvInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备序列号
     */
    private String deviceCode;

    /**
     * 设备负载状态
     */
    private String payLoad;

    /**
     * 序列号
     */
    private String devicePostionRec;

    /**
     * 设备当前位置
     */
    private String devicePosition;

    /**
     * 电池电量
     */
    private String battery;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备状态： 
0：离线， 
1：空闲， 
2：故障， 
3：初始化中， 
4：任务中， 
5：充电中， 
7：升级中。
     */
    private Integer deviceStatus;

    /**
     * Idle：空闲， 
Initializin: 初 始 化 中 ， 
InTask:任务中， 
Fault：故障， 
Offline:离线中， 
InCharging 充 电 中 ， 
InUpgrading：升级中。
     */
    private String state;

    /**
     * 方向,0.001 度
     */
    private Integer oritation;

    /**
     * 速度，单位 mm/s
     */
    private Integer speed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private Double x;

    private Double y;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AgvInfo{" +
            "id=" + id +
            ", deviceCode=" + deviceCode +
            ", payLoad=" + payLoad +
            ", devicePostionRec=" + devicePostionRec +
            ", devicePosition=" + devicePosition +
            ", battery=" + battery +
            ", deviceName=" + deviceName +
            ", deviceStatus=" + deviceStatus +
            ", state=" + state +
            ", oritation=" + oritation +
            ", speed=" + speed +
            ", createTime=" + createTime +
            ", x=" + x +
            ", y=" + y +
        "}";
    }
}
