package com.opencourse.chapter.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalServiceProp {
    private String sectionSubUrl;
    private String sectionCreatorUrl;
    private String authUrl;
}
