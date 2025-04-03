package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * QCP标准
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class DfQcpStandard extends Model<DfQcpStandard> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 生产阶段
     */
    @TableField(exist = false)

    public String productionPhase;

    /**
     * 文件名称
     */
//    @TableField(exist = false)
    public String fileName;

    /**
     * 文件版本
     */
    @TableField(exist = false)
    public String fileVersion;

    /**
     * 时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(exist = false)
    public String uploadTime;

    /**
     * 图纸名称
     */
    @TableField(exist = false)
    public String brawingName;

    /**
     * 图纸版本
     */
    @TableField(exist = false)
    public String brawingVersion;

    /**
     * 工艺
     */
    @TableField(exist = false)
    public String technology;

    /**
     * 变更内容
     */
    public String changeContent;

    /**
     * 客户审批
     */
    @TableField(exist = false)
    public String customerApproval;

    /**
     * 审批时间
     */
    @TableField(exist = false)
    public String approvalTime;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    @TableField(exist = false)
    public String createName;

    /**
     * 保存路径
     */
    @TableField(exist = false)
    public String uploadPath;
    @TableField(exist = false)
    public String factoryName;
    @TableField(exist = false)
    public String factoryCode;


    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("build")
    private String build;

    @ApiModelProperty("title")
    private String title;

    @ApiModelProperty("rev")
    private String rev;


    @ApiModelProperty("date")
    private String date;

    @ApiModelProperty("drawing")
    private String drawing;

    @ApiModelProperty("drawRev")
    private String drawRev;

    @ApiModelProperty("process")
    private String process;

    @ApiModelProperty("outerQcpFlie")
    private String outerQcpFlie;

    @ApiModelProperty("processToolingChanges")
    private String processToolingChanges;

    @ApiModelProperty("comment")
    private String comment;

    @ApiModelProperty("mqeApprove")
    private String mqeApprove;


    @ApiModelProperty("approveDate")
    private String approveDate;

    @ApiModelProperty("fileUpload")
    private String fileUpload;

    @ApiModelProperty("confirmer")
    private String confirmer;

    @ApiModelProperty("registrationTime")
    private String registrationTime;

    private String realPath;

    @ApiModelProperty("remarks")
    private String remarks;

    private String version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
