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
import com.redhat.darcy.ui.elements.Findable;

/**
 * Abstracts all of the interactions a user might make with a browser.
 */
public interface Browser extends WebContext, Findable {
    /**
     * Opens the URL and blocks until the associated {@link com.redhat.darcy.ui.View} is loaded, as
     * defined by the {@link Url} parameter.
     *
     * @param url
     * @return
     */
    <T extends View> T open(Url<T> url);

    /**
     * Opens the URL and blocks until the associated {@link com.redhat.darcy.ui.View} is loaded.
     *
     * @param url
     * @param destination
     * @return
     */
    <T extends View> T open(String url, T destination);

    /**
     * @return the current URL string this Browser window is pointing to.
     */
    String getCurrentUrl();

    /**
     * @return the title of the current page present in the Browser window.
     */
    String getTitle();

    /**
     * The HTML source code of the current page present in the Browser window.
     *
     * @return
     */
    String getSource();

    /**
     * Navigates "back" in the Browser history, and awaits for some expected destination {@link
     * View} to load as a result.
     *
     * @param destination
     * @return
     */
    <T extends View> T back(T destination);

    /**
     * Navigates "forward" in the Browser history, and awaits for some expected destination {@link
     * View} to load as a result.
     *
     * @param destination
     * @return
     */
    <T extends View> T forward(T destination);

    /**
     * Simulates clicking the "refresh" button within a browser, and waits for some expected
     * destination {@link View} to load as a result.
     *
     * @param destination
     * @return
     */
    <T extends View> T refresh(T destination);

    void close();

    void closeAll();

    @Override
    WebSelection find();
}
