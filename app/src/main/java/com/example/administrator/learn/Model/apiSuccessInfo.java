package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/29.
 */

public class apiSuccessInfo {

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

    @Override
    public String toString() {
        return "apiSuccessInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
