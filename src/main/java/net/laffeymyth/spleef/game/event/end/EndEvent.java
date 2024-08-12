package net.laffeymyth.spleef.game.event.end;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laffeymyth.localization.commons.service.ComponentLocalizationService;
import net.laffeymyth.spleef.api.Game;
import net.laffeymyth.spleef.game.event.GameEvent;

public class EndEvent implements GameEvent {
    private final ComponentLocalizationService lang;

    public EndEvent(ComponentLocalizationService lang, Game game) {
        this.lang = lang;
    }

    @Override
    public Component getName(String language, TagResolver... tagResolvers) {
        return lang.getMessage("game_event_board_end_game", language, tagResolvers);
    }
}
