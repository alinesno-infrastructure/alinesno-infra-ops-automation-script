package com.alinesno.infra.tool.backup.db;

import com.alinesno.infra.tool.backup.db.operation.DatabaseBackup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbBackupApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DbBackupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String DATABASE_NAME = "dev_alinesno_cloud_base_authority"; // 替换为你要备份的数据库名称
		String DB_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;
		String USER = "root";
		String PASS = "adminer";
		String BACKUP_DIRECTORY = "/Users/luodong/Desktop/db-backup"; // 指定备份文件的目录

		DatabaseBackup.backupDb(DATABASE_NAME, DB_URL, USER, PASS, BACKUP_DIRECTORY);
	}
}
