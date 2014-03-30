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

import com.redhat.darcy.ui.View;
import com.redhat.darcy.ui.ViewContext;

public abstract class BrowserContext implements Browser, ViewContext {
    protected final BrowserManager manager;
    
    public BrowserContext(BrowserManager manager) {
        this.manager = manager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T open(Url<T> url) {
        manager.open(url.url(), this);
        return (T) url.forView().setContext(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T open(String url, T destination) {
        manager.open(url, this);
        return (T) destination.setContext(this);
    }

    @Override
    public String getCurrentUrl() {
        return manager.getCurrentUrl(this);
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        manager.close(this);
    }
}
