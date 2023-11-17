import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class Transaction {
    private final Player player;
    private final Operation operation;
    private Optional<Match> match;
    private final int amount;
    private Optional<String> betSide;
    public Transaction(Player player, Operation operation, Match match, int amount, String betSide) {
        this.player = player;
        this.operation = operation;
        this.match = Optional.ofNullable(match);
        this.amount = amount;
        this.betSide = Optional.ofNullable(betSide);
    }
    public boolean playerBetWon() {
        return isBetOperation() && this.betSide.orElse("").equals(match.map(Match::result).orElse(""));
    }

    public boolean playerBetLost() {
        if (!isBetOperation()) {
            return false;
        }

        String betSideValue = this.betSide.orElse("");
        String matchResult = match.map(Match::result).orElse("");

        boolean isBetSideDifferentFromResult = !betSideValue.equals(matchResult);
        boolean isMatchResultNotDraw = !matchResult.equals("DRAW");

        return isBetSideDifferentFromResult && isMatchResultNotDraw;
    }

    private boolean isBetOperation() {
        return operation != Operation.DEPOSIT && operation != Operation.WITHDRAW;
    }
    public void performPlayerOperation() {
        switch (operation) {
            case DEPOSIT -> player.deposit(amount);
            case WITHDRAW ->  player.withdraw(amount);
            case BET -> player.bet(this);
        }
    }


    public long calculateBetWinnings() {
        BigDecimal winnerSideRate = match.map(Match::getMatchWinnerRate).orElse(BigDecimal.ZERO);
        BigDecimal betWinnings = new BigDecimal(amount).multiply(winnerSideRate);
        return betWinnings.longValue();
    }

    public int getAmount() {
        return amount;
    }


    public Player getPlayer() {
        return this.player;
    }

    public UUID getPlayerId() {
        return this.getPlayer().getPlayerId();
    }
    public Optional<Match> getMatch() {
        return match;
    }

    @Override
    public String toString() {
        String output;

        UUID matchId = null;
        String betSideString = null;
        if (!match.isEmpty() && !betSide.isEmpty()) {
            matchId = match.get().id();
            betSideString = betSide.get().toString();
            output = player.getPlayerId()+" "+operation+" "+matchId+" "+amount+" "+betSideString;
        } else {
            output = player.getPlayerId()+" "+operation+" "+amount;
        }
        return output;
    }
}
