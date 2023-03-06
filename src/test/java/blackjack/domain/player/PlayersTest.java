package blackjack.domain.player;

import static blackjack.domain.player.Players.from;
import static blackjack.domain.player.Result.PUSH;
import static blackjack.domain.player.Result.WIN;
import static blackjack.util.CardFixtures.ACE_DIAMOND;
import static blackjack.util.CardFixtures.ACE_SPADE;
import static blackjack.util.CardFixtures.EIGHT_SPADE;
import static blackjack.util.CardFixtures.JACK_CLOVER;
import static blackjack.util.CardFixtures.JACK_SPADE;
import static blackjack.util.CardFixtures.KING_HEART;
import static blackjack.util.CardFixtures.NINE_CLOVER;
import static blackjack.util.CardFixtures.NINE_HEART;
import static blackjack.util.CardFixtures.NINE_SPADE;
import static blackjack.util.CardFixtures.SEVEN_SPADE;
import static blackjack.util.CardFixtures.TWO_SPADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Deck;
import blackjack.util.FixedDeck;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class PlayersTest {

    @Test
    void 입력받은_플레이어의_이름이_중복되는_경우_예외를_던진다() {
        final List<String> names = List.of("name", "name");

        assertThatThrownBy(() -> from(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Players.DUPLICATE_NAMES_MESSAGE + names);
    }

    @ParameterizedTest(name = "입력받은 플레이어가 {0}명인 경우 예외를 던진다.")
    @ValueSource(ints = {0, 7})
    void 입력받은_플레이어가_1명_미만_6명_초과인_경우_예외를_던진다(final int count) {
        final List<String> names = IntStream.range(0, count)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        assertThatThrownBy(() -> from(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Players.INVALID_NAME_COUNT + count);
    }

    @Test
    void 플레이어들이_정상_생성된다() {
        final List<String> names = List.of("후추", "허브");

        final Players players = from(names);

        assertThat(players.getPlayers()).hasSize(3);
    }

    @Test
    void 플레이어들이_게임_시작_시_카드를_뽑는다() {
        final List<String> names = List.of("후추", "허브");
        final Deck deck = new FixedDeck(ACE_SPADE, KING_HEART, JACK_SPADE, TWO_SPADE, EIGHT_SPADE, JACK_SPADE);
        final Players players = from(names);

        players.initialDraw(deck);

        assertThat(players.getPlayers())
                .extracting(Player::calculateScore)
                .containsExactly(21, 12, 18);
    }

    @Test
    void 플레이어에게_카드를_뽑게한다() {
        final Players players = Players.from(List.of("허브"));
        final Deck deck = new FixedDeck(JACK_SPADE);
        final int gamblerIndex = 1;
        final Player player = players.getPlayers().get(gamblerIndex);

        players.drawTo(player, deck);

        assertThat(player.getCardLetters()).containsExactly("J스페이드");
    }

    @Test
    void 플레이어가_카드를_더_뽑을_수_없는_상태로_변경한다() {
        final Players players = Players.from(List.of("허브"));
        final int gamblerIndex = 1;
        final Player player = players.getPlayers().get(gamblerIndex);

        players.stay(player);

        assertThat(player.isDrawable()).isFalse();
    }

    @Test
    void 플레이어들을_반환한다() {
        final List<String> names = List.of("후추", "허브");
        final Players players = from(names);

        List<Player> result = players.getPlayers();

        assertThat(result).extracting(Player::getName)
                .containsExactly("딜러", "후추", "허브");
    }

    @Test
    void 딜러를_반환한다() {
        final List<String> names = List.of("후추", "허브");
        final Players players = from(names);

        final Player player = players.getDealer();

        assertThat(player.getName()).isEqualTo("딜러");
    }

    @Test
    void 딜러가_카드를_가능할_때_까지_뽑는다() {
        final List<String> names = List.of("후추");
        final Deck deck = new FixedDeck(ACE_DIAMOND, TWO_SPADE, JACK_CLOVER, TWO_SPADE, EIGHT_SPADE);
        final Players players = from(names);
        players.initialDraw(deck);

        players.drawToDealer(deck);

        assertThat(players.getDealer().calculateScore()).isEqualTo(21);
    }

    @Test
    void 게임_결과를_반환한다() {
        final List<String> names = List.of("후추", "허브");
        final Deck deck = new FixedDeck(ACE_DIAMOND, SEVEN_SPADE, JACK_CLOVER, NINE_SPADE, NINE_HEART, NINE_CLOVER);
        final Players players = from(names);
        players.initialDraw(deck);

        Map<Player, Result> result = players.play();

        assertThat(result.values()).containsExactly(WIN, PUSH);
    }
}
