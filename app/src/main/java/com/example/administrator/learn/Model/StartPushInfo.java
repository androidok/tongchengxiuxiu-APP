package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/29.开始直播
 */

public class StartPushInfo {

    /**
     * status : 1
     * msg : 成功
     * data : {"liveId":1}
     */

    private int status;
    private String msg;
    private DataBean data;

    @Override
    public String toString() {
        return "StartPushInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "liveId=" + liveId +
                    '}';
        }

        /**
         * liveId : 1
         */

        private int liveId;

        public int getLiveId() {
            return liveId;
        }

        public void setLiveId(int liveId) {
            this.liveId = liveId;
        }
    }
}
