package com.jkm.controller.page;


import com.jkm.controller.common.BaseController;
import com.jkm.entity.User;
import com.jkm.service.IUserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
@RequestMapping("/userPage")
public class UserPageController extends BaseController {
	@Resource
	private IUserService userService;

	/**
	 * 跳转到jsp页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/showJspUser")
	public String showJspUser(HttpServletRequest request,Model model){
		int userId = Integer.parseInt(request.getParameter("id"));
		User user = this.userService.getUserById(userId);
		model.addAttribute("user", user);
		return "showUser.jsp";
	}

	/**
	 * 跳转到html页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/showHtmlUser")
	public String showHtmlUser(HttpServletRequest request,Model model){
		int userId = Integer.parseInt(request.getParameter("id"));
		User user = this.userService.getUserById(userId);
		model.addAttribute("user", user);
		return "showUser.html";
	}
}
