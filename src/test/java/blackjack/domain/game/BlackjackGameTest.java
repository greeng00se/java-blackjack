package blackjack.domain.game;

import static blackjack.util.CardFixture.ACE_SPADE;
import static blackjack.util.CardFixture.JACK_SPADE;
import static blackjack.util.CardFixture.KING_SPADE;
import static blackjack.util.CardFixture.SIX_HEART;
import static blackjack.util.CardFixture.TWO_HEART;
import static blackjack.util.CardFixture.TWO_SPADE;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Deck;
import blackjack.domain.player.Name;
import blackjack.domain.player.Player;
import blackjack.util.FixedDeck;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class BlackjackGameTest {

    @Test
    void 플레이어가_게임에_참가한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();

        blackjackGame.addPlayers(List.of("허브", "후추"));

        assertThat(blackjackGame.getPlayers())
                .extracting(Player::getNameValue)
                .containsExactly("딜러", "허브", "후추");
    }

    @Test
    void 딜러가_카드를_뽑는다() {
        final Deck deck = new FixedDeck(JACK_SPADE, TWO_SPADE, TWO_HEART, SIX_HEART);
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.initialDraw(deck);

        blackjackGame.drawToDealer(deck);

        final Player dealer = blackjackGame.getDealer();
        assertThat(dealer.getSymbols().size()).isEqualTo(4);
    }

    @Test
    void 플레이어에게_카드를_뽑게한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        final Deck deck = new FixedDeck(JACK_SPADE);
        blackjackGame.addPlayers(List.of("허브"));

        blackjackGame.drawTo(Name.from("허브"), deck);

        final int playerIndex = 1;
        final Player player = blackjackGame.getPlayers().get(playerIndex);
        assertThat(player.getSymbols()).containsExactly("J스페이드");
    }

    @Test
    void 플레이어가_카드를_더_뽑을_수_없는_상태로_변경한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("허브"));
        final Player player = blackjackGame.getPlayers().get(1);

        blackjackGame.stay(Name.from("허브"));

        assertThat(player.isDrawable()).isFalse();
    }

    @Test
    void 카드를_뽑을_수_있는_플레이어들이_존재하는지_확인한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("후추"));

        final boolean result = blackjackGame.isExistDrawablePlayer();

        assertThat(result).isTrue();
    }

    @Test
    void 카드를_뽑을_수_있는_플레이어들이_없다면_거짓을_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();

        final boolean result = blackjackGame.isExistDrawablePlayer();

        assertThat(result).isFalse();
    }

    @Test
    void 카드를_뽑을_수_있는_플레이어를_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("후추"));

        final Player player = blackjackGame.findDrawablePlayer();

        assertThat(player.getNameValue()).isEqualTo("후추");
    }

    @Test
    void 게임_결과를_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("히브"));
        final Player player = blackjackGame.getGamblers().get(0);
        blackjackGame.addBet(player, 1000);
        final Deck deck = new FixedDeck(JACK_SPADE, TWO_SPADE, ACE_SPADE, KING_SPADE);
        blackjackGame.initialDraw(deck);

        final Bets result = blackjackGame.play();

        final Map<Player, Money> bets = result.getBets();
        assertThat(bets.get(player)).isEqualTo(Money.initialBet(1500));
    }

    @Test
    void 플레이어들을_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("허브", "후추"));

        final List<Player> players = blackjackGame.getPlayers();

        assertThat(players)
                .extracting(Player::getNameValue)
                .containsExactly("딜러", "허브", "후추");
    }

    @Test
    void 딜러를_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("허브"));

        final Player dealer = blackjackGame.getDealer();

        assertThat(dealer.isDealer()).isTrue();
    }

    @Test
    void 모든_겜블러의_이름을_반환한다() {
        final BlackjackGame blackjackGame = new BlackjackGame();
        blackjackGame.addPlayers(List.of("허브", "후추"));

        final List<Player> result = blackjackGame.getGamblers();

        assertThat(result)
                .extracting(Player::getNameValue)
                .containsExactly("허브", "후추");
    }
}
