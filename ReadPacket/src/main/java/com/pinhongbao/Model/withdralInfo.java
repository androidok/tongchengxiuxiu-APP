package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/10.
 */

public class withdralInfo {
    @Override
    public String toString() {
        return "withdralInfo{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", weixin='" + weixin + '\'' +
                ", fee='" + fee + '\'' +
                ", body='" + body + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", zfb_account=" + zfb_account +
                ", zfb_lxr=" + zfb_lxr +
                ", mob=" + mob +
                ", money='" + money + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }

    /**
     * id : 30
     * uid : 200086
     * weixin :
     * fee : 10.00
     * body : 兑换金额10元
     * time : 1486709898
     * type : 1
     * status : 2
     * zfb_account : null
     * zfb_lxr : null
     * mob : null
     * money : 9.70
     * domain :
     */

    private String id;
    private String uid;
    private String weixin;
    private String fee;
    private String body;
    private String time;
    private String type;
    private String status;
    private Object zfb_account;
    private Object zfb_lxr;
    private Object mob;
    private String money;
    private String domain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getZfb_account() {
        return zfb_account;
    }

    public void setZfb_account(Object zfb_account) {
        this.zfb_account = zfb_account;
    }

    public Object getZfb_lxr() {
        return zfb_lxr;
    }

    public void setZfb_lxr(Object zfb_lxr) {
        this.zfb_lxr = zfb_lxr;
    }

    public Object getMob() {
        return mob;
    }

    public void setMob(Object mob) {
        this.mob = mob;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
