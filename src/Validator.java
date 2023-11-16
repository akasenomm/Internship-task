import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class Validator {
    private HashMap<UUID, Queue<Transaction>> transactions;
    private ArrayList<Player> legitPlayers;
    private ArrayList<Transaction> illegimatePlayerTransactions;
    private long casinoHostBalance;

    public Validator(HashMap<UUID, Queue<Transaction>> transactions) {
        this.transactions = transactions;
        this.legitPlayers = new ArrayList<>();
        this.illegimatePlayerTransactions = new ArrayList<>();
        this.casinoHostBalance = 0;
    }
    public void validatePlayers() {
        for (UUID playerId : transactions.keySet()) {
            Queue<Transaction> playerTransactionsGroup = transactions.get(playerId);
            Player player = new Player(playerId);
            long reserveBalance = 0;
            for (Transaction transaction : playerTransactionsGroup) {
                processTransaction(transaction);
                int amount = transaction.getAmount();

                switch (transaction.getOperation()) {
                    case DEPOSIT -> player.deposit(amount);
                    case WITHDRAW ->  player.withdraw(amount);
                    case BET -> player.bet(transaction);
                }

                if (!player.isLegitimate()) {
                    transaction.setPlayerId(playerId);
                    illegimatePlayerTransactions.add(transaction);
                    break;
                }

                if (transaction.playerBetWon()) {
                    reserveBalance += transaction.calculateBetWinnings();
                } else if (transaction.playerBetLost()){
                    reserveBalance -= transaction.getAmount();
                }
            }
            if (player.isLegitimate()) {
                legitPlayers.add(player);
                casinoHostBalance -= reserveBalance;
            }
            System.out.println("HOSTBALANCE: "+casinoHostBalance);
        }
        System.out.println(casinoHostBalance);
        System.out.println(".........");
        System.out.println(".........");
        System.out.println("Legit : " + legitPlayers);
        System.out.println(" Illegal: " + illegimatePlayerTransactions);
    }
    public static void processTransaction(Transaction transaction) {

    }

    public HashMap<UUID, Queue<Transaction>> getTransactions() {
        return transactions;
    }

    public void setTransactions(HashMap<UUID, Queue<Transaction>> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Player> getLegitPlayers() {
        return legitPlayers;
    }

    public void setLegitPlayers(ArrayList<Player> legitPlayers) {
        this.legitPlayers = legitPlayers;
    }

    public ArrayList<Transaction> getIllegimatePlayerTransactions() {
        return illegimatePlayerTransactions;
    }

    public void setIllegimatePlayerTransactions(ArrayList<Transaction> illegimatePlayerTransactions) {
        this.illegimatePlayerTransactions = illegimatePlayerTransactions;
    }

    public long getCasinoHostBalance() {
        return casinoHostBalance;
    }

    public void setCasinoHostBalance(long casinoHostBalance) {
        this.casinoHostBalance = casinoHostBalance;
    }
}
