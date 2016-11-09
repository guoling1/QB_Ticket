package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.BankCardBin;

/**
 * Created by zhangbin on 2016/11/8.
 */
public interface BankCardBinService {



    /**
     * 分析银行卡号的bin信息
     * @param cardNo
     * @return
     */
    Optional<BankCardBin> analyseCardNo(final String cardNo);
}
