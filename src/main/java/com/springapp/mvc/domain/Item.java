package com.springapp.mvc.domain;

import java.util.Date;

public class Item {
    private String id;
    private Date createDate;

    public Item(String id) {
        this.id = id;
        createDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
