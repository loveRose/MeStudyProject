package com.lvyerose.baseadapterhelper.bean;

/**
 * author: lvyeRose
 * objective:
 * mailbox: lvyerose@163.com
 * time: 15/8/13 11:57
 */
public class MeDatas {
    private String title;
    private String content;
    private String url;

    public MeDatas() {
    }

    public MeDatas(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
