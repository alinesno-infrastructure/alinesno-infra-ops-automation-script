package com.alinesno.tools.qiniu;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.alinesno.tools.qiniu.data.QiniuStoreHandler;

/**
 * 上传七牛文件小工具
 * 
 * @author LuoAnDong
 * @since 2022年2月1日 下午10:23:43
 */
public class UploadQiniu {

	public static void main(String[] args) {
		
		System.out.println("参数为:" + ToStringBuilder.reflectionToString(args));

		String accessKey = args[0] ; 
		String secretKey = args[1] ; 
		String spaceBucket = args[2] ; 
		String domain = args[3] ; 
		String localFolder = args[4] ; 
		String remoteFolder = args[5] ; 
		
		boolean overrideUpload =  args[6]==null?true:Boolean.parseBoolean(args[7]) ; 
		boolean refresh = args[7]==null?true:Boolean.parseBoolean(args[7]) ; 

		QiniuStoreHandler qiniuStoreHandler = new QiniuStoreHandler(accessKey, secretKey, spaceBucket);
		qiniuStoreHandler.setOverrideUpload(overrideUpload);

		try {
			modifyFileContent(new File(localFolder), new File(localFolder) ,  remoteFolder, qiniuStoreHandler , spaceBucket);
			
			//待刷新的目录列表，目录必须以 / 结尾
			String refreshFloder = domain + "/" + remoteFolder + "/" ; 
			System.out.println("refreshFloder = " + refreshFloder);
			
			if(refresh) {
				String[] dirs = new String[]{ refreshFloder };
				qiniuStoreHandler.refreshFolder(dirs) ; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			System.exit(0);
		}
	}

	/**
	 * 文件上传
	 * @param file
	 * @param localFolder
	 * @param remoteFolder
	 * @param qiniuStoreHandler
	 * @throws IOException
	 */
	public static void modifyFileContent(File file , File localFolder , String remoteFolder , QiniuStoreHandler qiniuStoreHandler , String spaceBucket) throws IOException{
		
		if(file.exists()){//先判断文件是否存在
            File[] files=file.listFiles();//获取指定目录下当前的所有文件夹或者文件对象
            if(files!=null && files.length>0){//判断是否文件下为空
                for (int i = 0; i < files.length; i++) {//对文件进行遍历
                    if(files[i].exists()&&files[i].isDirectory()){//判断文件是否是文件夹，如果是文件夹则继续调用listAll进行递归遍历
                    	modifyFileContent(files[i], localFolder ,  remoteFolder , qiniuStoreHandler , spaceBucket);
                    }else {//如果不是文件夹，则输出文件名
                        
						String lengthIndex = localFolder.getAbsolutePath() ; 
						String removePath = remoteFolder + files[i].getAbsolutePath().substring(lengthIndex.length()) ; 
						
						qiniuStoreHandler.uploadQiniu(files[i].getAbsolutePath(), removePath) ; 
                    }
                }
            }
		}
		
	}
}
