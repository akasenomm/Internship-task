import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class Transaction {
    private String type;
    UUID matchId;
    private int amount;
    private String betSide;
    private UUID playerId;
    public Transaction(UUID playerId, String type, UUID matchId, int amount, String betSide) {
        this.playerId = playerId;
        this.type = type;
        this.matchId = matchId;
        this.amount = amount;
        this.betSide = betSide;
    }
    public Transaction(String type, UUID matchId, int amount, String betSide) {
        this.type = type;
        this.matchId = matchId;
        this.amount = amount;
        this.betSide = betSide;
    }
    public Transaction(UUID playerId, String type, int amount) {
        this.playerId = playerId;
        this.type = type;
        this.amount = amount;
    }
    public Transaction(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
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

    public void operationDeposit(Player player) {
        if (amount <= player.getBalance() && amount > 0) {
            player.setBalance(player.getBalance() + amount);
        } else {
            System.out.println("Error: insufficient funds to withdraw");
        }
    }
    public void operationWithdraw(Player player) {
        if (amount <= player.getBalance() && amount> 0) {
            player.setBalance(player.getBalance() - amount);
        } else {
            System.out.println("Error: insufficient funds to withdraw");
        }
    }

    public void operationBet(Player player, HashMap<UUID, Match> matches) {
        Match match = matches.get(matchId);
        String matchWinnerSide = match.getResult();

        BigDecimal matchWinnerRate = match.getMatchWinnerRate();

        if (betSide.equals(matchWinnerSide)) {
            BigDecimal playerBalance = new BigDecimal(player.getBalance());
            BigDecimal betWinnings = new BigDecimal(amount).multiply(matchWinnerRate);
            BigDecimal playerNewBalance = playerBalance.add(betWinnings);

            player.setBalance(playerNewBalance.longValue());
        } else {
            player.setBalance(player.getBalance() - amount);
        }
    }

    @Override
    public String toString() {
        return "Operation{" +
                " playerId=" + playerId + '\'' +
                ", operationName='" + type + '\'' +
                ", matchId=" + matchId +
                ", operationAmount=" + amount +
                ", betSide='" + betSide + '\''+"}";
    }
}
