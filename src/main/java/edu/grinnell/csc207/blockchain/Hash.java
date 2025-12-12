package edu.grinnell.csc207.blockchain;

import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {
    private byte[] data;

    /**
     * Constructs a new Hash with the given byte array.
     *
     * @param data the hash data
     */
    public Hash(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    /**
     * Gets a copy of the hash data.
     *
     * @return a copy of the hash data
     */
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Checks if this hash is valid (starts with three zero bytes).
     *
     * @return true if the hash is valid, false otherwise
     */
    public boolean isValid() {
        if (data.length < 3) {
            return false;
        }
        return data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    /**
     * Returns a hexadecimal string representation of this hash.
     *
     * @return a hexadecimal string representation of this hash
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02x", Byte.toUnsignedInt(data[i])));
        }
        return sb.toString();
    }

    /**
     * Compares this hash with another object for equality.
     *
     * @param other the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof Hash)) {
            return false;
        }
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }

    /**
     * Calculates a hash for the given message string.
     *
     * @param msg the message to hash
     * @return the hash as a byte array
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public static byte[] calculateHash(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        byte[] hash = md.digest();
        return hash;
    }
}
