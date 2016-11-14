package com.jkm.controller.api;

import com.google.common.base.Optional;
import com.jkm.controller.common.BaseController;
import com.jkm.controller.helper.ResponseEntityBase;
import com.jkm.entity.BankCardBin;
import com.jkm.service.BankCardBinService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/9.
 */
@Controller
@RequestMapping(value = "/bankCardBin")
public class BankCardBinContrller extends BaseController {

    @Autowired
    private BankCardBinService bankCardBinService;

    @ResponseBody
    @RequestMapping(value = "/cardNoInfo")
    public ResponseEntityBase cardNoInfo() throws IOException {

//        JSONObject responseJson = new JSONObject();
        JSONObject requestJson = null;

        requestJson = super.getRequestJsonParams();
        String cardNo = requestJson.getString("cardNo");
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(cardNo);
        final ResponseEntityBase<BankCardBin> results = new ResponseEntityBase<>();
        results.setData(bankCardBinOptional.get());

        return results;
    }


}
