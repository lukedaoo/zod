package com.infamous.zod.base.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.infamous.framework.logging.ZodLoggerUtil;
import com.infamous.zod.base.rest.ProxyEndpointContext.ProxyEndpointContextBuilder;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DynamicEndPointHandlerTest {

    IPersonEndPoint m_endPoint;

    @BeforeEach
    public void setup() {
        ProxyEndpointContext<IPersonEndPoint> context = new ProxyEndpointContextBuilder<IPersonEndPoint>()
            .anInterface(IPersonEndPoint.class)
            .target(new PersonEndPoint())
            .logger(ZodLoggerUtil.getLogger(DynamicEndPointHandlerTest.class, "endpoint.testing"))
            .postInit((endpoint) -> System.out.println("Post Init is called"))
            .build();

        m_endPoint = ProxyEndpointFactory.getInstance().create(context);
    }


    @Test
    void testInvoke() {

        Response actual = m_endPoint.fetchAll();

        assertEquals(2, ((List) actual.getEntity()).size());
    }

    @Test
    void testInvokeButHasException() {
        assertDoesNotThrow(() -> m_endPoint.exception());
    }

    @Test
    void testInvokeNoneRestEndPoint() {
        assertEquals("Hello", m_endPoint.ok());
    }
}

