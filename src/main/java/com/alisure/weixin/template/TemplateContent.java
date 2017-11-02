package com.alisure.weixin.template;

import java.util.Map;

/**
 * Created by ALISURE on 2017/10/2.
 */
public class TemplateContent {
    private String template_id;//ģ����Ϣid
    private String touser;//�û�openid
    private String url;//url�ÿգ����ͳɹ��󣬵��ģ����Ϣ�����һ���հ�ҳ�棬���޷����
    private String topcolor;//������ɫ
    private Map<String, TemplateData> data;//��ϸ����

    public TemplateContent() {

    }

    public TemplateContent(String template_id, String touser, String url) {
        this.template_id = template_id;
        this.touser = touser;
        this.url = url;
        this.topcolor = "#173177";
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}