package net.zn.ddxj.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.SensitiveKeywords;
import net.zn.ddxj.keywords.SensitiveWordUtil;
import net.zn.ddxj.mapper.SensitiveKeywordsMapper;
import net.zn.ddxj.service.ValidateKeywordsService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.RedisUtils;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class ValidateKeywordsImpl implements ValidateKeywordsService {
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private SensitiveKeywordsMapper sensitiveKeywordsMapper;
	@Override
	public Set<String> validateKeywords(String content)
	{
		Set<String> redisJSON = null;
		if(CmsUtils.isNullOrEmpty(redisUtils.get(Constants.SENSITIVEKEYWORDS)))
		{
			Set<String> sensitiveWordSet = new HashSet<>();
			List<SensitiveKeywords> querySensitiveKeywords = sensitiveKeywordsMapper.querySensitiveKeywordsList();
			for (SensitiveKeywords sensitiveKeywords : querySensitiveKeywords) 
			{
				sensitiveWordSet.add(sensitiveKeywords.getKeywordsContent());
			}
			redisJSON = sensitiveWordSet;
			redisUtils.set(Constants.SENSITIVEKEYWORDS,sensitiveWordSet);
		}
		else
		{
			redisJSON = (Set<String>)redisUtils.get(Constants.SENSITIVEKEYWORDS);
		}
		SensitiveWordUtil.init(redisJSON);
		Set<String> sensitiveWord = SensitiveWordUtil.getSensitiveWord(content, SensitiveWordUtil.MinMatchTYpe);
		
		return sensitiveWord;
	}
}
