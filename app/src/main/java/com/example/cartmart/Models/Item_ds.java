package com.example.cartmart.Models;

public class Item_ds {
    private String str_name_ds, str_phone_ds, str_address_ds, str_status_ds;

    public Item_ds() {
    }

    public Item_ds(String str_name_ds, String str_phone_ds, String str_address_ds, String str_status_ds) {
        this.str_name_ds = str_name_ds;
        this.str_phone_ds = str_phone_ds;
        this.str_address_ds = str_address_ds;
        this.str_status_ds = str_status_ds;
    }

    public String getStr_name_ds() {
        return str_name_ds;
    }

    public void setStr_name_ds(String str_name_ds) {
        this.str_name_ds = str_name_ds;
    }

    public String getStr_phone_ds() {
        return str_phone_ds;
    }

    public void setStr_phone_ds(String str_phone_ds) {
        this.str_phone_ds = str_phone_ds;
    }

    public String getStr_address_ds() {
        return str_address_ds;
    }

    public void setStr_address_ds(String str_address_ds) {
        this.str_address_ds = str_address_ds;
    }

    public String getStr_status_ds() {
        return str_status_ds;
    }

    public void setStr_status_ds(String str_status_ds) {
        this.str_status_ds = str_status_ds;
    }
}
