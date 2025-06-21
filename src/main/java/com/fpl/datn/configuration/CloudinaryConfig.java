package com.fpl.datn.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "da4p9sh2j",
                "api_key", "188561129188545",
                "api_secret", "P2sJ6y2Xq2_aqvmZqhaze_wwLuc",
                "secure", true));
    }
}
