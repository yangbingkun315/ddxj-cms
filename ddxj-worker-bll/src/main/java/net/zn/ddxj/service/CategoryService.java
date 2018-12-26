package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface CategoryService {
	/**
	 * 根据工种类型查询
	 * @param categoryType
	 * @return
	 */
	List<Category> getCategoryByType(Integer categoryType);
	
	List<Category> queryCategoryList(CmsRequestVo requestVo);//查询工种列表
	
	List<Category> queryParentCategory();//查询工种上级
	
	int deleteCategory(int categoryId);//删除工种
	
	Category queryCategoryDetail(int categoryId);//查询工种详情
	
	ResponseBase updateCategory(RequestVo requestVo);//修改工种	

	int updateCategoryEdit(CmsRequestVo requestVo);//修改工种_cms
}
