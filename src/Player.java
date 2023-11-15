import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.UUID;

public class Player {
    private UUID playerId;
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
            System.out.println("Error: negative balance");
        }
    }

    public void withdraw(int amount) {
        if (amount <= this.balance && amount > 0) {
            this.balance -= amount;
        } else {
            this.legitimate = false;
            System.out.println("Error: insufficient funds to withdraw");
        }
    }


    public void bet(Transaction transaction) {
        this.totalBets++;

        int betAmount = transaction.getAmount();
        if (betAmount > this.balance || betAmount < 1) {
            this.legitimate = false;
            return;
        }

        String playerBetSide = transaction.getBetSide();
        String result = transaction.getMatch().getResult();

        if (playerBetSide.equals(result)) {
            long betWinnings = transaction.calculateBetWin();
            this.balance += betWinnings;
            this.totalWins++;
        } else if (!result.equals("DRAW")) {
            this.balance -= betAmount;
        }
    }

    public boolean isLegitimate() {
        return legitimate;
    }

    public void setLegitimacy(boolean legitimacy) {
        this.legitimate = legitimacy;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
    public BigDecimal getWinRate() {
        double winRateDouble = (double) totalWins / (double) totalBets;
        BigDecimal winRate = BigDecimal.valueOf(winRateDouble);
        return winRate.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", totalBets=" + totalBets +
                ", totalWins=" + totalWins +
                ", balance=" + balance + ", winrate=" + getWinRate()+
                '}';
    }


}
