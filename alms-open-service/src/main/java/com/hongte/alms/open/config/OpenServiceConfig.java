package com.hongte.alms.open.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "openService")
@Component
public class OpenServiceConfig {

    /**
     * 3DES加解密的key
     */
    private String tripleDESKey;

    public String getTripleDESKey() {
        return tripleDESKey;
    }

    public void setTripleDESKey(String tripleDESKey) {
        this.tripleDESKey = tripleDESKey;
    }
}
