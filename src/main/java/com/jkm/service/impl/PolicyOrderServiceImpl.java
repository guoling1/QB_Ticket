package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.dao.PolicyOrderDao;
import com.jkm.entity.*;
import com.jkm.enums.*;
import com.jkm.service.*;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyPostPolicyOrderRequest;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.IdcardInfoExtractor;
import com.jkm.util.SnGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
public class PolicyOrderServiceImpl implements PolicyOrderService {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PolicyOrderServiceImpl.class);

    @Autowired
    private PolicyOrderDao policyOrderDao;
    @Autowired
    private HySdkService hySdkService;
    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private OrderFormDetailService orderFormDetailService;
    @Autowired
    private ContactInfoService contactInfoService;
    @Autowired
    private GrabTicketFormService grabTicketFormService;
    /**
     * {@inheritDoc}
     *
     * @param policyOrder
     */
    @Override
    public void init(PolicyOrder policyOrder) {
        this.policyOrderDao.insert(policyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormDetailId
     * @return
     */
    @Override
    public PolicyOrder getByOrderFormDetailId(long orderFormDetailId) {
        return this.policyOrderDao.selectByOrderFormDetailId(orderFormDetailId);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatusById(long id, EnumPolicyOrderStatus status) {
        this.policyOrderDao.updateStatusById(id, status.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     */
    @Override
    @Transactional
    public void batchBuyPolicy(long orderFormId) {
        final OrderForm orderForm = this.orderFormService.selectById(orderFormId).get();
        //没买套餐,不买保险
        if(orderForm.getBuyTicketPackageId() == EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()){
            return;
        }
        log.info("订单orderFromId:" + orderFormId + "申请购买保险!!!");
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderFormId);
        //创建保险单
        List<PolicyOrder> policyOrders = Lists.transform(orderFormDetails, new Function<OrderFormDetail, PolicyOrder>() {
            @Override
            public PolicyOrder apply(OrderFormDetail input) {
                PolicyOrder policyOrder = new PolicyOrder();
                policyOrder.setOrderFormDetailId(input.getId());
                final Optional<TbContactInfo> tbContactInfoOptional = contactInfoService.selectById(input.getPassengerId());
                Preconditions.checkArgument(tbContactInfoOptional.isPresent(), "乘客信息不存在");
                final TbContactInfo info = tbContactInfoOptional.get();
                if(input.getPiaoType().equals(EnumTrainTicketType.CHILDREN.getId())){
                    //儿童
                    //保险产品代码
                    if(orderForm.getBuyTicketPackageId() == EnumBuyTicketPackageType.TICKET_PACKAGE_SECOND.getId()){
                        policyOrder.setInsProductNo(HySdkConstans.CHILD);
                    }else{
                        policyOrder.setInsProductNo(HySdkConstans.PERSON);
                    }
                    if(info.getSex() == EnumSex.FEMALE.getId()){
                        //女
                        policyOrder.setGender("F");
                    }else{
                        //男
                        policyOrder.setGender("M");
                    }
                    policyOrder.setCardType(9);
                    policyOrder.setCardNo(info.getBirthday());
                    policyOrder.setBirthday(DateFormatUtil.parse(info.getBirthday(), DateFormatUtil.yyyy_MM_dd));
                }else{
                    //成人
                    // 保险产品代码
                    if(orderForm.getBuyTicketPackageId() == EnumBuyTicketPackageType.TICKET_PACKAGE_SECOND.getId()){
                        policyOrder.setInsProductNo(HySdkConstans.MAN);
                    }else{
                        policyOrder.setInsProductNo(HySdkConstans.PERSON);
                    }
                    // 如果是身份证, 则截取
                    if(input.getPassportTypeSeId().equals(EnumCertificatesType.SECOND_ID_CARD.getId())){
                        final IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(input.getPassportSeNoPlain());
                        policyOrder.setGender(idcardInfo.getGender());
                        policyOrder.setCardType(EnumCardType.SECOND_ID_CARD.getId());
                        policyOrder.setCardNo(input.getPassportSeNoPlain());
                        policyOrder.setBirthday(idcardInfo.getBirthday());
                    }else{
                        //如果是其他证件, 乘客信息取
                        if(info.getSex() == EnumSex.FEMALE.getId()){
                            //女
                            policyOrder.setGender("F");
                        }else{
                            //男
                            policyOrder.setGender("M");
                        }
                        policyOrder.setCardType(EnumCardType.OTHER.getId());
                        policyOrder.setCardNo(info.getIdenty());
                        policyOrder.setBirthday(DateFormatUtil.parse(info.getBirthday(), DateFormatUtil.yyyy_MM_dd));
                    }

                }
                policyOrder.setFlightDate(orderForm.getStartDate() + " " + orderForm.getStartTime());
                policyOrder.setFlightNumber(orderForm.getCheci());
                policyOrder.setSerialNo(SnGenerator.generate());
                policyOrder.setContractName(input.getPassengerName());
                //TODO
                policyOrder.setContractType("1");
                policyOrder.setPhone(orderForm.getMobile());
                policyOrder.setStatus(EnumPolicyOrderStatus.INIT.getId());
                policyOrderDao.insert(policyOrder);
                return policyOrder;
            }
        });
        for (PolicyOrder input : policyOrders){
            final HyPostPolicyOrderRequest request = new HyPostPolicyOrderRequest();
            request.setUsername(HySdkConstans.USERNAME);
            request.setMethod(EnumHTHYMethodCode.POST_POLICY_ORDER.getCode());
            request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
            request.setInsProductNo(input.getInsProductNo());
            request.setFlightDate(input.getFlightDate());
            request.setFlightNumber(input.getFlightNumber());
            request.setSerialNo(input.getSerialNo());
            request.setContractName(input.getContractName());
            request.setContractType(String.valueOf(input.getCardType()));
            request.setGender(input.getGender());
            request.setCardType(input.getCardType());
            request.setCardNo(input.getCardNo());
            request.setBirthday(DateFormatUtil.format(input.getBirthday() , DateFormatUtil.yyyy_MM_dd));
            request.setPhone(input.getPhone());
            log.info("订单orderFromId:" + orderFormId + "小订单:"+ input.getOrderFormDetailId() + "申请购买保险!!!发送请求中...........");
            final JSONArray jsonArray = this.hySdkService.postPolicyOrder(request);
            final JSONObject jsonObject = jsonArray.getJSONObject(0);

            if(jsonObject.getInt("resultId") == 0){
                log.info("订单orderFromId:" + orderFormId + "小订单:"+ input.getOrderFormDetailId() + "申请购买保险!!!保险购买成功...........");
                //成功
                input.setPolicyNo(jsonObject.getString("policyNo"));
                input.setRemark(jsonObject.getString("resultErrDesc"));
                input.setPrintNo(jsonObject.getString("printNo"));
                input.setApplyNo(jsonObject.getString("applyNo"));
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_SUCCESS.getId());
            }else{
                log.error("订单orderFromId:" + orderFormId + "小订单:"+ input.getOrderFormDetailId() +
                        "申请购买保险!!!保险购买失败......失败原因:" + jsonObject.getString("resultErrDesc"));
               // input.setPolicyNo(jsonObject.getString("policyNo"));
                input.setRemark(jsonObject.getString("resultErrDesc"));
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_FAIL.getId());
            }
            this.policyOrderDao.update(input);
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param grabTicketFormId
     */
    @Transactional
    @Override
    public void batchBuyGrabPolicy(long grabTicketFormId) {
        final GrabTicketForm orderForm = this.grabTicketFormService.selectById(grabTicketFormId).get();
        //没买套餐,不买保险
        if(orderForm.getBuyTicketPackage() == EnumBuyTicketPackageType.TICKET_PACKAGE_FIRST.getId()){
            return;
        }
        log.info("抢票单grabTicketFormId:" + grabTicketFormId + "申请购买保险!!!");
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByGrabTicketFormId(grabTicketFormId);
        //创建保险单
        List<PolicyOrder> policyOrders = Lists.transform(orderFormDetails, new Function<OrderFormDetail, PolicyOrder>() {
            @Override
            public PolicyOrder apply(OrderFormDetail input) {
                PolicyOrder policyOrder = new PolicyOrder();
                policyOrder.setOrderFormDetailId(input.getId());
                final Optional<TbContactInfo> tbContactInfoOptional = contactInfoService.selectById(input.getPassengerId());
                Preconditions.checkArgument(tbContactInfoOptional.isPresent(), "乘客信息不存在");
                final TbContactInfo info = tbContactInfoOptional.get();
                if(input.getPiaoType().equals(EnumTrainTicketType.CHILDREN.getId())){
                    //儿童
                    //保险产品代码
                    if(orderForm.getBuyTicketPackage() == EnumBuyTicketPackageType.TICKET_PACKAGE_SECOND.getId()){
                        policyOrder.setInsProductNo(HySdkConstans.CHILD);
                    }else{
                        policyOrder.setInsProductNo(HySdkConstans.PERSON);
                    }
                    if(info.getSex() == EnumSex.FEMALE.getId()){
                        //女
                        policyOrder.setGender("F");
                    }else{
                        //男
                        policyOrder.setGender("M");
                    }
                    policyOrder.setCardType(9);
                    policyOrder.setCardNo(info.getBirthday());
                    policyOrder.setBirthday(DateFormatUtil.parse(info.getBirthday(), DateFormatUtil.yyyy_MM_dd));
                }else{
                    //成人
                    // 保险产品代码
                    if(orderForm.getBuyTicketPackage() == EnumBuyTicketPackageType.TICKET_PACKAGE_SECOND.getId()){
                        policyOrder.setInsProductNo(HySdkConstans.MAN);
                    }else{
                        policyOrder.setInsProductNo(HySdkConstans.PERSON);
                    }
                    // 如果是身份证, 则截取
                    if(input.getPassportTypeSeId() .equals( EnumCertificatesType.SECOND_ID_CARD.getId())){
                        final IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(input.getPassportSeNoPlain());
                        policyOrder.setGender(idcardInfo.getGender());
                        policyOrder.setCardType(EnumCardType.SECOND_ID_CARD.getId());
                        policyOrder.setCardNo(input.getPassportSeNoPlain());
                        policyOrder.setBirthday(idcardInfo.getBirthday());
                    }else{
                        //如果是其他证件, 乘客信息取
                        if(info.getSex() == EnumSex.FEMALE.getId()){
                            //女
                            policyOrder.setGender("F");
                        }else{
                            //男
                            policyOrder.setGender("M");
                        }
                        policyOrder.setCardType(EnumCardType.OTHER.getId());
                        policyOrder.setCardNo(info.getIdenty());
                        policyOrder.setBirthday(DateFormatUtil.parse(info.getBirthday(), DateFormatUtil.yyyy_MM_dd));
                    }

                }
                policyOrder.setFlightDate(orderForm.getStartDate() + " " + orderForm.getStartTime());
                policyOrder.setSerialNo(SnGenerator.generate());
                policyOrder.setContractName(input.getPassengerName());
                //TODO
                policyOrder.setContractType("1");
                policyOrder.setPhone(orderForm.getPhone());
                policyOrder.setStatus(EnumPolicyOrderStatus.INIT.getId());
                policyOrderDao.insert(policyOrder);
                return policyOrder;
            }
        });
        for (PolicyOrder input : policyOrders){
            final HyPostPolicyOrderRequest request = new HyPostPolicyOrderRequest();
            request.setUsername(HySdkConstans.USERNAME);
            request.setMethod(EnumHTHYMethodCode.POST_POLICY_ORDER.getCode());
            request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
            request.setInsProductNo(input.getInsProductNo());
            request.setFlightDate(input.getFlightDate());
            request.setFlightNumber(input.getFlightNumber());
            request.setSerialNo(input.getSerialNo());
            request.setContractName(input.getContractName());
            request.setContractType(String.valueOf(input.getCardType()));
            request.setGender(input.getGender());
            request.setCardType(input.getCardType());
            request.setCardNo(input.getCardNo());
            request.setBirthday(DateFormatUtil.format(input.getBirthday() , DateFormatUtil.yyyy_MM_dd));
            request.setPhone(input.getPhone());
            log.info("抢票单grabTicketFormId:" + grabTicketFormId + "小订单:"+ input.getOrderFormDetailId() + "申请购买保险!!!发送请求中...........");
            final JSONArray jsonArray = this.hySdkService.postPolicyOrder(request);
            final JSONObject jsonObject = jsonArray.getJSONObject(0);
            if(jsonObject.getInt("resultId") == 0){
                //成功
                log.info("抢票单grabTicketFormId:" + grabTicketFormId + "小订单:"+ input.getOrderFormDetailId() + "申请购买保险!!!保险购买成功...........");
                input.setPolicyNo(jsonObject.getString("policyNo"));
                input.setRemark(jsonObject.getString("resultErrDesc"));
                input.setPrintNo(jsonObject.getString("printNo"));
                input.setApplyNo(jsonObject.getString("applyNo"));
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_SUCCESS.getId());
            }else{
                log.error("抢票单grabTicketFormId:" + grabTicketFormId + "小订单:"+ input.getOrderFormDetailId() +
                        "申请购买保险!!!保险购买失败......失败原因:" + jsonObject.getString("resultErrDesc"));
                input.setPolicyNo(jsonObject.getString("policyNo"));
                input.setRemark(jsonObject.getString("resultErrDesc"));
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_FAIL.getId());
            }
            this.policyOrderDao.update(input);
        }
    }

}
