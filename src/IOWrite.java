import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IOWrite {
    public IOWrite() {
    }

    public static void writeResult(Validator validator, String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

        for (Player legitimatePlayer : validator.getLegitPlayers()) {
            bw.write(String.valueOf(legitimatePlayer));

        }
        bw.write("\n\n");

        for (Transaction illegalTransaction : validator.getIllegimatePlayerTransactions()) {
            bw.write(String.valueOf(illegalTransaction));
        }
        bw.write("\n\n");

        bw.write(String.valueOf(validator.getCasinoHostBalance()));
        bw.close();
    }
}
