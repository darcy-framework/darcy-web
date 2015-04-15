/*
Modified by Red Hat

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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Cookie {
    private final String name;
    private final String value;
    private final String path;
    private final String domain;
    private final LocalDateTime expiry;
    private final boolean isSecure;
    private final boolean isHttpOnly;

    /**
     * Creates an insecure non-httpOnly cookie with no domain specified.
     *
     * @param name The name of the cookie; may not be null or an empty string.
     * @param value The cookie value; may not be null.
     * @param path The path the cookie is visible to. If left blank or set to null, will be set to
     *        "/".
     * @param expiry The cookie's expiration date; may be null.
     * @see #Cookie(String, String, String, String, LocalDateTime)
     */
    public Cookie(String name, String value, String path, LocalDateTime expiry) {
        this(name, value, null, path, expiry);
    }

    /**
     * Creates an insecure non-httpOnly cookie.
     *
     * @param name The name of the cookie; may not be null or an empty string.
     * @param value The cookie value; may not be null.
     * @param domain The domain the cookie is visible to.
     * @param path The path the cookie is visible to. If left blank or set to null, will be set to
     *        "/".
     * @param expiry The cookie's expiration date; may be null.
     * @see #Cookie(String, String, String, String, LocalDateTime, boolean)
     */
    public Cookie(String name, String value, String domain, String path, LocalDateTime expiry) {
        this(name, value, domain, path, expiry, false);
    }

    /**
     * Creates a non-httpOnly cookie.
     *
     * @param name The name of the cookie; may not be null or an empty string.
     * @param value The cookie value; may not be null.
     * @param domain The domain the cookie is visible to.
     * @param path The path the cookie is visible to. If left blank or set to null, will be set to
     *        "/".
     * @param expiry The cookie's expiration date; may be null.
     * @param isSecure Whether this cookie requires a secure connection.
     */
    public Cookie(String name, String value, String domain, String path, LocalDateTime expiry,
            boolean isSecure) {
        this(name, value, domain, path, expiry, isSecure, false);
    }

    /**
     * Creates a cookie.
     *
     * @param name The name of the cookie; may not be null or an empty string.
     * @param value The cookie value; may not be null.
     * @param domain The domain the cookie is visible to.
     * @param path The path the cookie is visible to. If left blank or set to null, will be set to
     *        "/".
     * @param expiry The cookie's expiration date; may be null.
     * @param isSecure Whether this cookie requires a secure connection.
     * @param isHttpOnly Whether this cookie is a httpOnly cooke.
     */
    public Cookie(String name, String value, String domain, String path, LocalDateTime expiry,
            boolean isSecure, boolean isHttpOnly) {
        this.name = name;
        this.value = value;
        this.path = path == null || path.isEmpty() ? "/" : path;

        this.domain = stripPort(domain);
        this.isSecure = isSecure;
        this.isHttpOnly = isHttpOnly;

        if(expiry != null) {
            expiry = expiry.truncatedTo(ChronoUnit.SECONDS);
        }

        this.expiry = expiry;
    }

    /**
     * Create a cookie for the default path with the given name and value with no expiry set.
     *
     * @param name The cookie's name
     * @param value The cookie's value
     */
    public Cookie(String name, String value) {
        this(name, value, "/", null);
    }

    /**
     * Create a cookie.
     *
     * @param name The cookie's name
     * @param value The cookie's value
     * @param path The path the cookie is for
     */
    public Cookie(String name, String value, String path) {
        this(name, value, path, null);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public boolean isHttpOnly() {
        return isHttpOnly;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    private static String stripPort(String domain) {
        return (domain == null) ? null : domain.split(":")[0];
    }

    public void validate() {
        if (name == null || "".equals(name) || value == null || path == null) {
            throw new IllegalArgumentException("Required attributes are not set or " +
                    "any non-null attribute set to null");
        }

        if (name.indexOf(';') != -1) {
            throw new IllegalArgumentException(
                    "Cookie names cannot contain a ';': " + name);
        }

        if (domain != null && domain.contains(":")) {
            throw new IllegalArgumentException("Domain should not contain a port: " + domain);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss z yyyy")
                .withZone(ZoneId.systemDefault());

        return name + "=" + value + (expiry == null ? "" : ", which expires on " +
                dtf.format(expiry)) + ("".equals(path) ? "" : "; path=" + path) +
                (domain == null ? "" : "; domain=" + domain) + (isSecure ? ";secure;" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cookie cookie = (Cookie) o;

        if (!name.equals(cookie.name)) {
            return false;
        }
        if (!value.equals(cookie.value)) {
            return false;
        }
        if (domain != null ? !domain.equals(cookie.domain) : cookie.domain != null) {
            return false;
        }
        if (expiry != null ? !expiry.equals(cookie.expiry) : cookie.expiry != null) {
            return false;
        }
        if (path != null ? !path.equals(cookie.path) : cookie.path != null) {
            return false;
        }
        if (isHttpOnly != cookie.isHttpOnly) {
            return false;
        }
        if (isSecure != cookie.isSecure) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static class Builder {

        private final String name;
        private final String value;
        private String path;
        private String domain;
        private LocalDateTime expiry;
        private boolean secure;
        private boolean httpOnly;

        public Builder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Builder domain(String host) {
            this.domain = stripPort(host);
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder expiresOn(LocalDateTime expiry) {
            this.expiry = expiry;
            return this;
        }

        public Builder isSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder isHttpOnly(boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public Cookie build() {
            return new Cookie(name, value, domain, path, expiry, secure, httpOnly);
        }
    }
}
