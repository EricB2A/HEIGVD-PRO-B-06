package com.gdx.uch2.networking.client;

public class ErrorHandler {
    private String error;
    private boolean set;

    private static class Instance {
        private static final ErrorHandler instance = new ErrorHandler();
    }

    private ErrorHandler() {
        set = false;
    }

    public static ErrorHandler getInstance() {
        return Instance.instance;
    }

    public boolean isSet() {
        return set;
    }

    public void setError(String error) {
        System.out.println(error);
        this.error = error;
        set = true;
    }

    public String getError() {
        return error;
    }

    public void reset() {
        set = false;
    }
}
