package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/29.开始直播
 */

public class StartPushInfo {

    /**
     * status : 1
     * msg : 成功
     * data : {"liveId":2,"share_url":"http://jy.jnooo23.top/index.php?s=/Live/Index/show/live_id/2.html"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StartPushInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * liveId : 2
         * share_url : http://jy.jnooo23.top/index.php?s=/Live/Index/show/live_id/2.html
         */

        private int liveId;
        private String share_url;

        public int getLiveId() {
            return liveId;
        }

        public void setLiveId(int liveId) {
            this.liveId = liveId;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "liveId=" + liveId +
                    ", share_url='" + share_url + '\'' +
                    '}';
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}
