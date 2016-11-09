package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.BankCardBinDao;
import com.jkm.entity.BankCardBin;
import com.jkm.service.BaseBankCardBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2016/11/8.
 */
@Service
public class BaseBankCardBinServiceImpl implements BaseBankCardBinService {

    @Autowired
    private BankCardBinDao bankCardBinDao;


    @Override
    public Optional<BankCardBin> loadByBinNo(final String binNo) {
        return Optional.fromNullable(bankCardBinDao.loadByBinNo(binNo));
    }
}
