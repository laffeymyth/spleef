package net.laffeymyth.spleef;

import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.board.BoardServiceImpl;
import net.laffeymyth.spleef.api.localization.LocalizationFactory;
import net.laffeymyth.spleef.api.waiting.WaitingState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;

public final class Spleef extends JavaPlugin {
    private WaitingState waitingState;
    private BoardServiceImpl boardService;

    @Override
    public void onEnable() {
        boardService = new BoardServiceImpl(this);
        boardService.enable();

        ComponentLocalizationService lang;

        try {
            lang = new LocalizationFactory(this).create();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(lang, 4, 10);

        waitingState = new WaitingState(this, game, boardService, lang, new Location(
                Bukkit.getWorld("world"), 94.0f, 86.0f, -275.0f, 0.0f, 0.0f
        ));
        waitingState.start();
    }

    @Override
    public void onDisable() {
        waitingState.end();
        boardService.disable();
    }
}
