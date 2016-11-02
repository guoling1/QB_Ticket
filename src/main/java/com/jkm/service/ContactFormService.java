package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.ContactForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangbin on 2016/10/28.
 */
public interface ContactFormService {
    /**
     * 增加用户
     */
    long add(ContactForm contactForm);

    /**
     * 根据id
     * 删除乘客
     */
//    int delete(@Param("id") long id);

    /**
     * 更新乘客
     */
//    int updata(ContactForm contactForm);/

    /**
     * 根据姓名查询
     */
    List<ContactForm> selectByUserName(@Param("userName") String userName);

    /**
     * 按uid查询
     *
     * @param uid
     * @return
     */
    Optional<ContactForm> selectByUid(String uid);

    /**
     * 按id 查询
     *
     * @param id
     * @return
     */
    Optional<ContactForm> selectById(long id);

    /**
     * 按ids 查询
     *
     * @param ids
     * @return
     */
    List<ContactForm> selectByIds(List<Long> ids);
}
