package com.jkm.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.dao.PolicyOrderDao;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.entity.PolicyOrder;
import com.jkm.entity.TbContactInfo;
import com.jkm.enums.*;
import com.jkm.service.ContactInfoService;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.PolicyOrderService;
import com.jkm.service.hy.HySdkService;
import com.jkm.service.hy.entity.HyPostPolicyOrderRequest;
import com.jkm.service.hy.helper.HySdkConstans;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.IdcardInfoExtractor;
import com.jkm.util.SnGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.regexp.RE;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
public class PolicyOrderServiceImpl implements PolicyOrderService {

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
    public void batchBuyPolicy(long orderFormId) {
        final OrderForm orderForm = this.orderFormService.selectById(orderFormId).get();
        final List<OrderFormDetail> orderFormDetails = this.orderFormDetailService.selectByOrderFormId(orderFormId);

        //创建保险单
        List<PolicyOrder> policyOrders = Lists.transform(orderFormDetails, new Function<OrderFormDetail, PolicyOrder>() {
            @Override
            public PolicyOrder apply(OrderFormDetail input) {

                PolicyOrder policyOrder = new PolicyOrder();
                policyOrder.setOrderFormDetailId(input.getId());

                final TbContactInfo info = contactInfoService.selectById(input.getPassengerId()).get();
                if(input.getPiaoType() == EnumTrainTicketType.CHILDREN.getId()){
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
                    if(input.getPassportTypeSeId() == EnumCertificatesType.SECOND_ID_CARD.getId()){
                        final IdcardInfoExtractor idcardInfo=new IdcardInfoExtractor(input.getPassportSeNo());
                        policyOrder.setGender(idcardInfo.getGender());
                        policyOrder.setCardType(EnumCardType.SECOND_ID_CARD.getId());
                        policyOrder.setCardNo(input.getPassportSeNo());
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
            request.setCardType(input.getCardType());
            request.setGender(input.getGender());
            request.setCardType(input.getCardType());
            request.setCardNo(input.getCardNo());
            request.setBirthday(DateFormatUtil.format(input.getBirthday() , DateFormatUtil.yyyy_MM_dd));
            request.setPhone(input.getPhone());

            final JSONArray jsonArray = this.hySdkService.postPolicyOrder(request);
            final JSONObject jsonObject = jsonArray.getJSONObject(0);
            input.setPolicyNo(jsonObject.getString("policyNo"));
            input.setRemark(jsonObject.getString("resultErrDesc"));
            input.setPrintNo(jsonObject.getString("printNo"));
            input.setApplyNo(jsonObject.getString("applyNo"));
            if(jsonObject.getInt("resultId") == 0){
                //成功
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_SUCCESS.getId());
            }else{
                input.setStatus(EnumPolicyOrderStatus.POLICY_BUY_FAIL.getId());
            }

            this.policyOrderDao.update(input);
        }
       /* final List<HyPostPolicyOrderRequest> requestList = Lists.transform(policyOrders, new Function<PolicyOrder, HyPostPolicyOrderRequest>() {
            @Override
            public HyPostPolicyOrderRequest apply(PolicyOrder input) {
                final HyPostPolicyOrderRequest request = new HyPostPolicyOrderRequest();
                request.setUsername(HySdkConstans.USERNAME);
                request.setMethod(EnumHTHYMethodCode.POST_POLICY_ORDER.getCode());
                request.setReqtime(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss));
                request.setInsProductNo(input.getInsProductNo());
                request.setFlightDate(input.getFlightDate());
                request.setFlightNumber(input.getFlightNumber());
                request.setSerialNo(input.getSerialNo());
                request.setContractName(input.getContractName());
                request.setCardType(input.getCardType());
                request.setGender(input.getGender());
                request.setCardType(input.getCardType());
                request.setCardNo(input.getCardNo());
                request.setBirthday(DateFormatUtil.format(input.getBirthday() , DateFormatUtil.yyyy_MM_dd));
                request.setPhone(input.getPhone());
                return request;
            }
        });*/

    }

}
