import java.math.BigDecimal;
import java.util.UUID;

public record Match(UUID id, BigDecimal rateA, BigDecimal rateB, String result) {
    public BigDecimal getMatchWinnerRate() {
        if (result.equals("A"))
            return rateA;
        if (result.equals("B"))
            return rateB;

        System.out.println("Invalid match result in match: " + this);
        return new BigDecimal(0);
    }

    public UUID getMatchId() {
        return id;
    }
}
