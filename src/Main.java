import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Read match data into hashmap.
        HashMap<UUID, Match> matchData = IO.readMatches("resource/match_data2.txt");

        Queue<Transaction> playerData = IO.readPlayerData("resource/player_data2.txt", matchData);

        TransactionsProcessor transactionsProcessor = new TransactionsProcessor(playerData);

        transactionsProcessor.validatePlayers();
        IO.writeResult(transactionsProcessor, "resultsProov1.txt");


    }
}
