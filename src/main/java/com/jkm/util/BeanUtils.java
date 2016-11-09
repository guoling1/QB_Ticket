package com.jkm.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext application;

    public void setApplicationContext(ApplicationContext application) throws BeansException {
        this.application = application;
    }

    /**
     * 获取容器中的bean
     *
     * @param id
     * @return
     */
    public static Object getBean(String id) {
        if (application == null) {
            return null;
        }
        return application.getBean(id);
    }

}
