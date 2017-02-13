package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/10.
 */

public class redPackRecordInfo {
    @Override
    public String toString() {
        return "redPackRecordInfo{" +
                "id='" + id + '\'' +
                ", uniacid=" + uniacid +
                ", rid='" + rid + '\'' +
                ", uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", kind='" + kind + '\'' +
                ", pay='" + pay + '\'' +
                ", back='" + back + '\'' +
                ", block='" + block + '\'' +
                ", paystatus='" + paystatus + '\'' +
                ", backstatus='" + backstatus + '\'' +
                ", refund='" + refund + '\'' +
                ", paytime='" + paytime + '\'' +
                ", backtime='" + backtime + '\'' +
                ", virtual='" + virtual + '\'' +
                ", title='" + title + '\'' +
                ", num='" + num + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * id : 7177
     * uniacid : null
     * rid : 5896
     * uid : 200086
     * type : 0
     * kind : 0
     * pay : 1
     * back : 0.21
     * block : 1
     * paystatus : 1
     * backstatus : 1
     * refund : 0
     * paytime : 1486630420
     * backtime : 1486631857
     * virtual : 0
     * title : 恭喜发财大吉大利!
     * num : 2
     * status : 1
     */

    private String id;
    private Object uniacid;
    private String rid;
    private String uid;
    private String type;
    private String kind;
    private String pay;
    private String back;
    private String block;
    private String paystatus;
    private String backstatus;
    private String refund;
    private String paytime;
    private String backtime;
    private String virtual;
    private String title;
    private String num;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getUniacid() {
        return uniacid;
    }

    public void setUniacid(Object uniacid) {
        this.uniacid = uniacid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getBackstatus() {
        return backstatus;
    }

    public void setBackstatus(String backstatus) {
        this.backstatus = backstatus;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getBacktime() {
        return backtime;
    }

    public void setBacktime(String backtime) {
        this.backtime = backtime;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
