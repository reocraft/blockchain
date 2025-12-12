package edu.grinnell.csc207.blockchain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A single block of a blockchain.
 */
public class Block {
    private final int num;
    private final int amount;
    private final Hash prevHash;
    private long nonce;
    private Hash hash;

    /**
     * Constructs a new Block by mining for a valid nonce.
     *
     * @param num the block number
     * @param amount the transaction amount
     * @param prevHash the hash of the previous block
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
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

    /**
     * Constructs a new Block with a given nonce.
     *
     * @param num the block number
     * @param amount the transaction amount
     * @param prevHash the hash of the previous block
     * @param nonce the nonce value for this block
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = mineHash(num, amount, prevHash, nonce);
    }

    /**
     * Gets the block number.
     *
     * @return the block number
     */
    public int getNum() {
        return num;
    }

    /**
     * Gets the transaction amount.
     *
     * @return the transaction amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the nonce value.
     *
     * @return the nonce value
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Gets the hash of the previous block.
     *
     * @return the hash of the previous block
     */
    public Hash getPrevHash() {
        return prevHash;
    }

    /**
     * Gets the hash of this block.
     *
     * @return the hash of this block
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * Mines a hash for a block with the given parameters.
     *
     * @param num the block number
     * @param amount the transaction amount
     * @param prevHash the hash of the previous block
     * @param nonce the nonce value
     * @return the computed hash
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
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

    /**
     * Checks if a hash is valid (starts with three zero bytes).
     *
     * @param h the hash to check
     * @return true if the hash is valid, false otherwise
     */
    public static boolean isValidHash(Hash h) {
        byte[] bytes = h.getData();
        return bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0;
    }

    /**
     * Returns a string representation of this block.
     *
     * @return a string representation of this block
     */
    @Override
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
