package edu.grinnell.csc207.blockchain;

import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {
    // TODO: fill me in!
    private byte[] data;

    public Hash (byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public boolean isValid() {
        if (data.length < 3) {
            return false;
        }
        return data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02x", Byte.toUnsignedInt(data[i])));
        }
        return sb.toString();
    }

    public boolean equals (Object other) {
        if (this == other) {
            return true;
        }
        if (other != null && (other instanceof Hash)) {
            return true;
        }
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }

    public static byte[] calculateHash(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        byte[] hash = md.digest();
        return hash;
    }
}
