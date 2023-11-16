import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Read match data into hashmap.
        HashMap<UUID, Match> matchData = IORead.readMatches("resource/match_data2.txt");

        // Read player data (each row/transaction is one operation) into operations queue.
        HashMap<UUID, Queue<Transaction>> playerData = IORead.readPlayerData("resource/player_data2.txt", matchData);
        Validator validator = new Validator(playerData);
        validator.validatePlayers();
        System.out.println("??????????????????");
        IOWrite.writeResult(validator, "resultsProov1.txt");

    }
}
