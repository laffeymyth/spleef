package net.laffeymyth.spleef.api.waiting;

import net.kyori.adventure.text.Component;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.util.ComponentResolver;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.GamerState;
import net.laffeymyth.spleef.api.board.Board;
import net.laffeymyth.spleef.api.board.BoardService;
import net.laffeymyth.spleef.api.util.MinPlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class GamerListener implements Listener {
    private final Game game;
    private final ComponentLocalizationService lang;
    private final WaitingTimer waitingTimer;
    private final Board waitingBoard;
    private final WaitingState waitingState;
    private final DotsTask dotsTask;

    public GamerListener(Game game, WaitingTimer waitingTimer, BoardService boardService, WaitingState waitingState, DotsTask dotsTask) {
        this.game = game;
        this.lang = game.getLang();
        this.waitingTimer = waitingTimer;
        this.waitingState = waitingState;
        this.dotsTask = dotsTask;
        this.waitingBoard = boardService.createBoard(new WaitingBoardUpdater(lang, game, waitingTimer, dotsTask), 0L, 1L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        game.normalize(player);
        game.putPlayer(player, GamerState.GAMER);

        event.joinMessage(
                lang.getMessage(
                        "waiting_player_join",
                        "ru", ComponentResolver.tag("prefix", game.getPrefix("ru")),
                        ComponentResolver.tag("player", player.displayName()),
                        ComponentResolver.tag("online_players", Component.text(game.getGamers().size())),
                        ComponentResolver.tag("max_players", Component.text(game.getMaxPlayers()))
                )
        );

        waitingBoard.send(player);

        player.teleport(waitingState.getSpawn());

        if (!MinPlayerUtil.isTwoThirds(game.getGamers().size(), game.getMaxPlayers())) {
            return;
        }

        if (waitingTimer.isStated()) {
            return;
        }

        waitingTimer.start();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        game.removePlayer(player);

        event.quitMessage(
                lang.getMessage(
                        "waiting_player_quit",
                        "ru", ComponentResolver.tag("prefix", game.getPrefix("ru")),
                        ComponentResolver.tag("player", player.displayName()),
                        ComponentResolver.tag("online_players", Component.text(game.getGamers().size())),
                        ComponentResolver.tag("max_players", Component.text(game.getMaxPlayers()))
                )
        );

        if (MinPlayerUtil.isTwoThirds(game.getGamers().size(), game.getMaxPlayers())) {
            return;
        }

        if (!waitingTimer.isStated()) {
            return;
        }

        waitingTimer.cancel();
    }
}
