package com.meaglin;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DA_Birman_Schiper_Stephen extends UnicastRemoteObject implements DA_Birman_Schiper_Stephen_RMI {


    int id;
    List<PendingMessage> buffer;
    int[] clock;

    public class PendingMessage {
        private int node;
        private String message;
        private int[] clock;

        PendingMessage(int node, String message, int[] clock) {
            this.node = node;
            this.message = message;
            this.clock = clock;
        }

        public boolean canAccept(int[] localClock) {
            int[] V = localClock.clone();
            V[node] += 1;
            for(int i = 0; i < V.length; i += 1) {
                if (V[i] < clock[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public DA_Birman_Schiper_Stephen(int id, int nodes) throws RemoteException {
        super();
        this.id = id;
        clock = new int[nodes];
        buffer = new ArrayList<>(0);
    }

    public void receive(int id, String message, int[] senderClock) {
        PendingMessage msg = new PendingMessage(id, message, senderClock);
        if (msg.canAccept(clock)) {
            deliver(msg);
            checkBuffer();
            return;
        }
        buffer.add(msg);
    }

    private void checkBuffer() {
        Iterator<PendingMessage> it = buffer.iterator();
        while(it.hasNext()) {
            PendingMessage msg = it.next();
            if (msg.canAccept(clock)) {
                it.remove();
                deliver(msg);
            }
        }
    }

    public void send(String target, String message) throws RemoteException, NotBoundException, MalformedURLException {
        DA_Birman_Schiper_Stephen_RMI node = (DA_Birman_Schiper_Stephen_RMI) Naming.lookup(target);
        clock[id] += 1;
        node.receive(id, message, clock);
    }

    public void broadcast(String message) throws RemoteException, NotBoundException, MalformedURLException {
        clock[id] += 1;
        for (int i = 0; i < clock.length; i += 1) {
            if (i == id) {
                continue;
            }
            DA_Birman_Schiper_Stephen_RMI node = (DA_Birman_Schiper_Stephen_RMI) Naming.lookup("p" + i);
            node.receive(id, message, clock);
        }
    }


    public void deliver(PendingMessage message) {
        clock[message.node] += 1;
        System.out.println("[Node #" + id + "]received: " + message.message);
    }
}
