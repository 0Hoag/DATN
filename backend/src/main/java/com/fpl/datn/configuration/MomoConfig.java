package com.fpl.datn.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "momo")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MomoConfig {
    String url;
    String refundUrl;
    String accessKey;
    String partnerCode;
    String secretKey;
    String returnUrl;
    String notifyUrl;
}
