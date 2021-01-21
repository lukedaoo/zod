package com.infamous.framework.http.factory;

import com.infamous.framework.http.ZodHttpException;
import java.lang.reflect.Method;

class Utils {

    public static RuntimeException parameterError(Method method, Throwable cause, int p, String message,
                                                  Object... args) {
        return methodError(method, cause, message + " (parameter #" + (p + 1) + ")", args);
    }

    public static RuntimeException parameterError(Method method, int p, String message, Object... args) {
        return methodError(method, message + " (parameter #" + (p + 1) + ")", args);
    }

    public static RuntimeException methodError(Method method, String message, Object... args) {
        return methodError(method, null, message, args);
    }

    public static RuntimeException methodError(Method method, Throwable cause, String message, Object... args) {
        message = String.format(message, args);
        return new ZodHttpException(
            message + " for method "
                + method.getDeclaringClass().getSimpleName()
                + "."
                + method.getName(),
            cause);
    }


}
