package com.jkm.interceptor;


import com.jkm.controller.common.BaseController;
import com.jkm.entity.MerchantAppInfo;
import com.jkm.service.MerchantAppInfoService;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 描述：<br>
 * 作者：邢留杰 <br>
 * 修改日期：2014年8月16日下午7:35:12 <br>
 */
public class BaseControllerInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = Logger.getLogger(BaseControllerInterceptor.class);
	@Autowired
	private MerchantAppInfoService merchantAppInfoService;
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean flag = true;

		String current = "";
		String appId = "";

		if(request.getMethod().toUpperCase().equals("POST")){//防止篡改url地址
			String url = request.getHeader("Referer");
			if(url!=null&&url.contains("appid")){
				current = url.substring(url.indexOf("?")+1,url.length());
				String[] arr = current.split("&");
				for(int i=0;i<arr.length;i++){
					String[] param = arr[i].split("=");
					if("appid".equals(param[0])){
						appId = param[1];
						MerchantAppInfo merchantAppInfo = merchantAppInfoService.selectByOpenId(appId);
						if(merchantAppInfo==null){
							response.sendError(506,"非法数据、未通过验证");
							flag = false;
						}
					}
					if(!flag){
						break;
					}
				}

				String uid = request.getParameter("uid");
				String reqTime = DateFormatUtil.format(new Date(),"yyyyMMddHHmmss");
				String key = merchantAppInfoService.selectSecretKeyByOpenId(appId);
				String sign = MD5Util.MD5(appId + uid + reqTime + MD5Util.MD5(key));
				response.sendRedirect(current + "?" +
						"appid=" + appId + "&" +
						"uid=" + uid + "&" +
						"reqTime=" + reqTime + "&" +
						"sign=" + sign);
			}
		}
		if(!flag){
			return false;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		BaseController baseController = (BaseController) handlerMethod
				.getBean();
		baseController.handle(request, response);
		return true;
	}
}
