package org.ilia.licensingservice.utils;

public final class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = userContext.get();

        if (context == null) {
            userContext.set(new UserContext());
            return userContext.get();
        }
        return context;
    }
}
