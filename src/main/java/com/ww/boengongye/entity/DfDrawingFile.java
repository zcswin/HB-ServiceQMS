package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 收集各阶段生产需参考的DFM文件
 * </p>
 *
 * @author zhao
 * @since 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("DFM文件")
public class DfDrawingFile extends Model<DfDrawingFile> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 真实路径
     */
    @ApiModelProperty("实际路径")
    public String realPath;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    public String project;

    /**
     * 类别
     */
    @ApiModelProperty("类别")
    public String category;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    public String model;

    /**
     * 适用阶段
     */
    @ApiModelProperty("适用范围/阶段")
    public String applicationStage;

    /**
     * 序号
     */
    public String xh;

    /**
     * 内部_外部
     */
    //@ApiModelProperty("内部_外部")
    public String inOut;

    /**
     * DFM名称
     */
    @ApiModelProperty("DFM名称")
    public String dfmName;

    /**
     * DFM版本
     */
    //@ApiModelProperty("DFM版本")
    public String dfmVersion;


    /**
     * 属性
     */
    @ApiModelProperty("属性")
    public String property;

    /**
     * 变更时间
     */
    @ApiModelProperty("变更日期")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public String changeTime;

    /**
     * 变更版本
     */
    @ApiModelProperty("变更DFM版本")
    public String changeVersion;

    /**
     * 变更图纸版本
     */
    @ApiModelProperty("变更DFM图纸版本")
    public String changeDrawingVersion;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public String uploadDate;

    /**
     * 上传路径
     */
    @ApiModelProperty("上传路径")
    public String uploadPath;

    /**
     * 客户审批时间
     */
    @ApiModelProperty("客户审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp approvalTime;

    /**
     * 变更内容
     */
    @ApiModelProperty("变更内容")
    public String changeContent;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    public String confirmor;

    /**
     * 确认人工号
     */
    public String confirmorCode;

    /**
     * 登记时间
     */
    @ApiModelProperty("登记时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public String registrationTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;

    /**
     * 工厂编号
     */
    public String factoryCode;
    /**
     * 工厂名称
     */
    public String factoryName;

    private String version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfDrawingFile() {
    }


}
