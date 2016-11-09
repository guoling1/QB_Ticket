package com.jkm.controller.api;

import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.service.QueryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/9.
 */
@Controller
@RequestMapping(value = "/queryHistory")
public class QueryHistoryController extends BaseController {

    @Autowired
    private QueryHistoryService queryHistoryService;

    @ResponseBody
    @RequestMapping(value = "/queryHistory", method = RequestMethod.POST)
    public ResponseEntityBase<Object> queryHistory() throws IOException {


        return null;
    }
}
