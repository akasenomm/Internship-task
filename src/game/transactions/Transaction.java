package game.transactions;

import game.transactions.Match;
import game.transactions.Operation;
import game.transactions.Player;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * This class represents a transaction in the betting system (a line in player_data)
 */
public class Transaction {
     final Player player;
    private final Operation operation;
    private final Optional<Match> match;
    private final int amount;
    private final Optional<String> betSide;

    /**
     * Constructs a new Transaction.
     *
     * @param player The player making the transaction.
     * @param operation The operation being performed (DEPOSIT, BET, WITHDRAW)
     * @param match The match being bet on, if applicable.
     * @param amount The amount of money involved in the transaction.
     * @param betSide The side the player is betting on, if applicable.
     */
    public Transaction(Player player, Operation operation, Match match, int amount, String betSide) {
        this.player = player;
        this.operation = operation;
        this.match = Optional.ofNullable(match);
        this.amount = amount;
        this.betSide = Optional.ofNullable(betSide);
    }

    /**
     * Checks if the transaction is a BET and if player's bet won.
     *
     * @return true if the player's bet won, false otherwise.
     */
    public boolean playerBetWon() {
        String playerBet = this.betSide.orElse("");
        String matchResult = match.map(Match::result).orElse("");
        return playerBet.equals(matchResult);
    }

    /**
     * Checks if the player's bet lost.
     *
     * @return true if the player's bet lost, false otherwise.
     */
    public boolean playerBetLost() {
        if (!isBetOperation())
            return false;

        String betSideValue = this.betSide.orElse("");
        String matchResult = match.map(Match::result).orElse("");
        // Player bet side is not result.
        boolean isBetSideDifferentFromResult = !betSideValue.equals(matchResult);
        // Player bet side is not DRAW
        boolean isMatchResultNotDraw = !matchResult.equals("DRAW");
        // Must be the side that lost
        return isBetSideDifferentFromResult && isMatchResultNotDraw;
    }

    /**
     * Checks if the operation is a bet operation.
     *
     * @return true if the operation is a bet operation, false otherwise.
     */
    public boolean isBetOperation() {
        return operation != Operation.DEPOSIT && operation != Operation.WITHDRAW;
    }

    /**
     * Performs the operation associated with this transaction. Calls player's method.
     */
    public void performPlayerOperation() {
        switch (operation) {
            case Operation.DEPOSIT -> player.deposit(amount);
            case Operation.WITHDRAW ->  player.withdraw(amount);
            case Operation.BET -> player.bet(this);
        }
    }

    /**
     * Calculates the winnings from a bet (bet amount * winner side rate)
     *
     * @return The winnings from the bet.
     */
    public int calculateBetWinnings() {
        BigDecimal winnerSideRate = match.map(Match::getMatchWinnerRate).orElse(BigDecimal.ZERO);
        BigDecimal betWinnings = new BigDecimal(amount).multiply(winnerSideRate);
        return betWinnings.intValue();
    }

    /**
     * Gets the amount of money involved in the transaction.
     *
     * @return The amount of money.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the player involved in the transaction.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the ID of the player involved in the transaction.
     *
     * @return The player's ID.
     */
    public UUID getPlayerId() {
        return this.getPlayer().getPlayerId();
    }


    /**
     * Returns a string representation of the transaction.
     *
     * @return A string representation of the transaction.
     */
    @Override
    public String toString() {
        // Player and operation should always be present
        StringBuilder output = new StringBuilder(player.getPlayerId() + " " + operation);
        // Match is optional. If not present, add whitespace
        match.map(Match::id).ifPresent(id -> output.append(" ").append(id));
        // Amount should always be present
        output.append(" ").append(amount);
        // Bet side is optional
        betSide.ifPresent(side -> output.append(" ").append(side));
        return output.toString();
    }
}
