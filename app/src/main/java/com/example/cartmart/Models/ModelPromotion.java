package com.example.cartmart.Models;

public class ModelPromotion {
    String id, url;

    public ModelPromotion() {
    }

    public ModelPromotion(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
