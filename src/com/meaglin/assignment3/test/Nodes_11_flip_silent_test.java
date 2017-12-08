package com.meaglin.assignment3.test;

import com.meaglin.assignment3.*;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Nodes_11_flip_silent_test {
    @org.junit.jupiter.api.Test
    void run() throws RemoteException {
        int nodeCount = 11;
        CommunicationBus bus = new LocalCommunicationBus(nodeCount);
        Node[] nodes = new Node[nodeCount];
        DA_Randomized_Bryzantine_Agreement[] interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        Thread[] threads = new Thread[nodeCount];
        for(int i = 0; i < nodes.length; i ++) {
            nodes[i] = new Node(i, "");
            if (i == 9) {
                interfaces[i] = new DA_Randomized_Bryzantine_Agreement_Faulty_Silent(i, nodes, bus);
            } else if(i == 10) {
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
            for(int i = 0; i < (nodes.length - 2); i++) {
                if (interfaces[i].decided) {
                    cnt++;
                }
            }
            if (cnt == (nodes.length - 2)) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < (nodes.length - 2); i++) {
            assertEquals(interfaces[0].value, interfaces[i].value);
        }
        System.out.println("Took " + interfaces[0].round.id + " rounds");
    }
}
