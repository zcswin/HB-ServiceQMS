package com.ww.boengongye.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 工艺配置路线关联的工序 前端控制器
 * </p>
 *
 * @author zhao
 * @since 2022-09-30
 */
@Controller
@RequestMapping("/dfRoutingRelationProcess")
@ResponseBody
@CrossOrigin
public class DfRoutingRelationProcessController {

    private static final Logger logger = LoggerFactory.getLogger(DfRoutingRelationProcessController.class);

    @Autowired
    com.ww.boengongye.service.DfRoutingRelationProcessService DfRoutingRelationProcessService;



}
