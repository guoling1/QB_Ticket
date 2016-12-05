package com.jkm.controller.page;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.common.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "")
public class WeiXinController extends BaseController {
    @Autowired
    private WeiXinUtil weiXinUtil;

    /*
    获取code
     */
    @RequestMapping(value = "wx",method = RequestMethod.GET)
   public void toPredetermine(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String appid = "wx23d3b3d674076e2e";
        String redirectUrl = "http://hcp.jinkaimen.com/predetermine";
        String scope = "snsapi_base";
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=" + appid + "&" +
                "redirect_uri=" + redirectUrl +
                "response_type=code" +
                "&scope=" + scope + "&state=STATE" + "#wechat_redirect";
        response.sendRedirect(url);
    }

    /*
    根据code获取access_token、refresh_token、openId
     */
    @RequestMapping(value = "predetermine",method = RequestMethod.GET)
    public String predetermine(final HttpServletRequest request,final HttpServletResponse response){
        String code = request.getParameter("code");
        if ("".equals(code) || code == null){
            return "";
        }
        String appid = "wx23d3b3d674076e2e";
        String secret = "2da8ace3ac93da9a34404c8ff9ada924";
        String redirectUrl = "http://hcp.jinkaimen.com/ticket/main-menu/reserve";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appid + "&" +
                "secret=" + secret + "&" +
                "code=" + code + "&grant_type=authorization_code";
        Map<String, String> map = weiXinUtil.getOpenId(code, url);
        String openId = map.get("openid");

        String hcpAppid = "1013";
        String hcpSecret = "";
        return redirectUrl + "?appid=" + hcpAppid + "&uid=" + openId;
    }
}

