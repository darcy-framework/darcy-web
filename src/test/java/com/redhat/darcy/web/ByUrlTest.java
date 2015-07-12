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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.LocatorNotSupportedException;
import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.internal.FindsByUrl;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class ByUrlTest {
    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportExceptionWhenFindingOneIfContextDoesNotSupportIt() {
        Context context = mock(Context.class);
        By.url("foo").find(Browser.class, context);
    }

    @Test(expected = LocatorNotSupportedException.class)
    public void shouldThrowLocatorNotSupportExceptionWhenFindingAllIfContextDoesNotSupportIt() {
        Context context = mock(Context.class);
        By.url("foo").findAll(Browser.class, context);
    }

    @Test
    public void shouldUseFindsByUrlToFindAllByUrl() {
        TestContext context = mock(TestContext.class);
        List found = mock(List.class);
        when(context.findAllByUrl(eq(Browser.class), refEq(equalTo("foo")))).thenReturn(found);
        assertSame(found, By.url("foo").findAll(Browser.class, context));
    }

    @Test
    public void shouldUseFindsByUrlToFindByUrl() {
        TestContext context = mock(TestContext.class);
        Browser found = mock(Browser.class);
        when(context.findByUrl(eq(Browser.class), refEq(equalTo("foo")))).thenReturn(found);
        assertSame(found, By.url("foo").find(Browser.class, context));
    }

    @Test
    // KNOWN ISSUE: Most matchers do not implement equals
    public void shouldCorrectlyImplementEquals() {
        Matcher equalToFoo = equalTo("foo");
        By.ByUrl byUrlFoo = By.url(equalToFoo);
        assertEquals(byUrlFoo, byUrlFoo);
        assertEquals(byUrlFoo, By.url(equalToFoo));
        assertNotEquals(byUrlFoo, By.url("bar"));
        assertEquals(byUrlFoo.hashCode(), By.url(equalToFoo).hashCode());
    }

    interface TestContext extends Context, FindsByUrl {}
}
