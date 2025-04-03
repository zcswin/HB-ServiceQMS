package com.ww.boengongye.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @autor 96901
 * @date 2024/12/25
 */
@Data
public class EquipmentPictureVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private EquipmentMaintenance equipmentMaintenance;
    private List<Pictures> pictures;


}
