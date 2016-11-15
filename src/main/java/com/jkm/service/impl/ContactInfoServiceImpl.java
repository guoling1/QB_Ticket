package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ContactInfoDao;
import com.jkm.entity.TbContactInfo;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.EnumTrainTicketType;
import com.jkm.service.ContactInfoService;
import com.jkm.util.IdcardInfoExtractor;
import com.jkm.util.ValidationUtil;
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
        if((EnumTrainTicketType.ADULT.getId()).equals(tbContactInfo.getPersonType())){
            IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(UserBankCardSupporter.decryptCardId(tbContactInfo.getIdenty()));
            tbContactInfo.setBirthday(idcardInfo.getYear()+"-"+idcardInfo.getMonth()+"-"+idcardInfo.getDay());
        }
        TbContactInfo tt = contactInfoDao.selectOneListByUid(tbContactInfo.getUid());
        if(tt!=null&& (EnumTrainTicketType.CHILDREN.getId()).equals(tbContactInfo.getPersonType())){
            tbContactInfo.setIdenty(tt.getIdenty());
        }
        if(tt==null&&(EnumTrainTicketType.CHILDREN.getId()).equals(tbContactInfo.getPersonType())){
            jo.put("result",false);
            jo.put("message","请先添加成人");
            return jo;
        }
        int count = contactInfoDao.selectCountByIdenty(tbContactInfo.getIdenty());
        if(count>0){
            jo.put("result",false);
            jo.put("message","已有此乘客信息，不能重复添加");
        }else{
            long l = contactInfoDao.insert(tbContactInfo);
            if(l>0){
                jo.put("result",true);
                jo.put("data",tbContactInfo.getId());
                jo.put("message","绑定成功");
            }else{
                jo.put("result",false);
                jo.put("message","新增失败");
            }
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
            if(tbContactInfo.getIdenty()!=null&&!"".equals(tbContactInfo.getIdenty())){
                IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(UserBankCardSupporter.decryptCardId(tbContactInfo.getIdenty()));
                tbContactInfo.setBirthday(idcardInfo.getYear()+"-"+idcardInfo.getMonth()+"-"+idcardInfo.getDay());
            }else{
                TbContactInfo ti = contactInfoDao.selectById(tbContactInfo.getId());
                IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(UserBankCardSupporter.decryptCardId(ti.getIdenty()));
                tbContactInfo.setBirthday(idcardInfo.getYear()+"-"+idcardInfo.getMonth()+"-"+idcardInfo.getDay());
            }

        }
        return contactInfoDao.updateByPrimaryKeySelective(tbContactInfo);
    }

    @Override
    public List<TbContactInfo> selectListByUid(String uid) {
        List<TbContactInfo> list = this.contactInfoDao.selectListByUid(uid);
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                if(list.get(i).getIdenty()!=null&&!"".equals(list.get(i).getIdenty())){
                    list.get(i).setIdenty(UserBankCardSupporter.decryptCardId(list.get(i).getIdenty()));
                }

            }
        }
        return list;
    }


}
