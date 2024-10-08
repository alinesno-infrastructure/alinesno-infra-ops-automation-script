package com.alinesno.infra.tool.backup.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DbBackupApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DbBackupApplication.class, args);
        log.debug("数据库备份程序启动...");
    }

    @Override
    public void run(String... args) throws Exception {

    }
}