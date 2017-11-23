package com.meaglin.assignment2;

public class Config {


    String host = "127.0.0.1";
    String remotehost = "127.0.0.1";


    String port = "1099";

    int nodeCount = 6;
    boolean isServer = false;

    public String getUrl(int nodeId) {
        String host = this.host;
        if (isServer && (nodeId >= (nodeCount/2))) {
            host = remotehost;
        } else if (!isServer && (nodeId < (nodeCount/2))) {
            host = remotehost;
        }
        return "rmi://" + host + ":" + port + "/node_" + nodeId;
    }
}
