package com.redhat.darcy.web.api;

import com.redhat.darcy.web.Cookie;

import java.util.Set;

public interface CookieManager{
    void add(Cookie cookie);
    void delete(Cookie cookie);
    void deleteAll();
    Set<Cookie> getAll();
    Cookie get(Cookie cookie);
    Cookie getNamed(String name);
}
