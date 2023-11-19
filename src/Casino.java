import transactions.Player;
import transactions.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * This class represents a Casino with a list of legitimate players,
 * a list of illegitimate transactions, a reserve balance, and a casino host balance.
 * The reserve balance is to keep track of player's impact on Casino balance.
 * The reserve balance impacts casino balance once player is deemed legitimate.
 */
public class Casino {
    private final List<Player> legitPlayers;
    private final List<Transaction> illegitimatePlayerTransactions;
    private long reserveBalance;
    private long casinoHostBalance;

    /**
     * Constructor for the Casino class.
     * Initializes the lists and balances.
     */
    public Casino() {
        this.legitPlayers = new ArrayList<>();
        this.illegitimatePlayerTransactions = new ArrayList<>();
        this.reserveBalance = 0;
        this.casinoHostBalance = 0;
    }

    /**
     * Updates the reserve balance based on the transaction.
     * If the player won the bet, the winnings are added to the reserve balance.
     * If the player lost the bet, the bet amount is subtracted from the reserve balance.
     * @param transaction The transaction to be processed.
     */
    public void updateReserveBalance(Transaction transaction) {
        if (transaction.isBetOperation() && transaction.playerBetWon())
            reserveBalance -= transaction.calculateBetWinnings();
        else if (transaction.playerBetLost())
            reserveBalance += transaction.getAmount();
    }

    /**
     * Checks if the given transaction is the last one for the player in the queue.
     * @param transaction The transaction to be checked.
     * @param transactions The queue of transactions.
     * @return true if the transaction is the last one for the player, false otherwise.
     */
    public boolean isLastTransactionForPlayer(Transaction transaction, Queue<Transaction> transactions) {
        if (transactions.peek()==null)
            return true;
        UUID nextPlayerId = transactions.peek().getPlayer().getPlayerId();
        return !nextPlayerId.equals(transaction.getPlayer().getPlayerId());
    }

    /**
     * Adds a player to the list of legitimate players and updates the balances.
     * @param player The player to be added.
     */
    public void addPlayer(Player player) {
        legitPlayers.add(player);
        casinoHostBalance += reserveBalance;
        reserveBalance = 0;
    }

    /**
     * Adds a transaction to the list of illegitimate transactions.
     * @param transaction The transaction to be added.
     */
    public void addIllegitimateTransaction(Transaction transaction) {
        illegitimatePlayerTransactions.add(transaction);
        reserveBalance = 0;
    }

    public List<Player> getLegitPlayers() {
        return legitPlayers;
    }

    public List<Transaction> getIllegitimatePlayerTransactions() {
        return illegitimatePlayerTransactions;
    }

    public long getCasinoHostBalance() {
        return casinoHostBalance;
    }
}
