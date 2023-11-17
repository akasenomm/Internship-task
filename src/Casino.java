import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class Casino {
    private final List<Player> legitPlayers;
    private final List<Transaction> illegitimatePlayerTransactions;
    private long reserveBalance;
    private long casinoHostBalance;

    public Casino() {
        this.legitPlayers = new ArrayList<>();
        this.illegitimatePlayerTransactions = new ArrayList<>();
        this.reserveBalance = 0;
        this.casinoHostBalance = 0;
    }

    public void updateReserveBalance(Transaction transaction) {
        if (transaction.playerBetWon()) {
            reserveBalance += transaction.calculateBetWinnings();
        } else if (transaction.playerBetLost()){
            reserveBalance -= transaction.getAmount();
        }
    }

    public boolean isFinalPlayerTransaction(Transaction transaction, Queue<Transaction> transactions) {
        if (transactions.peek()==null)
            return true;
        UUID nextPlayerId = transactions.peek().getPlayer().getPlayerId();
        return !nextPlayerId.equals(transaction.getPlayer().getPlayerId());
    }

    public void addLegitPlayer(Player player) {
        legitPlayers.add(player);
        casinoHostBalance -= reserveBalance;
        reserveBalance = 0;
    }

    public void addIllegitimatePlayerTransaction(Transaction transaction) {
        illegitimatePlayerTransactions.add(transaction);
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

    public void printResults() {
        System.out.println(casinoHostBalance);
        System.out.println("Legit : " + legitPlayers);
        System.out.println(" Illegal: " + illegitimatePlayerTransactions);
    }

}