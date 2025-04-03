package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 员工调机排名--尺寸NG率
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfAdSizeNgRate extends Model<DfAdSizeNgRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测量尺寸项
     */
    private String itemName;

    /**
     * 检测次数
     */
    private Integer numTest;

    /**
     * NG次数
     */
    private Integer numNg;

    /**
     * NG率
     */
    private Double rateNg;

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
     * 白夜班
     */
    private String dayOrNight;

    private Integer testType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfAdSizeNgRate() {
    }

    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getNumTest() {
        return numTest;
    }

    public void setNumTest(Integer numTest) {
        this.numTest = numTest;
    }

    public Integer getNumNg() {
        return numNg;
    }

    public void setNumNg(Integer numNg) {
        this.numNg = numNg;
    }

    public Double getRateNg() {
        return rateNg;
    }

    public void setRateNg(Double rateNg) {
        this.rateNg = rateNg;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
