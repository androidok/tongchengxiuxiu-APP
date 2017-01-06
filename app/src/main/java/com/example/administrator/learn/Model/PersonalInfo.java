package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/28.
 */

public class PersonalInfo {

    /**
     * status : 1
     * msg : 成功！
     * data : {"id":"258216","user_login":"15880268607","user_nicename":"想你的天","avatar":"","live_rtmp":"rtmp://video-center.alivecdn.com/shanmao1/258216?vhost=live.jnoo.com","share_url":"http://jy.jnooo23.top/index.php?s=/Live/Index/show/room_id/258216.html"}
     */

    private int status;
    private String msg;
    private DataBean data;

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
        return "PersonalInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
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
         * id : 258216
         * user_login : 15880268607
         * user_nicename : 想你的天
         * avatar :
         * live_rtmp : rtmp://video-center.alivecdn.com/shanmao1/258216?vhost=live.jnoo.com
         * share_url : http://jy.jnooo23.top/index.php?s=/Live/Index/show/room_id/258216.html
         */

        private String id;
        private String user_login;
        private String user_nicename;
        private String avatar;
        private String live_rtmp;
        private String share_url;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", user_login='" + user_login + '\'' +
                    ", user_nicename='" + user_nicename + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", live_rtmp='" + live_rtmp + '\'' +
                    ", share_url='" + share_url + '\'' +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
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

        public String getLive_rtmp() {
            return live_rtmp;
        }

        public void setLive_rtmp(String live_rtmp) {
            this.live_rtmp = live_rtmp;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}
