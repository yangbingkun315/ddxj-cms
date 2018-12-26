package com.baidu.ueditor.upload;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.qiniu.http.Response;

import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		// FileItemStream fileStream = null;
		// boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

        // ServletFileUpload upload = new ServletFileUpload(
			// 	new DiskFileItemFactory());
        //
        // if ( isAjaxUpload ) {
        //     upload.setHeaderEncoding( "UTF-8" );
        // }

		try {
			// FileItemIterator iterator = upload.getItemIterator(request);
            //
			// while (iterator.hasNext()) {
			// 	fileStream = iterator.next();
            //
			// 	if (!fileStream.isFormField())
			// 		break;
			// 	fileStream = null;
			// }
            //
			// if (fileStream == null) {
			// 	return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			// }
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			if(multipartFile==null){
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			//String originFileName = fileStream.getName();
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			//String physicalPath = (String) conf.get("rootPath") + savePath;
//			String basePath=(String) conf.get("basePath");
//			String physicalPath = basePath + savePath;

			//InputStream is = fileStream.openStream();
//			InputStream is = multipartFile.getInputStream();
//			State storageState = StorageManager.saveFileByInputStream(is,
//					physicalPath, maxSize);
//			is.close();
			BaseState baseState = new BaseState(true);//修改编辑器的上传图片
			//上传到七牛
			Response uploadFile = QiNiuUploadManager.uploadFile(savePath,multipartFile.getBytes());
			String bodyString = uploadFile.bodyString();
			if(!CmsUtils.isNullOrEmpty(bodyString))
			{
				JSONObject json = new JSONObject().parseObject(bodyString);
				if(!CmsUtils.isNullOrEmpty(json.get("key")))
				{
					baseState.putInfo("url", PathFormat.format(savePath));
					baseState.putInfo("type", suffix);
					baseState.putInfo("original", originFileName + suffix);
				}
			}
			return baseState;
		// } catch (FileUploadException e) {
		// 	return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
