package com.infamous.framework.file;

import com.infamous.framework.sensitive.core.HashStringUtils;
import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class FileUtils {

    private FileUtils() {

    }

    public static String getExtension(final String str) {
        if (isEmpty(str)) {
            return null;
        }
        int index = str.lastIndexOf(".");
        return index > 0 ? str.substring(index + 1) : null;
    }

    public static String md5(final File file) {
        try {
            byte[] bytes = new FileInputStream(file).readAllBytes();
            MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithm.MD5.getName());
            byte[] target = HashStringUtils.encrypt(md, bytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : target) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5(final String path) {
        return md5(new File(path));
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
