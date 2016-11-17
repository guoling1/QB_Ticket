package com.jkm.controller.common;

import com.google.common.base.Preconditions;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 控制层基类
 */
public class BaseController {
    private static Logger logger = Logger.getLogger(BaseController.class);
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String uid;
    /**
     * @param binder
     * @throws Exception
     */
    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    /**
     * 过滤函数
     * @param request
     * @param response
     * @throws IOException
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        String url = this.request.getContextPath()+this.request.getRequestURI();
        logger.info("请求地址："+url);
    }

    /**
     * 获取参数
     * @return
     * @throws IOException
     */
    protected JSONObject getRequestJsonParams() throws IOException {
        if (request == null) {
            return null;
        }
        String line = "";
        StringBuilder body = new StringBuilder();
        int counter = 0;
        InputStream stream;
        stream = request.getInputStream();
        //读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"utf-8"));
        while ((line = reader.readLine()) != null) {
            if(counter > 0){
                body.append("\r\n");
            }
            body.append(line);
            counter++;
        }
        if(!"".equals(body)){
            JSONObject jo = JSONObject.fromObject(body.toString());
            logger.info("请求参数为："+jo.toString());
            return  jo;
        }else{
            return null;
        }
    }

    /**
     * 成功返回
     * @param erroString
     * @throws IOException
     */
    public void returnFailJson(String erroString) throws IOException {
        JSONObject jObject=new JSONObject();
        jObject.put("result",false);
        jObject.put("data","");
        jObject.put("message",erroString);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=UTF-8");
        response.setHeader("cache-control", "max-age=7200");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", 0L);
        response.getWriter().write(jObject.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 失败返回
     * @param successResult
     * @throws IOException
     */
    public void returnSuccessJson(String successResult) throws IOException {
        JSONObject jObject=new JSONObject();
        jObject.put("result", true);
        jObject.put("data", successResult);
        jObject.put("message", "调用成功");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=UTF-8");
        response.setHeader("cache-control", "max-age=7200");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", 0L);
        response.getWriter().write(jObject.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 返回json格式数据
     * @param data
     * @throws IOException
     */
    public void returnJson(JSONObject data) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=UTF-8");
        response.setHeader("cache-control", "max-age=7200");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", 0L);
        response.getWriter().write(data.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 获取三方登录id
     * @return
     */
    public String getUid(String appid,String uid){
        Preconditions.checkNotNull(appid,"缺失参数appid");
        Preconditions.checkNotNull(uid,"缺失参数uid");
        return appid+"_"+uid;
    }
}
