package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/9.
 */

public class lockListInfo {


    @Override
    public String toString() {
        return "lockListInfo{" +
                "user_nicename='" + user_nicename + '\'' +
                ", avatar='" + avatar + '\'' +
                ", backmoney='" + backmoney + '\'' +
                ", ranking='" + ranking + '\'' +
                '}';
    }

    /**
     * user_nicename : 上官飞
     * avatar : http://wx.qlogo.cn/mmopen/Eg4LxAbic7gEOOFloicsdkTcgydpMtR9JwZjOdnzoz6qw5ciceTLsAuGcoIyAajia0s55r4DU8MYSiaaaMJgOiaWQg2DS1TKp4Nvcz/0
     * backmoney : 76.68
     */

    private String user_nicename;
    private String avatar;
    private String backmoney;
    private String ranking;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
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

    public String getBackmoney() {
        return backmoney;
    }

    public void setBackmoney(String backmoney) {
        this.backmoney = backmoney;
    }
}
