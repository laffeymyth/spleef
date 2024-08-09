package net.laffeymyth.spleef.api.waiting;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.util.ComponentResolver;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.board.Board;
import net.laffeymyth.spleef.api.board.BoardUpdater;

import java.util.Map;

@Slf4j
class WaitingBoardUpdater implements BoardUpdater {
    private final TagResolver onlinePlayers;
    private final TagResolver maxPlayers;
    private final TagResolver map;
    private final TagResolver server;
    private final TagResolver time;
    private final TagResolver dots;
    private final ComponentLocalizationService lang;
    private final WaitingTimer waitingTimer;

    WaitingBoardUpdater(ComponentLocalizationService lang, Game game, WaitingTimer waitingTimer, DotsTask dotsTask) {
        this.lang = lang;
        this.onlinePlayers = ComponentResolver.tag("online_player", (argumentQueue, context) -> Component.text(game.getGamers().size()));
        this.maxPlayers = ComponentResolver.tag("max_player", (argumentQueue, context) -> Component.text(game.getMaxPlayers()));
        this.map = ComponentResolver.tag("map", (argumentQueue, context) -> game.getServerMap("ru"));
        this.server = ComponentResolver.tag("server", (argumentQueue, context) -> game.getServerName("ru"));
        this.time = ComponentResolver.tag("time", (argumentQueue, context) -> Component.text(waitingTimer.getTime()));
        this.dots = ComponentResolver.tag("board_dots", (argumentQueue, context) -> Component.text(".".repeat(dotsTask.getDotCount())));
        this.waitingTimer = waitingTimer;
    }

    @Override
    public void onUpdate(Board board) {
        Map<Integer, Component> componentMap = board.getLineMap();

        board.setTitle(lang.getMessage("waiting_board_title", "ru"));

        componentMap.put(9, null);
        componentMap.put(8, lang.getMessage("waiting_board_players", "ru", onlinePlayers, maxPlayers));
        componentMap.put(7, null);
        if (!waitingTimer.isStated()) {
            componentMap.put(6, lang.getMessage("waiting_board_timer_on_wait", "ru", dots));
        } else {
            componentMap.put(6, lang.getMessage("waiting_board_timer_on_start", "ru", time));
        }
        componentMap.put(5, null);
        componentMap.put(4, lang.getMessage("waiting_board_map", "ru", map));
        componentMap.put(3, lang.getMessage("waiting_board_server", "ru", server));
        componentMap.put(2, null);
        componentMap.put(1, lang.getMessage("waiting_board_site", "ru"));
    }
}
