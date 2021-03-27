package com.partition.simulator;

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
    public boolean resolves(char id) {
        if (
                id < 'a' ||
                id > 'z'
        ) {
            throw new IllegalArgumentException("Invalid volume id: " + id);
        }

        return rangeMin <= id && id <= rangeMax;
    }


}
