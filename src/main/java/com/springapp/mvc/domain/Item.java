package com.springapp.mvc.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class Item {
    private String id;
    private String from;
    private String to;
    private Date createDate;
    private String message;
    private String bookingDate;
    private String status;

    public Item(String id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("from", from)
                .append("to", to)
                .append("createDate", createDate)
                .append("message", message)
                .append("bookingDate", bookingDate)
                .append("status", status)
                .toString();
    }
}
