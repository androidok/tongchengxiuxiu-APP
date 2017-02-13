package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/9.
 */

public class promotedatailInfo {
    @Override
    public String toString() {
        return "promotedatailInfo{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", pid='" + pid + '\'' +
                ", income='" + income + '\'' +
                ", bonus='" + bonus + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    /**
     * id : 4
     * uid : 200088
     * pid : 200086
     * income : 10.00
     * bonus : 0.20
     * status : 1
     * time : 1484560709
     */

    private String id;
    private String uid;
    private String pid;
    private String income;
    private String bonus;
    private String status;
    private String time;

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
