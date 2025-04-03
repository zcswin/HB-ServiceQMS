package com.ww.boengongye.entity.DTO;

import lombok.Data;

/**
 * @autor 96901
 * @date 2024/12/27
 */


    @Data
    public class xlsqueryDTO {

        // private TimeRangeDto timeRange;

        private FilterCondition filterCondition;
        private orderCondition orderCondition;
        private int currentPageNum;
        private int pageSize;



}
