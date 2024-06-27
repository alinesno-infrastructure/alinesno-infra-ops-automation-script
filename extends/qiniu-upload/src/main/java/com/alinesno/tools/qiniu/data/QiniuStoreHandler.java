package com.alinesno.tools.qiniu.data;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * 七牛数据存储方案
 *
 * @author LuoAnDong
 * @sine 2018年2月23日 下午2:37:04
 */
public class QiniuStoreHandler {

	private Configuration cfg;
	private UploadManager uploadManager;
	private Auth auth;
	private String spaceBucket;
	private boolean overrideUpload = false ; 

	public QiniuStoreHandler(String accessKey, String secretKey, String spaceBucket) {
		this.spaceBucket = spaceBucket;

		cfg = new Configuration(Region.autoRegion());
		uploadManager = new UploadManager(cfg);
		auth = Auth.create(accessKey, secretKey);
	}

	/**
	 * 覆盖上传配置 
	 * 
	 * @param localFilePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public void uploadQiniu(String localFilePath, String uplaodFileKey) throws IOException {
		byte[] bytes = FileUtils.readFileToByteArray(new File(localFilePath));
		this.upload(bytes, uplaodFileKey);
	}

	public void upload(byte[] data, String path) throws QiniuException {
		String token = overrideUpload?auth.uploadToken(this.spaceBucket , path) : auth.uploadToken(this.spaceBucket);
		Response res = uploadManager.put(data, path, token);

		System.out.println("文件 path = " + path + " 上传成功.");

		if (!res.isOK()) {
			throw new RuntimeException("上传七牛出错：" + res);
		}
	}

	/**
	 * 刷新目录
	 */
	public void refreshFolder(String[] dirs) {
		CdnManager c = new CdnManager(auth);
		try {
			CdnResult.RefreshResult result = c.refreshDirs(dirs);
			System.out.println(ToStringBuilder.reflectionToString(result));
		} catch (QiniuException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id删除文件
	 * 
	 * @param fileId
	 * @return
	 */
	public boolean deleteData(String bucket , String key) {
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
		    bucketManager.delete(bucket, key);
		} catch (QiniuException ex) {
		    //如果遇到异常，说明删除失败
		    System.err.println(ex.code());
		    System.err.println(ex.response.toString());
		}
		return true;
	}

	public boolean isOverrideUpload() {
		return overrideUpload;
	}

	public void setOverrideUpload(boolean overrideUpload) {
		this.overrideUpload = overrideUpload;
	}

}
