package com.jkm.dao;

import com.jkm.entity.BankCardBin;

/**
 * Created by zhangbin on 2016/11/8.
 */
public interface BankCardBinDao {

    BankCardBin loadByBinNo(String binNo);
}
