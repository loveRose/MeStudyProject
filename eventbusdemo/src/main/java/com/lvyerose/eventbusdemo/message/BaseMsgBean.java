package com.lvyerose.eventbusdemo.message;

/**
 * author: lvyeRose
 * objective: 基本消息实体
 * mailbox: lvyerose@163.com
 * time: 15/8/11 15:13
 */
public class BaseMsgBean {
    private int por;
    private String title;
    private String content;
    //...


    public BaseMsgBean() {
    }

    public BaseMsgBean(int por, String title, String content) {
        this.por = por;
        this.title = title;
        this.content = content;
    }

    public int getPor() {
        return por;
    }

    public void setPor(int por) {
        this.por = por;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
