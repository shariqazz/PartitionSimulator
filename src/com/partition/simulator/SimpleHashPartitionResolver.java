package com.partition.simulator;

import java.security.NoSuchAlgorithmException;

import static com.partition.simulator.Utils.md5_32;

public class SimpleHashPartitionResolver implements PartitionResolver {
    private int nodeId;
    private Node predecessor;

    public SimpleHashPartitionResolver(int nodeId, Node predecessor) {
        this.nodeId = nodeId;
        this.predecessor = predecessor;
    }

    @Override
    public boolean resolves(char id) {
        try {
            int hash = md5_32(String.valueOf(id));
            return hash <= nodeId && (predecessor == null || hash > predecessor.getId());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Unexpected MD5 algo exception: " + e.toString());
            return false;
        }
    }

//    // Source: https://stats.stackexchange.com/questions/281162/scale-a-number-between-a-range/281164
//    private int scaleId(int m) {
//        int rmin = Integer.MIN_VALUE;
//        int rmax = Integer.MAX_VALUE;
//        int tmin = 0;
//        int tmax = Integer.MAX_VALUE;
//
//        return ( ( (m - rmin) / (rmax - rmin) ) * ( tmax - tmin ) ) + tmin;
//    }
}
