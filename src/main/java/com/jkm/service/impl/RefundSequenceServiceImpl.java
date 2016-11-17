package com.jkm.service.impl;

import com.jkm.dao.RefundSequenceDao;
import com.jkm.entity.RefundSequence;
import com.jkm.service.RefundSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * 退款流水
 * 
 **/
@Service
public class RefundSequenceServiceImpl implements RefundSequenceService {
	private final Logger logger = LoggerFactory.getLogger(RefundSequenceServiceImpl.class);
	@Autowired
	private RefundSequenceDao refundSequenceDao;

	@Override
	public RefundSequence selectByPrimaryKey(Long id) {
		return refundSequenceDao.selectByPrimaryKey(id);
	}

	@Override
	public int insertSelective(RefundSequence record) {
		return refundSequenceDao.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(RefundSequence record) {
		return refundSequenceDao.updateByPrimaryKeySelective(record);
	}
}