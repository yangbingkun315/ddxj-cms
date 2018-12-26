package net.zn.ddxj.utils;


import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import net.zn.ddxj.utils.PropertiesUtils;

import com.qiniu.storage.Configuration;

/**
 * Author:Quintin Tang    Date:2017/3/2-9:28.
 */
public class QiNiuUtil {

    public String upload(byte[] file) {
        //构造一个带指定Zone对象的配置类
        //机房   Zone对象 华东  Zone.zone0()华北Zone.zone1()华南Zone.zone2()北美Zone.zoneNa0()
        Configuration cfg = new Configuration(Zone.zone0());
        //创建上传对象
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, null, getToekn());
            //打印返回的信息
            System.out.println(res.bodyString());
            return res.bodyString();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
                return r.bodyString();
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }

    public String getToekn() {
        //七牛的accesskey
        String accessKey = PropertiesUtils.getPropertiesByName("access_key");
        //七牛的secrectkey
        String secretKey = PropertiesUtils.getPropertiesByName("secret_key");
        //空间名称
        String bucket = PropertiesUtils.getPropertiesByName("bucket_name");
        //密钥配置
        Auth auth = Auth.create(accessKey, secretKey);
        //获取token
        String upToken = auth.uploadToken(bucket);
        //覆盖上传
        //String key = "file key";
        //String upToken = auth.uploadToken(bucket, key);
        //覆盖上传除了需要简单上传所需要的信息之外，还需要想进行覆盖的文件名称，这个文件名称同时可是客户端上传代码中指定的文件名，两者必须一致。
        System.out.println(upToken);
        return upToken;
    }

}