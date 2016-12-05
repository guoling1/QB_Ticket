package com.jkm.controller.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeiXinUtil {

    /**
     * 获取openId
     * @param code
     * @param url
     * @return
     */
    public Map<String, String> getOpenId(String code,String url){
        Map<String, String> ret = new HashMap<String, String>();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        JSONObject jsonparer = new JSONObject();// 初始化解析json格式的对象
        try
        {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JSONObject json = (JSONObject) jsonparer.parse(responseContent);
            // 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                if (json.get("errcode")!=null)
                {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                 // ret.put("errcode", json.get("errcode").getAsString());
                 // ret.put("errmsg",json.get("errmsg").getAsString());
                }
                else
                {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    ret.put("openid", json.get("openid").toString());
                    ret.put("accesstoken",json.get("access_token").toString());
                    ret.put("refresh_token",json.get("refresh_token").toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接 ,释放资源
            client.getConnectionManager().shutdown();
            return ret;
        }
    }

    /**
     * 获取code
     * @param request
     * @param response
     */
    public String getCode(final HttpServletRequest request, final HttpServletResponse response,String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        JSONObject jo = new JSONObject();
        String result = null;
        try {
            HttpResponse res = client.execute(get);
            String responseContent = null; // 响应内容
            HttpEntity entity = res.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            JSONObject json = (JSONObject) jo.parse(responseContent);
            // 将json字符串转换为json对象
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (Integer.getInteger(json.get("errcode").toString()) != 0) {// 错误时微信会返回错误码等信息，{"errcode":40013,"errmsg":"invalid appid"}
                } else {// 正常情况下{"access_token":"ACCESS_TOKEN","expires_in":7200}
                    result = json.get("code").toString();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接 ,释放资源
            client.getConnectionManager().shutdown();
            return result;
        }
    }
}
