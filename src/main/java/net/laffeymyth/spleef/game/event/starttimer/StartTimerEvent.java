package net.laffeymyth.spleef.game.event.starttimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.localization.commons.util.ComponentResolver;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.game.event.GameEvent;

public class StartTimerEvent implements GameEvent {
    private final Game game;
    private final ComponentLocalizationService lang;

    public StartTimerEvent(Game game, ComponentLocalizationService lang) {
        this.game = game;
        this.lang = lang;
    }

    @Override
    public void tick(int eventTime, int leftTime) {
        game.getGamers().forEach(player -> player.sendActionBar(
                lang.getMessage("game_event_start_action_bar", "ru", ComponentResolver.tag("time", (argumentQueue, context) ->
                        Component.text(leftTime).append(Component.space())
                                .append(lang.getWord("TIME_SECOND_1", leftTime, "ru"))))
        ));
    }

    @Override
    public int timeToNextEvent() {
        return 10;
    }

    @Override
    public Component getName(String language, TagResolver... tagResolvers) {
        return lang.getMessage("game_event_board_start_timer", language, tagResolvers);
    }
}
