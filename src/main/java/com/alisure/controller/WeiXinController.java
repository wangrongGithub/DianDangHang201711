package com.alisure.controller;

import com.alisure.entity.InfoUser;
import com.alisure.exception.LoginException;
import com.alisure.service.CommonService;
import com.alisure.tool.common.CommonLog;
import com.alisure.tool.core.CoreReflect;
import com.alisure.tool.core.CoreString;
import com.alisure.weixin.AllURL;
import com.alisure.service.WeixinService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ��΢����أ���Ȩ���˵���
 * ˼·��
 *      ���û�����˵�ʱ��ȡ�û�openId
 *      �����ݿ���û�и��û�����Ϣ����Ҫ������Ȩ��Ȼ���ȡ�û���Ϣ��
 *      �����ݿ����и��û�����Ϣ������Ҫ��Ȩ��ֱ�Ӵ����ݿ��л�ȡ�û���Ϣ��
 */
@Controller
@RequestMapping("/menu")
@Api(value = "/menu",description = "about wen xin")
public class WeiXinController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CommonService commonService;

    @Autowired
    private WeixinService weixinService;

    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public String auth(@RequestParam(required =false) String code, @RequestParam(required =false) String state, Model model) throws Exception{
        if(!CoreString.isNull(code) && !CoreString.isNull(state)){
            if(state.equals(AllURL.AuthState_UserInfo)){ /*��Ȩ*/
                CommonLog.writeAlisureLog(AllURL.PathLog, "��ʼ������Ȩ:" +  code + ":" + state);
                Map<String, String> nodeValues = weixinService.getAuthOpenId_AccessToken(code); /*��ȡ��Ȩaccess_token��openid*/
                if(nodeValues != null){
                    String access_token = nodeValues.get(WeixinService.access_token);
                    String openid = nodeValues.get(WeixinService.openid);
                    CommonLog.writeAlisureLog(AllURL.PathLog, "��Ȩ�ɹ�:" +  access_token + ":" + openid);
                    boolean result = weixinService.getAuthUserInfo(access_token, openid);/*��ȡ��Ϣ*/
                    if(result){ /*�ɹ���ȡ��Ϣ*/
                        InfoUser infoUser = commonService.getUser(openid);
                        commonService.setLogin(request, infoUser);/*����session*/
                        CommonLog.writeAlisureLog(AllURL.PathLog, "�ɹ���ȡ�û���Ϣ:" +  infoUser.toString());
                        return "redirect:" + AllURL.UserPageURL; /*�ض����û�����*/
                    }
                }
            }else{ /*����˵�������Ĵ����Ŀ�������õ�¼״̬*/
                String openId = weixinService.getAuthOpenId(code);
                CommonLog.writeAlisureLog(AllURL.PathLog, "�û�����˵�:" +  openId + ":" + state);
                if(!CoreString.isNull(openId)) { /*�õ���openId*/
                    /*�ж��Ƿ��ڹ�Ȩ�����ݿ��к��и��û�����Ϣ������Ѿ��ڹ�Ȩ��*/
                    InfoUser infoUser = commonService.getUser(openId);
                    if(infoUser == null){/*û�и��û�:�ض��������Ȩ*/
                        CommonLog.writeAlisureLog(AllURL.PathLog, "û�и��û�����Ҫ������Ȩ:" +  openId);
                        return "redirect:" + AllURL.URLAuthGetUserInfo.replace(AllURL.AuthState, AllURL.AuthState_UserInfo);
                    }else{ /*�Ѿ��ڹ�Ȩ��*/
                        commonService.setLogin(request, infoUser);/*����session*/
                        CommonLog.writeAlisureLog(AllURL.PathLog, "����˵���һ�������������ض���:" +  commonService.getLogin(request).toString());
                        /*�ض���*/
                        if(state.equals(AllURL.AuthState_Menu1)){
                            return "redirect:" + AllURL.IndexPageURL;
                        }else if(state.equals(AllURL.AuthState_Menu2)){
                            return "redirect:" + AllURL.PublishPageURL;
                        }else if(state.equals(AllURL.AuthState_Menu3)){
                            return "redirect:" + AllURL.UserPageURL;
                        }
                    }
                }
            }
        }
        return "redirect:" + AllURL.ErrorPageURL;
    }

}
