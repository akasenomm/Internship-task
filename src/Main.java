import transactions.Match;
import transactions.Operation;
import transactions.Player;
import transactions.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Java program to process betting data. Playtech winternship 2024 task.
 *
 * Artur Kasenõmm
 */
public class Main {
    private static final String matchData = "resource/match_data.txt";
    private static final String playerData = "resource/player_data.txt";
    private static final String resultData = "src/result.txt";

    public static void main(String[] args) {
        // Read match data into hashmap.
        HashMap<UUID, Match> matchData = readMatches();
        // Read player data into queue.
        Queue<Transaction> playerData = readPlayerData(matchData);
        // Process player data.
        TransactionsProcessor transactionsProcessor = new TransactionsProcessor(playerData);
        transactionsProcessor.processTransactions();
        // Write results.
        writeResult(transactionsProcessor);
    }

    /**
     * Reads match data from a file and returns a map of matches.
     *
     * @return A map of matches.
     */
    private static HashMap<UUID, Match> readMatches() {
        HashMap<UUID, Match> matchesMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(matchData))) {
            String line;
            // Read each line
            while ((line = br.readLine()) != null) {
                String[] matchData = line.trim().split(",");
                // Parse data
                UUID matchId = UUID.fromString(matchData[0]);
                BigDecimal rateA = new BigDecimal(matchData[1]);
                BigDecimal rateB = new BigDecimal(matchData[2]);
                String result = matchData[3];
                // Create Match and map it
                matchesMap.put(matchId, new Match(matchId, rateA, rateB, result));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return matchesMap;
    }

    /**
     * Reads player data from a file and returns a queue of transactions.
     * @param matchMap A map of matches.
     * @return A queue of transactions.
     */
    private static Queue<Transaction> readPlayerData(HashMap<UUID, Match> matchMap) {
        HashMap<UUID, Player> playersMap = new HashMap<>();
        Queue<Transaction> playerTransactions = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(playerData))) {
            // Read each line
            String line;
            while ((line = br.readLine()) != null) {
                // Create Transaction object and add it to queue
                Transaction transaction = parseTransaction(line, playersMap, matchMap);
                playerTransactions.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return playerTransactions;
    }

    /**
     * Parses a transaction from a line of text.
     * @param line The line of text.
     * @param playersMap A map of players.
     * @param matchMap A map of matches.
     * @return A transaction.
     */
    private static Transaction parseTransaction(String line, HashMap<UUID, Player> playersMap, HashMap<UUID, Match> matchMap) {
        String[] transactionData = line.trim().split(",");
        // Parse data
        UUID playerId = UUID.fromString(transactionData[0]);
        Operation operation = Operation.valueOf(transactionData[1]);
        int amount = Integer.parseInt(transactionData[3]);
        Match match = null;
        String betSide = null;
        // Match and bet side exist only for BET transaction
        if (operation == Operation.BET) {
            UUID matchId = UUID.fromString(transactionData[2]);
            match = matchMap.get(matchId);
            betSide = transactionData[4];
        }
        // Create Player object (if same Player already not mapped) and map it
        Player player = playersMap.getOrDefault(playerId, new Player(playerId));
        playersMap.put(playerId, player);

        // Create Transaction object
        return new Transaction(player, operation, match, amount, betSide);
    }

    /**
     * Writes the results of a TransactionsProcessor to a file.
     * @param processor The TransactionsProcessor.
     */
    private static void writeResult(TransactionsProcessor processor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(resultData))) {
            // Get legitimate and illegitimate player lists and sort them by ID
            List<Player> legitimatePlayers = processor.getCasino().getLegitPlayers();
            legitimatePlayers.sort(Comparator.comparing(Player::getPlayerId));
            // Same for illegitimate transactions
            List<Transaction> illegitimatePlayerTransactions = processor.getCasino().getIllegitimatePlayerTransactions();
            illegitimatePlayerTransactions.sort(Comparator.comparing(Transaction::getPlayerId));

            // Write data for each legitimate player
            writeList(bw, legitimatePlayers);
            // Write data for each illegal transaction
            writeList(bw, illegitimatePlayerTransactions);
            // Write the final casino host balance
            bw.write(String.valueOf(processor.getCasino().getCasinoHostBalance()));
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Writes a list to a BufferedWriter.
     * @param bw The BufferedWriter.
     * @param list The list.
     * @throws IOException If an I/O error occurs.
     */
    private static void writeList(BufferedWriter bw, List<?> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            bw.write(String.valueOf(list.get(i)));
            if (i != list.size()-1)
                bw.write(", ");
        }
        bw.write("\n\n");
    }
}
