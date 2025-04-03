package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C6_CNC3_UMS6检测数据
 * </p>
 *
 * @author zhao
 * @since 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeC6Cuc3Ums6 extends Model<DfTeC6Cuc3Ums6> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台长
     */
    private Double platformLength;

    /**
     * 平台宽
     */
    private Double platformWidth;

    /**
     * 平台中心_X
     */
    @TableField("platform_center_X")
    private Double platformCenterX;

    /**
     * 平台中心_Y
     */
    @TableField("platform_center_Y")
    private Double platformCenterY;

    /**
     * 平台左边距
     */
    private Double platformLeftMargin;

    /**
     * 平台左边距2
     */
    private Double platformLeftMargin2;

    /**
     * 平台顶边距
     */
    private Double platformTopMargin;

    /**
     * 平台顶边距2
     */
    private Double platformTopMargin2;

    /**
     * 长边外形面倒角
     */
    private Double longsideChamfer;

    @TableField("F03")
    private Double f03;

    @TableField("P1")
    private Double p1;

    @TableField("P2")
    private Double p2;

    @TableField("P3")
    private Double p3;

    @TableField("P4")
    private Double p4;

    @TableField("P5")
    private Double p5;

    /**
     * 厚度极差
     */
    private Double thicknessRange;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 检测结果
     */
    private String testResult;

    /**
     * 机台状态
     */
    private String machineStatus;

    /**
     * 结果
     */
    private String result;

    /**
     * 索引
     */
    private Integer indexing;

    /**
     * 检测时间
     */
    private LocalDateTime testTime;

    private LocalDateTime createTime;
    private String productId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPlatformLength() {
        return platformLength;
    }

    public void setPlatformLength(Double platformLength) {
        this.platformLength = platformLength;
    }

    public Double getPlatformWidth() {
        return platformWidth;
    }

    public void setPlatformWidth(Double platformWidth) {
        this.platformWidth = platformWidth;
    }

    public Double getPlatformCenterX() {
        return platformCenterX;
    }

    public void setPlatformCenterX(Double platformCenterX) {
        this.platformCenterX = platformCenterX;
    }

    public Double getPlatformCenterY() {
        return platformCenterY;
    }

    public void setPlatformCenterY(Double platformCenterY) {
        this.platformCenterY = platformCenterY;
    }

    public Double getPlatformLeftMargin() {
        return platformLeftMargin;
    }

    public void setPlatformLeftMargin(Double platformLeftMargin) {
        this.platformLeftMargin = platformLeftMargin;
    }

    public Double getPlatformLeftMargin2() {
        return platformLeftMargin2;
    }

    public void setPlatformLeftMargin2(Double platformLeftMargin2) {
        this.platformLeftMargin2 = platformLeftMargin2;
    }

    public Double getPlatformTopMargin() {
        return platformTopMargin;
    }

    public void setPlatformTopMargin(Double platformTopMargin) {
        this.platformTopMargin = platformTopMargin;
    }

    public Double getPlatformTopMargin2() {
        return platformTopMargin2;
    }

    public void setPlatformTopMargin2(Double platformTopMargin2) {
        this.platformTopMargin2 = platformTopMargin2;
    }

    public Double getLongsideChamfer() {
        return longsideChamfer;
    }

    public void setLongsideChamfer(Double longsideChamfer) {
        this.longsideChamfer = longsideChamfer;
    }

    public Double getF03() {
        return f03;
    }

    public void setF03(Double f03) {
        this.f03 = f03;
    }

    public Double getP1() {
        return p1;
    }

    public void setP1(Double p1) {
        this.p1 = p1;
    }

    public Double getP2() {
        return p2;
    }

    public void setP2(Double p2) {
        this.p2 = p2;
    }

    public Double getP3() {
        return p3;
    }

    public void setP3(Double p3) {
        this.p3 = p3;
    }

    public Double getP4() {
        return p4;
    }

    public void setP4(Double p4) {
        this.p4 = p4;
    }

    public Double getP5() {
        return p5;
    }

    public void setP5(Double p5) {
        this.p5 = p5;
    }

    public Double getThicknessRange() {
        return thicknessRange;
    }

    public void setThicknessRange(Double thicknessRange) {
        this.thicknessRange = thicknessRange;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getIndexing() {
        return indexing;
    }

    public void setIndexing(Integer indexing) {
        this.indexing = indexing;
    }

    public LocalDateTime getTestTime() {
        return testTime;
    }

    public void setTestTime(LocalDateTime testTime) {
        this.testTime = testTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}