package com.alinesno.infra.tool.backup.db;

import com.alinesno.infra.tool.backup.db.operation.DatabaseBackup;
import com.alinesno.infra.tool.backup.db.operation.QiniuUploader;
import com.alinesno.infra.tool.backup.db.properties.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * 数据库备份
 */
@Slf4j
@SpringBootApplication
public class DbBackupApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DbBackupApplication.class, args);
		log.debug("数据库备份程序启动...");
	}

	@Autowired
	private ConfigProperties properties ;

	public DbBackupApplication(ConfigProperties properties) {
		this.properties = properties;
	}

	@Override
	public void run(String... args) throws Exception {

		String backupFilePath = DatabaseBackup.backupDb(
				properties.getDb().getName(),
				properties.getDb().getUrl() ,
				properties.getDb().getUser(),
				properties.getDb().getPass(),
				properties.getDb().getBackupDir());
		log.debug("备份文件路径: {}", backupFilePath);

		QiniuUploader.uploadFile(
				backupFilePath,
				properties.getQiniu().getAccessKey(),
				properties.getQiniu().getSecretKey() ,
				properties.getQiniu().getBucketName(),
				properties.getQiniu().getFolderPrefix() + "/" + properties.getDb().getName());

		QiniuUploader.cleanOldFolders(
				properties.getQiniu().getAccessKey(),
				properties.getQiniu().getSecretKey(),
				properties.getQiniu().getBucketName(),
				properties.getQiniu().getFolderPrefix() + "/" + properties.getDb().getName());

		// 删除备份文件
		FileUtils.forceDeleteOnExit(new File(backupFilePath));
	}

}
