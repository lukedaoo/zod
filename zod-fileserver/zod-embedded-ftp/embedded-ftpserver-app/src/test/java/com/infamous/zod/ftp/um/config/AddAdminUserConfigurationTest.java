package com.infamous.zod.ftp.um.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserManager;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.ApplicationContext;

class AddAdminUserConfigurationTest {

    private AddAdminUserConfiguration m_addAdminUserConfiguration;

    private FTPServerConfigProperties m_serverConfigProperties;
    private FTPUserManager m_ftpUserManager;
    private FTPDataStore m_ds;
    private ApplicationContext m_applicationContext;

    @BeforeEach
    public void setup() {

        m_serverConfigProperties = mock(FTPServerConfigProperties.class);
        m_ftpUserManager = mock(FTPUserManager.class);
        m_ds = mock(FTPDataStore.class);
        m_applicationContext = mock(ApplicationContext.class);

        mockFtpServerConfig();
        mockAppContext();
        when(m_ftpUserManager.hashPassword("password")).thenReturn("passWordWasHashed");
        m_addAdminUserConfiguration = new AddAdminUserConfiguration(m_serverConfigProperties, m_ftpUserManager, m_ds,
            m_applicationContext);
    }

    public void mockFtpServerConfig() {
        when(m_serverConfigProperties.getUsername()).thenReturn("admin");
        when(m_serverConfigProperties.getPassword()).thenReturn("password");
        when(m_serverConfigProperties.getRootFolder()).thenReturn("src/test/resources/ftp");
        when(m_serverConfigProperties.getWorkspace()).thenReturn("wanderer");
    }

    public void mockAppContext() {
        when(m_applicationContext.getBeansOfType(ExitCodeGenerator.class)).thenReturn(Collections.emptyMap());
    }


    @Test
    public void testDoPostConstruct_WhenDBIsNotConnected() {
        mockOpenOrCloseDB(false);

        assertFalse(m_ds.isOpen());

        assertDoesNotThrow(() -> m_addAdminUserConfiguration.doPostConstruct());
        verify(m_applicationContext).getBeansOfType(eq(ExitCodeGenerator.class));
        verify(m_applicationContext).publishEvent(any(ExitCodeEvent.class));
    }

    public void mockOpenOrCloseDB(boolean isOpen) {
        EntityManager mockEm = mock(EntityManager.class);
        when(m_ds.isOpen()).thenReturn(isOpen);
    }

    @Test
    public void testDoPostConstruct_ExceptionOccurWhenSavingUser() {
        mockOpenOrCloseDB(true);

        assertTrue(m_ds.isOpen());

        when(m_serverConfigProperties.getPassword()).thenReturn("12");

        assertDoesNotThrow(() -> m_addAdminUserConfiguration.doPostConstruct());
        verify(m_applicationContext).getBeansOfType(eq(ExitCodeGenerator.class));
        verify(m_applicationContext).publishEvent(any(ExitCodeEvent.class));
    }

    @Test
    public void testDoPostConstruct_Successfully() throws Exception {
        mockOpenOrCloseDB(true);

        assertTrue(m_ds.isOpen());

        assertDoesNotThrow(() -> m_addAdminUserConfiguration.doPostConstruct());
        verify(m_ftpUserManager).hashPassword(eq("password"));
        verify(m_ftpUserManager).save(argThat(user -> user instanceof FTPUser
            && user.getName().equals("admin")
            && !user.getPassword().equals("password")
            && ((FTPUser) user).isAdmin()
            && user.getHomeDirectory().equals("src/test/resources/ftp/wanderer")));
        verify(m_applicationContext, never()).getBeansOfType(eq(ExitCodeGenerator.class));
        verify(m_applicationContext, never()).publishEvent(any(ExitCodeEvent.class));
    }

}