package com.meaglin.assignment2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DA_Peterson extends UnicastRemoteObject implements DA_Peterson_RMI {


    int id, nodeCount;
    Config config;

    int transmitId;
    int initialReceiveId;

    boolean elected, relay;

    public DA_Peterson(int nodeId, int nodeCount, Config configuration) throws RemoteException {
        super();
        id = nodeId;
        this.nodeCount = nodeCount;
        this.config = configuration;
    }

    public void start() {
        transmitId = id;
        initialReceiveId = -1;
        send(id);
    }

    /*
       do forever
        send(tid);
        receive(ntid)
        if(ntid=id)
            then elected ← true
        receive(nntid)
        if (nntid=id)
            then elected← true
        if ((ntid>=tid) and (ntid>=nntid))
            then tid ← ntid
        else goto relay
     */
    public void receive(int nodeId) throws java.rmi.RemoteException {
        synchronized (this) {
            if (nodeId == id) {
                System.out.println("[Node #" + id + "] is elected");
                elected = true;
                return;
            }
            /*
                receive(ntid)
                send(max(tid,ntid));
             */
            if (!relay && initialReceiveId == -1) {
                initialReceiveId = nodeId;
                nodeId = Math.max(transmitId, nodeId);
            } else if (!relay) {
                /*
                if ((ntid>=tid) and (ntid>=nntid))
                    then tid ← ntid
                 */
                if (initialReceiveId >= transmitId && initialReceiveId >= nodeId) {
                    transmitId = initialReceiveId;
                    initialReceiveId = -1;
                /*
                    else goto relay
                */
                } else {
                    System.out.println("[Node #" + id + "] Became a relay");
                    relay = true;
                    return;
                }
                nodeId = transmitId;
            }
        }
        send(nodeId);
    }

    public void send(int nodeId) {
        System.out.println("[Node #" + id + "] Sending:" + nodeId);
        try {
            DA_Peterson_RMI iface = (DA_Peterson_RMI) Naming.lookup(nextNodeUrl());
            iface.receive(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getNodeUrl() {
        return config.getUrl(id);
    }

    String nextNodeUrl() {
        return config.getUrl(nextNodeId());
    }

    int nextNodeId() {
        return (id == (nodeCount - 1) ? 0 : id + 1);
    }
}
