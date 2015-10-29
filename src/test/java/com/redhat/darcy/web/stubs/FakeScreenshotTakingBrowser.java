package com.redhat.darcy.web.stubs;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.CookieManager;
import com.redhat.darcy.web.api.WebSelection;
import com.redhat.synq.Event;

import java.io.IOException;
import java.io.OutputStream;

public class FakeScreenshotTakingBrowser implements Browser {
    private byte[] data;

    public FakeScreenshotTakingBrowser(byte[] data) {
        this.data = data;
    }

    @Override
    public <T extends View> Event<T> open(String url, T destination) {
        throw new UnsupportedOperationException("open");
    }

    @Override
    public String getCurrentUrl() {
        throw new UnsupportedOperationException("getCurrentUrl");
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("getTitle");
    }

    @Override
    public String getSource() {
        throw new UnsupportedOperationException("getSource");
    }

    @Override
    public <T extends View> Event<T> back(T destination) {
        throw new UnsupportedOperationException("back");
    }

    @Override
    public <T extends View> Event<T> forward(T destination) {
        throw new UnsupportedOperationException("forward");
    }

    @Override
    public <T extends View> Event<T> refresh(T destination) {
        throw new UnsupportedOperationException("refresh");
    }

    @Override
    public CookieManager cookies() {
        throw new UnsupportedOperationException("cookies");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("close");
    }

    @Override
    public void closeAll() {
        throw new UnsupportedOperationException("closeAll");
    }

    @Override
    public void takeScreenshot(OutputStream outputStream) {
        try {
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new DarcyException(e);
        }
    }

    @Override
    public WebSelection find() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}
