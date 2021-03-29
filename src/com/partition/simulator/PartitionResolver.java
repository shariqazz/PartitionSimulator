package com.partition.simulator;

public interface PartitionResolver {
    boolean resolves(String id);
    void setPredecessor(Node node);
}
