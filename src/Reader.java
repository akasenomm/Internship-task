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

            UUID matchId = UUID.fromString(matchData[0]);
            BigDecimal rateA = new BigDecimal(matchData[1]);
            BigDecimal rateB = new BigDecimal(matchData[2]);
            String result = matchData[3];

            Match match = new Match(matchId, rateA, rateB, result);
            matchesMap.put(matchId, match);
        }
        return matchesMap;
    }
    public static List<String> readPlayerDataV3(String path) throws IOException {
        List<String> data = readAllLines(Paths.get(path));
        for (int i = 1; i < data.size()-1; i++) {
            System.out.println(data.get(i));
        }
        return new ArrayList<String>();
    }
    public static Queue<Transaction> readPlayerDataV2(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        Queue<Transaction> playerData = new LinkedList<>();

        String textRow;
        while ((textRow = br.readLine()) != null) {
            Transaction transaction = null;
            String[] operationData = textRow.trim().split(",");

            UUID playerId = UUID.fromString(operationData[0]);
            String operationName = operationData[1];
            if (operationName.equals("DEPOSIT") || operationName.equals("WITHDRAW")) {
                int amount = Integer.parseInt(operationData[3]);
                transaction = new Transaction(playerId, operationName, amount);
            } else if (operationName.equals("BET")) {
                UUID matchId = UUID.fromString(operationData[2]);
                int amount = Integer.parseInt(operationData[3]);
                String betSide = operationData[4];
                transaction = new Transaction(playerId, operationName, matchId, amount, betSide );
            } else {
                System.out.println("Invalid operation");
            }
            playerData.add(transaction);
        }
        return playerData;

    }

    public static HashMap<UUID, Queue<Transaction>> readPlayerData(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        HashMap<UUID, Queue<Transaction>> playerData = new HashMap<>();

        String textRow;
        while ((textRow = br.readLine()) != null) {
            String[] transactionData = textRow.trim().split(",");
            Transaction transaction;

            UUID playerId = UUID.fromString(transactionData[0]);
            String type = transactionData[1];
            int amount = Integer.parseInt(transactionData[3]);

            if (type.equals("DEPOSIT") || type.equals("WITHDRAW")) {
                transaction = new Transaction(type, amount);
            } else {
                UUID matchId = UUID.fromString(transactionData[2]);
                String betSide = transactionData[4];
                transaction = new Transaction(type, matchId, amount, betSide);
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
}
