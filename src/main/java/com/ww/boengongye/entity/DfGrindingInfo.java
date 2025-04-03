package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 *  砂轮刀具记录
 * </p>
 *
 * @author zhao
 * @since 2024-05-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("砂轮刀具记录")
public class DfGrindingInfo extends Model<DfGrindingInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * json包/数据CODE
     */
    private Integer typeData;

    /**
     * 设备编号
     */
    private String machineCode;

    /**
     * 设备当前主轴刀号
     */
    private Integer nIndexTool;

    /**
     * 刀具状态：0-停止废弃；1-初始更换;2-使用中；
     */
    private Integer nStatusTool;

    /**
     * 更换时间
     */
    private Timestamp dtChanage;

    /**
     * 废弃时间(nStatusTool=0时)
     */
    private Timestamp dtAbandon;

    /**
     * 刀具二维码编号
     */
    private String toolCode;

    /**
     * 刀具规格编号
     */
    private String toolSpecCode;

    /**
     * 换刀后已加工片数
     */
    private Integer nTotalUsagePro;

    /**
     * 换刀后总切削时间(s)	
     */
    private Integer nTotalUsageSec;

    /**
     * 有无换刀 (0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)
     */
    private Integer changeKnifeStatus;

    /**
     * 信息推送时间戳
     */
    private Timestamp pubTime;

    /**
     * 是否使用
     */
    private Integer isUse;

}
