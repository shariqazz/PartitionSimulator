package com.partition.simulator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    static public int md5_32(String id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(id.getBytes(StandardCharsets.UTF_8));
//        byte[] hashBytesTruncated = truncate(hashBytes, 32);
//        System.out.println("Generated Hash: " + ByteBuffer.wrap(hashBytesTruncated).getInt());
        return ByteBuffer.wrap(hashBytes).getInt();
    }

    static private byte[] truncate(byte[] arr, int len) {
        if (arr.length <= len) {
            return arr;
        }

        byte[] truncatedArr = new byte[len];
        for (int i = 0; i < len; i++) {
            truncatedArr[i] = arr[i];
        }

        return truncatedArr;
    }
}
