package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jkm.entity.BankCardBin;
import com.jkm.service.BankCardBinService;
import com.jkm.service.BaseBankCardBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/8.
 */
@Service
public class BankCardBinServiceImpl implements BankCardBinService {

    @Autowired
    private BaseBankCardBinService baseBankCardBinService;
    private final static List<Integer> BIN_LENGTH_LIST = Lists.newArrayList(
            9, 6, 10, 8, 7, 5, 4, 3
    );

    @Override
    public Optional<BankCardBin> CardNoInfo(String cardNo) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(cardNo) && cardNo.length() >= 10, "卡号不正确");
        for (final int binLength : BIN_LENGTH_LIST) {
            final Optional<BankCardBin> cardBinOptional = baseBankCardBinService.loadByBinNo(cardNo.substring(0, binLength));
            if (cardBinOptional.isPresent()) {
                return cardBinOptional;
            }
        }
        return Optional.absent();
    }
}
