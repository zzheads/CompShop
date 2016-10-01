package com.zzheads.CompShop.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PickupRequestTest {

    @Test
    public void getDimensionsInCmTest() throws Exception {
        PickupRequest pickupRequest = new PickupRequest("1000", "1000x1000x1000", "MSK_1", "FALSE", "999.99", "hundredths-inches", "hundredths-pounds");
        assertEquals("25x25x25", pickupRequest.getDimensionsInCm());
        assertEquals("4.53592", pickupRequest.getWeightInKg());
    }
}