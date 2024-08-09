package net.laffeymyth.spleef.api.waiting;

import lombok.Getter;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.State;
import net.laffeymyth.spleef.api.board.BoardService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@Getter
public class WaitingState implements State {
    private final WaitingTimer waitingTimer;
    private final Plugin plugin;
    private final Game game;
    private final BoardService boardService;
    private final ComponentLocalizationService lang;
    private final Location spawn;
    private DotsTask dotsTask;
    private Listener gamerListener;
    private Listener guardListener;

    public WaitingState(Plugin plugin, Game game, BoardService boardService, ComponentLocalizationService lang, Location spawn) {
        this.plugin = plugin;
        this.game = game;
        this.boardService = boardService;
        this.lang = lang;
        this.spawn = spawn;
        this.waitingTimer = new WaitingTimer(game, plugin, lang);
    }

    @Override
    public void start() {
        dotsTask = new DotsTask(plugin);
        dotsTask.start();

        gamerListener = new GamerListener(game, waitingTimer, boardService, this, dotsTask);
        guardListener = new LobbyGuardListener();
        Bukkit.getPluginManager().registerEvents(gamerListener, plugin);
        Bukkit.getPluginManager().registerEvents(guardListener, plugin);
    }

    @Override
    public void end() {
        dotsTask.cancel();
        HandlerList.unregisterAll(gamerListener);
        HandlerList.unregisterAll(guardListener);
    }
}
