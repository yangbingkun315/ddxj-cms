package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.CreditUrgentContacts;

public interface CreditUrgentContactsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditUrgentContacts record);

    int insertSelective(CreditUrgentContacts record);

    CreditUrgentContacts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditUrgentContacts record);

    int updateByPrimaryKey(CreditUrgentContacts record);
    
    CreditUrgentContacts getCreditUrgentContactsById(int id);

	int delCreditUrgentContactRecord(Integer userId);

	int deleteUrgent(Integer id);
}