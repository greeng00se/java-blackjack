package blackjack.domain.player;

import static blackjack.domain.card.Rank.ACE;
import static blackjack.domain.card.Rank.JACK;
import static blackjack.domain.card.Rank.SEVEN;
import static blackjack.domain.card.Rank.SIX;
import static blackjack.domain.card.Shape.CLOVER;
import static blackjack.domain.card.Shape.DIAMOND;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.Hand;
import blackjack.domain.card.Shape;
import blackjack.util.FixedDeck;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class DealerTest {

    @Test
    void 딜러를_생성한다() {
        final Dealer dealer = Dealer.create();

        assertThat(dealer.getName()).isEqualTo("딜러");
    }

    @Test
    void 게임_시작_시_카드를_뽑는다() {
        final Dealer dealer = Dealer.create();
        final Deck deck = new FixedDeck(List.of(
                new Card(ACE, DIAMOND)
        ));

        dealer.initialDraw(deck);

        assertThat(dealer.getCardLetters()).containsExactly("A다이아몬드");
    }

    @Test
    void 카드를_뽑는다() {
        final Dealer dealer = Dealer.create();
        final Deck deck = new FixedDeck(List.of(
                new Card(ACE, DIAMOND),
                new Card(JACK, CLOVER)
        ));
        dealer.initialDraw(deck);

        dealer.draw(deck);

        assertThat(dealer.getCardLetters()).containsExactly("A다이아몬드", "J클로버");
    }

    @ParameterizedTest(name = "카드를 뽑을 수 있는지 확인한다. 입력: {0}, 결과: {1}")
    @MethodSource("isDrawableSource")
    void 카드를_뽑을_수_있는지_확인한다(final List<Card> cards, final boolean result) {
        final Dealer dealer = Dealer.create();
        final Deck deck = new FixedDeck(cards);
        dealer.initialDraw(deck);
        dealer.draw(deck);

        assertThat(dealer.isDrawable()).isEqualTo(result);
    }

    static Stream<Arguments> isDrawableSource() {
        return Stream.of(
                Arguments.of(List.of(new Card(JACK, CLOVER), new Card(SIX, CLOVER)), true),
                Arguments.of(List.of(new Card(JACK, CLOVER), new Card(SEVEN, CLOVER)), false)
        );
    }

    @Test
    void 딜러인지_확인한다() {
        final Dealer dealer = Dealer.create();

        assertThat(dealer.isDealer()).isTrue();
    }

    @Test
    void 점수를_반환한다() {
        final Dealer dealer = Dealer.create();
        final Deck deck = new FixedDeck(List.of(
                new Card(ACE, Shape.DIAMOND)
        ));
        dealer.draw(deck);

        assertThat(dealer.calculateScore()).isEqualTo(11);
    }

    @Test
    void 카드를_더_뽑을_수_없는_상태로_변경한다() {
        final Dealer dealer = Dealer.create();

        dealer.stay();

        assertThat(dealer.isDrawable()).isFalse();
    }

    @Test
    void 딜러의_카드수를_반환한다() {
        final Dealer dealer = Dealer.create();
        final Deck deck = new FixedDeck(List.of(
                new Card(ACE, DIAMOND)
        ));

        dealer.draw(deck);

        assertThat(dealer.getCardCount()).isEqualTo(1);
    }

    @Test
    void 핸드를_반환한다() {
        final Dealer dealer = Dealer.create();

        final Hand hand = dealer.getHand();

        assertThat(hand.calculateScore()).isEqualTo(0);
    }
}

