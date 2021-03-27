package com.partition.simulator;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int identifier;
    private List<Character> ownedData;
    private PartitionResolver resolver;

    public Node successor;
    public Node predecessor;

    public Node(int id, PartitionResolver resolver) {
        this.identifier = id;
        ownedData = new ArrayList<>();
        this.resolver = resolver;
    }

    public boolean addData(char id) {
        if (!ownsId(id)) {
            throw new IllegalArgumentException("Can't add id that this node doesn't resolve");
        }
        return ownedData.add(id);
    }

    public void clear() {
        ownedData.clear();
    }

    public int getSize() {
        return ownedData.size();
    }

    public void setSuccessor(Node succ) {
        this.successor = succ;
    }

    public void setPredecessor(Node pred) {
        this.predecessor = pred;
    }

    public int getId() {
        return this.identifier;
    }

    public boolean ownsId(char id) {
        return this.resolver.resolves(id);
    }

    public String toString() {
        return String.format("%d -> %d", getId(), getSize());
    }
}
