import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Player {
    private final UUID playerId;
    private int totalBets;
    private int totalWins;
    private long balance;
    private boolean legitimate;

    public Player(UUID playerId) {
        this.legitimate = true;
        this.playerId = playerId;
        this.totalBets = 0;
        this.totalWins = 0;
        this.balance = 0;
    }

    protected void deposit(int amount) {
        if (amount > 0)
            this.balance += amount;
        else {
            this.legitimate = false;
            System.out.printf("Error: negative deposit %s, %s", amount, playerId);
        }
    }

    public void withdraw(int amount) {
        if (amount <= this.balance && amount > 0) {
            this.balance -= amount;
        } else {
            this.legitimate = false;
            System.out.printf("Error: current balance %s, withdraw amount %s, %s", balance, amount, playerId);
        }
    }


    public void bet(Transaction transaction) {
        this.totalBets++;

        int betAmount = transaction.getAmount();
        if (betAmount > this.balance || betAmount < 1) {
            this.legitimate = false;
            return;
        }

        if (transaction.playerBetWon()) {
            long betWinnings = transaction.calculateBetWinnings();
            this.balance += betWinnings;
            this.totalWins++;
        } else if (transaction.playerBetLost()) {
            this.balance -= betAmount;
        }
    }

    public BigDecimal getWinRate() {
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

    @Override
    public String toString() {
        return playerId + " " +
                balance +" " +
                getWinRate();
    }
}
