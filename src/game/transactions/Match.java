package game.transactions;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A record representing a match.
 *
 * @param id     The unique identifier for the match.
 * @param rateA  The rate for side A
 * @param rateB  The rate for side B
 * @param result The result of the match.
 */
public record Match(UUID id, BigDecimal rateA, BigDecimal rateB, String result) {

    /**
     * Returns the rate of the winner side of the match.
     *
     * @return The rate of the winner side. If the result is invalid, returns 0.
     */
    public BigDecimal getMatchWinnerRate() {
        if (result.equals("A"))
            return rateA;
        if (result.equals("B"))
            return rateB;

        // If the result is neither "A" nor "B", it's invalid
        System.err.println("Invalid match result in match: " + this);
        return BigDecimal.ZERO;
    }
}
