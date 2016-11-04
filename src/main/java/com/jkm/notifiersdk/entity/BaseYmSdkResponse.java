package com.jkm.notifiersdk.entity;

import com.jkm.util.xml.XML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Created on 16/8/1.
 *
 * @author hutao
 * @version 1.0
 */
@Data
@XStreamAlias("response")
public class BaseYmSdkResponse {
    private static final String SUCCESS_RET_CODE = "0";
    @XStreamAlias("error")
    private final String code;
    private final String message;

    public static BaseYmSdkResponse of(final String xmlContent) {
        return XML.fromXML(xmlContent, BaseYmSdkResponse.class);
    }

    public boolean isSuccess() {
        return java.util.Objects.equals(this.code, SUCCESS_RET_CODE);
    }
}
