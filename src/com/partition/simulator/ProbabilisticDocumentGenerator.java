package com.partition.simulator;

import java.util.HashMap;
import java.util.Map;

public class ProbabilisticDocumentGenerator {
    private Map<Integer, Double> probMap;
    private double probSum;

    public ProbabilisticDocumentGenerator() {
        probMap = new HashMap<Integer, Double>();
        probSum = 0.0;
    }

    public void add(char c, double prob) {
        int key = c - 'a';
        if (probMap.containsKey(key)) {
            probSum -= probMap.get(key);
        }
        probMap.put(key, prob);
        probSum += prob;
    }

    public char getNext() {
        if ( (1.0 - probSum) > 0.01 ) {
            throw new IllegalArgumentException("Invalid setup. Prob sum needs to be 1.0. Error wider than 0.01. Was: " + probSum);
        }

        double random = Math.random();
        double tempProb = 0.0;
        for (Integer i : probMap.keySet()) {
            tempProb += probMap.get(i);
            if (random <= tempProb)
                return (char) (i + 'a');
        }
        // error
        return Character.MIN_VALUE;
    }
}
