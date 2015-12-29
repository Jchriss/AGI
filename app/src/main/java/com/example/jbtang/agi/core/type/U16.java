package com.example.jbtang.agi.core.type;

/**
 * Base data type U16 = Unsigned short
 * Created by jbtang on 10/1/2015.
 */
public class U16 {
    private int base;
    private byte[] bytes;

    //[min,max}
    private static final int min = 0x0000;
    private static final int max = 0xFFFF;

    public static final int byteArrayLen = 2;

    public U16(int base) {
        validate(base);
        this.base = base;
        this.bytes = toBytes(base);
    }

    public U16(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
        this.base = toUnsigned(bytes);
    }

    public U16(){
        this.base = min;
        this.bytes = toBytes(min);
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

    public int getBase() {
        return base;
    }

    public byte[] getBytes() {
        return bytes;
    }

    private void validate(int base) {
        if (base < min || base > max) {
            throw new IllegalArgumentException(String.format("%d is not unsigned short: [%d, %d]", base, min, max));
        }
    }

    private void validate(byte[] bytes) {
        if (bytes.length != 2) {
            throw new IllegalArgumentException("Invalid byte array, not 16 bit");
        }
    }

    /**
     * little endian
     */
    private static int toUnsigned(byte[] bytes) {
        return (bytes[0] & 0xFF) |
                ((bytes[1] & 0xFF) << 8);
    }

    private static byte[] toBytes(int base) {
        return new byte[]{
                (byte) (base & 0xFF),
                (byte) ((base >> 8) & 0xFF)};
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
        final U16 other = (U16) o;
        return base == other.getBase();
    }
}
