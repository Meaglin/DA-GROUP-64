package com.meaglin;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class DA_Birman_Schiper_StephenTest {

    @org.junit.jupiter.api.Test
    void receive() throws RemoteException {
        DA_Birman_Schiper_Stephen iface = new DA_Birman_Schiper_Stephen(0, 3);
        iface.receive(2, "hoi", new int[] {0,1,1});
        iface.receive(1, "hoi", new int[] {0,1,0});

        assertArrayEquals(new PendingMessage[]{
                new PendingMessage(1, "hoi", new int[] {0,1,0}),
                new PendingMessage(2, "hoi", new int[] {0,1,1})
        }, iface.delivered.toArray());

    }

}