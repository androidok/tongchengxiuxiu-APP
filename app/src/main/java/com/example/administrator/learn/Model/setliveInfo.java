package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2017/1/16.
 */

public class setliveInfo {

    /**
     * status : 1
     * msg : 成功
     */

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "setliveInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
