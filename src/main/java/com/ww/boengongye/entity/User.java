package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    public String id;

    /**
     * 用户名(员工工号)
     */
    public String name;


    /**
     * 密码
     */
    public String password;

    /**
     * 权限
     */
    public String permission;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 创建人
     */
    public String createName;

    /**
     * 电话
     */
    public String phone;

    /**
     * 别名（姓名）
     */
    public String alias;

    /**
     *
     */
    public Integer enabled;

    /**
     *
     */
    public String dataDesc;

    /**
     * 组id
     */
    public String  departmentId;

    /**
     * 组名
     */
    @TableField(exist = false)
    public String departmentName;

    /**
     * 职位
     */
    private String position;

    /**
     * 用户类型
     */
    private String type;


    /**
     * 工序
     */
    @TableField(exist = false)
    private String process;

    /**
     * 考核等级
     */
    @TableField(exist = false)
    private String grade;

    /**
     * 劳动关系
     */
    @TableField(exist = false)
    private String laborRelation;

    /**
     * 学历
     */
    @TableField(exist = false)
    private String education;

    /**
     * 性别
     */
    @TableField(exist = false)
    private String sex;

    /**
     * 所属车间id
     */
    @TableField(exist = false)
    private Integer factoryId;

    /**
     * 所属车间名称
     */
    @TableField(exist = false)
    private String factoryName;

    /**
     * 是否有效
     */
    @TableField(exist = false)
    private String isUse;

    /**
     * 最新修改时间
     */
    @TableField(exist = false)
    private Timestamp updateTime;

    /**
     * 修改人
     */
    @TableField(exist = false)
    private String updateName;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String remark;

    /**
     * 投入数量
     */
    @TableField(exist = false)
    private Integer inputNumber;

    /**
     * 产出数量
     */
    @TableField(exist = false)
    private Integer outputNumber;

    /**
     * 良率
     */
    @TableField(exist = false)
    private String passPoint;

    /**
     * OQC检测数（FQC检测后，同时OQC也检测后的数量）
     */
    @TableField(exist = false)
    private Integer oqcNumber;

    /**
     * 漏检数量（FQC检测为OK，OQC检测为NG）
     */
    @TableField(exist = false)
    private Integer escapeNumber;

    /**
     * 漏检占比
     */
    @TableField(exist = false)
    private String escapePoint;

    @TableField(exist = false)
    @ApiModelProperty("班别")
    private String classCategory;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public User() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", permission='" + permission + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createName='" + createName + '\'' +
                ", phone='" + phone + '\'' +
                ", alias='" + alias + '\'' +
                ", enabled=" + enabled +
                ", dataDesc='" + dataDesc + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", position='" + position + '\'' +
                ", process='" + process + '\'' +
                ", grade='" + grade + '\'' +
                ", laborRelation='" + laborRelation + '\'' +
                ", education='" + education + '\'' +
                ", sex='" + sex + '\'' +
                ", factoryId=" + factoryId +
                ", factoryName='" + factoryName + '\'' +
                ", isUse='" + isUse + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                ", remark='" + remark + '\'' +
                ", inputNumber=" + inputNumber +
                ", outputNumber=" + outputNumber +
                ", passPoint='" + passPoint + '\'' +
                ", oqcNumber=" + oqcNumber +
                ", escapeNumber=" + escapeNumber +
                ", escapePoint='" + escapePoint + '\'' +
                ", classCategory='" + classCategory + '\'' +
                '}';
    }
}
