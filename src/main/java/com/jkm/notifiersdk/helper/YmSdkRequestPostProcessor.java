package com.jkm.notifiersdk.helper;

import com.jkm.notifiersdk.enums.EnumYmSkdRequestBusinessType;
import java.util.Map;

/**
 * Created on 16/8/1.
 *
 * @author hutao
 * @version 1.0
 */
public interface YmSdkRequestPostProcessor {
    /**
     * 处理联泰的请求
     *
     * @param businessType
     * @param requestParams
     * @param retCode
     * @param response
     * @param millisecond   毫秒单位
     * @param remark
     */
    void handle(final EnumYmSkdRequestBusinessType businessType,
                final Map<String, String> requestParams,
                final String retCode,
                final String response,
                final long millisecond,
                final String remark);
}
