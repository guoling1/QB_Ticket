package com.jkm.util;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpMethod {
    /**
     * 使用HttpClient实现发送http请求方法
     * @param jsonObject
     * @author lijia
     */
    public static JSONObject httpClient(JSONObject jsonObject, String url) {
        CCSHttpClient httpClient = new CCSHttpClient(false, 10000L);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter("jsonStr", jsonObject.toString());
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            httpClient.executeMethod(postMethod);
            reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            String str = "";
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String ts = stringBuffer.toString();
        System.out.println(ts);
        return JSONObject.fromObject(ts);
    }
}
