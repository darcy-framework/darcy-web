/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-web.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.web;

import static com.redhat.synq.Synq.after;
import static java.util.concurrent.TimeUnit.MINUTES;

import com.redhat.darcy.ui.View;

/**
 * Useful for implementations that require many windows to report to one underlying "master" object,
 * the "manager."
 */
public abstract class ManagedBrowserContext implements BrowserContext {
    private final BrowserManager manager;
    
    public ManagedBrowserContext(BrowserManager manager) {
        this.manager = manager;
    }

    @Override
    public <T extends View> T open(Url<T> url) {
        return after(() -> manager.open(url.url(), this))
                .expect(transition().to(url.destination()))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T open(String url, T destination) {
        return open(new StaticUrl<T>(url, destination));
    }

    @Override
    public String getCurrentUrl() {
        return manager.getCurrentUrl(this);
    }

    @Override
    public String getTitle() {
        return manager.getTitle(this);
    }
    
    @Override
    public String getSource() {
        return manager.getSource(this);
    }

    @Override
    public <T extends View> T back(T destination) {
        return after(() -> manager.back(this))
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T forward(T destination) {
        return after(() -> manager.forward(this))
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T refresh(T destination) {
        return after(() -> manager.refresh(this))
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public void close() {
        manager.close(this);
    }
    
    @Override
    public void closeAll() {
        manager.closeAll();
    }
}
