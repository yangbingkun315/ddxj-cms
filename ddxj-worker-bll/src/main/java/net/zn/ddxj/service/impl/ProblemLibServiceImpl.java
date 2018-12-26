package net.zn.ddxj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.ProblemLib;
import net.zn.ddxj.mapper.ProblemLibMapper;
import net.zn.ddxj.service.ProblemLibService;
import net.zn.ddxj.vo.RequestVo;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class ProblemLibServiceImpl implements ProblemLibService{
	@Autowired
	private ProblemLibMapper problemLibMapper;
	@Override
	public List<ProblemLib> queryProblemLibList(String keywords) {
		// TODO Auto-generated method stub
		return problemLibMapper.queryProblemLibList(keywords);
	}

	@Override
	public ProblemLib queryProblemLibDetails(Integer keywordsId) {
		// TODO Auto-generated method stub
		return problemLibMapper.queryProblemLibDetails(keywordsId);
	}

	@Override
	public List<ProblemLib> findProblemLibList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return problemLibMapper.findProblemLibList(requestVo);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return problemLibMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ProblemLib record) {
		// TODO Auto-generated method stub
		return problemLibMapper.insert(record);
	}

	@Override
	public int insertSelective(ProblemLib record) {
		// TODO Auto-generated method stub
		return problemLibMapper.insertSelective(record);
	}

	@Override
	public ProblemLib selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return problemLibMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ProblemLib record) {
		// TODO Auto-generated method stub
		return problemLibMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ProblemLib record) {
		// TODO Auto-generated method stub
		return problemLibMapper.updateByPrimaryKey(record);
	}

	@Override
	public int deleteProblemLib(Integer id) {
		// TODO Auto-generated method stub
		return problemLibMapper.deleteProblemLib(id);
	}

}
