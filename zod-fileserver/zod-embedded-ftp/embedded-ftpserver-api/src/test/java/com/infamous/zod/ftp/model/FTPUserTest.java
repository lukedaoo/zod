package com.infamous.zod.ftp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.jupiter.api.Test;

class FTPUserTest {


    @Test
    void testCreateFTPUser_WithInvalidUsername_UserNameIsNull() {
        checkErrorMessage(null, "password", "Username is mandatory");
        checkErrorMessage("", "password", "Username is mandatory");
    }

    @Test
    void testCreateFTPUser_WithInvalidUsername_UserNameDoesNotEnoughLength() {
        checkErrorMessage("sa", "password", "Username must be at least 4 character");
    }

    @Test
    void testCreateFTPUser_WithInvalidUsername_UserNameHasInvalidCharacter() {
        checkErrorMessage("sa123@", "password", "Username can not contain '@'");
        checkErrorMessage("sa123/", "password", "Username can not contain '/'");
        checkErrorMessage("sa123\\", "password", "Username can not contain '\\");
    }


    @Test
    void testCreateFTPUser_WithInvalidPassword_PasswordIsNull() {
        checkErrorMessage("admin", null, "Password is mandatory");
        checkErrorMessage("admin", "", "Password is mandatory");
    }

    @Test
    void testCreateFTPUser_WithInvalidPassword_PasswordDoesNotEnoughLength() {
        checkErrorMessage("admin", "213", "Password must be at least 4 character");
    }

    @Test
    void testCreateFTPUser_WithInvalidPassword_PasswordHasInvalidCharacter() {
        checkErrorMessage("admin", "/password", "Password can not contain '/'");
    }

    @Test
    public void testBuilder() {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("password")
            .workspace("/root/")
            .maxUploadRate(10)
            .maxDownloadRate(10)
            .maxConcurrentLogins(20)
            .idleTime(1000)
            .isAdmin(true)
            .enabled(false)
            .build();
        user.addDefaultAuthorities();
        assertNotNull(user);
        assertEquals(3, user.getAuthorities().size());
        assertTrue(user.getAuthorities().get(0) instanceof WritePermission);
        assertTrue(user.getAuthorities().get(1) instanceof ConcurrentLoginPermission);
        assertTrue(user.getAuthorities().get(2) instanceof TransferRatePermission);
    }

    private void checkErrorMessage(final String userName, final String password, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new FTPUser(new FTPUserName(userName), new FTPPassword(password)));

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }

}