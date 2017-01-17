package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2017/1/16.
 */

public class checkliveInfo  {

    /**
     * 'status'=>1,'msg'=>'成功','live_status'=> 0=>'未推送',1=>'直播中',2=>'结束',3=>'禁播'

     * status : 1
     * msg : 成功
     * live_status : 1
     */

    private int status;
    private String msg;
    private String live_status;

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "checkliveInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", live_status='" + live_status + '\'' +
                '}';
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLive_status() {
        return live_status;
    }

    public void setLive_status(String live_status) {
        this.live_status = live_status;
    }
}
