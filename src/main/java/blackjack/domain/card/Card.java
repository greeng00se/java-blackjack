package blackjack.domain.card;

public class Card {
    private final Rank rank;
    private final Shape shape;

    public Card(final Rank rank, final Shape shape) {
        this.rank = rank;
        this.shape = shape;
    }

    public boolean isAce() {
        return rank.isAce();
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", shape=" + shape +
                '}';
    }

    public int getScore() {
        return rank.getScore();
    }

    public String getLetter() {
        return rank.getLetter() + shape.getLetter();
    }
}
