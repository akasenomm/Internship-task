package game.transactions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Represents a player in the casino.
 */
public class Player {
    private final UUID playerId;
    private int totalBets;
    private int totalWins;
    private long balance;
    private boolean legitimate;

    /**
     * Constructs a new Player with the given ID.
     *
     * @param playerId the ID of the player
     */
    public Player(UUID playerId) {
        this.legitimate = true;
        this.playerId = playerId;
        this.totalBets = 0;
        this.totalWins = 0;
        this.balance = 0;
    }

    /**
     * Deposits the given amount into the player's balance.
     *
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the amount is negative
     */
    public void deposit(int amount) {
        try {
            if (amount >= 0)
                this.balance += amount;
            else
                throw new IllegalArgumentException("Cannot deposit negative amount");
        } catch (IllegalArgumentException e) {
            this.legitimate = false;
        }
    }

    /**
     * Withdraws the given amount from the player's balance.
     *
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if the amount is negative or exceeds the player's balance
     */
    public void withdraw(int amount) {
        try {
            if (amount <= this.balance && amount > 0)
                this.balance -= amount;
            else
                throw new IllegalArgumentException("Cannot withdraw negative amount or more than balance");
        } catch (IllegalArgumentException e) {
            this.legitimate = false;
        }

    }
    /**
     * Places a bet using the given transaction; changes player balance
     *
     * @param transaction the transaction representing the bet
     */
    public void bet(Transaction transaction) {
        this.totalBets++;

        // Check if bet can be made
        int betAmount = transaction.getAmount();
        if (betAmount > this.balance || betAmount < 0) {
            this.legitimate = false;
            return;
        }

        if (transaction.playerBetWon()) {
            int betWinnings = transaction.calculateBetWinnings();
            this.balance += betWinnings;
            this.totalWins++;
        } else if (transaction.playerBetLost())
            this.balance -= betAmount;

    }

    /**
     * Returns the player's win rate.
     *
     * @return the win rate
     */
    public BigDecimal getWinRate() {
        // To avoid dividing by zero
        if (this.totalBets == 0)
            return new BigDecimal(0);

        double winRateDouble = (double) totalWins / (double) totalBets;
        BigDecimal winRate = BigDecimal.valueOf(winRateDouble);
        return winRate.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isLegitimate() {
        return legitimate;
    }

    public UUID getPlayerId() {
        return playerId;
    }


    /**
     * Returns a string representation of the player for writing to results.
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return playerId + " " +
                balance +" " +
                getWinRate();
    }
}
