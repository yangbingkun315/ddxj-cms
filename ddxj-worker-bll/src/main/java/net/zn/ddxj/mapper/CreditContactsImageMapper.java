package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CreditContactsImage;

public interface CreditContactsImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditContactsImage record);

    int insertSelective(CreditContactsImage record);

    CreditContactsImage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditContactsImage record);

    int updateByPrimaryKey(CreditContactsImage record);
    
    List<CreditContactsImage> getImageListById(int id);

	int delCreditContactsImageRecord(Integer userId);

	int deleteImages(Integer id);//删除授信图片根据项目授信id
}