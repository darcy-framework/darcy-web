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

import java.util.List;

import com.redhat.darcy.ui.Context;
import com.redhat.darcy.ui.Locator;

public abstract class By extends com.redhat.darcy.ui.By {
    public static Locator cssSelector(String css) {
        return new ByCss(css);
    }
    
    public static Locator htmlTag(String tag) {
        return new ByHtmlTag(tag);
    }
    
    public static class ByCss implements Locator {
        private final String css;
        
        public ByCss(String css) {
            this.css = css;
        }

        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByCssSelector) context).findAllByCssSelector(type, css);
        }
        
    }
    
    public static class ByHtmlTag implements Locator {
        private final String tag;
        
        public ByHtmlTag(String tag) {
            this.tag = tag;
        }

        @Override
        public <T> List<T> findAll(Class<T> type, Context context) {
            return ((FindsByHtmlTag) context).findAllByHtmlTag(type, tag);
        }
        
    }
}
