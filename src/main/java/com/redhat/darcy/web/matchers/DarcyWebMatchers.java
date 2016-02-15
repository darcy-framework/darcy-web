package com.redhat.darcy.web.matchers;

import com.redhat.darcy.ui.matchers.DarcyMatchers;
import com.redhat.darcy.web.api.elements.HtmlElement;

import org.hamcrest.Matcher;

public abstract class DarcyWebMatchers extends DarcyMatchers {
    public static <T extends HtmlElement> Matcher<T> hasClass(Matcher<? super String> matcher) {
        return new HasClass<>(matcher);
    }
}
