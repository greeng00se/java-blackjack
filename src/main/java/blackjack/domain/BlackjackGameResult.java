package blackjack.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlackjackGameResult {
    private final Map<Player, Result> result;

    public BlackjackGameResult(final Map<Player, Result> result) {
        this.result = result;
    }

    public int calculateDealerWinCount() {
        return Collections.frequency(result.values(), Result.LOSE);
    }

    public int calculateDealerDrawCount() {
        return Collections.frequency(result.values(), Result.DRAW);
    }

    public int calculateDealerLoseCount() {
        return Collections.frequency(result.values(), Result.WIN);
    }

    public Map<Player, Result> getResult() {
        return new LinkedHashMap<>(result);
    }
}
