package net.laffeymyth.spleef.game;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.util.ComponentResolver;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.api.board.Board;
import net.laffeymyth.spleef.api.board.BoardUpdater;
import net.laffeymyth.spleef.api.util.StringUtil;

import java.util.Map;

@Slf4j
public class SpleefBoardUpdater implements BoardUpdater {
    private final TagResolver onlinePlayers;
    private final Game game;
    private final ComponentLocalizationService lang;
    private final TagResolver map;
    private final TagResolver time;
    private final TagResolver server;

    public SpleefBoardUpdater(Game game, ComponentLocalizationService lang, SpleefTimer spleefTimer) {
        this.onlinePlayers = ComponentResolver.tag("online_player", (argumentQueue, context) -> Component.text(game.getGamers().size()));
        this.map = ComponentResolver.tag("map", (argumentQueue, context) -> game.getServerMap("ru"));
        this.server = ComponentResolver.tag("server", (argumentQueue, context) -> game.getServerName("ru"));
        this.time = ComponentResolver.tag("time", (argumentQueue, context) -> {
            if (spleefTimer.getNextEvent() == null) {
                return lang.getMessage("game_end_game_over_board", "ru");
            }

            return spleefTimer.getNextEvent().getName("ru", ComponentResolver.tag("time", Component.text(StringUtil.convertSeconds(spleefTimer.getLeftTime()))));
        });
        this.game = game;
        this.lang = lang;
    }

    @Override
    public void onUpdate(Board board) {
        Map<Integer, Component> componentMap = board.getLineMap();

        board.setTitle(game.getBoardTitle("ru"));
        componentMap.put(10, null);
        componentMap.put(9, lang.getMessage("game_board_next_event", "ru"));
        componentMap.put(8, lang.getMessage("game_board_next_event_time", "ru", time));
        componentMap.put(7, null);
        componentMap.put(6, lang.getMessage("game_board_players", "ru", onlinePlayers));
        componentMap.put(5, null);
        componentMap.put(4, lang.getMessage("game_board_map", "ru", map));
        componentMap.put(3, lang.getMessage("game_board_server", "ru", server));
        componentMap.put(2, null);
        componentMap.put(1, lang.getMessage("credentials_board_site", "ru"));
    }
}
