package com.jkm.service.impl;

import com.jkm.dao.DeleteHistoryDao;
import com.jkm.service.DeleteHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2016/11/15.
 */
@Service
public class DeleteHistoryServiceImpl implements DeleteHistoryService {

    @Autowired
    private DeleteHistoryDao deleteHistoryDao;


    @Override
    public void delete(String uid) {
        deleteHistoryDao.delete(uid);
    }
}
