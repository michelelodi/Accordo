package com.example.accordo;

import com.example.accordo.data.Channel;
import com.example.accordo.data.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChannelUnitTest {

    private final String UID = "uid";
    private final String CTITLE = "ctitle";
    private User user = new User(UID);
    private Channel channel = new Channel(CTITLE, user);

    @Test
    public void testGetCTitleFail() { assertEquals("UID", channel.getCTitle()); }

    @Test
    public void testGetCTitleSuccess(){ assertEquals(CTITLE, channel.getCTitle()); }

}
