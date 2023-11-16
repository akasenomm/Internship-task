import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {
    private Operation operation;
    private Match match;
    private int amount;
    private String betSide;
    private UUID playerId;
    public Transaction(Operation operation, Match match, int amount, String betSide) {
        this.operation = operation;
        this.match = match;
        this.amount = amount;
        this.betSide = betSide;
    }
    public Transaction(Operation operation, int amount) {
        this.operation = operation;
        this.amount = amount;
    }
    public boolean playerBetWon() {
        if (operation == Operation.DEPOSIT || operation == Operation.WITHDRAW)
            return false;

        return this.betSide.equals(match.result());
    }
    public boolean playerBetLost() {
        if (operation == Operation.DEPOSIT || operation == Operation.WITHDRAW)
            return false;

        return !this.betSide.equals(match.result()) && !(match.result().equals("DRAW"));
    }

    public boolean playerBetDraw() {
        return match.result().equals("DRAW");
    }

    public long calculateBetWinnings() {
        BigDecimal winnerSideRate = match.getMatchWinnerRate();

        //BigDecimal winnerSideRate = match.getMatchWinnerRate();
        BigDecimal betWinnings = new BigDecimal(amount).multiply(winnerSideRate);
        return betWinnings.longValue();
    }




    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBetSide() {
        return betSide;
    }

    public void setBetSide(String betSide) {
        this.betSide = betSide;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }


    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return playerId + " " +
                operation + " " +
                match + " " +
                amount + " " +
                betSide;
    }
}
