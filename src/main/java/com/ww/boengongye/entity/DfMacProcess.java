package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacProcess extends Model<DfMacProcess> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("Type_Data")
    private String type_data;

    @TableField("MachineCode")
    private String machinecode;
    @TableField("file_prog_main")
    private String file_prog_main;
    @TableField("num_prog_main")
    private Integer num_prog_main;
    @TableField("file_prog_sub")
    private String file_prog_sub;
    @TableField("num_prog_sub")
    private Integer num_prog_sub;
    @TableField("pub_time")
    private Integer pub_time;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMacProcess() {
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

    public String getType_data() {
        return type_data;
    }

    public void setType_data(String type_data) {
        this.type_data = type_data;
    }

    public String getMachinecode() {
        return machinecode;
    }

    public void setMachinecode(String machinecode) {
        this.machinecode = machinecode;
    }

    public String getFile_prog_main() {
        return file_prog_main;
    }

    public void setFile_prog_main(String file_prog_main) {
        this.file_prog_main = file_prog_main;
    }

    public Integer getNum_prog_main() {
        return num_prog_main;
    }

    public void setNum_prog_main(Integer num_prog_main) {
        this.num_prog_main = num_prog_main;
    }

    public String getFile_prog_sub() {
        return file_prog_sub;
    }

    public void setFile_prog_sub(String file_prog_sub) {
        this.file_prog_sub = file_prog_sub;
    }

    public Integer getNum_prog_sub() {
        return num_prog_sub;
    }

    public void setNum_prog_sub(Integer num_prog_sub) {
        this.num_prog_sub = num_prog_sub;
    }

    public Integer getPub_time() {
        return pub_time;
    }

    public void setPub_time(Integer pub_time) {
        this.pub_time = pub_time;
    }
}
