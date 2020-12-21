package com.infamous.zod.base.rest;

import com.infamous.framework.logging.ZodLogger;
import java.util.function.Supplier;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class BaseEndPoint {

    protected void postInitLog(Supplier<String> messageSupplier) {
        getLogger().info(messageSupplier.get());
    }

    protected Response executeWithTemplate(Supplier<Response> supplier) {
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
        getLogger().error("Internal Exception: ", e);
    }

    protected abstract ZodLogger getLogger();
}
