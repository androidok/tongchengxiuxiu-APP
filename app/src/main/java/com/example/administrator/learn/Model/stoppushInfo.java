package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/29.结束直播
 */

public class stoppushInfo {

    /**
     * status : 1
     * msg : 成功
     * data : {"start_time":"1483000733","end_time":"0","live_num":"0","live_money":"0","fx_num":"0","live_img":"http://img.jnooo.cc/dating/2016-12-28/17/e267556df30df3fb0973d1f759b09f36.png"}
     */

    private int status;
    private String msg;
    private DataBean data;

    @Override
    public String toString() {
        return "stoppushInfo{" +
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
        /**
         * start_time : 1483000733
         * end_time : 0
         * live_num : 0
         * live_money : 0
         * fx_num : 0
         * live_img : http://img.jnooo.cc/dating/2016-12-28/17/e267556df30df3fb0973d1f759b09f36.png
         */

        private String start_time;
        private String end_time;
        private String live_num;
        private String live_money;
        private String fx_num;
        private String live_img;

        @Override
        public String toString() {
            return "DataBean{" +
                    "start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", live_num='" + live_num + '\'' +
                    ", live_money='" + live_money + '\'' +
                    ", fx_num='" + fx_num + '\'' +
                    ", live_img='" + live_img + '\'' +
                    '}';
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getLive_num() {
            return live_num;
        }

        public void setLive_num(String live_num) {
            this.live_num = live_num;
        }

        public String getLive_money() {
            return live_money;
        }

        public void setLive_money(String live_money) {
            this.live_money = live_money;
        }

        public String getFx_num() {
            return fx_num;
        }

        public void setFx_num(String fx_num) {
            this.fx_num = fx_num;
        }

        public String getLive_img() {
            return live_img;
        }

        public void setLive_img(String live_img) {
            this.live_img = live_img;
        }
    }
}
