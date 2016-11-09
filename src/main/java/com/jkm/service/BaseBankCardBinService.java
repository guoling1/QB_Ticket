package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.BankCardBin;

/**
 * Created by zhangbin on 2016/11/8.
 */
public interface BaseBankCardBinService {


    Optional<BankCardBin> loadByBinNo(final String binNo);
}
