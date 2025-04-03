package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 全检设备-缺陷对照表
 * </p>
 *
 * @author liwei
 * @since 2024-08-22
 */
public class DfInspectionEquipmentDefect extends Model<DfInspectionEquipmentDefect> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createTime;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 检测英文名称
     */
    private String testEnName;

    /**
     * 检测中文名称
     */
    private String testCnName;

    /**
     * 外观/尺寸(1:外观,2:尺寸)
     */
    private Integer sizeOrAppearance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getTestEnName() {
        return testEnName;
    }

    public void setTestEnName(String testEnName) {
        this.testEnName = testEnName;
    }
    public String getTestCnName() {
        return testCnName;
    }

    public void setTestCnName(String testCnName) {
        this.testCnName = testCnName;
    }
    public Integer getSizeOrAppearance() {
        return sizeOrAppearance;
    }

    public void setSizeOrAppearance(Integer sizeOrAppearance) {
        this.sizeOrAppearance = sizeOrAppearance;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfInspectionEquipmentDefect{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", deviceType=" + deviceType +
            ", testEnName=" + testEnName +
            ", testCnName=" + testCnName +
            ", sizeOrAppearance=" + sizeOrAppearance +
        "}";
    }
}
