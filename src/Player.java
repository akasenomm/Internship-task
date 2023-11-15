import java.math.BigDecimal;
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


    public void bet(Match match, Transaction transaction) {
        this.totalBets++;

        int betAmount = transaction.getAmount();
        if (betAmount > this.balance || betAmount < 1) {
            this.legitimate = false;
            return;
        }

        String playerBetSide = transaction.getBetSide();
        String result = match.getResult();
        BigDecimal matchWinnerRate = match.getMatchWinnerRate();

        if (playerBetSide.equals(result)) {
            this.balance = calculateNewBalance(betAmount, matchWinnerRate);
            this.totalWins++;
        } else if (result.equals("DRAW")) {
            return;
        } else {
            this.balance -= betAmount;
        }
    }

    public long calculateNewBalance(long betAmount, BigDecimal matchWinnerRate) {
        BigDecimal betWinnings = new BigDecimal(betAmount).multiply(matchWinnerRate);
        BigDecimal newBalance = betWinnings.add(new BigDecimal(this.balance));
        return newBalance.longValue();
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

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", totalBets=" + totalBets +
                ", totalWins=" + totalWins +
                ", balance=" + balance +
                '}';
    }


}
