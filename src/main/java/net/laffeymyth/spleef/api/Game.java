package net.laffeymyth.spleef.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Game {
    private final Deque<State> stateQueue = new ArrayDeque<>();
    private final Map<Player, GamerState> gamerStateMap = new HashMap<>();
    private final Set<Player> winners = new HashSet<>();
    private final ComponentLocalizationService lang;
    private final int maxPlayers;
    private final int startTime; //в секундах
    private final Location spectatorLocation;
    private State currentState;
    private boolean firstState = true;

    @Setter
    private GameMode gameMode = GameMode.CREATIVE; //TODO убрать, это для теста uwu

    public List<Player> getGamers() {
        return gamerStateMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(GamerState.GAMER))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Player> getSpectators() {
        return gamerStateMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(GamerState.SPECTATOR))
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean isSpectator(Player player) {
        return getSpectators().contains(player);
    }

    public void putPlayer(Player player, GamerState gamerState) {
        gamerStateMap.put(player, gamerState);

        if (!player.isOnline()) {
            return;
        }

        switch (gamerState) {
            case GAMER -> player.setGameMode(gameMode);
            case SPECTATOR -> {
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(spectatorLocation);

                player.sendTitlePart(TitlePart.TITLE, lang.getMessage("spectator_title", "ru"));
                player.sendTitlePart(TitlePart.SUBTITLE, lang.getMessage("spectator_sub_title", "ru"));

                normalize(player);

                //TODO: добавить scoreboard)
            }
        }
    }

    public void normalize(Player player) {
        player.clearActivePotionEffects();
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    public void removePlayer(Player player) {
        gamerStateMap.remove(player);
    }

    public Component getPrefix(String language) {
        return lang.getMessage("game_prefix", language);
    }

    public Component getServerName(String language) {
        return Component.text("spleef-1");
    }

    public Component getServerMap(String language) {
        return Component.text("xz");
    }

    public Component getBoardTitle(String language) {
        return lang.getMessage("game_board_title", language);
    }

    public void nextState() {
        if (stateQueue.isEmpty()) {
            throw new RuntimeException("очередь пуста");
        }

        State state = stateQueue.pop();

        if (firstState) {
            firstState = false;
            state.start();
            currentState = state;
        } else {
            currentState.end();
            currentState = state;
            state.start();
        }
    }
}
