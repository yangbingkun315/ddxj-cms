package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.mapper.CategoryMapper;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public List<Category> getCategoryByType(Integer categoryType) {
		return categoryMapper.getCategoryByType(categoryType);
	}

	@Override
	public List<Category> queryCategoryList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return categoryMapper.queryCategoryList(requestVo);
	}

	@Override
	public int deleteCategory(int categoryId) {
		// TODO Auto-generated method stub
		return categoryMapper.updateCategoryById(categoryId);
	}

	@Override
	public Category queryCategoryDetail(int categoryId) {
		// TODO Auto-generated method stub
		return categoryMapper.selectByPrimaryKey(categoryId);
	}

	@Override
	public List<Category> queryParentCategory() {
		// TODO Auto-generated method stub
		return categoryMapper.queryParentCategory();
	}

	@Override
	public ResponseBase updateCategory(RequestVo requestVo) {
		// TODO Auto-generated method stub
		ResponseBase result = ResponseBase.getInitResponse();
		Category  category = null;
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()))
		{
			category = categoryMapper.selectByPrimaryKey(requestVo.getId());
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorySort()))
			{
				category.setCategorySort(requestVo.getCategorySort());
			}
			category.setCategoryName(requestVo.getCategoryName());
			category.setCategoryType(requestVo.getCategoryType());
			if(!CmsUtils.isNullOrEmpty(requestVo.getParentId()))
			{
				category.setParentId(Integer.valueOf(requestVo.getParentId()));
			}
			category.setUpdateTime(new Date());
			categoryMapper.updateByPrimaryKeySelective(category);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("更新成功");
		}
		else
		{
			category = new Category();
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorySort()))
			{
				category.setCategorySort(requestVo.getCategorySort());
			}
			category.setCategoryName(requestVo.getCategoryName());
			category.setCategoryType(requestVo.getCategoryType());
			if(!CmsUtils.isNullOrEmpty(requestVo.getParentId()))
			{
				category.setParentId(Integer.valueOf(requestVo.getParentId()));
			}
			category.setUpdateTime(new Date());
			category.setCreateTime(new Date());
			categoryMapper.insertSelective(category);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("添加成功");
		}
		return result;
	}

	@Override
	public int updateCategoryEdit(CmsRequestVo requestVo) {
		Category category = null;
		int count = 0;
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()) && requestVo.getId() > 0)
		{
			category = categoryMapper.selectByPrimaryKey(requestVo.getId());
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorySort()))
			{
				category.setCategorySort(requestVo.getCategorySort());
			}
			category.setCategoryName(requestVo.getCategoryName());
			category.setCategoryType(requestVo.getCategoryType());
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategoryParentId()))
			{
				category.setParentId(Integer.valueOf(requestVo.getCategoryParentId()));
			}
			category.setUpdateTime(new Date());
			count = categoryMapper.updateByPrimaryKeySelective(category);
		}
		else
		{
			category = new Category();
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategorySort()))
			{
				category.setCategorySort(requestVo.getCategorySort());
			}
			category.setCategoryName(requestVo.getCategoryName());
			category.setCategoryType(requestVo.getCategoryType());
			if(!CmsUtils.isNullOrEmpty(requestVo.getCategoryParentId()))
			{
				category.setParentId(Integer.valueOf(requestVo.getCategoryParentId()));
			}
			category.setUpdateTime(new Date());
			category.setCreateTime(new Date());
			count = categoryMapper.insertSelective(category);
		}
		return count;
	}
}
