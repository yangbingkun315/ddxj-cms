package net.zn.ddxj.tool.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zn.ddxj.entity.Setting;
import net.zn.ddxj.mapper.SettingMapper;
import net.zn.ddxj.tool.CacheSettingService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.RedisUtils;
@Service
public class CacheSettingServiceImpl implements CacheSettingService {
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	public SettingMapper settingMapper;
	@Override
	public String findSettingValue(String key) {
		return findSettingValue(key,true);
	}

	@Override
	public String findSettingValue(String key, boolean cache) {
		String value = "";
		if(cache)
		{
			if(redisUtils.exists(String.valueOf(redisUtils.get(key))))
			{
				value = String.valueOf(redisUtils.get(key));
			}
			else
			{
				Setting setting = settingMapper.querySettingByKey(key);
				if(!CmsUtils.isNullOrEmpty(setting))
				{
					redisUtils.set(key, setting.getCfgValue());
					value = setting.getCfgValue();
				}
			}
		}
		else
		{
			value = settingMapper.querySettingByKey(key).getCfgValue();
		}
		return value;
	}

	@Override
	public Setting findSetting(String key) {
		// TODO Auto-generated method stub
		return settingMapper.querySettingByKey(key);
	}

	@Override
	public void saveSetting(String key, String value) {
		Setting setting = settingMapper.querySettingByKey(key);
		if(CmsUtils.isNullOrEmpty(setting))
		{
			setting = new Setting();
			setting.setCfgKey(key);
			setting.setCreateTime(new Date());
			setting.setUpdateTime(new Date());
			setting.setCfgValue(value);
			settingMapper.insertSelective(setting);
		}
		else
		{
			setting.setUpdateTime(new Date());
			setting.setCfgValue(value);
			settingMapper.updateByPrimaryKeySelective(setting);
		}
		redisUtils.set(key, value);
	}


    
}
