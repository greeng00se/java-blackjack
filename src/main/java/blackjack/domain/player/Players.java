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

public final class Players {

    private static final int COUNT_LOWER_BOUND = 1;
    private static final int COUNT_UPPER_BOUND = 6;
    private static final int DEALER_COUNT = 1;
    private static final String INVALID_NAME_MESSAGE = "해당 이름을 가진 플레이어가 존재하지 않습니다.";

    private final List<Player> players;

    private Players(final List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public static Players from(final List<String> names) {
        return new Players(generate(names));
    }

    public static Players create() {
        return new Players(List.of(Dealer.create()));
    }

    public void addPlayers(final List<String> names) {
        validateDuplicate(names);
        validatePlayerCount(players.size() + names.size() - DEALER_COUNT);
        for (String name : names) {
            final Gambler gambler = Gambler.create(name);
            players.add(gambler);
        }
    }

    private void validateDuplicate(final List<String> names) {
        if (isNameDuplicate(names)) {
            throw new IllegalArgumentException("플레이어의 이름이 중복될 수 없습니다. 입력값:" + names);
        }
    }

    private boolean isNameDuplicate(final List<String> names) {
        return names.size() != new HashSet<>(names).size();
    }

    private void validatePlayerCount(final int count) {
        if (isValidNameCount(count)) {
            throw new IllegalArgumentException(
                    "플레이어는 최소 " + COUNT_LOWER_BOUND + "명 이상, 최대 " + COUNT_UPPER_BOUND + "명 이하여야 합니다. 입력값:" + count
            );
        }
    }

    private boolean isValidNameCount(final int count) {
        return count < COUNT_LOWER_BOUND || COUNT_UPPER_BOUND < count;
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

    public void drawTo(final Name name, final Deck deck) {
        final Player player = findPlayerBy(name);
        player.draw(deck);
    }

    private Player findPlayerBy(final Name name) {
        return players.stream()
                .filter(player -> player.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_NAME_MESSAGE));
    }

    public void stay(final Name name) {
        final Player player = findPlayerBy(name);
        player.stay();
    }

    public void stay(final Player player) {
        player.stay();
    }

    public Map<Player, Result> play() {
        final Map<Player, Result> result = new LinkedHashMap<>();
        for (Player player : getGamblers()) {
            result.put(player, player.play(getDealer().hand));
        }
        return result;
    }

    public List<Player> getGamblers() {
        return players.stream()
                .filter(player -> !player.isDealer())
                .collect(Collectors.toList());
    }

    public Dealer getDealer() {
        return (Dealer) players.stream()
                .filter(Player::isDealer)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("딜러가 존재하지 않습니다."));
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
