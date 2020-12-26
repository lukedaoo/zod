package com.infamous.zod.base.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Test;

class CORSFilterTest {


    @Test
    public void testFilter() {
        CORSFilter filter = new CORSFilter();

        ContainerRequestContext request = mock(ContainerRequestContext.class);
        ContainerResponseContext response = mock(ContainerResponseContext.class);

        MultivaluedMap headers = mock(MultivaluedMap.class);
        when(response.getHeaders()).thenReturn(headers);

        filter.filter(request, response);

        verify(headers, times(4)).add(any(), any());
    }
}