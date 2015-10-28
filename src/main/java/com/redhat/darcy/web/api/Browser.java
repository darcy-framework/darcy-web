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

package com.redhat.darcy.web.api;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.View;
import com.redhat.synq.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Abstracts all of the interactions a user might make with a browser.
 */
public interface Browser extends FindableWebContext {
    /**
     * Constructs an {@link com.redhat.synq.Event} that will opens the URL and block until the
     * associated {@link com.redhat.darcy.ui.api.View} is loaded, as defined by the {@link ViewUrl}
     * parameter. Note that merely calling this function does not actually <em>open</em> the URL and
     * wait. You have to call {@link Event#waitUpTo(java.time.Duration)}, the core terminal
     * operation in the Event DSL.
     * <p>
     * Because an {@link com.redhat.synq.Event} is returned, the event that you ultimately wait for
     * can be further configured if desired, adding {@link Event#failIf(com.redhat.synq.Event)}
     * clauses, and the like.
     *
     * @param viewUrl If you don't have a {@link com.redhat.darcy.web.api.ViewUrl} instance, but you
     * know the url and the resulting {@link com.redhat.darcy.ui.api.View}, see
     * {@link #open(String, com.redhat.darcy.ui.api.View)}.
     * @return An {@link com.redhat.synq.Event} that can be further configured and awaited. This
     * event is set up to expect a {@link com.redhat.darcy.ui.api.TransitionEvent} to occur to the
     * view specified in {@code viewUrl}.
     */
    default <T extends View> Event<T> open(ViewUrl<T> viewUrl) {
        return open(viewUrl.url(), viewUrl.destination());
    }

    /**
     * Constructs an event that will opens the URL and block until the associated
     * {@link com.redhat.darcy.ui.api.View} is loaded, as defined by the {@link ViewUrl} parameter.
     * Note that merely calling this function does not actually <em>open</em> the URL and wait. You
     * have to call {@link Event#waitUpTo(java.time.Duration)}, the core terminal operation in
     * the Event DSL.
     * <p>
     * Because an {@link com.redhat.synq.Event} is returned, the event that you ultimately wait for
     * can be further configured if desired, adding {@link Event#failIf(com.redhat.synq.Event)}
     * clauses, and the like.
     *
     * @param destination The {@link com.redhat.darcy.ui.api.View} you expect to be loaded after
     * navigating to the specified url. The returned {@link com.redhat.synq.Event} will be
     * configured to wait for a {@link com.redhat.darcy.ui.api.TransitionEvent} to occur for this
     * view.
     * @return An {@link com.redhat.synq.Event} that can be further configured and awaited.
     */
    <T extends View> Event<T> open(String url, T destination);

    /**
     * Opens the URL and immediately blocks the thread for a maximum of the specified
     * duration, after which a {@link com.redhat.synq.TimeoutException} will be thrown.
     * @param viewUrl If you don't have a {@link com.redhat.darcy.web.api.ViewUrl} instance, but you
     * know the url and the resulting {@link com.redhat.darcy.ui.api.View}, see
     * {@link #open(String, com.redhat.darcy.ui.api.View)}.
     * @param duration Maximum specified duration of the view loading
     * @return The awaited view once it has met all criteria for loading
     */
    default <T extends View> T openAndWaitUpTo(ViewUrl<T> viewUrl, Duration duration) {
        return open(viewUrl).waitUpTo(duration);
    }

    /**
     * Opens the URL and immediately blocks the thread for a maximum of the duration of the
     * specified amount of units, after which a {@link com.redhat.synq.TimeoutException} will be
     * thrown.
     * @param viewUrl If you don't have a {@link com.redhat.darcy.web.api.ViewUrl} instance, but you
     * know the url and the resulting {@link com.redhat.darcy.ui.api.View}, see
     * {@link #open(String, com.redhat.darcy.ui.api.View)}.
     * @param amount The amount of the duration, expressed in units
     * @param unit The unit the duration is measured in
     * @return The awaited view once it has met all criteria for loading
     */
    default <T extends View> T openAndWaitUpTo(ViewUrl<T> viewUrl, Long amount, ChronoUnit unit) {
        return open(viewUrl).waitUpTo(Duration.of(amount, unit));
    }

    /**
     * @return the current URL string this Browser window is pointing to.
     */
    String getCurrentUrl();

    /**
     * @return the title of the current page present in the Browser window.
     */
    String getTitle();

    /**
     * @return The HTML source code of the current page present in the Browser window.
     */
    String getSource();

    /**
     * Constructs an {@link com.redhat.synq.Event} that navigates "back" in the Browser history,
     * and awaits for some expected destination {@link com.redhat.darcy.ui.api.View} to load as a
     * result.
     */
    <T extends View> Event<T> back(T destination);

    /**
     * Constructs an {@link com.redhat.synq.Event} that navigates "forward" in the Browser history,
     * and awaits for some expected destination {@link com.redhat.darcy.ui.api.View} to load as a
     * result.
     */
    <T extends View> Event<T> forward(T destination);

    /**
     * Constructs an {@link com.redhat.synq.Event} that refreshes the browser and awaits for some
     * expected destination {@link com.redhat.darcy.ui.api.View} to load as a result.
     */
    <T extends View> Event<T> refresh(T destination);

    CookieManager cookies();

    void close();

    void closeAll();

    /**
     * Takes a screenshot as bytes and writes to the provided {@link OutputStream}.
     * Consumers should consult the implementation to see what image format the screenshot
     * is written as.
     * <p>
     * Implementations should handle closing the {@link OutputStream}.
     */
    void takeScreenshot(OutputStream outputStream);

    /**
     * Takes a screenshot and writes it to the provided {@link Path}. To determine
     * what file extension to use in the provided {@link Path}, consumers should look
     * to the documentation for the {@link #takeScreenshot(OutputStream)} implementation
     * being used.
     * <p>
     * Any nonexistent directories included in the {@link Path} will be created.
     * An exception will not be thrown if the directories already exist.
     * @param path The {@link Path} of the desired file destination.
     */
    default void takeScreenshot(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            OutputStream fileOut = Files.newOutputStream(path);
            takeScreenshot(fileOut);
        } catch (IOException e) {
            throw new DarcyException("Could not take screenshot", e);
        }
    }

    @Override
    WebSelection find();
}
