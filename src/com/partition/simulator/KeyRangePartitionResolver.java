package com.partition.simulator;

import java.io.NotActiveException;

public class KeyRangePartitionResolver implements PartitionResolver {
    private char rangeMin;
    private char rangeMax;

    public KeyRangePartitionResolver(char rangeMin, char rangeMax) {
        if (
                rangeMin < 'a'||
                rangeMin > 'z' ||
                rangeMax < 'a' ||
                rangeMax > 'z' ||
                rangeMax < rangeMin
        ) {
            throw new IllegalArgumentException("Invalid params.");
        }

        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    @Override
    public boolean resolves(String id) {
        char c = id.charAt(0);
        if (
                c < 'a' ||
                c > 'z'
        ) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }

        return rangeMin <= c && c <= rangeMax;
    }

    @Override
    public void setPredecessor(Node node) {
        return;
    }


}
