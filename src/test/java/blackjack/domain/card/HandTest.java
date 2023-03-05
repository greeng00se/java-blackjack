package blackjack.domain.card;

import static blackjack.domain.card.Rank.ACE;
import static blackjack.domain.card.Rank.EIGHT;
import static blackjack.domain.card.Rank.JACK;
import static blackjack.domain.card.Rank.KING;
import static blackjack.domain.card.Rank.SEVEN;
import static blackjack.domain.card.Rank.SIX;
import static blackjack.domain.card.Rank.TWO;
import static blackjack.domain.card.Shape.HEART;
import static blackjack.domain.card.Shape.SPADE;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.player.Result;
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
public class HandTest {

    @ParameterizedTest(name = "승패를 구한다. 갬블러: {0}, 딜러: {1}, 결과값: {2}")
    @MethodSource("playBlackjackSource")
    void 승패를_구한다(final List<Rank> ranks, final List<Rank> dealerRanks, final Result expectedResult) {
        final Hand hand = generateHand(ranks);
        final Hand dealerHand = generateHand(dealerRanks);

        final Result result = hand.play(dealerHand);

        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> playBlackjackSource() {
        return Stream.of(
                Arguments.of(List.of(ACE, JACK), List.of(ACE, JACK), Result.DRAW),
                Arguments.of(List.of(ACE, JACK), List.of(JACK, JACK), Result.WIN),
                Arguments.of(List.of(ACE, JACK), List.of(JACK, SIX, JACK), Result.WIN),
                Arguments.of(List.of(JACK, JACK), List.of(JACK, ACE), Result.LOSE),
                Arguments.of(List.of(JACK, JACK, JACK), List.of(JACK, ACE), Result.LOSE),
                Arguments.of(List.of(JACK, JACK), List.of(JACK, EIGHT), Result.WIN),
                Arguments.of(List.of(JACK, JACK), List.of(JACK, JACK), Result.DRAW),
                Arguments.of(List.of(JACK, SEVEN), List.of(JACK, JACK), Result.LOSE),
                Arguments.of(List.of(JACK, SEVEN), List.of(JACK, SIX, JACK), Result.WIN),
                Arguments.of(List.of(JACK, SEVEN, KING), List.of(JACK, SEVEN), Result.LOSE),
                Arguments.of(List.of(JACK, SIX, KING), List.of(JACK, SIX, KING), Result.LOSE)
        );
    }

    private Hand generateHand(List<Rank> ranks) {
        final Hand hand = new Hand();
        for (Rank rank : ranks) {
            hand.add(new Card(rank, SPADE));
        }
        return hand;
    }

    @Test
    void 카드가_추가된다() {
        final Hand hand = new Hand();

        hand.add(new Card(ACE, SPADE));

        assertThat(hand.getCardLetters()).containsExactly("A스페이드");
    }

    @ParameterizedTest(name = "카드를 뽑을 수 있는지 확인한다. 입력값: {0}, 결과값: {1}")
    @MethodSource("isPlayableSource")
    void 카드를_뽑을_수_있는지_확인한다(final List<Rank> ranks, final boolean result) {
        final Hand hand = generateHand(ranks);

        assertThat(hand.isPlayable()).isEqualTo(result);
    }

    static Stream<Arguments> isPlayableSource() {
        return Stream.of(
                Arguments.of(List.of(ACE, JACK), false),
                Arguments.of(List.of(JACK, JACK, TWO), false),
                Arguments.of(List.of(JACK, JACK), true)
        );
    }

    @Test
    void 점수_산정에_들어갈_결과값을_반환한다() {
        final Hand hand = new Hand();
        hand.add(new Card(ACE, HEART));

        assertThat(hand.calculateScore()).isEqualTo(11);
    }

    @Test
    void 카드를_더_뽑을_수_없는_상태로_변경한다() {
        final Hand hand = new Hand();

        hand.stay();

        assertThat(hand.isPlayable()).isFalse();
    }
}
