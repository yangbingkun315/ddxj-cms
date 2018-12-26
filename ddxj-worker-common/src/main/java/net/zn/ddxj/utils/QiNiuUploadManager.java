package net.zn.ddxj.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;

import net.zn.ddxj.constants.Constants;

public class QiNiuUploadManager
{

	public static final String accessKey = PropertiesUtils.getPropertiesByName("access_key");
	public static final String secretKey = PropertiesUtils.getPropertiesByName("secret_key");
	public static final String bucketName = PropertiesUtils.getPropertiesByName("bucket_name");

	public static void deleteFile(BucketManager bucketManager,String remotePath) throws QiniuException
	{
		if(remotePath.startsWith(Constants.SPT))
		{
			remotePath = remotePath.substring(1);
		}
		File file = new File(bucketName + remotePath);
		if(file.isFile())
		{
			file.delete();
		}
	}
	public static void deleteFile(String remotePath) throws QiniuException
	{
		if(remotePath.startsWith(Constants.SPT))
		{
			remotePath = remotePath.substring(1);
		}
		File file = new File(bucketName + remotePath);
		if(file.isFile())
		{
			file.delete();
		}
	}
	public static boolean checkImage(String image) throws IOException
	{
		return checkSexImage(image);
	}
	//鉴定黄图
	public static boolean checkSexImage(String image) throws IOException
	{
		String url = PropertiesUtils.getPropertiesByName("static_url") + image + "?qpulp";
		
		HttpClient client = new HttpClient();
		GetMethod postMethod = new GetMethod(url);
		client.executeMethod(postMethod);
        String returnString = postMethod.getResponseBodyAsString();
        JSONObject rep = new JSONObject().parseObject(returnString);
        
        JSONObject data = rep.getJSONObject("result");
        if("2".equals(data.getString("label")) && !data.getBoolean("review"))
        {
        	return true;
        }
		return false;
	}
	// 上传图片
	public static Response uploadFile(String uploadPath, byte[] file) throws QiniuException
	{
		if(uploadPath.startsWith(Constants.SPT))
		{
			uploadPath = uploadPath.substring(1);
		}
		
		//构造一个带指定Zone对象的配置类
        //机房   Zone对象 华东  Zone.zone0()华北Zone.zone1()华南Zone.zone2()北美Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        //创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        
		String token = createOverwriteUploadToken(uploadPath);
		return uploadManager.put(file, uploadPath, token);
	}
	private static String createOverwriteUploadToken(String key) 
	{
		Auth auth = Auth.create(accessKey, secretKey);
		return auth.uploadToken(bucketName, key);
	}
}
