package com.jkm.notifiersdk.helper;

import com.jkm.notifiersdk.enums.EnumYmSkdRequestBusinessType;
import com.jkm.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created on 16/8/2.
 *
 * @author hutao
 * @version 1.0
 */
@Slf4j
public class LogYmSdkRequestPostProcessor implements YmSdkRequestPostProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final EnumYmSkdRequestBusinessType businessType,
                       final Map<String, String> requestParams,
                       final String retCode,
                       final String response,
                       final long millisecond,
                       final String remark) {
        if (retCode.equals("0")) {
            if (log.isDebugEnabled()) {
                log.debug("短信sdk业务请求[{}]成功,请求内容[{}],请求耗时[{}]",
                        businessType.getMsg(), JsonUtil.toJsonString(requestParams), millisecond);
            }
        } else {
            log.error("短信sdk业务请求[{}]失败:code=[{}],response=[{}],请求内容[{}]",
                    businessType.getMsg(), retCode, response, JsonUtil.toJsonString(requestParams));
        }
    }
}
