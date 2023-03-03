package blackjack.domain;

import java.util.List;

public class Gambler implements Player {
    private static final int DRAW_COUNT = 2;

    private final Name name;
    private final Hand hand;

    public Gambler(final String name) {
        this.name = Name.from(name);
        this.hand = new Hand();
    }

    @Override
    public void initialDraw(final Deck deck) {
        for (int count = 0; count < DRAW_COUNT; count++) {
            hand.add(deck.draw());
        }
    }

    @Override
    public void draw(final Deck deck) {
        hand.add(deck.draw());
    }

    @Override
    public boolean isDrawable() {
        return hand.isPlayable();
    }

    @Override
    public boolean isDealer() {
        return false;
    }

    @Override
    public int calculateScore() {
        return hand.calculateScore();
    }

    @Override
    public void stay() {
        hand.stay();
    }

    @Override
    public Result play(final Hand hand) {
        return this.hand.play(hand);
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public List<String> getCardLetters() {
        return hand.getCardLetters();
    }
}
