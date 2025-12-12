package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;

/**
 * A linked list of hash-consistent blocks representing a ledger of
 * monetary transactions.
 */
public class BlockChain {
    /**
     * A node in the blockchain linked list.
     */
    private static class Node {
        private Block data;
        private Node next;

        /**
         * Constructs a new Node with the given block.
         *
         * @param data the block data
         */
        Node(Block data) {
            this.data = data;
            this.next = null;
        }
    }
    private Node first;
    private Node last;
    private int size;

    /**
     * Constructs a new blockchain with an initial genesis block.
     *
     * @param initial the initial amount for the genesis block
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public BlockChain(int initial) throws NoSuchAlgorithmException {
        Block block = new Block(0, initial, null);
        first = new Node(block);
        last = first;
        size = 1;
    }

    /**
     * Mines a new block with the given transaction amount.
     *
     * @param amount the transaction amount
     * @return the newly mined block
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public Block mine(int amount) throws NoSuchAlgorithmException {
        Block prev = last.data;
        Block cur = new Block(prev.getNum() + 1, amount, prev.getHash());
        return cur;
    }

    /**
     * Gets the size of the blockchain.
     *
     * @return the number of blocks in the chain
     */
    public int getSize() {
        return size;
    }

    /**
     * Appends a block to the end of the blockchain.
     *
     * @param blk the block to append
     * @throws IllegalArgumentException if the block is invalid, hashes don't match,
     *                                  or block numbers don't match
     */
    public void append(Block blk) {
        if (!Block.isValidHash(blk.getHash())) {
            throw new IllegalArgumentException("Block is not valid.");
        }
        if (!blk.getPrevHash().equals(last.data.getHash())) {
            throw new IllegalArgumentException("Hash does not match!");
        }
        if (blk.getNum() != size) {
            throw new IllegalArgumentException("Block numbers do not match.");
        }
        Node newNode = new Node(blk);
        last.next = newNode;
        last = newNode;
        size++;
    }

    /**
     * Removes the last block from the blockchain.
     *
     * @return true if the block was removed, false if the chain only has
     *         the genesis block
     */
    public boolean removeLast() {
        if (size <= 1) {
            return false;
        }
        Node cur = first;
        while (cur.next != last) {
            cur = cur.next;
        }
        cur.next = null;
        last = cur;
        size--;
        return true;
    }

    /**
     * Gets the hash of the last block in the chain.
     *
     * @return the hash of the last block
     */
    public Hash getHash() {
        return last.data.getHash();
    }

    /**
     * Gets the previous node of the target node.
     *
     * @param target the target node
     * @return the previous node, or null if not found
     */
    public Node getPrevNode(Node target) {
        Node cur = first;
        while (cur != null && cur.next != target) {
            cur = cur.next;
        }
        return cur;
    }

    /**
     * Validates the entire blockchain.
     *
     * @return true if the blockchain is valid, false otherwise
     */
    public boolean isValidBlockChain() {
        int start = first.data.getAmount();

        Node cur = first;
        while (cur != null) {
            Block block = cur.data;
            if (!Block.isValidHash(block.getHash())) {
                return false;
            }

            if (cur != first) {
                if (!block.getPrevHash().equals(getPrevNode(cur).data.getHash())) {
                    return false;
                }
                if (block.getAmount() > start) {
                    return false;
                }
            }
            start -= block.getAmount();

            cur = cur.next;
        }
        return true;
    }

    /**
     * Prints the current balances of Alice and Bob.
     */
    public void printBalances() {
        int alice = first.data.getAmount();
        int bob = 0;

        Node cur = first.next;
        while (cur != null) {
            int transaction = cur.data.getAmount();
            alice -= transaction;
            bob += transaction;
            cur = cur.next;
        }
        System.out.println("Alice: " + alice + ", Bob: " + bob);
    }

    /**
     * Returns a string representation of the blockchain.
     *
     * @return a string representation of the blockchain
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node cur = first;
        while (cur != null) {
            sb.append(cur.data.toString());
            sb.append("\n");
            cur = cur.next;
        }
        return sb.toString();
    }
}
