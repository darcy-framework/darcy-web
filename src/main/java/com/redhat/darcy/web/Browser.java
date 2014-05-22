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

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.View;

public interface Browser {
    <T extends View> T open(Url<T> url);
    <T extends View> T open(String url, T destination);
    String getCurrentUrl();
    String getTitle();
    String getSource();
    <T extends View> T back(T destination);
    <T extends View> T forward(T destination);
    <T extends View> T refresh(T destination);
    FrameContext frame(Locator locator);
    void close();
    void closeAll();
}
