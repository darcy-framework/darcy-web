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

package com.redhat.uiautotool.web;

import com.redhat.uiautotool.ui.View;

public class StaticUrl<T extends View> implements Url<T> {
    private final String url;
    private final T view;
    
    public StaticUrl(String url, T view) {
        this.url = url;
        this.view = view;
    }
    
    @Override
    public String url() {
        return url;
    }
    
    @Override
    public T forView() {
        return view;
    }
    
}
