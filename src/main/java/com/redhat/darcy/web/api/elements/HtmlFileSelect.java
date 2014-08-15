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

package com.redhat.darcy.web.api.elements;

import com.redhat.darcy.ui.api.elements.FileSelect;

import java.util.Set;

public interface HtmlFileSelect extends HtmlElement, FileSelect {
    /**
     * The {@code accept} attribute of file input elements defines a list of file types that the
     * file picker will allow.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/Input#attr-accept">MDN Article on Input Elements</a>
     * @return The comma delimited {@code accept} attribute value split into a
     * {@link java.util.Set}.
     */
    Set<String> getAcceptedTypes();
}
