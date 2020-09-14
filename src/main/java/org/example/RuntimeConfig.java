package org.example;

import java.io.IOException;
import java.util.Properties;

public class RuntimeConfig {

    private final String userName;
    private final String password;
    private final String partnerId;
    private final String moveBaseUrl;

    public RuntimeConfig() {
        try {
            Properties prop = new Properties();
            prop.load(RuntimeConfig.class.getClassLoader().getResourceAsStream("config.properties"));
            userName = prop.getProperty("auth.user");
            password = prop.getProperty("auth.password");
            partnerId = prop.getProperty("partnerId");
            moveBaseUrl = prop.getProperty("moveBaseUrl");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read runtime props.", e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getMoveBaseUrl() {
        return moveBaseUrl;
    }
}
