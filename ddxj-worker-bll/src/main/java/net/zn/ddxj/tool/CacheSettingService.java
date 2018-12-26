package net.zn.ddxj.tool;

import net.zn.ddxj.entity.Setting;

/**
 * 缓存数据设置
 * @author ddxj
 *
 */
public interface CacheSettingService {

	public String findSettingValue(String key);
	public String findSettingValue(String key,boolean cache);
    public Setting findSetting(String key);
    public void saveSetting(String key, String value);
}
