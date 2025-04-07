package com.example.demo.controller;

import com.example.demo.config.ConfigAutoRefresh;
import com.example.demo.config.ConfigNoRefresh;
import com.example.demo.config.ConfigProperty;
import com.example.demo.config.ConfigRefreshProperty;
import com.example.demo.service.ConfigService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConfigController {

    private final ConfigAutoRefresh configAutoRefresh;
    private final ConfigNoRefresh configNoRefresh;
    private final ConfigService configService;
    private final ConfigProperty configProperty;
    private final ConfigRefreshProperty configRefreshProperty;

    public ConfigController(ConfigAutoRefresh configAutoRefresh, ConfigNoRefresh configNoRefresh,
                            ConfigService configService, ConfigProperty configProperty, ConfigRefreshProperty configRefreshProperty) {
        this.configAutoRefresh = configAutoRefresh;
        this.configNoRefresh = configNoRefresh;
        this.configService = configService;
        this.configProperty = configProperty;
        this.configRefreshProperty = configRefreshProperty;
    }

    @GetMapping(value = "/config-auto-refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConfigAutoRefresh() {
        return "feature value is : \n\t" + configAutoRefresh.getFeatureValue() +
                "\n\nuser list is : \n\t" + configAutoRefresh.getUserList();
    }

    @GetMapping(value = "/config-no-refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConfigNoRefresh() {
        return "feature value is : \n\t" + configNoRefresh.getFeatureValue() +
                "\n\nuser list is : \n\t" + configNoRefresh.getUserList();
    }

    @GetMapping(value = "/config-property", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConfigProperty() {
        return "feature value is : \n\t" + configProperty.getValue() +
                "\n\nuser list is : \n\t" + configProperty.getUserList() +
                "\n\nfeature list is : \n\t" + configProperty.getFeatureList();
    }

    @GetMapping(value = "/config-refresh-property", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConfigRefreshProperty() {
        return "feature value is : \n\t" + configRefreshProperty.getValue() +
                "\n\nuser list is : \n\t" + configRefreshProperty.getUserList() +
                "\n\nfeature list is : \n\t" + configRefreshProperty.getFeatureList();
    }

    @GetMapping(value = "/config-from-config-server", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getConfigFromConfigServer() {
        return configService.fetchConfig();
    }

    @GetMapping(value = "/config-from-config-server-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getConfigFromConfigServerJson() {
        return configService.fetchConfigJson();
    }
}
