package com.meaglin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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
//        System.out.println("[" + id + "->" + this.id + "](" + Arrays.toString(clock) + ";" + Arrays.toString(senderClock) + "):" + message);
        PendingMessage msg = new PendingMessage(id, message, senderClock);
        if (msg.canAccept(clock)) {
            deliver(msg);
            return;
        }
        buffer.add(msg);
    }

    private void checkBuffer() {
        Iterator<PendingMessage> it = buffer.iterator();
        PendingMessage delivarable = null;
        while(it.hasNext()) {
            PendingMessage msg = it.next();
            if (msg.canAccept(clock)) {
                it.remove();
                delivarable = msg;
                break;
            }
        }
        if (delivarable != null) {
            deliver(delivarable);
        }
    }

    public void broadcast(String message) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("[Node #" + id + "]broadcast:" + message);
        int[] send_clock;
        synchronized (clock) {
            clock[id] += 1;
            send_clock = clock.clone();
        }
        for (int i = 0; i < clock.length; i += 1) {
            if (i == id) {
                continue;
            }
            DA_Birman_Schiper_Stephen_RMI node = (DA_Birman_Schiper_Stephen_RMI) Naming.lookup("rmi://127.0.0.1:1099/p" + i);
            node.receive(id, message, send_clock);
        }
    }


    public void deliver(PendingMessage message) {
        synchronized (clock) {
            clock[message.node] += 1;
        }
        System.out.println("[Node " + message.node + "->" + id + "]" + message.message);
        checkBuffer();
    }
}
