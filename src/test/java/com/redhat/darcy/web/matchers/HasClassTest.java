package com.redhat.darcy.web.matchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.web.api.elements.HtmlElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class HasClassTest {
    @Test
    public void shouldMatchFullClass() {
        HasClass<HtmlElement> matcher = new HasClass<>(equalTo("clazz"));
        HtmlElement mockElement = mock(HtmlElement.class);

        List<String> classes = new ArrayList<>();
        classes.add("clazz");
        when(mockElement.getClasses()).thenReturn(classes);

        assertTrue(matcher.matches(mockElement));
    }

    @Test
    public void shouldMatchPartialClass() {
        HasClass<HtmlElement> matcher = new HasClass<>(containsString("cla"));
        HtmlElement mockElement = mock(HtmlElement.class);

        List<String> classes = new ArrayList<>();
        classes.add("clazz");
        when(mockElement.getClasses()).thenReturn(classes);

        assertTrue(matcher.matches(mockElement));
    }

    @Test
    public void shouldNotMatchMissingClass() {
        HasClass<HtmlElement> matcher = new HasClass<>(containsString("missing"));
        HtmlElement mockElement = mock(HtmlElement.class);

        List<String> classes = new ArrayList<>();
        classes.add("clazz");
        when(mockElement.getClasses()).thenReturn(classes);

        assertFalse(matcher.matches(mockElement));
    }
}
