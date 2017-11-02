package com.alisure.weixin.menu;

import com.alisure.tool.core.CoreNetwork2;
import com.alisure.weixin.AllURL;

/**
 * Created by ALISURE on 2017/9/26.
 * �˵���ʱ��������������񡢸�������
 */
public class Menu {

    public static boolean setMenu(String accessToken){
        try{
            String indexPage = AllURL.URLAuthGetOpenId.replace(AllURL.AuthState, AllURL.AuthState_Menu1);
            String publishPage = AllURL.URLAuthGetOpenId.replace(AllURL.AuthState, AllURL.AuthState_Menu2);
            String userPage = AllURL.URLAuthGetOpenId.replace(AllURL.AuthState, AllURL.AuthState_Menu3);

            String menuBody = "{\"button\":[" +
                    "{\"type\": \"view\", \"name\": \"ʱ�����\", \"url\": \"" + indexPage + "\"}," +
                    "{\"type\": \"view\", \"name\": \"��������\", \"url\": \"" + publishPage + "\"}," +
                    "{\"type\": \"view\", \"name\": \"��������\", \"url\": \"" + userPage + "\"}" +
                    "]}";
            System.out.println(menuBody);

            String url = AllURL.URLMenuCreate.replace(AllURL.AccessToken, accessToken);
            String result = CoreNetwork2.sendPost(url, menuBody);

            System.out.println(result);
            return result.contains("ok");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // ��Ҫ����POST����
        System.out.println(Menu.setMenu(AllURL.accessTokenTest));
    }
}
