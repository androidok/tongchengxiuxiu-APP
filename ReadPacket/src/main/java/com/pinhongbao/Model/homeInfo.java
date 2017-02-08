package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/6.
 */

public class homeInfo {

    /**
     * id : 5687
     * uniacid : null
     * uid : 2567
     * nickname : 相遇是缘
     * title : 恭喜发财大吉大利!
     * num : 4
     * numed : 1
     * pay : 10
     * status : 2
     * type : 0
     * display : 1
     * stime : 1486445870
     * etime : 1486532281
     * avatar : http://wx.qlogo.cn/mmopen/lZx3wFEpPgMFP6B39pQj4Is24h3Dicrkt8icrj9rEN2yX2yl5ngzaSRSAvMoJsicfmldv9p63t3szpgrzHKbxvXAWibG4jonB36ib/0
     */

    private String id;
    private Object uniacid;
    private String uid;
    private String nickname;
    private String title;
    private String num;
    private String numed;
    private String pay;
    private String status;
    private String type;
    private String display;
    private String stime;
    private String etime;
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getUniacid() {
        return uniacid;
    }

    public void setUniacid(Object uniacid) {
        this.uniacid = uniacid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "homeInfo{" +
                "id='" + id + '\'' +
                ", uniacid=" + uniacid +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", title='" + title + '\'' +
                ", num='" + num + '\'' +
                ", numed='" + numed + '\'' +
                ", pay='" + pay + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", display='" + display + '\'' +
                ", stime='" + stime + '\'' +
                ", etime='" + etime + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getNumed() {
        return numed;
    }

    public void setNumed(String numed) {
        this.numed = numed;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
