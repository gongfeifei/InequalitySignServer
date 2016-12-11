package com.example.lenovo.inequalitysignserver.entity;

/**
 * Created by lenovo on 2016/11/30.
 */
public class Account {
    public int id;
    public String shop_id;
    public String shop_pwd;
    public byte[] shop_img_small;
    public byte[] shop_img_big;
    public String shop_name;
    public String shop_type;
    public String shop_address;
    public String shop_tel;
    public String shop_city;
    public String shop_description;

    public Account() {
//        this.shop_id = "";
//        this.shop_pwd = "";
//        this.shop_img_small = "";
//        this.shop_img_big = "";
//        this.shop_name = "";
//        this.shop_type = "";
//        this.shop_address = "";
//        this.shop_tel = "";
//        this.shop_city = "";
//        this.shop_description = "";
    }

    @Override
    public String toString() {
        String result = "";
//        result += "ID:" + this.id + ",";
        result += "用户名:" + this.shop_id + ",";
        result += "密码:" + shop_pwd + ",";
        result += "商家图片（小）:" + this.shop_img_small + ",";
        result += "商家图片（大）:" + this.shop_img_big + ",";
        result += "商家名称:" + this.shop_name + ",";
        result += "商家类型:" + this.shop_type + ",";
        result += "所在地址:" + this.shop_address + ",";
        result += "联系方式:" + this.shop_tel + ",";
        result += "城市:" + this.shop_city + ",";
        result += "描述信息:" + this.shop_description + ",";

        return result;
    }
}
