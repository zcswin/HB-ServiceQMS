package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 责任人
 * </p>
 *
 * @author zhao
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfLiableMan extends Model<DfLiableMan> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String factoryName;

    private String sectionName;

    private String stationName;

    private String processName;

    private String problemLevel;

    private String dayOrNight;

    private String liableManName;

    private String liableManCode;

    private String createMan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp createTime;

    /**
     * 类型
     */
    private String type;

    /**
     * 开始处理时间
     */
    private Integer startTime;

    /**
     * 结束处理时间
     */
    private Integer endTime;

    private LocalDateTime updateTime;

 private String bimonthly;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }



    public DfLiableMan() {
    }

    public String getBimonthly() {
        return bimonthly;
    }

    public void setBimonthly(String bimonthly) {
        this.bimonthly = bimonthly;
    }
}
