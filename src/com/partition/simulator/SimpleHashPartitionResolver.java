package com.partition.simulator;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.partition.simulator.Utils.md5_32;

public class SimpleHashPartitionResolver implements PartitionResolver {
    private int nodeId;
    private Node predecessor;
    List<Integer> ids;
    private int numNodes = 20;

    public SimpleHashPartitionResolver(int nodeId) {
        this.nodeId = nodeId;
//        ids = new ArrayList<>();
//        try {
//            for (int i = 0; i < 20; i++) {
//                ids.add(md5_32(String.valueOf(UUID.randomUUID())));
//            }
//        } catch (Exception e) {
//            // shouldnt get here
//            System.out.println("Error during hash partition resolver constructor");
//        }
    }

    @Override
    public void setPredecessor(Node pred) {
        this.predecessor = pred;
    }

    @Override
    public boolean resolves(String id) {
        if (predecessor == null) {
            System.out.println("Predecessor null for: " + nodeId);
            return false;
        }

//        try {
//            int hash = md5_32(id);
//            boolean found = false;
//            for (int nodeIdentifier : ids) {
//                if (predecessor.getId() > nodeIdentifier) {
//                    // looping over
//                    found |= (hash >= predecessor.getId() && hash >= nodeIdentifier) ||
//                            (hash <= predecessor.getId() && hash <= nodeIdentifier);
//                } else {
//                    found |= hash <= nodeIdentifier && hash > predecessor.getId();
//                }
//            }
//            return found;
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("Unexpected MD5 algo exception: " + e.toString());
//            return false;
//        }

        try {
            int hash = md5_32(id);
            if (predecessor.getId() > nodeId) {
                // looping over
                return (hash >= predecessor.getId() && hash >= nodeId) ||
                        (hash <= predecessor.getId() && hash <= nodeId);
            } else {
                return hash <= nodeId && hash > predecessor.getId();
            }
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
