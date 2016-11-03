package com.jkm.dao;

import com.jkm.entity.TbContactInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
public interface ContactInfoDao {

    /**
     * 根据用户id和证件号查找用户信息
     *
     * @param contactInfo
     * @return
     */
    TbContactInfo findByUidAndIdenty(TbContactInfo contactInfo);

    /**
     * 插入
     *
     * @param contactInfo
     */
    long insert(TbContactInfo contactInfo);

    /**
     * 更新
     *
     * @param contactInfo
     * @return
     */
    int update(TbContactInfo contactInfo);

    /**
     * 按id
     *
     * @param id
     * @return
     */
    TbContactInfo selectById(@Param("id") long id);

    /**
     * 按ids
     *
     * @param ids
     * @return
     */
    List<TbContactInfo> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 按id
     *
     * @param uid
     * @return
     */
    TbContactInfo selectByUid(@Param("uid") String uid);

    /**
     * 根据身份证号查询是否有记录
     * @param identy
     * @return
     */
    int selectCountByIdenty(@Param("identy") String identy);

    /**
     * 根据id更改状态
     * @param id
     * @return
     */
    int updateStatus(long id);
    /**
     * 根据主键更新
     *
     * @param contactInfo
     * @return
     */
    int updateByPrimaryKeySelective(TbContactInfo contactInfo);
}
