package com.redhat.darcy.web.matchers;

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
    public void shouldMatchClass() {
        HasClass<HtmlElement> matcher = new HasClass<>("clazz");
        HtmlElement mockElement = mock(HtmlElement.class);

        List<String> classes = new ArrayList<>();
        classes.add("clazz");
        when(mockElement.getClasses()).thenReturn(classes);

        assertTrue(matcher.matches(mockElement));
    }

    @Test
    public void shouldNotMatchClass() {
        HasClass<HtmlElement> matcher = new HasClass<>("missing");
        HtmlElement mockElement = mock(HtmlElement.class);

        List<String> classes = new ArrayList<>();
        classes.add("clazz");
        when(mockElement.getClasses()).thenReturn(classes);

        assertFalse(matcher.matches(mockElement));
    }
}
