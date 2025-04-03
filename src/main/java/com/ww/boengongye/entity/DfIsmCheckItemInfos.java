package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * ISM测量
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@Data
@ToString
public class DfIsmCheckItemInfos extends Model<DfIsmCheckItemInfos> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Timestamp createTime;

    /**
     * 检测时间
     */
//    @ExcelProperty("时间")
    private Timestamp checkTime;

    /**
     * rowId
     */
//    @ExcelProperty("RowID")
    private Integer rowId;

    /**
     * 位置
     */
//    @ExcelProperty("位置")
    private String position;

    /**
     * 结果
     */
//    @ExcelProperty("结果")
    private String result;

    /**
     * 暗码
     */
//    @ExcelProperty("暗码")
    private String privateMark;

    /**
     * 分料分仓
     */
//    @ExcelProperty("分料分仓")
    private String divideMaterialsDifferentParts;

    /**
     * 暗码结果
     */
//    @ExcelProperty("暗码结果")
    private String privateMarkResult;

    /**
     * 网络校验结果
     */
//    @ExcelProperty("网络校验结果")
    private String networkValidationResults;

    /**
     * trace卡站
     */
//    @ExcelProperty("Trace卡站")
    private String trackUrca;

    /**
     * 厚度异常
     */
//    @ExcelProperty("厚度异常")
    private String abnormalThickness;

    /**
     * 应力结果
     */
//    @ExcelProperty("应力结果")
    private String stressResults;

    /**
     * cs
     */
//    @ExcelProperty("cs")
    private Double cs;

    /**
     * csk
     */
//    @ExcelProperty("csk")
    private Double csk;

    /**
     * dol
     */
//    @ExcelProperty("dol")
    private Double dol;

    /**
     * doc
     */
//    @ExcelProperty("doc")
    private Double doc;

    /**
     * ct
     */
//    @ExcelProperty("ct")
    private Double ct;

    /**
     * ctorta
     */
//    @ExcelProperty("ct/ta")
    private Double ctOrTa;

//    @ExcelProperty("ta*ct")
    private Double tact;

//    @ExcelProperty("gt")
    private Double gGt;

//    @ExcelProperty("ISM回传时间")
    private Timestamp ismRollbackTime;

//    @ExcelProperty("ISM回传型号")
    private String ismRollbackModel;

//    @ExcelProperty("ISM回传报错")
    private String ismRollbackError;


    private String ngType;

    private String machineId;

    private Integer checkType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
