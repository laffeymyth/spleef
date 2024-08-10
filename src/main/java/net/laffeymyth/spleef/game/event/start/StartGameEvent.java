package net.laffeymyth.spleef.game.event.start;

import net.kyori.adventure.title.TitlePart;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.game.event.GameEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class StartGameEvent implements GameEvent {
    private final Game game;
    private final Plugin plugin;
    private final Listener cancelBlockListener;
    private StartGameListener startGameListener;

    public StartGameEvent(Game game, Plugin plugin, Listener cancelBlockListener) {
        this.game = game;
        this.plugin = plugin;
        this.cancelBlockListener = cancelBlockListener;
    }

    public void start() {
        HandlerList.unregisterAll(cancelBlockListener);

        game.getGamers().forEach(player -> {

            player.sendTitlePart(TitlePart.TITLE, game.getLang().getMessage(
                    "game_event_start_title",
                    "ru"
            ));
        });

        startGameListener = new StartGameListener();
        Bukkit.getPluginManager().registerEvents(startGameListener, plugin);
    }

    public void endGame() {
        HandlerList.unregisterAll(startGameListener);
    }

    @Override
    public int time() {
        return 3 * 60;
    }
}
