package org.ilia.organizationservice.utils;

public final class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        if (userContext.get() == null) {
            userContext.set(createEmptyContext());
        }
        return userContext.get();
    }

    private static UserContext createEmptyContext() {
        return new UserContext();
    }
}
