package com.jkm.interceptor;


import com.jkm.controller.common.BaseController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述：<br>
 * 作者：邢留杰 <br>
 * 修改日期：2014年8月16日下午7:35:12 <br>
 */
public class BaseControllerInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		BaseController baseController = (BaseController) handlerMethod
				.getBean();
		baseController.handle(request, response);
		return true;
	}
}
