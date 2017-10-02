package com.alisure.weixin.template;

import com.alisure.entity.InfoTask;
import com.alisure.tool.core.CoreNetwork2;
import com.alisure.weixin.AllURL;
import com.alisure.weixin.task.GetWeiXinInf;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ���û�������Ϣ
 */
public class Template {

    /*��������ɹ�*/
    private static final String TemplateID_PubTask = "oXIRrYbbzTOtJ7tn-m2tuqcqCoxAmiahwwl41ZxT_KA";
    /*��������ɹ�:������*/
    private static final String TemplateID_RecTask_pub = "5_Q4pV3OyBdEQ2bOCSLO4wcDFdVfWIUWB45GgIWD4s4";
    /*��������ɹ�:������*/
    private static final String TemplateID_RecTask_rec = "5_Q4pV3OyBdEQ2bOCSLO4wcDFdVfWIUWB45GgIWD4s4";
    /*����ȡ��:������ȡ��*/
    private static final String TemplateID_CancelTask_rec = "5DlCfqGKFeTXWedsreA_NUhcIx9bNerNXUtDvxx4s4I";
    /*����ȡ��:������ȡ��*/
    private static final String TemplateID_CancelTask_pub = "LN6Zu4nznfBQPZWaYEpoCV3F4BS7fIqHKCesYexvHJQ";
    /*�������*/
    private static final String TemplateID_FinshTask = "uMluUBlaRetEPJGELNe5sZuJ-G5Ds9HVLffrs-NhhJ4";
    /*�������*/
    private static final String TemplateID_EndTask = "slnkszJZ2aPmnueP6BhlYmG1G3A7FVhtCpwIT1VGPCk";

    /*����ģ����Ϣ*/
    private static boolean sendTemplate(String body){
        try{
            String accessToken = new GetWeiXinInf().getAccessToken();
            System.out.println(accessToken);
            String result = CoreNetwork2.sendPost(AllURL.URLSendTemplate.replace(AllURL.AccessToken, accessToken), body);
            System.out.println(result);
            return result.contains("ok");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /*����ģ����Ϣ������*/
    private static Map<String, TemplateData> dealData(InfoTask task, String status, boolean hasRec){
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        data.put("first", new TemplateData(task.getTitle()));
        data.put("time", new TemplateData(task.getPubTime()));
        data.put("title", new TemplateData(task.getTitle()));
        data.put("content", new TemplateData(task.getContent()));
        data.put("coins", new TemplateData(String.valueOf(task.getCoins())));
        data.put("rewards", new TemplateData(task.getRewards()));
        data.put("secret", new TemplateData(task.getWords()));
        data.put("publisher", new TemplateData(task.getPub().getNickname()));
        data.put("publisher_contact", new TemplateData(task.getPub().getPhone()));
        data.put("startData", new TemplateData(task.getStartTime()));
        data.put("endData", new TemplateData(task.getEndTime()));
        data.put("status", new TemplateData(status));
        if(hasRec) {
            data.put("reciever", new TemplateData(task.getRec().getNickname()));
            data.put("reciever_contact", new TemplateData(task.getRec().getPhone()));
        }
        return data;
    }

    /* ��������ɹ� */
    public static boolean pubTaskOK(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_PubTask, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task,"�����ɹ�", false));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ��������ɹ�:������ */
    public static boolean recTaskOK_pub(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_RecTask_pub, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "���ֳɹ�", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ��������ɹ�:������ */
    public static boolean recTaskOK_rec(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_RecTask_rec, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "���ֳɹ�", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ����ȡ�� */
    public static boolean cancelTaskOK_rec(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_CancelTask_rec, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "ȡ���ɹ�", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ����ȡ�� */
    public static boolean cancelTaskOK_pub(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_CancelTask_pub, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "ȡ���ɹ�", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ������� */
    public static boolean finshTaskOK(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_FinshTask, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "�������", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }
    /* ������� */
    public static boolean endTaskOK(String toUser, InfoTask task){
        TemplateContent templateContent = new TemplateContent(TemplateID_EndTask, toUser,
                AllURL.Template_Task_URL.replace(AllURL.TID, String.valueOf(task.getTid())));
        templateContent.setData(dealData(task, "�������", true));
        String body = JSONObject.fromObject(templateContent).toString();
        return sendTemplate(body);
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
