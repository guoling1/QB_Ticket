package com.jkm.dao;

import com.jkm.entity.QueryHistory;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangbin on 2016/11/8.
 */
public interface QueryHistoryDao {

    /**
     * 查询记录
     * @param uid
     */
    List<QueryHistory> queryHistory(@Param("uid") String uid);

    /**
     * 插入
     * @param jsonObject
     */
    void save(JSONObject jsonObject);


    /**
     * 更新
     * @param jsonObject
     */
    void update(JSONObject jsonObject);



}
