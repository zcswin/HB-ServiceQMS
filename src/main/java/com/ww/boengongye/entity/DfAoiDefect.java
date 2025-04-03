package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * AOI缺陷表
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Data
@ApiModel("AOI缺陷表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DfAoiDefect extends Model<DfAoiDefect> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * AOI机台检测id（父类id）
     */
    private Integer checkId;

    /**
     * 框架ID，如果没有，可与样本ID相同
     */
    private String frameid;

    /**
     * 区域
     */

    private String area;

    /**
     * 缺陷大类编号
     */
    private String classCode;

    /**
     * 缺陷大类名称
     */
    private String className;

    /**
     * 大区域
     */
    private String bigArea;
    /**
     * 缺陷ID
     */
    private String defectid;

    /**
     * 缺陷编号
     */
    private String classid;

    /**
     * 是否是重大缺陷（Y/N）
     */
    private String majorDefect;

    /**
     * 缺陷特征
     */
    private String featurevalues;

    /**
     * 缺陷X坐标
     */
    @TableField("AOIxcenter")
    private String AOIxcenter;

    /**
     * 缺陷Y坐标
     */
    @TableField("AOIycenter")
    private String AOIycenter;

    @TableField(exist = false )
    private String aoIycenter;

    @TableField(exist = false )
    private String aoIxcenter;

    /**
     * 缺陷抓取规则
     */
    private String attribute;

    /**
     * 缺陷严重等级
     */
    private String severityid;

    /**
     * 缺陷判定结果
     */
    private String qualityid;

    /**
     * QC复判结果
     */
    @TableField("RE_result")
    private String reResult;

    /**
     * QC复判时间
     */
    @TableField("RE_Time")
    private String reTime;


    @TableField(exist = false)
    private String rE_time;

    @TableField(exist = false)
    private String rE_result;
    /**
     * AOI过机次数
     */
    private String aoiCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 更新人
     */
    private String updateName;

    /**
     * 批次码
     */
    private String batchCode;

    /**
     * 检测通道
     */
    private String channelkey;

    /**
     * 缺陷小图ID
     */
    private String imageid;

    /**
     * 图片保存路径
     */
    private String imageUrl;

    /**
     * 图片base64
     */
    private String base64str;


    /**
     * 展示的x坐标
     */
    private Double showX;

    /**
     * 展示的y坐标
     */
    private Double showY;

    @TableField(exist = false)
    @ApiModelProperty("缺陷类名")
    private String defectClassName;

    @TableField(exist = false)
    @ApiModelProperty("缺陷区域")
    private String defectArea;

    @TableField(exist = false)
    @ApiModelProperty("项目id")
    private String projectId;

    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String projectName;


    @TableField(exist = false)
    @ApiModelProperty("缺陷权重数")
    private Integer defectWeight;

    @TableField(exist = false)
    @ApiModelProperty("缺陷总数")
    private Integer defectNumber;

    @TableField(exist = false)
    @ApiModelProperty("单位缺陷机会数")
    private Integer unitDefectNumber;

    @TableField(exist = false)
    @ApiModelProperty("时间")
    private String defectTime;

    @TableField(exist = false)
    @ApiModelProperty("总片数")
    private Integer pieceNumber;

    @TableField(exist = false)
    @ApiModelProperty("NG率")
    private String NGPoint;

    @TableField(exist = false)
    @ApiModelProperty("DPMO")
    private String DPMO;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public String getFrameid() {
        return frameid;
    }

    public void setFrameid(String frameid) {
        this.frameid = frameid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBigArea() {
        return bigArea;
    }

    public void setBigArea(String bigArea) {
        this.bigArea = bigArea;
    }

    public String getDefectid() {
        return defectid;
    }

    public void setDefectid(String defectid) {
        this.defectid = defectid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getMajorDefect() {
        return majorDefect;
    }

    public void setMajorDefect(String majorDefect) {
        this.majorDefect = majorDefect;
    }

    public String getFeaturevalues() {
        return featurevalues;
    }

    public void setFeaturevalues(String featurevalues) {
        this.featurevalues = featurevalues;
    }

    public String getAOIxcenter() {
        return AOIxcenter;
    }

    public void setAOIxcenter(String AOIxcenter) {
        this.AOIxcenter = AOIxcenter;
    }

    public String getAOIycenter() {
        return AOIycenter;
    }

    public void setAOIycenter(String AOIycenter) {
        this.AOIycenter = AOIycenter;
    }

    public String getAoIycenter() {
        return aoIycenter;
    }

    public void setAoIycenter(String aoIycenter) {
        this.aoIycenter = aoIycenter;
    }

    public String getAoIxcenter() {
        return aoIxcenter;
    }

    public void setAoIxcenter(String aoIxcenter) {
        this.aoIxcenter = aoIxcenter;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getSeverityid() {
        return severityid;
    }

    public void setSeverityid(String severityid) {
        this.severityid = severityid;
    }

    public String getQualityid() {
        return qualityid;
    }

    public void setQualityid(String qualityid) {
        this.qualityid = qualityid;
    }

    public String getReResult() {
        return reResult;
    }

    public void setReResult(String reResult) {
        this.reResult = reResult;
    }

    public String getReTime() {
        return reTime;
    }

    public void setReTime(String reTime) {
        this.reTime = reTime;
    }

    public String getrE_time() {
        return rE_time;
    }

    public void setrE_time(String rE_time) {
        this.rE_time = rE_time;
    }

    public String getrE_result() {
        return rE_result;
    }

    public void setrE_result(String rE_result) {
        this.rE_result = rE_result;
    }

    public String getAoiCount() {
        return aoiCount;
    }

    public void setAoiCount(String aoiCount) {
        this.aoiCount = aoiCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getChannelkey() {
        return channelkey;
    }

    public void setChannelkey(String channelkey) {
        this.channelkey = channelkey;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBase64str() {
        return base64str;
    }

    public void setBase64str(String base64str) {
        this.base64str = base64str;
    }

    public String getDefectClassName() {
        return defectClassName;
    }

    public void setDefectClassName(String defectClassName) {
        this.defectClassName = defectClassName;
    }

    public String getDefectArea() {
        return defectArea;
    }

    public void setDefectArea(String defectArea) {
        this.defectArea = defectArea;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getDefectWeight() {
        return defectWeight;
    }

    public void setDefectWeight(Integer defectWeight) {
        this.defectWeight = defectWeight;
    }

    public Integer getDefectNumber() {
        return defectNumber;
    }

    public void setDefectNumber(Integer defectNumber) {
        this.defectNumber = defectNumber;
    }

    public Integer getUnitDefectNumber() {
        return unitDefectNumber;
    }

    public void setUnitDefectNumber(Integer unitDefectNumber) {
        this.unitDefectNumber = unitDefectNumber;
    }

    public String getDefectTime() {
        return defectTime;
    }

    public void setDefectTime(String defectTime) {
        this.defectTime = defectTime;
    }

    public Integer getPieceNumber() {
        return pieceNumber;
    }

    public void setPieceNumber(Integer pieceNumber) {
        this.pieceNumber = pieceNumber;
    }

    public String getNGPoint() {
        return NGPoint;
    }

    public void setNGPoint(String NGPoint) {
        this.NGPoint = NGPoint;
    }

    public String getDPMO() {
        return DPMO;
    }

    public void setDPMO(String DPMO) {
        this.DPMO = DPMO;
    }

    public Double getShowX() {
        return showX;
    }

    public void setShowX(Double showX) {
        this.showX = showX;
    }

    public Double getShowY() {
        return showY;
    }

    public void setShowY(Double showY) {
        this.showY = showY;
    }

    @Override
    public String toString() {
        return "DfAoiDefect{" +
                "id=" + id +
                ", checkId=" + checkId +
                ", frameid='" + frameid + '\'' +
                ", area='" + area + '\'' +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", bigArea='" + bigArea + '\'' +
                ", defectid='" + defectid + '\'' +
                ", classid='" + classid + '\'' +
                ", majorDefect='" + majorDefect + '\'' +
                ", featurevalues='" + featurevalues + '\'' +
                ", AOIxcenter='" + AOIxcenter + '\'' +
                ", AOIycenter='" + AOIycenter + '\'' +
                ", attribute='" + attribute + '\'' +
                ", severityid='" + severityid + '\'' +
                ", qualityid='" + qualityid + '\'' +
                ", reResult='" + reResult + '\'' +
                ", reTime='" + reTime + '\'' +
                ", aoiCount='" + aoiCount + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", channelkey='" + channelkey + '\'' +
                ", imageid='" + imageid + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", base64str='" + base64str + '\'' +
                ", defectClassName='" + defectClassName + '\'' +
                ", defectArea='" + defectArea + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", defectWeight=" + defectWeight +
                ", defectNumber=" + defectNumber +
                ", unitDefectNumber=" + unitDefectNumber +
                ", defectTime='" + defectTime + '\'' +
                ", pieceNumber=" + pieceNumber +
                ", NGPoint='" + NGPoint + '\'' +
                ", DPMO='" + DPMO + '\'' +
                '}';
    }
}
