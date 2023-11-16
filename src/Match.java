import java.math.BigDecimal;
import java.util.UUID;

public record Match(UUID id, BigDecimal rateA, BigDecimal rateB, String result) {
    public BigDecimal getMatchWinnerRate() {
        if (result.equals("A"))
            return rateA;
        if (result.equals("B"))
            return rateB;
        return new BigDecimal(-1);
    }
}
