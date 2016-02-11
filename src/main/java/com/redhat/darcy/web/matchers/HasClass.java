package com.redhat.darcy.web.matchers;

import com.redhat.darcy.web.api.elements.HtmlElement;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HasClass<T extends HtmlElement> extends TypeSafeMatcher<T> {
    private String expectedClass;

    public HasClass(String expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    protected boolean matchesSafely(T t) {
        return t.getClasses().contains(expectedClass);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an Element that has the class '" + expectedClass + "'");
    }
}
