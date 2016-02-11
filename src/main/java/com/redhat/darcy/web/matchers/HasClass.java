package com.redhat.darcy.web.matchers;

import com.redhat.darcy.web.api.elements.HtmlElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HasClass<T extends HtmlElement> extends TypeSafeMatcher<T> {
    private Matcher<? super String> matcher;

    public HasClass(Matcher<? super String> matcher) {
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(T t) {
        for (String clazz : t.getClasses()) {
            if (matcher.matches(clazz)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an Element that has the class");
        matcher.describeTo(description);
    }
}
