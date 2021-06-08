package com.example.accordo;

import com.example.accordo.data.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserUnitTest {

    private final String UID = "uid";
    private User user = new User(UID);

    @Test
    public void testGetUserIdFail() {
        assertEquals("UID", user.getUid());
    }

    @Test
    public void testGetUserIdSuccess() {
        assertEquals(UID, user.getUid());
    }
}