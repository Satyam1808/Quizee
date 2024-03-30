package com.example.cyberquize.Models;

public class CyberNewsModel {

    String cyberNewsHeading,cyberNewsImg,cyberNewsWebUrl;

    public CyberNewsModel() {
    }

    public CyberNewsModel(String cyberNewsHeading, String cyberNewsImg, String cyberNewsWebUrl) {
        this.cyberNewsHeading = cyberNewsHeading;
        this.cyberNewsImg = cyberNewsImg;
        this.cyberNewsWebUrl = cyberNewsWebUrl;
    }

    public String getCyberNewsHeading() {
        return cyberNewsHeading;
    }

    public void setCyberNewsHeading(String cyberNewsHeading) {
        this.cyberNewsHeading = cyberNewsHeading;
    }

    public String getCyberNewsImg() {
        return cyberNewsImg;
    }

    public void setCyberNewsImg(String cyberNewsImg) {
        this.cyberNewsImg = cyberNewsImg;
    }

    public String getCyberNewsWebUrl() {
        return cyberNewsWebUrl;
    }

    public void setCyberNewsWebUrl(String cyberNewsWebUrl) {
        this.cyberNewsWebUrl = cyberNewsWebUrl;
    }
}
