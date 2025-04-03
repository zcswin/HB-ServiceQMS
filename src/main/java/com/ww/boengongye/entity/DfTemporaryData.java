package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 临时数据表
 * </p>
 *
 * @author guangyao
 * @since 2023-09-25
 */
@Data
public class DfTemporaryData extends Model<DfTemporaryData> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String method;

    private String name;

    private String str1;

    private String str2;

    private String str3;

    private String inte1;

    private String inte2;

    private String inte3;

    private String dou1;

    private String dou2;

    private String dou3;

    private String startTime;

    private String endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfTemporaryData{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", name='" + name + '\'' +
                ", str1='" + str1 + '\'' +
                ", str2='" + str2 + '\'' +
                ", str3='" + str3 + '\'' +
                ", inte1='" + inte1 + '\'' +
                ", inte2='" + inte2 + '\'' +
                ", inte3='" + inte3 + '\'' +
                ", dou1='" + dou1 + '\'' +
                ", dou2='" + dou2 + '\'' +
                ", dou3='" + dou3 + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
