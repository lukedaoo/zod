package com.infamous.framework.persistence.utils;

public class StatusMessage {

    public static final StatusMessage SUCCESS = new StatusMessage("SUCCESS");

    private String m_message;

    public StatusMessage(String message) {
        m_message = message;
    }

    public void print() {
        System.out.println(m_message);
    }

    public String getStatus() {
        return m_message;
    }

    public void setStatus(String status) {
        m_message = status;
    }
}
