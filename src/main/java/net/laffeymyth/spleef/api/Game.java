package net.laffeymyth.spleef.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Game {
    private final Map<Player, GamerState> gamerStateMap = new HashMap<>();
    private final ComponentLocalizationService lang;
    private final int maxPlayers;
    private final int startTime; //в секундах
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

    public void putPlayer(Player player, GamerState gamerState) {
        gamerStateMap.put(player, gamerState);

        switch (gamerState) {
            case GAMER -> player.setGameMode(gameMode);
            case SPECTATOR -> player.setGameMode(GameMode.SPECTATOR);
        }
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

    public void nextState() {
        Bukkit.broadcast(Component.text("ok!"));
    }
}
