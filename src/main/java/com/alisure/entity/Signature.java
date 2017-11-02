package com.alisure.entity;

import com.alisure.weixin.AllURL;

/**
 * ǩ����Ϣ����࣬���ڷ��ظ�ǰ�˽��г�ʼ��
 * Created by ALISURE on 2017/4/30.
 */
public class Signature {

    private String appId; // ������ںŵ�Ψһ��ʶ
    private String timestamp; // �������ǩ����ʱ���
    private String nonceStr; // �������ǩ���������
    private String signature;// ���ǩ��������¼1

    public Signature(String signature) {
        this.signature = signature;

        this.appId = AllURL.AppId;
        this.timestamp = AllURL.Timestamp;
        this.nonceStr = AllURL.Noncestr;
    }

    public String getAppId() {
        return appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}