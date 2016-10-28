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
    int delete(long userId);

    /**
     * 更新乘客
     */
    int updata(ContactForm contactForm);

    /**
     * 根据姓名查询
     */
    List<ContactForm> selectByUserName(String userName);

    /**
     * 按uid查询
     *
     * @return
     */
    Optional<ContactForm> selectByUid(long uid);

    /**
     * 按id查询
     *
     * @return
     */
    Optional<ContactForm> selectById(long id);

}
