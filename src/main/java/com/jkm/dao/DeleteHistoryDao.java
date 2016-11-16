package com.jkm.dao;

import org.apache.ibatis.annotations.Param;

/**
 * Created by zhangbin on 2016/11/15.
 */
public interface DeleteHistoryDao {

    void delete(@Param("uid") String uid);
}
