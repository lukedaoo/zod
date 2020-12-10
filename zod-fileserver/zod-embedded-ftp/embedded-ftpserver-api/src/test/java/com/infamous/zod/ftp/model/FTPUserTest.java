package com.infamous.zod.ftp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        checkErrorMessage("admin", "password:", "Password can not contain ':'");
        checkErrorMessage("admin", "/password", "Password can not contain '/'");
    }


    private void checkErrorMessage(final String userName, final String password, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new FTPUser(new FTPUserName(userName), new FTPPassword(password)));

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }

}