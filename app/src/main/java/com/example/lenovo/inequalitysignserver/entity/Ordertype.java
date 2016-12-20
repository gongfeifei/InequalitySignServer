package com.example.lenovo.inequalitysignserver.entity;

/**
 * Created by lenovo on 2016/12/19.
 */
public class Ordertype {
    private String name_type1;
    private String name_type2;
    private String name_type3;

    public Ordertype(String name_type1, String name_type2, String name_type3) {
        this.name_type1 = name_type1;
        this.name_type2 = name_type2;
        this.name_type3 = name_type3;
    }

    public String getName_type1() {
        return name_type1;
    }

    public void setName_type1(String name_type1) {
        this.name_type1 = name_type1;
    }

    public String getName_type2() {
        return name_type2;
    }

    public void setName_type2(String name_type2) {
        this.name_type2 = name_type2;
    }

    public String getName_type3() {
        return name_type3;
    }

    public void setName_type3(String name_type3) {
        this.name_type3 = name_type3;
    }
}
