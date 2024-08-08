package com.alinesno.infra.tool.backup.db.operation;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.alinesno.infra.tool.backup.db.operation.DatabaseBackup.BACKUP_LABEL;

@Slf4j
public class QiniuUploader {

    public static void uploadFile(String filePath , String accessKey , String secretKey , String bucketName , String prefix) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);

        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        try {

            String key = prefix + "/" + new File(filePath).getName() ;

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

    @SneakyThrows
    public static void cleanOldFolders(String accessKey , String secretKey , String bucketName , String prefix) {
        Auth auth = Auth.create(accessKey, secretKey);
        Configuration cfg = new Configuration(Region.autoRegion());
        BucketManager bucketManager = new BucketManager(auth, cfg);

        // 获取所有前缀为"mysql/"的文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix);
        FileInfo[] items = fileListIterator.next();

        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        for (FileInfo entry : items) {
            String key = entry.key;

            log.debug("key:{} , value:{}", key, entry);

            if (key.endsWith(".zip")) {
                String dateStr = key.substring(key.indexOf(BACKUP_LABEL) + BACKUP_LABEL.length(), key.lastIndexOf(".zip"));
                LocalDate date = LocalDate.parse(dateStr, formatter);

                log.debug("date:{} , oneWeekAgo = {} , value:{}", date, oneWeekAgo , date.isBefore(oneWeekAgo));

                if (date.isBefore(oneWeekAgo)) {
                    // 删除旧文件
                    bucketManager.delete(bucketName, key);
                    log.debug("Deleted old file: " + key);
                }

            }
        }
    }
}