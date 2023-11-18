import game.transactions.Match;
import game.transactions.Transaction;
import game.TransactionsProcessor;

import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Read match data into hashmap.
        HashMap<UUID, Match> matchData = IO.readMatches("resource/match_data.txt");

        // Read player data into queue.
        Queue<Transaction> playerData = IO.readPlayerData("resource/player_data.txt", matchData);

        // Process player data.
        TransactionsProcessor transactionsProcessor = new TransactionsProcessor(playerData);
        transactionsProcessor.processTransactions();

        // Write results.
        IO.writeResult(transactionsProcessor, "src/result.txt");
    }
}
