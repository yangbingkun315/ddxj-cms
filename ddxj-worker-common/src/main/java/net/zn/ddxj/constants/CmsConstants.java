package net.zn.ddxj.constants;

import net.zn.ddxj.utils.PropertiesUtils;

public class CmsConstants {
	 public static final String TPL_PATH = "/templates/cms";
	 public static final String MD5_SALT = "DDXJ0208";
	 public static final class Tag
	 {
		 public static final String CMS_VERSION = PropertiesUtils.getPropertiesByName("cmsVersion");
		 public static final String RES_URL = PropertiesUtils.getPropertiesByName("res_url");
		 public static final String IMG_RES_URL = PropertiesUtils.getPropertiesByName("img_res_url");
		 public static final String BASE_URL = PropertiesUtils.getPropertiesByName("base_url");
		 public static final String STATIC_URL = PropertiesUtils.getPropertiesByName("static_url") + "/";
		 
		 
	 }
}
