package blackjack.domain.game;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Players;

public class BlackjackGame {
    private final Players players;

    public BlackjackGame(final Players players) {
        this.players = players;
    }

    public void drawToDealer(final Deck deck) {
        players.drawToDealer(deck);
    }

    public BlackjackGameResult play() {
        return new BlackjackGameResult(players.play());
    }

    public Players getPlayers() {
        return players;
    }
}
