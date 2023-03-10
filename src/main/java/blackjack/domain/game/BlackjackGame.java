package blackjack.domain.game;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Dealer;
import blackjack.domain.player.Name;
import blackjack.domain.player.Player;
import blackjack.domain.player.Players;
import java.util.List;
import java.util.Map;

public final class BlackjackGame {
    private final Players players;
    private final Bets bets;

    public BlackjackGame() {
        this.players = Players.create();
        this.bets = new Bets();
    }

    public void addPlayers(final List<String> names) {
        players.addPlayers(names);
    }

    public void addBets(final Map<Player, Money> bets) {
        this.bets.addBets(bets);
    }

    public void initialDraw(final Deck deck) {
        players.initialDraw(deck);
    }

    public void drawToDealer(final Deck deck) {
        players.drawToDealer(deck);
    }

    public void drawTo(final Name name, final Deck deck) {
        players.drawTo(name, deck);
    }

    public void drawTo(final Player player, Deck deck) {
        players.drawTo(player, deck);
    }

    public void stay(final Player player) {
        players.stay(player);
    }

    public Bets play() {
        bets.calculateProfit(players.play());
        return bets;
    }

    public List<Player> getPlayers() {
        return players.getPlayers();
    }

    public List<Player> getGambler() {
        return players.getGamblers();
    }

    public Dealer getDealer() {
        return players.getDealer();
    }

    public List<Name> getGamblerNames() {
        return players.getGamblerNames();
    }
}
