
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllLines;
public class Reader {

    public Reader() {
    }

    public static HashMap<UUID, Match> readMatches(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

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
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        HashMap<UUID, Queue<Transaction>> playerData = new HashMap<>();

        String textRow;
        while ((textRow = br.readLine()) != null) {
            String[] transactionData = textRow.trim().split(",");

            UUID playerId = UUID.fromString(transactionData[0]);
            String playerOperation = transactionData[1];
            Operation operation = getOperation(playerOperation);
            int amount = Integer.parseInt(transactionData[3]);

            Transaction transaction;
            if (operation == Operation.DEPOSIT || operation == Operation.WITHDRAW) {
                transaction = new Transaction(operation, amount);
            } else {
                String betSide = transactionData[4];
                UUID matchId = UUID.fromString(transactionData[2]);
                Match match = matchMap.get(matchId);
                transaction = new Transaction(operation, match, amount, betSide);
            }

            if (playerData.containsKey(playerId)) {
                playerData.get(playerId).add(transaction);
            } else {
                Queue<Transaction> newPlayerData = new LinkedList<>();
                newPlayerData.add(transaction);
                playerData.put(playerId, newPlayerData);
            }
        }

        return playerData;
    }
    private static Operation getOperation(String action) {
        switch (action) {
            case "BET" -> {
                return Operation.BET;
            }
            case "DEPOSIT" -> {
                return Operation.DEPOSIT;
            }
            case "WITHDRAW" -> {
                return Operation.WITHDRAW;
            }
            default -> {
                // Handle unknown or unsupported actions
                throw new IllegalArgumentException("Unsupported action: " + action);
            }
        }
    }

}


