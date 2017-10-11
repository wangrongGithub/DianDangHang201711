package com.alisure.weixin;

import com.alisure.tool.core.CoreString;
import com.alisure.tool.core.CoreSystemProperty;

public class AllURL {

    private static final boolean isMyServer = false;
    private static String configServer(String myServer, String otherServer){
        return isMyServer ? myServer : otherServer;
    }

    /*�û����������õ�¼�Ự*/
    public static final String Session_Login = "user";
    public static final String OpenId = configServer("orLZlwbU27TQl_NPpXPP7cZKHfns", "");

    /*�û��ϴ���ͼƬ��ַ*/
    public static final String PathUserUploadImage = configServer("image\\upload\\", "image/upload/");
    public static final String PathUseDir = configServer("C:\\software\\tomcat\\webapps\\ddh\\", "/usr/tomcat/apache-tomcat-8.0.36/webapps/ddh/");

    /*��־��ַ*/
    public static final String PathLog = PathUseDir + "log/ddh.log";

    /*����ַ*/
    public static final String BaseServeURL= configServer("http://www.alisure.xyz/ddh", "http://timeseller.fantasy512.cn/ddh"); //timeseller_v0.2
    public static final String BaseRViewURL= "/web/wechat/view";
    public static final String BaseAViewURL= BaseServeURL + BaseRViewURL;
    /*��ڵ�ַ*/
    public static final String IndexPageURL = BaseRViewURL + "/index.html";
    public static final String PublishPageURL = BaseRViewURL + "/publish.html";
    public static final String UserPageURL = BaseRViewURL + "/personCenter.html";
    public static final String ErrorPageURL = "/error/error.jsp";

    /*���ںŵ�����*/
    public static final String token = configServer("Javen", "weixinCourse");
    public static final String AppId= configServer("wx933eaa3a5b99cc72", "wxa793689f1fa61607");
    private static final String Secret= configServer("1b0f22fa60eb781fd3fa39a86373b5b6", "fbb4f1e78dadf3abf92826de96d97ed3");
    public static final String Noncestr = "fawfoiwvngcydjxsemsoi";
    public static final String Timestamp = "1414636257";

    /*����ʱʹ�õ�Token����Ҫ�ֶ�����*/
    public static String accessTokenTest = "";

    /*��ȡaccess_token��ticket*/
    public static final String AccessToken = "ACCESS_TOKEN";
    public static String URLAccessToken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppId + "&secret=" + Secret;
    public static String URLTicket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + AccessToken;

    /*�����˵�*/
    public static String URLMenuCreate = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + AccessToken;

    /*����ý����Դ*/
    public static final String MediaId = "MediaId";
    public static String URLDownloadMedia = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + AccessToken + "&media_id=" + MediaId;

    /*��Ȩ���*/
    public static final String AuthState = "AuthState";
    private static final String Auth_callback = CoreString.URLEncoder_encode(BaseServeURL + "/menu/auth", "UTF-8");
    public static String URLAuthGetUserInfo = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AppId + "&redirect_uri=" + Auth_callback + "&response_type=code&scope=snsapi_userinfo&state=" + AuthState + "#wechat_redirect";
    public static final String AuthOpenID = "OPENID";
    public static String URLGetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + AccessToken + "&openid=" + AuthOpenID + "&lang=zh_CN";

    /*�ڲ˵��л�ȡ�û�openId*/
    public static String URLAuthGetOpenId = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AppId + "&redirect_uri=" + Auth_callback + "&response_type=code&scope=snsapi_base&state=" + AuthState + "#wechat_redirect";
    public static final String AuthState_UserInfo = "UserInfo";
    public static final String AuthState_Menu1 = "Menu1";
    public static final String AuthState_Menu2 = "Menu2";
    public static final String AuthState_Menu3 = "Menu3";
    public static final String Auth_Code = "CODE";
    public static String URLAuthAccessToken = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AppId + "&secret=" + Secret + "&code=" + Auth_Code + "&grant_type=authorization_code";

    /*ģ�����*/
    public static final String TID= "TID";
    public static final String Template_Task_URL = BaseAViewURL + "/task.html?from=template&id=" + TID;
    public static String URLSendTemplate = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + AccessToken;

}
