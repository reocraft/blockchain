package edu.grinnell.csc207.blockchain;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A single block of a blockchain.
 */
public class Block {
    // TODO: fill me in!
    private final int num;
    private final int amount;
    private final Hash prevHash;
    private long nonce;
    private Hash hash;

    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException{
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        // nonce = we will make method to calculate
        long nonce = 0;
        Hash computed;
        while (true) {
            computed = mineHash(num, amount, prevHash, nonce);
            if (isValidHash(computed)) {
                break;
            }
            nonce++;
        }
        this.nonce = nonce;
        this.hash = mineHash(num, amount, prevHash, nonce);
    }

    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = mineHash(num, amount, prevHash, nonce);
    }

    public int getNum() {
        return num;
    }
    public int getAmount() {
        return amount;
    }
    public long getNonce() {
        return nonce;
    }
    public Hash getPrevHash() {
        return prevHash;
    }
    public Hash getHash() {
        return hash;
    }
 
    public static Hash mineHash(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(ByteBuffer.allocate(4).putInt(num).array());
        md.update(ByteBuffer.allocate(4).putInt(amount).array());
        if (prevHash != null) {
            md.update(prevHash.getData());
        }
        md.update(ByteBuffer.allocate(8).putLong(nonce).array());
        byte[] digest = md.digest();
        return new Hash(digest);
    }

    public static boolean isValidHash(Hash h) {
        byte[] bytes = h.getData();
        return bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0;
    }


    public String toString() {
        String prevHashStr;
        if (prevHash == null) {
            prevHashStr = "null";
        } else {
            prevHashStr = prevHash.toString();
        }
        return ("Block " + num + " (Amount: " + amount + ", Nonce: " + nonce + ", prevHash: " + prevHashStr + ", hash: " + hash.toString() + ")");
    }
}
