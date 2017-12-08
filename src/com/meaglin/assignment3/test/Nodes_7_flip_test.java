package com.meaglin.assignment3.test;

import com.meaglin.assignment3.*;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Nodes_7_flip_test {
    @org.junit.jupiter.api.Test
    void run() throws RemoteException {
        int nodeCount = 7;
        CommunicationBus bus = new LocalCommunicationBus(nodeCount);
        Node[] nodes = new Node[nodeCount];
        DA_Randomized_Bryzantine_Agreement[] interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        Thread[] threads = new Thread[nodeCount];
        for(int i = 0; i < nodes.length; i ++) {
            nodes[i] = new Node(i, "");
            if (i == 6) {
                interfaces[i] = new DA_Randomized_Bryzantine_Agreement_Faulty_Flip(i, nodes, bus);
            } else {
                interfaces[i] = new DA_Randomized_Bryzantine_Agreement(i, nodes, bus);
            }
            threads[i] = new Thread(interfaces[i]);
        }
        for(int i = 0; i < nodes.length; i ++) {
            threads[i].start();
        }
        while(true) {
            int cnt = 0;
            for(int i = 0; i < (nodes.length - 1); i++) {
                if (interfaces[i].decided) {
                    cnt++;
                }
            }
            if (cnt == (nodes.length - 1)) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < (nodes.length - 1); i++) {
            assertEquals(interfaces[0].value, interfaces[i].value);
        }
        System.out.println("Took " + interfaces[0].round.id + " rounds");
    }
}
