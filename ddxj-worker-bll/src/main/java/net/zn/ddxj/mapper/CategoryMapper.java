package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.zn.ddxj.entity.Category;
import net.zn.ddxj.vo.CmsRequestVo;
@Mapper
public interface CategoryMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Category record);

	int insertSelective(Category record);

	Category selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Category record);

	int updateByPrimaryKey(Category record);

	List<Category> getCategoryByUserId(Integer userId);
	
	List<Category> getCategoryByType(Integer categoryType);
	
	List<Category> getCategoryByRecruit(Integer recruitId);
	
	List<Category> queryCategoryList(CmsRequestVo requestVo);//查询工种列表
	
	List<Category> queryParentCategory();//查询工种上级
	
	int updateCategoryById(Integer id);//删除工种
	
	String getCategoryNameById(Integer id);//查询工种名称
}