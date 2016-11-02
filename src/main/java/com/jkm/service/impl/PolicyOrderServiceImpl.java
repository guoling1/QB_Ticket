package com.jkm.service.impl;

import com.jkm.dao.PolicyOrderDao;
import com.jkm.service.PolicyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
public class PolicyOrderServiceImpl implements PolicyOrderService {

    @Autowired
    private PolicyOrderDao policyOrderDao;
}
