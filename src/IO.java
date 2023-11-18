import game.*;
import game.transactions.Match;
import game.transactions.Operation;
import game.transactions.Player;
import game.transactions.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * This class provides static methods for reading match and player data from files.
 */
public class IO {
    /**
     * Reads match data from a file and returns a map of matches.
     * @param path The path to the file.
     * @return A map of matches.
     */
    public static HashMap<UUID, Match> readMatches(String path) {
        HashMap<UUID, Match> matchesMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            // Read each line
            while ((line = br.readLine()) != null) {
                String[] matchData = line.trim().split(",");
                // Error handling
                if (matchData.length != 4) {
                    System.out.println("Invalid data format in line: " + line);
                    continue;
                }
                // Parse data
                UUID matchId = UUID.fromString(matchData[0]);
                BigDecimal rateA = new BigDecimal(matchData[1]);
                BigDecimal rateB = new BigDecimal(matchData[2]);
                String result = matchData[3];
                // Create Match and map it
                Match match = new Match(matchId, rateA, rateB, result);
                matchesMap.put(matchId, match);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return matchesMap;
    }

    /**
     * Reads player data from a file and returns a queue of transactions.
     * @param path The path to the file.
     * @param matchMap A map of matches.
     * @return A queue of transactions.
     */
    public static Queue<Transaction> readPlayerData(String path, HashMap<UUID, Match> matchMap) {
        HashMap<UUID, Player> playersMap = new HashMap<>();
        Queue<Transaction> playerTransactions = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read each line
            String line;
            while ((line = br.readLine()) != null) {
                // Create Transaction object and add it to queue
                Transaction transaction = parseTransaction(line, playersMap, matchMap);
                if (transaction != null)
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
    public static Transaction parseTransaction(String line, HashMap<UUID, Player> playersMap, HashMap<UUID, Match> matchMap) {
        String[] transactionData = line.trim().split(",");
        // Error handling
        if (transactionData.length < 4) {
            System.out.println("Invalid data format in line: " + line);
            return null;
        }
        // Parse data
        UUID playerId = UUID.fromString(transactionData[0]);
        Operation operation = getOperation(transactionData[1]);
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
     * @param fileName The name of the file.
     */
    public static void writeResult(TransactionsProcessor processor, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // Get legitimate and illegitimate player lists and sort them by ID
            List<Player> legitimatePlayers = processor.getCasino().getLegitPlayers();
            legitimatePlayers.sort(Comparator.comparing(Player::getPlayerId));

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
    public static void writeList(BufferedWriter bw, List<?> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            bw.write(String.valueOf(list.get(i)));
            if (i != list.size()-1)
                bw.write(", ");
        }
        emptyLine(bw);
    }

    /**
     * Writes two empty lines to a BufferedWriter.
     * @param bw The BufferedWriter.
     */
    public static void emptyLine(BufferedWriter bw) {
        for (int i = 0; i < 2; i++) {
            try {
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the Operation corresponding to a string.
     * @param action The string.
     * @return The Operation.
     * @throws IllegalArgumentException If the string does not correspond to a valid Operation.
     */
    public static Operation getOperation(String action) {
        return switch (action) {
            case "BET" -> Operation.BET;
            case "DEPOSIT" -> Operation.DEPOSIT;
            case "WITHDRAW" -> Operation.WITHDRAW;
            default -> throw new IllegalArgumentException("Unsupported action: " + action);
        };
    }
}


