package com.alinesno.infra.tool.backup.db.operation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HealthChecker {

    public List<UnhealthyResource> checkHealthOfWebsitesAndPorts(List<UnhealthyResource> resources) {

        List<UnhealthyResource> unhealthyResources = new ArrayList<>();

        // 检查网站
        resources.forEach(resource -> {
            if (resource.getType().equals("web")) {
                if (!isWebsiteHealthy(resource.getAddress())) {
                    unhealthyResources.add(new UnhealthyResource(resource.getName() , resource.getType(), resource.getAddress(), resource.getPort(), "HTTP status code is not in the 2xx range"));
                }
            }
        });

        // 检查端口
        resources.forEach(resource -> {
            if (resource.getType().equals("socket")) {
                if (!isPortOpen(resource.getAddress(), resource.getPort())) {
                    unhealthyResources.add(new UnhealthyResource(resource.getName() , resource.getType(), resource.getAddress(), resource.getPort(), "Connection refused or timeout"));
                }
            }
        });

        return unhealthyResources;
    }

    private boolean isWebsiteHealthy(String url) {
        try {
            URL websiteUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) websiteUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000); // 设置超时时间为 1 秒
            int responseCode = connection.getResponseCode();
            connection.disconnect();

            return (responseCode >= 200 && responseCode < 300);
        } catch (IOException e) {
            log.error("Error checking website: " + url);
            return false;
        }
    }

    private boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            log.error("Error checking port: " + host + ":" + port);
            return false;
        }
    }
}