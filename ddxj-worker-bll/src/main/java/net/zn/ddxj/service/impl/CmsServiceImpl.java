package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.CmsLogs;
import net.zn.ddxj.entity.CmsResource;
import net.zn.ddxj.entity.CmsRole;
import net.zn.ddxj.entity.CmsStorage;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.mapper.CircleMapper;
import net.zn.ddxj.mapper.CmsLogsMapper;
import net.zn.ddxj.mapper.CmsResourceMapper;
import net.zn.ddxj.mapper.CmsRoleMapper;
import net.zn.ddxj.mapper.CmsStorageMapper;
import net.zn.ddxj.mapper.CmsUserMapper;
import net.zn.ddxj.mapper.RealAuthMapper;
import net.zn.ddxj.mapper.RecruitMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.mapper.UserWithdrawMapper;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.ZtreeView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class CmsServiceImpl implements CmsService {

	@Autowired
	private CmsUserMapper cmsUserMapper;
	@Autowired
	private CmsRoleMapper cmsRoleMapper;
	@Autowired
	private CmsResourceMapper cmsResourceMapper;
	@Autowired
	private CmsLogsMapper cmsLogsMapper;
	@Autowired
	private CmsStorageMapper cmsStorageMapper;
	@Autowired
	private RecruitMapper recruitMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserTransferMapper userTransferMapper;
	@Autowired
	private UserWithdrawMapper userWithdrawMapper;
	@Autowired
	private RealAuthMapper realAuthMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Override
	public CmsUser findByUserName(String userName) {
		// TODO Auto-generated method stub
		return cmsUserMapper.findByUserName(userName);
	}
	@Override
	public List<CmsUser> findUserList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return cmsUserMapper.findUserList(requestVo);
	}
	@Override
	public List<CmsRole> findRoleList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return cmsRoleMapper.findRoleList(requestVo);
	}
	@Override
	public List<CmsResource> findResourceList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return cmsResourceMapper.findResourceList(requestVo);
	}
	@Override
	public CmsUser queryUserById(Integer userId) {
		// TODO Auto-generated method stub
		return cmsUserMapper.selectByPrimaryKey(userId);
	}
	@Override
	public int insertCmsUser(CmsUser user) {
		return cmsUserMapper.insertSelective(user);
	}
	@Override
	public int updateCmsUser(CmsUser user) {
		return cmsUserMapper.updateByPrimaryKeySelective(user);
	}
	@Override
	public int updateRole(CmsRole role) {
		//修改
		if(!CmsUtils.isNullOrEmpty(role.getId()) && role.getId() > 0)
		{
			role.setUpdateTime(new Date());
			cmsRoleMapper.updateByPrimaryKeySelective(role);
		}
		else
		{
			role.setCreateTime(new Date());
			role.setUpdateTime(new Date());
			cmsRoleMapper.insertSelective(role);
		}
		
		
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public CmsRole findRoleById(Integer roleId) {
		// TODO Auto-generated method stub
		return cmsRoleMapper.selectByPrimaryKey(roleId);
	}
	@Override
	public int deleteRole(Integer id) {
		// TODO Auto-generated method stub
		int deleteByPrimaryKey = cmsRoleMapper.deleteByPrimaryKey(id);
		if(deleteByPrimaryKey > 0)
		{
			cmsRoleMapper.deleteRoleResource(id);
		}
		return deleteByPrimaryKey;
	}
	@Override
	public List<ZtreeView> findRoleResourceList(Integer roleId) {
		List<ZtreeView> resulTreeNodes = new ArrayList<ZtreeView>();
		CmsRole role = findRoleById(roleId);
		List<CmsResource> roleResources = role.getResourceList();
		ZtreeView node;
		List<CmsResource> all = findResourceList(null);
		for (CmsResource resource : all) {
			node = new ZtreeView();
			node.setId(Long.valueOf(resource.getId()));
			if (resource.getResourceParent() == null) {
				node.setpId(0L);
			} else {
				node.setpId(Long.valueOf(resource.getResourceParent().getId()));
			}
			node.setName(resource.getResourceName());
			if (roleResources != null && roleResources.contains(resource)) {
				node.setChecked(true);
			}
			resulTreeNodes.add(node);
		}
		return resulTreeNodes;
	}
	@Override
	@Async
	public void updateRoleResource(Integer roleId, String resourceIds) {
		cmsRoleMapper.deleteRoleResource(roleId);//删除所有该角色的资源
		if(!CmsUtils.isNullOrEmpty(resourceIds)){
			String[] resourceIdsObj = resourceIds.split(",");
			for (int i = 0; i < resourceIdsObj.length; i++) {
				if(CmsUtils.isNullOrEmpty(resourceIdsObj[i]) || "0".equals(resourceIdsObj[i])){
					continue;
				}
				
				Integer rid = Integer.parseInt(resourceIdsObj[i]);
				cmsRoleMapper.addRoleResource(roleId, rid);//重新添加权限
			}
		}
		
	}
	@Override
	public CmsResource findResourceById(Integer resourceId) {
		// TODO Auto-generated method stub
		return cmsResourceMapper.selectByPrimaryKey(resourceId);
	}
	@Override
	@Async
	public void addOperateLogs(String idAddress,String content,Integer userId) {
		if(!CmsUtils.isNullOrEmpty(userId))
		{
			CmsLogs cmsLogs = new CmsLogs();
			cmsLogs.setLogContent(content);
			cmsLogs.setLogIpAddress(idAddress);
			cmsLogs.setUserId(userId);
			cmsLogs.setLogTime(new Date());
			cmsLogsMapper.insertSelective(cmsLogs);
		}
	}
	@Override
	public List<CmsLogs> findLogsByUserId(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return cmsLogsMapper.findLogsByUserId(requestVo);
	}
	
	@Override
	public int deleteCmsUser(Integer id) {
		
		return cmsRoleMapper.deleteByPrimaryKey(id);
	}
	@Override
	public List<CmsResource> findParentResourceList() {
		// TODO Auto-generated method stub
		return cmsResourceMapper.findParentResourceList();
	}
	@Override
	public List<CmsResource> findMenuResourceList() {
		// TODO Auto-generated method stub
		return cmsResourceMapper.findMenuResourceList();
	}
	@Override
	public int updateResource(CmsRequestVo requestVo) {
		CmsResource cmsResource = new CmsResource();
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()) && requestVo.getId() > 0)
		{
			cmsResource = findResourceById(requestVo.getId());
			cmsResource.setUpdateTime(new Date());
		}
		else
		{
			cmsResource.setCreateTime(new Date());
			cmsResource.setUpdateTime(new Date());
		}
		cmsResource.setResourceName(requestVo.getResourceName());
		cmsResource.setIcon(requestVo.getIcon());
		cmsResource.setResourceDescription(requestVo.getResourceDescription());
		cmsResource.setResourceKey(requestVo.getResourceKey());
		if(requestVo.getResourceType() == 1)
		{
			cmsResource.setParentId(requestVo.getParentContentsId());
		}
		else if(requestVo.getResourceType() == 2)
		{
			if(!CmsUtils.isNullOrEmpty(requestVo.getGroupBtnId()))
			{
				cmsResource.setParentId(requestVo.getGroupBtnId());
			}
			else
			{
				cmsResource.setParentId(requestVo.getParentMenuId());
			}
		}
		else if(requestVo.getResourceType() == 3)
		{
			cmsResource.setParentId(requestVo.getParentMenuId());
		}
		else if(requestVo.getResourceType() == 4)
		{
			cmsResource.setParentId(requestVo.getParentMenuId());
		}
		cmsResource.setResourceLevel(requestVo.getResourceLevel());
		cmsResource.setResourceType(requestVo.getResourceType());
		cmsResource.setResourceUrl(requestVo.getResourceUrl());
		cmsResource.setResourceSort(requestVo.getResourceSort());
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()) && requestVo.getId() > 0)
		{
			cmsResourceMapper.updateByPrimaryKeySelective(cmsResource);
		}
		else
		{
			cmsResourceMapper.insertSelective(cmsResource);
		}
		return 1;
	}
	@Override
	public int deleteResource(Integer id) {
		// TODO Auto-generated method stub
		return cmsResourceMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int changeUserPassword(int id, String newPassword) {
		// TODO Auto-generated method stub
		return cmsUserMapper.changeUserPassword(id,newPassword);
	}
	@Override
	public List<CmsResource> findResourceBtnGroup(Integer parentMenuId) {
		// TODO Auto-generated method stub
		return cmsResourceMapper.findResourceBtnGroup(parentMenuId);
	}
	@Override
	public int addAllQiNiuStorage() {
		//构造一个带指定Zone对象的配置类
			Configuration cfg = new Configuration(Zone.zone0());
			//...其他参数参考类注释
			String accessKey = QiNiuUploadManager.accessKey;
			String secretKey = QiNiuUploadManager.secretKey;
			String bucket = QiNiuUploadManager.bucketName;
			Auth auth = Auth.create(accessKey, secretKey);
			BucketManager bucketManager = new BucketManager(auth, cfg);
			//文件名前缀
			String prefix = "";
			//每次迭代的长度限制，最大1000，推荐值 1000
			int limit = 1000;
			//指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
			String delimiter = "";
			//列举空间文件列表
			BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
			int i = 0;
			while (fileListIterator.hasNext()) {
			    //处理获取的file list结果
			    FileInfo[] items = fileListIterator.next();
			    for (FileInfo item : items) {
			        System.out.println(item.key);
			        
			        CmsStorage storage = new CmsStorage();
			        storage.setStorageHash(item.hash);
			        storage.setStorageKey(item.key);
			        storage.setStorageSize(item.fsize/1024+"KB");
			        storage.setStorageTime(DateUtils.getDateTime(item.putTime /10000));
			        storage.setStorageType(item.mimeType);
			        storage.setCreateTime(new Date());
			        cmsStorageMapper.insertSelective(storage);
			        i++;
			    }
			}
			System.out.println("同步总数量为"+i);
		return 0;
	}
	@Override
	public List<CmsStorage> queryAllStorage(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return cmsStorageMapper.queryAllStorage(requestVo);
	}
	@Override
	public int deleteCmsStorage(CmsRequestVo requestVo) {
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		//...其他参数参考类注释
		//...其他参数参考类注释
		String accessKey = QiNiuUploadManager.accessKey;
		String secretKey = QiNiuUploadManager.secretKey;
		String bucket = QiNiuUploadManager.bucketName;
		Auth auth = Auth.create(accessKey, secretKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
		    bucketManager.delete(bucket, requestVo.getStorageKey());
		    cmsStorageMapper.deleteByPrimaryKey(requestVo.getId());
		} catch (QiniuException ex) {
		    //如果遇到异常，说明删除失败
		    System.err.println(ex.code());
		    System.err.println(ex.response.toString());
		}
		return 1;
	}
	@Override
	public int addStorate(CmsStorage cmsStorage) {
		// TODO Auto-generated method stub
		return cmsStorageMapper.insertSelective(cmsStorage);
	}
	@Override
	public List<Map<String,Object>> findRecruitCount() {
		// TODO Auto-generated method stub
		return recruitMapper.findRecruitCount();
	}
	@Override
	public List<Map<String, Object>> findUserCount() {
		// TODO Auto-generated method stub
		return userMapper.findUserCount();
	}
	@Override
	public CmsUser findCmsUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return cmsUserMapper.findCmsUserByUserName(userName);
	}
	@Override
	public BigDecimal findRegisterAwardMoneyCount() {
		// TODO Auto-generated method stub
		return userTransferMapper.findRegisterAwardMoneyCount();
	}
	@Override
	public BigDecimal findWithdrawSuccessCount() {
		// TODO Auto-generated method stub
		return userWithdrawMapper.findWithdrawSuccessCount();
	}
	@Override
	public int findRealAuthCount() {
		// TODO Auto-generated method stub
		return realAuthMapper.findRealAuthCount();
	}
	@Override
	public int findCircleCount() {
		// TODO Auto-generated method stub
		return circleMapper.findCircleCount();
	}

}
