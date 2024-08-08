package com.alinesno.infra.tool.backup.db;

import com.alinesno.infra.tool.backup.db.operation.DatabaseBackup;
import com.alinesno.infra.tool.backup.db.operation.QiniuUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
	}

	@Override
	public void run(String... args) throws Exception {

		String databaseName = "dev_alinesno_cloud_base_authority"; // 替换为你要备份的数据库名称
		String dbUrl = "jdbc:mysql://localhost:3306/" + databaseName;
		String user = "root";
		String pass = "adminer";
		String backupDir = "/tmp/db-backup"; // 指定备份文件的目录

		String backupFilePath = DatabaseBackup.backupDb(databaseName, dbUrl, user, pass, backupDir);
		log.debug("备份文件路径: {}", backupFilePath);

		String accessKey = getEnv("ALINESNO_QINIU_ACCESS_KEY" , "") ;
		String secretKey = getEnv("ALINESNO_QINIU_SECRET_KEY" , "") ;
		String bucketName = getEnv("ALINESNO_QINIU_BUCKET_NAME" , "") ;
		String prefix = getEnv("ALINESNO_QINIU_FOLDER_PREFIX" , "mysql-backup-uat") ;

		log.debug("accessKey: {}", accessKey);
		log.debug("secretKey: {}", secretKey);
		log.debug("bucketName: {}", bucketName);

		QiniuUploader.uploadFile(backupFilePath, accessKey, secretKey, bucketName, prefix + File.separator + databaseName);

		// 删除备份文件
		FileUtils.forceDeleteOnExit(new File(backupFilePath));
	}

	public static String getEnv(String key, String defaultValue) {
		String value = System.getenv(key);
		return value == null ? defaultValue : value;
	}
}
