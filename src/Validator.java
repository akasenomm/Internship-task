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
            long playerImpactOnCasino = 0;;
            for (Transaction transaction : playerTransactionsGroup) {
                int amount = transaction.getAmount();

                switch (transaction.getOperation()) {
                    case DEPOSIT -> player.deposit(amount);
                    case WITHDRAW ->  player.withdraw(amount);
                    case BET -> player.bet(transaction);
                }

                if (!player.isLegitimate()) {
                    illegimatePlayerTransactions.add(transaction);
                    break;
                }
                if (transaction.getOperation() == Operation.BET && transaction.playerBetWon()) {
                    //System.out.println("KASSA KAOTAS: "+ amount + ", bet . "+transaction.getAmount() + ", t= "+ transaction.getMatch().getId());
                    System.out.println("this one : "+ transaction);
                    playerImpactOnCasino += transaction.calculateBetWin();
                } else if (transaction.getOperation() == Operation.BET){
                    playerImpactOnCasino -= amount;
                }
                //System.out.println(playerImpactOnCasino + " , " +transaction);
            }
            if (player.isLegitimate()) {
                legitPlayers.add(player);
                casinoHostBalance -= playerImpactOnCasino;
            }
        }
        System.out.println(".........");
        System.out.println(".........");
        System.out.println("Legit : " + legitPlayers);
        System.out.println(" Illegal: " + illegimatePlayerTransactions);
        System.out.println(casinoHostBalance);
    }
}
