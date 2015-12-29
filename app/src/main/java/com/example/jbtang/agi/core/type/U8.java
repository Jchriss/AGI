package com.example.jbtang.agi.core.type;

/**
 * Base data type U8 = Unsigned byte
 * Created by jbtang on 9/30/2015.
 */
public class U8 {
    private int base;
    private byte[] bytes;

    //[min,max]
    private static final int min = 0x00;
    private static final int max = 0xFF;

    public static final int byteArrayLen = 1;

    public U8(int base) {
        validate(base);
        this.base = base;
        this.bytes = toBytes(base);
    }

    public U8(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
        this.base = toUnsigned(bytes);
    }

    public U8() {
        this.base = min;
        this.bytes = toBytes(min);
    }

    public int getBase() {
        return base;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBase(int base) {
        validate(base);
        this.base = base;
        this.bytes = toBytes(base);
    }

    public void setBytes(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
        this.base = toUnsigned(bytes);
    }

    private void validate(int base) {
        if (base < min || base > max) {
            throw new IllegalArgumentException(String.format("%d is not unsigned byte: [%d, %d]", base, min, max));
        }
    }

    private void validate(byte[] bytes) {
        if (bytes.length != 1) {
            throw new IllegalArgumentException("Invalid byte array, not 8 bit");
        }
    }

    /**
     * little endian
     */
    private static int toUnsigned(byte[] bytes) {
        return bytes[0] & 0xFF;
    }

    private static byte[] toBytes(int base) {
        return new byte[]{(byte) base};
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + base;
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
        final U8 other = (U8) o;
        return base == other.getBase();
    }

    //TODO: equal, compareTo, hashcode, etc.
}
