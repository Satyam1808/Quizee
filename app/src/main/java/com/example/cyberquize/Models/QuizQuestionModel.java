package com.example.cyberquize.Models;

public class QuizQuestionModel {

    String correctOption,option01,option02,option03,option04,question;

    public QuizQuestionModel() {
    }

    public QuizQuestionModel(String correctOption, String option01, String option02, String option03, String option04, String question) {
        this.correctOption = correctOption;
        this.option01 = option01;
        this.option02 = option02;
        this.option03 = option03;
        this.option04 = option04;
        this.question = question;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getOption01() {
        return option01;
    }

    public void setOption01(String option01) {
        this.option01 = option01;
    }

    public String getOption02() {
        return option02;
    }

    public void setOption02(String option02) {
        this.option02 = option02;
    }

    public String getOption03() {
        return option03;
    }

    public void setOption03(String option03) {
        this.option03 = option03;
    }

    public String getOption04() {
        return option04;
    }

    public void setOption04(String option04) {
        this.option04 = option04;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
