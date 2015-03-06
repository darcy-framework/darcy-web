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

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class CookieTest {

    @Test
    public void shouldBeACookie() {
        new Cookie("chocolate", "chip", "", "", null, false);
    }

    @Test
    public void shouldValidateAValidCookie() {
        new Cookie("chocolate", "chip", "", "", null, false)
                .validate();
    }

    @Test
    public void shouldFailValidationForACookieWithAnIllegalName() {
        try {
            new Cookie("chocolate;", "chip", "", "", null, false).validate();
            fail();
        }
        catch(IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void shouldPrintCookieToString() {
        System.out.println(new Cookie("chocolate", "chip", "", "", LocalDateTime.now(), false)
                .toString());
    }
}
