package com.jkm.dao;

import java.util.List;
import java.util.Map;

public interface CommonDao {
    List<Map<String, Object>> select(Map<String, Object> map);

    Map<String,Object> selectMap(Map<String, Object> map);

    List<String> selectStr(Map<String, Object> map);

    Integer save(Map<String, Object> map);
}
