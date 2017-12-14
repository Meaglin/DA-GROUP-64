package com.meaglin.assignment3.test;

import com.meaglin.assignment3.*;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

public class Nodes_31_silent_test {
    @Test
    public void run() throws RemoteException {
        int nodeCount = 31;
        int faulty = 6;
        CommunicationBus bus = new LocalCommunicationBus(nodeCount);
        Node[] nodes = new Node[nodeCount];
        DA_Randomized_Bryzantine_Agreement[] interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        Thread[] threads = new Thread[nodeCount];
        for(int i = 0; i < nodes.length; i ++) {
            nodes[i] = new Node(i, "");
            if (i >= (nodeCount-faulty)) {
                interfaces[i] = new DA_Randomized_Bryzantine_Agreement_Faulty_Silent(i, nodes, bus);
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
            for(int i = 0; i < (nodeCount-faulty); i++) {
                if (interfaces[i].decided) {
                    cnt++;
                }
            }
            if (cnt == (nodeCount-faulty)) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < (nodeCount-faulty); i++) {
            assertEquals(interfaces[0].value, interfaces[i].value);
        }
        System.out.println("Took " + interfaces[0].round.id + " rounds");
    }
}
