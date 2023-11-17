import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class IO {

    public static HashMap<UUID, Match> readMatches(String path) {
        HashMap<UUID, Match> matchesMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read each line in the file
            String line;
            while ((line = br.readLine()) != null) {
                String[] matchData = line.trim().split(",");

                // Error handling for data format issues
                if (matchData.length != 4) {
                    System.out.println("Invalid data format in line: " + line);
                    continue;
                }

                // Parse data from the line
                UUID matchId = UUID.fromString(matchData[0]);
                BigDecimal rateA = new BigDecimal(matchData[1]);
                BigDecimal rateB = new BigDecimal(matchData[2]);
                String result = matchData[3];

                // Create Match object and add it to the map
                Match match = new Match(matchId, rateA, rateB, result);
                matchesMap.put(matchId, match);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return matchesMap;
    }



    public static Queue<Transaction> readPlayerData(String path, HashMap<UUID, Match> matchMap) {
        HashMap<UUID, Player> playersMap = new HashMap<>();
        Queue<Transaction> playerTransactions = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read each line in the file
            String line;
            while ((line = br.readLine()) != null) {
                Transaction transaction = parseTransaction(line, playersMap, matchMap);
                if (transaction != null)
                    playerTransactions.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return playerTransactions;
    }

    private static Transaction parseTransaction(String line, HashMap<UUID, Player> playersMap, HashMap<UUID, Match> matchMap) {
        String[] transactionData = line.trim().split(",");

        // Error handling for data format issues
        if (transactionData.length < 4) {
            System.out.println("Invalid data format in line: " + line);
            return null;
        }

        // Parse data from the line
        UUID playerId = UUID.fromString(transactionData[0]);
        Operation operation = getOperation(transactionData[1]);
        int amount = Integer.parseInt(transactionData[3]);
        Match match = null;
        String betSide = null;

        // Match and bet side are only for BET operation
        if (operation == Operation.BET) {
            UUID matchId = UUID.fromString(transactionData[2]);
            match = matchMap.get(matchId);
            betSide = transactionData[4];
        }

        Player player = playersMap.getOrDefault(playerId, new Player(playerId));
        playersMap.put(playerId, player);

        return new Transaction(player, operation, match, amount, betSide);
    }


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

    private static void writeList(BufferedWriter bw, List<?> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            bw.write(String.valueOf(list.get(i)));
            if (i != list.size()-1) {
                bw.write(", ");
            }
        }
        emptyLine(bw);
    }


    public static void emptyLine(BufferedWriter bw) {
        for (int i = 0; i < 2; i++) {
            try {
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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


