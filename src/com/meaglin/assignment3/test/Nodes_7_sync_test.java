package com.meaglin.assignment3.test;

import com.meaglin.assignment3.CommunicationBus;
import com.meaglin.assignment3.DA_Randomized_Bryzantine_Agreement;
import com.meaglin.assignment3.LocalCommunicationBus;
import com.meaglin.assignment3.Node;
import org.junit.Test;

import java.rmi.RemoteException;

import static junit.framework.TestCase.assertEquals;


public class Nodes_7_sync_test extends BaseTest {
    @Test
    public void run() throws RemoteException {
        int nodeCount = 7;
        CommunicationBus bus = new LocalCommunicationBus(nodeCount);
        Node[] nodes = new Node[nodeCount];
        DA_Randomized_Bryzantine_Agreement[] interfaces = new DA_Randomized_Bryzantine_Agreement[nodeCount];
        for(int i = 0; i < nodes.length; i ++) {
            nodes[i] = new Node(i, "");
            interfaces[i] = new DA_Randomized_Bryzantine_Agreement(i, nodes, bus);
        }
        for(int i = 0; i < nodes.length; i ++) {
            interfaces[i].init();
        }
        reportStart(interfaces);
        for(int i = 0; i < nodes.length; i ++) {
            interfaces[i].broadcast(interfaces[i].value);
        }
        for(int i = 0; i < nodes.length; i++) {
            assertEquals(interfaces[0].value, interfaces[i].value);
        }
        reportEnd(interfaces);
//        System.out.println("Took " + interfaces[0].round.id + " rounds");
    }
}
