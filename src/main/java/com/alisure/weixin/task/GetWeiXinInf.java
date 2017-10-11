package com.alisure.weixin.task;

import com.alisure.tool.core.CoreProperties;
import com.alisure.tool.core.CoreTime;
import com.alisure.weixin.check.JSSDKSignUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component("getWeiXinInf")
public class GetWeiXinInf {

    /*��ʱ���ʽ*/
    /*ÿ��Сʱ��**��10��10����*/
    private static final String Cron = "20 10 * * * ?";

    /*key*/
    private static final String AccessToken = "accessToken";
    private static final String Ticket = "ticket";
    /*�����ļ���*/
    private static final String file = "task.properties";

    private String getFile(){
        URL url = getClass().getResource(file);
        return url.getPath();
    }

    /**
     * ���� AccessToken
     */
    public boolean updateAccessToken(){
        try {
            String accessToken = JSSDKSignUtil.getAccessToken();
            CoreProperties.updateOrWriteProp(getFile(), AccessToken, accessToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ��ȡ�����ļ��е� AccessToken
     * @return
     */
    public String getAccessToken(){
        try {
            return CoreProperties.getPropByFileAndProp2(getFile(), AccessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ���� Ticket
     * @return
     */
    public boolean updateTicket(){
        try {
            String accessToken = getAccessToken();
            if(accessToken == null || "".equals(accessToken)) return false;
            String ticket = JSSDKSignUtil.getTicket(accessToken);
            CoreProperties.updateOrWriteProp(getFile(), Ticket, ticket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ��ȡ�����ļ��е� Ticket
     * @return
     */
    public String getTicket(){
        try {
            return CoreProperties.getPropByFileAndProp2(getFile(), Ticket);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Scheduled(cron = Cron)
    public void runTask(){
        System.out.println("-------------------------------------------------------------------");
        System.out.println(CoreTime.getDataTime() + " update access_token and ticket\n");
        updateAccessToken();
        updateTicket();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("\n");
    }

    public static void main(String[] args) {
        GetWeiXinInf getWeiXinInf = new GetWeiXinInf();
        getWeiXinInf.updateAccessToken();
        System.out.println(getWeiXinInf.getAccessToken());
        getWeiXinInf.updateTicket();
        System.out.println(getWeiXinInf.getTicket());
    }
}
