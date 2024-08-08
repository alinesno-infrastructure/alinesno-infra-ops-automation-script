package com.alinesno.infra.tool.backup.db.operation;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QiniuUploader {

//    private static final String accessKey = "your_access_key";
//    private static final String secretKey = "your_secret_key";
//    private static final String bucketName = "aip-backup";
//    private static final String prefix = "mysql/";
//
//    public static void main(String[] args) {
//        // 1. 上传文件
//        uploadFile("path/to/your/backup.zip");
//
//        // 2. 清理一周之前的文件夹
//        cleanOldFolders();
//    }

    public static void uploadFile(String filePath , String accessKey , String secretKey , String bucketName , String prefix) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);

        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        try {

            String key = prefix + File.separator + new File(filePath).getName() ;

            Response response = uploadManager.put(filePath, key , upToken);
            System.out.println("Upload response: " + response.bodyString());
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
    }

    private static void cleanOldFolders(String accessKey , String secretKey , String bucketName , String prefix) {
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration cfg = new Configuration(Region.autoRegion());
        BucketManager bucketManager = new BucketManager(auth, cfg);

        try {
            // 获取所有前缀为"mysql/"的文件列表
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix);
            FileInfo[] items = fileListIterator.next();

            LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            for (FileInfo entry : items) {
                String key = entry.key;
                if (key.endsWith(".zip")) {
                    String dateStr = key.substring(key.indexOf("-") + 1, key.lastIndexOf(".zip"));
                    LocalDate date = LocalDate.parse(dateStr, formatter);

                    if (date.isBefore(oneWeekAgo)) {
                        // 删除旧文件
                        bucketManager.delete(bucketName, key);
                        System.out.println("Deleted old file: " + key);
                    }
                }
            }
        } catch (QiniuException e) {
            System.err.println(e.getMessage());
        }
    }
}