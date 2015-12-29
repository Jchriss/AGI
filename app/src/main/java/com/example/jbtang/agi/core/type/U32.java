package com.example.jbtang.agi.core.type;

/**
 * Base data type U32 = Unsigned int
 * Created by jbtang on 10/1/2015.
 */
public class U32 {
    private long base;
    private byte[] bytes;

    //[min,max}
    private static final long min = 0x00000000L;
    private static final long max = 0xFFFFFFFFL;

    public static final int byteArrayLen = 4;

    public U32(long base) {
        validate(base);
        this.base = base;
        this.bytes = toBytes(base);
    }

    public U32(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
        this.base = toUnsigned(bytes);
    }

    public U32() {
        this.base = min;
        this.bytes = toBytes(min);
    }

    public void setBase(long base) {
        validate(base);
        this.base = base;
        this.bytes = toBytes(base);
    }

    public void setBytes(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
        this.base = toUnsigned(bytes);
    }


    public long getBase() {
        return base;
    }

    public byte[] getBytes() {
        return bytes;
    }

    private void validate(long base) {
        if (base < min || base > max) {
            throw new IllegalArgumentException(String.format("%d is not unsigned int: [%d, %d]", base, min, max));
        }
    }

    private void validate(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Invalid byte array, not 32 bit");
        }
    }

    /**
     * little endian
     */
    private static long toUnsigned(byte[] bytes) {
        return (bytes[0] & 0xFF) |
                ((bytes[1] & 0xFF) << 8) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[3] & 0xFFL) << 24);
    }

    private static byte[] toBytes(long base) {
        return new byte[]{
                (byte) (base & 0xFF),
                (byte) ((base >> 8) & 0xFF),
                (byte) ((base >> 16) & 0xFF),
                (byte) ((base >> 24) & 0xFF)};
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) base;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final U32 other = (U32) o;
        return base == other.getBase();
    }
}
