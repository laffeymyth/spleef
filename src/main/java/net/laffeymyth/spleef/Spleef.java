package net.laffeymyth.spleef;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.board.BoardServiceImpl;
import net.laffeymyth.spleef.api.end.EndState;
import net.laffeymyth.spleef.command.TestSchematicCommand;
import net.laffeymyth.spleef.localization.LocalizationFactory;
import net.laffeymyth.spleef.api.waiting.WaitingState;
import net.laffeymyth.spleef.game.SpleefState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;

public final class Spleef extends JavaPlugin {
    private BoardServiceImpl boardService;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        liteCommands = LiteBukkitFactory.builder("my-plugin", this)
                .commands(
                        new TestSchematicCommand(this)
                )
                .build();

        boardService = new BoardServiceImpl(this);
        boardService.enable();

        ComponentLocalizationService lang;

        try {
            lang = new LocalizationFactory(this).create();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(lang, 1, 10, new Location(
                Bukkit.getWorld("world"), 9.5f, 40.0f, 9.5f, 0f, 0f
        ));

        game.getStateQueue().add(new WaitingState(this, game, boardService, lang, new Location(
                Bukkit.getWorld("world"), 9.5f, 30.0f, 9.5f, 0f, 0f
        )));

        game.getStateQueue().add(new SpleefState(game, new Location(
                Bukkit.getWorld("world"), 9.5f, 30.0f, 9.5f, 0f, 0f
        ), this, 0, boardService));

        game.getStateQueue().add(new EndState(game));

        game.nextState();


    }

    @Override
    public void onDisable() {
        boardService.disable();
        liteCommands.unregister();
    }
}
