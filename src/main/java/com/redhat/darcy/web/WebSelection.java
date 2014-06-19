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

import com.redhat.darcy.ui.ContextSelection;
import com.redhat.darcy.ui.ElementSelection;
import com.redhat.darcy.ui.Locator;

/**
 * Extends the default {@link ContextSelection} interface with some web-specific defaults.
 */
public interface WebSelection extends ContextSelection, ElementSelection {
    default Browser browser(Locator locator) {
        return contextOfType(Browser.class, locator);
    }

    default Frame frame(Locator locator) {
        return contextOfType(Frame.class, locator);
    }

    /**
     * Returns a reference to a Javascript alert window. Will not throw an exception immediately if
     * one is not open, but attempting to interact with one where none is present <em>will</em>
     * throw an exception. Because there can only be one alert within one window at a time,
     * providing a {@link com.redhat.darcy.ui.Locator} is unnecessary.
     *
     * @see Alert
     * @see Alert#isPresent()
     */
    Alert alert();
}
