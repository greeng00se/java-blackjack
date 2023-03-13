package blackjack.domain.card;

public class Blackjack extends Finished {
    protected Blackjack(final Cards cards) {
        super(cards);
    }

    @Override
    public Result play(final Hand other) {
        if (other.cards().isBlackjack()) {
            return Result.PUSH;
        }
        return Result.BLACKJACK_WIN;
    }
}
