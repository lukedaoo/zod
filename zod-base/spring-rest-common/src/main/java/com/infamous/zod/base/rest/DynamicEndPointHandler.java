package com.infamous.zod.base.rest;

import com.infamous.framework.logging.ZodLogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@SuppressWarnings("SuspiciousInvocationHandlerImplementation")
public class DynamicEndPointHandler<T> implements InvocationHandler {

    private final ZodLogger m_logger;
    private final T m_target;

    public DynamicEndPointHandler(ProxyEndpointContext<T> context) {
        this.m_logger = context.getLogger();
        this.m_target = context.getTarget();
        Consumer<T> postInit = context.getPostInit();
        if (postInit != null) {
            postInit.accept(m_target);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getAnnotation(RestEndPoint.class) != null) {
            return executeWithTemplate(() -> {
                try {
                    return (Response) method.invoke(m_target, args);
                } catch (Exception e) {
                    throw new RuntimeException(
                        "Exception occurred while executing method " + m_target.getClass().getSimpleName() +
                            "#" + method.getName(), e);
                }
            });
        }
        return method.invoke(m_target, args);

    }

    private Response executeWithTemplate(Supplier<Response> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return unknownResponse(e);
        }
    }

    private Response unknownResponse(Exception e) {
        logUnexpectedException(e);
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }

    private void logUnexpectedException(Exception e) {
        m_logger.error("Internal Exception: ", e);
    }
}
