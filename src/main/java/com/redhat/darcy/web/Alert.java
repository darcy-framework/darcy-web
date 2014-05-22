package com.redhat.darcy.web;

public interface Alert {
    boolean isPresent();
    void accept();
    void dismiss();
    void sendKeys(String text);
    String readText();
}
