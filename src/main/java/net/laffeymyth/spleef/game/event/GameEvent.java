package net.laffeymyth.spleef.game.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface GameEvent {
    default void start() {
    }

    default void tick(int eventTime, int leftTime) {
    }

    default void endGame() {
    }

    default void end() {
    }

    default int timeToNextEvent() {
        return 0;
    }

    Component getName(String language, TagResolver... tagResolvers);
}
