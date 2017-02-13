package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/10.
 */

public class ApprenticeInfo {

    /**
     * id : 200087
     * user_login : 18850148261
     * user_pass : 225d3e58aec8ca8a54d0c0e6289bda2a
     * user_nicename : Seven+1
     * avatar : http://wx.qlogo.cn/mmopen/gusVIoztTQZtJUeu2fSTANCltzVDcaeRJJQd8fAeVrZYcPthrU4SosqLgAVS14FIvKyfm0Rj5mtTjaXolmRbyw3g03f7Qk7v/0
     * last_login_ip : 120.41.23.163
     * last_login_time : 1484814587
     * create_time : 1484814471
     * regip : 120.41.23.163
     * user_status : 1
     * weixin : oe9nEwyd_9DlNxQ4zsg8ZR0LevCE
     * wxpay :
     * unionid : 0
     * parent_id : 200086
     * subscribe : 0
     * sex : 1
     * age : 0
     * country : 中国
     * province : 福建
     * city : 漳州
     * subscribe_time : 0
     * jifen : 0.00
     * money : 0.00
     * user_rank : 0
     * provinceid : 0
     * cityid : 0
     * type : 0
     * rank_time : 0
     * appcode :
     * last_ts_time : 0
     * virtual : 0
     * paymoney : 0.00
     * backmoney : 0.00
     * cashmoney : 0.00
     * weixin_app :
     * mobile :
     */

    private String id;
    private String user_login;
    private String user_pass;
    private String user_nicename;
    private String avatar;
    private String last_login_ip;
    private String last_login_time;
    private String create_time;
    private String regip;
    private String user_status;
    private String weixin;
    private String wxpay;
    private String unionid;
    private String parent_id;
    private String subscribe;
    private String sex;
    private String age;
    private String country;
    private String province;
    private String city;
    private String subscribe_time;
    private String jifen;
    private String money;
    private String user_rank;

    @Override
    public String toString() {
        return "ApprenticeInfo{" +
                "id='" + id + '\'' +
                ", user_login='" + user_login + '\'' +
                ", user_pass='" + user_pass + '\'' +
                ", user_nicename='" + user_nicename + '\'' +
                ", avatar='" + avatar + '\'' +
                ", last_login_ip='" + last_login_ip + '\'' +
                ", last_login_time='" + last_login_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", regip='" + regip + '\'' +
                ", user_status='" + user_status + '\'' +
                ", weixin='" + weixin + '\'' +
                ", wxpay='" + wxpay + '\'' +
                ", unionid='" + unionid + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", subscribe='" + subscribe + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", subscribe_time='" + subscribe_time + '\'' +
                ", jifen='" + jifen + '\'' +
                ", money='" + money + '\'' +
                ", user_rank='" + user_rank + '\'' +
                ", provinceid='" + provinceid + '\'' +
                ", cityid='" + cityid + '\'' +
                ", type='" + type + '\'' +
                ", rank_time='" + rank_time + '\'' +
                ", appcode='" + appcode + '\'' +
                ", last_ts_time='" + last_ts_time + '\'' +
                ", virtual='" + virtual + '\'' +
                ", paymoney='" + paymoney + '\'' +
                ", backmoney='" + backmoney + '\'' +
                ", cashmoney='" + cashmoney + '\'' +
                ", weixin_app='" + weixin_app + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    private String provinceid;
    private String cityid;
    private String type;
    private String rank_time;
    private String appcode;
    private String last_ts_time;
    private String virtual;
    private String paymoney;
    private String backmoney;
    private String cashmoney;
    private String weixin_app;
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRegip() {
        return regip;
    }

    public void setRegip(String regip) {
        this.regip = regip;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWxpay() {
        return wxpay;
    }

    public void setWxpay(String wxpay) {
        this.wxpay = wxpay;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(String subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUser_rank() {
        return user_rank;
    }

    public void setUser_rank(String user_rank) {
        this.user_rank = user_rank;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRank_time() {
        return rank_time;
    }

    public void setRank_time(String rank_time) {
        this.rank_time = rank_time;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getLast_ts_time() {
        return last_ts_time;
    }

    public void setLast_ts_time(String last_ts_time) {
        this.last_ts_time = last_ts_time;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getBackmoney() {
        return backmoney;
    }

    public void setBackmoney(String backmoney) {
        this.backmoney = backmoney;
    }

    public String getCashmoney() {
        return cashmoney;
    }

    public void setCashmoney(String cashmoney) {
        this.cashmoney = cashmoney;
    }

    public String getWeixin_app() {
        return weixin_app;
    }

    public void setWeixin_app(String weixin_app) {
        this.weixin_app = weixin_app;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
