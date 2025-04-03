package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 文件培训文件管理
 * </p>
 *
 * @author zhao
 * @since 2023-09-27
 */
@Data
public class DfTrainingDocumentManagement extends Model<DfTrainingDocumentManagement> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 相对路径
     */
    private String path;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
