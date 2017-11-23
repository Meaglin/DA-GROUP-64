package com.meaglin.assignment1;


public class Main {


    public static int nodeCount = 3;
    public static NodeRunner[] nodes;

    public static void main(String[] args) {

        nodes = new NodeRunner[nodeCount];
        for(int i = 0; i < nodeCount; i += 1) {
            nodes[i] = new NodeRunner(i, nodeCount);
            new Thread(nodes[i]).start();
        }
    }
}
