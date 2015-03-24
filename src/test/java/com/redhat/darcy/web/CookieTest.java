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


THIS FILE INCLUDES CODE THAT HAS BEEN MODIFIED FROM ITS ORIGIN FORM
AND IS COVERED BY THE FOLLOWING:


Copyright 2007-2009 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.redhat.darcy.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class CookieTest {

    @Test
    public void shouldCreateAWellFormedCookie() {
        new Cookie("Fish", "cod", "", "", null, false);
    }

    @Test
    public void shouldThrowAnExceptionWhenSemiColonExistsInTheCookieAttribute() {
        Cookie cookie = new Cookie("hi;hi", "value", null, null, null, false);
        try {
            cookie.validate();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void shouldThrowAnExceptionTheNameIsNull() {
        Cookie cookie = new Cookie(null, "value", null, null, null, false);
        try {
            cookie.validate();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void shouldAllowSecureToBeSet() {
        Cookie cookie = new Cookie("name", "value", "", "/", LocalDateTime.now(), true);
        assertTrue(cookie.isSecure());
    }

    @Test
    public void ShouldDefaultSecureToFalse() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.isSecure());
    }

    @Test
    public void shouldAllowHttpOnlyToBeSet() {
        Cookie cookie = new Cookie("name", "value", "", "/", LocalDateTime.now(), false, true);
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    public void httpShouldOnlyDefaultToFalse() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.isHttpOnly());
    }
}
