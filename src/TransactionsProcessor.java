import transactions.Transaction;

import java.util.*;

/**
 * This class processes transactions (player data)
 */
public class TransactionsProcessor {
    private final Queue<Transaction> transactions;
    private final Casino casino;

    /**
     * Constructs a new TransactionsProcessor with the given transactions.
     *
     * @param transactions the transactions to be processed
     */
    public TransactionsProcessor(Queue<Transaction> transactions) {
        this.transactions = transactions;
        this.casino = new Casino();
    }

    /**
     * Processes all transactions.
     */
    public void processTransactions() {
        while (!transactions.isEmpty()){
            Transaction transaction = transactions.poll();
            processTransaction(transaction);
        }
    }

    /**
     * Processes a single transaction.
     *
     * @param transaction the transaction to be processed
     */
    private void processTransaction(Transaction transaction) {
        // Perform the operation associated with the transaction
        transaction.performPlayerOperation();

        // If the player is not legitimate after performing transaction, handle the illegitimate player
        // (player has boolean field for legitimacy, which changes to false if player attempts illegal move)
        if (!transaction.getPlayer().isLegitimate()) {
            handleIllegitimatePlayer(transaction);
            // Do not need to process transaction further
            return;
        }

        // Update the casino's reserve balance
        // (reserve balance impacts real balance only when player is claimed legitimate after all transactions)
        casino.updateReserveBalance(transaction);

        // If this is the last transaction for the player, and it was legitimate,
        // we can add the player to the legitimate player's list (all other moves were too then)
        if (casino.isLastTransactionForPlayer(transaction, transactions))
            casino.addPlayer(transaction.getPlayer());
    }

    /**
     * Handles an illegitimate player.
     *
     * @param transaction the transaction associated with the illegitimate player
     */
    private void handleIllegitimatePlayer(Transaction transaction) {
        // Add the transaction to the list of illegitimate transactions
        casino.addIllegitimateTransaction(transaction);
        // Remove all remaining transactions for the player
        removeRemainingTransactionsForPlayer(transaction);
    }

    /**
     * Removes all remaining transactions for a player.
     *
     * @param transaction the transaction associated with the player
     */
    private void removeRemainingTransactionsForPlayer(Transaction transaction) {
        // While there are more transactions for the player, remove them
        while (!casino.isLastTransactionForPlayer(transaction, transactions))
            transactions.remove();
    }

    /**
     * Returns the casino associated with this TransactionsProcessor.
     *
     * @return the casino
     */
    public Casino getCasino() {
        return casino;
    }
}
