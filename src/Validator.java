import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class Validator {
    private HashMap<UUID, Match> matches;
    private HashMap<UUID, Queue<Transaction>> transactions;
    private ArrayList<Player> legitPlayers;
    private ArrayList<Transaction> illegimatePlayerTransactions;
    private long casinoHostBalance;

    public Validator(HashMap<UUID, Queue<Transaction>> transactions, HashMap<UUID, Match> matches) {
        this.matches = matches;
        this.transactions = transactions;
        this.legitPlayers = new ArrayList<>();
        this.illegimatePlayerTransactions = new ArrayList<>();
        this.casinoHostBalance = 0;
    }
    public void validatePlayers() {

        for (UUID playerId : transactions.keySet()) {
            Queue<Transaction> playerTransactionsGroup = transactions.get(playerId);
            Player player = new Player(playerId);

            for (Transaction playerTransaction : playerTransactionsGroup) {

                switch (playerTransaction.getType()) {
                    case "DEPOSIT" -> player.deposit(playerTransaction.getAmount());
                    case "WITHDRAW" -> player.withdraw(playerTransaction.getAmount());
                    case "BET" -> {
                        UUID matchId = playerTransaction.getMatchId();
                        Match match = matches.get(matchId);
                        player.bet(match, playerTransaction);
                    }
                }
                if (!player.isLegitimate()) {
                    illegimatePlayerTransactions.add(playerTransaction);
                }
            }
            if (player.isLegitimate()) {
                legitPlayers.add(player);
            }
        }
        System.out.println(legitPlayers);
        System.out.println(illegimatePlayerTransactions);
    }
    public boolean checkPlayerWin() {
        return false;
    }
    public void playerWins() {

    }
    public void validateBet() {

    }
}
