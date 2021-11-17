package com.diego.securitysystem.models;

public class HistoryLog {

    private Long date;
    String event;

    public HistoryLog(String event, Long date) {
        this.event = event;
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
