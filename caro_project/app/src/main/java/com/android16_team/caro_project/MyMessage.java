package com.android16_team.caro_project;

public class MyMessage {
    private String message;
    private boolean mode;

    public MyMessage(String message, boolean mode) {
        this.message = message;
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}
