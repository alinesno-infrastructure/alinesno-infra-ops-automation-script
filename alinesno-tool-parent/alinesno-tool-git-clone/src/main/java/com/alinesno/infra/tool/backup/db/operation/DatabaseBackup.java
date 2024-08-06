package com.alinesno.infra.tool.backup.db.operation;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 数据库备份工具类
 *
 */
public class DatabaseBackup {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASS = "your_password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String backupFileName = "backup_" + sdf.format(now) + ".zip";

            // 获取所有表的列表
            ResultSet tables = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backupFileName));

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                // 创建 SQL 文件
                String sqlFileName = tableName + "_" + sdf.format(now) + ".sql";
                createSqlBackupFile(sqlFileName, tableName, conn, zipOut);
            }

            tables.close();
            zipOut.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void createSqlBackupFile(String sqlFileName, String tableName, Connection conn, ZipOutputStream zipOut) throws SQLException, IOException {
        // 构建 SQL 查询语句
        String query = "SELECT * FROM " + tableName;
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        // 将查询结果写入到 ZIP 文件中
        ZipEntry zipEntry = new ZipEntry(sqlFileName);
        zipOut.putNextEntry(zipEntry);

        // 写入 SQL DROP TABLE 和 CREATE TABLE 语句
        writeDropCreateTableStatement(tableName, zipOut, conn);

        // 写入 INSERT 语句
        while (resultSet.next()) {
            StringBuilder insertQuery = new StringBuilder("INSERT INTO ").append(tableName).append(" VALUES (");
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                if (i > 1) insertQuery.append(", ");
                Object value = resultSet.getObject(i);
                insertQuery.append(value == null ? "NULL" : value.toString());
            }
            insertQuery.append(");\n");
            zipOut.write(insertQuery.toString().getBytes());
        }

        resultSet.close();
        statement.close();
        zipOut.closeEntry();
    }

    private static void writeDropCreateTableStatement(String tableName, ZipOutputStream zipOut, Connection conn) throws SQLException, IOException {
        // 构建 SQL 查询语句
        String query = "SHOW CREATE TABLE " + tableName;
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String dropTable = "DROP TABLE IF EXISTS " + tableName + ";\n";
            zipOut.write(dropTable.getBytes());

            String createTable = resultSet.getString("Create Table") + ";\n";
            zipOut.write(createTable.getBytes());
        }

        resultSet.close();
        statement.close();
    }
}