import java.math.BigDecimal;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final BigDecimal rateA;
    private final BigDecimal rateB;
    private final String result;

    public Match(UUID id, BigDecimal rateA, BigDecimal rateB, String result) {
        this.id = id;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getRateA() {
        return rateA;
    }

    public BigDecimal getRateB() {
        return rateB;
    }

    public String getResult() {
        return result;
    }
    public BigDecimal getMatchWinnerRate() {
        if (result.equals("A"))
            return rateA;
        if (result.equals("B"))
            return rateB;
        return new BigDecimal(0);
    }
}
