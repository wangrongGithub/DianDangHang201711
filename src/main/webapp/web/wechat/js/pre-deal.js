﻿/**
 * Created by ALISURE on 2017/4/30.
 */

$(function () {
    /*配置微信*/
    var url = location.href;
    var jsApiList = ["chooseImage","uploadImage","downloadImage"];

    getSignature(url, jsApiList);
});

/**
 * 获取网页的签名
 * @param url
 * @param jsApiList
 */
function getSignature(url,jsApiList) {
    $.get("http://www.alisure.xyz/ddh/predeal/signature", {url:url},
        function (data) {
            if (data.status == 1) {
                var object = data.object;
                config(object.appId, object.timestamp, object.nonceStr, object.signature, jsApiList);
            } else {

            }
        }
    );

    ready();
    error();
    check(jsApiList);

}

/**
 * 配置签名等信息
 * @param appId
 * @param timestamp
 * @param nonceStr
 * @param signature
 * @param jsApiList
 */
function config(appId,timestamp, nonceStr, signature, jsApiList) {
    /*通过config接口注入权限验证配置*/
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: appId, // 必填，公众号的唯一标识
        timestamp: timestamp, // 必填，生成签名的时间戳
        nonceStr: nonceStr, // 必填，生成签名的随机串
        signature: signature,// 必填，签名，见附录1
        jsApiList: jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
}
/**
 * 通过ready接口处理成功验证
 * @param callback
 */
function ready(callback) {
    wx.ready(function () {
        if(typeof callback == "function") callback();
    });
}
/**
 * 发生错误时调用
 * @param callback
 */
function error(callback) {
    /*通过error接口处理失败验证*/
    wx.error(function () {
        if(typeof callback == "function") callback();
    });
}

/**
 * 检查是否支持指定的API
 * @param jsApiList
 */
function check(jsApiList) {
    /*判断当前客户端版本是否支持指定JS接口*/
    wx.checkJsApi({
        jsApiList: jsApiList,
        success: function(res) {
            // 以键值对的形式返回，可用的api值true，不可用为false
            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
        },
        fail: function (res) {

        }
    });
}

/**
 * 通知服务器去下载图片
 * @param mediaId
 * @param callback
 */
function downImage(mediaId,callback) {
    $.get("http://www.alisure.xyz/ddh/predeal/image", {mediaId:mediaId}, callback);
}

/**
 * 获取URL 根地址
 */
function getRoot() {
    var href = location.href;
    var project = "ddh";
    href = href.split(project);
    if(href.length >= 2){
        return href[0] + project + "/";
    }else{
        return location.host + "/";
    }
}

