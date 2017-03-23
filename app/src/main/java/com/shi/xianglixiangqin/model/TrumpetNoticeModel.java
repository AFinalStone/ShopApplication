package com.shi.xianglixiangqin.model;

import java.io.Serializable;

/**
 * 通知对象
 * Created by SHI on 2016/12/29 11:24
 */
public class TrumpetNoticeModel implements Serializable {
    private String Content;
    private int ID;
    private String Title;

    public TrumpetNoticeModel(String content, int ID, String title) {
        Content = content;
        this.ID = ID;
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
