package com.meaglin.assignment2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Peterson's election algorithm in a unidirectional ring.
 */
public class DA_Peterson extends UnicastRemoteObject implements DA_Peterson_RMI {

    int id, nodeCount;                      // Unique ID of the node, total number of nodes.
    Config config;                          // Server configuration.
    int transmitId;                         // Unique ID of a message
    int initialReceiveId;                   // Unique ID of ?
    boolean elected, relay, canReceive;     // Booleans indicating whether one is elected, one is a relay, a message can be received,
    List<Integer> pendingReceive;           // List of messages waiting to be received.

    /**
     * Constructs a component/process of Peterson's algorithm.
     * @param nodeId            Unique ID of a node.
     * @param nodeCount         Total number of nodes.
     * @param configuration     Server configuration.
     * @throws RemoteException  Remote Exception.
     */
    public DA_Peterson(int nodeId, int nodeCount, Config configuration) throws RemoteException {
        super();
        id = nodeId;
        this.nodeCount = nodeCount;
        this.config = configuration;
        this.pendingReceive = new ArrayList<>();
    }

    /**
     *  Initializes the component/process of Peterson's algorithm.
     */
    public void start() {
        transmitId = id;
        initialReceiveId = -1;
        send(id);
        canReceive = true;
        for (int el : pendingReceive)
            receive(el);
    }

    /**
     * Receives a node ID and applies Peterson's election algorithm in a
     * unidirectional ring as described in the lecture notes.
     * @param nodeId    Unique ID of a node.
     */
    public void receive(int nodeId) {
        synchronized (this) {
            if (!canReceive) {
                pendingReceive.add(nodeId);
                return;
            }
            //  if (nntid=id) then elected <- true
            if (nodeId == id) {     //
                System.out.println("[Node #" + id + "] is elected");
                elected = true;
                return;
            }
            //  receive(ntid)
            //  send(max(tid,ntid));
            if (!relay && initialReceiveId == -1) {
                initialReceiveId = nodeId;
                nodeId = Math.max(transmitId, nodeId);
            } else if (!relay) {
                //  if ((ntid>=tid) and (ntid>=nntid))
                //      then tid <- ntid
                if (initialReceiveId >= transmitId && initialReceiveId >= nodeId) {
                    transmitId = initialReceiveId;
                    initialReceiveId = -1;
                    //  else goto relay
                } else {
                    System.out.println("[Node #" + id + "] Became a relay");
                    relay = true;           //  Change active process to relay process
                    return;
                }
                nodeId = transmitId;
            }
        }
        send(nodeId);
    }

    /**
     * Retrieves the interface of the next node and lets this receive the given ID.
     * @param nodeId    Unique ID of a node.
     */
    public void send(int nodeId) {
        System.out.println("[Node #" + id + "] Sending to " + nextNodeUrl() + ":" + nodeId);
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
