package com.redhat.darcy.web.matchers;

import com.redhat.darcy.web.api.elements.HtmlElement;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class HasClass<T extends HtmlElement> extends TypeSafeDiagnosingMatcher<T> {
    private Matcher<? super String> matcher;

    public HasClass(Matcher<? super String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an element that has the class ");
        matcher.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(T t, Description description) {
        for (String clazz : t.getClasses()) {
            if (matcher.matches(clazz)) {
                return true;
            }
        }

        description.appendText("could not find a matching class in ")
                .appendValue(t.getClasses());

        return false;
    }
}
