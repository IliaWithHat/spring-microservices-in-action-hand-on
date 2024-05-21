package org.ilia.licensingservice.utils;

import org.springframework.util.Assert;

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

    public static void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }
}
