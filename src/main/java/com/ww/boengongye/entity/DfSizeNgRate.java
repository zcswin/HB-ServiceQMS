package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 尺寸NG率
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSizeNgRate extends Model<DfSizeNgRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 外形长1
     */
    private Double appearLength1;

    /**
     * 外形长2
     */
    private Double appearLength2;

    /**
     * 外形长3
     */
    private Double appearLength3;

    /**
     * 外形宽1
     */
    private Double appearWidth1;

    /**
     * 外形宽2
     */
    private Double appearWidth2;

    /**
     * 外形宽3
     */
    private Double appearWidth3;

    /**
     * MI孔直径
     */
    private Double miHoleDiameter;

    /**
     * MI孔真圆度
     */
    private Double miHoleRoundness;

    /**
     * MI孔中心距X
     */
    private Double miHoleCenterDistanceX;

    /**
     * MI孔中心距Y
     */
    private Double miHoleCenterDistanceY;

    /**
     * DB孔直径
     */
    private Double dbHoleDiameter;

    /**
     * DB孔真圆度
     */
    private Double dbHoleRoundness;

    /**
     * DB孔中心距X
     */
    private Double dbHoleCenterDistanceX;

    /**
     * DB孔中心距Y
     */
    private Double dbHoleCenterDistanceY;

    /**
     * S孔直径
     */
    private Double sHoleDiameter;

    /**
     * S孔真圆度
     */
    private Double sHoleRoundness;

    /**
     * S孔中心距X
     */
    private Double sHoleCenterDistanceX;

    /**
     * S孔中心距Y
     */
    private Double sHoleCenterDistanceY;

    /**
     * MIC孔直径
     */
    private Double micHoleDiameter;

    /**
     * MIC孔真圆度
     */
    private Double micHoleRoundness;

    /**
     * MIC孔中心距X
     */
    private Double micHoleCenterDistanceX;

    /**
     * MIC孔中心距Y
     */
    private Double micHoleCenterDistanceY;

    /**
     * 测量时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp testTime;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 工序
     */
    private String process;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 白/夜班
     */
    private String dayOrNight;

    /**
     * 测试人
     */
    private String testMan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp  createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSizeNgRate() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAppearLength1() {
        return appearLength1;
    }

    public void setAppearLength1(Double appearLength1) {
        this.appearLength1 = appearLength1;
    }

    public Double getAppearLength2() {
        return appearLength2;
    }

    public void setAppearLength2(Double appearLength2) {
        this.appearLength2 = appearLength2;
    }

    public Double getAppearLength3() {
        return appearLength3;
    }

    public void setAppearLength3(Double appearLength3) {
        this.appearLength3 = appearLength3;
    }

    public Double getAppearWidth1() {
        return appearWidth1;
    }

    public void setAppearWidth1(Double appearWidth1) {
        this.appearWidth1 = appearWidth1;
    }

    public Double getAppearWidth2() {
        return appearWidth2;
    }

    public void setAppearWidth2(Double appearWidth2) {
        this.appearWidth2 = appearWidth2;
    }

    public Double getAppearWidth3() {
        return appearWidth3;
    }

    public void setAppearWidth3(Double appearWidth3) {
        this.appearWidth3 = appearWidth3;
    }

    public Double getMiHoleDiameter() {
        return miHoleDiameter;
    }

    public void setMiHoleDiameter(Double miHoleDiameter) {
        this.miHoleDiameter = miHoleDiameter;
    }

    public Double getMiHoleRoundness() {
        return miHoleRoundness;
    }

    public void setMiHoleRoundness(Double miHoleRoundness) {
        this.miHoleRoundness = miHoleRoundness;
    }

    public Double getMiHoleCenterDistanceX() {
        return miHoleCenterDistanceX;
    }

    public void setMiHoleCenterDistanceX(Double miHoleCenterDistanceX) {
        this.miHoleCenterDistanceX = miHoleCenterDistanceX;
    }

    public Double getMiHoleCenterDistanceY() {
        return miHoleCenterDistanceY;
    }

    public void setMiHoleCenterDistanceY(Double miHoleCenterDistanceY) {
        this.miHoleCenterDistanceY = miHoleCenterDistanceY;
    }

    public Double getDbHoleDiameter() {
        return dbHoleDiameter;
    }

    public void setDbHoleDiameter(Double dbHoleDiameter) {
        this.dbHoleDiameter = dbHoleDiameter;
    }

    public Double getDbHoleRoundness() {
        return dbHoleRoundness;
    }

    public void setDbHoleRoundness(Double dbHoleRoundness) {
        this.dbHoleRoundness = dbHoleRoundness;
    }

    public Double getDbHoleCenterDistanceX() {
        return dbHoleCenterDistanceX;
    }

    public void setDbHoleCenterDistanceX(Double dbHoleCenterDistanceX) {
        this.dbHoleCenterDistanceX = dbHoleCenterDistanceX;
    }

    public Double getDbHoleCenterDistanceY() {
        return dbHoleCenterDistanceY;
    }

    public void setDbHoleCenterDistanceY(Double dbHoleCenterDistanceY) {
        this.dbHoleCenterDistanceY = dbHoleCenterDistanceY;
    }

    public Double getSHoleDiameter() {
        return sHoleDiameter;
    }

    public void setSHoleDiameter(Double sHoleDiameter) {
        this.sHoleDiameter = sHoleDiameter;
    }

    public Double getSHoleRoundness() {
        return sHoleRoundness;
    }

    public void setSHoleRoundness(Double sHoleRoundness) {
        this.sHoleRoundness = sHoleRoundness;
    }

    public Double getSHoleCenterDistanceX() {
        return sHoleCenterDistanceX;
    }

    public void setSHoleCenterDistanceX(Double sHoleCenterDistanceX) {
        this.sHoleCenterDistanceX = sHoleCenterDistanceX;
    }

    public Double getSHoleCenterDistanceY() {
        return sHoleCenterDistanceY;
    }

    public void setSHoleCenterDistanceY(Double sHoleCenterDistanceY) {
        this.sHoleCenterDistanceY = sHoleCenterDistanceY;
    }

    public Double getMicHoleDiameter() {
        return micHoleDiameter;
    }

    public void setMicHoleDiameter(Double micHoleDiameter) {
        this.micHoleDiameter = micHoleDiameter;
    }

    public Double getMicHoleRoundness() {
        return micHoleRoundness;
    }

    public void setMicHoleRoundness(Double micHoleRoundness) {
        this.micHoleRoundness = micHoleRoundness;
    }

    public Double getMicHoleCenterDistanceX() {
        return micHoleCenterDistanceX;
    }

    public void setMicHoleCenterDistanceX(Double micHoleCenterDistanceX) {
        this.micHoleCenterDistanceX = micHoleCenterDistanceX;
    }

    public Double getMicHoleCenterDistanceY() {
        return micHoleCenterDistanceY;
    }

    public void setMicHoleCenterDistanceY(Double micHoleCenterDistanceY) {
        this.micHoleCenterDistanceY = micHoleCenterDistanceY;
    }

    public Timestamp getTestTime() {
        return testTime;
    }

    public void setTestTime(Timestamp testTime) {
        this.testTime = testTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getLinebody() {
        return linebody;
    }

    public void setLinebody(String linebody) {
        this.linebody = linebody;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public String getTestMan() {
        return testMan;
    }

    public void setTestMan(String testMan) {
        this.testMan = testMan;
    }

 
}
