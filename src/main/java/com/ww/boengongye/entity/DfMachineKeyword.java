package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备关键词
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMachineKeyword extends Model<DfMachineKeyword> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 设备编号
     */
    public String machineCode;

    /**
     * 关键词
     */
    public String keywords;

    /**
     * 创建时间
     */
    public LocalDateTime createTime;

    /**
     * 创建人
     */
    public String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMachineKeyword() {
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

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
