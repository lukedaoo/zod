package com.infamous.framework.sensitive.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.infamous.framework.sensitive.core.SensitiveHashingService;
import org.junit.jupiter.api.Test;

class SensitiveHashingServiceProviderTest {


    @Test
    public void testUse() {
        SensitiveHashingService service = mock(SensitiveHashingService.class);
        SensitiveHashingServiceProvider.getInstance().use(service);

        assertEquals(service, SensitiveHashingServiceProvider.getInstance().getService());
    }

    @Test
    public void testUse_WithNullObject() {

        assertThrows(NullPointerException.class, () -> SensitiveHashingServiceProvider.getInstance().use(null));
    }


}