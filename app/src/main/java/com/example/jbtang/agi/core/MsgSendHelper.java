package com.example.jbtang.agi.core;

/**
 * utils API for the convenience
 * Created by jbtang on 9/29/2015.
 */
public class MsgSendHelper {
    public static String convertBytesToString(final byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.valueOf(b)).append(", ");
        }
        return builder.toString();
    }

    public static boolean byteArrayEquals(final byte[] expected, final byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }
        return true;
    }

    //[from, to)
    public static byte[] getSubByteArray(final byte[] src, int start, int len) {
        byte[] dst = new byte[len];
        System.arraycopy(src, start, dst, 0, len);
        return dst;
    }

    public static class AppendByteArray {
        private byte[] base;

        public AppendByteArray() {
            base = new byte[0];
        }

        public AppendByteArray append(byte[] append) {
            byte[] dst = new byte[base.length + append.length];
            System.arraycopy(base, 0, dst, 0, base.length);
            System.arraycopy(append, 0, dst, base.length, append.length);
            base = dst;
            return this;
        }

        public AppendByteArray append(byte b) {
            byte[] dst = new byte[base.length + 1];
            System.arraycopy(base, 0, dst, 0, base.length);
            dst[base.length] = b;
            base = dst;
            return this;
        }

        public byte[] toByteArray() {
            return base;
        }
    }

    public static byte[] shortToByteArray(short num) {
        return new byte[]{
                Integer.valueOf(num & 0xFF).byteValue(),
                Integer.valueOf((num >> 8) & 0xFF).byteValue()
        };
    }

    public static short byteArrayToShort(byte[] bytes) {
        if (bytes.length > 2) {
            throw new IllegalArgumentException("Invalid input: not short type byte array!");
        }
        return (short) ((bytes[0] & 0xFF) |
                ((bytes[1] & 0xFF) << 8));
    }
}
