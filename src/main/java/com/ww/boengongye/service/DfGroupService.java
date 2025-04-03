package com.ww.boengongye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfGroup;
import com.ww.boengongye.utils.Excelable;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
public interface DfGroupService extends IService<DfGroup>, Excelable {
    Map<String, Integer> getMachineCode(String month, String dayNight, String process);

    Map<Integer, Integer> getGroupMacNum(String month, String dayNight);

    Map<String, Integer> getMacResGroupId(String month, String dayNight);

    Map<String, String> getMacResRespon(String month, String dayNight);
}
