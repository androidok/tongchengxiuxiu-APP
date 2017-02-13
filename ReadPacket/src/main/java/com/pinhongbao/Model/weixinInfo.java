package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/8.
 */

public class weixinInfo {
    String  nickname;
    String gender;
    String openid;
    String city;
    String province;
    String unionid;
    String icon;

    @Override
    public String toString() {
        return "weixinInfo{" +
                "nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", openid='" + openid + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", unionid='" + unionid + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
