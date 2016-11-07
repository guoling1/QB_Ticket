package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ContactInfoDao;
import com.jkm.entity.TbContactInfo;
import com.jkm.service.ContactInfoService;
import com.jkm.util.IdcardInfoExtractor;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/2.
 */
@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    @Autowired
    private ContactInfoDao contactInfoDao;


    @Override
    public TbContactInfo findByUidAndIdenty(TbContactInfo contactInfo) {
        return contactInfoDao.findByUidAndIdenty(contactInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param contactInfo
     */
    @Override
    public void add(final TbContactInfo contactInfo) {
        this.contactInfoDao.insert(contactInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param contactInfo
     * @return
     */
    @Override
    public int update(final TbContactInfo contactInfo) {
        return this.contactInfoDao.update(contactInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbContactInfo> selectById(final long id) {
        return Optional.fromNullable(this.contactInfoDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<TbContactInfo> selectByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.contactInfoDao.selectByIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param uid
     * @return
     */
    @Override
    public Optional<TbContactInfo> selectByUid(final String uid) {
        return Optional.fromNullable(this.contactInfoDao.selectByUid(uid));
    }


    @Override
    public JSONObject insert(TbContactInfo tbContactInfo) {
        JSONObject jo = new JSONObject();
        tbContactInfo.setCountry("CN");
        tbContactInfo.setCheckStatus(0);
        tbContactInfo.setIsUserSelf(1);
        if("1".equals(tbContactInfo.getIdentyType())){
            IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(tbContactInfo.getIdenty());
            tbContactInfo.setBirthday(idcardInfo.getYear()+"-"+idcardInfo.getMonth()+"-"+idcardInfo.getDay());
        }
        int count = contactInfoDao.selectCountByIdenty(tbContactInfo.getIdenty());
        if(count>0){
            jo.put("result",false);
            jo.put("message","已有此乘客信息，不能重复添加");
        }else{
            jo.put("result",true);
            jo.put("data",count);
            jo.put("message","绑定成功");
        }
        return jo;
    }

    @Override
    public TbContactInfo selectOne(long id) {
        return contactInfoDao.selectById(id);
    }

    @Override
    public int updateStatus(long id) {
        return contactInfoDao.updateStatus(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TbContactInfo tbContactInfo) {
        if("1".equals(tbContactInfo.getIdentyType())){
            IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(tbContactInfo.getIdenty());
            tbContactInfo.setBirthday(idcardInfo.getYear()+"-"+idcardInfo.getMonth()+"-"+idcardInfo.getDay());
        }
        return contactInfoDao.updateByPrimaryKeySelective(tbContactInfo);
    }

    @Override
    public List<TbContactInfo> selectListByUid(String uid) {
        return this.contactInfoDao.selectListByUid(uid);
    }

}
