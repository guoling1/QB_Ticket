package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TbContactInfo extends BaseEntity{
    private String appId;
    private String uid;
    private String birthday;
    private Integer sex;
    private String identy;
    private String tel;
    private String identyType;
    private String country;
    private Integer checkStatus;
    private String email;
    private String address;
    private String name;
    private Integer personType;
    private Integer isUserSelf;
    private String provinceName;
    private String provinceCode;
    private String schoolCode;
    private String schoolName;
    private String studentNo;
    private String schoolSystem;
    private String preferenceFromStationName;
    private String preferenceFromStationCode;
    private String preferenceToStationName;
    private String preferenceToStationCode;
    private String enterYear;
}
