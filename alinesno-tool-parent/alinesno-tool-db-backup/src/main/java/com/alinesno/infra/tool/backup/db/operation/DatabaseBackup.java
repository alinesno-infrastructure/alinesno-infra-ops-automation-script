package com.alinesno.infra.tool.backup.db.operation;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseBackup {

    public static void backupDb(
                                String dbName ,
                                String dbUrl ,
                                String user ,
                                String pass ,
                                String backupDir) {

        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String backupFileName = dbName+ "_backup_" + sdf.format(now) + ".zip";

            // 获取指定数据库中的所有表的列表
            ResultSet tables = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backupDir + File.separator + backupFileName));

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                String tableCatalog = tables.getString("TABLE_CAT"); // 数据库名称

                log.debug("Processing table: " + tableName);
                log.debug("Table Catalog: " + tableCatalog);

                // 检查表是否属于指定的数据库
                if (tableCatalog.equals(dbName)) {

                    System.out.println("Processing table: " + tableName);
                    System.out.println("Table Catalog: " + tableCatalog);

                    // 创建 SQL 文件
                    String sqlFileName = tableName + ".sql";
                    createSqlBackupFile(sqlFileName, tableName, conn, zipOut);
                }
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