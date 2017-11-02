package com.alisure.controller;

import com.alisure.weixin.check.SignUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ALISURE on 2017/4/27.
 *
 * ΢����֤����������ʵ��
 */
@Controller
@RequestMapping("/token")
@Api(value = "/token",description = "����Token")
public class HandleTokenController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value="/token",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="����Token",httpMethod="GET")
    public void handleToken(HttpServletResponse response) throws IOException {
        // ΢�ż���ǩ��
        String signature = request.getParameter("signature");
        // ʱ��¾
        String timestamp = request.getParameter("timestamp");
        // �����
        String nonce = request.getParameter("nonce");
        // ����ַ���
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // ͨ������ signature ���������У�飬��У��ɹ���ԭ������ echostr����ʾ����ɹ����������ʧ��
        if(SignUtil.checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        }

        out.close();
        out = null;
    }

}
