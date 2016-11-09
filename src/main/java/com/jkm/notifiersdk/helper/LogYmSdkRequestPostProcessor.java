package com.jkm.notifiersdk.helper;

import com.jkm.notifiersdk.enums.EnumYmSkdRequestBusinessType;
import com.jkm.util.JsonUtil;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created on 16/8/2.
 *
 * @author hutao
 * @version 1.0
 */
public class LogYmSdkRequestPostProcessor implements YmSdkRequestPostProcessor {
    private static Logger log = Logger.getLogger(LogYmSdkRequestPostProcessor.class);

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
                log.debug("短信sdk业务请求[" + businessType.getMsg() + "]成功," +
                        "请求内容[" + JsonUtil.toJsonString(requestParams) + "],请求耗时[" + millisecond + "]");
            }
        } else {
            log.error("短信sdk业务请求[" + businessType.getMsg() + "]失败:code=[" + retCode + "],response=[" + response + "]," +
                    "请求内容[" + JsonUtil.toJsonString(requestParams) +"]");
        }
    }
}
