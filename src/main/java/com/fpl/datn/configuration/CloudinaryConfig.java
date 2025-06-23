package com.fpl.datn.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.AccessLevel;

//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryConfig {

    String cloudName;
    String apiKey;
    String apiSecret;
    Boolean secure = true;
    
    @SuppressWarnings("unchecked") 
    @Bean
    Cloudinary cloudinary() {
        Map<String, Object> config =  ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", secure
        );
        return new Cloudinary(config);
    }
}
