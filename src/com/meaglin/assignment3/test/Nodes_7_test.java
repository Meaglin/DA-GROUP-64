package com.meaglin.assignment3.test;

import com.meaglin.assignment3.CommunicationBus;
import com.meaglin.assignment3.DA_Randomized_Bryzantine_Agreement;
import com.meaglin.assignment3.LocalCommunicationBus;
import com.meaglin.assignment3.Node;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

public class Nodes_7_test extends BaseTest {

    @Test
    public void run() throws RemoteException {
        int nodeCount = 7;
        CommunicationBus bus = new LocalCommunicationBus(nodeCount);
        Node[] nodes = new Node[nodeCount];
        DA_Randomized_Bryzantine_Agreement[] interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        Thread[] threads = new Thread[nodeCount];
        for(int i = 0; i < nodes.length; i ++) {
            nodes[i] = new Node(i, "");
            interfaces[i] = new DA_Randomized_Bryzantine_Agreement(i, nodes, bus);
            threads[i] = new Thread(interfaces[i]);
        }
        reportStart(interfaces);
        for(int i = 0; i < nodes.length; i ++) {
            threads[i].start();
        }
        while(true) {
            int cnt = 0;
            for(int i = 0; i < nodes.length; i++) {
                if (interfaces[i].decided) {
                    cnt++;
                }
            }
            if (cnt == nodes.length) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < nodes.length; i++) {
            assertEquals(interfaces[0].value, interfaces[i].value);
        }
        reportEnd(interfaces);
//        System.out.println("Took " + interfaces[0].round.id + " rounds");
    }


}
