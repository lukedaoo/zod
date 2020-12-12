package com.infamous.framework.sensitive.core;

import java.math.BigInteger;
import java.security.MessageDigest;

public class HashStringUtils {

    public static String hash(MessageDigest digest, String source) {
        byte[] encrypted = encrypt(digest, source.getBytes());
        return toHexString(encrypted);
    }

    private static byte[] encrypt(MessageDigest md, byte[] source) {
        md.reset();
        md.update(source);
        return md.digest();
    }

    public static String toHexString(byte[] byteArr) {
        return new BigInteger(1, byteArr).toString(16);
    }

}
