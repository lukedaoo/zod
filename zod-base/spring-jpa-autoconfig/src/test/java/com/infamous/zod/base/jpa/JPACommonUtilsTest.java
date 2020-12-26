package com.infamous.zod.base.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

class JPACommonUtilsTest {


    @Test
    public void tesCreateEMF() {

        LocalContainerEntityManagerFactoryBean res = JPACommonUtils.createEntityManagerFactory((emf) -> {
            emf.setPersistenceUnitName("persistence.name");
        });

        assertNotNull(res);
        assertEquals("persistence.name", res.getPersistenceUnitName());
    }
}