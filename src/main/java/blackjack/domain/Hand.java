package blackjack.domain;

import java.util.List;

public class Hand {
    private final Cards cards;
    private State state;

    public Hand() {
        this.cards = new Cards();
        this.state = State.PLAY;
    }

    public void add(final Card card) {
        cards.add(card);
        state = State.calculateState(cards);
    }

    public Result play(final Hand other) {
        if (state.isBlackjack()) {
            return playWithBlackjack(other);
        }
        if (state.isBust()) {
            return playWithBust(other);
        }
        return playWithScore(other);
    }

    private Result playWithBlackjack(final Hand other) {
        if (other.state.isBlackjack()) {
            return Result.DRAW;
        }
        return Result.WIN;
    }

    private Result playWithBust(final Hand other) {
        if (other.state.isBust()) {
            return Result.DRAW;
        }
        return Result.LOSE;
    }

    private Result playWithScore(final Hand other) {
        if (cards.calculateTotalScore() > other.cards.calculateTotalScore() || other.state.isBust()) {
            return Result.WIN;
        }
        if (cards.calculateTotalScore() < other.cards.calculateTotalScore() && other.state.isNotBust()) {
            return Result.LOSE;
        }
        return Result.DRAW;
    }
    
    public boolean isPlayable() {
        return state.isPlayable();
    }

    public int calculateScore() {
        return cards.calculateTotalScore();
    }

    public void stay() {
        state = State.STOP;
    }

    public List<String> getCardLetters() {
        return cards.getCardLetters();
    }
}
