package com.tsingyun.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 解决中文乱码问题
 * SpringProcessEngineConfiguration类为spring boot默认使用的流程引擎配置类
 */
@Component
public class ShareniuProcessEngineConfigurationConfigurer implements ProcessEngineConfigurationConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareniuProcessEngineConfigurationConfigurer.class);

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        LOGGER.info(processEngineConfiguration.getActivityFontName());
    }
}