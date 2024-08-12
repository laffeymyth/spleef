package net.laffeymyth.spleef.game.event.start;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.spleef.api.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class SpleefGameEvent implements net.laffeymyth.spleef.game.event.GameEvent {
    private final Game game;
    private final Plugin plugin;
    private final Listener cancelBlockListener;
    private SpleefGameListener spleefGameListener;
    private final ComponentLocalizationService lang;

    public SpleefGameEvent(Game game, Plugin plugin, Listener cancelBlockListener, ComponentLocalizationService lang) {
        this.game = game;
        this.plugin = plugin;
        this.cancelBlockListener = cancelBlockListener;
        this.lang = lang;
    }

    public void start() {
        HandlerList.unregisterAll(cancelBlockListener);

        game.getGamers().forEach(player -> player.sendTitlePart(TitlePart.TITLE, game.getLang().getMessage(
                "game_event_start_title",
                "ru"
        )));

        spleefGameListener = new SpleefGameListener();
        Bukkit.getPluginManager().registerEvents(spleefGameListener, plugin);
    }

    public void endGame() {
        HandlerList.unregisterAll(spleefGameListener);
    }

    @Override
    public int timeToNextEvent() {
        return 60;
    }

    @Override
    public Component getName(String language, TagResolver... tagResolvers) {
        return lang.getMessage("game_event_board_start_game", language, tagResolvers);
    }
}
