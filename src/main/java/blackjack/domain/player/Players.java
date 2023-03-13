package blackjack.domain.player;

import static java.util.stream.Collectors.toList;

import blackjack.domain.card.Deck;
import blackjack.domain.card.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class Players {
    private static final int COUNT_LOWER_BOUND = 1;
    private static final int COUNT_UPPER_BOUND = 6;
    private static final int DEALER_COUNT = 1;

    private final List<Player> players;

    private Players(final List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public static Players create() {
        return new Players(List.of(Dealer.create()));
    }

    public void addPlayers(final List<String> names) {
        validateDuplicate(names);
        validatePlayerCount(players.size() + names.size() - DEALER_COUNT);
        final List<Player> players = names.stream()
                .map(Gambler::create)
                .collect(toList());
        this.players.addAll(players);
    }

    private void validateDuplicate(final List<String> names) {
        if (isNameDuplicate(names)) {
            throw new IllegalArgumentException("플레이어의 이름이 중복될 수 없습니다. 입력값:" + names);
        }
    }

    private boolean isNameDuplicate(final List<String> names) {
        final List<String> nonDuplicatedNames = names.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(toList());
        return names.size() != nonDuplicatedNames.size();
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

    public void initialDraw(final Deck deck) {
        for (Player player : players) {
            player.initialDraw(deck);
        }
    }

    public void drawToDealer(final Deck deck) {
        final Player dealer = getDealer();
        while (dealer.isDrawable()) {
            dealer.draw(deck);
        }
    }

    public void drawTo(final Name name, final Deck deck) {
        final Player player = findPlayerBy(name);
        player.draw(deck);
    }

    private Player findPlayerBy(final Name name) {
        return players.stream()
                .filter(player -> player.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 플레이어가 존재하지 않습니다."));
    }

    public void stay(final Name name) {
        final Player player = findPlayerBy(name);
        player.stay();
    }

    public boolean isExistDrawablePlayer() {
        return gamblers().stream()
                .anyMatch(Player::isDrawable);
    }

    private List<Player> gamblers() {
        return players.stream()
                .filter(player -> !player.isDealer())
                .collect(toList());
    }

    public Player findDrawablePlayer() {
        return gamblers().stream()
                .filter(Player::isDrawable)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("카드를 뽑을 수 있는 플레이어가 존재하지 않습니다."));
    }

    public Map<Player, Result> play() {
        final Map<Player, Result> result = new LinkedHashMap<>();
        final Player dealer = getDealer();
        for (Player player : gamblers()) {
            result.put(player, player.play(dealer.hand()));
        }
        return result;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getDealer() {
        return players.stream()
                .filter(Player::isDealer)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("딜러가 존재하지 않습니다."));
    }

    public List<Player> getGamblers() {
        return gamblers();
    }
}
