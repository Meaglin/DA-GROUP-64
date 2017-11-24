package com.meaglin.assignment2;

/**
 * Represents a configuration for the host on which a node runs.
 */
public class Config {

    String host = "127.0.0.1";          // Host IP.
    String remotehost = "127.0.0.1";    // Remote host IP.
    String port = "1099";               // Port number.
    int nodeCount = 6;                  // Total number of nodes.
    boolean isServer = false;           // Boolean indicating whether this is a server configuration or not.

    /**
     * Returns the URL for a given node ID.
     * @param nodeId    Unique ID of the node.
     * @return          The complete URL of the node is returned as a string.
     */
    public String getUrl(int nodeId) {
        String host = this.host;        // Initialize host address of this configuration as variable.

        // The host is changed to the address of the remote host if the given node is not running on localhost.
        if (isServer && (nodeId >= (nodeCount/2))) {
            host = remotehost;
        } else if (!isServer && (nodeId < (nodeCount/2))) {
            host = remotehost;
        }
        return "rmi://" + host + ":" + port + "/node_" + nodeId;
    }
}