package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.TbContactInfo;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
public interface ContactInfoService {

    /**
     * 根据用户id和证件号查找用户信息
     *
     * @param contactInfo
     * @return
     */
    public TbContactInfo findByUidAndIdenty(TbContactInfo contactInfo);

    /**
     * 增加联系人
     *
     * @param contactInfo
     */
    void add(TbContactInfo contactInfo);

    /**
     * 更新
     *
     * @param contactInfo
     * @return
     */
    int update(TbContactInfo contactInfo);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<TbContactInfo> selectById(long id);

    /**
     * 按ids查询
     *
     * @param ids
     */
    List<TbContactInfo> selectByIds(List<Long> ids);

    /**
     * 按id查询
     *
     * @param uid
     * @return
     */
    Optional<TbContactInfo> selectByUid(String uid);

    /**
     * 新增乘客信息
     * @param tbContactInfo
     * @return
     */
    JSONObject insert(TbContactInfo tbContactInfo);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    TbContactInfo selectOne(long id);

    /**
     * 根据用户id更改状态
     * @param id
     * @return
     */
    int updateStatus(long id);
    /**
     * 根据主键更新
     * @param contactInfo
     * @return
     */
    int updateByPrimaryKeySelective(TbContactInfo tbContactInfo);

    /**
     * 根据uid查询用户列表
     * @param uid
     * @return
     */
    List<TbContactInfo> selectListByUid(String uid);

}
