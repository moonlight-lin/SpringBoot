package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ConfigService {

    private final RestTemplate restTemplate;

    public ConfigService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> fetchConfig() {
        String configServerUrl = "http://localhost:8888" + "/application/preprod";
        Map<String, Object> response = restTemplate.getForObject(configServerUrl, Map.class);

        Map<String, Object> configMap = ((List<Map<String, Object>>) response.get("propertySources")).get(0);
        configMap = (Map<String, Object>) configMap.get("source");

        return configMap;
    }

    public Map<String, Object> fetchConfigJson() {
        String configServerUrl = "http://localhost:8888" + "/application-preprod.json";

        return restTemplate.getForObject(configServerUrl, Map.class);
    }
}
