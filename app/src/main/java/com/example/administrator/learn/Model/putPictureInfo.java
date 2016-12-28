package com.example.administrator.learn.Model;

/**
 * Created by Administrator on 2016/12/27.
 */

public class putPictureInfo {

    /**
     * status : 1
     * msg : 上传成功
     * data : {"name":"QQ截图20150722143925.png","type":"image/png","size":104473,"key":"qqq","ext":"png","md5":"3ecda0193e941494f49b57b135ab8937","sha1":"07028d08edac6573df9632a0eb05d92bfee8f972","savename":"5860c10c073b0.png","savepath":"2016-12-26/","title":"QQ截图20150722143925.png","original":"http://img.jnooo.xin/dating/2016-12-26/15/f8b97f7163642e90595381fc09246210.png","url":"http://img.jnooo.xin/dating/2016-12-26/15/320c971418d6e87ae07be813e1957ffd.png"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "putPictureInfo{" +
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
        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", size=" + size +
                    ", key='" + key + '\'' +
                    ", ext='" + ext + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", sha1='" + sha1 + '\'' +
                    ", savename='" + savename + '\'' +
                    ", savepath='" + savepath + '\'' +
                    ", title='" + title + '\'' +
                    ", original='" + original + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        /**
         * name : QQ截图20150722143925.png
         * type : image/png
         * size : 104473
         * key : qqq
         * ext : png
         * md5 : 3ecda0193e941494f49b57b135ab8937
         * sha1 : 07028d08edac6573df9632a0eb05d92bfee8f972
         * savename : 5860c10c073b0.png
         * savepath : 2016-12-26/
         * title : QQ截图20150722143925.png
         * original : http://img.jnooo.xin/dating/2016-12-26/15/f8b97f7163642e90595381fc09246210.png
         * url : http://img.jnooo.xin/dating/2016-12-26/15/320c971418d6e87ae07be813e1957ffd.png
         */

        private String name;
        private String type;
        private int size;
        private String key;
        private String ext;
        private String md5;
        private String sha1;
        private String savename;
        private String savepath;
        private String title;
        private String original;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public String getSavename() {
            return savename;
        }

        public void setSavename(String savename) {
            this.savename = savename;
        }

        public String getSavepath() {
            return savepath;
        }

        public void setSavepath(String savepath) {
            this.savepath = savepath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
