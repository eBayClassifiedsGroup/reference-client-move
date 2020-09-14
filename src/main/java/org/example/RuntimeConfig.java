package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class RuntimeConfig {

    private String userName;
    private String password;
    private String partnerId;
    private String moveBaseUrl;

    public RuntimeConfig() {
        ingestProperties("config.properties");
        ingestProperties("untracked-config.properties");
    }

    private void ingestProperties(String s) {
        URL resource = RuntimeConfig.class.getClassLoader().getResource(s);
        if (resource != null) {
            try {
                Properties prop = new Properties();
                prop.load(RuntimeConfig.class.getClassLoader().getResourceAsStream(s));
                userName = prop.getProperty("auth.user");
                password = prop.getProperty("auth.password");
                partnerId = prop.getProperty("partnerId");
                moveBaseUrl = prop.getProperty("moveBaseUrl");
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read runtime props.", e);
            }
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
