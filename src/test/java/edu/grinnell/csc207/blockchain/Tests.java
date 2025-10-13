package edu.grinnell.csc207.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tests {
    // TODO: fill me in with tests that you write for this project!
    
    @Test
    @DisplayName("Placeholder Test")
    public void placeholderTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testCopy() {
        byte[] data = {1, 2, 3, 4};
        Hash h = new Hash(data);
        data[0] = 99;
        assertEquals(1, h.getData()[0]);
    }

    @Test
    public void testHexToString() {
        byte[] data = {(byte)0xAB, (byte)0xCD};
        Hash h = new Hash(data);
        assertEquals("abcd", h.toString());
    }

    @Test
    public void testEqualsSameData() {
        byte[] a = {1, 2, 3};
        byte[] b = {1, 2, 3};
        Hash h1 = new Hash(a);
        Hash h2 = new Hash(b);
        assertEquals(h1, h2);
    }

    @Test
    public void testMineHashDeterministic() throws NoSuchAlgorithmException {
        Hash h1 = Block.mineHash(1, 10, null, 5);
        Hash h2 = Block.mineHash(1, 10, null, 5);
        assertEquals(h1.toString(), h2.toString());
    }

    @Test
    public void testMineHashDifferentNonceDifferentHash() throws NoSuchAlgorithmException {
        Hash h1 = Block.mineHash(1, 10, null, 5);
        Hash h2 = Block.mineHash(1, 10, null, 6);
        assertNotEquals(h1.toString(), h2.toString());
    }

    @Test
    public void testIsValidHash() {
        byte[] valid = {0, 0, 0, 1, 2, 3};
        Hash h = new Hash(valid);
        assertEquals(true, Block.isValidHash(h));
    }

    @Test
    public void testAppendValidBlockIncreasesSize() throws NoSuchAlgorithmException {
        BlockChain chain = new BlockChain(100);
        Block blk = chain.mine(20);
        int oldSize = chain.getSize();
        chain.append(blk);
        assertEquals(oldSize + 1, chain.getSize());
    }

}
