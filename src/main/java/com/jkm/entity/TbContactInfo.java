package com.jkm.entity;

import com.jkm.entity.helper.UserBankCardSupporter;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TbContactInfo extends BaseEntity{
    /**
     * 三方用户编码
     */
    private String uid;
    /**
     * 生日（格式 1990-01-21）
     */
    private String birthday;
    /**
     * 性别{@link com.jkm.enums.EnumSex}
     */
    private Integer sex;
    /**
     * 证件号
     */
    private String identy;
    /**
     * 电话
     */
    private String tel;
    /**
     * 证件类型{@link com.jkm.enums.EnumCertificatesType}
     */
    private String identyType;
    /**
     * 国家编码
     */
    private String country;
    /**
     * 审核是否通过{@link com.jkm.enums.EnumAddUserStatus}
     */
    private Integer checkStatus;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 地址
     */
    private String address;
    /**
     * 名字
     */
    private String name;
    /**
     * 车票类型{@link com.jkm.enums.EnumTrainTicketType}
     */
    private Integer personType;
    /**
     * 是否是自己（0.是 1.不是）
     */
    private Integer isUserSelf;
    /**
     * 省份名
     */
    private String provinceName;
    /**
     * 省份编码
     */
    private String provinceCode;
    /**
     * 学校编码
     */
    private String schoolCode;
    /**
     * 学校名
     */
    private String schoolName;
    /**
     * 学号
     */
    private String studentNo;
    /**
     * 学制
     */
    private String schoolSystem;
    /**
     *起始名称
     */
    private String preferenceFromStationName;
    /**
     *起始地代码
     */
    private String preferenceFromStationCode;
    /**
     *到达名称
     */
    private String preferenceToStationName;
    /**
     *到达地代号
     */
    private String preferenceToStationCode;
    /**
     *入学年份
     */
    private String enterYear;
    /**
     * 解密后的身份证
     * @param cardNo
     * @return
     */
    public String identyOrg(String identy) {
        return UserBankCardSupporter.decryptCardId(identy);
    }
}
