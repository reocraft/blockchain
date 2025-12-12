package edu.grinnell.csc207.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * The main driver for the block chain program.
 */
public class BlockChainDriver {

    /**
     * Main method that runs the blockchain interactive program.
     *
     * @param args command line arguments; expects one argument: initial amount
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Ensure exactly one argument is provided
        if (args.length != 1) {
            System.out.println("Usage: java BlockChainDriver <initial amount>");
            return;
        }

        int initial;
        try {
            initial = Integer.parseInt(args[0]);
            if (initial < 0) {
                System.out.println("Initial amount must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Initial amount must be an integer.");
            return;
        }

        // Initialize blockchain
        BlockChain chain = new BlockChain(initial);

        // Command loop
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nCurrent chain:");
                System.out.println(chain.toString());
                System.out.print("Command? ");
                String cmd = sc.next().toLowerCase();

                if (cmd.equals("help")) {
                    System.out.println("Valid commands:");
                    System.out.println("    mine: discovers the nonce for a given transaction");
                    System.out.println("    append: appends a new block onto the end of the chain");
                    System.out.println("    remove: removes the last block from the end of the chain");
                    System.out.println("    check: checks that the block chain is valid");
                    System.out.println("    report: reports the balances of Alice and Bob");
                    System.out.println("    help: prints this list of commands");
                    System.out.println("    quit: quits the program");
                } else if (cmd.equals("mine")) {
                    System.out.print("Amount transferred? ");
                    int mineAmount = sc.nextInt();
                    Block minedBlock = chain.mine(mineAmount);
                    System.out.println("Mined block: " + minedBlock);
                } else if (cmd.equals("append")) {
                    System.out.print("Amount transferred? ");
                    int appendAmount = sc.nextInt();
                    System.out.print("Nonce? ");
                    long nonce = sc.nextLong();
                    Block blk = new Block(chain.getSize(), appendAmount, chain.getHash(), nonce);
                    chain.append(blk);
                    System.out.println("Block appended.");
                } else if (cmd.equals("remove")) {
                    if (chain.removeLast()) {
                        System.out.println("Last block removed.");
                    } else {
                        System.out.println("Cannot remove the genesis block.");
                    }
                } else if (cmd.equals("check")) {
                    if (chain.isValidBlockChain()) {
                        System.out.println("Chain is valid!");
                    } else {
                        System.out.println("Chain is invalid!");
                    }
                } else if (cmd.equals("report")) {
                    chain.printBalances();
                } else if (cmd.equals("quit")) {
                    System.out.println("Exiting...");
                    return;
                } else {
                    System.out.println("Invalid input. Type \"help\" to see a list of commands.");
                }
            }
        }
    }
}
