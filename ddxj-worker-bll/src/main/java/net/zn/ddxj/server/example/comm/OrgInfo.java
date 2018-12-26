package net.zn.ddxj.server.example.comm;

import net.zn.ddxj.utils.PropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/31.
 */
public class OrgInfo {
    public static String ORG_NAME = PropertiesUtils.getPropertiesByName("ORG_NAME");
    public static String APP_NAME = PropertiesUtils.getPropertiesByName("APP_NAME");
}
