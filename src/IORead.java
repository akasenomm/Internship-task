import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.readAllLines;
public class IORead {

    public IORead() {
    }

    public static HashMap<UUID, Match> readMatches(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        String textRow;
        HashMap<UUID, Match> matchesMap = new HashMap<>();
        // Condition holds true till
        // there are lines in the file
        while ((textRow = br.readLine()) != null) {
            String[] matchData = textRow.trim().split(",");

            // Read into variables
            UUID matchId = UUID.fromString(matchData[0]);
            BigDecimal rateA = new BigDecimal(matchData[1]);
            BigDecimal rateB = new BigDecimal(matchData[2]);
            String result = matchData[3];

            // Create Match object and add it to hashmap of Match objects
            Match match = new Match(matchId, rateA, rateB, result);
            matchesMap.put(matchId, match);
        }
        return matchesMap;
    }

    public static HashMap<UUID, Queue<Transaction>> readPlayerData(String path, HashMap<UUID, Match> matchMap) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        HashMap<UUID, Queue<Transaction>> playerTransactions = new HashMap<>();

        String textRow;
        while ((textRow = br.readLine()) != null) {
            String[] transactionData = textRow.trim().split(",");

            UUID playerId = UUID.fromString(transactionData[0]);
            Operation operation = getOperation(transactionData[1]);

            Transaction transaction = null;
            if (operation == Operation.DEPOSIT || operation == Operation.WITHDRAW)
                transaction = createNonBetTransaction(operation, transactionData);
            if (operation == Operation.BET)
                transaction = createBetTransaction(transactionData, matchMap);

            addPlayerTransaction(playerId, transaction, playerTransactions);
        }
        return playerTransactions;
    }
    public static Transaction createBetTransaction(String[] transactionData, HashMap<UUID, Match> matchMap) {
        int amount = Integer.parseInt(transactionData[3]);
        UUID matchId = UUID.fromString(transactionData[2]);
        Match match = matchMap.get(matchId);
        String betSide = transactionData[4];
        return new Transaction(Operation.BET, match, amount, betSide);
    }
    public static Transaction createNonBetTransaction(Operation operation, String[] transactionData) {
        int amount = Integer.parseInt(transactionData[3]);
        return new Transaction(operation, amount);
    }

    public static void addPlayerTransaction(UUID playerId, Transaction transaction, HashMap<UUID, Queue<Transaction>> playerData) {
        if (playerData.containsKey(playerId)) {
            playerData.get(playerId).add(transaction);
        } else {
            Queue<Transaction> newPlayerData = new LinkedList<>();
            newPlayerData.add(transaction);
            playerData.put(playerId, newPlayerData);
        }
    }
    private static Operation getOperation(String action) {
        return switch (action) {
            case "BET" -> Operation.BET;
            case "DEPOSIT" -> Operation.DEPOSIT;
            case "WITHDRAW" -> Operation.WITHDRAW;
            // Handle unknown or unsupported player operation
            default -> throw new IllegalArgumentException("Unsupported action: " + action);
        };
    }

}


