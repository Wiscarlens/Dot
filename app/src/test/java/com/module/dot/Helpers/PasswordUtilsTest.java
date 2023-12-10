package com.module.dot.Helpers;

import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;

public class PasswordUtilsTest extends TestCase {

    public void testHashPassword() {
        // Given
        String password = "password123";

        // When
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Then
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    public void testVerifyPassword() {
        // Given
        String password = "password123";
        String hashedPassword = PasswordUtils.hashPassword(password);

        // When/Then
        assertTrue(PasswordUtils.verifyPassword(password, hashedPassword));
        assertFalse(PasswordUtils.verifyPassword("wrongpassword", hashedPassword));
    }
}