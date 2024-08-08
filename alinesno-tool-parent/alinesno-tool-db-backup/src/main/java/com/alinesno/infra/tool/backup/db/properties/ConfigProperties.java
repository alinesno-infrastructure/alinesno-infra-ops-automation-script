package com.alinesno.infra.tool.backup.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alinesno")
public class ConfigProperties {

    private Db db;
    private Qiniu qiniu;

    @Data
    public static class Db {
        private String name;
        private String url;
        private String user;
        private String pass;
        private String backupDir;
    }

    @Data
    public static class Qiniu{
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String folderPrefix;
    }

}