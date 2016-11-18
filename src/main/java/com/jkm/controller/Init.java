package com.jkm.controller;

import com.jkm.service.notifier.NoticeTemplateInitService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yulong.zhang on 2016/11/16.
 */
@Component
public class Init {

    private static Logger log = Logger.getLogger(Init.class);

    @Autowired
    private NoticeTemplateInitService noticeTemplateInitService;

    @PostConstruct
    public void initSystem() {
        log.info("######################初始化项目--start##########################");
        this.initSmsTemplate();
        log.info("######################初始化项目--end##########################");
    }

    /**
     * 初始化模板
     */
    private void initSmsTemplate() {
        this.noticeTemplateInitService.initTemplate();
    }
}
