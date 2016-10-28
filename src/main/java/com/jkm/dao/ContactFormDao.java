package com.jkm.dao;

import com.jkm.entity.ContactForm;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhangbin on 2016/10/27.
 */
public interface ContactFormDao {

    /**
     * 增加乘客
     * @param contactForm
     */
    void insert(ContactForm contactForm);

    /**
     * 根据乘客userId删除
     * @param userId
     * @return
     */
    int delete(@Param("userId") long userId);

    /**
     * 更新乘客
     * @param contactForm
     * @return
     */
    int update(ContactForm contactForm);

    /**
     * 根据乘客姓名查询
     * @param userName
     * @return
     */
    ContactForm selectByUserName(@Param("userName") String userName);

    /**
     * 按uid 查询
     *
     * @param uid
     * @return
     */
    ContactForm selectByUid(@Param("uid") long uid);

    ContactForm selectById(@Param("id") long id);

}
