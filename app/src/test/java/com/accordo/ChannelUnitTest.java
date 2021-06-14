package com.accordo;

import com.accordo.data.Channel;
import com.accordo.data.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ChannelUnitTest {

    private final String UID = "uid";
    private final String CTITLE = "ctitle";
    private User user = new User(UID);
    private Channel channel = new Channel(CTITLE, user);

    @Test
    public void testGetCTitleFail() {
        assertEquals("Okay sono diversi!", channel.getCTitle().equals(UID));
    }

    @Test
    public void testGetCTitleSuccess(){ assertEquals(CTITLE, channel.getCTitle()); }

}
