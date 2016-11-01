package com.jkm.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;

import java.util.ArrayList;
import java.util.Collection;

public class CCSHttpClient extends HttpClient {

    private int TIME_OUT = 60000;

    public CCSHttpClient(boolean ifAddConnectionCloseHeader, Long TIME_OUT) {

        init(ifAddConnectionCloseHeader, TIME_OUT);
    }

    private void init(boolean ifAddConnectionCloseHeader, Long timeout) {
        if (null != timeout) {
            this.TIME_OUT = timeout.intValue();
        }

        Collection<Header> headers = new ArrayList<Header>();
        if (ifAddConnectionCloseHeader) {
            headers.add(new Header("Connection", "close"));
        }
        this.getHttpConnectionManager().getParams().setConnectionTimeout(TIME_OUT);
        this.getHttpConnectionManager().getParams().setSoTimeout(TIME_OUT);

        this.getParams().setParameter("http.default-headers", headers);
    }

}
