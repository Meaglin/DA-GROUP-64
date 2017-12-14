package com.meaglin.assignment1;

import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertArrayEquals;

class DA_Birman_Schiper_StephenTest {

    @Test
    void receive() throws RemoteException {
        System.out.println("Test");
        DA_Birman_Schiper_Stephen iface = new DA_Birman_Schiper_Stephen(0, 3);
        iface.receive(2, "hoi", new int[] {0,1,1});
        iface.receive(1, "hoi", new int[] {0,1,0});

        assertArrayEquals(new PendingMessage[]{
                new PendingMessage(1, "hoi", new int[] {0,1,0}),
                new PendingMessage(2, "hoi", new int[] {0,1,1})
        }, iface.delivered.toArray());

    }

    @Test
    void receive2() throws RemoteException {

        System.out.println("Test2");
        DA_Birman_Schiper_Stephen iface = new DA_Birman_Schiper_Stephen(0, 3);
        iface.receive(2, "hoi", new int[] {0,1,1});
        iface.receive(2, "hoi2", new int[] {0,1,2});
        iface.receive(1, "hello2", new int[] {0,2,0});
        iface.receive(1, "hello", new int[] {0,1,0});

        assertArrayEquals(new PendingMessage[]{
                new PendingMessage(1, "hello", new int[] {0,1,0}),
                new PendingMessage(2, "hoi", new int[] {0,1,1}),
                new PendingMessage(2, "hoi2", new int[] {0,1,2}),
                new PendingMessage(1, "hello2", new int[] {0,2,0})
        }, iface.delivered.toArray());

    }

}