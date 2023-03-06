package blackjack.domain.player;

import blackjack.domain.card.Deck;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Players {

    static final String DUPLICATE_NAMES_MESSAGE = "플레이어의 이름이 중복될 수 없습니다. 입력값:";
    private static final int NAME_COUNT_LOWER_BOUND = 1;
    private static final int NAME_COUNT_UPPER_BOUND = 6;
    static final String INVALID_NAME_COUNT =
            "플레이어는 최소 " + NAME_COUNT_LOWER_BOUND + "명 이상, 최대 " + NAME_COUNT_UPPER_BOUND + "명 이하여야 합니다. 입력값:";
    private static final String INVALID_DEALER_MESSAGE = "딜러가 존재하지 않습니다.";

    private final List<Player> players;

    private Players(final List<Player> players) {
        this.players = players;
    }

    public static Players from(final List<String> names) {
        validateDuplicate(names);
        validateNameCount(names);
        return new Players(generate(names));
    }

    private static void validateDuplicate(final List<String> names) {
        if (isNameDuplicate(names)) {
            throw new IllegalArgumentException(DUPLICATE_NAMES_MESSAGE + names);
        }
    }

    private static boolean isNameDuplicate(final List<String> names) {
        return names.size() != new HashSet<>(names).size();
    }

    private static void validateNameCount(final List<String> names) {
        if (isValidNameCount(names)) {
            throw new IllegalArgumentException(INVALID_NAME_COUNT + names.size());
        }
    }

    private static boolean isValidNameCount(final List<String> names) {
        return names.size() < NAME_COUNT_LOWER_BOUND || NAME_COUNT_UPPER_BOUND < names.size();
    }

    private static List<Player> generate(final List<String> names) {
        final List<Player> players = new ArrayList<>();
        players.add(Dealer.create());
        for (String name : names) {
            players.add(Gambler.create(name));
        }
        return players;
    }

    public void initialDraw(final Deck deck) {
        for (Player player : players) {
            player.initialDraw(deck);
        }
    }

    public void drawToDealer(final Deck deck) {
        final Dealer dealer = getDealer();
        while (dealer.isDrawable()) {
            dealer.draw(deck);
        }
    }

    public void drawTo(final Player player, final Deck deck) {
        player.draw(deck);
    }

    public void stay(final Player player) {
        player.stay();
    }

    public Map<Player, Result> play() {
        final Map<Player, Result> result = new LinkedHashMap<>();
        for (Player player : getGambler()) {
            result.put(player, player.play(getDealer().hand));
        }
        return result;
    }

    private List<Player> getGambler() {
        return players.stream()
                .filter(player -> !player.isDealer())
                .collect(Collectors.toList());
    }

    public Dealer getDealer() {
        return (Dealer) players.stream()
                .filter(Player::isDealer)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(INVALID_DEALER_MESSAGE));
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
