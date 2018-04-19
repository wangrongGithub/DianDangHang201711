package com.alisure.entity;

import com.alisure.weixin.filter.EmojiFilter;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoUser implements RowMapper<InfoUser>, Serializable {

    private int uid;
    private String openid;
    private String headimgurl;
    private String province;
    private String nickname;
    private String sex;
    private String phone;
    private String qq;
    private String weixin;
    private int goldCoins;
    private String card;
    private int schoolId;
    private String city;
    private String country;
    private String time;

    public InfoUser() {

    }

    public InfoUser(String openid, String headimgurl, String province, String nickname, String sex, String city, String country, String time) {
        this.openid = openid;
        this.headimgurl = headimgurl;
        this.province = province;
        this.nickname = EmojiFilter.filterEmoji(nickname);
        this.sex = sex;
        this.city = city;
        this.country = country;
        this.time = time;
    }

    public InfoUser(int uid, String openid, String headimgurl, String province, String nickname, String sex, String phone, String qq,
                    String weixin, int goldCoins, String card, int schoolId, String city, String country, String time) {
        this.uid = uid;
        this.openid = openid;
        this.headimgurl = headimgurl;
        this.province = province;
        this.nickname = EmojiFilter.filterEmoji(nickname);
        this.sex = sex;
        this.phone = phone;
        this.qq = qq;
        this.weixin = weixin;
        this.goldCoins = goldCoins;
        this.card = card;
        this.schoolId = schoolId;
        this.city = city;
        this.country = country;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = EmojiFilter.filterEmoji(nickname);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public int getGoldCoins() {
        return goldCoins;
    }

    public void setGoldCoins(int goldCoins) {
        this.goldCoins = goldCoins;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public InfoUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return new InfoUser(
                resultSet.getInt("uid"), resultSet.getString("openid"), resultSet.getString("headimgurl"),
                resultSet.getString("province"), resultSet.getString("nickname"), resultSet.getString("sex"),
                resultSet.getString("phone"), resultSet.getString("qq"), resultSet.getString("weixin"),
                resultSet.getInt("goldCoins"), resultSet.getString("card"), resultSet.getInt("schoolId"),
                resultSet.getString("city"), resultSet.getString("country"), resultSet.getString("time"));
    }

    @Override
    public String toString() {
        return "InfoUser{" +
                "uid=" + uid +
                ", openid='" + openid + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", province='" + province + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", goldCoins=" + goldCoins +
                ", card='" + card + '\'' +
                ", schoolId=" + schoolId +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
