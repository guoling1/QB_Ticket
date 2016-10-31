package com.jkm.service.ys.entity;


import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.service.ys.helper.YsSdkSignUtil;
import com.jkm.service.ys.helper.serialize.YsSdkSerializeUtil;
import lombok.Data;

import java.util.Map;

/**
 * Created by yuxiang on 2016-09-29.
 */
@Data
public class CanCheckSignResponse {
    private String sign;

    /**
     * 签名是否正确
     *
     * @return
     */
    public boolean isSignCorrect() {
        final Map<String, String> stringStringMap =
                YsSdkSerializeUtil.converterObjectToMap(this);
        return YsSdkSignUtil.checkSign(stringStringMap,
                YsSdkConstants.SIGN_KEY, getSign());
    }
}
