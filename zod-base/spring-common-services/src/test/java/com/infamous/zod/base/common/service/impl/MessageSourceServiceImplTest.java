package com.infamous.zod.base.common.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageSourceServiceImplTest {

    MessageSourceServiceImpl m_messageSourceService;

    @BeforeEach
    public void setup() {
        m_messageSourceService = new MessageSourceServiceImpl("classpath:locale/message");
    }

    @Test
    public void testGetMessage() {
        String mess = m_messageSourceService.getMessage("storage.invalid.input");
        assertEquals("Invalid Input", mess);
    }
}