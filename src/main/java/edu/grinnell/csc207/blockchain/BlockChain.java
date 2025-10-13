package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;

/**
 * A linked list of hash-consistent blocks representing a ledger of
 * monetary transactions.
 */
public class BlockChain {
    // TODO: fill me in!
    private static class Node {
        private Block data;
        private Node next;

        Node (Block data) {
            this.data = data;
            this.next = null;
        }
    }
    private Node first;
    private Node last;
    private int size;

    public BlockChain(int initial) throws NoSuchAlgorithmException {
        Block block = new Block(0, initial, null);
        first = new Node(block);
        last = first;
        size = 1;
    }

    public Block mine(int amount) throws NoSuchAlgorithmException {
        Block prev = last.data;
        Block cur = new Block(prev.getNum() + 1, amount, prev.getHash());
        return cur;
    }

    public int getSize() {
        return size;
    }

    public void append(Block blk) {
        if (Block.isValidHash(blk.getHash()) == false) {
            throw new IllegalArgumentException("Block is not valid.");
        }
        if(blk.getPrevHash().equals(last.data.getHash()) == false) {
            throw new IllegalArgumentException("Hash does not match!");
        }
        if(blk.getNum() != size) {
            throw new IllegalArgumentException("Block numbers do not match.");
        }
        Node newNode = new Node(blk);
        last.next = newNode;
        last = newNode;
        size++;
    }

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

    public Hash getHash() {
        return last.data.getHash();
    }

    public Node getPrevNode(Node target) {
        Node cur = first;
        while (cur != null && cur.next != target) {
            cur = cur.next;
        }
        return cur;
    }

    public boolean isValidBlockChain() {
        int start = first.data.getAmount();
        int dest = 0;

        Node cur = first;
        while (cur != null) {
            Block block = cur.data;
            if (Block.isValidHash(block.getHash()) == false) { // checks if it's a valid hash
                return false;
            }

            if (cur != first) {
                if (block.getPrevHash().equals(getPrevNode(cur).data.getHash()) == false) { // Check if the hash of previous block is same as prevhash of current block
                    return false;
                }
                if (block.getAmount() > start) {
                return false; // Invalid transaction
                }
            }
            start -= block.getAmount();
            dest += block.getAmount();

            cur = cur.next;
        }
        return true;
    }

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
        //Ex. format: Alice: 300, Bob: 0
    }

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
