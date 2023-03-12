package blackjack.domain.card;

import blackjack.domain.player.Result;

public final class Stay extends Finished {
    Stay(final Cards cards) {
        super(cards);
    }

    @Override
    public Result play(final Hand dealer) {
        final Cards dealerCards = dealer.cards();
        if (dealerCards.isBust() || cards().totalScore() > dealerCards.totalScore()) {
            return Result.WIN;
        }
        if (cards().totalScore() < dealerCards.totalScore()) {
            return Result.LOSE;
        }
        return Result.PUSH;
    }
}
