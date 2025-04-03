package com.ww.boengongye;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BoengongyeApplicationTests {

    @Autowired
    private DfQmsIpqcWaigTotalService dfQmsIpqcWaigTotalService;

    @Test
    void contextLoads() {

        QueryWrapper<DfQmsIpqcWaigTotal> littleqwAppear = new QueryWrapper<>();  //  -- 外观
        littleqwAppear.between("f_time", "2023-03-15", "2023-04-15");
        List<DfQmsIpqcWaigTotal> littleMachineCodeAppear = dfQmsIpqcWaigTotalService.listMachineCode(littleqwAppear);
        for (DfQmsIpqcWaigTotal dfQmsIpqcWaigTotal : littleMachineCodeAppear) {
            System.out.println(dfQmsIpqcWaigTotal);
        }
    }

}
