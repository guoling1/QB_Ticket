package com.jkm.controller.page;

import com.jkm.util.ReadParameter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 出票、退票回调回复信息
 * Created by lt on 2016/12/6.
 */
@Controller
@RequestMapping(value = "")
public class CallBackMsgController {

    private static Logger log = Logger.getLogger(CallBackMsgController.class);
    /**
     * 出票、退票回调回复信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "callbackMsg")
    public void callbackMsg(final HttpServletRequest request, final HttpServletResponse response) {
        final String process = ReadParameter.getProcess(request);
        if (process != null){
            String result = "";
            int trainNo = 0; //出票有trainNo属性  退票没有
            final String[] arr = process.split("&");
            int len = arr.length;
            for (int i = 0;i < len; i++){
                String[] split = arr[i].split("=");
                if ("success".equals(split[0])){
                    result = split[1];
                }
                if ("trainNo".equals(split[0])){
                    trainNo = 1;
                }
            }
            if ("true".equals(result)){
                try {
                    response.getWriter().write("成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if ("false".equals(result)){
                if (trainNo == 0){
                    log.error("退票失败！");
                    try {
                        response.getWriter().write("退票失败！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    log.error("出票失败！");
                    try {
                        response.getWriter().write("出票失败！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            return ;
        }
    }
}
