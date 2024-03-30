package com.example.cyberquize.Models;

public class DiscoverModel {

    String topicImg,topicName;

    public DiscoverModel() {
    }

    public DiscoverModel(String topicImg, String topicName) {
        this.topicImg = topicImg;
        this.topicName = topicName;
    }

    public String getTopicImg() {
        return topicImg;
    }

    public void setTopicImg(String topicImg) {
        this.topicImg = topicImg;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
