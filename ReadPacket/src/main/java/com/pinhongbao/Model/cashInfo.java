package com.pinhongbao.Model;

/**
 * Created by Administrator on 2017/2/6.
 */

public class cashInfo {
    String amount;
    int image;

    @Override
    public String toString() {
        return "cashInfo{" +
                "amount='" + amount + '\'' +
                ", image=" + image +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
